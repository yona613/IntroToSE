package primitives;

import java.util.Objects;

import static primitives.Point3D.ZERO;

public class Vector {

    private Point3D head;

    public Vector(double a, double b, double c) {
        head = new Point3D(a, b, c);
        if (head.equals(ZERO))
            throw new IllegalArgumentException("Zero vector is forbidden !");
    }

    public Vector(Coordinate x, Coordinate y, Coordinate z) {
        head = new Point3D(x, y, z);
        if (head.equals(ZERO))
            throw new IllegalArgumentException("Zero vector is forbidden !");
    }

    public Vector(Point3D head) {
        this.head = new Point3D(head._x, head._y, head._z);
    }

    /**
     * returns result of addition of 2 vectors
     *
     * @param other
     * @return result vector
     */

    public Vector add(Vector other) {
        return new Vector(this.head._x.coord + other.head._x.coord,
                this.head._y.coord + other.head._y.coord,
                this.head._z.coord + other.head._z.coord);

    }

    /**
     * returns result of substraction of 2 vectors
     *
     * @param other
     * @return result vector
     */
    public Vector substract(Vector other) {

        return new Vector(this.head._x.coord - other.head._x.coord,
                this.head._y.coord - other.head._y.coord,
                this.head._z.coord - other.head._z.coord);
    }

    /**
     * returns vector result of product with scale
     *
     * @param scale
     * @return result vector
     */
    public Vector scale(double scale) {
        return new Vector(this.head._x.coord * scale,
                this.head._y.coord * scale,
                this.head._z.coord * scale);

    }

    public Point3D getHead() {
        return head;
    }

    /**
     * normalize the vector
     *
     * @return the vector normalized
     */
    public Vector normalize() {
        this.head = new Point3D(head._x.coord / length(), head._y.coord / length(), head._z.coord / length());
        return this;
    }

    /**
     * returns a new normalized vector based on that one
     *
     * @return normalized new vector
     */
    public Vector normalized() {
        return new Vector(head._x.coord,
                head._y.coord,
                head._z.coord).normalize();
    }

    /**
     * returns result of dot products with other vector
     *
     * @param other other vector
     * @return result of dot product
     */
    public double dotProduct(Vector other) {
        return head._x.coord * other.head._x.coord + head._y.coord * other.head._y.coord
                + head._z.coord * other.head._z.coord;
    }

    /**
     * returns result of cross product with other vector
     *
     * @param other the other vector
     * @return vector that is result of cross product
     */
    public Vector crossProduct(Vector other) {
        return new Vector(head._y.coord * other.head._z.coord - head._z.coord * other.head._y.coord,
                head._z.coord * other.head._x.coord - head._x.coord * other.head._z.coord,
                head._x.coord * other.head._y.coord - head._y.coord * other.head._x.coord);
    }

    /**
     * returns length of vector
     *
     * @return the length
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * returns squared length of vector
     *
     * @return squared length
     */
    public double lengthSquared() {
        return ZERO.distanceSquared(head);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return head.equals(vector.head);
    }

    @Override
    public int hashCode() {
        return Objects.hash(head);
    }

    @Override
    public String toString() {
        return "Vector{" +
                "head=" + head +
                '}';
    }

}
