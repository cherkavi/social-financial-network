<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebClientBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.wcClubCardObject"%>
<%@page import="bc.objects.wcNatPrsObject"%><html>
<%

request.setCharacterEncoding("UTF-8");

String pageFormName = "WEBCLIENT_PROFILE_PARAM";

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String profilepage = Bean.getDecodeParam(parameters.get("profilepage"));

wcNatPrsObject natprs = new wcNatPrsObject(Bean.getLoginUserIDNatPrs());

%>
<body>
<table <%=Bean.getTableDetail2Param() %>>
		<tr>
			<td>
				<div id="div_oper_list">
					<div class="div_oper_group">Профиль</div>
					<div class="div_oper" onclick="ajaxpage('profile/profile.jsp?profilepage=1', 'div_main')">Параметры клиента</div>
					<div class="div_oper" onclick="ajaxpage('profile/profile.jsp?profilepage=2', 'div_main')">Корзина</div>
					<div class="div_oper" onclick="ajaxpage('profile/profile.jsp?profilepage=3', 'div_main')">Подарки</div>
					<div class="div_oper" onclick="ajaxpage('profile/profile.jsp?profilepage=4', 'div_main')">Сообщения</div>
				</div>
			</td>
			<td>
				<div id="div_oper_param">
					<%if ("1".equalsIgnoreCase(profilepage)) { %>
					<table class="tablebottom-filter"> 
						<thead>
						<tr>
							<th colspan=4>Параметры клиента</td>
						</tr>
						</thead>
						<tbody>
						<tr>
							<td colspan=4>&nbsp;</td>
						</tr>
						<tr>
							<td><%= Bean.natprsXML.getfieldTransl("surname", false) %></td><td><input type="text" name="surname" size="30" value="<%= natprs.getValue("SURNAME") %>" readonly class="inputfield-ro"></td>
							<td><%= Bean.natprsXML.getfieldTransl("date_of_birth", false) %> </td><td><input type="text" name="date_of_birth" size="10" value="<%= natprs.getValue("DATE_OF_BIRTH_FRMT") %>" readonly class="inputfield-ro"></td>
						</tr>
						<tr>
							<td><%= Bean.natprsXML.getfieldTransl("name", false) %> </td><td><input type="text" name="name" size="30" value="<%= natprs.getValue("NAME") %>" readonly class="inputfield-ro"></td>
							<td><%= Bean.natprsXML.getfieldTransl("sex", false) %></td><td><input type="text" name="sex" size="30" value="<%= natprs.getValue("SEX_TSL") %>" readonly class="inputfield-ro"></td>
						</tr>
						<tr>
							<td><%= Bean.natprsXML.getfieldTransl("patronymic", false) %></td><td><input type="text" name="patronymic" size="30" value="<%= natprs.getValue("PATRONYMIC") %>" readonly class="inputfield-ro"></td>
							<td colspan=2>&nbsp;</td>
						</tr>
						<tr>
							<td colspan=4 class="top_line"><b><%= Bean.natprsXML.getfieldTransl("FACT_ADR_FULL", false) %></b></td>
						</tr>
						<tr>
							<td><%= Bean.natprsXML.getfieldTransl("code_country", false) %></td><td><input type="text" name="fact_code_country" size="30" value="<%= natprs.getValue("FACT_NAME_COUNTRY") %>" readonly class="inputfield-ro"></td>
							<td><%= Bean.natprsXML.getfieldTransl("fact_adr_city", false) %></td><td><input type="text" name="fact_adr_city" size="30" value="<%= natprs.getValue("FACT_ADR_CITY") %>" readonly class="inputfield-ro"></td>
						</tr>
						<tr>
							<td><%= Bean.natprsXML.getfieldTransl("fact_adr_zip_code", false) %></td><td><input type="text" name="fact_adr_zip_code" size="30" value="<%= natprs.getValue("FACT_ADR_ZIP_CODE") %>" readonly class="inputfield-ro"></td>
							<td><%= Bean.natprsXML.getfieldTransl("fact_adr_street", false) %> </td><td><input type="text" name="fact_adr_street" size="30" value="<%= natprs.getValue("FACT_ADR_STREET") %>" readonly class="inputfield-ro"></td>
						</tr>
						<tr>
							<td><%= Bean.natprsXML.getfieldTransl("fact_adr_oblast", false) %> </td><td><input type="text" name="fact_adr_oblast" size="30" value="<%= natprs.getValue("FACT_ADR_NAME_OBLAST") %>" readonly class="inputfield-ro"></td>
							<td><%= Bean.natprsXML.getfieldTransl("fact_adr_house", false) %>/<%= Bean.natprsXML.getfieldTransl("fact_adr_case", false) %></td>
							<td>
								<input type="text" name="fact_adr_house" size="10" value="<%= natprs.getValue("FACT_ADR_HOUSE") %>" readonly class="inputfield-ro">
								/&nbsp;<input type="text" name="fact_adr_case" size="10" value="<%= natprs.getValue("FACT_ADR_CASE") %>" readonly class="inputfield-ro">
							</td>
						</tr>
						<tr>
							<td><%= Bean.natprsXML.getfieldTransl("fact_adr_district", false) %></td><td><input type="text" name="fact_adr_district" size="30" value="<%= natprs.getValue("FACT_ADR_DISTRICT") %>" readonly class="inputfield-ro"></td>
							<td><%= Bean.natprsXML.getfieldTransl("fact_adr_apartment", false) %></td>
							<td>
								<input type="text" name="fact_adr_apartment" size="10" value="<%= natprs.getValue("FACT_ADR_APARTMENT") %>" readonly class="inputfield-ro">
							</td>
						</tr>
						<tr>
							<td colspan=4 class="top_line"><b><%= Bean.natprsXML.getfieldTransl("h_contact_information", false) %></b></td>
						</tr>
						<tr>
							<td><%= Bean.natprsXML.getfieldTransl("phone_work", false) %> </td><td><input type="text" name="phone_work" size="30" value="<%= natprs.getValue("PHONE_WORK") %>" readonly class="inputfield-ro"></td>
							<td><%= Bean.natprsXML.getfieldTransl("phone_mobile", false) %></td><td><input type="text" name="phone_mobile" size="30" value="<%= natprs.getValue("PHONE_MOBILE") %>" readonly class="inputfield-ro"></td>
						</tr>
						<tr>
							<td><%= Bean.natprsXML.getfieldTransl("phone_home", false) %></td><td><input type="text" name="phone_home" size="30" value="<%= natprs.getValue("PHONE_HOME") %>" readonly class="inputfield-ro"></td>
							<td><%= Bean.natprsXML.getfieldTransl("email", false) %> </td><td><input type="text" name="email" size="30" value="<%= natprs.getValue("EMAIL") %>" readonly class="inputfield-ro"></td>
						</tr>
						</tbody>
					</table>
					<% } else if ("2".equalsIgnoreCase(profilepage)) {  %>
					<table class="tablebottom-filter"> 
						<thead>
						<tr>
							<th colspan=4>Корзина</td>
						</tr>
						</thead>
						<tbody>
						<tr>
							<td colspan=4>&nbsp;</td>
						</tr>
						<tr>
							<td colspan=4 style="font-size: 14px;font-weight: bold;">Заглушка!<br><br></td>
						</tr>
						<tr>
							<td colspan=4 style="font-size: 14px;">В этом меню пользователь увидит все свои заказанные товары в магазине по льготным ценам для учасников СМУР</td>
						</tr>
						</tbody>
					</table>
					<% } else if ("3".equalsIgnoreCase(profilepage)) {  %>
					<table class="tablebottom-filter"> 
						<thead>
						<tr>
							<th colspan=4>Подарки</td>
						</tr>
						</thead>
						<tbody>
						<tr>
							<td colspan=4>&nbsp;</td>
						</tr>
						<tr>
							<td colspan=4 style="font-size: 14px;font-weight: bold;">Заглушка!<br><br></td>
						</tr>
						<tr>
							<td colspan=4 style="font-size: 14px;">В этом меню пользователь увидит все полученные им подарки</td>
						</tr>
						</tbody>
					</table>
					<% } else if ("4".equalsIgnoreCase(profilepage)) {  
				    	
				    	%><table class="tablebottom-filter"> 
						<thead>
						<tr>
							<th>Личные сообщения</td>
						</tr>
						</thead>
						</table><br>
				    	<%=natprs.getNatPersonMessagesHTML("", "", "1", "100") %>
					<% } %>
				</div>
			</td>
		</tr>
	</table>
</body>
</html>