package ru.study.invisibledesktop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import ru.study.invisibledesktop.InvisibleApplication;
import ru.study.invisibledesktop.core.InvisibleMain;
import ru.study.invisibledesktop.core.converters.TextConverter;
import ru.study.invisibledesktop.core.objects.Session;

public class AuthController implements TextConverter {

    @FXML
    public Button createButton;
    @FXML
    public TextField groupField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Button connectButton;

    @FXML
    public Label errorField;
    @FXML
    private Label welcomeText;

    @FXML
    private PasswordField groupPasswordField;



    @FXML
    protected void onConnect() {
        String password = groupPasswordField.getText().trim();

        if(password.isEmpty()) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText("Ошибка!");
            a.setContentText("Пароль не должен быть пустым!");
            a.show();

            return;
        }

        if(password.length() < 3) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText("Ошибка!");
            a.setContentText("Пароль не должен быть меньше трёх символов!");
            a.show();

            return;
        }

        Session session = InvisibleMain.connect(password);

        InvisibleApplication.setGui("chat.fxml", "Invisible | Чат", fxmlLoader -> {
            ChatController controller = fxmlLoader.getController();

            controller.session = session;

            session.setMessageListener(fxmlLoader.getController());
            session.getMessageListener().onMessage("ВЫ ПОДКЛЮЧИЛИСЬ К ГРУППЕ");
        });

    }

    public void load() {
        welcomeText.setText("С возвращением, " + formatID(InvisibleMain.profile.getId()) + "!");

        System.out.println(formatID(InvisibleMain.profile.getId()));
    }
}