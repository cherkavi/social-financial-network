package bc.ws.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import bc.ws.domain.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import bc.ws.exception.PersistentException;
import bc.ws.exception.WsException;
import bc.ws.persistent.CardOperations;
import bc.ws.persistent.CardOperations.ConfirmationType;
import bc.ws.persistent.CardOperations.OperationType;
import bc.ws.persistent.CardOperations.PaymentType;
import bc.ws.persistent.CardOperations.ReplenishKind;
import bc.ws.persistent.CardOperations.ReplenishType;
import bc.ws.persistent.ConnectionManager;

@WebService
public class CardOperationsService {
	private final static Logger LOGGER=Logger.getLogger(CardOperationsService.class);
	
	@Autowired
	ConnectionManager connectionManager;
	
	@WebMethod
	public CheckCardResult checkCard(
			@WebParam(name="userName")
			String userName, 
			@WebParam(name="operationDate")
			String operationDate, 
			@WebParam(name="signature")
			String signature,
			@WebParam(name="terminanlId")
			String terminalId,
			@WebParam(name="cardNumber")
			String cardNumber, 
			@WebParam(name="operationType")
			OperationType operationType, 
			@WebParam(name="dateFormat")
			String dateFormat
			) throws WsException{
		LOGGER.debug("check card");
		Connection connection=null;
		try{
			connection=connectionManager.openHash(userName, operationDate, signature);
			CardOperations operation=new CardOperations(connection);
			return operation.checkCard(terminalId, cardNumber, operationType, dateFormat);
		}catch(SQLException ex){
			LOGGER.warn(userName+"/"+signature, ex);
			throw new WsException("check login/password");
		}catch(PersistentException ex){
			LOGGER.warn(userName+"/"+signature, ex);
			throw new WsException("check parameters");
		}finally{
			connectionManager.close(connection);
		}
	}
	
	@WebMethod
	public PaymentResult payment(
			@WebParam(name="userName")
			String userName, 
			@WebParam(name="operationDate")
			String operationDate, 
			@WebParam(name="signature")
			String signature,
			@WebParam(name="terminalId")
			String terminalId,
			@WebParam(name="cardNumber")
			String cardNumber,
			@WebParam(name="promoCode")
			String promoCode,
			@WebParam(name="replenishKind")
			ReplenishKind replenishKind, 
			@WebParam(name="paymentType")
			PaymentType paymentType, 
			@WebParam(name="bankTrn")
			String bankTrn,
			@WebParam(name="payTotal")
			String payTotal,
			@WebParam(name="shareAccountSum")
			String shareAccountSum,
			@WebParam(name="shareFeeMargin")
			String shareFeeMargin,
			@WebParam(name="paymentSum")
			String paymentSum, 
			@WebParam(name="percentPoint")
			String percentPoint, 
			@WebParam(name="sumPoint")
			String sumPoint, 
			@WebParam(name="enteredSum")
			String enteredSum, 
			@WebParam(name="sumChange")
			String sumChange, 
			@WebParam(name="memberShip")
			String memberShip, 
			@WebParam(name="memberShipMargin")
			String memberShipMargin, 
			@WebParam(name="changeToShareAccount")
			boolean changeToShareAccount, 
			@WebParam(name="canUseShareAccount")
			boolean canUseShareAccount) 
					throws WsException {
		LOGGER.debug("payment");
		Connection connection=null;
		try{
			connection=connectionManager.openHash(userName, operationDate, signature);
			CardOperations operation=new CardOperations(connection);
			return operation.payment(terminalId, cardNumber, promoCode, replenishKind, paymentType, bankTrn, payTotal,
					shareAccountSum, shareFeeMargin, paymentSum, percentPoint, sumPoint, enteredSum, sumChange, memberShip,
					memberShipMargin, changeToShareAccount, canUseShareAccount);
		}catch(SQLException ex){
			LOGGER.warn(userName+"/"+signature,ex);
			throw new WsException("check login/password");
		}catch(PersistentException ex){
			LOGGER.warn(userName+"/"+signature,ex);
			throw new WsException("check parameters");
		}finally{
			connectionManager.close(connection);
		}
	}
	

