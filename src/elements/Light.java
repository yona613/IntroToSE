package elements;

import primitives.Color;

abstract class Light {

    /**
     * Intensity of the light
     */
    protected Color _intensity;

    /**
     * Constructor
     * @param color
     */
    protected Light(Color color) {
        this._intensity = color;
    }

    /**
     * Get intensity color of the light
     * @return the intensity color
     */
    public Color getIntensity() {
        return _intensity;
    }
}
