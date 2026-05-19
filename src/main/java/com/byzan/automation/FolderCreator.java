package com.byzan.automation;

import java.io.File;

public class FolderCreator {

    public static void createFolders() {

        new File("input").mkdirs();
        new File("output").mkdirs();
        new File("processed").mkdirs();
        new File("failed").mkdirs();
        new File("logs").mkdirs();
        new File("db-backups").mkdirs();
        new File("github-actions-db").mkdirs();

        System.out.println("ALL FOLDERS READY");
    }
}
