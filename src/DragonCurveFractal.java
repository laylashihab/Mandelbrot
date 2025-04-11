import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

public class DragonCurveFractal extends JComponent {
    Graphics2D g2;
    Path2D.Double initializer;
    private static JFrame frame;

    int width = 100;
    int maxGen = 2;

    public DragonCurveFractal() {
    }

    public static void main(String[] args) {
        // sets up frame
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null); //centers frame on screen

        frame.add(new DragonCurveFractal());

        frame.setVisible(true);
        frame.repaint();

    }

    public void paintComponent(Graphics g) {
        g2 = (Graphics2D) g;

        // sets coordinate origin to center
        g2.setStroke(new BasicStroke(2));
        g2.setColor(Color.PINK);

        int centerX = getWidth()/2;
        int centerY = getHeight()/2;

        // Draw first line
        g2.drawLine(centerX-(width/2), centerY, centerX+(width/2), centerY);
    }

    public void drawNext(int gen){

    }


}
