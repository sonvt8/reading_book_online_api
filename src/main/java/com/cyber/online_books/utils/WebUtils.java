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
}
