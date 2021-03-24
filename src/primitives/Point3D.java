package primitives;

import java.util.Objects;

public class Point3D {

    final Coordinate _x;
    final Coordinate _y;
    final Coordinate _z;

    public final static Point3D ZERO = new Point3D(0.0, 0.0, 0.0);

    public Point3D(Coordinate _x, Coordinate _y, Coordinate _z) {
        this._x = new Coordinate(_x.coord);
        this._y = new Coordinate(_y.coord);
        this._z = new Coordinate(_z.coord);
    }

    public Point3D(double a, double b, double c) {
        this._x = new Coordinate(a);
        this._y = new Coordinate(b);
        this._z = new Coordinate(c);
    }

    /**
     * distance between two 3D points
     *
     * @param other the second point
     * @return the distance
     */
    public double distance(Point3D other) {
        return Math.sqrt(distanceSquared(other));
    }

    /**
     * squared distance between two 3D points
     *
     * @param other the second point
     * @return the distance
     */
    public double distanceSquared(Point3D other) {
        double d1 = _x.coord - other._x.coord;
        double d2 = _y.coord - other._y.coord;
        double d3 = _z.coord - other._z.coord;
        return d1 * d1 + d2 * d2 + d3 * d3;
    }

    /**
     * adds vector to a point and return the getted point
     *
     * @param vector the vector to add
     * @return the new point
     */
    public Point3D add(Vector vector) {
        Point3D other = vector.getHead();
        return new Point3D(_x.coord + other._x.coord,
                _y.coord + other._y.coord,
                _z.coord + other._z.coord);
    }

    /**
     * vector from substraction of two point's coordinates
     *
     * @param other the second point
     * @return the vector
     */
    public Vector subtract(Point3D other) {
        return new Vector(_x.coord - other._x.coord, _y.coord - other._y.coord, _z.coord - other._z.coord);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Point3D))
            return false;
        Point3D oth = (Point3D) obj;
        return _x.equals(oth._x) && _y.equals(oth._y) && _z.equals(oth._z);
    }

    @Override
    public String toString() {
        return "Point3D{" +
                "x=" + _x +
                ", y=" + _y +
                ", z=" + _z +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(_x, _y, _z);
    }

    public double getX() {
        return _x.coord;
    }
}
