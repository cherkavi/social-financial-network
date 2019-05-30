package bc.objects;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import bc.connection.Connector;

public class bcKeyObject extends bcObject {
	
    private String key_version ="";
    private String key_date ="";
    private String key_nameUser;
    private String key_generateKeyError;

	public bcKeyObject() {
	}

	public String getKeyError() {
		return this.key_generateKeyError;
	}

	public String getKeyVersion() {
		return this.key_version;
	}

	public String getKeyDate() {
		return this.key_date;
	}

	public String getKeyUser() {
		return this.key_nameUser;
	}
	
    public void getFeature() {
        Statement st = null;
        Connection con = null;
        try{
        	con = Connector.getConnection(getSessionId());
            st = con.createStatement();
            ResultSet rset = st.executeQuery(
                    "select ver_key, date_key, name_user FROM " + getGeneralDBScheme() + ".v_cur_key");
            while (rset.next())
            {
                this.key_version = rset.getString("VER_KEY");
                this.key_date = rset.getString("DATE_KEY");
                this.key_nameUser = rset.getString("NAME_USER");

            }
        }catch (SQLException e) {e.toString();}
        catch (Exception el) {el.toString();}
        finally {
            try {
                if (st!=null) st.close();
                if (con!=null) con.close();
            } catch (SQLException w) {w.toString();}

        }
        Connector.closeConnection(con);
    } //getKeyFeatures

    public boolean addNewKey()
    {
        boolean isOk = false;
        StringBuilder error = new StringBuilder();
        CallableStatement cs = null;
        Connection con = null;
        try
        {
        	con = Connector.getConnection(getSessionId());
        cs = con.prepareCall("{call " + getGeneralDBScheme() + ".PACK_BC_KEY.ADD_NEW_KEY()}");
        cs.execute();
        cs.close();
        isOk = true;
        } catch (SQLException e) {error.append(e.toString()); isOk=false;}
          catch (Exception el) {error.append(el.toString()); isOk=false;}
          finally {
              this.key_generateKeyError = error.toString();
              try {
                  if (cs!=null) cs.close();
                  if (con!=null) con.close();
              } catch (SQLException es) {es.toString();}
          }
          Connector.closeConnection(con);
        return isOk;
    } // addNewKey()

}
