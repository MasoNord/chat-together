package org.masonord.security;

import com.password4j.Hash;
import com.password4j.Password;

import java.util.Arrays;

public class PasswordEncoder {
    public String encodePassword(byte[] password) {
        Hash hash = Password.hash(password)
                .addSalt("12") // read salt from property file
                .addPepper("shared_secret") // read paper from property file
                .withArgon2();
        return hash.getResult();
    }

    public boolean matchPasswords(byte[] password, byte[] hash) {
        return Password.check(password, hash).addSalt("12").addPepper("shared_secret").withArgon2();
    }

    public boolean matchPasswords(String password, String hash) {
        return Password.check(password, hash).addSalt("12").addPepper("shared_secret").withArgon2();
    }

    public boolean passwordEquals(byte[] password1, byte[] password2) {
        return Arrays.equals(password1, password2);
    }
}
