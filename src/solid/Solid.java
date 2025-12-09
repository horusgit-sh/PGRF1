package solid;

import transforms.Mat4;
import transforms.Point3D;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Abstraktní třída reprezentující 3D těleso.
 * Obsahuje Vertex Buffer (vrcholy), Index Buffer (hrany) a transformační matici.
 */
public abstract class Solid {

    // Vertex Buffer: Seznam vrcholů
    protected List<Point3D> vertexBuffer = new ArrayList<>();

    // Index Buffer: Seznam indexů
    protected List<Integer> indexBuffer = new ArrayList<>();

    protected Mat4 modelMatrix = new Mat4();

    protected Color color = Color.WHITE; // Barva

    public List<Point3D> getVertexBuffer() {
        return vertexBuffer;
    }

    public List<Integer> getIndexBuffer() {
        return indexBuffer;
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

    //Pomocná metoda pro přidání indexů.
    protected void addIndices(Integer... indices) {
        indexBuffer.addAll(Arrays.asList(indices));
    }
}