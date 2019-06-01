package ua.com.nmtg.private_office.jdbc.destination;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;


import ua.com.nmtg.private_office.jdbc.destination.exception.DestinationException;
import ua.com.nmtg.private_office.jdbc.source.Parameter;

public class OracleProcedureHelperDestinationPoint implements IDestinationPoint{
	private String pathToOutput;
	private String className;
	private String inputClassName;
	private String outputClassName;
	private String procedureName;
	private String packageName;
	private String procedurePackage;
	/**
	 * @param pathToOutput - file for write results
	 * @param className - name of create class
	 * @param inputClassName - name of input parameter for procedure
	 * @param outputClassName - name of output parameter for procedure
	 * @praam oracleProcedureName - name of Oracle procedure 
	 * @param oracleProcedurePackage - name of Oracle package ( package of procedure )
	 * @param oracleProcedureName - name of procedure
	 */
	public OracleProcedureHelperDestinationPoint(String pathToOutput, 
												 String className, 
												 String inputClassName, 
												 String outputClassName, 
												 String oracleProcedureName,
												 String oracleProcedurePackage,
												 String packageName
												 ){
		this.pathToOutput=pathToOutput;
		this.className=className;
		this.inputClassName=inputClassName;
		this.outputClassName=outputClassName;
		this.procedureName=oracleProcedureName;
		this.procedurePackage=oracleProcedurePackage;
		this.packageName=packageName;
	}
	
	protected static final String space="    ";
	protected static final String newLine="\n";
	
	@Override
	public void write(Collection<Parameter> parameterCollection) throws DestinationException {
		try{
			
			StringBuilder returnValue=new StringBuilder();
			returnValue
				.append("package ").append(packageName).append(";").append(newLine)
				.append("import java.net.URLEncoder;").append(newLine)
				.append("import java.net.URLDecoder;").append(newLine)
				.append("import java.sql.CallableStatement;").append(newLine)
				.append("import java.sql.Connection;").append(newLine)
				.append("import java.sql.Types;").append(newLine)
				.append("import javax.sql.DataSource;").append(newLine)
				.append("import org.apache.log4j.BasicConfigurator;").append(newLine)
				.append("import org.apache.log4j.Logger;").append(newLine)
				.append(newLine)
				.append(newLine)
				.append("public class ").append(this.className).append(" ").append("{").append(newLine)
				.append(space).append("private DataSource dataSource;").append(newLine)
				.append(newLine)
				.append(space).append("public ").append(this.className).append("(DataSource dataSource){this.dataSource=dataSource;}").append(newLine)
				.append(space).append("public ").append(Parameter.getJavaType(Parameter.getReturnParameterWithoutName(parameterCollection))).append(" ")
					.append(this.procedureName).append("(").append(this.inputClassName).append(" ").append("input, ").append(this.outputClassName).append(" output").append(") throws java.sql.SQLException{").append(newLine)
				.append(space).append("if (output==null)output=new "+this.outputClassName+"();").append(newLine)
				.append(space).append("Connection connection=null;CallableStatement statement=null;").append(newLine)
				.append(space).append("connection=this.dataSource.getConnection();").append(newLine)
				.append(space).append("statement = connection.prepareCall(\"{").append(createProcedureSql(this.procedureName, parameterCollection)).append("}\");").append(newLine);
			// set input parameters
			for(Parameter parameter: parameterCollection){
				if(Parameter.Direction.IN.equals(parameter.getDirection())){
					if("java.lang.String".equals(Parameter.getJavaType(parameter))){
						returnValue.append(space).append("statement.set").append(Parameter.getStatementParameter(parameter))
												 .append("(").append(parameter.getPosition()+1).append(", ").append("URLDecoder.decode(input.").append(Parameter.getNameOfGetter(parameter)).append("())")
												 .append(");").append(newLine);
						
					}else{
						returnValue.append(space).append("statement.set").append(Parameter.getStatementParameter(parameter))
												 .append("(").append(parameter.getPosition()+1).append(", ").append("input.").append(Parameter.getNameOfGetter(parameter)).append("()")
												 .append(");").append(newLine);
						
					}
				}
			}
			// register output 
			for(Parameter parameter: parameterCollection){
				if(Parameter.Direction.OUT.equals(parameter.getDirection())){
					returnValue.append(space).append("statement.registerOutParameter")
											 .append("(").append(parameter.getPosition()+1).append(", ").append("Types.").append(Parameter.getSqlType(parameter))
											 .append(");").append(newLine);
				}
			}
			// execute
			returnValue.append(space).append("statement.executeUpdate();").append(newLine);
			// fill output parameters
			for(Parameter parameter: parameterCollection){
				if(Parameter.Direction.OUT.equals(parameter.getDirection()) && parameter.getName()!=null){
					if("java.lang.String".equals(Parameter.getJavaType(parameter))){
						// output.setP_card_name_issuer( (statement.getString(9)==null)?null:URLEncoder.encode(statement.getString(9)));
						returnValue.append(space).append("output.").append(Parameter.getNameOfSGetter(parameter)).append("(")
						 .append("(statement.get").append(Parameter.getStatementParameter(parameter)).append("(").append(parameter.getPosition()+1).append(")==null)?null:URLEncoder.encode(")
						 .append("statement.get").append(Parameter.getStatementParameter(parameter)).append("(").append(parameter.getPosition()+1).append(")").append(")")
						 .append(");").append(newLine);
						
					}else{
						returnValue.append(space).append("output.").append(Parameter.getNameOfSGetter(parameter)).append("(")
						 .append("statement.get").append(Parameter.getStatementParameter(parameter)).append("(").append(parameter.getPosition()+1).append(")")
						 .append(");").append(newLine);
					}
				}
			}
			// return value
			Parameter returnParameter=Parameter.getReturnParameterWithoutName(parameterCollection);
			returnValue.append(space).append("return statement.get").append(Parameter.getStatementParameter(returnParameter)).append("("+(returnParameter.getPosition()+1)+");").append(newLine)
			.append(space).append("}").append(newLine)
			.append("}");
			FileUtils.writeStringToFile(new File(this.pathToOutput+""+this.className+".java"), returnValue.toString());
		}catch(Exception ex){
			System.err.println(this.getClass().getName()+"  Exception:"+ex.getMessage());
			throw new DestinationException(ex.getMessage());
		}
	}

	/**
	 * @param procedureName2 - name of procedure ( for example: check_nat_prs )
	 * @param parameterSource - collection of parameters
	 * @return
	 * ?= call check_nat_prs(?, ?, ?, ?)
	 */
	private StringBuilder createProcedureSql(String procedureName2,
			Collection<Parameter> parameterSource) {
		StringBuilder returnValue=new StringBuilder();
		returnValue.append("?= call ").append(StringUtils.trimToEmpty(this.procedurePackage.trim())).append(".").append(procedureName2).append("(");
		for(int index=0;index<parameterSource.size()-1;index++){
			if(index>0){
				returnValue.append(", ");
			}
			returnValue.append("?");
		}
		returnValue.append(")");
		return returnValue;
	}

}
