package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

import java.util.List;

/**
 * Class implementing a direction oriented light source
 */
public class DirectionalLight extends Light implements LightSource{

    private Vector _direction;

    /**
     * Initialize directional light with it's intensity and direction, direction
     * vector will be normalized.
     *
     * @param c   intensity of the light
     * @param dir direction vector
     */
    public DirectionalLight(Color c, Vector dir) {
        super(c);
        _direction = dir.normalized();
    }

    @Override
    public Color getIntensity(Point3D p) {
        return _intensity;
    }

    @Override
    public Vector getL(Point3D p) {
        return _direction;
    }

    @Override
    public List<Vector> getListL(Point3D p) {
        return List.of(getL(p));
    }

    @Override
    public double getDistance(Point3D point) {
        return Double.POSITIVE_INFINITY;
    }
}
