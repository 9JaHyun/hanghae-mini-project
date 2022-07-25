package com.miniproject.email;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class SignUpEmail implements SendingEmailFuc{

    private final String certificationCode;
    private final String charSet = "utf-8";
    private final String url;
    private final String email;
    private final String authKey;

    public SignUpEmail(String certificationCode, String url, String email, String authKey) {
        this.certificationCode = certificationCode;
        this.url = url;
        this.email = email;
        this.authKey = authKey;
    }

    @Override
    public void fillMessage(MimeMessage message) {
        try {
            message.setSubject("회원가입 이메일 인증 - 혜림스 포토존", charSet);

            message.setText(new StringBuffer().append("<h1>[이메일 인증]</h1>")
                  .append("<p>아래 링크를 클릭하시면 이메일 인증이 완료됩니다.</p>")
                  .append("<a href='" + url + "/user/certification?email=")
                  .append(email)
                  .append("&authKey=")
                  .append(authKey)
                  .append("' target='_blenk'>이메일 인증 확인</a>")
                  .toString(), charSet, "html");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
