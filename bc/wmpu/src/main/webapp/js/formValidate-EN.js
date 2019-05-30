function validateType(tapeName,str)
		{
			switch (tapeName) 
			{
				// case 'number': {return(/^-?\d+[\.|\,]?\d+$/.test(str))}; break; // editrd by emix
				case 'number': {return(/^-?\d+[\.|\,]?\d*$/.test(str))}; break;
				case 'integer': {if (!/[^0-9]/.test(str)) return true}; break;
				case 'char': {if (str.length<2) return true}; break;
				case 'varchar2': {return (/./.test(str))}; break;
				case 'varchar': {if (!/(\r\n|\r|\n)/.test(str)) return true}; break;
				case 'date': {return (/^\d{1,2}([-. /])\d{1,2}\1\d{2,4}$/.test(str))}; break;
				case 'email': {return (/[0-9a-z_]+@[0-9a-z_^.]+.[a-z]{2,3}/i.test(str))}; break;
				default: ;
			}
		}

		function validateForm(arg)
		{
			requaredMsg='Fill in all obvious fields!';
			typeMsg='Field is not filled in correctly!';
			var flagFocus=false;
			var flagReturn=true;
			obj=document.forms[0];
			for (var i=0;i<arg.length;i++) 
			{ 
				eval('var tmp=obj.'+arg[i][0]);
				if (arg[i][2]) if (!tmp.value) 
				{
					tmp.className='focus'; if (!flagFocus) {alert(requaredMsg); tmp.focus();flagFocus=true}; if (flagReturn) flagReturn=false
				} 	else if (tmp.className=='focus') tmp.className='valid';
				if (tmp.value) 
				{
					if (!validateType(arg[i][1],tmp.value)) 
					{
						tmp.className='incorrect'; if (!flagFocus) {alert(typeMsg); tmp.focus();flagFocus=true}; if (flagReturn) flagReturn=false
					}	else if (tmp.className=='incorrect') tmp.className='valid';
				}   
				
			} 
			if (flagReturn) {
				if (!validateCurrentClub()) {
					flagReturn = false;
				}
			}
			return flagReturn;
		}

		function validateForm(arg, formId)
		{
			requaredMsg='Fill in all obvious fields!';
			typeMsg='Field is not filled in correctly!';
			var flagFocus=false;
			var flagReturn=true;
			obj=document.getElementById(formId);
			for (var i=0;i<arg.length;i++) 
			{ 
				eval('var tmp=obj.'+arg[i][0]);
				if (arg[i][2]) if (!tmp.value) 
				{
					tmp.className='focus'; if (!flagFocus) {alert(requaredMsg); tmp.focus();flagFocus=true}; if (flagReturn) flagReturn=false
				} 	else if (tmp.className=='focus') tmp.className='valid';
				if (tmp.value) 
				{
					if (!validateType(arg[i][1],tmp.value)) 
					{
						tmp.className='incorrect'; if (!flagFocus) {alert(typeMsg); tmp.focus();flagFocus=true}; if (flagReturn) flagReturn=false
					}	else if (tmp.className=='incorrect') tmp.className='valid';
				}   
				
			} 
			if (flagReturn) {
				if (!validateCurrentClub()) {
					flagReturn = false;
				}
			}
			return flagReturn;
		}