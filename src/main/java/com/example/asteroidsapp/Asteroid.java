package com.example.asteroidsapp;

import javafx.scene.shape.Polygon;

import java.util.Random;

public class Asteroid extends Character {
    private double rotate;

    public Asteroid(int x, int y) {
        super(new Polygon(20, -20, 20, 20, -20, 20, -20, -20), x, y);
        Random random = new Random();

        this.rotate = random.nextDouble();
        super.getCharacter().setRotate(random.nextInt(360));

        for (int i = 0; i < random.nextInt(10); i++) {
            super.accelerate();
        }
    }

    public void move() {
        super.move();
        super.getCharacter().setRotate(super.getCharacter().getRotate() + this.rotate);
    }



}
