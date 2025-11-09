package surveyapp;

import javax.swing.*;
import java.awt.*;

public class HomePage {
    private JPanel panel;
    private JFrame frame;

    public HomePage(JFrame frame){
        this.frame = frame;
        panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);

        JLabel lblTitle = new JLabel("IN-HOUSE SURVEY & FEEDBACK SYSTEM", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));

        JButton btnAdmin = new JButton("Admin Section");
        JButton btnUser = new JButton("User Section");

        gbc.gridx=0; gbc.gridy=0; gbc.gridwidth=2;
        panel.add(lblTitle, gbc);
        gbc.gridy=1; gbc.gridwidth=1;
        panel.add(btnAdmin, gbc);
        gbc.gridx=1;
        panel.add(btnUser, gbc);

        btnAdmin.addActionListener(e -> {
            String pwd = JOptionPane.showInputDialog(panel,"Enter Admin Password:");
            if("admin123".equals(pwd)){
                AdminPage adminPage = new AdminPage(frame, panel);
                frame.getContentPane().removeAll();
                frame.add(adminPage.getPanel());
                frame.revalidate();
                frame.repaint();
            } else JOptionPane.showMessageDialog(panel,"Incorrect password!");
        });

        btnUser.addActionListener(e -> {
            UserPage userPage = new UserPage(frame, panel);
            frame.getContentPane().removeAll();
            frame.add(userPage.getPanel());
            frame.revalidate();
            frame.repaint();
        });
    }

    public JPanel getPanel(){ return panel; }
}
