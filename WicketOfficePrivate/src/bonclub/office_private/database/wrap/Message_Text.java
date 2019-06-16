package bonclub.office_private.database.wrap;
import javax.persistence.*;

@Entity
@Table(name="MESSAGE_TEXT")
public class Message_Text {
	@Id
	@SequenceGenerator(name="generator",sequenceName="GEN_MESSAGE_TEXT_ID")	
	@GeneratedValue(strategy=GenerationType.AUTO,generator="generator")	
	@Column(name="ID")
	private int id;
	@Column(name="TEXT_VALUE",length=500)
	private String text_value;

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText_value() {
		return text_value;
	}

	public void setText_value(String text_value) {
		this.text_value = text_value;
	}

	public Message_Text(){
		
	}
	
	public Message_Text(String value){
		this.text_value=value;
	}
	
	public void clear(){
		this.id=0;
		this.text_value="";
	}
}
