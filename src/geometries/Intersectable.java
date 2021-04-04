package geometries;

import primitives.Point3D;
import primitives.Ray;

import java.util.List;

/**
 * Class for interface intersectable with fonction that returns list of intersections of a ray with the geometry object
 */
public interface Intersectable {
    public List<Point3D> findIntersections(Ray ray);
}
