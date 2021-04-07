package geometries;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;
import static primitives.Util.*;


import java.util.List;

/**
 * Triangle class represents two-dimensional Tube in 3D Cartesian coordinate
 * system
 *
 * @author Hillel and Yona
 */
public class Triangle extends Polygon {

    public Triangle(Point3D... vertices) {
        super(vertices);
    }

    @Override
    public List<Point3D> findIntersections(Ray ray) {

         /*

         We're starting to check if the plane where our triangle is ,intersect the ray ,if not return null,if yes :



        */
        List<Point3D> intersections = _plane.findIntersections(ray);
        if (intersections == null) return null;//Our plan doesn't intersect the ray

        Point3D p0 = ray.get_p0();
        Vector v = ray.get_dir();
//This step is necessary if the ray doesn't start at 0,0,0
        Vector v1 = _vertices.get(0).subtract(p0);
        Vector v2 = _vertices.get(1).subtract(p0);
        Vector v3 = _vertices.get(2).subtract(p0);
//Check every side of the triangle
        double s1 = v.dotProduct(v1.crossProduct(v2));

        if (isZero(s1)) return null;

        double s2 = v.dotProduct(v2.crossProduct(v3));

        if (isZero(s2)) return null;

        double s3 = v.dotProduct(v3.crossProduct(v1));

        if (isZero(s3)) return null;

        if (!((s1 > 0 && s2 > 0 && s3 > 0) || (s1 < 0 && s2 < 0 && s3 < 0))) return null;

        return intersections;
    }
}
