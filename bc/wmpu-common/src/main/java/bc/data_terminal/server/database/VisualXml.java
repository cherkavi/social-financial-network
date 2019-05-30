package bc.data_terminal.server.database;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.apache.log4j.*;
import org.w3c.dom.Document;

/** �������� XML ����, ������� ���������� �������� �� ���������� ����������� ���������� ��� ���������� ������� 
 * */
public class VisualXml {
	private final static Logger LOGGER=Logger.getLogger(VisualXml.class);
	
	private String field_user_name;
	
	public VisualXml(String user_name){
		this.field_user_name=user_name;
	}

	public Document getVisualXmlByUser(String terminal_id, Connection connection){
		//Document document=null;
		Document returnValue=null;
		CallableStatement statement=null;
		try{
			statement = connection.prepareCall("{? = call pack_bc_dt.get_tasl_xml(?,?,?)}");
			statement.registerOutParameter(1, Types.INTEGER);
			statement.setInt(2,Integer.parseInt(terminal_id));
			statement.registerOutParameter(3, Types.CLOB);
			statement.registerOutParameter(4, Types.VARCHAR);
			statement.execute();
			if(statement.getInt(1)==0){
				// OK
				Clob result=statement.getClob(3);
				BufferedReader reader=new BufferedReader(result.getCharacterStream());
				ByteArrayOutputStream baos=new ByteArrayOutputStream();

				LOGGER.debug("Begin");
				Charset charset=Charset.forName("UTF-8");
				String currentLine=null;
				while( (currentLine=reader.readLine())!=null){
					LOGGER.debug(currentLine);
					baos.write(currentLine.getBytes(charset));
				}
				baos.flush();
				LOGGER.debug("End");
				
				javax.xml.parsers.DocumentBuilderFactory document_builder_factory=javax.xml.parsers.DocumentBuilderFactory.newInstance();
		        document_builder_factory.setValidating(false);
		        try {
		            // ��������� �����������
		            javax.xml.parsers.DocumentBuilder parser=document_builder_factory.newDocumentBuilder();
		            // Parse ��������
		            returnValue=parser.parse(new ByteArrayInputStream(baos.toByteArray()));
		        }catch(Exception ex){
		        	LOGGER.error("VisualXml#getXmlByUser String to XML Exception: "+ex.getMessage());
		        }
			}else{
				// Error
				LOGGER.error("VisualXml#getXmlByUser SQL not response: "+statement.getString(4));
			};
		}catch(Exception ex){
			LOGGER.error("VisualXml#getXmlByUser: Exception:"+ex.getMessage());
		}finally{
			try{
				statement.close();
			}catch(Exception ex){};
		}
		return returnValue;
	}
	
	public Document getVisualXml(Connection connection){
		return this.getVisualXmlByUser(this.field_user_name,connection);
	}

}


class VisualElement{
	private String field_id_term;
	private String field_id_task;
	private String field_name_task;
	private String field_id_parent;
	private String field_visible;
	private String field_caption;
	
	public VisualElement(){
		this.field_id_term="";
		this.field_id_task="";
		this.field_name_task="";
		this.field_id_parent="";
		this.field_visible="";
		this.field_caption="";
	}
	
	public String getIdTerm() {
		return field_id_term;
	}
	public void setIdTerm(String field_id_term) {
		this.field_id_term = field_id_term;
	}
	public String getIdTask() {
		return field_id_task;
	}
	public void setIdTask(String field_id_task) {
		this.field_id_task = field_id_task;
	}
	public String getNameTask() {
		return field_name_task;
	}
	public void setNameTask(String field_id_name) {
		this.field_name_task = field_id_name;
	}
	public String getIdParent() {
		return field_id_parent;
	}
	public void setIdParent(String field_id_parent) {
		this.field_id_parent = field_id_parent;
	}
	
	public String getCaption(){
		return this.field_caption;
	}
	
	public void loadFromResultSet(ResultSet rs) throws SQLException{
		this.field_id_term=rs.getString("ID_TERM");
		this.field_id_task=rs.getString("ID_TASK");
		this.field_name_task=rs.getString("CD_TASK"); // ���� NAME_TASK
		this.field_id_parent=rs.getString("ID_TASK_PARENT");
		this.field_visible=rs.getString("IS_VISIBLE");
		this.field_caption=rs.getString("NAME_TASK");
	}
	
	public boolean isVisible(){
		return this.field_visible.equals("Y");
	}
	
	public String toString(){
		return "n:"+this.field_name_task+"    v:"+this.isVisible();
	}
	
}