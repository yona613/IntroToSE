package elements;

import primitives.Color;

abstract class Light {

    /**
     * Intensity of the light
     */
    protected Color _intensity;

    protected Light(Color color) {
        this._intensity = color;
    }

    public Color getIntensity() {
        return _intensity;
    }
}
