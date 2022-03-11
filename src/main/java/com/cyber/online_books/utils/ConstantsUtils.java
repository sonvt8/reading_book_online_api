package com.cyber.online_books.utils;

import static java.lang.Long.valueOf;

public class ConstantsUtils {
    //Chuỗi random
    public static final String CHAR_LIST = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    //Độ dài chuỗi random
    public static final int RANDOM_STRING_LENGTH = 6;

    //Giá cập nhật Ngoại Hiệu
    public static final Double PRICE_UPDATE_NICK = Double.valueOf(2000);
    public static final Integer ROLE_ADMIN = 1;
    public static final Integer ROLE_SMOD = 2;
    public static final Integer ROLE_CONVERTER = 3;
    public static final Integer ROLE_USER = 4;

    // Page Default
    public static Integer PAGE_DEFAULT = 1;

    // Page size default
    public static Integer PAGE_SIZE_DEFAULT = 4;

    //Số Story Trong Home Page
    public static Integer PAGE_SIZE_HOME = 10;

    //Page size list comment default
    public static Integer PAGE_SIZE_COMMENT_DEFAULT = 10;

    // Page size Top View
    public static Integer RANK_SIZE = 10;

    //Page size Chapter list default
    public static Integer PAGE_SIZE_CHAPTER_OF_STORY = 5;

    //Page size Top View
    public static Integer PAGE_SIZE_TOP_VIEW_DEFAULT = 10;

    //Thời Gian Một Ngày Theo Giờ
    public static Long TIME_DAY = valueOf(24);

    //Thời gian nửa tiếng theo phút
    public static long HALF_HOUR = valueOf(30);

    //Link avartar default
    public static final String AVATAR_DEFAULT = "https://randomuser.me/api/portraits/men/97.jpg";

    //Link pay MoMo default
    public static final String LINK_PAY_MOMO = "https://res.cloudinary.com/thang1988/image/upload/v1543465570/truyenmvc/momo.png";

    //Link pay VietTel default
    public static final String LINK_PAY_VIETTEL = "https://res.cloudinary.com/thang1988/image/upload/v1543465569/truyenmvc/viettelpay.png";
}
