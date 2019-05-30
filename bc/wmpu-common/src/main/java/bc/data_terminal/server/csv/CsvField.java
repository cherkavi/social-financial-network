package bc.data_terminal.server.csv;

import java.text.SimpleDateFormat;
import org.w3c.dom.*;

/** объекты, которые содержат информацию о столбце CSV файла */
public class CsvField{
	private SimpleDateFormat sdf;
	/** поле, которое содержит текстовое имя класса*/
	private String field_class_name;
	/** поле, которое содержит значение, могут ли отсутствовать - иметь пустое значение, данные из CSV*/
	private boolean field_can_empty;
	/** поле, которое содержит текстовое имя самого столбца */
	private String field_column_name;
	/** поле, которое содержит текстовое представление формата данных */
	private String field_format;
	
	/** имя аттрибута FIELD для обозначения возможности пустого значения */
	private static String field_xml_canEmpty="can_empty";

	/** имя аттрибута FIELD для обозначения имени поля */
	private static String field_xml_fieldName="field_name";

	/** имя аттрибута FIELD для обозначения класса переменной, сохраненной как текст - во что конвертировать */
	private static String field_xml_class="class";
	
	/** имя аттрибута FIELD для обозначения формата данных, используется для Date */
	private static String field_xml_format="format";
	
	/** 
	 * Объект, который содержит описание столбца из XML
	 * @param class_name
	 * @param is_can_empty
	 */
/*	public Field(String class_name, String is_can_empty){
		this.field_class_name=class_name;
		field_can_empty=Boolean.valueOf(is_can_empty);
	}
*/	
	/**
	 * Объект, который содержит описание столбца из XML
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
		}catch(Exception ex){};// на случай, когда данных может и не оказаться
		
	}
	
	private void setClass(String class_name){
		this.field_class_name="java.lang.Object";
		this.field_class_name=class_name;
	}
	
	private void setFieldName(String fieldName){
		this.field_column_name=fieldName;
	}
	
	
	/** может ли данный объект не иметь значений - быть пустым */
	public boolean isCanEmpty(){
		return this.field_can_empty;
	}
	
	public void setFormat(String value){
		this.sdf=new SimpleDateFormat(this.convertOracleFormatToJavaFormat(value));
		this.field_format=value;
	}
	
	/** получить формат поля (используется для java.util.Date )
	 * @return может возвращать null
	 * */
	public String getFormat(){
		return this.field_format;
	}
	
	/** возвращает true, если переданное значение String может быть преобразовано в данный тип */
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
	
	/** возвращает имя класса */
	public String getClassName(){
		return this.field_class_name;
	}
	
	/** возвращает имя столбца */
	public String getColumnName(){
		return this.field_column_name;
	}
	
	private static String[] dateFormatOracle=new String[]  {"YYYY","MM","DD","HH24","MI","SS"};
	private static String[] dateFormatJava=new String[]{"yyyy","MM","dd","HH",  "mm","ss"};
	/** получить строку для инициализации объекта java.text.SimpleDateFormat из строки формата Oracle YYYYMMDDHH24MISS*/
	private String convertOracleFormatToJavaFormat(String oracleFormat){
		String returnValue=oracleFormat.toUpperCase();
		for(int counter=0;counter<dateFormatOracle.length;counter++){
			returnValue=returnValue.replaceAll(dateFormatOracle[counter],dateFormatJava[counter]);
		}
		return returnValue;
	}
}
