package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Util;
import primitives.Vector;

public class SpotLight extends PointLight implements LightSource {

    protected Vector _dir;

    public SpotLight(Color c, Point3D pos, double kc, double kl, double kq, Vector direction) {
        super(c, pos, kc, kl, kq);
        this._dir = direction;
    }

    @Override
    public Color getIntensity(Point3D p) {
        double projection = _dir.dotProduct(getL(p));

        if (Util.isZero(projection)) {
            return Color.BLACK;
        }
        double factor = Math.max(0, projection);
        Color pointlightIntensity = super.getIntensity(p);


        return (pointlightIntensity.scale(factor));
    }

    @Override
    public Vector getL(Point3D p) {
        return super.getL(p).normalized();
    }
}
