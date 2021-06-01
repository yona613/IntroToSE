package renderer;

import elements.LightSource;
import geometries.Intersectable.GeoPoint;
import primitives.*;
import scene.Scene;

import java.util.List;

import static primitives.Util.alignZero;

/**
 * Class to implement rayTracing between the camera rays and the scene
 *
 * @author Hillel & Yona
 */
public class RayTracerBasic extends RayTracerBase {

    private static final double INITIAL_K = 1.0;
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;

    public RayTracerBasic(Scene scene) {
        super(scene);
    }

    /**
     * Get color of the intersection of the ray with the scene
     *
     * @param ray Ray to trace
     * @return Color of intersection
     */
    @Override
    public Color traceRay(Ray ray) {

        List<GeoPoint> myPoints = _scene.geometries.findGeoIntersections(ray);
        if (myPoints != null) {
            GeoPoint myPoint = ray.getClosestGeoPoint(myPoints);
            return calcColor(myPoint, ray);
        }
        return _scene.background;
    }

    /**
     * Calculate color using recursive function
     *
     * @param geopoint the point of intersection
     * @param ray      the ray
     * @return the color
     */
    private Color calcColor(GeoPoint geopoint, Ray ray) {
        return calcColor(geopoint, ray, MAX_CALC_COLOR_LEVEL, INITIAL_K)
                .add(_scene.ambientLight.getIntensity());
    }

    /**
     * Get the color of an intersection point using the Phong model
     * Recursive function
     *
     * @param geoPoint point of intersection
     * @return Color of the intersection point
     */
    private Color calcColor(GeoPoint geoPoint, Ray ray, int level, double k) {
        Color color = geoPoint.geometry.getEmission()
                .add(calcLocalEffects(geoPoint, ray, k));
        return 1 == level ? color : color.add(calcGlobalEffects(geoPoint, ray, level, k));
    }

    /**
     * Calculate global effect of the light on the color
     *
     * @param geopoint the point
     * @param inRay    the ray
     * @param level    level
     * @param k
     * @return the color
     */
    private Color calcGlobalEffects(GeoPoint geopoint, Ray inRay, int level, double k) {
        Color color = Color.BLACK;
        Material material = geopoint.geometry.getMaterial();
        double kr = material.kR;
        double kkr = k * kr;
        Vector n = geopoint.geometry.getNormal(geopoint.point);
        if (kkr > MIN_CALC_COLOR_K) {
            Ray reflectedRay = constructReflectedRay(n, geopoint.point, inRay);
            GeoPoint reflectedPoint = findClosestIntersection(reflectedRay);
            if (reflectedPoint != null){
                color = color.add(calcColor(reflectedPoint, reflectedRay, level - 1, kkr).scale(kr));
            }
        }
        double kt = material.kT;
        double kkt = k * kt;
        if (kkt > MIN_CALC_COLOR_K) {
            Ray refractedRay = constructRefractedRay(n, geopoint.point, inRay);
            GeoPoint refractedPoint = findClosestIntersection(refractedRay);
            if (refractedPoint != null){
                color = color.add(calcColor(refractedPoint, refractedRay, level - 1, kkt).scale(kt));
            }
        }
        return color;
    }

    /**
     * Construct the ray getting refracted on a point
     * @param n normal to the point
     * @param point the point
     * @param inRay the ray entering
     * @return the refracted ray
     */
    private Ray constructRefractedRay(Vector n, Point3D point, Ray inRay) {
        return new Ray(point, inRay.get_dir(), n);
    }

    /**
     * Construct the ray getting reflected on a point
     * @param n normal to the point
     * @param point the point
     * @param inRay the ray entering
     * @return the reflected ray
     */
    private Ray constructReflectedRay(Vector n, Point3D point, Ray inRay) {
        //𝒓 = 𝒗 − 𝟐 ∙ (𝒗 ∙ 𝒏) ∙ 𝒏
        Vector v = inRay.get_dir();
        Vector r = null;
        try {
            r = v.substract(n.scale(v.dotProduct(n)).scale(2)).normalized();
        } catch (Exception e) {
            return null;
        }
        return new Ray(point, r, n);
    }

    /**
     * Find closest intersection point between a ray and the scene's geometries
     * @param ray the ray
     * @return the closest point
     */
    private GeoPoint findClosestIntersection(Ray ray) {
        List<GeoPoint> geoPoints = _scene.geometries.findGeoIntersections(ray);
        return ray.getClosestGeoPoint(geoPoints);
    }


