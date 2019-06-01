package ua.com.nmtg.private_office.web_service.generator.name_decorator;

import org.apache.commons.lang.StringUtils;

public class PrePostNameDecorator implements INameDecorator{
	private final String preambula;
	private final String postambula;
	
	public PrePostNameDecorator(String preambula, String postambula){
		this.preambula=StringUtils.trimToNull(preambula);
		this.postambula=StringUtils.trimToNull(postambula);
	}
	
	@Override
	public String decorate(String value) {
		return ( (this.preambula==null)?"":this.preambula) 
					+ StringUtils.trimToEmpty(value)
						+ ((this.postambula==null)?"":this.postambula); 
	}

}
