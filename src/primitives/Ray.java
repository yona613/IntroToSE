package primitives;

import geometries.Intersectable.GeoPoint;

import java.util.List;
import java.util.Objects;


/**
 * Class to implement a ray in our model
 */
public class Ray {

    /**
     * Base point of the ray
     */
    final Point3D _p0;

    /**
     * direction's vector of the ray
     */
    final Vector _dir;

    /**
     * delta to move the p0 of the vector because of the model constraints
     */
    private static final double DELTA = 0.1;


    public Ray(Point3D _p0, Vector _dir) {
        this._p0 = _p0;
        this._dir = _dir.normalized();
    }

    public Ray(Point3D head, Vector direction, Vector normal) {
        double delta = direction.dotProduct(normal) >= 0 ? DELTA : -DELTA;
        _p0 = head.add(normal.scale(delta));
        _dir = direction;
    }

    public Point3D get_p0() {
        return _p0;
    }

    public Vector get_dir() {
        return _dir;
    }

    /**
     * Get point on ray at a distance from ray's head
     *
     * @param t distance from ray head
     * @return the point
     */
    public Point3D getPoint(double t) {
        if (t == 0)
            return _p0;
        else {
            try {
                Point3D point = new Point3D(_p0).add(_dir.scale(t));
                return point;
            } catch (Exception exception) {
                return null;
            }
        }
    }

    /**
     * Find intersections of a ray with a list of Point3D and return the
     * intersection point that is closest to the ray head. If there are no
     * intersections, null will be returned.
     *
     * @return the closest point
     */
    public Point3D findClosestPoint(List<Point3D> points) {
        if (points == null)
            return null;

        Point3D myPoint = points.get(0);

        for (Point3D point : points
        ) {
            if (_p0.distance(myPoint) > _p0.distance(point)) {
                myPoint = point;
            }
        }

        return myPoint;
    }

    /**
     * get the closest GeoPoint in the list of points
     * @param points list of intersection points
     * @return the closest point
     */
    public GeoPoint getClosestGeoPoint(List<GeoPoint> points) {
        if (points == null)
            return null;

        GeoPoint myPoint = points.get(0);

        for (var point : points
        ) {
            if (_p0.distance(myPoint.point) > _p0.distance(point.point)) {
                myPoint = point;
            }
        }

        return myPoint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ray ray = (Ray) o;
        return _p0.equals(ray._p0) && _dir.equals(ray._dir);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_p0, _dir);
    }

    @Override
    public String toString() {
        return "Ray{" +
                "p0=" + _p0 +
                ", dir=" + _dir +
                '}';
    }
}
