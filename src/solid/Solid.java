package solid;

import transforms.Mat4;
import transforms.Point3D;
import transforms.Vec3D;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Solid {

    protected List<Point3D> vertexBuffer = new ArrayList<>();
    protected List<Integer> indexBuffer = new ArrayList<>();
    protected List<Integer> triangleIndexBuffer = new ArrayList<>();
    protected List<Vec3D> normalBuffer = new ArrayList<>();
    protected List<Vec3D> uvBuffer = new ArrayList<>();

    protected Mat4 modelMatrix = new Mat4();
    protected Color color = Color.WHITE;
    protected BufferedImage texture = null;
    protected boolean useTexture = false;
    protected boolean useLighting = false;

    public List<Point3D> getVertexBuffer() {
        return vertexBuffer;
    }

    public List<Integer> getIndexBuffer() {
        return indexBuffer;
    }

    public List<Integer> getTriangleIndexBuffer() {
        return triangleIndexBuffer;
    }

    public List<Vec3D> getNormalBuffer() {
        return normalBuffer;
    }

    public List<Vec3D> getUvBuffer() {
        return uvBuffer;
    }

    public Mat4 getModelMatrix() {
        return modelMatrix;
    }

    public void setModelMatrix(Mat4 modelMatrix) {
        this.modelMatrix = modelMatrix;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public BufferedImage getTexture() {
        return texture;
    }

    public void setTexture(BufferedImage texture) {
        this.texture = texture;
    }

    public boolean isUseTexture() {
        return useTexture;
    }

    public void setUseTexture(boolean useTexture) {
        this.useTexture = useTexture;
    }

    public boolean isUseLighting() {
        return useLighting;
    }

    public void setUseLighting(boolean useLighting) {
        this.useLighting = useLighting;
    }

    protected void addIndices(Integer... indices) {
        indexBuffer.addAll(Arrays.asList(indices));
    }

    protected void addTriangleIndices(Integer... indices) {
        triangleIndexBuffer.addAll(Arrays.asList(indices));
    }
}