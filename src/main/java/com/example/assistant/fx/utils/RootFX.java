package com.example.assistant.fx.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RootFX {
    private final ConfigurableApplicationContext applicationContext;


    public void goToStartView(ActionEvent event) {
        goToView(event, "/template/start-view.fxml");
    }

    public void goToCreateOrEditScenarioView(ActionEvent event) {
        goToView(event, "/template/create-or-edit-view.fxml");
    }

    private void goToView(ActionEvent event, String view) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(view));
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
