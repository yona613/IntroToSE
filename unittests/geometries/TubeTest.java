package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing Tube
 *
 * @author Yona and Hillel
 *
 */

class TubeTest {


    /**
     * Test method for {@link Tube#getNormal(Point3D)}.
     */
    @Test
   public void testGetNormal() {
        Tube tube = new Tube(1d, new Ray(new Point3D(0,0,0), new Vector(0,0,1)));
        Vector normal = new Vector(0,1,0);
        assertEquals(tube.getNormal(new Point3D(0,1,1)), normal, "Bad normal calculation !");
    }
}