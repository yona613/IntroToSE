package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class TubeTest {

    @Test
   public void testGetNormal() {
        Tube tube = new Tube(new Ray(new Point3D(0,0,0), new Vector(0,0,1)),1d);
        Vector normal = new Vector(0,1,0);
        assertEquals(tube.getNormal(new Point3D(0,1,1)), normal, "Bad normal calculation !");
    }
}