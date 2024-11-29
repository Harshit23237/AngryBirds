package io.github.some_example_name;

import com.badlogic.gdx.math.Vector2;

public class BirdData {
    public String texturePath;
    public float positionX;
    public float positionY;
    public float health;
//    public Vector2 velocity;
    public Boolean isActive;

    public BirdData() {}

//    public BirdData(Bird bird, Vector2 velocity) {
//        this.texturePath = bird.getTexturePath();
//        this.positionX = bird.getImage().getX();
//        this.positionY = bird.getImage().getY();
//        this.health = bird.getHealth();
//        this.velocity = velocity;
//    }

    public BirdData(Bird bird, Vector2 velocity, Boolean isActive) {
        this.texturePath = bird.getTexturePath();
        this.positionX = bird.getImage().getX();
        this.positionY = bird.getImage().getY();
        this.health = bird.getHealth();
//        this.velocity = velocity;
        this.isActive = isActive;
    }

}
