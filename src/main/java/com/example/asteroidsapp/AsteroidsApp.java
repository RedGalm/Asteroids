package com.example.asteroidsapp;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AsteroidsApp extends Application {

    public void start(Stage stage) throws IOException {
        int width = 450;
        int height = 300;
        Pane pane = new Pane();
        Ship ship = new Ship(width / 2, height / 2);
        pane.getChildren().add(ship.getCharacter());
        Scene scene = new Scene(pane, width, height);
        Map<KeyCode, Boolean> Keys = new HashMap<>();

        scene.setOnKeyPressed(event -> {
            Keys.put(event.getCode(), Boolean.TRUE);
        });
        scene.setOnKeyReleased(event -> {
            Keys.put(event.getCode(), Boolean.FALSE);
        });

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (Keys.getOrDefault(KeyCode.LEFT, false)) {
                    ship.turnLeft();
                }
                if (Keys.getOrDefault(KeyCode.RIGHT, false)) {
                    ship.turnRight();
                }
            }
        }.start();

        stage.setTitle("Asteroids!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}