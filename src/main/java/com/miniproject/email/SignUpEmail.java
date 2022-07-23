package com.miniproject.email;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class SignUpEmail implements SendingEmailFuc{

    private final String certificationCode;
    private final String charSet = "utf-8";

    public SignUpEmail(String certificationCode) {
        this.certificationCode = certificationCode;
    }

    public void fillMessage(MimeMessage message) {
        try {
            message.setSubject("가입 인증 메일 - 혜림스 포토존", charSet);

            message.setText("<!DOCTYPE html>\n"
                  + "<html lang=\"UTF-8\">\n"
                  + "<head>\n"
                  + "    <meta charset=\"UTF-8\">\n"
                  + "    <title></title>\n"
                  + "</head>\n"
                  + "<body>\n"
                  + "<h1>가입을 환영합니다!!!</h1>\n"
                  + "인증코드: " + certificationCode
                  + "</body>\n"
                  + "</html>", charSet, "html");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
