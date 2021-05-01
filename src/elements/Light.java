package elements;

import primitives.Color;

abstract class Light {
    protected Color _intensity;
    protected Light(Color color)
    {
        _intensity=color;
    }

    public Color getIntensity() {
        return _intensity;
    }
}
