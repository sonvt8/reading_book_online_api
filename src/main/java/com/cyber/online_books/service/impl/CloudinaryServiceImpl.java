package com.cyber.online_books.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Singleton;
import com.cloudinary.utils.ObjectUtils;
import com.cyber.online_books.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {
    private Cloudinary cloudinary;

    @Value("${cloudinary.cloudname}")
    private String cloudname;

    @Value("${cloudinary.apisecret}")
    private String apisecret;

    @Value("${cloudinary.apikey}")
    private String apikey;

    @PostConstruct
    public void initializeCloudinary() {
        cloudinary = Singleton.getCloudinary();
        cloudinary.config.cloudName = cloudname;
        cloudinary.config.apiKey = apikey;
        cloudinary.config.apiSecret = apisecret;
    }

    @Override
    public String uploadAvatar(MultipartFile sourceFile, String fileName) {
        try {
            String publicId = Long.toString(System.nanoTime());
            Map params = ObjectUtils.asMap("folder", "truyenonline/tai_khoan/" + fileName, "public_id", publicId);
            cloudinary.uploader().upload(sourceFile.getBytes(), params);
            //Lấy đường dẫn ảnh vừa upload
            return cloudinary.url().generate("truyenonline/tai_khoan/" + fileName + "/" + publicId);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public String uploadCover(MultipartFile sourceFile, String fileName) {
        try {
            String publicId = Long.toString(System.nanoTime());
            Map params = ObjectUtils.asMap("folder", "truyenonline/truyen/" + fileName, "public_id", publicId);
            cloudinary.uploader().upload(sourceFile.getBytes(), params);
            //Lấy đường dẫn ảnh vừa upload
            return cloudinary.url().generate("truyenonline/truyen/" + fileName + "/" + publicId);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public Map delete(String publicId) throws IOException {
        Map result = cloudinary.uploader().destroy("truyenonline/" + publicId, ObjectUtils.emptyMap());
        return result;
    }
}
