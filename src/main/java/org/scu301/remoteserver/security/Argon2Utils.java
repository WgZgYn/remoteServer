package org.scu301.remoteserver.security;

import de.mkammerer.argon2.Argon2Advanced;
import de.mkammerer.argon2.Argon2Factory;
import org.apache.tomcat.util.buf.HexUtils;
import org.scu301.remoteserver.util.Pair;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public class Argon2Utils {
    private static final int SALT_LENGTH = 32;
    private static final int HASH_LENGTH = 32;
    private static final Argon2Advanced argon2 = Argon2Factory.createAdvanced(Argon2Factory.Argon2Types.ARGON2id, SALT_LENGTH, HASH_LENGTH);
    ;

    private Argon2Utils() {}


    // 生成随机盐
    private static byte[] genSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    public static Pair<String, byte[]> encrypt(char[] password) {
        byte[] salt = genSalt();
        return new Pair<>(passwordHash(password, salt), salt);
    }

    // 哈希密码
    private static String passwordHash(char[] password, byte[] salt) {
        return argon2.hash(2, 19456, 1, password, StandardCharsets.UTF_8, salt);
    }

    public static boolean verify(String password, char[] passwordHash) {
        return argon2.verify(password, passwordHash);
    }

    public static String hex(byte[] salt) {
        return HexUtils.toHexString(salt);
    }
}
