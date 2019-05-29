package com.bc;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import junit.framework.Test;

class TestCaseBuilder {
	private List<String> listOfPositive=new LinkedList<String>();
	private List<String> listOfNegative=new LinkedList<String>();
	
	TestCaseBuilder appendPositive(String className){
		this.listOfPositive.add(className);
		return this;
	}
	
	TestCaseBuilder appendNegative(String className){
		this.listOfNegative.add(className);
		return this;
	}

	List<? extends Test> build() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException{
		// create source code and compile each file
		for(String eachPositive:this.listOfPositive){
			createTest(eachPositive, getPositiveFileContent(eachPositive));
		}
		for(String eachNegative:this.listOfNegative){
			createTest(eachNegative, getNegativeFileContent(eachNegative));
		}
		
		// create class loader
		URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { new File(PATH_BASE).toURI().toURL() });
		
		// load all classes
		List<NamedTestCase> returnValue=new ArrayList<NamedTestCase>(this.listOfNegative.size()+this.listOfPositive.size());
		for(String eachPositive:this.listOfPositive){
			Class<?> cls = Class.forName(eachPositive, true, classLoader);
			returnValue.add((NamedTestCase)cls.newInstance());
		}
		for(String eachNegative:this.listOfNegative){
			Class<?> cls = Class.forName(eachNegative, true, classLoader);
			returnValue.add((NamedTestCase)cls.newInstance());
		}

		// sort for output
		Collections.sort(returnValue, new Comparator<NamedTestCase>(){
			@Override
			public int compare(NamedTestCase o1, NamedTestCase o2) {
				return o2.getName().compareTo(o1.getName());
			}
		});
		return returnValue; 
	}
	
	/**
	 * @param className - name of class
	 * @param content - text content of the java class 
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private static void createTest(String className, String content) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		// create class with positive response
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		File javaFile=createFile(className);

		FileUtils.write(javaFile, content, Charset.forName("UTF-8"));
		
		compiler.run(null, null, null, javaFile.getAbsolutePath());
		markForDelete(javaFile);
		
	}
	
	
	private final static String PREFIX_JAVA=".java";
	private final static String PREFIX_CLASS=".class";
		
	private static void markForDelete(File javaFile) {
		javaFile.deleteOnExit();
		String absolutePath=javaFile.getAbsolutePath();
		new File(StringUtils.removeEnd(absolutePath, PREFIX_JAVA)+PREFIX_CLASS).deleteOnExit();
	}

	private final static String SUB_FOLDER="fortest";
	private static String PATH_BASE; 
	static{
		PATH_BASE=StringUtils.appendIfMissing(System.getProperty("java.io.tmpdir"), File.separator)+SUB_FOLDER+File.separator;

		File folderForJava=new File(PATH_BASE);
		folderForJava.mkdirs();
		folderForJava.deleteOnExit();
	}
	
	private static File createFile(String className) throws IOException{
		return new File(PATH_BASE+className+".java");
	}
	
/*	public static Test negative(String className, String errorMessage){
		// create class with negative response
	}
	*/
	
	private static String getPositiveFileContent(String className){
		return "import com.bc.NamedTestCase; public class "+className+" extends NamedTestCase { public "+className+"(){super(\""+className+"\", \""+className+"\");} public void "+className+"(){}}";
	}

	private static String getNegativeFileContent(String className){
		return "import junit.framework.Assert; import com.bc.NamedTestCase; public class "+className+" extends NamedTestCase { public "+className+"(){super(\""+className+"\", \""+className+"\");} public void "+className+"(){ Assert.assertFalse(true);}}";
	}

	
}
