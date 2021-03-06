import modelS.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Base64;

public class LoginForm extends JDialog{
    private JTextField tfEmail;
    private JPasswordField pfPassword;
    private JButton btnLogin;
    private JPanel loginPanel;
    private JButton btnGoBack;


    public LoginForm(JFrame parent){
        super(parent);
        setTitle("Login");
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(500, 350));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);



        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = tfEmail.getText();
                String password = String.valueOf(pfPassword.getPassword());
                user = getAuthenticatedUser(email, password);

                if (user != null){
                    JOptionPane.showMessageDialog(LoginForm.this,
                            "logged in successfully: " + user.getName(),
                            "Welcome",
                            JOptionPane.INFORMATION_MESSAGE);

                            dispose();
                            new UserForm(user);
                }
                else {
                    JOptionPane.showMessageDialog(LoginForm.this,
                            "Invalid email or password",
                            "Try again",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnGoBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                StartMenu startMenu = new StartMenu(new JFrame());

            }
        });
        setVisible(true);

    }
    public User user;
    private User getAuthenticatedUser(String email, String password) {
        User user = null;

        final String DB_URL = "jdbc:mysql://localhost:3306/agileMethodsDB";
        final String USERNAME = "root";
        final String PASSWORD = "root";
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM users WHERE email=? AND password=?";
            PreparedStatement prepStatement = conn.prepareStatement(sql);
            prepStatement.setString(1, email);
            String encodedString = encode(password);
            prepStatement.setString(2, encodedString);

            ResultSet resultSet = prepStatement.executeQuery();

            if (resultSet.next()) {
                user = new User();
                user.name = resultSet.getString("name");
                user.email = resultSet.getString("email");
                user.password = resultSet.getString("password");
            }

            stmt.close();
            conn.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return user;
    }

    private String encode(String password){

        Base64.Encoder encoder = Base64.getEncoder();
        String encodedString = encoder.encodeToString(password.getBytes(StandardCharsets.UTF_8));
        return encodedString;
    }


    public static void main(String[] args) {
        LoginForm loginForm = new LoginForm(null);
        User user = loginForm.user;
        if (user != null) {
            System.out.printf("Login successful. \nWelcome " + user.name);
            System.out.printf("\nEmail: " + user.getEmail());
        }
        else{
            System.out.println("Login cancelled");
        }
    }
}