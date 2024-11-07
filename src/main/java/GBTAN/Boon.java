package GBTAN;

public abstract class Boon extends CollideableObject {
    private final double radius;
    protected boolean spent;

    public Boon(double radius, Game game) {
        super(game);
        this.radius = radius;
        this.spent = false;
    }

    public double getRadius() {
        return radius;
    }

    abstract public void affect(Ball ball);

    public boolean isSpent() {
        return spent;
    }
}
