function putToSelect1(element_name, array_of_values, array_of_text) {
	var element = document.getElementById(element_name);
	var el_value = element.value;
	for(var counter=element.childNodes.length-1;counter>=0;counter--){
		element.removeChild(element.childNodes[counter]);
	}
	var option_element_null=document.createElement("option");
	element.appendChild(option_element_null);
	for(counter=0;counter<array_of_values.length;counter++){
		var option_element=document.createElement("option");
		var text_element=document.createTextNode(array_of_text[counter]);
		option_element.value=array_of_values[counter];
		if(array_of_values[counter]==el_value){
			option_element.selected="selected";
		}
		option_element.appendChild(text_element);
		element.appendChild(option_element);
	}
}

function putToSelect2(element_name, selected_value, array_of_values, array_of_text) {
	var element = document.getElementById(element_name);
	var el_value = selected_value;
	for(var counter=element.childNodes.length-1;counter>=0;counter--){
		element.removeChild(element.childNodes[counter]);
	}

	var option_element_null=document.createElement("option");
	element.appendChild(option_element_null);
	for(counter=0;counter<array_of_values.length;counter++){
		var option_element=document.createElement("option");
		var text_element=document.createTextNode(array_of_text[counter]);
		option_element.value=array_of_values[counter];
		if(array_of_values[counter]==el_value){
			option_element.selected="selected";
		}
		option_element.appendChild(text_element);
		element.appendChild(option_element);
	}
}

//------------- КОМИССИИ -----------
function dwr_get_card_bon_disc_category(select_object, sessionId){
	var client_transport=new Object();
	
	client_transport.functionName	= "GET_CARD_CATEGORY";
	client_transport.functionParam	= select_object.value;
	client_transport.sessionId		= sessionId;
	responseUtility.get_responce(client_transport, resp_card_bon_disc_category);
}

function resp_card_bon_disc_category(transport_from_server){
	putToSelect1('id_bon_category', transport_from_server.functionKeys, transport_from_server.functionValues);
	putToSelect1('id_disc_category', transport_from_server.functionKeys, transport_from_server.functionValues);
}			

function dwr_get_card_bon_disc_category2(obj_type, select_object, sessionId){
	var client_transport=new Object();
	
	client_transport.functionName	= "GET_CARD_CATEGORY";
	client_transport.functionParam	= select_object.value;
	client_transport.functionParam2	= obj_type;
	client_transport.sessionId		= sessionId;
	responseUtility.get_responce(client_transport, resp_card_bon_disc_category2);
}

function resp_card_bon_disc_category2(transport_from_server){
	var obj_type = transport_from_server.functionParam2;

	putToSelect1(obj_type+'_id_bon_category', transport_from_server.functionKeys, transport_from_server.functionValues);
	putToSelect1(obj_type+'_id_disc_category', transport_from_server.functionKeys, transport_from_server.functionValues);
}

function dwr_get_card_bon_disc_category3(obj_type, select_object, id_bon_category, id_disc_category, sessionId){
	var client_transport=new Object();
	client_transport.categoryPrefix   = obj_type;
	client_transport.idCardStatus   = select_object.value;
	client_transport.idBonCategory   = id_bon_category;
	client_transport.idDiscCategory   = id_disc_category;
	client_transport.sessionId		= sessionId;
	responseUtility.getCardCategoriesArray(client_transport, resp_card_bon_disc_category3);
}

function resp_card_bon_disc_category3(categ){
	var obj_type = categ.categoryPrefix;
	if (obj_type==null) {
		obj_type = '';
	}
	putToSelect2(obj_type+'id_bon_category', categ.idBonCategory, categ.bonCategoriesId, categ.bonCategoriesName);
	putToSelect2(obj_type+'id_disc_category', categ.idDiscCategory, categ.discCategoriesId, categ.discCategoriesName);
}

// -- Тип банковского счета в бух.счете

function dwr_get_bk_bank_acc_type(select_object, sessionId){
	var client_transport=new Object();
	
	client_transport.functionName="GET_BK_BANK_ACCOUNT_TYPE_FOR_CLUB_REL";
	client_transport.functionParam=select_object.value;
	client_transport.sessionId		= sessionId;
	responseUtility.get_responce(client_transport, resp_bk_bank_acc_type);
}

function resp_bk_bank_acc_type(transport_from_server){
	putToSelect1('receiver_cd_bk_bank_accnt_type', transport_from_server.functionKeys, transport_from_server.functionValues);
	putToSelect1('payer_cd_bk_bank_accnt_type', transport_from_server.functionKeys, transport_from_server.functionValues);
}

// Получение списка областей для страны 
var oblast_destination = "";
function dwr_oblast_array(destination, code_country, id_oblast, sessionId){
	var client_transport=new Object();
	oblast_destination = destination;
	client_transport.codeCountry	= code_country;
	client_transport.idOblast		= id_oblast;
	client_transport.sessionId		= sessionId;
	responseUtility.getOblastArray(client_transport, resp_oblast_array);
}

