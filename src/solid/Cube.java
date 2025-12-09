package solid;

import transforms.Point3D;
import java.awt.Color;

/**
 * Třída reprezentující krychli (drátový model).
 * Skupina 1 dle zadání.
 */
public class Cube extends Solid {

    public Cube() {
        // Krychle o velikosti 1x1x1 kolem středu [0,0,0]
        vertexBuffer.add(new Point3D(-0.5, -0.5, -0.5)); //Vlevo dole vzadu
        vertexBuffer.add(new Point3D( 0.5, -0.5, -0.5)); //Vpravo dole vzadu
        vertexBuffer.add(new Point3D( 0.5,  0.5, -0.5)); //Vpravo nahoře vzadu
        vertexBuffer.add(new Point3D(-0.5,  0.5, -0.5)); //Vlevo nahoře vzadu
        vertexBuffer.add(new Point3D(-0.5, -0.5,  0.5)); //Vlevo dole vpředu
        vertexBuffer.add(new Point3D( 0.5, -0.5,  0.5)); //Vpravo dole vpředu
        vertexBuffer.add(new Point3D( 0.5,  0.5,  0.5)); //Vpravo nahoře vpředu
        vertexBuffer.add(new Point3D(-0.5,  0.5,  0.5)); //Vlevo nahoře vpředu


        // Zadní stěna
        addIndices(0, 1, 1, 2, 2, 3, 3, 0);

        // Přední stěna
        addIndices(4, 5, 5, 6, 6, 7, 7, 4);

        // Propojení přední a zadní stěny
        addIndices(0, 4, 1, 5, 2, 6, 3, 7);

        this.color = Color.MAGENTA;
    }
}