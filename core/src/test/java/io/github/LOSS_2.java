package io.github;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.backends.headless.mock.audio.MockAudio;
import com.badlogic.gdx.backends.headless.mock.graphics.MockGraphics;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import io.github.some_example_name.LoseScreen;
import io.github.some_example_name.WinScreen;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class LOSS_2 {
    @Before
    public void setUp() {
        Gdx.graphics = Mockito.mock(MockGraphics.class);
        Mockito.when(Gdx.graphics.getWidth()).thenReturn(800);
        Mockito.when(Gdx.graphics.getHeight()).thenReturn(600);

        Gdx.audio = Mockito.mock(MockAudio.class);
        Music mockMusic = Mockito.mock(Music.class);
        Mockito.when(Gdx.audio.newMusic(Mockito.any(FileHandle.class))).thenReturn(mockMusic);

        Gdx.gl = Mockito.mock(GL20.class);
        Gdx.files = Mockito.mock(com.badlogic.gdx.Files.class);

        FileHandle mockFileHandle = Mockito.mock(FileHandle.class);
        Mockito.when(mockFileHandle.name()).thenReturn("mocked_file.png"); // Return a valid file name
        Mockito.when(Gdx.files.internal(Mockito.anyString())).thenReturn(mockFileHandle);

        Mockito.mockStatic(Texture.class);
    }

    @Test
    public void test1() {
        Game mockGame = Mockito.mock(Game.class);
        LoseScreen loseScreen = new LoseScreen(mockGame);

//        winScreen.show();
        assertEquals(true, loseScreen.isLoseMusic("loss_music.mp3"));
//        winScreen.dispose();
    }



}
