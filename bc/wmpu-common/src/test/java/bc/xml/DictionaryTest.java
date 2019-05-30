package bc.xml;

import java.util.Map;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

public class DictionaryTest {

	@Ignore
	@Test
	public void parseElements() {
		// given
		String pathToXml = "/home/technik/projects/nmtg/BonusDemo/WEB-INF/XML";

		// when
		Map<String, bcXML> files = Dictionary.parseFiles(pathToXml);

		// then
		Assert.assertNotNull(files.get("accounts"));
		Assert.assertEquals("Банковские счета", files.get("accounts")
				.getfieldTransl2("RU", "general"));
		Assert.assertEquals("Bank accounts", files.get("accounts")
				.getfieldTransl2("EN", "general"));

	}
}
