package com.byzan.automation;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseManager {

    private static final String URL =
            "jdbc:sqlite:github-actions-db/automation.db";

    public static Connection getConnection()
            throws Exception {

        Class.forName(
                "org.sqlite.JDBC"
        );

        return DriverManager.getConnection(
                URL
        );
    }

    public static void createTable() {

        try (
                Connection conn =
                        getConnection()
        ) {

            String sql1 =

                    "CREATE TABLE IF NOT EXISTS EXCEL_DATA ("

                            +

                            "ID INTEGER PRIMARY KEY AUTOINCREMENT,"

                            +

                            "NAME TEXT,"

                            +

                            "EMAIL TEXT,"

                            +

                            "AMOUNT TEXT"

                            +

                            ")";

            String sql2 =

                    "CREATE TABLE IF NOT EXISTS PROCESSED_FILES("

                            +

                            "ID INTEGER PRIMARY KEY AUTOINCREMENT,"

                            +

                            "FILE_NAME TEXT UNIQUE"

                            +

                            ")";

            conn.createStatement()
                    .execute(
                            sql1
                    );

            conn.createStatement()
                    .execute(
                            sql2
                    );

            System.out.println(
                    "DATABASE READY"
            );

        }

        catch (
                Exception e
        ) {

            e.printStackTrace();
        }

    }

}
