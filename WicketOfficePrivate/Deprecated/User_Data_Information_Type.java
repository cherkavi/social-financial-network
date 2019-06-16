package bonclub.office_private.database.wrap;

import javax.persistence.*;

import java.io.Serializable;


@Entity
@Table(name="USER_DATA_INFORMATION_TYPE")
public class User_Data_Information_Type implements Serializable{
	private final static long serialVersionUID=1L;
	
	@Id
	@Column(name="ID")
	private int id;
	@Column(name="NAME",length=25)
	private String name;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
