import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.Random;
import javax.swing.*;

/**
 * Class to generate a Sierpinski triangle fractal. User specifies the max number of generations
 *
 *
 * NOTE: this could be greatly improved by simply moving each triangle from center out a distance of d and rotating g2 accordingly.
 *
 * @author Layla Shihab
 * @version April 11 2025
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

    JLabel hausdorff;

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton b = (JButton)e.getSource();
            if (b.getText().equals("Next Generation")) {
                if (maxGen < 16) {
                    maxGen++;
                    hausdorff.setText(String.format("Hausdorff Dimension: log %.2f / log %.2f = %.4f",
                            Math.pow(3, maxGen+1), Math.pow(2,maxGen+1),
                            Math.log(Math.pow(3, maxGen+1))/Math.log(Math.pow(2,maxGen+1))));
                    frame.repaint();
                    frame.revalidate();
                } else {
                    JOptionPane.showMessageDialog(null, "Generation Limit Reached");
                }
            }
            if (b.getText().equals("Last Generation")) {
                if (maxGen > 0) {
                    maxGen--;
                    hausdorff.setText(String.format("Hausdorff Dimension: log %.2f / log %.2f = %.4f",
                            Math.pow(3, maxGen+1), Math.pow(2,maxGen+1),
                            Math.log(Math.pow(3, maxGen+1))/Math.log(Math.pow(2,maxGen+1))));
                    frame.repaint();
                    frame.revalidate();
                }
            }
        }
    };

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new SierpinskiTriangle());
    }

    public void run(){
        // sets up frame
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null); //centers frame on screen

        JPanel content = new JPanel();
        JButton nextGenButton = new JButton("Next Generation");
        nextGenButton.addActionListener(actionListener);
        JButton lastGenButton = new JButton("Last Generation");
        lastGenButton.addActionListener(actionListener);
        content.add(nextGenButton);
        content.add(lastGenButton);
        frame.add(content, BorderLayout.NORTH);

        JPanel info = new JPanel();
        hausdorff = new JLabel();
        hausdorff.setFont(new Font("Courier", Font.PLAIN, 20));
        info.add(hausdorff);
        frame.add(info, BorderLayout.SOUTH);

        frame.add(new SierpinskiTriangle(), BorderLayout.CENTER);

        frame.setVisible(true);
        frame.repaint();
    }

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