package org.masonord.mapper;

import org.masonord.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper {

    public static User toUser(ResultSet rs) throws SQLException {
        User user = new User();
        while (rs.next()) {
            user.setName(rs.getString("name"));
        }
        return user;
    }
}
