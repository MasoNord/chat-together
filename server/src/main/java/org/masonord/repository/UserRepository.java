package org.masonord.repository;

import org.masonord.entity.User;
import org.masonord.mapper.UserMapper;

import javax.sql.DataSource;
import java.sql.*;

public class UserRepository {

    private final DataSource dataSource;

    public UserRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public User findByName(String name) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement selectStatement = connection.prepareStatement("SELECT * FROM user WHERE name = ?");
            selectStatement.setString(1, name);
            ResultSet rs = selectStatement.executeQuery();

            return UserMapper.toUser(rs);
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
