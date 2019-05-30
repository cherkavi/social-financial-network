package bc.objects;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class bcDictionary {
	private final static Logger LOGGER=Logger.getLogger(bcDictionary.class);
	
	private static Map<String, bcXML>	elements;

	private final static String	XML_EXTENSION	= ".xml";
	/**
	 * path to resource directory in the compiled JAR 
	 */
	public final static String XML_FOLDER="XML/";
	
	private final static String ZIP_SEPARATOR="/";

	private final static String JAR_URL_PREAMBULA="jar:file:";
	private final static String JAR_URL_POSTAMBULA="!/";
	
	static {
		try {
			elements = processXml();
		} catch (IOException e) {
			LOGGER.error("can't read data from folder: "+XML_FOLDER);
		}
	}

	public static bcXML getInstance(String fileName) {
		//LOGGER.debug("bcDictionary.getInstance: fileName=" +fileName + ", count="+elements.size());
		return elements.get(fileName.toLowerCase());
	}

	public static String getElementCount() {
		return "" + elements.size();
	}


	/**
	 * read data from XML files 
	 * @return
	 * @throws IOException 
	 */
	private static Map<String, bcXML> processXml() throws IOException  {
		List<String> files=retrieveXmlFilesNames();
		
		Map<String, bcXML> returnValue = new HashMap<String, bcXML>();
		
		for (String eachFile : files) {
			String fileName = eachFile.toLowerCase().replace(XML_EXTENSION.toLowerCase(), "");
			//LOGGER.debug("bcDictionary.parseFiles: fileName=" +fileName);
			
			String relativePathToFile=XML_FOLDER+fileName+XML_EXTENSION;
			InputStream inputStream=Thread.currentThread().getContextClassLoader().getResourceAsStream(relativePathToFile);
			if(inputStream==null){
				LOGGER.error("bcDictionary.processXML critical Error - resource was not found: "+fileName);
			}
			returnValue.put(fileName, new bcXML(new HashMap<String, String>(), fileName, inputStream));
		}
		return returnValue;
	}

	private static List<String> retrieveXmlFilesNames() throws IOException {
		// read ZIP file 
		Enumeration<? extends ZipEntry> enumerator=new ZipFile(getCurrentJarFilePath()).entries();
		
		List<String> returnValue=new ArrayList<String>();
		while(enumerator.hasMoreElements()){
			
			ZipEntry nextEntry=enumerator.nextElement();
			String entryFilePath=nextEntry.getName();
			if(!StringUtils.startsWith(entryFilePath, XML_FOLDER)){
				continue;
			}
			String fileForAdd=StringUtils.removeStart(entryFilePath, XML_FOLDER);
			if(StringUtils.contains(fileForAdd, ZIP_SEPARATOR)){
				continue;
			}
			if(StringUtils.isEmpty(fileForAdd)){
				continue;
			}
			returnValue.add(fileForAdd);
		}
		return returnValue;
	}

	private static String getCurrentJarFilePath() {
		return getAbsolutePathToResource(StringUtils.EMPTY);
	}
	
	private static String getAbsolutePathToResource(String resourcePath){
		try {
			return StringUtils.substringBeforeLast(StringUtils.removeStart(bcDictionary.class.getResource(resourcePath).toURI().toString(), JAR_URL_PREAMBULA), JAR_URL_POSTAMBULA);
		} catch (URISyntaxException e) {
			LOGGER.error("can't retrieve URI from resource: "+resourcePath);
			return null;
		}
	}

}
