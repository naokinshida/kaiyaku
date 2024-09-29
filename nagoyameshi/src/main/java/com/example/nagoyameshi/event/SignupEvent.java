package com.example.nagoyameshi.event;

import org.springframework.context.ApplicationEvent;

import com.example.nagoyameshi.entity.Memberinfo;

import lombok.Getter;

@Getter
public class SignupEvent extends ApplicationEvent {
    private Memberinfo user;
    private String requestUrl;        

    public SignupEvent(Object source, Memberinfo user, String requestUrl) {
        super(source);
        
        this.user = user;        
        this.requestUrl = requestUrl;
    }
}
