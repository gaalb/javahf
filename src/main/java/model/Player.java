package model;

import engine.GameEngine;

public class Player {
    private GameEngine gameEngine;
    private String name;
    private int score;

    public Player(String name, GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        this.name = name;
        score = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

}

