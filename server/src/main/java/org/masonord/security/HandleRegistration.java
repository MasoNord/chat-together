package org.masonord.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.masonord.dto.UserDto;
import org.masonord.entity.User;
import org.masonord.mapper.UserMapper;
import org.masonord.repository.UserRepository;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Objects;

public class HandleRegistration {
    private static final Logger LOGGER = LogManager.getLogger(HandleRegistration.class);
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public HandleRegistration(PasswordEncoder passwordEncoder,
                              UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public UserDto doRegistration(BufferedReader in, PrintWriter out) throws SQLException {
        User user = new User();

        try {
            out.println("Enter your username: ");
            String username = in.readLine();
            out.println("Enter your password: ");
            byte[] password = in.readLine().getBytes();
            out.println("Enter password again: ");
            byte[] repeatPassword = in.readLine().getBytes();

            if (!Objects.isNull(userRepository.getByName(username))) {
                out.println("SERVER: User already exist with such name, try again");
                return null;
            }

            if (!passwordEncoder.passwordEquals(password, repeatPassword)) {
                out.println("SERVER: Passwords does not match, try again");
                return null;
            }

            user.setPassword(passwordEncoder.encodePassword(password));
            user.setUsername(username);
        }catch (IOException e) {
            // TODO: make proper logging
        }
        out.println("SERVER: WELCOME to the chat together !!!");
        return UserMapper.toUserDto(userRepository.createNewRecord(user));
    }

}
