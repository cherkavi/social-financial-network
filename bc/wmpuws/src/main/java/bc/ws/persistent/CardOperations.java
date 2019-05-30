package bc.ws.persistent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import bc.ws.domain.*;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import bc.ws.exception.PersistentException;

public class CardOperations extends ManagedConnection{
	final static Logger LOGGER=Logger.getLogger(CardOperations.class);


	/**
	 * -- Тип операции (разрещенные значения)
	 */
	public enum OperationType{
//		оплата за товар
		PAYMENT,
//		пополнение баллов
		POINT_FEE,
//		-- ‘’ - паевой взнос
		SHARE_FEE,
//		-- ‘’ - членский взнос
		MEMBERSHIP_FEE,
//		-- ‘’ - членский целевой взнос
		MEMBERSHIP_TARGET_FEE,
//		-- ‘’ - выдача карты
		PUT_CARD,
//		-- ‘’ - Активация карты
		ACTIVATION
	}
	
	/**
	 * Дополнительная операция
	 */
	public enum ReplenishKind{
		// паевой взнос
		SHARE_FEE,
		// списание с паевого фонда
		GET_FROM_SHARE_ACCOUNT
	}
	
	/**
	 * Вид взноса:
	 */
	public enum ReplenishType{
//		- Пополнение баллов
		POINT_FEE,
//		- Паевой взнос
		SHARE_FEE,
//		 - Членский взнос
		MEMBERSHIP_FEE,
//		- Членский целевой взнос
		MEMBERSHIP_TARGET_FEE,
	}
	

	/**
	 *	Способ оплаты:
	 */
	public enum PaymentType{
		// наличными
		CASH,
		// банковской картой
		BANK_CARD,
		// картой СМПУ
		SMPU_CARD,
		// выставляется счет
		INVOICE
	}

	public enum ConfirmationType{
		//		-- ‘’ - в p_confirmation_code не передается дополнительный параметр (если предыдущая операция вернула C_NEED_END_INFO)
		NONE,
		//		-- ‘’ - в p_confirmation_code передан ПИН (если предыдущая операция вернула C_NEED_SMS_CONFIRMATION или C_SMS_CONFIRM_CREATED)
		PIN,
		//		-- ‘’ - в p_confirmation_code передан код, полученный из СМС (если предыдущая операция вернула C_NEED_PIN)
		SMS,
		//		-- ‘’ - в p_confirmation_code передан код активации (если предыдущая операция вернула C_NEED_ACTIVATION_CODE)
		ACTIVATION_CODE
	}
	
	private final CheckCardOperation operation;
	private final Payment payment;
	private final Confirmation confirmation;
	private final Activation activation;
	private final Certificate certificate;
	private final PayInvoice payInvoice;
	private final GiveCard giveCard;
	private final ContributionsCheckFee contributionsCheckFee;
	private final ContributionsCheckProgram contributionsCheckProgram;
	private final AdmissionFee admissionFee;
	private final StornoCancellation stornoCancellation;
	private final StornoReturn stornoReturn;
	private final TransferFromToCard transferFromToCard;
	private final GetComissionDescription getComissionDescription;
	
	public CardOperations(Connection connection) {
		super(connection);
		operation=new CheckCardOperation(dataSource);
		payment=new Payment(dataSource);
		confirmation=new Confirmation(dataSource);
		activation=new Activation(dataSource);
		certificate=new Certificate(dataSource);
		payInvoice=new PayInvoice(dataSource);
		giveCard=new GiveCard(dataSource);
		contributionsCheckFee=new ContributionsCheckFee(dataSource);
		contributionsCheckProgram=new ContributionsCheckProgram(dataSource);
		admissionFee=new AdmissionFee(dataSource);
		stornoCancellation=new StornoCancellation(dataSource);
		stornoReturn=new StornoReturn(dataSource);
		transferFromToCard=new TransferFromToCard(dataSource);
		getComissionDescription=new GetComissionDescription(dataSource);
	}
	
	/**
	 * Прием взносов: проверка целевой программы (ЦП)
	 * @param idTerm
	 * @param cardNumber
	 * @param idTargetProgram
	 * @param dateFormat
	 * @return
	 * @throws PersistentException
	 */
	public ContributionsCheckProgramResult contributionCheckProgram(String idTerm, String cardNumber, Integer idTargetProgram, String dateFormat) throws PersistentException{
		return contributionsCheckProgram.executeFunction(idTerm, cardNumber, idTargetProgram, dateFormat);
	}

	/**
	 * Прием взносов: проверка сумм
	 * @param idTerm
	 * @param cardNumber
	 * @param replenishType
	 * @param totalFee
	 * @param shareFee
	 * @param shareFeeMarginInput
	 * @param fee
	 * @param feeMarginInput
	 * @param tpEntranceFee
	 * @param tpMembershipFee
	 * @param tpFee
	 * @return
	 * @throws PersistentException
	 */
	public ContributionsCheckFeeResult contributionCheckFee(String idTerm, String cardNumber, OperationType replenishType,
			String totalFee, String shareFee, String shareFeeMarginInput, String fee, String feeMarginInput, 
			String tpEntranceFee, String tpMembershipFee, String tpFee) throws PersistentException{
		return contributionsCheckFee.executeFunction(idTerm, cardNumber, replenishType==null?null:replenishType.toString(), totalFee, shareFee, shareFeeMarginInput, fee, feeMarginInput, tpEntranceFee, tpMembershipFee, tpFee);
	}
	
	/**
	 * Выдача карты
	 * @param idTerm
	 * @param cardNumber
	 * @param codeCountry
	 * @param newClientPackage
	 * @param payType
	 * @param enteredSum
	 * @param sumChange
	 * @param changeToShareAccount
	 * @param bankTrn
	 * @param questionDate
	 * @param dateFormat
	 * @return
	 * @throws PersistentException
	 */
	public GiveCardResult giveCard(
			String idTerm, String cardNumber, String codeCountry,
			String newClientPackage, PaymentType payType, String enteredSum, 
			String sumChange, boolean changeToShareAccount, String bankTrn, String questionDate, String dateFormat) throws PersistentException{
		return giveCard.executeFunction(idTerm, cardNumber, codeCountry,
				newClientPackage, payType, enteredSum, 
				sumChange, RowMapperUtils.toString(changeToShareAccount), bankTrn, questionDate, dateFormat);
	}
	
	/**
	 *  Проверка карты
	 * @param terminalId
	 * @param cardNumber
	 * @param operationType
	 * @param dateFormat
	 * @return
	 * @throws PersistentException
	 */
	public CheckCardResult checkCard(String terminalId, String cardNumber, OperationType operationType, String dateFormat) throws PersistentException{
		return operation.execute(terminalId, cardNumber, operationType, dateFormat);
	}

