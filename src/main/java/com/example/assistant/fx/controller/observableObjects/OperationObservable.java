package com.example.assistant.fx.controller.observableObjects;

import com.example.assistant.fx.state.ScenarioState;
import com.example.assistant.model.Operation;
import com.example.assistant.service.OperationService;
import com.example.assistant.service.ScenarioService;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Getter
@Component
@RequiredArgsConstructor
public class OperationObservable {
    private final ScenarioState scenarioState;
    private final ScenarioService scenarioService;
    private final OperationService operationService;

    private final AtomicInteger orderingOperation = new AtomicInteger(0);

    private final ObservableList<Operation> observable = FXCollections.observableArrayList();

    public void addOperation(Operation operation) {
        Long scenarioId = scenarioState.getScenarioId();

        if (operation.getOrderOperation() == 0) {
            operation.setOrderOperation(orderingOperation.incrementAndGet());
        }

        if (scenarioId != null) {
            log.info("OperationObservable: Operation add for scenario {}", operation.getName());
            scenarioService.addOperation(scenarioId, operation);
        }
        observable.add(operation);

        refreshObservableList();
    }

    public void addAll(List<Operation> operations) {
        orderingOperation.set(operations.size());
        observable.addAll(operations);
    }

    public void remove(Operation operation) {
        observable.remove(operation);
    }

    public void clear() {
        orderingOperation.set(0);
        observable.clear();
    }

    public void refreshObservableList() {
        List<Operation> list = new ArrayList<>(observable);

        list.sort(Comparator.comparingInt(Operation::getOrderOperation));

        list = addEndPointForOperation(list);
        updateEndPointForOperation(list);

        observable.clear();
        observable.addAll(list);
    }

    private List<Operation> addEndPointForOperation(List<Operation> list) {
        int maxOrderOperation = list.stream()
                .filter(operation -> operation.getTracking() == null)
                .mapToInt(Operation::getOrderOperation).max().orElse(0);

        log.info("addEndPointForOperation: maxOrderOperation={}", maxOrderOperation);
        log.info("addEndPointForOperation: list ={}", list.size());
        return list.stream()
                .peek(operation -> operation.setEndPoint(false))
                .peek(operation -> {
                    if (operation.getOrderOperation() == maxOrderOperation) {
                        operation.setEndPoint(true);
                    }
                })
                .toList();
    }

    private void updateEndPointForOperation(List<Operation> operationList) {
        operationList.stream()
                .filter(operation -> operation.getId() != null)
                .forEach(operationService::updateEndPoint);
    }

    public void connect(ListView<Operation> listView) {

        observable.addListener((ListChangeListener<Operation>) change -> {
            while (change.next()) {

                if (change.wasAdded()) {
                    change.getAddedSubList()
                            .forEach(operation -> {
                                listView.getItems().add(operation);
                            });
                }

                if (change.wasRemoved()) {
                    change.getRemoved()
                            .forEach(operation -> listView.getItems().remove(operation));

                }

            }
        });
    }
}
