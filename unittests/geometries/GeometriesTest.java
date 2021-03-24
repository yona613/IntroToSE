package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class GeometriesTest {

    @Test
    void findIntersectionsTest() {
        Geometries geos = new Geometries();
        geos.add(new Sphere(1d, new Point3D(0,0,0)));
        assertNull(geos.findIntersections(new Ray(new Point3D(1,1,1), new Vector(1,2,3))));
    }
}