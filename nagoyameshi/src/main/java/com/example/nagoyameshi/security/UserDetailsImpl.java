package com.example.nagoyameshi.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.nagoyameshi.entity.Memberinfo;

public class UserDetailsImpl implements UserDetails {
    private final Memberinfo memberinfo;
    private final Collection<GrantedAuthority> authorities;
    
    public UserDetailsImpl(Memberinfo memberinfo, Collection<GrantedAuthority> authorities) {
        this.memberinfo = memberinfo;
        this.authorities = authorities;
    }
    
    public Memberinfo getMemberinfo() {
        return memberinfo;
    }
    
    @Override
    public String getPassword() {
        return memberinfo.getPassword();
    }
    
    @Override
    public String getUsername() {
        return memberinfo.getMailaddress();
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }    
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return memberinfo.getEnabled();
    }
}