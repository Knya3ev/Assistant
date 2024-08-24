package com.example.assistant.fx.utils;

import com.example.assistant.model.emp_type.OpenUrl;
import com.example.assistant.model.Operation;
import com.example.assistant.model.Scenario;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Paint;

import java.util.Arrays;
import java.util.Objects;

public class FxUtils {


    public static void clearErrorMessageForLabel(Label... labels) {
        Arrays.stream(labels).forEach(label -> label.setVisible(false));
    }

    public static boolean isValidLabelForEmptyText(TextField textField, Label labelError, String errorMessage) {
        if (textField.getText().isEmpty()) {
            visibleErrorMessage(labelError, errorMessage);
            return false;
        }
        return true;
    }

    public static boolean isValidStringIsDigit(TextField textField, Label labelError, String errorMessage) {
        if (!textField.getText().matches("\\d+")) {
            visibleErrorMessage(labelError, errorMessage);
            return false;
        }
        return true;
    }

    public static void visibleErrorMessage(Label labelError, String errorMessage) {
        labelError.setText(errorMessage);
        labelError.setVisible(true);
        labelError.setAlignment(Pos.CENTER);
    }

    public static Label label(String text) {
        Label label = new Label(String.format("   %s   ", text));
        return label;
    }

    public static HBox getHBoxForTypeOperation(Operation operation, Node... nodes) {
        final int maxLength = 50;
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);

        String name;

        switch (operation.getType()) {
            case OPEN -> {
                OpenUrl openUrl = (OpenUrl) operation;
                name = "Url go to -> %s".formatted(
                        openUrl.getUrl().length() > maxLength
                                ? openUrl.getUrl().substring(0, maxLength) + "..."
                                : openUrl.getUrl());
            }
            case TRACKING -> name = "@____%s____@".formatted(operation.getName());
            case CLICK, INSERT -> name = Objects.isNull(operation.getTracking())
                    ? operation.getName()
                    : "    " + operation.getName() + "    for tracking --> @" + operation.getTracking().getName() + "@";
            case BACK -> {
                name = Objects.isNull(operation.getTracking())
                        ? "<-Back"
                        : "    <-Back    for tracking --> @" + operation.getTracking().getName() + "@";
            }

            default -> name = "";

        }

        HBox hbox = new HBox(label(name), region, label(operation.getType().toString()));
        hbox.getChildren().addAll(nodes);
        hbox.setAlignment(Pos.CENTER_LEFT);

        return hbox;
    }

    public static HBox getHBoxForScenario(Scenario scenario, Node... nodes) {
        Label errorMessage = checkErrorMessage(scenario);

        final int maxLength = 50;
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        HBox hbox = new HBox(label(scenario.getName()), errorMessage, region);
        hbox.getChildren().addAll(nodes);
        hbox.setAlignment(Pos.CENTER_LEFT);

        return hbox;
    }

    private static Label checkErrorMessage(Scenario scenario) {
        if (!Objects.isNull(scenario.getErrorMessage())) {
            Label label = new Label(scenario.getErrorMessage());
            label.setTextFill(Paint.valueOf("red"));
            return label;
        }

        return new Label("");
    }

}
