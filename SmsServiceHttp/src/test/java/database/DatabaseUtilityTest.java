package database;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import database.DatabaseUtility.DatabaseFunction;
import database.DatabaseUtility.DatabaseFunctionParameter;
import database.DatabaseUtility.FunctionParameter;
import database.DatabaseUtility.SQL_TYPE;

/**
 * test of mini-framework for execute procedures and functions without certain description ( only by parameters )
 * @author technik
 *
 */
public class DatabaseUtilityTest {

	@Test
	public void givenFunctionNameCheckNameGeneration(){
		String generatedValue=DatabaseUtility.getSqlFunctionDeclaration("PACK_BC_SMS_SEND", new TestParameter());
		String expectedValue="{? = call PACK_BC_SMS_SEND.get_profile_param(?,?)}";
		Assert.assertEquals(expectedValue, generatedValue);
	}
	
	@Test 
	public void givenParameterReadValues(){
		// given 
		Long testId=10001L;
		String testName="this is test name";
		
		// when 
		TestParameter testParameter=new TestParameter();
		testParameter.id=testId;
		testParameter.name=testName;
		List<DatabaseFunctionParameter> listOfParameters=DatabaseUtility.getFunctionParameters(testParameter);

		// then
		DatabaseFunctionParameter parameterFirst=listOfParameters.get(0);
		DatabaseFunctionParameter parameterSecond=listOfParameters.get(1);
		Assert.assertFalse(parameterFirst.directionOut());
		Assert.assertTrue(parameterSecond.directionOut());

		Assert.assertTrue(parameterSecond.order()>parameterFirst.order());
		
		Assert.assertEquals(SQL_TYPE.NUMBER, parameterFirst.sqlType());
		Assert.assertEquals(SQL_TYPE.VARCHAR2, parameterSecond.sqlType());
		
		Object executedId=DatabaseUtility.getParameterValue(testParameter, parameterFirst);
		Object expectedName=DatabaseUtility.getParameterValue(testParameter, parameterSecond);

		Assert.assertEquals(testId, executedId);
		Assert.assertEquals(testName, expectedName);
	}
	
	
	@Test
	public void givenValueSetToParameter(){
		// given 
		String testName="this is test value";
		TestParameter testParameter=new TestParameter();
		List<DatabaseFunctionParameter> listOfParameters=DatabaseUtility.getFunctionParameters(testParameter);
		Assert.assertNull(DatabaseUtility.getParameterValue(testParameter, listOfParameters.get(1)));
		
		// when 
		DatabaseUtility.setParameterValue(testParameter, listOfParameters.get(1), testName);

		// then 
		Assert.assertNotNull(DatabaseUtility.getParameterValue(testParameter, listOfParameters.get(1)));
		Assert.assertEquals(testName, DatabaseUtility.getParameterValue(testParameter, listOfParameters.get(1)));
		
	}

	@Test(expected=RuntimeException.class)
	public void givenWrongTypeValueSetToParameter(){
		// given 
		TestParameter testParameter=new TestParameter();
		List<DatabaseFunctionParameter> listOfParameters=DatabaseUtility.getFunctionParameters(testParameter);
		Assert.assertNull(DatabaseUtility.getParameterValue(testParameter, listOfParameters.get(1)));
		
		// when 
		DatabaseUtility.setParameterValue(testParameter, listOfParameters.get(1), testParameter);

	}
}

@DatabaseFunction(name="get_profile_param")
class TestParameter implements FunctionParameter{
	@DatabaseFunctionParameter(order=1, sqlType=SQL_TYPE.NUMBER, directionOut=false)
	Long id;
	@DatabaseFunctionParameter(order=2, sqlType=SQL_TYPE.VARCHAR2)
	String name;
}
