package bonclub.reports;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import database.StaticConnector;

import bonclub.reports.engine.ReportEngine;

/**
 * Сервлет, который загружает все доступные переменные 
 */
public class StartupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	@Override
	public void init() throws ServletException {
		super.init();
		// INFO OfficePrivatePartnerReporter start первоначальная загрузки настроек
		Logger.getRootLogger().setLevel(Level.ERROR);
		Logger.getLogger("bonclub.reports").setLevel(Level.DEBUG);
		Logger.getRootLogger().addAppender(new ConsoleAppender(new PatternLayout(PatternLayout.TTCC_CONVERSION_PATTERN)));
		
		String osName=System.getProperty("os.name");
		String prefix=null;
		if(osName.indexOf("Linux")>=0){
			prefix="linux_";
		}else if(osName.indexOf("Windows")>=0){
			prefix="windows_";
		}else{
			assert false:"StartupServlet OS does not recognized";
		}
		
		StaticConnector.login=this.getInitParameter("oracle_login");
		StaticConnector.password=this.getInitParameter("oracle_password");
		StaticConnector.databaseUrl=this.getInitParameter("oracle_url");
		
		String parameter=this.getInitParameter(prefix+"path_settings");
		ReportExecuterParameters.loadVariables(parameter);
		// запустить "Создатель отчетов"
		ReportEngine.notifyAboutNewReport();
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
