	var page = 1;
	var expr = "";
	var func = "";
	var par2 = "";
	var par3 = "";
	var par4 = "";
	var lang = "";
	var sess = "";
	var rows = "50";
	
	function setParam(language, sessionId, rowsPerPage) {
		lang = language;
		sess = sessionId;
		rows = rowsPerPage;
	}

	function send_request_to_server_base(funcName, param2, param3, param4){
		func = funcName;
		par2 = param2;
		par3 = param3;
		par4 = param4;
		var val = document.getElementById("expr").value;
		var imgWait = document.getElementById("imgWait"); 
		imgWait.style.visibility = "visible";
		
		var client_transport=new Object();
		
		client_transport.functionName	= func;
		client_transport.functionParam	= val;
		client_transport.functionParam2	= par2;
		client_transport.functionParam3	= par3;
		client_transport.functionParam4	= par4;
		client_transport.idDestination	= 'rows';
		client_transport.language		= lang;
		client_transport.pageNumber		= page;
		client_transport.sessionId		= sess;
		client_transport.pageRowCount	= rows;
		responseUtility.get_responce(client_transport, get_response);
	}

	function send_request_to_server(funcName, param2, param3){
		send_request_to_server_base(funcName, param2, param3, "");
	}
	
	function send_request_to_server(funcName, param2, param3, param4){
		send_request_to_server_base(funcName, param2, param3, param4);
	}
	
	function send_request_to_server2() {
		send_request_to_server_base(func, par2, par3, par4);
	}

	function get_response(transport_from_server){
		var respText = transport_from_server.functionHTMLReturnValue;
		var el = document.getElementById("rows");
        el.innerHTML =  respText;
        var imgWait = document.getElementById("imgWait"); 
		imgWait.style.visibility = "hidden";
	}
	
	function goNext(){
		page++;				
		send_request_to_server2();
		showArrs();
	}
	function goPrev(){
		page--;
		send_request_to_server2();		
		showArrs();
	}
	function goFirst(){
		page = 1;		
		send_request_to_server2();
		showArrs();
	}
	function showArrs(){
		document.getElementById("pcounter").innerHTML = page;
		if (page == 1) showFirstPage(); else showNotFirstPage();
	}
	function showFirstPage(){
		document.getElementById("arr_beg").src = "../images/go/arr_beg2.gif";
		document.getElementById("arr_left").src = "../images/go/arr_left2.gif";
		document.getElementById("a_beg").removeAttribute("href");
		document.getElementById("a_beg").onclick = null;
		document.getElementById("a_left").removeAttribute("href");
		document.getElementById("a_left").onclick = null;
	}
	function showNotFirstPage(){
		document.getElementById("arr_beg").src = "../images/go/arr_beg.gif";
		document.getElementById("arr_left").src = "../images/go/arr_left.gif";
		document.getElementById("a_beg").setAttribute('href', '#');
		document.getElementById("a_beg").onclick = goFirst;
		document.getElementById("a_left").setAttribute('href', '#');
		document.getElementById("a_left").onclick = goPrev;
	}
	function checkExpr(){
		var str = document.getElementById("expr").value;
		if (str!= expr) {
			page = 1;
			expr = str;
			showArrs();
		}
	}
	function focusInput(){
		document.getElementById("expr").focus();
	}	

	function setTitle(pTitle) {
		document.title=pTitle;
	}
