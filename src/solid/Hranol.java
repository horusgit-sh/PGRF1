package solid;

import transforms.Point3D;

import java.awt.*;

public class Hranol extends Solid{
    public Hranol(){


            // ===== Spodní osmiúhelník (y = -0.5) =====
            vertexBuffer.add(new Point3D( 0.5, -0.5,  0.0)); // 0
            vertexBuffer.add(new Point3D( 0.35, -0.5,  0.35)); // 1
            vertexBuffer.add(new Point3D( 0.0, -0.5,  0.5)); // 2
            vertexBuffer.add(new Point3D(-0.35, -0.5,  0.35)); // 3
            vertexBuffer.add(new Point3D(-0.5, -0.5,  0.0)); // 4
            vertexBuffer.add(new Point3D(-0.35, -0.5, -0.35)); // 5
            vertexBuffer.add(new Point3D( 0.0, -0.5, -0.5)); // 6
            vertexBuffer.add(new Point3D( 0.35, -0.5, -0.35)); // 7

            // ===== Horní osmiúhelník (y = +0.5) =====
            vertexBuffer.add(new Point3D( 0.5,  0.5,  0.0)); // 8
            vertexBuffer.add(new Point3D( 0.35, 0.5,  0.35)); // 9
            vertexBuffer.add(new Point3D( 0.0,  0.5,  0.5)); // 10
            vertexBuffer.add(new Point3D(-0.35, 0.5,  0.35)); // 11
            vertexBuffer.add(new Point3D(-0.5,  0.5,  0.0)); // 12
            vertexBuffer.add(new Point3D(-0.35, 0.5, -0.35)); // 13
            vertexBuffer.add(new Point3D( 0.0,  0.5, -0.5)); // 14
            vertexBuffer.add(new Point3D( 0.35, 0.5, -0.35)); // 15



        addIndices(
                0,1,
                1,2,
                2,3,
                3,4,
                4,5,
                5,6,
                6,7,
                7,0
        );
        addIndices(
                8,9,
                9,10,
                10,11,
                11,12,
                12,13,
                13,14,
                14,15,
                15,8
        );

        addIndices(
                0,8,
                1,9,
                2,10,
                3,11,
                4,12,
                5,13,
                6,14,
                7,15
        );

        this.color = Color.ORANGE;



    }

}
