package v1;

public class Player {
    private int xPosition;
    private double aimingAngle;
    private int ballCount;

    public Player() {
        xPosition = GamePanel.SCREEN_SIZE.width/2;
        aimingAngle = 90.0;
        ballCount = 1;
    }
    public double getXPosition() {
        return xPosition;
    }
    public void setxPosition(int x) {
        xPosition = x;
    }
    public double getAimingAngle() {
        return aimingAngle;
    }
    public void setAimingAngle(double angle) {
        aimingAngle = angle;
    }
    public int getBallCount() {
        return ballCount;
    }
    public void setBallCount(int ballCount) {
        this.ballCount = ballCount;
    }
}
