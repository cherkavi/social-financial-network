package bc.data_terminal.server.csv;
import java.io.ByteArrayInputStream;

import org.apache.log4j.Logger;
import org.w3c.dom.*;

import javax.xml.parsers.*;
import javax.xml.xpath.*;


/** ������� ������ �������� �� �������� ������������ ������ � XML ������ � � CSV */
public class WrapXmlChecker {
	private Document field_document;
	private final static Logger LOGGER=Logger.getLogger(WrapXmlChecker.class);
	
	/** ������ ������������ ���� ������� � XML */
	private CsvField[] field_fields;
	/** ����, ������� ������� � ������� ��������� */
	private boolean field_header_exists;
	/** �����������, ������� ������������ � CSV ����� */
	private String field_delimeter=",";
	
	/** ����, ������� ������� �� ������������� ������������*/
	private boolean field_comment_exists=true;
	
	/** ��� attribute ��� NODE ��������� DELIMETER */
	private static String field_delimeter_xml_attribute="value";
	
	/** ��� attribute ��� NODE ��������� CHECKER */
	private static String field_checker_xml_attribute="full_log";

	/** �������� ������ ��� = true, ��� �� �������� ������ ��� ������ �� �� ����������=false*/
	private boolean field_log_full=false;
	
	/**
	 * �� ������ ������� XML ����� ��������� ���������� ������ � CSV ����� 
	 * @param xmlFile - ����� Xml ����� 
	 * @param path_to_csv
	 * @throws Exception ���� ���� �� �������, ��������� ������ ������ XML...
	 */
	public WrapXmlChecker(String xmlFile) throws Exception {
		this.field_header_exists=false;
		LOGGER.debug("check for exists");
		try{
			if(xmlFile==null){
				throw new Exception("xml file is null");
			}
		}catch(Exception ex){
			LOGGER.error("data is not valid ");
			throw new Exception("����� �� ������");
		}
		LOGGER.debug("compile XML ");
		try{
	        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
	        domFactory.setNamespaceAware(true); // never forget this!
	        DocumentBuilder builder = domFactory.newDocumentBuilder();
	        field_document =builder.parse(new ByteArrayInputStream(xmlFile.getBytes())); 
		}catch(Exception ex){
			LOGGER.error("xml parsing error");
			throw new Exception("XML �� �������");
		}
		LOGGER.debug("parsing XML");
		try{
			parseXML();
		}catch(Exception ex){
			LOGGER.error("XML parse ERROR");
			throw new Exception("������ ��������� ������������ XML");
		}
	}
	
	
	/** �����, ������� ������ ������ �� XML � ��������� �� � ���� ������� �������:
	 * <li>������ ������� ��������� ([HEADER] exists='true')</li>
	 * <li>������ �����, ������������� ������� (class)</li>
	 * <li>������ �����, ������������� �������������� ���� (canEmpty)</li>
	 * */
	private void parseXML() throws Exception{
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        XPathExpression expression;
        
        LOGGER.debug("get DELIMETER");
        expression= xpath.compile("//PATTERN/DELIMETER");
        field_delimeter=",";
        try{
        	Node node=(Node)expression.evaluate(field_document,XPathConstants.NODE);
        	if(node!=null){
        		field_delimeter=((Attr)node.getAttributes().getNamedItem(field_delimeter_xml_attribute)).getValue();
        		LOGGER.debug("DELIMETER is found:"+field_delimeter);
        	}
        }catch(Exception ex){
        	LOGGER.debug("DELIMETER not found:");
        }
 
        LOGGER.debug("get COMMENT");
        try{
        	// check for exists COMMENT
        	Node comment_node=(Node)xpath.compile("//PATTERN/COMMENT").evaluate(field_document,XPathConstants.NODE);
        	if(comment_node!=null){
        		// check for exists COMMENT [@exists]
        		if(comment_node.getAttributes().getNamedItem("exists")!=null){
                    // find comment [@exists='true']
        			expression= xpath.compile("//PATTERN/COMMENT[@exists='true']");
                    Boolean comment_value=(Boolean)expression.evaluate(field_document, XPathConstants.BOOLEAN);
                    if(comment_value!=null){
                    	this.field_comment_exists=comment_value.booleanValue();
                    }else{
                    	this.field_comment_exists=true;
                    }
        		}
        	}
        }catch(Exception ex){
        	//this.field_comment_exists=true;
        }
 		
        LOGGER.debug("get HEADER"); 
 		expression= xpath.compile("//PATTERN/HEADER[@exists='true']");
        Boolean header_value=(Boolean)expression.evaluate(field_document, XPathConstants.BOOLEAN);
        if(header_value!=null){
        	this.field_header_exists=header_value.booleanValue();
        }else{
        	this.field_header_exists=false;
        }
        
        LOGGER.debug("get ELEMENTS");
        expression=xpath.compile("//PATTERN/FIELDS/FIELD");
        NodeList nodes=(NodeList)expression.evaluate(field_document,XPathConstants.NODESET);
        LOGGER.debug("NODESET:"+nodes.getLength());
        this.field_fields=new CsvField[nodes.getLength()];
        for(int counter=0;counter<nodes.getLength();counter++){
        	this.field_fields[counter]=new CsvField(nodes.item(counter));
        }
        
        LOGGER.debug("get CHECKER");
        try{
        	// check for exists COMMENT
        	Node comment_node=(Node)xpath.compile("//PATTERN/CHECKER").evaluate(field_document,XPathConstants.NODE);
        	if(comment_node!=null){
        		// check for exists COMMENT [@exists]
        		if(comment_node.getAttributes().getNamedItem(field_checker_xml_attribute)!=null){
                    // find comment [@exists='true']
        			expression= xpath.compile("//PATTERN/CHECKER[@"+field_checker_xml_attribute+"='true']");
                    Boolean comment_value=(Boolean)expression.evaluate(field_document, XPathConstants.BOOLEAN);
                    if(comment_value!=null){
                    	this.field_log_full=comment_value.booleanValue();
                    }else{
                    	this.field_log_full=false;
                    }
        		}
        	}
        }catch(Exception ex){
        	//this.field_log_full=false;
        }
	}
	
	/**
	 * ��������� ������� ���� 
	 * @param value true - ������ ���
	 * false - ������ ������ ��� ������ �� ���������� ������
	 */
	public void setFullLog(boolean value){
		this.field_log_full=value;
	}

	/** ������� ����������� ��� ������� ����� */
	public String getDelimeter(){
		return this.field_delimeter;
	}
	
	/** �������� ���� ������� ������������ � ������ ����� */
	public boolean isCommentExists(){
		return this.field_comment_exists;
	}
	/** �������� ���� ������� ���������� */
	public boolean isHeaderExists(){
		return this.field_header_exists;
	}
	/** �������� ������������� ������� ������ ���� - ��� ���������� ������� */
	public boolean isFullLog(){
		return this.field_log_full;
	}

	/** �������� ���-�� ����� ��� ������ ���� */
	public int getCsvFieldCount(){
		return this.field_fields.length;
	}
	
	/** �������� ���� ��� ����� Xml ����� �� ������� 
	 * @param index - ���������� ����� � �����
	 * @return null - ���� ������ ������� �� ������� 
	 * */
	public CsvField getCsvField(int index){
		if((index>=0)&&(index<this.getCsvFieldCount())){
			return this.field_fields[index];
		}else{
			return null;
		}
	}
	
	/** �������� ��� ���� �� ����� XML */
	public CsvField[] getCsvFieldAll(){
		return this.field_fields;
	}
}






