package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

public class SerializationUtil {

    private static final String SAVE_FILE = "savegame.json";

    public static void saveGameState(GameState gameState) {
        Json json = new Json();
        String jsonString = json.toJson(gameState);
        FileHandle file = Gdx.files.local(SAVE_FILE);
        file.writeString(jsonString, false);
        Gdx.app.log("Serialization", "Game state saved.");
    }

    public static GameState loadGameState() {
        FileHandle file = Gdx.files.local(SAVE_FILE);
        if (!file.exists()) {
            Gdx.app.log("Serialization", "Save file does not exist.");
            return null;
        }
        Json json = new Json();
        String jsonString = file.readString();
        GameState gameState = json.fromJson(GameState.class, jsonString);
        Gdx.app.log("Serialization", "Game state loaded.");
        return gameState;
    }
}
