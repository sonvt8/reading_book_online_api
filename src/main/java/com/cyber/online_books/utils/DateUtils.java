package com.cyber.online_books.utils;

import java.time.*;
import java.util.Date;

public class DateUtils {
    //Lấy ngày đầu tiên của tuần hiện tại

    public static Date getFirstDayOfWeek() {
        LocalDate monday = LocalDate.now();
        while (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
            monday = monday.minusDays(1);
        }
        return java.sql.Timestamp.valueOf(monday.atStartOfDay());
    }

    //Lấy ngày cuối của tuần hiện tại
    public static Date getLastDayOfWeek() {
        LocalDate sunday = LocalDate.now();
        while (sunday.getDayOfWeek() != DayOfWeek.SUNDAY) {
            sunday = sunday.plusDays(1);
        }
        return java.sql.Timestamp.valueOf(sunday.atStartOfDay());
    }

    //Lấy ngày đầu tiên của tháng hiện tại
    public static Date getFirstDayOfMonth() {
        LocalDate today = LocalDate.now();
        LocalDate firstday = today.withDayOfMonth(1);
        return java.sql.Timestamp.valueOf(firstday.atStartOfDay());
    }

    //Lấy ngày cuối cùng của tháng hiện tại
    public static Date getLastDayOfMonth() {
        LocalDate today = LocalDate.now();
        LocalDate lastday = today.withDayOfMonth(today.lengthOfMonth());
        return java.sql.Timestamp.valueOf(lastday.atStartOfDay());
    }

    //Lấy thời gian hiện tại
    public static Date getCurrentDate() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return java.sql.Timestamp.valueOf(localDateTime);
    }

    public static String betweenTwoDays(Date createDate) {
        LocalDateTime startDate = createDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDate startLocalDate = createDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDateTime endDate = LocalDateTime.now();
        LocalDate endLocalDate = LocalDate.now();
        long differentInSeconds = Duration.between(startDate, endDate).getSeconds();
        long differentInMinutes = Duration.between(startDate, endDate).toMinutes();
        long differentInHours = Duration.between(startDate, endDate).toHours();
        long differentInDays = Period.between(startLocalDate, endLocalDate).getDays();
        long differentInMonths = Period.between(startLocalDate, endLocalDate).getMonths();
        long differentInYears = Period.between(startLocalDate, endLocalDate).getYears();
        if (differentInYears > 0)
            return differentInYears + " năm trước";
        if (differentInMonths > 0)
            return differentInMonths + " tháng trước";
        if (differentInDays > 0)
            return differentInDays + " ngày trước";
        if (differentInHours > 0)
            return differentInHours + " giờ trước";
        if (differentInMinutes > 0)
            return differentInMinutes + " phút trước";
        return differentInSeconds + " giây trước";
    }


    public static String betweenHours(Date createDate) {
        LocalDateTime startDate = createDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime endDate = getCurrentDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        long differentInSeconds = Duration.between(startDate, endDate).getSeconds();
        long differentInMinutes = Duration.between(startDate, endDate).toMinutes();
        if (differentInMinutes > 0)
            return differentInMinutes + " phút";
        return differentInSeconds + " giây";
    }

    public static Date getHoursAgo(Date now, long hours) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(now.toInstant(), ZoneId.systemDefault());
        LocalDateTime result = localDateTime.minusHours(hours);
        return java.sql.Timestamp.valueOf(result);
    }

    public static Date getMinutesAgo(Date now, long minutes) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(now.toInstant(), ZoneId.systemDefault());
        LocalDateTime result = localDateTime.minusMinutes(minutes);
        return java.sql.Timestamp.valueOf(result);
    }

    public static Date getDateDeal(Integer count) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(getCurrentDate().toInstant(), ZoneId.systemDefault());
        LocalDateTime result = localDateTime.plusDays(count);
        return java.sql.Timestamp.valueOf(result);
    }
}
