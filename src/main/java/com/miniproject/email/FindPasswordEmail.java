package com.miniproject.email;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class FindPasswordEmail implements SendingEmailStrategy {

    private final String certificationCode;
    private final String charSet = "utf-8";
    private final String url;
    private final String email;
    private final String authKey;

    public FindPasswordEmail(String certificationCode, String url, String email, String authKey) {
        this.certificationCode = certificationCode;
        this.url = url;
        this.email = email;
        this.authKey = authKey;
    }

    @Override
    public void fillMessage(MimeMessage message) {
        try {
            message.setSubject("비밀번호 찾기 인증 - 혜림스 포토존", charSet);

            message.setText(new StringBuffer().append("<h1>[이메일 인증]</h1>")
                  .append("<p>아래 링크를 클릭하시면 비밀번호 변경 페이지로 이동합니다.</p>")
                  .append("<a href='" + url + "/user/certification?email=")
                  .append(email)
                  .append("&authKey=")
                  .append(authKey)
                  .append("' target='_blenk'>비밀번호 변경하기</a>")
                  .toString(), charSet, "html");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
