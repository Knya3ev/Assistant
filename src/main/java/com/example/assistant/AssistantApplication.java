package com.example.assistant;

import com.example.assistant.fx.JavaFxApplication;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class AssistantApplication {

    public static void main(String[] args) {
        Application.launch(JavaFxApplication.class, args);
    }

}