	/**
	 * Оплата за товар
	 * @param terminalId
	 * @param cardNumber
	 * @param replenishKind
	 * @param paymentType
	 * @param receiptNumber
	 * @param amountOfPaymentInCents
	 * @param amountOfShareAccountInCents
	 * @param amountOfShareFeeInCents
	 * @param paymentSum
	 * @param percentPoint
	 * @param sumPoint
	 * @param enteredSum
	 * @param sumChange
	 * @param memberShip
	 * @param memberShipMargin
	 * @param changeToShareAccount
	 * @param canUseShareAccount
	 * @return
	 * @throws PersistentException
	 */
	public PaymentResult payment(String terminalId, String cardNumber, String promoCode, ReplenishKind replenishKind, PaymentType paymentType, String receiptNumber,
			String amountOfPaymentInCents, String amountOfShareAccountInCents, String amountOfShareFeeInCents, String paymentSum, String percentPoint, 
			String sumPoint, String enteredSum, String sumChange, String memberShip, String memberShipMargin, boolean changeToShareAccount, boolean canUseShareAccount) 
					throws PersistentException {
		return payment.executePayment(terminalId, cardNumber, promoCode, replenishKind, paymentType, receiptNumber, amountOfPaymentInCents,
				amountOfShareAccountInCents, amountOfShareFeeInCents, paymentSum, percentPoint, sumPoint, enteredSum, sumChange, memberShip, 
				memberShipMargin, changeToShareAccount, canUseShareAccount);
	}

	/**
	 * Подтверждение операций (общая функция)
	 * @param idTelegr
	 * @param confirmationType
	 * @param confirmationCode
	 * @param paymentDescription
	 * @return
	 * @throws PersistentException
	 */
	public ConfirmationResult confirm(Integer idTelegr, ConfirmationType confirmationType, String confirmationCode, String paymentDescription) throws PersistentException{
		return confirmation.executeFunction(idTelegr, confirmationType, confirmationCode, paymentDescription);
	}

	/**
	 * Активация карты
	 * @param idTerminal
	 * @param cardNumber
	 * @param activationCode
	 * @return
	 * @throws PersistentException
	 */
	public ActivationResult activate(String idTerminal, String cardNumber, String activationCode) throws PersistentException{
		return activation.executeFunction(idTerminal.toString(), cardNumber, activationCode);
	}
	
	/**
	 * Выдача сертификата/купона
	 * @param idTerminal
	 * @param cardNumber
	 * @param cdClubEventCoupon
	 * @param couponControlCode
	 * @return
	 * @throws PersistentException
	 */
	public CertificateResult giveCertificate(String idTerminal, String cardNumber, String cdClubEventCoupon, String couponControlCode) throws PersistentException{
		return certificate.executeFunction(idTerminal.toString(), cardNumber, cdClubEventCoupon, couponControlCode);
	}
	
	/**
	 * Оплата счета
	 * @param idTerminal
	 * @param idTelgrInvoice
	 * @param replenishKind
	 * @param paymentType
	 * @param bankTrn
	 * @param payTotal
	 * @param shareAccountSum
	 * @param shareFeeMargin
	 * @param enteredSum
	 * @param sumChange
	 * @param changeToShareAccount
	 * @param canUseShareAccount
	 * @return
	 * @throws PersistentException
	 */
	public PayInvoiceResult payInvoice(
			String idTerminal,
			String idTelgrInvoice,
			ReplenishKind replenishKind, 
			PaymentType paymentType,
			String bankTrn,
			String payTotal,
			String shareAccountSum, 
			String shareFeeMargin,
			String enteredSum,
			String sumChange,
			boolean changeToShareAccount,
			boolean canUseShareAccount) throws PersistentException{
		return payInvoice.executeFunction(
				idTerminal,
				idTelgrInvoice,
				replenishKind,
				paymentType,
				bankTrn,
				payTotal,
				shareAccountSum, 
				shareFeeMargin,
				enteredSum,
				sumChange,
				RowMapperUtils.toString(changeToShareAccount),
				RowMapperUtils.toString(canUseShareAccount)
				);
	}
	
	/**
	 * Прием взносов: основная функция
	 * @param idTerm
	 * @param cdCard1
	 * @param replenishType
	 * @param replenishKind
	 * @param payType
	 * @param totalFee
	 * @param pointFee
	 * @param shareFee
	 * @param shareFeeMarginInput
	 * @param membershipFee
	 * @param membershipFeeMarginInput
	 * @param tpEntranceFee
	 * @param tpMembershipFee
	 * @param tpFee
	 * @param mtfMarginInput
	 * @param bankTrn
	 * @param enteredSum
	 * @param sumChange
	 * @param changeToShareAccount
	 * @param idTargetPrg
	 * @param idTargetPrgPlace
	 * @param canSubscribeTargetPrg
	 * @param canUseShareAccount
	 * @param dateFormat
	 * @return
	 * @throws PersistentException
	 */
	public AdmissionFeeResult admissionFee(
			String idTerm,
			String cdCard1,
			ReplenishType replenishType,
			ReplenishKind replenishKind,
			PaymentType payType,
			String totalFee,
			String pointFee,
			String shareFee,
			String shareFeeMarginInput,
			String membershipFee,
			String membershipFeeMarginInput,
			String tpEntranceFee,
			String tpMembershipFee,
			String tpFee,
			String mtfMarginInput,
			String bankTrn,
			String enteredSum,
			String sumChange,
			boolean changeToShareAccount,
			Integer idTargetPrg,
			Integer idTargetPrgPlace,
			boolean canSubscribeTargetPrg,
			boolean canUseShareAccount,
			String dateFormat
			) throws PersistentException{
		return admissionFee.executeFunction(
				idTerm,
				cdCard1,
				replenishType,
				replenishKind,
				payType,
				totalFee,
				pointFee,
				shareFee,
				shareFeeMarginInput,
				membershipFee,
				membershipFeeMarginInput,
				tpEntranceFee,
				tpMembershipFee,
				tpFee,
				mtfMarginInput,
				bankTrn,
				enteredSum,
				sumChange,
				RowMapperUtils.toString(changeToShareAccount),
				idTargetPrg,
				idTargetPrgPlace,
				RowMapperUtils.toString(canSubscribeTargetPrg),
				RowMapperUtils.toString(canUseShareAccount),
				dateFormat
		);
	}

	/**
	 * Отмена операции
	 * @param idTerm
	 * @return
	 * @throws PersistentException
	 */
	public StornoCancellationResult stornoCancellation(String idTerm, String rrn) throws PersistentException{
		return stornoCancellation.executeFunction(idTerm, rrn);
	}
	
	
	/**
	 * Операция возврата
	 * @param idTerm
	 * @param stornoTelgrId
	 * @param stornoReturnValue
	 * @return
	 * @throws PersistentException
	 */
	public StornoReturnResult stornoReturn(String idTerm,
			String stornoTelgrId,
			String stornoReturnValue) throws PersistentException{
		return stornoReturn.executeFunction(idTerm,
				stornoTelgrId,
				stornoReturnValue
				);
	}

	/**
	 * Перевод баллов с карты на карту
	 * @param idTerm
	 * @param fromCdCard1
	 * @param toCdCard1
	 * @param inputValue
	 * @param canUseShareAccount
	 * @return
	 * @throws PersistentException
	 */
	public TransferFromToCardResult transferFromToCard(String idTerm,
			String fromCdCard1,
			String toCdCard1,
			String inputValue,
			boolean canUseShareAccount) throws PersistentException{
		return transferFromToCard.executeFunction(idTerm,fromCdCard1,toCdCard1,inputValue,RowMapperUtils.toString(canUseShareAccount));
	}


