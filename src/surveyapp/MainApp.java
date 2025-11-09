package surveyapp;

import javax.swing.*;
import java.util.*;

public class MainApp {
    // Shared data across pages
    public static final Map<String, List<Question>> surveys = new LinkedHashMap<>();
    public static final Map<String, Map<String, List<String>>> responses = new LinkedHashMap<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Survey & Feedback Kiosk");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 600);

            HomePage homePage = new HomePage(frame);
            frame.add(homePage.getPanel());

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
