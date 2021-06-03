package primitives;

/**
 * Class to implement the material of the objects
 */
public class Material {

    /**
     * Diffusion ratio
     */
    public double _kd = 0d;

    /**
     * Specular ratio
     */
    public double _ks = 0d;

    /**
     * Shininess of the object ratio
     */
    public double _nShininess = 1d;

    /**
     * Transparency ratio
     */
    public double kT = 0d;

    /**
     * Reflection ratio ratio
     */
    public double kR = 0d;

    public Material setkT(double kT) {
        this.kT = kT;
        return this;
    }

    public Material setkR(double kR) {
        this.kR = kR;
        return this;
    }

    public Material setKd(double kd) {
        _kd = kd;
        return this;
    }

    public Material setKs(double ks) {
        _ks = ks;
        return this;
    }

    public Material setnShininess(double nShininess) {
        _nShininess = nShininess;
        return this;
    }
}
