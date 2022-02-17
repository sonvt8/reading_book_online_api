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
    public String upload(MultipartFile sourceFile, String fileName) {
        try {

            Map params = ObjectUtils.asMap("public_id", "truyenonline/" + fileName);
            cloudinary.uploader().upload(sourceFile.getBytes(), params);

            //Lấy đường dẫn ảnh vừa upload
            return cloudinary.url().generate("truyenonline/" + fileName);
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
