package com.example.assistant.comanda;

import com.example.assistant.model.emp_type.InputText;
import com.example.assistant.model.Operation;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ExecuteInsert extends ExecuteOperation {

    protected ExecuteInsert(WebDriver driver, Operation operation) {
        super(driver, operation);
    }

    @Override
    public void executeOperation() {
        Actions actions = new Actions(driver);


        InputText inputText = (InputText) operation;

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_TIME_SECONDS));
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(inputText.getSelector())));

        if(element.isDisplayed()) {
            log.info("Insert operation {}", inputText.getName());
            log.info("Insert text {}", inputText.getInputText());
            element.sendKeys(inputText.getInputText());
            sleep(inputText.getTimeout(), TimeUnit.SECONDS);

            if (inputText.isPush()) {
                log.info("-- push ENTER --");
                actions.sendKeys(Keys.ENTER).perform();
                sleep(inputText.getTimeout(), TimeUnit.SECONDS);
            }
        }
    }
}
