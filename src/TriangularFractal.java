import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.Random;
import javax.swing.*;

/**
 * Class to generate a triangular fractal. User specifies the max number of generations
 *
 *
 * @author Layla Shihab
 * @version April 11 2025
 *
 */

public class TriangularFractal extends JComponent {
     Graphics2D g2;
     double l = 300; //length of one side
     double h = l*(Math.sqrt(3))*(1/2f); //height from midpoint of one segment to opposite vertex
     double d = (1/(2*Math.sqrt(3)))*l; // distance from centroid to midpoint of one segment
     double r = h -d;
     Path2D.Double initializer;
     static int maxGen = -1;
     Random random = new Random();

    public static void main(String[] args) {
        while (maxGen == -1) {
            try {
                maxGen = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter a number of generations:"));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a number");
            }
        }
        // sets up frame
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null); //centers frame on screen

        frame.add(new TriangularFractal());

        frame.setVisible(true);

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


         nextGen(0);
     }

     public void nextGen(int generation) {

         if (generation == maxGen-1) {
             return;
         }

         double multiplier = 3;
         AffineTransform here;

         //g2.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
         g2.scale(1/3f, 1/3f);

         // translates from center to the right triangle for right triangle
         g2.translate(multiplier*(l/3), -multiplier*(h * (2/9f)));
         g2.rotate(Math.PI/3f);

         g2.draw(initializer); // Draw the outline
         g2.fill(initializer); // Fill the polygon
         here = g2.getTransform();
         nextGen(generation+1);
         g2.setTransform(here);

         // moves to bottom for next triangle
         g2.rotate(-Math.PI/3f);
         g2.translate(-multiplier*(1/3f)*l, multiplier*(2 * d));
         g2.rotate(+Math.PI/3f);

         g2.draw(initializer); // Draw the outline
         g2.fill(initializer); // Fill the polygon
         here = g2.getTransform();
         nextGen(generation+1);
         g2.setTransform(here);

         // moves to left for next triangle
         g2.rotate(-Math.PI/3f);
         g2.translate(-multiplier*(1/3f)*l, -multiplier*(2 * d));
         g2.rotate(+Math.PI/3f);

         g2.draw(initializer); // Draw the outline
         g2.fill(initializer); // Fill the polygon

         here = g2.getTransform();
         nextGen(generation+1);
         g2.setTransform(here);

     }
 }