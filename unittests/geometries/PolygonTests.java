/**
 * 
 */
package geometries;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import geometries.*;
import primitives.*;

import java.util.List;
import java.util.function.Supplier;

/**
 * Testing Polygons
 * 
 * @author Dan
 *
 */
public class PolygonTests {

    /**
     * Test method for
     * {@link Polygon#Polygon(Point3D...)}.
     */
    @Test
    public void testConstructor() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Correct concave quadrangular with vertices in correct order
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0),
                    new Point3D(0, 1, 0), new Point3D(-1, 1, 1));
        } catch (IllegalArgumentException e) {
            fail("Failed constructing a correct polygon");
        }

        // TC02: Wrong vertices order
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(0, 1, 0),
                    new Point3D(1, 0, 0), new Point3D(-1, 1, 1));
            fail("Constructed a polygon with wrong order of vertices");
        } catch (IllegalArgumentException e) {}

        // TC03: Not in the same plane
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0),
                    new Point3D(0, 1, 0), new Point3D(0, 2, 2));
            fail("Constructed a polygon with vertices that are not in the same plane");
        } catch (IllegalArgumentException e) {}

        // TC04: Concave quadrangular
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0),
                    new Point3D(0, 1, 0), new Point3D(0.5, 0.25, 0.5));
            fail("Constructed a concave polygon");
        } catch (IllegalArgumentException e) {}

        // =============== Boundary Values Tests ==================

        // TC10: Vertex on a side of a quadrangular
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0),
                    new Point3D(0, 1, 0), new Point3D(0, 0.5, 0.5));
            fail("Constructed a polygon with vertix on a side");
        } catch (IllegalArgumentException e) {}

        // TC11: Last point = first point
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0),
                    new Point3D(0, 1, 0), new Point3D(0, 0, 1));
            fail("Constructed a polygon with vertice on a side");
        } catch (IllegalArgumentException e) {}

        // TC12: Colocated points
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0),
                    new Point3D(0, 1, 0), new Point3D(0, 1, 0));
            fail("Constructed a polygon with vertice on a side");
        } catch (IllegalArgumentException e) {}

    }

    /**
     * Test method for {@link geometries.Polygon#getNormal(primitives.Point3D)}.
     */
    @Test
    public void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here
        Polygon pl = new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0), new Point3D(0, 1, 0),
                new Point3D(-1, 1, 1));
        double sqrt3 = Math.sqrt(1d / 3);
        assertEquals(new Vector(sqrt3, sqrt3, sqrt3), pl.getNormal(new Point3D(0, 0, 1)), "Bad normal to triangle");
    }

    @Test
    public void testFindIntersections(){

            Polygon pol = new Polygon(new Point3D(0, 0, 1), new Point3D(2, 0, 1), new Point3D(2, 2, 1),
                    new Point3D(0, 2, 1));
            Plane pl = new Plane(new Point3D(0, 0, 1), new Point3D(1, 0, 1), new Point3D(0, 1, 1));
            Ray ray;
            String errorPlane = "Wrong intersection with plane";
            String errorBad = "Bad intersection";
            // ============ Equivalence Partitions Tests ==============
            // TC01: Inside polygon
            ray = new Ray(new Point3D(1, 1, 0), new Vector(0, 0, 1));
            assertEquals(List.of(new Point3D(1, 1, 1)), pol.findIntersections(ray), errorBad);

            // TC02: Against edge
            ray = new Ray(new Point3D(-1, 1, 0), new Vector(0, 0, 1));
            assertEquals(List.of(new Point3D(-1, 1, 1)), pl.findIntersections(ray), errorPlane);
            assertNull(pol.findIntersections(ray), errorBad);

            // TC03: Against vertex
            ray = new Ray(new Point3D(-1, -1, 0), new Vector(0, 0, 1));
            assertEquals(List.of(new Point3D(-1, -1, 1)), pl.findIntersections(ray), errorPlane);
            assertNull(pol.findIntersections(ray), errorBad);

            // =============== Boundary Values Tests ==================
            // TC04: In vertex
            ray = new Ray(new Point3D(0, 2, 0), new Vector(0, 0, 1));
            assertEquals(List.of(new Point3D(0, 2, 1)), pl.findIntersections(ray), errorPlane);
            assertNull(pol.findIntersections(ray), errorBad);

            // TC05: On edge
            ray = new Ray(new Point3D(0, 1, 0), new Vector(0, 0, 1));
            assertEquals(List.of(new Point3D(0, 1, 1)), pl.findIntersections(ray), errorPlane);
            assertNull(pol.findIntersections(ray), errorBad);

            // TC06: On edge continuation
            ray = new Ray(new Point3D(0, 3, 0), new Vector(0, 0, 1));
            assertEquals(List.of(new Point3D(0, 3, 1)), pl.findIntersections(ray), errorPlane);
            assertNull(pol.findIntersections(ray), errorBad);

    }


}
