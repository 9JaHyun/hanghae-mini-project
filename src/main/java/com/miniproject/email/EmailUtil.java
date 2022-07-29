package com.miniproject.email;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailUtil {

    @Value("${email.username}")
    private String HOST_USERNAME;

    @Value("${email.password}")
    private String HOST_PASSWORD;

    public void sendEmail(String toEmail, SendingEmailStrategy sendingEmailStrategy) {
        log.info("sending email to {}", toEmail);

        String hostSMTP = "smtp.gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.host", hostSMTP);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(HOST_USERNAME, HOST_PASSWORD);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(HOST_USERNAME));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));

            sendingEmailStrategy.fillMessage(message);

            // send the message
            Transport.send(message);

        } catch (MessagingException e) {
            log.info("이메일 발송 실패!");
            e.printStackTrace();
        }
    }
}
