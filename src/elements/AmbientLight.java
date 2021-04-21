package elements;

import primitives.Color;

public class AmbientLight {

     private Color _Intensity;
    public AmbientLight(Color color,double ka)
    {
       _Intensity=color.scale(ka);


    }
    public Color getIntensity()
    {
        return _Intensity;
    }


}
