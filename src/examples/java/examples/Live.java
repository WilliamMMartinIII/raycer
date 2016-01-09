package examples;

import ray_tracer.cameras.Camera;
import ray_tracer.cameras.FancyCamera;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.*;

/**
 * Created by William Martin III on 12/25/15.
 */
public class Live {
    static Camera camera = new FancyCamera();

    static ExampleScene scene = new ExampleScene(camera);

    static JPanel pane;
    static Timer timer;

    public static class RepaintListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            pane.repaint();
        }
    }

    public static class KeyPressListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_RIGHT:
                    scene.cameraPosition[0]++;
                    break;
                case KeyEvent.VK_LEFT:
                    scene.cameraPosition[0]--;
                    break;
                case KeyEvent.VK_UP:
                    scene.cameraPosition[2]++;
                    break;
                case KeyEvent.VK_DOWN:
                    scene.cameraPosition[2]--;
                    break;
                case KeyEvent.VK_PAGE_UP:
                    scene.cameraPosition[1]++;
                    break;
                case KeyEvent.VK_PAGE_DOWN:
                    scene.cameraPosition[1]--;
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                camera.tearDown();
            }
        });
        frame.setSize((int) (scene.WIDTH * scene.ZOOM), (int) (scene.HEIGHT * scene.ZOOM));
        frame.setVisible(true);

        frame.addKeyListener(new KeyPressListener());

        camera.setUp();

        pane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                long time = System.currentTimeMillis();
                BufferedImage original = scene.render((System.currentTimeMillis() / 1000.0));

                int w = (int) (original.getWidth() * scene.ZOOM);
                int h = (int) (original.getHeight() * scene.ZOOM);
                BufferedImage finished = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                AffineTransform at = new AffineTransform();
                at.scale(scene.ZOOM, scene.ZOOM);
                AffineTransformOp scaleOp =
                        new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
                finished = scaleOp.filter(original, finished);

                g.drawImage(finished, 0, 0, null);

                System.out.println(((System.currentTimeMillis() - time) / 1000.0) + " seconds");
            }
        };

        frame.add(pane);
        pane.repaint();

        timer = new Timer((int)(1.0 / scene.FRAME_RATE * 1000.0), new RepaintListener());
        timer.start();
    }
}
