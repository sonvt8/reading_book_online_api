package com.cyber.online_books.controller.account;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PropertySource(ignoreResourceNotFound = true, value = "classpath:messages.properties")
@RestController
@RequestMapping(value = "/api/account/pay")
public class AccountPayController {
    @Value("${cyber.truyenonline.email.from}")
    private String emailForm;
    @Value("${cyber.truyenonline.email.display}")
    private String emailDisplay;
    @Value("${cyber.truyenonline.email.signature}")
    private String emailSignature;
    @Value("${cyber.truyenonline.email.url}")
    private String emailUrl;
}
