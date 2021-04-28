package Utils;

import elements.AmbientLight;
import geometries.Geometries;
import geometries.Sphere;
import geometries.Triangle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import primitives.Color;
import primitives.Point3D;
import scene.Scene;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DalXml {

    // parse XML file
    private DocumentBuilder db;
    private final String _filePath;

    //The constructor will set the path of the file and create an instance of DocumentBuilder and too it
    // add to it precautions/security to our DocumentBuilder "db"
    public DalXml(String filePath) throws ParserConfigurationException {
        this._filePath = filePath;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        db = dbf.newDocumentBuilder();
    }

    public Scene getSceneFromXML() {
        try {
            Scene.SceneBuilder sceneBuilder = new Scene.SceneBuilder(_filePath);
            Document doc = db.parse(new File(_filePath + ".xml"));
            // optional, but recommended
            // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            //At this point we get the root of the xml file loaded in doc,but when we look at the file ,the value
            //of the background-color is in the attribute of the root so we get it by the getAttribute methods.


            //We studied together how the xml file is structured and we know that every time the background-color and
            //the ambient light will be the first data in the xml file and after we have all the data on the geometries
            //so we will get the first 2 value manually (attribute after attribute...)and for the geometries we set a switch
            //to get data on all the geometries possible writed in the xml file.

            var scene = doc.getDocumentElement();
            String color = scene.getAttribute("background-color");
            List<String> stringList = Arrays.stream(color.split("\\s")).collect(Collectors.toList());
            Color backgroundColor = new Color(
                    Double.valueOf(stringList.get(0)),
                    Double.valueOf(stringList.get(1)),
                    Double.valueOf(stringList.get(2))
            );
            sceneBuilder.setBackground(backgroundColor);

           // At this point we've got the background-color value present in the xml file and we added it to
            // our scene,now we have ot get the ambient light which is located just after the background color

            var list = scene.getElementsByTagName("ambient-light");
            var ambientLight = list.item(0);
            var element = (Element) ambientLight;
            color = ((Element) ambientLight).getAttribute("color");
            stringList = Arrays.stream(color.split("\\s")).collect(Collectors.toList());
            Color ambientLightColor = new Color(
                    Double.valueOf(stringList.get(0)),
                    Double.valueOf(stringList.get(1)),
                    Double.valueOf(stringList.get(2))
            );

            AmbientLight ambientLight1 = new AmbientLight(ambientLightColor, 1d);
            sceneBuilder.setAmbientLight(ambientLight1);

            //AmbientLight caught and loaded in our scene.Now we have to get all the geometries described in the xm file
            //so the loop will iterate on every data of the node called "geometries" and will add automatically the full object
            //to the variable "geos". Finally,we set the geos in our scene and return the scene.
            Geometries geos = new Geometries();

            list = scene.getElementsByTagName("geometries");

            var geometries = list.item(0).getChildNodes();

            for (int i = 0; i < geometries.getLength(); i++) {

                var node = geometries.item(i);
                if (node.hasAttributes()) {
                    String attribute = node.getNodeName();
                    var element1 = (Element) node;
                    switch (attribute) {
                        case "triangle":
                            String pointStr = element1.getAttribute("p0");
                            stringList = Arrays.stream(pointStr.split("\\s")).collect(Collectors.toList());
                            Point3D p0 = new Point3D(
                                    Double.valueOf(stringList.get(0)),
                                    Double.valueOf(stringList.get(1)),
                                    Double.valueOf(stringList.get(2))
                            );
                            pointStr = element1.getAttribute("p1");
                            stringList = Arrays.stream(pointStr.split("\\s")).collect(Collectors.toList());
                            Point3D p1 = new Point3D(
                                    Double.valueOf(stringList.get(0)),
                                    Double.valueOf(stringList.get(1)),
                                    Double.valueOf(stringList.get(2))
                            );
                            pointStr = element1.getAttribute("p2");
                            stringList = Arrays.stream(pointStr.split("\\s")).collect(Collectors.toList());
                            Point3D p2 = new Point3D(
                                    Double.valueOf(stringList.get(0)),
                                    Double.valueOf(stringList.get(1)),
                                    Double.valueOf(stringList.get(2))
                            );
                            Triangle triangle1 = new Triangle(p0, p1, p2);
                            geos.add(triangle1);
                            break;
                        case "sphere":
                            String center1 = element1.getAttribute("center");
                            stringList = Arrays.stream(center1.split("\\s")).collect(Collectors.toList());
                            Point3D point1 = new Point3D(
                                    Double.valueOf(stringList.get(0)),
                                    Double.valueOf(stringList.get(1)),
                                    Double.valueOf(stringList.get(2))
                            );
                            String radius1 = element1.getAttribute("radius");
                            Sphere mySphere1 = new Sphere(Double.valueOf(radius1), point1);
                            geos.add(mySphere1);
                            break;
                        default:
                            break;
                    }

                }
            }

            sceneBuilder.setGeometries(geos);
            return sceneBuilder.build();


        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
