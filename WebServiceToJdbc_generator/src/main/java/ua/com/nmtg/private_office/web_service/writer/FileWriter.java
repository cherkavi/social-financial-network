package ua.com.nmtg.private_office.web_service.writer;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import ua.com.nmtg.private_office.web_service.generator.ISourceGenerator;
import ua.com.nmtg.private_office.web_service.generator.code_description.elements.UnitDescription;
import ua.com.nmtg.private_office.web_service.generator.code_description.parser.CodeParser;
import ua.com.nmtg.private_office.web_service.generator.code_description.parser.exceptions.AnalizeParserException;

public class FileWriter implements IWriter{
	private final ISourceGenerator generator;
	private final String sourcePath;
	private final String destinationDirectory;
	
	/**
	 * @param generator - source code generator 
	 * @param sourcePath - path of source code 
	 * @param destinationDirectory - path to output directory 
	 * @param visitors - additional visitors for analise the annotation of the source file  
	 */
	public FileWriter(ISourceGenerator generator,  
					  String sourcePath, 
					  String destinationDirectory
					  ){
		this.generator=generator;
		this.sourcePath=sourcePath;
		this.destinationDirectory=destinationDirectory;
	}
	 
	  
	@Override
	public void write() throws AnalizeParserException {
		if(sourcePath==null){
			throw new IllegalArgumentException("check the Path to Source file ");
		}
		if(destinationDirectory==null){
			throw new IllegalArgumentException("check the Path to Destination directory ");
		}
		UnitDescription unitDescription=CodeParser.parseSourceFile(sourcePath, generator.getAnnotationVisitors());
		String pathToFile=getPathToFile(destinationDirectory,generator.getSourceName(unitDescription));
		try{
			FileUtils.writeStringToFile(new File(pathToFile), 
										generator.generateSourceCode(unitDescription)) ;
		}catch(IOException ex){
			System.err.println("Write data to File "+pathToFile+"   Exception:"+ex.getMessage());
		}
	}

	
	private String getPathToFile(String directory, String fileName){
		String clearDirectoryPath=StringUtils.trimToEmpty(directory);
		if(!clearDirectoryPath.endsWith(File.separator)){
			clearDirectoryPath+=File.separator;
		}
		return clearDirectoryPath+fileName;
	}
}
