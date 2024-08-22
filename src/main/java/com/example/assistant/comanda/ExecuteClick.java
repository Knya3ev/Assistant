package com.example.assistant.comanda;

import com.example.assistant.model.Click;
import com.example.assistant.model.Operation;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ExecuteClick extends ExecuteOperation {

    protected ExecuteClick(WebDriver driver, Operation operation) {
        super(driver, operation);
    }

    @Override
    public void executeOperation() {
        Click click = (Click) operation;

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_TIME_SECONDS));
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(click.getSelector())));

        element.click();
        log.info("Clicked: {}", click.getName());
        sleep(click.getTimeout(), TimeUnit.SECONDS);
    }
}
