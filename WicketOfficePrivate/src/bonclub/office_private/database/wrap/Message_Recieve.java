package bonclub.office_private.database.wrap;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

import bonclub.office_private.OfficePrivateApplication;
import bonclub.office_private.database.ConnectUtility;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="MESSAGE_RECIEVE")
public class Message_Recieve implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name="generator",sequenceName="GEN_MESSAGE_RECIEVE_ID")	
	@GeneratedValue(strategy=GenerationType.AUTO,generator="generator")	
	@Column(name="ID")
	private int id;
	
	@Column(name="ID_USER")
	private int id_user;
	
	@Column(name="ID_USER_SENDER")
	private int id_user_sender;
	
	@Column(name="ID_MESSAGE_TEXT")
	private int id_message_text;
	
	@Column(name="DATE_RECIEVE")
	private Date date_recieve;

	@Column(name="FLAG")
	private int flag;
	
	@Transient
	private Boolean selected=false;
	
	public Boolean isSelected(){
		return this.selected;
	}
	
	public void setSelected(Boolean value){
		this.selected=value;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId_user_sender() {
		return id_user_sender;
	}

	public void setId_user_sender(int id_user_sender) {
		this.id_user_sender = id_user_sender;
	}

	public int getId_message_text() {
		return id_message_text;
	}

	public void setId_message_text(int id_message_text) {
		this.id_message_text = id_message_text;
	}

	public Date getDate_recieve() {
		return date_recieve;
	}

	public void setDate_recieve(Date date_recieve) {
		this.date_recieve = date_recieve;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public void clear(){
		id=0;
		id_user_sender=0;
		id_message_text=0;
		date_recieve=null;
		flag=0;
		selected=false;
	}

	public int getId_user() {
		return id_user;
	}

	public void setId_user(int id_user) {
		this.id_user = id_user;
	}
	
	/** получить отправителя для данного письма */
	public String getSender(OfficePrivateApplication application){
		ConnectUtility connectUtility=application.getConnectUtility();
		String returnValue=this.getMessageInputSender(id,connectUtility);
		connectUtility.close();
		return returnValue;
	}
	
	@SuppressWarnings("unchecked")
	@Transient
	public String getMessageInputSender(int id_message_input,ConnectUtility connector){
		//out_debug("getMessageInputSender>>> id:"+id_message_input);
		String return_value=null;
		try{
			Session session=connector.getSession();
			SQLQuery query=session.createSQLQuery("select {u.*} "+
												  "from message_recieve "+
												  "inner join users {u} on message_recieve.id_user_sender=u.id "+
												  "where message_recieve.id=:message_recieve");
			query.setParameter("message_recieve", new Integer(id_message_input));
			query.addEntity("u",Users.class);
			List<Users> list=(List<Users>)query.list();
			return_value=list.get(0).getNick();
		}catch(Exception ex){
			//out_error("getMessageInputSender Exception:"+ex.getMessage());
		}finally{
		}
		return (return_value==null)?"":return_value;
	}
}
