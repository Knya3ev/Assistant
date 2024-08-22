package com.example.assistant.fx.controller.embeddable_controller;

import com.example.assistant.fx.controller.OperationController;
import com.example.assistant.fx.controller.observableObjects.OperationObservable;
import com.example.assistant.fx.controller.observableObjects.TrackingObservable;
import com.example.assistant.fx.utils.ErrorMessage;
import com.example.assistant.fx.utils.FxUtils;
import com.example.assistant.model.Click;
import com.example.assistant.model.Operation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClickController {
    @FXML
    private ComboBox<Integer> timeoutField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField elementField;
    @FXML
    private Label errorNameField;
    @FXML
    private Label errorElementField;
    private final TrackingObservable trackingObservable;
    private final OperationObservable operationObservable;

    @FXML
    private ChoiceBox<String> choiceBoxTracking;

    @FXML
    public void initialize() {
        timeoutField.getItems().addAll(1, 2, 3, 4, 5);
        timeoutField.valueProperty().set(2);

        trackingObservable.connect(choiceBoxTracking);
    }


    public void addClick(ActionEvent event) {

        FxUtils.clearErrorMessageForLabel(errorElementField, errorNameField);

        if (isValidNameField(nameField) && isValidNameElement(elementField)) {
            Click click = new Click();

            click.setName(nameField.getText());
            click.setSelector(elementField.getText());

            trackingObservable.addTrackingForOperation(click,choiceBoxTracking);

            operationObservable.addOperation(click);
        }

    }

    private boolean isValidNameField(TextField textField) {
        return FxUtils.isValidLabelForEmptyText(textField, errorNameField, ErrorMessage.NAME_FIELD);
    }

    private boolean isValidNameElement(TextField textField) {
        return FxUtils.isValidLabelForEmptyText(textField, errorElementField, ErrorMessage.ELEMENT_FIELD);
    }
}
