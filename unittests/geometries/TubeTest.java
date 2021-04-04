package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

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

    @Test
    void findIntersectionsTest() {

        Tube tube = new Tube(1d, new Ray(new Point3D(2,0,0), new Vector(0,0,1)));

        // ============ Equivalence Partitions Tests ==============

        //TC01 ray is outside and parallel to the tube's ray

        List<Point3D> result = tube.findIntersections(new Ray(new Point3D(5,0,0), new Vector(0,0,1)));
        assertNull(result, "Wrong number of points");


        //TC02 ray is inside and parallel to the tube's ray

        result = tube.findIntersections(new Ray(new Point3D(2.5,0,0), new Vector(0,0,1)));
        assertNull(result, "Wrong number of points");

        //TC03 ray starts from outside and crosses the tube

        result = tube.findIntersections(new Ray(new Point3D(-2,0,0), new Vector(1,0,0)));
        assertEquals(2, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point3D(1,0,0), new Point3D(3,0,0)), result, "Bad intersection points");

        //TC04 ray starts from inside and crosses the tube

        result = tube.findIntersections(new Ray(new Point3D(1.5,0,0), new Vector(1,0,0)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point3D(3,0,0)), result, "Bad intersection points");

        //TC05 ray starts from outside the tube and doesn't cross the tube

        result = tube.findIntersections(new Ray(new Point3D(5,0,0), new Vector(1,0,0)));
        assertNull(result, "Wrong number of points");

    }
}