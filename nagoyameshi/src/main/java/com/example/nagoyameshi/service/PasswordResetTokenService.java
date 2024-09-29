package com.example.nagoyameshi.service;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.Memberinfo;
import com.example.nagoyameshi.entity.PasswordResetToken;
import com.example.nagoyameshi.repository.MemberinfoRepository;
import com.example.nagoyameshi.repository.PasswordResetTokenRepository;

@Service
public class PasswordResetTokenService {

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private MemberinfoRepository memberinfoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void createPasswordResetTokenForMemberinfo(String mailaddress, String token) {
        Memberinfo user = memberinfoRepository.findByMailaddress(mailaddress);
        if (user == null) {
            throw new RuntimeException("ユーザーが見つかりません。");
        }
        PasswordResetToken myToken = new PasswordResetToken(token, user);
        myToken.setExpiryDate(calculateExpiryDate(24 * 60));  // 24時間後に期限切れ

        // 古いトークンを削除
        passwordResetTokenRepository.deleteByUser(user);

        // 新しいトークンを保存
        passwordResetTokenRepository.save(myToken);
    }

    public boolean updatePassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token);
        if (resetToken == null) {
            return false;
        }
        Calendar cal = Calendar.getInstance();
        if ((resetToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return false;
        }
        Memberinfo user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        memberinfoRepository.save(user); // 新しいパスワードの保存
        passwordResetTokenRepository.delete(resetToken); // 使ったトークンの削除
        return true;
    }

    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

	public PasswordResetToken getPasswordResetToken(String token) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}
}