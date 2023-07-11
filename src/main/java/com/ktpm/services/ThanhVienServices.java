package com.ktpm.services;

import static com.ktpm.constants.DBConstants.DATABASE;
import static com.ktpm.constants.DBConstants.PASSWORD;
import static com.ktpm.constants.DBConstants.USERNAME;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ktpm.model.ThanhVien;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ThanhVienServices {

	public ThanhVienServices() {
		// TODO Auto-generated constructor stub
	}
	
	public static int getIDHoKhauViaIDNhanKhau(int IDNhanKhau) throws SQLException {
		Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
		String query = "SELECT idhokhau FROM thanhviencuaho WHERE idnhankhau = ?";
		PreparedStatement preparedStatement = conn.prepareStatement(query);
		preparedStatement.setInt(1, IDNhanKhau);
		ResultSet rs = preparedStatement.executeQuery();
		rs.next();
		return rs.getInt("idhoKhau");
	}
	
	public static ObservableList<String> getThanhVienViaIDHoKhau(int IDhokhau) throws SQLException {
		ObservableList<String> lstNhanKhau = FXCollections.observableArrayList();
		Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
		String query = "SELECT * FROM thanhviencuaho JOIN nhankhau ON thanhviencuaho.idNhankhau = nhankhau.id WHERE idhokhau = ?";
		PreparedStatement preparedStatement = conn.prepareStatement(query);
		preparedStatement.setInt(1, IDhokhau);
		ResultSet rs = preparedStatement.executeQuery();
		while(rs.next()) {
			lstNhanKhau.add(rs.getString("hoTen"));
		}
		return lstNhanKhau;
	}
	
	public static void xoaCacThanhVienKhoiHoKhauCu(ObservableList<ThanhVien> thanhVienMoi, int maChuHoMoi) throws SQLException {
		Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
		String query = "DELETE FROM thanhviencuaho WHERE idNhanKhau = ?";
		PreparedStatement preparedStatement = conn.prepareStatement(query);
		preparedStatement.setInt(1, maChuHoMoi);
		preparedStatement.executeUpdate();
		for (ThanhVien t: thanhVienMoi) {
			preparedStatement.setInt(1, NhanKhauServices.findIDNhanKhauViaTen(t.getHoTen()));
			int rs = preparedStatement.executeUpdate();
    	}	
	}
	
	public static void themCacThanhVienVaoHoKhauMoi(ObservableList<ThanhVien> thanhVienMoi, int maChuHoMoi) throws SQLException {
		int IDHoKhauMoi = SoHoKhauServices.getMaSoHoKhauViaMaChuHo(maChuHoMoi);
		Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
		String query = "INSERT INTO thanhviencuaho (idNhanKhau, idHoKhau, quanHeVoiChuHo) VALUES (?, ?, ?)";
		PreparedStatement preparedStatement = conn.prepareStatement(query);
		for (ThanhVien t: thanhVienMoi) {
			preparedStatement.setInt(1, NhanKhauServices.findIDNhanKhauViaTen(t.getHoTen()));
			preparedStatement.setInt(2, IDHoKhauMoi);
			preparedStatement.setString(3, t.getQuanHeVoiChuHo());
			int rs = preparedStatement.executeUpdate();
    	}	
	}

}
