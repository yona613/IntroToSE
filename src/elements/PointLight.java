package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

public class PointLight extends Light implements LightSource{

    private Point3D _position;
    private double _kC,_kl,_kQ;

    public PointLight(Color c, Point3D pos, double kc, double kl, double kq) {
        super(c);
        _position = new Point3D(pos);
        _kC = kc;
        _kl = kl;
        _kQ = kq;
    }



    @Override
    public Color getIntensity(Point3D p) {
        return _intensity;
    }

    @Override
    public Vector getL(Point3D p) {
        return null;
    }
}