	public List<GetTargetProgramsResult> getTargetPrograms(String terminalId) {
		LOGGER.debug("SELECT a.id_target_prg, a.id_target_prg_parent, a.name_target_prg, a.sname_target_prg, a.desc_target_prg, a.name_nat_prs_initiator, a.name_nat_prs_administrator, a.sname_jur_prs, a.adr_jur_prs, a.date_beg_frmt, a.date_end_frmt, a.child_count, a.cd_target_prg_pay_period, a.name_target_prg_pay_period, a.pay_amount_frmt, a.min_pay_amount_frmt, a.cd_currency, a.pay_count, a.need_subscribe, a.need_administrator_confirm, a.entrance_fee_frmt, a.membership_fee_frmt, a.membership_period, a.membership_period_value, a.image_target_prg  FROM vws$target_prg_all a");

		return template.query("SELECT a.id_target_prg, a.id_target_prg_parent, a.name_target_prg, a.sname_target_prg, a.desc_target_prg, a.name_nat_prs_initiator, a.name_nat_prs_administrator, a.sname_jur_prs, a.adr_jur_prs, a.date_beg_frmt, a.date_end_frmt, a.child_count, a.cd_target_prg_pay_period, a.name_target_prg_pay_period, a.pay_amount_frmt, a.min_pay_amount_frmt, a.cd_currency, a.pay_count, a.need_subscribe, a.need_administrator_confirm, a.entrance_fee_frmt, a.membership_fee_frmt, a.membership_period, a.membership_period_value, a.image_target_prg  FROM vws$target_prg_all a",
				new RowMapper<GetTargetProgramsResult>(){
					@Override
					public GetTargetProgramsResult mapRow(ResultSet rs, int rowNum) throws SQLException {
						GetTargetProgramsResult returnValue=new GetTargetProgramsResult();
						// child_count
						returnValue.setTargetProgramId(rs.getInt("id_target_prg"));
						returnValue.setTargetProgramParentId(rs.getInt("id_target_prg_parent"));
						returnValue.setTargetProgramName(rs.getString("name_target_prg"));
						returnValue.setTargetProgramShortName(rs.getString("sname_target_prg"));
						returnValue.setTargetProgramDescription(rs.getString("desc_target_prg"));
						returnValue.setTargetProgramInitiator(rs.getString("name_nat_prs_initiator"));
						returnValue.setTargetProgramAdministrator(rs.getString("name_nat_prs_administrator"));
						returnValue.setTargetProgramPartnerName(rs.getString("sname_jur_prs"));
						returnValue.setTargetProgramAddress(rs.getString("adr_jur_prs"));
						returnValue.setBeginActionDate(rs.getString("date_beg_frmt"));
						returnValue.setEndActionDate(rs.getString("date_end_frmt"));
						returnValue.setPaymentPeriodCode(rs.getString("cd_target_prg_pay_period"));
						returnValue.setPaymentPeriodName(rs.getString("name_target_prg_pay_period"));
						returnValue.setPaymentAmount(rs.getString("pay_amount_frmt"));
						returnValue.setMinimalPaymentAmount(rs.getString("min_pay_amount_frmt"));
						returnValue.setPaymentCurrencyCode(rs.getString("cd_currency"));
						returnValue.setPaymentPeriodCount(rs.getString("pay_count"));
						returnValue.setNeedSubscribe(rs.getString("need_subscribe"));
						returnValue.setNeedAdministratorConfirmation(rs.getString("need_administrator_confirm"));
						returnValue.setEntranceFeeAmount(rs.getString("entrance_fee_frmt"));
						returnValue.setMembershipFeeAmount(rs.getString("membership_fee_frmt"));
						returnValue.setMembershipPeriodCode(rs.getString("membership_period"));
						returnValue.setMembershipPeriodValue(rs.getString("membership_period_value"));
						returnValue.setTargetProgramImagePath(rs.getString("image_target_prg"));
						return returnValue;
					}
				});
	}

	public GetComissionDescriptionResult getComissionDescription(String terminalId, OperationType operationType) throws PersistentException {
		return this.getComissionDescription.executeFunction(terminalId, operationType);
	}

	public List<GetCardPackagesResult> getCardPackages(String terminanlId) {
		LOGGER.debug("SELECT  a.id_jur_prs_card_pack, a.name_jur_prs_card_pack, a.desc_jur_prs_card_pack, a.id_card_status, a.name_card_status, a.cd_currency, a.name_currency, a.sname_currency, a.entrance_fee_frmt, a.membership_fee_frmt, a.membership_fee_month_count, a.share_fee_frmt, a.dealer_margin_frmt, a.agent_margin_frmt, a.total_amount_jp_card_pack_frmt, a.action_date_beg_frmt, a.action_date_end_frmt FROM vws$card_package_all a");
		return template.query("SELECT  a.id_jur_prs_card_pack, a.name_jur_prs_card_pack, a.desc_jur_prs_card_pack, a.id_card_status, a.name_card_status, a.cd_currency, a.name_currency, a.sname_currency, a.entrance_fee_frmt, a.membership_fee_frmt, a.membership_fee_month_count, a.share_fee_frmt, a.dealer_margin_frmt, a.agent_margin_frmt, a.total_amount_jp_card_pack_frmt, a.action_date_beg_frmt, a.action_date_end_frmt FROM vws$card_package_all a",
				new RowMapper<GetCardPackagesResult>(){
					@Override
					public GetCardPackagesResult mapRow(ResultSet rs, int rowNum) throws SQLException {
						GetCardPackagesResult returnValue=new GetCardPackagesResult();
						returnValue.setCardPackageId(rs.getInt("id_jur_prs_card_pack"));
						returnValue.setCardPackageName(rs.getString("name_jur_prs_card_pack"));
						returnValue.setCardPackageDescription(rs.getString("desc_jur_prs_card_pack"));
						returnValue.setCardStatusId(rs.getInt("id_card_status"));
						returnValue.setCardStatusName(rs.getString("name_card_status"));
						returnValue.setCurrencyCode(rs.getString("cd_currency"));
						returnValue.setCurrencyName(rs.getString("name_currency"));
						returnValue.setCurrencyShortName(rs.getString("sname_currency"));
						returnValue.setEntranceFeeAmount(rs.getString("entrance_fee_frmt"));
						returnValue.setMembershipFeeAmount(rs.getString("membership_fee_frmt"));
						returnValue.setMembershipFeeMonthCount(rs.getString("membership_fee_month_count"));
						returnValue.setMinimalShareFeeAmount(rs.getString("share_fee_frmt"));
						returnValue.setDealerMarginAmount(rs.getString("dealer_margin_frmt"));
						returnValue.setAgentMarginAmount(rs.getString("agent_margin_frmt"));
						returnValue.setTotalCardPackageAmount(rs.getString("total_amount_jp_card_pack_frmt"));
						returnValue.setBeginActionDate(rs.getString("action_date_beg_frmt"));
						returnValue.setEndActionDate(rs.getString("action_date_end_frmt"));
						return returnValue;
					}
				});
	}

	// TO_DATE('20020315', 'yyyymmdd')

	private final static String ORACLE_DATE_FORMAT="yyyyMMdd";

	private static String toOracleStringDate(Date date) {
		return new SimpleDateFormat(ORACLE_DATE_FORMAT).format(date);
	}

