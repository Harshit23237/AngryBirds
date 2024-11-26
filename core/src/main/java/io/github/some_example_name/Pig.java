package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Pig {
    private Texture texture;
    private Image image;
    private int health;

    public Pig(String texturePath, float x, float y, float width, float height) {
        texture = new Texture(Gdx.files.internal(texturePath));
        this.image = new Image(texture);
        this.image.setSize(width, height);
        this.health = 1;
    }
    public Pig(String texturePath, float x, float y, float width, float height, int health) {
        Texture texture = new Texture(Gdx.files.internal(texturePath));
        this.image = new Image(texture);
        this.image.setBounds(x, y, width, height);
        this.health = health;
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

