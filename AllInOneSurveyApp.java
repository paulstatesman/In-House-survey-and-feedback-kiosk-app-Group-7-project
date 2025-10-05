import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class AllInOneSurveyApp extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private final String ADMIN_PASSWORD = "admin123";

    private static final Map<String, List<Question>> surveys = new LinkedHashMap<>();
    private static final Map<String, Map<String, List<String>>> responses = new LinkedHashMap<>();

    public AllInOneSurveyApp() {
        setTitle("Survey & Feedback Kiosk");
        setSize(1200, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createHomePage(), "Home");
        mainPanel.add(createAdminPage(), "Admin");
        mainPanel.add(createUserPage(), "User");

        add(mainPanel);
        cardLayout.show(mainPanel, "Home");
    }

    // ================= HOME PAGE =================
    private JPanel createHomePage() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblTitle = new JLabel("IN-HOUSE SURVEY & FEEDBACK SYSTEM", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));

        JButton btnAdmin = new JButton("Admin Section");
        JButton btnUser = new JButton("User Section");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblTitle, gbc);
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(btnAdmin, gbc);
        gbc.gridx = 1;
        panel.add(btnUser, gbc);

        btnAdmin.addActionListener(e -> {
            String pwd = JOptionPane.showInputDialog(panel, "Enter Admin Password:");
            if (pwd != null && pwd.equals(ADMIN_PASSWORD)) {
                cardLayout.show(mainPanel, "Admin");
            } else {
                JOptionPane.showMessageDialog(panel, "Incorrect password!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnUser.addActionListener(e -> cardLayout.show(mainPanel, "User"));

        return panel;
    }

    // ================= ADMIN PAGE =================
    private JPanel createAdminPage() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JLabel lbl = new JLabel("Admin Section", JLabel.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 20));

        // Table for responses
        String[] columns = {"User Name", "Survey", "Question", "Answer"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable responseTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(responseTable);

        // Buttons
        JButton btnCreateSurvey = new JButton("Create Survey");
        JButton btnEditSurvey = new JButton("Edit Survey Question");
        JButton btnRefresh = new JButton("Refresh Table");
        JButton btnDeleteResponse = new JButton("Delete Response");
        JButton btnEditResponse = new JButton("Edit Response");
        JButton btnDeleteSurvey = new JButton("Delete Survey");
        JButton btnBack = new JButton("Back");

        JPanel topPanel = new JPanel();
        topPanel.add(btnCreateSurvey);
        topPanel.add(btnEditSurvey);
        topPanel.add(btnRefresh);
        topPanel.add(btnDeleteResponse);
        topPanel.add(btnEditResponse);
        topPanel.add(btnDeleteSurvey);
        topPanel.add(btnBack);

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(topPanel, BorderLayout.SOUTH);

        // Actions
        btnCreateSurvey.addActionListener(e -> openSurveyCreationDialog(panel));
        btnEditSurvey.addActionListener(e -> editSurveyDialog(panel));
        btnRefresh.addActionListener(e -> refreshTable(tableModel));

        btnDeleteResponse.addActionListener(e -> {
            int row = responseTable.getSelectedRow();
            if (row >= 0) {
                String user = (String) tableModel.getValueAt(row, 0);
                String question = (String) tableModel.getValueAt(row, 2);
                if (responses.containsKey(user)) {
                    responses.get(user).remove(question);
                    if (responses.get(user).isEmpty()) responses.remove(user);
                    refreshTable(tableModel);
                    JOptionPane.showMessageDialog(panel, "Response deleted successfully.");
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Select a response to delete.");
            }
        });

        btnEditResponse.addActionListener(e -> {
            int row = responseTable.getSelectedRow();
            if (row >= 0) {
                String user = (String) tableModel.getValueAt(row, 0);
                String question = (String) tableModel.getValueAt(row, 2);
                String currentAnswer = tableModel.getValueAt(row, 3).toString();
                String newAnswer = JOptionPane.showInputDialog(panel, "Edit Answer:", currentAnswer);
                if (newAnswer != null) {
                    responses.get(user).put(question, Arrays.asList(newAnswer.split("\\s*,\\s*")));
                    refreshTable(tableModel);
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Select a response to edit.");
            }
        });

        btnDeleteSurvey.addActionListener(e -> {
            String[] surveyArray = surveys.keySet().toArray(new String[0]);
            if (surveyArray.length == 0) {
                JOptionPane.showMessageDialog(panel, "No surveys to delete.");
                return;
            }
            String surveyToDelete = (String) JOptionPane.showInputDialog(panel, "Select survey to delete:",
                    "Delete Survey", JOptionPane.QUESTION_MESSAGE, null, surveyArray, surveyArray[0]);
            if (surveyToDelete != null) {
                surveys.remove(surveyToDelete);
                // Remove all responses belonging to deleted survey questions
                responses.forEach((user, map) -> map.entrySet().removeIf(entry -> {
                    for (Question q : surveys.getOrDefault(surveyToDelete, new ArrayList<>())) {
                        if (q.text.equals(entry.getKey())) return true;
                    }
                    return false;
                }));
                refreshTable(tableModel);
                JOptionPane.showMessageDialog(panel, "Survey deleted successfully.");
            }
        });

        btnBack.addActionListener(e -> cardLayout.show(mainPanel, "Home"));

        return panel;
    }

    // ================= SURVEY CREATION =================
    private void openSurveyCreationDialog(JPanel parentPanel) {
        String surveyTitle = JOptionPane.showInputDialog(parentPanel, "Enter Survey Title:");
        if (surveyTitle == null || surveyTitle.isEmpty()) return;

        List<Question> questionList = new ArrayList<>();
        while (true) {
            String qText = JOptionPane.showInputDialog(parentPanel, "Enter question text (Cancel to stop):");
            if (qText == null || qText.isEmpty()) break;

            String[] types = {"Text", "Single Choice", "Multiple Choice"};
            String qType = (String) JOptionPane.showInputDialog(parentPanel, "Select answer type:",
                    "Question Type", JOptionPane.QUESTION_MESSAGE, null, types, types[0]);

            List<String> options = new ArrayList<>();
            if (qType.equals("Single Choice") || qType.equals("Multiple Choice")) {
                String opts = JOptionPane.showInputDialog(parentPanel, "Enter options (comma separated):");
                if (opts != null && !opts.isEmpty()) options = Arrays.asList(opts.split("\\s*,\\s*"));
                else { JOptionPane.showMessageDialog(parentPanel, "Options required!"); continue; }
            }

            questionList.add(new Question(qText, qType, options));
        }

        if (!questionList.isEmpty()) {
            surveys.put(surveyTitle, questionList);
            JOptionPane.showMessageDialog(parentPanel, "Survey \"" + surveyTitle + "\" created successfully!");
        }
    }

    // ================= SURVEY EDIT =================
    private void editSurveyDialog(JPanel parentPanel) {
        if (surveys.isEmpty()) { JOptionPane.showMessageDialog(parentPanel, "No surveys to edit."); return; }

        String[] surveyArray = surveys.keySet().toArray(new String[0]);
        String selectedSurvey = (String) JOptionPane.showInputDialog(parentPanel, "Select Survey to Edit:",
                "Edit Survey", JOptionPane.QUESTION_MESSAGE, null, surveyArray, surveyArray[0]);
        if (selectedSurvey == null) return;

        List<Question> questionList = surveys.get(selectedSurvey);
        String[] questionTexts = questionList.stream().map(q -> q.text).toArray(String[]::new);

        String selectedQuestionText = (String) JOptionPane.showInputDialog(parentPanel, "Select Question to Edit:",
                "Edit Question", JOptionPane.QUESTION_MESSAGE, null, questionTexts, questionTexts[0]);
        if (selectedQuestionText == null) return;

        Question qToEdit = questionList.stream().filter(q -> q.text.equals(selectedQuestionText)).findFirst().orElse(null);
        if (qToEdit == null) return;

        String newQuestionText = JOptionPane.showInputDialog(parentPanel, "Edit Question Text:", qToEdit.text);
        if (newQuestionText == null || newQuestionText.isEmpty()) return;

        String[] types = {"Text", "Single Choice", "Multiple Choice"};
        String newType = (String) JOptionPane.showInputDialog(parentPanel, "Select Question Type:",
                "Question Type", JOptionPane.QUESTION_MESSAGE, null, types, qToEdit.type);

        List<String> newOptions = new ArrayList<>();
        if (newType.equals("Single Choice") || newType.equals("Multiple Choice")) {
            String opts = JOptionPane.showInputDialog(parentPanel, "Edit Options (comma separated):",
                    String.join(", ", qToEdit.options));
            if (opts != null && !opts.isEmpty()) newOptions = Arrays.asList(opts.split("\\s*,\\s*"));
            else { JOptionPane.showMessageDialog(parentPanel, "Options required!"); return; }
        }

        qToEdit.text = newQuestionText;
        qToEdit.type = newType;
        qToEdit.options = newOptions;

        JOptionPane.showMessageDialog(parentPanel, "Question updated successfully!");
    }

    // ================= REFRESH TABLE =================
    private void refreshTable(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        for (Map.Entry<String, Map<String, List<String>>> userEntry : responses.entrySet()) {
            String user = userEntry.getKey();
            for (Map.Entry<String, List<String>> ansEntry : userEntry.getValue().entrySet()) {
                String question = ansEntry.getKey();
                String answer = String.join(", ", ansEntry.getValue());
                String surveyTitle = "";
                for (Map.Entry<String, List<Question>> surveyEntry : surveys.entrySet()) {
                    for (Question q : surveyEntry.getValue()) {
                        if (q.text.equals(question)) { surveyTitle = surveyEntry.getKey(); break; }
                    }
                }
                tableModel.addRow(new Object[]{user, surveyTitle, question, answer});
            }
        }
    }

    // ================= USER PAGE =================
    private JPanel createUserPage() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

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
            if (userName.isEmpty()) { JOptionPane.showMessageDialog(panel, "Please enter your name."); return; }
            if (surveys.isEmpty()) { JOptionPane.showMessageDialog(panel, "No surveys available."); return; }

            surveyPanel.removeAll();
            Map<String, List<String>> userAnswers = new LinkedHashMap<>();

            for (String survey : surveys.keySet()) {
                JLabel lblSurvey = new JLabel("Survey: " + survey);
                lblSurvey.setFont(new Font("Arial", Font.BOLD, 16));
                surveyPanel.add(lblSurvey);

                for (Question q : surveys.get(survey)) {
                    JPanel qPanel = new JPanel();
                    qPanel.setLayout(new BoxLayout(qPanel, BoxLayout.Y_AXIS));
                    qPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    qPanel.add(new JLabel(q.text));

                    List<String> answerList = new ArrayList<>();

                    switch (q.type) {
                        case "Text":
                            JTextField tf = new JTextField(30);
                            qPanel.add(tf);
                            tf.addFocusListener(new FocusAdapter() {
                                public void focusLost(FocusEvent ev) {
                                    answerList.clear();
                                    answerList.add(tf.getText());
                                    userAnswers.put(q.text, new ArrayList<>(answerList));
                                }
                            });
                            break;
                        case "Single Choice":
                            ButtonGroup group = new ButtonGroup();
                            for (String opt : q.options) {
                                JRadioButton rb = new JRadioButton(opt);
                                group.add(rb);
                                qPanel.add(rb);
                                rb.addActionListener(ev -> {
                                    answerList.clear();
                                    answerList.add(rb.getText());
                                    userAnswers.put(q.text, new ArrayList<>(answerList));
                                });
                            }
                            break;
                        case "Multiple Choice":
                            for (String opt : q.options) {
                                JCheckBox cb = new JCheckBox(opt);
                                qPanel.add(cb);
                                cb.addActionListener(ev -> {
                                    answerList.clear();
                                    for (Component c : qPanel.getComponents()) {
                                        if (c instanceof JCheckBox && ((JCheckBox) c).isSelected()) answerList.add(((JCheckBox) c).getText());
                                    }
                                    userAnswers.put(q.text, new ArrayList<>(answerList));
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
                if (userAnswers.isEmpty()) { JOptionPane.showMessageDialog(panel, "Please answer questions."); return; }
                responses.put(userName, userAnswers);
                JOptionPane.showMessageDialog(panel, "Thank you, " + userName + "! Your responses have been recorded.");
                txtName.setText("");
                surveyPanel.removeAll();
                scrollPane.setVisible(false);
                panel.revalidate();
                panel.repaint();
            });
        });

        btnBack.addActionListener(e -> cardLayout.show(mainPanel, "Home"));
        return panel;
    }

    // ================= QUESTION CLASS =================
    static class Question {
        String text;
        String type;
        List<String> options;

        Question(String text, String type, List<String> options) {
            this.text = text;
            this.type = type;
            this.options = options;
        }
    }

    // ================= MAIN =================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AllInOneSurveyApp().setVisible(true));
    }
}
