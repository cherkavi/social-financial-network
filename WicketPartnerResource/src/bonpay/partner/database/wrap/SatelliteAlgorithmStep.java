package bonpay.partner.database.wrap;
import javax.persistence.*;


@Entity
@Table(name="SATELLITE_ALGORITHM_STEP")
public class SatelliteAlgorithmStep {
	@Id
	@GeneratedValue(generator="generator",strategy=GenerationType.AUTO)
	@SequenceGenerator(name="generator",sequenceName="GEN_SATELLITE_ALGORITHM_STEP_ID")
	@Column(name="ID")
	private int id;
	@Column(name="ID_ALGORITHM")
	private int idAlgorithm;
	@Column(name="SPEC_TP")
	private int specTp;
	@Column(name="IS_CLUB")
	private Integer isClub;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIdAlgorithm() {
		return idAlgorithm;
	}
	public void setIdAlgorithm(int idAlgorithm) {
		this.idAlgorithm = idAlgorithm;
	}
	public int getSpecTp() {
		return specTp;
	}
	public void setSpecTp(int specTp) {
		this.specTp = specTp;
	}
	public Integer getIsClub() {
		return isClub;
	}
	public void setIsClub(Integer isClub) {
		this.isClub = isClub;
	}
}