	public List<GetOperationsResult> getOperations(String terminanlId, Date beginPeriodDate, Date endPeriodDate) {
		LOGGER.debug("SELECT a.id_telgr, a.id_trans, a.nc_term, a.fcd_trans_type, a.name_trans_type, a.sys_date_frmt, a.opr_sum_frmt, a.cd_currency, a.rrn FROM vws$operations_success a WHERE id_term = "+terminanlId+" AND sys_date BETWEEN TO_DATE('"+toOracleStringDate(beginPeriodDate)+"', 'yyyymmdd') AND TO_DATE('"+toOracleStringDate(endPeriodDate)+"', 'yyyymmdd')");
		return template.query("SELECT a.id_telgr, a.id_trans, a.nc_term, a.fcd_trans_type, a.name_trans_type, a.sys_date_frmt, a.opr_sum_frmt, a.cd_currency, a.rrn FROM vws$operations_success a WHERE id_term = "+terminanlId+" AND sys_date BETWEEN TO_DATE('"+toOracleStringDate(beginPeriodDate)+"', 'yyyymmdd') AND TO_DATE('"+toOracleStringDate(endPeriodDate)+"', 'yyyymmdd')",
				new RowMapper<GetOperationsResult>(){
					@Override
					public GetOperationsResult mapRow(ResultSet rs, int rowNum) throws SQLException {
						GetOperationsResult returnValue=new GetOperationsResult();
						returnValue.setTransactionId(rs.getInt("id_telgr"));
						returnValue.setOperationId(rs.getInt("id_trans"));
						returnValue.setDocumentNumber(rs.getString("nc_term"));
						returnValue.setOperationTypeCode(rs.getString("fcd_trans_type"));
						returnValue.setOperationTypeName(rs.getString("name_trans_type"));
						returnValue.setOperationDate(rs.getString("sys_date_frmt"));
						returnValue.setOperationAmount(rs.getString("opr_sum_frmt"));
						returnValue.setOperationCurrencyCode(rs.getString("cd_currency"));
						returnValue.setRRN(rs.getString("rrn"));
						return returnValue;
					}
				});
	}

	public List<GetTargetProgramPlacesResult> getTargetProgramPlaces(String terminanlId, Integer targetProgramId) {
		LOGGER.debug("SELECT a.id_target_prg_place, a.name_service_place, a.sname_service_place, a.adr_short FROM vws$target_prg_place_all a WHERE a.id_target_prg="+targetProgramId);
		return template.query("SELECT a.id_target_prg_place, a.name_service_place, a.sname_service_place, a.adr_short FROM vws$target_prg_place_all a WHERE a.id_target_prg="+targetProgramId,
				new RowMapper<GetTargetProgramPlacesResult>(){
					@Override
					public GetTargetProgramPlacesResult mapRow(ResultSet rs, int rowNum) throws SQLException {
						GetTargetProgramPlacesResult returnValue=new GetTargetProgramPlacesResult();
						returnValue.setTargetProgramPlaceId(rs.getInt("id_target_prg_place"));
						returnValue.setTargetProgramPlaceName(rs.getString("name_service_place"));
						returnValue.setTargetProgramPlaceShortName(rs.getString("sname_service_place"));
						returnValue.setTargetProgramPlaceAddress(rs.getString("adr_short"));
						return returnValue;
					}
				});
	}


}

class GetComissionDescription extends StringParamsFunction<GetComissionDescriptionResult>{

	GetComissionDescription(DataSource dataSource){
		super(dataSource, "pack$ws.get_comission_description",
				new String[]{"p_id_term", "p_cd_oper_type"},
				new String[]{"p_comission_percent_value","p_comission_fixed_value","p_cd_currency","p_comission_description"}
		);
	}

	@Override
	protected GetComissionDescriptionResult convert(Map<String, Object> out) {
		GetComissionDescriptionResult returnValue=new GetComissionDescriptionResult((String)out.get(RESULT_PARAM), (String)out.get(RESULT_MESSAGE));
		returnValue.setComissionPercentValue((String)out.get("p_comission_percent_value"));
		returnValue.setComissionFixedValue((String)out.get("p_comission_fixed_value"));
		returnValue.setComissionCurrencyCode((String)out.get("p_cd_currency"));
		returnValue.setComissionDescriptioin((String)out.get("p_comission_description"));
		return returnValue;
	}

}


class TransferFromToCard extends StringParamsFunction<TransferFromToCardResult>{

	TransferFromToCard(DataSource dataSource) {
		super(dataSource, 
				"pack$ws_ui.transfer_from_to_card", 
				new String[]{"p_id_term","p_from_cd_card1","p_to_cd_card1","p_input_value","p_can_use_share_account"}, 
				new String[]{"p_id_telgr","p_phone_mobile","p_can_send_pin_in_sms","p_card_type_error"});
	}
	
	@Override
	protected TransferFromToCardResult convert(Map<String, Object> out) {
		TransferFromToCardResult returnValue=new TransferFromToCardResult((String)out.get(RESULT_PARAM), (String)out.get(RESULT_MESSAGE));
		returnValue.setIdTelgr(RowMapperUtils.fromStringToInteger((String)out.get("p_id_telgr")));
		returnValue.setPhoneMobile((String)out.get("p_phone_mobile"));
		returnValue.setCanSendPinInSms(RowMapperUtils.fromStringToBoolean((String)out.get("p_can_send_pin_in_sms")));
		returnValue.setCardTypeError((String)out.get("p_card_type_error"));
		return returnValue;
	}
	
}

class StornoReturn extends StringParamsFunction<StornoReturnResult>{
	StornoReturn(DataSource dataSource) {
		super(dataSource, 
				"pack$ws_ui.storno_return", 
				new String[]{"p_id_term","p_rrn","p_storno_return_value"},
				new String[]{"p_return_telegr_id"});
	}

	@Override
	protected StornoReturnResult convert(Map<String, Object> out) {
		StornoReturnResult returnValue=new StornoReturnResult((String)out.get(RESULT_PARAM), (String)out.get(RESULT_MESSAGE));
		returnValue.setReturnTelegrId(RowMapperUtils.fromStringToInteger((String)out.get("p_return_telegr_id")));
		return returnValue;
	}
	
}

class StornoCancellation extends StringParamsFunction<StornoCancellationResult>{

	StornoCancellation(DataSource dataSource) {
		super(dataSource, 
			"pack$ws_ui.storno_cancellation ", 
			new String[]{"p_id_term","p_RRN",},
			new String[]{"p_calcel_telgr_id"}
		);
	}

	@Override
	protected StornoCancellationResult convert(Map<String, Object> out) {
		StornoCancellationResult returnValue=new StornoCancellationResult((String)out.get(RESULT_PARAM), (String)out.get(RESULT_MESSAGE));
		returnValue.setCalcelTelgrId(RowMapperUtils.fromStringToInteger( (String)out.get("p_calcel_telgr_id") ) );
		return returnValue;
	}
	
}

class AdmissionFee extends StringParamsFunction<AdmissionFeeResult>{

