module ru.study.invisibledesktop {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens ru.study.invisibledesktop to javafx.fxml;
    exports ru.study.invisibledesktop;
    exports ru.study.invisibledesktop.controller;
    opens ru.study.invisibledesktop.controller to javafx.fxml;
}