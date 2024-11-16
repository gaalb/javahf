package GBTAN;

import GBTAN.CollideableObject.ObjectType;

import javax.json.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a player in the game, storing their name, high score, and configuration settings
 * for generating new rows of blocks and boons.
 */
public class Player {

    /**
     * The file to save/load to/from.
     */
    private File file;

    /**
     * The name of the player.
     */
    private String name;

    /**
     * The player's high score.
     */
    private int highScore;

    /**
     * The chance of spawning a block with double health points.
     */
    private double doubleBlockChance;

    /**
     * The chance of spawning a randomizer boon, if a spot is left open for it.
     */
    private double randomizerChance;

    /**
     * A map defining the relative chances of spawning each block type.
     */
    private Map<ObjectType, Double> blockTypeChance;

    /**
     * A map defining the relative chances of spawning a specific number of blocks.
     */
    private Map<Integer, Double> blockNumChance;

    /**
     * Retrieves the chance of spawning a block with double health points.
     *
     * @return The chance as a double value.
     */
    public double getDoubleHPChance() {
        return doubleBlockChance;
    }

    /**
     * Retrieves the chance of spawning a randomizer boon.
     *
     * @return The chance as a double value.
     */
    public double getRandomizerChance() {
        return randomizerChance;
    }

    /**
     * Retrieves the map defining the relative chances of spawning a specific number of blocks.
     *
     * @return A map where keys are block counts and values are their respective chances.
     */
    public Map<Integer, Double> getBlockNumChance() {
        return blockNumChance;
    }

    /**
     * Retrieves the map defining the relative chances of spawning each block type.
     *
     * @return A map where keys are block types and values are their respective chances.
     */
    public Map<ObjectType, Double> getBlockTypeChance() {
        return blockTypeChance;
    }

    /**
     * Constructs a player by reading their data from a JSON file.
     *
     * @param file The JSON file containing the player's data.
     */
    public Player(File file) {
        this.file = file;
        try (JsonReader reader = Json.createReader(new FileInputStream(file))) {
            JsonObject playerJson = reader.readObject();
            this.name = playerJson.getString("name");
            this.highScore = playerJson.getInt("highScore");
            this.doubleBlockChance = playerJson.getJsonNumber("doubleBlockChance").doubleValue();
            this.randomizerChance = playerJson.getJsonNumber("randomizerChance").doubleValue();
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

    /**
     * Retrieves the player's name.
     *
     * @return The player's name as a string.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the player's name.
     *
     * @param name The new name for the player.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the player's high score.
     *
     * @return The player's high score as an integer.
     */
    public int getHighScore() {
        return highScore;
    }

    /**
     * Updates the player's high score.
     *
     * @param highScore The new high score for the player.
     */
    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    /**
     * Saves the player's data to a JSON file.
     */
    public void saveToFile() {
        if (file.equals(GameSettings.DEFAULT_PLAYER_FILE)) return;
        JsonObjectBuilder blockTypeChanceBuilder = Json.createObjectBuilder();
        for (Map.Entry<ObjectType, Double> entry : blockTypeChance.entrySet()) {
            blockTypeChanceBuilder.add(entry.getKey().name(), entry.getValue());
        }
        JsonObjectBuilder blockNumChanceBuilder = Json.createObjectBuilder();
        for (Map.Entry<Integer, Double> entry : blockNumChance.entrySet()) {
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
