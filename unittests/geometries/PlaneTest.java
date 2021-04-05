package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point3D;
import primitives.Vector;
import primitives.Ray;

import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing Planes
 *
 * @author Yona and Hillel
 *
 */
class PlaneTest {

    /**
     * Test method for {@link Plane#getNormal(Point3D)}.
     */
    @Test
    public void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here
        Plane pl = new Plane(new Point3D(0, 0, 1), new Point3D(1, 0, 0), new Point3D(0, 1, 0));
        double sqrt3 = Math.sqrt(1d / 3);
        assertEquals(new Vector(sqrt3, sqrt3, sqrt3), pl.getNormal(new Point3D(0, 0, 1)), "Bad normal to plane");
    }

    @Test
    public void testFindIntersections() {
        Plane pl = new Plane(new Point3D(0, 0, 1), new Vector(1, 1, 1));

        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray into plane
        assertEquals(List.of(new Point3D(1,0,0)), pl.findIntersections(new Ray(new Point3D(0.5, 0, 0), new Vector(1, 0, 0))),
                "Bad plane intersection");

        // TC02: Ray out of plane
        assertNull(pl.findIntersections(new Ray(new Point3D(2,0,0),new Vector(1,0,0))),"Must not be plane intersection");


        // =============== Boundary Values Tests ==================
        // TC03: Ray parallel to plane
        assertNull( pl.findIntersections(new Ray(new Point3D(1, 1, 1), new Vector(0, 1, -1))),"Must not be plane intersection");

        // TC04: Ray in plane

        assertNull(pl.findIntersections(new Ray(new Point3D(0, 0.5, .5), new Vector(0, 1, -1))),"Must not be plane intersection");

        // TC05: Orthogonal ray into plane

        assertEquals(List.of(new Point3D(1d / 3, 1d / 3, 1d / 3)), pl.findIntersections(new Ray(new Point3D(1, 1, 1), new Vector(-1, -1, -1))),
                "Bad plane intersection");



        // TC06: Orthogonal ray out of plane
        assertNull(  pl.findIntersections(new Ray(new Point3D(1, 1, 1), new Vector(1, 1, 1))),"Must not be plane intersection");

         //TC07: Orthogonal ray from plane
//        assertNull( pl.findIntersections(new Ray(new Point3D(0, 0.5, 0.5), new Vector(1, 1, 1))),"Must not be plane intersection");

        // TC08: Ray from plane
//        assertNull(pl.findIntersections(new Ray(new Point3D(0, 0.5, 0.5), new Vector(1, 1, 0))),"Must not be plane intersection");
//
//        // TC09: Ray from plane's Q point
//        assertNull(   pl.findIntersections(new Ray(new Point3D(0, 0, 1), new Vector(1, 1, 0))),"Must not be plane intersection");

    }
}