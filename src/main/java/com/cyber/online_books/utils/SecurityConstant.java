package com.cyber.online_books.utils;

public class SecurityConstant {
    public static final long EXPIRATION_TIME = 432_000_000; // 5 days expressed in milliseconds
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "Jwt-Token";
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
    public static final String COMPANY  = "Cyber Ltd";
    public static final String APPLICATION_NAME  = "Read Book Online";
    public static final String AUTHORITIES = "authorities";
    public static final String FORBIDDEN_MESSAGE = "Bạn cần đăng nhập để truy cập trang";
    public static final String ACCESS_DENIED_MESSAGE = "Bạn không đủ quyền truy cập đường dẫn này";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
    public static final String[] PUBLIC_URLS = {
            "/thanh-vien/dang-nhap",
            "/thanh-vien/dang-ky",
            "/thanh-vien/quen-mat-khau",
            "/member/image/**"
    };
    //public static final String[] PUBLIC_URLS = { "**" };
    public static final String[] ROLE_USER_LINK = {"/tai-khoan",
            "/tai-khoan/theo_doi",
            "/tai-khoan/doi_mat_khau",
            "/tai-khoan/nap_dau",
            "/tai-khoan/giao_dich",
            "/tai-khoan/quan_ly_truyen",
            "/tai-khoan/them_truyen",
            "/tai-khoan/list_chuong/",
            "/tai-khoan/them_chuong/**",
    };
    public static final String[] ROLE_CONANDMOD_LINK = {"/tai-khoan/rut_tien"};

    public static final String[] ROLE_ADMIN_MOD_LINK = {"/quan-tri",
            "/quan-tri/nguoi_dung/**",
            "/quan-tri/the_loai/**",
            "/quan-tri/truyen/**"};
}
