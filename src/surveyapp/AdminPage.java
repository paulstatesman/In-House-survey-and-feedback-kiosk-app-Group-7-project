package surveyapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
<<<<<<< HEAD
import java.awt.*;
import java.util.*;
import java.util.List;
=======
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
>>>>>>> da4077d (Updated files along with db connection)

public class AdminPage {
    private JPanel panel;
    private JFrame frame;
    private JPanel homePanel;

    public AdminPage(JFrame frame, JPanel homePanel){
        this.frame = frame;
        this.homePanel = homePanel;

        panel = new JPanel(new BorderLayout(10,10));
<<<<<<< HEAD
        JLabel lbl = new JLabel("Admin Section", JLabel.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 20));
=======
        panel.setBackground(new Color(135, 206, 235)); // sky blue

        JLabel lbl = new JLabel("IN-HOUSE SURVEY & FEEDBACK SYSTEM - Admin Section", JLabel.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 24));
        lbl.setForeground(Color.WHITE);
        lbl.setOpaque(true);
        lbl.setBackground(new Color(70,130,180));
        lbl.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
>>>>>>> da4077d (Updated files along with db connection)

        // Table setup
        String[] columns = {"User Name", "Survey", "Question", "Answer"};
        DefaultTableModel tableModel = new DefaultTableModel(columns,0);
        JTable responseTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(responseTable);

        // Buttons
        JButton btnCreateSurvey = new JButton("Create Survey");
        JButton btnEditSurvey = new JButton("Edit Survey");
        JButton btnRefresh = new JButton("Refresh Table");
        JButton btnDeleteResponse = new JButton("Delete Response");
        JButton btnSyncOffline = new JButton("Sync Offline Responses");
        JButton btnBack = new JButton("Back");
<<<<<<< HEAD
        JButton btnReports = new JButton("View Reports"); // create the button

        // Top panel
        JPanel topPanel = new JPanel();
=======
        JButton btnReports = new JButton("View Reports");

        // Top panel
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(135, 206, 235));
>>>>>>> da4077d (Updated files along with db connection)
        topPanel.add(btnCreateSurvey);
        topPanel.add(btnEditSurvey);
        topPanel.add(btnRefresh);
        topPanel.add(btnDeleteResponse);
        topPanel.add(btnSyncOffline);
<<<<<<< HEAD
        topPanel.add(btnBack);
        topPanel.add(btnReports); // add Reports button AFTER topPanel is created

        // Add components to main panel
=======
        topPanel.add(btnReports);
        topPanel.add(btnBack);

        // Add components
>>>>>>> da4077d (Updated files along with db connection)
        panel.add(lbl, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(topPanel, BorderLayout.SOUTH);

        // ================= Button Actions =================
        btnCreateSurvey.addActionListener(e -> openSurveyCreationDialog(panel));
        btnEditSurvey.addActionListener(e -> editSurveyDialog(panel));
        btnRefresh.addActionListener(e -> refreshTable(tableModel));
<<<<<<< HEAD
        btnDeleteResponse.addActionListener(e -> {
            int row = responseTable.getSelectedRow();
            if(row>=0){
                String user = (String)tableModel.getValueAt(row,0);
                String question = (String)tableModel.getValueAt(row,2);
                if(MainApp.responses.containsKey(user)){
                    MainApp.responses.get(user).remove(question);
                    if(MainApp.responses.get(user).isEmpty()) MainApp.responses.remove(user);
                    refreshTable(tableModel);
                    JOptionPane.showMessageDialog(panel,"Response deleted successfully.");
                }
            } else JOptionPane.showMessageDialog(panel,"Select a response to delete.");
        });

=======
        btnDeleteResponse.addActionListener(e -> deleteResponse(responseTable, tableModel));
>>>>>>> da4077d (Updated files along with db connection)
        btnSyncOffline.addActionListener(e -> {
            OfflineQueue.syncResponses();
            refreshTable(tableModel);
            JOptionPane.showMessageDialog(panel,"Offline responses synced successfully!");
        });
<<<<<<< HEAD

=======
>>>>>>> da4077d (Updated files along with db connection)
        btnBack.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.add(homePanel);
            frame.revalidate();
            frame.repaint();
        });
