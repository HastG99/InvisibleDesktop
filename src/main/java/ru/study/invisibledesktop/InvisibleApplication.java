package ru.study.invisibledesktop;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.study.invisibledesktop.controller.AuthController;
import ru.study.invisibledesktop.core.InvisibleMain;

import java.awt.*;
import java.io.IOException;
import java.util.function.Consumer;

public class InvisibleApplication extends Application {

    public static Stage stage;


    @Override
    public void start(Stage stage) throws IOException {
        InvisibleApplication.stage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(InvisibleApplication.class.getResource("authorization.fxml"));


        Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
        Scene scene = new Scene(fxmlLoader.load(), resolution.getWidth(), resolution.getHeight());

        stage.setTitle("Invisible | Добро пожаловать");
        stage.setScene(scene);

        stage.setOnCloseRequest(windowEvent -> {
            Platform.exit();
            System.exit(0);
        });

        AuthController authController = fxmlLoader.getController();
        authController.load();

        stage.show();
    }


    public static void setGui(String path, String title, Consumer<FXMLLoader> callback) {
        FXMLLoader fxmlLoader = new FXMLLoader(InvisibleApplication.class.getResource(path));

        Platform.runLater(() -> {
            stage.setTitle(title);

            try {
                stage.getScene().setRoot(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            callback.accept(fxmlLoader);

            stage.show();
        });
    }

    public static void main(String[] args) {
        if(!InvisibleMain.init())
            return;

        launch();
    }
}