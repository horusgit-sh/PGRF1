package render;

import rasterize.LineRasterizer;
import rasterize.Raster;
import rasterize.TriangleRasterizer;
import solid.Solid;
import transforms.Mat4;
import transforms.Point3D;
import transforms.Vec3D;
import java.awt.image.BufferedImage;
import java.util.List;

public class Render3D {
    private final LineRasterizer lineRasterizer;
    private final Raster raster;
    private final TriangleRasterizer triangleRasterizer;

    private boolean drawFilled = true;
    private boolean drawWireframe = true;

    private Mat4 viewMatrix;
    private Mat4 projMatrix;

    public Render3D(Raster raster, LineRasterizer lineRasterizer, TriangleRasterizer triangleRasterizer) {
        this.raster = raster;
        this.lineRasterizer = lineRasterizer;
        this.triangleRasterizer = triangleRasterizer;
        this.viewMatrix = new Mat4();
        this.projMatrix = new Mat4();
    }

    public void setViewMatrix(Mat4 viewMatrix) {
        this.viewMatrix = viewMatrix;
    }

    public void setProjMatrix(Mat4 projMatrix) {
        this.projMatrix = projMatrix;
    }

    public void setDrawFilled(boolean drawFilled) {
        this.drawFilled = drawFilled;
    }

    public void setDrawWireframe(boolean drawWireframe) {
        this.drawWireframe = drawWireframe;
    }

    public void render(List<Solid> scene) {
        int width = raster.getSirska();
        int height = raster.getVyska();

        for (Solid solid : scene) {
            // P*V*M
            Mat4 transformation = projMatrix
                    .mul(viewMatrix)
                    .mul(solid.getModelMatrix());

            List<Point3D> vb = solid.getVertexBuffer();
            List<Vec3D> uvb = solid.getUvBuffer();
            List<Vec3D> nb = solid.getNormalBuffer();
            boolean hasUV = uvb.size() == vb.size();
            boolean hasNormals = nb.size() == vb.size();

            // Vyplněné trojúhelníky
            if (drawFilled) {
                List<Integer> tb = solid.getTriangleIndexBuffer();
                BufferedImage tex = solid.isUseTexture() ? solid.getTexture() : null;
                boolean useLighting = solid.isUseLighting();

                for (int i = 0; i + 2 < tb.size(); i += 3) {
                    int i1 = tb.get(i);
                    int i2 = tb.get(i + 1);
                    int i3 = tb.get(i + 2);

                    Point3D p1 = vb.get(i1);
                    Point3D p2 = vb.get(i2);
                    Point3D p3 = vb.get(i3);

                    Point3D t1 = transformation.multiply(p1);
                    Point3D t2 = transformation.multiply(p2);
                    Point3D t3 = transformation.multiply(p3);

                    if (t1.w <= 0.1 || t2.w <= 0.1 || t3.w <= 0.1) {
                        continue;
                    }

                    double x1 = t1.x / t1.w;
                    double y1 = t1.y / t1.w;
                    double z1 = t1.z / t1.w;
                    double x2 = t2.x / t2.w;
                    double y2 = t2.y / t2.w;
                    double z2 = t2.z / t2.w;
                    double x3 = t3.x / t3.w;
                    double y3 = t3.y / t3.w;
                    double z3 = t3.z / t3.w;

                    int u1 = (int) ((x1 + 1) * 0.5 * width);
                    int v1 = (int) ((1 - y1) * 0.5 * height);
                    int u2 = (int) ((x2 + 1) * 0.5 * width);
                    int v2 = (int) ((1 - y2) * 0.5 * height);
                    int u3 = (int) ((x3 + 1) * 0.5 * width);
                    int v3 = (int) ((1 - y3) * 0.5 * height);

                    double dz1 = (z1 + 1) * 0.5;
                    double dz2 = (z2 + 1) * 0.5;
                    double dz3 = (z3 + 1) * 0.5;

                    if (hasUV && hasNormals && useLighting) {
                        Vec3D uv1 = uvb.get(i1);
                        Vec3D uv2 = uvb.get(i2);
                        Vec3D uv3 = uvb.get(i3);
                        Vec3D n1 = nb.get(i1);
                        Vec3D n2 = nb.get(i2);
                        Vec3D n3 = nb.get(i3);
                        triangleRasterizer.rasterize(
                            u1, v1, dz1, uv1.x, uv1.y, n1,
                            u2, v2, dz2, uv2.x, uv2.y, n2,
                            u3, v3, dz3, uv3.x, uv3.y, n3,
                            solid.getColor(), tex, true
                        );
                    } else if (hasUV && tex != null) {
                        Vec3D uv1 = uvb.get(i1);
                        Vec3D uv2 = uvb.get(i2);
                        Vec3D uv3 = uvb.get(i3);
                        triangleRasterizer.rasterize(
                            u1, v1, dz1, uv1.x, uv1.y,
                            u2, v2, dz2, uv2.x, uv2.y,
                            u3, v3, dz3, uv3.x, uv3.y,
                            solid.getColor(), tex
                        );
                    } else {
                        triangleRasterizer.rasterize(u1, v1, dz1, u2, v2, dz2, u3, v3, dz3, solid.getColor());
                    }
                }
            }

            // Drátový model (hrany)
            if (drawWireframe) {
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
}