	AdmissionFee(DataSource dataSource) {
		super(dataSource, "pack$ws_ui.replenish", 
				new String[]{
"p_id_term","p_cd_card1","p_replenish_type","p_replenish_kind","p_pay_type","p_total_fee", "p_point_fee", 
"p_share_fee","p_share_fee_margin_input", "p_membership_fee", "p_membership_fee_margin_input", "p_tp_entrance_fee", 
"p_tp_membership_fee", "p_tp_fee", "p_mtf_margin_input", "p_bank_trn", "p_entered_sum", "p_sum_change", "p_change_to_share_account", 
"p_id_target_prg","p_id_target_prg_place","p_can_subscribe_target_prg","p_can_use_share_account","p_date_format" 						
				}, 
				new String[]{
"p_id_telgr","p_id_card_status","p_sum_share_fee_need", "p_opr_sum","p_point_fee_margin","p_share_fee_margin",
"p_membership_fee_margin","p_mtf_margin", "p_change_margin","p_total_margin","p_membership_last_date",
"p_calc_point_total", "p_calc_point_shareholder", "p_sum_put_to_share_account", "p_sum_get_from_share_account", 
"p_sum_get_point","p_phone_mobile","p_can_send_pin_in_sms"						
				}
		);
	}

	@Override
	protected AdmissionFeeResult convert(Map<String, Object> out) {
		AdmissionFeeResult returnValue=new AdmissionFeeResult((String)out.get(RESULT_PARAM), (String)out.get(RESULT_MESSAGE));
		returnValue.setIdTelgr( RowMapperUtils.fromStringToInteger( (String)out.get("p_id_telgr")));
		returnValue.setIdCardStatus(RowMapperUtils.fromStringToInteger((String)out.get("p_id_card_status")));
		returnValue.setSumShareFeeNeed((String)out.get("p_sum_share_fee_need"));
		returnValue.setOprSum((String)out.get("p_opr_sum"));
		returnValue.setPointFeeMargin((String)out.get("p_point_fee_margin"));
		returnValue.setShareFeeMargin((String)out.get("p_share_fee_margin"));

		returnValue.setMembershipFeeMargin((String)out.get("p_membership_fee_margin"));
		returnValue.setMtfMargin((String)out.get("p_mtf_margin"));
		returnValue.setChangeMargin((String)out.get("p_change_margin"));
		returnValue.setTotalMargin((String)out.get("p_total_margin"));
		returnValue.setMembershipLastDate((String)out.get("p_membership_last_date"));
		returnValue.setCalcPointTotal((String)out.get("p_calc_point_total"));
		returnValue.setCalcPointShareholder((String)out.get("p_calc_point_shareholder"));
		returnValue.setSumPutToShareAccount((String)out.get("p_sum_put_to_share_account"));
		returnValue.setSumGetFromShareAccount((String)out.get("p_sum_get_from_share_account"));
		returnValue.setSumGetPoint((String)out.get("p_sum_get_point"));
		returnValue.setPhoneMobile((String)out.get("p_phone_mobile"));
		returnValue.setCanSendPinInSms(RowMapperUtils.fromStringToBoolean((String)out.get("p_can_send_pin_in_sms")));
		return returnValue;
	}
	
}

class ContributionsCheckProgram extends StringParamsFunction<ContributionsCheckProgramResult>{
	ContributionsCheckProgram(DataSource dataSource){
		super(dataSource, "pack$ws_ui.replenish_check_target_prg", 
				new String[]{
						"p_id_term",
						"p_cd_card1",
						"p_id_target_prg",
						"p_date_format" 						
						},
				new String[]{
						"p_id_card_status",
						"p_need_membership_fee",
						"p_membership_month_sum", 
						"p_membership_last_date", 
						"p_membership_nopay_month", 
						"p_membership_need_pay_sum", 
						"p_membership_max_pay_month", 
						"p_need_tp_subscribe",
						"p_need_tp_admin_confirm",
						"p_need_tp_entrance_fee",
						"p_tp_entrance_fee",
						"p_need_tp_membership_fee",
						"p_tp_membership_fee_sum",
						"p_tp_membership_last_date",
						"p_need_pay_sum"
						}
				);
	}

	@Override
	protected ContributionsCheckProgramResult convert(Map<String, Object> out) {
		ContributionsCheckProgramResult returnValue=new ContributionsCheckProgramResult((String)out.get(RESULT_PARAM), (String)out.get(RESULT_MESSAGE));
		returnValue.setIdCardStatus(RowMapperUtils.fromStringToInteger ((String)out.get("p_id_card_status")));
		returnValue.setNeedMembershipFee(RowMapperUtils.fromStringToBoolean((String)out.get("p_need_membership_fee")));
		returnValue.setMembershipMonthSum((String)out.get("p_membership_month_sum"));
		returnValue.setMembershipLastDate((String)out.get("p_membership_last_date"));
		returnValue.setMembershipNopayMonth((String)out.get("p_membership_nopay_month"));
		returnValue.setMembershipNeedPaySum((String)out.get("p_membership_need_pay_sum"));
		returnValue.setMembershipMaxpayMonth((String)out.get("p_membership_max_pay_month"));
		returnValue.setNeedTpSubscribe(RowMapperUtils.fromStringToBoolean((String)out.get("p_need_tp_subscribe")) );
		returnValue.setNeedTpAdminConfirm(RowMapperUtils.fromStringToBoolean((String)out.get("p_need_tp_admin_confirm")));
		returnValue.setNeedTpEntranceFee(RowMapperUtils.fromStringToBoolean((String)out.get("p_need_tp_entrance_fee")));
		returnValue.setTpEntranceFee((String)out.get("p_tp_entrance_fee"));
		returnValue.setNeedTpMembershipFee(RowMapperUtils.fromStringToBoolean((String)out.get("p_need_tp_membership_fee")));
		returnValue.setTpMembershipFeeSum((String)out.get("p_tp_membership_fee_sum"));
		returnValue.setTpMembershipLastDate((String)out.get("p_tp_membership_last_date"));
		returnValue.setNeedPaySum((String)out.get("p_need_pay_sum"));
		return returnValue;
	}
}

class ContributionsCheckFee extends StringParamsFunction<ContributionsCheckFeeResult>{

	ContributionsCheckFee(DataSource dataSource) {
		super(dataSource, "pack$ws_ui.replenish_check_sum", 
				new String[]{
						"p_id_term","p_cd_card1","p_replenish_type","p_total_fee","p_share_fee","p_share_fee_margin_input",
						"p_membership_fee","p_membership_fee_margin_input","p_tp_entrance_fee","p_tp_membership_fee","p_tp_fee"
						},
				new String[]{"p_id_card_status","p_share_fee_margin","p_membership_fee_margin","p_mtf_margin","p_opr_sum"}
				);
	}

	@Override
	protected ContributionsCheckFeeResult convert(Map<String, Object> out) {
		ContributionsCheckFeeResult returnValue=new ContributionsCheckFeeResult((String)out.get(RESULT_PARAM), (String)out.get(RESULT_MESSAGE));
		returnValue.setIdCardStatus(RowMapperUtils.fromStringToInteger( (String)out.get("p_id_card_status")) );
		returnValue.setShareFeeMargin((String)out.get("p_share_fee_margin"));
		returnValue.setMembershipFeeMargin((String)out.get("p_membership_fee_margin")); 
		returnValue.setMtfMargin((String)out.get("p_mtf_margin"));
		returnValue.setOprSum((String)out.get("p_opr_sum"));
		return returnValue;
	}
	
}

class GiveCard extends StringParamsFunction<GiveCardResult>{

