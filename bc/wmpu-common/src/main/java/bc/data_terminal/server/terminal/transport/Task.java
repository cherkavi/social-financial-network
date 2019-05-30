package bc.data_terminal.server.terminal.transport;

import java.io.Serializable;
import java.util.ArrayList;

public class Task implements Serializable{
	/** */
	private static final long serialVersionUID = 2L;
	/** ���������� ��� ������� ������� */
	private String field_TaskName="";
	/** ���������� ��� ������� ������� � ��������� ���������� ����������� */
	private String field_VisualName="";
	/** ��������� ��� �������� ������ */
	private ArrayList<Data> field_data=new ArrayList<Data>();
	/** ���� ��������� ������� ������� */
	private int field_flag_state=0;
	/** ��������� ��� ����������� ��������� ������������ � ��������� ������ */
	private String field_information;
	
	/** ������ ��� ���������� */
	private static int STATE_FOR_DO=0;
	/** ������ ���������*/
	private static int STATE_DONE=1;
	/** ������ �� ���������, ��������, �������� ��� ������ */
	private static int STATE_ERROR=2;
	
	/** 
	 * ������� ������
	 * @param ��� ������ 
	 */
	public Task(String TaskName){
		this.field_TaskName=TaskName;
	}

	/** 
	 * ������� ������
	 * @param ��� ������
	 * @param ��� ����������� �������������  
	 */
	public Task(String TaskName,String visualName){
		this.field_TaskName=TaskName;
		this.field_VisualName=visualName;
	}

	/** 
	 * ������� ������
	 * @param ��� ������
	 * @param ������ ��� ������
	 */
	public Task(String TaskName, Data ... data){
		this.field_TaskName=TaskName;
		if((data!=null)&&(data.length>0)){
			for(int counter=0;counter<data.length;counter++){
				this.addData(data[counter]);
			}
		}
	}
	
	/** �������� ��� ������� */
	public String getName(){
		return this.field_TaskName;
	}
	/** �������� ��� ������� � ��������� ��������� */
	public String getVisualName(){
		return this.field_VisualName;
	}
	
	/** �������� Data � ������ */
	public void addData(Data value){
		this.field_data.add(value);
	}
	
	/** �������� �� ������� ������, ���� ��� ����, ��� ������� null 
	 * @return List<Data>[0] 
	 */
	public Data getData(){
		if(this.field_data.size()>0){
			return this.field_data.get(0);
		}else{
			return null;
		}
	}
	
	/** �������� ���-�� Data � ������ */
	public int getDataCount(){
		return this.field_data.size();
	}
	
	/** �������� Data, �������� �� ����������� ����� 
	 * @param number - ����� Data � ������ 
	 * @return ���������� Data ���� �� ���������� null, ���� ���������� ����� �� ������� 
	 * */
	public Data getData(int number){
		if((number<this.getDataCount())&&(number>=0)){
			return this.field_data.get(number);
		}else{
			return null;
		}
	}
	
	/** ������� ��� ������ �� ������� */
	public void clearData(){
		this.field_data.clear();
	}
	
	/** ���������� ��������� ��� ������ ��� <b>�����������</b> */
	public void setStateIsDone(){
		this.field_flag_state=Task.STATE_DONE;
	}

	/** ���������� ��������� ��� ������ ��� <b>�� ������������</b>*/
	public void setStateIsError(){
		this.field_flag_state=Task.STATE_ERROR;
	}

	/** ���������� ��������� ��� ������ ��� <b>��� ���������� </b>*/
	public void setStateIsForDo(){
		this.field_flag_state=Task.STATE_FOR_DO;
	}
	
	/** ��������� ������ - ��������� ? */
	public boolean isStateIsDone(){
		return (this.field_flag_state==Task.STATE_DONE);
	}

	/** ��������� ������ - ������ ��� ���������� */
	public boolean isStateIsError(){
		return (this.field_flag_state==Task.STATE_ERROR);
	}
	
	/** ���������� ���������� ��� ������ ������ */
	public void setInformation(String information){
		this.field_information=information;
	}
	
	/** */
	public String getInformation(){
		return this.field_information;
	}
}
