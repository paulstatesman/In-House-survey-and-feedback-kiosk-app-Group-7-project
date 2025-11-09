package surveyapp;

import javax.swing.*;
import java.awt.*;
<<<<<<< HEAD
import java.util.*;
import java.util.List;
=======
import java.awt.event.*;
import java.sql.*;
import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Arrays;
>>>>>>> da4077d (Updated files along with db connection)

public class UserPage {
    private JPanel panel;
    private JFrame frame;
    private JPanel homePanel;

<<<<<<< HEAD
    public UserPage(JFrame frame, JPanel homePanel){
        this.frame = frame;
        this.homePanel = homePanel;

        panel = new JPanel(new BorderLayout(10,10));
        JLabel lbl = new JLabel("User Section - Participate in Survey", JLabel.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 20));

=======
    public UserPage(JFrame frame, JPanel homePanel) {
        this.frame = frame;
        this.homePanel = homePanel;

        panel = new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(135, 206, 250)); // Sky blue
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

        // User input panel
>>>>>>> da4077d (Updated files along with db connection)
        JTextField txtName = new JTextField(20);
        JButton btnStart = new JButton("Start Survey");
        JButton btnBack = new JButton("Back");

<<<<<<< HEAD
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Enter your name:"));
        topPanel.add(txtName);
        topPanel.add(btnStart);
        topPanel.add(btnBack);

=======
        // Fetch available surveys from DB
        JComboBox<String> surveyDropdown = new JComboBox<>();
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT title FROM surveys";
            try (PreparedStatement pst = conn.prepareStatement(query)) {
                try (ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        surveyDropdown.addItem(rs.getString("title"));
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(135, 206, 250));
        topPanel.add(new JLabel("Enter your name:"));
        topPanel.add(txtName);
        topPanel.add(new JLabel("Select Survey:"));
        topPanel.add(surveyDropdown);
        topPanel.add(btnStart);
        topPanel.add(btnBack);

        // Survey panel
>>>>>>> da4077d (Updated files along with db connection)
        JPanel surveyPanel = new JPanel();
        surveyPanel.setLayout(new BoxLayout(surveyPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(surveyPanel);
        scrollPane.setVisible(false);

<<<<<<< HEAD
        panel.add(lbl, BorderLayout.NORTH);
        panel.add(topPanel, BorderLayout.SOUTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        btnStart.addActionListener(e -> {
            String userName = txtName.getText().trim();
            if(userName.isEmpty()){ JOptionPane.showMessageDialog(panel,"Please enter your name."); return; }
            if(MainApp.surveys.isEmpty()){ JOptionPane.showMessageDialog(panel,"No surveys available."); return; }

            surveyPanel.removeAll();
            Map<String,List<String>> userAnswers = new LinkedHashMap<>();

            for(String survey: MainApp.surveys.keySet()){
                JLabel lblSurvey = new JLabel("Survey: "+survey);
                lblSurvey.setFont(new Font("Arial",Font.BOLD,16));
                surveyPanel.add(lblSurvey);

                for(Question q: MainApp.surveys.get(survey)){
                    JPanel qPanel = new JPanel();
                    qPanel.setLayout(new BoxLayout(qPanel, BoxLayout.Y_AXIS));
                    qPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    qPanel.add(new JLabel(q.text));

                    List<String> answerList = new ArrayList<>();

                    switch(q.type){
                        case "Text":
                            JTextField tf = new JTextField(30);
                            qPanel.add(tf);
                            tf.addFocusListener(new java.awt.event.FocusAdapter() {
                                public void focusLost(java.awt.event.FocusEvent ev){
                                    answerList.clear();
                                    answerList.add(tf.getText());
                                    userAnswers.put(q.text,new ArrayList<>(answerList));
                                }
                            });
                            break;
                        case "Single Choice":
                            ButtonGroup group = new ButtonGroup();
                            for(String opt: q.options){
                                JRadioButton rb = new JRadioButton(opt);
                                group.add(rb);
                                qPanel.add(rb);
                                rb.addActionListener(ev -> {
                                    answerList.clear();
                                    answerList.add(rb.getText());
                                    userAnswers.put(q.text,new ArrayList<>(answerList));
                                });
                            }
                            break;
                        case "Multiple Choice":
                            for(String opt: q.options){
                                JCheckBox cb = new JCheckBox(opt);
                                qPanel.add(cb);
                                cb.addActionListener(ev -> {
                                    answerList.clear();
                                    for(Component c: qPanel.getComponents()){
                                        if(c instanceof JCheckBox && ((JCheckBox)c).isSelected()) answerList.add(((JCheckBox)c).getText());
                                    }
                                    userAnswers.put(q.text,new ArrayList<>(answerList));
                                });
                            }
                            break;
                    }
                    surveyPanel.add(qPanel);
                    surveyPanel.add(Box.createVerticalStrut(5));
                }
                surveyPanel.add(Box.createVerticalStrut(10));
            }

            JButton btnSubmit = new JButton("Submit Survey");
=======
        panel.add(lblProjectTitle, BorderLayout.NORTH);
        panel.add(topPanel, BorderLayout.SOUTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Start survey action
        btnStart.addActionListener(e -> {
            String userName = txtName.getText().trim();
            if (userName.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please enter your name.");
                return;
            }

            String selectedSurvey = (String) surveyDropdown.getSelectedItem();
            if (selectedSurvey == null) {
                JOptionPane.showMessageDialog(panel, "No survey selected.");
                return;
            }

            surveyPanel.removeAll();
            Map<String, java.util.List<String>> userAnswers = new LinkedHashMap<>();

            JLabel lblSurvey = new JLabel("Survey: " + selectedSurvey);
            lblSurvey.setFont(new Font("Arial", Font.BOLD, 20));
            surveyPanel.add(lblSurvey);
            surveyPanel.add(Box.createVerticalStrut(10));

            try (Connection conn = DBConnection.getConnection()) {
                String qQuery = "SELECT * FROM questions WHERE survey_id=(SELECT id FROM surveys WHERE title=?)";
                try (PreparedStatement pst = conn.prepareStatement(qQuery)) {
                    pst.setString(1, selectedSurvey);
                    try (ResultSet rs = pst.executeQuery()) {
                        while (rs.next()) {
                            int questionId = rs.getInt("id");
                            String qText = rs.getString("text");
                            String qType = rs.getString("type");
                            String opts = rs.getString("options");
                            java.util.List<String> options = opts == null || opts.isEmpty() ? new ArrayList<>() : Arrays.asList(opts.split(","));

                            JPanel qPanel = new JPanel();
                            qPanel.setLayout(new BoxLayout(qPanel, BoxLayout.Y_AXIS));
                            qPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                            qPanel.add(new JLabel(qText));

                            java.util.List<String> answerList = new ArrayList<>();

                            switch (qType) {
                                case "Text":
                                    JTextField tf = new JTextField(30);
                                    qPanel.add(tf);
                                    tf.addFocusListener(new java.awt.event.FocusAdapter() {
                                        public void focusLost(java.awt.event.FocusEvent ev) {
                                            answerList.clear();
                                            answerList.add(tf.getText());
                                            userAnswers.put(qText, new ArrayList<>(answerList));
                                        }
                                    });
                                    break;

                                case "Single Choice":
                                    ButtonGroup group = new ButtonGroup();
                                    for (String opt : options) {
                                        JRadioButton rb = new JRadioButton(opt);
                                        group.add(rb);
                                        qPanel.add(rb);
                                        rb.addActionListener(ev2 -> {
                                            answerList.clear();
                                            answerList.add(rb.getText());
                                            userAnswers.put(qText, new ArrayList<>(answerList));
                                        });
                                    }
                                    break;

                                case "Multiple Choice":
                                    for (String opt : options) {
                                        JCheckBox cb = new JCheckBox(opt);
                                        qPanel.add(cb);
                                        cb.addActionListener(ev3 -> {
                                            answerList.clear();
                                            for (Component c : qPanel.getComponents()) {
                                                if (c instanceof JCheckBox && ((JCheckBox) c).isSelected())
                                                    answerList.add(((JCheckBox) c).getText());
                                            }
                                            userAnswers.put(qText, new ArrayList<>(answerList));
                                        });
                                    }
                                    break;
                            }

                            surveyPanel.add(qPanel);
                            surveyPanel.add(Box.createVerticalStrut(5));
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            JButton btnSubmit = new JButton("Submit Survey");
            surveyPanel.add(Box.createVerticalStrut(10));
>>>>>>> da4077d (Updated files along with db connection)
            surveyPanel.add(btnSubmit);
            scrollPane.setVisible(true);
            panel.revalidate();

            btnSubmit.addActionListener(ev -> {
<<<<<<< HEAD
                if(userAnswers.isEmpty()){ JOptionPane.showMessageDialog(panel,"Please answer questions."); return; }
                MainApp.responses.put(userName,userAnswers);
                JOptionPane.showMessageDialog(panel,"Thank you, "+userName+"! Your responses have been recorded.");
=======
                if (userAnswers.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Please answer questions.");
                    return;
                }

                // Save responses in DB
                try (Connection conn = DBConnection.getConnection()) {
                    for (Map.Entry<String, java.util.List<String>> entry : userAnswers.entrySet()) {
                        String qText = entry.getKey();
                        java.util.List<String> ansList = entry.getValue();
                        String insertResp = "INSERT INTO responses(user_name, survey_id, question_id, answer) " +
                                "VALUES(?, (SELECT id FROM surveys WHERE title=?), " +
                                "(SELECT id FROM questions WHERE text=? AND survey_id=(SELECT id FROM surveys WHERE title=?)), ?)";
                        try (PreparedStatement pst = conn.prepareStatement(insertResp)) {
                            for (String ans : ansList) {
                                pst.setString(1, userName);
                                pst.setString(2, selectedSurvey);
                                pst.setString(3, qText);
                                pst.setString(4, selectedSurvey);
                                pst.setString(5, ans);
                                pst.executeUpdate();
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                JOptionPane.showMessageDialog(panel, "Thank you, " + userName + "! Your responses have been recorded.");
>>>>>>> da4077d (Updated files along with db connection)
                txtName.setText("");
                surveyPanel.removeAll();
                scrollPane.setVisible(false);
                panel.revalidate();
                panel.repaint();
            });
        });

<<<<<<< HEAD
        btnBack.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.add(homePanel);
=======
        // Back button
        btnBack.addActionListener(e -> {
            frame.setContentPane(homePanel);
>>>>>>> da4077d (Updated files along with db connection)
            frame.revalidate();
            frame.repaint();
        });
    }

<<<<<<< HEAD
    public JPanel getPanel(){ return panel; }
=======
    public JPanel getPanel() {
        return panel;
    }
>>>>>>> da4077d (Updated files along with db connection)
}
