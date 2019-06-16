package bonpay.partner.database.wrap;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name="SATELLITE_CLIENTS")
public class SatelliteClients {
	@Id
	@Column(name="ID")
	@GeneratedValue(generator="generator",strategy=GenerationType.AUTO)
	@SequenceGenerator(name="generator",sequenceName="GEN_SATELLITE_ID")
	private int id;
	@Column(name="SATELLITE_SESSION_ID",length=50)
	private String satelliteSessionId;
	@Column(name="SATELLITE_ID")
	private Integer satelliteId;
	@Column(name="CLIENT_STATE")
	private int clientState;
	@Column(name="BONPAY_SESSION_ID",length=50)
	private String bonpaySessionId;
	@Column(name="SATELLITE_CLIENT_HTTP_KEY",length=50)
	private String satelliteClientHttpKey;
	@Column(name="AMOUNT_IN_CENT")
	private Integer amountInCent;
	@Column(name="DATE_WRITE")
	private Date dateWrite;

	public SatelliteClients(){
		this.changeData();
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSessionId() {
		return satelliteSessionId;
	}
	public void setSessionId(String sessionId) {
		this.satelliteSessionId = sessionId;
	}
	public Integer getSatelliteId() {
		return satelliteId;
	}
	public void setSatelliteId(Integer satelliteId) {
		this.satelliteId = satelliteId;
	}
	public int getClientState() {
		return clientState;
	}
	public void setClientState(int clientState) {
		this.clientState = clientState;
	}
	public String getSatelliteSessionId() {
		return satelliteSessionId;
	}
	public void setSatelliteSessionId(String satelliteSessionId) {
		this.satelliteSessionId = satelliteSessionId;
	}
	public String getBonpaySessionId() {
		return bonpaySessionId;
	}
	public void setBonpaySessionId(String bonpaySessionId) {
		this.bonpaySessionId = bonpaySessionId;
	}
	public String getSatelliteClientHttpKey() {
		return satelliteClientHttpKey;
	}
	public void setSatelliteClientHttpKey(String satelliteClientsHttpKey) {
		this.satelliteClientHttpKey = satelliteClientsHttpKey;
	}
	public Integer getAmountInCent() {
		return amountInCent;
	}
	public void setAmountInCent(Integer amountInCent) {
		this.amountInCent = amountInCent;
	}
	public Date getDateWrite() {
		return dateWrite;
	}
	public void setDateWrite(Date dateWrite) {
		this.dateWrite = dateWrite;
	}

	
	private void changeData(){
		this.setDateWrite(new Date());
	}
}
