package io.github.some_example_name;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class PauseScreen implements Screen {
    private Stage stage;
    private Texture backgroundTexture;

    public PauseScreen() {
        stage = new Stage(new FitViewport(800, 600));

        // Load and Set Background Image
        backgroundTexture = new Texture("background.png");
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(800, 600);  // Full screen
        stage.addActor(backgroundImage);

        // Set input processor
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.2f, 0.2f, 0.2f, 1);  // Clear with greyish background
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
    }
}
