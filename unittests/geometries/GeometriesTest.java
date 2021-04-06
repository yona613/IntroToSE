package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing Geometries class
 * @author Yona
 *
 */
class GeometriesTest {

    /**
     * Test method for {@link Geometries#findIntersections(Ray)}.
     */
    @Test
    void findIntersectionsTest() {
        List<Point3D> result;
        Geometries geos = new Geometries(
                new Plane(new Point3D(2, 0, 0), new Vector(-1, 1, 0)),
                new Sphere(2d, new Point3D(5,0,0)),
                new Triangle(new Point3D(8.5, -1, 0), new Point3D(7.5, 1.5, 1), new Point3D(7.5, 1.5, -1))
        );

        // ============ Equivalence Partitions Tests ==============
        // TC01: Some geo intersect
        result = geos.findIntersections(new Ray(new Point3D(1, 0, 0), new Vector(7, 3, 0)));
        assertNotNull(result, "It is empty!");
        assertEquals(3, result.size(), "Bad intersects");

        // =============== Boundary Values Tests ==================
        // TC11: Empty collection
        result = new Geometries().findIntersections(new Ray(new Point3D(1, 0, 0), new Vector(1, 0, 0)));
        assertNull(result, "It is not empty!");

        // TC12: None geo intersect
        result = geos.findIntersections(new Ray(new Point3D(1, 0, 0), new Vector(1, 3, 0)));
        assertNull(result, "Bad intersects");

        // TC13: Single geo intersect
        result = geos.findIntersections(new Ray(new Point3D(1, 0, 0), new Vector(4, 3, 0)));
        assertNotNull(result, "It is empty!");
        assertEquals(1, result.size(), "Bad intersects");

        // TC14: All geo intersect
        result = geos.findIntersections(new Ray(new Point3D(1, 0, 0), new Vector(7, 1, 0)));
        assertNotNull(result, "It is empty!");
        assertEquals(4, result.size(), "Bad intersects");

    }
}