package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Pig {
    private String texturePath;
    private Texture texture;
    private Image image;
    private int health;

    public Pig(String texturePath, float x, float y, float width, float height) {
        this.texturePath = texturePath;
        this.texture = new Texture(Gdx.files.internal(texturePath)); // Corrected
        this.image = new Image(this.texture);
        this.image.setSize(width, height);
        this.image.setPosition(x, y); // Added to set position
        this.health = 1;
    }

    public Pig(String texturePath, float x, float y, float width, float height, int health) {
        this.texturePath = texturePath;
        this.texture = new Texture(Gdx.files.internal(texturePath)); // Corrected
        this.image = new Image(this.texture);
        this.image.setSize(width, height); // Use setSize for consistency
        this.image.setPosition(x, y); // Set position separately
        this.health = health;
    }

    public String getTexturePath() {
        return this.texturePath;
    }

    public float getPositionX() {
        return image.getX();
    }
    public float getPositionY() {
        return image.getY();
    }
    public float getWidth() {
        return image.getWidth();
    }
    public float getHeight() {
        return image.getHeight();
    }



    public Image getImage() {
        return this.image;
    }

    public Texture getTexture() {
        return this.texture;
    }

    public void dispose() {
        texture.dispose();
    }

    public int getHealth() {
        return health;
    }

    public void decrementHealth() {
        this.health--;
    }

    public boolean isDestroyed() {
        return health <= 0;
    }
}

