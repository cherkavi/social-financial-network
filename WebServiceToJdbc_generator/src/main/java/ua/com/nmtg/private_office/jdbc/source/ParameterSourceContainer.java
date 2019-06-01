package ua.com.nmtg.private_office.jdbc.source;

import java.util.TreeSet;

public class ParameterSourceContainer implements IParametersSource{
	private final String ownerName;
	private final TreeSet<Parameter> parameters;
	private final String packageName;
	
	public ParameterSourceContainer(String groupName,
								    String packageName,
									TreeSet<Parameter> parameters) {
		super();
		this.ownerName = groupName;
		this.parameters = parameters;
		this.packageName=packageName;
	}

	@Override
	public String getOwner() {
		return this.ownerName;
	}

	@Override
	public TreeSet<Parameter> getParameters() {
		return this.parameters;
	}

	@Override
	public String getPackage() {
		return this.packageName;
	}

}
