/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 *
 * @author boris
 */
public class MailSender {

    public static void send(String to, String messageText) throws EmailException {
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

    }
}
