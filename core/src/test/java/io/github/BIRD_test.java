package io.github;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import io.github.some_example_name.Bird;
import io.github.some_example_name.BirdData;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class BIRD_test {
    private Bird mockBird;
    private final String mockTexturePath = "mock/texture/path.png";
    private final float mockHealth = 15f;
    private final float mockWidth = 40f;
    private final float mockHeight = 40f;
    private final float mockX = 30f;
    private final float mockY = 50f;
    private final Vector2 mockVelocity = new Vector2(5f, -3f);
    private final Boolean mockIsActive = true;

    @Before
    public void setUp() {
        // Mocking the Image object
        Image mockImage = mock(Image.class);
        when(mockImage.getWidth()).thenReturn(mockWidth);
        when(mockImage.getHeight()).thenReturn(mockHeight);
        when(mockImage.getX()).thenReturn(mockX);
        when(mockImage.getY()).thenReturn(mockY);

        // Mocking the Bird object
        mockBird = mock(Bird.class);
        when(mockBird.getTexturePath()).thenReturn(mockTexturePath);
        when(mockBird.getImage()).thenReturn(mockImage);
        when(mockBird.getHealth()).thenReturn((int) mockHealth);
    }

    @Test
    public void CheckX_Y() {
        BirdData birdData = new BirdData(mockBird, mockVelocity, mockIsActive);
        assertEquals(mockX, birdData.positionX, 0.01f);
        assertEquals(mockY, birdData.positionY, 0.01f);
    }

    @Test
    public void CheckHealth() {
        BirdData birdData = new BirdData(mockBird, mockVelocity, mockIsActive);
        assertEquals(mockHealth, birdData.health, 0.01f);
    }

//    @Test
//    public void CheckVelocity() {
//        BirdData birdData = new BirdData(mockBird, mockVelocity, mockIsActive);
//        assertEquals(mockVelocity.x, birdData.velocity.x, 0.01f);
//        assertEquals(mockVelocity.y, birdData.velocity.y, 0.01f);
//    }

    @Test
    public void CheckIsActive() {
        BirdData birdData = new BirdData(mockBird, mockVelocity, mockIsActive);
        assertEquals(mockIsActive, birdData.isActive);
    }

    @Test
    public void CheckTexturePath() {
        BirdData birdData = new BirdData(mockBird, mockVelocity, mockIsActive);
        assertEquals(mockTexturePath, birdData.texturePath);
    }
}
