package bonclub.office_private.web_service.common_objects;

import java.io.Serializable;
import java.math.BigDecimal;

public class CommodityInformation implements Serializable{
	private static final long serialVersionUID = 1L;

	private BigDecimal id_furshet_purchase; 
	private String card_serial_number; 
	private BigDecimal id_issuer;
	private BigDecimal id_payment_system; 
	private BigDecimal id_trans; 
	private BigDecimal id_term; 
	private BigDecimal s_area_id;
	private BigDecimal cashid; 
	private BigDecimal check_s_num; 
	private BigDecimal count_purch;
	private BigDecimal sum_check; 
	private BigDecimal purch_num;
	private String purch_datetime; 
    private BigDecimal purch_refund; 
    private String purch_text; 
    private String purch_group;
    private String purch_barc; 
    private BigDecimal purch_price; 
    private BigDecimal purch_count; 
    private String measure;
    private BigDecimal purch_tax; 
    private BigDecimal sum_bon_purch; 
    private BigDecimal sum_purch; 
    private BigDecimal sum_pay_cash_purch;
    private BigDecimal sum_pay_bon_purch; 
    private BigDecimal sum_pay_card_purch; 
    private String bon_card_nr;
    
	public BigDecimal getId_furshet_purchase() {
		return id_furshet_purchase;
	}
	public void setId_furshet_purchase(BigDecimal idFurshetPurchase) {
		id_furshet_purchase = idFurshetPurchase;
	}
	public String getCard_serial_number() {
		return card_serial_number;
	}
	public void setCard_serial_number(String cardSerialNumber) {
		card_serial_number = cardSerialNumber;
	}
	public BigDecimal getId_issuer() {
		return id_issuer;
	}
	public void setId_issuer(BigDecimal idIssuer) {
		id_issuer = idIssuer;
	}
	public BigDecimal getId_payment_system() {
		return id_payment_system;
	}
	public void setId_payment_system(BigDecimal idPaymentSystem) {
		id_payment_system = idPaymentSystem;
	}
	public BigDecimal getId_trans() {
		return id_trans;
	}
	public void setId_trans(BigDecimal idTrans) {
		id_trans = idTrans;
	}
	public BigDecimal getId_term() {
		return id_term;
	}
	public void setId_term(BigDecimal idTerm) {
		id_term = idTerm;
	}
	public BigDecimal getS_area_id() {
		return s_area_id;
	}
	public void setS_area_id(BigDecimal sAreaId) {
		s_area_id = sAreaId;
	}
	public BigDecimal getCashid() {
		return cashid;
	}
	public void setCashid(BigDecimal cashid) {
		this.cashid = cashid;
	}
	public BigDecimal getCheck_s_num() {
		return check_s_num;
	}
	public void setCheck_s_num(BigDecimal checkSNum) {
		check_s_num = checkSNum;
	}
	public BigDecimal getCount_purch() {
		return count_purch;
	}
	public void setCount_purch(BigDecimal countPurch) {
		count_purch = countPurch;
	}
	public BigDecimal getSum_check() {
		return sum_check;
	}
	public void setSum_check(BigDecimal sumCheck) {
		sum_check = sumCheck;
	}
	public BigDecimal getPurch_num() {
		return purch_num;
	}
	public void setPurch_num(BigDecimal purchNum) {
		purch_num = purchNum;
	}
	public String getPurch_datetime() {
		return purch_datetime;
	}
	public void setPurch_datetime(String purchDatetime) {
		purch_datetime = purchDatetime;
	}
	public BigDecimal getPurch_refund() {
		return purch_refund;
	}
	public void setPurch_refund(BigDecimal purchRefund) {
		purch_refund = purchRefund;
	}
	public String getPurch_text() {
		return purch_text;
	}
	public void setPurch_text(String purchText) {
		purch_text = purchText;
	}
	public String getPurch_group() {
		return purch_group;
	}
	public void setPurch_group(String purchGroup) {
		purch_group = purchGroup;
	}
	public String getPurch_barc() {
		return purch_barc;
	}
	public void setPurch_barc(String purchBarc) {
		purch_barc = purchBarc;
	}
	public BigDecimal getPurch_price() {
		return purch_price;
	}
	public void setPurch_price(BigDecimal purchPrice) {
		purch_price = purchPrice;
	}
	public BigDecimal getPurch_count() {
		return purch_count;
	}
	public void setPurch_count(BigDecimal purchCount) {
		purch_count = purchCount;
	}
	public String getMeasure() {
		return measure;
	}
	public void setMeasure(String measure) {
		this.measure = measure;
	}
	public BigDecimal getPurch_tax() {
		return purch_tax;
	}
	public void setPurch_tax(BigDecimal purchTax) {
		purch_tax = purchTax;
	}
	public BigDecimal getSum_bon_purch() {
		return sum_bon_purch;
	}
	public void setSum_bon_purch(BigDecimal sumBonPurch) {
		sum_bon_purch = sumBonPurch;
	}
	public BigDecimal getSum_purch() {
		return sum_purch;
	}
	public void setSum_purch(BigDecimal sumPurch) {
		sum_purch = sumPurch;
	}
	public BigDecimal getSum_pay_cash_purch() {
		return sum_pay_cash_purch;
	}
	public void setSum_pay_cash_purch(BigDecimal sumPayCashPurch) {
		sum_pay_cash_purch = sumPayCashPurch;
	}
	public BigDecimal getSum_pay_bon_purch() {
		return sum_pay_bon_purch;
	}
	public void setSum_pay_bon_purch(BigDecimal sumPayBonPurch) {
		sum_pay_bon_purch = sumPayBonPurch;
	}
	public BigDecimal getSum_pay_card_purch() {
		return sum_pay_card_purch;
	}
	public void setSum_pay_card_purch(BigDecimal sumPayCardPurch) {
		sum_pay_card_purch = sumPayCardPurch;
	}
	public String getBon_card_nr() {
		return bon_card_nr;
	}
	public void setBon_card_nr(String bonCardNr) {
		bon_card_nr = bonCardNr;
	}
	

}
