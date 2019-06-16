package bonclub.office_private.database.wrap;

import javax.persistence.*;

import java.io.Serializable;

/** 
 * <table border="1">
 * 	<tr> <th>DataBase</th> <th>POJO </th> </tr>
 * <tr> <td>ID</td> <td> id</td></tr> 
 * <tr> <td>NAME</td> <td>name </td></tr> 
 * </table>
 * 
 * */
@Entity
@Table(name="USER_DATA_GROUP")
public class User_Data_Group implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name="generator",sequenceName="GEN_USER_DATA_GROUP_ID")	
	@GeneratedValue(strategy=GenerationType.AUTO,generator="generator")	
	@Column(name="ID")
	private int id;
	@Column(name="NAME",length=30)
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
