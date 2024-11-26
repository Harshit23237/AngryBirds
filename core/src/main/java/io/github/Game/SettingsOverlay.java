package io.github.Game;



import com.badlogic.gdx.Game;
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

import io.github.some_example_name.Level3;
//import io.github.some_example_name.LevelPage;

import java.util.ArrayList;

public class SettingsOverlay extends ScreenAdapter {
    private Game game;
    private SpriteBatch batch;
    private Stage Setting_stage;
    private ImageButton closeButton;
    private ImageButton LoadGameButton;
    public Boolean isActive = false;
    private Texture settings_background;
    private ImageButton MuteButton;

    private Boolean ShowLoadGame = false;

    private ImageButton LoadLevel1_button;
    private ImageButton LoadLevel2_button;


    float SCREEN_WIDTH = Gdx.graphics.getWidth();
    float SCREEN_HEIGHT = Gdx.graphics.getHeight();

//    public SettingsOverlay(Game game) {
//        this.game = game;
//    }


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
        button.setPosition(SCREEN_WIDTH*pos_X-button.getWidth()*0.5f , SCREEN_HEIGHT*pos_Y-button.getHeight()*0.5f);

        button.setHeight(button_height); button.setWidth(button_width);
//        Setting_stage.addActor(button);
        return button;
    }



    public SettingsOverlay(Game game) {
        this.game = game;

        batch = new SpriteBatch();
        settings_background = new Texture(Gdx.files.internal("menu_settings.png"));
//        settings_background.setHeight();


        System.out.println("Stage set setting");
        Setting_stage = new Stage(new ScreenViewport());

        // Create a label for "Volume"
//        Label.LabelStyle labelStyle = new Label.LabelStyle();
//        labelStyle.font = new BitmapFont();
//        Label volumeLabel = new Label("Volume", labelStyle);

        // Create a slider for volume control
//        volumeSlider = new Slider(0, 100, 1, false, new Slider.SliderStyle());
//        volumeSlider.setValue(50);  // Default value

        // Create a close button
//        Texture closeTexture = new Texture(Gdx.files.internal("close_button.png"));
//        Drawable closeDrawable = new TextureRegionDrawable(closeTexture);
//        closeButton = new ImageButton(closeDrawable);

        closeButton = ImageButton_create("close_button.png", "close_button_down.png", 100,100, 0.65f,0.8f);

        System.out.println("close button created");
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ShowLoadGame = false;
                System.out.println("close button clicked - ShowLoadGame: "+ ShowLoadGame);
                closeOverlay();
                if(!ShowLoadGame){
                    Setting_stage.addActor(LoadGameButton);
                    Setting_stage.addActor(MuteButton);
                    LoadLevel1_button.remove();
                    LoadLevel2_button.remove();
                }

            }
        });

        Setting_stage.addActor(closeButton);

        // LOAD GAME BUTTON
        LoadGameButton = ImageButton_create("Load_Game.png", "Load_Game.png", 400,150, 0.51f,0.6f);

        LoadGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("CLICKED LOAD GAME BUTTON - earlier ShowLoadGame: " + ShowLoadGame );
                ShowLoadGame = true;
                if(ShowLoadGame){
                    LoadGameButton.remove();
                    MuteButton.remove();
                    Setting_stage.addActor(LoadLevel1_button);
                    Setting_stage.addActor(LoadLevel2_button);
                }

            }
        });

        Setting_stage.addActor(LoadGameButton);




        // MUTE BUTTON
        MuteButton = ImageButton_create("Mute.png", "Mute.png", 400,150, 0.51f,0.4f);


        MuteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("CLICKED MUTE BUTTON");
//               TutorialGame.setScreen(new TutorialGame(TutorialGame));
//                Setting_stage.clear(); // Clear the settings overlay when close is clicked

            }
        });

        Setting_stage.addActor(MuteButton);




        // LOAD LEVEL 1 BUTTON
        LoadLevel1_button = ImageButton_create("Load_Game1.png", "Load_Game1.png", 400,150, 0.51f,0.6f);

        LoadLevel1_button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("CLICKED Load level 1 BUTTON");
                game.setScreen( new Level3(game));
