package com.example.assistant.comanda;


import com.example.assistant.fx.controller.observableObjects.ScenarioObservable;
import com.example.assistant.model.Operation;
import com.example.assistant.model.Scenario;
import com.example.assistant.service.ScenarioService;
import javafx.application.Platform;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Component
@RequiredArgsConstructor
@Scope(value = "prototype")
public class OperationsOrchestrator {

    private List<Operation> operations;
    private WebDriver driver;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final ScenarioService scenarioService;
    private final ScenarioObservable scenarioObservable;
    private final AtomicReference<List<ExecuteOperation>> executeOperation = new AtomicReference<>();


    public void executeScenario() {
        Runnable task = () -> {
            Scenario scenario = ExecuteOperation.getScenario(this.operations);

            try {

                executeOperation.set(ExecuteOperation.getExecuteOperation(this.operations, this.driver));
                executeOperation.get().forEach(ExecuteOperation::execute);


            } catch (RuntimeException e) {
                log.error(e.getMessage(), e);
                scenarioService.pushErrorMessage(scenario.getId(), e.getMessage().length() < 255 ? e.getMessage() : "error");
                driver.quit();


            } finally {
                log.info("FINALLY --- Stopping scenario in thread " + Thread.currentThread().getName());
                scenarioService.toggle(scenario);
                Platform.runLater(scenarioObservable::refresh);
                scenario = scenarioService.get(scenario.getId());

                if (scenario.getCountRestarts() > 0 && scenario.getErrorMessage() != null){
                    log.info("scenario restart");
                    scenarioService.updateCountRestart(scenario, scenario.getCountRestarts()- 1);
                    scenarioService.toggle(scenario);
                    scenarioService.startScenario(scenario.getId());
                    Platform.runLater(scenarioObservable::refresh);
                }

            }
            executorService.shutdown();
        };


        executorService.submit(task);
    }

    public void close() {

        executeOperation.get().forEach(ExecuteOperation::close);

        executorService.shutdown();
        scenarioObservable.refresh();
        driver.quit();
    }


    public OperationsOrchestrator driver(WebDriver driver) {
        this.driver = driver;
        return this;
    }

    public OperationsOrchestrator operations(List<Operation> operations) {
        this.operations = operations;
        return this;
    }

}

