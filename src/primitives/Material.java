package primitives;

public class Material {

    public double _kd = 0d;
    public double _ks = 0d;
    public double _nShininess = 1d;

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
