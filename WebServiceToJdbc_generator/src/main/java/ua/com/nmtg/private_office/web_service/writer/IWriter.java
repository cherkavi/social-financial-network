package ua.com.nmtg.private_office.web_service.writer;

import ua.com.nmtg.private_office.web_service.generator.code_description.parser.exceptions.AnalizeParserException;


public interface IWriter {
	/** write SourceGenerator to Destination */
	public void write() throws AnalizeParserException;
}
