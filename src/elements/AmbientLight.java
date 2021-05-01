package elements;

import primitives.Color;

/**
 * Class to implement ambient light of the scene
 */
public class AmbientLight extends Light {

    /**
     * Intensity of ambient light
     */
    public AmbientLight(Color color, double ka) {
        // 𝑰𝑷=𝒌𝑨∙𝑰𝑨
        super(color.scale(ka));
    }

}
