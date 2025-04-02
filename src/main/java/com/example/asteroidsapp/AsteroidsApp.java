package com.example.asteroidsapp;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static javafx.application.Platform.exit;

public class AsteroidsApp extends Application {
    public static int width = 450;
    public static int height = 300;

    public void start(Stage stage) throws IOException {
        AtomicInteger points = new AtomicInteger();
        BorderPane startScreenLayout = new BorderPane();
        Text welcomeMessage1 = new Text("Welcome to the Asteroids game!");
        Text welcomeMessage2 = new Text("Press Start to begin");
        Button startButton = new Button("Start");
        VBox vBox1 = new VBox();
        vBox1.getChildren().addAll(welcomeMessage1, welcomeMessage2, startButton);
        vBox1.setAlignment(Pos.CENTER);
        vBox1.setSpacing(20.0);
        startScreenLayout.setCenter(vBox1);
        Scene startScreen = new Scene(startScreenLayout, width, height);

        BorderPane endScreenLayout = new BorderPane();
        Text endMessage = new Text("Game Over! Your score: 0");
        Button exit = new Button("Exit game");
        VBox vBox2 = new VBox();
        vBox2.getChildren().addAll(endMessage, exit);
        vBox2.setAlignment(Pos.CENTER);
        vBox2.setSpacing(20.0);
        endScreenLayout.setCenter(vBox2);
        Scene endScreen = new Scene(endScreenLayout, width, height);

        Pane pane = new Pane();
        Ship ship = new Ship(width / 2, height / 2);
        Text text = new Text(10, 20, "Points: 0");
        pane.getChildren().add(text);
        pane.getChildren().add(ship.getCharacter());
        Scene game = new Scene(pane, width, height);
        Random random = new Random();

        Map<KeyCode, Boolean> keys = new HashMap<>();
        List<Asteroid> asteroids = new ArrayList<>();
        List<Projectile> projectiles = new ArrayList<>();
        List<Asteroid> disappearedAsteroids = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            asteroids.add(new Asteroid(random.nextInt(width / 3), random.nextInt(height)));
        }
        asteroids.forEach(asteroid -> pane.getChildren().add(asteroid.getCharacter()));

        game.setOnKeyPressed(event -> {
            keys.put(event.getCode(), Boolean.TRUE);
        });
        game.setOnKeyReleased(event -> {
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
                        stage.setScene(endScreen);
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
                        endMessage.setText("Game Over! Your score: " + points.addAndGet(100));

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
        stage.setScene(startScreen);
        startButton.setOnAction(event -> {
            stage.setScene(game);
        });

        exit.setOnAction(event -> {
            exit();
        });

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}