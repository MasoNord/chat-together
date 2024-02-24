package org.masonord.security;

import org.masonord.dto.UserDto;
import org.masonord.entity.User;
import org.masonord.mapper.UserMapper;
import org.masonord.repository.UserRepository;

import javax.sound.midi.Soundbank;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Objects;

public class HandleAuthentication {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public HandleAuthentication(PasswordEncoder passwordEncoder,
                                UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto doAuthentication(BufferedReader in, PrintWriter out) {
        UserDto userDto = new UserDto();

        try {
            out.println("Enter your username: ");
            String username = in.readLine();
            out.println("Enter your password");
            byte[] password = in.readLine().getBytes();

            User user = userRepository.getByName(username);

            if (Objects.isNull(user)) {
                out.println("SERVER: There is not user with such a name, try again");
                return null;
            }

            if (!passwordEncoder.matchPasswords(password, user.getPassword().getBytes())) {
                out.println("SERVER: Password does not match, try again");
                return null;
            }

            userDto = UserMapper.toUserDto(user);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
        out.println("SERVER: WELCOME to the chat together !!!");
        return userDto;
    }
}