function resp_oblast_array(address){
	//window.alert('aa');
	putToSelect1(oblast_destination, address.oblastListCode, address.oblastListName);
}

//------------- ЮРИДИЧЕСКИЙ И ФИКТИЧЕСКИЙ АДРЕСА -----------------------

function dwr_make_jur_adr_onload(){
	var client_transport=new Object();
	client_transport.codeCountry	= document.getElementById('JUR_ADR_CODE_COUNTRY').value;
	client_transport.idOblast		= document.getElementById('JUR_ADR_ID_OBLAST').value;
	client_transport.idDistrict		= document.getElementById('JUR_ADR_ID_DISTRICT').value;
	client_transport.idCity			= document.getElementById('JUR_ADR_ID_CITY').value;
	client_transport.idCityDistrict	= document.getElementById('JUR_ADR_ID_CITY_DISTRICT').value;
	client_transport.sessionId		= lSessionId;
	responseUtility.getAddressArray(client_transport, get_jur_response_onload);
}

function get_jur_response_onload(transport_from_server){
	load_address(transport_from_server,'JUR_ADR_CODE_COUNTRY','JUR_ADR_ID_OBLAST','JUR_ADR_ID_DISTRICT','JUR_ADR_ID_CITY','JUR_ADR_ID_CITY_DISTRICT');
	dwr_make_fact_adr(lSessionId);
}

function dwr_make_jur_adr(sessionId){
	var client_transport=new Object();
	client_transport.codeCountry	= document.getElementById('JUR_ADR_CODE_COUNTRY').value;
	client_transport.idOblast		= document.getElementById('JUR_ADR_ID_OBLAST').value;
	client_transport.idDistrict		= document.getElementById('JUR_ADR_ID_DISTRICT').value;
	client_transport.idCity			= document.getElementById('JUR_ADR_ID_CITY').value;
	client_transport.idCityDistrict	= document.getElementById('JUR_ADR_ID_CITY_DISTRICT').value;
	client_transport.sessionId		= sessionId;
	responseUtility.getAddressArray(client_transport, get_jur_response);
}

function get_jur_response(transport_from_server){
	load_address(transport_from_server,'JUR_ADR_CODE_COUNTRY','JUR_ADR_ID_OBLAST','JUR_ADR_ID_DISTRICT','JUR_ADR_ID_CITY','JUR_ADR_ID_CITY_DISTRICT');
}

function dwr_make_fact_adr(sessionId){
	var client_transport=new Object();
	client_transport.codeCountry	= document.getElementById('FACT_ADR_CODE_COUNTRY').value;
	client_transport.idOblast		= document.getElementById('FACT_ADR_ID_OBLAST').value;
	client_transport.idDistrict		= document.getElementById('FACT_ADR_ID_DISTRICT').value;
	client_transport.idCity			= document.getElementById('FACT_ADR_ID_CITY').value;
	client_transport.idCityDistrict	= document.getElementById('FACT_ADR_ID_CITY_DISTRICT').value;
	client_transport.sessionId		= sessionId;
	responseUtility.getAddressArray(client_transport, get_fact_response);
	//window.alert('test2');
}

function dwr_make_fact_adr_cur(codeCountry, idOblast, idDistrict, idCity, idCityDistrict, sessionId){
	var client_transport=new Object();
	client_transport.codeCountry	= codeCountry;	  //document.getElementById('FACT_ADR_CODE_COUNTRY').value;
	client_transport.idOblast		= idOblast;		  //document.getElementById('FACT_ADR_ID_OBLAST').value;
	client_transport.idDistrict		= idDistrict;	  //document.getElementById('FACT_ADR_ID_DISTRICT').value;
	client_transport.idCity			= idCity;		  //document.getElementById('FACT_ADR_ID_CITY').value;
	client_transport.idCityDistrict	= idCityDistrict; //document.getElementById('FACT_ADR_ID_CITY_DISTRICT').value;
	client_transport.sessionId		= sessionId;
	responseUtility.getAddressArray(client_transport, get_fact_response);
	//window.alert('test');
}

function get_fact_response(transport_from_server){
	load_address(transport_from_server,'FACT_ADR_CODE_COUNTRY','FACT_ADR_ID_OBLAST','FACT_ADR_ID_DISTRICT','FACT_ADR_ID_CITY','FACT_ADR_ID_CITY_DISTRICT');
}

