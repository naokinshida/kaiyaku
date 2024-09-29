package com.example.nagoyameshi.form;

import lombok.Data;

@Data
public class ReservationInputForm {
    
    private String reservationDay; // 予約日
    private Integer people; // 人数
    
    // getter setter
    public String getReservationDay() {
        return reservationDay;
    }

    public void setReservationDay(String reservationDay) {
        this.reservationDay = reservationDay;
    }

    public Integer getPeople() {
        return people;
    }

    public void setPeople(Integer people) {
        this.people = people;
    }
}
