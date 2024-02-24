package org.masonord.repository;

import org.masonord.entity.ChatRoom;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChatRepository {

    private final DataSource dataSource;

    public ChatRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<String> getAll() {
        List<String> rooms = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement getStatement = connection.prepareStatement("SELECT DISTINCT name FROM Chats");
            ResultSet rs = getStatement.executeQuery();
            while (rs.next()) {
                rooms.add(rs.getString("name"));
            }
        }catch(SQLException e) {
            // TODO: logging
        }
        return rooms;
    }

    public int createRoom(String name) {
        int result = 0;

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement createStatement = connection.prepareStatement("INSERT INTO Chats(name) VALUES (?)");
            createStatement.setString(1, name);
            result = createStatement.executeUpdate();
        }catch (SQLException e) {
            // TODO: logging
        }

        return result;
    }

    public ChatRoom findByName(String chatName) {
        ChatRoom chatRoom = null;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement getStatement = connection.prepareStatement("SELECT * FROM Chats WHERE name = ?");
            getStatement.setString(1, chatName);
            ResultSet rs = getStatement.executeQuery();

            while (rs.next()) {
                chatRoom = new ChatRoom();
                chatRoom.setName(rs.getString("name"));
            }
        }catch (SQLException e) {
            // TODO: logging
        }
        return chatRoom;
    }

    public int addNewUser(String chatName, int id) {
        int response = 0;

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement updateStatement = connection.prepareStatement("UPDATE Chats SET user_id = ? WHERE name = ?");
            updateStatement.setString(1, String.valueOf(id));
            updateStatement.setString(2, chatName);
            response = updateStatement.executeUpdate();
        }catch (SQLException e) {
            // TODO: logging
        }

        return response;
    }
}