	GiveCard(DataSource dataSource) {
		super(dataSource, "pack$ws_ui.put_nat_prs_card", 
				new String[]{"p_id_term","p_cd_card1","p_code_country","p_new_client_package",
						"p_pay_type","p_entered_sum","p_sum_change","p_change_to_share_account","p_bank_trn","p_quesionnaire_date","p_date_format",},
				new String[]{"p_id_telgr","p_common_margin","p_sum_put_share_account_change","p_phone_mobile","p_can_send_pin_in_sms"}
				);
	}

	@Override
	protected GiveCardResult convert(Map<String, Object> out) {
		GiveCardResult returnValue=new GiveCardResult((String)out.get(RESULT_PARAM), (String)out.get(RESULT_MESSAGE));
		returnValue.setIdTelegr(RowMapperUtils.fromStringToInteger ((String)out.get("p_id_telgr")));
		returnValue.setCommonMargin((String)out.get("p_common_margin"));
		returnValue.setSumPutShareAccountChange((String)out.get("p_sum_put_share_account_change"));
		returnValue.setPhoneMobile((String)out.get("p_phone_mobile"));
		returnValue.setCanSendPinInSms(RowMapperUtils.fromStringToBoolean((String)out.get("p_can_send_pin_in_sms")));
		return returnValue;
	}
	
}


class PayInvoice extends StringParamsFunction<PayInvoiceResult>{

	PayInvoice(DataSource dataSource) {
		super(dataSource, "pack$ws_ui.payment_invoice", 
				new String[]{"p_id_term", "p_id_telgr_invoice", "p_replenish_kind", "p_pay_type_primary", "p_bank_trn","p_pay_total","p_share_account_sum", "p_share_fee_margin","p_entered_sum","p_sum_change","p_change_to_share_account","p_can_use_share_account"}, 
				new String[]{"p_id_telgr", "p_sum_share_fee_need", "p_share_fee_need_margin", "p_change_margin", "p_total_margin", "p_sum_put_to_share_account", "p_sum_get_from_share_account", "p_phone_mobile","p_can_send_pin_in_sms"}
				);
	}

	@Override
	protected PayInvoiceResult convert(Map<String, Object> out) {
		PayInvoiceResult returnValue=new PayInvoiceResult((String)out.get(RESULT_PARAM), (String)out.get(RESULT_MESSAGE));
		returnValue.setIdTelegr(RowMapperUtils.fromStringToInteger((String)out.get("p_id_telgr")));
		returnValue.setSumShareFeeNeed((String)out.get("p_sum_share_fee_need"));
		returnValue.setSumShareFeeNeedMargin((String)out.get("p_share_fee_need_margin"));
		returnValue.setChangeMargin((String)out.get("p_change_margin"));
		returnValue.setTotalMargin((String)out.get("p_total_margin"));
		returnValue.setSumPutToShareAccount((String)out.get("p_sum_put_to_share_account"));
		returnValue.setSumGetFromShareAccount((String)out.get("p_sum_get_from_share_account"));
		returnValue.setPhoneMobile((String)out.get("p_phone_mobile"));
		returnValue.setCanSendPinInSms(RowMapperUtils.fromStringToBoolean((String)out.get("p_can_send_pin_in_sms")));
		return returnValue;
	}
	
}


class Certificate extends StringParamsFunction<CertificateResult>{

	Certificate(DataSource dataSource) {
		
		super(dataSource, "pack$ws_ui.coupon_put", 
				new String[]{"p_id_term", "p_cd_card1", "p_cd_club_event_coupon", "p_coupon_control_code"}, 
				new String[]{"p_id_telgr"});
	}

	@Override
	protected CertificateResult convert(Map<String, Object> out) {
		CertificateResult returnValue=new CertificateResult((String)out.get(RESULT_PARAM), (String)out.get(RESULT_MESSAGE));
		returnValue.setIdTelegr(RowMapperUtils.fromStringToInteger( (String)out.get("p_id_telgr")) );
		return returnValue;
	}
	
}

class Activation extends StringParamsFunction<ActivationResult>{
	
	Activation(DataSource dataSource){
		super(dataSource, "pack$ws_ui.activation_nat_prs_card", 
				new String[]{"p_id_telgr", "p_cd_card1", "p_card_activation_code"},
				new String[]{"p_id_telgr", "p_phone_mobile_confirm", "p_can_send_pin_in_sms"});
	}

	@Override
	protected ActivationResult convert(Map<String, Object> out) {
		ActivationResult returnValue=new ActivationResult((String)out.get(RESULT_PARAM), (String)out.get(RESULT_MESSAGE));
		returnValue.setIdTelegr(RowMapperUtils.fromStringToInteger((String)out.get("p_id_telgr")));
		return returnValue;
	}

}

class Confirmation extends StringParamsFunction<ConfirmationResult>{

	Confirmation(DataSource dataSource){
		super(dataSource, "pack$ws_ui.oper_confirm",
				new String[]{"p_id_telgr", "p_confirmation_type", "p_confirmation_code", "p_payment_description"},
				new String[]{"p_phone_mobile_confirm", "p_can_send_pin_in_sms", }
				);
	}

	@Override
	protected ConfirmationResult convert(Map<String, Object> out) {
		ConfirmationResult returnValue=new ConfirmationResult((String)out.get(RESULT_PARAM), (String)out.get(RESULT_MESSAGE));
		returnValue.setPhoneMobileConfirm((String)out.get("p_phone_mobile_confirm"));
		returnValue.setCanSendPinInSms((String)out.get("p_can_send_pin_in_sms"));
		return returnValue;
	}
	
}

class Payment extends StoredProcedure{
	private final static String RESULT_PARAM="result";
	private final static String RESULT_MESSAGE="result_message";
	
