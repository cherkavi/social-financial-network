<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebClientBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.wcClubCardObject"%>
<%@page import="bc.objects.wcNatPrsObject"%>
<html>
<%

request.setCharacterEncoding("UTF-8");

String pageFormName = "WEBCLIENT_CARD_CARD_PARAM";
String tagTrans = "_TRANS";
String tagTransFind = "_TRANS_FIND";
String tagOper = "_OPER";
String tagOperFind = "_OPER_FIND";

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String cardid = Bean.getDecodeParam(parameters.get("cardid"));
String iss = Bean.getDecodeParam(parameters.get("iss"));
String paysys = Bean.getDecodeParam(parameters.get("paysys"));
String cd_card1 = Bean.getDecodeParam(parameters.get("cd_card1"));
String cardpage = Bean.getDecodeParam(parameters.get("cardpage"));

String l_trans_page = Bean.getDecodeParam(parameters.get("trans_page"));
Bean.pageCheck(pageFormName + tagTrans, l_trans_page);
String l_trans_page_beg = Bean.getFirstRowNumber(pageFormName + tagTrans);
String l_trans_page_end = Bean.getLastRowNumber(pageFormName + tagTrans);

String find_trans 		= Bean.getDecodeParam(parameters.get("find_trans"));
find_trans 	= Bean.checkFindString(pageFormName + tagTransFind, find_trans, l_trans_page);

String l_oper_page = Bean.getDecodeParam(parameters.get("oper_page"));
Bean.pageCheck(pageFormName + tagOper, l_oper_page);
String l_oper_page_beg = Bean.getFirstRowNumber(pageFormName + tagTrans);
String l_oper_page_end = Bean.getLastRowNumber(pageFormName + tagTrans);

String find_oper 		= Bean.getDecodeParam(parameters.get("find_oper"));
find_oper 	= Bean.checkFindString(pageFormName + tagOperFind, find_oper, l_oper_page);

if (cardid==null || "".equalsIgnoreCase(cardid)) {
	cardid = Bean.getCurrentCardSerialNumber();
	iss = Bean.getCurrentCardIdIssuer();
	paysys = Bean.getCurrentCardIdPaymentsystem();
	cd_card1 = Bean.getCurrentCardCDCard1();
}

if (cardpage==null || "".equalsIgnoreCase(cardpage)) {
	cardpage = "1";
}

Bean.setCurrentUserClubCards(Bean.getLoginUserIDNatPrs());

Bean.setCurrentCard(cardid, iss, paysys);
wcClubCardObject clubcard = new wcClubCardObject(cardid, iss, paysys);

wcNatPrsObject natprs = new wcNatPrsObject(Bean.getLoginUserIDNatPrs());

