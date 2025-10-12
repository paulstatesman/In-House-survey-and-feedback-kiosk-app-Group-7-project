package surveyapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

public class AdminPage {
    private JPanel panel;
    private JFrame frame;
    private JPanel homePanel;

    public AdminPage(JFrame frame, JPanel homePanel){
        this.frame = frame;
        this.homePanel = homePanel;

        panel = new JPanel(new BorderLayout(10,10));
        JLabel lbl = new JLabel("Admin Section", JLabel.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 20));

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

        JPanel topPanel = new JPanel();
        topPanel.add(btnCreateSurvey);
        topPanel.add(btnEditSurvey);
        topPanel.add(btnRefresh);
        topPanel.add(btnDeleteResponse);
        topPanel.add(btnSyncOffline);
        topPanel.add(btnBack);

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(topPanel, BorderLayout.SOUTH);

        // ================= Button Actions =================
        btnCreateSurvey.addActionListener(e -> openSurveyCreationDialog(panel));
        btnEditSurvey.addActionListener(e -> editSurveyDialog(panel));
        btnRefresh.addActionListener(e -> refreshTable(tableModel));

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

        btnSyncOffline.addActionListener(e -> {
            OfflineQueue.syncResponses();
            refreshTable(tableModel);
            JOptionPane.showMessageDialog(panel,"Offline responses synced successfully!");
        });

        btnBack.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.add(homePanel);
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

        List<Question> questionList = new ArrayList<>();
        while(true){
            String qText = JOptionPane.showInputDialog(parentPanel,"Enter question text (Cancel to stop):");
            if(qText==null || qText.isEmpty()) break;

            String[] types = {"Text","Single Choice","Multiple Choice"};
            String qType = (String)JOptionPane.showInputDialog(parentPanel,"Select answer type:","Question Type",JOptionPane.QUESTION_MESSAGE,null,types,types[0]);

            List<String> options = new ArrayList<>();
            if(qType.equals("Single Choice") || qType.equals("Multiple Choice")){
                String opts = JOptionPane.showInputDialog(parentPanel,"Enter options (comma separated):");
                if(opts!=null && !opts.isEmpty()) options = Arrays.asList(opts.split("\\s*,\\s*"));
                else { JOptionPane.showMessageDialog(parentPanel,"Options required!"); continue; }
            }

            questionList.add(new Question(qText,qType,options));
        }

        if(!questionList.isEmpty()){
            MainApp.surveys.put(surveyTitle,questionList);
            JOptionPane.showMessageDialog(parentPanel,"Survey \""+surveyTitle+"\" created successfully!");
        }
    }

    // ================= Edit Survey =================
    private void editSurveyDialog(JPanel parentPanel){
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
    }

    // ================= Refresh Table =================
    private void refreshTable(DefaultTableModel tableModel){
        tableModel.setRowCount(0);
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
    }
}
