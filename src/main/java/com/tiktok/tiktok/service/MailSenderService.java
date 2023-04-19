package com.tiktok.tiktok.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailSenderService {

    protected static final Logger logger = LogManager.getLogger(AbstractService.class);

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String toEmail, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("ittalents.tiktok@gmail.com");
            message.setTo(toEmail);
            message.setText(body);
            message.setSubject(subject);
            mailSender.send(message);
            logger.info("Email sent to " + toEmail + " with subject: " + subject);
        } catch (MailException e) {
            logger.error("Failed to send email to " + toEmail + " with subject: " + subject, e);
            throw new InternalError("Failed to send email. Please try again later.");
        }
    }

}
