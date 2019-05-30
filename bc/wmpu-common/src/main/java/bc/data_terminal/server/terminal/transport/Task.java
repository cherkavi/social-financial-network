package bc.data_terminal.server.terminal.transport;

import java.io.Serializable;
import java.util.ArrayList;

public class Task implements Serializable{
	/** */
	private static final long serialVersionUID = 2L;
	/** уникальное имя данного задания */
	private String field_TaskName="";
	/** уникальное имя данного задания в контексте визуальных компонентов */
	private String field_VisualName="";
	/** хранилище для объектов данных */
	private ArrayList<Data> field_data=new ArrayList<Data>();
	/** флаг состояния данного задания */
	private int field_flag_state=0;
	/** сообщение для отображению конечному пользователю о возможном ответе */
	private String field_information;
	
	/** задача для выполнения */
	private static int STATE_FOR_DO=0;
	/** задача выполнена*/
	private static int STATE_DONE=1;
	/** задача не выполнена, возможно, вернулся код ошибки */
	private static int STATE_ERROR=2;
	
	/** 
	 * Создать задачу
	 * @param имя задачи 
	 */
	public Task(String TaskName){
		this.field_TaskName=TaskName;
	}

	/** 
	 * Создать задачу
	 * @param имя задачи
	 * @param имя визуального представления  
	 */
	public Task(String TaskName,String visualName){
		this.field_TaskName=TaskName;
		this.field_VisualName=visualName;
	}

	/** 
	 * Создать задачу
	 * @param имя задачи
	 * @param данные для задачи
	 */
	public Task(String TaskName, Data ... data){
		this.field_TaskName=TaskName;
		if((data!=null)&&(data.length>0)){
			for(int counter=0;counter<data.length;counter++){
				this.addData(data[counter]);
			}
		}
	}
	
	/** получить имя задания */
	public String getName(){
		return this.field_TaskName;
	}
	/** получить имя задания в визульном контексте */
	public String getVisualName(){
		return this.field_VisualName;
	}
	
	/** добавить Data в данные */
	public void addData(Data value){
		this.field_data.add(value);
	}
	
	/** получить из объекта данные, если они есть, или вернуть null 
	 * @return List<Data>[0] 
	 */
	public Data getData(){
		if(this.field_data.size()>0){
			return this.field_data.get(0);
		}else{
			return null;
		}
	}
	
	/** получить кол-во Data в пакете */
	public int getDataCount(){
		return this.field_data.size();
	}
	
	/** получить Data, согласно ее порядковому номер 
	 * @param number - номер Data в пакете 
	 * @return возвращает Data либо же возвращает null, если происходит выход за пределы 
	 * */
	public Data getData(int number){
		if((number<this.getDataCount())&&(number>=0)){
			return this.field_data.get(number);
		}else{
			return null;
		}
	}
	
	/** удалить все данные из объекта */
	public void clearData(){
		this.field_data.clear();
	}
	
	/** установить состояние для задачи как <b>выполненное</b> */
	public void setStateIsDone(){
		this.field_flag_state=Task.STATE_DONE;
	}

	/** установить состояние для задачи как <b>НЕ выполненнное</b>*/
	public void setStateIsError(){
		this.field_flag_state=Task.STATE_ERROR;
	}

	/** установить состояние для задачи как <b>для выполнения </b>*/
	public void setStateIsForDo(){
		this.field_flag_state=Task.STATE_FOR_DO;
	}
	
	/** Состояние задачи - ВЫПОЛНЕНА ? */
	public boolean isStateIsDone(){
		return (this.field_flag_state==Task.STATE_DONE);
	}

	/** Состояние задачи - ОШИБКА при выполнении */
	public boolean isStateIsError(){
		return (this.field_flag_state==Task.STATE_ERROR);
	}
	
	/** установить информацию для данной задачи */
	public void setInformation(String information){
		this.field_information=information;
	}
	
	/** */
	public String getInformation(){
		return this.field_information;
	}
}
