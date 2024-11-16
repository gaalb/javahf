package GBTAN;
import GBTAN.GameSave.SpotConfig;
import GBTAN.CollideableObject.ObjectType;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class GameSaveTest {
    /**
     * Test if a save can be saved/loaded to/from a json file.
     */
    @Test
    public void testLoadSave() throws IOException {
        GameSave gameSave = new GameSave(GameSettings.DEFAULT_SAVE_FILE);
        assertEquals(gameSave.ballNum, 1);
        assertEquals(gameSave.cannonPos, 210);
        assertEquals(gameSave.score, 0);
        for (SpotConfig[] row: gameSave.spots) {
            for (SpotConfig spot: row) {
                assertEquals(spot.objectType, ObjectType.NULL);
            }
        }


        gameSave.ballNum = 100;
        gameSave.cannonPos = 0;
        gameSave.score = 100;
        File saveFile = new File(GameSettings.SAVES_FOLDER, "testSave.json");
        gameSave.saveToFile(saveFile);
        JsonReader reader = Json.createReader(new FileInputStream(saveFile));
        JsonObject json = reader.readObject();
        assertEquals(json.getInt("ballNum"), 100);
        assertEquals(json.getInt("cannonPos"), 0);
        assertEquals(json.getInt("score"), 100);
    }
}
