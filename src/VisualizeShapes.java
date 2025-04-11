import javax.swing.*;
public class VisualizeShapes {
    private static JFrame frame;
    public static void main(String[] args) {
        // sets up frame
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null); //centers frame on screen

        frame.add(new TriangularFractal());

        frame.setVisible(true);

    }

    public int[] findCenter() {
        int x = frame.getWidth() / 2;
        int y = frame.getHeight() / 2;
        return new int[]{x, y};
    }
}
