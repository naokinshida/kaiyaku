package com.example.nagoyameshi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Storeinfo storeInfo;

    private String comment;
    
    private int star;

    // No-argument constructor (required by JPA)
    public Review() {}

    // Constructor with all arguments
    public Review(Integer id, Storeinfo storeInfo, String comment, int star) {
        this.id = id;
        this.storeInfo = storeInfo;
        this.comment = comment;
        this.star = star;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Storeinfo getStoreInfo() {
        return storeInfo;
    }

    public void setStoreInfo(Storeinfo storeInfo) {
        this.storeInfo = storeInfo;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }
}
