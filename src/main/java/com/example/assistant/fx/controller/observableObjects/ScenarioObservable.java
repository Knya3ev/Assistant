package com.example.assistant.fx.controller.observableObjects;


import com.example.assistant.model.Scenario;
import com.example.assistant.service.ScenarioService;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScenarioObservable {

    private final ScenarioService scenarioService;

    private final ObservableList<Scenario> observable = FXCollections.observableArrayList();


    public void refresh(){
        observable.clear();
        observable.addAll(scenarioService.findAll());
    }

    public void remove(Scenario scenario) {
        log.info("Scenario removed: {}", scenario.getName());
        scenarioService.delete(scenario);
        observable.remove(scenario);
    }

    public void clear() {
        observable.clear();
    }


    public void connect(ListView<Scenario> listView) {

        observable.addListener((ListChangeListener<Scenario>) change -> {
            while (change.next()) {

                if (change.wasAdded()) {
                    change.getAddedSubList()
                            .forEach(scenario -> {
                                listView.getItems().add(scenario);
                            });
                }

                if (change.wasRemoved()) {
                        change.getRemoved()
                                .forEach(scenario -> listView.getItems().remove(scenario));
                }
            }
        });
    }
}
