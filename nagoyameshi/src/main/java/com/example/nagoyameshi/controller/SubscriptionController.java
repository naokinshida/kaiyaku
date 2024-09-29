package com.example.nagoyameshi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SubscriptionController {

    @GetMapping("/subscribe")
    public String showSubscriptionForm() {
        return "subscribe/subscribe";  // サブディレクトリとテンプレート名を含めて指定
    }

    @PostMapping("/subscribe")
    public String handleSubscription() {
        // Stripe APIと連携してサブスクリプションを作成する処理
        return "redirect:/success";  // 成功時のリダイレクト処理
    }
}
