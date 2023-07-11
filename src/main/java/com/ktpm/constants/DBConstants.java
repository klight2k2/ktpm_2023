package com.ktpm.constants;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DBConstants {
    public DBConstants() {}

    public static final String DATABASE = "jdbc:mysql://localhost:3306/quan_ly_to";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "";

    public static final int ROWS_PER_PAGE = 10;
    
    public static ObservableList<String> lstLoaiDoDung = FXCollections.observableArrayList();
    
    
}
