package primitives;

import java.util.Objects;

import static primitives.Point3D.ZERO;

/**
 * Class to implement a vector in our model
 */
public class Vector {

    /**
     * Head of the vector
     */
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

    /***
     * Rotate the vector by angle (in degrees) and axis of rotation
     * @param axis Axis of rotation
     * @param theta Angle of rotation (degrees)
     */
    public void rotateVector(Vector axis, double theta) {

        //Use of Rodrigues' rotation formula
        //https://en.wikipedia.org/wiki/Rodrigues%27_rotation_formula
        //v: vector to rotate
        //k: axis of rotation
        //θ: angle of rotation
        //Vrot = v * Cos θ + (k * v) * Sin θ + k(k,v) * (1 - Cos θ)

        //Variables used in computing
        double x, y, z;
        double u, v, w;
        x = this.head.getX();
        y = this.head.getY();
        z = this.head.getZ();
        u = axis.head.getX();
        v = axis.head.getY();
        w = axis.head.getZ();
        double v1 = u * x + v * y + w * z;

        //Convert degrees to Rad
        double thetaRad = Math.toRadians(theta);

        //Calculate X's new coordinates
        double xPrime = u * v1 * (1d - Math.cos(thetaRad))
                + x * Math.cos(thetaRad)
                + (-w * y + v * z) * Math.sin(thetaRad);

        //Calculate Y's new coordinates
        double yPrime = v * v1 * (1d - Math.cos(thetaRad))
                + y * Math.cos(thetaRad)
                + (w * x - u * z) * Math.sin(thetaRad);

        //Calculate Z's new coordinates
        double zPrime = w * v1 * (1d - Math.cos(thetaRad))
                + z * Math.cos(thetaRad)
                + (-v * x + u * y) * Math.sin(thetaRad);

        this.head = new Point3D(xPrime, yPrime, zPrime);
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
