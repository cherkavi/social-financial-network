package bonpay.partner.database.wrap;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="PARTNER")
public class Partner implements Serializable {
	private final static long serialVersionUID=1L;
	@Id
	@Column(name="ID")
	@GeneratedValue(generator="generator",strategy=GenerationType.AUTO)
	@SequenceGenerator(name="generator",sequenceName="GEN_PARTNER_ID")
	private int id;
	@Column(name="SURNAME",length=50)
	private String surname;
	@Column(name="NAME",length=50)
	private String name;
	@Column(name="EMAIL",length=50)
	private String email;
	@Column(name="PASSWORD_VALUE",length=25)
	private String password;
	@Column(name="PHONE",length=50)
	private String phone;
	@Column(name="IP_LOCK",length=15)
	private String ipLock;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getIpLock() {
		return ipLock;
	}
	public void setIpLock(String ipLock) {
		this.ipLock = ipLock;
	}
}
