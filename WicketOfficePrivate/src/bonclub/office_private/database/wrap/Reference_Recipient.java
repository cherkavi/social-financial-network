package bonclub.office_private.database.wrap;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name="REFERENCE_RECIPIENT")
public class Reference_Recipient implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name="generator",sequenceName="GEN_REFERENCE_RECIPIENT_ID")	
	@GeneratedValue(strategy=GenerationType.AUTO,generator="generator")	
	@Column(name="ID")
	private int id;
	@Column(name="ID_MESSAGE_SEND")
	private int id_message_send;
	@Column(name="ID_RECIPIENT")
	private int id_recipient;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId_message_send() {
		return id_message_send;
	}
	public void setId_message_send(int id_message_send) {
		this.id_message_send = id_message_send;
	}
	public int getId_recipient() {
		return id_recipient;
	}
	public void setId_recipient(int id_recipient) {
		this.id_recipient = id_recipient;
	}

	public void clear(){
		this.id=0;
		this.id_message_send=0;
		this.id_recipient=0;
	}
	
}
