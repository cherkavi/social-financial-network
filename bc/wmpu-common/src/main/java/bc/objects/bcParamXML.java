package bc.objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.*;

public class bcParamXML {
	private final static Logger LOGGER=Logger.getLogger(bcParamXML.class);
	
	private static final String RELATIVE_PATH = "settings/setting.xml";

	private Map<String, String> oneXMLFileFields = new HashMap<String, String>();

	private static bcParamXML instance = null;

	/*
	 * public static bcParamXML getInstance(Map<String,String> myFields, String
	 * myXMLFile){ if(instance==null){ instance=new bcParamXML(myFields,
	 * myXMLFile); instance.parseAndLoadMap(); } return instance; }
	 */
	public static bcParamXML getInstance() {
		if (instance == null) {
			instance = new bcParamXML(new HashMap<String, String>(), Thread
					.currentThread()
					.getContextClassLoader()
					.getResourceAsStream(
							bcDictionary.XML_FOLDER + RELATIVE_PATH));
		}
		return instance;
	}

	private bcParamXML(Map<String, String> myFields, InputStream inputStream) {
		oneXMLFileFields = myFields;
		parseAndLoadMap(inputStream);
	}

	public String getValue(String fld) {
		String return_value = "";
		if (oneXMLFileFields.isEmpty()) {
			// parseAndLoadMap();
			oneXMLFileFields.put("0", "0");
		}
		String key = "";
		try {
			key = fld.toUpperCase();
		} catch (Exception e) {
			LOGGER.error("bcParamXML.getValue Exception: " + e);
		}

		return_value = oneXMLFileFields.get(key);
		// LOGGER.debug("bcParamXML.getValue("+key+")="+return_value);
		return return_value;
	}

	// not override
	private void parseAndLoadMap(InputStream source) {
		Document doc = null;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(source);
			if (doc != null)
				loadMap(doc);
		} catch (Exception e) {
			LOGGER.error("bcParamXML#parseAndLoadMap Sorry, an error occurred when opening&parsing XML file : "
					+ e);
		}
	}

	// not override
	private void loadMap(Document doca) {
		// LOGGER.debug("bcParamXML.loadMap() BEGIN");
		Element root = doca.getDocumentElement();
		NodeList fieldsNamesXML = root.getChildNodes();
		for (int i = 0; i < fieldsNamesXML.getLength(); i++) {
			Node fieldNameXML = fieldsNamesXML.item(i);
			if (fieldNameXML instanceof Element) {
				String key = fieldNameXML.getNodeName().toUpperCase();
				Text keyText = (Text) fieldNameXML.getFirstChild();
				String value = keyText.getData().trim();
				// LOGGER.debug("bcParamXML.loadMap(), key="+key+", value="+value);
				this.oneXMLFileFields.put(key, value);
			} // if
		} // for i - назви полів
			// LOGGER.debug("bcParamXML.loadMap() END");
	} // loadMap
}
