package GBTAN;

import GBTAN.CollideableObject.ObjectType;

import javax.json.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class GameSave {
    public static class SpotConfig {
        public ObjectType objectType;
        public int hp;
    }
    public int ballNum;
    public double cannonPos;
    public int score;
    public SpotConfig[][] spots;

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

    public GameSave(Game game) {
        GameData gameData = game.getGameData();
        ballNum = gameData.getBalls().size();
        cannonPos = gameData.getCannon().getPosition().x;
        score = game.getScore();
        ObjectSpot[][] objectSpots = gameData.getSpots();
        spots = new SpotConfig[GameSettings.BLOCK_ROWS][GameSettings.BLOCK_COLUMNS];
        for (int i=0; i<GameSettings.BLOCK_ROWS; i++) {
            for (int j=0; j<GameSettings.BLOCK_COLUMNS; j++) {
                CollideableObject obj = objectSpots[i][j].getObject();
                SpotConfig spotConfig = new SpotConfig();
                spotConfig.hp = obj instanceof Block ? ((Block) obj).getHealth() : 0;
                spotConfig.objectType = obj == null ? ObjectType.NULL : obj.getType();
                spots[i][j] = spotConfig;
            }
        }
    }

    public void saveToFile(File file) {
        JsonObjectBuilder gameConfigBuilder = Json.createObjectBuilder().
                add("ballNum", ballNum)
                .add("cannonPos", cannonPos)
                .add("score", score);
        JsonArrayBuilder spotsArrayBuilder = Json.createArrayBuilder();
        for (SpotConfig[] row: spots) {
            JsonArrayBuilder rowArrayBuilder = Json.createArrayBuilder();
            for (SpotConfig spot: row) {
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
