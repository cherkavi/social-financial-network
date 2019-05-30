package bonclub.reports.web_service.common;

/** описание удаленного файла на сервере для получения отчетов  */
public class RemoteReportFileDescription {
	private String url;
	private String format;
	
	/** описание удаленного файла на сервере для получения отчетов  */
	public RemoteReportFileDescription(){
	}
	
	/** описание удаленного файла на сервере для получения отчетов  
	 * @param url - ссылка для получения данного отчета 
	 * @param format 
	 * <table>
	 * 	<tr>
	 * 		<td>pdf</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>xls</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>rtf</td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td>html</td>
	 * 	</tr>
	 * </table>
	 */
	public RemoteReportFileDescription(String url, String format){
		this.url=url;
		this.format=format;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}
	
	
}
