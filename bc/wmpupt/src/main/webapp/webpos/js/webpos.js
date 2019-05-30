function ajaxpage3(url, containerid){
			/*
			<% Bean.goBack(); %>
			alert('goBack(1)');
			<% Bean.goBack(); %>
			alert('goBack(2)');
			*/
			return ajaxpage(url, containerid);
		}

/**
@param textElement - имя текстового элемента, который содержит текст для отправки
@param url - адрес, куда нужно отправлять 
@param parameterName - имя параметра, который нужно отправлять 
*/
function sendText2(event, textElement,url,parameterName,div_name){
 while(true){
  if(event!=null){
   try{
    if(event.keyCode==13){
    }else{
     break;
    }
   }catch(err){
    break;
   }; 
  }
  //var textForSend=escape(document.getElementById(textElement).value);
  //var textForSend=Utf8.encode(document.getElementById(textElement).value);
  var textForSend=document.getElementById(textElement).value;
  //var textForSend=URLEncoder.encode(document.getElementById(textElement).value);
  //var textForSend=encode_utf8(document.getElementById(textElement).value);
  var urlString=new String(url);
  
  var position=urlString.indexOf("?");
  var lastIndex=urlString.lastIndexOf("&");
  var urlString;
  if(position>0){
   //alert(urlString.length+"   "+lastIndex);
   if((urlString.length-1)==lastIndex){
    urlString=url+parameterName+"="+textForSend;
   }else{
    urlString=url+"&"+parameterName+"="+textForSend;
   } 
  }else{
   urlString=url+"?"+parameterName+"="+textForSend;
  }
  //alert(encodeURI(urlString));
  //window.location=encodeURI(urlString);
  ajaxpage(encodeURI(urlString), div_name);

  break;
 }
}
function sendText(event, textElement,url,parameterName){
	return sendText2(event, textElement,url,parameterName,'div_main');
	}

AjaxUtility=function(){};
// назначение функции для прослушивания нажатия кнопки
AjaxUtility.TextField=function(textFieldId, buttonId,url, parameterName){
 var buttonElement=document.getElementById(buttonId);
 buttonElement.setAttribute("onclick", "sendText(null, '"+textFieldId+"','"+url+"','"+parameterName+"');");
 var textfieldElement=document.getElementById(textFieldId);
 textfieldElement.setAttribute("onkeydown", "sendText(event, '"+textFieldId+"','"+url+"','"+parameterName+"');");
 //alert(element.onclick);
}


