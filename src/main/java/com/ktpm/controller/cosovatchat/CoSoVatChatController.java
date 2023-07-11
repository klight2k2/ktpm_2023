package com.ktpm.controller.cosovatchat;

import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Pagination;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import static com.ktpm.constants.DBConstants.*;
import static com.ktpm.constants.FXMLConstants.*;
import static com.ktpm.utils.Utils.createDialog;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import com.ktpm.HomeApplication;
import com.ktpm.controller.KhaiTuController;
import com.ktpm.model.CoSoVatChat;
import com.ktpm.services.CoSoVatChatServices;
import com.ktpm.utils.ViewUtils;

public class CoSoVatChatController implements Initializable {
    @FXML
    private AnchorPane basePane;
    
    @FXML
    private Button addBtn;
    
    public static boolean isSelected = false;
    
    @FXML
    private TableView<CoSoVatChat> tableView;

    @FXML
    private TableColumn indexColumn;

    @FXML
    private TableColumn<CoSoVatChat, String> tenDoDungColumn, tenLoaiDoDungColumn, tinhTrangColumn;

    @FXML
    private TableColumn<CoSoVatChat, Integer> maDoDungColumn;

    @FXML
    private Pagination pagination;

    private ObservableList<CoSoVatChat> coSoVatChatList = FXCollections.observableArrayList();

    private Connection conn;

