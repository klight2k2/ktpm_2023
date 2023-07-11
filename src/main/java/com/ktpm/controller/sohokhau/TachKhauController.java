package com.ktpm.controller.sohokhau;

import static com.ktpm.constants.FXMLConstants.CO_SO_VAT_CHAT_VIEW_FXML;
import static com.ktpm.utils.Utils.createDialog;

import java.sql.SQLException;
import java.util.Optional;

import com.ktpm.model.BaseNhanKhau;
import com.ktpm.model.SoHoKhau;
import com.ktpm.model.ThanhVien;
import com.ktpm.services.NhanKhauServices;
import com.ktpm.services.SoHoKhauServices;
import com.ktpm.services.ThanhVienServices;
import com.ktpm.utils.ViewUtils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TachKhauController {
	private ObservableList<ThanhVien> thanhVienCu = FXCollections.observableArrayList();
	private ObservableList<ThanhVien> thanhVienMoi = FXCollections.observableArrayList();
	
	
	public void setInfo() throws SQLException {
		chuHoHienTai.setText(this.soHoKhauHienTai.getTenChuHo());
		diaChiHienTai.setText(this.soHoKhauHienTai.getDiaChi());
		maHoKhauHienTaiText.setText(this.soHoKhauHienTai.getMaHoKhau());
		chuHoBox.setItems(SoHoKhauServices.getThanhVienGiaDinh(this.soHoKhauHienTai.getMaHoKhau()));
		
		hoKhauCuTable.setRowFactory(tv -> {
            TableRow<ThanhVien> row = new TableRow<>();
            return row;
        });
		
		hoTenCuColumn.setCellValueFactory(new PropertyValueFactory<ThanhVien, String>("HoTen"));
		quanHeCuColumn.setCellValueFactory(new PropertyValueFactory<ThanhVien, String>("quanHeVoiChuHo"));
		hoKhauCuTable.setItems(SoHoKhauServices.getObjThanhVienGiaDinh(this.soHoKhauHienTai.getMaHoKhau()));
		
		hoTenMoiColumn.setCellValueFactory(new PropertyValueFactory<ThanhVien, String>("HoTen"));
		quanHeMoiColumn.setCellValueFactory(new PropertyValueFactory<ThanhVien, String>("quanHeVoiChuHo"));
		
		chuHoBox.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//				System.out.println(oldValue);
				try {
					thanhVienCu = SoHoKhauServices.getObjThanhVienGiaDinhExcept(soHoKhauHienTai.getMaHoKhau(), newValue);
					hoKhauCuTable.setItems(thanhVienCu);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		
	}
	
    @FXML
    private TableView<ThanhVien> hoKhauCuTable;

    @FXML
    private TableView<ThanhVien> hoKhauMoiTable;

    @FXML
    private TableColumn<ThanhVien, String> hoTenCuColumn =  new TableColumn<>("hoTen");

    @FXML
    private TableColumn<ThanhVien, String> hoTenMoiColumn =  new TableColumn<>("hoTen");
    
    @FXML
    private TableColumn<ThanhVien, String> quanHeCuColumn = new TableColumn<>("quanHeVoiChuHo");

    @FXML
    private TableColumn<ThanhVien, String> quanHeMoiColumn = new TableColumn<>("quanHeVoiChuHo");
	
    @FXML
    private Text maHoKhauHienTaiText;
	
	private SoHoKhau soHoKhauHienTai;
	
    @FXML
    private ComboBox<String> chuHoBox;

    @FXML
    private Text chuHoHienTai;

    @FXML
    private Text diaChiHienTai;


	public SoHoKhau getSoHoKhauHienTai() {
		return soHoKhauHienTai;
	}

	public void setSoHoKhauHienTai(SoHoKhau soHoKhauHienTai) {
		this.soHoKhauHienTai = soHoKhauHienTai;
	}

	@FXML
    private TextField diaChiMoiText;

    @FXML
    private Text hoKhauHienTai;

    @FXML
    private TextField maHoKhauMoiText;
    
    
    
    
	@FXML
    void denHoKhauMoi(MouseEvent event) {
		if (chuHoBox.getValue() != null) {
			ThanhVien biRuongBo = hoKhauCuTable.getSelectionModel().getSelectedItem();
			if (biRuongBo != null) {
				thanhVienCu.remove(biRuongBo);
				thanhVienMoi.add(biRuongBo);
				
				
				TextInputDialog dialog = new TextInputDialog();
				dialog.setTitle("Nhập thông tin Quan hệ với chủ hộ");
		        dialog.setHeaderText(biRuongBo.getHoTen());
		        dialog.setContentText("Quan hệ với chủ hộ:");
		        Optional<String> result = dialog.showAndWait();
		        if (result.isPresent()) {
		        	biRuongBo.setQuanHeVoiChuHo(result.get()); 
		        }
		        
		        hoKhauCuTable.setItems(thanhVienCu);
				hoKhauMoiTable.setItems(thanhVienMoi);	
			}
		}
    }

    @FXML
    void onSubmit(MouseEvent event) throws SQLException {
    	String diaChiMoi = diaChiMoiText.getText();
    	String maHoKhauMoi = maHoKhauMoiText.getText();
    	int maChuHoMoi = NhanKhauServices.findIDNhanKhauViaTen(chuHoBox.getValue());
    	
    	// Tao ho khau moi
    	int rs = SoHoKhauServices.addSoHoKhauKhiTach(maHoKhauMoi, diaChiMoi, maChuHoMoi);
    	if (rs == 1) {
    		createDialog(Alert.AlertType.INFORMATION, "Thông báo", "", "Tách nhân khẩu thành công");
    	}else {
    		createDialog(Alert.AlertType.WARNING, "Thông báo", "", "Có lỗi đã xảy ra");
    	}
    	
    	// xoa ho khau cu cua thanh vien moi
    	ThanhVienServices.xoaCacThanhVienKhoiHoKhauCu(thanhVienMoi, maChuHoMoi);
    	
    	// them thanh vien moi vao ho khau moi
    	ThanhVienServices.themCacThanhVienVaoHoKhauMoi(thanhVienMoi, maChuHoMoi);
    	
    	((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    	
    }

    @FXML
    void veHoKhauCu(MouseEvent event) {
    	if (chuHoBox.getValue() != null) {
    		ThanhVien biRuongBo = hoKhauMoiTable.getSelectionModel().getSelectedItem();
    		if (biRuongBo != null) {
    			thanhVienMoi.remove(biRuongBo);
    			thanhVienCu.add(biRuongBo);
    			  
    			TextInputDialog dialog = new TextInputDialog();
				dialog.setTitle("Nhập thông tin Quan hệ với chủ hộ");
		        dialog.setHeaderText(biRuongBo.getHoTen());
		        dialog.setContentText("Quan hệ với chủ hộ:");
		        Optional<String> result = dialog.showAndWait();
		        if (result.isPresent()) {
		        	biRuongBo.setQuanHeVoiChuHo(result.get()); 
		        }
		        
		        hoKhauCuTable.setItems(thanhVienCu);
    			hoKhauMoiTable.setItems(thanhVienMoi); 
    		}
    	}
    }

}
