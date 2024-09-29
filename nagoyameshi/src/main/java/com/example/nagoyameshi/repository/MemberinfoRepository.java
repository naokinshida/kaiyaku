package com.example.nagoyameshi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Memberinfo;

public interface MemberinfoRepository extends JpaRepository<Memberinfo,Integer> {
	public Page<Memberinfo> findByMailaddressLike(String keyword, Pageable pageable);
	public Memberinfo findByMailaddress(String mailaddress);
}