package ua.nure.movenko.summaryTask4.util;

import java.util.Random;

/**
 * Util class for random password generation
 *
 * @author M.Movenko
 */
public final class PasswordGenerator {
    private static final String[] charCategories = new String[]{
            "abcdefghijklmnopqrstuvwxyz",
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
            "0123456789"
    };

    public static String generate(int length) {
        StringBuilder password = new StringBuilder(length);
        Random random = new Random(System.nanoTime());

        for (int i = 0; i < length; i++) {
            String charCategory = charCategories[random.nextInt(charCategories.length)];
            int position = random.nextInt(charCategory.length());
            password.append(charCategory.charAt(position));
        }
        return password.toString();
    }
}
