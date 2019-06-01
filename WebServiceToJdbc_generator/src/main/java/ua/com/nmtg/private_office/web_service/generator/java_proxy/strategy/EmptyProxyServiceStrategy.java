package ua.com.nmtg.private_office.web_service.generator.java_proxy.strategy;

public class EmptyProxyServiceStrategy implements IProxyServiceStrategy{
	
	@Override
	public String getAdditionalImport() {
		return null;
	}

	@Override
	public String getConstructorAndFields(String className, String fieldName) {
		return "public "+className+"(){  }";
	}

}
