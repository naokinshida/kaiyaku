package com.example.nagoyameshi.repository;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Storeinfo;

public interface StoreinfoRepository extends JpaRepository<Storeinfo, Integer> {
    Page<Storeinfo> findByStorenameLike(String keyword, Pageable pageable);
    Page<Storeinfo> findByStorenameLikeOrAddressLike(String storenameKeyword, String addressKeyword, Pageable pageable);    
    Page<Storeinfo> findByAddressLike(String area, Pageable pageable);
    Page<Storeinfo> findByCategoriesId(String categories_id, Pageable pageable); 

    public List<Storeinfo> findTop10ByOrderByCreatedAtDesc();
	
}