package bc.payments.sberbank.report.task.domain;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="registry", strict=false)
public class SberbankReport {
	
	@Attribute(name="format")
	private String format;
	
	@Attribute(name="form_date")
	private String formatDate;
	
	@Element(name="reg_date")
	private String regDate;
	
	@Element(name="agent_name")
	private String agentName;

	@Element(name="prov_name")
	private String provName;

	@ElementList(name="pays")
	private List<ReportPayRecord> listOfPays;

	
	public String getFormat() {
		return format;
	}

	public String getFormatDate() {
		return formatDate;
	}

	public String getRegDate() {
		return regDate;
	}

	public String getAgentName() {
		return agentName;
	}

	public String getProvName() {
		return provName;
	}

	public List<ReportPayRecord> getListOfPays() {
		return listOfPays;
	}
	
	
}
