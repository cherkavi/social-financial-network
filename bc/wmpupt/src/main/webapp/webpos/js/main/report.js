// Объект, который имитирует двухмерную таблицу, с возможностями добавления и поиска по ней
util=new Object();
// constructor
util.table=function(dimension){
	this.field_columns=new Array();
	this.field_rows=new Array();
	for(var counter=0;counter<dimension;counter++){
		this.field_columns[counter]="";
	}
}
// установить текст для заголовков
util.table.prototype.setColumnText=function(column_number,column_name){
	this.field_columns[column_number]=column_name;
}
// установить значение для таблицы
util.table.prototype.setValue=function(row_number,column_number,value){
	if(this.field_rows.length<=row_number){
		this.field_rows[row_number]=new Array();
	}
	this.field_rows[row_number][column_number]=value;
}
// получить значение по ячейке
util.table.prototype.getValue=function(row_number,column_number){
	return this.field_rows[row_number][column_number];
}
// добавить еще одну строку в виде массива
util.table.prototype.addRow=function(array){
	this.field_rows[this.field_rows.length]=array;
}
// получить кол-во строк
util.table.prototype.getRowCount=function(){
	return this.field_rows.length;
}
// вернуть true если существует ключ
util.table.prototype.keyExists=function(column_number,value){
	var return_value=false;
	for(var counter=0;counter<this.field_rows.length;counter++){
		if(this.getValue(counter,column_number)==value){
			return_value=true;
			break;
		}
	}
	return return_value;
}
// вернуть массив из индексов ключей
util.table.prototype.getIndexArray=function(column_number,value){
	var return_value=new Array();
	for(var counter=0;counter<this.field_rows.length;counter++){
		if(this.getValue(counter,column_number)==value){
			return_value[return_value.length]=counter;
		}
	}
	return return_value;
}
// функция вывода на консоль
util.table.prototype.out=function(){
	for(var row_counter=0;row_counter<this.field_rows.length;row_counter++){
		var current_string="";
		for(var column_counter=0;column_counter<this.field_columns.length;column_counter++){
			current_string=current_string+"  |  "+column_counter+":"+this.getValue(row_counter,column_counter);
		}
	}
}

function select_all(tag_id){
	var select_object=document.getElementById(tag_id);
	for(var counter=0;counter<select_object.options.length;counter++){
		//select_object.childNodes[counter].selected="selected";
		select_object.options[counter].selected=true;
	}
}
function deselect_all(tag_id){
	var select_object=document.getElementById(tag_id);
	for(var counter=0;counter<select_object.options.length;counter++){
		select_object.options[counter].selected=false;
	}
	if(select_object.childNodes.length>0){
		if(select_object.childNodes[0]!=null){
			select_object.options[0].selected=true;	
		}else{
			select_object.options[1].selected=true;
		}
	}
}

// создание объекта, который содержит данные обо всех зависимых элементах 
var field_data;
field_data=new util.table(4);
field_element_width=new util.table(2);

// служебная функция вывода данных на консоль 
function toConsole(data){
	var console=document.getElementById('console');
	if (console!=null){
		var newline=document.createElement("div");
	    console.appendChild(newline);
	    var txt=document.createTextNode(data);
	    newline.appendChild(txt);
	  }
}


// функция, которая получает значение по выделенному элементу SELECT
function get_select_value(element_id){
	var return_value="";
	var element=document.getElementById(element_id);
	if(element!=null){
		for(var counter=0;counter<element.childNodes.length;counter++){
			var current_element=element.childNodes.item(counter);
			if(current_element.selected){
				if(return_value!=""){
					return_value=return_value+","+current_element.value;						
				}else{
					return_value=current_element.value;
				}
			}
		}				
	}
	return return_value;
}
var response_value;
// функция, которая получает управление во время возврата значения
function get_server_responce(responce){
	//ответ сервера состоит из объекта Fragment:
	//	String Function;
	//	String[] Parameters;
		//    Parameters[]:[0] ID element
		//	  Parameters[]:[1] Default_value
		//	  Values[][]:[0] - SELECT.OPTION.values
		//    Values[][]:[1] - SELECT.OPTION.text
	try{
		if(responce.name!="error"){
			// check function
			if(responce.name=="FILL_ELEMENT"){
				load_data_to_select(responce.parameters[0],responce.parameters[1],responce.values[0],responce.values[1]);
			}
		}
	}catch(Exception){
		
	}
}

// функция, которая заполняет нужный SELECT(id_select)   
//	default_value - значение OPTION.VALUE по умолчанию - нужно поставить тэг select
//	array_of_values - значения для OPTION.VALUE
//	array_of_text - значения для OPTION.TEXT

