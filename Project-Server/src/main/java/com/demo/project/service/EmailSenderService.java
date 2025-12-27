package com.demo.project.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Properties;


/**
 * This class is a service that deals with sending emails to users.
 */
@Service
@Setter
public class EmailSenderService {
    private String username;
    private String password;
    private final Properties props;

    /**
     * Instantiates a new email sender service, setting the
     * application properties to enable sending emails.
     */
    public EmailSenderService() {
        props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
        password = "wmnuqoduhqdnuftr";
        username ="sararaly.RA@gmail.com";
    }

    /**
     * Send a email.
     *
     * @param toEmail the email where the message is sent.
     * @param subject the email's subject.
     * @param body    the message that is sent.
     */
    public boolean sendEmail(String toEmail,
                          String subject,
                          String body
    ) {
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };

        Session session = Session.getDefaultInstance(props, auth);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username, "Events App"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
            System.out.println("Sent message successfully....");
            return true;
        } catch (MessagingException | UnsupportedEncodingException mex) {
            mex.printStackTrace();
        }

        return false;
    }

}