package com.example.assistant.fx.controller.embeddable_controller;

import com.example.assistant.fx.controller.observableObjects.OperationObservable;
import com.example.assistant.fx.utils.ErrorMessage;
import com.example.assistant.model.emp_type.OpenUrl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OpenUrlController {

    private final OperationObservable operationObservable;

    @FXML
    private TextField urlField;

    @FXML
    private Label errorLabel;


    public void createUrl(ActionEvent event) {
        String url = urlField.getText();

        if (isValidUrl(url)) {
            errorLabel.setVisible(false);
            urlField.setText("");
            operationObservable.addOperation(
                    OpenUrl.builder()
                            .url(url)
                            .build()
            );
        } else {
            errorLabel.setText(ErrorMessage.URL_FIELD);
            errorLabel.setVisible(true);
            errorLabel.setAlignment(Pos.CENTER);
        }
    }

    private boolean isValidUrl(String url) {
        return url != null && (url.startsWith("http://") || url.startsWith("https://"));
    }

}
