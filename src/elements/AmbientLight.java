package elements;

import primitives.Color;

/**
 * Class to implement ambient light of the scene
 */
public class AmbientLight {

    /**
     * Intensity of ambient light
     */
    private Color _Intensity;

    public AmbientLight(Color color, double ka) {
        // 𝑰𝑷=𝒌𝑨∙𝑰𝑨
        _Intensity = color.scale(ka);
    }

    public Color getIntensity() {
        return _Intensity;
    }
}
