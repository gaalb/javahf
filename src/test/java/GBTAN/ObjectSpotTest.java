package GBTAN;
import GBTAN.CollideableObject.ObjectType;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.awt.geom.Point2D;

public class ObjectSpotTest {
    /**
     * Test if a spot and a block housed in it correctly calculate corners.
     */
    @Test
    public void testObjectPlacement() {
        ObjectSpot spot = new ObjectSpot(new Point2D.Double(100, 100), 50);
        // Spots are initialized empty
        assertTrue(spot.isEmpty());
        assertEquals(spot.getCorners()[0].x, 75, 0.0001);
        assertEquals(spot.getCorners()[0].y, 125, 0.0001);
        assertEquals(spot.getCorners()[1].x, 125, 0.0001);
        assertEquals(spot.getCorners()[1].y, 125, 0.0001);
        assertEquals(spot.getCorners()[2].x, 125, 0.0001);
        assertEquals(spot.getCorners()[2].y, 75, 0.0001);
        assertEquals(spot.getCorners()[3].x, 75, 0.0001);
        assertEquals(spot.getCorners()[3].y, 75, 0.0001);
        Block block = new Block(ObjectType.TRIANGLE_LOWER_LEFT, 10, null);
        spot.setObject(block);
        assertFalse(spot.isEmpty());
        // A lower left triangle takes all corners except the upper right
        assertEquals(block.getCorners()[0].x, 75, 0.0001);
        assertEquals(block.getCorners()[0].y, 125, 0.0001);
        assertEquals(block.getCorners()[1].x, 125, 0.0001);
        assertEquals(block.getCorners()[1].y, 125, 0.0001);
        assertEquals(block.getCorners()[2].x, 75, 0.0001);
        assertEquals(block.getCorners()[2].y, 75, 0.0001);
        spot.clearObject();
        // After clearing the object, the spot is empty again
        assertTrue(spot.isEmpty());
    }
}
