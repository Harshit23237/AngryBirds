package io.github;

import io.github.some_example_name.Pig;
import io.github.some_example_name.PigData;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class PIG_test {
    private Pig mockPig;
    private final String mockTexturePath = "mock/texture/path.png";
    private final float mockHealth = 20f;
    private final float mockWidth = 60f;
    private final float mockHeight = 40f;
    private final float mockX = 100f;
    private final float mockY = 150f;

    @Before
    public void setUp() {
        // Mocking the Pig object
        mockPig = mock(Pig.class);
        when(mockPig.getTexturePath()).thenReturn(mockTexturePath);
        when(mockPig.getPositionX()).thenReturn(mockX);
        when(mockPig.getPositionY()).thenReturn(mockY);
        when(mockPig.getWidth()).thenReturn(mockWidth);
        when(mockPig.getHeight()).thenReturn(mockHeight);
        when(mockPig.getHealth()).thenReturn((int) mockHealth);
    }

    @Test
    public void CheckX_Y() {
        PigData pigData = new PigData(mockPig);
        assertEquals(mockX, pigData.positionX, 0.01f);
        assertEquals(mockY, pigData.positionY, 0.01f);
    }

    @Test
    public void CheckHealth() {
        PigData pigData = new PigData(mockPig);
        assertEquals(mockHealth, pigData.health, 0.01f);
    }

    @Test
    public void CheckWidth_Height() {
        PigData pigData = new PigData(mockPig);
        assertEquals(mockWidth, pigData.width, 0.01f);
        assertEquals(mockHeight, pigData.height, 0.01f);
    }

    @Test
    public void CheckTexturePath() {
        PigData pigData = new PigData(mockPig);
        assertEquals(mockTexturePath, pigData.texturePath);
    }
}
