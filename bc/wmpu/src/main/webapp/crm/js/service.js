
function checkJurPrsClubMemberStatus(value) {
	var isDisable = '';
	var className = 'checbox_label';
	if (value == 'OUT_OF_CLUB' || value == '') {
		isDisable = 'DISABLED';
		className = 'checbox_label_disable';
	}
	document.getElementById('is_shareholder').disabled = 
		document.getElementById('is_registrator').disabled = 
		document.getElementById('is_coordinator').disabled = 
		document.getElementById('is_curator').disabled = 
		document.getElementById('is_operator').disabled =  
		document.getElementById('is_agent').disabled =  
		document.getElementById('is_dealer').disabled =  
		document.getElementById('is_bank').disabled =  
		document.getElementById('is_issuer').disabled =  
		document.getElementById('is_finance_acquirer').disabled =  
		document.getElementById('is_technical_acquirer').disabled =  
		document.getElementById('is_terminal_manufacturer').disabled = isDisable;
	document.getElementById('is_shareholder_label').className = 
		document.getElementById('is_registrator_label').className = 
		document.getElementById('is_coordinator_label').className = 
		document.getElementById('is_curator_label').className = 
		document.getElementById('is_operator_label').className =  
		document.getElementById('is_agent_label').className = 
		document.getElementById('is_dealer_label').className = 
		document.getElementById('is_bank_label').className = 
		document.getElementById('is_issuer_label').className =  
		document.getElementById('is_finance_acquirer_label').className = 
		document.getElementById('is_technical_acquirer_label').className = 
		document.getElementById('is_terminal_manufacturer_label').className = className;
	document.getElementById('is_shareholder').checked = (isDisable == 'DISABLED'?'':document.getElementById('is_shareholder').checked);
	document.getElementById('is_registrator').checked = (isDisable == 'DISABLED'?'':document.getElementById('is_registrator').checked);
	document.getElementById('is_coordinator').checked = (isDisable == 'DISABLED'?'':document.getElementById('is_coordinator').checked);
	document.getElementById('is_curator').checked = (isDisable == 'DISABLED'?'':document.getElementById('is_curator').checked);
	document.getElementById('is_operator').checked = (isDisable == 'DISABLED'?'':document.getElementById('is_operator').checked);
	document.getElementById('is_agent').checked = (isDisable == 'DISABLED'?'':document.getElementById('is_agent').checked);
	document.getElementById('is_dealer').checked = (isDisable == 'DISABLED'?'':document.getElementById('is_dealer').checked);
	document.getElementById('is_bank').checked = (isDisable == 'DISABLED'?'':document.getElementById('is_bank').checked);
	document.getElementById('is_issuer').checked = (isDisable == 'DISABLED'?'':document.getElementById('is_issuer').checked);
	document.getElementById('is_finance_acquirer').checked = (isDisable == 'DISABLED'?'':document.getElementById('is_finance_acquirer').checked);
	document.getElementById('is_technical_acquirer').checked = (isDisable == 'DISABLED'?'':document.getElementById('is_technical_acquirer').checked);
	document.getElementById('is_terminal_manufacturer').checked = (isDisable == 'DISABLED'?'':document.getElementById('is_terminal_manufacturer').checked);

	checkJurPrsShareholder();
}
function checkJurPrsShareholder() {
	var is_shareholder = document.getElementById('is_shareholder');
	var isDisable = '';
	var className = 'checbox_label';
	
	if (!is_shareholder.checked) {
		isDisable = 'DISABLED';
		className = 'checbox_label_disable';
	}
	document.getElementById('is_registrator').disabled = 
		document.getElementById('is_coordinator').disabled = 
		document.getElementById('is_curator').disabled = isDisable;
	document.getElementById('is_registrator_label').className = 
		document.getElementById('is_coordinator_label').className = 
		document.getElementById('is_curator_label').className = className;
	document.getElementById('is_registrator').checked = (isDisable == 'DISABLED'?'':document.getElementById('is_registrator').checked);
	document.getElementById('is_coordinator').checked = (isDisable == 'DISABLED'?'':document.getElementById('is_coordinator').checked);
	document.getElementById('is_curator').checked = (isDisable == 'DISABLED'?'':document.getElementById('is_curator').checked);
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