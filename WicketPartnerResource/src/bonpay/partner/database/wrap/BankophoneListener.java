package bonpay.partner.database.wrap;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import javax.persistence.*;

@Entity
@Table(name="BANKOPHONE_LISTENER")
public class BankophoneListener {
	@Id
	@Column(name="ID")
	@SequenceGenerator(name="generator",sequenceName="GEN_BANKOPHONE_LISTENER_ID")
	@GeneratedValue(generator="generator",strategy=GenerationType.AUTO)
	private int id;
	@Column(name="PHONE",length=18)
	private String phone;
	@Column(name="DATE_VALUE")
	private Date dateValue;
	@Column(name="ORDER_NUMBER")
	private String orderNumber;
	@Column(name="AMOUNT")
	private Integer amount;
	@Column(name="TIME_VALUE")
	private Date timeValue;
	@Column(name="RESPONSE_CODE")
	private Integer responseCode;
	@Column(name="ORDER_DESCRIPTION")
	private String orderDescription;
	@Column(name="ADDINFO",length=255)
	private String addInfo;
	@Column(name="ADDINFO_1",length=50)
	private String addinfo_1;
	@Column(name="ADDINFO_2",length=50)
	private String addinfo_2;
	@Column(name="ADDINFO_3",length=50)
	private String addinfo_3;
	@Column(name="ADDINFO_4",length=50)
	private String addinfo_4;
	@Column(name="ADDINFO_5",length=50)
	private String addinfo_5;
	@Column(name="DATE_WRITE")
	private java.util.Date date_write;
	@Column(name="REQUEST")
	private String request;
	@Column(name="REQUEST_URL")
	private String requestUrl;
	@Column(name="ORDER_ID")
	private Integer order_id;
	@Column(name="INVALID")
	private Integer invalid;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getAddInfo() {
		return addInfo;
	}
	public void setAddInfo(String addInfo) {
		if(addInfo!=null){
			this.addInfo = addInfo.trim();
			// убираем последний символ, если он будет в посылке 
			if(this.addInfo.charAt(this.addInfo.length()-1)=='!'){
				this.addInfo=this.addInfo.substring(0,this.addInfo.length()-2);
			}
			// попытка распарсить объект на необходимые данные
			// пример строки: addInfo= 10123,200,,401!СУММА!!НОМЕР_КЛИЕНТА!НОМЕР_ТРАНЗАКЦИИ_У_НАС!order
				// получить номер заказа, если он есть
			int orderIndex=this.addInfo.lastIndexOf('!');
			try{
				this.setOrder_id(Integer.parseInt(this.addInfo.substring(orderIndex+1)));
			}catch(Exception ex){};
			StringTokenizer tokenizer=new StringTokenizer(this.addInfo,"!");
			try{
				this.setAddinfo_1(tokenizer.nextToken());// 
				this.setAddinfo_2(tokenizer.nextToken());// сумма
				this.setAddinfo_3(tokenizer.nextToken());// номер клиента
				this.setAddinfo_4(tokenizer.nextToken());// номер транзакции в БанкОФон
			}catch(NoSuchElementException ex){};
		}
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Date getDateValue() {
		return dateValue;
	}
	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Date getTimeValue() {
		return timeValue;
	}
	public void setTimeValue(Date timeValue) {
		this.timeValue = timeValue;
	}
	public Integer getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(Integer responseCode) {
		this.responseCode = responseCode;
	}
	public String getOrderDescription() {
		return orderDescription;
	}
	
	private static final String orderIdBegin="Zakaz:";
	private static final String orderIdEnd="N:";
	
	public void setOrderDescription(String orderDescription) {
		this.orderDescription = orderDescription;
		try{
			int zakazPosition=orderDescription.indexOf(orderIdBegin);
			int numberPosition=orderDescription.indexOf(orderIdEnd);
			this.setOrder_id(Integer.parseInt(orderDescription.substring(zakazPosition+orderIdBegin.length(),numberPosition).trim()));
		}catch(Exception ex){
			System.err.println("Listener#setOrderDescription: "+ex.getMessage());
		}
	}
	public String getAddinfo_1() {
		return addinfo_1;
	}
	public void setAddinfo_1(String addinfo_1) {
		this.addinfo_1 = addinfo_1;
	}
	public String getAddinfo_2() {
		return addinfo_2;
	}
	public void setAddinfo_2(String addinfo_2) {
		this.addinfo_2 = addinfo_2;
	}
	public String getAddinfo_3() {
		return addinfo_3;
	}
	public void setAddinfo_3(String addinfo_3) {
		this.addinfo_3 = addinfo_3;
	}
	public String getAddinfo_4() {
		return addinfo_4;
	}
	public void setAddinfo_4(String addinfo_4) {
		this.addinfo_4 = addinfo_4;
	}
	public String getAddinfo_5() {
		return addinfo_5;
	}
	public void setAddinfo_5(String addinfo_5) {
		this.addinfo_5 = addinfo_5;
	}
	public java.util.Date getDate_write() {
		return date_write;
	}
	public void setDate_write(java.util.Date date_write) {
		this.date_write = date_write;
	}
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}
	public String getRequestUrl() {
		return requestUrl;
	}
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
	public Integer getOrder_id() {
		return order_id;
	}
	public void setOrder_id(Integer order_id) {
		this.order_id = order_id;
	}
	
	@Transient
	public boolean isValid(){
		if((this.amount!=null)&&(this.orderDescription!=null)&&(timeValue!=null)&&(dateValue!=null)){
			return true;
		}else{
			return false;
		}
	}
	public Integer getInvalid() {
		return invalid;
	}
	public void setInvalid(Integer invalid) {
		this.invalid = invalid;
	}
	
	@Transient
	public boolean isReturnValueOk(){
		if((this.responseCode!=null)&&(this.responseCode.intValue()==0)){
			return true;
		}else{
			return false;
		}
	}
	
}
