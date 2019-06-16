package bonclub.office_private.database.wrap;
import javax.persistence.*;

@Entity
@Table(name="PARAMETERS")
public class Parameters {
	@Id
	@SequenceGenerator(name="generator",sequenceName="GEN_PARAMETERS_ID")
	@GeneratedValue(generator="generator",strategy=GenerationType.AUTO)
	@Column(name="ID")
	private int id;
	@Column(name="NAME",length=100)
	private String name;
	@Column(name="PARAM_VALUE",length=255)
	private String value;
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
