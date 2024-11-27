package io.github.some_example_name;

import com.badlogic.gdx.math.Vector2;

public class BlockData {
    public String texturePath;
    public float positionX;
    public float positionY;
    public float width;
    public float height;
    public float rotation;
    public float health;

    public BlockData() {} // Default constructor for serialization

    public BlockData(Block block, float rotation) {
        this.texturePath = block.getTexturePath();
        this.positionX = block.getImage().getX();
        this.positionY = block.getImage().getY();
        this.width = block.getImage().getWidth();
        this.height = block.getImage().getHeight();
        this.rotation = rotation;
        this.health = block.getHealth();
    }
}
