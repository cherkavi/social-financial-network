var page = 1;
	var expr = "";			
	var request = null;
		 	
	function goNext(){
		page++;				
		send_to_server();
		showArrs();
	}
	function goPrev(){
		page--;
		send_to_server();		
		showArrs();
	}
	function goFirst(){
		page = 1;		
		send_to_server();
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