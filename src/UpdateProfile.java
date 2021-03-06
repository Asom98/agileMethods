import modelS.User;
import modelS.UserProfile;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class UpdateProfile extends JFrame {
    private JPanel panel1;
    private JPanel mainPanel;
    private JButton UPDATEButton;
    private JButton goBackButton;
    private JPanel TopPanel;
    private JPanel InfoPanel;
    private JTextField dateOfBirthTf;
    private JTextField genderTf;
    private JTextField heightTf;
    private JTextField weightTf;

    public User user;


    //creating and setting Jframe
    public UpdateProfile(JFrame parent, User user) {

        setContentPane(mainPanel);
        setTitle("Update Profile");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                try {
                    UserProfileForm userProfile = new UserProfileForm(null, user);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                //this button should call on UserProfile (need to change the login function)
            }
        });

        UPDATEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UpdateUser(user);
                updateUserDataBase(user);
                dispose();
                try {

                    UserProfileForm userProfileForm = new UserProfileForm(null, user);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                //this funtion should add and uppdate the table users and Userprofile in database

            }
        });
        setVisible(true);
    }


    //This method store the value in textfields
    private void UpdateUser(User user) {

        String dateOfBirth = dateOfBirthTf.getText();
        String gender = genderTf.getText();
        int height = Integer.parseInt(heightTf.getText());
        int weight = Integer.parseInt(weightTf.getText());


        user.setDateOfBirth(dateOfBirth);
        user.setGender(gender);
        user.setHeight(height);
        user.setWeight(weight);
    }

    //creating database path and store values in database
    private void updateUserDataBase (User user) {

        final String DB_URL = "jdbc:mysql://localhost:3306/agileMethodsDB";
        final String USERNAME = "root";
        final String PASSWORD = "root";

        try {

            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "Update users set "
                    + "    gender = ?, "
                    + "    dateOfBirth = ?, "
                    + "    height = ?, "
                    + "    weight = ? "
                    + "WHERE name = ? ";
            PreparedStatement prepStatement2 = conn.prepareStatement(sql);

            prepStatement2.setString(1, user.getGender());
            prepStatement2.setString(2, user.getDateOfBirth());
            prepStatement2.setString(3, String.valueOf(user.getHeight()));
            prepStatement2.setString(4, String.valueOf(user.getWeight()));
            prepStatement2.setString(5, user.getName());

            prepStatement2.executeUpdate();

            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
