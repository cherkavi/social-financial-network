package bc.objects;

public class bcPostingEditObject extends bcObject {
    
	
	public bcPostingEditObject() {
	}

    public String getBKDocTypeOptions(String cd_type, boolean isNull) {
    	return getSelectBodyFromQuery(
   			" SELECT cd_bk_doc_type, name_bk_doc_type " +
   			"   FROM " + getGeneralDBScheme()+".vc_bk_doc_type " +
   			"  ORDER BY name_bk_doc_type", cd_type, isNull);
    }
    
    public String getBKDocTypeName (String cd_bk_doc_type) {
    	return getOneValueByStringId("SELECT name_bk_doc_type FROM " + getGeneralDBScheme()+".vc_bk_doc_type WHERE cd_bk_doc_type = ? ", cd_bk_doc_type);
    }

	public String getPostingEditHTML(String pIdPostingDetail, String pDateFormatTitle) {
		StringBuilder html = new StringBuilder();
		
		bcPostingDetailObject posting = new bcPostingDetailObject(pIdPostingDetail);
		
		html.append("<tr>\n");
		html.append("<td>" + postingXML.getfieldTransl("id_posting_detail", false) + "</td><td><input type=\"text\" name=\"id_posting_detail\" size=\"15\" value=\"" + posting.getValue("ID_POSTING_DETAIL") + "\" readonly class=\"inputfield-ro\"></td>\n");
		html.append("<td>" + postingXML.getfieldTransl("operation_date", true) + "</td><td>");
		html.append(getCalendarInputField("operation_date", posting.getValue("OPERATION_DATE_FRMT"), "10"));
		html.append("</td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td>" + postingXML.getfieldTransl("debet_id_bk_account", true));
		html.append(getGoToHyperLink(
				"FINANCE_BK_ACCOUNTS",
				posting.getValue("DEBET_ID_BK_ACCOUNT"),
				"../crm/finance/bk_accountspecs.jsp?id=" + posting.getValue("DEBET_ID_BK_ACCOUNT")
			));
		html.append("</td>\n");
		html.append("<td>\n");
		html.append(getWindowFindBKAccount("debet", posting.getValue("DEBET_ID_BK_ACCOUNT"), "70"));
		html.append("</td>\n");
		html.append("<td>" + postingXML.getfieldTransl("name_currency", true) + "</td> <td><select name=\"cd_currency\" class=\"inputfield\">" + getCurrencyOptions(posting.getValue("CD_CURRENCY"), true) + "</select> </td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td>" + postingXML.getfieldTransl("credit_id_bk_account", true));
		html.append(getGoToHyperLink(
				"FINANCE_BK_ACCOUNTS",
				posting.getValue("CREDIT_ID_BK_ACCOUNT"),
				"../crm/finance/bk_accountspecs.jsp?id=" + posting.getValue("CREDIT_ID_BK_ACCOUNT")
			));
		html.append("</td>\n");
		html.append("<td>\n");
		html.append(getWindowFindBKAccount("credit", posting.getValue("CREDIT_ID_BK_ACCOUNT"), "70"));
		html.append("</td>\n");
		html.append("<td>" + postingXML.getfieldTransl("entered_amount", true)  + "</td> <td><input type=\"text\" name=\"entered_amount\" size=\"16\" value=\"" + posting.getValue("ENTERED_AMOUNT_FRMT") + "\" class=\"inputfield\"> </td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td rowspan=\"3\" valign=\"top\">" + postingXML.getfieldTransl("assignment_posting", false)  + "</td><td rowspan=\"3\"><textarea name=\"assignment_posting\" cols=\"80\" rows=\"4\" class=\"inputfield\">" + posting.getValue("ASSIGNMENT_POSTING") + "</textarea></td>\n");
		html.append("<td colspan=\"2\" class=\"top_line\"><b>" + postingXML.getfieldTransl("h_to_base", false) + "</b></td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td>" + postingXML.getfieldTransl("base_currency", false)  + "</td> <td><select name=\"base_currency\" class=\"inputfield\">" + getCurrencyOptions(posting.getValue("BASE_CURRENCY"), true) + "</select></td>\n");
		html.append("</tr>\n");
		html.append("<tr>");
		html.append("<td>" + postingXML.getfieldTransl("exchange_rate", false)  + "</td> <td><input type=\"text\" name=\"exchange_rate\" size=\"16\" value=\"" + posting.getValue("EXCHANGE_RATE_FRMT") + "\" class=\"inputfield\"> </td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td>" + postingXML.getfieldTransl("id_bk_operation_scheme_line", false));
		html.append(getGoToHyperLink(
				"FINANCE_POSTING_SCHEME",
				posting.getValue("ID_BK_OPERATION_SCHEME_LINE"),
				"../crm/finance/posting_scheme_linespecs.jsp?id=" + posting.getValue("ID_BK_OPERATION_SCHEME_LINE")
			));
		html.append("</td>\n");
		html.append("<td>\n");
		html.append(getWindowFindBKOperationScheme("bk_operation_scheme_line", posting.getValue("ID_BK_OPERATION_SCHEME_LINE"), "70"));
		html.append("</td>\n");
		html.append("<td>" + postingXML.getfieldTransl("conversion_date", false)  + "</td><td>");
		html.append(getCalendarInputField("conversion_date", posting.getValue("CONVERSION_DATE_FRMT"), "10"));
		html.append("</td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td>" + postingXML.getfieldTransl("id_club_rel", false));
		html.append(getGoToHyperLink(
				"CLUB_RELATIONSHIP",
				posting.getValue("ID_CLUB_REL"),
				"../crm/club/relationshipspecs.jsp?id=" + posting.getValue("ID_CLUB_REL")
			));
		html.append("</td>\n");
		html.append("<td>\n"); 
		html.append(getWindowFindClurRelationship("club_rel", posting.getValue("ID_CLUB_REL"), "", "70"));
		html.append("</td>\n");
		html.append("<td>" + postingXML.getfieldTransl("accounted_amount", false)  + "</td> <td><input type=\"text\" name=\"accounted_amount\" size=\"16\" value=\"" + posting.getValue("ACCOUNTED_AMOUNT_FRMT") + "\" class=\"inputfield\"> </td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<tr>\n");
		html.append("<td>" + postingXML.getfieldTransl("rejection_state", false)  + "</td> <td><input type=\"text\" name=\"rejection_state\" size=\"16\" value=\"" + posting.getValue("REJECTION_STATE_TSL") + "\" readonly class=\"inputfield-ro\"> </td>\n");
		html.append("<td colspan=2>&nbsp;</td>\n");
		html.append("</tr>\n");
		html.append("<td colspan=\"2\" class=\"top_line\"><b>" + postingXML.getfieldTransl("h_grouping", false) + "</b></td>\n");
		html.append("<td colspan=\"2\" class=\"top_line\"><b>" + postingXML.getfieldTransl("h_source", false) + "</b></td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td>" + posting_schemeXML.getfieldTransl("run_postings_export", true) + "</td> <td><select name=\"run_postings_export\" id=\"run_postings_export\" class=\"inputfield\" onchange=\"checkBKDocType()\">" + getMeaningFromLookupNameOptions("YES_NO", posting.getValue("RUN_POSTINGS_EXPORT"), false) + "</select> </td>\n");
		html.append("<td>" + postingXML.getfieldTransl("id_trans", false));
		html.append(getGoToHyperLink(
				"CARDS_TRANSACTIONS",
				posting.getValue("ID_TRANS"),
				"../crm/cards/transactionspecs.jsp?id=" + posting.getValue("ID_TRANS")
			));
		html.append("</td><td><input type=\"text\" name=\"id_trans\" size=\"16\" value=\"" + posting.getValue("ID_TRANS") + "\" readonly class=\"inputfield-ro\"></td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td>" + postingXML.getfieldTransl("is_grouped", false)  + "</td><td><input type=\"text\" name=\"is_grouped\" size=\"15\" value=\"" + posting.getValue("IS_GROUPED_TSL") + "\" readonly class=\"inputfield-ro\"> </td>\n");
		html.append("<td>" + postingXML.getfieldTransl("id_quest_int", false));
		html.append(getGoToHyperLink(
				"CARDS_QUESTIONNAIRE_IMPORT",
				posting.getValue("ID_QUEST_INT"),
				"../crm/cards/questionnaire_importspecs.jsp?id=" + posting.getValue("ID_QUEST_INT")
			));
		html.append("</td><td><input type=\"text\" name=\"id_quest_int\" size=\"16\" value=\"" + posting.getValue("ID_QUEST_INT") + "\" readonly class=\"inputfield-ro\"></td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td><span id=\"span_cd_bk_doc_type\">" + posting_schemeXML.getfieldTransl("cd_bk_doc_type", false)  + "</span></td><td><select name=\"cd_bk_doc_type\" id=\"cd_bk_doc_type\" class=\"inputfield\">" + getBKDocTypeOptions(posting.getValue("CD_BK_DOC_TYPE"),true) + "</select></td>\n");
		html.append("<td>" + postingXML.getfieldTransl("id_remittance", false));
		html.append(getGoToHyperLink(
				"FINANCE_REMITTANCE",
				posting.getValue("ID_REMITTANCE"),
				"../crm/finance/remittancespecs.jsp?id=" + posting.getValue("ID_REMITTANCE")
			));
		html.append("</td> <td><input type=\"text\" name=\"id_remittance\" size=\"16\" value=\"" + posting.getValue("ID_REMITTANCE") + "\" readonly class=\"inputfield-ro\"> </td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td>" + postingXML.getfieldTransl("id_posting_line", false));
		html.append(getGoToHyperLink(
				"FINANCE_ACCOUNTING_DOC",
				posting.getValue("ID_POSTING_LINE"),
				"../crm/finance/accounting_docspecs.jsp?type=posting&id_posting=" + posting.getValue("ID_POSTING_LINE")
			));
		html.append("</td><td><input type=\"text\" name=\"id_posting_line\" size=\"15\" value=\"" + posting.getValue("ID_POSTING_LINE") + "\" readonly class=\"inputfield-ro\"> </td>\n");
		html.append("<td>" + postingXML.getfieldTransl("id_card_operation", false));
		html.append(getGoToHyperLink(
				"CARDS_CARD_TASKS",
				posting.getValue("ID_CARD_OPERATION"),
				"../crm/cards/card_taskspecs.jsp?id=" + posting.getValue("ID_CARD_OPERATION")
			));
		html.append("</td> <td><input type=\"text\" name=\"id_card_operation\" size=\"16\" value=\"" + posting.getValue("ID_CARD_OPERATION") + "\" readonly class=\"inputfield-ro\"> </td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td colspan=2> </td>\n");
		html.append("<td>" + postingXML.getfieldTransl("id_bank_statement_line", false));
		html.append(getGoToHyperLink(
				"FINANCE_BANKSTATEMENT",
				posting.getValue("ID_BANK_STATEMENT_LINE"),
				"../crm/finance/bankstatement_linespecs.jsp?id=" + posting.getValue("ID_BANK_STATEMENT_LINE")
			));
		html.append("</td> <td><input type=\"text\" name=\"id_bank_statement_line\" size=\"16\" value=\"" + posting.getValue("ID_BANK_STATEMENT_LINE") + "\" readonly class=\"inputfield-ro\"> </td>\n");
		html.append("</tr>\n");

		html.append("<tr>\n");
		html.append("<td colspan=\"2\" class=\"top_line\"><b>" + postingXML.getfieldTransl("h_clearing", false) + "</b></td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td>" + posting_schemeXML.getfieldTransl("using_in_clearing", true) + "</td> <td><select name=\"using_in_clearing\" id=\"using_in_clearing\" onchange=\"checkClearing();\" class=\"inputfield\">" + getMeaningFromLookupNameOptions("YES_NO", posting.getValue("USING_IN_CLEARING"), false) + "</select> </td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td>" + postingXML.getfieldTransl("is_clearing", false)  + "</td><td><input type=\"text\" name=\"is_clearing\" size=\"16\" value=\"" + posting.getValue("IS_CLEARING_TSL") + "\" readonly class=\"inputfield-ro\"> </td>\n");
		html.append("</tr>\n");

		html.append("<tr>\n");
		html.append("<td>" + postingXML.getfieldTransl("id_clearing_line", false));
		html.append(getGoToHyperLink(
				"FINANCE_CLEARING",
				posting.getValue("ID_CLEARING_LINE"),
				"../crm/finance/clearing_linespecs.jsp?id=" + posting.getValue("ID_CLEARING_LINE")
			));
		html.append("<td> <input type=\"text\" name=\"id_clearing_line\" size=\"16\" value=\"" + posting.getValue("ID_CLEARING_LINE") + "\" readonly class=\"inputfield-ro\"> </td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td><span id=\"span_receiver_id_bank_account\">" + postingXML.getfieldTransl("receiver_id_bank_account", false) + "</span>");
		html.append(getGoToHyperLink(
				"CLIENTS_BANK_ACCOUNTS",
				posting.getValue("RECEIVER_ID_BANK_ACCOUNT"),
				"../crm/clients/accountspecs.jsp?id=" + posting.getValue("RECEIVER_ID_BANK_ACCOUNT")
			));
		html.append("</td>\n");
		html.append("<td>\n");
		html.append(getWindowFindBankAccount2("bank_account_debet", posting.getValue("RECEIVER_ID_BANK_ACCOUNT"), "70"));
		html.append("</td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td><span id=\"span_payer_id_bank_account\">" + postingXML.getfieldTransl("payer_id_bank_account", false) + "</span>");
		html.append(getGoToHyperLink(
				"CLIENTS_BANK_ACCOUNTS",
				posting.getValue("PAYER_ID_BANK_ACCOUNT"),
				"../crm/clients/accountspecs.jsp?id=" + posting.getValue("PAYER_ID_BANK_ACCOUNT")
			));
		html.append("</td>\n");
		html.append("<td>\n");
		html.append(getWindowFindBankAccount2("bank_account_credit", posting.getValue("PAYER_ID_BANK_ACCOUNT"), "70"));
		html.append("</td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td valign=\"top\">" + relationshipXML.getfieldTransl("payment_function", false)  + "</td><td><textarea name=\"payment_function\" cols=\"80\" rows=\"3\" class=\"inputfield\">" + posting.getValue("PAYMENT_FUNCTION") + "</textarea></td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td colspan=4 class=\"top_line\"><b>" + commonXML.getfieldTransl("h_record_param", false) + "</b></td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td>" + commonXML.getfieldTransl("creation_date", false) + "</td> <td><input type=\"text\" name=\"creation_date\" size=\"20\" value=\"" + posting.getValue("CREATION_DATE_DHMSF") + "\" readonly class=\"inputfield-ro\"> </td>\n");
		html.append("<td>" + commonXML.getfieldTransl("last_update_date",false) + "</td> <td><input type=\"text\" name=\"last_update_date\" size=\"20\" value=\"" + posting.getValue("LAST_UPDATE_DATE_DHMSF") + "\" readonly class=\"inputfield-ro\"> </td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td>" + commonXML.getfieldTransl("created_by", false) + "</td> <td><input type=\"text\" name=\"created_by\" size=\"20\" value=\"" + getSystemUserName(posting.getValue("CREATED_BY")) + "\" readonly class=\"inputfield-ro\"> </td>\n");
		html.append("<td>" + commonXML.getfieldTransl("last_update_by", false) + "</td> <td><input type=\"text\" name=\"last_update_by\" size=\"20\" value=\"" + getSystemUserName(posting.getValue("LAST_UPDATE_BY")) + "\" readonly class=\"inputfield-ro\"> </td>\n");
		html.append("</tr>\n");
		
		return html.toString();
	}
	
