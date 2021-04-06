package geometries;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * Tube class represents three-dimensional Tube in 3D Cartesian coordinate
 * system
 *
 * @author Hillel and Yona
 */
public class Tube extends RadialGeometry implements Geometry {

    /**
     * Ray of the Tube
     */
    protected Ray _axisRay;

    public Ray getAxisRay() {
        return _axisRay;
    }

    public Tube(double radius, Ray axisRay) {
        super(radius);
        _axisRay = axisRay;
    }

    @Override
    public Vector getNormal(Point3D point) {
        double t = _axisRay.get_dir().dotProduct(point.subtract(_axisRay.get_p0()));
        Point3D o = _axisRay.get_p0().add(_axisRay.get_dir().scale(t));
        return point.subtract(o).normalize();
    }

    @Override
    public String toString() {
        return "Tube{" +
                "_axisRay=" + _axisRay +
                ", _radius=" + _radius +
                '}';
    }

    @Override
    public List<Point3D> findIntersections(Ray ray) {

        Vector v = ray.get_dir();
        Vector va = this.getAxisRay().get_dir();

        if (v.normalize().equals(va.normalize()))
            return null;

        double vva;
        double pva;
        double a;
        double b;
        double c;

        if (ray.get_p0().equals(this._axisRay.get_p0())){
            vva = v.dotProduct(va);
            if (vva == 0){
                a = v.dotProduct(v);
            }
            else{
                a = (v.substract(va.scale(vva))).dotProduct(v.substract(va.scale(vva)));
            }
            b = 0;
            c = - _radius * _radius;
        }
        else{
            Vector deltaP = ray.get_p0().subtract(this._axisRay.get_p0());
            vva = v.dotProduct(va);
            pva = deltaP.dotProduct(va);

            if (vva == 0 && pva == 0){
                a = v.dotProduct(v);
                b = 2 * v.dotProduct(deltaP);
                c = deltaP.dotProduct(deltaP) - _radius * _radius;
            }
            else if (vva == 0){
                a = v.dotProduct(v);
                if (deltaP.equals(va.scale(deltaP.dotProduct(va)))){
                    b = 0;
                    c = - _radius * _radius;
                }
                else{
                    b = 2 * v.dotProduct(deltaP.substract(va.scale(deltaP.dotProduct(va))));
                    c = (deltaP.substract(va.scale(deltaP.dotProduct(va))).dotProduct(deltaP.substract(va.scale(deltaP.dotProduct(va))))) - this._radius * this._radius;
                }
            }
            else if (pva == 0){
                a = (v.substract(va.scale(vva))).dotProduct(v.substract(va.scale(vva)));
                b = 2 * v.substract(va.scale(vva)).dotProduct(deltaP);
                c = (deltaP.dotProduct(deltaP)) - this._radius * this._radius;
            }
            else {
                a = (v.substract(va.scale(vva))).dotProduct(v.substract(va.scale(vva)));
                if (deltaP.equals(va.scale(deltaP.dotProduct(va)))){
                    b = 0;
                    c = - _radius * _radius;
                }
                else{
                    b = 2 * v.substract(va.scale(vva)).dotProduct(deltaP.substract(va.scale(deltaP.dotProduct(va))));
                    c = (deltaP.substract(va.scale(deltaP.dotProduct(va))).dotProduct(deltaP.substract(va.scale(deltaP.dotProduct(va))))) - this._radius * this._radius;
                }
            }
        }

        double delta = b * b - 4 * a * c;

        if (delta < 0){
            return null; // no intersections
        }
        else if (delta == 0){
            /*double t = - b / (2 * a);
            if (t > 0){
                Point3D p = new Point3D(ray.getPoint(t));
                return List.of(p);
            }*/
            return null;
        }
        else{
            double t1 = (- b - Math.sqrt(delta)) / (2 * a);
            double t2 = (- b + Math.sqrt(delta)) / (2 * a);
            if (t1 > 0 && t2 > 0){
                Point3D p1 = new Point3D(ray.getPoint(t1));
                Point3D p2 = new Point3D(ray.getPoint(t2));
                return List.of(p1, p2);
            }
            else if (t1 > 0){
                Point3D p1 = new Point3D(ray.getPoint(t1));
                return List.of(p1);
            }
            else if (t2 > 0){
                Point3D p2 = new Point3D(ray.getPoint(t2));
                return List.of(p2);
            }
        }
        return null;
    }
}
