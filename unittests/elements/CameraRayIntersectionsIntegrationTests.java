package elements;

import geometries.Intersectable;
import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import org.junit.jupiter.api.Test;
import primitives.Point3D;
import primitives.Vector;
import elements.Camera.CameraBuilder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration tests of Camera Ray construction with Ray-Sphere intersections
 *
 * @author Yona & Hillel
 */
public class CameraRayIntersectionsIntegrationTests {


    private int countIntersectionsCameraGeometry(Camera camera, int nX, int nY, Intersectable geometry){

        //This function return the number of point of intersection between the geometries and a ray from our camera

        //In details,we get the position of the pixel by nX & nY and launch a ray to this pixel ,if there's no
        //intersection so the count will be null but if there's intersection point so we add them up to our variable
        //"count" and return it
        int count = 0;
        List<Point3D> intersections;

        for (int i = 0; i < nX; i++) {
            for (int j = 0; j < nY; j++) {
                intersections = geometry.findIntersections(camera.constructRayThroughPixel(nX, nY, j, i));
                count += intersections == null ? 0 : intersections.size();
            }
        }
        return count;
    }

    @Test
    public void CameraRaySphereIntegration() {

        Camera cam1 = new CameraBuilder(Point3D.ZERO, new Vector(0, 0, -1), new Vector(0, -1, 0))
                .setDistance(1d)
                .setViewPlaneSize(3d, 3d)
                .build();
        Camera cam2 = new CameraBuilder(new Point3D(0, 0, 0.5), new Vector(0, 0, -1), new Vector(0, -1, 0))
                .setDistance(1d)
                .setViewPlaneSize(3d, 3d)
                .build();

        //TC01:2 points
        assertEquals(2, countIntersectionsCameraGeometry(cam1, 3,3, new Sphere(1d, new Point3D(0,0,-3))), "Bad number of intersections");

        // TC02:  18 points
        assertEquals(18, countIntersectionsCameraGeometry(cam2, 3, 3, new Sphere(2.5, new Point3D(0,0,-2.5))),"Bad number of intersections");

        //TC03: 10 points
        assertEquals(10, countIntersectionsCameraGeometry(cam2, 3, 3, new Sphere(2d, new Point3D(0,0,-2))),"Bad number of intersections");

        // TC04: 9 points
        assertEquals(9, countIntersectionsCameraGeometry(cam2,3,3, new Sphere(4d, new Point3D(0, 0, 1))), "Bad number of intersections");

        // TC05: 0 points
        assertEquals(0, countIntersectionsCameraGeometry(cam1,3,3, new Sphere(0.5, new Point3D(0, 0, 1))), "Bad number of intersections");
    }


    @Test
    public void CameraRayTriangleIntegration() {
        Camera cam = new CameraBuilder(Point3D.ZERO, new Vector(0, 0, -1), new Vector(0, -1, 0))
                .setDistance(1d)
                .setViewPlaneSize(3d, 3d)
                .build();

        // TC01:  1 point
        assertEquals(1, countIntersectionsCameraGeometry(cam,3,3,new Triangle(new Point3D(1, -1, -2), new Point3D(-1, -1, -2), new Point3D(0, 1, -2))),"Bad number of intersections");

        // TC02:  2 points
        assertEquals(2, countIntersectionsCameraGeometry(cam,3,3,new Triangle(new Point3D(1, -1, -2), new Point3D(-1, -1, -2), new Point3D(0, 20, -2))),"Bad number of intersections");
    }

    @Test
    public void CameraRayPlaneIntegration() {
        Camera cam = new CameraBuilder(Point3D.ZERO, new Vector(0, 0, 1), new Vector(0, -1, 0))
                .setDistance(1d)
                .setViewPlaneSize(3d, 3d)
                .build();

        // TC01: Plane against camera 9 points
        assertEquals(countIntersectionsCameraGeometry(cam,3,3 ,new Plane(new Point3D(0, 0, 5), new Vector(0, 0, 1))), 9,"Bad number of intersections");

        // TC02: Plane with small angle 9 points
        assertEquals(countIntersectionsCameraGeometry(cam,3,3, new Plane(new Point3D(0, 0, 5), new Vector(0, -1, 2))), 9,"Bad number of intersections");

        // TC03:  6 points
        assertEquals(6, countIntersectionsCameraGeometry(cam, 3,3, new Plane(new Point3D(0,0,2), new Vector(1,1,1))), "Bad number of intersections");

        // TC04:  0 points
        assertEquals(0, countIntersectionsCameraGeometry(cam, 3, 3, new Plane(new Point3D(0, 0, -4), new Vector(0,0,1))), "Bad number of intersections");
    }
}
