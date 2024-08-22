package com.example.assistant.fx.controller.embeddable_controller;

import com.example.assistant.fx.controller.observableObjects.OperationObservable;
import com.example.assistant.fx.controller.observableObjects.TrackingObservable;
import com.example.assistant.fx.utils.ErrorMessage;
import com.example.assistant.fx.utils.FxUtils;
import com.example.assistant.model.Tracking;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TrackingController {

    private final TrackingObservable trackingObservable;
    private final OperationObservable operationObservable;

    @FXML
    private Label errorNameField;
    @FXML
    private Label errorElementField;
    @FXML
    private Label errorNumberIteration;

    @FXML
    private TextField nameField;
    @FXML
    private TextField elementField;
    @FXML
    private ComboBox<Integer> timeoutField;
    @FXML
    private CheckBox isClicked;
    @FXML
    private TextField numberOfIterationsField;

    @FXML
    public void initialize() {
        timeoutField.valueProperty().set(100);
        timeoutField.getItems().addAll(100, 200, 300, 400, 500);
    }

    public void addTracking(ActionEvent event) {
        FxUtils.clearErrorMessageForLabel(errorElementField, errorNameField, errorNumberIteration);

        if (isValidNameField(nameField)
            && isValidElementField(elementField)
            && isValidStringOfDigits(numberOfIterationsField)) {

            Tracking tracking = new Tracking();
            tracking.setSelector(elementField.getText());
            tracking.setName(nameField.getText());
            tracking.setTimeout(timeoutField.getValue());
            tracking.setClicked(isClicked.isSelected());
            tracking.setNumberOfIterations(Integer.parseInt(numberOfIterationsField.getText()));

            trackingObservable.getObservable().stream()
                    .filter(t -> t.getName().equals(tracking.getName()))
                    .findFirst()
                    .ifPresentOrElse(
                            t -> {
                                log.error("Tracking already exists");
                                FxUtils.visibleErrorMessage(errorNameField, ErrorMessage.TRACKER_EXISTS);
                                throw new RuntimeException("Tracking already exists");
                            },
                            () -> {
                                operationObservable.addOperation(tracking);
                                trackingObservable.addTracking(tracking);
                            });

        }
    }

    private boolean isValidNameField(TextField textField) {
        return FxUtils.isValidLabelForEmptyText(textField, errorNameField, ErrorMessage.NAME_FIELD);
    }

    private boolean isValidElementField(TextField textField) {
        return FxUtils.isValidLabelForEmptyText(textField, errorElementField, ErrorMessage.ELEMENT_FIELD);
    }

    private boolean isValidStringOfDigits(TextField textField) {
        return FxUtils.isValidStringIsDigit(textField, errorNumberIteration, ErrorMessage.NUMBER_OF_ITERATIONS_FIELD);
    }

}
