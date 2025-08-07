import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

/**
 * Java class to produce a dragon curve fractal
 * Sets up JFrame Window to visualize the fractal and allows user to step through diff generations
 *
 * @author Layla Shihab
 * @version August 5 2025
 */

public class DragonCurveFractal extends JComponent implements Runnable {
    Graphics2D g2;
    Path2D initializer;
    private JFrame frame;

    int sideLength = 256;
    static int maxGen =0; // the maximum number of generations to produce
    double scaleFactor = Math.sqrt(2) / 2;

    int n = 2; // number of self-similar objects

    /**
     * Updates the Hausdorff label with the new scale and number of self similar objects
     *
     * @param hausdorff: the JLabel to update
     */
    public void updateHausdorffText(JLabel hausdorff) {
        hausdorff.setText(String.format("Hausdorff Dimension: log %.2f / log %.2f = %.4f",
                Math.pow(2, maxGen + 1), Math.pow(Math.sqrt(2), maxGen + 1),
                Math.log(Math.pow(2, maxGen + 1)) / Math.log(Math.pow(Math.sqrt(2), maxGen + 1))));
        frame.repaint();
        frame.revalidate();
    }

    /**
     * Blank class instantiation
     */
    public DragonCurveFractal() {
    }

    /**
     * Sets up the Dragon Curve frame to house the fractal and buttons
     */
    public void run() {
        // sets up frame
        frame = Main.createBasicFrame();

        // content panel with buttons and title
        JPanel content = new JPanel();
        JLabel hausdorff = new JLabel();
        updateHausdorffText(hausdorff);
        content.setLayout(new GridBagLayout());
        JLabel title = new JLabel("Dragon Curve Fractal Generator");
        JButton nextGenButton = new JButton("Next Generation");
        nextGenButton.addActionListener(e -> {
            if (maxGen < 16) {
                maxGen++;
                updateHausdorffText(hausdorff);
            } else {
                JOptionPane.showMessageDialog(null, "Generation Limit Reached");
            }

        });
        JButton lastGenButton = new JButton("Last Generation");
        lastGenButton.addActionListener(e ->{
            if (maxGen > 0) {
                maxGen--;
                updateHausdorffText(hausdorff);
            }
        });
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            SwingUtilities.invokeLater(new DrawInitializer());
            maxGen = 0;
            frame.dispose();
        });

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 0;
        content.add(title,c);
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy=1;
        content.add(lastGenButton,c);
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy=1;
        content.add(backButton,c);
        c.gridwidth = 1;
        c.gridx = 2;
        c.gridy=1;
        content.add(nextGenButton,c);
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 2;
        content.add(hausdorff,c);

        JPanel returnHomePanel = Main.returnHomePanel(frame);

        // styles all panels and buttons and labels
        Main.styleAll(new JButton[]{nextGenButton, lastGenButton, backButton},
                new JPanel[]{content},
                new JLabel[]{hausdorff},
                new JLabel[]{title});

        // adds all panels to frame
        frame.add(content, BorderLayout.NORTH);
        frame.add(new DragonCurveFractal(), BorderLayout.CENTER);
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

        g2.setStroke(new BasicStroke(5));
        g2.setColor(Color.PINK);

        initializer = DrawInitializer.getInitializer();

        // sets coordinate origin to first point
        AffineTransform at = g2.getTransform();
        at.translate(getWidth() / 2f - sideLength / 2f, getHeight() / 2f);
        g2.setTransform(at);

        nextGeneration(1);
    }

    /**
     * Draws the next generation of a triangular fractal
     * Recursively runs until the generation hits the maxGen
     *
     * @param gen: the generation being drawn in that iteration
     */
    public void nextGeneration(int gen) {
        if (maxGen == 0) {
            g2.draw(initializer);
            return;
        }
        // rotate and scale
        g2.rotate(Math.PI / 4);
        g2.scale(scaleFactor, scaleFactor);

        if (gen < maxGen) {
            nextGeneration(gen + 1);
        } else {
            // draw half of next gen
            g2.draw(initializer);
        }

        // move origin
        g2.rotate(-Math.PI / 4);
        g2.scale(1 / scaleFactor, 1 / scaleFactor);
        g2.translate(sideLength, 0);
        g2.scale(scaleFactor, scaleFactor);
        g2.rotate(Math.PI / 4);

        // rotate and draw second half of next gen
        g2.rotate(Math.PI / 2);

        if (gen < maxGen) {
            nextGeneration(gen + 1);
        } else {
            g2.draw(initializer);
            n *= 2;
        }

        // move origin back to first point
        g2.rotate(-3 * Math.PI / 4);
        g2.scale(1 / scaleFactor, 1 / scaleFactor);
        g2.translate(-sideLength, 0);
    }

    /**
     * Returns the side length of the dragon curve
     * @return side length
     */
    int getSideLength() {
        return sideLength;
    }
}
