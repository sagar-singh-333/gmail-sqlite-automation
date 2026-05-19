package com.byzan.automation;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.search.*;

import java.io.File;
import java.util.Properties;

public class GmailReader {

    private static final String INPUT_FOLDER = "input";

    // CHANGE THESE
    private static final String ALLOWED_SENDER =
            "sagarsinghsago@gmail.com";

    private static final String SUBJECT_KEYWORD =
            "test";

    public void downloadAttachments() {

        Store store = null;

        Folder inbox = null;

        try {

            System.out.println("CONNECTING TO GMAIL");

            Properties props = new Properties();

            props.put(
                    "mail.store.protocol",
                    "imaps"
            );

            Session session =
                    Session.getInstance(props);

            store =
                    session.getStore("imaps");

            store.connect(
                    "imap.gmail.com",
                    System.getenv("MAIL_USERNAME"),
                    System.getenv("MAIL_PASSWORD")
            );

            inbox =
                    store.getFolder("INBOX");

            inbox.open(
                    Folder.READ_WRITE
            );

            SearchTerm unread =
                    new FlagTerm(
                            new Flags(
                                    Flags.Flag.SEEN
                            ),
                            false
                    );

            Message[] messages =
                    inbox.search(unread);

            System.out.println(
                    "UNREAD MAILS="
                            + messages.length
            );

            new File(
                    INPUT_FOLDER
            ).mkdirs();

            for (
                    Message msg
                            : messages
            ) {

                try {

                    String subject =
                            msg.getSubject();

                    String sender =
                            ((InternetAddress)
                                    msg.getFrom()[0])
                                    .getAddress();

                    System.out.println(
                            "CHECKING="
                                    + subject
                    );

                    // FILTER SENDER

                    if (
                            !sender
                                    .equalsIgnoreCase(
                                            ALLOWED_SENDER
                                    )
                    ) {

                        continue;
                    }

                    // FILTER SUBJECT

                    if (
                            subject == null
                                    ||
                                    !subject.contains(
                                            SUBJECT_KEYWORD
                                    )
                    ) {

                        continue;
                    }

                    Object content =
                            msg.getContent();

                    if (
                            !(content
                                    instanceof Multipart)
                    ) {

                        continue;
                    }

                    Multipart multipart =
                            (Multipart)
                                    content;

                    for (
                            int i = 0;
                            i < multipart.getCount();
                            i++
                    ) {

                        BodyPart body =
                                multipart
                                        .getBodyPart(
                                                i
                                        );

                        if (
                                Part.ATTACHMENT
                                        .equalsIgnoreCase(
                                                body
                                                        .getDisposition()
                                        )
                        ) {

                            MimeBodyPart part =
                                    (MimeBodyPart)
                                            body;

                            String file =
                                    part
                                            .getFileName();

                            System.out.println(
                                    "ATTACHMENT="
                                            + file
                            );

                            if (
                                    file
                                            .toLowerCase()
                                            .endsWith(
                                                    ".xlsx"
                                            )
                            ) {

                                File save =
                                        new File(
                                                INPUT_FOLDER
                                                        +
                                                        "/"
                                                        +
                                                        file
                                        );

                                part.saveFile(
                                        save
                                );

                                System.out.println(
                                        "DOWNLOADED="
                                                +
                                                file
                                );
                            }
                        }
                    }

                    msg.setFlag(
                            Flags.Flag.SEEN,
                            true
                    );

                    System.out.println(
                            "MAIL PROCESSED"
                    );

                }

                catch (
                        Exception e
                ) {

                    e.printStackTrace();
                }

            }

            inbox.close(
                    true
            );

            store.close();

        }

        catch (
                Exception e
        ) {

            e.printStackTrace();
        }

    }

}
