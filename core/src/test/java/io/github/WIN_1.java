package io.github;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.backends.headless.mock.audio.MockAudio;
import com.badlogic.gdx.backends.headless.mock.graphics.MockGraphics;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import io.github.some_example_name.WinScreen;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class WIN_1 {

    @Before
    public void setUp() {
        // Mock Gdx.graphics
        Gdx.graphics = Mockito.mock(MockGraphics.class);
        Mockito.when(Gdx.graphics.getWidth()).thenReturn(800);
        Mockito.when(Gdx.graphics.getHeight()).thenReturn(600);

        // Mock Gdx.audio
        Gdx.audio = Mockito.mock(MockAudio.class);
        Music mockMusic = Mockito.mock(Music.class);
        Mockito.when(Gdx.audio.newMusic(Mockito.any(FileHandle.class))).thenReturn(mockMusic);

        // Mock Gdx.gl
        Gdx.gl = Mockito.mock(GL20.class);

        // Mock Gdx.files
        Gdx.files = Mockito.mock(com.badlogic.gdx.Files.class);

        // Mock FileHandle
        FileHandle mockFileHandle = Mockito.mock(FileHandle.class);
        Mockito.when(mockFileHandle.name()).thenReturn("mocked_file.png"); // Return a valid file name
        Mockito.when(Gdx.files.internal(Mockito.anyString())).thenReturn(mockFileHandle);

        // Mock Texture to avoid loading real textures
        Mockito.mockStatic(Texture.class);
    }

    @Test
    public void test1() {
        Game mockGame = Mockito.mock(Game.class);
        WinScreen winScreen = new WinScreen(mockGame);

//        winScreen.show();

        assertEquals(false, winScreen.isPlayingMusic());

//        winScreen.dispose();
    }



}
