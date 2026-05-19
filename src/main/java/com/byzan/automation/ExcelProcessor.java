package com.byzan.automation;

import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ExcelProcessor {

    public void processExcel(File file) {

        try(FileInputStream fis = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(fis);
            Connection conn = DatabaseManager.getConnection()) {

            Sheet sheet = workbook.getSheetAt(0);

            for(int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if(row == null) {
                    continue;
                }

                String name = row.getCell(0).toString();
                String email = row.getCell(1).toString();
                String amount = row.getCell(2).toString();

                String sql = "INSERT INTO EXCEL_DATA(NAME,EMAIL,AMOUNT) VALUES(?,?,?)";

                PreparedStatement ps = conn.prepareStatement(sql);

                ps.setString(1, name);
                ps.setString(2, email);
                ps.setString(3, amount);

                ps.executeUpdate();
            }

            System.out.println("EXCEL PROCESSED : " + file.getName());

        } catch(Exception e) {

            e.printStackTrace();
        }
    }
}
