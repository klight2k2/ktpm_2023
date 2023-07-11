package com.ktpm.controller;

import static com.ktpm.constants.FXMLConstants.ICON;
import static com.ktpm.constants.FXMLConstants.NHAN_KHAU_VIEW_FXML;
import static com.ktpm.constants.FXMLConstants.TAM_TRU_FXML;
import static com.ktpm.constants.FXMLConstants.TAM_VANG_FXML;
import static com.ktpm.utils.Utils.createDialog;

import java.sql.SQLException;

import com.ktpm.services.NhanKhauServices;
import com.ktpm.utils.Utils;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class TamTruController {
	private int  idNhankhau;

    @FXML
    private DatePicker denNgayDatePicker;

    @FXML
    private TextField liDoTextField;

    @FXML
    private TextField noiTamTruTextField;

    @FXML
    private Button submit;

    @FXML
    private DatePicker tuNgayDatePicker;
    
    
    @FXML
    void onSubmit(MouseEvent event) throws SQLException {
    	String denNgay=denNgayDatePicker.getValue().toString();
    	String tuNgay=tuNgayDatePicker.getValue().toString();
    	String lido=liDoTextField.getText();
    	String noiTamTru=noiTamTruTextField.getText();
    	
    	if(denNgay.equals("") || tuNgay.equals("")|| lido.equals("")|| noiTamTru.equals("")) {
    		  createDialog(
                      Alert.AlertType.WARNING,
                      "Thông báo",
                      "", "Vui lòng nhập đủ thông tin!");
    	}else {
    		int result = NhanKhauServices.dangKiTamTru(idNhankhau, tuNgay, denNgay, lido, noiTamTru);
        	
        	if (result == 1) {
        		createDialog(Alert.AlertType.INFORMATION, "Thông báo", "Thêm thành công!", "");
        	}else {
        		createDialog(Alert.AlertType.ERROR, "Thông báo", "Có lỗi!", "");
        	}
    	}
    	((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }
    public void setIdNhanKhau(int id) {
    	this.idNhankhau=id;
    }
    
    public static void main(String[] args) {
		
	}
}
