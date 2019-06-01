package ua.com.nmtg.private_office.jdbc.destination.decorator;

import ua.com.nmtg.private_office.jdbc.source.Parameter;

public interface IConditionDecorator {
	public boolean isNeedToExecute(Parameter parameter);
	public String decorate(String value);
}
