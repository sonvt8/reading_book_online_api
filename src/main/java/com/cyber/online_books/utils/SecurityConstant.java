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
            "/member/image/**",
            "/the-loai/**",
            "/thong-tin",
            "/home",
            "/danh-muc/**",
            "/chuong/**"
    };
    //public static final String[] PUBLIC_URLS = { "**" };
    public static final String[] ROLE_USER_LINK = {"/tai_khoan",
            "/tai_khoan/theo_doi",
            "/tai_khoan/doi_mat_khau",
            "/tai_khoan/nap_dau",
            "/tai_khoan/giao_dich",
            "/tai_khoan/quan_ly_truyen",
            "/tai_khoan/them_truyen",
            "/tai_khoan/list_chuong/",
            "/tai_khoan/them_chuong/**",
    };
    public static final String[] ROLE_CONANDMOD_LINK = {"/tai_khoan/rut_tien"};

    public static final String[] ROLE_ADMIN_MOD_LINK = {"/quan_tri",
            "/quan_tri/nguoi_dung/**",
            "/quan_tri/the_loai/**",
            "/quan_tri/truyen/**"};
}
