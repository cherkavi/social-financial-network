package bc.data_terminal.server.terminal.transport;

import java.io.*;

import org.apache.log4j.Logger;

public class Data implements Serializable{
	private final static Logger LOGGER=Logger.getLogger(Data.class);
	/** */
	private static final long serialVersionUID = 1L;
	/** ���������� ��� ������ � ��������� Task */
	private String field_DataName="";
	/** ��� ������, ������� �� �������� �� ().getClass().getName() */
	private String field_ClassName=null;
	/** ������, ������� �������� ��������������� ������ */
	private byte[] field_data=null; 
	
	
	/** ������ ��� ������� Task
	 * @param DataName ��������, ���������� ��� ������ � ��������� ������� Task
	 * @param object_for_send ������, ������� ����� ����������/���������
	 * */
	public Data(String DataName, Object object_for_send){
		this.field_DataName=DataName;
		if(object_for_send!=null){
			this.field_ClassName=object_for_send.getClass().getName();
			field_data=writeObjectToByteArray(object_for_send);
		}
	}

	/** ������ ��� ������� Task
	 * @param object_for_send ������, ������� ����� ����������/���������
	 * */
	public Data(Object object_for_send){
		this("",object_for_send);
	}
	
	/** �������� ������ */
	public Object getObject(){
		Object return_value=null;
		return_value=readObjectFromByteArray(field_data);
		return return_value;
	}
	/** �������� ��� ������ ��� ����������� ������ */
	public String getClassName(){
		return this.field_ClassName;
	}
	
	/** �������� ��� ��� ������ ( ��������, ���������� � ��������� Task) */
	public String getDataName(){
		return this.field_DataName;
	}
	
	
	/** ��������/������������� ������ � byte[] */
	private byte[] writeObjectToByteArray(Object object_for_write){
        try{
            ByteArrayOutputStream byte_array=new ByteArrayOutputStream();
            ObjectOutputStream oos=new ObjectOutputStream(byte_array);
            oos.writeObject(object_for_write);
            oos.close();
            byte_array.close();
            return byte_array.toByteArray();
        }catch(IOException ex){
            LOGGER.debug("writeObjectToByteArray: ERROR :"+ex.getMessage());
            return null;
        }
    }
    
    /** ���������/������������� ������ �� byte[]*/
    private Object readObjectFromByteArray(byte[] data){
        Object return_value=null;
        try{
            ByteArrayInputStream inputStream=new ByteArrayInputStream(data);
            ObjectInputStream ois=new ObjectInputStream(inputStream);
            return_value=ois.readObject();
            ois.close();
            inputStream.close();
        }catch(IOException ex){
            LOGGER.debug("readObjectFromByteArray ERROR:"+ex.getMessage());
        }catch(ClassNotFoundException ex){
            LOGGER.debug("Class not found exception:"+ex.getMessage());
        }
        return return_value;
    }
	
	
}
