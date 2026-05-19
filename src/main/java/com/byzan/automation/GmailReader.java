package com.byzan.automation;

import jakarta.mail.*;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.search.FlagTerm;

import java.io.File;
import java.util.Properties;

public class GmailReader {

    public void downloadAttachments() {

        try {

            Properties props = new Properties();

            props.put("mail.store.protocol", "imaps");

            Session session = Session.getDefaultInstance(props);

            Store store = session.getStore("imaps");

            store.connect(
                    "imap.gmail.com",
                    System.getenv("MAIL_USERNAME"),
                    System.getenv("MAIL_PASSWORD")
            );

            Folder inbox = store.getFolder("INBOX");

            inbox.open(Folder.READ_WRITE);

            Message[] messages = inbox.search(
                    new FlagTerm(new Flags(Flags.Flag.SEEN), false)
            );

            for(Message message : messages) {

                Multipart multipart = (Multipart) message.getContent();

                for(int i = 0; i < multipart.getCount(); i++) {

                    BodyPart bodyPart = multipart.getBodyPart(i);

                    if(Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {

                        MimeBodyPart part = (MimeBodyPart) bodyPart;

                        String fileName = part.getFileName();

                        if(fileName.endsWith(".xlsx")) {

                            File file = new File("input/" + fileName);

                            part.saveFile(file);

                            System.out.println("DOWNLOADED : " + fileName);
                        }
                    }
                }

                message.setFlag(Flags.Flag.SEEN, true);
            }

            inbox.close(true);

            store.close();

        } catch(Exception e) {

            e.printStackTrace();
        }
    }
}
