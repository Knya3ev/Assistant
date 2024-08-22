package com.example.assistant.fx.controller;

import com.example.assistant.fx.controller.embeddable_controller.EditOperationController;
import com.example.assistant.fx.controller.observableObjects.OperationObservable;
import com.example.assistant.fx.controller.observableObjects.TrackingObservable;
import com.example.assistant.fx.state.OperationState;
import com.example.assistant.fx.state.ScenarioState;
import com.example.assistant.fx.utils.FxUtils;
import com.example.assistant.fx.utils.RootFX;
import com.example.assistant.model.Operation;
import com.example.assistant.model.Scenario;
import com.example.assistant.model.Tracking;
import com.example.assistant.model.enums.OperationName;
import com.example.assistant.service.OperationService;
import com.example.assistant.service.ScenarioService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
@RequiredArgsConstructor
public class OperationController {
    private final ScenarioState scenarioState;
    private final OperationState operationState;

    private final OperationService operationService;
    private final RootFX rootFX;
    private final ScenarioService scenarioService;
    private final EditOperationController editOperationController;

    private final TrackingObservable trackingObservable;
    private final OperationObservable operationObservable;

    @FXML
    private TabPane tabPane;
    @FXML
    private Tab fieldEditOperation;
    @FXML
    private TextField nameScenarioField;
    @FXML
    private ListView<Operation> listView;


//    private final ObservableList<Operation> observableOperation = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        operationObservable.connect(listView);

        listView.setCellFactory(lv -> new ListCell<Operation>() {
            private final ComboBox<Integer> comboBox = new ComboBox<>();
            private final Spinner<Integer> spinner = new Spinner<>();
            private final Button edit = new Button("Edit");
            private final Button deleteButton = new Button("Удалить");

            {
                comboBox.setPrefWidth(70);
                spinner.setPrefWidth(60);


                spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));

                spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
                    Operation operation = getItem();

                    if (operation != null && newValue == null) {
                        spinner.getValueFactory().setValue(operation.getOrderOperation());
                    }

                    if (operation != null && newValue != null && !newValue.equals(oldValue)) {
                        updateOrderInList(operation, newValue);
                    }
                });

                edit.setOnAction(event -> {
                    Operation operation = getItem();
                    operationState.setOperation(operation);
                    editOperationController.initialize();
                    tabPane.getSelectionModel().select(fieldEditOperation);
                });

                deleteButton.setOnAction(event -> {
                    Operation operation = getItem();
                    operationObservable.remove(operation);
                    listView.getItems().remove(operation);

                    if (operation.getId() != null) {
                        operationService.delete(operation);
                    }
                    if (operation.getType() == OperationName.TRACKING) {
                        trackingObservable.remove((Tracking) operation);
                    }
                    operationObservable.refreshObservableList();
                });
//                load();
            }

            @Override
            protected void updateItem(Operation operation, boolean empty) {
                super.updateItem(operation, empty);

                if (empty || operation == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    spinner.getValueFactory().setValue(operation.getOrderOperation());

                    comboBox.getItems().clear();

                    if (operation.getType() == OperationName.TRACKING) {
                        comboBox.getItems().addAll(100, 200, 300, 400, 500);
                    } else {
                        comboBox.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
                    }

                    comboBox.setValue(operation.getTimeout());

                    comboBox.valueProperty().addListener((obs, oldValue, newValue) -> {


                        if (newValue != null && oldValue != null) {
                            if (operation.getId() != null) {
                                operationService.updateTimeout(operation, newValue);
                            }
                            if (oldValue == operation.getTimeout()) {
                                operation.setTimeout(newValue);
                            }

                        }
                    });

                    HBox hBox = FxUtils.getHBoxForTypeOperation(operation, spinner, comboBox, edit, deleteButton);

                    setGraphic(hBox);
                }
            }
        });

        this.load();
    }

    @FXML
    public void backToStart(ActionEvent event) {
        clear();
        rootFX.goToStartView(event);
    }

    private void load() {

        if (scenarioState.getScenarioId() != null) {
            Scenario scenario = getScenario();

            nameScenarioField.setText(scenario.getName());
            operationObservable.addAll(scenario.getOperations());

            scenario.getOperations().stream()
                    .filter(operation -> operation.getType() == OperationName.TRACKING)
                    .forEach(operation -> trackingObservable.addTracking((Tracking) operation));

        }
    }

    private void clear() {
        operationObservable.clear();
        trackingObservable.clear();
        listView.getItems().clear();

        scenarioState.setScenarioId(null);
        operationState.setOperation(null);
    }

    public void saveScenario(ActionEvent event) {
        Long scenarioId = scenarioState.getScenarioId();

        if (scenarioId != null) {
            scenarioService.updateNameScenario(scenarioId, nameScenarioField.getText());
        } else {
            scenarioService.saveScenario(nameScenarioField.getText(), listView.getItems());
        }
        backToStart(event);
    }

    private void updateOrderInList(Operation operation, int newOrder) {
        if (operation.getId() != null) {
            operationService.updateOrdering(operation, newOrder);
        }

        operation.setOrderOperation(newOrder);
        listView.getItems().sort(Comparator.comparingInt(Operation::getOrderOperation));
    }

    private Scenario getScenario() {
        return scenarioService.get(scenarioState.getScenarioId());
    }
}
