package ua.com.nmtg.private_office.jdbc.source.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import ua.com.nmtg.private_office.jdbc.source.exception.SourceException;
import ua.com.nmtg.private_office.jdbc.source.IParametersSource;
import ua.com.nmtg.private_office.jdbc.source.Parameter;
import ua.com.nmtg.private_office.jdbc.source.ParameterSourceContainer;

public class OracleProcedureBuilder {
	private static Logger logger=Logger.getLogger(OracleProcedureBuilder.class);
	
	private Parameter.Direction getDirectionByName(String directionName){
		String clearName=StringUtils.trimToNull(directionName);
		if(clearName==null){
			return Parameter.Direction.IN;
		}
		if(clearName.equalsIgnoreCase("OUT")){
			return Parameter.Direction.OUT;
		}
		return Parameter.Direction.IN;
	}
	
	/**
	 * 
	 * @param connection - database connection 
	 * @param ownerName - name of owner 
	 * @param nameOfProcedure - name of procedure 
	 * @return
	 * @throws SourceException - if something wrong 
	 */
	public IParametersSource getParameterSource(Connection connection,
												String ownerName,
											    String nameOfProcedure) throws SourceException{
		PreparedStatement ps=null;
		ResultSet rs=null;
		TreeSet<Parameter> set=new TreeSet<Parameter>();
		String nameOfPackage=null;
		try{
			StringBuilder sql=new StringBuilder();
			sql
			.append("select a.object_name,a.argument_name,a.position,a.sequence,a.data_type,a.in_out,a.package_name \n")
			.append("from all_arguments a \n")
			.append("where 1=1 \n")
			.append("and upper(object_name)=upper(?)")
			.append("and upper(owner)=upper(?)")
			.append("order by a.position");
			ps=connection.prepareStatement(sql.toString());
			ps.setString(1, nameOfProcedure);
			ps.setString(2, ownerName);
			rs=ps.executeQuery();
			while(rs.next()){
				if(nameOfPackage==null){
					nameOfPackage=rs.getString("package_name");
				}
				set.add(new Parameter(rs.getInt("position"), 
									  this.getDirectionByName(rs.getString("in_out")), 
									  rs.getString("argument_name"), 
									  rs.getString("data_type"))
						);
			}
		}catch(SQLException sqlEx){
			logger.error("try to read parameters from Database Exception:"+sqlEx.getMessage());
			throw new SourceException(sqlEx.getMessage());
		}finally{			
			try{
				if(ps!=null){
					ps.close();
				}
			}catch(Exception ex){
				System.err.println("PreparedStatement close Exception:"+ex.getMessage());
			};
		}
		
		return new ParameterSourceContainer(ownerName, nameOfPackage, set);
	}

	
	
}

