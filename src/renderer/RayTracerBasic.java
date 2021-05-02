package renderer;

import geometries.Intersectable.GeoPoint;
import primitives.Color;
import primitives.Point3D;
import primitives.Ray;
import scene.Scene;

import java.util.List;

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

        List<GeoPoint> myPoints = _scene.geometries.findGeoIntersections(ray);

        if (myPoints == null)
            return _scene.background;

        GeoPoint myPoint = ray.getClosestGeoPoint(myPoints);

        return calcColor(myPoint);
    }

    /**
     * Get the color of an intersection point
     * @param point point of intersection
     * @return Color of the intersection poitn
     */
    private Color calcColor(GeoPoint point){
        return point.geometry.getEmission();
    }


//    private Color calcLocalEffects(GeoPoint intersection, Ray ray) {
//        Vector v = ray.getDir (); Vector n = intersection.geometry.getNormal();
//        double nv = alignZero(n.dotProduct(v); if (nv == 0) return Color.BLACK;
//        int nShininess = intersection.geometry.;
//        double kd = intersection.geometry.getKd(), ks = intersection.geometry.getKs();
//        Color color = Color.BLACK;
//        for (LightSource lightSource : scene.lights) {
//            Vector l = lightSource.getL(intersection.point);
//            double nl = alignZero(n.dotProduct(l);
//            if (nl * nv > 0) { // sign(nl) == sing(nv)
//                Color lightIntensity = lightSource.getIntensity(intersection.point);
//                color = color.add(calcDiffusive(kd, l, n, lightIntensity),
//                        calcSpecular(ks, l, n, v, nShininess, lightIntensity));
//            }
//        }
//        return color;
//    }
}
