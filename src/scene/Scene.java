package scene;

import elements.AmbientLight;
import elements.LightSource;
import geometries.Geometries;
import primitives.Color;

import java.util.LinkedList;
import java.util.List;

/**
 * Class for implementation of picture's Scene
 *
 * @author Hillel & Yona
 */
public class Scene {

    /**
     * name of the scene
     */
    public String name;

    /**
     * background color of the scene
     */
    public Color background;

    /**
     * Ambient light of the scene
     */
    public AmbientLight ambientLight;

    /**
     * Geometries figures of the scene
     */
    public Geometries geometries;

    /**
     * Light sources of the scene
     */
    public List<LightSource> lights;

    private Scene(SceneBuilder builder) {
        this.name = builder.name;
        this.background = builder.background;
        this.ambientLight = builder.ambientLight;
        this.geometries = builder.geometries;
        this.lights = builder.lights;
    }


    public static class SceneBuilder {

        public String name;
        public Color background = Color.BLACK;
        public AmbientLight ambientLight = new AmbientLight(background, 0);
        public Geometries geometries = new Geometries();
        public List<LightSource> lights = new LinkedList<>();

        public SceneBuilder(String name) {
            this.name = name;
        }
        public SceneBuilder(Scene scene){
            this.name = scene.name;
            this.background = scene.background;
            this.ambientLight = scene.ambientLight;
            this.geometries = scene.geometries;
            this.lights = scene.lights;
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

        public SceneBuilder setLights(List<LightSource> Lights) {
            this.lights = Lights;
            return this;
        }

        public Scene build() {
            return new Scene(this);
        }
    }
}
