package solid;

import transforms.Point3D;
import java.awt.Color;

//Třída reprezentující pyramidu.
public class Pyramid extends Solid {

    public Pyramid() {
        vertexBuffer.add(new Point3D(-0.5, -0.5, -0.5)); //Vlevo vzadu
        vertexBuffer.add(new Point3D( 0.5, -0.5, -0.5)); //Vpravo vzadu
        vertexBuffer.add(new Point3D( 0.5, -0.5,  0.5)); //Vpravo vpředu
        vertexBuffer.add(new Point3D(-0.5, -0.5,  0.5)); //Vlevo vpředu
        vertexBuffer.add(new Point3D( 0, 0.5, 0));       //Vrchol nahoře

        addIndices(0, 1, 1, 2, 2, 3, 3, 0);

        addIndices(0, 4, 1, 4, 2, 4, 3, 4);

        this.color = Color.YELLOW;
    }
}