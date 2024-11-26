package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Block {

    private Texture texture;
    private Image image;
    private int health;

//    private float xPercent, yPercent; // Store percentage positions
//    private float widthPercent, heightPercent; // Store percentage sizes
//    float SCREEN_WIDTH = Gdx.graphics.getWidth();
//    float SCREEN_HEIGHT = Gdx.graphics.getHeight();
    public Block(String texturePath, float x, float y, float width, float height) {
        texture = new Texture(Gdx.files.internal(texturePath));
//        this.xPercent=x;
//        this.yPercent=y;
//        this.widthPercent=width;
//        this.heightPercent=height;
        this.image = new Image(texture);
//        this.image.setPosition(SCREEN_WIDTH*x-width*0.5f,SCREEN_HEIGHT* y-height*0.5f);
        this.image.setSize(width, height);
        this.health = 1; // Initial health (2 collisions needed to destroy)
    }
    public Block(String texturePath, float x, float y, float width, float height, int health) {
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

//    public Image getImage() {
//        return this.image;
//    }
//    public void resize(float newWidth, float newHeight) {
//        // Resize and reposition the block based on the new screen dimensions
//        float newBlockWidth = newWidth * widthPercent;
//        float newBlockHeight = newHeight * heightPercent;
////        float newX = newWidth * xPercent - newBlockWidth * 0.5f;
////        float newY = newHeight * yPercent - newBlockHeight * 0.5f;
//
//        this.image.setSize(newBlockWidth, newBlockHeight);
//        this.image.setPosition(newX, newY);
//    }
}
