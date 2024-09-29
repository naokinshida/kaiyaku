package com.example.nagoyameshi.service;
 
 import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.nagoyameshi.entity.Storeinfo;
import com.example.nagoyameshi.form.StoreinfoEditForm;
import com.example.nagoyameshi.form.StoreinfoRegisterForm;
import com.example.nagoyameshi.repository.StoreinfoRepository;
 
 @Service
 public class StoreinfoService {
     private final StoreinfoRepository storeinfoRepository;    
     
     public StoreinfoService(StoreinfoRepository storeinfoRepository) {
         this.storeinfoRepository = storeinfoRepository;        
     }    
     
     @Transactional
     public void create(StoreinfoRegisterForm storeinfoRegisterForm) {
         Storeinfo storeinfo = new Storeinfo();        
         MultipartFile imageFile = storeinfoRegisterForm.getImageFile();
         
         if (!imageFile.isEmpty()) {
             String imageName = imageFile.getOriginalFilename(); 
             String hashedImageName = generateNewFileName(imageName);
             Path filePath = Paths.get("src/main/resources/static/images/" + hashedImageName);
             copyImageFile(imageFile, filePath);
             storeinfo.setImageName(hashedImageName);
         }
         
         storeinfo.setStorename(storeinfoRegisterForm.getStorename());
         storeinfo.setCategoriesId(storeinfoRegisterForm.getCategoriesId());
         storeinfo.setDescription(storeinfoRegisterForm.getDescription());
         storeinfo.setLowerprice(storeinfoRegisterForm.getLowerprice());
         storeinfo.setMaxprice(storeinfoRegisterForm.getMaxprice());
         storeinfo.setPostalCode(storeinfoRegisterForm.getPostalCode());
         storeinfo.setOpening(storeinfoRegisterForm.getOpening());
         storeinfo.setClosed(storeinfoRegisterForm.getClosed());
         storeinfo.setAddress(storeinfoRegisterForm.getAddress());
         storeinfo.setPhoneNumber(storeinfoRegisterForm.getPhoneNumber());
         storeinfo.setRegularHoliday(storeinfoRegisterForm.getRegularHoliday());            
         storeinfoRepository.save(storeinfo);
     }
     
     @Transactional
     public void update(StoreinfoEditForm storeinfoEditForm) {
         Storeinfo storeinfo = storeinfoRepository.getReferenceById(storeinfoEditForm.getId());
         MultipartFile imageFile = storeinfoEditForm.getImageFile();
         
         if (!imageFile.isEmpty()) {
             String imageName = imageFile.getOriginalFilename(); 
             String hashedImageName = generateNewFileName(imageName);
             Path filePath = Paths.get("src/main/resources/static/images/" + hashedImageName);
             copyImageFile(imageFile, filePath);
             storeinfo.setImageName(hashedImageName);
         }
         
         storeinfo.setStorename(storeinfoEditForm.getStorename());
         storeinfo.setCategoriesId(storeinfoEditForm.getCategoriesId());
         storeinfo.setDescription(storeinfoEditForm.getDescription());
         storeinfo.setLowerprice(storeinfoEditForm.getLowerprice());
         storeinfo.setMaxprice(storeinfoEditForm.getMaxprice());
         storeinfo.setPostalCode(storeinfoEditForm.getPostalCode());
         storeinfo.setOpening(storeinfoEditForm.getOpening());
         storeinfo.setClosed(storeinfoEditForm.getClosed());
         storeinfo.setAddress(storeinfoEditForm.getAddress());
         storeinfo.setPhoneNumber(storeinfoEditForm.getPhoneNumber());
         storeinfo.setRegularHoliday(storeinfoEditForm.getRegularHoliday());            
         storeinfoRepository.save(storeinfo);
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
     
     // 画像ファイルを指定したファイルにコピーする
     public void copyImageFile(MultipartFile imageFile, Path filePath) {           
         try {
             Files.copy(imageFile.getInputStream(), filePath);
         } catch (IOException e) {
             e.printStackTrace();
         }          
     }

	public Storeinfo findById(int id) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

 }