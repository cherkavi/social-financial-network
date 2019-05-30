		function validateType(tapeName,str)
		{
			switch (tapeName) 
			{
				// case 'number': {return(/^-?\d+[\.|\,]?\d+$/.test(str))}; break; // edited by emix
				case 'number': {return(/^-?\d+[\.|\,]?\d*$/.test(str))}; break;
				case 'integer': {if (!/[^0-9]/.test(str)) return true}; break;
				case 'char': {if (str.length<2) return true}; break;
				case 'varchar2': {return (/./.test(str))}; break;
				case 'varchar': {if (!/(\r\n|\r|\n)/.test(str)) return true}; break;
				case 'date': {return (/^\d{1,2}([-. /])\d{1,2}\1\d{2,4}$/.test(str))}; break;
				case 'email': {return (/[0-9a-z_]+@[0-9a-z_^.]+.[a-z]{2,3}/i.test(str))}; break;
				case 'card': {var card = str.replace(/ /g,''); if (!/[^0-9]/.test(card)) {if (card.length==13) return true}}; break;
				case 'oper_sum': {if (/^-?\d+[\.|\,]?\d*$/.test(str)){ var strInt=str.replace(',','.');  var fr=parseFloat(strInt); return((strInt==fr.toFixed(2) || strInt==fr.toFixed(1) || strInt==fr.toFixed(0)) && (fr>0)); } }; break;
				case 'oper_sum_zero': {if (/^-?\d+[\.|\,]?\d*$/.test(str)){ var strInt=str.replace(',','.');  var fr=parseFloat(strInt); return((strInt==fr.toFixed(2) || strInt==fr.toFixed(1) || strInt==fr.toFixed(0)) && (fr>=0)); } }; break;
				default: ;
			}
		}

		function validateForm(arg)
		{
			alert(arg.length);
		}

		function validateForm(arg)
		{
			return validateFormForID(arg, null);
		}

		function validateFormForID(arg, formId)
		{
			requaredMsg='Вы не заполнили все обязательные поля!';
			typeMsg='Некоректно заполненное поле!';
			cardMsg='Некоректный номер карты!';
			var flagFocus=false;
			var flagReturn=true;
			var obj = null;
			if (formId == null || formId == '') {
				obj=document.forms[0];
			} else {
				obj=document.getElementById(formId);
			}
			var undefTmp = null;
			for (var i=0;i<arg.length;i++) 
			{ 
				eval('var tmp=obj.'+arg[i][0]);
				//alert('tmp['+i+'].name='+tmp.name+', tmp['+i+'].type='+tmp.type);
				var chbChecked = true;
				
				if (tmp.type== null || tmp.type=='undefined') {
					chbChecked = false;
					undefTmp = tmp[0];
					for( j = 0; j < tmp.length; j++ ) {
						//alert('tmp['+j+'].id='+tmp[j].id+', tmp['+j+'].checked='+tmp[j].checked);
						if (tmp[j].checked) {
							chbChecked = true;
						}
					}
					if (!chbChecked) {
						undefTmp.className='inputfield focus'; if (!flagFocus) {alert(requaredMsg); undefTmp.focus();flagFocus=true}; if (flagReturn) flagReturn=false
					}
				//}
				//alert('tmp.type='+tmp.type);
				} else if (tmp.type=='radio') {
					if (!tmp.checked) {
						tmp.className='inputfield focus'; if (!flagFocus) {alert(requaredMsg); tmp.focus();flagFocus=true}; if (flagReturn) flagReturn=false
					}
				} else {
					if (arg[i][2]) if (!tmp.value) 
					{
						tmp.className='inputfield focus'; if (!flagFocus) {alert(requaredMsg); tmp.focus();flagFocus=true}; if (flagReturn) flagReturn=false
					} 	else if (tmp.className=='inputfield focus') tmp.className='valid';
					if (tmp.value) 
					{
						if (!validateType(arg[i][1],tmp.value)) 
						{
							tmp.className='inputfield incorrect'; if (!flagFocus) {alert((arg[i][1]=='card')?cardMsg:typeMsg); tmp.focus();flagFocus=true}; if (flagReturn) flagReturn=false
						}	else if (tmp.className=='inputfield incorrect') tmp.className='valid';
					}   
				}
				//alert('flagReturn='+flagReturn);
			} 
			//window.alert(tmp.className);
			if (flagReturn) {
				if (!validateCurrentClub()) {
					flagReturn = false;
				}
			}
			return flagReturn;
		}