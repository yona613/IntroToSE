package elements;

import primitives.Color;

/**
 * Class to implement ambient light of the scene
 */
public class AmbientLight extends Light {

    /**
     * Intensity of ambient light
     * The ambient light's color is the color scaled by the ka factor
     *
     * @param color color at start
     * @param ka attenuation factor
     */
    public AmbientLight(Color color, double ka) {
        super(color.scale(ka));
    }

}
