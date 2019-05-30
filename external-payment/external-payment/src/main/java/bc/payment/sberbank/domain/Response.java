package bc.payment.sberbank.domain;

import org.apache.commons.lang3.StringUtils;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import bc.payment.sberbank.service.MarshallerSberbank;


public class Response {

	private Parameters params;

	private String sign;

	public Response(Parameters parameters){
		this.params=parameters;
	}

	public Parameters getParams() {
		return this.params;
	}

	public String getSign() {
		return this.sign;
	}

	public static String toXml(Response.Parameters responseParameters){
		String params=MarshallerSberbank.toXmlStringWithoutHeader(responseParameters);

		StringBuilder returnValue=new StringBuilder();
		returnValue.append(MarshallerSberbank.getXmlHeader());
		returnValue.append("<response>");
		returnValue.append(params);
		returnValue.append("<sign>");
		returnValue.append(StringUtils.removeEnd(StringUtils.removeStart(MarshallerSberbank.calculateMd5(params), "<params>"), "</params>"));
		returnValue.append("</sign>");
		returnValue.append("</response>");
		return returnValue.toString();
	}

	@Root(name="params")
	public static class Parameters{
		@Element(name="err_code")
		private int errorCode; // change to enum
		@Element(name="err_text", required=false)
		private String errorText;
		@Element(name="account")
		private String account;
		@Element(name="desired_amount", required=false)
		private Integer desiredAmount; // amount in cents !!!
		@Element(name="client_name", required=false)
		private String clientName;
		@Element(name="balance", required=false)
		private String balance; // delimiter DOT !!!

		public Parameters(){
		}

		public Parameters(int errorCode, String errorText, String account,
				int desiredAmount, String clientName, String balance) {
			super();
			this.errorCode = errorCode;
			this.errorText = errorText;
			this.account = account;
			this.desiredAmount = desiredAmount;
			this.clientName = clientName;
			this.balance = balance;
		}
		
		public Parameters(String account) {
			super();
			this.errorCode = 0;
			this.errorText = "OK";
			this.account = account;
		}


		public int getErrorCode() {
			return this.errorCode;
		}
		public String getErrorText() {
			return this.errorText;
		}
		public String getAccount() {
			return this.account;
		}
		public Integer getDesiredAmount() {
			return this.desiredAmount;
		}
		public String getClientName() {
			return this.clientName;
		}
		public String getBalance() {
			return this.balance;
		}


	}

}
