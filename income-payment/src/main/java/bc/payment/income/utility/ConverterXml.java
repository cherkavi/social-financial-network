package bc.payment.income.utility;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.camel.Exchange;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ConverterXml {
	
	public static Document fromString(Exchange exchange) throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        DocumentBuilder builder;  
        builder = factory.newDocumentBuilder();  
        Document doc = builder.parse( new InputSource( new StringReader( (String) exchange.getIn().getBody() ) ) ); 
        return doc;
	}
	
	public static String fromXml(Exchange exchange) throws TransformerException{
		 TransformerFactory tf = TransformerFactory.newInstance();
	        Transformer transformer;
            transformer = tf.newTransformer();
            // below code to remove XML declaration
            // transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource((Document)exchange.getIn().getBody()), new StreamResult(writer));
            String output = writer.getBuffer().toString();
            return output;
	}
	
}
