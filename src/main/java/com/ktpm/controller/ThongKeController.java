package com.ktpm.controller;

import static com.ktpm.constants.DBConstants.DATABASE;
import static com.ktpm.constants.DBConstants.PASSWORD;
import static com.ktpm.constants.DBConstants.ROWS_PER_PAGE;
import static com.ktpm.constants.DBConstants.USERNAME;

import java.io.IOException;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.ktpm.model.BaseNhanKhau;
import com.ktpm.model.CoSoVatChat;
import com.ktpm.services.CoSoVatChatServices;
import com.ktpm.services.ThongKeServices;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class ThongKeController implements Initializable {

    @FXML
    private TextField denNamField;

    @FXML
    private TextField denTuoiField;

    @FXML
    private ComboBox<String> gioiTinhList;

    @FXML
    private ComboBox<String> tinhTrangList;

    @FXML
    private TextField tuNamField;

    @FXML
    private TextField tuTuoiField;

    
    @FXML
    private TableColumn<BaseNhanKhau, String> gioiTinhColumn;


    @FXML
    private TableColumn<BaseNhanKhau, String> hoTenColumn;

    @FXML
    private TableColumn<BaseNhanKhau, String>ngaySinhColumn;

    @FXML
    private TableColumn<BaseNhanKhau, String> noiThuongTruColumn;
    
    @FXML
    private TableColumn indexColumn;
    
    @FXML
    private Pagination pagination;
    
    @FXML
    private TableView<BaseNhanKhau> tableView;
    
    private ObservableList<BaseNhanKhau> thongKeList = FXCollections.observableArrayList();
    
    
    private String statisticNhanKhauQuery(int tuTuoi, int denTuoi, String gender, String Status, int tuNam, int denNam) {        
        String query = "SELECT * FROM nhankhau "
                    + " INNER JOIN cccd ON nhankhau.ID = cccd.idNhanKhau"
                    + " LEFT JOIN tamtru ON nhankhau.ID = tamtru.idNhanKhau "
                    + " LEFT JOIN tamvang ON nhankhau.ID = tamvang.idNhanKhau "
                    + " LEFT JOIN khaitu ON nhankhau.ID = khaitu.idnguoichet "
                    + " WHERE ROUND(DATEDIFF(CURDATE(),NgaySinh)/365 , 0) >= "
                    + tuTuoi
                    + " AND ROUND(DATEDIFF(CURDATE(),NgaySinh)/365 , 0) <= "
                    + denTuoi;
        if (!gender.equalsIgnoreCase("Toàn bộ")) {
            query += " AND nhankhau.GioiTinh = '" + gender + "'";
        }
        if (Status.equalsIgnoreCase("Toàn bộ")) {
            query += " AND (tamtru.denNgay >= CURDATE() OR tamtru.denNgay IS NULL)"
                    + " AND (tamvang.denNgay <= CURDATE() OR tamvang.denNgay IS NULL)";
        } else if (Status.equalsIgnoreCase("Thuong tru")) {
            query += " AND tamtru.denNgay IS NULL";
            
        } else if (Status.equalsIgnoreCase("Đăng kí tạm trú")) {
            query += " AND (YEAR(tamtru.tuNgay) BETWEEN "
                    + tuNam
                    + " AND "
                    + denNam
                    + ")";
        } else if (Status.equalsIgnoreCase("Đăng kí tạm vắng")) {
            query += " AND (YEAR(tamvang.tuNgay) BETWEEN "
                    + tuNam
                    + " AND "
                    + denNam
                    + ")";
        } else if (Status.equalsIgnoreCase("Khai tử")) {
            query += " AND (YEAR(khaitu.ngayChet) BETWEEN "
                    + tuNam
                    + " AND "
                    + denNam
                    + ")";
        }
        return query;
    }
    
    @FXML
    void onStatistic(MouseEvent event) throws SQLException {
    	thongKeList = FXCollections.observableArrayList();
    	System.out.println("Clicked");
    	int denNam= Integer.parseInt(denNamField.getText());
    	int tuNam= Integer.parseInt(tuNamField.getText());
    	String gioiTinh=gioiTinhList.getValue();
    	String tinhTrang=tinhTrangList.getValue();
    	int denTuoi= Integer.parseInt(denTuoiField.getText());
    	int tuTuoi=  Integer.parseInt(tuTuoiField.getText());
    	
    	String query = statisticNhanKhauQuery(tuTuoi, denTuoi, gioiTinh, tinhTrang, tuNam, denNam);
    	
    	ResultSet result = ThongKeServices.statisticNhanKhau(query);
    	while (result.next()) {
    		
    		thongKeList.add(new BaseNhanKhau(result.getString("HoTen"), result.getString("GioiTinh"),
                    result.getString("NgaySinh"), result.getString("NoiThuongTru")));
//    		System.out.println(thongKeList.get(0).getHoTen());

        }
    	
        int soDu = thongKeList.size() % ROWS_PER_PAGE;
        if (soDu != 0) pagination.setPageCount(thongKeList.size() / ROWS_PER_PAGE + 1);
        else pagination.setPageCount(thongKeList.size() / ROWS_PER_PAGE);
        pagination.setMaxPageIndicatorCount(5);
        pagination.setPageFactory(this::createTableView);

        tableView.setRowFactory(tv -> {
            TableRow<BaseNhanKhau> row = new TableRow<>();
//            row.setOnMouseClicked(ev -> {
//                if (event.getClickCount() == 2 && (!row.isEmpty())) {
//                    // Perform actions with rowData
//                    try {
//                        try {
//							detail(event);
//						} catch (SQLException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            });
            return row;
        });
    }
    	
    public Node createTableView(int pageIndex) {
        indexColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<BaseNhanKhau, BaseNhanKhau>, ObservableValue<BaseNhanKhau>>) p -> new ReadOnlyObjectWrapper(p.getValue()));
        indexColumn.setCellFactory(new Callback<TableColumn<BaseNhanKhau, BaseNhanKhau>, TableCell<BaseNhanKhau, BaseNhanKhau>>() {
            @Override
            public TableCell<BaseNhanKhau, BaseNhanKhau> call(TableColumn<BaseNhanKhau, BaseNhanKhau> param) {
                return new TableCell<BaseNhanKhau, BaseNhanKhau>() {
                    @Override
                    protected void updateItem(BaseNhanKhau item, boolean empty) {
                        super.updateItem(item, empty);

                        if (this.getTableRow() != null && item != null) {
                            setText(this.getTableRow().getIndex() + 1 + pageIndex * ROWS_PER_PAGE + "");
                        } else {
                            setText("");
                        }
                    }
                };
            }
        });

        indexColumn.setSortable(false);
        hoTenColumn.setCellValueFactory(new PropertyValueFactory<BaseNhanKhau, String>("hoTen"));
        gioiTinhColumn.setCellValueFactory((new PropertyValueFactory<BaseNhanKhau, String>("gioiTinh")));
        ngaySinhColumn.setCellValueFactory(new PropertyValueFactory<BaseNhanKhau, String>("ngaySinh"));
        noiThuongTruColumn.setCellValueFactory(new PropertyValueFactory<BaseNhanKhau, String>("noiThuongTru"));
        
        int lastIndex = 0;
        int displace = thongKeList.size() % ROWS_PER_PAGE;
        if (displace > 0) {
            lastIndex = thongKeList.size() / ROWS_PER_PAGE;
        } else {
            lastIndex = thongKeList.size() / ROWS_PER_PAGE - 1;
        }
        if (thongKeList.isEmpty()) tableView.setItems(FXCollections.observableArrayList(thongKeList));
        else {
            if (lastIndex == pageIndex && displace > 0) {
                tableView.setItems(FXCollections.observableArrayList(thongKeList.subList(pageIndex * ROWS_PER_PAGE, pageIndex * ROWS_PER_PAGE + displace)));
            } else {
                tableView.setItems(FXCollections.observableArrayList(thongKeList.subList(pageIndex * ROWS_PER_PAGE, pageIndex * ROWS_PER_PAGE + ROWS_PER_PAGE)));
            }
        }
        return tableView;
    }
    	


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ObservableList<String> listGioiTinhOptions = FXCollections.observableArrayList("Nam", "Nữ","Toàn bộ");
		gioiTinhList.setItems(listGioiTinhOptions);
		gioiTinhList.setValue("Toàn bộ");
		ObservableList<String> tinhTrangOptions = FXCollections.observableArrayList("Toàn bộ", "Đăng kí tạm trú","Đăng kí tạm vắng","Khai tử");
		tinhTrangList.setItems(tinhTrangOptions);
		tinhTrangList.setValue("Toàn bộ");
		tuNamField.setText("0");
		denNamField.setText("3000");
		denTuoiField.setText("100");
		tuTuoiField.setText("0");
		
		
		
		 
		
	}
	
	public static void main(String[] args) {
		ThongKeController c = new ThongKeController();
		String query = c.statisticNhanKhauQuery(0, 100, "Nam", "Toàn bộ", 0, 3000);
		System.out.println(query);
	}

}
