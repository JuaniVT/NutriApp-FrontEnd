package com.NutriApp.NutriApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.beans.JavaBean;

@Service
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void enviarMail (String para, String asunto, String mensaje){
        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setTo(para);
        mail.setSubject(asunto);
        mail.setText(mensaje);

        javaMailSender.send(mail);
    }
}
