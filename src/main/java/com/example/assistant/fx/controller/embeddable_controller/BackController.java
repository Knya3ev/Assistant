package com.example.assistant.fx.controller.embeddable_controller;

import com.example.assistant.fx.controller.observableObjects.OperationObservable;
import com.example.assistant.fx.controller.observableObjects.TrackingObservable;
import com.example.assistant.model.Back;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BackController {
    private final TrackingObservable trackingObservable;
    private final OperationObservable operationObservable;

    @FXML
    private ComboBox<Integer> timeoutField;
    @FXML
    private ChoiceBox<String> choiceBoxTracking;

    @FXML
    public void initialize() {
        timeoutField.getItems().addAll(1, 2, 3, 4, 5);
        timeoutField.valueProperty().set(2);

        trackingObservable.connect(choiceBoxTracking);
    }


    public void addBack(ActionEvent event) {
        Back back = new Back();
        trackingObservable.addTrackingForOperation(back, choiceBoxTracking);
        operationObservable.addOperation(back);
    }
}
