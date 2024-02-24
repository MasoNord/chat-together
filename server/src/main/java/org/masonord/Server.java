package org.masonord;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.masonord.config.DatabaseConfig;
import org.masonord.dto.UserDto;
import org.masonord.entity.ChatRoom;
import org.masonord.repository.ChatRepository;
import org.masonord.repository.UserRepository;
import org.masonord.security.HandleAuthentication;
import org.masonord.security.HandleRegistration;
import org.masonord.security.PasswordEncoder;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger(Server.class);
    private final ArrayList<ConnectionHandler> connections;
    private ServerSocket server;
    private boolean done;
    private ExecutorService pool;

    public Server() {
        done = false;
        LOGGER.info("Starting server on port " + 9999 + " ...");
        connections = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(9999);
            pool = Executors.newCachedThreadPool();
            DataSource dataSource = DatabaseConfig.createDataSource();
            while (!done) {
                Socket client = server.accept();

                ConnectionHandler handler = new ConnectionHandler(
                        client,
                        new HandleRegistration(new PasswordEncoder(), new UserRepository(dataSource)),
                        new HandleAuthentication(new PasswordEncoder(), new UserRepository(dataSource)),
                        new ChatRepository(dataSource)
                );

                pool.execute(handler);
            }
        } catch (Exception e) {
            shutdown();
        }
    }
    public void shutdown() {
        try {
            done = true;
            pool.shutdown();
            if (!server.isClosed()) {
                server.close();
            }
            for (ConnectionHandler ch : connections) {
                ch.shutdown();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static class ConnectionHandler implements Runnable {

        private final HandleRegistration registration;
        private final HandleAuthentication authentication;
        private final ChatRepository chatRepository;
        private final Socket client;
        private BufferedReader in;
        private PrintWriter out;

        public ConnectionHandler(Socket client,
                                 HandleRegistration registration,
                                 HandleAuthentication authentication,
                                 ChatRepository chatRepository) {
            this.client = client;
            this.registration = registration;
            this.authentication = authentication;
            this.chatRepository = chatRepository;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(client.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                UserDto user = null;

                while(true) {
                    out.println("1. Login\n2. SigUp\n3. Exit");
                    String choice = in.readLine();

                    if (Objects.isNull(user)) {
                        switch (choice.toLowerCase()) {
                            case "1": case "login":
                                user = authentication.doAuthentication(in, out);
                                break;
                            case "2": case "sigup":
                                user = registration.doRegistration(in, out);
                                break;
                            case "3": case "exit":
                            default:
                                out.println("SERVER: Wrong choice supplied, try again");
                                break;
                        }
                    }
                    String message;
                    while ((message = in.readLine()) != null && !Objects.isNull(user)) {
                        if (message.startsWith("/nick")) {
                            out.println(user.getName());
                        }else if (message.startsWith("/chats")) {
                            List<String> chats = chatRepository.getAll();
                            for (String s : chats) {
                                out.println(s);
                            }
                        }else if (message.startsWith("/join_chatroom")) {
                            String[] messageSplit = message.split(" ", 2);
                            if (messageSplit.length == 2) {
                                ChatRoom room = chatRepository.findByName(messageSplit[1]);
                                if (Objects.isNull(room)) {
                                    out.println("No room found, try again");
                                }else {
                                    int response = chatRepository.addNewUser(room.getName(), user.getId());
                                    if (response != 0) {
                                        out.println("SERVER: welcome to the " + room.getName() + " happy chatting :)");

                                    }else {
                                        out.println("Something went wrong, try again");
                                    }
                                }
                            }else {
                                out.println("No name provided");
                            }
                        }else if (message.startsWith("/create_chatroom")) {
                            String[] messageSplit = message.split(" ", 2);
                            if (messageSplit.length == 2) {
                                int result = chatRepository.createRoom(messageSplit[1]);
                                if (result != 0) {
                                    out.println("Room has been successfully created");
                                }else {
                                    out.println("Something went wrong, try again");
                                }
                            }else {
                                out.println("No name provided");
                            }
                        }else if (message.startsWith("/quit_chatroom")) {

                        }else if (message.startsWith("/quit")) {
//                            broadcast(nickname + " left the chat!");
//                            shutdown();
                        }
                    }
                }
            } catch (IOException e) {
                shutdown();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


        public void shutdown() {
            try{
                in.close();
                out.close();
                if(!client.isClosed()){
                    client.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
