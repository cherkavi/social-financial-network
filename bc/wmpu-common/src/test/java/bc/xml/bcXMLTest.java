package bc.xml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

public class bcXMLTest {

	@Test
	public void parseGivenFile() throws IOException {
		// given
		File xmlFile = createTestFile();
		Map<String, String> fields = new HashMap<String, String>();

		bcXML target = new bcXML(fields, xmlFile.getAbsolutePath());
		String prefix = target.getFirstObligatoryText();
		String suffix = target.getLastObligatoryText();

		// when

		// then
		Assert.assertEquals("Call-центр",
				target.getfieldTransl2("RU", "general"));
		Assert.assertEquals("Call-center",
				target.getfieldTransl2("EN", "general"));

		Assert.assertEquals(prefix + "Call-центр" + suffix,
				target.getfieldTransl("RU", "general", true));
		Assert.assertEquals(prefix + "Call-center" + suffix,
				target.getfieldTransl("EN", "general", true));
	}

	@Test
	public void parseEmptyFile() throws IOException {
		// given
		File xmlFile = createEmtpyTestFile();
		Map<String, String> fields = new HashMap<String, String>();

		bcXML target = new bcXML(fields, xmlFile.getAbsolutePath());
		// when

		// then
		Assert.assertNull(target.getfieldTransl2("RU", "general"));
		Assert.assertNull(target.getfieldTransl2("EN", "general"));
		System.out.println();
		System.out.println();
	}

	private File createEmtpyTestFile() throws IOException {
		File tempFile = File.createTempFile(
				bcXMLTest.class.getName() + "wrong", "test");
		tempFile.deleteOnExit();
		return tempFile;
	}

	private File createTestFile() throws IOException {
		File tempFile = File.createTempFile(bcXMLTest.class.getName(), "test");
		tempFile.deleteOnExit();

		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(tempFile)));
			writer.write(getXmlContent());
			writer.flush();
		} finally {
			writer.close();
		}
		return tempFile;
	}

	/**
	 * return full content of the XML file
	 * 
	 * @return
	 */
	private String getXmlContent() {
		StringBuilder returnValue = new StringBuilder();
		returnValue.append("﻿<?xml version=\"1.0\" encoding=\"UTF-8\" ?>")
				.append("\n");
		returnValue.append("<call_center>").append("\n");
		returnValue.append("	<general>").append("\n");
		returnValue.append("		<RU>Call-центр</RU>").append("\n");
		returnValue.append("		<UA>Call-центр</UA>").append("\n");
		returnValue.append("		<EN>Call-center</EN>").append("\n");
		returnValue.append("	</general>").append("\n");
		returnValue.append("	<registration>").append("\n");
		returnValue.append("		<RU>Регистрация вопросов</RU>").append("\n");
		returnValue.append("		<UA>Реєстрація запитань</UA>").append("\n");
		returnValue.append("		<EN>Questions registrations</EN>").append("\n");
		returnValue.append("	</registration>").append("\n");
		returnValue.append("	<administration>").append("\n");
		returnValue.append("		<RU>Администрирование</RU>").append("\n");
		returnValue.append("		<UA>Адміністрування</UA>").append("\n");
		returnValue.append("		<EN>Administration</EN>").append("\n");
		returnValue.append("	</administration>").append("\n");
		returnValue.append("</call_center>").append("\n");

		return returnValue.toString();
	}
}
