<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcCardSettingObject"%>
<%@page import="java.util.HashMap"%><html>
<%= Bean.getLogOutScript(request) %>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>
<body>
<div id="div_tabsheet">
<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CARDS_CARDSETTING";
String tagCategory = "_CATEGORY";
String tagCategoryFind = "_CATEGORY_FIND";

String tagCardPackage = "_CARD_PACKAGE";
String tagCardPackageFind = "_CARD_PACKAGE_FIND";

Bean.setJspPageForTabName(pageFormName);


String id = Bean.getDecodeParam(parameters.get("id"));
String id_club = Bean.getDecodeParam(parameters.get("id_club"));
String	tab = Bean.getDecodeParam(parameters.get("tab"));
tab = Bean.isEmpty(tab)?Bean.tabsHmGetValue(pageFormName):tab;
if (Bean.isEmpty(id)) {
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
} else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcCardSettingObject cardset = new bcCardSettingObject(id, id_club);
	
	//Обрабатываем номера страниц
	String l_category_page = Bean.getDecodeParam(parameters.get("category_page"));
	Bean.pageCheck(pageFormName + tagCategory, l_category_page);
	String l_category_page_beg = Bean.getFirstRowNumber(pageFormName + tagCategory);
	String l_category_page_end = Bean.getLastRowNumber(pageFormName + tagCategory);
	
	String category_find 	= Bean.getDecodeParam(parameters.get("category_find"));
	category_find 	= Bean.checkFindString(pageFormName + tagCategoryFind, category_find, l_category_page);
	
	String l_card_pack_page = Bean.getDecodeParam(parameters.get("card_pack_page"));
	Bean.pageCheck(pageFormName + tagCardPackage, l_card_pack_page);
	String l_card_pack_page_beg = Bean.getFirstRowNumber(pageFormName + tagCardPackage);
	String l_card_pack_page_end = Bean.getLastRowNumber(pageFormName + tagCardPackage);

	String card_pack_find 	= Bean.getDecodeParam(parameters.get("card_pack_find"));
	card_pack_find 	= Bean.checkFindString(pageFormName + tagCardPackageFind, card_pack_find, l_card_pack_page);
	
	