	@WebMethod
	public ConfirmationResult confirm(
			@WebParam(name="userName")
			String userName, 
			@WebParam(name="operationDate")
			String operationDate, 
			@WebParam(name="signature")
			String signature,
			@WebParam(name="transactionId")
			Integer idTelegr,
			@WebParam(name="confirmationType")
			ConfirmationType confirmationType, 
			@WebParam(name="confirmationCode")
			String confirmationCode, 
			@WebParam(name="paymentDescription")
			String paymentDescription) 
			throws WsException{
		LOGGER.debug("confirmation");
		Connection connection=null;
		try{
			connection=connectionManager.openHash(userName, operationDate, signature);
			CardOperations operation=new CardOperations(connection);
			return operation.confirm(idTelegr, confirmationType, confirmationCode, paymentDescription);
		}catch(SQLException ex){
			LOGGER.warn(userName+"/"+signature,ex);
			throw new WsException("check login/password");
		}catch(PersistentException ex){
			LOGGER.warn(userName+"/"+signature,ex);
			throw new WsException("check parameters");
		}finally{
			connectionManager.close(connection);
		}
	}
	
	@WebMethod
	public ActivationResult activate(
			@WebParam(name="userName")
			String userName, 
			@WebParam(name="operationDate")
			String operationDate, 
			@WebParam(name="signature")
			String signature,
			@WebParam(name="terminanlId")
			String terminanlId,
			@WebParam(name="cardNumber")
			String cardNumber, 
			@WebParam(name="activationCode")
			String activationCode)
			throws WsException{
		LOGGER.debug("activate");
		Connection connection=null;
		try{
			connection=connectionManager.openHash(userName, operationDate, signature);
			CardOperations operation=new CardOperations(connection);
			return operation.activate(terminanlId, cardNumber, activationCode);
		}catch(SQLException ex){
			LOGGER.warn(userName+"/"+signature,ex);
			throw new WsException("check login/password");
		}catch(PersistentException ex){
			LOGGER.warn(userName+"/"+signature,ex);
			throw new WsException("check parameters");
		}finally{
			connectionManager.close(connection);
		}
	}
	
	@WebMethod
	public CertificateResult giveCertificate(
			@WebParam(name="userName")
			String userName, 
			@WebParam(name="operationDate")
			String operationDate, 
			@WebParam(name="signature")
			String signature,
			@WebParam(name="terminanlId")
			String terminanlId,
			@WebParam(name="cardNumber")
			String cardNumber, 
			@WebParam(name="certificateNumber")
			String cdClubEventCoupon, 
			@WebParam(name="certificateControlCode")
			String couponControlCode ) 
			throws WsException{
		LOGGER.debug("give certificate");
		Connection connection=null;
		try{
			connection=connectionManager.openHash(userName, operationDate, signature);
			CardOperations operation=new CardOperations(connection);
			return operation.giveCertificate(terminanlId, cardNumber, cdClubEventCoupon, couponControlCode);
		}catch(SQLException ex){
			LOGGER.warn(userName+"/"+signature,ex);
			throw new WsException("check login/password");
		}catch(PersistentException ex){
			LOGGER.warn(userName+"/"+signature,ex);
			throw new WsException("check parameters");
		}finally{
			connectionManager.close(connection);
		}
	}

