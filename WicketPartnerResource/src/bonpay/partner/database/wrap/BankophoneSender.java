package bonpay.partner.database.wrap;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name="BANKOPHONE_SENDER")
public class BankophoneSender {
	@Id
	@GeneratedValue(generator="generator",strategy=GenerationType.AUTO)
	@SequenceGenerator(name="generator",sequenceName="GEN_BANKOPHONE_SENDER_ID")
	@Column(name="ID")
	private int id;
	@Column(name="ID_CLIENT")
	private Integer idClient;
	@Column(name="PHONE")
	private String phone;
	@Column(name="AMOUNT")
	private Integer amount;
	@Column(name="SMS_ORDER_DESCRIPTION")
	private String smsOrderDescription;
	@Column(name="DATE_WRITE")
	private Date dateWrite;
	@Column(name="RESULT_FLAG")
	private String resultFlag;
	@Column(name="RESULT_DESCRIPTION")
	private String resultDescription;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Integer getIdClient() {
		return idClient;
	}
	public void setIdClient(Integer idClient) {
		this.idClient = idClient;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public String getSmsOrderDescription() {
		return smsOrderDescription;
	}
	public void setSmsOrderDescription(String smsOrderDescription) {
		this.smsOrderDescription = smsOrderDescription;
	}
	public Date getDateWrite() {
		return dateWrite;
	}
	public void setDateWrite(Date dateWrite) {
		this.dateWrite = dateWrite;
	}
	public String getResultFlag() {
		return resultFlag;
	}
	public void setResultFlag(String resultFlag) {
		this.resultFlag = resultFlag;
	}
	public String getResultDescription() {
		return resultDescription;
	}
	public void setResultDescription(String resultDescription) {
		this.resultDescription = resultDescription;
	}
	
	
}
