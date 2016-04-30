package com.aseproject.askumkc;

/**
 * Created by sravan on 4/27/2016.
 */

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

//import javax.websocket.Session;
        import javax.mail.Authenticator;

public class NotificationMail {

    public NotificationMail() {
        final String SMTP_HOST_NAME = "smtp.gmail.com";
        final String SMTP_AUTH_USER = "vikesh.ls11@gmail.com";
        final String SMTP_AUTH_PWD  = "Vikesh@91";

        //clientWindow cw=new clientWindow();
        Properties props = System.getProperties();
        System.out.println("Started");
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.user", SMTP_AUTH_USER);
        props.put("mail.smtp.password", SMTP_AUTH_PWD);
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.starttls.enable", "true");


        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator()
                {
                    protected PasswordAuthentication getPasswordAuthentication()
                    { return new PasswordAuthentication(SMTP_AUTH_USER,SMTP_AUTH_PWD);	}
                });

        try {

            Message message = new MimeMessage(session);
            MimeBodyPart sigAttachment = new MimeBodyPart();
            //textField_1
            message.setFrom(new InternetAddress("vikesh.ls11@gmail.com"));
            // message.setFrom(new InternetAddress(textField_1.getText()));
            //textField_2
            //message.setRecipients(Message.RecipientType.TO,
            //InternetAddress.parse(textField_2.getText()));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("vikesh.ls11@gmail.com"));
            //textField_3
            message.setSubject("Answer Replied");
            //textArea
            //message.setText("Mail recieved from java application built by "+ SMTP_AUTH_USER);

            Transport.send(message);

            System.out.println("YIPPEEEE..!!!");

        } catch (MessagingException e) {

        }
    }

}
