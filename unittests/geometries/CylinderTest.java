package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

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
        Cylinder pl = new Cylinder(1d, new Ray(new Point3D(0, 0, 0),new Vector(0,0,1)),  1d);
        assertEquals(new Vector(0, 0, 1d), pl.getNormal(new Point3D(0, 0.5, 1)), "Bad normal to cylinder");
        // TC02: There is a simple single test here (when point is on the base of the cylinder)
        assertEquals(new Vector(0, 0, 1d), pl.getNormal(new Point3D(0, 0.5, 0)), "Bad normal to cylinder");
        // TC03: There is a simple single test here (when point is on the surface of the cylinder)
        assertEquals(new Vector(0, 1d, 0), pl.getNormal(new Point3D(0, 0.5, 0.5)), "Bad normal to cylinder");

        // =============== Boundary Values Tests ==================
        // TC04: There is a single boundary test here (between surface and base of cylinder)
        assertNotEquals(pl.getNormal(new Point3D(0,1,0.00001)),pl.getNormal(new Point3D(0,0.99999999,0)), "Error in boundary test with base !");
        // TC05: There is a single boundary test here (between surface and top of cylinder)
        assertNotEquals(pl.getNormal(new Point3D(0,1,0.99999)),pl.getNormal(new Point3D(0,0.99999999,1)), "Error in boundary test with top !");
    }

    /**
     * Test method for {@link Cylinder#findIntersections(Ray)}.
     */
    @Test
    void findIntersectionsTest() {

        Cylinder cylinder = new Cylinder(1d,new Ray(new Point3D(2,0,0), new Vector(0,0,1)), 2d);

        // ============ Equivalence Partitions Tests ==============

        //TC01 ray is outside and parallel to the cylinder's ray

        List<Point3D> result = cylinder.findIntersections(new Ray(new Point3D(5,0,0), new Vector(0,0,1)));
        assertNull(result, "Wrong number of points");


        //TC02 ray starts inside and parallel to the cylinder's ray

        result = cylinder.findIntersections(new Ray(new Point3D(2.5,0,1), new Vector(0,0,1)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point3D(2.5,0,2)), result, "Bad intersection point");

        //TC03 ray starts outside and parallel to the cylinder's ray and crosses the cylinder

        result = cylinder.findIntersections(new Ray(new Point3D(2.5,0,-1), new Vector(0,0,1)));
        assertEquals(2, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point3D(2.5, 0, 0), new Point3D(2.5,0,2)), result, "Bad intersection point");

        //TC04 ray starts from outside and crosses the cylinder

        result = cylinder.findIntersections(new Ray(new Point3D(-2,0,0), new Vector(1,0,0)));
        assertEquals(2, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point3D(1,0,0), new Point3D(3,0,0)), result, "Bad intersection points");

        //TC05 ray starts from inside and crosses the cylinder

        result = cylinder.findIntersections(new Ray(new Point3D(1.5,0,0), new Vector(1,0,0)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point3D(3,0,0)), result, "Bad intersection points");

        //TC06 ray starts from outside the cylinder and doesn't cross the cylinder

        result = cylinder.findIntersections(new Ray(new Point3D(5,0,0), new Vector(1,0,0)));
        assertNull(result, "Wrong number of points");

        // =============== Boundary Values Tests ==================

        //TC07 ray is on the surface of the cylinder (not bases)

        result = cylinder.findIntersections(new Ray(new Point3D(3,0,0), new Vector(0,0,1)));
        assertNull(result, "Wrong number of points");

        //TC08 ray is on the base of the cylinder and crosses 2 times

        result = cylinder.findIntersections(new Ray(new Point3D(-1,0,0), new Vector(1,0,0)));
        assertEquals(2, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point3D(1,0,0), new Point3D(3,0,0)), result, "Bad intersection points");

        /*
        //TC08 ray is in center of the cylinder

        result = cylinder.findIntersections(new Ray(new Point3D(2,0,0), new Vector(0,0,1)));
        assertNull(result, "Wrong number of points");

        //TC09 ray is perpendicular to cylinder's ray and starts from outside the tube

        result = cylinder.findIntersections(new Ray(new Point3D(-2,0,0), new Vector(1,0,0)));
        assertEquals(2, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point3D(1,0,0), new Point3D(3,0,0)), result, "Bad intersection points");

        //TC09 ray is perpendicular to cylinder's ray and starts from inside cylinder (not center)

        result = cylinder.findIntersections(new Ray(new Point3D(1.5,0,0), new Vector(1,0,0)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point3D(3,0,0)), result, "Bad intersection points");

        //TC10 ray is perpendicular to cylinder's ray and starts from the center of cylinder

        result = cylinder.findIntersections(new Ray(new Point3D(2,0,0), new Vector(1,0,0)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point3D(3,0,0)), result, "Bad intersection points");

        //TC11 ray is perpendicular to cylinder's ray and starts from the surface of cylinder to inside

        result = cylinder.findIntersections(new Ray(new Point3D(1,0,0), new Vector(1,0,0)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point3D(3,0,0)), result, "Bad intersection points");

        //TC12 ray is perpendicular to cylinder's ray and starts from the surface of cylinder to outside

        result = cylinder.findIntersections(new Ray(new Point3D(3,0,0), new Vector(1,0,0)));
        assertNull(result, "Wrong number of points");

        //TC13 ray starts from the surface to outside

        result = cylinder.findIntersections(new Ray(new Point3D(3,0,0), new Vector(1,1,1)));
        assertNull(result, "Wrong number of points");

        //TC14 ray starts from the surface to inside

        result = cylinder.findIntersections(new Ray(new Point3D(3,0,0), new Vector(-1,0,1)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point3D(1,0,2)), result, "Bad intersection point");

        //TC15 ray starts from the center

        result = cylinder.findIntersections(new Ray(new Point3D(2,0,0), new Vector(1,0,1)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point3D(3,0,1)), result, "Bad intersection point");

        //TC16 prolongement of ray crosses cylinder

        result = cylinder.findIntersections(new Ray(new Point3D(3,0,0), new Vector(1,0,0)));
        assertNull(result, "Wrong number of points");
        */
    }
}