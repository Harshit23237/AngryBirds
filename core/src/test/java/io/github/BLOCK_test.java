package io.github;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import io.github.some_example_name.Block;
import io.github.some_example_name.BlockData;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class BLOCK_test {
    private Block mockBlock;
    private final String mockTexturePath = "mock/texture/path.png";
    private final float mockHealth = 10f;
    private final float mockWidth = 50f;
    private final float mockHeight = 50f;
    private final float mockX = 20f;
    private final float mockY = 20f;
    private final float mockRotation = 0f;

    @Before
    public void setUp() {
        // Mocking the Image object
        Image mockImage = mock(Image.class);
        when(mockImage.getWidth()).thenReturn(mockWidth);
        when(mockImage.getHeight()).thenReturn(mockHeight);
        when(mockImage.getX()).thenReturn(mockX);
        when(mockImage.getY()).thenReturn(mockY);

        // Mocking the Block object
        mockBlock = mock(Block.class);
        when(mockBlock.getTexturePath()).thenReturn(mockTexturePath);
        when(mockBlock.getImage()).thenReturn(mockImage);
        when(mockBlock.getHealth()).thenReturn((int) mockHealth);
    }

    @Test
    public void CheckX_Y(){
        BlockData blockData = new BlockData(mockBlock, mockRotation);
        assertEquals(mockX, blockData.positionX, 0.01f);
        assertEquals(mockY, blockData.positionY, 0.01f);
    }
    @Test
    public void CheckHealth(){
        BlockData blockData = new BlockData(mockBlock, mockRotation);
        assertEquals(mockHealth, blockData.health, 0.01f);
    }
    @Test
    public void CheckRotation(){
        BlockData blockData = new BlockData(mockBlock, mockRotation);
        assertEquals(mockRotation, blockData.rotation, 0.01f);
    }





}