%>

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>
			
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_CARDSETTINGS_CATEGORIES")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CARDS_CARDSETTINGS_CATEGORIES")) {%>
				    <%= Bean.getMenuButton("ADD", "../crm/cards/cardsettingupdate.jsp?id=" + id + "&id_club=" + id_club + "&type=category&action=add2&process=no", "", "") %>
				<% } %>

			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagCategory, "../crm/cards/cardsettingspecs.jsp?id=" + id + "&id_club=" + id_club + "&tab="+Bean.currentMenu.getTabID("CARDS_CARDSETTINGS_CATEGORIES")+"&", "category_page") %>
			<% }%>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_CARDSETTINGS_CARD_PACKAGES")) { %>
			    <% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CARDS_CARDSETTINGS_CARD_PACKAGES")) { %>
				    <%= Bean.getMenuButtonBase("ADD", "../crm/club/card_packageupdate.jsp?back_type=CARD_SETTING&id=" + id + "&id_club=" + id_club + "&id_card_status=" + id + "&type=general&action=add&process=no", "", "", "", "div_data_detail") %>
				<%  } %>
				<!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagCardPackage, "../crm/cards/cardsettingspecs.jsp?id=" + id + "&id_club=" + id_club + "&tab="+Bean.currentMenu.getTabID("CARDS_CARDSETTINGS_CARD_PACKAGES")+"&", "card_pack_page") %>
			<% } %>

		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(cardset.getValue("NAME_CARD_STATUS")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/cards/cardsettingspecs.jsp?id=" + id + "&id_club=" + id_club) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_CARDSETTINGS_INFO")) {
%>
	
		<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CARDS_CARDSETTINGS_INFO")) { %>
		<script>
			var formData = new Array (
				new Array ('telgr_name_card_status', 'varchar2', 1),
				new Array ('day_next_online', 'varchar2', 1)
			);
		</script>

		  <form action="../crm/cards/cardsettingupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onsubmit="return confirmUpdate();">
			<input type="hidden" name="action" value="edit">
	    	<input type="hidden" name="process" value="yes">
	    	<input type="hidden" name="type" value="general">
	    	<input type="hidden" name="id" value="<%= id %>">
	    	<input type="hidden" name="id_club" value="<%= id_club %>">
		<%} %>

		<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CARDS_CARDSETTINGS_INFO")) {%>
		<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.cardsettingXML.getfieldTransl("name_card_status", false) %> </td><td><input type="text" name="name_card_status" size="30" value="<%= cardset.getValue("NAME_CARD_STATUS") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(cardset.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(cardset.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.cardsettingXML.getfieldTransl("telgr_name_card_status", true) %></td><td><input type="text" name="telgr_name_card_status" size="30" value="<%= cardset.getValue("TELGR_NAME_CARD_STATUS") %>" class="inputfield"></td>
			<td><%= Bean.cardsettingXML.getfieldTransl("categories_count", false) %> </td><td><input type="text" name="categories_count" size="30" value="<%= cardset.getValue("CATEGORIES_COUNT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.cardsettingXML.getfieldTransl("day_next_online", true) %></td><td><input type="text" name="day_next_online" size="30" value="<%= cardset.getValue("DAY_NEXT_ONLINE") %>" class="inputfield"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				cardset.getValue("ID_CARD_STATUS"),
				cardset.getValue(Bean.getCreationDateFieldName()),
				cardset.getValue("CREATED_BY"),
				cardset.getValue(Bean.getLastUpdateDateFieldName()),
				cardset.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/cards/cardsettingupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/cards/cardsetting.jsp") %>
			</td>
		</tr>
		</table>
		<%} else {%>
		<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.cardsettingXML.getfieldTransl("name_card_status", false) %> </td><td><input type="text" name="name_card_status" size="30" value="<%= cardset.getValue("NAME_CARD_STATUS") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(cardset.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(cardset.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.cardsettingXML.getfieldTransl("telgr_name_card_status", true) %></td><td><input type="text" name="telgr_name_card_status" size="30" value="<%= cardset.getValue("TELGR_NAME_CARD_STATUS") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.cardsettingXML.getfieldTransl("categories_count", false) %> </td><td><input type="text" name="categories_count" size="30" value="<%= cardset.getValue("CATEGORIES_COUNT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.cardsettingXML.getfieldTransl("day_next_online", true) %></td><td><input type="text" name="day_next_online" size="30" value="<%= cardset.getValue("DAY_NEXT_ONLINE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				cardset.getValue("ID_CARD_STATUS"),
				cardset.getValue(Bean.getCreationDateFieldName()),
				cardset.getValue("CREATED_BY"),
				cardset.getValue(Bean.getLastUpdateDateFieldName()),
				cardset.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getGoBackButton("../crm/cards/cardsetting.jsp") %>
			</td>
		</tr>
		</table>
		<%} %>

	</form>

<% }
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_CARDSETTINGS_CATEGORIES")) { %>

	<table <%=Bean.getTableBottomFilter() %>>
	  <tr>
		<%= Bean.getFindHTML("category_find", category_find, "../crm/cards/cardsettingspecs.jsp?id=" + id + "&id_club=" + id_club + "&category_page=1&") %>

		<td>&nbsp;</td>

	  </tr>
	</table>

	<%=cardset.getCategoriesHTML(category_find, l_category_page_beg, l_category_page_end)%> 
<% }

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_CARDSETTINGS_CARD_PACKAGES")) {%>
<table <%=Bean.getTableBottomFilter() %>>
  <tr>
	<%= Bean.getFindHTML("card_pack_find", card_pack_find, "../crm/cards/cardsettingspecs.jsp?id=" + id + "&id_club=" + id_club + "&card_pack_page=1&") %>

  </tr>
</table>
	<%= cardset.getCardPackagesHTML(card_pack_find, l_card_pack_page_beg, l_card_pack_page_end) %>
<%}

}
		
%>
</div></div>
</body>
</html>
