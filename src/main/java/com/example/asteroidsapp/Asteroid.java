package com.example.asteroidsapp;

import javafx.scene.shape.Polygon;

public class Asteroid extends Character {
    public Asteroid(int x, int y) {
        super(new Polygon(-5, 20, 40, 30, 20, 5, 25, 15), x, y);
    }

}
