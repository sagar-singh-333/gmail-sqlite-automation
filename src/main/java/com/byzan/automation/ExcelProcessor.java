package com.byzan.automation;

import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.time.LocalDate;

public class ExcelProcessor {

    public void processExcel(File file) {

        try (

                FileInputStream fis =
                        new FileInputStream(file);

                Workbook workbook =
                        WorkbookFactory.create(fis);

                Connection conn =
                        DatabaseManager.getConnection()

        ) {

            conn.setAutoCommit(false);

            Sheet sheet =
                    workbook.getSheetAt(0);

            String sql =

                    "INSERT INTO EXCEL_DATA("

                            +

                            "FILE_DATE,"

                            +

                            "FILE_NAME,"

                            +

                            "NAME,"

                            +

                            "EMAIL,"

                            +

                            "AMOUNT"

                            +

                            ") VALUES(?,?,?,?,?)";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            int inserted = 0;

            for (

                    int i = 1;

                    i <= sheet.getLastRowNum();

                    i++

            ) {

                Row row =
                        sheet.getRow(i);

                if (row == null) {

                    continue;

                }

                String name =
                        getValue(
                                row.getCell(0)
                        );

                String email =
                        getValue(
                                row.getCell(1)
                        );

                String amount =
                        getValue(
                                row.getCell(2)
                        );

                if (

                        name.isEmpty()

                                &&

                                email.isEmpty()

                                &&

                                amount.isEmpty()

                ) {

                    continue;

                }

                ps.setString(

                        1,

                        LocalDate
                                .now()
                                .toString()

                );

                ps.setString(

                        2,

                        file.getName()

                );

                ps.setString(

                        3,

                        name

                );

                ps.setString(

                        4,

                        email

                );

                ps.setString(

                        5,

                        amount

                );

                ps.addBatch();

                inserted++;

            }

            ps.executeBatch();

            conn.commit();

            System.out.println(

                    "================================"

            );

            System.out.println(

                    "FILE : "
                            +
                            file.getName()

            );

            System.out.println(

                    "DATE : "
                            +
                            LocalDate.now()

            );

            System.out.println(

                    "ROWS INSERTED : "
                            +
                            inserted

            );

            System.out.println(

                    "================================"

            );

        }

        catch (

                Exception e

        ) {

            e.printStackTrace();

        }

    }


    private String getValue(Cell cell) {

        if (

                cell == null

        ) {

            return "";

        }

        DataFormatter formatter =
                new DataFormatter();

        return formatter
                .formatCellValue(
                        cell
                )
                .trim();

    }

}
