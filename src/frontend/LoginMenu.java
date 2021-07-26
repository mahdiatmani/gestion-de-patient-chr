package frontend;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import backend.DataBase.DatabaseConnection;
import backend.LoggerStartup;
import backend.Practitioner;
import frontend.management.ManagementMenu;

/**
 * Creates a Login menu to login to the Patient Management System.
 * An action listener detects if the username and password are the same as those contained in the practitioner class.
 * If that is the case then the main management menu is initialised.
 *
 * @author Alessandro
 * @version 1.0
 */

public class LoginMenu extends JPanel {

    private final static Logger LOGGER = Logger.getLogger(LoggerStartup.class.getName());
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JFrame frame;

    /**
     * Initialises a new Login menu.
     *
     * @param frame The application frame. Required to initialise the Management menu.
     */
    public LoginMenu(JFrame frame) {
        this.frame = frame;
        /* Initialises the various JComponents of the Login menu. */
        LOGGER.log(Level.INFO, "Initialising Fields");
        usernameField = new JTextField(20);
        usernameField.addActionListener(confirmAction);
        passwordField = new JPasswordField(20);
        passwordField.addActionListener(confirmAction);
        JButton ok = new JButton("OK");
        ok.addActionListener(confirmAction);
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {

            /* If the user presses the cancel option then the application is closed and terminated. */
            @Override
            public void actionPerformed(ActionEvent arg0) {
                frame.dispose();
            }

        });

        /* Sets the layout for the Login menu. */
        LOGGER.log(Level.INFO, "Adding Fields to the Login Panel");
        setLayout(new GridLayout(3, 2));
        add(new JLabel("Username: "));
        add(usernameField);
        add(new JLabel("Password: "));
        add(passwordField);
        add(ok);
        add(cancel);
    }

    /**
     * Defines an action listener to check whether the correct credentials have been entered and if so proceed to initialise the management menu.
     */
    private ActionListener confirmAction = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            /* Checks correct credentials and initialises the management menu. */
            Practitioner practitioner = new Practitioner();
            LOGGER.log(Level.INFO, "Creating New Practitioner");
            LOGGER.log(Level.INFO, "Checking Credentials");
          try {
              DatabaseConnection connectNow = new DatabaseConnection();
              Connection connectDB = connectNow.connectionDuBd();
              String verifyLogin = "SELECT * FROM admin where username = '" + usernameField.getText() + "' and password = '" + passwordField.getText() + "'";
              Statement st = connectDB.createStatement();
              ResultSet rs = st.executeQuery(verifyLogin);

              if(rs.next()){

            if (rs.getString("username").equals(usernameField.getText()) && rs.getString("password").equals(passwordField.getText())) {
                LOGGER.log(Level.INFO, "Correct Crendentials Entered");
                JOptionPane.showMessageDialog(frame, "Correct Username and Password Entered", "Correct Login", JOptionPane.INFORMATION_MESSAGE);
                frame.getContentPane().removeAll();
                LOGGER.log(Level.INFO, "Initialising Management Menu");
                frame.getContentPane().add(new ManagementMenu());
                frame.setTitle("Patient Management System");
                frame.setResizable(true);
                frame.setLocationRelativeTo(null);
                frame.setMinimumSize(new Dimension(1024, 768));
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
              }
            /* If the credentials are incorrect a warning is displayed and the text fields are reset. */
            else {
                LOGGER.log(Level.INFO, "Incorrect Credentials Entered");
                JOptionPane.showMessageDialog(frame, "Wrong Username and/or Password.\nPlease Try Again.", "Wrong Login", JOptionPane.ERROR_MESSAGE);
                usernameField.setText("");
                passwordField.setText("");
                usernameField.grabFocus();
            }

          }catch (Exception v){

              v.printStackTrace();
          }
        }
    };

}