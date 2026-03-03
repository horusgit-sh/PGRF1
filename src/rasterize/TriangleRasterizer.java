
package rasterize;

import java.awt.Color;
import java.awt.image.BufferedImage;

import transforms.Vec3D;

public class TriangleRasterizer {
    private final Raster raster;
    private final ZBuffer zBuffer;
    private Vec3D lightPos = new Vec3D(5, 5, 5);
    private double ambientStrength = 0.2;
    private double diffuseStrength = 0.8;

    public TriangleRasterizer(Raster raster, ZBuffer zBuffer) {
        this.raster = raster;
        this.zBuffer = zBuffer;
    }

    public void setLightPos(Vec3D lightPos) {
        this.lightPos = lightPos;
    }


    public void rasterize(int x1, int y1, double z1,
                          int x2, int y2, double z2,
                          int x3, int y3, double z3,
                          Color color) {
        rasterize(x1, y1, z1, 0, 0, null,
                  x2, y2, z2, 0, 0, null,
                  x3, y3, z3, 0, 0, null,
                  color, null, false);
    }

    public void rasterize(int x1, int y1, double z1, double u1, double v1,
                          int x2, int y2, double z2, double u2, double v2,
                          int x3, int y3, double z3, double u3, double v3,
                          Color color, BufferedImage texture) {
        rasterize(x1, y1, z1, u1, v1, null,
                  x2, y2, z2, u2, v2, null,
                  x3, y3, z3, u3, v3, null,
                  color, texture, false);
    }

