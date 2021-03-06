package frontend.management;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;

import backend.DataBase.DatabaseConnection;
import backend.InfoPrinter;
import backend.JSONParser;
import backend.LoggerStartup;
import backend.Patient;
import frontend.dialog.PersonalDetailsDialog;
import frontend.dialog.SearchDialog;

/**
 * Creates the Management menu for the Patient Management System.
 * This menu manages the main functions of the Patient Management System such as adding, deleting, editing, searching, opening, or saving patients.
 * Whenever one of the options is chosen a different JOptionPane appears giving the users more options.
 * The Management menu also acts as the container for the Patient menu, where different patients can be chosen to see more details about them.
 *
 * @author EL MAHDI ATMANI
 * @version 1.0
 */
public class ManagementMenu extends JPanel {

    private final static Logger LOGGER = Logger.getLogger(LoggerStartup.class.getName());

    /**
     * Initialises the Management menu.
     */
    public ManagementMenu() {
        setLayout(new BorderLayout());

        /* Initialises the Patient menu and adds it to the Management menu. */
        LOGGER.log(Level.INFO, "Initialising Patient Menu");
        PatientMenu patientMenu = new PatientMenu();
        LOGGER.log(Level.INFO, "Adding Patient Menu to the Management Menu");
        add(patientMenu, BorderLayout.CENTER);

        /*
         * Creates various buttons responsible for the various actions in the Management menu.
         * An action listener is added to each button to define its function.
         */
        LOGGER.log(Level.INFO, "Initialising Open File Button");
        JButton openBtn = new JButton(new ImageIcon(getClass().getResource("/open.gif")));
        openBtn.setToolTipText("Open Patient Register File");
        openBtn.addActionListener(new ActionListener() {

            /* Creates a new JSONParser to read a JSON text file.
             * An ArrayList is returned by the parser and added to the list of patients contained in the Patient menu.
             */
            @Override
            public void actionPerformed(ActionEvent arg0) {
                LOGGER.log(Level.INFO, "Open File Selected");
                JSONParser json = new JSONParser();
                ArrayList<Patient> patient = json.load();
                LOGGER.log(Level.INFO, "Adding Patients To Management System");
                for (Patient p : patient) {
                    patientMenu.addToList(p);
                }
            }

        });

        LOGGER.log(Level.INFO, "Initialising Save To Data Base Button");
        JButton saveBtn = new JButton(new ImageIcon(getClass().getResource("/save.gif")));
        saveBtn.setToolTipText("Save Patient Register File");
        saveBtn.addActionListener(new ActionListener() {

            /*
             * Opens a search dialog where the user can choose the patients to export.
             * Once a list of patients has been selected a JSONParser is initialised to export the data to the selected text file.
             */
            @Override
            public void actionPerformed(ActionEvent arg0) {
                LOGGER.log(Level.INFO, "Save To File Selected");
                SearchDialog search = new SearchDialog(patientMenu.getPatients());

                String[][] data = new String[patientMenu.getPatients().size()][15];
                for (int i = 0; i < patientMenu.getPatients().size(); i++) {
                    data[i][0] = patientMenu.getPatients().get(i).getFirstName();
                    data[i][1] = patientMenu.getPatients().get(i).getLastName();
                    data[i][2] = patientMenu.getPatients().get(i).getBirthday();
                    data[i][3] = patientMenu.getPatients().get(i).getPhoneNumber();
                    data[i][4] = String.valueOf(patientMenu.getPatients().get(i).getBilling());
                    data[i][5] = patientMenu.getPatients().get(i).getPostcode();
                    data[i][6] = patientMenu.getPatients().get(i).getCondition();
                    data[i][7] = patientMenu.getPatients().get(i).getDoctor();
                    data[i][8] = patientMenu.getPatients().get(i).getID();
                    data[i][9] = patientMenu.getPatients().get(i).getConditionCIN();
                    data[i][10] = patientMenu.getPatients().get(i).getProfilePicturePath();
                    data[i][11] = patientMenu.getPatients().get(i).getComments();
                    data[i][12] = patientMenu.getPatients().get(i).getAppointments();
                    data[i][13] = patientMenu.getPatients().get(i).getStreetAddress();
                    boolean paid = patientMenu.getPatients().get(i).isPaid();

     InfoPrinter infoPrinter = new InfoPrinter();
                            //name    //lastname //billing  //l'anniv  //phone   //Condition //Docteur  //ID      //CIN
     infoPrinter.patientPdf(data[i][0],data[i][1],data[i][4],data[i][2],data[i][3],data[i][6],data[i][7],data[i][8],data[i][9],data[i][12],paid);

                    //sql save
                    try {
                        DatabaseConnection connectNow = new DatabaseConnection();
                        Connection connectDB = connectNow.connectionDuBd();
                        String savePatient = "INSERT INTO `patient` " +
          "(`firstname`, `lastname`, `birthday`, `phone`, `street`, `CodePost`, `Doctor`, `billing`,`CIN`,`Condiction`) " +
   "VALUES ('"+data[i][0]+"', '"+data[i][1]+"','"+data[i][2]+"','"+data[i][3]+"','"+data[i][13]+"','"+data[i][5]+"','"+data[i][7]+"','"+data[i][4]+"','"+data[i][9]+"','"+data[i][6]+"');";

                        Statement st = connectDB.createStatement();
                        st.executeUpdate(savePatient);

          //save the comment and the appointment
                        String savePatient_more_info = "INSERT INTO `commentetappointment` (`comments`, `appointment`) VALUES ('"+data[i][11]+"', '"+data[i][12]+"');";
                        st.executeUpdate(savePatient_more_info);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                        //--------------------------------------------------------------
                }


                search.setSingleSelection(false);
                int optionChosen = JOptionPane.showConfirmDialog(null, search, "Search Patient", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (optionChosen == JOptionPane.OK_OPTION) {
                    ArrayList<Patient> list = new ArrayList<Patient>();
                    list = patientMenu.getPatients();
                    ArrayList<Patient> filteredlist = new ArrayList<Patient>();
                    int[] selectedRows = search.getSelectedRows();

                    for (int i = 0; i < selectedRows.length; i++) {
                        filteredlist.add(list.get(selectedRows[i]));
                    }

                    JSONParser json = new JSONParser();
                    LOGGER.log(Level.INFO, "Saving Patients To File");
                    json.save(filteredlist);
                }
            }
        });

        LOGGER.log(Level.INFO, "Initialising Add Button");
        JButton addBtn = new JButton(new ImageIcon(getClass().getResource("/add.gif")));
        addBtn.setToolTipText("Add Patient");
        addBtn.addActionListener(new ActionListener() {

            /* Opens a patient dialog where the user can add a user to the Patient menu. */
            @Override
            public void actionPerformed(ActionEvent arg0) {
                LOGGER.log(Level.INFO, "Add Patient Selected");
                PersonalDetailsDialog personalDetailsDialog = new PersonalDetailsDialog();
                int optionChosen = JOptionPane.showConfirmDialog(null, personalDetailsDialog, "Add Patient", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (optionChosen == JOptionPane.OK_OPTION) {
                    patientMenu.addToList(new Patient(personalDetailsDialog.getFirstName(), personalDetailsDialog.getLastName(), personalDetailsDialog.getBirthday(),
                            personalDetailsDialog.getPhoneNumber(), personalDetailsDialog.getStreetAddress(), personalDetailsDialog.getPostcode(),
                            personalDetailsDialog.getBilling(), personalDetailsDialog.getDoctor(), personalDetailsDialog.getMedicalCondition(),
                            personalDetailsDialog.getCIN()));
                    LOGGER.log(Level.INFO, "Patient Added To Management System");
                }
            }
        });

        JButton deleteBtn = new JButton(new ImageIcon(getClass().getResource("/delete.gif")));
        deleteBtn.setToolTipText("Remove Patient");
        deleteBtn.addActionListener(new ActionListener() {

            /* Deletes a patient from the system after the user confirms its deletion. */
            @Override
            public void actionPerformed(ActionEvent arg0) {
                LOGGER.log(Level.INFO, "Delete Patient Selected");
                int optionChosen = JOptionPane.showConfirmDialog(null, "Are You Sure You Want To Delete This Patient?", "Delete Patient", JOptionPane.OK_CANCEL_OPTION);
                if (optionChosen == JOptionPane.OK_OPTION) {
                    Patient patient = patientMenu.getSelectedPatient();
                    patientMenu.removeFromList(patient);

                    LOGGER.log(Level.INFO, "Patient Deleted From Management System");
                }
            }
        });

        JButton editBtn = new JButton(new ImageIcon(getClass().getResource("/edit.gif")));
        editBtn.setToolTipText("Edit Patient");
        editBtn.addActionListener(new ActionListener() {

            /* Opens a patient dialog where the user can edit personal details of the user. */
            @Override
            public void actionPerformed(ActionEvent arg0) {
                LOGGER.log(Level.INFO, "Edit Patient Selected");
                PersonalDetailsDialog personalDetailsDialog = new PersonalDetailsDialog();
                if (patientMenu.getSelectedPatient() != null) {
                    Patient patient = patientMenu.getSelectedPatient();
                    personalDetailsDialog.setPatientData(patient.getFirstName(), patient.getLastName(), patient.getBirthday(), patient.getPhoneNumber(), patient.getStreetAddress(),
                            patient.getPostcode(), patient.getBilling(), patient.getDoctor(), patient.getCondition(), patient.getConditionCIN());
                    int optionChosen = JOptionPane.showConfirmDialog(null, personalDetailsDialog, "Edit Patient", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (optionChosen == JOptionPane.OK_OPTION) {
                        patient.setData(personalDetailsDialog.getFirstName(), personalDetailsDialog.getLastName(), personalDetailsDialog.getBirthday(),
                                personalDetailsDialog.getPhoneNumber(), personalDetailsDialog.getStreetAddress(), personalDetailsDialog.getPostcode(),
                                personalDetailsDialog.getBilling(), personalDetailsDialog.getDoctor(), personalDetailsDialog.getMedicalCondition(),
                                personalDetailsDialog.getCIN());
                        patientMenu.revalidatePatientMenu();
                        LOGGER.log(Level.INFO, "Patient Edited");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No User Selected", "User Selection", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        JButton searchBtn = new JButton(new ImageIcon(getClass().getResource("/search.gif")));
        searchBtn.setToolTipText("Search Patient Register");
        searchBtn.addActionListener(new ActionListener() {

            /* Opens a search dialog where the user can search patients and select the one to view. */
            @Override
            public void actionPerformed(ActionEvent arg0) {
                LOGGER.log(Level.INFO, "Search Patient Selected");
                if (patientMenu.getPatients() != null) {
                    SearchDialog search = new SearchDialog(patientMenu.getPatients());
                    search.setSingleSelection(true);
                    int optionChosen = JOptionPane.showConfirmDialog(null, search, "Search Patient", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (optionChosen == JOptionPane.OK_OPTION) {
                        if (patientMenu.getPatients() != null) {
                            int i = search.getRow();
                            patientMenu.setSelectedPatient(i);
                            LOGGER.log(Level.INFO, "Patient Selected");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No Users Found in Database", "Empty Database", JOptionPane.INFORMATION_MESSAGE);
                }
            }

        });

        /* Creates a toolbar to display all of the buttons previously created. */
        LOGGER.log(Level.INFO, "Initialising Toolbar");
        JToolBar toolbar = new JToolBar();
        LOGGER.log(Level.INFO, "Adding Buttons to Toolbar");
        toolbar.add(openBtn);
        toolbar.add(saveBtn);
        toolbar.addSeparator();
        toolbar.add(addBtn);
        toolbar.add(deleteBtn);
        toolbar.addSeparator();
        toolbar.add(editBtn);
        toolbar.add(searchBtn);
        toolbar.setFloatable(false);
        LOGGER.log(Level.INFO, "Adding Toolbar To Management Menu");
        add(toolbar, BorderLayout.NORTH);
    }

}