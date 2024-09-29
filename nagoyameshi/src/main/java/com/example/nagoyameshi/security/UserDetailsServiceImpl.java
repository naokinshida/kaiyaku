package com.example.nagoyameshi.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.Memberinfo;
import com.example.nagoyameshi.repository.MemberinfoRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberinfoRepository memberinfoRepository;    
    
    public UserDetailsServiceImpl(MemberinfoRepository memberinfoRepository) {
        this.memberinfoRepository = memberinfoRepository;        
    }
    
    @Override
    public UserDetails loadUserByUsername(String mailaddress) throws UsernameNotFoundException {  
    	try {
            Memberinfo memberinfo = memberinfoRepository.findByMailaddress(mailaddress);
            String memberinfoRoleName = memberinfo.getRole().getName();
            Collection<GrantedAuthority> authorities = new ArrayList<>();         
            authorities.add(new SimpleGrantedAuthority(memberinfoRoleName));
            return new UserDetailsImpl(memberinfo, authorities);
        } catch (Exception e) {
        	// 詳細な例外をログに出力
            e.printStackTrace();
            throw new UsernameNotFoundException("ユーザー情報の取得に失敗しました。", e);
        }
    }   
}