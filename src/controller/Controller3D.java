package controller;

import rasterize.FilledLineRasterizer;
import rasterize.LineRasterizer;
import rasterize.Raster;
import rasterize.TriangleRasterizer;
import rasterize.ZBuffer;
import render.Render3D;
import solid.*;
import transforms.*;
import view.Panel;

import java.awt.Color;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Controller3D {

    private final Panel panel;
    private final Raster raster;
    private final ZBuffer zBuffer;

    private Render3D render3D;
    private Camera camera;

    private Mat4 perspProj;
    private Mat4 orthoProj;
    private boolean usePerspective = true;
    private boolean drawFilled = true;
    private boolean drawWireframe = true;

    private int activeIndex = -1;
    private int lightSourceIndex = -1;

    private final List<Solid> scene = new ArrayList<>();

    private int startX, startY;
    private boolean mouseLeftDown = false;



    public Controller3D(Panel panel) {
        this.panel = panel;
        this.raster = panel.getRaster();
        this.zBuffer = new ZBuffer(raster.getSirska(), raster.getVyska());

        initObjects();
        initListeners();

        panel.setFocusTraversalKeysEnabled(false);
        display();
    }

    private void initObjects() {
        LineRasterizer lineRasterizer = new FilledLineRasterizer(panel.getRaster());
        TriangleRasterizer triangleRasterizer = new TriangleRasterizer(raster, zBuffer);
        render3D = new Render3D(raster, lineRasterizer, triangleRasterizer);
        render3D.setDrawFilled(drawFilled);
        render3D.setDrawWireframe(drawWireframe);

        camera = new Camera()
                .withPosition(new Vec3D(6, 4, 6))
                .withAzimuth(Math.toRadians(225))
                .withZenith(Math.toRadians(-25));

        double aspect = (double) raster.getSirska() / raster.getVyska();

        perspProj = new Mat4PerspRH(Math.PI / 4, aspect, 0.1, 100.0);
        orthoProj = new Mat4OrthoRH(10 * aspect, 10, 0.1, 100.0, 0, 0);

        render3D.setProjMatrix(perspProj);

        scene.add(new Axis(2, 0, 0, Color.RED));
        scene.add(new Axis(0, 2, 0, Color.GREEN));
        scene.add(new Axis(0, 0, 2, Color.BLUE));

        Sphere lightSphere = new Sphere(8);
        lightSphere.setModelMatrix(new Mat4Transl(5, 5, 5));
        lightSphere.setColor(Color.YELLOW);
        scene.add(lightSphere);
        lightSourceIndex = scene.size() - 1;

        Cube cube = new Cube();
        cube.setModelMatrix(new Mat4Transl(0, 0, 0).mul(new Mat4Scale(2, 2, 2)));
        cube.setColor(Color.MAGENTA);
        cube.setTexture(TextureGenerator.createCheckerboard(128, Color.WHITE, Color.BLACK));
        cube.setUseTexture(false);
        cube.setUseLighting(true);
        scene.add(cube);

        Hranol hranol = new Hranol();
        hranol.setModelMatrix(new Mat4Transl(0.5, 0, 0.5).mul(new Mat4Scale(1.5, 1.5, 1.5)));
        hranol.setColor(Color.ORANGE);
        hranol.setTexture(TextureGenerator.createGradient(128, Color.RED, Color.BLUE));
        hranol.setUseTexture(false);
        hranol.setUseLighting(true);
        scene.add(hranol);

        Pyramid pyramid = new Pyramid();
        pyramid.setModelMatrix(new Mat4Transl(-2, 0, -2));
        pyramid.setColor(Color.YELLOW);
        pyramid.setTexture(TextureGenerator.createCheckerboard(128, Color.YELLOW, Color.GREEN));
        pyramid.setUseTexture(false);
        pyramid.setUseLighting(true);
        scene.add(pyramid);

        setFirstActive();
    }

    private void initListeners() {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                panel.requestFocusInWindow();
                if (e.getButton() == MouseEvent.BUTTON1) {
                    startX = e.getX();
                    startY = e.getY();
                    mouseLeftDown = true;
                }
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) mouseLeftDown = false;
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (mouseLeftDown) {
                    int dx = startX - e.getX();
                    int dy = startY - e.getY();
                    double sensitivity = 0.005;

                    camera = camera.addAzimuth(dx * sensitivity);
                    camera = camera.addZenith(dy * sensitivity);

                    startX = e.getX();
                    startY = e.getY();
                    display();
                }
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                double speed = 0.3;
                double moveStep = 0.2;
                double rotStep = Math.toRadians(10);
                double scaleStep = 1.1;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W: camera = camera.forward(speed); break;
                    case KeyEvent.VK_S: camera = camera.backward(speed); break;
                    case KeyEvent.VK_A: camera = camera.left(speed); break;
                    case KeyEvent.VK_D: camera = camera.right(speed); break;

                    case KeyEvent.VK_R:
                        camera = new Camera()
                                .withPosition(new Vec3D(6, 4, 6))
                                .withAzimuth(Math.toRadians(225))
                                .withZenith(Math.toRadians(-25));
                        break;
                    case KeyEvent.VK_P:
                        usePerspective = !usePerspective;
                        if (usePerspective) {
                            render3D.setProjMatrix(perspProj);
                        } else {
                            render3D.setProjMatrix(orthoProj);
                        }
                        break;
                    case KeyEvent.VK_M:
                        if (drawFilled && drawWireframe) {
                            drawWireframe = false;
                        } else if (drawFilled) {
                            drawFilled = false;
                            drawWireframe = true;
                        } else {
                            drawFilled = true;
                            drawWireframe = true;
                        }
                        render3D.setDrawFilled(drawFilled);
                        render3D.setDrawWireframe(drawWireframe);
                        break;

                    case KeyEvent.VK_TAB:
                        selectNextActive();
                        break;

                    case KeyEvent.VK_LEFT:
                        applyToActiveWorld(new Mat4Transl(-moveStep, 0, 0));
                        break;
                    case KeyEvent.VK_RIGHT:
                        applyToActiveWorld(new Mat4Transl(moveStep, 0, 0));
                        break;
                    case KeyEvent.VK_UP:
                        applyToActiveWorld(new Mat4Transl(0, 0, -moveStep));
                        break;
                    case KeyEvent.VK_DOWN:
                        applyToActiveWorld(new Mat4Transl(0, 0, moveStep));
                        break;
                    case KeyEvent.VK_PAGE_UP:
                        applyToActiveWorld(new Mat4Transl(0, moveStep, 0));
                        break;
                    case KeyEvent.VK_PAGE_DOWN:
                        applyToActiveWorld(new Mat4Transl(0, -moveStep, 0));
                        break;

                    case KeyEvent.VK_Q:
                        applyToActiveLocal(new Mat4RotY(rotStep));
                        break;
                    case KeyEvent.VK_E:
                        applyToActiveLocal(new Mat4RotY(-rotStep));
                        break;
                    case KeyEvent.VK_Z:
                        applyToActiveLocal(new Mat4RotX(rotStep));
                        break;
                    case KeyEvent.VK_X:
                        applyToActiveLocal(new Mat4RotX(-rotStep));
                        break;
                    case KeyEvent.VK_C:
                        applyToActiveLocal(new Mat4RotZ(rotStep));
                        break;
                    case KeyEvent.VK_V:
                        applyToActiveLocal(new Mat4RotZ(-rotStep));
                        break;

                    case KeyEvent.VK_EQUALS:
                    case KeyEvent.VK_PLUS:
                        applyToActiveLocal(new Mat4Scale(scaleStep, scaleStep, scaleStep));
                        break;
                    case KeyEvent.VK_MINUS:
                        applyToActiveLocal(new Mat4Scale(1.0 / scaleStep, 1.0 / scaleStep, 1.0 / scaleStep));
                        break;

                    case KeyEvent.VK_T:
                        toggleActiveTexture();
                        break;
                }
                display();
            }
        });



        panel.setFocusable(true);
        panel.requestFocusInWindow();
    }

    private void display() {
        raster.clear();
        zBuffer.clear();

        // Aktualizace pozice světla podle pozice sféry
        if (lightSourceIndex >= 0 && lightSourceIndex < scene.size()) {
            Solid lightSolid = scene.get(lightSourceIndex);
            Mat4 lightModel = lightSolid.getModelMatrix();
            // Extrahovat pozici ze světelné matice (translační část)
            Point3D lightOrigin = lightModel.multiply(new Point3D(0, 0, 0));
            Vec3D lightPos = new Vec3D(lightOrigin.x / lightOrigin.w,
                                       lightOrigin.y / lightOrigin.w,
                                       lightOrigin.z / lightOrigin.w);

            // Předat aktualizovanou pozici světla do triangle rasterizeru
            LineRasterizer lineRasterizer = new FilledLineRasterizer(panel.getRaster());
            TriangleRasterizer triangleRasterizer = new TriangleRasterizer(raster, zBuffer);
            triangleRasterizer.setLightPos(lightPos);
            render3D = new Render3D(raster, lineRasterizer, triangleRasterizer);
            render3D.setDrawFilled(drawFilled);
            render3D.setDrawWireframe(drawWireframe);
            render3D.setProjMatrix(usePerspective ? perspProj : orthoProj);
        }

        render3D.setViewMatrix(camera.getViewMatrix());
        render3D.render(scene);
        panel.repaint();
    }

    private void setFirstActive() {
        for (int i = 0; i < scene.size(); i++) {
            if (!(scene.get(i) instanceof Axis)) {
                activeIndex = i;
                return;
            }
        }
        activeIndex = -1;
    }

    private void selectNextActive() {
        if (scene.isEmpty()) return;
        int start = activeIndex < 0 ? 0 : activeIndex;
        for (int offset = 1; offset <= scene.size(); offset++) {
            int idx = (start + offset) % scene.size();
            if (!(scene.get(idx) instanceof Axis)) {
                activeIndex = idx;
                return;
            }
        }
    }

    private void applyToActiveWorld(Mat4 transform) {
        if (activeIndex < 0 || activeIndex >= scene.size()) return;
        Solid active = scene.get(activeIndex);
        active.setModelMatrix(transform.mul(active.getModelMatrix()));
    }

    private void applyToActiveLocal(Mat4 transform) {
        if (activeIndex < 0 || activeIndex >= scene.size()) return;
        Solid active = scene.get(activeIndex);
        active.setModelMatrix(active.getModelMatrix().mul(transform));
    }

    private void toggleActiveTexture() {
        if (activeIndex < 0 || activeIndex >= scene.size()) return;
        Solid active = scene.get(activeIndex);
        active.setUseTexture(!active.isUseTexture());
    }
}

