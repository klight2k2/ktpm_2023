package com.ktpm.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Logger;
import static com.ktpm.constants.DBConstants.*;
public class DBConnect {
    private static Connection connect;
    private static Logger LOGGER = Logger.getLogger(DBConnect.class.getName());

    public static Connection getConnection() {
        if (connect != null)
            return connect;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/itss";
            connect = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
            LOGGER.info("Connect database successfully");

        } catch (Exception e) {
            // LOGGER.info(e.getMessage());
            System.out.println(e);
        }
        return connect;
    }

    public static void main(String[] args) {
      
    }
}
