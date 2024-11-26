package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;

public class OverlaySaveGame extends ScreenAdapter {
    private SpriteBatch batch;
    private Stage saveGameStage;
    private ImageButton closeButton;
    private ImageButton confirmSaveButton;
    private Boolean isActive = false;
    private Texture saveGameBackground;

    float SCREEN_WIDTH = Gdx.graphics.getWidth();
    float SCREEN_HEIGHT = Gdx.graphics.getHeight();

    private ArrayList<Drawable> buttonTextures(String upTexture, String downTexture) {
        Texture buttonUpTexture = new Texture(Gdx.files.internal(upTexture));
        Texture buttonDownTexture = new Texture(Gdx.files.internal(downTexture));

        Drawable buttonUp = new TextureRegionDrawable(buttonUpTexture);
        Drawable buttonDown = new TextureRegionDrawable(buttonDownTexture);

        ArrayList<Drawable> buttonDrawables = new ArrayList<>();
        buttonDrawables.add(buttonUp);
        buttonDrawables.add(buttonDown);
        return buttonDrawables;
    }

    private ImageButton createImageButton(String upTexture, String downTexture,
                                          float buttonWidth, float buttonHeight,
                                          float posX, float posY) {
        ImageButton button;
        ArrayList<Drawable> buttonDrawables = buttonTextures(upTexture, downTexture);

        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.up = buttonDrawables.get(0);
        buttonStyle.down = buttonDrawables.get(1);

        button = new ImageButton(buttonStyle);
        button.setPosition(SCREEN_WIDTH * posX - button.getWidth() * 0.5f, SCREEN_HEIGHT * posY-button.getHeight()*0.5f);
        button.setHeight(buttonHeight);
        button.setWidth(buttonWidth);
        saveGameStage.addActor(button);
        return button;
    }

    public OverlaySaveGame() {
        batch = new SpriteBatch();
        saveGameBackground = new Texture(Gdx.files.internal("menu_settings2.png"));//save_game_background.png
        saveGameStage = new Stage(new ScreenViewport());

        // Create Close Button
        closeButton = createImageButton("close_button.png", "close_button_down.png", 100, 100, 0.65f, 0.8f);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                closeOverlay(); // Close functionality
            }
        });
        saveGameStage.addActor(closeButton);

        // Create Confirm Save Button
        confirmSaveButton = createImageButton("confirm_save.png", "confirm_save.png", 400, 100, 0.51f, 0.5f);//"confirm_save.png"
        confirmSaveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Game Saved!");
                closeOverlay();
            }
        });
        saveGameStage.addActor(confirmSaveButton);
    }

    public void render(float delta) {
        if (isActive) {
            batch.begin();
            batch.draw(saveGameBackground, SCREEN_WIDTH / 2 - saveGameBackground.getWidth() / 2, SCREEN_HEIGHT / 2 - saveGameBackground.getHeight() / 2-40,500,600);
            batch.end();

            saveGameStage.act(delta);
            saveGameStage.draw();
        }
    }

    public void resize(int width, int height) {
        saveGameStage.getViewport().update(width, height, true);
        confirmSaveButton.setPosition(SCREEN_WIDTH * 0.51f - confirmSaveButton.getWidth() * 0.5f, SCREEN_HEIGHT * 0.2f-confirmSaveButton.getHeight()*0.5f);
        closeButton.setPosition(SCREEN_WIDTH * 0.65f - closeButton.getWidth() * 0.5f, SCREEN_HEIGHT * 0.8f-closeButton.getHeight()*0.5f);

    }

    public void dispose() {
        saveGameStage.dispose();
        batch.dispose();
        saveGameBackground.dispose();
    }

    public void setActive(boolean active) {
        isActive = active;
        if (isActive) {
            Gdx.input.setInputProcessor(saveGameStage);
            saveGameStage.getRoot().setVisible(true); // Show the stage
        }
    }

    private void closeOverlay() {
        setActive(false); // Mark overlay as inactive
        saveGameStage.getRoot().setVisible(false); // Hide the stage without clearing it
    }

    public boolean isActive() {
        return isActive;
    }

    public Stage getStage() {
        return saveGameStage;
    }
}
