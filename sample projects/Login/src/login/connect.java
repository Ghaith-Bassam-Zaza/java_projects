package login;
import java.sql.*;
import javax.swing.*;
public class connect {
     
    public static Connection connect() {
     
    try {
    Class.forName("org.sqlite.JDBC");
     
    Connection con = DriverManager.getConnection("jdbc:sqlite:ACC.sqlite");
    return con;
     
    } catch (Exception e) {
    JOptionPane.showMessageDialog(null, "cant connect to database"+e);
    }
    return null;
    }
   
 
}