AjaxUtility.TextField2=function(textFieldId, buttonId,url, parameterName,div_name){
 var buttonElement=document.getElementById(buttonId);
 buttonElement.setAttribute("onclick", "sendText2(null, '"+textFieldId+"','"+url+"','"+parameterName+"','"+div_name+"');");
 var textfieldElement=document.getElementById(textFieldId);
 textfieldElement.setAttribute("onkeydown", "sendText2(event, '"+textFieldId+"','"+url+"','"+parameterName+"','"+div_name+"');");
 //alert(element.onclick);
}

	 function replaceForUrl(value){
	  	 var returnValue = value;
	  	 returnValue = replaceAllString(returnValue, "#","%23");
	  	 returnValue = replaceAllString(returnValue, "+","%2B");
	     returnValue = replaceAllString(returnValue, "&","%26");
	  	
	   	 return returnValue;
	 }

	  	function replaceAllString(value, findString, replaceString){
	  	   	while(value.indexOf(findString)>=0){
	  	    	value=value.replace(findString, replaceString);
	  	   	}
	  	   	return value;
	  	}

	    function getMessageLength(source, destination) {
	        var dest = document.getElementById(destination);
	        dest.value = source.value.length;
	        //window.alert('getMessageLength');
	    }
	  	  
	  	function mySubmitForm(pFormName) {
	  		return mySubmitForm(pFormName, true);
	  	}
	  	  
	  	function mySubmitForm(pFormName, pDisableButton) {
			var result_str = "";

			//window.alert(pFormName);
			var formElement=document.getElementById(pFormName);
			
			if (pDisableButton) {
				var elems = formElement.getElementsByTagName('button');
				for(var i=0; i < elems.length; i++) {
					if (elems[i].className == 'button') {
						elems[i].className = 'button_disable';
						elems[i].disabled=true;
					}
				}
			}
			
			for (var i = 0; i < formElement.length; i++) {
				myName = formElement[i].name;
				myValue = formElement[i].value; 
				if (formElement[i].type=="checkbox") {
					myCheckBox = formElement[i];
					if (!myCheckBox.checked) {
						myName = "";
						myValue = "";
					} else {
						myValue = "Y";
					}
				}
				if (formElement[i].type=="radio") {
					myCheckBox = formElement[i];
					if (!myCheckBox.checked) {
						myName = "";
						myValue = "";
					}
				}
				if (!(myName==null || myName=="")) {
					result_str = result_str + myName + "=" + replaceForUrl(encodeURI(myValue)) + "&";
				}
	  		}

	  		//window.alert(result_str);
	  		return result_str;
	  	}

	  	 function getSubStringCondition(value){
	  	    if(value.indexOf('OPERATION_COMPLETE_SUCCESSFUL')>=0){
	  	     var beginIndex=value.indexOf("ajaxpage(");
	  	     if(beginIndex>=0){
	  	      var tempValue=value.substring(beginIndex+10);
	  	      var endIndexSingle=tempValue.indexOf("'");
	  	      var endIndexDouble=tempValue.indexOf('"');
	  	      if((endIndexSingle>0)&&(endIndexDouble>0)){
	  	       if(endIndexSingle<endIndexDouble){
	  	        return tempValue.substring(0,endIndexSingle);
	  	       }else{
	  	        return tempValue.substring(0,endIndexDouble);
	  	       }
	  	      }else{
	  	       if(endIndexSingle>0){
	  	        return tempValue.substring(0,endIndexSingle);
	  	       }else if(endIndexDouble>0){
	  	        return tempValue.substring(0,endIndexDouble);
	  	       }else{
	  	        // incorrect answer from server
	  	        return "";
	  	       }
	  	      }
	  	     }else{
	  	      // incorrect answer from server
	  	      return "";
	  	     }
	  	    } else if(value.indexOf('OPERATION_COMPLETE_ERROR')>=0){
	  	  	    //window.alert(value);
	  	  	    return value;
	  	    } else {
	  	     // answer from server is not Positive
	  	     return "";
	  	    };
	  	    return "";
	  	   }
		
		var post_form_div = "div_action_big";
		function start_load(){
			document.getElementById('imgWait').style.visibility = "visible";
		}
		function done_load(response){
			document.getElementById('imgWait').style.visibility = "hidden";
		 	document.getElementById(post_form_div).innerHTML= response;
		 	try{
		 		onAjaxDone();
		 	}catch(e){
		 		console.log('onAjaxDone function execution error: '+e.message);
		 	}
		 	/*if (!res=='') {
			 	if(response.indexOf('OPERATION_COMPLETE_ERROR')>=0){
				 	document.getElementById(post_form_div).innerHTML= response;
			 	}
		 	} else {
		 		ajaxpage(res, post_form_div);
			 	alert(res);
		 	}*/
		}
		

		function string_replace(source_string, find, sub) {
		    return source_string.split(find).join(sub);
		}

		/**
		@param [0] path_to_server - п�_�'�_ к �_���_�_���_�_
		@param [1] form_id -  
		*/
		function post_form(){
			  var argumentsLength=post_form.arguments.length;
              //alert("argumentsLength="+argumentsLength);
			  var form_id=post_form.arguments[1];
			  var destinationFormElement=document.getElementById(form_id);//document.createElement("FORM");
			  destinationFormElement.setAttribute("enctype","multipart/form-data");
			  var path_to_server=post_form.arguments[0];
              if (argumentsLength>2) {
			  	post_form_div=post_form.arguments[2];
			  }
			  destinationFormElement.setAttribute("action", path_to_server);
			  destinationFormElement.setAttribute("method","post");
			  //destinationFormElement.setAttribute("style","display:none");
			  

			  //destinationFormElement.setAttribute("onsubmit","return AIM.submit(this, {'"+start_load_function+"' : startCallback, '"+done_load_function+"' : completeCallback})");
			  //destinationFormElement.onsubmit="return AIM.submit(this, {'"+start_load_function+"' : startCallback, '"+done_load_function+"' : completeCallback})";
			  /*
			     for(var index=1;index<argumentsLength;index++){
			      	var form_id=post_form.arguments[index];
			   		var sourceFormElement=document.getElementById(form_id);
			   		findAndAppendInputChild(destinationFormElement, sourceFormElement);
			     }
			     document.body.appendChild(destinationFormElement); 
				*/
				  //destinationFormElement.submit();
			  AIM.submit(destinationFormElement, {'onStart' : start_load, 'onComplete': done_load });
			  //AIM.submit(destinationFormElement);
			  //var nd = destinationFormElement.name_doc.value;
			  //alert('First: ' + nd);
			  //nd = string_replace(nd, "\n","G");
			  //alert('Second: ' + nd);
			//var formElement=destinationFormElement.getElementById(pFormName);
			for (var i = 0; i < destinationFormElement.length; i++) {
				if (destinationFormElement[i].type=="textarea") {
					destinationFormElement[i].value = string_replace(destinationFormElement[i].value, "\n","_NR_");
				}
	  		}
			  destinationFormElement.submit();
			  //destinationFormElement.name_doc.value = string_replace(destinationFormElement.name_doc.value, "_NR_", "\n");
			  //alert(destinationFormElement.name_doc.value);
	/*		  for(var childItem in destinationFormElement.childNodes){
				  destinationFormElement.removeChild(destinationFormElement.childNodes[childItem]);
				  //findAndAppendInputChild(destinationFormElement, element.childNodes[childItem]);
			  }
			  document.body.removeChild(destinationFormElement);
	*/
			  return false;
			 }

			 /** 
			 @param destinationFormElement
			 @param 
			 */
			 function findAndAppendInputChild(destinationFormElement, element){
			  try{
			   if(element.hasChildNodes()){
			    for(var childItem in element.childNodes){
			     findAndAppendInputChild(destinationFormElement, element.childNodes[childItem]);
			    }
			   }else{
			    try{
			     if(element.tagName.toUpperCase()=='INPUT'){
			      destinationFormElement.appendChild(element);
			      return;
			     }
			    }catch(err){
			     return ;
			    }
			   }
			  }catch(err_outer){
			   return; 
			  }
			 }

			 function validateCurrentClub() {
				 var currentClub = '<%=Bean.getCurrentClubID()%>';
				 if (currentClub=='' || currentClub=='null' || currentClub=='NULL') {
					 currentClub = '';
				 }
				 try {
				 	var formClub = document.getElementById("id_club").value;
				 	if (formClub=='' || formClub=='null' || formClub=='NULL') {
				 		formClub='';
				 	}
				 	//alert("currentClub="+currentClub+", formClub="+formClub);
				 	if (currentClub!='') {
					 	if (currentClub!=formClub) {
					 		var msg='<%=Bean.commonXML.getfieldTransl(Bean.getLanguage(),"h_change_club_confirm", false)%>';
	    					return window.confirm(msg);
					 	}
				 	}
				 } catch(err){
					 return true;
				 }
				 
				 return true;
			 }
			 
			 var currentColor = '';
			 var mouseOverColor = '#F7F7F7';
			 var mouseOutColor = '#FFFFFF';
			 function colored(theCells,theColor){
			 var rowCellsCnt = theCells.length;
			 for (var c = 0; c < rowCellsCnt; c++) {
			 theCells[c].style.backgroundColor = theColor;
			 }
			 }
			 function coloredOver(theRow,stopColor) {
			 var theCells = null;
			 if (typeof(document.getElementsByTagName) != 'undefined') {theCells = theRow.getElementsByTagName('td');}
			 else if (typeof(theRow.cells) != 'undefined') {theCells = theRow.cells;}
			 else {return false;}
			 if (stopColor && theRow.ismarked=="1"){return;}
			 currentColor = theCells[1].style.backgroundColor;
			 colored(theCells,mouseOverColor);
			 }
			 function coloredOut(theRow,stopColor) {
			 var theCells = null;
			 if (typeof(document.getElementsByTagName) != 'undefined') {theCells = theRow.getElementsByTagName('td');}
			 else if (typeof(theRow.cells) != 'undefined') {theCells = theRow.cells;}
			 else {return false;}
			 if (stopColor && theRow.ismarked=="1"){return;}
			 colored(theCells,currentColor);
			 } 
				
	function disable_element(element_name){
		element = document.getElementById(element_name);
		element.value = '';
		element.className = 'inputfield-ro';
		element.readOnly = 1;
	}
	function enable_element(element_name){
		element = document.getElementById(element_name);
		element.className = 'inputfield';
		element.readOnly = 0;
	}
	

	
	function checkPayType(checkBox){
		var bank_trn = document.getElementById('bank_trn');
		if (checkBox.id == 'pay_type_BANK_CARD') {
			bank_trn.className = 'inputfield';
			bank_trn.readOnly = 0;
		} else {
			bank_trn.value = '';
			bank_trn.className = 'inputfield-ro';
			bank_trn.readOnly = 1;
		}

		try {
			var entered_sum = document.getElementById('entered_sum');
			var sum_change = document.getElementById('sum_change');
			var change_to_share_account = document.getElementById('change_to_share_account');
			if (checkBox.id == 'pay_type_CASH') {
				entered_sum.className = 'inputfield';
				entered_sum.readOnly = 0;
				change_to_share_account.disabled = "";
			} else {
				entered_sum.value = '';
				entered_sum.className = 'inputfield-ro';
				entered_sum.readOnly = 1;
				sum_change.value = '';
				change_to_share_account.disabled = "disabled";
				change_to_share_account.checked = false;
			}
		} catch(err) {}
		return true;
	}
	
	function calcChangeExtend(payElemId){
		
		var elemName = "";
		if (payElemId=='none') {
			elemName = "pay_value";
		} else {
			elemName = payElemId;
		}
		var pay_value = document.getElementById(elemName);
		var entered_sum = document.getElementById('entered_sum');
		var sum_change = document.getElementById('sum_change');
		var change_to_share_account = document.getElementById('change_to_share_account');
		var change_calc_error = document.getElementById('change_calc_error');
		//var operation_margin = document.getElementById('operation_margin');
		try {
			//alert("entered_sum.value='"+entered_sum.value+"', pay_value.value='"+pay_value.value+"'");
			if (!(entered_sum.value == null || entered_sum.value == "") && !(pay_value.value == null || pay_value.value == "")) {
				var sum1 = entered_sum.value.replace(",",".");
				var sum2 = pay_value.value.replace(",",".");
				var amount = +sum1 - +sum2;
				//var entered_sum_prepared = +sum1.toFixed(2);
				entered_sum.value = (0 + +sum1).toFixed(2).replace(".",",");
				if (isNaN(amount)) {
					change_calc_error.value = "Y";
					sum_change.value = "Ошибка";
					change_to_share_account.disabled = "disabled";
				} else { 
					if (amount > 0) {
						//sum_change.value =  Math.floor(sum_change.value * 100) / 100;
						sum_change.value = amount.toFixed(2).replace(".",",");
						change_calc_error.value = "N";
						change_to_share_account.disabled = "";
					} else if (amount == 0) {
						sum_change.value = amount.toFixed(2).replace(".",",");
						change_calc_error.value = "N";
						change_to_share_account.disabled = "disabled";
					} else {
						change_calc_error.value = "Y";
						change_to_share_account.disabled = "disabled";
						sum_change.value = "Ошибка";
					}
				}
			} else {
				change_calc_error.value = "N";
				sum_change.value = "";
			}
		} catch(err) { 
			alert(err);
			sum_change.value = "Ошибка";
			change_calc_error.value = "Y";
		}
		return true;
	}
	
	function calcChange(){
		return calcChangeExtend('none');
	}
	
	function validateChange() {
		var returnValue = true;
		try {
			var sum_change_element = document.getElementById('sum_change');
			var sum_change = 0 + +sum_change_element.value.replace(',','.');
			var change_calc_error = document.getElementById('change_calc_error');
			var change_to_share_account = document.getElementById('change_to_share_account');
			if (change_calc_error.value=="Y") {
				if (sum_change < 0) {
					alert("Сумма, внесенная пайщиком, не может быть меньше итоговой суммы");
				} else {
					alert("Ошибка вычисления суммы сдачи");
				}
				returnValue = false;
			} else {
				if (sum_change < 0) {
					alert("Сумма, внесенная пайщиком, не может быть меньше итоговой суммы");
					returnValue = false;
				} else if (sum_change ==0) {
				} else {
					//alert("Сумма сдачи валидна: change_to_share_account.value="+change_to_share_account.value+", change_to_share_account.checked="+change_to_share_account.checked);
					if (change_to_share_account.value!="U" && !change_to_share_account.checked) {
						if (confirm("Зачислить сдачу " + sum_change_element.value + " в паевой фонд?")) {
							change_to_share_account.value = 'Y';
							change_to_share_account.checked = "checked";
						}
					}
				}
			}
		}  catch(err) {alert("validateChange error: "+err);}
		return returnValue;
	}
		
	function showCheckCardButton(card){
		//checkBox = document.getElementById(elem);
		element = document.getElementById('check_card');
		if (card.value == '' || card.value == null) {
			element.className = 'img_check_card_inactive';
		} else {
			element.className = 'img_check_card';
		}
	}
	
	function changeCardPackPayValue(cardPackElement) {
		var returnValue = "";
		var found = false;
		try {
			for	(index = 0; index < cardPackageParam.length; index++) {
				var row = cardPackageParam[index];
				//alert('row[0]='+row[0]+", cardPackElement="+cardPackElement.value);
				if (row[0]==cardPackElement.value) {
					returnValue = row[1];
					found = true;
				}
			}
			if (found) {
				document.getElementById('pay_value').value=returnValue;
				calcChange();
			}
		}  catch(err) {}
		return true;
	}
	
	  function getCdCard1() {
		  var cdCard1 = "";
		  try {
			  cdCard1 = document.getElementById('cd_card1').value;
		  } catch (err) {}
		  return cdCard1;
	  }
	  
	  function calcPaymentTotalSum() {
			var pay_for_goods = 0;			
			var membership_fee = 0;
			var membership_fee_margin = 0;
			try {
				pay_for_goods_element = document.getElementById('pay_for_goods');
				pay_for_goods = pay_for_goods_element.value.replace(",",".");
			} catch(err) {}
			try {
				membership_fee_element = document.getElementById('membership_fee');
				membership_fee = membership_fee_element.value.replace(",",".");
			} catch(err) {}
			try {
				membership_fee_margin_element = document.getElementById('membership_fee_margin');
				membership_fee_margin = membership_fee_margin_element.value.replace(",",".");
			} catch(err) {}
			
			try {
				pay_value_element = document.getElementById('pay_value');
				totalSum = (+pay_for_goods + +membership_fee + +membership_fee_margin).toFixed(2);
				if (isNaN(totalSum)) {
					pay_value_element.value = 'Ошибка';
				} else {
					pay_value_element.value = totalSum.replace(".",",");
				}
			} catch(err) {
				//alert(err);
				return false;
			}
			return true;
		}

	  function calcInvoiceLineTotalSum() {
	  	var count = 0;			
	  	var price = 0;
	  	var tax_percent = 0;
	  	var total_amount = document.getElementById('total_amount');
	  	var tax_amount = document.getElementById('tax_amount');
	  	try {
	  		var count_element = document.getElementById('count_nomenkl').value;
	  		if (count_element == '') {
	  			count_element = "0,00";
	  		}
	  		count = count_element.replace(",",".");
	  	} catch(err) {}
	  	try {
	  		var price_element = document.getElementById('price_nomenkl').value;
	  		if (price_element== '') {
	  			price_element = "0,00";
	  		}
	  		price = price_element.replace(",",".");
	  	} catch(err) {}
	  	try {
	  		var tax_percent_element = document.getElementById('tax_percent').value;
	  		if (tax_percent_element == '') {
	  			tax_percent_element = "0,00";
	  		}
	  		tax_percent = tax_percent_element.replace(",",".");
	  	} catch(err) {}
	  	
	  	try {
	  		var totalSum = (+count * +price).toFixed(2);
	  		var totalTax = (+count * +price * +tax_percent / 100).toFixed(2);
	  		if (isNaN(totalSum)) {
	  			total_amount.value = 'Ошибка';
	  		} else {
	  			total_amount.value = totalSum.replace(".",",");
	  		}
	  		if (isNaN(totalTax)) {
	  			tax_amount.value = 'Ошибка';
	  		} else {
	  			tax_amount.value = totalTax.replace(".",",");
	  		}
	  	} catch(err) {
	  		//alert(err);
	  		return false;
	  	}
	  	return true;
	  }