	public String getPostingPreviewHTML(String pIdPostingDetail) {
		StringBuilder html = new StringBuilder();
		
		bcPostingDetailObject posting = new bcPostingDetailObject(pIdPostingDetail);
		
		html.append("<tr>\n");
		html.append("<td>" + postingXML.getfieldTransl("id_posting_detail", false) + "</td><td><input type=\"text\" name=\"id_posting_detail\" size=\"15\" value=\"" + posting.getValue("ID_POSTING_DETAIL") + "\" readonly class=\"inputfield-ro\"></td>\n");
		html.append("<td>" + postingXML.getfieldTransl("operation_date", false) + "</td> <td><input type=\"text\" name=\"operation_date\" size=\"16\" value=\"" + posting.getValue("OPERATION_DATE_FRMT") + "\" readonly class=\"inputfield-ro\"> </td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td>" + postingXML.getfieldTransl("debet_id_bk_account", false));
		html.append(getGoToHyperLink(
				"FINANCE_BK_ACCOUNTS",
				posting.getValue("DEBET_ID_BK_ACCOUNT"),
				"../crm/finance/bk_accountspecs.jsp?id=" + posting.getValue("DEBET_ID_BK_ACCOUNT")
			));
		html.append("</td>\n");
		html.append("<td>\n");
		html.append("<input type=\"text\" name=\"debet_name_bk_account\" size=\"70\" value=\"" + getBKAccountName(posting.getValue("DEBET_ID_BK_ACCOUNT"))+ "\" readonly class=\"inputfield-ro\">\n");
		html.append("</td>\n");
		html.append("<td>" + postingXML.getfieldTransl("name_currency", false)+ "</td> <td><input type=\"text\" name=\"name_currency\" size=\"16\" value=\"" + getCurrencyNameById(posting.getValue("CD_CURRENCY"))+ "\" readonly class=\"inputfield-ro\"> </td>");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td>" + postingXML.getfieldTransl("credit_id_bk_account", false));
		html.append(getGoToHyperLink(
				"FINANCE_BK_ACCOUNTS",
				posting.getValue("CREDIT_ID_BK_ACCOUNT"),
				"../crm/finance/bk_accountspecs.jsp?id=" + posting.getValue("CREDIT_ID_BK_ACCOUNT")
			));
		html.append("</td>\n");
		html.append("<td>\n");
		html.append("<input type=\"text\" name=\"credit_name_bk_account\" size=\"70\" value=\"" + getBKAccountName(posting.getValue("CREDIT_ID_BK_ACCOUNT"))+ "\" readonly class=\"inputfield-ro\">\n");
		html.append("</td>\n");
		html.append("<td>" + postingXML.getfieldTransl("entered_amount", false)+ "</td> <td><input type=\"text\" name=\"entered_amount\" size=\"16\" value=\"" + posting.getValue("ENTERED_AMOUNT_FRMT") + "\" readonly class=\"inputfield-ro\"> </td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td rowspan=\"3\" valign=\"top\">" + postingXML.getfieldTransl("assignment_posting", false)+ "</td><td rowspan=\"3\"><textarea name=\"assignment_posting\" cols=\"67\" rows=\"4\" readonly class=\"inputfield-ro\">" +  posting.getValue("ASSIGNMENT_POSTING") + "</textarea></td>\n");
		html.append("<td colspan=\"2\" class=\"top_line\"><b>" + postingXML.getfieldTransl("h_to_base", false) + "</b></td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td>" + postingXML.getfieldTransl("base_currency", false)+ "</td> <td><input type=\"text\" name=\"base_currency\" size=\"16\" value=\"" + getCurrencyNameById(posting.getValue("BASE_CURRENCY"))+ "\" readonly class=\"inputfield-ro\"></td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td>" + postingXML.getfieldTransl("exchange_rate", false)+ "</td> <td><input type=\"text\" name=\"exchange_rate\" size=\"16\" value=\"" + posting.getValue("EXCHANGE_RATE_FRMT") + "\" readonly class=\"inputfield-ro\"> </td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td>" + postingXML.getfieldTransl("id_bk_operation_scheme_line", false));
		html.append(getGoToHyperLink(
				"FINANCE_POSTING_SCHEME",
				posting.getValue("ID_BK_OPERATION_SCHEME_LINE"),
				"../crm/finance/posting_scheme_linespecs.jsp?id=" + posting.getValue("ID_BK_OPERATION_SCHEME_LINE")
			));
		html.append("</td>\n");
		html.append("<td>\n");
		html.append("<input type=\"text\" name=\"name_bk_operation_scheme_line\" id=\"name_bk_operation_scheme_line\" size=\"70\" value=\"" + getBKOperationSchemeLineAdditionalName(posting.getValue("ID_BK_OPERATION_SCHEME_LINE")) + "\" readonly class=\"inputfield-ro\">\n");
		html.append("</td>\n");
		html.append("<td>" + postingXML.getfieldTransl("conversion_date", false)+ "</td> <td><input type=\"text\" name=\"conversion_date\" size=\"16\" value=\"" + posting.getValue("CONVERSION_DATE_FRMT") + "\" readonly class=\"inputfield-ro\"> </td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td>" + postingXML.getfieldTransl("id_club_rel", false));
		html.append(getGoToHyperLink(
				"CLUB_RELATIONSHIP",
				posting.getValue("ID_CLUB_REL"),
				"../crm/club/relationshipspecs.jsp?id=" + posting.getValue("ID_CLUB_REL")
			));
		html.append("</td>\n");
		html.append("<td>\n"); 
		html.append("<input type=\"text\" id=\"name_club_rel\" name=\"name_club_rel\" size=\"70\" value=\"" + getClubRelationshipName(posting.getValue("ID_CLUB_REL")) + "\" readonly class=\"inputfield-ro\">\n");
		html.append("</td>\n");
		html.append("<td>" + postingXML.getfieldTransl("accounted_amount", false) + "</td> <td><input type=\"text\" name=\"accounted_amount\" size=\"16\" value=\"" + posting.getValue("ACCOUNTED_AMOUNT_FRMT") + "\" readonly class=\"inputfield-ro\"> </td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td>" + postingXML.getfieldTransl("rejection_state", false) + "</td> <td><input type=\"text\" name=\"rejection_state\" size=\"16\" value=\"" + posting.getValue("REJECTION_STATE_TSL") + "\" readonly class=\"inputfield-ro\"> </td>\n");
		html.append("<td colspan=2>&nbsp;</td>\n");
		html.append("</tr>\n");

		html.append("<tr>\n");
		html.append("<td colspan=\"2\" class=\"top_line\"><b>" + postingXML.getfieldTransl("h_grouping", false) + "</b></td>\n");
		html.append("<td colspan=\"2\" class=\"top_line\"><b>" + postingXML.getfieldTransl("h_source", false) + "</b></td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td>" + posting_schemeXML.getfieldTransl("run_postings_export", false) + "</td><td><input type=\"text\" name=\"run_postings_export\" size=\"16\" value=\"" + getMeaningFoCodeValue("YES_NO", posting.getValue("RUN_POSTINGS_EXPORT"))+ "\" readonly class=\"inputfield-ro\"> </td>\n");
		html.append("<td>" + postingXML.getfieldTransl("id_trans", false));
		html.append(getGoToHyperLink(
				"CARDS_TRANSACTIONS",
				posting.getValue("ID_TRANS"),
				"../crm/cards/transactionspecs.jsp?id=" + posting.getValue("ID_TRANS")
			));
		html.append("</td><td><input type=\"text\" name=\"id_trans\" size=\"16\" value=\"" + posting.getValue("ID_TRANS") + "\" readonly class=\"inputfield-ro\"></td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td>" + postingXML.getfieldTransl("is_grouped", false) + "</td><td><input type=\"text\" name=\"is_grouped\" size=\"15\" value=\"" + posting.getValue("IS_GROUPED_TSL") + "\" readonly class=\"inputfield-ro\"> </td>\n");
		html.append("<td>" + postingXML.getfieldTransl("id_quest_int", false));
		html.append(getGoToHyperLink(
				"CARDS_QUESTIONNAIRE_IMPORT",
				posting.getValue("ID_QUEST_INT"),
				"../crm/cards/questionnaire_importspecs.jsp?id=" + posting.getValue("ID_QUEST_INT")
			));
		html.append("</td><td><input type=\"text\" name=\"id_quest_int\" size=\"16\" value=\"" + posting.getValue("ID_QUEST_INT") + "\" readonly class=\"inputfield-ro\"></td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td>" + posting_schemeXML.getfieldTransl("cd_bk_doc_type", false) + "</td><td><input type=\"text\" name=\"cd_bk_doc_type\" size=\"70\" value=\"" + getBKDocTypeName(posting.getValue("CD_BK_DOC_TYPE")) + "\" readonly class=\"inputfield-ro\"> </td>\n");
		html.append("<td>" + postingXML.getfieldTransl("id_remittance", false));
		html.append(getGoToHyperLink(
				"FINANCE_REMITTANCE",
				posting.getValue("ID_REMITTANCE"),
				"../crm/finance/remittancespecs.jsp?id=" + posting.getValue("ID_REMITTANCE")
			));
		html.append("</td> <td><input type=\"text\" name=\"id_remittance\" size=\"16\" value=\"" + posting.getValue("ID_REMITTANCE") + "\" readonly class=\"inputfield-ro\"> </td>\n");
		html.append("</tr>\n");

		html.append("<tr>\n");
		html.append("<td>" + postingXML.getfieldTransl("id_posting_line", false));
		html.append(getGoToHyperLink(
				"FINANCE_ACCOUNTING_DOC",
				posting.getValue("ID_POSTING_LINE"),
				"../crm/finance/accounting_docspecs.jsp?type=posting&id_posting=" + posting.getValue("ID_POSTING_LINE")
			));
		html.append("</td><td><input type=\"text\" name=\"id_posting_line\" size=\"15\" value=\"" + posting.getValue("ID_POSTING_LINE") + "\" readonly class=\"inputfield-ro\"> </td>\n");
		html.append("<td>" + postingXML.getfieldTransl("id_card_operation", false));
		html.append(getGoToHyperLink(
				"CARDS_CARD_TASKS",
				posting.getValue("ID_CARD_OPERATION"),
				"../crm/cards/card_taskspecs.jsp?id=" + posting.getValue("ID_CARD_OPERATION")
			));
		html.append("</td> <td><input type=\"text\" name=\"id_card_operation\" size=\"16\" value=\"" + posting.getValue("ID_CARD_OPERATION") + "\" readonly class=\"inputfield-ro\"> </td>\n");
		html.append("</tr>\n");
		html.append("<td colspan=2> </td>\n");
		html.append("<td>" + postingXML.getfieldTransl("id_bank_statement_line", false));
		html.append(getGoToHyperLink(
				"FINANCE_BANKSTATEMENT",
				posting.getValue("ID_BANK_STATEMENT_LINE"),
				"../crm/finance/bankstatement_linespecs.jsp?id=" + posting.getValue("ID_BANK_STATEMENT_LINE")
			));
		html.append("</td> <td><input type=\"text\" name=\"id_bank_statement_line\" size=\"16\" value=\"" + posting.getValue("ID_BANK_STATEMENT_LINE") + "\" readonly class=\"inputfield-ro\"> </td>\n");
		html.append("</tr>\n");

		html.append("<tr>\n");
		html.append("<td colspan=\"4\" class=\"top_line\"><b>" + postingXML.getfieldTransl("h_clearing", false) + "</b></td>\n");
		html.append("</tr>\n");

		html.append("<tr>\n");
		html.append("<td>" + posting_schemeXML.getfieldTransl("using_in_clearing", false) + "</td><td><input type=\"text\" name=\"using_in_clearing\" size=\"16\" value=\"" + getMeaningFoCodeValue("YES_NO", posting.getValue("USING_IN_CLEARING"))+ "\" readonly class=\"inputfield-ro\"> </td>\n");
		html.append("<td colspan=\"2\">&nbsp;</td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td>" + postingXML.getfieldTransl("is_clearing", false)  + "</td><td><input type=\"text\" name=\"is_clearing\" size=\"16\" value=\"" + posting.getValue("IS_CLEARING_TSL") + "\" readonly class=\"inputfield-ro\"> </td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td>" + postingXML.getfieldTransl("id_clearing_line", false));
		html.append(getGoToHyperLink(
				"FINANCE_CLEARING",
				posting.getValue("ID_CLEARING_LINE"),
				"../crm/finance/clearing_linespecs.jsp?id=" + posting.getValue("ID_CLEARING_LINE")
			));
		html.append("<td> <input type=\"text\" name=\"id_clearing_line\" size=\"16\" value=\"" + posting.getValue("ID_CLEARING_LINE") + "\" readonly class=\"inputfield-ro\"> </td>\n");
		html.append("<td colspan=\"2\">&nbsp;</td>\n");
		html.append("</tr>\n");

		html.append("<tr>\n");
		html.append("<td>" + postingXML.getfieldTransl("receiver_id_bank_account", false));
		html.append(getGoToHyperLink(
				"CLIENTS_BANK_ACCOUNTS",
				posting.getValue("RECEIVER_ID_BANK_ACCOUNT"),
				"../crm/clients/accountspecs.jsp?id=" + posting.getValue("RECEIVER_ID_BANK_ACCOUNT")
			));
		html.append("</td>");
		html.append("<td>\n");
		html.append("<input type=\"text\" id=\"name_bank_account_debet\" name=\"name_bank_account_debet\" size=\"70\" value=\"" + getBankAccountNumberAndName(posting.getValue("RECEIVER_ID_BANK_ACCOUNT")) + "\" readonly class=\"inputfield-ro\">\n");
		html.append("</td>\n");
		html.append("<td colspan=\"2\">&nbsp;</td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td>" + postingXML.getfieldTransl("payer_id_bank_account", false));
		html.append(getGoToHyperLink(
				"CLIENTS_BANK_ACCOUNTS",
				posting.getValue("PAYER_ID_BANK_ACCOUNT"),
				"../crm/clients/accountspecs.jsp?id=" + posting.getValue("PAYER_ID_BANK_ACCOUNT")
			));
		html.append("</td>\n");
		html.append("<td>\n");
		html.append("<input type=\"text\" id=\"name_bank_account_credit\" name=\"name_bank_account_credit\" size=\"70\" value=\"" + getBankAccountNumberAndName(posting.getValue("PAYER_ID_BANK_ACCOUNT")) + "\" readonly class=\"inputfield-ro\">\n");
		html.append("</td>\n");
		html.append("<td colspan=\"2\">&nbsp;</td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td valign=\"top\">" + relationshipXML.getfieldTransl("payment_function", false)+ "</td><td><textarea name=\"payment_function\" cols=\"67\" rows=\"3\" readonly class=\"inputfield-ro\">" + posting.getValue("PAYMENT_FUNCTION") + "</textarea></td>\n");
		html.append("<td colspan=\"2\">&nbsp;</td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td colspan=4 class=\"top_line\"><b>" + commonXML.getfieldTransl("h_record_param", false) + "</b></td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td>" + commonXML.getfieldTransl("creation_date", false) + "</td> <td><input type=\"text\" name=\"creation_date\" size=\"20\" value=\"" + posting.getValue("CREATION_DATE_DHMSF") + "\" readonly class=\"inputfield-ro\"> </td>\n");
		html.append("<td>" + commonXML.getfieldTransl("last_update_date",false) + "</td> <td><input type=\"text\" name=\"last_update_date\" size=\"20\" value=\"" + posting.getValue("LAST_UPDATE_DATE_DHMSF") + "\" readonly class=\"inputfield-ro\"> </td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td>" + commonXML.getfieldTransl("created_by", false) + "</td> <td><input type=\"text\" name=\"created_by\" size=\"20\" value=\"" + getSystemUserName(posting.getValue("CREATED_BY")) + "\" readonly class=\"inputfield-ro\"> </td>\n");
		html.append("<td>" + commonXML.getfieldTransl("last_update_by", false) + "</td> <td><input type=\"text\" name=\"last_update_by\" size=\"20\" value=\"" + getSystemUserName(posting.getValue("LAST_UPDATE_BY")) + "\" readonly class=\"inputfield-ro\"> </td>\n");
		html.append("</tr>\n");

		return html.toString();
	}

