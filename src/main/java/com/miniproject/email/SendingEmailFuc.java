package com.miniproject.email;

import javax.mail.internet.MimeMessage;

@FunctionalInterface
public interface SendingEmailFuc {

    void fillMessage(MimeMessage message);
}
