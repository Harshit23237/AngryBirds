package io.github.Game;

import io.github.Game.TutorialGame;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;

public class StartGame extends ScreenAdapter {//Start game is start page
    SpriteBatch batch;
    private Stage stage;
    private Game game;
    Texture background_texture;
    private ImageButton button;
    private ImageButton exitButton;
    float SCREEN_WIDTH = Gdx.graphics.getWidth();
    float SCREEN_HEIGHT = Gdx.graphics.getHeight();

    private ArrayList<Drawable> level_button_texture(String up_texture, String down_texture ) {
        Texture buttonUpTexture = new Texture(Gdx.files.internal(up_texture));
        Texture buttonDownTexture = new Texture(Gdx.files.internal(down_texture));

        Drawable buttonUp = new TextureRegionDrawable(buttonUpTexture);
        Drawable buttonDown = new TextureRegionDrawable(buttonDownTexture);

        ArrayList<Drawable> level_button_up_down  = new ArrayList<Drawable>();
        level_button_up_down.add(buttonUp);
        level_button_up_down.add(buttonDown);
        return level_button_up_down;
    }

    private ImageButton ImageButton_create(String up_texture, String down_texture,
                                           float button_width, float button_height,
                                           float pos_X, float pos_Y) {
        ImageButton button;

        ArrayList<Drawable> button_drawable;
        button_drawable = level_button_texture(up_texture, down_texture);

        ImageButton.ImageButtonStyle button_style = new ImageButton.ImageButtonStyle();
        button_style.up = button_drawable.get(0);
        button_style.down = button_drawable.get(1);

        button = new ImageButton(button_style);
//        button.setPosition(SCREEN_WIDTH*0.5f - button.getWidth()*0.5f, SCREEN_HEIGHT*0.1f);
        button.setPosition(SCREEN_WIDTH*pos_X - button.getWidth()*0.5f, SCREEN_HEIGHT*pos_Y);

        button.setHeight(button_height); button.setWidth(button_width);
        stage.addActor(button);
        return button;
    }


    public StartGame(Game game) {
        this.game = game;
        batch = new SpriteBatch();
        background_texture = new Texture(Gdx.files.internal("GameStart_bg.jpg"));

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Button textures
        Texture buttonUpTexture = new Texture(Gdx.files.internal("PlayButton.png"));
        Texture buttonDownTexture = new Texture(Gdx.files.internal("PlayButton_down.png"));

        Drawable buttonUp = new TextureRegionDrawable(buttonUpTexture);
        Drawable buttonDown = new TextureRegionDrawable(buttonDownTexture);

        // Create a imageButtonStyle
        ImageButton.ImageButtonStyle imageButtonStyle = new ImageButton.ImageButtonStyle();
        imageButtonStyle.up = buttonUp;
        imageButtonStyle.down = buttonDown;


        button = new ImageButton(imageButtonStyle);
        button.setPosition(SCREEN_WIDTH / 2f - button.getWidth() / 2f, SCREEN_HEIGHT*0.1f);
        button.setHeight(250);      // maintain a ratio of 2/3
        button.setWidth(375);

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new TutorialGame(game));
            }
        });

        stage.addActor(button);

        exitButton = ImageButton_create("back_button.png", "back_button.png", 100,100,0.5f,0.5f);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
//                game.setScreen(new TutorialGame(game));
                Gdx.app.exit();
//                System.out.println("APP EXITTED ");
            }
        });

        stage.addActor(exitButton);

        // SETTINGS BUTTON



        // Initialize the settings overlay
//        settingsOverlay = new SettingsOverlay();

    }

    @Override
    public void render(float delta){
        batch.begin();
        batch.draw(background_texture, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        batch.end();
        stage.act(delta);
        stage.draw();

    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

        button.setPosition(Gdx.graphics.getWidth()*0.5f - button.getWidth()*0.5f, Gdx.graphics.getHeight() *0.2f - button.getHeight() *0.5f);
        exitButton.setPosition(Gdx.graphics.getWidth()*0.15f - button.getWidth()*0.5f, Gdx.graphics.getHeight() *0.2f - button.getHeight() *0.5f);

    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
    }
}
