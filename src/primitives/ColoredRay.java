package primitives;

public class ColoredRay {
    private Ray ray;
    private Color color;

    public ColoredRay(Ray ray, Color color) {
        this.ray = ray;
        this.color = color;
    }

    public Ray getRay() {
        return ray;
    }

    public Color getColor() {
        return color;
    }
}
