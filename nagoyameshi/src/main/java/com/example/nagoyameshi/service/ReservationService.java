package com.example.nagoyameshi.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Memberinfo;
import com.example.nagoyameshi.entity.Reservation;
import com.example.nagoyameshi.entity.Storeinfo;
import com.example.nagoyameshi.form.ReservationRegisterForm;
import com.example.nagoyameshi.repository.MemberinfoRepository;
import com.example.nagoyameshi.repository.ReservationRepository;
import com.example.nagoyameshi.repository.StoreinfoRepository;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;  
    private final StoreinfoRepository storeinfoRepository;  
    private final MemberinfoRepository memberinfoRepository;

    public ReservationService(ReservationRepository reservationRepository, StoreinfoRepository storeinfoRepository, MemberinfoRepository memberinfoRepository) {
        this.reservationRepository = reservationRepository;  
        this.storeinfoRepository = storeinfoRepository;  
        this.memberinfoRepository = memberinfoRepository;  
    }

    @Transactional
    public void create(ReservationRegisterForm reservationRegisterForm) { 
        Reservation reservation = new Reservation();
        Storeinfo storeinfo = storeinfoRepository.getReferenceById(reservationRegisterForm.getStoreinfoId());
        Memberinfo memberinfo = memberinfoRepository.getReferenceById(reservationRegisterForm.getMemberinfoId());
        LocalDate reservationDay = LocalDate.parse(reservationRegisterForm.getReservationDay());

        reservation.setStoreinfo(storeinfo);
        reservation.setMemberinfo(memberinfo);
        reservation.setReservationDay(reservationDay);
        reservation.setPeople(reservationRegisterForm.getPeople());

        reservationRepository.save(reservation);
    }

    @Transactional
    public void cancel(Integer reservationId, Memberinfo memberinfo) throws Exception {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new Exception("予約が見つかりません"));

        // 予約をキャンセルできるのはその予約を作ったユーザーであることを確認
        if (!reservation.getMemberinfo().equals(memberinfo)) {
            throw new Exception("この予約をキャンセルする権限がありません。");
        }

        reservationRepository.delete(reservation);
    }
}