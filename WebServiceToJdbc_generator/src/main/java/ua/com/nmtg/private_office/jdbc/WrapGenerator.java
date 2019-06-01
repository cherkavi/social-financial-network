package ua.com.nmtg.private_office.jdbc;

import java.sql.Connection;
import java.util.Collection;

import ua.com.nmtg.private_office.jdbc.destination.JavaBeanDestinationPoint;
import ua.com.nmtg.private_office.jdbc.destination.PhpBeanDestinationPoint;
import ua.com.nmtg.private_office.jdbc.source.IParametersSource;
import ua.com.nmtg.private_office.jdbc.source.Parameter;
import ua.com.nmtg.private_office.jdbc.source.oracle.OracleConnection;
import ua.com.nmtg.private_office.jdbc.source.oracle.OracleProcedureBuilder;
import ua.com.nmtg.private_office.jdbc.destination.OracleProcedureHelperDestinationPoint;
import ua.com.nmtg.private_office.jdbc.destination.decorator.IConditionDecorator;
import ua.com.nmtg.private_office.web_service.generator.code_description.parser.exceptions.AnalizeParserException;
import ua.com.nmtg.private_office.web_service.writer.IWriter;

/**
 * класс для генерации вспомогательного кода при работе с процедурами: 
 */
public class WrapGenerator implements IWriter{
	
	public static void main(String[] args) throws Exception{
		System.out.println("begin");

		String url="jdbc:oracle:thin:@91.195.53.27:1521:demo";
        String username="bc_office"; 
        String password="office";
        String ownerName="bc_admin";
        String procedureName="check_nat_prs";
        String pathToOutput="c:\\temp\\";
        String javaPackageName="com.ntmg";
        
    	new WrapGenerator(url, username, password, ownerName, procedureName, javaPackageName, pathToOutput).write();
        
		System.out.println("-end-");
	}
	
	/**
	 * 
	 * @param url - JDBC url
	 * @param username - name of user
	 * @param password - name of password
	 * @param ownerName - name of owner of the procedure 
	 * @param procedureName - name of procedure 
	 * @param javaPackageName - java package name for generator 
	 * @param pathToOutput - path to output files 
	 */
	public WrapGenerator(String url, 
						 String username, 
						 String password, 
						 String ownerName, 
						 String procedureName, 
						 String javaPackageName, 
						 String pathToOutput){
		this.url=url; 
		this.username=username; 
		this.password=password; 
		this.ownerName=ownerName; 
		this.procedureName=procedureName;
		this.javaPackageName=javaPackageName; 
		this.pathToOutput=pathToOutput;		
	}

	private String url; 
	private String username; 
	private String password;
	private String ownerName; 
	private String procedureName; 
	private String javaPackageName; 
	private String pathToOutput;		
	
	@Override
	public void write() throws AnalizeParserException {
		
        String nameOfInputParameters=procedureName+"_in";
        String nameOfOutputParameters=procedureName+"_out";

        Connection connection=null;
		try{
			connection=OracleConnection.getConnection(url, username, password);
		}catch(Exception ex){
			System.out.println("Check connection with DB:"+ex.getMessage());
			return;
		}
		
		try{
			IParametersSource parametersSource= new OracleProcedureBuilder().getParameterSource(connection, ownerName, procedureName);
			
			Collection<Parameter> inDirection=Parameter.getParameterByDirection(parametersSource.getParameters(), Parameter.Direction.IN);
			new JavaBeanDestinationPoint(pathToOutput+nameOfInputParameters+".java", 
										 nameOfInputParameters, 
										 javaPackageName
										 ).write(inDirection);
			
			Collection<Parameter> outDirection=Parameter.getParameterByDirection(parametersSource.getParameters(), Parameter.Direction.OUT);
			new JavaBeanDestinationPoint(pathToOutput+nameOfOutputParameters+".java", 
										 nameOfOutputParameters, 
										 javaPackageName
										 ).write(outDirection);

			new PhpBeanDestinationPoint(pathToOutput+nameOfInputParameters+".php", 
										 nameOfInputParameters,
										 null,
										 new IConditionDecorator() {
											@Override
											public boolean isNeedToExecute(Parameter parameter) {
												return "java.lang.String".equals(Parameter.getJavaType(parameter));
											}
											@Override
											public String decorate(String value) {
												return "urlencode("+value+")";
											}
										}
										 ).write(inDirection);
			new PhpBeanDestinationPoint(pathToOutput+nameOfOutputParameters+".php", 
										 nameOfOutputParameters,
										 new IConditionDecorator() {
											@Override
											public boolean isNeedToExecute(Parameter parameter) {
												return "java.lang.String".equals(Parameter.getJavaType(parameter));
											}
											@Override
											public String decorate(String value) {
												return "urldecode("+value+")";
											}
										},
										null
										 ).write(outDirection);
			
			new OracleProcedureHelperDestinationPoint(pathToOutput,
													  nameOfOutputParameters+"_helper", 
													  nameOfInputParameters, 
													  nameOfOutputParameters,  
													  procedureName, 
													  parametersSource.getOwner()+"."+parametersSource.getPackage(),
													  javaPackageName).write(parametersSource.getParameters());
		}catch(Exception ex){
			throw new AnalizeParserException(ex.getMessage());
		}
	}
}
