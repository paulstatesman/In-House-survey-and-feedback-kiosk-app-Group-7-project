package surveyapp;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;

public class ReportPage {
    private JPanel panel;
    private JFrame frame;
    private JPanel homePanel;

    public ReportPage(JFrame frame, JPanel homePanel){
        this.frame = frame;
        this.homePanel = homePanel;

        panel = new JPanel(new BorderLayout(10,10));
        JLabel lbl = new JLabel("Survey Reports", JLabel.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 20));

        panel.add(lbl, BorderLayout.NORTH);

        // Dropdown to select survey
        JComboBox<String> surveyDropdown = new JComboBox<>(MainApp.surveys.keySet().toArray(new String[0]));
        JButton btnGenerate = new JButton("Generate Report");

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Select Survey:"));
        topPanel.add(surveyDropdown);
        topPanel.add(btnGenerate);

        panel.add(topPanel, BorderLayout.CENTER);

        // Table for report
        String[] columns = {"Question", "Answer", "Count"};
        DefaultTableModel tableModel = new DefaultTableModel(columns,0);
        JTable reportTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(reportTable);
        panel.add(scrollPane, BorderLayout.SOUTH);

        // Back button
        JButton btnBack = new JButton("Back");
        topPanel.add(btnBack);

        btnBack.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.add(homePanel);
            frame.revalidate();
            frame.repaint();
        });

        // Generate report
        btnGenerate.addActionListener(e -> {
            String surveyName = (String)surveyDropdown.getSelectedItem();
            if(surveyName == null) return;
            tableModel.setRowCount(0);

            Map<String, List<Question>> surveyQuestions = MainApp.surveys;
            List<Question> questions = surveyQuestions.get(surveyName);

            Map<String, Map<String, Integer>> summary = new LinkedHashMap<>();

            // Count responses
            for(Question q : questions){
                Map<String,Integer> ansCount = new LinkedHashMap<>();
                for(Map<String, List<String>> userResp : MainApp.responses.values()){
                    if(userResp.containsKey(q.text)){
                        for(String ans : userResp.get(q.text)){
                            ansCount.put(ans, ansCount.getOrDefault(ans,0)+1);
                        }
                    }
                }
                summary.put(q.text, ansCount);
            }

            // Populate table
            for(String question : summary.keySet()){
                Map<String,Integer> ansMap = summary.get(question);
                for(String answer : ansMap.keySet()){
                    tableModel.addRow(new Object[]{question, answer, ansMap.get(answer)});
                }
            }
        });
    }

    public JPanel getPanel(){ return panel; }
}
