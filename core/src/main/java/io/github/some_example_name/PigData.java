package io.github.some_example_name;

import com.badlogic.gdx.math.Vector2;

public class PigData {
    public String texturePath;
    public float positionX;
    public float positionY;
    public float health;
    public float width;
    public float height;

    public PigData() {} // Default constructor for serialization

    public PigData(Pig pig) {
        this.texturePath = pig.getTexturePath();
//        this.positionX = pig.getImage().getX();
        this.positionX = pig.getPositionX();
//        this.positionY = pig.getImage().getY();
        this.positionY = pig.getPositionY();
        this.health = pig.getHealth();
//        this.width = pig.getImage().getWidth();
        this.width = pig.getWidth();
//        this.height = pig.getImage().getHeight();
        this.height = pig.getHeight();
    }
}