	@WebMethod
	public PayInvoiceResult payInvoice(
			@WebParam(name="userName")
			String userName, 
			@WebParam(name="operationDate")
			String operationDate, 
			@WebParam(name="signature")
			String signature,
			@WebParam(name="terminalId")
			String idTerminal,
			@WebParam(name="invoiceId")
			String idTelgrInvoice,
			@WebParam(name="replenishKind")
			ReplenishKind replenishKind, 
			@WebParam(name="paymentType")
			PaymentType paymentType,
			@WebParam(name="bankTrn")
			String bankTrn,
			@WebParam(name="payTotal")
			String payTotal,
			@WebParam(name="shareAccountSum")
			String shareAccountSum, 
			@WebParam(name="shareFeeMargin")
			String shareFeeMargin,
			@WebParam(name="enteredSum")
			String enteredSum,
			@WebParam(name="sumChange")
			String sumChange,
			@WebParam(name="changeToShareAccount")
			boolean changeToShareAccount,
			@WebParam(name="canUseShareAccount")
			boolean canUseShareAccount
			) 
			throws WsException{
		LOGGER.debug("pay invoice");
		Connection connection=null;
		try{
			connection=connectionManager.openHash(userName, operationDate, signature);
			CardOperations operation=new CardOperations(connection);
			return operation.payInvoice(idTerminal, idTelgrInvoice, replenishKind, paymentType, bankTrn, payTotal, shareAccountSum, shareFeeMargin, enteredSum, sumChange, changeToShareAccount, canUseShareAccount);
		}catch(SQLException ex){
			LOGGER.warn(userName+"/"+signature,ex);
			throw new WsException("check login/password");
		}catch(PersistentException ex){
			LOGGER.warn(userName+"/"+signature,ex);
			throw new WsException("check parameters");
		}finally{
			connectionManager.close(connection);
		}
	}
	
	
	@WebMethod
	public ContributionsCheckProgramResult contributionCheckProgram(
			@WebParam(name="userName")
			String userName,
			@WebParam(name="operationDate")
			String operationDate, 
			@WebParam(name="signature")
			String signature,
			@WebParam(name="terminalId")
			String idTerm,
			@WebParam(name="cardNumber")
			String cardNumber,
			@WebParam(name="targetProgramId")
			Integer idTargetProgram, 
			@WebParam(name="dateFormat")
			String dateFormat			
			) throws WsException{
		LOGGER.debug("contribution check program");
		Connection connection=null;
		try{
			connection=connectionManager.openHash(userName, operationDate, signature);
			CardOperations operation=new CardOperations(connection);
			return operation.contributionCheckProgram(idTerm, cardNumber, idTargetProgram, dateFormat);
		}catch(SQLException ex){
			LOGGER.warn(userName+"/"+signature,ex);
			throw new WsException("check login/password");
		}catch(PersistentException ex){
			LOGGER.warn(userName+"/"+signature,ex);
			throw new WsException("check parameters");
		}finally{
			connectionManager.close(connection);
		}
	}
			
	@WebMethod
	public ContributionsCheckFeeResult contributionCheckFee(
			@WebParam(name="userName")
			String userName,
			@WebParam(name="operationDate")
			String operationDate, 
			@WebParam(name="signature")
			String signature,
			@WebParam(name="terminalId")
			String idTerm,
			@WebParam(name="cardNumber")
			String cardNumber, 
			@WebParam(name="replenishType")
			OperationType replenishType, 
			@WebParam(name="totalFee")
			String totalFee, 
			@WebParam(name="shareFee")
			String shareFee, 
			@WebParam(name="shareFeeMarginInput")
			String shareFeeMarginInput, 
			@WebParam(name="membershipFee")
			String fee, 
			@WebParam(name="membershipFeeMarginInput")
			String feeMarginInput, 
			@WebParam(name="tpEntranceFee")
			String tpEntranceFee, 
			@WebParam(name="tpMembershipFee")
			String tpMembershipFee, 
			@WebParam(name="tpFee")
			String tpFee
			) throws WsException{
		LOGGER.debug("contribution check fee");
		Connection connection=null;
		try{
			connection=connectionManager.openHash(userName, operationDate, signature);
			CardOperations operation=new CardOperations(connection);
			return operation.contributionCheckFee(idTerm, cardNumber, replenishType, totalFee, shareFee, shareFeeMarginInput, fee, feeMarginInput, tpEntranceFee, tpMembershipFee, tpFee);
		}catch(SQLException ex){
			LOGGER.warn(userName+"/"+signature,ex);
			throw new WsException("check login/password");
		}catch(PersistentException ex){
			LOGGER.warn(userName+"/"+signature,ex);
			throw new WsException("check parameters");
		}finally{
			connectionManager.close(connection);
		}
	}


