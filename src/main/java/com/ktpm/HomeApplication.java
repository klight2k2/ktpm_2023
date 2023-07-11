package com.ktpm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import static com.ktpm.constants.FXMLConstants.HOME_VIEW_FXML;
import static com.ktpm.constants.FXMLConstants.ICON;

import java.io.IOException;

public class HomeApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HomeApplication.class.getResource(HOME_VIEW_FXML));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Easy Manage");
    	
        stage.getIcons().add(new Image(HomeApplication.class.getResourceAsStream(ICON)));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}




