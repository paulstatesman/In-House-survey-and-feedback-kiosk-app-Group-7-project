package surveyapp;

import javax.swing.*;
import java.awt.*;
<<<<<<< HEAD
=======
import java.util.Arrays;
>>>>>>> da4077d (Updated files along with db connection)

public class HomePage {
    private JPanel panel;
    private JFrame frame;

    public HomePage(JFrame frame){
        this.frame = frame;
<<<<<<< HEAD
        panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);

        JLabel lblTitle = new JLabel("IN-HOUSE SURVEY & FEEDBACK SYSTEM", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
=======

        panel = new JPanel(new GridBagLayout()){
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                g.setColor(new Color(135,206,250)); // sky blue
                g.fillRect(0,0,getWidth(),getHeight());
            }
        };

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15,15,15,15);

        JLabel lblProjectTitle = new JLabel("IN-HOUSE SURVEY & FEEDBACK SYSTEM", JLabel.CENTER);
        lblProjectTitle.setFont(new Font("Verdana", Font.BOLD, 26));
        lblProjectTitle.setForeground(Color.WHITE);
>>>>>>> da4077d (Updated files along with db connection)

        JButton btnAdmin = new JButton("Admin Section");
        JButton btnUser = new JButton("User Section");

<<<<<<< HEAD
        gbc.gridx=0; gbc.gridy=0; gbc.gridwidth=2;
        panel.add(lblTitle, gbc);
=======
        btnAdmin.setBackground(new Color(34,139,34));
        btnAdmin.setForeground(Color.WHITE);
        btnUser.setBackground(new Color(0,191,255));
        btnUser.setForeground(Color.WHITE);

        gbc.gridx=0; gbc.gridy=0; gbc.gridwidth=2;
        panel.add(lblProjectTitle, gbc);
>>>>>>> da4077d (Updated files along with db connection)
        gbc.gridy=1; gbc.gridwidth=1;
        panel.add(btnAdmin, gbc);
        gbc.gridx=1;
        panel.add(btnUser, gbc);

<<<<<<< HEAD
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
=======
        // ================= Admin login (masked password) =================
        btnAdmin.addActionListener(e -> {
            JPasswordField pwdField = new JPasswordField();
            int option = JOptionPane.showConfirmDialog(
                    panel,
                    pwdField,
                    "Enter Admin Password:",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if(option == JOptionPane.OK_OPTION){
                char[] pwd = pwdField.getPassword();
                if(Arrays.equals(pwd, "admin123".toCharArray())){
                    Arrays.fill(pwd,'\0'); // clear password
                    AdminPage adminPage = new AdminPage(frame, panel);
                    frame.setContentPane(adminPage.getPanel());
                    frame.revalidate();
                    frame.repaint();
                } else {
                    Arrays.fill(pwd,'\0'); // clear password
                    JOptionPane.showMessageDialog(panel,"Incorrect password!");
                }
            }
        });

        // ================= User section =================
        btnUser.addActionListener(e -> {
            UserPage userPage = new UserPage(frame, panel);
            frame.setContentPane(userPage.getPanel());
>>>>>>> da4077d (Updated files along with db connection)
            frame.revalidate();
            frame.repaint();
        });
    }

    public JPanel getPanel(){ return panel; }
}
