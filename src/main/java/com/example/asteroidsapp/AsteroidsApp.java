package com.example.asteroidsapp;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class AsteroidsApp extends Application {
    public static int width = 450;
    public static int height = 300;

    public void start(Stage stage) throws IOException {
        Pane pane = new Pane();
        Ship ship = new Ship(width / 2, height / 2);
        Text text = new Text(10, 20, "Points: 0");
        AtomicInteger points = new AtomicInteger();
        pane.getChildren().add(text);
        pane.getChildren().add(ship.getCharacter());
        Scene scene = new Scene(pane, width, height);
        Random random = new Random();

        Map<KeyCode, Boolean> keys = new HashMap<>();
        List<Asteroid> asteroids = new ArrayList<>();
        List<Projectile> projectiles = new ArrayList<>();
        List<Asteroid> disappearedAsteroids = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            asteroids.add(new Asteroid(random.nextInt(width / 3), random.nextInt(height)));
        }
        asteroids.forEach(asteroid -> pane.getChildren().add(asteroid.getCharacter()));

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

                if (keys.getOrDefault(KeyCode.SPACE, false)) {
                    if (projectiles.size() < 3) {
                        Projectile projectile = new Projectile((int) ship.getCharacter().getTranslateX(), (int) ship.getCharacter().getTranslateY());
                        projectile.getCharacter().setRotate(ship.getCharacter().getRotate());
                        projectile.accelerate();
                        projectile.setMovement(projectile.getMovement().normalize().multiply(2));
                        pane.getChildren().add(projectile.getCharacter());
                        projectiles.add(projectile);
                    }
                }
                projectiles.forEach(projectile -> projectile.move());
                asteroids.forEach(asteroid -> {
                    asteroid.move();
                    if (asteroid.collide(ship)) {
                        stop();
                    }
                });
                ship.move();

                List<Projectile> removableProjectiles = new ArrayList<>();

                projectiles.forEach(projectile -> {
                    List<Asteroid> collisions = asteroids.stream()
                            .filter(asteroid -> asteroid.collide(projectile))
                            .toList();

                    collisions.forEach(collided -> {
                        disappearedAsteroids.add(collided);
                        pane.getChildren().remove(collided.getCharacter());
                        asteroids.remove(collided);
                        removableProjectiles.add(projectile);
                        text.setText("Points: " + points.addAndGet(100));

                    });

                });

                removableProjectiles.forEach(projectile -> {
                    pane.getChildren().remove(projectile.getCharacter());
                    projectiles.remove(projectile);
                });

                if (!(disappearedAsteroids.isEmpty())) {
                    if (Math.random() < 0.010) {
                        if (!(disappearedAsteroids.getFirst().collide(ship))) {
                            asteroids.add(disappearedAsteroids.getFirst());
                            pane.getChildren().add(disappearedAsteroids.getFirst().getCharacter());
                            disappearedAsteroids.removeFirst();
                        }
                    }

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