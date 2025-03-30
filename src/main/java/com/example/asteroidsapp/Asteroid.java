package com.example.asteroidsapp;

import javafx.scene.shape.Polygon;

import java.util.Random;

public class Asteroid extends Character {
    private double rotate;

    public Asteroid(int x, int y) {
        super(new Polygon(20, 0, 30, 20, 40, 10), x, y);
        Random random = new Random();

        this.rotate = random.nextDouble();
    }

    public void move() {
        super.move();
        super.getCharacter().setRotate(super.getCharacter().getRotate() + this.rotate);
    }



}
