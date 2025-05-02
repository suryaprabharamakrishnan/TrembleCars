package com.trimbleCars.security;

import java.security.SecureRandom;

public class CodeGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String DIGITS = "0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateCode(int length) {
        StringBuilder code = new StringBuilder(length);

        //Ensure at least one digit is present
        code.append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));

        // Fill the rest of the code with random characters
        for (int i = 1; i < length; i++) {
            code.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }

        // Shuffle the characters to ensure randomness
        for (int i = 0; i < code.length(); i++) {
            int randomIndex = RANDOM.nextInt(code.length());
            char temp = code.charAt(i);
            code.setCharAt(i, code.charAt(randomIndex));
            code.setCharAt(randomIndex, temp);
        }

        return code.toString();
    }

}
