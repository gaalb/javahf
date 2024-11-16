package GBTAN;

import GBTAN.CollideableObject.ObjectType;

import javax.json.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Handles the serialization and deserialization of game state to and from JSON files.
 * This class allows saving and loading the game state, including ball positions,
 * cannon configurations, scores, and object configurations.
 */
public class GameSave {

    /**
     * Represents the configuration of a single game spot, including the type of object
     * present and its health points (if applicable).
     */
    public static class SpotConfig {
        /** The type of the object in the spot. */
        public ObjectType objectType;

        /** The health points of the object in the spot. */
        public int hp;
    }

    /** The number of balls currently in play or in store. */
    public int ballNum;

    /** The horizontal position of the cannon. */
    public double cannonPos;

    /** The current score of the game. */
    public int score;

    /** A 2D array of spot configurations, representing the state of the game grid. */
    public SpotConfig[][] spots;

    /**
     * Constructs a GameSave instance by loading data from a specified JSON file.
     *
     * @param file The file containing the saved game state in JSON format.
     */
    public GameSave(File file) {
        try (JsonReader reader = Json.createReader(new FileInputStream(file))) {
            JsonObject gameConfigJson = reader.readObject();
            this.ballNum = gameConfigJson.getInt("ballNum");
            this.cannonPos = gameConfigJson.getJsonNumber("cannonPos").doubleValue();
            this.score = gameConfigJson.getInt("score");
            JsonArray spotsArray = gameConfigJson.getJsonArray("spots");
            this.spots = new SpotConfig[spotsArray.size()][];
            for (int i = 0; i < spotsArray.size(); i++) {
                JsonArray rowArray = spotsArray.getJsonArray(i);
                spots[i] = new SpotConfig[rowArray.size()];
                for (int j = 0; j < rowArray.size(); j++) {
                    JsonObject spotJson = rowArray.getJsonObject(j);
                    SpotConfig spotConfig = new SpotConfig();
                    spotConfig.objectType = ObjectType.valueOf(spotJson.getString("objectType"));
                    spotConfig.hp = spotJson.getInt("hp");
                    spots[i][j] = spotConfig;
                }
            }
        } catch (IOException e) {
            System.out.println("JSON file couldn't be read.");
        }
    }

    /**
     * Constructs a GameSave instance from the current game state.
     *
     * @param game The {@link Game} instance containing the current state to save.
     */
    public GameSave(Game game) {
        GameData gameData = game.getGameData();
        ballNum = gameData.getBalls().size();
        cannonPos = gameData.getCannon().getPosition().x;
        score = game.getScore();
        ObjectSpot[][] objectSpots = gameData.getSpots();
        spots = new SpotConfig[GameSettings.BLOCK_ROWS][GameSettings.BLOCK_COLUMNS];
        for (int i = 0; i < GameSettings.BLOCK_ROWS; i++) {
            for (int j = 0; j < GameSettings.BLOCK_COLUMNS; j++) {
                CollideableObject obj = objectSpots[i][j].getObject();
                SpotConfig spotConfig = new SpotConfig();
                spotConfig.hp = obj instanceof Block ? ((Block) obj).getHealth() : 0;
                spotConfig.objectType = obj == null ? ObjectType.NULL : obj.getType();
                spots[i][j] = spotConfig;
            }
        }
    }

    /**
     * Saves the current game state to a specified file in JSON format.
     *
     * @param file The file to which the game state will be written.
     */
    public void saveToFile(File file) {
        JsonObjectBuilder gameConfigBuilder = Json.createObjectBuilder()
                .add("ballNum", ballNum)
                .add("cannonPos", cannonPos)
                .add("score", score);
        JsonArrayBuilder spotsArrayBuilder = Json.createArrayBuilder();
        for (SpotConfig[] row : spots) {
            JsonArrayBuilder rowArrayBuilder = Json.createArrayBuilder();
            for (SpotConfig spot : row) {
                JsonObject spotJson = Json.createObjectBuilder()
                        .add("objectType", spot.objectType.name())
                        .add("hp", spot.hp)
                        .build();
                rowArrayBuilder.add(spotJson);
            }
            spotsArrayBuilder.add(rowArrayBuilder.build());
        }
        gameConfigBuilder.add("spots", spotsArrayBuilder.build());
        try (JsonWriter writer = Json.createWriter(new FileOutputStream(file))) {
            writer.writeObject(gameConfigBuilder.build());
        } catch (IOException e) {
            System.out.println("JSON file couldn't be written.");
        }
    }
}
