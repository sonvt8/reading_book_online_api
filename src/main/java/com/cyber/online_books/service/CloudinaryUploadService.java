package com.cyber.online_books.service;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryUploadService {

    /**
     * Upload file lên Cloudinary
     *
     * @param sourceFile File upload
     * @param fileName   tên File mới
     * @return String trả về đường dẫn ảnh vừa upload
     * @throws java.io.IOException trả về null
     */
    String upload(MultipartFile sourceFile, String fileName);
}