	public String getPostingAddHTML(String pCDCurrency, String pDateFormatTitle) {

		StringBuilder html = new StringBuilder();
		
		html.append("<tr>\n");
		html.append("<td>" + postingXML.getfieldTransl("debet_id_bk_account", true) + "</td>\n");
		html.append("<td>\n");
		html.append(getWindowFindBKAccount("debet", "", "70"));
		html.append("</td>\n");
		html.append("<td>" + postingXML.getfieldTransl("operation_date", true) + "</td><td>");
		html.append(getCalendarInputField("operation_date", "", "10"));
		html.append("</td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td>" + postingXML.getfieldTransl("credit_id_bk_account", true) + "</td>\n");
		html.append("<td>\n");
		html.append(getWindowFindBKAccount("credit", "", "70"));
		html.append("</td>\n");
		html.append("<td>" + postingXML.getfieldTransl("name_currency", true) + "</td> <td><select name=\"cd_currency\" class=\"inputfield\">" + getCurrencyOptions(pCDCurrency, true) + "</select> </td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td rowspan=\"3\" valign=\"top\">" + postingXML.getfieldTransl("assignment_posting", false)  + "</td><td rowspan=\"3\"><textarea name=\"assignment_posting\" cols=\"80\" rows=\"4\" class=\"inputfield\"></textarea></td>\n");
		html.append("<td>" + postingXML.getfieldTransl("entered_amount", true)  + "</td> <td><input type=\"text\" name=\"entered_amount\" size=\"16\" value=\"\" class=\"inputfield\"> </td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td colspan=\"2\" class=\"top_line\"><b>" + postingXML.getfieldTransl("h_to_base", false) + "</b></td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td>" + postingXML.getfieldTransl("base_currency", false)  + "</td> <td><select name=\"base_currency\" class=\"inputfield\">" + getCurrencyOptions(pCDCurrency, true) + "</select></td>\n");
		html.append("</tr>\n");
		html.append("<tr>");
		html.append("<td>" + postingXML.getfieldTransl("id_bk_operation_scheme_line", false) + "</td>\n");
		html.append("<td>\n");
		html.append(getWindowFindBKOperationScheme("bk_operation_scheme_line", "", "70"));
		html.append("<td>" + postingXML.getfieldTransl("exchange_rate", false)  + "</td> <td><input type=\"text\" name=\"exchange_rate\" size=\"16\" value=\"\" class=\"inputfield\"> </td>\n");
		html.append("</td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td>" + postingXML.getfieldTransl("id_club_rel", false) + "</td>\n");
		html.append("<td>\n"); 
		html.append(getWindowFindClurRelationship("club_rel", "", "", "70"));
		html.append("</td>\n");
		html.append("<td>" + postingXML.getfieldTransl("conversion_date", false)  + "</td><td>");
		html.append(getCalendarInputField("conversion_date", "", "10"));
		html.append("</td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td colspan=\"2\">&nbsp;</td>\n");
		html.append("<td>" + postingXML.getfieldTransl("accounted_amount", false)  + "</td> <td><input type=\"text\" name=\"accounted_amount\" size=\"16\" value=\"\" class=\"inputfield\"> </td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td colspan=\"4\" class=\"top_line\"><b>" + postingXML.getfieldTransl("h_grouping", false) + "</b></td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td>" + posting_schemeXML.getfieldTransl("run_postings_export", true) + "</td> <td colspan=\"3\"><select name=\"run_postings_export\" id=\"run_postings_export\" class=\"inputfield\" onchange=\"checkBKDocType()\">" + getMeaningFromLookupNameOptions("YES_NO", "N", false) + "</select> </td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td><span id=\"span_cd_bk_doc_type\">" + posting_schemeXML.getfieldTransl("cd_bk_doc_type", true) + "</span></td> <td colspan=\"3\"><select name=\"cd_bk_doc_type\" id=\"cd_bk_doc_type\" class=\"inputfield\">" + getBKDocTypeOptions("", true) + "</select> </td>\n");
		html.append("</tr>\n");

		html.append("<tr>\n");
		html.append("<td colspan=\"4\" class=\"top_line\"><b>" + postingXML.getfieldTransl("h_clearing", false) + "</b></td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td>" + posting_schemeXML.getfieldTransl("using_in_clearing", true) + "</td> <td><select name=\"using_in_clearing\" id=\"using_in_clearing\" onchange=\"checkClearing();\" class=\"inputfield\">" + getMeaningFromLookupNameOptions("YES_NO", "Y", false) + "</select> </td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td><span id=\"span_receiver_id_bank_account\">" + postingXML.getfieldTransl("receiver_id_bank_account", false) + "</span></td>\n");
		html.append("<td>\n");
		html.append(getWindowFindBankAccount2("bank_account_debet", "", "70"));
		html.append("</td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td><span id=\"span_payer_id_bank_account\">" + postingXML.getfieldTransl("payer_id_bank_account", false) + "</span></td>\n");
		html.append("<td>\n");
		html.append(getWindowFindBankAccount2("bank_account_credit", "", "70"));
		html.append("</td>\n");
		html.append("</tr>\n");
		html.append("<tr>\n");
		html.append("<td valign=\"top\">" + relationshipXML.getfieldTransl("payment_function", false)  + "</td>");
		html.append("<td colspan=3><textarea name=\"payment_function\" cols=\"80\" rows=\"3\" class=\"inputfield\"></textarea></td>\n");
		html.append("</tr>\n");
		
		return html.toString();
	}
}
