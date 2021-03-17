package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point3D;
import primitives.Vector;
import geometries.*;
import primitives.*;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Testing Triangle
 *
 * @author Yona and Hillel
 *
 */

class TriangleTest {

    /**
     * Test method for {@link Triangle#getNormal(Point3D)}.
     */
    @Test
    public void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here
        Triangle pl = new Triangle(new Point3D(0, 0, 1), new Point3D(1, 0, 0), new Point3D(0, 1, 0));
        double sqrt3 = Math.sqrt(1d / 3);
        assertEquals(new Vector(sqrt3, sqrt3, sqrt3), pl.getNormal(new Point3D(0, 0, 1)), "Bad normal to trinagle");
    }
}