package bc.payment.sberbank.domain;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;

import bc.payment.exception.MarshallingException;
import bc.payment.exception.SecurityCheckException;
import bc.payment.sberbank.service.MarshallerSberbank;
import bc.payment.sberbank.service.SberbankDateConverter;


public class Request {

	private Parameters params;

	private String sign;

	public Request(Parameters parameters){
		this.params=parameters;
	}

	public Parameters getParams() {
		return this.params;
	}

	public String getSign() {
		return this.sign;
	}

	private final static String MARKER_PARAM_BEGIN="<params>";
	private final static String MARKER_PARAM_END="</params>";

	private final static String MARKER_SIGN_BEGIN="<sign>";
	private final static String MARKER_SIGN_END="</sign>";

	public static String toXml(Request request){
		String params=MarshallerSberbank.toXmlStringWithoutHeader(request.params);

		StringBuilder returnValue=new StringBuilder();
		returnValue.append(MarshallerSberbank.getXmlHeader());
		returnValue.append("<request>");
		returnValue.append(params);
		returnValue.append("<sign>");
		returnValue.append(MarshallerSberbank.calculateMd5(StringUtils.substringBefore(StringUtils.substringAfter(params, Request.MARKER_PARAM_BEGIN), Request.MARKER_PARAM_END)));
		returnValue.append("</sign>");
		returnValue.append("</request>");
		return returnValue.toString();
	}

	public static Parameters fromXmlAndCheck(String xmlRepresentation) throws SecurityCheckException, MarshallingException{
		String valueForCheck=StringUtils.substringAfter(xmlRepresentation, Request.MARKER_PARAM_BEGIN);
		valueForCheck=StringUtils.substringBeforeLast(valueForCheck, Request.MARKER_PARAM_END);
		String signValue=StringUtils.substringAfterLast(xmlRepresentation, Request.MARKER_SIGN_BEGIN);
		signValue=StringUtils.trim(StringUtils.substringBeforeLast(signValue, Request.MARKER_SIGN_END));

		if(!StringUtils.equalsIgnoreCase(signValue, MarshallerSberbank.calculateMd5(valueForCheck))){
			throw new SecurityCheckException("MD5 value is not as expected");
		}

		return MarshallerSberbank.getFromXmlString(Request.MARKER_PARAM_BEGIN+valueForCheck+Request.MARKER_PARAM_END, Request.Parameters.class);
	}


	@Root(name="params")
	public static class Parameters{

		private static enum Type{
			CHECK(1),
			PAYMENT(2);

			private final int value;
			Type(int typeValue){
				this.value=typeValue;
			}
		}

		public boolean isCheck(){
			return Type.CHECK.value==this.act;
		}
		public boolean isPayment(){
			return Type.PAYMENT.value==this.act;
		}


		@Element(name="act")
		private int act;
		@Element(name="agent_code", required=false)
		private int agentCode;
		@Element(name="agent_date", required=false)
		@Convert(SberbankDateConverter.class)
		private Date agentDate;
		@Element(name="serv_code", required=false)
		private String servCode;
		@Element(name="account")
		private String account;
		@Element(name="pay_amount", required=false)
		private int payAmount; // amount in cents !!!
		@Element(name="pay_id", required=false)
		private long payId;

		public Parameters(){
		}

		public Parameters(int act, int agentCode, Date agentDate,
				String servCode, String account, int payAmount, int payId) {
			super();
			this.act = act;
			this.agentCode = agentCode;
			this.agentDate = agentDate;
			this.servCode = servCode;
			this.account = account;
			this.payAmount = payAmount;
			this.payId=payId;
		}

		public int getAct() {
			return this.act;
		}

		public void setAct(int act) {
			this.act = act;
		}
		public int getAgentCode() {
			return this.agentCode;
		}
		public void setAgentCode(int agentCode) {
			this.agentCode = agentCode;
		}
		public Date getAgentDate() {
			return this.agentDate;
		}
		public void setAgentDate(Date agentDate) {
			this.agentDate = agentDate;
		}
		public String getServCode() {
			return this.servCode;
		}
		public void setServCode(String servCode) {
			this.servCode = servCode;
		}
		public String getAccount() {
			return this.account;
		}
		public void setAccount(String account) {
			this.account = account;
		}
		public int getPayAmount() {
			return this.payAmount;
		}
		public void setPayAmount(int payAmount) {
			this.payAmount = payAmount;
		}
		public long getPayId() {
			return payId;
		}
		public void setPayId(long payId) {
			this.payId = payId;
		}
		
		@Override
		public String toString() {
			return "Parameters [act=" + act + ", agentCode=" + agentCode + ", agentDate=" + agentDate + ", servCode="
					+ servCode + ", account=" + account + ", payAmount=" + payAmount + ", payId=" + payId + "]";
		}
		
	}

}
