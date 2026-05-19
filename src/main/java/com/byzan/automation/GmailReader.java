package com.byzan.automation;

import jakarta.mail.BodyPart;
import jakarta.mail.Flags;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.Part;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.search.FlagTerm;

import java.io.File;
import java.util.Properties;

public class GmailReader {

    private static final String INPUT_FOLDER = "input";

    public void downloadAttachments() {

        Store store = null;
        Folder inbox = null;

        try {

            System.out.println("================================");
            System.out.println("CONNECTING TO GMAIL");
            System.out.println("================================");

            String username = System.getenv("MAIL_USERNAME");
            String password = System.getenv("MAIL_PASSWORD");

            if (username == null || password == null) {

                throw new RuntimeException(
                        "MAIL_USERNAME / MAIL_PASSWORD missing"
                );
            }

            Properties props = new Properties();

            props.put(
                    "mail.store.protocol",
                    "imaps"
            );

            props.put(
                    "mail.imap.ssl.enable",
                    "true"
            );

            props.put(
                    "mail.imap.host",
                    "imap.gmail.com"
            );

            props.put(
                    "mail.imap.port",
                    "993"
            );

            Session session =
                    Session.getInstance(props);

            store =
                    session.getStore("imaps");

            store.connect(
                    "imap.gmail.com",
                    username,
                    password
            );

            System.out.println(
                    "GMAIL LOGIN SUCCESS"
            );

            inbox =
                    store.getFolder("INBOX");

            inbox.open(Folder.READ_WRITE);

            Message[] messages =
                    inbox.search(
                            new FlagTerm(
                                    new Flags(
                                            Flags.Flag.SEEN
                                    ),
                                    false
                            )
                    );

            System.out.println(
                    "MESSAGES FOUND = "
                            + messages.length
            );

            File inputDir =
                    new File(INPUT_FOLDER);

            if (!inputDir.exists()) {

                inputDir.mkdirs();

                System.out.println(
                        "INPUT FOLDER CREATED"
                );
            }

            for (Message message : messages) {

                try {

                    System.out.println(
                            "SUBJECT = "
                                    + message.getSubject()
                    );

                    Object content =
                            message.getContent();

                    if (!(content instanceof Multipart)) {

                        System.out.println(
                                "NO ATTACHMENT"
                        );

                        continue;
                    }

                    Multipart multipart =
                            (Multipart) content;

                    for (
                            int i = 0;
                            i < multipart.getCount();
                            i++
                    ) {

                        BodyPart bodyPart =
                                multipart.getBodyPart(i);

                        if (
                                bodyPart.getDisposition()
                                        == null
                        ) {

                            continue;
                        }

                        if (
                                Part.ATTACHMENT
                                        .equalsIgnoreCase(
                                                bodyPart
                                                        .getDisposition()
                                        )
                        ) {

                            MimeBodyPart part =
                                    (MimeBodyPart)
                                            bodyPart;

                            String fileName =
                                    part.getFileName();

                            System.out.println(
                                    "ATTACHMENT = "
                                            + fileName
                            );

                            if (
                                    fileName == null
                            ) {

                                continue;
                            }

                            if (
                                    !fileName
                                            .toLowerCase()
                                            .endsWith(".xlsx")
                            ) {

                                System.out.println(
                                        "SKIPPED (NOT XLSX)"
                                );

                                continue;
                            }

                            File saveFile =
                                    new File(
                                            INPUT_FOLDER
                                                    + File.separator
                                                    + fileName
                                    );

                            part.saveFile(
                                    saveFile
                            );

                            System.out.println(
                                    "DOWNLOADED = "
                                            + saveFile
                                                    .getAbsolutePath()
                            );
                        }
                    }

                    message.setFlag(
                            Flags.Flag.SEEN,
                            true
                    );

                    System.out.println(
                            "MAIL MARKED READ"
                    );

                } catch (Exception ex) {

                    System.out.println(
                            "MAIL FAILED"
                    );

                    ex.printStackTrace();
                }
            }

            System.out.println(
                    "DOWNLOAD PROCESS FINISHED"
            );

        } catch (Exception e) {

            System.out.println(
                    "GMAIL CONNECTION FAILED"
            );

            e.printStackTrace();

        } finally {

            try {

                if (
                        inbox != null
                                && inbox.isOpen()
                ) {

                    inbox.close(true);
                }

            } catch (Exception ignored) {
            }

            try {

                if (
                        store != null
                                && store.isConnected()
                ) {

                    store.close();
                }

            } catch (Exception ignored) {
            }
        }
    }
}