function load_data_to_select(id_select,default_value,array_of_values, array_of_text){
	var select_element=document.getElementById(id_select);
	if(select_element!=null){
		for(var counter=select_element.childNodes.length-1;counter>=0;counter--){
			select_element.removeChild(select_element.childNodes[counter]);
		}
		// select_element.innerHTML=html_text;
		for(counter=0;counter<array_of_values.length;counter++){
			var option_element=document.createElement("option");
			var text_element=document.createTextNode(array_of_text[counter]);
			option_element.value=array_of_values[counter];
			if(array_of_values[counter]==default_value){
				option_element.selected="selected";
			}
			option_element.appendChild(text_element);
			select_element.appendChild(option_element);
		}
		if(select_element.length>5)select_element.size=5;
	}
}
// функция, которая обращается с запросом к серверу  
//	id_function - идентификатор функции
//	value_source - 	двухмерный массив возможных параметров для запроса ( сжимаем ID_DESTINATION,NUMBER,ORDER) получаем:
//					<ID_SOURCE>	<value of ID_SOURCE>
//					<id_source> - идентификатор HTML, который инициировал запрос, и является необходимым параметром для запроса на сервере для приемника (id_destination)
//								<value of ID_SOURCE> - значение идентификатора, который инициировал запрос
//	id_destination - идентификатор HTML, который будет принимать данные с сервера 
//	report_number - необходимые данные для получения параметров на сервере (получения самого запроса)
//	order_number - необходимые данные для получения параметров на сервере (получения самого запроса)

function send_data(id_function,value_source,id_destination,report_number,order_number, sessionId){
	 var fragment=new Object();
	 fragment.name=id_function;
	 fragment.parameters=new Array(id_destination,report_number,order_number);
	 fragment.values=value_source;
	 fragment.sessionId = sessionId;
	 ReporterUtility.sendToServer(fragment,get_server_responce);
}
// функция, которая анализирует объект SELECT, который вызвал событий onChange
function on_change_select(changed_object, sessionId){
	//поиск в объекте field_data уникального ID измененного элемента, и если он найден - заполнение объекта source
	var array_of_element=field_data.getIndexArray(0,changed_object.id);
	for(var counter=0;counter<array_of_element.length;counter++){
		send_data('FILL_ELEMENT',get_values(array_of_element[counter]),field_data.getValue(array_of_element[counter],1),field_data.getValue(array_of_element[counter],2),field_data.getValue(array_of_element[counter],3),sessionId);
	}
	
} 
// функция, которая возвращает двухмерный массив из значений, которые получены из сжатия/GROUP_BY  <ID_DESTINATION><NUMBER><ORDER> 
//	получаем <ID_SOURCE> <value of ID_SOURCE>

function get_values(control_counter){
	var return_value=new Array();
	var temp_value=field_data.getRowCount();
	// пробежаться по всему списку и получить все значения
	for(var index=0;index<field_data.getRowCount();index++){
		if(  (field_data.getValue(control_counter,1)==field_data.getValue(index,1))
		   &&(field_data.getValue(control_counter,2)==field_data.getValue(index,2))
		   &&(field_data.getValue(control_counter,3)==field_data.getValue(index,3))
			){
			// add to return_value
			return_value[return_value.length]=new Array(field_data.getValue(index,0),get_select_value(field_data.getValue(index,0)));
		}else{
			//nothing adding
		}
	}
	return return_value;
}

// функция, которая присоединяет данные, полученные с текущей страницей к объекту field_data
//  param_name,  - имя параметра из запроса
//  this_html_name, - имя HTML от которого зависит данных запрос
//  report_id, - номер отчета
//  order_number - номер переменной в запросе	
function fill_data(id_destination,id_source,report_id,order_number){
	//window.alert(id_destination);
	field_data.addRow(new Array(id_destination,id_source,report_id,order_number));
}

// функция, которая присоединяет данные о необходимой ширине объекта после загрузки страницы 
function add_object_width(element_id, element_width){
	field_element_width.addRow(new Array(element_id,element_width));
}

//функция, которая задает ширину для объекта
function set_width(element_name, value){
	try{
		document.getElementById(element_name).style.width = value+'px';
	}catch(exception){
	}
}

// функция, которая перебирает все элементы, которым необходимо задать ширину
function set_width_for_all(){
	var element_name;
	var element_width;
	//window.alert("set_width_for_all()");
	for(var index=0;index<field_element_width.getRowCount();index++){
		element_name=field_element_width.getValue(index,0);
		element_width=field_element_width.getValue(index,1);
		//set_width(field_element_width.getValue(index,0),field_element_width.getValue(index,1));
		set_width(element_name,element_width);
	}
} 	