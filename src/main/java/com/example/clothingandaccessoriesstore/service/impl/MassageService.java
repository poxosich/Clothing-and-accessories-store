package com.example.clothingandaccessoriesstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MassageService {
    private final JavaMailSender javaMailSender;

    @Async
    public void sendMassage(String to, String subject, String massage) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(massage);
        javaMailSender.send(message);
    }
}
