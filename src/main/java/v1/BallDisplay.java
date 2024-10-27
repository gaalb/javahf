package v1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;

public class BallDisplay {
    public static void draw(Graphics g, Ball ball) {
        if (ball.isActive()) {
            Point2D.Double pos = ball.getPosition();
            int size = 10; // Ball size
            g.setColor(Color.BLACK);
            g.fillOval((int) pos.x, (int) pos.y, size, size);
//            System.out.println("Drawing ball at position " + pos);
        }
    }
}