	@WebMethod
	public GiveCardResult giveCard(
			@WebParam(name="userName")
			String userName, 
			@WebParam(name="operationDate")
			String operationDate, 
			@WebParam(name="signature")
			String signature,
			@WebParam(name="terminanlId")
			String terminanlId,
			@WebParam(name="cardNumber")
			String cardNumber, 
			@WebParam(name="codeCountry")
			String codeCountry,
			@WebParam(name="cardPackageId")
			String newClientPackage, 
			@WebParam(name="paymentType")
			PaymentType payType, 
			@WebParam(name="enteredSum")
			String enteredSum, 
			@WebParam(name="sumChange")
			String sumChange, 
			@WebParam(name="changeToShareAccount")
			boolean changeToShareAccount, 
			@WebParam(name="bankTrn")
			String bankTrn, 
			@WebParam(name="questionDate")
			String questionDate, 
			@WebParam(name="dateFormat")
			String dateFormat
			) throws WsException{
		LOGGER.debug("give card");
		Connection connection=null;
		try{
			connection=connectionManager.openHash(userName, operationDate, signature);
			CardOperations operation=new CardOperations(connection);
			return operation.giveCard(terminanlId, cardNumber, codeCountry, newClientPackage, payType, enteredSum, sumChange, changeToShareAccount, bankTrn, questionDate, dateFormat);
		}catch(SQLException ex){
			LOGGER.warn(userName+"/"+signature,ex);
			throw new WsException("check login/password");
		}catch(PersistentException ex){
			LOGGER.warn(userName+"/"+signature,ex);
			throw new WsException("check parameters");
		}finally{
			connectionManager.close(connection);
		}
	}

	@WebMethod
	public AdmissionFeeResult admissionFee(
			@WebParam(name="userName")
			String userName, 
			@WebParam(name="operationDate")
			String operationDate, 
			@WebParam(name="signature")
			String signature,
			@WebParam(name="terminanlId")
			String terminanlId,
			@WebParam(name="cardNumber")
			String cardNumber,
			@WebParam(name="replenishType")
			ReplenishType replenishType,
			@WebParam(name="replenishKind")
			ReplenishKind replenishKind,
			@WebParam(name="paymentType")
			PaymentType payType,
			@WebParam(name="totalFee")
			String totalFee,
			@WebParam(name="pointFee")
			String pointFee,
			@WebParam(name="shareFee")
			String shareFee,
			@WebParam(name="shareFeeMarginInput")
			String shareFeeMarginInput,
			@WebParam(name="membershipFee")
			String membershipFee,
			@WebParam(name="membershipFeeMarginInput")
			String membershipFeeMarginInput,
			@WebParam(name="tpEntranceFee")
			String tpEntranceFee,
			@WebParam(name="tpMembershipFee")
			String tpMembershipFee,
			@WebParam(name="tpFee")
			String tpFee,
			@WebParam(name="mtfMarginInput")
			String mtfMarginInput,
			@WebParam(name="bankTrn")
			String bankTrn,
			@WebParam(name="enteredSum")
			String enteredSum,
			@WebParam(name="sumChange")
			String sumChange,
			@WebParam(name="changeToShareAccount")
			boolean changeToShareAccount,
			@WebParam(name="targetProgramId")
			Integer idTargetPrg,
			@WebParam(name="targetProgramPlaceId")
			Integer idTargetPrgPlace,
			@WebParam(name="canSubscribeTargetPrg")
			boolean canSubscribeTargetPrg,
			@WebParam(name="canUseShareAccount")
			boolean canUseShareAccount,
			@WebParam(name="dateFormat")
			String dateFormat
			) throws WsException{
		LOGGER.debug("admission fee");
		Connection connection=null;
		try{
			connection=connectionManager.openHash(userName, operationDate, signature);
			CardOperations operation=new CardOperations(connection);
			return operation.admissionFee(
					terminanlId,
					cardNumber,
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
					changeToShareAccount,
					idTargetPrg,
					idTargetPrgPlace,
					canSubscribeTargetPrg,
					canUseShareAccount,
					dateFormat							
					);
		}catch(SQLException ex){
			LOGGER.warn(userName+"/"+signature,ex);
			throw new WsException("check login/password");
		}catch(PersistentException ex){
			LOGGER.warn(userName+"/"+signature,ex);
			throw new WsException("check parameters");
		}finally{
			connectionManager.close(connection);
		}
	}

	
	@WebMethod
	public StornoCancellationResult stornoCancellation(
			@WebParam(name="userName")
			String userName, 
			@WebParam(name="operationDate")
			String operationDate, 
			@WebParam(name="signature")
			String signature,
			@WebParam(name="terminanlId")
			String terminanlId,
			@WebParam(name="rrn")
			String stornoTelgrId
			) throws WsException{
		LOGGER.debug("storno cancellation");
		Connection connection=null;
		try{
			connection=connectionManager.openHash(userName, operationDate, signature);
			CardOperations operation=new CardOperations(connection);
			return operation.stornoCancellation(terminanlId, stornoTelgrId);
		}catch(SQLException ex){
			LOGGER.warn(userName+"/"+signature,ex);
			throw new WsException("check login/password");
		}catch(PersistentException ex){
			LOGGER.warn(userName+"/"+signature,ex);
			throw new WsException("check parameters");
		}finally{
			connectionManager.close(connection);
		}
	}
	
