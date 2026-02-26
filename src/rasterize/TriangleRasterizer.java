package rasterize;

import java.awt.Color;
import java.awt.image.BufferedImage;
import transforms.Vec3D;

public class TriangleRasterizer {
    private final Raster raster;
    private final ZBuffer zBuffer;
    private Vec3D lightPos = new Vec3D(5, 5, 5);
    private Color lightColor = Color.WHITE;
    private double ambientStrength = 0.2;
    private double diffuseStrength = 0.8;

    public TriangleRasterizer(Raster raster, ZBuffer zBuffer) {
        this.raster = raster;
        this.zBuffer = zBuffer;
    }

    public void setLightPos(Vec3D lightPos) {
        this.lightPos = lightPos;
    }

    public void setLightColor(Color lightColor) {
        this.lightColor = lightColor;
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

    public void rasterize(int x1, int y1, double z1, double u1, double v1, Vec3D n1,
                          int x2, int y2, double z2, double u2, double v2, Vec3D n2,
                          int x3, int y3, double z3, double u3, double v3, Vec3D n3,
                          Color color, BufferedImage texture, boolean useLighting) {
        int minX = Math.max(0, Math.min(x1, Math.min(x2, x3)));
        int maxX = Math.min(raster.getSirska() - 1, Math.max(x1, Math.max(x2, x3)));
        int minY = Math.max(0, Math.min(y1, Math.min(y2, y3)));
        int maxY = Math.min(raster.getVyska() - 1, Math.max(y1, Math.max(y2, y3)));

        double area = edge(x1, y1, x2, y2, x3, y3);
        if (area == 0) {
            return;
        }

        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                double px = x + 0.5;
                double py = y + 0.5;

                double w1 = edge(x2, y2, x3, y3, px, py) / area;
                double w2 = edge(x3, y3, x1, y1, px, py) / area;
                double w3 = 1.0 - w1 - w2;

                if ((w1 >= 0 && w2 >= 0 && w3 >= 0) || (w1 <= 0 && w2 <= 0 && w3 <= 0)) {
                    double z = w1 * z1 + w2 * z2 + w3 * z3;
                    if (z < 0 || z > 1) {
                        continue;
                    }
                    if (z < zBuffer.getDepth(x, y)) {
                        zBuffer.setDepth(x, y, z);

                        int baseColor;
                        if (texture != null) {
                            double u = w1 * u1 + w2 * u2 + w3 * u3;
                            double v = w1 * v1 + w2 * v2 + w3 * v3;
                            int tx = (int) (u * texture.getWidth()) % texture.getWidth();
                            int ty = (int) (v * texture.getHeight()) % texture.getHeight();
                            if (tx < 0) tx += texture.getWidth();
                            if (ty < 0) ty += texture.getHeight();
                            baseColor = texture.getRGB(tx, ty);
                        } else {
                            baseColor = color.getRGB();
                        }

                        int finalColor = baseColor;
                        if (useLighting && n1 != null && n2 != null && n3 != null) {
                            Vec3D normal = new Vec3D(
                                w1 * n1.x + w2 * n2.x + w3 * n3.x,
                                w1 * n1.y + w2 * n2.y + w3 * n3.y,
                                w1 * n1.z + w2 * n2.z + w3 * n3.z
                            ).normalized();

                            double ambient = ambientStrength;
                            double diff = Math.max(0, normal.dot(lightPos.normalized()));
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
    }

    private static double edge(double ax, double ay, double bx, double by, double px, double py) {
        return (px - ax) * (by - ay) - (py - ay) * (bx - ax);
    }
}
