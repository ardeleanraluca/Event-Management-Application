package com.demo.project.service;

import com.demo.project.dto.EmailEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EmailListener {

    @Autowired
    EmailSenderService emailSenderService;


    /**
     * Send mail.
     *
     * @param emailEvent the email event
     */
    @EventListener()
    public void sendMail(EmailEvent emailEvent) {
        System.out.println(emailEvent.getBody());
        emailSenderService.sendEmail(emailEvent.getToEmail(), emailEvent.getSubject(), emailEvent.getBody());
    }
}
