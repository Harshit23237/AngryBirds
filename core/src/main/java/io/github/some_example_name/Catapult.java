package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Catapult {
    private Image image;
    float SCREEN_WIDTH = Gdx.graphics.getWidth();
    float SCREEN_HEIGHT = Gdx.graphics.getHeight();

    public Catapult(String texturePath, float x, float y, float width, float height) {
        Texture texture = new Texture(texturePath);
        this.image = new Image(texture);
        this.image.setPosition(SCREEN_WIDTH*x-width*0.5f,SCREEN_HEIGHT* y-height*0.5f);
        this.image.setSize(width, height);  // Resize to specified dimensions
    }

    public Image getImage() {
        return image;
    }
}
