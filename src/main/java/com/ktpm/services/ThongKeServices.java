package com.ktpm.services;

import static com.ktpm.constants.DBConstants.DATABASE;
import static com.ktpm.constants.DBConstants.PASSWORD;
import static com.ktpm.constants.DBConstants.USERNAME;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ThongKeServices {

    public static ResultSet statisticNhanKhau(String query) throws SQLException {
        Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        return preparedStatement.executeQuery();
    }
    
	public ThongKeServices() {
		// TODO Auto-generated constructor stub
	}
	
	public static int SoLuongNhanKhauTamVang()  {
		 Connection conn;
		try {
			conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
			String query="SELECT COUNT(DISTINCT nhankhau.ID) AS SoLuongTamVang FROM nhankhau INNER JOIN tamvang ON nhankhau.ID = tamvang.idNhankhau WHERE CURDATE() BETWEEN tamvang.tuNgay AND tamvang.denNgay;";
			PreparedStatement preparedStatement = conn.prepareStatement(query);
			ResultSet result=preparedStatement.executeQuery();
			while (result.next()) {
				return result.getInt(1);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	    }
	
	public static int SoLuongNhanKhauTuVong()  {
		 Connection conn;
		try {
			conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
			String query="SELECT COUNT(DISTINCT idNguoiChet) AS SoLuongTuVong FROM khaitu;";
			PreparedStatement preparedStatement = conn.prepareStatement(query);
			ResultSet result=preparedStatement.executeQuery();
			while (result.next()) {
				return result.getInt(1);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	    }
	
	public static int SoLuongNhanKhauTamTru()  {
		 Connection conn;
		try {
			conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
			String query="SELECT COUNT(DISTINCT nhankhau.ID) AS SoLuongTamTru FROM nhankhau INNER JOIN tamtru ON nhankhau.ID = tamtru.idNhankhau WHERE CURDATE() BETWEEN tamtru.tuNgay AND tamtru.denNgay;";
			PreparedStatement preparedStatement = conn.prepareStatement(query);
			ResultSet result=preparedStatement.executeQuery();
			while (result.next()) {
				return result.getInt(1);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
}
