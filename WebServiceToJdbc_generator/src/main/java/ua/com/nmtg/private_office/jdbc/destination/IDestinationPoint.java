package ua.com.nmtg.private_office.jdbc.destination;

import java.util.Collection;

import ua.com.nmtg.private_office.jdbc.destination.exception.DestinationException;
import ua.com.nmtg.private_office.jdbc.source.Parameter;

public interface IDestinationPoint {
	public void write(Collection<Parameter> parameterSource) throws DestinationException;
}
