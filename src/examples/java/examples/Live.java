package examples;

import org.jblas.DoubleMatrix;
import ray_tracer.MatrixUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;

/**
 * Created by William Martin III on 12/25/15.
 */
public class Live {

    static AtomScene scene = new AtomScene();

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
            DoubleMatrix posX = new DoubleMatrix(new double[] {0.2, 0, 0});
            DoubleMatrix posY = new DoubleMatrix(new double[] {0, 0.2, 0});
            DoubleMatrix posZ = new DoubleMatrix(new double[] {0, 0, 0.2});

            switch (e.getKeyCode()) {
                case KeyEvent.VK_RIGHT:
                    scene.cameraPosition[0] += 0.2;
                    break;
                case KeyEvent.VK_LEFT:
                    scene.cameraPosition[0] -= 0.2;
                    break;
                case KeyEvent.VK_UP:
                    scene.cameraPosition[2] += 0.2;;
                    break;
                case KeyEvent.VK_DOWN:
                    scene.cameraPosition[2] -= 0.2;;
                    break;
                case KeyEvent.VK_PAGE_UP:
                    scene.cameraPosition[1] += 0.2;
                    break;
                case KeyEvent.VK_PAGE_DOWN:
                    scene.cameraPosition[1] -= 0.2;
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

    public static class MouseListener implements MouseMotionListener {
        private Point lastPoint;

        @Override
        public void mouseDragged(MouseEvent e) {
            if (lastPoint == null) {
                lastPoint = e.getPoint();
                return;
            }
            Point point = e.getPoint();
            double x = -((double) point.x - lastPoint.x) / scene.WIDTH;
            double y = ((double) point.y - lastPoint.y) / scene.HEIGHT;

            DoubleMatrix angle = new DoubleMatrix(new double[] {x, y, 1.0});
            System.out.println(angle);
            DoubleMatrix rotation = MatrixUtil.rotate(angle, MatrixUtil.Y, MatrixUtil.Z);
            DoubleMatrix camera = new DoubleMatrix(scene.cameraRotation);
            scene.cameraRotation = rotation.mmul(camera).toArray();

            lastPoint = e.getPoint();
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize((int) (scene.WIDTH * scene.ZOOM), (int) (scene.HEIGHT * scene.ZOOM));
        frame.setVisible(true);

        frame.addKeyListener(new KeyPressListener());
        frame.addMouseMotionListener(new MouseListener());

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
