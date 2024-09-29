package com.example.nagoyameshi.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.nagoyameshi.entity.Storeinfo;
import com.example.nagoyameshi.repository.StoreinfoRepository;

@Controller
public class HomeController {
    private final StoreinfoRepository storeinfoRepository;        

    public HomeController(StoreinfoRepository storeinfoRepository) {
        this.storeinfoRepository = storeinfoRepository;            
    }    

    @GetMapping("/")
    public String index(Model model) {
        // 最新の10件の店舗情報を取得する
        List<Storeinfo> newStoreinfo = storeinfoRepository.findTop10ByOrderByCreatedAtDesc();
        // モデルに店舗情報を追加する
        model.addAttribute("newStoreinfo", newStoreinfo); 
        return "index";
    }
}