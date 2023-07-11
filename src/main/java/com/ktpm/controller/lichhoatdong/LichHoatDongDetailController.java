package com.ktpm.controller.lichhoatdong;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import static com.ktpm.constants.DBConstants.*;
import static com.ktpm.controller.AdminController.userRole;
import static com.ktpm.utils.Utils.*;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

import com.ktpm.controller.AddThanhVienController;
import com.ktpm.controller.nhankhau.NhanKhauController;
import com.ktpm.model.LichHoatDong;
import com.ktpm.model.NhanKhau;
import com.ktpm.model.Phong;
import com.ktpm.model.SoHoKhau;
import com.ktpm.services.LichHoatDongServices;
import com.ktpm.services.NhanKhauServices;
import com.ktpm.utils.ViewUtils;

public class LichHoatDongDetailController implements Initializable {
    @FXML
    private Button add_btn, update_btn;
    @FXML
    private Button doiNguoiTaoBtn, addCSVCBtn;
    @FXML
    private TextField endTimeTextField, startTimeTextField, maHoatDongTextField, tenHoatDongTextField, nguoiTaoTextField;
    @FXML
    private TextField thuPhiTextField;
    @FXML
    private DatePicker startDatePicker, endDatePicker;
    @FXML
    private ChoiceBox<String> statusChoiceBox,roomChoiceBox;
    @FXML
    private Pane maHoatDongPane;
    @FXML
    private Pane statusPane;
    @FXML
    private Text title;
    private LichHoatDong lichHoatDong;

    @FXML
    private TableView<NhanKhau> tableView;
    @FXML
    private TableColumn indexColumn;
    @FXML
    private TableColumn<NhanKhau, String> hoVaTenColumn, biDanhColumn, ngaySinhColumn, cccdColumn, noiSinhColumn, gioiTinhColumn,
            nguyenQuanColumn, danTocColumn, noiThuongTruColumn, tonGiaoColumn, quocTichColumn, diaChiHienNayColumn, ngheNghiepColumn;
    @FXML
    private Pagination pagination;
    private ObservableList<NhanKhau> nhanKhauList = FXCollections.observableArrayList();
    // Connect to database
    private Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
    private PreparedStatement preparedStatement = null;
    private String pre_status = "";
    public LichHoatDongDetailController() throws SQLException {
    }

    public void setLichHoatDong(LichHoatDong lichHoatDong) throws SQLException {
        this.lichHoatDong = lichHoatDong;
        System.out.println(lichHoatDong.getMaHoatDong());
        pre_status = lichHoatDong.getStatus();
        System.out.println(pre_status);
        maHoatDongTextField.setText(String.valueOf(lichHoatDong.getMaHoatDong()));
        tenHoatDongTextField.setText(lichHoatDong.getTenHoatDong());
        String startTime = lichHoatDong.getStartTime();System.out.println(startTime);
        String [] starttime = startTime.split(" ");
        startDatePicker.setValue(LOCAL_DATE(starttime[1]));
        startTimeTextField.setText(starttime[0].substring(0,8));
        String endTime = lichHoatDong.getEndTime();
        String[] endtime = endTime.split(" ");
        endDatePicker.setValue(LOCAL_DATE(endtime[1]));
        endTimeTextField.setText(endtime[0].substring(0,8));
        statusChoiceBox.setValue(String.valueOf(lichHoatDong.getStatus()));
        roomChoiceBox.setValue(String.valueOf(lichHoatDong.getTenPhong()));
        thuPhiTextField.setText(lichHoatDong.getThuPhi());
        nguoiTaoTextField.setText(String.valueOf(LichHoatDongServices.getNamebyID(conn, lichHoatDong.getMaNguoiTao())));
    }

    public void goBack(ActionEvent event) throws IOException {
        ViewUtils viewUtils = new ViewUtils();
        viewUtils.switchToLichHoatDong_Admin_view(event);
    }

