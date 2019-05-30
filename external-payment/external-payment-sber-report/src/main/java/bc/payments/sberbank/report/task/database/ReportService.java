package bc.payments.sberbank.report.task.database;

import java.io.File;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.support.JdbcUtils;

import bc.payments.sberbank.report.exception.GeneralReportException;

public class ReportService {
	private final static Logger	LOGGER = Logger.getLogger(ReportService.class);
	private DataSource dataSource;
	private String outputDirectory;

	public ReportService(DataSource dataSource, String outputDirectory) {
		this.dataSource = dataSource;
		this.outputDirectory=outputDirectory;
	}
	

	public Integer saveFile(String fileName, File file, String fileDesription) throws GeneralReportException{
		LOGGER.debug("save file to DB: "+file.getAbsolutePath());
		Connection connection=null;
		CallableStatement callableStatement = null;
		try {
			connection=this.dataSource.getConnection();
			callableStatement = connection.prepareCall("{? = call pack$external_payment.add_external_oper_file(?,?,?,?,?,?)}");
			// Загружается информация о самом файле функцией  
			// 0 - операция успешная, 
			// не 0 - ошибка (в этом случае в возвращаемом значении p_result_message будет указана сама ошибка)
			callableStatement.registerOutParameter(1, Types.VARCHAR); // function result
			
			// p_cd_trans_pay_type IN VARCHAR2
			callableStatement.setString(2, "CLEARING_REPORT ");
			// p_file_description IN VARCHAR2 -- ОПИСАНИЕ ФАЙЛА
			callableStatement.setString(3, fileDesription);
		    // p_file_name IN VARCHAR2
			callableStatement.setString(4, fileName);
		    // p_stored_file_name IN VARCHAR2
			try {
				callableStatement.setString(5, moveToDestinationWithNewName(file));
			} catch (IOException e) {
				throw new GeneralReportException("can't move file to destination folder "+this.outputDirectory, e);
			}
			// p_file_data
			// ИД загруженного файла
			callableStatement.registerOutParameter(6, Types.NUMERIC);
			// если вставка успешная, иначе - текст ошибки
			callableStatement.registerOutParameter(7, Types.VARCHAR);  
			callableStatement.execute();

			connection.commit();
	
			if (isPositiveProcedureCall(callableStatement.getString(1))) {
				LOGGER.debug("file was saved: "+file.getAbsolutePath());
				return callableStatement.getInt(6);
			}else{
				throw new GeneralReportException("function return error pack$external_payment.add_external_oper_file: "+callableStatement.getString(7));
			}
		} catch (SQLException e) {
			throw new GeneralReportException("can't execute function: pack$external_payment.add_external_oper_file", e);
		} finally {
			JdbcUtils.closeStatement(callableStatement);
			JdbcUtils.closeConnection(connection);
		}		   	
	}

	private final static String PROC_POSITIVE_CALL="0";
	
	private final static boolean isPositiveProcedureCall(String value){
		return PROC_POSITIVE_CALL.equals(StringUtils.trimToNull(value));
	}
	
	private final String moveToDestinationWithNewName(File file) throws IOException{
		File destinationFile=new File(outputDirectory+changeFileName(file.getName()));
		file.renameTo(destinationFile);
		return destinationFile.getAbsolutePath();
	}

	private final SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMDD_HHmmss");
	private final String FILENAME="sberbank-report-xml";

	private String changeFileName(String name) {
		return FILENAME+sdf.format(new Date());
	}
	
}
