package renderer;

import primitives.Color;
import primitives.Point3D;
import primitives.Ray;
import scene.Scene;

/**
 * Class to implement rayTracing between the camera rays and the scene
 *
 * @author Hillel & Yona
 */
public class RayTracerBasic extends RayTracerBase {

    public RayTracerBasic(Scene scene) {
        super(scene);
    }

    /**
     * Get color of the intersection of the ray with the scene
     * @param ray Ray to trace
     * @return Color of intersection
     */
    @Override
    public Color traceRay(Ray ray) {

        var myPoints = _scene.geometries.findIntersections(ray);

        if (myPoints == null)
            return _scene.background;

        Point3D myPoint = ray.findClosestPoint(myPoints);

        return calcColor(myPoint);
    }

    /**
     * Get the color of an intersection point
     * @param point point of intersection
     * @return Color of the intersection poitn
     */
    private Color calcColor(Point3D point){
        return _scene.ambientLight.getIntensity();
    }
}
