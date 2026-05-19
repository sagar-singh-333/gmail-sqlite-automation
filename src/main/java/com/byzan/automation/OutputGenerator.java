package com.byzan.automation;

import java.io.File;
import java.io.FileWriter;

public class OutputGenerator {

    public File generateOutput() {

        try {

            File file = new File("output/report.txt");

            FileWriter writer = new FileWriter(file);

            writer.write("Automation completed successfully");

            writer.close();

            return file;

        } catch(Exception e) {

            e.printStackTrace();
        }

        return null;
    }
}
