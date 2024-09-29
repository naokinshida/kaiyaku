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

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.form.CategoryEditForm;
import com.example.nagoyameshi.form.CategoryRegisterForm;
import com.example.nagoyameshi.repository.CategoryRepository;
import com.example.nagoyameshi.service.CategoryService;

@Controller
@RequestMapping("/admin/category")
public class AdminCategoryController {
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;

    public AdminCategoryController(CategoryRepository categoryRepository, CategoryService categoryService) {
        this.categoryRepository = categoryRepository;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String index(Model model,
                        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
                        @RequestParam(name = "keyword", required = false) String keyword) {
        Page<Category> categoryPage;

        if (keyword != null && !keyword.isEmpty()) {
            categoryPage = categoryRepository.findByCategoryNameLike("%" + keyword + "%", pageable);
        } else {
            categoryPage = categoryRepository.findAll(pageable);
        }

        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryPage", categoryPage); 

        return "admin/category/index"; 
    }

    @GetMapping("/{id}")
    public String show(@PathVariable(name = "id") Integer id, Model model) {
        Category category = categoryRepository.findById(id).orElse(null);
        
        model.addAttribute("category", category);
        
        return "admin/category/show";
    } 

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("categoryRegisterForm", new CategoryRegisterForm());
        return "admin/category/register";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute @Validated CategoryRegisterForm categoryRegisterForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {        
        if (bindingResult.hasErrors()) {
            return "admin/category/register";
        }

        categoryService.create(categoryRegisterForm);
        redirectAttributes.addFlashAttribute("successMessage", "カテゴリを登録しました。");    

        return "redirect:/admin/category";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable(name = "id") Integer id, Model model) {
        Category category = categoryRepository.findById(id).orElse(null);
        CategoryEditForm categoryEditForm = new CategoryEditForm(category.getId(), category.getCategoryName());

        model.addAttribute("categoryEditForm", categoryEditForm);

        return "admin/category/edit";
    }

    @PostMapping("/{id}/update")
    public String update(@ModelAttribute @Validated CategoryEditForm categoryEditForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {        
        if (bindingResult.hasErrors()) {
            return "admin/category/edit";
        }

        categoryService.update(categoryEditForm);
        redirectAttributes.addFlashAttribute("successMessage", "カテゴリを編集しました。");

        return "redirect:/admin/category";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {        
        categoryRepository.deleteById(id);

        redirectAttributes.addFlashAttribute("successMessage", "カテゴリを削除しました。");

        return "redirect:/admin/category";
    }
}