package bonpay.partner.database.wrap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="SATELLITE")
public class Satellite {
	@Id
	@Column(name="ID")
	@SequenceGenerator(name="generator",sequenceName="GEN_SATELLITE_ID")
	@GeneratedValue(generator="generator",strategy=GenerationType.AUTO)
	private int id;
	@Column(name="NAME")
	private String name;
	@Column(name="KEY_FOR_SERVICE")
	private String key_for_service;
	@Column(name="KEY_FOR_SATELLITE")
	private String key_for_satellite;
	@Column(name="ID_PARTNER")
	private Integer idPartner;
	@Column(name="URL",length=100)
	private String url;
	@Column(name="ID_STATUS")
	private int idStatus;
	@Column(name="DESCRIPTION",length=200)
	private String description;
	@Column(name="URL_OK",length=100)
	private String urlOk;
	@Column(name="URL_OK_METHOD",length=5)
	private String urlOkMethod;
	@Column(name="URL_CANCEL",length=100)
	private String urlCancel;
	@Column(name="URL_CANCEL_METHOD",length=5)
	private String urlCancelMethod;
	@Column(name="URL_ERROR",length=100)
	private String urlError;
	@Column(name="URL_ERROR_METHOD",length=5)
	private String urlErrorMethod;
	@Column(name="PARAMETER_KEY",length=50)
	private String parameterKey;
	@Column(name="E_MAIL",length=50)
	private String eMail;
	@Column(name="PHONE_FOR_SMS",length=100)
	private String phoneForSms;
	@Column(name="IM_ID")
	private Integer imId;
	@Column(name="DISCOUNT")
	private Integer discount;
	
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
	public String getKey_for_service() {
		return key_for_service;
	}
	public void setKey_for_service(String key_for_service) {
		this.key_for_service = key_for_service;
	}
	public String getKey_for_satellite() {
		return key_for_satellite;
	}
	public void setKey_for_satellite(String key_for_satellite) {
		this.key_for_satellite = key_for_satellite;
	}
	public Integer getIdPartner() {
		return idPartner;
	}
	public void setIdPartner(Integer idPartner) {
		this.idPartner = idPartner;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getIdStatus() {
		return idStatus;
	}
	public void setIdStatus(int idStatus) {
		this.idStatus = idStatus;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrlOk() {
		return urlOk;
	}
	public void setUrlOk(String urlOk) {
		this.urlOk = urlOk;
	}
	public String getUrlOkMethod() {
		return urlOkMethod;
	}
	public void setUrlOkMethod(String urlOkMethod) {
		this.urlOkMethod = urlOkMethod;
	}
	public String getUrlCancel() {
		return urlCancel;
	}
	public void setUrlCancel(String urlCancel) {
		this.urlCancel = urlCancel;
	}
	public String getUrlCancelMethod() {
		return urlCancelMethod;
	}
	public void setUrlCancelMethod(String urlCancelMethod) {
		this.urlCancelMethod = urlCancelMethod;
	}
	public String getUrlError() {
		return urlError;
	}
	public void setUrlError(String urlError) {
		this.urlError = urlError;
	}
	public String getUrlErrorMethod() {
		return urlErrorMethod;
	}
	public void setUrlErrorMethod(String urlErrorMethod) {
		this.urlErrorMethod = urlErrorMethod;
	}
	public String getParameterKey() {
		return parameterKey;
	}
	public void setParameterKey(String parameterKey) {
		this.parameterKey = parameterKey;
	}
	public String getEMail() {
		return eMail;
	}
	public void setEMail(String mail) {
		eMail = mail;
	}
	public String getPhoneForSms() {
		return phoneForSms;
	}
	public void setPhoneForSms(String phoneForSms) {
		this.phoneForSms = phoneForSms;
	}
	public Integer getImId() {
		return imId;
	}
	public void setImId(Integer imId) {
		this.imId = imId;
	}
	public Integer getDiscount() {
		return discount;
	}
	public void setDiscount(Integer discount) {
		this.discount = discount;
	}
	
}