	Payment(DataSource dataSource){
		super(dataSource, "pack$ws_ui.payment_for_goods");
		declareParameter(new SqlOutParameter(RESULT_PARAM, Types.VARCHAR, 50));
		// -- Входящие параметры
		
//		-- ИД терминала
//		p_id_term VARCHAR2 (50) := ‘20001’;
		declareParameter(new SqlParameter("p_id_term", java.sql.Types.VARCHAR, 50));
//		-- Номер карты
//		p_cd_card1 VARCHAR2 (40) := ‘9900990010014’;
		declareParameter(new SqlParameter("p_cd_card1", java.sql.Types.VARCHAR, 50));
//      -- p_promo_code IN VARCHAR2
		declareParameter(new SqlParameter("p_promo_code", java.sql.Types.VARCHAR, 50));
//		-- Дополнительная операция
//		-- ‘SHARE_FEE’ - паевой взнос
//		-- ‘GET_FROM_SHARE_ACCOUNT’ - списание с паевого фонда
//		p_replenish_kind VARCHAR2 (50) := '';
		declareParameter(new SqlParameter("p_replenish_kind", java.sql.Types.VARCHAR, 50));
//		-- Способ оплаты:
//		-- ‘CASH’ - наличными
//		-- ‘BANK_CARD’ - банковской картой
//		-- ‘SMPU_CARD’ - картой СМПУ
//		-- ‘INVOICE’ - выставляется счет
//		p_pay_type_primary VARCHAR2 (50) := ‘CASH’;
		declareParameter(new SqlParameter("p_pay_type_primary", java.sql.Types.VARCHAR, 50));
//		-- Номер чека при оплате банковской картой
//		p_bank_trn VARCHAR2 (50) := '';
		declareParameter(new SqlParameter("p_bank_trn", java.sql.Types.VARCHAR, 50));
//		-- Общая сумма оплаты (в рублях с копейками)
//		p_pay_total VARCHAR2 (50) := ‘100.00’;
		declareParameter(new SqlParameter("p_pay_total", java.sql.Types.VARCHAR, 50));
//		-- Сумма паевого взноса (в рублях с копейками)
//		p_share_account_sum VARCHAR2 (50) := '';
		declareParameter(new SqlParameter("p_share_account_sum", java.sql.Types.VARCHAR, 50));
//		-- Комиссия на прием паевого взноса (в рублях с копейками)
//		p_share_fee_margin VARCHAR2 (50) := '';
		declareParameter(new SqlParameter("p_share_fee_margin", java.sql.Types.VARCHAR, 50));
//		-- Сумма оплаты за товар (в рублях с копейками)
//		p_pay_sum VARCHAR2 (50) := ‘100.00’;
		declareParameter(new SqlParameter("p_pay_sum", java.sql.Types.VARCHAR, 50));
//		-- % начисленных баллов
//		p_percent_point VARCHAR2 (50) := '';
		declareParameter(new SqlParameter("p_percent_point", java.sql.Types.VARCHAR, 50));
//		-- Сумма начисленных баллов
//		p_sum_point VARCHAR2 (50) := '';
		declareParameter(new SqlParameter("p_sum_point", java.sql.Types.VARCHAR, 50));
//		-- Внесенная сумма при оплате наличными (в рублях с копейками)
//		p_entered_sum VARCHAR2 (50) := ‘150.00’;
		declareParameter(new SqlParameter("p_entered_sum", java.sql.Types.VARCHAR, 50));
//		-- Сумма сдачи при оплате наличными (в рублях с копейками)
//		p_sum_change VARCHAR2 (50) := ‘50.00’;
		declareParameter(new SqlParameter("p_sum_change", java.sql.Types.VARCHAR, 50));
//		-- Сумма членского взноса (в рублях с копейками)
//		p_membership_fee VARCHAR2 (50) := '';
		declareParameter(new SqlParameter("p_membership_fee", java.sql.Types.VARCHAR, 50));
//		-- Комиссия за прием членского взноса (в рублях с копейками)
//		p_membership_fee_margin VARCHAR2 (50) := '';
		declareParameter(new SqlParameter("p_membership_fee_margin", java.sql.Types.VARCHAR, 50));
//		-- ‘Y’ - зачислить сдачу в паевой фонд
//		p_change_to_share_account VARCHAR2 (50) := ‘Y’;
		declareParameter(new SqlParameter("p_change_to_share_account", java.sql.Types.VARCHAR, 50));
//		-- ‘Y’ - разрешено использовать паевой фонд при оплате картой СМПУ
//		p_can_use_share_account VARCHAR2 (50) := ‘Y’;		
		declareParameter(new SqlParameter("p_can_use_share_account", java.sql.Types.VARCHAR, 50));
		
//		-- Исходящие параметры
//		-- ИД сформированной операции
//		p_id_telgr VARCHAR2 (50);
		declareParameter(new SqlOutParameter("p_id_telgr", Types.VARCHAR, 50));
//		-- Сумма, которую необходимо внести в паевой фонд при недостаточном количестве баллов (в рублях с копейками)
//		p_sum_share_fee_need VARCHAR2 (50);
		declareParameter(new SqlOutParameter("p_sum_share_fee_need", Types.VARCHAR, 50));
//		-- Комиссия за прием паевого взноса (в рублях с копейками)
//		p_share_fee_need_margin VARCHAR2 (50);
		declareParameter(new SqlOutParameter("p_share_fee_need_margin", Types.VARCHAR, 50));
//		-- Количество неоплаченных месяцев членского взноса (в рублях с копейками)
//		p_change_margin VARCHAR2 (50);
		declareParameter(new SqlOutParameter("p_change_margin", Types.VARCHAR, 50));
//		-- Общая сумма комиссии (в рублях с копейками)
//		p_total_margin VARCHAR2 (50);
		declareParameter(new SqlOutParameter("p_total_margin", Types.VARCHAR, 50));
//		-- Сумма начисленных баллов всего
//		p_calc_point_total VARCHAR2 (50);
		declareParameter(new SqlOutParameter("p_calc_point_total", Types.VARCHAR, 50));
//		-- Сумма начисленных баллов пайщику
//		p_calc_point_shareholder VARCHAR2 (50);
		declareParameter(new SqlOutParameter("p_calc_point_shareholder", Types.VARCHAR, 50));
//		-- Зачислено в паевой фонд (в рублях с копейками)
//		p_sum_put_to_share_account VARCHAR2 (50);
		declareParameter(new SqlOutParameter("p_sum_put_to_share_account", Types.VARCHAR, 50));
//		-- Списано с паевого фонда (в рублях с копейками)
//		p_sum_get_from_share_account VARCHAR2 (50);
		declareParameter(new SqlOutParameter("p_sum_get_from_share_account", Types.VARCHAR, 50));
//		-- Моб. телефон пайщика, на который отправлен СМС с кодом подтверждения
//		p_phone_mobile VARCHAR2 (50);
		declareParameter(new SqlOutParameter("p_phone_mobile", Types.VARCHAR, 50));
//		-- ‘Y’ - на моб.телефон пайщика отправлен СМС с кодом
//		p_can_send_pin_in_sms VARCHAR2 (50);		
		declareParameter(new SqlOutParameter("p_can_send_pin_in_sms", Types.VARCHAR, 50));
		
		// -- Исходящее сообщение
		declareParameter(new SqlOutParameter(RESULT_MESSAGE, Types.VARCHAR, 4000));
		
		setFunction(true);
		compile();
	}
	
