package com.byzan.automation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DuplicateFileChecker {

    public static boolean isAlreadyProcessed(String fileName) {

        try(Connection conn = DatabaseManager.getConnection()) {

            String sql = "SELECT COUNT(*) FROM PROCESSED_FILES WHERE FILE_NAME=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, fileName);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {

                return rs.getInt(1) > 0;
            }

        } catch(Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    public static void markProcessed(String fileName) {

        try(Connection conn = DatabaseManager.getConnection()) {

            String sql = "INSERT INTO PROCESSED_FILES(FILE_NAME) VALUES(?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, fileName);

            ps.executeUpdate();

        } catch(Exception e) {

            e.printStackTrace();
        }
    }
}
