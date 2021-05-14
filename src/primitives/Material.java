package primitives;

public class Material {

    public double _kd = 0d;
    public double _ks = 0d;
    public double _nShininess = 1d;

    public double kT = 0d;
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
