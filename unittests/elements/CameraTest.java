package elements;

import org.junit.jupiter.api.Test;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;
import elements.Camera.*;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class CameraTest {

    @Test
    public void testConstructRayThroughPixel() {
        Camera camera = new CameraBuilder(Point3D.ZERO, new Vector(0, 0, 1), new Vector(0, -1, 0)).setDistance(10).build();

        // ============ Equivalence Partitions Tests ==============
        // TC01: 3X3 Corner (0,0)
        assertEquals(new Ray(Point3D.ZERO, new Vector(-2, -2, 10)),
               new CameraBuilder(camera).setViewPlaneSize(6, 6).build().constructRayThroughPixel(3, 3, 0, 0));

        // TC02: 4X4 Corner (0,0)
        assertEquals(new Ray(Point3D.ZERO, new Vector(-3, -3, 10)),
                new CameraBuilder(camera).setViewPlaneSize(8, 8).build().constructRayThroughPixel(4, 4, 0, 0));

        // TC03: 4X4 Side (0,1)
        assertEquals(new Ray(Point3D.ZERO, new Vector(-1, -3, 10)),
                new CameraBuilder(camera).setViewPlaneSize(8, 8).build().constructRayThroughPixel(4, 4, 1, 0));

        // TC04: 4X4 Inside (1,1)
        assertEquals(new Ray(Point3D.ZERO, new Vector(-1, -1, 10)),
                new CameraBuilder(camera).setViewPlaneSize(8, 8).build().constructRayThroughPixel(4, 4, 1, 1));

        // =============== Boundary Values Tests ==================
        // TC11: 3X3 Center (1,1)
        assertEquals(new Ray(Point3D.ZERO, new Vector(0, 0, 10)),
                new CameraBuilder(camera).setViewPlaneSize(6, 6).build().constructRayThroughPixel(3, 3, 1, 1));

        // TC12: 3X3 Center of Upper Side (0,1)
        assertEquals(new Ray(Point3D.ZERO, new Vector(0, -2, 10)),
                new CameraBuilder(camera).setViewPlaneSize(6, 6).build().constructRayThroughPixel(3, 3, 1, 0));

        // TC13: 3X3 Center of Left Side (1,0)
        assertEquals(new Ray(Point3D.ZERO, new Vector(-2, 0, 10)),
                new CameraBuilder(camera).setViewPlaneSize(6, 6).build().constructRayThroughPixel(3, 3, 0, 1));
    }

    @Test
    void turnCameraTest() {

        Camera camera = new CameraBuilder(Point3D.ZERO, new Vector(1,0,0), new Vector(0,0,1)).build();
        Camera camera2 = new CameraBuilder(Point3D.ZERO, new Vector(1,0,0), new Vector(0,-1,0)).build();
        camera.rotateCamera(new Vector(1,0,0),90);
        System.out.println(camera);
        System.out.println(camera2);

    }

    @Test
    void construct5RaysFromRay(){
        HashMap<Integer, Ray> myRays = new HashMap<>();
        Camera camera = new CameraBuilder(Point3D.ZERO, new Vector(1,0,0), new Vector(0,0,1)).setViewPlaneSize(3,3).setDistance(1).build();
        myRays.put(3, new Ray(new Point3D(0,0,0), new Vector(1,0,0)));
        myRays = camera.construct5RaysFromRay(myRays,1, 1);
    }
}