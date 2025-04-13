import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Path2D;

public class DragonCurveFractal extends JComponent {
    static Graphics2D g2;
    static Path2D.Double initializer;
    private static JFrame frame;

    static int sideLength;
    static int maxGen = 1;

    static Button nextGenButton;
    static Button lastGenButton;

    static ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == (nextGenButton)) {
                maxGen++;
                nextGeneration(1);
                frame.repaint();
                frame.revalidate();
            }
            if (e.getSource() == (lastGenButton)) {
                maxGen--;
                nextGeneration(1);
                frame.repaint();
                frame.revalidate();
            }
        }
    };

    public DragonCurveFractal() {
    }

    public static void main(String[] args) {
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

        sideLength = 256;

        frame.add(new DragonCurveFractal());

        frame.setVisible(true);
        frame.repaint();

    }

    public void paintComponent(Graphics g) {
        g2 = (Graphics2D) g;

        // sets coordinate origin to center
        g2.setStroke(new BasicStroke(5));
        g2.setColor(Color.PINK);

        int[] xPoints;
        int[] yPoints;

        // points to create initializer
        xPoints = new int[]{0, sideLength / 2, sideLength};
        yPoints = new int[]{0, sideLength / 2, 0};

        // sets coordinate origin to first point
        g2.translate(getWidth() / 2 - sideLength / 2, getHeight() / 2);

        // stores two lines that form a 90 degree angle to manipulated to form curve
        initializer = new Path2D.Double();

        // Move to the first point
        initializer.moveTo(xPoints[0], yPoints[0]);

        // Draw lines to the remaining points
        for (int i = 1; i < xPoints.length; i++) {
            initializer.lineTo(xPoints[i], yPoints[i]);
        }

        nextGeneration(1);
    }

    public static void nextGeneration(int gen) {
        // rotate and scale
        g2.rotate(Math.PI / 4);
        g2.scale(Math.sqrt(2) / 2f, Math.sqrt(2) / 2f);

        if (gen < maxGen) {
            nextGeneration(gen + 1);
        } else {
            // draw half of next gen
            g2.draw(initializer);
        }

        // move origin
        g2.rotate(-Math.PI / 4);
        g2.scale(2 / Math.sqrt(2), 2 / Math.sqrt(2));
        g2.translate(sideLength, 0);
        g2.scale(Math.sqrt(2) / 2f, Math.sqrt(2) / 2f);
        g2.rotate(Math.PI / 4);

        // rotate and draw second half of next gen
        g2.rotate(Math.PI / 2);

        if (gen < maxGen) {
            nextGeneration(gen + 1);
        } else {
            g2.draw(initializer);
        }

        // move origin back to first point
        g2.rotate(-3 * Math.PI / 4);
        g2.scale(2 / Math.sqrt(2), 2 / Math.sqrt(2));
        g2.translate(-sideLength, 0);
    }


}
