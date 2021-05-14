package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Util;
import primitives.Vector;

/**
 * Class to implement a spot light source of the scene
 */
public class SpotLight extends PointLight implements LightSource {

    /**
     * Direction of the light
     */
    protected Vector _dir;

    public SpotLight(Color c, Point3D pos, Vector direction) {
        super(c, pos);
        this._dir = direction.normalized();
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
    public double getDistance(Point3D point){
        return getDistance(point);
    }

}
