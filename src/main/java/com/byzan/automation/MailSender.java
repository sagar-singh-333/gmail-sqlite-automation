package com.byzan.automation;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Properties;

public class MailSender {

    public void sendMailWithAttachment(File file) {

        try {

            Properties props = new Properties();

            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(
                    props,
                    new Authenticator() {

                        protected PasswordAuthentication getPasswordAuthentication() {

                            return new PasswordAuthentication(
                                    System.getenv("MAIL_USERNAME"),
                                    System.getenv("MAIL_PASSWORD")
                            );
                        }
                    });

            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(System.getenv("MAIL_USERNAME")));

            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(System.getenv("MAIL_USERNAME"))
            );

            message.setSubject("Automation Output");

            MimeBodyPart body = new MimeBodyPart();

            body.setText("Attached automation file");

            MimeBodyPart attachment = new MimeBodyPart();

            attachment.attachFile(file);

            Multipart multipart = new MimeMultipart();

            multipart.addBodyPart(body);

            multipart.addBodyPart(attachment);

            message.setContent(multipart);

            Transport.send(message);

            System.out.println("EMAIL SENT : " + file.getName());

        } catch(Exception e) {

            e.printStackTrace();
        }
    }

    public void sendLatestBackup() {

        try {

            File folder = new File("db-backups");

            File[] backups = folder.listFiles();

            if(backups == null || backups.length == 0) {

                return;
            }

            Arrays.sort(backups, Comparator.comparingLong(File::lastModified).reversed());

            sendMailWithAttachment(backups[0]);

        } catch(Exception e) {

            e.printStackTrace();
        }
    }
}
