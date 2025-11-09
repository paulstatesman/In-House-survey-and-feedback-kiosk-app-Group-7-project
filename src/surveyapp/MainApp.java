package surveyapp;

import javax.swing.*;
import java.util.*;

public class MainApp {
    public static Map<String, java.util.List<Question>> surveys = new LinkedHashMap<>();
    public static Map<String, Map<String, java.util.List<String>>> responses = new LinkedHashMap<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("IN-HOUSE SURVEY & FEEDBACK SYSTEM");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 600);
            frame.setLocationRelativeTo(null);

            HomePage homePage = new HomePage(frame);
            JPanel homePanel = homePage.getPanel();
            frame.setContentPane(homePanel);
            frame.setVisible(true);
        });
    }
}
