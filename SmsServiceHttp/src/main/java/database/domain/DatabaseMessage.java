package database.domain;

public class DatabaseMessage {
	private Integer id_sms_message;
	private String cd_sms_message_type;
	private String cd_sms_state;
	private String recepient;
	private String text_message;
	private Integer repeat_count;
	private Integer send_count;
	private String alphaname;
	private String data_charset;
	
	
	public DatabaseMessage(){
	}


	public Integer getId_sms_message() {
		return id_sms_message;
	}


	public void setId_sms_message(Integer id_sms_message) {
		this.id_sms_message = id_sms_message;
	}


	public String getCd_sms_message_type() {
		return cd_sms_message_type;
	}


	public void setCd_sms_message_type(String cd_sms_message_type) {
		this.cd_sms_message_type = cd_sms_message_type;
	}


	public String getCd_sms_state() {
		return cd_sms_state;
	}


	public void setCd_sms_state(String cd_sms_state) {
		this.cd_sms_state = cd_sms_state;
	}


	public String getRecepient() {
		return recepient;
	}


	public void setRecepient(String recepient) {
		this.recepient = recepient;
	}


	public String getText_message() {
		return text_message;
	}


	public void setText_message(String text_message) {
		this.text_message = text_message;
	}


	public Integer getRepeat_count() {
		return repeat_count;
	}


	public void setRepeat_count(Integer repeat_count) {
		this.repeat_count = repeat_count;
	}


	public Integer getSend_count() {
		return send_count;
	}


	public void setSend_count(Integer send_count) {
		this.send_count = send_count;
	}


	public String getAlphaname() {
		return alphaname;
	}


	public void setAlphaname(String alphaname) {
		this.alphaname = alphaname;
	}


	public String getData_charset() {
		return data_charset;
	}


	public void setData_charset(String data_charset) {
		this.data_charset = data_charset;
	}


	@Override
	public String toString() {
		return "DatabaseMessage [id_sms_message=" + id_sms_message
				+ ", cd_sms_message_type=" + cd_sms_message_type
				+ ", cd_sms_state=" + cd_sms_state + ", recepient=" + recepient
				+ ", text_message=" + text_message + ", repeat_count="
				+ repeat_count + ", send_count=" + send_count + ", alphaname="
				+ alphaname + ", data_charset=" + data_charset + "]";
	}
	
}
