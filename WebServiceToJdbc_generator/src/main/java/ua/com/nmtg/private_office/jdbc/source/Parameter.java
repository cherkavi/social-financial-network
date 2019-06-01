package ua.com.nmtg.private_office.jdbc.source;

import java.sql.Types;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

public class Parameter implements Comparable<Parameter> {
	/** direction of parameter, procedure Point Of View */
	public static enum Direction{
		IN,
		OUT
	}
	
	public static Collection<Parameter> getParameterByDirection(Collection<Parameter> paramCollection, Direction direction){
		Set<Parameter> returnValue=new TreeSet<Parameter>();
		Iterator<Parameter> iterator=paramCollection.iterator();
		while(iterator.hasNext()){
			Parameter parameter=iterator.next();
			if(parameter.direction.equals(direction)){
				if(parameter.getName()!=null)returnValue.add(parameter);
			}
		}
		return returnValue;
	}
	
	/**
	 * @param paramCollection
	 * @return return parameter for Oracle procedure/function 
	 */
	public static Parameter getReturnParameterWithoutName(Collection<Parameter> paramCollection){
		Iterator<Parameter> iterator=paramCollection.iterator();
		while(iterator.hasNext()){
			Parameter parameter=iterator.next();
			if(parameter.getName()==null)return parameter;
		}
		return null;
	}
	
	private int position;
	private Direction direction;
	private String name;
	private ClassPairRule rule;
	private int precision;
	private int scale;

	public Parameter(int position, Direction direction, String name, String sqlDataType) {
		this(position, direction, name, sqlDataType, 0, 0 );
	}

	public Parameter(int position, Direction direction, String name, String sqlDataType, int precision, int scale) {
		super();
		this.position = position;
		this.direction = direction;
		this.name = name;
		this.rule = getRuleBySqlName(sqlDataType);
		this.precision=precision;
		this.scale=scale;
	}


	public int getPosition() {
		return position;
	}

	public Direction getDirection() {
		return direction;
	}

	public String getName() {
		return name;
	}

	public int getPrecision() {
		return precision;
	}

	public int getScale() {
		return scale;
	}

	@Override
	public int compareTo(Parameter another) {
		if(another==null){
			return -1;
		}
		return another.position-this.position;
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((direction == null) ? 0 : direction.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + position;
		result = prime * result + precision;
		result = prime * result + ((rule == null) ? 0 : rule.hashCode());
		result = prime * result + scale;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Parameter other = (Parameter) obj;
		if (direction == null) {
			if (other.direction != null)
				return false;
		} else if (!direction.equals(other.direction))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (position != other.position)
			return false;
		if (precision != other.precision)
			return false;
		if (rule == null) {
			if (other.rule != null)
				return false;
		} else if (!rule.equals(other.rule))
			return false;
		if (scale != other.scale)
			return false;
		return true;
	}

	/**
	 * @param parameter
	 * @return full name of the Java class ( use for return value from function )
	 * <br />
	 *  public {@return} check_nat_prs(JavaBean input, JavaBean output)
	 *  <br >
	 *  java.lang.Long, java.lang.Integer, java.lang.String  
	 */
	public static String getJavaType(Parameter parameter) {
		return parameter.rule.outputClass.getName();
	}

	/**
	 * 
	 * @param returnParameterWithoutName
	 * @return {@link Types}
	 * <br />
	 * 
	 * Types.{@return}: <br />
	 * Types.VARCHAR, Types.INTEGER, Types.DATE
	 *
	 * 
	 */
	public static String getSqlType(Parameter parameter) {
		return parameter.rule.jdbcType;
	}

	
	/**
	 * 
	 * @param parameter
	 * @return text representation of an input parameter <br />
	 *  statement.set{@return} <br />
	 *  statement.setString, statement.setInteger, statement.setLong
	 * 
	 */
	public static String getStatementParameter(Parameter parameter) {
		return parameter.rule.statementJdbcType;
	}

	/**
	 * @param parameter
	 * @return
	 * get name of JavaBean Getter <br />
	 *  for example, if parameter has name : <b>value</b>, then return: <b>getValue</b>
	 */
	public static String getNameOfGetter(Parameter parameter) {
		String returnValue=parameter.getName().toLowerCase();
		return "get"+((returnValue.charAt(0)+"").toUpperCase())+StringUtils.substring(returnValue, 1);
	}

	/**
	 * @param parameter
	 * @return
	 * get name of JavaBean Setter <br />
	 *  for example, if parameter has name : <b>value</b>, then return: <b>setValue</b>
	 */
	public static String getNameOfSGetter(Parameter parameter) {
		String returnValue=parameter.getName().toLowerCase();
		return "set"+((returnValue.charAt(0)+"").toUpperCase())+StringUtils.substring(returnValue, 1);
	}
	
	
	private ClassPairRule getRuleBySqlName(String name) {
		ClassPairRule returnValue=ClassPairRule.getRuleBySqlName(ruleSet, name);
		if(returnValue==null){
			return ClassPairRule.UNKNOWN;
		}
		return returnValue;
	}
	
	
	private static final Set<ClassPairRule> ruleSet=new HashSet<ClassPairRule>();
	{
		// INFO rules for type conversation between Oracle and Java 
		ruleSet.add(new ClassPairRule("VARCHAR2", java.lang.String.class,"VARCHAR", "String"));
		ruleSet.add(new ClassPairRule("NUMBER", java.lang.Integer.class, "INTEGER", "Int"));
		ruleSet.add(new ClassPairRule("DATE", java.sql.Date.class, "DATE", "Date"));
	}
	

	/**
	 * one rule for SQL name and one Java class  
	 */
	public static class ClassPairRule{
		public static ClassPairRule UNKNOWN=new ClassPairRule("", java.lang.Object.class, "VARCHAR", "String");

		private String sqlName;
		private Class<?> outputClass;
		private String jdbcType; 
		private String statementJdbcType;
		
		public ClassPairRule(String sqlName, Class<?> outputClass, String jdbcType, String statementJdbcType){
			this.sqlName=sqlName;
			this.outputClass=outputClass;
			this.jdbcType=jdbcType;
			this.statementJdbcType=statementJdbcType;
		}
		
		/**
		 * utility method for get all Rule by SQL name 
		 * @param collection
		 * @param sqlName
		 * @return
		 */
		public static ClassPairRule getRuleBySqlName(Collection<ClassPairRule> collection, String sqlName){
			Iterator<ClassPairRule> iterator=collection.iterator();
			while(iterator.hasNext()){
				ClassPairRule value=iterator.next();
				if(StringUtils.equalsIgnoreCase(value.sqlName, sqlName)){
					return value;
				}
			}
			return null;
		}
		
	}
	
}
