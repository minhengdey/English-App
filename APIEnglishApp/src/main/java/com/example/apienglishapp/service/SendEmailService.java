package com.example.apienglishapp.service;

import com.example.apienglishapp.exception.AppException;
import com.example.apienglishapp.enums.ErrorCode;
import com.example.apienglishapp.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class SendEmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private UserRepository userRepository;

    public String sendOtp(String toEmail) throws MessagingException {
        if (!userRepository.existsByEmail(toEmail)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        String otp = generateOtp();

        MimeMessage message =javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(toEmail);
        helper.setSubject("Your OTP Code");
        helper.setText("Your OTP code is: " + otp);

        javaMailSender.send(message);
        return otp;
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Mã OTP 6 chữ số
        return String.valueOf(otp);
    }
}
