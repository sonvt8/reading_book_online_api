package com.cyber.online_books.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class WebUtils {

    public static String convertStringToMetaTitle(String name) {
        try {
            name = name.replaceAll("[+.^:,^$|?*+()!]", "");
            String temp = Normalizer.normalize(name, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(temp).replaceAll("").toLowerCase().replaceAll(" ", "-").replaceAll("đ", "d");
        } catch (Exception e) {
            return " ";
        }
    }

    public static boolean equalsPassword(String rawPassword, String encodedPassword) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * Kiểm tra string có phải Long number
     *
     * @param number
     * @return true - nếu không phải / false - nếu đúng
     */
    public static boolean checkLongNumber(String number) {
        try {
            Long.parseLong(number);
        } catch (Exception e) {
            return true;
        }
        return false;
    }

    /**
     * Kiểm tra string có phải Integer number
     *
     * @param number
     * @return true - nếu sai / false - nếu đúng
     */
    public static boolean checkIntNumber(String number) {
        try {
            Integer.parseInt(number);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static Integer countWords(String word) {
        if (word == null || word.isEmpty()) {
            return 0;
        }
        int wordCount = 0;
        boolean isWord = false;
        int endOfLine = word.length() - 1;
        char[] characters = word.toCharArray();
        for (int i = 0; i < characters.length; i++) {
            // if the char is a letter, word = true.
            if (Character.isLetter(characters[i]) && i != endOfLine) {
                isWord = true;
                // if char isn't a letter and there have been letters before,
                // counter goes up.
            } else if (!Character.isLetter(characters[i]) && isWord) {
                wordCount++;
                isWord = false;
                // last word of String; if it doesn't end with a non letter, it
                // wouldn't count without this.
            } else if (Character.isLetter(characters[i]) && i == endOfLine) {
                wordCount++;
            }
        }
        return wordCount;
    }
}
