package io.github.some_example_name;

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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.Game.MyGame;
import io.github.Game.TutorialGame;

import java.util.ArrayList;
import com.badlogic.gdx.audio.Music;

public class LoseScreen extends ScreenAdapter {
    private Stage stage;
    private Game game;
    private Texture background_texture;

    private ImageButton retryButton;
    private ImageButton exitButton;

    float SCREEN_WIDTH = Gdx.graphics.getWidth();
    float SCREEN_HEIGHT = Gdx.graphics.getHeight();

    private Music loseMusic;
    String music_PATH = "loss_music.mp3";

    public LoseScreen(Game game) {
        this.game = game;
    }

    public LoseScreen() {}

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
        button.setPosition(SCREEN_WIDTH*pos_X - button.getWidth()*0.5f, SCREEN_HEIGHT*pos_Y-button.getHeight()*0.5f);

        button.setHeight(button_height); button.setWidth(button_width);
        stage.addActor(button);
        return button;
    }
    @Override
    public void show() {
       background_texture = new Texture(Gdx.files.internal("loss_bg.jpeg"));

        loseMusic = Gdx.audio.newMusic(Gdx.files.internal("loss_music.mp3"));
        loseMusic.setLooping(true);
        loseMusic.setVolume(0.1f);
        loseMusic.play();


        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        exitButton = ImageButton_create("exit1.png", "exit1.png", 230, 160, 0.5f, 0.1f);
        exitButton.setPosition(5,20);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(isLoseMusic("loss_music.mp3") && isPlayingMusic()){
                    loseMusic.dispose();
                }
                game.setScreen(new TutorialGame(game));
            }
        });
    }


    public Boolean isLoseMusic(String s){
        if(music_PATH == null) return false;
        if(music_PATH.equals(s)){
            return true;
        }
        return false;
    }
    public Boolean isPlayingMusic(){
        if(loseMusic == null) return false;
        return loseMusic.isPlaying();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        exitButton.setPosition(Gdx.graphics.getWidth()*0.5f - exitButton.getWidth()*0.5f, Gdx.graphics.getHeight() *0.3f - exitButton.getHeight() *0.5f);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        SpriteBatch batch = ((MyGame) game).getBatch();
        batch.begin();
        batch.draw(background_texture, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        batch.end();


        Gdx.input.setInputProcessor(stage);     // Revert to main stage input
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        background_texture.dispose();
        stage.dispose();
        if (loseMusic != null) loseMusic.dispose();
    }
}
