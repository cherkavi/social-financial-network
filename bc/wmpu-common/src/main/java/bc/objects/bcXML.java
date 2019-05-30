package bc.objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import bc.bcBase;

import java.io.InputStream;
import java.util.*;

public class bcXML {
	private final static Logger LOGGER=Logger.getLogger(bcXML.class);
	private Map<String, String> fields = new HashMap<String, String>();
	private String XMLfile = "";
    
	/**
	 * name of XML file, unique identifier
	 * 
	 * @return
	 */
	protected String getXmlFile() {
		return this.XMLfile;
	}

	protected Map<String, String> getXmlFileFields() {
		return this.fields;
	}

	private static bcXML instance = null;

	public static bcXML getInstance(Map<String, String> myFields,
			String myXMLFile) {
		if (instance == null) {
			instance = new bcXML(myFields, myXMLFile);
		}
		return instance;
	}

	public static bcXML getInstance() {
		return instance;
	}

	@Deprecated
	public bcXML(Map<String, String> existingFields, String fileName) {
		fields = existingFields;
		XMLfile = fileName;
		parseAndLoadMap(fileName);
	}

	public bcXML(Map<String, String> existingFields, String fileName,
			InputStream resourceAsStream) {
		fields = existingFields;
		XMLfile = fileName;
		parseAndLoadMap(resourceAsStream);
	}

	public String getFirstObligatoryText() {
		return "<font color=\"red\"><b>";
	}

	public String getLastObligatoryText() {
		return "</b></font>";
		//return "&nbsp;*</b></font>";
	}

	public String getFirstDisableText() {
		return "<font color=\"gray\">";
	}

	public String getLastDisableText() {
		return "</font>";
		//return "&nbsp;*</b></font>";
	}

	// not override
	public String getfieldTransl(String fld, boolean pIsObligatory) { // отримати
																					// переклад
																					// поля
																					// по
																					// мові
																					// і
																					// полю
		//setLanguage(bcBase.SESSION_PARAMETERS.LANG.getValue());
		//System.out.println("bcXML.language="+bcBase.SESSION_PARAMETERS.LANG.getValue());
		return getfieldTransl(bcBase.SESSION_PARAMETERS.LANG.getValue(), fld, pIsObligatory, false);
	}
	

	public String getfieldTranslDisable(String fld, boolean pIsObligatory, boolean pIsDisable) { 
		//setLanguage(bcBase.SESSION_PARAMETERS.LANG.getValue());
		//System.out.println("bcXML.language="+bcBase.SESSION_PARAMETERS.LANG.getValue());
		return getfieldTransl(bcBase.SESSION_PARAMETERS.LANG.getValue(), fld, pIsObligatory, pIsDisable);
	}

	// not override
	/*public String getfieldTransl(String lng, String fld, boolean pIsObligatory) { // отримати
																					// переклад
																					// поля
																					// по
																					// мові
																					// і
																					// полю
		return getfieldTransl(lng, fld, pIsObligatory, false);
	}*/

	// not override
	public String getfieldTransl(String lng, String fld, boolean pIsObligatory, boolean pIsDisable) { // отримати
																					// переклад
																					// поля
																					// по
																					// мові
																					// і
																					// полю
		String return_value = getfieldTransl2(lng, fld);
		if (pIsDisable) {
			return_value = getFirstDisableText() + return_value
			+ getLastDisableText();
		} else {
			if (pIsObligatory) {
				return_value = getFirstObligatoryText() + return_value
						+ getLastObligatoryText();
			}
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
		if (fields.isEmpty()) {
			parseAndLoadMap(fld); // load Map if it's empty
			LOGGER.debug("fields was empty, now fields.size="+fields.size());
			fields.put("0", "0"); // щоб не була пустою
									// (natPrsFields.isEmpty()==false) якщо файл
									// пустий
		}
		//LOGGER.error("bcXML.getfieldTransl2(): lng=" + lng + ", fld=" + fld + ", total count="+fields.size());
		String key = "";
		try {
			key = lng.toUpperCase() + "_" + fld.toUpperCase();
			if (key.toUpperCase().endsWith("_FRMT")) {
				key = key.substring(0, key.length()-5);
			} else if (key.toUpperCase().endsWith("_AMNT")) {
				key = key.substring(0, key.length()-5);
			} else if (key.toUpperCase().endsWith("_TSL")) {
				key = key.substring(0, key.length()-4);
			}
		} catch (Exception e) {
			LOGGER.error("bcXML.getfieldTransl2(): lng=" + lng + ", fld=" + fld + ", total count="+fields.size() + " -  Exception: " + e);
		}

		return_value = fields.get(key);
		if (return_value==null || "".equalsIgnoreCase(return_value)) {
			LOGGER.debug("getfieldTransl ERROR ('"+lng+"','"+fld+"') - return_value IS EMPTY");
		}
		//LOGGER.debug("getfieldTransl('"+lng+"','"+fld+"')="+return_value);
		return return_value;
		// return "TEST";
	}

	private void parseAndLoadMap(String fileName) {
		try {
			Document doc = null;
			// LOGGER.debug("bcXML.parseAndLoadMap: XMLFileName="+XMLfile);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(fileName);
			if (doc != null){
				loadMapFromDomDocument(doc);
			}
		} catch (Exception e) {
			LOGGER.error("bcXML#parseAndLoadMap  Sorry, an error occurred when opening&parsing XML file : " + e);
		}finally{
		}
	}

	private void parseAndLoadMap(InputStream source) {
		try {
			Document doc = null;
			// LOGGER.debug("bcXML.parseAndLoadMap: XMLFileName="+XMLfile);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(source);
			if (doc != null){
				loadMapFromDomDocument(doc);
			}
		} catch (Exception e) {
			LOGGER.error("bcXML#parseAndLoadMap  Sorry, an error occurred when opening&parsing XML file : " + e);
		}finally{
			IOUtils.closeQuietly(source);
		}
	}

	// not override
	private void loadMapFromDomDocument(Document doca) {
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
