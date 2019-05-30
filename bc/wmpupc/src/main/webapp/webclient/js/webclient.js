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

		function start_load(){
			document.getElementById('imgWait').style.visibility = "visible";
		}
		function done_load(response){
			document.getElementById('imgWait').style.visibility = "hidden";
		 	var res = getSubStringCondition(response);
		 	if (!res=='') {
			 	if(response.indexOf('OPERATION_COMPLETE_ERROR')>=0){
				 	document.getElementById('div_main').innerHTML= response;
			 	} else {
			 		ajaxpage(res, 'div_main');
			 	}
		 	}
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
			  var form_id=post_form.arguments[1];
			  var destinationFormElement=document.getElementById(form_id);//document.createElement("FORM");
			  destinationFormElement.setAttribute("enctype","multipart/form-data");
			  var path_to_server=post_form.arguments[0];
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