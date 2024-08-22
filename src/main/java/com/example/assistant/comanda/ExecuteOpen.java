package com.example.assistant.comanda;

import com.example.assistant.model.OpenUrl;
import com.example.assistant.model.Operation;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;

@Slf4j
public class ExecuteOpen extends ExecuteOperation {

    protected ExecuteOpen(WebDriver driver, Operation operation) {
        super(driver, operation);
    }

    @Override
    public void executeOperation() {
        OpenUrl openUrl = (OpenUrl) operation;
        driver.get(openUrl.getUrl());
        log.info("Open URL: {}", openUrl.getUrl());
        sleep(openUrl.getTimeout(), TimeUnit.SECONDS);
    }
}
