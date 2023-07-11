package com.ktpm.services;

import static com.ktpm.constants.DBConstants.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.ktpm.model.NhanKhau;
import com.ktpm.model.SoHoKhau;
import com.ktpm.model.ThanhVien;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SoHoKhauServices {
    public static ResultSet getSoHoKhauViaMaHoKhau(Connection conn, SoHoKhau soHoKhau) throws SQLException {
        String query = "SELECT * FROM sohokhau, nhankhau, cccd where sohokhau.MaChuHo = nhankhau.ID and nhankhau.id = cccd.idNhankhau and sohokhau.MaHoKhau = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, soHoKhau.getMaHoKhau());
        return preparedStatement.executeQuery();
    }
    
    public static int getMaSoHoKhauViaMaChuHo(int maChuHo) throws SQLException {
    	Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
        String query = "SELECT * FROM sohokhau WHERE MaChuHo = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setInt(1, maChuHo);
        ResultSet rs = preparedStatement.executeQuery();
        rs.next();
        return rs.getInt("ID");
    }
    
    public static String getChuHoViaIDHoKhau(int IDhoKhau) throws SQLException {
    	Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
        String query = "SELECT * FROM sohokhau JOIN nhankhau ON sohokhau.machuho = nhankhau.id WHERE sohokhau.ID = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setInt(1, IDhoKhau);
        ResultSet rs = preparedStatement.executeQuery();
        rs.next();
        return rs.getString("Hoten");
    }
    

    public static ObservableList<String> getThanhVienGiaDinh(String maHoKhau) throws SQLException {
    	ObservableList<String> lstThanhVien = FXCollections.observableArrayList();
    	Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
    	String query = "SELECT `hoTen` FROM `thanhviencuaho` JOIN `nhankhau` ON `thanhviencuaho`.`idNhanKhau`= `nhankhau`.`ID` \r\n"
    			+ "JOIN `sohokhau` ON `thanhviencuaho`.`idHoKhau` = `sohokhau`.`ID` WHERE `maHoKhau` = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, maHoKhau);
        ResultSet rs =  preparedStatement.executeQuery();
    	while(rs.next()) {
    		lstThanhVien.add(rs.getString("hoTen"));
    		System.out.println(lstThanhVien.get(0));
    	}
    	return lstThanhVien;
    }
    
    public static ObservableList<ThanhVien> getObjThanhVienGiaDinh(String maHoKhau) throws SQLException {
    	ObservableList<ThanhVien> lstThanhVien = FXCollections.observableArrayList();
    	Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
    	String query = "SELECT `hoTen`, `quanHeVoiChuHo` FROM `thanhviencuaho` JOIN `nhankhau` ON `thanhviencuaho`.`idNhanKhau`= `nhankhau`.`ID` \r\n"
    			+ "JOIN `sohokhau` ON `thanhviencuaho`.`idHoKhau` = `sohokhau`.`ID` WHERE `maHoKhau` = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, maHoKhau);
        ResultSet rs =  preparedStatement.executeQuery();
    	while(rs.next()) {
    		lstThanhVien.add(new ThanhVien(rs.getString("hoTen"), rs.getString("quanHeVoiChuHo")));
    	}
    	return lstThanhVien;
    }
    
    public static ObservableList<ThanhVien> getObjThanhVienGiaDinhExcept(String maHoKhau, String tenChuHoMoi) throws SQLException {
    	ObservableList<ThanhVien> lstThanhVien = FXCollections.observableArrayList();
    	Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
    	String query = "SELECT `hoTen`, `quanHeVoiChuHo` FROM `thanhviencuaho` JOIN `nhankhau` ON `thanhviencuaho`.`idNhanKhau`= `nhankhau`.`ID` \r\n"
    			+ "JOIN `sohokhau` ON `thanhviencuaho`.`idHoKhau` = `sohokhau`.`ID` WHERE `maHoKhau` = ? AND `hoTen` != ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, maHoKhau);
        preparedStatement.setString(2, tenChuHoMoi);
        ResultSet rs =  preparedStatement.executeQuery();
    	while(rs.next()) {
    		lstThanhVien.add(new ThanhVien(rs.getString("hoTen"), rs.getString("quanHeVoiChuHo")));
    	}
    	return lstThanhVien;
    }
    
    public static ObservableList<ThanhVien> getObjThanhVienGiaDinhInclude(String maHoKhau, String tenChuHoMoi) throws SQLException {
    	ObservableList<ThanhVien> lstThanhVien = FXCollections.observableArrayList();
    	Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
    	String query = "SELECT `hoTen`, `quanHeVoiChuHo` FROM `thanhviencuaho` JOIN `nhankhau` ON `thanhviencuaho`.`idNhanKhau`= `nhankhau`.`ID` \r\n"
    			+ "JOIN `sohokhau` ON `thanhviencuaho`.`idHoKhau` = `sohokhau`.`ID` WHERE `maHoKhau` = ? AND `hoTen` = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, maHoKhau);
        preparedStatement.setString(2, tenChuHoMoi);
        ResultSet rs =  preparedStatement.executeQuery();
    	while(rs.next()) {
    		lstThanhVien.add(new ThanhVien(rs.getString("hoTen"), rs.getString("quanHeVoiChuHo")));
    	}
    	return lstThanhVien;
    }
    
    public static ResultSet getAllSoHoKhau(Connection conn) throws SQLException {
        String SELECT_QUERY = "select nhankhau.HoTen,sohokhau.DiaChi,sohokhau.MaHoKhau, count(thanhviencuaho.idNhanKhau)+1 as 'SoLuong' from sohokhau\n" +
                "left join thanhviencuaho on thanhviencuaho.idHoKhau = sohokhau.ID\n" +
                "inner join nhankhau on sohokhau.MaChuHo = nhankhau.ID\n" +
                "group by nhankhau.HoTen,sohokhau.DiaChi,sohokhau.MaHoKhau;";
        PreparedStatement preparedStatement = conn.prepareStatement(SELECT_QUERY);
        return preparedStatement.executeQuery();
    }

    public static ResultSet getThanhVienCuaHo(Connection conn, SoHoKhau soHoKhau) throws SQLException {
        String query = "select nhankhau.HoTen, cccd.CCCD, thanhviencuaho.quanHeVoiChuHo from thanhviencuaho, nhankhau, sohokhau, cccd\n" +
                "where thanhviencuaho.idNhanKhau = nhankhau.ID and thanhviencuaho.idHoKhau = sohokhau.ID and cccd.idNhankhau = nhankhau.ID\n" +
                "and sohokhau.MaHoKhau = ?;";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, soHoKhau.getMaHoKhau());
        return preparedStatement.executeQuery();
    }

    public static int deleteSoHoKhau(Connection conn, SoHoKhau selected) throws SQLException {
        String DELETE_QUERY = "DELETE FROM sohokhau WHERE MaHoKhau = ?";
        conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
        PreparedStatement preparedStatement = conn.prepareStatement(DELETE_QUERY);
        preparedStatement.setString(1, selected.getMaHoKhau());
        return preparedStatement.executeUpdate();
    }

    public static ResultSet getChuHo(Connection conn) throws SQLException {
        String SELECT_QUERY = "SELECT nhankhau.*, cccd.CCCD\n" +
                "FROM nhankhau\n" +
                "JOIN cccd\n" +
                "ON nhankhau.ID = cccd.idNhankhau\n" +
                "WHERE nhankhau.ID not in (SELECT idNhanKhau FROM thanhviencuaho) " +
                "and nhankhau.ID not in (SELECT MaChuHo FROM sohokhau)";
        PreparedStatement preparedStatement = conn.prepareStatement(SELECT_QUERY);
        return preparedStatement.executeQuery();
    }

    public static PreparedStatement addSoHoKhau(String maHoKhau, String diaChi, NhanKhau selected) throws SQLException {
        String INSERT_QUERY = "INSERT INTO `sohokhau`(`MaHoKhau`, `DiaChi`, `MaChuHo`,`NgayLap`) VALUES (?,?,?,?)";
        Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
        PreparedStatement preparedStatement = conn.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, maHoKhau);
        preparedStatement.setString(2, diaChi);
        preparedStatement.setInt(3, selected.getID());
        preparedStatement.setString(4, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        return preparedStatement;
    }
    
    public static int addSoHoKhauKhiTach(String maHoKhau, String diaChi, int maChuHo) throws SQLException {
        String INSERT_QUERY = "INSERT INTO `sohokhau`(`MaHoKhau`, `DiaChi`, `MaChuHo`,`NgayLap`) VALUES (?,?,?,?)";
        Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
        PreparedStatement preparedStatement = conn.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, maHoKhau);
        preparedStatement.setString(2, diaChi);
        preparedStatement.setInt(3, maChuHo);
        preparedStatement.setString(4, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        return preparedStatement.executeUpdate();
    }

    public static int updateChuHo(Connection conn, int idHoKhau, NhanKhau selected) throws SQLException {
        String UPDATE_QUERY = "UPDATE sohokhau SET MaChuHo = ? WHERE ID = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_QUERY);
        preparedStatement.setInt(1, selected.getID());
        preparedStatement.setInt(2, idHoKhau);
        return preparedStatement.executeUpdate();
    }

    public static int update(Connection conn, int idHoKhau, String maHoKhau, String diaChi) throws SQLException {
        String UPDATE_QUERY = "UPDATE sohokhau SET sohokhau.MaHoKhau = ?, sohokhau.DiaChi = ? WHERE ID = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_QUERY);
        preparedStatement.setString(1, maHoKhau);
        preparedStatement.setString(2, diaChi);
        preparedStatement.setInt(3, idHoKhau);
        return preparedStatement.executeUpdate();
    }
    public static int deleteThanhVienInSoHoKhau(Connection conn, ThanhVien selected) throws SQLException {
        String DELETE_QUERY = "DELETE thanhviencuaho\n" +
                "FROM thanhviencuaho\n" +
                "JOIN nhankhau\n" +
                "ON thanhviencuaho.idNhanKhau = nhankhau.ID\n" +
                "JOIN cccd\n" +
                "ON nhankhau.ID = cccd.idNhankhau\n" +
                "WHERE cccd.CCCD = ?\n";
        PreparedStatement preparedStatement = conn.prepareStatement(DELETE_QUERY);
        preparedStatement.setString(1, selected.getCCCD());
        return preparedStatement.executeUpdate();
    }

    public static int updateThanhVienInSoHoKhau(Connection conn, String quanHe, ThanhVien selected) throws SQLException {
        String UPDATE_QUERY = "UPDATE thanhviencuaho\n" +
                "SET quanHeVoiChuHo = ?\n" +
                "WHERE idNhanKhau in (SELECT nhankhau.ID FROM nhankhau JOIN cccd ON nhankhau.ID = cccd.idNhankhau WHERE cccd.CCCD = ?)\n";
        PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_QUERY);
        preparedStatement.setString(1, quanHe);
        preparedStatement.setString(2, selected.getCCCD());
        return preparedStatement.executeUpdate();
    }

    public static int getTotalSoHoKhau() {
        int total = 0;
        String GET_QUERY = "SELECT COUNT(*) FROM sohokhau";
        try {
            Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
            PreparedStatement preparedStatement = conn.prepareStatement(GET_QUERY);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                total = result.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return total;
    }
}