    /**
     * Calculate the color of the local effects of the light
     *
     * @param point point calculated
     * @param ray ray entering to the point
     * @return local color effect on the point
     */
    private Color calcLocalEffects(GeoPoint point, Ray ray, double k) {
        //direction vector of the ray
        Vector v = ray.get_dir();
        //normal to geometry of the point
        Vector n = point.geometry.getNormal(point.point);
        //check if normal to the geometry is orthogonal to ray
        double nv = alignZero(n.dotProduct(v));
        if (nv == 0) return Color.BLACK; //then no color
        double nShininess = point.geometry.getMaterial()._nShininess;
        double kd = point.geometry.getMaterial()._kd;
        double ks = point.geometry.getMaterial()._ks;
        Color color = Color.BLACK;
        //get color given by every light source
        for (LightSource lightSource : _scene.lights) {
            Vector l = lightSource.getL(point.point);
            double nl = alignZero(n.dotProduct(l));
            if (nl * nv > 0) { // sign(nl) == sign(nv)
                //get transparency of the object
                double ktr = transparency(lightSource, l, n, point);
                if (ktr * k > MIN_CALC_COLOR_K) { //check if the depth of calculation was reached then don't calculate any more
                    // color is scaled by transparency to get the right color effect
                    Color lightIntensity = lightSource.getIntensity(point.point).scale(ktr);
                    //get effects of the color and add them to the color
                    color = color.add(calcDiffusive(kd, l, n, lightIntensity),
                            calcSpecular(ks, l, n, v, nShininess, lightIntensity));
                }
            }
        }
        return color;
    }


    /**
     * Calculate color of the diffusive effects of the light
     *
     * @param kd diffusive ratio
     * @param l light's direction vector
     * @param n normal vector
     * @param lightIntensity intensity of the light
     * @return color of the diffusive effect
     */
    private Color calcDiffusive(double kd, Vector l, Vector n, Color lightIntensity) {
        double lN;
        try {
            lN = l.normalized().dotProduct(n.normalized());
        } catch (Exception exception) {
            return lightIntensity.scale(0);
        }
        //color = light * |l.n| * kd
        return lightIntensity.scale(Math.abs(lN) * kd);
    }

    /**
     * Calculate color of the specular effects of the light
     *
     * @param ks specular ratio
     * @param l light's direction vector
     * @param n normal vector
     * @param v ray's direction vector
     * @param nShininess shininess of the object
     * @param lightIntensity intensity of the light
     * @return color of specular effect
     */
    private Color calcSpecular(double ks, Vector l, Vector n, Vector v, double nShininess, Color lightIntensity) {

        Vector r = l.add(n.scale(n.dotProduct(l) * -2));
        double vR;
        try {
            vR = v.scale(-1).normalized().dotProduct(r.normalized());
        } catch (Exception exception) {
            //vR = 0, 0^everything = 1
            return lightIntensity.scale(1);
        }
        //color = ks * max(0, -v.r)^nSh
        return lightIntensity.scale(ks * Math.pow(Math.max(0, vR), nShininess));
    }

    /**
     * Calculate value of transparency of the point
     *
     * @param light light source
     * @param l light to point direction vector (normalized)
     * @param n normal vector (normalized)
     * @param geopoint checked geo-point
     * @return value of the transparency
     */
    private double transparency(LightSource light, Vector l, Vector n, GeoPoint geopoint) {
        Vector lightDirection = l.scale(-1); // from point to light source
        Ray lightRay = new Ray(geopoint.point, lightDirection, n);
        double lightDistance = light.getDistance(geopoint.point);
        var intersections = _scene.geometries.findGeoIntersections(lightRay);
        if (intersections == null) return 1.0; //no intersection
        double ktr = 1.0;
        for (GeoPoint gp : intersections) {
            if (alignZero(gp.point.distance(geopoint.point) - lightDistance) <= 0) {
                ktr *= gp.geometry.getMaterial().kT;
                if (ktr < MIN_CALC_COLOR_K) return 0.0;
            }
        }
        return ktr;
    }

    /**
     * The function checks whether there are any objects shading the light source
     * from the point and returns false if it is and true otherwise
     * @param light light source
     * @param l light to point direction vector (normalized)
     * @param n normal vector (normalized)
     * @param geopoint checked geo-point
     * @return is the point unshaded
     */
    private boolean unshaded(LightSource light, Vector l, Vector n, GeoPoint geopoint) {
        Vector lightDirection = l.scale(-1); // from point to light source
        Ray lightRay = new Ray(geopoint.point, lightDirection, n); // refactored ray head move
        List<GeoPoint> intersections = _scene.geometries.findGeoIntersections(lightRay, light.getDistance(geopoint.point));
        if (intersections != null) {
            double lightDistance = light.getDistance(geopoint.point);
            for (GeoPoint intersection : intersections) {
                if (alignZero(intersection.point.distance(geopoint.point) - lightDistance) <= 0 && intersection.geometry.getMaterial().kT == 0){
                    return false;
                }
            }
        }
        return true;
    }
}
