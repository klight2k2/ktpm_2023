package com.ktpm.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static com.ktpm.constants.DBConstants.*;
import static com.ktpm.constants.TypeConstants.*;
import static com.ktpm.constants.FXMLConstants.*;
import static com.ktpm.utils.Utils.toUpperFirstLetter;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import com.ktpm.HomeApplication;
import com.ktpm.model.LoaiCoSoVatChat;
import com.ktpm.services.CoSoVatChatServices;
import com.ktpm.services.LichHoatDongServices;
import com.ktpm.services.NhanKhauServices;
import com.ktpm.services.SoHoKhauServices;
import com.ktpm.services.ThongKeServices;
import com.ktpm.utils.ViewUtils;

public class AdminController implements Initializable {
	@FXML
	private AnchorPane basePane;
	@FXML
	private Button signUpUserButton;

	@FXML
	private Button dashboardButton;

	@FXML
	private Button nhanKhauButton;
	@FXML
	private Button hokhauButton;

	@FXML
	private Button coSoVatChatButton;

	@FXML
	private Button thongKeButton;

	@FXML
	private Button lichSinhHoatButton;

	private Button selectedButton;

	@FXML
	private Label nhankhauLabel, hokhauLabel, usernameLabel, tamTruLabel, tamVangLabel;
	
    @FXML
    private Label tuVongLabel;
	
	@FXML
	private Text lichHoatDongLabel, thoiGianLabel;
	@FXML
	private BarChart facilityChart;

	// Save user role
	private static final Preferences userPreferences = Preferences.userRoot();
	public static final String userRole = userPreferences.get("role", "");
	public static final String userName = userPreferences.get("username", "");
	private final ViewUtils viewUtils = new ViewUtils();
	private Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);

	public AdminController() throws SQLException {
	}

	public void switchToDashboard(ActionEvent event) throws IOException {
		viewUtils.changeScene(event, ADMIN_VIEW_FXML);
	}

	public void switchToSignUp() throws IOException {
		viewUtils.changeAnchorPane(basePane, SIGN_UP_USER_VIEW_FXML);
	}

	public void switchToNhanKhau() throws IOException {
		viewUtils.changeAnchorPane(basePane, NHAN_KHAU_VIEW_FXML);

	}

	public void switchToCoSoVatChat() throws IOException {
		viewUtils.changeAnchorPane(basePane, CO_SO_VAT_CHAT_VIEW_FXML);
	}

	public void switchToSoHoKhau() throws IOException {
		viewUtils.changeAnchorPane(basePane, SO_HO_KHAU_VIEW_FXML);
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		signUpUserButton.setVisible(userRole.equals("totruong"));
		nhankhauLabel.setText("" + NhanKhauServices.getTotalNhanKhau());
		hokhauLabel.setText("" + SoHoKhauServices.getTotalSoHoKhau());
		usernameLabel.setText(toUpperFirstLetter(userName));
		tamTruLabel.setText(""+ThongKeServices.SoLuongNhanKhauTamTru());
		tamVangLabel.setText(""+ThongKeServices.SoLuongNhanKhauTamVang());
		tuVongLabel.setText(""+ThongKeServices.SoLuongNhanKhauTuVong());
		ResultSet result = null;
		try {
			result = LichHoatDongServices.getLichHoatDongGanNhat(conn);
			if (result.next()) {
				lichHoatDongLabel.setText(result.getString(1));
				thoiGianLabel.setText(result.getString(2));
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		List<LoaiCoSoVatChat> listCoSoVatChat = CoSoVatChatServices.getStatisticCSVC();

		XYChart.Series<String, Number> seriesConDungDuoc = new XYChart.Series<>();

		XYChart.Series<String, Number> seriesHong = new XYChart.Series<>();
		seriesHong.setName("Hỏng");
		for (LoaiCoSoVatChat loaiCSVC : listCoSoVatChat) {
			if (loaiCSVC.getTinhTrang().equals(CON_DUNG_DUOC)) {

				seriesConDungDuoc.getData().add(new XYChart.Data<>(loaiCSVC.getLoaiDoDung(), loaiCSVC.getSoLuong()));
			} else {
				seriesHong.getData().add(new XYChart.Data<>(loaiCSVC.getLoaiDoDung(), loaiCSVC.getSoLuong()));
			}
		}
		seriesConDungDuoc.setName("Còn dùng được");

		// Đặt dữ liệu cho StackedBarChart
		facilityChart.getData().addAll(seriesConDungDuoc, seriesHong);

		
	}

	public void switchToLichHoatDong() throws IOException {
		viewUtils.changeAnchorPane(basePane, LICH_HOAT_DONG_VIEW_FXML);
	}

	public void switchToStatistic() throws IOException {
		viewUtils.changeAnchorPane(basePane, THONG_KE_FXML);
	}
	
}
