package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing Cylinders
 *
 * @author Yona and Hillel
 *
 */
class CylinderTest {

    /**
     * Test method for {@link Cylinder#getNormal(Point3D)}.
     */
    @Test
    public void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here (when point is on the top of the cylinder)
        Cylinder pl = new Cylinder(new Ray(new Point3D(0, 0, 0),new Vector(0,0,1)), 1d, 1d);
        assertEquals(new Vector(0, 0, 1d), pl.getNormal(new Point3D(0, 0.5, 1)), "Bad normal to cylinder");
        // TC02: There is a simple single test here (when point is on the base of the cylinder)
        assertEquals(new Vector(0, 0, 1d), pl.getNormal(new Point3D(0, 0.5, 0)), "Bad normal to cylinder");
        // TC03: There is a simple single test here (when point is on the maatefet of the cylinder)
        assertEquals(new Vector(0, 1d, 0), pl.getNormal(new Point3D(0, 0.5, 0.5)), "Bad normal to cylinder");
    }
}