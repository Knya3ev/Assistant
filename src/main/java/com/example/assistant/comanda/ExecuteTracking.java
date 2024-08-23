package com.example.assistant.comanda;

import com.example.assistant.exception.TrackingException;
import com.example.assistant.model.Operation;
import com.example.assistant.model.emp_type.Tracking;
import com.example.assistant.model.enums.OperationName;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ExecuteTracking extends ExecuteOperation {
    private final int DEFAULT_TIMEOUT_BEFORE_CLICK = 3;

    private final List<Operation> trackingOperations;
    private final Tracking tracking;

    protected ExecuteTracking(WebDriver driver, Operation operation) {
        super(driver, operation);
        this.tracking = (Tracking) operation;
        this.trackingOperations = tracking.getOperations();
        this.checkedTracking(trackingOperations);
    }

    @Override
    public void executeOperation() {
        int i = 0;
        String script = "return document.querySelector(arguments[0]);";
        List<ExecuteOperation> executeOperation = ExecuteOperation.getExecuteOperation(trackingOperations, driver);

        while (isRunning && i <= tracking.getNumberOfIterations() - 1) {
            try {

                WebElement element = (WebElement) ((JavascriptExecutor) driver)
                        .executeScript(script, tracking.getSelector());

                if (element != null) {

                    i++;
                    log.info("Tracking: элемент найден");

                    if (tracking.isClicked()) {
                        element.click();
                        log.info("Tracking: нажать на элемент");
                        sleep(DEFAULT_TIMEOUT_BEFORE_CLICK, TimeUnit.SECONDS);
                        executeOperation.forEach(ExecuteOperation::execute);
                    }

                    log.info("Tracking: операции трекера были выполнены");

                }

            } catch (TimeoutException | StaleElementReferenceException e) {
                log.error("Tracking element waiting error ");
                i--;
                continue;

            } catch (NoSuchElementException | TrackingException | ElementClickInterceptedException e) {
                log.error("Tracking: element not found");
                continue;
            }
            sleep(tracking.getTimeout(), TimeUnit.MILLISECONDS);
        }
        sleep(DEFAULT_TIMEOUT_BEFORE_CLICK, TimeUnit.SECONDS);
    }

    private void checkedTracking(List<Operation> operation) {
        trackingOperations.forEach(o -> {
            if (o.getType().equals(OperationName.TRACKING)) {
                throw new RuntimeException("There can't be nested trackers ");
            }
        });
    }

}
