import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

public class DragonCurveFractal extends JComponent implements Runnable {
    static Graphics2D g2;
    static Path2D initializer;
    private JFrame frame;

    DrawInitializer drawInitializer;

    static int sideLength;
    static int maxGen = 0;
    static double scaleFactor;

    int[] xPoints;
    int[] yPoints;

    Button nextGenButton;
    Button lastGenButton;
    JLabel hausdorff;

    int n = 2; // number of self-similar objects

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == (nextGenButton)) {
                if (maxGen < 16) {
                    maxGen++;
                    hausdorff.setText(String.format("Hausdorff Dimension: log %.2f / log %.2f = %.4f",
                            Math.pow(2, maxGen+1), Math.pow(Math.sqrt(2),maxGen+1),
                            Math.log(Math.pow(2, maxGen+1))/Math.log(Math.pow(Math.sqrt(2),maxGen+1))));
                    frame.repaint();
                    frame.revalidate();
                } else {
                    JOptionPane.showMessageDialog(null, "Generation Limit Reached");
                }
            }
            if (e.getSource() == (lastGenButton)) {
                if (maxGen > 0) {
                    maxGen--;
                    hausdorff.setText(String.format("Hausdorff Dimension: log %.2f / log %.2f = %.4f",
                            Math.pow(2, maxGen+1), Math.pow(Math.sqrt(2),maxGen+1),
                            Math.log(Math.pow(2, maxGen+1))/Math.log(Math.pow(Math.sqrt(2),maxGen+1))));
                    frame.repaint();
                    frame.revalidate();
                }
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
        nextGenButton = new Button("Next Generation");
        nextGenButton.addActionListener(actionListener);
        lastGenButton = new Button("Last Generation");
        lastGenButton.addActionListener(actionListener);
        content.add(nextGenButton);
        content.add(lastGenButton);
        frame.add(content, BorderLayout.NORTH);

        JPanel info = new JPanel();
        hausdorff = new JLabel();
        hausdorff.setFont(new Font("Courier", Font.PLAIN, 20));
        info.add(hausdorff);
        frame.add(info, BorderLayout.SOUTH);

        //sideLength = 256;
        sideLength = DrawInitializer.getSideLength();
        scaleFactor = DrawInitializer.getScaleFactor();

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
        at.translate( getWidth()/2f - sideLength/2f, getHeight()/2f);
        g2.setTransform(at);
        g2.drawLine(0,0,0,0);


        //initializer.transform(at);
        //initializer.transform(AffineTransform.getTranslateInstance(getWidth()/2f - sideLength/2f, getHeight()/2f));

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
        g2.scale(1/scaleFactor, 1/scaleFactor);
        g2.translate(sideLength, 0);
        g2.scale(scaleFactor, scaleFactor);
        g2.rotate(Math.PI / 4);

        // rotate and draw second half of next gen
        g2.rotate(Math.PI / 2);

        if (gen < maxGen) {
            nextGeneration(gen + 1);
        } else {
            g2.draw(initializer);
            n *=2;
        }

        // move origin back to first point
        g2.rotate(-3 * Math.PI / 4);
        g2.scale(1/scaleFactor, 1/scaleFactor);
        g2.translate(-sideLength, 0);
    }
}
