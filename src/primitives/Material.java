package primitives;

public class Material {
    private double _kd = 0d;
    private double _ks = 0d;
    private int _nShininess = 1;
    private Material material;

    public Material getMaterial() {
        return this.material;
    }

    public Material setKd(double kd) {
        _kd = kd;
        return this;
    }

    public Material setKs(double ks) {
        _ks = ks;
        return this;
    }

    public Material setnShininess(int nShininess) {
        _nShininess = nShininess;
        return this;
    }
}
