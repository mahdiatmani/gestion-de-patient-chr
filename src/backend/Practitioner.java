package backend;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class representing the user/practitioner logging to the Patient Management System.
 *
 * @author EL MAHDI ATMANI
 * @version 1.0
 */
public class Practitioner {

    private String nom;
    private String prenom;
    private String email;
    private String username;
    private char[] password;

    /**
     * Initialises a new practitioner with default values for username and password.
     */
    public Practitioner() {
        LOGGER.log(Level.INFO, "Creating New Practitioner");
        this.username = "admin";
        this.password = new char[]{'1', '2', '3', '4'};
    }

    private final static Logger LOGGER = Logger.getLogger(LoggerStartup.class.getName());

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the practitioner's username.
     *
     * @return The practitioner's username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the practitioner's password.
     *
     * @return The practitioner's password.
     */
    public char[] getPassword() {
        return password;
    }

}