//               TutorialGame.setScreen(new TutorialGame(TutorialGame));
//                Setting_stage.clear(); // Clear the settings overlay when close is clicked
            }
        });

        // LOAD LEVEL 2 BUTTON
        LoadLevel2_button = ImageButton_create("Load_Game2.png", "Load_Game2.png", 400,150, 0.51f,0.4f);

        LoadLevel2_button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("CLICKED Load level 2 BUTTON");
                game.setScreen( new Level3(game));
//               TutorialGame.setScreen(new TutorialGame(TutorialGame));
//                Setting_stage.clear(); // Clear the settings overlay when close is clicked
            }
        });




//        Table table = new Table();
//        table.setFillParent(true); // Make the table take up the entire screen
//        table.center(); // Center the table
//        table.add(volumeLabel).padBottom(10).row();  // Add volume label
//        table.add(volumeSlider).padBottom(10).row();  // Add slider
//        table.add(closeButton).padTop(10);  // Add close button

//        Setting_stage.addActor(table);  // Add the table to the Setting_stage
    }

    public void render(float delta) {
        if(isActive ) {
            batch.begin();
            batch.draw(settings_background, SCREEN_WIDTH/2 - settings_background.getWidth()/2, SCREEN_HEIGHT/2 - settings_background.getHeight()/2 -40, 500, 600);
            batch.end();
            Setting_stage.act(delta);
            Setting_stage.draw();
        }

    }

    public void resize(int width, int height) {
        Setting_stage.getViewport().update(width, height, true);
        closeButton.setPosition(Gdx.graphics.getWidth() * 0.65f - closeButton.getWidth()*0.5f, Gdx.graphics.getHeight() *0.8f - closeButton.getHeight() *0.5f);
        LoadGameButton.setPosition(Gdx.graphics.getWidth() * 0.51f - LoadGameButton.getWidth()*0.5f, Gdx.graphics.getHeight() *0.6f - LoadGameButton.getHeight() *0.5f);
        MuteButton.setPosition(Gdx.graphics.getWidth() * 0.51f - MuteButton.getWidth()*0.5f, Gdx.graphics.getHeight() *0.4f - MuteButton.getHeight() *0.5f);
        LoadLevel1_button.setPosition(Gdx.graphics.getWidth() * 0.51f - LoadLevel1_button.getWidth()*0.5f, Gdx.graphics.getHeight() *0.6f - LoadLevel1_button.getHeight() *0.5f);
        LoadLevel2_button.setPosition(Gdx.graphics.getWidth() * 0.51f - LoadLevel2_button.getWidth()*0.5f, Gdx.graphics.getHeight() *0.4f - LoadLevel2_button.getHeight() *0.5f);

//        System.out.printf("close button position: %f, %f\n"  ,Gdx.graphics.getWidth() * 0.3f - closeButton.getWidth()*0.5f , Gdx.graphics.getHeight() *0.6f - closeButton.getHeight() *0.5f);
    }

    public void dispose() {
        Setting_stage.dispose();
        //batch.dispose();
        //settings_background.dispose();
    }

    public Stage getStage() {
        return Setting_stage;
    }

    public void setActive(boolean active) {
        isActive = active;
        if (isActive) {
            System.out.println("Setting turned on");
            Gdx.input.setInputProcessor(Setting_stage);
            Setting_stage.getRoot().setVisible(true); // Show the stage
            System.out.println("Setting turned on - yo");


        }
    }


//    private void closeOverlay() {
//        Setting_stage.clear();  // Clear the settings overlay
//
//        setActive(false);  // Mark overlay as inactive
//
//    }

    protected void closeOverlay() {
        setActive(false);  // Mark overlay as inactive
        Setting_stage.getRoot().setVisible(false); // Hide the stage without clearing it
    }



    public boolean isActive() {
        return isActive;
    }
}
