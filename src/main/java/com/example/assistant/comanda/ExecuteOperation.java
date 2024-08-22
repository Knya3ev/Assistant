package com.example.assistant.comanda;

import com.example.assistant.exception.TrackingException;
import com.example.assistant.model.Operation;
import com.example.assistant.model.Scenario;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public abstract class ExecuteOperation {

    protected final WebDriver driver;
    protected final Operation operation;
    protected volatile boolean isRunning = true;
    private volatile boolean isQuit = false;
    protected int DEFAULT_WAIT_TIME_SECONDS = 2;

    protected ExecuteOperation(WebDriver driver, Operation operation) {
        this.driver = driver;
        this.operation = operation;
    }


    protected void execute() {
        try {

            executeOperation();
            quitDriverIfIsEndPoint();

        }catch (NoSuchSessionException e) {
            log.error(e.getMessage(), e);
            sleep(5, TimeUnit.SECONDS);

            if (operation.getTracking() != null) {
                throw new TrackingException("Exception is Tracking / operation: %s".formatted(operation.getName()), e);
            }

            isQuit = true;
            log.error("driver is quit");
            driverQuit();
            throw new RuntimeException("Error -> operation name: %s".formatted(operation.getName()));
        }
    }

    void executeOperation() {

    }

    public static List<ExecuteOperation> getExecuteOperation(List<Operation> operations, WebDriver driver) {
        AtomicReference<Scenario> scenario = new AtomicReference<>();

        return operations.stream()
                .peek(operation -> {
                    if (scenario.get() == null) {
                        if (operation.getScenario() != null) {
                            scenario.set(operation.getScenario());
                        }
                    }
                })
                .map(o -> getExecuteOperation(o, driver))
                .toList();
    }

    public static Scenario getScenario(List<Operation> operations) {
        AtomicReference<Scenario> scenario = new AtomicReference<>();

        operations.forEach(operation -> {
            if (scenario.get() == null) {
                if (operation.getScenario() != null) {
                    scenario.set(operation.getScenario());
                }
            }
        });
        return scenario.get();
    }


    private static ExecuteOperation getExecuteOperation(Operation operation, WebDriver driver) {
        switch (operation.getType()) {
            case OPEN -> {
                return new ExecuteOpen(driver, operation);
            }
            case TRACKING -> {
                return new ExecuteTracking(driver, operation);
            }
            case CLICK -> {
                return new ExecuteClick(driver, operation);
            }
            case INSERT -> {
                return new ExecuteInsert(driver, operation);
            }
            case BACK -> {
                return new ExecuteBack(driver, operation);
            }
            default -> throw new IllegalArgumentException("Invalid operation type");
        }
    }

    private void quitDriverIfIsEndPoint() {
        if (operation.isEndPoint()) {
            driverQuit();
            log.info("Operation name {} is end point", operation.getName());
        }
    }

    protected void sleep(int value, TimeUnit timeout) {
        long time = 0;

        if (timeout != null) {
            switch (timeout) {
                case SECONDS: {
                    time = TimeUnit.SECONDS.toMillis(value);
                    break;
                }
                case MICROSECONDS:
                    time = value;
            }
        }

        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    protected void close() {
        isRunning = false;
    }

    protected void driverQuit() {
        if (!this.isQuit) {
            driver.quit();
        }
    }
}
