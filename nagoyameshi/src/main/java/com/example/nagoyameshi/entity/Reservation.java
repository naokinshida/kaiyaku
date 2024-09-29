package com.example.nagoyameshi.entity;

 import java.sql.Timestamp;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Data;
 
 @Entity
 @Table(name = "reservations")
 @Data
public class Reservation {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "id")
     private Integer id;
     
     @ManyToOne
     @JoinColumn(name = "store_id")
     private Storeinfo storeinfo; 
     
     @ManyToOne
     @JoinColumn(name = "memberinfo_id")
     private Memberinfo memberinfo;     
     
     @Column(name = "reservationday")
     private LocalDate reservationDay;
     
     @Column(name = "people")
     private Integer  People;       
     
     @Column(name = "created_at", insertable = false, updatable = false)
     private Timestamp createdAt;
     
     @Column(name = "updated_at", insertable = false, updatable = false)
     private Timestamp updatedAt;    
}
