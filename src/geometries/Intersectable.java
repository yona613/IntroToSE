package geometries;

import primitives.Point3D;
import primitives.Ray;

import java.util.List;

/**
 * Class for interface intersectable with fonction that returns list of intersections of a ray with the geometry object
 */
public interface Intersectable {
    /**
     * Function that finds all intersections between geometric element and ray
     * @param ray
     * @return List of points of intersection
     */
     List<Point3D> findIntersections(Ray ray);

}

public static class GeoPoint {
    public Geometry geometry;
    public Point3D point;
}

