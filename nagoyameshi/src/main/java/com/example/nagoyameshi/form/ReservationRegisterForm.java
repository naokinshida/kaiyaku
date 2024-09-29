package com.example.nagoyameshi.form;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReservationRegisterForm {
	private Integer storeinfoId;
    
    private Integer memberinfoId;    
        
    private String reservationDay;     
    
    private Integer People;
       
}