<<<<<<< HEAD

        btnReports.addActionListener(e -> {
            ReportPage reportPage = new ReportPage(frame, homePanel);
=======
        btnReports.addActionListener(e -> {
            ReportPage reportPage = new ReportPage(frame, panel);
>>>>>>> da4077d (Updated files along with db connection)
            frame.getContentPane().removeAll();
            frame.add(reportPage.getPanel());
            frame.revalidate();
            frame.repaint();
        });

        refreshTable(tableModel);
    }

    public JPanel getPanel(){ return panel; }

    // ================= Create Survey =================
    private void openSurveyCreationDialog(JPanel parentPanel){
        String surveyTitle = JOptionPane.showInputDialog(parentPanel,"Enter Survey Title:");
        if(surveyTitle==null || surveyTitle.isEmpty()) return;

<<<<<<< HEAD
        List<Question> questionList = new ArrayList<>();
=======
        java.util.List<Question> questionList = new java.util.ArrayList<>();
>>>>>>> da4077d (Updated files along with db connection)
        while(true){
            String qText = JOptionPane.showInputDialog(parentPanel,"Enter question text (Cancel to stop):");
            if(qText==null || qText.isEmpty()) break;

            String[] types = {"Text","Single Choice","Multiple Choice"};
            String qType = (String)JOptionPane.showInputDialog(parentPanel,"Select answer type:","Question Type",JOptionPane.QUESTION_MESSAGE,null,types,types[0]);

<<<<<<< HEAD
            List<String> options = new ArrayList<>();
=======
            java.util.List<String> options = new java.util.ArrayList<>();
>>>>>>> da4077d (Updated files along with db connection)
            if(qType.equals("Single Choice") || qType.equals("Multiple Choice")){
                String opts = JOptionPane.showInputDialog(parentPanel,"Enter options (comma separated):");
                if(opts!=null && !opts.isEmpty()) options = Arrays.asList(opts.split("\\s*,\\s*"));
                else { JOptionPane.showMessageDialog(parentPanel,"Options required!"); continue; }
            }

            questionList.add(new Question(qText,qType,options));
        }

        if(!questionList.isEmpty()){
<<<<<<< HEAD
            MainApp.surveys.put(surveyTitle,questionList);
            JOptionPane.showMessageDialog(parentPanel,"Survey \""+surveyTitle+"\" created successfully!");
=======
            try(Connection conn = DBConnection.getConnection()){
                String insertSurvey = "INSERT INTO surveys(title) VALUES(?)";
                int surveyId;
                try(PreparedStatement pst = conn.prepareStatement(insertSurvey, Statement.RETURN_GENERATED_KEYS)){
                    pst.setString(1,surveyTitle);
                    pst.executeUpdate();
                    try(ResultSet rs = pst.getGeneratedKeys()){
                        rs.next();
                        surveyId = rs.getInt(1);
                    }
                }

                for(Question q: questionList){
                    String insertQ = "INSERT INTO questions(survey_id,text,type,options) VALUES(?,?,?,?)";
                    try(PreparedStatement pst = conn.prepareStatement(insertQ)){
                        pst.setInt(1,surveyId);
                        pst.setString(2,q.text);
                        pst.setString(3,q.type);
                        pst.setString(4, String.join(",",q.options));
                        pst.executeUpdate();
                    }
                }

                JOptionPane.showMessageDialog(parentPanel,"Survey \""+surveyTitle+"\" created successfully!");
            } catch(Exception e){ e.printStackTrace(); }
>>>>>>> da4077d (Updated files along with db connection)
        }
    }

    // ================= Edit Survey =================
    private void editSurveyDialog(JPanel parentPanel){
<<<<<<< HEAD
        if(MainApp.surveys.isEmpty()){ JOptionPane.showMessageDialog(parentPanel,"No surveys to edit."); return; }

        String[] surveyArray = MainApp.surveys.keySet().toArray(new String[0]);
        String selectedSurvey = (String)JOptionPane.showInputDialog(parentPanel,"Select Survey to Edit:","Edit Survey",JOptionPane.QUESTION_MESSAGE,null,surveyArray,surveyArray[0]);
        if(selectedSurvey==null) return;

        List<Question> questionList = MainApp.surveys.get(selectedSurvey);
        String[] questionTexts = questionList.stream().map(q->q.text).toArray(String[]::new);
        String selectedQuestionText = (String)JOptionPane.showInputDialog(parentPanel,"Select Question to Edit:","Edit Question",JOptionPane.QUESTION_MESSAGE,null,questionTexts,questionTexts[0]);
        if(selectedQuestionText==null) return;

        Question qToEdit = questionList.stream().filter(q->q.text.equals(selectedQuestionText)).findFirst().orElse(null);
        if(qToEdit==null) return;

        String newQuestionText = JOptionPane.showInputDialog(parentPanel,"Edit Question Text:",qToEdit.text);
        if(newQuestionText==null || newQuestionText.isEmpty()) return;

        String[] types = {"Text","Single Choice","Multiple Choice"};
        String newType = (String)JOptionPane.showInputDialog(parentPanel,"Select Question Type:","Question Type",JOptionPane.QUESTION_MESSAGE,null,types,qToEdit.type);

        List<String> newOptions = new ArrayList<>();
        if(newType.equals("Single Choice")||newType.equals("Multiple Choice")){
            String opts = JOptionPane.showInputDialog(parentPanel,"Edit Options (comma separated):",String.join(", ",qToEdit.options));
            if(opts!=null && !opts.isEmpty()) newOptions = Arrays.asList(opts.split("\\s*,\\s*"));
            else { JOptionPane.showMessageDialog(parentPanel,"Options required!"); return; }
        }

        qToEdit.text = newQuestionText;
        qToEdit.type = newType;
        qToEdit.options = newOptions;

        JOptionPane.showMessageDialog(parentPanel,"Question updated successfully!");
=======
        try(Connection conn = DBConnection.getConnection()){
            // Load surveys
            java.util.List<String> surveyTitles = new java.util.ArrayList<>();
            try(PreparedStatement pst = conn.prepareStatement("SELECT title FROM surveys")){
                try(ResultSet rs = pst.executeQuery()){
                    while(rs.next()) surveyTitles.add(rs.getString("title"));
                }
            }

            if(surveyTitles.isEmpty()){ JOptionPane.showMessageDialog(parentPanel,"No surveys to edit."); return; }

            String selectedSurvey = (String)JOptionPane.showInputDialog(parentPanel,"Select Survey to Edit:", "Edit Survey", JOptionPane.QUESTION_MESSAGE,null,surveyTitles.toArray(),surveyTitles.get(0));
            if(selectedSurvey==null) return;

            // Load questions
            java.util.List<Question> questionList = new java.util.ArrayList<>();
            int surveyId = 0;
            try(PreparedStatement pst = conn.prepareStatement("SELECT id FROM surveys WHERE title=?")){
                pst.setString(1,selectedSurvey);
                try(ResultSet rs = pst.executeQuery()){
                    if(rs.next()) surveyId = rs.getInt("id");
                }
            }
            try(PreparedStatement pst = conn.prepareStatement("SELECT * FROM questions WHERE survey_id=?")){
                pst.setInt(1,surveyId);
                try(ResultSet rs = pst.executeQuery()){
                    while(rs.next()){
                        String text = rs.getString("text");
                        String type = rs.getString("type");
                        String opts = rs.getString("options");
                        java.util.List<String> options = opts==null||opts.isEmpty()?new java.util.ArrayList<>():Arrays.asList(opts.split(","));
                        questionList.add(new Question(text,type,options));
                    }
                }
            }

            // Select question to edit
            String[] qTexts = questionList.stream().map(q->q.text).toArray(String[]::new);
            String selectedQuestion = (String)JOptionPane.showInputDialog(parentPanel,"Select Question to Edit:","Edit Question",JOptionPane.QUESTION_MESSAGE,null,qTexts,qTexts[0]);
            if(selectedQuestion==null) return;

            Question qToEdit = questionList.stream().filter(q->q.text.equals(selectedQuestion)).findFirst().orElse(null);
            if(qToEdit==null) return;

            String newText = JOptionPane.showInputDialog(parentPanel,"Edit Question Text:", qToEdit.text);
            if(newText==null || newText.isEmpty()) return;

            String[] types = {"Text","Single Choice","Multiple Choice"};
            String newType = (String)JOptionPane.showInputDialog(parentPanel,"Select Question Type:","Question Type",JOptionPane.QUESTION_MESSAGE,null,types,qToEdit.type);

            java.util.List<String> newOptions = new java.util.ArrayList<>();
            if(newType.equals("Single Choice")||newType.equals("Multiple Choice")){
                String opts = JOptionPane.showInputDialog(parentPanel,"Edit Options (comma separated):", String.join(",",qToEdit.options));
                if(opts!=null && !opts.isEmpty()) newOptions = Arrays.asList(opts.split("\\s*,\\s*"));
                else { JOptionPane.showMessageDialog(parentPanel,"Options required!"); return; }
            }

            // Update in DB
            String updateQ = "UPDATE questions SET text=?, type=?, options=? WHERE text=? AND survey_id=?";
            try(PreparedStatement pst = conn.prepareStatement(updateQ)){
                pst.setString(1,newText);
                pst.setString(2,newType);
                pst.setString(3,String.join(",",newOptions));
                pst.setString(4,qToEdit.text);
                pst.setInt(5,surveyId);
                pst.executeUpdate();
            }

            JOptionPane.showMessageDialog(parentPanel,"Question updated successfully!");
        } catch(Exception e){ e.printStackTrace(); }
>>>>>>> da4077d (Updated files along with db connection)
    }

    // ================= Refresh Table =================
    private void refreshTable(DefaultTableModel tableModel){
        tableModel.setRowCount(0);
<<<<<<< HEAD
        for(Map.Entry<String, Map<String,List<String>>> userEntry: MainApp.responses.entrySet()){
            String user = userEntry.getKey();
            for(Map.Entry<String,List<String>> ansEntry: userEntry.getValue().entrySet()){
                String question = ansEntry.getKey();
                String answer = String.join(", ",ansEntry.getValue());
                String surveyTitle = "";
                for(Map.Entry<String,List<Question>> surveyEntry: MainApp.surveys.entrySet()){
                    for(Question q: surveyEntry.getValue()){
                        if(q.text.equals(question)){ surveyTitle = surveyEntry.getKey(); break; }
                    }
                }
                tableModel.addRow(new Object[]{user,surveyTitle,question,answer});
            }
        }
=======
        try(Connection conn = DBConnection.getConnection()){
            String query = "SELECT r.user_name, s.title, q.text, r.answer " +
                    "FROM responses r " +
                    "JOIN surveys s ON r.survey_id = s.id " +
                    "JOIN questions q ON r.question_id = q.id";
            try(PreparedStatement pst = conn.prepareStatement(query)){
                try(ResultSet rs = pst.executeQuery()){
                    while(rs.next()){
                        tableModel.addRow(new Object[]{
                                rs.getString("user_name"),
                                rs.getString("title"),
                                rs.getString("text"),
                                rs.getString("answer")
                        });
                    }
                }
            }
        } catch(Exception e){ e.printStackTrace(); }
    }

    // ================= Delete Response =================
    private void deleteResponse(JTable table, DefaultTableModel tableModel){
        int row = table.getSelectedRow();
        if(row < 0){
            JOptionPane.showMessageDialog(panel,"Select a response to delete.");
            return;
        }

        String user = (String)tableModel.getValueAt(row,0);
        String question = (String)tableModel.getValueAt(row,2);

        try(Connection conn = DBConnection.getConnection()){
            String query = "DELETE r FROM responses r " +
                    "JOIN questions q ON r.question_id=q.id " +
                    "WHERE r.user_name=? AND q.text=?";
            try(PreparedStatement pst = conn.prepareStatement(query)){
                pst.setString(1,user);
                pst.setString(2,question);
                pst.executeUpdate();
            }
            refreshTable(tableModel);
            JOptionPane.showMessageDialog(panel,"Response deleted successfully.");
        } catch(Exception e){ e.printStackTrace(); }
>>>>>>> da4077d (Updated files along with db connection)
    }
}
