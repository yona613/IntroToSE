package geometries;

import primitives.Point3D;
import primitives.Ray;

import java.util.List;
import java.util.Objects;

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

<<<<<<< HEAD
    /**
     * Function that finds all intersections between geometric element and ray
     * @param ray
     * @return List of GeoPoints of intersection
     */
    public List<GeoPoint> findGeoIntersections(Ray ray);

    /**
     * Class that contains geometry and point for implementation of intersectable
     */
    public static class GeoPoint {
        public Geometry geometry;
        public Point3D point;

        /**
         * Constructor
         * @param geometry the geometry
         * @param point the point
         */
        public GeoPoint(Geometry geometry, Point3D point) {
            this.geometry = geometry;
            this.point = point;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GeoPoint geoPoint = (GeoPoint) o;
            return geometry.equals(geoPoint.geometry) && point.equals(geoPoint.point);
        }

        @Override
        public int hashCode() {
            return Objects.hash(geometry, point);
        }
    }

=======
}

public static class GeoPoint {
    public Geometry geometry;
    public Point3D point;
>>>>>>> main
}

