package com.ktpm.controller.cosovatchat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.input.*;

import static com.ktpm.constants.DBConstants.*;
import static com.ktpm.utils.Utils.createDialog;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.concurrent.ThreadLocalRandom;

import com.ktpm.model.CoSoVatChat;
import com.ktpm.services.CoSoVatChatServices;
import com.ktpm.utils.ViewUtils;

import java.util.ResourceBundle;


public class CoSoVatChatDeTailController implements Initializable {
    @FXML
    private ComboBox<String> loaiDoDungLabel;

    @FXML
    private TextField tenDoDungLabel;

    @FXML
    private ComboBox<String> tinhTrangLabel;
    
    private int maDoDungHienTai;

    @FXML
    private Button add_btn;

    @FXML
    private Button update_btn;

    @FXML
    private Text title;

    @FXML
    private Pane maDoDungPane, soLuongKhaDungPane;


    public void setCoSoVatChat(CoSoVatChat coSoVatChat) throws SQLException {
    	ObservableList<String> lstTenLoaiDoDung = FXCollections.observableArrayList();
    	ObservableList<String> lstTinhTrang = FXCollections.observableArrayList();
    	Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
    	PreparedStatement check = conn.prepareStatement("SELECT TenDoDung, MaDoDung, LoaiDoDung, TinhTrang FROM `cosovatchat`");
    	ResultSet rs = check.executeQuery();
    	String s;
    	while (rs.next()) {
    		s = rs.getString("LoaiDoDung");
    		if (s!=null && !lstTenLoaiDoDung.contains(s)) {
    			lstTenLoaiDoDung.add(s);
    		}
    		   		
    	}
    	loaiDoDungLabel.setValue(coSoVatChat.getTenLoaiDoDung());
    	loaiDoDungLabel.setItems(lstTenLoaiDoDung);
    	
    	check = conn.prepareStatement("SELECT DISTINCT TinhTrang FROM `cosovatchat`");
    	rs = check.executeQuery();
    	while (rs.next()) {
    		s = rs.getString("TinhTrang");
    		if (s!=null) {
    			lstTinhTrang.add(s);
    		}	
    	}
    	tinhTrangLabel.setValue(coSoVatChat.getTinhTrang());
    	tinhTrangLabel.setItems(lstTinhTrang);
    	
    	tenDoDungLabel.setText(coSoVatChat.getTenDoDung());
    	maDoDungHienTai = coSoVatChat.getMaDoDung();
    	

    }

    public void goBack(MouseEvent event) throws IOException {
//        ViewUtils viewUtils = new ViewUtils();
//        viewUtils.switchToCoSoVatChat_Admin_view(event);
    	((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }
    
    public void onClick(ActionEvent event) throws IOException{
    	
    }
    
    @FXML
    public void update(MouseEvent event) throws IOException {
        ViewUtils viewUtils = new ViewUtils();
        String tenDoDung = tenDoDungLabel.getText();
        String tenLoaiDoDung = loaiDoDungLabel.getSelectionModel().getSelectedItem();
        String tinhTrang = tinhTrangLabel.getSelectionModel().getSelectedItem();


        if (tenDoDung.trim().equals("")) {
            createDialog(
                    Alert.AlertType.WARNING,
                    "Chưa đủ số trường cần thiết",
                    "", "Vui lòng nhập đủ thông tin!"
            );
        } else {
            try {
                Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
                int result = CoSoVatChatServices.updateFacility(conn, maDoDungHienTai, tenDoDung, tinhTrangLabel.getSelectionModel().getSelectedItem(), tenLoaiDoDung);
                if (result == 1) {
                    createDialog(
                            Alert.AlertType.CONFIRMATION,
                            "Thành công",
                            "", "Cập nhật cơ sở vật chất thành công!"
                    );
//                    viewUtils.switchToCoSoVatChat_Admin_view(event);
                } else {
                    createDialog(
                            Alert.AlertType.ERROR,
                            "Thất bại",
                            "", "Có lỗi xảy ra, vui lòng thử lại!"
                    );
                }
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }
    
    public void getModal() throws SQLException {
    	ObservableList<String> lstTenLoaiDoDung = FXCollections.observableArrayList();
    	ObservableList<String> lstTinhTrang = FXCollections.observableArrayList();
    	Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
    	PreparedStatement check = conn.prepareStatement("SELECT TenDoDung, MaDoDung, LoaiDoDung, TinhTrang FROM `cosovatchat`");
    	ResultSet rs = check.executeQuery();
    	String s;
    	while (rs.next()) {
    		s = rs.getString("LoaiDoDung");
    		if (s!=null && !lstTenLoaiDoDung.contains(s)) {
    			lstTenLoaiDoDung.add(s);
    		}
    		   		
    	}
    	loaiDoDungLabel.setItems(lstTenLoaiDoDung);
    	
    	check = conn.prepareStatement("SELECT DISTINCT TinhTrang FROM `cosovatchat`");
    	rs = check.executeQuery();
    	while (rs.next()) {
    		s = rs.getString("TinhTrang");
    		if (s!=null) {
    			lstTinhTrang.add(s);
    		}	
    	}
    	tinhTrangLabel.setItems(lstTinhTrang);
    }

    public void addnew(ActionEvent event) throws IOException, SQLException {
    	Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
    	
        ViewUtils viewUtils = new ViewUtils();
        String tenDoDung = tenDoDungLabel.getText();
        String loaiDoDung = loaiDoDungLabel.getSelectionModel().getSelectedItem();
        String tinhTrang = tinhTrangLabel.getSelectionModel().getSelectedItem();
        PreparedStatement preparedStatement;
        int result = CoSoVatChatServices.addFacility(conn, tenDoDung, loaiDoDung, tinhTrang);
        if (result == 1) {
            createDialog(
                    Alert.AlertType.CONFIRMATION,
                    "Thành công",
                    "", "Thêm cơ sở vật chất thành công!"
            );
        } else {
            createDialog(
                    Alert.AlertType.ERROR,
                    "Thất bại",
                    "", "Có lỗi xảy ra, vui lòng thử lại!"
            );
        }
        conn.close();
    }

    public void hide_add_btn() {
        add_btn.setVisible(false);
    }

    public void hide_update_btn() {
        update_btn.setVisible(false);
        add_btn.setTranslateX(100);
    }

    public void hide_Pane() {
        maDoDungPane.setVisible(false);
        soLuongKhaDungPane.setVisible(false);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
