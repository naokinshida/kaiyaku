package com.example.nagoyameshi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Memberinfo;
import com.example.nagoyameshi.entity.Reservation;
import com.example.nagoyameshi.entity.Storeinfo;
import com.example.nagoyameshi.form.ReservationInputForm;
import com.example.nagoyameshi.form.ReservationRegisterForm;
import com.example.nagoyameshi.repository.ReservationRepository;
import com.example.nagoyameshi.repository.StoreinfoRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.ReservationService;

@Controller
public class ReservationController {
	private final ReservationRepository reservationRepository;      
	private final StoreinfoRepository storeinfoRepository;
    private final ReservationService reservationService; 

    public ReservationController(ReservationRepository reservationRepository, StoreinfoRepository storeinfoRepository, ReservationService reservationService) {        
        this.reservationRepository = reservationRepository;
        this.storeinfoRepository = storeinfoRepository;
        this.reservationService = reservationService;             
    }    

    @GetMapping("/reservations")
    public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, Model model) {
        Memberinfo memberinfo = userDetailsImpl.getMemberinfo();
        Page<Reservation> reservationPage = reservationRepository.findByMemberinfoOrderByCreatedAtDesc(memberinfo, pageable);
        
        model.addAttribute("reservationPage", reservationPage);         
        return "reservations/index";
    }
    
    @GetMapping("/storeinfo/{id}/reservations/input")
    public String input(@PathVariable(name = "id") Integer id,
                        @ModelAttribute @Validated ReservationInputForm reservationInputForm,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes,
                        Model model) {   
        Storeinfo storeinfo = storeinfoRepository.getReferenceById(id);

        if (bindingResult.hasErrors()) {            
            model.addAttribute("storeinfo", storeinfo);            
            model.addAttribute("errorMessage", "予約内容に不備があります。"); 
            return "houses/show";
        }
        
        redirectAttributes.addFlashAttribute("reservationInputForm", reservationInputForm);           
        return "redirect:/houses/" + id + "/reservations/confirm";
    }    

    @GetMapping("/{id}")
    public String show(@PathVariable(name = "id") Integer id, Model model) {
        try {
            Storeinfo storeinfo = storeinfoRepository.getReferenceById(id);
            model.addAttribute("storeinfo", storeinfo);         
            model.addAttribute("reservationInputForm", new ReservationInputForm());
            return "storeinfo/show";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "店舗情報を取得できませんでした。");
            return "storeinfo/error";
        }
    }
    
    @GetMapping("/houses/{id}/reservations/confirm")
    public String confirm(@PathVariable(name = "id") Integer id,
                          @ModelAttribute ReservationInputForm reservationInputForm,
                          @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,                          
                          Model model) {        
        Storeinfo storeinfo = storeinfoRepository.getReferenceById(id);
        Memberinfo memberinfo = userDetailsImpl.getMemberinfo(); 
                
        // 予約日を取得する
        String reservationDay = reservationInputForm.getReservationDay();
        
        ReservationRegisterForm reservationRegisterForm = new ReservationRegisterForm(storeinfo.getId(), memberinfo.getId(), reservationDay, reservationInputForm.getPeople());
        
        model.addAttribute("storeinfo", storeinfo);  
        model.addAttribute("reservationRegisterForm", reservationRegisterForm);       
        return "reservations/confirm";
    }

    @PostMapping("/houses/{id}/reservations/create")
    public String create(@PathVariable(name = "id") Integer id,
                         @ModelAttribute ReservationRegisterForm reservationRegisterForm,
                         @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,                          
                         Model model) {
        // 予約の作成処理をここに追加
        // reservationService.createReservationメソッドなどを呼び出して、予約をデータベースに保存する
        // reservationService.createReservation(reservationRegisterForm);

        return "redirect:/reservations"; // 予約完了後のリダイレクト先
    }  
    @PostMapping("/storeinfo/{id}/reservations/create")
    public String create(@ModelAttribute ReservationRegisterForm reservationRegisterForm) {                
        reservationService.create(reservationRegisterForm);        
        
        return "redirect:/reservations?reserved";
    }
    @PostMapping("/reservations/cancel/{reservationId}")
    public String cancelReservation(@PathVariable(name = "reservationId") Integer reservationId, 
                                    @AuthenticationPrincipal UserDetailsImpl userDetailsImpl, 
                                    RedirectAttributes redirectAttributes) {
        try {
            reservationService.cancel(reservationId, userDetailsImpl.getMemberinfo());
            redirectAttributes.addFlashAttribute("message", "予約をキャンセルしました。");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "予約のキャンセルに失敗しました。");
        }
        return "redirect:/reservations";
    }
}