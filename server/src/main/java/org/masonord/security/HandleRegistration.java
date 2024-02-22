package org.masonord.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.masonord.repository.UserRepository;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class HandleRegistration {
    private static final Logger LOGGER = LogManager.getLogger(HandleRegistration.class);
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public HandleRegistration(PasswordEncoder passwordEncoder,
                              UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public boolean doRegistration(BufferedReader in, PrintWriter out) {
        try {
            out.println("Enter your username: ");
            String username = in.readLine();
            out.println("Enter your password: ");
            byte[] password = in.readLine().getBytes();
            out.println("Enter password again: ");
            byte[] repeatPassword = in.readLine().getBytes();

            if (!passwordEncoder.matchPasswords(password, repeatPassword)) {
                out.println("Passwords does not match");
                return false;
            }

            // TODO:

        }catch (IOException e) {
            // TODO: make proper logging
        }

        return true;
    }

}
