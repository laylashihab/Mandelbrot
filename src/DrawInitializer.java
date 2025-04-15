import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;

public class DrawInitializer extends JComponent implements Runnable {
    JFrame frame;
    Image image; // the canvas
    Graphics2D graphics2D;  // this will enable drawing
    int curX; // current mouse x coordinate
    int curY; // current mouse y coordinate
    int startX; // previous mouse x coordinate
    int startY; // previous mouse y coordinate

    JButton enterButton; // button to enter information
    JButton useOriginalDragonCurveInitializer;

    DrawInitializer paint; // variable of the type SimplePaint

    Path2D initializer;
    int sideLength;
    int[] startEndPoints;

    private int maxGen;

    public DrawInitializer() {
        initializer = new Path2D.Double();
        startEndPoints = new int[4];

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (initializer.getCurrentPoint() == null) {
                    startX = e.getX();
                    startY = e.getY();

                    startEndPoints[0] = startX;
                    startEndPoints[1] = startY;

                    initializer.moveTo(startX, startY);
                }
            }
            public void mouseReleased(MouseEvent e) {
                startX = e.getX();
                startY = e.getY();

                initializer.lineTo(startX, startY);
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // removes old line
                graphics2D.setPaint(Color.white);
                graphics2D.fillRect(0, 0, getSize().width, getSize().height);
                graphics2D.setPaint(Color.black);

                // redraws complete lines
                graphics2D.draw(initializer);

                // set current coordinates to where mouse is being dragged
                curX = e.getX();
                curY = e.getY();

                // draw the line between start and current coords
                graphics2D.drawLine(startX, startY, curX, curY);

                // refresh frame and reset old coordinates
                repaint();
            }
        });
    }


    public void run() {
        frame = new JFrame();
        frame.setTitle("Create an initializer");

        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        paint = new DrawInitializer();
        content.add(paint, BorderLayout.CENTER);


        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        enterButton = new JButton("Enter");
        // action listener to store new initializer information
        enterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // stores end points
                startEndPoints[2] = startX;
                startEndPoints[3] = startY;

                // calculates sidelength
                sideLength = (int)(Math.sqrt(
                        Math.pow(startEndPoints[2]-startEndPoints[0],2)+Math.pow(startEndPoints[3]-startEndPoints[1],2)));
            }
        });

        useOriginalDragonCurveInitializer = new JButton("Use Original Dragon Curve");
        // action listener to return dragon curve initializer
        useOriginalDragonCurveInitializer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // stores two lines that form a 90 degree angle to manipulated to form curve
                initializer = new Path2D.Double();

                sideLength = 256;

                // points to create initializer
                int[] xPoints = new int[]{0, sideLength / 2, sideLength};
                int[] yPoints = new int[]{0, sideLength / 2, 0};

                // Move to the first point
                initializer.moveTo(xPoints[0], yPoints[0]);

                // Draw lines to the remaining points
                for (int i = 1; i < xPoints.length; i++) {
                    initializer.lineTo(xPoints[i], yPoints[i]);
                }
            }
        });

        JPanel panel = new JPanel();
        panel.add(enterButton);
        panel.add(useOriginalDragonCurveInitializer);
        content.add(panel, BorderLayout.NORTH);

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
            repaint();
        }
        g.drawImage(image, 0, 0, null);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new DrawInitializer());
    }
}
