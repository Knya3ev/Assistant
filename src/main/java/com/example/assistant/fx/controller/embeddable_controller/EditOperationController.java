package com.example.assistant.fx.controller.embeddable_controller;

import com.example.assistant.fx.controller.observableObjects.OperationObservable;
import com.example.assistant.fx.controller.observableObjects.TrackingObservable;
import com.example.assistant.fx.state.OperationState;
import com.example.assistant.fx.state.ScenarioState;
import com.example.assistant.fx.utils.RootFX;
import com.example.assistant.model.emp_type.Click;
import com.example.assistant.model.emp_type.InputText;
import com.example.assistant.model.emp_type.OpenUrl;
import com.example.assistant.model.Operation;
import com.example.assistant.model.emp_type.Tracking;
import com.example.assistant.service.OperationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EditOperationController {

    private final ScenarioState scenarioState;
    private final OperationState operationState;

    private final OperationService operationService;
    private final RootFX rootFX;

    private final TrackingObservable trackingObservable;
    private final OperationObservable operationObservable;

    @FXML
    private Label typeOperation;
    @FXML
    private TextField name;
    @FXML
    private TextField url;
    @FXML
    private TextField selector;
    @FXML
    private TextArea inputText;
    @FXML
    private Label forTrackingLabel;
    @FXML
    private ChoiceBox<String> choiceBoxTracking;
    @FXML
    private CheckBox isClicked;
    @FXML
    private TextField numberOfIteration;


    @FXML
    public void initialize() {

        clear();
        trackingObservable.connect(choiceBoxTracking);

        if (operationState.getOperation() != null) {
            Operation operation = operationState.getOperation();
            typeOperation.setText(operation.getType().getCode());
            name.setText(operation.getName() != null ? operation.getName() : "");

            if (operation.getTracking() != null) choiceBoxTracking.setValue(operation.getTracking().getName());

            switch (operation.getType()) {

                case OPEN -> {
                    OpenUrl openUrl = (OpenUrl) operation;
                    url.setText(openUrl.getUrl());
                    forTrackingLabel.setVisible(false);
                    choiceBoxTracking.setVisible(false);
                }
                case TRACKING -> {
                    Tracking tracking = (Tracking) operation;
                    selector.setText(tracking.getSelector() != null ? tracking.getSelector() : "");
                    numberOfIteration.setText(String.valueOf(tracking.getNumberOfIterations()));
                    isClicked.setSelected(tracking.isClicked());

                    forTrackingLabel.setVisible(false);
                    choiceBoxTracking.setVisible(false);
                }
                case CLICK -> {
                    forTrackingLabel.setVisible(true);
                    choiceBoxTracking.setVisible(true);
                    Click click = (Click) operation;
                    selector.setText(click.getSelector() != null ? click.getSelector() : "");


                }
                case INSERT -> {
                    InputText input = (InputText) operation;
                    forTrackingLabel.setVisible(true);
                    choiceBoxTracking.setVisible(true);
                    isClicked.setSelected(input.isPush());

                    InputText insert = (InputText) operation;
                    selector.setText(insert.getSelector() != null ? insert.getSelector() : "");
                    inputText.setText(insert.getInputText() != null ? insert.getInputText() : "");
                }
            }

        }


    }

    private void clear() {
        name.setText("");
        url.setText("");
        selector.setText("");
        inputText.setText("");
        typeOperation.setText("");
        forTrackingLabel.setText("");
        choiceBoxTracking.getItems().clear();
        isClicked.setSelected(false);
        numberOfIteration.setText("");

    }

    @FXML
    public void saveOperation(ActionEvent event) {
        if (operationState.getOperation() != null) {
            Operation operation = operationState.getOperation();

            operationObservable.remove(operation);

            operation.setName(name.getText());
            operation = trackingObservable.addTrackingForOperation(operation, choiceBoxTracking);

            switch (operation.getType()) {
                case OPEN -> {
                    OpenUrl openUrl = (OpenUrl) operation;
                    openUrl.setUrl(url.getText());
                }
                case TRACKING -> {
                    Tracking tracking = (Tracking) operation;
                    tracking.setSelector(selector.getText());
                    tracking.setClicked(isClicked.isSelected());
                    tracking.setNumberOfIterations(Integer.parseInt(numberOfIteration.getText()));
                }
                case CLICK -> {
                    Click click = (Click) operation;
                    click.setSelector(selector.getText());
                }
                case INSERT -> {
                    InputText insert = (InputText) operation;
                    insert.setSelector(selector.getText());
                    insert.setInputText(inputText.getText());
                    insert.setPush(isClicked.isSelected());
                }

                default -> throw new IllegalStateException("Unexpected value: " + operation.getType());
            }

            operationObservable.addOperation(operation);
        }
        log.info("Edited operation successfully list size {}", operationObservable.getObservable().size());
        updatePage(event);
    }

    private void updatePage(ActionEvent event) {
        trackingObservable.clear();
    }

}
