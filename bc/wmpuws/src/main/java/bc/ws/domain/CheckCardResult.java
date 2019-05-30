package bc.ws.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * result of checking the card by number  
 */
@XmlRootElement(name = "checkCardResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class CheckCardResult extends CommonResult{
    @XmlElement(name = "cardStatusId")
	private int idCardStatus;
	private String membershipMonthSum;
	private String membershipLastDate;
	private String membershipNopayMonth;
	private String membershipNeedPaySum;
	private String membershipMaxPayMonth;
	private String membershipCdCurrency;
	private String membershipFeeMargin;

	public CheckCardResult() {
		super();
	}

	public CheckCardResult(String returnCode, String message) {
		super(returnCode, message);
	}

	// -- ИД вида карты (не используется)
	// declareParameter(new SqlOutParameter("", Types.VARCHAR, 10));
	public void setIdCardStatus(int object) {
		idCardStatus=object;
	}

	// -- Сумма ежемесячного членского взноса (в рублях с копейками)
	// declareParameter(new SqlOutParameter("", Types.VARCHAR, 50));
	public void setMembershipMonthSum(String string) {
		membershipMonthSum=string;
	}

	// -- Дата, до которой оплачен членский взнос
	// declareParameter(new SqlOutParameter("", Types.VARCHAR, 50));
	public void setMembershipLastDate(String string) {
		membershipLastDate=string;
	}

	// -- Количество неоплаченных месяцев членского взноса
	// declareParameter(new SqlOutParameter("", Types.VARCHAR, 50));
	public void setMembershipNopayMonth(String string) {
		membershipNopayMonth=string;
	}

	// -- Сумма членского взноса, которую необходимо оплатить
	// declareParameter(new SqlOutParameter("", Types.VARCHAR, 50));
	public void setMembershipNeedPaySum(String string) {
		membershipNeedPaySum=string;
	}

	// -- Максимальное количество месяцев, за которые можно оплатить членский взнос
	// declareParameter(new SqlOutParameter("", Types.VARCHAR, 50));
	public void setMembershipMaxPayMonth(String string) {
		membershipMaxPayMonth=string;
	}

	// -- Валюта, в которой принимается членский взнос
	// declareParameter(new SqlOutParameter("", Types.VARCHAR, 50));
	public void setMembershipCdCurrency(String string) {
		membershipCdCurrency=string;
	}

	// -- Комиссия партнера за прием членского взноса
	// declareParameter(new SqlOutParameter("", Types.VARCHAR, 50));
	public void setMembershipFeeMargin(String string) {
		membershipFeeMargin=string;
	}

	public int getIdCardStatus() {
		return idCardStatus;
	}

	public String getMembershipMonthSum() {
		return membershipMonthSum;
	}

	public String getMembershipLastDate() {
		return membershipLastDate;
	}

	public String getMembershipNopayMonth() {
		return membershipNopayMonth;
	}

	public String getMembershipNeedPaySum() {
		return membershipNeedPaySum;
	}

	public String getMembershipMaxPayMonth() {
		return membershipMaxPayMonth;
	}

	public String getMembershipCdCurrency() {
		return membershipCdCurrency;
	}

	public String getMembershipFeeMargin() {
		return membershipFeeMargin;
	}

}
