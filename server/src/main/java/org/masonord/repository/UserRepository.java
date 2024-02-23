package org.masonord.repository;

import org.masonord.dto.UserDto;
import org.masonord.entity.User;
import javax.sql.DataSource;
import java.sql.*;
import java.util.Objects;

public class UserRepository {

    private final DataSource dataSource;

    public UserRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public User getByName(String name) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement selectStatement = connection.prepareStatement("SELECT * FROM Users WHERE name = ?");
            selectStatement.setString(1, name);
            ResultSet rs = selectStatement.executeQuery();
            User user = null;

            while (rs.next()) {
                user = new User();
                user.setUsername(rs.getString("name"));
                user.setPassword(rs.getString("password"));
            }

            if (Objects.isNull(user)) {
                return null;
            }

            rs.close();
            return user;
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User createNewRecord(User user) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement createStatement = connection.prepareStatement("INSERT INTO Users (name, password) values (?, ?)");
            createStatement.setString(1, user.getUsername());
            createStatement.setString(2, user.getPassword());
            int result = createStatement.executeUpdate();
            if (result == 0) {
                return null;
            }
            return getByName(user.getUsername());
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public UserDto updateName(String name) {
        return new UserDto();
    }
}
