package com.example.nagoyameshi.service;
 
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.form.CategoryEditForm;
import com.example.nagoyameshi.form.CategoryRegisterForm;
import com.example.nagoyameshi.repository.CategoryRepository;
 
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;    
    
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;        
    }    
    
    @Transactional
    public void create(CategoryRegisterForm categoryRegisterForm) {
        Category category = new Category();        
        
        category.setCategoryName(categoryRegisterForm.getCategory_name());
                    
        categoryRepository.save(category);
    }
    
    @Transactional
    public void update(CategoryEditForm categoryEditForm) {
        Category category = categoryRepository.getReferenceById(categoryEditForm.getId());
        
        category.setCategoryName(categoryEditForm.getCategory_name());
                     
        categoryRepository.save(category);
    }
    
    // UUIDを使って生成したファイル名を返す
    public String generateNewFileName(String fileName) {
        String[] fileNames = fileName.split("\\.");                
        for (int i = 0; i < fileNames.length - 1; i++) {
            fileNames[i] = UUID.randomUUID().toString();            
        }
        String hashedFileName = String.join(".", fileNames);
        return hashedFileName;
    }     
}