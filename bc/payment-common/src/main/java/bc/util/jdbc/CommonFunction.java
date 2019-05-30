package bc.util.jdbc;

import java.sql.Types;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

public class CommonFunction extends StoredProcedure{
	private final static String RESULT_POSITIVE="0";
	private final static String RESULT_PARAM="result";
	
	protected CommonFunction(DataSource dataSource, String procedureName, SqlParameter ... parameters){
		super(dataSource, procedureName);
		declareParameter(new SqlOutParameter(RESULT_PARAM, Types.VARCHAR));
		if(parameters!=null && parameters.length!=0){
			for(SqlParameter eachParameter:parameters){
				declareParameter(eachParameter);
			}
		}
		setFunction(true);
		compile();
	}

	/**
	 * use it in execute method  
	 * @param in
	 * @return
	 */
	protected boolean executeAndParseResult(Map<String, Object> in) {
		Map<String, Object> out = execute(in);
		if (!out.isEmpty())
			return RESULT_POSITIVE.equals(out.get(RESULT_PARAM));
		else
			return false;
	}
	
}
