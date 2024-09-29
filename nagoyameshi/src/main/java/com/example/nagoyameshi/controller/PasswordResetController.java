package com.example.nagoyameshi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.nagoyameshi.form.PasswordResetForm;
import com.example.nagoyameshi.service.PasswordResetTokenService;

@Controller
public class PasswordResetController {

    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    @GetMapping("/auth/passwordreset")
    public String showPasswordResetForm(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        model.addAttribute("passwordResetForm", new PasswordResetForm());
        return "auth/passwordreset";
    }

    @PostMapping("/auth/passwordreset")
    public String processPasswordReset(@ModelAttribute PasswordResetForm form, Model model) {
        boolean result = passwordResetTokenService.updatePassword(form.getToken(), form.getPassword());
        if (result) {
            model.addAttribute("successMessage", "パスワードが変更されました。"); 
            return "auth/login";
        } else {
            model.addAttribute("errorMessage", "トークンが無効か期限が切れています。やり直してください。");
            return "auth/passwordreset";
        }
    }
}