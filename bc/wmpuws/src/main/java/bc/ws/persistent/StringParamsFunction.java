package bc.ws.persistent;

import bc.ws.exception.PersistentException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.object.StoredProcedure;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;


abstract class StringParamsFunction<T> extends StoredProcedure {
    private final static Logger LOGGER=Logger.getLogger(StringParamsFunction.class);
    protected final static String RESULT_PARAM="result";
    protected final static String RESULT_MESSAGE="result_message";

    private final String[] inputParametersName;
    private final String functionName;

    protected static DataSource createFrom(Connection connection){
        return new SingleConnectionDataSource(connection, false);
    }

    StringParamsFunction(DataSource dataSource, String procedureName, String[] inputParameters, String[] outputParameters){
        super(dataSource, procedureName);
        functionName=procedureName;

        declareParameter(new SqlOutParameter(RESULT_PARAM, Types.VARCHAR, 50));
        inputParametersName=inputParameters;
        if(inputParameters!=null && inputParameters.length>0){
            for(int index=0;index<inputParameters.length; index++){
                declareParameter(new SqlParameter(inputParameters[index], java.sql.Types.VARCHAR, 50));
            }
        }

        if(outputParameters!=null && outputParameters.length>0){
            for(int index=0;index<outputParameters.length; index++){
                declareParameter(new SqlOutParameter(outputParameters[index], java.sql.Types.VARCHAR, 50));
            }
        }

        declareParameter(new SqlOutParameter(RESULT_MESSAGE, Types.VARCHAR, 4000));
        setFunction(true);
        compile();
    }

    T executeFunction(Object ... inputValues) throws PersistentException {
        Map<String, Object> in=new HashMap<String, Object>();

        if(inputValues!=null && inputValues.length>0){
            for(int counter=0;counter<inputValues.length; counter++){
                if(inputValues[counter]==null){
                    in.put(inputParametersName[counter], null);
                }else{
                    in.put(inputParametersName[counter], inputValues[counter].toString());
                }
            }
        }
        CardOperations.LOGGER.debug("function name: "+this.functionName);
        CardOperations.LOGGER.debug(">>>  params: "+in.toString());

        Map<String, Object> out = execute(in);
        CardOperations.LOGGER.debug("<<<  results: "+out);
        if (out.isEmpty()){
            LOGGER.warn("message : "+out.get(RESULT_MESSAGE));
            throw new PersistentException(functionName+": operation is not successfull: ");
        }
        return convert(out);
    }

    protected abstract T convert(Map<String, Object> out);

    protected String getString(Map<String, Object> out, String nameOfParameter) {
        return (String)out.get(nameOfParameter);
    }

    protected int getInteger(Map<String, Object> out, String parameterName) {
        return RowMapperUtils.fromStringToInteger((String)out.get(parameterName));
    }

    protected boolean getBoolean(Map<String, Object> out, String parameterName) {
        return RowMapperUtils.fromStringToBoolean((String)out.get(parameterName));
    }


}


