package com.cyber.online_books.utils;

public class SecurityConstant {
    public static final long EXPIRATION_TIME = 86_400_000; // 5 days expressed in milliseconds
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "Jwt-Token";
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Không thể xác thực Token";
    public static final String COMPANY  = "Cyber Ltd";
    public static final String APPLICATION_NAME  = "Read Book Online";
    public static final String AUTHORITIES = "authorities";
    public static final String FORBIDDEN_MESSAGE = "Bạn cần đăng nhập để truy cập trang";
    public static final String ACCESS_DENIED_MESSAGE = "Bạn không đủ quyền truy cập đường dẫn này";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
    public static final String[] PUBLIC_URLS = {
            "/thanh_vien/dang_nhap",
            "/thanh_vien/dang_ky",
            "/thanh_vien/quen_mat_khau",
            "/thanh_vien/xem_top_converter",
            "/thanh_vien/thong-tin-converter",
            "/thanh_vien/binh-luan/xem",
            "/member/image/**",
            "/the-loai/**",
            "/theo-doi/**",
            "/thong-tin",
            "/trang-chu",
            "/danh-muc/**",
            "/chuong/**",
            "/truyen-home/**",
    };
    //public static final String[] PUBLIC_URLS = { "**" };
    public static final String[] ROLE_USER_LINK = {
            "/tai_khoan/theo_doi",
            "/tai_khoan/doi_mat_khau",
            "/tai_khoan/nap_dau",
            "/tai_khoan/doi_ngoai_hieu",
            "/tai_khoan/doi_thong_bao",
            "/tai_khoan/anh_dai_dien",
            "/tai_khoan/de_cu_truyen",
            "/tai_khoan/thanh-toan/danh-sach",
    };
    public static final String[] ROLE_CONANDMOD_LINK = {"/tai_khoan",
            "/tai_khoan/**",
    };

    public static final String[] ROLE_ADMIN_LINK = {"/quan_tri",
            "/quan_tri/nguoi_dung/**",
            "/quan_tri/the_loai/**",
            "/quan_tri/truyen/**"
    };

    public static final String[] ROLE_SMOD_LINK = {"/quan_tri",
            "/quan_tri/the_loai/**",
            "/quan_tri/truyen/**"
    };
}
