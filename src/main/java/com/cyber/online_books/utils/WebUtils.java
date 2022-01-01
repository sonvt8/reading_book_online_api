package com.cyber.online_books.utils;

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
}
