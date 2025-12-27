package com.demo.project;

import com.demo.project.service.EmailSenderService;
import jakarta.mail.Transport;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class EmailSenderServiceTest {

    @Test
    public void testSendMail() throws jakarta.mail.MessagingException {

        EmailSenderService emailSenderService = new EmailSenderService();

        try (MockedStatic<Transport> mockedTransport = Mockito.mockStatic(Transport.class)) {
            emailSenderService.sendEmail("ardelean.raluca.3@gmail.com",
                    "Subject", "How are you?");
            Transport.send(Mockito.argThat(message -> {
                try {
                    assertEquals("sararaly.RA@gmail.com", message.getFrom()[0].toString());
                    assertEquals("ardelean.raluca.3@gmail.com", message.getAllRecipients()[0].toString());
                    assertEquals("Subject", message.getSubject().trim());
                    assertEquals("How are you?", message.getContent().toString().trim());
                } catch (Exception e) {
                    return false;
                }
                return false;
            }));


        }


    }
}
