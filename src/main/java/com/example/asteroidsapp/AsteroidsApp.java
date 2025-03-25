package com.example.asteroidsapp;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class AsteroidsApp extends Application {

    public void start(Stage stage) throws IOException {
        int width = 450;
        int height = 300;
        Pane pane = new Pane();
        Ship ship = new Ship(width / 2, height / 2);
        pane.getChildren().add(ship.getCharacter());
        Scene scene = new Scene(pane, width, height);

        Map<KeyCode, Boolean> keys = new HashMap<>();
        List<Asteroid> asteroids = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Random random = new Random();
            asteroids.add(new Asteroid(random.nextInt(width / 3), random.nextInt(height)));
        }
        asteroids.forEach(asteroid -> pane.getChildren().add(asteroid.getCharacter()));
        asteroids.forEach(asteroid -> asteroid.accelerate());

        scene.setOnKeyPressed(event -> {
            keys.put(event.getCode(), Boolean.TRUE);
        });
        scene.setOnKeyReleased(event -> {
            keys.put(event.getCode(), Boolean.FALSE);
        });

        new AnimationTimer() {
            @Override
            public void handle(long now) {

                if (keys.getOrDefault(KeyCode.LEFT, false)) {
                    ship.turnLeft();
                }
                if (keys.getOrDefault(KeyCode.RIGHT, false)) {
                    ship.turnRight();
                }
                if (keys.getOrDefault(KeyCode.UP, false)) {
                    ship.accelerate();
                }
                ship.move();

                asteroids.forEach(asteroid -> {
                    asteroid.move();
                });

                asteroids.forEach(asteroid -> {
                   if (ship.collide(asteroid)) {
                       stop();
                   }
                });


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