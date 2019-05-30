package bc.ws.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ConvertParameters {
	
	/**
	 * converting parameters like:
	 * p_id_telgr to idTelegr
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException{
		if(args.length<1){
			System.err.println("first parameter should be a path to file");
			System.exit(1);
		}
		
		BufferedReader reader=new BufferedReader(new FileReader(new File(args[0])));
		String currentLine=null;
		while((currentLine=reader.readLine())!=null){
			// System.out.println("private String "+convertToParameterName(currentLine)+";");
			// System.out.println(convertToParameterName(currentLine)+",");
			System.out.println("String "+convertToParameterName(currentLine)+",");
			// System.out.println(retrieveDefaultParamters(currentLine)+",");
		}
		reader.close();
	}

	private static String retrieveDefaultParamters(String currentLine) {
		String[] values=currentLine.split(" ");
		for(int index=1;index<values.length;index++){
			if(values[index-1].equals(":=")){
				return "\""+values[index].substring(1, values[index].length()-2)+"\"";
			}
		}
		return "\"\"";
	}

	private static String convertToParameterName(String currentLine) {
		String[] values=currentLine.split(" ");
		String parameter=values[0].substring(2);
		
		StringBuilder returnValue=new StringBuilder();
		boolean flagUpper=false;
		for(int index=0;index<parameter.length();index++){
			char nextChar=parameter.charAt(index);
			if(nextChar=='_'){
				flagUpper=true;
				continue;
			}
			if(flagUpper){
				returnValue.append((nextChar+"").toUpperCase());
				flagUpper=false;
			}else{
				returnValue.append(nextChar);
			}
			
		}
		return returnValue.toString();
	}
}
