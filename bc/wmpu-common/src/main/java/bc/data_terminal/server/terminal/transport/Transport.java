package bc.data_terminal.server.terminal.transport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Transport implements Serializable{
	/** */
	private static final long serialVersionUID = 2L;
	/** хранилище для задач */
	private ArrayList<Task> field_task=new ArrayList<Task>();
	/** хранилище для системных параметров*/
	private HashMap<String,String> field_transport_parameters=new HashMap<String,String>();
	
	/** флаг направления - от сервера к клиенту, либо от клиента к серверу */
	private int field_flag_direction=0;
	private static int DIRECTION_FROM_CLIENT=0;
	private static int DIRECTION_FROM_SERVER=1;
	
	/** установить системный параметр */
	public void setTransportParameter(String key, String value){
		this.field_transport_parameters.put(key, value);
	}
	
	/** получить системный параметр */
	public String getTransportParameter(String key){
		String return_value=this.field_transport_parameters.get(key);
		if(return_value==null){
			return_value="";
		}
		return return_value;
	}
	
	/** Транспорт для общения между клиентом и сервером */
	public Transport(){
	}
	
	/** Транспорт для общения между клиентом и сервером */
	public Transport(Task task){
		this.addTask(task);
	}
	
	/** добавить задачу в пакет */
	public void addTask(Task task){
		this.field_task.add(task);
	}
	
	/** получить первую задчу из списка, если она есть 
	 * @return List<Task>[0] или null, если список задач пуст 
	 *  
	 */
	public Task getTask(){
		if(this.getTaskCount()>0){
			return this.getTask(0);
		}else{
			return null;
		}
	}
	
	/** получить задачу из пакета */
	public Task getTask(int number){
		if((number<this.getTaskCount())&&(number>=0)){
			return this.field_task.get(number);
		}else{
			return null;
		}
	}
	
	/** получить кол-во задач */
	public int getTaskCount(){
		return this.field_task.size();
	}
	
	/** установить направление от Клиента к серверу */
	public void setDirectionFromClient(){
		this.field_flag_direction=Transport.DIRECTION_FROM_CLIENT;
	}
	
	/** установить направление от Сервера к клиенту */
	public void setDirectionFromServer(){
		this.field_flag_direction=Transport.DIRECTION_FROM_SERVER;
	}
	
	/** направление от Клиента к серверу ?*/
	public boolean isDirectionFromClient(){
		return (this.field_flag_direction==Transport.DIRECTION_FROM_CLIENT);
	}
	
	/** направление от Сервера к клиенту ?*/
	public boolean isDirectionFromServer(){
		return (this.field_flag_direction==Transport.DIRECTION_FROM_SERVER);
	}
}
