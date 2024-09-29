package com.example.nagoyameshi.controller;

import java.util.UUID;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Memberinfo;
import com.example.nagoyameshi.entity.PasswordResetToken;
import com.example.nagoyameshi.entity.VerificationToken;
import com.example.nagoyameshi.event.SignupEventPublisher;
import com.example.nagoyameshi.form.PasswordResetForm;
import com.example.nagoyameshi.form.SignupForm;
import com.example.nagoyameshi.service.MemberinfoService;
import com.example.nagoyameshi.service.PasswordResetTokenService;
import com.example.nagoyameshi.service.VerificationTokenService;

@Controller
public class AuthController {
    private final MemberinfoService memberinfoService;
    private final SignupEventPublisher signupEventPublisher;
    private final VerificationTokenService verificationTokenService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final JavaMailSender mailSender;

    public AuthController(MemberinfoService memberinfoService, SignupEventPublisher signupEventPublisher, VerificationTokenService verificationTokenService, PasswordResetTokenService passwordResetTokenService, JavaMailSender mailSender) {
        this.memberinfoService = memberinfoService;
        this.signupEventPublisher = signupEventPublisher;
        this.verificationTokenService = verificationTokenService;
        this.passwordResetTokenService = passwordResetTokenService;
        this.mailSender = mailSender;
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("signupForm", new SignupForm());
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute @Validated SignupForm signupForm, BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
        if (memberinfoService.isMailaddressRegistered(signupForm.getMailaddress())) {
            bindingResult.addError(new FieldError(bindingResult.getObjectName(), "mailaddress", "すでに登録済みのメールアドレスです。"));
        }

        if (!memberinfoService.isSamePassword(signupForm.getPassword(), signupForm.getPasswordConfirmation())) {
            bindingResult.addError(new FieldError(bindingResult.getObjectName(), "password", "パスワードが一致しません。"));
        }

        if (bindingResult.hasErrors()) {
            return "auth/signup";
        }

        Memberinfo createdUser = memberinfoService.create(signupForm);
        String requestUrl = new String(httpServletRequest.getRequestURL());
        signupEventPublisher.publishSignupEvent(createdUser, requestUrl);
        redirectAttributes.addFlashAttribute("successMessage", "ご入力いただいたメールアドレスに認証メールを送信しました。メールに記載されているリンクをクリックし、会員登録を完了してください。");

        return "redirect:/";
    }

    @GetMapping("/signup/verify")
    public String verify(@RequestParam(name = "token") String token, Model model) {
        VerificationToken verificationToken = verificationTokenService.getVerificationToken(token);
        if (verificationToken != null) {
            Memberinfo user = verificationToken.getMemberinfo();
            memberinfoService.enableUser(user);
            model.addAttribute("successMessage", "会員登録が完了しました。");
        } else {
            model.addAttribute("errorMessage", "トークンが無効です。");
        }
        return "auth/verify";
    }

    @GetMapping("/auth/UserNewPasswordIssue")
    public String showForgotPasswordForm() {
        return "auth/UserNewPasswordIssue";
    }

    @PostMapping("/auth/UserNewPasswordIssue")
    public String processForgotPassword(@RequestParam("mailaddress") String mailaddress, RedirectAttributes redirectAttributes) {
        try {
            String token = UUID.randomUUID().toString();
            passwordResetTokenService.createPasswordResetTokenForMemberinfo(mailaddress, token);
            String resetUrl = "http://localhost:8080/reset-password?token=" + token;
            sendEmail(mailaddress, resetUrl);
            redirectAttributes.addFlashAttribute("successMessage", "パスワード再発行リンクをメールで送信しました。");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "メールアドレスが見つかりません。");
        }
        return "redirect:/auth/UserNewPasswordIssue";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        PasswordResetToken resetToken = passwordResetTokenService.getPasswordResetToken(token);
        if (resetToken == null || resetToken.isExpired()) {
            model.addAttribute("errorMessage", "トークンが無効または期限切れです。");
            return "auth/reset-password";
        }

        PasswordResetForm passwordResetForm = new PasswordResetForm();
        passwordResetForm.setToken(token);
        model.addAttribute("passwordResetForm", passwordResetForm);

        return "auth/reset-password";
    }

   /* @PostMapping("/reset-password")
    public String processResetPassword(@ModelAttribute @Validated PasswordResetForm form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "auth/reset-password";
        }

        boolean success = passwordResetTokenService.updatePassword(form.getToken(), form.getPassword());

        if (success) {
            model.addAttribute("message", "パスワードが正常にリセットされました");
        } else {
            model.addAttribute("message", "パスワードのリセットに失敗しました");
        }
		return "auth/login";
    }*/

    private void sendEmail(String email, String resetUrl) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setTo(email);
        helper.setSubject("NAGOYAMESHI：パスワード再発行リンク");
        helper.setText("以下のリンクをクリックしてパスワードを再発行してください: " + resetUrl);

        mailSender.send(message);
    }
}