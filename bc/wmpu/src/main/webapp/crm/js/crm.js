function ajaxpage3(url, containerid){
	return ajaxpage(url, containerid);
}

/**
@param textElement - имя текстового элемента, который содержит текст для отправки
@param url - адрес, куда нужно отправлять 
@param parameterName - имя параметра, который нужно отправлять 
*/
function sendText(event, textElement,url,parameterName){
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
  ajaxpage(encodeURI(urlString), 'div_main');

  break;
 }
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


imgslide = new Array() //предварительная загрузка изображений в страницу
	imgslide[0] =new Image()
	imgslide[1] =new Image()
	imgslide[2] =new Image()
	imgslide[0].src = "/wmpu/images/menu/papka.gif"
	imgslide[1].src = "/wmpu/images/menu/text.gif"
	imgslide[2].src = "/wmpu/images/menu/papkaop.gif"
	  	
	var currElement;
	function markElement(newElement){
		if (currElement) {
			currElement.style.backgroundColor = 'transparent'; 
			currElement.style.fontStyle = 'normal';
			currElement.style.fontWeight = 'normal';
			currElement.style.fontSize = '100%';
			currElement.style.border = '0px';
		}   	 
		 newElement.style.backgroundColor = '#EFEFEF';
		 newElement.style.fontWeight = 'bold';
	  	 newElement.style.fontStyle = 'italic';
		 newElement.style.fontSize = '100%';
		 newElement.style.border = '1px inset maroon';	 
	  	 currElement = newElement;
		 top.document.currBankElement = currElement;
	}

	function chhidElem (obj, img) //функция, открывающая и закрывающая пункты меню
	{
	  	if (obj.className == "colelem") {
			obj.className = "expelem";
			img.src = "/wmpu/images/menu/papka.gif"
	  	} else {
			obj.className = "colelem";
			img.src = "/wmpu/images/menu/papka.gif"
	  	}
	} 

	 function replaceForUrl(value){
	  	 var returnValue = value;
	  	 returnValue = replaceAllString(returnValue, "#","%23");
	  	 returnValue = replaceAllString(returnValue, "+","%2B");
	     returnValue = replaceAllString(returnValue, "&","%26");
	  	
	   	 return returnValue;
	 }

	function checkMaxRowNumber(hyperLink, rowCount, enteredRow){
		//alert ('checkMaxRowNumber: enteredRow=' + enteredRow + ', rowCount='+rowCount);
		if (rowCount != '0') {
  	  	if (enteredRow > rowCount) {
  	  	  	alert ('<%= Bean.commonXML.getfieldTransl(Bean.getLanguage(),"error_max_row_count", false) %> ' + rowCount);
  	  	  	return false;
  	  	}
		}
		ajaxpage(hyperLink, 'div_main');
	 }

  	function checkMaxRowNumberForDiv(hyperLink, rowCount, enteredRow, div_name){
  		//alert ('checkMaxRowNumber: enteredRow=' + enteredRow + ', rowCount='+rowCount);
  		if (rowCount != '0') {
	  	  	if (enteredRow > rowCount) {
	  	  	  	alert ('<%= Bean.commonXML.getfieldTransl(Bean.getLanguage(),"error_max_row_count", false) %> ' + rowCount);
	  	  	  	return false;
	  	  	}
  		}
  		ajaxpage(hyperLink, div_name);
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
	var result_str = "";

	//window.alert(pFormName);
	var formElement=document.getElementById(pFormName);
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

	 	var post_form_div = "div_main";
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
		 		//alert('onAjaxDone function execution error: '+e.message);
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

	/**
	@param path_to_server - п�_�'�_ к �_���_�_���_�_
	@param form_id -  
		 
	*/
	
	function string_replace(source_string, find, sub) {
	    return source_string.split(find).join(sub);
	}
	
	function post_form(){
		  var argumentsLength=post_form.arguments.length;
          //alert("arguments="+post_form.arguments);
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
	  //alert('Exit');
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
	 

		function show_change_row1(){
			try{
				if (document.getElementById('id_param_row1').style.display=='none') {
					document.getElementById('id_param_row1').style.display='';
				} else {
					document.getElementById('id_param_row1').style.display='none';
				}
			}catch(err){}
			if (document.getElementById('change_param_row1').style.display=='none') {
				document.getElementById('change_param_row1').style.display='';
				document.getElementById('change_param_row2').style.display='';
				document.getElementById('change_button').innerHTML = '<<';
			} else {
				document.getElementById('change_param_row1').style.display='none';
				document.getElementById('change_param_row2').style.display='none';
				document.getElementById('change_button').innerHTML = '>>';
			}
			if (document.getElementById('update_param_row1').style.display=='none') {
				document.getElementById('update_param_row1').style.display='';
				document.getElementById('update_param_row2').style.display='';
			} else {
				document.getElementById('update_param_row1').style.display='none';
				document.getElementById('update_param_row2').style.display='none';
			}
		}
		function show_change_row2(){
			try{
				if (document.getElementById('id_param_row1').style.display=='none') {
					document.getElementById('id_param_row1').style.display='';
				} else {
					document.getElementById('id_param_row1').style.display='none';
				}
			}catch(err){}
			if (document.getElementById('change_param_row').style.display=='none') {
				document.getElementById('change_param_row').style.display='';
				document.getElementById('change_button').innerHTML = '<<';
			} else {
				document.getElementById('change_param_row').style.display='none';
				document.getElementById('change_button').innerHTML = '>>';
			}
			if (document.getElementById('update_param_row').style.display=='none') {
				document.getElementById('update_param_row').style.display='';
			} else {
				document.getElementById('update_param_row').style.display='none';
			}
		}

		function show_addit_param(){
			if (document.getElementById('addit_row1').style.display=='none') {
				try{
					document.getElementById('addit_row1').style.display='';
				}catch(err){}
				try{
					document.getElementById('addit_row2').style.display='';
				}catch(err){}
				try{
					document.getElementById('addit_row3').style.display='';
				}catch(err){}
				try{
					document.getElementById('addit_row4').style.display='';
				}catch(err){}
				try{
					document.getElementById('addit_row5').style.display='';
				}catch(err){}
				try{
					document.getElementById('addit_row6').style.display='';
				}catch(err){}
				try{
					document.getElementById('addit_row7').style.display='';
				}catch(err){}
				try{
					document.getElementById('addit_row8').style.display='';
				}catch(err){}
				document.getElementById('addit_button').innerHTML = '<<';
			} else {
				try{
					document.getElementById('addit_row1').style.display='none';
				}catch(err){}
				try{
					document.getElementById('addit_row2').style.display='none';
				}catch(err){}
				try{
					document.getElementById('addit_row3').style.display='none';
				}catch(err){}
				try{
					document.getElementById('addit_row4').style.display='none';
				}catch(err){}
				try{
					document.getElementById('addit_row5').style.display='none';
				}catch(err){}
				try{
					document.getElementById('addit_row6').style.display='none';
				}catch(err){}
				try{
					document.getElementById('addit_row7').style.display='none';
				}catch(err){}
				try{
					document.getElementById('addit_row8').style.display='none';
				}catch(err){}
				document.getElementById('addit_button').innerHTML = '>>';
			}
		}

		function disable_jur(element_name){
			element = document.getElementById(element_name);
			element.value = '';
			element.className = 'inputfield-ro';
			element.readOnly = 1;
		}
		function enable_jur(element_name){
			element = document.getElementById(element_name);
			element.className = 'inputfield';
			element.readOnly = 0;
		}
