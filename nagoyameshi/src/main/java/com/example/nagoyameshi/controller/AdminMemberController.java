package com.example.nagoyameshi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.nagoyameshi.entity.Memberinfo;
import com.example.nagoyameshi.repository.MemberinfoRepository;

@Controller
@RequestMapping("/admin/memberinfo")
public class AdminMemberController {
    private final MemberinfoRepository memberinfoRepository;

    public AdminMemberController(MemberinfoRepository memberinfoRepository) {
        this.memberinfoRepository = memberinfoRepository;
    }

    @GetMapping
    public String index(Model model, 
                        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, 
                        @RequestParam(name = "keyword", required = false) String keyword) {
        Page<Memberinfo> memberinfoPage;

        if (keyword != null && !keyword.isEmpty()) {
            memberinfoPage = memberinfoRepository.findByMailaddressLike("%" + keyword + "%", pageable);
        } else {
            memberinfoPage = memberinfoRepository.findAll(pageable);
        }

        model.addAttribute("keyword", keyword);
        model.addAttribute("memberinfoPage", memberinfoPage); 

        return "admin/memberinfo/index"; 
    }
}