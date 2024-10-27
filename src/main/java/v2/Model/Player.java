package v2.Model;

public class Player {
    private String name;
    private int score;
    private Cannon cannon;

    public Player(String name) {
        this.name = name;
        score = 0;
        cannon = new Cannon(GameData.GAME_WIDTH / 2, 90, 4);
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

    public Cannon getCannon() {
        return cannon;
    }

    public void setCannon(Cannon cannon) {
        this.cannon = cannon;
    }
}