function load_address(address, selCountry, selOblast, selDistrict, selCity, selCityDistrict){
	var select_codeCountry		= document.getElementById(selCountry);
	var codeCountry				= address.codeCountry;

	for(var counter=select_codeCountry.childNodes.length-1;counter>=0;counter--){
		if(select_codeCountry.childNodes[counter].value==codeCountry){
			select_codeCountry.childNodes[counter].selected="selected";
		}
	}

	putToSelect2(selOblast, address.idOblast, address.oblastListCode, address.oblastListName);
	putToSelect2(selDistrict, address.idDistrict, address.districtListCode, address.districtListName);
	putToSelect2(selCity, address.idCity, address.cityListCode, address.cityListName);
	putToSelect2(selCityDistrict, address.idCityDistrict, address.cityDistrictListCode, address.cityDistrictListName);
}

function copyJurPrsFactAddress(sessionId){
	document.getElementById('FACT_ADR_ZIP_CODE').value = document.getElementById('JUR_ADR_ZIP_CODE').value;
	document.getElementById('FACT_ADR_STREET').value = document.getElementById('JUR_ADR_STREET').value;
	document.getElementById('FACT_ADR_HOUSE').value = document.getElementById('JUR_ADR_HOUSE').value;
	document.getElementById('FACT_ADR_CASE').value = document.getElementById('JUR_ADR_CASE').value;
	document.getElementById('FACT_ADR_APARTMENT').value = document.getElementById('JUR_ADR_APARTMENT').value;
	dwr_make_fact_adr_cur(document.getElementById('JUR_ADR_CODE_COUNTRY').value,
				  	  document.getElementById('JUR_ADR_ID_OBLAST').value,
				  	  document.getElementById('JUR_ADR_ID_DISTRICT').value,
				  	  document.getElementById('JUR_ADR_ID_CITY').value,
				  	  document.getElementById('JUR_ADR_ID_CITY_DISTRICT').value,
				  	  sessionId);
}

function dwr_make_ser_place_adr(sessionId){
	var client_transport=new Object();
	client_transport.codeCountry	= document.getElementById('ADR_CODE_COUNTRY').value;
	client_transport.idOblast		= document.getElementById('ADR_ID_OBLAST').value;
	client_transport.idDistrict		= document.getElementById('ADR_ID_DISTRICT').value;
	client_transport.idCity			= document.getElementById('ADR_ID_CITY').value;
	client_transport.idCityDistrict	= document.getElementById('ADR_ID_CITY_DISTRICT').value;
	client_transport.sessionId		= sessionId;
	responseUtility.getAddressArray(client_transport, resp_serv_place_adr);
	//window.alert('test');
}

function dwr_make_ser_place_adr_copy(codeCountry, idOblast, idDistrict, idCity, idCityDistrict, sessionId){
	var client_transport=new Object();
	client_transport.codeCountry	= codeCountry;
	client_transport.idOblast		= idOblast;
	client_transport.idDistrict		= idDistrict;
	client_transport.idCity			= idCity;
	client_transport.idCityDistrict	= idCityDistrict;
	client_transport.sessionId		= sessionId;
	responseUtility.getAddressArray(client_transport, resp_serv_place_adr);
	//window.alert('test');
}

function resp_serv_place_adr(transport_from_server){
	load_serv_place_adr(transport_from_server,'ADR_ID_OBLAST','ADR_ID_DISTRICT','ADR_ID_CITY','ADR_ID_CITY_DISTRICT');
}

function load_serv_place_adr(address, selOblast, selDistrict, selCity, selCityDistrict){
	putToSelect2(selOblast, address.idOblast, address.oblastListCode, address.oblastListName);
	putToSelect2(selDistrict, address.idDistrict, address.districtListCode, address.districtListName);
	putToSelect2(selCity, address.idCity, address.cityListCode, address.cityListName);
	putToSelect2(selCityDistrict, address.idCityDistrict, address.cityDistrictListCode, address.cityDistrictListName);

}


//------------ ТИП УСТРОЙСТВА ТЕРМИНАЛА --------------
function dwr_get_term_device_type(select_object, id_destination, sessionId){
	var client_transport=new Object();
	
	client_transport.functionName	= "GET_TERM_DEVICE_TYPE";
	client_transport.functionParam	= select_object.value;
	client_transport.idDestination	= id_destination;
	client_transport.sessionId		= sessionId;
	//window.alert(select_object.value);
	responseUtility.get_responce(client_transport, resp_term_device_type);
}

function resp_term_device_type(transport_from_server){
	putToSelect1(transport_from_server.idDestination, transport_from_server.functionKeys, transport_from_server.functionValues);
}

function dwr_get_card_category(select_object, id_destination, sessionId){
	var client_transport=new Object();
	
	client_transport.functionName="GET_CARD_CATEGORY";
	client_transport.functionParam=select_object.value;
	client_transport.idDestination=id_destination;
	client_transport.sessionId		= sessionId;
	responseUtility.get_responce(client_transport, resp_get_card_category);
}

function resp_get_card_category(transport_from_server){
	putToSelect1(transport_from_server.idDestination, transport_from_server.functionKeys, transport_from_server.functionValues);
}