	@WebMethod
	public StornoReturnResult stornoReturn(
			@WebParam(name="userName")
			String userName, 
			@WebParam(name="operationDate")
			String operationDate, 
			@WebParam(name="signature")
			String signature,
			@WebParam(name="terminanlId")
			String terminanlId,
			@WebParam(name="rrn")
			String stornoTelgrId,
			@WebParam(name="returnValue")
			String stornoReturnValue
			) throws WsException{
		LOGGER.debug("storno return");
		Connection connection=null;
		try{
			connection=connectionManager.openHash(userName, operationDate, signature);
			CardOperations operation=new CardOperations(connection);
			return operation.stornoReturn(terminanlId, stornoTelgrId, stornoReturnValue);
		}catch(SQLException ex){
			LOGGER.warn(userName+"/"+signature,ex);
			throw new WsException("check login/password");
		}catch(PersistentException ex){
			LOGGER.warn(userName+"/"+signature,ex);
			throw new WsException("check parameters");
		}finally{
			connectionManager.close(connection);
		}
	}

	@WebMethod	
	public TransferFromToCardResult transferFromToCard(
			@WebParam(name="userName")
			String userName, 
			@WebParam(name="operationDate")
			String operationDate, 
			@WebParam(name="signature")
			String signature,
			@WebParam(name="terminanlId")
			String terminanlId,
			@WebParam(name="fromCardNumber")
			String fromCardNumber,
			@WebParam(name="toCardNumber")
			String toCardNumber,
			@WebParam(name="transferAmount")
			String transferAmount,
			@WebParam(name="canUseShareAccount")
			boolean canUseShareAccount			
			) throws WsException{
		LOGGER.debug("transfer to/from card");
		Connection connection=null;
		try{
			connection=connectionManager.openHash(userName, operationDate, signature);
			CardOperations operation=new CardOperations(connection);
			return operation.transferFromToCard(terminanlId, fromCardNumber, toCardNumber, transferAmount, canUseShareAccount);
		}catch(SQLException ex){
			LOGGER.warn(userName+"/"+signature,ex);
			throw new WsException("check login/password");
		}catch(PersistentException ex){
			LOGGER.warn(userName+"/"+signature,ex);
			throw new WsException("check parameters");
		}finally{
			connectionManager.close(connection);
		}
	}

	@WebMethod
	public List<GetTargetProgramsResult> getTargetPrograms(
			@WebParam(name="userName")
			String userName,
			@WebParam(name="operationDate")
			String operationDate, 
			@WebParam(name="signature")
			String signature,
			@WebParam(name="terminanlId")
			String terminanlId) throws WsException {
		LOGGER.debug("get target programs");
		Connection connection=null;
		try{
			connection=connectionManager.openHash(userName, operationDate, signature);
			CardOperations operation=new CardOperations(connection);
			return operation.getTargetPrograms(terminanlId);
		}catch(SQLException ex){
			LOGGER.warn(userName+"/"+signature,ex);
			throw new WsException("check login/password");
		}finally{
			connectionManager.close(connection);
		}
	}

