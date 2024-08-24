package com.example.assistant.fx.controller;

import com.example.assistant.fx.controller.observableObjects.ScenarioObservable;
import com.example.assistant.fx.state.ScenarioState;
import com.example.assistant.fx.utils.FxUtils;
import com.example.assistant.fx.utils.RootFX;
import com.example.assistant.model.Scenario;
import com.example.assistant.service.ScenarioService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartController {

    private final ScenarioService scenarioService;

    private final RootFX rootFX;

    private final ScenarioState scenarioState;

    private final ScenarioObservable scenarioObservable;

    @FXML
    public ListView<Scenario> listViewScenario;

    @FXML
    protected void createScenario(ActionEvent event) {
        rootFX.goToCreateOrEditScenarioView(event);
    }

    @FXML
    public void initialize() {

        scenarioObservable.connect(listViewScenario);
        scenarioObservable.refresh();

        listViewScenario.setCellFactory(lv -> new ListCell<Scenario>() {
            private final ComboBox<Integer> comboBox = new ComboBox<>();
            private final Button edit = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            private final Button run = new Button();

            {

                run.setOnAction(event -> {
                    Scenario scenario = getItem();
                    scenarioService.toggle(scenario);
                    scenarioService.startScenario(scenario.getId());
                    scenarioObservable.refresh();
                });

                edit.setOnAction(event -> {
                    Scenario scenario = getItem();
                    scenarioState.setScenarioId(scenario.getId());
                    rootFX.goToCreateOrEditScenarioView(event);
                });

                deleteButton.setOnAction(event -> {
                    Scenario scenario = getItem();
                    scenarioObservable.remove(scenario);
                    scenarioObservable.refresh();
                });

            }

            @Override
            protected void updateItem(Scenario scenario, boolean empty) {
                super.updateItem(scenario, empty);

                if (empty || scenario == null) {
                    setText(null);
                    setGraphic(null);
                } else {

                    comboBox.getItems().clear();
                    comboBox.getItems().addAll(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
                    comboBox.setValue(scenario.getCountRestarts());

                    comboBox.valueProperty().addListener((obs, oldValue, newValue) -> {

                        if (newValue != null && oldValue != null) {
                            if (scenario.getId() != null) {
                                scenarioService.updateCountRestart(scenario, newValue);
                            }
                            if (oldValue == scenario.getCountRestarts()) {
                                scenario.setCountRestarts(newValue);
                            }

                        }
                    });



                    run.setText(scenario.isRun() ? "Stop" : "Run");

                    HBox hBox = FxUtils.getHBoxForScenario(scenario,comboBox, run, edit, deleteButton);
                    setGraphic(hBox);
                }
            }
        });

        scenarioObservable.refresh();
    }

    @FXML
    public void updateList(ActionEvent event) {
        scenarioObservable.refresh();
    }
}