    /**
     * Scanline triangle fill with:
     * - ZBuffer test/write
     * - Texture sampling (if texture != null)
     * - Simple Lambert lighting (ambient + diffuse) using interpolated normals
     *
     * NOTE: UV/normal interpolation is linear in screen space (not perspective-correct).
     */
    public void rasterize(int x1, int y1, double z1, double u1, double v1, Vec3D n1,
                          int x2, int y2, double z2, double u2, double v2, Vec3D n2,
                          int x3, int y3, double z3, double u3, double v3, Vec3D n3,
                          Color color, BufferedImage texture, boolean useLighting) {

        // Sort vertices by Y ascending (swap all attributes together)
        if (y2 < y1) {
            int tx = x1; x1 = x2; x2 = tx;
            int ty = y1; y1 = y2; y2 = ty;
            double tz = z1; z1 = z2; z2 = tz;
            double tu = u1; u1 = u2; u2 = tu;
            double tv = v1; v1 = v2; v2 = tv;
            Vec3D tn = n1; n1 = n2; n2 = tn;
        }
        if (y3 < y1) {
            int tx = x1; x1 = x3; x3 = tx;
            int ty = y1; y1 = y3; y3 = ty;
            double tz = z1; z1 = z3; z3 = tz;
            double tu = u1; u1 = u3; u3 = tu;
            double tv = v1; v1 = v3; v3 = tv;
            Vec3D tn = n1; n1 = n3; n3 = tn;
        }
        if (y3 < y2) {
            int tx = x2; x2 = x3; x3 = tx;
            int ty = y2; y2 = y3; y3 = ty;
            double tz = z2; z2 = z3; z3 = tz;
            double tu = u2; u2 = u3; u3 = tu;
            double tv = v2; v2 = v3; v3 = tv;
            Vec3D tn = n2; n2 = n3; n3 = tn;
        }

        // Degenerate triangle (all on one scanline)
        if (y1 == y3) return;

        int height = raster.getVyska();
        int width = raster.getSirska();

        // Scanline loop
        for (int y = y1; y <= y3; y++) {

            if (y < 0 || y >= height) continue;

            boolean secondHalf = (y > y2) || (y2 == y1);
            int segmentHeight = secondHalf ? (y3 - y2) : (y2 - y1);
            if (segmentHeight == 0) continue;

            double alpha = (double) (y - y1) / (double) (y3 - y1);
            double beta  = (double) (y - (secondHalf ? y2 : y1)) / (double) segmentHeight;

            // Point A on long edge (v1 -> v3)
            double axd = x1 + (x3 - x1) * alpha;
            double azd = z1 + (z3 - z1) * alpha;
            double aud = u1 + (u3 - u1) * alpha;
            double avd = v1 + (v3 - v1) * alpha;

            Vec3D and = null;
            if (n1 != null && n3 != null) {
                and = new Vec3D(
                        n1.x + (n3.x - n1.x) * alpha,
                        n1.y + (n3.y - n1.y) * alpha,
                        n1.z + (n3.z - n1.z) * alpha
                );
            }

            // Point B on short edge (v1 -> v2) or (v2 -> v3)
            double bxd, bzd, bud, bvd;
            Vec3D bnd = null;

            if (!secondHalf) {
                bxd = x1 + (x2 - x1) * beta;
                bzd = z1 + (z2 - z1) * beta;
                bud = u1 + (u2 - u1) * beta;
                bvd = v1 + (v2 - v1) * beta;

                if (n1 != null && n2 != null) {
                    bnd = new Vec3D(
                            n1.x + (n2.x - n1.x) * beta,
                            n1.y + (n2.y - n1.y) * beta,
                            n1.z + (n2.z - n1.z) * beta
                    );
                }
            } else {
                bxd = x2 + (x3 - x2) * beta;
                bzd = z2 + (z3 - z2) * beta;
                bud = u2 + (u3 - u2) * beta;
                bvd = v2 + (v3 - v2) * beta;

                if (n2 != null && n3 != null) {
                    bnd = new Vec3D(
                            n2.x + (n3.x - n2.x) * beta,
                            n2.y + (n3.y - n2.y) * beta,
                            n2.z + (n3.z - n2.z) * beta
                    );
                }
            }

            // Ensure left-to-right order
            if (axd > bxd) {
                double tmp;
                tmp = axd; axd = bxd; bxd = tmp;
                tmp = azd; azd = bzd; bzd = tmp;
                tmp = aud; aud = bud; bud = tmp;
                tmp = avd; avd = bvd; bvd = tmp;

                Vec3D tn = and; and = bnd; bnd = tn;
            }

            int ax = (int) Math.ceil(axd);
            int bx = (int) Math.floor(bxd);

            if (bx < 0 || ax >= width) continue;
            ax = Math.max(ax, 0);
            bx = Math.min(bx, width - 1);

            double dx = (bxd - axd);
            if (dx == 0) dx = 1.0;

            for (int x = ax; x <= bx; x++) {

                double phi = (x - axd) / dx;

                double z = azd + (bzd - azd) * phi;
                if (z < 0 || z > 1) continue;

                if (z >= zBuffer.getDepth(x, y)) continue;
                zBuffer.setDepth(x, y, z);

                // Base color (texture or solid)
                int baseColor;
                if (texture != null) {
                    int tw = texture.getWidth();
                    int th = texture.getHeight();

                    double u = aud + (bud - aud) * phi;
                    double v = avd + (bvd - avd) * phi;

                    int tx = (int) (u * tw) % tw;
                    int ty = (int) (v * th) % th;
                    if (tx < 0) tx += tw;
                    if (ty < 0) ty += th;

                    baseColor = texture.getRGB(tx, ty);
                } else {
                    baseColor = color.getRGB();
                }

                int finalColor = baseColor;

                // Lighting (ambient + diffuse) with interpolated normal
                if (useLighting && and != null && bnd != null) {
                    Vec3D normal = new Vec3D(
                            and.x + (bnd.x - and.x) * phi,
                            and.y + (bnd.y - and.y) * phi,
                            and.z + (bnd.z - and.z) * phi
                    ).normalized();

                    double ambient = ambientStrength;

                    // Simple directional light from lightPos
                    Vec3D lightDir = lightPos.normalized();
                    double diff = Math.max(0, normal.dot(lightDir));
                    double diffuse = diffuseStrength * diff;

                    double lighting = Math.min(1.0, ambient + diffuse);

                    Color base = new Color(baseColor);
                    int r = (int) (base.getRed() * lighting);
                    int g = (int) (base.getGreen() * lighting);
                    int b = (int) (base.getBlue() * lighting);

                    finalColor = new Color(
                            Math.min(255, r),
                            Math.min(255, g),
                            Math.min(255, b)
                    ).getRGB();
                }

                raster.setPixel(x, y, finalColor);
            }
        }
    }

}