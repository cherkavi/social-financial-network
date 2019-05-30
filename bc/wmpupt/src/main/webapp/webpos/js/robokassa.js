	function robokassa_change_phone(label_caption) {
		var setting = document.getElementById('robokassa_phone_setting');
		setting.innerHTML = '<input id="send_robokassa_payment_SMS" type="checkbox" value="SMS" name="send_robokassa_payment_SMS" checked>&nbsp;<label class="inputfield_finish_green" for="send_robokassa_payment_SMS">' + label_caption + '</label><input type="text" id="robokassa_payment_phone" name="robokassa_payment_phone" class="inputfield" size="20">';
	}
	
	function robokassa_change_email(label_caption) {
		var setting = document.getElementById('robokassa_email_setting');
		setting.innerHTML = '<input id="send_robokassa_payment_EMAIL" type="checkbox" value="SMS" name="send_robokassa_payment_EMAIL" checked>&nbsp;<label class="inputfield_finish_green" for="send_robokassa_payment_EMAIL">' + label_caption + '</label><input type="text" id="robokassa_payment_email" name="robokassa_payment_email" class="inputfield" size="20">';
	}
	
	function invoice_change_email(label_caption) {
		var setting = document.getElementById('invoice_email_setting');
		setting.innerHTML = '<input id="send_invoice_payment_EMAIL" type="checkbox" value="SMS" name="send_invoice_payment_EMAIL" checked>&nbsp;<label class="inputfield_finish_green" for="send_invoice_payment_EMAIL">' + label_caption + '</label><input type="text" id="invoice_payment_email" name="invoice_payment_email" class="inputfield" size="20">';
	}
