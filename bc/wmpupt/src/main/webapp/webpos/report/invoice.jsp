<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebPosBean"  />

<%@page import="java.util.HashMap"%>
<%@page import="webpos.wpChequeObject"%>
	<link rel="stylesheet" type="text/css" href="../<%=Bean.getThemePath()%>/CSS/webpos.css">
	<script type="text/javascript" src="../js/frame_emulator.js" > </script>
	<script type="text/javascript" src="../js/webpos.js" > </script>
	<title>WebPOS - <%=Bean.webposXML.getfieldTransl("window_cheque", false) %></title>

	<meta content="width=device-width, initial-scale=0.8,maximum-scale=1.5,user-scalable=1" name="viewport">
	
	<link content="yes" name="apple-mobile-web-app-capable">
	<meta content="black" name="apple-mobile-web-app-status-bar-style">
<%

request.setCharacterEncoding("UTF-8");

String pageFormName = "CHEQUE";

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());


String id_telgr		= Bean.getDecodeParam(parameters.get("id_telgr")); 
String print		= Bean.getDecodeParam(parameters.get("print")); 

Bean.loginTerm.getTermFeature();

String cheque_format = Bean.getChequeSaveFormat();

%>
<body>
	<%
	wpChequeObject cheque = new wpChequeObject(id_telgr, Bean.getChequeSaveFormat(), Bean.loginTerm);
	%>
	<script language="javascript">
	function printDiv(divName) {
	    var printContents = document.getElementById(divName).innerHTML;
	    var originalContents = document.body.innerHTML;
	    document.body.innerHTML = printContents;
	    window.print();
	    document.body.innerHTML = originalContents;
	}
	function printDivShort(divName) {
	    window.print();
	}

    var gAutoPrint = true;
    function processPrint(divName){
    	if (document.getElementById != null){
    		var html = '<HTML>\n<HEAD>\n';
    		if (document.getElementsByTagName != null){
    			var headTags = document.getElementsByTagName("head");
    			if (headTags.length > 0) html += headTags[0].innerHTML;
    		}
    		html += '\n</HE' + 'AD>\n<BODY>\n';
    		var printReadyElem = document.getElementById(divName);
    		if (printReadyElem != null) html += printReadyElem.innerHTML;
    		else{
    			alert("Error, no contents.");
    			return;
    		}

    		html += '\n</BO' + 'DY>\n</HT' + 'ML>';
    		var printWin = window.open("","processPrint");
    		printWin.document.open();
    		printWin.document.write(html);
   			printWin.document.close();

    		if (gAutoPrint) printWin.print();
    	} else alert("Browser not supported.");
    }
	</script>
					

	<table class="table_cheque"><tbody>
	<tr>
		<td style="width:404px; text-align: center; margin: 0 auto;" colspan="2" class="centerb">
			<%= cheque.getInvoiceAllButtons(true, false) %>
		</td>
	</tr>
	</tbody></table>
	<div id="printableArea"> 
	<table class="table_cheque"><tbody>
		<%=cheque.getInvoiceHTML() %>
	</tbody></table>
	</div>
	<script language="javascript">
	<% if ("Y".equalsIgnoreCase(print)) {%>
	printDivShort('printableArea');
	<% }%>
	</script>


</body>
</html>