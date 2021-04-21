package scene;

import elements.AmbientLight;
import geometries.Geometries;
import geometries.Geometry;
import primitives.Color;

public class Scene {

    private String _name;
    private Color _background = Color.BLACK;
    private AmbientLight _ambientLight = new AmbientLight(Color.BLACK, 0);
    private Geometries _geometries = new Geometries();

    public Scene(String name) {

        _name = name;
    }

    public Scene setBackground(Color background) {
        _background = background;
        return this;
    }

    public Scene setAmbientLight(AmbientLight ambientLight) {
        _ambientLight = ambientLight;
        return this;
    }

    public Scene setGeometries(Geometries geometries) {
        _geometries = geometries;
        return this;
    }
}
