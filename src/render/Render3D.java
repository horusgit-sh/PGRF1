package render;

import rasterize.LineRasterizer;
import rasterize.Raster;
import solid.Solid;
import transforms.Mat4;
import transforms.Point3D;
import java.util.List;

/**
 * Třída odpovědná za vykreslování 3D scény.
 * Provádí transformace vrcholů,
 * ořezání, dehomogenizaci a mapování do okna.
 */
public class Render3D {
    private final LineRasterizer lineRasterizer;
    private final Raster raster;

    // Transformacni matice
    private Mat4 viewMatrix;
    private Mat4 projMatrix;

    public Render3D(Raster raster, LineRasterizer lineRasterizer) {
        this.raster = raster;
        this.lineRasterizer = lineRasterizer;
        this.viewMatrix = new Mat4();
        this.projMatrix = new Mat4();
    }

    public void setViewMatrix(Mat4 viewMatrix) {
        this.viewMatrix = viewMatrix;
    }

    public void setProjMatrix(Mat4 projMatrix) {
        this.projMatrix = projMatrix;
    }

    /**
     * Hlavní metoda pro vykreslení seznamu těles.
     * @param scene Seznam objektů
     */
    public void render(List<Solid> scene) {
        for (Solid solid : scene) {
            // P*V*M
            Mat4 transformation = projMatrix
                    .mul(viewMatrix)
                    .mul(solid.getModelMatrix());

            List<Point3D> vb = solid.getVertexBuffer();
            List<Integer> ib = solid.getIndexBuffer();

            for (int i = 0; i < ib.size(); i += 2) {
                int index1 = ib.get(i);
                int index2 = ib.get(i + 1);

                Point3D p1 = vb.get(index1);
                Point3D p2 = vb.get(index2);

                // Transformace
                Point3D t1 = transformation.multiply(p1);
                Point3D t2 = transformation.multiply(p2);

                // Clipping
                if (t1.w < 0.1 || t2.w < 0.1) {
                    continue;
                }

                // Dehomogenizace
                double x1 = t1.x / t1.w;
                double y1 = t1.y / t1.w;
                double x2 = t2.x / t2.w;
                double y2 = t2.y / t2.w;

                // Viewport transformace
                int width = raster.getSirska();
                int height = raster.getVyska();

                int u1 = (int) ((x1 + 1) * 0.5 * width);
                int v1 = (int) ((1 - y1) * 0.5 * height);
                int u2 = (int) ((x2 + 1) * 0.5 * width);
                int v2 = (int) ((1 - y2) * 0.5 * height);

                // Rasterizace
                lineRasterizer.rasterize(u1, v1, u2, v2, solid.getColor());
            }
        }
    }
}