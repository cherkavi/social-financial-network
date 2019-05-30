package bc.xml;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.*;

public class bcXML {
	private final static Logger LOGGER = Logger.getLogger(bcXML.class);
	private Map<String, String> oneXMLFileFields = new HashMap<String, String>();
	private String XMLfile = "";

	protected String getXmlFile() {
		return this.XMLfile;
	}

	protected Map<String, String> getXmlFileFields() {
		return this.oneXMLFileFields;
	}

	private static bcXML instance = null;

	public static bcXML getInstance(Map<String, String> myFields,
			String myXMLFile) {
		if (instance == null) {
			instance = new bcXML(myFields, myXMLFile);
			instance.parseAndLoadMap();
		}
		return instance;
	}

	public static bcXML getInstance() {
		return instance;
	}

	protected bcXML(Map<String, String> myFields, String myXMLFile) {
		oneXMLFileFields = myFields;
		XMLfile = myXMLFile;
		parseAndLoadMap();
	}

	public String getFirstObligatoryText() {
		return "<font color=\"darkbrown\"><b>";
	}

	public String getLastObligatoryText() {
		return "&nbsp;*</b></font>";
	}

	// not override
	public String getfieldTransl(String lng, String fld, boolean pIsObligatory) { // отримати
																					// переклад
																					// поля
																					// по
																					// мові
																					// і
																					// полю
		String return_value = getfieldTransl2(lng, fld);
		if (pIsObligatory) {
			return_value = getFirstObligatoryText() + return_value
					+ getLastObligatoryText();
		}
		return return_value;
		// return "TEST";
	}

	// not override
	public String getfieldTranslNoDiv(String lng, String fld) { // отримати
																// переклад поля
																// по мові і
																// полю
		String return_value = getfieldTransl2(lng, fld);
		return return_value;
		// return "TEST";
	}

	// not override
	public String getfieldTransl2(String lng, String fld) { // отримати переклад
															// поля по мові і
															// полю
		String return_value = "";
		if (oneXMLFileFields.isEmpty()) {
			parseAndLoadMap(); // load Map if it's empty
			oneXMLFileFields.put("0", "0"); // щоб не була пустою
											// (natPrsFields.isEmpty()==false)
											// якщо файл пустий
		}
		String key = "";
		try {
			key = lng.toUpperCase() + "_" + fld.toUpperCase();
		} catch (Exception e) {
			LOGGER.error("bcXML.getfieldTransl2() Exception: " + e);
		}
		// LOGGER.debug("allXML: getfieldTransl");

		return_value = oneXMLFileFields.get(key);
		return return_value;
		// return "TEST";
	}

	// not override
	protected void parseAndLoadMap() {
		Document doc = null;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(this.getXmlFile());
			if (doc != null)
				loadMap(doc);
		} catch (Exception e) {
			LOGGER.error("Sorry, an error occurred when opening&parsing XML file : "
					+ e);
		}
	}

	// not override
	protected void loadMap(Document doca) {
		Element root = doca.getDocumentElement();
		NodeList fieldsNamesXML = root.getChildNodes();
		for (int i = 0; i < fieldsNamesXML.getLength(); i++) {
			Node fieldNameXML = fieldsNamesXML.item(i);
			if (fieldNameXML instanceof Element) {
				NodeList fieldsTranslationsXML = fieldNameXML.getChildNodes();
				for (int j = 0; j < fieldsTranslationsXML.getLength(); j++) {
					Node fieldTranslationXML = fieldsTranslationsXML.item(j);
					if (fieldTranslationXML instanceof Element) {
						String lang = fieldTranslationXML.getNodeName()
								.toString();
						Text translationText = (Text) fieldTranslationXML
								.getFirstChild();
						String translation = translationText.getData().trim();
						this.getXmlFileFields().put(
								lang.toUpperCase()
										+ "_"
										+ fieldNameXML.getNodeName()
												.toUpperCase(), translation);
					} // if
				} // for j переклад полів
			} // if
		} // for i - назви полів
	} // loadMap
}
