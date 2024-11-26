package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Bird {
    private Texture texture;
    private Image image;
    private int health;

    public Bird(String texturePath, float width, float height) {
        texture = new Texture(Gdx.files.internal(texturePath));
        image = new Image(texture);
        image.setSize(width, height);
        this.health = 5;
    }

    public Bird(String texturePath, float width, float height, int health) {
        texture = new Texture(Gdx.files.internal(texturePath));
        image = new Image(texture);
        image.setSize(width, height);
        this.health = health;
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
