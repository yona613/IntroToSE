package primitives;

import java.util.List;
import java.util.Objects;


public class Ray {

    final Point3D _p0;
    final Vector _dir;

    public Ray(Point3D _p0, Vector _dir) {
        this._p0 = _p0;
        this._dir = _dir.normalized();
    }

    public Point3D get_p0() {
        return _p0;
    }

    public Vector get_dir() {
        return _dir;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ray ray = (Ray) o;
        return _p0.equals(ray._p0) && _dir.equals(ray._dir);
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
            return new Point3D(_p0).add(_dir.scale(t));
        }

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
}
