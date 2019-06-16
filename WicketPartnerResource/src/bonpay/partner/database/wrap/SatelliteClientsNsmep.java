package bonpay.partner.database.wrap;
import javax.persistence.*;

@Entity
@Table(name="SATELLITE_CLIENTS_NSMEP")
public class SatelliteClientsNsmep {
	@Id
	@GeneratedValue(generator="generator",strategy=GenerationType.AUTO)
	@SequenceGenerator(name="generator",sequenceName="GEN_SATELLITE_CLIENTS_NSMEP_ID")
	@Column(name="ID")
	private int id;
	
	@Column(name="ID_CLIENT")
	private int idClient;

	@Lob
	@Column(name="DATA_FIELD")
	private byte[] data;
	
	@Column(name="DATE_WRITE")
	private java.util.Date dateWrite;
	
	@Column(name="SPEC_TP")
	private Integer specTp;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdClient() {
		return idClient;
	}

	public void setIdClient(int idClient) {
		this.idClient = idClient;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public java.util.Date getDateWrite() {
		return dateWrite;
	}

	public void setDateWrite(java.util.Date dateWrite) {
		this.dateWrite = dateWrite;
	}

	public Integer getSpecTp() {
		return specTp;
	}

	public void setSpecTp(Integer specTp) {
		this.specTp = specTp;
	}
}
