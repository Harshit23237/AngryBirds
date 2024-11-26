package io.github.Game;

import com.badlogic.gdx.Screen;
import io.github.Game.SettingsOverlay;
import io.github.Game.StartGame;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
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

import io.github.some_example_name.BonusLevel;
import io.github.some_example_name.Level2;
import io.github.some_example_name.Level3;
import io.github.some_example_name.LevelPage;

public class TutorialGame extends ScreenAdapter {//pages directed (load game,
    private Game game;
    private Texture background_texture;
    private Stage stage;
    private ImageButton button_lvl1;
    private ImageButton button_lvl2;
    private ImageButton button_lvl3;
    private ImageButton back_button;
    private ImageButton settingsButton;
    private SettingsOverlay settingsOverlay;
    private boolean showSettings = false;


    float SCREEN_WIDTH = Gdx.graphics.getWidth();
    float SCREEN_HEIGHT = Gdx.graphics.getHeight();

    public TutorialGame(Game game) {
        this.game = game;
    }

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
        button.setPosition(SCREEN_WIDTH*pos_X - button.getWidth()*0.5f, SCREEN_HEIGHT*pos_Y);

        button.setHeight(button_height); button.setWidth(button_width);
        stage.addActor(button);
        return button;
    }

    @Override
    public void show() {

        // DRAWING BACKGROUND
        background_texture = new Texture(Gdx.files.internal("level_page_bg.png"));

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        button_lvl1 = ImageButton_create("level1_box.png", "level1_box_down.png", 200,200, 0.5f, 0.5f );

        button_lvl1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
//                game.setScreen(new StartGame(game));
                game.setScreen( new BonusLevel(game));
            }
        });


        button_lvl2 = ImageButton_create("level2_box.png", "level2_box_down.png", 200,200, 0.5f, 0.5f );

        button_lvl2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
//                game.setScreen(new TutorialGame(game));
//                game.setScreen( new LevelPage(game));
                game.setScreen( new Level2(game) );
            }
        });

        button_lvl3 = ImageButton_create("level3_box.png", "level3_box_down.png", 200,200, 0.5f, 0.5f );

        button_lvl3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
//                game.setScreen(new TutorialGame(game));
                game.setScreen( new Level3(game));
            }
        });




//        BACK BUTTON

        back_button = ImageButton_create("back_button.png", "back_button.png",120,120, 0.1f, 0.05f);

        back_button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new StartGame(game));
            }
        });


        // SETTINGS BUTTON
        settingsButton = ImageButton_create("settings_button.png", "settings_button.png", 100,98,0.5f,0.5f);
        settingsButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("clicked settings");
//                showSettings = !showSettings;
//                settingsOverlay.setActive(showSettings);
                if (!settingsOverlay.isActive()) {
                    showSettings = true;
                    settingsOverlay.setActive(true);
                }
                System.out.println("returned from settings" );
            }

        });
//        stage.addActor(settingsButton);
        settingsOverlay = new SettingsOverlay(game);



    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (game instanceof MyGame) {
            SpriteBatch batch = ((MyGame) game).getBatch();
            batch.begin();
            batch.draw(background_texture, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
            batch.end();


            Gdx.input.setInputProcessor(stage);     // Revert to main stage input
            stage.act(delta);
            stage.draw();

            if (!showSettings) {
                // If settings are not shown, set input to main stage and draw it
                Gdx.input.setInputProcessor(stage);
                stage.act(delta);
                stage.draw();
            }

            // If settings are shown, set input to the settingsOverlay stage and render it
            if (showSettings && settingsOverlay.isActive() ) {
                //settingsOverlay.first_time=1;
                Gdx.input.setInputProcessor(settingsOverlay.getStage());
                settingsOverlay.render(delta);
            }


//            if (showSettings && settingsOverlay.isActive()) {
//                settingsOverlay.render(delta);
////                Gdx.input.setInputProcessor(settingsOverlay.getStage());  // Set input for settings overlay
//            }
//            else {
//                Gdx.input.setInputProcessor(stage);     // Revert to main stage input
//                stage.act(delta);
//                stage.draw();
//            }
        }
        else {
            System.err.println("Error: game is not an instance of MyGame");
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        settingsOverlay.resize(width, height);
        button_lvl1.setPosition(Gdx.graphics.getWidth() * 0.25f - button_lvl1.getWidth()*0.5f, Gdx.graphics.getHeight() *0.6f - button_lvl1.getHeight() *0.5f);
        button_lvl2.setPosition(Gdx.graphics.getWidth() * 0.50f - button_lvl2.getWidth()*0.5f, Gdx.graphics.getHeight() *0.6f - button_lvl2.getHeight() *0.5f);
        button_lvl3.setPosition(Gdx.graphics.getWidth() * 0.75f - button_lvl3.getWidth()*0.5f, Gdx.graphics.getHeight() *0.6f - button_lvl3.getHeight() *0.5f);
        settingsButton.setPosition(Gdx.graphics.getWidth() * 0.2f - settingsButton.getWidth()*0.5f, Gdx.graphics.getHeight() *0.11f - settingsButton.getHeight() *0.5f);
        back_button.setPosition(Gdx.graphics.getWidth() * 0.1f - back_button.getWidth()*0.5f, Gdx.graphics.getHeight() *0.1f - back_button.getHeight() *0.5f);

    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        background_texture.dispose();
        settingsOverlay.dispose();
        stage.dispose();
    }
}
