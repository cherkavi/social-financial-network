	<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="java.util.HashMap"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="../CSS/main.css">
	<script language="JavaScript">
		var indx_location;
		function goBack() { history.back(); }
	</script>
	<%=Bean.getTopFrameCSS() %>
	<%= Bean.getBottomFrameCSS() %>
    <%=Bean.getTableSorterCSS("[0,0]") %>
	<script type="text/javascript" language="javascript" src="../dwr/interface/responseUtility.js"></script>
	<script type="text/javascript" language="javascript" src="../dwr/engine.js"></script>


<title><%=Bean.calculatorXML.getfieldTransl("general", false) %></title>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

request.setCharacterEncoding("UTF-8");
String pageFormName = "FIND_TERMINALS";

String field = Bean.getDecodeParam(parameters.get("field"));
if (field==null || "".equalsIgnoreCase(field)) {
	field = "";
}

String formula = "";

String id_line = Bean.getDecodeParam(parameters.get("id_line"));
if (id_line==null || "".equalsIgnoreCase(id_line)) {
	id_line = "";
} else {
	if (!"".equalsIgnoreCase(field)) {
		if (!"".equalsIgnoreCase(id_line)) {
			if ("amount".equalsIgnoreCase(field)) {
				formula = Bean.getCalculatorExpression(id_line, "AMOUNT");
			} else if ("assignment_posting".equalsIgnoreCase(field)) {
				formula = Bean.getCalculatorExpression(id_line, "ASSIGNMENT_POSTING");
			} else if ("payment_function".equalsIgnoreCase(field)) {
				formula = Bean.getCalculatorExpression(id_line, "PAYMENT_FUNCTION");
			} else if ("rel_payment_function".equalsIgnoreCase(field)) {
				formula = Bean.getCalculatorRelationShipExpression(id_line, "PAYMENT_FUNCTION");
			}
			if (formula==null || "".equalsIgnoreCase(formula)) {
				formula = "";
			}
		} else {
			formula = "";
		}
	}
}

String calc = Bean.getDecodeParam(parameters.get("calc"));
if (calc==null || "".equalsIgnoreCase(calc)) {
	calc = "no";
}

String need_operation = Bean.getDecodeParam(parameters.get("need_operation"));
if (need_operation==null || "".equalsIgnoreCase(need_operation)) {
	need_operation = "no";
}

String cd_club_rel_type = Bean.getDecodeParam(parameters.get("cd_club_rel_type"));
if (cd_club_rel_type==null || "".equalsIgnoreCase(cd_club_rel_type)) {
	cd_club_rel_type = "";
}

String cd_bk_operation_type = Bean.getDecodeParam(parameters.get("cd_bk_operation_type"));
if (cd_bk_operation_type==null || "".equalsIgnoreCase(cd_bk_operation_type)) {
	cd_bk_operation_type = "";
}


%>	

</head>
<body style="background: rgb(256, 256, 230);"  onload="init()">

 <script type="text/javascript">
  var element_text=new Array();

   function init(){
   <%=Bean.getComissionTypeArray("", "element_text")%>
  }

  function setTitle(select_element){
    var selected_index=select_element.selectedIndex;
   select_element.title = element_text[selected_index];
  }  
 </script>

<script type="text/javascript">

function js_countTextAreaChars(text) {
    var n = 0;
    for (var i = 0; i < text.length; i++) {
        if (text.charAt(i) != '\r') {
            n++;
        }
    }
    return n;
}

function js_getCursorPositionBegin(textArea) {
    var start = 0;
    var end = 0;
    if (document.selection) { // IE…
        textArea.focus();
        var sel1 = document.selection.createRange();
        var sel2 = sel1.duplicate();
        sel2.moveToElementText(textArea);
        var selText = sel1.text;
        sel1.text = "|~";
        var index = sel2.text.indexOf("|~");
        start = js_countTextAreaChars((index == -1) ? sel2.text : sel2.text.substring(0, index));
        end = js_countTextAreaChars(selText) + start;
        sel1.moveStart("character", -2);
        sel1.text = selText;
    } else if (textArea.selectionStart || (textArea.selectionStart == 0)) { // Mozilla/Netscape…
        start = textArea.selectionStart;
        end = textArea.selectionEnd;
    }
    //return new js_CursorPos(start, end);
 return start;
}

