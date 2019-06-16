package bonclub.office_private.database.wrap;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.Session;

import bonclub.office_private.OfficePrivateApplication;
import bonclub.office_private.database.ConnectUtility;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="MESSAGE_SEND")
public class Message_Send implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final int length_limit=20;
	@Id
	@SequenceGenerator(name="generator",sequenceName="GEN_MESSAGE_SEND_ID")	
	@GeneratedValue(strategy=GenerationType.AUTO,generator="generator")	
	@Column(name="ID")
	private int id;
	@Column(name="ID_SENDER")
	private int id_sender;
	@Column(name="ID_MESSAGE_TEXT")
	private int id_message_text;
	@Column(name="DATE_CREATE")
	private Date date_create;
	
	@Transient
	private String recipient;
	
	@Transient
	private Boolean selected=false;
	
	
	public String getRecipient(OfficePrivateApplication application){
		ConnectUtility connector=application.getConnectUtility();
		if((this.recipient==null)||(this.recipient.equals(""))){
			// чтение из базы 
			List<Users> list=this.getUsersByMessageSend(id,connector);
			StringBuffer return_value=new StringBuffer();
			String current_value;
			for(int counter=0;counter<list.size();counter++){
				current_value=list.get(counter).getNick();
				if((current_value.length()+return_value.length())>length_limit){
					return_value.append("...");
					break;
				}else{
					return_value.append(current_value);
					if(counter!=(list.size()-1)){
						return_value.append(", ");
					}
				}
			}
			this.recipient=return_value.toString();
		}
		connector.close();
		return this.recipient;
	}
	
	@SuppressWarnings("unchecked")
	private List<Users> getUsersByMessageSend(int message_send_id, ConnectUtility connector) {
		//out_debug("getUsersByMessageSend: "+message_send_id);
		List<Users> return_value=null;
		Session session=connector.getSession();
		//out_debug("getUsersByMessageSend: Session:"+session);
		try{
			List<?> list=session.createSQLQuery("select {u.*} " +
											 "from reference_recipient " +
											 "inner join users {u} on u.id=reference_recipient.id_recipient " +
											 "where reference_recipient.id_message_send="+message_send_id
											 ).addEntity("u",Users.class).list();
			//out_debug("getUsersByMessageSend: list is getted");
			return_value=(List<Users>)list;
		}catch(Exception ex){
			//out_error("getUsersByMessageSend:"+ex.getMessage());
		}finally{
			//closeSession(session);
		}
		if(return_value==null){
			return_value=new ArrayList<Users>();
		};
		return return_value;
	}

	public void setRecipient(String value){
		this.recipient=value;
	}
	
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
	public int getId_sender() {
		return id_sender;
	}
	public void setId_sender(int id_sender) {
		this.id_sender = id_sender;
	}
	public int getId_message_text() {
		return id_message_text;
	}
	public void setId_message_text(int id_message_text) {
		this.id_message_text = id_message_text;
	}
	public Date getDate_create() {
		return date_create;
	}
	public void setDate_create(Date date_create) {
		this.date_create = date_create;
	}
}
