package ru.study.invisibledesktop.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import ru.study.invisibledesktop.core.converters.TextConverter;
import ru.study.invisibledesktop.core.objects.MessageListener;
import ru.study.invisibledesktop.core.objects.Session;

public class ChatController implements TextConverter, MessageListener {

    public Session session;

    @FXML
    public TextField messageField;

    @FXML
    public Button sendButton;

    @FXML
    public TextArea chatArea;

    @FXML
    public void onSend() {
        if(messageField.getText().isEmpty())
            return;

        if(session != null)
            session.sendMessage(messageField.getText());
        else
            onMessage("Произошла непредвиденная ошибка!");

        messageField.setText("");
    }


    @Override
    public void onMessage(long senderId, String message) {
        chatArea.appendText(String.format("[%s] => %s\n", formatID(senderId), message));
    }

    @Override
    public void onMessage(String message) {
        chatArea.appendText(String.format("[СИСТЕМА] => %s\n", message));
    }
}
