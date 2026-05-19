package com.byzan.automation;

import java.io.File;

public class AutomationMain {

    public static void main(String[] args) {

        try {

            System.out.println("================================");
            System.out.println("AUTOMATION JOB STARTED");
            System.out.println("================================");

            FolderCreator.createFolders();

            DatabaseManager.createTable();

            GmailReader reader = new GmailReader();

            reader.downloadAttachments();

            BackupManager.backupDB("BEFORE_INSERT");

            ExcelProcessor processor = new ExcelProcessor();

            File folder = new File("input");

File[] files = folder.listFiles();
System.out.println("INPUT FOLDER = " + folder.getAbsolutePath());
if(files==null){
System.out.println("NO FILES FOUND");System.out.println("FILES COUNT = " + files.length);

}else{
System.out.println("FILES COUNT = " + files.length);
for(File f:files){
System.out.println("FOUND FILE = "+f.getName());

}
}

            if(files != null) {

                for(File file : files) {

                    if(file.isFile() && file.getName().endsWith(".xlsx")) {

                        if(DuplicateFileChecker.isAlreadyProcessed(file.getName())) {

                            System.out.println("DUPLICATE FILE SKIPPED : " + file.getName());

                            continue;
                        }

                        processor.processExcel(file);

                        DuplicateFileChecker.markProcessed(file.getName());

                        FileMover.moveToProcessed(file);
                    }
                }
            }

            BackupManager.backupDB("AFTER_INSERT");

            OutputGenerator generator = new OutputGenerator();

            File report = generator.generateOutput();

            MailSender sender = new MailSender();

            sender.sendMailWithAttachment(report);

            sender.sendLatestBackup();

            System.out.println("================================");
            System.out.println("AUTOMATION JOB COMPLETED");
            System.out.println("================================");

        } catch(Exception e) {

            e.printStackTrace();
        }
    }
}
