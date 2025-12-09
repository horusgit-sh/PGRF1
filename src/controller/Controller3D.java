package controller;

import rasterize.FilledLineRasterizer;
import rasterize.LineRasterizer;
import rasterize.Raster;
import render.Render3D;
import solid.*;
import transforms.*;
import view.Panel;

import javax.sound.sampled.Line;
import java.awt.Color;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Controller3D {

    private final Panel panel;
    private final Raster raster;

    private Render3D render3D;
    private Camera camera;

    // Seznam objektu
    private final List<Solid> scene = new ArrayList<>();

    private int startX, startY;
    private boolean mouseLeftDown = false;



    public Controller3D(Panel panel) {
        this.panel = panel;
        this.raster = panel.getRaster();

        initObjects();
        initListeners();

        display();
    }

    //Inicializace objektu, kamery a rendereru.
    private void initObjects() {
        //Rasterizer
        LineRasterizer lineRasterizer = new FilledLineRasterizer(panel.getRaster());
        //Renderer
        render3D = new Render3D(raster, lineRasterizer);

        // Kamera
        camera = new Camera()
                .withPosition(new Vec3D(6, 4, 6))
                .withAzimuth(Math.toRadians(225))
                .withZenith(Math.toRadians(-25));

        // Projekce
        double aspect = (double) raster.getSirska() / raster.getVyska();
        Mat4 pers = new Mat4PerspRH(Math.PI / 4, aspect, 0.1, 100.0);
        render3D.setProjMatrix(pers);


        // Osy
        // Osa X
        scene.add(new Axis(2, 0, 0, Color.RED));

        // Osa Y
        scene.add(new Axis(0, 2, 0, Color.GREEN));

        // Osa Z
        scene.add(new Axis(0, 0, 2, Color.BLUE));


        // Cube
        Cube cube = new Cube();
        cube.setModelMatrix(new Mat4Transl(2, 0, -2));
        cube.setColor(Color.MAGENTA);
        scene.add(cube);

        //Hranol
        Hranol hranol = new Hranol();
        hranol.setModelMatrix(new Mat4Transl(-2, 0, 0));
        hranol.setColor(Color.ORANGE);
        scene.add(hranol);

        //Pyramid
        Pyramid pyramid = new Pyramid();
        pyramid.setModelMatrix(new Mat4Transl(2, 1, 2));
        pyramid.setColor(Color.YELLOW);
        scene.add(pyramid);

        //Bezier Krivka
        Point3D p1 = new Point3D(-3, 0, -2);
        Point3D p2 = new Point3D(-4, 4,  2);
        Point3D p3 = new Point3D(-2, 4,  4);
        Point3D p4 = new Point3D(-3, 0, 6);
        BezierCurve curve = new BezierCurve(p1, p2, p3, p4);
        scene.add(curve);

        // Fergusonova krivka
        Point3D fp0 = new Point3D(1, 0, -1);
        Point3D fp1 = new Point3D(1, 2, -1);
        Point3D ft0 = new Point3D(0, 2, 1);
        Point3D ft1 = new Point3D(0, -2, 1);
        FergusonCurve ferguson = new FergusonCurve(fp0, fp1, ft0, ft1);
        scene.add(ferguson);

        // Coonsova krivka
        Point3D cp0 = new Point3D(-1, 0, -2);
        Point3D cp1 = new Point3D(-1, 4,  1);
        Point3D cp2 = new Point3D(-1, 2,  4);
        Point3D cp3 = new Point3D(-1, 0, 6);
        CoonsovaCurve coons = new CoonsovaCurve(cp0, cp1, cp2, cp3);
        scene.add(coons);

        // Parametricka plocha
        Surface surface = new Surface();
        surface.setModelMatrix(new Mat4Transl(0, -2, 0)); // Posuneme dolů
        scene.add(surface);
    }

    private void initListeners() {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
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

        //Klavesnice (Pohyb WSAD + Reset R)
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                double speed = 0.3;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W: camera = camera.forward(speed); break;
                    case KeyEvent.VK_S: camera = camera.backward(speed); break;
                    case KeyEvent.VK_A: camera = camera.left(speed); break;
                    case KeyEvent.VK_D: camera = camera.right(speed); break;

                    // Reset kamery
                    case KeyEvent.VK_R:
                        camera = new Camera()
                                .withPosition(new Vec3D(6, 4, 6))
                                .withAzimuth(Math.toRadians(225))
                                .withZenith(Math.toRadians(-25));
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
        render3D.setViewMatrix(camera.getViewMatrix());
        render3D.render(scene);
        panel.repaint();
    }
}