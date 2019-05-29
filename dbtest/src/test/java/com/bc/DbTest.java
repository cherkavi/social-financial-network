package com.bc;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class DbTest extends TestCase {
	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	public DbTest() {
		super("just simple test");
	}
	 */

	/**
	 * @return the suite of tests being tested
	 * @throws IOException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	public static Test suite() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, SQLException {
		TestSuite returnValue= new TestSuite();
		PathMatchingResourcePatternResolver resourceResolver=new PathMatchingResourcePatternResolver();
		TestCaseBuilder testBuilder=new TestCaseBuilder();
		// Resource[] resources=resourceResolver.getResources("classpath*:*.sql");
		Connection connection = null;
		try{
			connection=ConnectionFactory.openConnection();
			Resource[] resources=resourceResolver.getResources("classpath:sql/*.sql");
			for(Resource eachSql:resources){
				if(isExecutedWithoutError(eachSql, connection)){
					testBuilder.appendPositive(getNameOfFile(eachSql));
				}else{
					testBuilder.appendNegative(getNameOfFile(eachSql));
				}
			}
		}finally{
			if(connection!=null && !connection.isClosed()){
				try{
					connection.close();
				}catch(SQLException ex){};
			}
		}

		List<? extends Test> listOfTest=testBuilder.build();
		for(Test  eachTest:listOfTest){
			returnValue.addTest(eachTest);
		}
		return returnValue;
	}
	
	private final static String SQL_SUFFIX=".sql";
	
	private static String getNameOfFile(Resource eachSql) {
		return StringUtils.substringAfter(StringUtils.removeEnd(eachSql.getFilename(), SQL_SUFFIX), "_");
	}

	private static boolean isExecutedWithoutError(Resource eachSql, Connection connection) throws IOException, SQLException {
		String sqlContent=IOUtils.toString(eachSql.getInputStream(), Charset.forName("UTF-8"));
		Statement statement=connection.createStatement();
		try{
			statement.executeUpdate(sqlContent);
		}catch(SQLException ex){
			System.err.println(ex.getMessage());
			return false;
		}finally{
			statement.close();
		}
		return true;
	}
	
}
