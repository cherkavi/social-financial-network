package bc;
import java.sql.Connection;
import javax.sql.DataSource;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
public class ConnectionManager {

    private static InitialContext ctx = null;
    private static DataSource ds = null;

    public ConnectionManager(){}

    //as the staic method is updating static var i am synchonizing it
    private static synchronized void getDatasource () throws NamingException, SQLException{
        if (ds == null){
            ctx = new InitialContext();
            ds = (DataSource)ctx.lookup("java:comp/env/jdbc/oracle");
        }
    }

    //making getConnection() also static as it is not instance specific
    public static Connection getConnection () throws NamingException, SQLException, Exception{
        Connection conn = null;
        try{
            if (ds == null) {getDatasource ();}
            if (ds != null) {
                conn = ds.getConnection();
            }
        }catch (Exception e){
            throw new Exception("From ConnectionManager_Static",e);
        }
         return conn;
    }//getConnection

}