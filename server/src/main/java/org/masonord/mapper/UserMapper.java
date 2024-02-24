package org.masonord.mapper;

import org.masonord.dto.UserDto;
import org.masonord.entity.User;
import java.sql.SQLException;

public class UserMapper {
    public static UserDto toUserDto(User user) throws SQLException {
        UserDto userDto = new UserDto();
        userDto.setName(user.getUsername());
        userDto.setId(userDto.getId());
        return userDto;
    }
}
