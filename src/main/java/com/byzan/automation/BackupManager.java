package com.byzan.automation;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BackupManager {

    public static void backupDB(String type) {

        try {

            File db = new File("github-actions-db/automation.db");

            if(!db.exists()) {

                return;
            }

            String time = new SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(new Date());

            File backup = new File(
                    "db-backups/" + type + "_" + time + ".db"
            );

            Files.copy(
                    db.toPath(),
                    backup.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            );

            System.out.println("BACKUP CREATED");

        } catch(Exception e) {

            e.printStackTrace();
        }
    }
}
