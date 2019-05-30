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
