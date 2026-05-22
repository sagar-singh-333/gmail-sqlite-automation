package com.byzan.automation;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseManager {

    private static final String DB =
            "jdbc:sqlite:github-actions-db/automation.db";

    public static Connection getConnection()
            throws Exception {

        Class.forName(
                "org.sqlite.JDBC"
        );

        return DriverManager.getConnection(
                DB
        );

    }

    public static void createTable() {

        try (
                Connection conn =
                        getConnection()
        ) {

            conn.createStatement()
                    .execute(

                            "CREATE TABLE IF NOT EXISTS EXCEL_DATA("

                                    +

                                    "ID INTEGER PRIMARY KEY AUTOINCREMENT,"

                                    +

                                    "FILE_DATE TEXT,"

                                    +

                                    "FILE_NAME TEXT,"

                                    +

                                    "NAME TEXT,"

                                    +

                                    "EMAIL TEXT,"

                                    +

                                    "AMOUNT TEXT,"

                                    +

                                    "INSERT_TIME DATETIME DEFAULT CURRENT_TIMESTAMP"

                                    +

                                    ")"

                    );

            conn.createStatement()
                    .execute(

                            "CREATE TABLE IF NOT EXISTS PROCESSED_FILES("

                                    +

                                    "FILE_NAME TEXT UNIQUE)"

                    );

            System.out.println(
                    "DATE TABLE READY"
            );

        }

        catch (
                Exception e
        ) {

            e.printStackTrace();

        }

    }

}
