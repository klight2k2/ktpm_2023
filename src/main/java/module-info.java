module comz.quartermanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.prefs;
	requires javafx.base;


    opens com.ktpm to javafx.fxml;
    exports com.ktpm;
    exports com.ktpm.model;
    opens com.ktpm.model to javafx.fxml;
    exports com.ktpm.controller;
    opens com.ktpm.controller to javafx.fxml;
    exports com.ktpm.controller.nhankhau;
    opens com.ktpm.controller.nhankhau to javafx.fxml;
    exports com.ktpm.controller.sohokhau;
    opens com.ktpm.controller.sohokhau to javafx.fxml;
    exports com.ktpm.constants;
    opens com.ktpm.constants to javafx.fxml;
    exports com.ktpm.controller.cosovatchat;
    opens com.ktpm.controller.cosovatchat to javafx.fxml;
    exports com.ktpm.utils;
    opens com.ktpm.utils to javafx.fxml;
    exports com.ktpm.controller.lichhoatdong;
    opens com.ktpm.controller.lichhoatdong to javafx.fxml;
}