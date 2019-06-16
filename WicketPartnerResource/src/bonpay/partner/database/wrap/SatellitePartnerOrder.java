package bonpay.partner.database.wrap;
import javax.persistence.*;

@Entity
@Table(name="SATELLITE_PARTNER_ORDER")
public class SatellitePartnerOrder {
	@Id
	@SequenceGenerator(name="generator",sequenceName="GEN_SATELLITE_PARTNER_ORDER_ID")
	@GeneratedValue(generator="generator",strategy=GenerationType.AUTO)
	@Column(name="ID")
	private int id;
	@Column(name="PARTNER_ORDER_NUMBER",length=20)
	private String partnerOrderNumber;
	@Column(name="ID_CLIENT")
	private int idClient;
	@Column(name="DATE_WRITE")
	private java.util.Date dateWrite;
	@Column(name="ID_ALGORITHM")
	private Integer idAlgorithm;
	@Column(name="ID_STEP_PAST")
	private Integer idStepPast;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPartnerOrderNumber() {
		return partnerOrderNumber;
	}
	public void setPartnerOrderNumber(String partnerOrderNumber) {
		this.partnerOrderNumber = partnerOrderNumber;
	}
	public int getIdClient() {
		return idClient;
	}
	public void setIdClient(int idClient) {
		this.idClient = idClient;
	}
	public java.util.Date getDateWrite() {
		return dateWrite;
	}
	public void setDateWrite(java.util.Date dateWrite) {
		this.dateWrite = dateWrite;
	}
	public Integer getIdAlgorithm() {
		return idAlgorithm;
	}
	public void setIdAlgorithm(Integer idAlgorithm) {
		this.idAlgorithm = idAlgorithm;
	}
	public Integer getIdStepPast() {
		return idStepPast;
	}
	public void setIdStepPast(Integer idStepPast) {
		this.idStepPast = idStepPast;
	}
	
}
