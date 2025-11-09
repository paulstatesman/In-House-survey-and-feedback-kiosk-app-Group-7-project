package surveyapp;
<<<<<<< HEAD
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
=======
>>>>>>> da4077d (Updated files along with db connection)

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
<<<<<<< HEAD
import java.util.*;
=======
import java.sql.*;
>>>>>>> da4077d (Updated files along with db connection)

public class ReportPage {
    private JPanel panel;
    private JFrame frame;
    private JPanel homePanel;

<<<<<<< HEAD
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
=======
    public ReportPage(JFrame frame, JPanel homePanel) {
        this.frame = frame;
        this.homePanel = homePanel;

        panel = new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(135, 206, 250)); // sky blue
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        // Project Title
        JLabel lblProjectTitle = new JLabel("IN-HOUSE SURVEY & FEEDBACK SYSTEM", JLabel.CENTER);
        lblProjectTitle.setFont(new Font("Verdana", Font.BOLD, 24));
        lblProjectTitle.setForeground(Color.WHITE);
        lblProjectTitle.setOpaque(true);
        lblProjectTitle.setBackground(new Color(70, 130, 180));
        lblProjectTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Table setup
        String[] columns = {"User Name", "Survey", "Question", "Answer"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Buttons
        JButton btnRefresh = new JButton("Refresh Reports");
        JButton btnBack = new JButton("Back");

        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(135, 206, 250));
        topPanel.add(btnRefresh);
        topPanel.add(btnBack);

        // Add components
        panel.add(lblProjectTitle, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(topPanel, BorderLayout.SOUTH);

        // Load reports
        refreshTable(tableModel);

        // Button actions
        btnRefresh.addActionListener(e -> refreshTable(tableModel));

        btnBack.addActionListener(e -> {
            frame.setContentPane(homePanel);
            frame.revalidate();
            frame.repaint();
        });
    }

    public JPanel getPanel() {
        return panel;
    }

    private void refreshTable(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT r.user_name, s.title, q.text, r.answer " +
                    "FROM responses r " +
                    "JOIN surveys s ON r.survey_id = s.id " +
                    "JOIN questions q ON r.question_id = q.id";
            try (PreparedStatement pst = conn.prepareStatement(query)) {
                try (ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        tableModel.addRow(new Object[]{
                                rs.getString("user_name"),
                                rs.getString("title"),
                                rs.getString("text"),
                                rs.getString("answer")
                        });
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
>>>>>>> da4077d (Updated files along with db connection)
}
