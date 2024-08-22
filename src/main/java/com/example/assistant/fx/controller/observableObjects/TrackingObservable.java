package com.example.assistant.fx.controller.observableObjects;


import com.example.assistant.model.Operation;
import com.example.assistant.model.Tracking;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TrackingObservable {
    private final ObservableList<Tracking> observable = FXCollections.observableArrayList();


    public ObservableList<Tracking> getObservable() {
        return this.observable;
    }

    public void addTracking(Tracking tracking) {
        log.info("Tracking added: {}", tracking.getName());
        observable.add(tracking);
    }

    public void remove(Tracking tracking) {
        log.info("Tracking removed: {}", tracking.getName());
        observable.remove((Tracking) tracking);
    }

    public void clear() {
        observable.clear();
    }


    public Operation addTrackingForOperation(Operation operation, ChoiceBox<String> choiceBox) {
        if (choiceBox.valueProperty().getValue() != null) {
            observable.stream()
                    .filter(t -> t.getName().equals(choiceBox.getValue()))
                    .findFirst()
                    .ifPresentOrElse(
                            tracking -> {
                                tracking.getOperations().add(operation);
                                operation.setTracking(tracking);
                            },
                            () -> {
                                throw new RuntimeException("Tracking not found");
                            });

        }
        return operation;
    }

    private void loadTrackingForChoiceBox(ChoiceBox<String> choiceBox) {
        choiceBox.getItems().clear();
        choiceBox.getItems().addAll(
                observable.stream()
                        .map(Operation::getName)
                        .toList());
    }

    public void connect(ChoiceBox<String> choiceBox) {
        loadTrackingForChoiceBox(choiceBox);

        observable.addListener((ListChangeListener<Tracking>) change -> {
            while (change.next()) {

                if (change.wasAdded()) {
                    change.getAddedSubList().stream()
                            .map(Tracking::getName)
                            .forEach(t -> {
                                if (!choiceBox.getItems().contains(t)) {
                                    choiceBox.getItems().add(t);
                                }
                            });
                }

                if (change.wasRemoved()) {
                    change.getRemoved().stream()
                            .map(Tracking::getName)
                            .forEach(t -> choiceBox.getItems().remove(t));
                }

            }
        });
    }
}
