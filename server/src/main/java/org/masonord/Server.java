package org.masonord;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.masonord.config.DatabaseConfig;
import org.masonord.dto.UserDto;
import org.masonord.entity.User;
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
                        new HandleRegistration(new PasswordEncoder(),new UserRepository(dataSource)),
                        new HandleAuthentication(new PasswordEncoder(), new UserRepository(dataSource))
                );

                connections.add(handler);
                pool.execute(handler);
            }
        } catch (Exception e) {
            shutdown();
        }
    }

    public void broadcast(String message) {
        for (ConnectionHandler ch : connections) {
            if (ch != null) {
                ch.sendMessage(message);
            }
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

    class ConnectionHandler implements Runnable {

        private final HandleRegistration registration;
        private final HandleAuthentication authentication;
        private final Socket client;
        private BufferedReader in;
        private PrintWriter out;

        public ConnectionHandler(Socket client,
                                 HandleRegistration registration,
                                 HandleAuthentication authentication) {
            this.client = client;
            this.registration = registration;
            this.authentication = authentication;

        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(client.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                UserDto user = null;

                while(true) {
                    String nickname = "Some ordinary guy";
                    String message = "Hello";

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
                                out.println("Wrong choice supplied, try again");
                                break;
                        }
                    }

                    while ((message = in.readLine()) != null && !Objects.isNull(user)) {
                        out.println("> ");

                        if (message.startsWith("/nick")) {
                            String[] messageSplit = message.split(" ", 2);
                            if (messageSplit.length == 2) {
                                broadcast(nickname + " renamed themselves to " + messageSplit[1]);
                                System.out.println(nickname + " renamed themselves to " + messageSplit[1]);
                                nickname = messageSplit[1];
                                out.println("Successfully changed nickname to: " + nickname);
                            } else {
                                out.println("No nickname provided");
                            }
                        } else if (message.startsWith("/quit")) {
                            broadcast(nickname + " left the chat!");
                            shutdown();
                        } else {
                            broadcast(nickname + ": " + message);
                        }
                    }
                }

            } catch (IOException e) {
                shutdown();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        public void sendMessage(String message) {
            out.println(message);
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
