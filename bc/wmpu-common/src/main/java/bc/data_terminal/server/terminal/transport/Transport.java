package bc.data_terminal.server.terminal.transport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Transport implements Serializable{
	/** */
	private static final long serialVersionUID = 2L;
	/** ��������� ��� ����� */
	private ArrayList<Task> field_task=new ArrayList<Task>();
	/** ��������� ��� ��������� ����������*/
	private HashMap<String,String> field_transport_parameters=new HashMap<String,String>();
	
	/** ���� ����������� - �� ������� � �������, ���� �� ������� � ������� */
	private int field_flag_direction=0;
	private static int DIRECTION_FROM_CLIENT=0;
	private static int DIRECTION_FROM_SERVER=1;
	
	/** ���������� ��������� �������� */
	public void setTransportParameter(String key, String value){
		this.field_transport_parameters.put(key, value);
	}
	
	/** �������� ��������� �������� */
	public String getTransportParameter(String key){
		String return_value=this.field_transport_parameters.get(key);
		if(return_value==null){
			return_value="";
		}
		return return_value;
	}
	
	/** ��������� ��� ������� ����� �������� � �������� */
	public Transport(){
	}
	
	/** ��������� ��� ������� ����� �������� � �������� */
	public Transport(Task task){
		this.addTask(task);
	}
	
	/** �������� ������ � ����� */
	public void addTask(Task task){
		this.field_task.add(task);
	}
	
	/** �������� ������ ����� �� ������, ���� ��� ���� 
	 * @return List<Task>[0] ��� null, ���� ������ ����� ���� 
	 *  
	 */
	public Task getTask(){
		if(this.getTaskCount()>0){
			return this.getTask(0);
		}else{
			return null;
		}
	}
	
	/** �������� ������ �� ������ */
	public Task getTask(int number){
		if((number<this.getTaskCount())&&(number>=0)){
			return this.field_task.get(number);
		}else{
			return null;
		}
	}
	
	/** �������� ���-�� ����� */
	public int getTaskCount(){
		return this.field_task.size();
	}
	
	/** ���������� ����������� �� ������� � ������� */
	public void setDirectionFromClient(){
		this.field_flag_direction=Transport.DIRECTION_FROM_CLIENT;
	}
	
	/** ���������� ����������� �� ������� � ������� */
	public void setDirectionFromServer(){
		this.field_flag_direction=Transport.DIRECTION_FROM_SERVER;
	}
	
	/** ����������� �� ������� � ������� ?*/
	public boolean isDirectionFromClient(){
		return (this.field_flag_direction==Transport.DIRECTION_FROM_CLIENT);
	}
	
	/** ����������� �� ������� � ������� ?*/
	public boolean isDirectionFromServer(){
		return (this.field_flag_direction==Transport.DIRECTION_FROM_SERVER);
	}
}
