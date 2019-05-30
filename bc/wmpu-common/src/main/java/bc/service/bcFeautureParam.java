package bc.service;

import org.apache.commons.lang.StringUtils;

public class bcFeautureParam {
	
	/*private int orderNumber;*/
	private String dataType;
	private String dataValue;

	/*
	public bcFeautureParam(String pOrderNumber, String pDataType, String pDataValue) {
		this.orderNumber = Integer.parseInt(pOrderNumber);
		this.dataType = pDataType;
		this.dataValue = pDataValue;
	}
	*/

	public bcFeautureParam(/*int pOrderNumber,*/ String pDataType, String pDataValue) {
		//this.orderNumber = pOrderNumber;
		//LOGGER.debug("pOrderNumber=" + pOrderNumber);
		this.dataType = StringUtils.trimToEmpty(pDataType);
		this.dataValue = pDataValue;
	}
	
	public String getDataType() {
		// TODO need to change to ENUM
		return this.dataType;
	}
	
	public String getValue() {
		return this.dataValue;
	}
	
}
