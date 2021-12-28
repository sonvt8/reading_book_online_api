package com.cyber.online_books.service;

import com.cyber.online_books.entity.Mail;

/**
 * @author Cyber Group
 */
public interface EmailService {
    
    boolean sendSimpleMessage(Mail mail, String template);
    
}