    public void update(ActionEvent event) throws IOException {
        ViewUtils viewUtils = new ViewUtils();
        String maHoatDong = maHoatDongTextField.getText();
        String tenHoatDong = tenHoatDongTextField.getText();
        String startTime = startTimeTextField.getText();
        String endTime = endTimeTextField.getText();
        String status = statusChoiceBox.getValue();
        String maNguoiTao = String.valueOf(lichHoatDong.getMaNguoiTao());
        String tenPhong =roomChoiceBox.getValue();
        String thuPhi=thuPhiTextField.getText();
        NhanKhau selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            maNguoiTao = String.valueOf(selected.getID());
        }
        if (maHoatDong.trim().equals("") || tenHoatDong.trim().equals("") || startTime.trim().equals("") || endTime.trim().equals("") ||tenPhong.equals("")
                || startDatePicker.getValue() == null || endDatePicker.getValue() == null) {
            createDialog(
                    Alert.AlertType.WARNING,
                    "Thông báo",
                    "", "Vui lòng nhập đủ thông tin!"
            );
        } else {
            String startDateTime = startDatePicker.getValue().toString();
            String starttime = startDateTime + " " + startTime;
            String endDateTime = endDatePicker.getValue().toString();
            String endtime = endDateTime + " " + endTime;


            if (!isValidTime(startTime) || !isValidTime(endTime)) {
                createDialog(Alert.AlertType.WARNING, "Thông báo", "Hãy chọn đúng định dạng hh:mm:ss", "");
            } else if (!greaterTime(startDateTime, startTime, endDateTime, endTime)) {
                createDialog(Alert.AlertType.WARNING, "Thông báo", "Thời gian kết thúc phải lớn hơn thời gian bắt đầu!", "");
            } else {
                try {
                    Connection conn;
                    PreparedStatement preparedStatement;
                    conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);

                    int result = 0;

                    System.out.println(pre_status + "|||" + status);

                     result = LichHoatDongServices.updateLichHoatDong(conn, maHoatDong, tenHoatDong, starttime, endtime, status, maNguoiTao,tenPhong,thuPhi);
                        if (result == 1) {
                            createDialog(
                                    Alert.AlertType.CONFIRMATION,
                                    "Thành công",
                                    "", "Cập nhật lịch hoạt động thành công!"
                            );
                            viewUtils.switchToLichHoatDong_Admin_view(event);
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
        }
    }

    public void addnew(ActionEvent event) throws IOException {
        ViewUtils viewUtils = new ViewUtils();
        String maHoatDong;
        String tenHoatDong = tenHoatDongTextField.getText();
        String startTime = startTimeTextField.getText();
        String endTime = endTimeTextField.getText();
        String status = "Chưa duyệt";
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String thoiGianTao = dtf.format(currentTime);
        String tenPhong =roomChoiceBox.getValue();
        String thuPhi=thuPhiTextField.getText();
        NhanKhau selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) createDialog(Alert.AlertType.WARNING, "Thông báo", "", "Vui lòng chọn nhân khẩu");
        if (tenHoatDong.trim().equals("") || startTime.trim().equals("") || endTime.trim().equals("")
                || startDatePicker.getValue() == null || endDatePicker.getValue() == null) {

            createDialog(
                    Alert.AlertType.WARNING,
                    "Thông báo",
                    "", "Vui lòng nhập đủ thông tin!"
            );
        } else {
            String startDateTime = startDatePicker.getValue().toString();
            String starttime = startDateTime + " " + startTime;
            String endDateTime = endDatePicker.getValue().toString();
            String endtime = endDateTime + " " + endTime;

            if (!isValidTime(startTime) || !isValidTime(endTime)) {
                createDialog(Alert.AlertType.WARNING, "Thông báo", "Hãy chọn đúng định dạng hh:mm:ss", "");
            } else if (!greaterTime(startDateTime, startTime, endDateTime, endTime)) {
                createDialog(Alert.AlertType.WARNING, "Thông báo", "Thời gian kết thúc phải lớn hơn thời gian bắt đầu!", "");
            } else {

                try {
                    Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
                    PreparedStatement preparedStatement;
                    ResultSet rs;
                    do {
                        int rand = ThreadLocalRandom.current().nextInt(100000, 999999);
                        maHoatDong = String.valueOf(rand);
                        PreparedStatement check = conn.prepareStatement("SELECT MaHoatDong From lichhoatdong WHERE `MaHoatDong` =?");
                        check.setInt(1, rand);
                        rs = check.executeQuery();
                    } while (rs.next());

                    int result = LichHoatDongServices.insertLichHoatDong(conn, maHoatDong, tenHoatDong, starttime, endtime, status, thoiGianTao, selected,tenPhong,thuPhi);
                    if (result == 1) {
                        createDialog(
                                Alert.AlertType.CONFIRMATION,
                                "Thành công",
                                "", "Thêm lịch hoạt động thành công!"
                        );
                       
                    }
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                viewUtils.switchToLichHoatDong_Admin_view(event);

            }
        }
    }

    public void hide_add_btn() {
        add_btn.setVisible(false);
        tableView.setVisible(false);
    }


    public void hide_update_btn() {
        update_btn.setVisible(false);
        add_btn.setTranslateX(100);
        nguoiTaoTextField.setVisible(false);
        doiNguoiTaoBtn.setVisible(false);
        addCSVCBtn.setVisible(false);
    }

    public void hide_maHoatDongPane() {
        maHoatDongPane.setVisible(false);
    }

    public void hide_statusPane() {
        statusPane.setVisible(false);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	addCSVCBtn.setVisible(false);
        statusChoiceBox.getItems().add("Chưa duyệt");
        statusChoiceBox.getItems().add("Chấp nhận");

        try {
        	ArrayList< Phong> listPhong= LichHoatDongServices.getAllRoom(conn);
        	for(Phong phong:listPhong) {
        		roomChoiceBox.getItems().add(phong.getTenPhong());
        	}
        	statusChoiceBox.setValue("Chưa duyệt");
        	statusPane.setVisible(userRole.equals("totruong"));
            ResultSet result = NhanKhauServices.getAllNhanKhau();
            while (result.next()) {
                nhanKhauList.add(new NhanKhau(result.getInt("ID"), result.getString("HoTen"), result.getString("BiDanh"),
                        convertDate(result.getString("NgaySinh")), result.getString("CCCD"), result.getString("NoiSinh"),
                        result.getString("GioiTinh"), result.getString("NguyenQuan"), result.getString("DanToc"),
                        result.getString("NoiThuongTru"), result.getString("TonGiao"), result.getString("QuocTich"),
                        result.getString("DiaChiHienNay"), result.getString("NgheNghiep")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        indexColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<NhanKhau, NhanKhau>, ObservableValue<NhanKhau>>) p -> new ReadOnlyObjectWrapper(p.getValue()));

        indexColumn.setCellFactory(new Callback<TableColumn<NhanKhau, NhanKhau>, TableCell<NhanKhau, NhanKhau>>() {
            @Override
            public TableCell<NhanKhau, NhanKhau> call(TableColumn<NhanKhau, NhanKhau> param) {
                return new TableCell<NhanKhau, NhanKhau>() {
                    @Override
                    protected void updateItem(NhanKhau item, boolean empty) {
                        super.updateItem(item, empty);

                        if (this.getTableRow() != null && item != null) {
                            setText(this.getTableRow().getIndex() + 1 + "");
                        } else {
                            setText("");
                        }
                    }
                };
            }
        });
        indexColumn.setSortable(false);
        hoVaTenColumn.setCellValueFactory(new PropertyValueFactory<NhanKhau, String>("HoTen"));
        biDanhColumn.setCellValueFactory(new PropertyValueFactory<NhanKhau, String>("BiDanh"));
        ngaySinhColumn.setCellValueFactory(new PropertyValueFactory<NhanKhau, String>("NgaySinh"));
        cccdColumn.setCellValueFactory(new PropertyValueFactory<NhanKhau, String>("CCCD"));
        noiSinhColumn.setCellValueFactory(new PropertyValueFactory<NhanKhau, String>("NoiSinh"));
        gioiTinhColumn.setCellValueFactory(new PropertyValueFactory<NhanKhau, String>("GioiTinh"));
        nguyenQuanColumn.setCellValueFactory(new PropertyValueFactory<NhanKhau, String>("NguyenQuan"));
        danTocColumn.setCellValueFactory(new PropertyValueFactory<NhanKhau, String>("DanToc"));
        noiThuongTruColumn.setCellValueFactory(new PropertyValueFactory<NhanKhau, String>("NoiThuongTru"));
        tonGiaoColumn.setCellValueFactory(new PropertyValueFactory<NhanKhau, String>("TonGiao"));
        quocTichColumn.setCellValueFactory(new PropertyValueFactory<NhanKhau, String>("QuocTich"));
        diaChiHienNayColumn.setCellValueFactory(new PropertyValueFactory<NhanKhau, String>("DiaChiHienNay"));
        ngheNghiepColumn.setCellValueFactory(new PropertyValueFactory<NhanKhau, String>("NgheNghiep"));
        tableView.setItems(FXCollections.observableArrayList(nhanKhauList));
    }


    public void doiNguoiTao() {
        tableView.setVisible(true);
        doiNguoiTaoBtn.setVisible(false);
        nguoiTaoTextField.setTranslateX(-200);
        nguoiTaoTextField.setTranslateY(45);
    }
    public void addCSVC(ActionEvent event) throws IOException, SQLException {
     
    }
}
