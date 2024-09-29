package com.example.nagoyameshi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendResetTokenMail(String to, String token, String serverUrl) {
        String resetUrl = serverUrl + "/auth/passwordreset?token=" + token;
        String subject = "パスワードリセットの確認メール";
        String message = "こちらのリンクをクリックしてパスワードを再設定してください:\n" + resetUrl;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);
    }
}