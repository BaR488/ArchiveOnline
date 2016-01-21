/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.utilsproject;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;

/**
 *
 * @author boris
 */
public class MailSender {

    public static void send(String to, String messageText) {
        try {
            Email email = new SimpleEmail();
            email.setHostName("smtp.yandex.ru");
            email.setSmtpPort(465);
            email.setAuthenticator(new DefaultAuthenticator("root@karabanovbs.ddns.net", "rootpass"));
            email.setSSLOnConnect(true);
            email.setFrom("root@karabanovbs.ddns.net");
            email.setSubject("File link");
            email.setMsg(messageText);
            email.addTo(to);
            email.send();
            System.out.println("SUCCESS");
        } catch (Exception e) {
            System.err.println(e);
        }

    }
}
