import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class TriangularFractal extends JComponent implements Runnable {
     Graphics2D g2;
     double l = 300; //length of one side
     double h = l*(Math.sqrt(3))*(1/2f); //height from midpoint of one segment to opposite vertex
     double d = (1/(2*Math.sqrt(3)))*l; // distance from centroid to midpoint of one segment
     double r = h -d;
     Path2D.Double initializer;
     static int maxGen = 1;


    public void run() {
        // sets up frame
        JFrame frame = Main.createBasicFrame();

        // sets up Title and button content
        JPanel content = new JPanel();
        content.setLayout(new GridBagLayout());
        JLabel title = new JLabel("Triangular Fractal Generator");
        JButton nextGenButton = new JButton("Next Generation");
        nextGenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (maxGen < 8) {
                    maxGen++;
                    frame.repaint();
                    frame.revalidate();
                } else {
                    JOptionPane.showMessageDialog(null, "Generation Limit Reached");
                }
            }
        });
        JButton lastGenButton = new JButton("Last Generation");
        lastGenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (maxGen > 0) {
                    maxGen--;
                    frame.repaint();
                    frame.revalidate();
                }
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

        JPanel returnHomePanel = Main.returnHomePanel(frame);

        // styles all buttons and panels
        Main.styleAll(new JButton[]{nextGenButton,lastGenButton},
                new JPanel[]{content},
                new JLabel[]{},
                new JLabel[]{title});


        // adds all content/panels to frame
        frame.add(content, BorderLayout.NORTH);
        frame.add(new TriangularFractal());
        frame.add(returnHomePanel, BorderLayout.SOUTH);

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

        if (maxGen > 0){
             nextGen(0);
         }
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