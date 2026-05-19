package com.byzan.automation;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FileMover {

    public static void moveToProcessed(File file) {

        try {

            File target = new File("processed/" + file.getName());

            Files.move(
                    file.toPath(),
                    target.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            );

            System.out.println("FILE MOVED : " + file.getName());

        } catch(Exception e) {

            e.printStackTrace();
        }
    }
}
