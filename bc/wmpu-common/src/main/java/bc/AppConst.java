package bc;

public class AppConst{

    private AppConst(){
    }

    public static final String appRoot="/wmpu";
    public static String appPath="";
    //public static String contextPath="";
    public static String interfaceVersion="4.0.1.8";
    
    public static final String WMPU_MAIN_PAGE="/crm/main.jsp";

    /**
     * using from JSP
     * @return
     */
    public static String getInstanceName(){
		return appRoot.substring(1);
    }
    
}
