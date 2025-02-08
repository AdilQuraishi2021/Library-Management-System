import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Conn
{
    Conn() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection c = (Connection) DriverManager.getConnection("jdbc:mysql:/LibraryManagementSystem", "root", "AdIl@6969");
            Statement s = c.createStatement();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
