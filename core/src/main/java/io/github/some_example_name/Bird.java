package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Bird {
    private String texturePath;
    private Texture texture;
    private Image image;
    private int health;

    public Bird(String texturePath, float width, float height) {
        this.texturePath = texturePath;
        texture = new Texture(Gdx.files.internal(texturePath));
        this.image = new Image(texture);
        this.image.setSize(width, height);
        this.health = 5;
    }

    public Bird(String texturePath, float width, float height, int health) {
        this.texturePath = texturePath;
        texture = new Texture(Gdx.files.internal(texturePath));
        this.image = new Image(texture);
        this.image.setSize(width, height);
        this.health = health;
    }
    public Bird(String texturePath, float x, float y, float width, float height, int health) {
        this.texturePath = texturePath;
        this.texture = new Texture(Gdx.files.internal(texturePath));
        this.image = new Image(this.texture);
        this.image.setSize(70, 70);
        this.image.setPosition(x, y);
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

    public int getHealth() {
        return health;
    }

    public void decrementHealth() {
        this.health--;
    }

    public boolean isDestroyed() {
        return health <= 0;
    }

    public void dispose() {
        texture.dispose();
    }
}
