package xml_ini;
import java.io.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathExpression;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
/** файл, который загружает переменные из указанных путей вида XPath из прочитанного XML файла*/
public class Loader {
	
	private final static String NAME="Loader";
	/** вывод отладочной информации */
	@SuppressWarnings("unused")
	private static void debug(String information){
		System.out.print(NAME);
		System.out.print(" DEBUG ");
		System.out.println(information);
	}
	/** вывод ошибочной информации */
	private static void error(String information){
		System.out.print(NAME);
		System.out.print(" ERROR ");
		System.out.println(information);
	}
	
	private Document field_document;
	private XPath field_xpath;
	
	/** файл, который загружает переменные из указанных путей вида XPath из прочитанного XML файла
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException */
	public Loader(String path_to_xml) throws ParserConfigurationException, SAXException, IOException {
		// чтение файла 
		File file=new File(path_to_xml);
		if(!file.exists()){
			throw new FileNotFoundException("file not exists");
		}
		// создание документа XML 
		this.field_document=getXmlByPath(path_to_xml);
		// создание объекта XPath
		XPathFactory factory=XPathFactory.newInstance();
		this.field_xpath=factory.newXPath();
	}
	
	/** получить XML файл из указанного абсолютного пути 
	 * @throws IOException 
	 * @throws SAXException */
	private Document getXmlByPath(String path_to_xml) throws ParserConfigurationException, SAXException, IOException{
		javax.xml.parsers.DocumentBuilderFactory document_builder_factory=javax.xml.parsers.DocumentBuilderFactory.newInstance();
        // установить непроверяемое порождение Parser-ов
        document_builder_factory.setValidating(false);

        // получение анализатора
        javax.xml.parsers.DocumentBuilder parser = document_builder_factory.newDocumentBuilder();
        
        return parser.parse(new File(path_to_xml));
	}
	
	/** получить значение из XML файла согласно заданному XPath пути <br>
	 * @return возвращает либо пустую строку, либо значение 
	 * */
	public String getString(String path){
		String return_value="";
		try{
			XPathExpression expression=this.field_xpath.compile(path);
			return_value=(String) expression.evaluate(this.field_document,XPathConstants.STRING);
		}catch(Exception ex){
			error("getValue: Exception:"+ex.getMessage());
		}
		return return_value;
	}

	/** получить значение из XML файла согласно заданному XPath пути <br>
	 * @return возвращает либо default_value 
	 * */
	public int getInteger(String path,int default_value){
		int return_value=default_value;
		try{
			return_value=Integer.parseInt(getString(path));
		}catch(Exception ex){
			error("getInteger: Exception:"+ex.getMessage());
		}
		return return_value;
	}
	
	public double getDouble(String path, double default_value){
		double return_value=default_value;
		try{
			return_value=Double.parseDouble(getString(path));
		}catch(Exception ex){
			error("getInteger: Exception:"+ex.getMessage());
		}
		return return_value;
	}

}
