package com.example.nagoyameshi.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEditForm {
	 @NotNull
     private Integer id;
     
     @NotBlank(message = "氏名を入力してください。")
     private String name;
     
     @NotBlank(message = "フリガナを入力してください。")
     private String furigana;
     
     @NotBlank(message = "メールアドレスを入力してください。")
     @Email
     private String mailaddress;
     
     @NotBlank(message = "郵便番号を入力してください。")
     private String postal_code;
     
     @NotBlank(message = "住所を入力してください。")
     private String address;
     
     @NotBlank(message = "電話番号を入力してください。")
     private String phone_number;
     
    
}

