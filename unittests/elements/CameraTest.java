package elements;

import geometries.Intersectable;
import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import org.junit.jupiter.api.Test;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CameraTest {

    @Test
    public void testConstructRayThroughPixel() {
        Camera camera = new Camera(Point3D.ZERO, new Vector(0, 0, 1), new Vector(0, -1, 0)).setDistance(10);

        // ============ Equivalence Partitions Tests ==============
        // TC01: 3X3 Corner (0,0)
        assertEquals(new Ray(Point3D.ZERO, new Vector(-2, -2, 10)),
                camera.setViewPlaneSize(6, 6).constructRayThroughPixel(3, 3, 0, 0));

        // TC02: 4X4 Corner (0,0)
        assertEquals(new Ray(Point3D.ZERO, new Vector(-3, -3, 10)),
                camera.setViewPlaneSize(8, 8).constructRayThroughPixel(4, 4, 0, 0));

        // TC03: 4X4 Side (0,1)
        assertEquals(new Ray(Point3D.ZERO, new Vector(-1, -3, 10)),
                camera.setViewPlaneSize(8, 8).constructRayThroughPixel(4, 4, 1, 0));

        // TC04: 4X4 Inside (1,1)
        assertEquals(new Ray(Point3D.ZERO, new Vector(-1, -1, 10)),
                camera.setViewPlaneSize(8, 8).constructRayThroughPixel(4, 4, 1, 1));

        // =============== Boundary Values Tests ==================
        // TC11: 3X3 Center (1,1)
        assertEquals(new Ray(Point3D.ZERO, new Vector(0, 0, 10)),
                camera.setViewPlaneSize(6, 6).constructRayThroughPixel(3, 3, 1, 1));

        // TC12: 3X3 Center of Upper Side (0,1)
        assertEquals(new Ray(Point3D.ZERO, new Vector(0, -2, 10)),
                camera.setViewPlaneSize(6, 6).constructRayThroughPixel(3, 3, 1, 0));

        // TC13: 3X3 Center of Left Side (1,0)
        assertEquals(new Ray(Point3D.ZERO, new Vector(-2, 0, 10)),
                camera.setViewPlaneSize(6, 6).constructRayThroughPixel(3, 3, 0, 1));


        // ============ Equivalence Partitions Tests ==============
        // TC01: Sphere r = 1, two intersection points
        camera = new Camera(new Point3D(0,0,0), new Vector(0,0,-1), new Vector(0,1,0));

        assertEquals(2, countIntersectionsCameraGeometry(
                camera.setDistance(1d).setViewPlaneSize(3d,3d),
                3,3,
                new Sphere(1d, new Point3D(0,0,-3))
        ), "Bad number of intersections");
    }

    @Test
    public void testIntersectionsCameraGeometries(){



    }

    /**
     * Integration tests of Camera Ray construction with Ray-Sphere intersections
     */


    private int countIntersectionsCameraGeometry(Camera camera, int nX, int nY, Intersectable geometry){

        int count = 0;
        List<Point3D> intersections;
        for (int i = 0; i <= nX/2; i++) {
            for (int j = 0; j <= nY/2; j++) {
                if (i == 0 && j == 0){
                    if ((nX % 2 != 0) && (nY % 2 != 0)){
                        intersections = geometry.findIntersections(camera.constructRayThroughPixel(nX,nY,j,i));
                        count += intersections== null ? 0 : intersections.size();
                    }
                }
                else if (i == 0){
                    intersections = geometry.findIntersections(camera.constructRayThroughPixel(nX,nY,j,i));
                    count += intersections== null ? 0 : intersections.size();
                    intersections = geometry.findIntersections(camera.constructRayThroughPixel(nX,nY,(j * - 1),i));
                    count += intersections== null ? 0 : intersections.size();
                }
                else if (j == 0){
                    intersections = geometry.findIntersections(camera.constructRayThroughPixel(nX,nY,j,i));
                    count += intersections== null ? 0 : intersections.size();
                    intersections = geometry.findIntersections(camera.constructRayThroughPixel(nX,nY,j,(i * -1)));
                    count += intersections== null ? 0 : intersections.size();
                }
                else {
                    intersections = geometry.findIntersections(camera.constructRayThroughPixel(nX,nY,j,i));
                    count += intersections== null ? 0 : intersections.size();
                    intersections = geometry.findIntersections(camera.constructRayThroughPixel(nX,nY,(j * - 1),i));
                    count += intersections== null ? 0 : intersections.size();
                    intersections = geometry.findIntersections(camera.constructRayThroughPixel(nX,nY,j,(i * -1)));
                    count += intersections== null ? 0 : intersections.size();
                    intersections = geometry.findIntersections(camera.constructRayThroughPixel(nX,nY,(j * - 1),(i * -1)));
                    count += intersections== null ? 0 : intersections.size();
                }
            }
        }
        return count;
    }
    @Test
    public void CameraRaySphereIntegration() {
        Camera cam1 = new Camera(Point3D.ZERO, new Vector(0, 0, 1), new Vector(0, -1, 0));
        Camera cam2 = new Camera(new Point3D(0, 0, -0.5), new Vector(0, 0, 1), new Vector(0, -1, 0));
       cam1.setDistance(1);
       cam2.setDistance(1);

        // TC01:  18 points
        assertEquals(countIntersectionsCameraGeometry(cam1, 3, 3, new Sphere(1,new Point3D(0,0,3))),18,"Error !");

        // TC02: 9 points
        assertEquals(countIntersectionsCameraGeometry(cam2,3,3, new Sphere(4, new Point3D(0, 0, 1))), 9,"Error !");

        // TC03: 0 points
        assertEquals(countIntersectionsCameraGeometry(cam1,3,3, new Sphere(0.5, new Point3D(0, 0, -1))), 0,"Error !");

        //TC04: 10 points

        //TC05:2 points
    }
    @Test
    public void CameraRayTriangleIntegration() {
        Camera cam = new Camera(Point3D.ZERO, new Vector(0, 0, 1), new Vector(0, -1, 0));
        cam.setDistance(1);
        cam.setViewPlaneSize(3,3);
        // TC01:  1 point
        assertEquals(countIntersectionsCameraGeometry(cam,3,3,new Triangle(new Point3D(1, 1, 2), new Point3D(-1, 1, 2), new Point3D(0, -1, 2))),1,"Error ");

        // TC02:  2 points

    }

    @Test
    public void CameraRayPlaneIntegration() {
        Camera cam = new Camera(Point3D.ZERO, new Vector(0, 0, 1), new Vector(0, -1, 0));
        cam.setViewPlaneSize(3,3);
        cam.setDistance(1);
        // TC01: Plane against camera 9 points
        assertEquals(countIntersectionsCameraGeometry(cam,3,3 ,new Plane(new Point3D(0, 0, 5), new Vector(0, 0, 1))), 9,"Error !");

        // TC02: Plane with small angle 9 points
        assertEquals(countIntersectionsCameraGeometry(cam,3,3, new Plane(new Point3D(0, 0, 5), new Vector(0, -1, 2))), 9,"Error !");

        // TC03:  6 points

        // TC04:  0 points
    }
}