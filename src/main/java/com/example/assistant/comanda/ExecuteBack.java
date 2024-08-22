package com.example.assistant.comanda;

import com.example.assistant.model.Back;
import com.example.assistant.model.Operation;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;

@Slf4j
public class ExecuteBack extends ExecuteOperation {

    protected ExecuteBack(WebDriver driver, Operation operation) {
        super(driver, operation);
    }

    @Override
    public void executeOperation() {
        Back back = (Back) operation;
        driver.navigate().back();
        log.info("<-Back");
        sleep(back.getTimeout(), TimeUnit.SECONDS);
    }
}
