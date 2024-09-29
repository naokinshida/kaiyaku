package com.example.nagoyameshi.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Memberinfo;
import com.example.nagoyameshi.entity.Role;
import com.example.nagoyameshi.form.SignupForm;
import com.example.nagoyameshi.form.UserEditForm;
import com.example.nagoyameshi.repository.MemberinfoRepository;
import com.example.nagoyameshi.repository.RoleRepository;

@Service
public class MemberinfoService {

    private final MemberinfoRepository memberinfoRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberinfoService(MemberinfoRepository memberinfoRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.memberinfoRepository = memberinfoRepository;
        this.roleRepository = roleRepository;        
        this.passwordEncoder = passwordEncoder;
    }

    // ここに isMailaddress メソッドを追加します
    public boolean isMailaddress(UserEditForm userEditForm) {
        Memberinfo memberinfo = memberinfoRepository.getReferenceById(userEditForm.getId());
        return !memberinfo.getMailaddress().equals(userEditForm.getMailaddress());
    }

    @Transactional
    public Memberinfo create(SignupForm signupForm) {
        Memberinfo memberinfo = new Memberinfo();

        // "ROLE_GENERAL" がデータベースに事前に存在していることを前提としています。
        Role role = roleRepository.findByName("ROLE_GENERAL");
        if (role == null) {
            System.out.println("role_idにはnullが入っています");
        } else {
            System.out.println("role_idにはnullは入っていません");
        }

        memberinfo.setName(signupForm.getName());
        memberinfo.setFurigana(signupForm.getFurigana());
        memberinfo.setPostal_code(signupForm.getPostal_code());
        memberinfo.setAddress(signupForm.getAddress());
        memberinfo.setPhone_number(signupForm.getPhone_number());
        memberinfo.setMailaddress(signupForm.getMailaddress());
        memberinfo.setPassword(passwordEncoder.encode(signupForm.getPassword()));
        memberinfo.setRole(role);
        memberinfo.setEnabled(true);

        return memberinfoRepository.save(memberinfo);
    }

    @Transactional
    public void update(UserEditForm userEditForm) {
        Memberinfo memberinfo = memberinfoRepository.getReferenceById(userEditForm.getId());

        memberinfo.setName(userEditForm.getName());
        memberinfo.setFurigana(userEditForm.getFurigana());
        memberinfo.setPostal_code(userEditForm.getPostal_code());
        memberinfo.setAddress(userEditForm.getAddress());
        memberinfo.setPhone_number(userEditForm.getPhone_number());
        memberinfo.setMailaddress(userEditForm.getMailaddress());      

        memberinfoRepository.save(memberinfo);
    }

    public boolean isMailaddressRegistered(String mailaddress) {
        Memberinfo memberinfo = memberinfoRepository.findByMailaddress(mailaddress);  
        return memberinfo != null;
    }

    public boolean isSamePassword(String password, String passwordConfirmation) {
        return password.equals(passwordConfirmation);
    }
    
 // ユーザーを有効にする
    @Transactional
    public void enableUser(Memberinfo user) {
        user.setEnabled(true); 
        memberinfoRepository.save(user);
    }

	public static void createVerificationTokenForMemberinfo(String mailaddress, String token) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
	
	@Transactional
    public void upgradeUserRole(String mailaddress, String newRoleName) {
        Memberinfo memberinfo = memberinfoRepository.findByMailaddress(mailaddress);
        if (memberinfo != null) {
            Role newRole = roleRepository.findByName(newRoleName);
            if (newRole != null) {
                memberinfo.setRole(newRole);
                memberinfoRepository.save(memberinfo);
            } else {
                throw new RuntimeException("Role not found: " + newRoleName);
            }
        } else {
            throw new RuntimeException("Member not found with email: " + mailaddress);
        }
    }
}