function js_getCursorPositionEnd(textArea) {
    var start = 0;
    var end = 0;
    if (document.selection) { // IE…
        textArea.focus();
        var sel1 = document.selection.createRange();
        var sel2 = sel1.duplicate();
        sel2.moveToElementText(textArea);
        var selText = sel1.text;
        sel1.text = "~|";
        var index = sel2.text.indexOf("~|");
        start = js_countTextAreaChars((index == -1) ? sel2.text : sel2.text.substring(0, index));
        end = js_countTextAreaChars(selText) + start;
        sel1.moveStart("character", -2);
        sel1.text = selText;
    } else if (textArea.selectionStart || (textArea.selectionStart == 0)) { // Mozilla/Netscape…
        start = textArea.selectionStart;
        end = textArea.selectionEnd;
    }
    //return new js_CursorPos(start, end);
 return end;
}

function js_setCursorPosition(textArea, position_begin,position_end) {
    if (document.selection) { // IE…
        var sel = textArea.createTextRange();
        sel.collapse(true);
        sel.moveStart("character", position_begin);
        sel.moveEnd("character", position_end);
        sel.select();
    } else if (textArea.selectionStart || (textArea.selectionStart == 0)) { // Mozilla/Netscape…
        textArea.selectionStart = position_begin;
        textArea.selectionEnd = position_end;
    }
    textArea.focus();
}
function toConsole(value,value2){
	var console_element=document.getElementById("console");
	if(console_element){
		var span_element=document.createElement("div");
		var text_element=document.createTextNode(value);
		if(value2){
			text_element=document.createTextNode(value+value2);
		}else{
			text_element=document.createTextNode(value);
		}
		
		span_element.appendChild(text_element);
		console_element.appendChild(span_element);
	}
}

//----------- ПРОВЕРКА ВЫРАЖЕНИЯ КАЛЬКУЛЯТОРА -------------------
function dwr_check_calc_expression(){
	var val = document.getElementById("destination_text").value;	

	var el = document.getElementById("check_result");
    el.innerHTML =  '<img id=\"imgWait\" src=\"../images/ajax-loader-circle.gif\" align=\"absmiddle\" >';
	
	var client_transport=new Object();
	
	client_transport.functionName="CHECK_CALCULATOR_EXPRESSION";
	client_transport.functionParam=val;
	client_transport.functionParam2='<%=calc%>';
	client_transport.idDestination='<%=field%>';
	client_transport.language='<%=Bean.getLanguage()%>';
	client_transport.sessionId		= '<%= Bean.getSessionId()%>';
	responseUtility.get_responce(client_transport, get_calc_expression);
}

function get_calc_expression(transport_from_server){
	var respText = transport_from_server.functionHTMLReturnValue;

	respFormula = transport_from_server.functionValues[0];
	if (respFormula==null) {
		respFormula = '';
	}
	var el = document.getElementById("check_result");
	if (respText==null) {
		respText = "Ok";
	}
    el.innerHTML =  respText + ' - ' + respFormula;
    confirmed = 1;
}

</script>