	@WebMethod
	public GetComissionDescriptionResult getComissionDescription(
			@WebParam(name="userName")
			String userName,
			@WebParam(name="operationDate")
			String operationDate, 
			@WebParam(name="signature")
			String signature,
			@WebParam(name="terminanlId")
			String terminanlId,
			@WebParam(name="operationType")
			OperationType operationType
			) throws WsException{
		LOGGER.debug("get comission description ");
		Connection connection=null;
		try{
			connection=connectionManager.openHash(userName, operationDate, signature);
			CardOperations operation=new CardOperations(connection);
			return operation.getComissionDescription(terminanlId, operationType);
		}catch(SQLException ex){
			LOGGER.warn(userName+"/"+signature,ex);
			throw new WsException("check login/password");
		}catch(PersistentException ex){
			LOGGER.warn(userName+"/"+signature,ex);
			throw new WsException("check parameters");
		}finally{
			connectionManager.close(connection);
		}
	}


	@WebMethod
	public List<GetCardPackagesResult> getCardPackages(
			@WebParam(name="userName")
			String userName,
			@WebParam(name="operationDate")
			String operationDate, 
			@WebParam(name="signature")
			String signature,
			@WebParam(name="terminanlId")
			String terminanlId
	) throws WsException{
		LOGGER.debug("get card packages result ");
		Connection connection=null;
		try{
			connection=connectionManager.openHash(userName, operationDate, signature);
			CardOperations operation=new CardOperations(connection);
			return operation.getCardPackages(terminanlId);
		}catch(SQLException ex){
			LOGGER.warn(userName+"/"+signature,ex);
			throw new WsException("check login/password");
		}finally{
			connectionManager.close(connection);
		}
	}

	@WebMethod
	public List<GetOperationsResult> getOperations(
			@WebParam(name="userName")
			String userName,
			@WebParam(name="operationDate")
			String operationDate, 
			@WebParam(name="signature")
			String signature,
			@WebParam(name="terminanlId")
			String terminanlId,
			@WebParam(name="beginPeriodDate")
			String beginPeriodDate,
			@WebParam(name="endPeriodDate")
			String endPeriodDate
	) throws WsException{
		LOGGER.debug("get operations ");
		Connection connection=null;
		try{
			connection=connectionManager.openHash(userName, operationDate, signature);
			CardOperations operation=new CardOperations(connection);
			return operation.getOperations(terminanlId, toDate(beginPeriodDate), toDate(endPeriodDate));
		}catch(SQLException ex){
			LOGGER.warn(userName+"/"+signature,ex);
			throw new WsException("check login/password");
		}catch(ParseException ex){
			LOGGER.warn("can't parse input date: "+beginPeriodDate+" or "+endPeriodDate);
			throw new WsException("format of the input Date: "+DATE_FORMAT);
		}finally{
			connectionManager.close(connection);
		}
	}

	private final static String DATE_FORMAT="dd.MM.yyyy";

	private Date toDate(String stringRepresentation) throws ParseException {
		SimpleDateFormat sdf=new SimpleDateFormat(DATE_FORMAT);
		return sdf.parse(stringRepresentation);
	}

	@WebMethod
	public List<GetTargetProgramPlacesResult> getTargetProgramPlaces(
			@WebParam(name="userName")
			String userName,
			@WebParam(name="operationDate")
			String operationDate, 
			@WebParam(name="signature")
			String signature,
			@WebParam(name="terminanlId")
			String terminanlId,
			@WebParam(name="targetProgramId")
			Integer targetProgramId
	) throws WsException{
		LOGGER.debug("get target program places ");
		Connection connection=null;
		try{
			connection=connectionManager.openHash(userName, operationDate, signature);
			CardOperations operation=new CardOperations(connection);
			return operation.getTargetProgramPlaces(terminanlId, targetProgramId);
		}catch(SQLException ex){
			LOGGER.warn(userName+"/"+signature,ex);
			throw new WsException("check login/password");
		}finally{
			connectionManager.close(connection);
		}
	}

}
