package GBTAN;

import GBTAN.CollideableObject.ObjectType;

import javax.json.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Player {
    // A player is just a name, a highscore, and a bunch of settings for spawning new rows
    private String name;
    private int highScore;
    private double doubleBlockChance;
    private double randomizerChance;
    private Map<ObjectType, Double> blockTypeChance;
    private Map<Integer, Double> blockNumChance;

    public double getDoubleHPChance() {
        return doubleBlockChance;
    }

    public double getRandomizerChance() {
        return randomizerChance;
    }

    public Map<Integer, Double> getBlockNumChance() {
        return blockNumChance;
    }

    public Map<ObjectType, Double> getBlockTypeChance() {
        return blockTypeChance;
    }

    public Player(File file) {
        try (JsonReader reader = Json.createReader(new FileInputStream(file))) {
            JsonObject playerJson = reader.readObject();
            this.name = playerJson.getString("name");
            this.highScore = playerJson.getInt("highScore");
            this.doubleBlockChance = playerJson.getJsonNumber("doubleBlockChance").doubleValue();
            this.randomizerChance = playerJson.getJsonNumber("randomizerChance").doubleValue();
            this.blockTypeChance = new HashMap<>();
            this.blockTypeChance = new HashMap<>();
            JsonObject blockTypeJson = playerJson.getJsonObject("blockTypeChance");
            for (String key : blockTypeJson.keySet()) {
                ObjectType blockType = ObjectType.valueOf(key);
                double chance = blockTypeJson.getJsonNumber(key).doubleValue();
                blockTypeChance.put(blockType, chance);
            }
            this.blockNumChance = new HashMap<>();
            JsonObject blockNumJson = playerJson.getJsonObject("blockNumChance");
            for (String key : blockNumJson.keySet()) {
                int number = Integer.parseInt(key);
                double chance = blockNumJson.getJsonNumber(key).doubleValue();
                blockNumChance.put(number, chance);
            }
        } catch (IOException e) {
            System.out.println("JSON file couldn't be read.");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public void saveToFile(File file){
        JsonObjectBuilder blockTypeChanceBuilder = Json.createObjectBuilder();
        for (Map.Entry<ObjectType, Double> entry: blockTypeChance.entrySet()) {
            blockTypeChanceBuilder.add(entry.getKey().name(), entry.getValue());
        }
        JsonObjectBuilder blockNumChanceBuilder = Json.createObjectBuilder();
        for (Map.Entry<Integer, Double> entry: blockNumChance.entrySet()) {
            blockNumChanceBuilder.add(entry.getKey().toString(), entry.getValue());
        }
        JsonObject playerJson = Json.createObjectBuilder()
                .add("name", name)
                .add("highScore", highScore)
                .add("doubleBlockChance", doubleBlockChance)
                .add("randomizerChance", randomizerChance)
                .add("blockTypeChance", blockTypeChanceBuilder.build())
                .add("blockNumChance", blockNumChanceBuilder.build())
                .build();
        try (JsonWriter writer = Json.createWriter(new FileOutputStream(file))) {
            writer.writeObject(playerJson);
        } catch (IOException e) {
            System.out.println("JSON file couldn't be written.");
        }
    }
}

