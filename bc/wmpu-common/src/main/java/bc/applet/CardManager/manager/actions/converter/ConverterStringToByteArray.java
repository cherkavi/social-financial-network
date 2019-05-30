package bc.applet.CardManager.manager.actions.converter;

public class ConverterStringToByteArray extends ActionStepConverter{

	/** */
	private static final long serialVersionUID = 1L;

	/** получает объект String и преобразует его в byte[]*/
	public ConverterStringToByteArray(){
		
	}

	@Override
	public Object convert(Object object_for_convert) {
		byte[] return_value=null;
		try{
			return_value=((String)object_for_convert).getBytes();
		}catch(Exception ex){
			
		}
		return return_value;
	}
	

}
