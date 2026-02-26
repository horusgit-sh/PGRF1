package solid;

import transforms.Point3D;
import transforms.Vec3D;
import java.awt.Color;

public class Sphere extends Solid {

    public Sphere(int segments) {
        int rings = segments / 2;

        for (int ring = 0; ring <= rings; ring++) {
            double phi = Math.PI * ring / rings;
            double y = Math.cos(phi);
            double r = Math.sin(phi);

            for (int seg = 0; seg < segments; seg++) {
                double theta = 2 * Math.PI * seg / segments;
                double x = r * Math.cos(theta);
                double z = r * Math.sin(theta);

                vertexBuffer.add(new Point3D(x * 0.3, y * 0.3, z * 0.3));
                normalBuffer.add(new Vec3D(x, y, z).normalized());
                uvBuffer.add(new Vec3D((double) seg / segments, (double) ring / rings, 0));
            }
        }

        // Indexy trojúhelníků
        for (int ring = 0; ring < rings; ring++) {
            for (int seg = 0; seg < segments; seg++) {
                int curr = ring * segments + seg;
                int next = ring * segments + (seg + 1) % segments;
                int currNext = (ring + 1) * segments + seg;
                int nextNext = (ring + 1) * segments + (seg + 1) % segments;

                addTriangleIndices(curr, next, currNext);
                addTriangleIndices(next, nextNext, currNext);
            }
        }

        // Hrany pro wireframe
        for (int ring = 0; ring < rings; ring++) {
            for (int seg = 0; seg < segments; seg++) {
                int curr = ring * segments + seg;
                int next = ring * segments + (seg + 1) % segments;
                int down = (ring + 1) * segments + seg;

                addIndices(curr, next);
                addIndices(curr, down);
            }
        }

        this.color = Color.WHITE;
    }
}