<script type="text/javascript">
	var array_of_expression=new Array("first","second","third","fourth");
	var array_of_expression2=new Array("element_1","element_2","element_3","element_4");
	
	var destination_text="destination_text";
	var source_elements="source_elements"
	var delimeter_begin="\"";
	var delimeter_end="\"";

	function getPosition(){
		var destination_element=document.getElementById(destination_text);
		//return destination_element.selectionStart;
		
		return js_getCursorPositionBegin(destination_element);
	}

	function add(element){
		var selection_element=getSelection(element);
		var current_text="";
		for(var counter=0;counter<selection_element.length;counter++){
			current_text+=delimeter_begin+selection_element[counter]+delimeter_end;	
		}
		var text_element=document.getElementById(destination_text);
		var position=getPosition();
		text_element.value=insertIntoString(text_element.value,position,current_text);
		js_setCursorPosition(text_element,position+current_text.length,position+current_text.length);
		//text_element.selectionStart=position+current_text.length;
		//text_element.selectionEnd=position+current_text.length;
	}
	
	function add_comission(element){
		var selection_element=getSelection(element);
		var current_text="";
		current_text = delimeter_begin+element_text[element.selectedIndex]+delimeter_end;

		var com1 = document.getElementById("comission_value1");
		var com2 = document.getElementById("comission_value2");
		if (com1.checked) {
			current_text = current_text+"."+delimeter_begin+"<%=Bean.getMeaningFoCodeValue("COMISSION_VALUE","PERCENT_VALUE") %>"+delimeter_end;
		}
		if (com2.checked) {
			current_text = current_text+"."+delimeter_begin+"<%=Bean.getMeaningFoCodeValue("COMISSION_VALUE","FIXED_VALUE") %>"+delimeter_end;
		}
		var text_element=document.getElementById(destination_text);
		var position=getPosition();
		
		text_element.value=insertIntoString(text_element.value,position,current_text);
		js_setCursorPosition(text_element,position+current_text.length,position+current_text.length);
		//text_element.selectionStart=position+current_text.length;
		//text_element.selectionEnd=position+current_text.length;
	}

	var function_round_prefix="(";
	var function_round_suffix=")";
	  
	function add_roundint(element){
		var selection_element=getSelection(element);
		var current_text="";
		for(var counter=0;counter<selection_element.length;counter++){
			current_text+=delimeter_begin+selection_element[counter]+delimeter_end;	
		}
		var round1 = document.getElementById("rounding_rule1");
		var round2 = document.getElementById("rounding_rule2");
		var round3 = document.getElementById("rounding_rule3");
		if (round1.checked) {
			function_round_prefix = "\"<%=Bean.getMeaningFoCodeValue("ROUNDING_RULE", "NEAREST") %>\"(";
		}
		if (round2.checked) {
			function_round_prefix = "\"<%=Bean.getMeaningFoCodeValue("ROUNDING_RULE", "UP") %>\"(";
		}
		if (round3.checked) {
			function_round_prefix = "\"<%=Bean.getMeaningFoCodeValue("ROUNDING_RULE", "DOWN") %>\"(";
		}
		var text_element=document.getElementById(destination_text);
		var position_begin=js_getCursorPositionBegin(text_element);
		var position_end=js_getCursorPositionEnd(text_element)+1;

		text_element.value=insertIntoString(text_element.value,position_end,function_round_suffix);
		text_element.value=insertIntoString(text_element.value,position_begin,function_round_prefix);
		js_setCursorPosition(text_element,position+current_text.length,position+current_text.length);

	}

	function add_document(element){
		var selection_element=getSelection(element);
		var current_text="";

		var doc_number = document.getElementById("doc_number");
		var doc_date = document.getElementById("doc_date");
		var doc_name = document.getElementById("doc_name");

		current_text = current_text+delimeter_begin;
		
		current_text = current_text+element[element.selectedIndex].text+".";

		if (doc_number.checked) {
			current_text = current_text+"<%=Bean.calculatorXML.getfieldTransl("t_doc_number", false)%>";
		}
		if (doc_date.checked) {
			current_text = current_text+"<%=Bean.calculatorXML.getfieldTransl("t_doc_date", false)%>";
		}
		if (doc_name.checked) {
			current_text = current_text+"<%=Bean.calculatorXML.getfieldTransl("t_doc_name", false)%>";
		}

		current_text = current_text+delimeter_end;
		
		var text_element=document.getElementById(destination_text);
		var position=getPosition();
		
		text_element.value=insertIntoString(text_element.value,position,current_text);
		js_setCursorPosition(text_element,position+current_text.length,position+current_text.length);
	}

	function sub(){
		var position=getPosition();
		var text_element=document.getElementById(destination_text);
		var current_text=text_element.value;
		var position_before=current_text.substring(0,position).lastIndexOf(delimeter_begin);
		var position_after=current_text.substring(position,current_text.length).indexOf(delimeter_end);
		
		if((position_before>=0) && (position_after>=0)){
			text_element.value=removeFromString(text_element.value,position_before+1-delimeter_begin.length,position+position_after+delimeter_end.length);
		}
		js_setCursorPosition(text_element,position_before+1-delimeter_begin.length,position_before+1-delimeter_begin.length);
		//text_element.selectionStart=position_before+1-delimeter_begin.length
		//text_element.selectionEnd=position_before+1-delimeter_begin.length
	}

	function remove_char(){
		var text_element=document.getElementById(destination_text);
		var position=getPosition();
		if(position>0){
			text_element.value=removeFromString(text_element.value,position-1,position);
			//text_element.selectionStart=position-1;
			//text_element.selectionEnd=position-1;
			js_setCursorPosition(text_element,position-1,position-1);
		}
	}
	function add_char(value){
		var text_element=document.getElementById(destination_text);
		var position=getPosition();
		text_element.value=insertIntoString(text_element.value,position,value);
		//text_element.selectionStart=position+1;
		//text_element.selectionEnd=position+1;
		js_setCursorPosition(text_element,position+1,position+1);
	}

	function clear_text(){
		var text_element=document.getElementById(destination_text);
		text_element.value="";
		var el = document.getElementById("check_result");
        el.innerHTML =  "&nbsp;";
	}

	function insertIntoString(source_text,position,inner_text){
		var source=new String(source_text);
		var return_value="";
		if(source.length>0){
			return_value=source.substring(0,position)+inner_text+source.substring(position,source.length);
		}else{
			return_value=inner_text;
		}
		return return_value;
	}

	function removeFromString(source, index_begin, index_end){
		var return_value="";
		if(source.length==0){
			
		}else{
			if(index_end>source.length){
				if(index_begin>0){
					return_value=source.substring(0,index_begin);
				}else{
					return_value=source;
				}
			}else{
				if(index_begin>=0){
					return_value=source.substring(0,index_begin)+source.substring(index_end,source.length);
				}else{
					return_value="";
				}
			}
		}
		return return_value;
	}
	
	function getSelection(selection){
		var selection_array=new Array();
		for(var counter=0;counter<selection.childNodes.length;counter++){
			if(selection.childNodes.item(counter).selected){
				//toConsole(selection.childNodes.item(counter).text);
				selection_array[selection_array.length]=selection.childNodes.item(counter).text;
			}
		}
		return selection_array;
	}

		