    private PreparedStatement preparedStatement = null;

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        try {
            conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
            ResultSet result = CoSoVatChatServices.getAllFacility(conn);
            while (result.next()) {
                coSoVatChatList.add(new CoSoVatChat(result.getInt("MaDoDung"), result.getString("TenDoDung"),
                        result.getString("TinhTrang"), result.getString("LoaiDoDung")));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int soDu = coSoVatChatList.size() % ROWS_PER_PAGE;
        if (soDu != 0) pagination.setPageCount(coSoVatChatList.size() / ROWS_PER_PAGE + 1);
        else pagination.setPageCount(coSoVatChatList.size() / ROWS_PER_PAGE);
        pagination.setMaxPageIndicatorCount(5);
        pagination.setPageFactory(this::createTableView);

        tableView.setRowFactory(tv -> {
            TableRow<CoSoVatChat> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    // Perform actions with rowData
                    try {
                        try {
							detail(event);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            return row;
        });
    }
    
    @FXML
    void onClick(MouseEvent event) {
    	CoSoVatChat selected = tableView.getSelectionModel().getSelectedItem();
    	
    }
    

    public void add(MouseEvent event) throws IOException, SQLException {
    	Stage stage = new Stage();
    	FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(DETAIL_CO_SO_VAT_CHAT_ADD_FXML));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Them co so vat chat");
//    	CoSoVatChatDeTailController controller = new CoSoVatChatDeTailController();
//    	loader.setController(controller);
        CoSoVatChatDeTailController controller = loader.getController();
        controller.getModal();
        stage.getIcons().add(new Image(HomeApplication.class.getResourceAsStream(ICON)));
        stage.setScene(scene);
        stage.show();
        
    }
    
    public void reload() throws IOException {
    	ViewUtils viewUtils = new ViewUtils();
    	viewUtils.changeAnchorPane(basePane, CO_SO_VAT_CHAT_VIEW_FXML);
    }
    

    public void delete() {
        CoSoVatChat selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) createDialog(Alert.AlertType.WARNING,
                "Cảnh báo",
                "", "Bạn vui lòng chọn 1 mục để tiếp tục");
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận xoá ");
            alert.setContentText("Bạn muốn xoá nội dung này?");
            ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(okButton, noButton);
            alert.showAndWait().ifPresent(type -> {
                if (type == okButton) {
                    try {
                        conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
                        int result = CoSoVatChatServices.deleteFacility(conn, selected);
                        if (result == 1)
                            createDialog(Alert.AlertType.INFORMATION, "Xoá thành công!", "", "Bạn đã xóa thành công!");
                        else createDialog(Alert.AlertType.WARNING, "Thông báo", "", "Có lỗi, vui lòng thử lại!");
                        ViewUtils viewUtils = new ViewUtils();
                        viewUtils.changeAnchorPane(basePane, CO_SO_VAT_CHAT_VIEW_FXML);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

    public void detail(MouseEvent event) throws IOException, SQLException {
    	Stage stage = new Stage();
		FXMLLoader fxmlLoader = new FXMLLoader(HomeApplication.class.getResource(DETAIL_CO_SO_VAT_CHAT_VIEW_FXML));
		Scene scene;
		scene = new Scene(fxmlLoader.load());
		// TODO Auto-generated catch block
		stage.setTitle("Cập nhật cơ sở vật chất");
		CoSoVatChatDeTailController controller = fxmlLoader.getController();
		
		stage.getIcons().add(new Image(HomeApplication.class.getResourceAsStream(ICON)));
		stage.setScene(scene);
		stage.show();
        CoSoVatChat selected = tableView.getSelectionModel().getSelectedItem();
        
        if (selected == null)
            createDialog(Alert.AlertType.WARNING, "Thông báo", "", "Vui lòng chọn 1 mục để tiếp tục");
        else {
            controller.setCoSoVatChat(selected);
//            controller.hide_add_btn();
            controller.setTitle("Cập nhật cơ sở vật chất");
            stage.setScene(scene);
            stage.show();
        }

    }

    public Node createTableView(int pageIndex) {
        indexColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<CoSoVatChat, CoSoVatChat>, ObservableValue<CoSoVatChat>>) p -> new ReadOnlyObjectWrapper(p.getValue()));
        indexColumn.setCellFactory(new Callback<TableColumn<CoSoVatChat, CoSoVatChat>, TableCell<CoSoVatChat, CoSoVatChat>>() {
            @Override
            public TableCell<CoSoVatChat, CoSoVatChat> call(TableColumn<CoSoVatChat, CoSoVatChat> param) {
                return new TableCell<CoSoVatChat, CoSoVatChat>() {
                    @Override
                    protected void updateItem(CoSoVatChat item, boolean empty) {
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
        maDoDungColumn.setCellValueFactory(new PropertyValueFactory<CoSoVatChat, Integer>("maDoDung"));
        tenDoDungColumn.setCellValueFactory((new PropertyValueFactory<CoSoVatChat, String>("tenDoDung")));
        tenLoaiDoDungColumn.setCellValueFactory(new PropertyValueFactory<CoSoVatChat, String>("tenLoaiDoDung"));
        tinhTrangColumn.setCellValueFactory(new PropertyValueFactory<CoSoVatChat, String>("tinhTrang"));
        
        int lastIndex = 0;
        int displace = coSoVatChatList.size() % ROWS_PER_PAGE;
        if (displace > 0) {
            lastIndex = coSoVatChatList.size() / ROWS_PER_PAGE;
        } else {
            lastIndex = coSoVatChatList.size() / ROWS_PER_PAGE - 1;
        }
        if (coSoVatChatList.isEmpty()) tableView.setItems(FXCollections.observableArrayList(coSoVatChatList));
        else {
            if (lastIndex == pageIndex && displace > 0) {
                tableView.setItems(FXCollections.observableArrayList(coSoVatChatList.subList(pageIndex * ROWS_PER_PAGE, pageIndex * ROWS_PER_PAGE + displace)));
            } else {
                tableView.setItems(FXCollections.observableArrayList(coSoVatChatList.subList(pageIndex * ROWS_PER_PAGE, pageIndex * ROWS_PER_PAGE + ROWS_PER_PAGE)));
            }
        }
        return tableView;
    }

    @FXML
    private TextField searchTextField;

    public void search() {
        FilteredList<CoSoVatChat> filteredData = new FilteredList<>(coSoVatChatList, p -> true);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(coSoVatChat -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (coSoVatChat.getTenDoDung().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else {
                    return false;
                }
            });
            int soDu = filteredData.size() % ROWS_PER_PAGE;
            if (soDu != 0) pagination.setPageCount(filteredData.size() / ROWS_PER_PAGE + 1);
            else pagination.setPageCount(filteredData.size() / ROWS_PER_PAGE);
            pagination.setMaxPageIndicatorCount(5);
            pagination.setPageFactory(pageIndex -> {
                indexColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<CoSoVatChat, CoSoVatChat>, ObservableValue<CoSoVatChat>>) p -> new ReadOnlyObjectWrapper(p.getValue()));
                indexColumn.setCellFactory(new Callback<TableColumn<CoSoVatChat, CoSoVatChat>, TableCell<CoSoVatChat, CoSoVatChat>>() {
                    @Override
                    public TableCell<CoSoVatChat, CoSoVatChat> call(TableColumn<CoSoVatChat, CoSoVatChat> param) {
                        return new TableCell<CoSoVatChat, CoSoVatChat>() {
                            @Override
                            protected void updateItem(CoSoVatChat item, boolean empty) {
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
                maDoDungColumn.setCellValueFactory(new PropertyValueFactory<CoSoVatChat, Integer>("maDoDung"));
                tenDoDungColumn.setCellValueFactory((new PropertyValueFactory<CoSoVatChat, String>("tenDoDung")));
                tenLoaiDoDungColumn.setCellValueFactory(new PropertyValueFactory<CoSoVatChat, String>("tenLoaiDoDung"));
                tinhTrangColumn.setCellValueFactory(new PropertyValueFactory<CoSoVatChat, String>("tinhTrang"));
                int lastIndex = 0;
                int displace = filteredData.size() % ROWS_PER_PAGE;
                if (displace > 0) {
                    lastIndex = filteredData.size() / ROWS_PER_PAGE;
                } else {
                    lastIndex = filteredData.size() / ROWS_PER_PAGE - 1;
                }
                if (filteredData.isEmpty()) tableView.setItems(FXCollections.observableArrayList(filteredData));
                else {
                    if (lastIndex == pageIndex && displace > 0) {
                        tableView.setItems(FXCollections.observableArrayList(filteredData.subList(pageIndex * ROWS_PER_PAGE, pageIndex * ROWS_PER_PAGE + displace)));
                    } else {
                        tableView.setItems(FXCollections.observableArrayList(filteredData.subList(pageIndex * ROWS_PER_PAGE, pageIndex * ROWS_PER_PAGE + ROWS_PER_PAGE)));
                    }
                }
                return tableView;
            });
        });
    }
}
