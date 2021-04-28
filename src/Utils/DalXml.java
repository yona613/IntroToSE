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

            var scene = doc.getDocumentElement();
            String color = scene.getAttribute("background-color");
            List<String> stringList = Arrays.stream(color.split("\\s")).collect(Collectors.toList());
            Color backgroundColor = new Color(
                    Double.valueOf(stringList.get(0)),
                    Double.valueOf(stringList.get(1)),
                    Double.valueOf(stringList.get(2))
            );
            sceneBuilder.setBackground(backgroundColor);

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
