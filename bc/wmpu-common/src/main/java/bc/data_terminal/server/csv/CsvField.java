package bc.data_terminal.server.csv;

import java.text.SimpleDateFormat;
import org.w3c.dom.*;

/** �������, ������� �������� ���������� � ������� CSV ����� */
public class CsvField{
	private SimpleDateFormat sdf;
	/** ����, ������� �������� ��������� ��� ������*/
	private String field_class_name;
	/** ����, ������� �������� ��������, ����� �� ������������� - ����� ������ ��������, ������ �� CSV*/
	private boolean field_can_empty;
	/** ����, ������� �������� ��������� ��� ������ ������� */
	private String field_column_name;
	/** ����, ������� �������� ��������� ������������� ������� ������ */
	private String field_format;
	
	/** ��� ��������� FIELD ��� ����������� ����������� ������� �������� */
	private static String field_xml_canEmpty="can_empty";

	/** ��� ��������� FIELD ��� ����������� ����� ���� */
	private static String field_xml_fieldName="field_name";

	/** ��� ��������� FIELD ��� ����������� ������ ����������, ����������� ��� ����� - �� ��� �������������� */
	private static String field_xml_class="class";
	
	/** ��� ��������� FIELD ��� ����������� ������� ������, ������������ ��� Date */
	private static String field_xml_format="format";
	
	/** 
	 * ������, ������� �������� �������� ������� �� XML
	 * @param class_name
	 * @param is_can_empty
	 */
/*	public Field(String class_name, String is_can_empty){
		this.field_class_name=class_name;
		field_can_empty=Boolean.valueOf(is_can_empty);
	}
*/	
	/**
	 * ������, ������� �������� �������� ������� �� XML
	 */
	public CsvField(Node node) throws Exception{
		NamedNodeMap attributes=node.getAttributes();
		Attr attribute;
		// canEmpty
		attribute=((Attr)attributes.getNamedItem(field_xml_canEmpty));
		this.field_can_empty=Boolean.parseBoolean(attribute.getValue());

		// class
		attribute=((Attr)attributes.getNamedItem(field_xml_class));
		this.setClass(attribute.getValue());
		
		// fieldName
		attribute=((Attr)attributes.getNamedItem(field_xml_fieldName));
		this.setFieldName(attribute.getValue());
		
		// format
		try{
			attribute=((Attr)attributes.getNamedItem(field_xml_format));
			this.setFormat(attribute.getValue());
		}catch(Exception ex){};// �� ������, ����� ������ ����� � �� ���������
		
	}
	
	private void setClass(String class_name){
		this.field_class_name="java.lang.Object";
		this.field_class_name=class_name;
	}
	
	private void setFieldName(String fieldName){
		this.field_column_name=fieldName;
	}
	
	
	/** ����� �� ������ ������ �� ����� �������� - ���� ������ */
	public boolean isCanEmpty(){
		return this.field_can_empty;
	}
	
	public void setFormat(String value){
		this.sdf=new SimpleDateFormat(this.convertOracleFormatToJavaFormat(value));
		this.field_format=value;
	}
	
	/** �������� ������ ���� (������������ ��� java.util.Date )
	 * @return ����� ���������� null
	 * */
	public String getFormat(){
		return this.field_format;
	}
	
	/** ���������� true, ���� ���������� �������� String ����� ���� ������������� � ������ ��� */
	public boolean isParseToClass(String value){
		boolean return_value=false;
		if(this.getClassName().equals("java.lang.Integer")){
			try{
				Integer.parseInt(value);
				return_value=true;
			}catch(Exception ex){
				if((value.trim().length()==0)&&(this.isCanEmpty())){
					return_value=true;
				}
			}
		};
		if(this.getClassName().equals("java.lang.String")){
			return_value=true;
		};
		if(this.getClassName().equals("java.lang.Float")){
			try{
				Float.parseFloat(value);
				return_value=true;
			}catch(Exception ex){
				value=value.replaceAll(",", ".");
				try{
					Float.parseFloat(value);
					return_value=true;
				}catch(Exception ex2){
					if((value.trim().length()==0)&&(this.isCanEmpty())){
						return_value=true;
					}
				};
			}
		};
		if(this.getClassName().equals("java.util.Date")){
			try{
				sdf.parse(value);
				return_value=true;
			}catch(Exception ex){}
		};
		if(this.getClassName().equals("java.lang.Double")){
			try{
				Double.parseDouble(value);
				return_value=true;
			}catch(Exception ex){
				value=value.replaceAll(",", ".");
				try{
					Double.parseDouble(value);
					return_value=true;
				}catch(Exception ex2){
					if((value.trim().length()==0)&&(this.isCanEmpty())){
						return_value=true;
					}
				};
			};
		};
		return return_value;
	}
	
	/** ���������� ��� ������ */
	public String getClassName(){
		return this.field_class_name;
	}
	
	/** ���������� ��� ������� */
	public String getColumnName(){
		return this.field_column_name;
	}
	
	private static String[] dateFormatOracle=new String[]  {"YYYY","MM","DD","HH24","MI","SS"};
	private static String[] dateFormatJava=new String[]{"yyyy","MM","dd","HH",  "mm","ss"};
	/** �������� ������ ��� ������������� ������� java.text.SimpleDateFormat �� ������ ������� Oracle YYYYMMDDHH24MISS*/
	private String convertOracleFormatToJavaFormat(String oracleFormat){
		String returnValue=oracleFormat.toUpperCase();
		for(int counter=0;counter<dateFormatOracle.length;counter++){
			returnValue=returnValue.replaceAll(dateFormatOracle[counter],dateFormatJava[counter]);
		}
		return returnValue;
	}
}
