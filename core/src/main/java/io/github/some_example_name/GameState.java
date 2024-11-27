package io.github.some_example_name;

import java.util.ArrayList;

public class GameState {
    public ArrayList<BirdData> birds;
    public ArrayList<PigData> pigs;
    public ArrayList<BlockData> blocks;
    public int currentBirdIndex;

    public GameState() {
        birds = new ArrayList<>();
        pigs = new ArrayList<>();
        blocks = new ArrayList<>();
    }
}
