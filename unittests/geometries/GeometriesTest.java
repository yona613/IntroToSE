package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GeometriesTest {

    @Test
    void findIntersectionsTest() {

        Geometries geos;

        // ============ Equivalence Partitions Tests ==============

        //TC01 some geometries are intersected by ray

        geos = new Geometries();
        Cylinder cylinder = new Cylinder(1d,new Ray(new Point3D(2,0,0), new Vector(0,0,1)), 2d);
        Tube tube = new Tube(1d, new Ray(new Point3D(2,0,0), new Vector(0,0,1)));
        Plane pl = new Plane(new Point3D(0, 0, 1), new Vector(1, 1, 1));
        Sphere sphere = new Sphere(1d, new Point3D(1, 0, 0));
        geos.add(cylinder,tube,pl,sphere);
        List<Point3D> result = geos.findIntersections(new Ray(new Point3D(-2,0,0), new Vector(1,0,0)));
        assertEquals(7, result.size(), "Wrong number of intersections");


        // =============== Boundary Values Tests ==================

        //TC02 no geometries is crossed by the ray

        geos = new Geometries();
        geos.add(new Sphere(1d, new Point3D(0,0,0)));
        geos.add(cylinder,tube,pl);
        assertNull(geos.findIntersections(new Ray(new Point3D(25,25,255), new Vector(1,2,30))), "Wrong number of intersections");

        //TC03 the collection is empty

        geos = new Geometries();
        assertNull(geos.findIntersections(new Ray(new Point3D(0,0,1), new Vector(1,1,1))), "Wrong number of points");

        //TC04 only one geometrie is crossed


    }
}