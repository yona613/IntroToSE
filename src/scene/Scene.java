package scene;

import elements.AmbientLight;
import geometries.Geometries;
import primitives.Color;

/**
 * Class for implementation of picture's Scene
 *
 * @author Hillel & Yona
 */
public class Scene {

    public String name;
    public Color background;
    public AmbientLight ambientLight;
    public Geometries geometries;

    private Scene(SceneBuilder builder){
        this.name = builder.name;
        this. background = builder.background;
        this.ambientLight = builder.ambientLight;
        this.geometries = builder.geometries;
    }


    public static class SceneBuilder{

        public String name;
        public Color background = Color.BLACK;
        public AmbientLight ambientLight = new AmbientLight(background, 0);
        public Geometries geometries = new Geometries();

        public SceneBuilder(String name) {
            this.name = name;
        }

        public SceneBuilder setBackground(Color background) {
            this.background = background;
            return this;
        }

        public SceneBuilder setAmbientLight(AmbientLight ambientLight) {
            this.ambientLight = ambientLight;
            return this;
        }

        public SceneBuilder setGeometries(Geometries geometries) {
            this.geometries = geometries;
            return this;
        }

        public Scene build(){
            return new Scene(this);
        }
    }
}
