package com.example.assistant.service;

import com.example.assistant.comanda.OperationsOrchestrator;
import com.example.assistant.fx.controller.observableObjects.ScenarioObservable;
import com.example.assistant.model.Operation;
import com.example.assistant.model.Scenario;
import com.example.assistant.repository.ScenarioRepository;
import javafx.scene.control.ListView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScenarioService {

    private final ScenarioRepository scenarioRepository;
    private final OperationService operationService;
    private final SeleniumService seleniumService;
    private OperationsOrchestrator orchestrator;

    @Transactional
    public void startScenario(Long scenarioId) {
        Scenario scenario = get(scenarioId);
        scenario.setErrorMessage(null);

        if (scenario.isRun()) {
            log.info("Starting scenario: {}", scenario.getName());
            initializationOrchestrator(scenario.getOperations());

            orchestrator.executeScenario();


        } else {
            log.info("Scenario stop running: {}", scenario.getName());
            if (orchestrator != null) {
                orchestrator.close();
            }
        }
    }


    @Lookup
    public OperationsOrchestrator getOrchestrator() {
        return null;
    }

    private void initializationOrchestrator(List<Operation> operations) {
        orchestrator = getOrchestrator();
        orchestrator
                .driver(seleniumService.getDriver())
                .operations(operations);
    }


    public List<Scenario> findAll() {
        return scenarioRepository.findAll();
    }

    public Scenario get(Long scenarioId) {
        return scenarioRepository.findById(scenarioId).orElseThrow(() -> new RuntimeException("Scenario not found"));
    }

    public Scenario save(String nameScenario) {
        return scenarioRepository.save(Scenario.builder().name(nameScenario).build());
    }


    @Transactional
    public void saveScenario(String nameScenario, List<Operation> operations) {
        log.info("Saving scenario: {}", nameScenario);
        log.info("Saving operations count: {}", operations.size());

        Scenario scenario = save(nameScenario);
        List<Operation> savedOperation =
                operations.stream()
                        .peek(operation -> {
                            operationService.save(operation);
                            operation.setScenario(scenario);
                        }).toList();

        scenario.setOperations(savedOperation);
    }

    @Transactional
    public void toggle(Scenario scenario) {
        log.info("Toggle scenario: {}", scenario.getName());
        log.info("Toggle after is run: {}", scenario.isRun());
        scenarioRepository.toggleIsRunnable(scenario.getId(), scenario.isRun() ? Boolean.FALSE : Boolean.TRUE);
        log.info("Toggle before is run: {}", get(scenario.getId()).isRun());
    }

    public void delete(Scenario scenario) {
        scenarioRepository.delete(scenario);
    }

    @Transactional
    public void addOperation(Long scenarioId, Operation operation) {
        Scenario scenario = get(scenarioId);
        operation.setScenario(scenario);
        scenario.getOperations().add(operationService.save(operation));
    }

    @Transactional
    public void updateNameScenario(Long scenarioId, String newName) {
        Scenario scenario = get(scenarioId);
        if (!scenario.getName().equals(newName)) {
            scenario.setName(newName);
        }
    }

    @Transactional
    public void pushErrorMessage(Long scenarioId, String errorMessage) {
        Scenario scenario = get(scenarioId);
        scenario.setErrorMessage(errorMessage);
    }

    @Transactional
    public void updateCountRestart(Scenario scenario, Integer newValue) {
        log.info("scenario id {} updated", scenario.getId());
        scenarioRepository.updateCountRestartScenario(scenario.getId(), newValue);
    }
}
