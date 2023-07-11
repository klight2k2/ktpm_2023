package com.ktpm.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

import static com.ktpm.constants.DBConstants.*;
import static com.ktpm.utils.Utils.createDialog;
import static com.ktpm.utils.Utils.hashPassword;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
    @FXML
    private TextField signUpUsername, signUpPassword;
    @FXML
    private RadioButton isAdmin, isOfficer;
    private final ToggleGroup toggleRole = new ToggleGroup();

    public void handleSignUp() {
        String inputUsername = signUpUsername.getText();
        String inputPassword = signUpPassword.getText();
        String role = "";

        if (inputUsername.trim().equals("") || inputPassword.trim().equals("")) {
            createDialog(
                    Alert.AlertType.WARNING,
                    "Tạo tài khoản mới",""
                   , "Vui lòng nhập đủ username và password!"
            );

        }   else {
            if (!isOfficer.isSelected() && !isAdmin.isSelected()) {
                createDialog(
                        Alert.AlertType.WARNING,"",
                        "Tạo tài khoản mới", "Vui lòng chọn role cho username!"
                );
            }   else {
                if (isOfficer.isSelected()) role = "canbo";
                if (isAdmin.isSelected()) role = "totruong";
                String CREATE_QUERY = "INSERT INTO user (username, password, role) VALUES (?,?,?)";
                try {
                    Connection conn = DriverManager.getConnection(DATABASE, USERNAME, PASSWORD);
                    PreparedStatement preparedStatement = conn.prepareStatement(CREATE_QUERY);
                    preparedStatement.setString(1, inputUsername);
                    preparedStatement.setString(2, hashPassword(inputPassword));
                    preparedStatement.setString(3, role);
                    int result = preparedStatement.executeUpdate();
                    if (result == 1) {
                        signUpPassword.clear();
                        signUpUsername.clear();
                        isAdmin.setSelected(false);
                        isOfficer.setSelected(false);
                        createDialog(
                                Alert.AlertType.CONFIRMATION,
                                "Thành công",
                                "", "Đăng ký người dùng mới thành công!"
                        );
                    }   else {
                        createDialog(
                                Alert.AlertType.ERROR,
                                "Thất bại",
                                "", "Đăng ký người dùng mới thất bại!"
                        );
                    }
                }   catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isAdmin.setToggleGroup(toggleRole);
        isOfficer.setToggleGroup(toggleRole);
    }
}
