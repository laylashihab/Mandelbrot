/**
 * Java class to produce a dragon curve fractal
 * Sets up JFrame Window to visualize the fractal and allows user to step through diff generations
 *
 * @version May 13, 2025
 * @author Layla Shihab
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

public class DragonCurveFractal extends JComponent implements Runnable {
    Graphics2D g2;
    Path2D initializer;
    private JFrame frame;

    int sideLength = 256;
    static int maxGen;
    double scaleFactor = Math.sqrt(2) / 2;

    JLabel hausdorff;

    int n = 2; // number of self-similar objects

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            if (button.getText().equals("Next Generation")) {
                if (maxGen < 16) {
                    maxGen++;
                    hausdorff.setText(String.format("Hausdorff Dimension: log %.2f / log %.2f = %.4f",
                            Math.pow(2, maxGen + 1), Math.pow(Math.sqrt(2), maxGen + 1),
                            Math.log(Math.pow(2, maxGen + 1)) / Math.log(Math.pow(Math.sqrt(2), maxGen + 1))));
                    frame.repaint();
                    frame.revalidate();
                } else {
                    JOptionPane.showMessageDialog(null, "Generation Limit Reached");
                }
            }
            if (button.getText().equals("Last Generation")) {
                if (maxGen > 0) {
                    maxGen--;
                    hausdorff.setText(String.format("Hausdorff Dimension: log %.2f / log %.2f = %.4f",
                            Math.pow(2, maxGen + 1), Math.pow(Math.sqrt(2), maxGen + 1),
                            Math.log(Math.pow(2, maxGen + 1)) / Math.log(Math.pow(Math.sqrt(2), maxGen + 1))));
                    frame.repaint();
                    frame.revalidate();
                }
            }
            if (button.getText().equals("Back To 'Create Initializer'")) {
                SwingUtilities.invokeLater(new DrawInitializer());
                frame.dispose();
            }
        }
    };

    public DragonCurveFractal() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new DrawInitializer());
    }

    public void run() {
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
        JButton backButton = new JButton("Back To 'Create Initializer'");
        backButton.addActionListener(actionListener);
        content.add(nextGenButton);
        content.add(lastGenButton);
        content.add(backButton);
        frame.add(content, BorderLayout.NORTH);

        JPanel info = new JPanel();
        hausdorff = new JLabel();
        hausdorff.setFont(new Font("Courier", Font.PLAIN, 20));
        info.add(hausdorff);
        frame.add(info, BorderLayout.SOUTH);

        frame.add(new DragonCurveFractal());

        frame.setVisible(true);
        frame.repaint();
    }

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

    int getSideLength() {
        return sideLength;
    }
}
