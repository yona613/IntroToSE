package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Class to implement a point light source of the scene
 */
public class PointLight extends Light implements LightSource {

    /**
     * Position of the light
     */
    protected Point3D _position;

    protected double _radius;

    /**
     * Parameters of the light
     */
    protected double _kC = 1d;
    protected double _kL = 0d;
    protected double _kQ = 0d;


    public PointLight setkC(double kC) {
        _kC = kC;
        return this;
    }

    public PointLight setkL(double kL) {
        _kL = kL;
        return this;
    }

    public PointLight setkQ(double kQ) {
        _kQ = kQ;
        return this;
    }

    public PointLight(Color c, Point3D pos) {
        super(c);
        _position = new Point3D(pos);
    }

    public PointLight(Color c, Point3D pos, double radius) {
        super(c);
        _position = new Point3D(pos);
        _radius = radius;
    }

    @Override
    public Color getIntensity(Point3D p) {
        double factor = _kC;
        double distance;
        try {
            distance = _position.distance(p);
            factor += _kL * distance + _kQ * distance * distance;
        } catch (Exception exception) {
        }

        return _intensity.scale(1 / factor);
    }

    @Override
    public Vector getL(Point3D p) {
        try {
            return p.subtract(this._position).normalized();
        } catch (Exception exception) {
            return null;
        }
    }

    @Override
    public List<Vector> getListL(Point3D p) {
        Random r = new Random();
        List<Vector> vectors = new LinkedList();
        for (double i = -_radius; i < _radius; i += _radius / 10) {
            for (double j = -_radius; j < _radius; j += _radius / 10) {
                if (i != 0 && j != 0) {
                    Point3D point = _position.add(new Vector(i, 0.1d, j));
                    if (point.equals(_position)){
                        vectors.add(p.subtract(point).normalized());
                    }
                    else{
                        try{
                            if (point.subtract(_position).dotProduct(point.subtract(_position)) <= _radius * _radius){
                                vectors.add(p.subtract(point).normalized());
                            }
                        }
                        catch (Exception e){
                            vectors.add(p.subtract(point).normalized());
                        }

                    }
                }

            }
        }
        vectors.add(getL(p));
        return vectors;
    }

    @Override
    public double getDistance(Point3D point) {
        return this._position.distance(point);

    }
}

