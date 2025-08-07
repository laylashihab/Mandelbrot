import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Class to run Mandelbrot Fractal Maker Application
 * Contains constants and methods to create a uniformly stylized UI
 *
 * @author Layla Shihab
 * @version 8/5/25
 */

public class Main implements Runnable {

    // UI constants
    public static final Color buttonColor = Color.decode("#87aec2");
    public static final Color backgroundColor = Color.decode("#c7e4de");
    public static final Dimension buttonSize = new Dimension(100,30);
    public static final Font labelFont = new Font("Arial",Font.BOLD,20);
    public static final Font titleFont = new Font("Arial",Font.BOLD,30);

    public static void styleAll(JButton[] buttons, JPanel[] panels, JLabel[] labels, JLabel[] titles){
        styleButtons(buttons);
        stylePanels(panels);
        styleLabels(labels);
        styleTitles(titles);
    }

    public static void styleButtons(JButton[] buttons){
        for (JButton button : buttons) {
            button.setBackground(buttonColor);
            button.setSize(buttonSize);
            button.setFont(labelFont);
        }
    }

    public static void stylePanels(JPanel[] panels){
        for (JPanel panel : panels) {
            panel.setBackground(backgroundColor);
        }
    }

    public static void styleLabels(JLabel[] labels){
        for (JLabel label : labels) {
            label.setBackground(backgroundColor);
            label.setFont(labelFont);
            label.setHorizontalAlignment(SwingConstants.CENTER);
        }
    }

    public static void styleTitles(JLabel[] titles){
        for (JLabel title : titles) {
            title.setBackground(backgroundColor);
            title.setFont(titleFont);
            title.setHorizontalAlignment(SwingConstants.CENTER);
        }
    }

    public static JFrame createBasicFrame(){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null); //centers frame on screen
        frame.setBackground(backgroundColor);
        return frame;
    }

    public static JPanel returnHomePanel(JFrame frame){
        JPanel panel = new JPanel();
        JButton homeButton = new JButton("Return to Home Page");
        homeButton.addActionListener(e -> {
            SwingUtilities.invokeLater(new Main());
            frame.dispose();
        });

        styleButtons(new JButton[]{homeButton});
        stylePanels(new JPanel[]{panel});

        panel.add(homeButton);
        return panel;
    }

    public void run(){
        // sets up main frame
        JFrame frame = createBasicFrame();

        // sets up welcome content
        JPanel introPanel = new JPanel();
        introPanel.setLayout(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome!");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel instructionLabel = new JLabel("Select a Fractal Type.");
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        introPanel.add(welcomeLabel, BorderLayout.NORTH);
        introPanel.add(instructionLabel, BorderLayout.CENTER);

        // sets up buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        JButton triangularFractalButton = new JButton("Triangular Fractal");
        JButton sierpinskiTriangleButton = new JButton("Sierpinski Triangle Fractal");
        JButton dragonCurveFractalButton = new JButton("Dragon Curve Fractal");

        // sets up ActionListeners for buttons
        triangularFractalButton.addActionListener(e -> {
            SwingUtilities.invokeLater(new TriangularFractal());
            frame.dispose();
        });

        sierpinskiTriangleButton.addActionListener(e -> {
            SwingUtilities.invokeLater(new SierpinskiTriangle());
            frame.dispose();
        });

        dragonCurveFractalButton.addActionListener(e -> {
            SwingUtilities.invokeLater(new DrawInitializer());
            frame.dispose();
        });

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(10,10,10,10); // padding between elements
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.gridheight = 1;
        buttonPanel.add(triangularFractalButton,c);
        c.gridy = 1;
        buttonPanel.add(sierpinskiTriangleButton,c);
        c.gridy = 2;
        buttonPanel.add(dragonCurveFractalButton,c);

        // adds panels
        frame.add(introPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);

        // color scheming things
        styleAll(new JButton[] {sierpinskiTriangleButton,dragonCurveFractalButton,triangularFractalButton},
                new JPanel[] {introPanel,buttonPanel},
                new JLabel[] {instructionLabel},
                new JLabel[] {welcomeLabel});

        // displays the frame
        frame.setVisible(true);
        frame.repaint();
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Main());
    }
}
