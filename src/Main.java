import javax.swing.*;
import java.awt.*;

/**
 * Class to run Mandelbrot Fractal Generator Application
 * Contains constants and methods to create a uniformly stylized UI
 *
 * @author Layla Shihab
 * @version August 5 2025
 */
public class Main implements Runnable {

    // UI constants
    public static final Color buttonColor = Color.decode("#87aec2");
    public static final Color backgroundColor = Color.decode("#c7e4de");
    public static final Dimension buttonSize = new Dimension(100,30);
    public static final Font labelFont = new Font("Arial",Font.BOLD,20);
    public static final Font titleFont = new Font("Arial",Font.BOLD,30);

    /**
     * Uniformly styles all buttons, panels, labels, and titles passed in
     *
     * @param buttons: array of JButtons to style
     * @param panels: array of JPanels to style
     * @param labels: array of JLabels to style as labels (not titles)
     * @param titles: array of JLabels to style as titles
     */
    public static void styleAll(JButton[] buttons, JPanel[] panels, JLabel[] labels, JLabel[] titles){
        styleButtons(buttons);
        stylePanels(panels);
        styleLabels(labels);
        styleTitles(titles);
    }

    /**
     * Styles buttons to have a uniform color, size, and font
     *
     * @param buttons: array of JButtons to style
     */
    public static void styleButtons(JButton[] buttons){
        for (JButton button : buttons) {
            button.setBackground(buttonColor);
            button.setSize(buttonSize);
            button.setFont(labelFont);
        }
    }

    /**
     * Styles panels to have a uniform color
     *
     * @param panels: array of JPanels to style
     */
    public static void stylePanels(JPanel[] panels){
        for (JPanel panel : panels) {
            panel.setBackground(backgroundColor);
        }
    }

    /**
     * Styles labels to have a uniform background color, font, and alignment
     *
     * @param labels: array of JLabels to style
     */
    public static void styleLabels(JLabel[] labels){
        for (JLabel label : labels) {
            label.setBackground(backgroundColor);
            label.setFont(labelFont);
            label.setHorizontalAlignment(SwingConstants.CENTER);
        }
    }

    /**
     * Styles title labels to have a uniform background color, font, and alignment
     *
     * @param titles: array of JLabels to style
     */
    public static void styleTitles(JLabel[] titles){
        for (JLabel title : titles) {
            title.setBackground(backgroundColor);
            title.setFont(titleFont);
            title.setHorizontalAlignment(SwingConstants.CENTER);
        }
    }

    /**
     * Creates a frame and sets up initialing parameters
     *
     * @return JFrame: newly created frame
     */
    public static JFrame createBasicFrame(){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null); //centers frame on screen
        frame.setBackground(backgroundColor);
        return frame;
    }

    /**
     * Creates a panel with a button to navigates back to the home page.
     *
     * @param frame: frame that houses the new panel so the actionListener correctly disposes of the frame
     * @return JPanel: the newly created panel with a return home button
     */
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

    /**
     * Creates the Home frame that houses the fractal options and title
     * Runs the program
     */
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

    /**
     * Runs the program
     *
     * @param args
     */
    public static void main(String[] args){
        SwingUtilities.invokeLater(new Main());
    }
}
