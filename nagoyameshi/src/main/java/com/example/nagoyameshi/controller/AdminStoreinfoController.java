package com.example.nagoyameshi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Storeinfo;
import com.example.nagoyameshi.form.StoreinfoEditForm;
import com.example.nagoyameshi.form.StoreinfoRegisterForm;
import com.example.nagoyameshi.repository.StoreinfoRepository;
import com.example.nagoyameshi.service.StoreinfoService;

@Controller
@RequestMapping("/admin/storeinfo")
public class AdminStoreinfoController {
    private final StoreinfoRepository storeinfoRepository;
    private final StoreinfoService storeinfoService;

    public AdminStoreinfoController(StoreinfoRepository storeinfoRepository, StoreinfoService storeinfoService) {
        this.storeinfoRepository = storeinfoRepository;
        this.storeinfoService = storeinfoService;
    }

    @GetMapping
    public String index(Model model, 
                        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, 
                        @RequestParam(name = "keyword", required = false) String keyword) {
        Page<Storeinfo> storeinfoPage;

        if (keyword != null && !keyword.isEmpty()) {
            storeinfoPage = storeinfoRepository.findByStorenameLike("%" + keyword + "%", pageable);
        } else {
            storeinfoPage = storeinfoRepository.findAll(pageable);
        }

        model.addAttribute("keyword", keyword);
        model.addAttribute("storeinfoPage", storeinfoPage); 

        return "admin/storeinfo/index"; 
    }
    
    @GetMapping("/{id}")
    public String show(@PathVariable(name = "id") Integer id, Model model) {
        Storeinfo storeinfo = storeinfoRepository.getReferenceById(id);
        
        model.addAttribute("storeinfo", storeinfo);
        
        return "admin/storeinfo/show";
    } 
    
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("storeinfoRegisterForm", new StoreinfoRegisterForm());
        return "admin/storeinfo/register";
    }
    
    @PostMapping("/create")
    public String create(@ModelAttribute @Validated StoreinfoRegisterForm storeinfoRegisterForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {        
        if (bindingResult.hasErrors()) {
            return "admin/storeinfo/register";
        }
        
        storeinfoService.create(storeinfoRegisterForm);
        redirectAttributes.addFlashAttribute("successMessage", "店舗を登録しました。");    
        
        return "redirect:/admin/storeinfo";
    }
    
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable(name = "id") Integer id, Model model) {
        Storeinfo storeinfo = storeinfoRepository.getReferenceById(id);
        String image_name = storeinfo.getImageName();
        StoreinfoEditForm storeinfoEditForm = new StoreinfoEditForm(storeinfo.getId(), storeinfo.getStorename(), storeinfo.getCategoriesId(), null, storeinfo.getDescription(), storeinfo.getLowerprice(), storeinfo.getMaxprice(), storeinfo.getOpening(), storeinfo.getClosed(), storeinfo.getPostalCode(), storeinfo.getAddress(), storeinfo.getPhoneNumber(), storeinfo.getRegularHoliday());
        
        model.addAttribute("image_name", image_name);
        model.addAttribute("storeinfoEditForm", storeinfoEditForm);
        
        return "admin/storeinfo/edit";
    }
    
    @PostMapping("/{id}/update")
    public String update(@ModelAttribute @Validated StoreinfoEditForm storeinfoEditForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {        
        if (bindingResult.hasErrors()) {
            return "admin/storeinfo/edit";
        }
        
        storeinfoService.update(storeinfoEditForm);
        redirectAttributes.addFlashAttribute("successMessage", "店舗情報を編集しました。");
        
        return "redirect:/admin/storeinfo";
    }
    
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {        
        storeinfoRepository.deleteById(id);
                
        redirectAttributes.addFlashAttribute("successMessage", "店舗を削除しました。");
        
        return "redirect:/admin/storeinfo";
    }
}
