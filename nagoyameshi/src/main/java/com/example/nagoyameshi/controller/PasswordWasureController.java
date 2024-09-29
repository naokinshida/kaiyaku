package com.example.nagoyameshi.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.nagoyameshi.service.EmailService;
import com.example.nagoyameshi.service.PasswordResetTokenService;

@Controller
public class PasswordWasureController {

    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    @Autowired
    private EmailService emailService;

    @Value("${server.url}") // アプリケーションプロパティファイルに設定するサーバーのURL
    private String serverUrl;

    @GetMapping("/passwordwasure")
    public String showForgotPasswordForm() {
        return "auth/passwordwasure"; 
    }

    @PostMapping("/passwordwasure")
    public String processForgotPassword(@RequestParam("mailaddress") String mailaddress, Model model) {
        try {
            String token = UUID.randomUUID().toString();
            passwordResetTokenService.createPasswordResetTokenForMemberinfo(mailaddress, token);
            // メール送信
            emailService.sendResetTokenMail(mailaddress, token, serverUrl);
            model.addAttribute("successMessage", "パスワードリセット用のメールを送信しました。"); 
        } catch (Exception e) {
            model.addAttribute("errorMessage", "そのメールアドレスは存在しません。");
        }
        return "auth/passwordwasure";
    }
}