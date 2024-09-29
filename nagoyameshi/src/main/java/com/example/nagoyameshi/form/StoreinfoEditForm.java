package com.example.nagoyameshi.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StoreinfoEditForm {
	@NotNull
    private Integer id;
	
	@NotBlank(message = "店舗名を入力してください。")
    private String storename;
        
	
	@NotBlank(message = "カテゴリIDを入力してください。")
    private String categoriesId;
	
    private MultipartFile imageFile;
    
    @NotBlank(message = "説明を入力してください。")
    private String description;   
    
    @NotNull(message = "最低価格を入力してください。")
    @Min(value = 1, message = "最低価格は1円以上に設定してください。")
    private Integer lowerprice;  
    
    @NotNull(message = "最高価格を入力してください。")
    @Min(value = 1, message = "最高価格は1円以上に設定してください。")
    private Integer maxprice;     
    
    @NotBlank(message = "開店時間を入力してください。")
    private String opening;
    
    @NotBlank(message = "閉店時間を入力してください。")
    private String closed;
    
    @NotBlank(message = "郵便番号を入力してください。")
    private String postalCode;
    
    @NotBlank(message = "住所を入力してください。")
    private String address;
    
    @NotBlank(message = "電話番号を入力してください。")
    private String phoneNumber;
    
    @NotBlank(message = "定休日を入力してください。")
    private String regularHoliday;
}

