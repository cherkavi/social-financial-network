package bc.xml;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class Dictionary {
	private static Map<String, bcXML>	elements;

	static {
		// parse elements
		elements = parseFiles("WEB-INF/XML");
	}

	private final static String			XML_EXTENSION	= ".xml";

	protected static Map<String, bcXML> parseFiles(String directory) {
		Map<String, bcXML> returnValue = new HashMap<String, bcXML>();

		File[] files = new File(directory).listFiles();
		for (File eachFile : files) {
			if (!eachFile.isFile()) {
				continue;
			}

			String absolutePath = eachFile.getAbsolutePath();
			if (!absolutePath.endsWith(XML_EXTENSION)) {
				continue;
			}
			String fileName = StringUtils.removeEnd(StringUtils
					.substringAfterLast(absolutePath, File.separator),
					XML_EXTENSION);
			returnValue.put(fileName, new bcXML(new HashMap<String, String>(),
					absolutePath));
		}
		return returnValue;
	}

	public static bcXML getInstance(String fileName) {
		return elements.get(fileName);
	}

}
