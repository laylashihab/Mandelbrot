import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import javax.swing.*;

/**
 * Class to generate a Sierpinski triangle fractal. Buttons allow for more/less depth in the fractal
 *
 * @author Layla Shihab
 * @version August 5 2025
 *
 */
public class SierpinskiTriangle extends JComponent implements Runnable {
    Graphics2D g2;
    JFrame frame;
    double l = 300; //length of one side
    double h = l*(Math.sqrt(3))*(1/2f); //height from midpoint of one segment to opposite vertex
    double d = (1/(2*Math.sqrt(3)))*l; // distance from centroid to midpoint of one segment
    double r = h -d;
    Path2D.Double initializer;
    static int maxGen = 0;


    /**
     * Updates the Hausdorff label with the new scale and number of self similar objects
     *
     * @param hausdorff: the JLabel to update
     */
    public void updateHausdorffLabel(JLabel hausdorff) {
        hausdorff.setText(String.format("Hausdorff Dimension: log %.2f / log %.2f = %.4f",
                Math.pow(3, maxGen+1), Math.pow(2,maxGen+1),
                Math.log(Math.pow(3, maxGen+1))/Math.log(Math.pow(2,maxGen+1))));
        frame.repaint();
        frame.revalidate();
    }

    /**
     * Sets up the Sierpinski Triangle frame to house the fractal and buttons
     */
    public void run(){
        // sets up frame
        frame = Main.createBasicFrame();

        // sets up panel with buttons
        JPanel content = new JPanel();
        content.setLayout(new GridBagLayout());
        JLabel title = new JLabel("Sierpinski Triangle Generator");
        JLabel hausdorff = new JLabel();
        updateHausdorffLabel(hausdorff);
        JButton nextGenButton = new JButton("Next Generation");
        nextGenButton.addActionListener(e -> {
            if (maxGen < 16) {
                maxGen++;
                updateHausdorffLabel(hausdorff);
            } else {
                JOptionPane.showMessageDialog(null, "Generation Limit Reached");
            }
        });
        JButton lastGenButton = new JButton("Last Generation");
        lastGenButton.addActionListener(e->{
            if (maxGen > 0) {
                maxGen--;
                updateHausdorffLabel(hausdorff);
            }
        });

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 0;
        content.add(title,c);
        c.gridwidth = 1;
        c.gridy=1;
        content.add(lastGenButton,c);
        c.gridx = 1;
        content.add(nextGenButton,c);
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 2;
        content.add(hausdorff,c);

        JPanel returnHomePanel = Main.returnHomePanel(frame);

        Main.styleAll(new JButton[]{nextGenButton,lastGenButton},
                new JPanel[]{content},
                new JLabel[]{hausdorff},
                new JLabel[]{title});

        frame.add(content, BorderLayout.NORTH);
        frame.add(new SierpinskiTriangle(), BorderLayout.CENTER);
        frame.add(returnHomePanel, BorderLayout.SOUTH);

        frame.setVisible(true);
        frame.repaint();
    }

    /**
     * Sets up the initializer and draws initializer
     *
     * @param g the <code>Graphics</code> object to protect
     */
    public void paintComponent(Graphics g) {
        g2 = (Graphics2D) g;
        double[] xPoints;
        double[] yPoints;

        // sets coordinate origin to center
        g2.translate(getWidth()/2, getHeight()/2);

        // center of triangle
        double x = 0;
        double y = 0;

        g2.setColor(Color.PINK);
        xPoints = new double[] {x, x + (l/2), x - (l/2)};
        yPoints = new double[] {-1 * (y + r), -1 * (y - d), -1 * (y -d)};

        // initializer
        initializer = new Path2D.Double();

        // Move to the first point
        initializer.moveTo(xPoints[0], yPoints[0]);

        // Draw lines to the remaining points
        for (int i = 1; i < xPoints.length; i++) {
            initializer.lineTo(xPoints[i], yPoints[i]);
        }

        // Close the polygon
        initializer.closePath();

        g2.draw(initializer); // Draw the outline
        g2.fill(initializer); // Fill the polygon

        // rotate the initializer
        g2.rotate(Math.PI);
        g2.setColor(Color.WHITE);

        g2.scale(1/2f, 1/2f);
        g2.fill(initializer);

        if (maxGen > 0){
            nextGen(0);
        }
    }

    /**
     * Draws the next generation of a triangular fractal
     * Recursively runs until the generation hits the maxGen
     *
     * @param generation: the generation being drawn in that iteration
     */
    public void nextGen(int generation) {
        AffineTransform here;
        here = g2.getTransform();

        g2.translate(-l/2, -d);
        g2.scale(1/2f, 1/2f);
        g2.fill(initializer);
        if (generation+1 != maxGen) {
            nextGen(generation+1);
        }

        g2.setTransform(here);

        g2.translate(l/2, -d);
        g2.scale(1/2f, 1/2f);
        g2.fill(initializer);
        if (generation+1 != maxGen) {
            nextGen(generation+1);
        }

        g2.setTransform(here);

        g2.translate(0, 2*d);
        g2.scale(1/2f, 1/2f);
        g2.fill(initializer);
        if (generation+1 != maxGen) {
            nextGen(generation+1);
        }

        g2.setTransform(here);

    }
}