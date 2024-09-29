package com.example.nagoyameshi.controller;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Memberinfo;
import com.example.nagoyameshi.form.UserEditForm;
import com.example.nagoyameshi.repository.MemberinfoRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.MemberinfoService;

@Controller
@RequestMapping("/user")
public class UserController {
	private final MemberinfoRepository userRepository; 
	private final MemberinfoService memberinfoService; 
    
    public UserController(MemberinfoRepository memberinfoRepository, MemberinfoService memberinfoService) {
        this.userRepository = memberinfoRepository;
		this.memberinfoService = memberinfoService;       
    }    
    
    @GetMapping
    public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {         
        Memberinfo user = userRepository.getReferenceById(userDetailsImpl.getMemberinfo().getId());  
        
        model.addAttribute("user", user);
        
        return "user/index";
    }
    
    @GetMapping("/edit")
    public String edit(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {        
        Memberinfo user = userRepository.getReferenceById(userDetailsImpl.getMemberinfo().getId());  
        UserEditForm userEditForm = new UserEditForm(user.getId(), user.getName(), user.getFurigana(),user.getMailaddress(), user.getPostal_code(), user.getAddress(), user.getPhone_number());
        
        model.addAttribute("userEditForm", userEditForm);
        
        return "user/edit";
    } 
    
    @PostMapping("/update")
    public String update(@ModelAttribute @Validated UserEditForm userEditForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        // メールアドレスが変更されており、かつ登録済みであれば、BindingResultオブジェクトにエラー内容を追加する
        if (memberinfoService.isMailaddress(userEditForm) && memberinfoService.isMailaddressRegistered(userEditForm.getMailaddress())) {
            FieldError fieldError = new FieldError(bindingResult.getObjectName(), "email", "すでに登録済みのメールアドレスです。");
            bindingResult.addError(fieldError);                       
        }
        
        if (bindingResult.hasErrors()) {
            return "user/edit";
        }
        
        memberinfoService.update(userEditForm);
        redirectAttributes.addFlashAttribute("successMessage", "会員情報を編集しました。");
        
        return "redirect:/user";
    } 
}