%>
<body>
	<table <%=Bean.getTableDetail2Param() %>>
		<tr>
			<td>
				<div id="div_card_list">
				<%=Bean.getWebClientCardParamList(cd_card1, "", cardpage) %>
				<div class="div_card_group div_add_card_group" onclick="ajaxpage('card/card_paramupdate.jsp?cardid=<%=cardid %>&type=card&action=add&process=no', 'div_card_detail')">Добавить</div>
				</div>
			</td>
			<td>
				<div id="div_card_detail">
				<div id="div_card_param">
					<div id="slidetabsmenu">
						<ul>
						<li <%if ("1".equalsIgnoreCase(cardpage)) { %> id="current"<%} %>><a href="#"><span onclick="ajaxpage('card/card_param.jsp?cardid=<%=cardid %>&iss=<%=iss %>&paysys=<%=paysys %>&cd_card1=<%=cd_card1 %>&cardpage=1', 'div_main')">Карта</span></a></li>
						<li <%if ("2".equalsIgnoreCase(cardpage)) { %> id="current"<%} %>><a href="#"><span onclick="ajaxpage('card/card_param.jsp?cardid=<%=cardid %>&iss=<%=iss %>&paysys=<%=paysys %>&cd_card1=<%=cd_card1 %>&cardpage=2', 'div_main')">Покупки</span></a></li>
						<li <%if ("3".equalsIgnoreCase(cardpage)) { %> id="current"<%} %>><a href="#"><span onclick="ajaxpage('card/card_param.jsp?cardid=<%=cardid %>&iss=<%=iss %>&paysys=<%=paysys %>&cd_card1=<%=cd_card1 %>&cardpage=3', 'div_main')">Подарки</span></a></li>
						<li <%if ("4".equalsIgnoreCase(cardpage)) { %> id="current"<%} %>><a href="#"><span onclick="ajaxpage('card/card_param.jsp?cardid=<%=cardid %>&iss=<%=iss %>&paysys=<%=paysys %>&cd_card1=<%=cd_card1 %>&cardpage=4', 'div_main')">Операции</span></a></li>
						</ul>
					</div><br>
					<%if ("1".equalsIgnoreCase(cardpage)) { %>
						<br>
						<form name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
						<table class="tablebottom-filter">
							<tr>
								<td><%= Bean.getClubCardXMLFieldTransl("card_serial_number", false) %></td><td><input type="text" name="card_serial_number" size="20" value="<%= cardid %>" readonly class="inputfield-ro"></td>
								<td><%= Bean.getClubCardXMLFieldTransl("id_card_type", false) %></td><td><input type="text" name="id_card_type" size="20" value="<%= clubcard.getValue("NAME_CARD_TYPE") %>" readonly class="inputfield-ro"></td>
							</tr>
							<tr>
								<td><%= Bean.getClubCardXMLFieldTransl("cd_card1", false) %> </td><td><input type="text" name="cd_card1" size="20" value="<%= clubcard.getValue("CD_CARD1") %>" readonly class="inputfield-ro"></td>
								<td><%= Bean.getClubCardXMLFieldTransl("name_card_status", false) %></td><td><input type="text" name="name_card_status" size="20" value="<%= clubcard.getValue("NAME_CARD_STATUS") %>" readonly class="inputfield-ro"></td>
							</tr>
							<tr>
								<td><%= Bean.getClubCardXMLFieldTransl("cd_card2", false) %> </td><td><input type="text" name="cd_card2" size="20" value="<%= clubcard.getValue("CD_CARD2") %>" readonly class="inputfield-ro"></td>
								<td><%= Bean.getClubCardXMLFieldTransl("id_card_state", false) %></td><td><input type="text" name="id_card_state" size="20" value="<%= clubcard.getValue("NAME_CARD_STATE") %>" readonly class="inputfield-ro"></td>
							</tr>
							<tr>
					 			<td><%= Bean.getClubCardXMLFieldTransl("name_payment_system", false) %></td><td><input type="text" name="name_payment_system" size="20" value="<%= clubcard.getValue("NAME_PAYMENT_SYSTEM")%>" readonly class="inputfield-ro"></td>
								<td><%= Bean.getClubCardXMLFieldTransl("nt_icc", false) %></td><td><input type="text" name="nt_icc" size="10" value="<%= clubcard.getValue("NT_ICC")%>" readonly class="inputfield-ro"></td>
							</tr>
							<tr>
								<td colspan=4 class="top_line"><b><%= Bean.getClubCardXMLFieldTransl("h_card_categories", false) %></b></td>
							</tr>
							<tr>			
								<td><%= Bean.getClubCardXMLFieldTransl("CLUB_BON", false) %> </td> <td><input type="text" name="club_bon" size="10" value="<%= clubcard.getValue("CLUB_BON_FRMT") %>" readonly class="inputfield-ro"></td>
								<td><%= Bean.getClubCardXMLFieldTransl("CLUB_DISC", false) %></td> <td><input type="text" name="club_disc" size="10" value="<%= clubcard.getValue("CLUB_DISC_FRMT") %>" readonly class="inputfield-ro"></td>
							</tr>
							<tr>
								<td colspan=4 class="top_line"><b><%= Bean.getClubCardXMLFieldTransl("h_card_rests", false) %></b></td>
							</tr>
							<tr>
							    <td><%= Bean.getClubCardXMLFieldTransl("BAL_ACC", false) %> (<%=clubcard.getValue("SNAME_CURRENCY") %>)</td> <td><input type="text" name="bal_acc" size="10" value="<%= clubcard.getValue("BAL_ACC_FRMT") %>" readonly class="inputfield-ro"> </td>
								<td><%= Bean.getClubCardXMLFieldTransl("BAL_BON_PER", false) %> (<%=clubcard.getValue("SNAME_CURRENCY") %>)</td> <td><input type="text" name="bal_bon_per" size="10" value="<%= clubcard.getValue("BAL_BON_PER_FRMT") %>" readonly class="inputfield-ro"></td>
							</tr>
							<tr>
								<td><%= Bean.getClubCardXMLFieldTransl("BAL_CUR", false) %> (<%=clubcard.getValue("SNAME_CURRENCY") %>)</td> <td><input type="text" name="bal_cur" size="10" value="<%= clubcard.getValue("BAL_CUR_FRMT") %>" readonly class="inputfield-ro"> </td>
								<td><%= Bean.getClubCardXMLFieldTransl("BAL_DISC_PER", false) %> (<%=clubcard.getValue("SNAME_CURRENCY") %>)</td> <td><input type="text" name="bal_disc_per" size="10" value="<%= clubcard.getValue("BAL_DISC_PER_FRMT") %>" readonly class="inputfield-ro"></td>
							</tr>
							<tr>
								<td colspan=4 class="top_line"><b><%= Bean.getClubCardXMLFieldTransl("h_card_dates", false) %></b></td>
							</tr>
							<tr>
								<td><%= Bean.getClubCardXMLFieldTransl("date_acc", false) %></td> <td><input type="text" name="date_acc" size="10" value="<%= clubcard.getValue("DATE_ACC_DF") %>" readonly class="inputfield-ro"> </td>
								<td><%= Bean.getClubCardXMLFieldTransl("date_calc", false) %></td> <td><input type="text" name="date_calc" size="10" value="<%= clubcard.getValue("DATE_CALC_DF") %>" readonly class="inputfield-ro"> </td>
							</tr>
							<tr>
								<td><%= Bean.getClubCardXMLFieldTransl("date_mov", false) %></td> <td><input type="text" name="date_mov" size="10" value="<%= clubcard.getValue("DATE_MOV_DF") %>" readonly class="inputfield-ro"> </td>
								<td><%= Bean.getClubCardXMLFieldTransl("date_onl", false) %></td> <td><input type="text" name="date_onl" size="10" value="<%= clubcard.getValue("DATE_ONL_DF") %>" readonly class="inputfield-ro"> </td>
							</tr>
						</table>
						</form>
					<% } else if ("2".equalsIgnoreCase(cardpage)) { %>
					
						<table <%=Bean.getTableBottomFilter() %>>
						  <tr><td><b><%=cd_card1 %></b></td></tr>
						  <tr>
							<%= Bean.getFindHTML("find_trans", find_trans, "card/card_param.jsp?id=" + cardid + "&iss=" + iss + "&paysys=" + paysys + "&trans_page=1&cardpage=" + cardpage + "&") %>
							<%= Bean.getPagesHTML(pageFormName + tagTrans, "card/card_param.jsp?id=" + cardid + "&iss=" + iss + "&paysys=" + paysys + "&cardpage=" + cardpage + "&", "trans_page") %>
						  </tr>
						</table>
						<%=clubcard.getWEBClientCardTransHTML(find_trans, l_trans_page_beg, l_trans_page_end) %>

					<% } else if ("3".equalsIgnoreCase(cardpage)) { %>
					
						<table <%=Bean.getTableBottomFilter() %>>
						  	<tr>
								<td><b><%=cd_card1 %></b></td>
							</tr>
							<tr>
								<td colspan=4>&nbsp;</td>
							</tr>
							<tr>
								<td colspan=4 style="font-size: 14px;font-weight: bold;">Заглушка!<br><br></td>
							</tr>
							<tr>
								<td colspan=4 style="font-size: 14px;">В этом меню пользователь увидит все полученные им с помощью этой карты подарки</td>
							</tr>
						</table>
					<% } else if ("4".equalsIgnoreCase(cardpage)) { %>
					
						<table <%=Bean.getTableBottomFilter() %>>
						  <tr><td><b><%=cd_card1 %></b></td></tr>
						  <tr>
							<%= Bean.getFindHTML("find_oper", find_oper, "card/card_param.jsp?id=" + cardid + "&iss=" + iss + "&paysys=" + paysys + "&trans_page=1&cardpage=" + cardpage + "&") %>
							<%= Bean.getPagesHTML(pageFormName + tagOper, "card/card_param.jsp?id=" + cardid + "&iss=" + iss + "&paysys=" + paysys + "&cardpage=" + cardpage + "&", "oper_page") %>
						  </tr>
						</table>
						<%=clubcard.getWEBClientCardOperationHTML(find_oper, l_oper_page_beg, l_oper_page_end) %>

					<%}  %>
					</div>
				</div>
			</td>
		</tr>
	</table>
</body>
</html>