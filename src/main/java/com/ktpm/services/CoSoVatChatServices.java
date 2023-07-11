package com.ktpm.services;

import static com.ktpm.constants.DBConstants.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ktpm.model.CoSoVatChat;
import com.ktpm.model.*;
public class CoSoVatChatServices {
    public static ResultSet getAllFacility(Connection conn) throws SQLException {
        String SELECT_QUERY = "SELECT MaDoDung, TenDoDung, LoaiDoDung, TinhTrang  FROM `cosovatchat`";
        PreparedStatement preparedStatement = conn.prepareStatement(SELECT_QUERY);
        return preparedStatement.executeQuery();
    }

    public static int deleteFacility(Connection conn, CoSoVatChat selected) throws SQLException {
        String DELETE_QUERY = "DELETE FROM cosovatchat WHERE `MaDoDung`= ?";
        PreparedStatement preparedStatement = conn.prepareStatement(DELETE_QUERY);
        preparedStatement.setString(1, String.valueOf(selected.getMaDoDung()));
        return preparedStatement.executeUpdate();
    }

    public static int addFacility(Connection conn, String tenDoDung, String loaiDoDung, String tinhTrang) throws SQLException {
    	
    	String INSERT_QUERY = "INSERT INTO cosovatchat(TenDoDung, LoaiDoDung, TinhTrang) VALUES(?, ?, ?)";
        PreparedStatement preparedStatement = conn.prepareStatement((INSERT_QUERY));
        preparedStatement.setString(1, tenDoDung);
        preparedStatement.setString(2, loaiDoDung);
        preparedStatement.setString(3, tinhTrang);
        return preparedStatement.executeUpdate();
    }

    public static int updateFacility(Connection conn, int maDoDung, String tenDoDung, String tinhTrang, String loaiDoDung) throws SQLException {
        String UPDATE_QUERY = "UPDATE cosovatchat SET `TenDoDung`=?, `TinhTrang`=?, `LoaiDoDung`=? WHERE `MaDoDung`=?";
        PreparedStatement preparedStatement = conn.prepareStatement((UPDATE_QUERY));
        preparedStatement.setString(1, tenDoDung);
        preparedStatement.setString(2, tinhTrang);
        preparedStatement.setString(3, loaiDoDung);
        preparedStatement.setString(4, Integer.toString(maDoDung));
        return preparedStatement.executeUpdate();
    }

    public static List<LoaiCoSoVatChat> getStatisticCSVC() {
    	List<LoaiCoSoVatChat>result = new ArrayList<LoaiCoSoVatChat>();
        String GET_QUERY = "SELECT LoaiDoDung, TinhTrang, COUNT(*) AS SoLuong FROM cosovatchat GROUP BY LoaiDoDung, TinhTrang;";
        try {
            Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
            PreparedStatement preparedStatement = conn.prepareStatement(GET_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.add(new LoaiCoSoVatChat(resultSet.getString(1), resultSet.getString(2), resultSet.getInt(3)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}