	PaymentResult executePayment(String terminalId, String cardNumber, String promoCode, CardOperations.ReplenishKind replenishKind, CardOperations.PaymentType paymentType,
						  String receiptNumber, String amountOfPaymentInCents, String amountOfShareAccountInCents, String amountOfShareFeeInCents, 
						  String paymentSum, String percentPoint, String sumPoint, String enteredSum, String sumChange,
						  String memberShip, String memberShipMargin, boolean changeToShareAccount, boolean canUseShareAccount) throws PersistentException{
		Map<String, Object> in=new HashMap<String, Object>();
//		-- ИД терминала
		in.put("p_id_term", terminalId);
		in.put("p_cd_card1", cardNumber);
		in.put("p_promo_code", promoCode);
		in.put("p_replenish_kind", replenishKind==null?null:replenishKind.toString());
		in.put("p_pay_type_primary", paymentType==null?null:paymentType.toString());
		in.put("p_bank_trn", receiptNumber);
		
		in.put("p_pay_total", amountOfPaymentInCents);
		in.put("p_share_account_sum", amountOfShareAccountInCents);
		in.put("p_share_fee_margin", amountOfShareFeeInCents);
		in.put("p_pay_sum", paymentSum);
		in.put("p_percent_point", percentPoint);
		in.put("p_sum_point", sumPoint);
		in.put("p_entered_sum", enteredSum);
		in.put("p_sum_change", sumChange);
		in.put("p_membership_fee", memberShip);
		in.put("p_membership_fee_margin", memberShipMargin);
		
		in.put("p_change_to_share_account", RowMapperUtils.toString(changeToShareAccount));
		in.put("p_can_use_share_account", RowMapperUtils.toString(canUseShareAccount));
		
		CardOperations.LOGGER.debug("pack$ws_ui.payment_for_goods");
		CardOperations.LOGGER.debug(">>> :"+in.toString());
		Map<String, Object> out = execute(in);
		CardOperations.LOGGER.debug("<<< : "+out);
		if (out.isEmpty()){
			throw new PersistentException("pack$ws_ui.payment_for_goods: operation is not successfull: ");
		}
		PaymentResult returnValue=new PaymentResult((String)out.get(RESULT_PARAM), (String)out.get(RESULT_MESSAGE));

		returnValue.setIdTelegr(RowMapperUtils.fromStringToInteger((String)out.get("p_id_telgr")));
		returnValue.setSumShareFeeNeed((String)out.get("p_sum_share_fee_need"));
		returnValue.setShareFeeNeedMargin((String)out.get("p_share_fee_need_margin"));
		returnValue.setChangeMargin((String)out.get("p_change_margin"));
		returnValue.setTotalMargin((String)out.get("p_total_margin"));
		returnValue.setCalcPointTotal((String)out.get("p_calc_point_total"));
		returnValue.setCalcPointShareholder((String)out.get("p_calc_point_shareholder"));
		returnValue.setSumPutToShareAccount((String)out.get("p_sum_put_to_share_account"));
		returnValue.setSumGetFromShareAccount((String)out.get("p_sum_get_from_share_account"));
		returnValue.setPhoneMobile((String)out.get("p_phone_mobile"));
		returnValue.setCanSendPinInSms( RowMapperUtils.fromStringToBoolean((String)out.get("p_can_send_pin_in_sms")));

		return returnValue;
	}
}


class CheckCardOperation extends StoredProcedure{
	private final static String RESULT_PARAM="result";
	private final static String RESULT_MESSAGE="result_message";

	CheckCardOperation(DataSource dataSource){
		super(dataSource, "pack$ws_ui.oper_check_card");
		declareParameter(new SqlOutParameter(RESULT_PARAM, Types.VARCHAR, 50));
		// -- ИД терминала
		declareParameter(new SqlParameter("p_id_term", java.sql.Types.VARCHAR, 50));
		// -- Номер карты
		declareParameter(new SqlParameter("p_cd_card1", java.sql.Types.VARCHAR, 40));
		
		declareParameter(new SqlParameter("p_oper_type", java.sql.Types.VARCHAR, 50));
		// -- Формат даты
		declareParameter(new SqlParameter("p_date_format", java.sql.Types.VARCHAR, 50));

		// -- ИД вида карты (не используется)
		declareParameter(new SqlOutParameter("p_id_card_status", Types.VARCHAR, 10));
		// -- Сумма ежемесячного членского взноса (в рублях с копейками)
		declareParameter(new SqlOutParameter("p_membership_month_sum", Types.VARCHAR, 50));
		// -- Дата, до которой оплачен членский взнос
		declareParameter(new SqlOutParameter("p_membership_last_date", Types.VARCHAR, 50));
		// -- Количество неоплаченных месяцев членского взноса
		declareParameter(new SqlOutParameter("p_membership_nopay_month", Types.VARCHAR, 50));
		// -- Сумма членского взноса, которую необходимо оплатить
		declareParameter(new SqlOutParameter("p_membership_need_pay_sum", Types.VARCHAR, 50));
		// -- Максимальное количество месяцев, за которые можно оплатить членский взнос
		declareParameter(new SqlOutParameter("p_membership_max_pay_month", Types.VARCHAR, 50));
		// -- Валюта, в которой принимается членский взнос
		declareParameter(new SqlOutParameter("p_membership_cd_currency", Types.VARCHAR, 50));
		// -- Комиссия партнера за прием членского взноса
		declareParameter(new SqlOutParameter("p_membership_fee_margin", Types.VARCHAR, 50));
		// -- Исходящее сообщение
		declareParameter(new SqlOutParameter(RESULT_MESSAGE, Types.VARCHAR, 4000));
		
		setFunction(true);
		compile();
	}
	
	CheckCardResult execute(String terminalId, String cardNumber, CardOperations.OperationType operationType, String dateFormat) throws PersistentException{
		Map<String, Object> in=new HashMap<String, Object>();
		in.put("p_id_term", terminalId.toString());
		in.put("p_cd_card1", cardNumber);
		
//		-- Тип операции (разрещенные значения)
//		-- ‘PAYMENT’ - оплата за товар
//		-- ‘POINT_FEE’ - пополнение баллов
//		-- ‘SHARE_FEE’ - паевой взнос
//		-- ‘MEMBERSHIP_FEE’ - членский взнос
//		-- ‘MEMBERSHIP_TARGET_FEE’ - членский целевой взнос
		in.put("p_oper_type", operationType.toString());
		in.put("p_date_format", dateFormat);
		CardOperations.LOGGER.debug("pack$ws_ui.oper_check_card");
		CardOperations.LOGGER.debug(">>> :"+in.toString());
		Map<String, Object> out = execute(in);
		CardOperations.LOGGER.debug("<<< : "+out);
		if (out.isEmpty()){
			throw new PersistentException("pack$ws_ui.oper_check_card: operation is not successfull: ");
		}
		
		CheckCardResult returnValue=new CheckCardResult((String)out.get(RESULT_PARAM), (String)out.get(RESULT_MESSAGE));
		returnValue.setIdCardStatus(RowMapperUtils.fromStringToInteger((String)out.get("p_id_card_status")));
		returnValue.setMembershipMonthSum((String)out.get("p_membership_month_sum"));
		returnValue.setMembershipLastDate((String)out.get("p_membership_last_date"));
		returnValue.setMembershipNopayMonth((String)out.get("p_membership_nopay_month"));
		returnValue.setMembershipNeedPaySum((String)out.get("p_membership_need_pay_sum"));
		returnValue.setMembershipMaxPayMonth((String)out.get("p_membership_max_pay_month"));
		returnValue.setMembershipCdCurrency((String)out.get("p_membership_cd_currency"));
		returnValue.setMembershipFeeMargin((String)out.get("p_membership_fee_margin"));
		return returnValue;
	}

}


//
//	public GetTargetProgramsResult getTargetPrograms(String terminanlId) {
//		Map<String, Object> in=new HashMap<String, Object>();
//		in.put("p_id_term", terminalId.toString());
//
//		return null;
//	}


class GetTargetProgram extends StringParamsFunction<ConfirmationResult>{

	GetTargetProgram(DataSource dataSource){
		super(dataSource, "pack$ws_ui.oper_confirm",
				new String[]{"p_id_telgr", "p_confirmation_type", "p_confirmation_code", "p_payment_description"},
				new String[]{"p_phone_mobile_confirm", "p_can_send_pin_in_sms", }
		);
	}

	@Override
	protected ConfirmationResult convert(Map<String, Object> out) {
		ConfirmationResult returnValue=new ConfirmationResult((String)out.get(RESULT_PARAM), (String)out.get(RESULT_MESSAGE));
		returnValue.setPhoneMobileConfirm((String)out.get("p_phone_mobile_confirm"));
		returnValue.setCanSendPinInSms((String)out.get("p_can_send_pin_in_sms"));
		return returnValue;
	}

}


