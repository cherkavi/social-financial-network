package ua.com.nmtg.private_office.web_service.generator.name_decorator;

public class EmptyNameDecorator implements INameDecorator{

	@Override
	public String decorate(String value) {
		return value;
	}

}
