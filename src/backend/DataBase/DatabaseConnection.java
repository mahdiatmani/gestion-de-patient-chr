package backend.DataBase;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnection {
    //#######################################################################"
    /* pour entrer a la base de donner aller au https://freedb.tech/phpmyadmin
      username : freedbtech_mahdiatmani / password : Doha1599               */
    //#######################################################################"
    public static Connection connectionDuBd() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");

        Connection databaseLink = DriverManager.getConnection("jdbc:mysql://localhost/chrdb","root"
                ,"");
        return databaseLink;

    }
}