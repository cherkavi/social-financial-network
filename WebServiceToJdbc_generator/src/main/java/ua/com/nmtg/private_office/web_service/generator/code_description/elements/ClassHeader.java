package ua.com.nmtg.private_office.web_service.generator.code_description.elements;

public class ClassHeader {
	private final String name;
	private final boolean isInteface ;
	
	public ClassHeader(String name, boolean isInterface ){
		this.name=name;
		this.isInteface=isInterface;
	}

	public String getName() {
		return name;
	}

	public boolean isInteface() {
		return isInteface;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isInteface ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClassHeader other = (ClassHeader) obj;
		if (isInteface != other.isInteface)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	

	@Override
	public String toString() {
		StringBuilder returnValue=new StringBuilder();
		returnValue.append("public ");
		returnValue.append( (this.isInteface)?" interface ":" class " );
		returnValue.append(this.name);
		return returnValue.toString();
	}
}
