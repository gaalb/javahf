package GBTAN;

public abstract class Boon extends CollideableObject {
    private final double radius;
    protected boolean spent;

    public Boon(double radius, ObjectSpot spot, Game game) {
        super(spot, game);
        this.radius = radius;
        this.spent = false;
    }

    public double getRadius() {
        return radius;
    }

    abstract public void bless(Ball ball);

    public enum BoonType {
        PLUS_ONE,
        RANDOMIZER
    }

    public boolean isSpent() {
        return spent;
    }

    public static class BoonConfig {
        public BoonType type;
        public int x;
        public int y;
        public BoonConfig(int x, int y, BoonType type) {
            this.x = x;
            this.y = y;
            this.type = type;
        }
    }
}
