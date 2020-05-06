package ua.nure.movenko.summaryTask4.hash;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This util class hashes passwords
 */
public final class Password {

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * Hashes given {@code str} and returns the result as new String object
     *
     * @param str is {@code String} object to be hashed
     * @return hashed {@code String} object
     */
    public static String hash(String str) {
        MessageDigest digest;
        StringBuilder hexString = new StringBuilder();
        try {
            digest = MessageDigest.getInstance("SHA-512");
            digest.update(str.getBytes("UTF-8"));
            for (byte d : digest.digest()) {
                hexString.append(getFirstHexDigit(d)).append(getSecondHexDigit(d));
            }
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return hexString.toString();
    }

    private static char getFirstHexDigit(byte x) {
        return HEX_DIGITS[(0xFF & x) / 16];
    }

    private static char getSecondHexDigit(byte x) {
        return HEX_DIGITS[(0xFF & x) % 16];
    }

}
