package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

public class PointLight extends Light implements LightSource {

    protected Point3D _position;
    protected double _kC, _kl, _kQ;

    public PointLight(Color c, Point3D pos, double kc, double kl, double kq) {
        super(c);
        _position = new Point3D(pos);
        _kC = kc;
        _kl = kl;
        _kQ = kq;
    }

    @Override
    public Color getIntensity(Point3D p) {
        double factor = _kC;
        double distance;
        try{
            distance = _position.distance(p);
            factor += _kl * distance + _kQ * distance * distance;
        }
        catch (Exception exception){ }

        return _intensity.scale(1/factor);
    }

    @Override
    public Vector getL(Point3D p) {
        try {
            return p.subtract(this._position).normalized();
        }
        catch (Exception exception){
            return null;
        }
    }
    @Override
    public double getDistance(Point3D point3D){
        return _position.distance(point3D);
    }
}

