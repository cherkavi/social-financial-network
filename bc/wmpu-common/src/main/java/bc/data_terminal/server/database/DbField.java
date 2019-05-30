package bc.data_terminal.server.database;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.apache.log4j.Logger;

/** ������, ������� ����� �������������� ���� � ���� ������, � ��������������� ���� � CSV ����� */ 
public class DbField {
	private final static Logger LOGGER=Logger.getLogger(DbField.class);
	
	private final static String[] typesString=new String[]{"java.lang.Integer","java.lang.Float","java.lang.String","java.util.Date"};
	private final static int[] typesInt=new int[]{java.sql.Types.INTEGER,java.sql.Types.FLOAT,java.sql.Types.VARCHAR,java.sql.Types.TIMESTAMP};
	private final static SimpleDateFormat sdf=new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	/** ������� � CSV ����� */
	private int fieldPosition;
	/** ��� ���� � ��������� ���� ������ */
	private String fieldName;
	/** ��� ���� � ��������� ���� ������ */
	private int fieldType;
	
	/** ������, ������� ��������� ���� � ���� ������ � ���� �� CSV ����� */
	public DbField(int fieldPosition, 
				   String fieldName,
				   int fieldType){
		this.fieldPosition=fieldPosition;
		this.fieldName=fieldName;
		this.fieldType=fieldType;
	}

	public DbField(int fieldPosition, 
			   String fieldName,
			   String fieldType){
		this.fieldPosition=fieldPosition;
		this.fieldName=fieldName;
		this.fieldType=this.getTypeIntByString(fieldType);
	}
	
	public int getTypeIntByString(String value){
		//int index=Arrays.binarySearch(typesString, value);
		int index=getStringArrayIndex(typesString, value);
		if(index>=0){
			return typesInt[index];
		}else{
			LOGGER.error("DbField SQL Type is not found (typesString)): >"+value+"<");
			return (-1);
		}
	}
	
	private int getStringArrayIndex(String[] array, String value){
		int returnValue=(-1);
		for(int counter=0;counter<array.length;counter++){
			if(array[counter].equals(value)){
				returnValue=counter;
				break;
			}
		}
		return returnValue;
	}
	

	public String getTypeStrigByInt(int value){
		int index=Arrays.binarySearch(typesInt, value);
		if(index>=0){
			return typesString[index];
		}else{
			LOGGER.error("DbField SQL Type is not found (typesInt): "+value);
			return "";
		}
	}
	
	/** �������� ����� ������� � CSV ����� */
	public int getFieldPosition() {
		return fieldPosition;
	}

	public void setFieldPosition(int fieldPosition) {
		this.fieldPosition = fieldPosition;
	}

	/** �������� ��� ������� � ���� {@link java.sql.Types} */
	public int getFieldType() {
		return fieldType;
	}

	public void setFieldType(int fieldType) {
		this.fieldType = fieldType;
	}


	public String getFieldName() {
		return fieldName;
	}


	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/** �������� �� ��������� ������� ������ �������� ��� ���������� � CSV ���� */
	public String getValue(ResultSet rs) {
		String returnValue=null;
		try{
			switch(this.fieldType){
				case java.sql.Types.INTEGER:{
					returnValue=Integer.toString(rs.getInt(this.fieldName));
					if(rs.wasNull()){
						returnValue=null;
					}
				}break;
				case java.sql.Types.FLOAT:{
					returnValue=Float.toString(rs.getFloat(this.fieldName));
					if(rs.wasNull()){
						returnValue=null;
					}
				} break;
				case java.sql.Types.VARCHAR:{
					returnValue=rs.getString(this.fieldName);
					if(rs.wasNull()){
						returnValue=null;
					}
				} break;
				case java.sql.Types.TIMESTAMP:{
					try{
						returnValue=sdf.format(new Date(rs.getTimestamp(this.fieldName).getTime()));
					}catch(NullPointerException ex){
					}catch(Exception ex){
						LOGGER.error("DbField#getValue Exception: "+ex.getMessage());
					}
					if(rs.wasNull()){
						returnValue=null;
					}
				} break;
			}
		}catch(Exception ex){
			LOGGER.error("DbField# getValue: Exception: "+ex.getMessage()+"   FieldName:"+this.fieldName);
		}
		return (returnValue==null)?null:returnValue;
	}
	
	
}
