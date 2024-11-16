package GBTAN;
import GBTAN.CollideableObject.ObjectType;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;

public class PlayerTest {
    /**
     * Test if a player profile can be saved and loaded to/from a json file.
     */
    @Test
    public void testLoadSave() throws IOException {
        Player player = new Player(GameSettings.DEFAULT_PLAYER_FILE);
        assertEquals("Default Player", player.getName());
        assertEquals(1, player.getHighScore());
        assertEquals(0.2, player.getDoubleHPChance(), 0.0001);
        assertEquals(0.2, player.getRandomizerChance(), 0.0001);
        assertEquals(0.4, player.getBlockTypeChance().get(ObjectType.SQUARE), 0.0001);
        assertEquals(0.1, player.getBlockTypeChance().get(ObjectType.TRIANGLE_LOWER_LEFT), 0.0001);
        assertEquals(0.1, player.getBlockTypeChance().get(ObjectType.TRIANGLE_UPPER_LEFT), 0.0001);
        assertEquals(0.1, player.getBlockTypeChance().get(ObjectType.TRIANGLE_LOWER_RIGHT), 0.0001);
        assertEquals(0.1, player.getBlockTypeChance().get(ObjectType.TRIANGLE_UPPER_RIGHT), 0.0001);
        assertEquals(0.2, player.getBlockNumChance().get(1), 0.0001);
        assertEquals(0.3, player.getBlockNumChance().get(2), 0.0001);
        assertEquals(0.3, player.getBlockNumChance().get(3), 0.0001);
        assertEquals(0.2, player.getBlockNumChance().get(4), 0.0001);
        assertEquals(0.1, player.getBlockNumChance().get(5), 0.0001);
        assertEquals(0.05, player.getBlockNumChance().get(6), 0.0001);
        player.setHighScore(10);
        assertEquals(player.getHighScore(), 10);

        player.setFile(new File(GameSettings.PLAYERS_FOLDER, "testPlayer.json"));
        player.setName("Test Player");
        player.saveToFile();
        JsonReader reader = Json.createReader(new FileInputStream(player.getFile()));
        JsonObject json = reader.readObject();
        assertEquals(json.getString("name"), "Test Player");
        assertEquals(json.getInt("highScore"), 10);
    }
}