</script>


	<script>
	var respFormula = "";

	var confirmed = 0;

	
	function changeOpener(){
		if (confirmed == 0) {
			window.alert('<%=Bean.calculatorXML.getfieldTransl("t_not_checked", false) %>');
			return false;
		}
		window.opener.document.getElementById('<%=field%>').value = respFormula;
		window.opener.document.getElementById('<%=field%>').className = "inputfield_modified";
		window.close();
	}
	
	</script>

	<br>
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><font size="2"><b><%=Bean.calculatorXML.getfieldTransl("variables", false) %></b></font><br>
			<%= Bean.getCalculatorVariablesOptions(field, cd_bk_operation_type) %>
			</td>
			<td valign="middle" align="center">
				<input type="button" value="<%=Bean.calculatorXML.getfieldTransl("add", false) %>" onclick="add(document.getElementById('source_numbers'))" class="inputfield2"><br>
				<input type="button" value="<%=Bean.calculatorXML.getfieldTransl("delete", false) %>" onclick="sub()" class="inputfield2">
			</td>
			<td rowspan="3" valign="top" style="border-style: double; border: 2px;">
				<center>
					<table <%=Bean.getTableDetailParam() %> style="border: 0px;">
						<tr>
							<td><input type="button" value="(" onclick="add_char('(')" class="inputfield2"></td>
							<td><input type="button" value=")" onclick="add_char(')')" class="inputfield2"></td>
							<td><input type="button" value="+" onclick="add_char('+')" class="inputfield2"></td>
							<td><input type="button" value="-" onclick="add_char('-')" class="inputfield2"></td>
							<td><input type="button" value="*" onclick="add_char('*')" class="inputfield2"></td>
							<td><input type="button" value="/" onclick="add_char('/')" class="inputfield2"></td>
							<td width="5"></td>
							<td><input type="button" value="<%=Bean.calculatorXML.getfieldTransl("backspace", false) %>" onclick="remove_char()" class="inputfield2"></td>
							<td width="5"></td>
							<td><input type="button" value="<%=Bean.calculatorXML.getfieldTransl("space", false) %>" onclick="add_char(' ')" class="inputfield2"></td>
							
						</tr>
					</table>
				</center>
				<table <%=Bean.getTableDetailParam() %> style="border: 0px;">
					<tr>
						<td rowspan="4" valign=top><textarea cols=40 rows=12 id="destination_text" class="inputfield2"><%=formula %></textarea>
						<br>
						<% if ("Y".equalsIgnoreCase(need_operation)) { %>
						<input type="button" value="<%=Bean.calculatorXML.getfieldTransl("formula", false) %>" onclick="add_char(delimeter_begin+'#<%=Bean.calculatorXML.getfieldTransl("formula_begin", false) %>#'+delimeter_end+' '+delimeter_begin+'#<%=Bean.calculatorXML.getfieldTransl("formula_end", false) %>#'+delimeter_end)" class="inputfield2"></td>
						<% } %>
						<td valign=top>
							<table <%=Bean.getTableDetailParam() %>>
								<tr>
									<td><input type="button" value="1" onclick="add_char('1')" class="inputfield2"></td>
									<td><input type="button" value="2" onclick="add_char('2')" class="inputfield2"></td>
									<td><input type="button" value="3" onclick="add_char('3')" class="inputfield2"></td>
								</tr>
								<tr>
									<td><input type="button" value="4" onclick="add_char('4')" class="inputfield2"></td>
									<td><input type="button" value="5" onclick="add_char('5')" class="inputfield2"></td>
									<td><input type="button" value="6" onclick="add_char('6')" class="inputfield2"></td>
								</tr>
								<tr>
									<td><input type="button" value="7" onclick="add_char('7')" class="inputfield2"></td>
									<td><input type="button" value="8" onclick="add_char('8')" class="inputfield2"></td>
									<td><input type="button" value="9" onclick="add_char('9')" class="inputfield2"></td>
								</tr>
								<tr>
									<td><input type="button" value="0" onclick="add_char('0')" class="inputfield2"></td>
									<td><input type="button" value="." onclick="add_char('.')" class="inputfield2"></td>
								</tr>
								
							</table>
						</td>
					</tr>
					<tr>
						<td valign=top>
							<table <%=Bean.getTableDetailParam() %>>
								<tr>
									<td>
										<input type="radio" name="rounding_rule" id="rounding_rule1" value="NEAREST" checked><label class="calculator_label" for="rounding_rule1"><%=Bean.getMeaningFoCodeValue("ROUNDING_RULE", "NEAREST") %></label><br>
										<input type="radio" name="rounding_rule" id="rounding_rule2" value="UP"><label class="calculator_label" for="rounding_rule2"><%=Bean.getMeaningFoCodeValue("ROUNDING_RULE", "UP") %></label><br>
										<input type="radio" name="rounding_rule" id="rounding_rule3" value="DOWN"><label class="calculator_label" for="rounding_rule3"><%=Bean.getMeaningFoCodeValue("ROUNDING_RULE", "DOWN") %></label><br>
										<input type="button" value="<%=Bean.calculatorXML.getfieldTransl("add", false) %>" onclick="add_roundint(document.getElementById('source_elements'))" class="inputfield2"><br>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td  colspan="2" width="100%" align="left" valign="top">
							<input type="button" value="<%=Bean.calculatorXML.getfieldTransl("clear", false) %>" onclick="clear_text()" class="inputfield2">
							<input type="button" value="<%=Bean.calculatorXML.getfieldTransl("check", false) %>" onclick="dwr_check_calc_expression(); return false;"class="inputfield2">
						</td>
					</tr>
				</table>				
			</td>

		</tr>
		<tr>
			<td><font size="2"><b><%=Bean.taxXML.getfieldTransl("general", false) %></b></font><br>
			<select id="taxes" class="inputfield2" size="3">
				<%= Bean.getTaxOptions("", false) %>
			</select>
			</td>
			<td valign="middle" align="center">
				<input type="button" value="<%=Bean.calculatorXML.getfieldTransl("add", false) %>" onclick="add(document.getElementById('taxes'))" class="inputfield2"><br>
				<input type="button" value="<%=Bean.calculatorXML.getfieldTransl("delete", false) %>" onclick="sub()" class="inputfield2">
			</td>
		</tr>
		<tr>
			<td><font size="2"><b><%=Bean.calculatorXML.getfieldTransl("comissions", false) %></b></font><br>
			<select id="source_elements" class="inputfield2" size="5" onclick="setTitle(this)">
				<%= Bean.getComissionTypeForCalculatorOptions("", "", false) %>
			</select><br>
			<input type="radio" name="comission_value" id="comission_value1" value="PERCENT_VALUE" checked><label class="calculator_label" for="comission_value1"><%=Bean.calculatorXML.getfieldTransl("t_percent_value", false) %></label>
			<input type="radio" name="comission_value" id="comission_value2" value="FIXED_VALUE"><label class="calculator_label" for="comission_value2"><%=Bean.calculatorXML.getfieldTransl("t_fixed_value", false) %></label>
			</td>
			<td valign="middle" align="center">
				<input type="button" value="<%=Bean.calculatorXML.getfieldTransl("add", false) %>" onclick="add_comission(document.getElementById('source_elements'))" class="inputfield2"><br>
				<input type="button" value="<%=Bean.calculatorXML.getfieldTransl("delete", false) %>" onclick="sub()" class="inputfield2">
			</td>
		</tr>
		<tr>
			<% if ("Y".equalsIgnoreCase(need_operation)) { %>
			<td><font size="2"><b><%=Bean.calculatorXML.getfieldTransl("t_docs", false) %></b></font><br>
			<select multiple="multiple" id="contract_data" class="inputfield2" size="7">
				<%=Bean.getDocTypeOptions("", false) %>
			</select><br>
			<input type="radio" name="doc_parameter" id="doc_number" value="DOC_NUMBER" checked><label class="calculator_label" for="doc_number"><%=Bean.calculatorXML.getfieldTransl("t_doc_number", false) %></label>
			<input type="radio" name="doc_parameter" id="doc_date" value="DOC_DATE"><label class="calculator_label" for="doc_date"><%=Bean.calculatorXML.getfieldTransl("t_doc_date", false) %></label>
			<input type="radio" name="doc_parameter" id="doc_name" value="DOC_NAME"><label class="calculator_label" for="doc_name"><%=Bean.calculatorXML.getfieldTransl("t_doc_name", false) %></label>
			</td>
			<td valign="middle" align="center">
				<input type="button" value="<%=Bean.calculatorXML.getfieldTransl("add", false) %>" onclick="add_document(document.getElementById('contract_data'))" class="inputfield2"><br>
				<input type="button" value="<%=Bean.calculatorXML.getfieldTransl("delete", false) %>" onclick="sub()" class="inputfield2">
			</td>
			<% } else { %>
			<td colspan="2">&nbsp;</td>
			<% } %>
						<td  colspan="2" align="left" valign="top">
							<div id="check_result" class="div_check_result">&nbsp;
							</div>
						</td>
		</tr>
		<tr>
			<td colspan="3" align="center">
				<input type="button" value="<%=Bean.calculatorXML.getfieldTransl("submit", false) %>" onclick="changeOpener();" class="inputfield2">
				<input type="button" value="<%=Bean.calculatorXML.getfieldTransl("cancel", false) %>" onclick="window.close();" class="inputfield2">
			</td>
		</tr>
	</table>
	<span id="console">
	</span>

</body>

</html>