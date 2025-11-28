package com.example.clothingandaccessoriesstore.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MassageServiceTest {

    @Mock
    JavaMailSender javaMailSender;

    @InjectMocks
    MassageService massageService;

    @Test
    void sendMassage() {
        String to = "cholakyanars4@gmail.com";
        String subject = "Clothing and accessories";
        String massage = "hjnjgffghloi  lg jff ";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(massage);

        massageService.sendMassage(to, subject, massage);

        verify(javaMailSender).send(message);
    }
}