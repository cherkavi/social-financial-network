package bc.applet.CardManager.manager.actions.converter;

public class ConverterIntToString extends ActionStepConverter{
	
	/** */
	private static final long serialVersionUID = 1L;

	/** передать объект в виде Integer получить объект в виде String*/
	public ConverterIntToString(){
		
	}

	@Override
	public Object convert(Object object_for_convert) {
		String return_value=null;
		try{
			return_value=((Integer)object_for_convert).toString();
		}catch(Exception ex){}
		return return_value;
	}
}
