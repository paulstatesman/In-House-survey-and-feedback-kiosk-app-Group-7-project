package surveyapp;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class UserPage {
    private JPanel panel;
    private JFrame frame;
    private JPanel homePanel;

    public UserPage(JFrame frame, JPanel homePanel){
        this.frame = frame;
        this.homePanel = homePanel;

        panel = new JPanel(new BorderLayout(10,10));
        JLabel lbl = new JLabel("User Section - Participate in Survey", JLabel.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 20));

        JTextField txtName = new JTextField(20);
        JButton btnStart = new JButton("Start Survey");
        JButton btnBack = new JButton("Back");

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Enter your name:"));
        topPanel.add(txtName);
        topPanel.add(btnStart);
        topPanel.add(btnBack);

        JPanel surveyPanel = new JPanel();
        surveyPanel.setLayout(new BoxLayout(surveyPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(surveyPanel);
        scrollPane.setVisible(false);

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
            surveyPanel.add(btnSubmit);
            scrollPane.setVisible(true);
            panel.revalidate();

            btnSubmit.addActionListener(ev -> {
                if(userAnswers.isEmpty()){ JOptionPane.showMessageDialog(panel,"Please answer questions."); return; }
                MainApp.responses.put(userName,userAnswers);
                JOptionPane.showMessageDialog(panel,"Thank you, "+userName+"! Your responses have been recorded.");
                txtName.setText("");
                surveyPanel.removeAll();
                scrollPane.setVisible(false);
                panel.revalidate();
                panel.repaint();
            });
        });

        btnBack.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.add(homePanel);
            frame.revalidate();
            frame.repaint();
        });
    }

    public JPanel getPanel(){ return panel; }
}
