package com.cyber.online_books.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface CloudinaryService {

    /**
     * Upload file lên Cloudinary
     *
     * @param sourceFile File upload
     * @param fileName   tên File mới
     * @return String trả về đường dẫn ảnh vừa upload
     * @throws java.io.IOException trả về null
     */
    String upload(MultipartFile sourceFile, String fileName);

    /**
     * Delete file lên Cloudinary
     *
     * @param publicId Id của ảnh trên Cloudinary
     * @return JSON Response chi tiết thông tin ảnh trên Cloudinary
     * @throws java.io.IOException trả về null
     */
    Map delete(String publicId) throws IOException;
}
