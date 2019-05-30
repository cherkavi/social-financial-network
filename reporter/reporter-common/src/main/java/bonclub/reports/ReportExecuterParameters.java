package bonclub.reports;

import java.io.ByteArrayOutputStream;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;


public class ReportExecuterParameters {
	/** полный путь к отчетам  */
	public static String pathToPattern;
	/** полный путь к выходным отчетам (сформированным файлам ) */
	public static String pathToOutputReport;
	/** путь к месту складирования файлов с параметрами */
	public static String pathToParameters;
	/** полный путь к сервлету, который выдает файлы  */
	public static String urlToReportServlet;
	private Logger logger=Logger.getLogger(this.getClass());
	/*
	private static void controlSave(String fileName){
		try{
			FileOutputStream fos=new FileOutputStream(fileName);
			fos.write(10);
			fos.flush();
			fos.close();
		}catch(Exception ex){
			System.err.println("controlSave Exception:"+ex.getMessage());
		}
	}*/
	/** загрузка всех переменных, которые содержат пути к каталогам для выгрузки/загрузки файлов (шаблонов, отчетов, сериализованных параметров )*/
	public static void loadVariables(String pathToFile){
		try{
			// прочесть значения из Property файла
			Properties properties=new Properties();
			properties.load(new FileInputStream(pathToFile));
			// получить разделитель для файлов
			// String separator=System.getProperty("file.separator");
			pathToPattern=properties.getProperty("path_to_pattern");
			pathToOutputReport=properties.getProperty("path_to_output_report");
			pathToParameters=properties.getProperty("path_to_parameters");
			urlToReportServlet=properties.getProperty("url_to_reporter");
		}catch(Exception ex){
			System.err.println("loadVariable from file <"+pathToFile+"> Exception:"+ex.getMessage());
		}
	}

	/** обработать строку и присоединить к ней разделитель файлов, если он не обнаружен в конце строки 
	 * @param value - строка, которая содержит путь к каталогу
	 * @param separator - разделитель для файлов 
	 * @return
	private static String checkEndSeparator(String value,String separator){
		if(value!=null){
			String returnValue=value.trim();
			if(!returnValue.endsWith(separator)){
				if(returnValue.length()==0){
					return returnValue;
				}else{
					return returnValue+separator;
				}
			}else{
				return returnValue;
			}
		}else{
			return null;
		}
	}
	 */
	
	private int userId;
	private int reportId;
	private ArrayList<ReportExecuterQueryParameters> queryOfReports;
	private String outputFormatStringRepresent;
	private HashMap<String,Object> parameters;
	private HashMap<String,String> jasperParameters;
	
	private void clear(){
		userId=0;
		reportId=0;
		queryOfReports=new ArrayList<ReportExecuterQueryParameters>();
		outputFormatStringRepresent="";
		parameters=new HashMap<String,Object>();
		jasperParameters=new HashMap<String,String>();
	}
	
	/** получение параметров из потока байт 
	 * @param array - поток байт, который содержит сериализованный поток объектов
	 * <br>
	 * порядок чтения полей  
	 *  <table border=1>
	 *  	<tr><td>reportId</td> </tr>
	 *  	<tr><td>userId</td> </tr>
	 *  	<tr><td>queryOfReports</td> </tr>
	 *  	<tr><td>outputFormatStaringRepresent</td></tr>
	 *  	<tr><td>parameters</td></tr>
	 *  	<tr><td>jasperParameters</td></tr>
	 *  </table>  
	 * */
	@SuppressWarnings("unchecked")
	public boolean loadFromByteArray(InputStream inputStream ) throws Exception{
		logger.debug("loadFromByteArray begin");
		clear();
		boolean returnValue=false;
		try{
			ObjectInputStream inputObject=new ObjectInputStream(inputStream);
			this.reportId=inputObject.readInt();
			this.userId=inputObject.readInt();
			this.queryOfReports=(ArrayList<ReportExecuterQueryParameters>)inputObject.readObject();
			this.outputFormatStringRepresent=(String)inputObject.readObject();
			this.parameters=(HashMap<String,Object>)inputObject.readObject();
			this.jasperParameters=(HashMap<String,String>)inputObject.readObject();
			logger.debug("loadFromByteArray OK");
			returnValue=true;
		}catch(Exception ex){
			this.clear();
			returnValue=false;
			logger.debug("loadFromByteArray Exception:"+ex.getMessage());
		}
		return returnValue;
	}
	 
	/** преобразование полей данного объекта в поток байт
	 *  @return получить поток байт из сериализованных полей данного объекта 
	 *  <table border=1>
	 *  	<tr><td>reportId</td> </tr>
	 *  	<tr><td>userId</td> </tr>
	 *  	<tr><td>queryOfReports</td> </tr>
	 *  	<tr><td>outputFormatStaringRepresent</td></tr>
	 *  	<tr><td>parameters</td></tr>
	 *  	<tr><td>jasperParameters</td></tr>
	 *  </table>  
	 *  <br>
	 *  возвращает null если произошла ошибка сохранения
	 * */
	public byte[] saveToByteArray(){
		ByteArrayOutputStream output=null;
		try{
			output=new ByteArrayOutputStream();
			ObjectOutputStream outputObject=new ObjectOutputStream(output);
			outputObject.writeInt(this.reportId);
			outputObject.writeInt(this.userId);
			outputObject.writeObject(this.queryOfReports);
			outputObject.writeObject(this.outputFormatStringRepresent);
			outputObject.writeObject(this.parameters);
			outputObject.writeObject(this.jasperParameters);
			output.flush();
			return output.toByteArray();
		}catch(Exception ex){
			return null;
		}finally{
			try{
				output.close();
			}catch(Exception ex){};
		}
	}

	/** уникальный идентификатор отчета  */
	public int getReportId() {
		return reportId;
	}

	/** уникальный идентификатор отчета  */
	public void setReportId(int reportId) {
		this.reportId = reportId;
	}

	/** параметры запроса */
	public ArrayList<ReportExecuterQueryParameters> getQueryOfReports() {
		return queryOfReports;
	}

	/** параметры запроса */
	public void setQueryOfReports(
			ArrayList<ReportExecuterQueryParameters> queryOfReports) {
		this.queryOfReports = queryOfReports;
	}

	/** формат исходящих данных (PDF, XLS, RTF, HTML) */
	public String getOutputFormatStringRepresent() {
		return outputFormatStringRepresent;
	}

	/** формат исходящих данных (PDF, XLS, RTF, HTML) */
	public void setOutputFormatStringRepresent(String outputFormatStringRepresent) {
		this.outputFormatStringRepresent = outputFormatStringRepresent;
	}

	/** параметры  */
	public HashMap<String, Object> getParameters() {
		return parameters;
	}

	/** параметры  */
	public void setParameters(HashMap<String, Object> parameters) {
		this.parameters = parameters;
	}

	/** параметры для JasperReport */
	public HashMap<String, String> getJasperParameters() {
		return jasperParameters;
	}

	/** параметры для JasperReport */
	public void setJasperParameters(HashMap<String, String> jasperParameters) {
		this.jasperParameters = jasperParameters;
	}

	/** идентификатор пользователя, по которому данный отчет создается  */
	public int getUserId() {
		return userId;
	}

	/** идентификатор пользователя, по которому данный отчет создается  */
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
}
