package com.demo.project.dto;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EmailEvent extends ApplicationEvent {
    private String toEmail;
    private String subject;
    private String body ;

    public EmailEvent(Object source, String toEmail, String subject, String body) {
        super(source);
        this.toEmail = toEmail;
        this.subject = subject;
        this.body = body;
    }
}
