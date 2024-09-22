package com.ims.connection;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author _Spoidey-
 */
public class DBConnection {

    static Connection con = null;
    static String url = "jdbc:mysql://localhost:3306/ims";

    public static Connection getConnection() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, "root", "");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return con;
    }

}
