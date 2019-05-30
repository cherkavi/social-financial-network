function card_mask(field_id){
		jQuery(document).ready(function(){
			jQuery("#"+field_id).mask("9999 9999 9999 9",{placeholder:" "});
		});
	}
	jQuery.fn.formatCard = function(){
		return this.each(function(){
			$(this).bind('keyup', function(){
				var num = this.value.replace( /\D/g, '' ).split( /(?=.)/ ), i = num.length;
				if ( 4 <= i ) num.splice( 4, 0, ' ' );
				if ( 8 <= i ) num.splice( 9, 0, ' ' );
				if ( 12 <= i ) num.splice( 14, 0, ' ' );
				if ( 13 <= i ) num.splice( 16, num.length - 16 );
				this.value = num.join( '' );

				//alert(this.value);
			});
			$(this).keyup();
		});
	};
	function card_mask2(field_id){
		jQuery(document).ready(function(){
			jQuery("#"+field_id).formatCard();
		});
	}

	jQuery.fn.formatPnoneNumber = function(lang, is_empty){
		return this.each(function(){
			// $(this).bind('keyup', function(){
			execute_function=function(){
				var num = this.value.replace( '+7 (' , '' ).replace( '+7(' , '' ).replace( '+7' , '' ).replace( '+38(' , '' ).replace( '+38 (' , '' ).replace( '+38' , '' ).replace( '+375 (' , '' ).replace( '+375(' , '' ).replace( '+375' , '' ).replace( /\D/g, '' ).split( /(?=.)/ );
				var i = num.length;
				if (lang == '643' || lang == '398') {
					if ( num == '' ) { 
						if (is_empty) {
							num.unshift( '+7 (' );
						} else {
							num.unshift( '' );
						}
						//alert('num=\''+num+'\', lentgh='+num.length);
					} else {
						if ( 1 <= i ) num.splice( 0, 0, '+7 (' );
						if ( 4 <= i ) num.splice( 4, 0, ') ' );
						if ( 7 <= i ) num.splice( 8, 0, '-' );
						if ( 11 <= i ) num.splice( 13, num.length - 13 );
					}
					this.value = num.join( '' );
				} else if (lang == '804') {
					if ( num == '' ) { 
						if (is_empty) {
							num.unshift( '+38 (' );
						} else {
							num.unshift( '' );
						}
					} else {
						if ( 1 <= i ) num.splice( 0, 0, '+38 (' );
						if ( 4 <= i ) num.splice( 4, 0, ') ' );
						if ( 7 <= i ) num.splice( 8, 0, '-' );
						//if ( 9 <= i ) num.splice( 12, 0, '-' );
						if ( 11 <= i ) num.splice( 13, num.length - 13 );
					}
					this.value = num.join( '' );
				} else if (lang == '112') {
					if ( num == '' ) { 
						if (is_empty) {
							num.unshift( '+375 (' );
						} else {
							num.unshift( '' );
						}
					} else {
						if ( 1 <= i ) num.splice( 0, 0, '+375 (' );
						if ( 3 <= i ) num.splice( 3, 0, ') ' );
						if ( 6 <= i ) num.splice( 7, 0, '-' );
						//if ( 9 <= i ) num.splice( 12, 0, '-' );
						if ( 10 <= i ) num.splice( 12, num.length - 12 );
					}
					this.value = num.join( '' );
				}
			};
			$(this).bind('keyup', execute_function);
			$(this).bind('focusout', execute_function);  // onblur - doesn't work
			$(this).keyup();
		});
	};
	
	function phone_mask(field_id,lang) {
		jQuery(document).ready(function(){
			
			jQuery("#"+field_id).formatPnoneNumber(lang,false);
		});
	}
	
	function phone_mask_empty(field_id,lang) {
		jQuery(document).ready(function(){
			jQuery("#"+field_id).formatPnoneNumber(lang,true);
		});
		
	}

	jQuery.fn.formatSMS = function(){
		return this.each(function(){
			$(this).bind('keyup', function(){
				var num = this.value.replace( /\D/g, '' ).split( /(?=.)/ ), i = num.length;
				if ( 2 <= i ) num.splice( 2, 0, ' ' );
				if ( 4 <= i ) num.splice( 5, 0, ' ' );
				if ( 6 <= i ) num.splice( 8, 0, ' ' );
				if ( 8 <= i ) num.splice( 11, num.length - 11 );
				this.value = num.join( '' );

				//alert(this.value);
			});
			$(this).keyup();
		});
	};
	function sms_mask(field_id) {
		jQuery(document).ready(function(){
			jQuery("#"+field_id).formatSMS();
		});
		
	}
	
	function GenerateCaptcha() {
		/*
		 * @deprecated
		var a = Math.ceil(Math.random() * 9) + '';
		var b = Math.ceil(Math.random() * 9) + '';
		var c = Math.ceil(Math.random() * 9) + '';
		var d = Math.ceil(Math.random() * 9) + '';
		var e = Math.ceil(Math.random() * 9) + '';
		var f = Math.ceil(Math.random() * 9) + '';
		var g = Math.ceil(Math.random() * 9) + '';
		var code = a + ' ' + b + ' ' + ' ' + c + ' ' + d + ' ' + e + ' ' + f + ' ' + g;
		document.getElementById('cpc').innerHTML = code;			
		*/
		document.getElementById('cpc').src="../CaptchaGenerator";
	}
	// Validate the Entered input
	/**
	 * @Deprecated
	function ValidCaptcha() {
		
		var txtCaptchaValue = document.getElementById('cpc').innerHTML;
		var str1 = removeSpaces(txtCaptchaValue);
		var str2 = removeSpaces(document.getElementById('txtInput').value);
		if (str1 == str2) return true;
		alert('Неверный код, указанный на картинке');
		return false;
	}
	*/
	
	// Remove the spaces from the entered and generated code
	function removeSpaces(string) {
		return string.split(' ').join('');
	}
	