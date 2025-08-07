import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;

/**
 * Java class to create an initializer for creating a fractal
 * Sets up JFrame Window to produce a custom initializer or the classic dragon curve initializer
 *
 * @version August 5, 2025
 * @author Layla Shihab
 */

public class DrawInitializer extends JComponent implements Runnable {
    JFrame frame;
    Image image; // the canvas
    Graphics2D graphics2D;  // this will enable drawing
    int curX; // current mouse x coordinate
    int curY; // current mouse y coordinate
    int lastX; // previous mouse x coordinate
    int lastY; // previous mouse y coordinate

    JButton useOriginalDragonCurveInitializer;
    JButton customInitializer;

    DrawInitializer paint; // variable of the type SimplePaint

    static Path2D initializer;
    int sideLength = new DragonCurveFractal().getSideLength();
    double scaleFactor;

    int xf;
    int yf;

    boolean startFlag = false;

    ArrayList<Integer> xPoints = new ArrayList<>();
    ArrayList<Integer> yPoints = new ArrayList<>();

    public DrawInitializer() {
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int delta = 50;

                // if clicking on first point
                if (Math.abs(e.getX() - (getWidth() / 2 - sideLength / 2)) < delta &&
                        Math.abs(e.getY() - (getHeight() / 2)) < delta) {
                    lastX = (getWidth() / 2 - sideLength / 2);
                    lastY = (getHeight() / 2);

                    xPoints.add(lastX);
                    yPoints.add(lastY);

                    startFlag = true;
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (startFlag) {
                    // adds newest point to arraylists
                    lastX = e.getX();
                    lastY = e.getY();

                    xPoints.add(lastX);
                    yPoints.add(lastY);
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (startFlag) {
                    // removes old line
                    graphics2D.setPaint(Color.white);
                    graphics2D.fillRect(0, 0, getSize().width, getSize().height);
                    graphics2D.setPaint(Color.black);

                    // redraws complete lines
                    for (int i = 0; i < xPoints.size() - 1; i++) {
                        graphics2D.drawLine(xPoints.get(i), yPoints.get(i), xPoints.get(i + 1), yPoints.get(i + 1));
                    }

                    // draws the start and end points
                    graphics2D.setStroke(new BasicStroke(10));
                    graphics2D.drawLine(getWidth() / 2 - sideLength / 2, getSize().height / 2,
                            getWidth() / 2 - sideLength / 2, getSize().height / 2);
                    graphics2D.drawLine(getWidth() / 2 + sideLength / 2, getSize().height / 2,
                            getWidth() / 2 + sideLength / 2, getSize().height / 2);
                    graphics2D.setStroke(new BasicStroke(5));

                    // set current coordinates to where mouse is being dragged
                    curX = e.getX();
                    curY = e.getY();

                    // draw the line between start and current coords
                    graphics2D.drawLine(lastX, lastY, curX, curY);

                    // refresh frame and reset old coordinates
                    repaint();
                }
            }
        });
    }

    public void run() {
        frame = Main.createBasicFrame();
        frame.setTitle("Create an initializer");

        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        paint = this;
        content.add(paint, BorderLayout.CENTER);

        useOriginalDragonCurveInitializer = new JButton("Use Original Dragon Curve");
        // action listener to return dragon curve initializer
        useOriginalDragonCurveInitializer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // stores two lines that form a 90 degree angle to manipulated to form curve
                initializer = new Path2D.Double();

                sideLength = 256;
                scaleFactor = Math.sqrt(2) / 2;

                // points to create initializer
                int[] xPoints = new int[]{0, sideLength / 2, sideLength};
                int[] yPoints = new int[]{0, sideLength / 2, 0};

                // Move to the first point
                initializer.moveTo(xPoints[0], yPoints[0]);

                // Draw lines to the remaining points
                for (int i = 1; i < xPoints.length; i++) {
                    initializer.lineTo(xPoints[i], yPoints[i]);
                }

                initializer.moveTo(xPoints[0], yPoints[0]);
                initializer.closePath();

                SwingUtilities.invokeLater(new DragonCurveFractal());
                frame.dispose();
            }
        });

        customInitializer = new JButton("Custom Initializer");
        customInitializer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startFlag = false;

                // sets final coordinates to the last point
                xf = (getWidth() / 2 + sideLength / 2);
                yf = (getHeight() / 2);

                xPoints.add(xf);
                yPoints.add(yf);

                initializer = new Path2D.Double();

                // Move to the first point
                initializer.moveTo(xPoints.get(0) - (getWidth() / 2f - sideLength / 2f), yPoints.get(0) - getHeight() / 2f);

                // Draw lines to the remaining points
                for (int i = 1; i < xPoints.size(); i++) {
                    // shifts the points to account for centering (first point will be at origin)
                    initializer.lineTo(xPoints.get(i) - (getWidth() / 2f - sideLength / 2f), yPoints.get(i) - getHeight() / 2f);
                }

                initializer.moveTo(xPoints.get(0), yPoints.get(0));
                initializer.closePath();

                sideLength = 256;

                // sets scaleFactor
                scaleFactor = Math.sqrt(2) / 2;

                SwingUtilities.invokeLater(new DragonCurveFractal());
                frame.dispose();
            }
        });

        JPanel introPanel = new JPanel();
        introPanel.setLayout(new GridLayout(2,1));
        JLabel welcomeLabel = new JLabel("Dragon Curve Initializer Creation!");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel instructionsLabel = new JLabel("Draw your own initializer or use the classic");
        introPanel.add(welcomeLabel);
        introPanel.add(instructionsLabel);

        // panel with buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(useOriginalDragonCurveInitializer);
        buttonPanel.add(customInitializer);

        Main.styleAll(new JButton[]{useOriginalDragonCurveInitializer,customInitializer},
                new JPanel[]{introPanel, buttonPanel},
                new JLabel[]{instructionsLabel},
                new JLabel[]{welcomeLabel});

        content.add(introPanel, BorderLayout.NORTH);
        content.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }


    protected void paintComponent(Graphics g) {
        if (image == null) {
            image = createImage(getSize().width, getSize().height);

            // this lets us draw on the image (ie. the canvas)
            graphics2D = (Graphics2D) image.getGraphics();

            // gives us better rendering quality for the drawing lines
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            // set canvas to white with default paint color
            graphics2D.setPaint(Color.white);
            graphics2D.fillRect(0, 0, getSize().width, getSize().height);
            graphics2D.setPaint(Color.black);

            // draws the start and end points
            graphics2D.setStroke(new BasicStroke(10));
            graphics2D.drawLine(getWidth() / 2 - sideLength / 2, getSize().height / 2,
                    getWidth() / 2 - sideLength / 2, getSize().height / 2);
            graphics2D.drawLine(getWidth() / 2 + sideLength / 2, getSize().height / 2,
                    getWidth() / 2 + sideLength / 2, getSize().height / 2);
            graphics2D.setStroke(new BasicStroke(3));

            repaint();
        }
        g.drawImage(image, 0, 0, null);

    }

    public static Path2D getInitializer() {
        return initializer;
    }

}
