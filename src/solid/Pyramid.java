package solid;

import transforms.Point3D;
import java.awt.Color;

/**
 * Třída reprezentující pyramidu.
 */
public class Pyramid extends Solid {

    public Pyramid() {
        // Podstava
        vertexBuffer.add(new Point3D(-0.5, -0.5, -0.5)); //Vlevo vzadu
        vertexBuffer.add(new Point3D( 0.5, -0.5, -0.5)); //Vpravo vzadu
        vertexBuffer.add(new Point3D( 0.5, -0.5,  0.5)); //Vpravo vpředu
        vertexBuffer.add(new Point3D(-0.5, -0.5,  0.5)); //Vlevo vpředu

        // Špička
        vertexBuffer.add(new Point3D( 0, 0.5, 0));       //Vrchol nahoře

        // Hrany podstavy
        addIndices(0, 1, 1, 2, 2, 3, 3, 0);

        // Hrany ke špičce
        addIndices(0, 4, 1, 4, 2, 4, 3, 4);

        this.color = Color.YELLOW;
    }
}