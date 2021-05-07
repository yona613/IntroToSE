package renderer;

import elements.LightSource;
import geometries.Intersectable.GeoPoint;
import primitives.Color;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import java.util.List;

import static primitives.Util.alignZero;

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

        return calcColor(myPoint, ray);
    }

    /**
     * Get the color of an intersection point
     * @param point point of intersection
     * @return Color of the intersection poitn
     */
    private Color calcColor(GeoPoint point, Ray ray){
        return _scene.ambientLight.getIntensity()
                .add(point.geometry.getEmission())
                .add(calcLocalEffects(point, ray));
    }


    private Color calcLocalEffects(GeoPoint point, Ray ray) {
        Vector v = ray.get_dir();
        Vector n = point.geometry.getNormal(point.point);
        double nv = alignZero(n.dotProduct(v));
        if (nv == 0) return Color.BLACK;
        double nShininess = point.geometry.getMaterial()._nShininess;
        double kd = point.geometry.getMaterial()._kd;
        double ks = point.geometry.getMaterial()._ks;
        Color color = Color.BLACK;
        for (LightSource lightSource : _scene.lights) {
            Vector l = lightSource.getL(point.point);
            double nl = alignZero(n.dotProduct(l));
            if (nl * nv > 0) { // sign(nl) == sing(nv)
                Color lightIntensity = lightSource.getIntensity(point.point);
                color = color.add(calcDiffusive(kd, l, n, lightIntensity),
                        calcSpecular(ks, l, n, v, nShininess, lightIntensity));
            }
        }

        return color;
    }

    private Color calcDiffusive(double kd, Vector l, Vector n, Color lightIntensity){
        double lN;
        try {
            lN = l.normalized().dotProduct(n.normalized());
        }
        catch (Exception exception) {
            return lightIntensity.scale(0);
        }
        return lightIntensity.scale(Math.abs(lN) * kd);
    }

    private Color calcSpecular(double ks, Vector l, Vector n, Vector v, double nShininess, Color lightIntensity){

        Vector r = l.add(n.scale(n.dotProduct(l) * -2));
        double vR;
        try {
            vR = v.scale(-1).normalized().dotProduct(r.normalized());
        }
        catch (Exception exception) {
            return lightIntensity.scale(1);
        }

        return lightIntensity.scale(ks * Math.pow(Math.max(0, vR), nShininess));
    }
}
