package bc.ws.persistent;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import bc.util.ConnectorUtils;
import bc.ws.domain.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.Ignore;
import org.junit.Test;

import bc.ws.exception.PersistentException;
import bc.ws.persistent.CardOperations.OperationType;
import bc.ws.persistent.CardOperations.PaymentType;
import bc.ws.persistent.CardOperations.ReplenishKind;
import bc.ws.persistent.CardOperations.ReplenishType;
import junit.framework.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-context-test.xml"})
public class CardOperationsTest {
	private final static String DEFAULT_USERNAME = "U20001";
	private final static String DEFAULT_PASSWORD = "1234";

	@Autowired
	ConnectionManager connectionManager;

	@Test
	public void checkCard() throws SQLException, PersistentException{
		// given
		Connection connection=connectionManager.open(DEFAULT_USERNAME, DEFAULT_PASSWORD);
		try{
			CardOperations operations=new CardOperations(connection);

			// when
			CheckCardResult checkCardResult=operations.checkCard("20001", "9900990010014", CardOperations.OperationType.PAYMENT, "DD.MM.RRRR");

			// then
			Assert.assertNull(checkCardResult.getWarnMessage());
		}finally{
			ConnectorUtils.closeQuietly(connection);
		}
	}
	

	@Test
	public void checkPayment() throws SQLException, PersistentException{
		// given
		Connection connection=connectionManager.open(DEFAULT_USERNAME, DEFAULT_PASSWORD);
		try {
			CardOperations operations = new CardOperations(connection);

			// when
			PaymentResult result = operations.payment(
					"20001", // p_id_term VARCHAR2 (50) := ‘20001’;
					"9900990010014", // p_cd_card1 VARCHAR2 (40) := ‘9900990010014’;
					"", // promoCode
					null, // p_replenish_kind VARCHAR2 (50) := '';
					PaymentType.CASH, // p_pay_type_primary VARCHAR2 (50) := ‘CASH’;
					"", // p_bank_trn VARCHAR2 (50) := '';
					"100.00", // p_pay_total VARCHAR2 (50) := ‘100.00’;
					"", // p_share_account_sum VARCHAR2 (50) := '';
					"", // p_share_fee_margin VARCHAR2 (50) := '';
					"100.00", // p_pay_sum VARCHAR2 (50) := ‘100.00’;
					"", // p_percent_point VARCHAR2 (50) := '';
					"", // p_sum_point VARCHAR2 (50) := '';
					"150.00", // p_entered_sum VARCHAR2 (50) := ‘150.00’;
					"50.00", // p_sum_change VARCHAR2 (50) := ‘50.00’;
					"", // p_membership_fee VARCHAR2 (50) := '';
					"", // p_membership_fee_margin VARCHAR2 (50) := '';
					true, // p_change_to_share_account VARCHAR2 (50) := ‘Y’;
					true // p_can_use_share_account VARCHAR2 (50) := ‘Y’;
			);

			// then
			Assert.assertNotNull(result);
		}finally{
			ConnectorUtils.closeQuietly(connection);
		}
	}
	
	
	@Test
	public void confirmOperation() throws SQLException, PersistentException{
		// given
		Connection connection=connectionManager.open(DEFAULT_USERNAME, DEFAULT_PASSWORD);
		try {
			CardOperations operations = new CardOperations(connection);

			// when
			ConfirmationResult result = operations.confirm(20001, null, "", null);

			// then
			Assert.assertTrue(result.getWarnMessage().contains("Операция с ИД 20001 не найдена"));
		}finally{
			ConnectorUtils.closeQuietly(connection);
		}
	}
	
	@Test
	public void activateOperation() throws SQLException, PersistentException{
		// given
		Connection connection=connectionManager.open(DEFAULT_USERNAME, DEFAULT_PASSWORD);
		try {
			CardOperations operations = new CardOperations(connection);

			// when
			ActivationResult result = operations.activate("20001", "9900990010125", "12345");

			// then
			Assert.assertTrue(result.getWarnMessage().equals("Карта 9900990010125 не выдана"));
		}finally{
			ConnectorUtils.closeQuietly(connection);
		}
	}

	@Test
	public void giveCertificateOperation() throws SQLException, PersistentException{
		// given
		Connection connection=connectionManager.open(DEFAULT_USERNAME, DEFAULT_PASSWORD);
		try {
			CardOperations operations = new CardOperations(connection);

			// when
			CertificateResult result = operations.giveCertificate("20001", "9900990010125", null, null);

			// then
			Assert.assertNotNull(StringUtils.trimToNull(result.getWarnMessage()));
			Assert.assertTrue(result.getWarnMessage().contains("Сертификат  не найден"));
		}finally {
			ConnectorUtils.closeQuietly(connection);
		}
	}
	
	@Test
	public void payInvoiceOperation() throws SQLException, PersistentException{
		// given
		Connection connection=connectionManager.open(DEFAULT_USERNAME, DEFAULT_PASSWORD);
		try {
			CardOperations operations = new CardOperations(connection);

			// when
			PayInvoiceResult result = operations.payInvoice("20001", "12345", null, PaymentType.CASH,
					null, "100.00",
					null, null,
					"150.00", "50.00",
					true, true);

			Assert.assertTrue(result.getWarnMessage().contains("Не найдены параметры лояльности, отказ в обслуживании"));
		}finally{
			ConnectorUtils.closeQuietly(connection);
		}
	}
	
	@Test
	public void giveCard() throws SQLException, PersistentException{
		// given
		Connection connection=connectionManager.open(DEFAULT_USERNAME, DEFAULT_PASSWORD);
		try {
			CardOperations operations = new CardOperations(connection);

			// when
			GiveCardResult result = operations.giveCard("20001", "9900990010014", "643",
					"43",
					PaymentType.CASH,
					"150.00", "50.00",
					true,
					null,
					"26.05.2016",
					"DD.MM.RRRR");

			Assert.assertTrue(result.getWarnMessage().contains("назначена другому пайщику"));
		}finally {
			ConnectorUtils.closeQuietly(connection);
		}
	}
	
	@Test
	public void contributionCheckResult() throws SQLException, PersistentException{
		// given
		Connection connection=connectionManager.open(DEFAULT_USERNAME, DEFAULT_PASSWORD);
		try {
			CardOperations operations = new CardOperations(connection);

			// when
			ContributionsCheckFeeResult result = operations.contributionCheckFee("20001", "9900990010014", OperationType.POINT_FEE,
					"150.00", null,
					"1.00", null, null,
					null, null, "150.00");

			// then
			Assert.assertNotNull(result);
			Assert.assertTrue(result.getOprSum().startsWith("150"));
		}finally {
			ConnectorUtils.closeQuietly(connection);
		}
	}

	@Test
	public void contributionCheckProgram() throws SQLException, PersistentException{
		// given
		Connection connection=connectionManager.open(DEFAULT_USERNAME, DEFAULT_PASSWORD);
		try {
			CardOperations operations = new CardOperations(connection);

			// when
			ContributionsCheckProgramResult result = operations.contributionCheckProgram("20001", "9900990010014", 11, "DD.MM.RRRR");

			// then
			Assert.assertNotNull(result);
			Assert.assertNotNull(result.getMembershipMonthSum());
		}finally {
			ConnectorUtils.closeQuietly(connection);
		}
	}


	@Test
	public void admissionFee() throws SQLException, PersistentException{
		// given
		Connection connection=connectionManager.open(DEFAULT_USERNAME, DEFAULT_PASSWORD);
		try {
			CardOperations operations = new CardOperations(connection);

			// when
			AdmissionFeeResult result = operations.admissionFee(
					"20001",
					"9900990010014",
					ReplenishType.POINT_FEE,
					ReplenishKind.SHARE_FEE,
					PaymentType.CASH,
					"",
					"",
					"",
					"",
					"",
					"",
					"",
					"",
					"",
					"",
					"",
					"150.00",
					"50.00",
					true,
					0,
					0,
					false,
					false,
					"DD.MM.RRRR");

			// then
			Assert.assertNotNull(result);
			Assert.assertTrue(result.getIdTelgr() > 0);
			Assert.assertNotNull(result.getPhoneMobile());
			Assert.assertNotNull(result.getSumPutToShareAccount());
		}finally {
			ConnectorUtils.closeQuietly(connection);
		}
	}
	
	@Test
	public void stornoCancellation() throws SQLException, PersistentException{
		// given
		Connection connection=connectionManager.open(DEFAULT_USERNAME, DEFAULT_PASSWORD);
		try {
			CardOperations operations = new CardOperations(connection);

			// when
			StornoCancellationResult result = operations.stornoCancellation("20001", "12345");

			// then
			Assert.assertNotNull(result);
			Assert.assertTrue(result.getWarnMessage().startsWith("Операция с RRN 12345 не найдена"));
		}finally {
			ConnectorUtils.closeQuietly(connection);
		}
	}
	
	@Test
	public void stornoReturnResult() throws PersistentException, SQLException{
		// given
		Connection connection=connectionManager.open(DEFAULT_USERNAME, DEFAULT_PASSWORD);
		try {
			CardOperations operations = new CardOperations(connection);

			// when
			StornoReturnResult result = operations.stornoReturn("20001", "12345", "100");

			// then
			Assert.assertNotNull(result);
		}finally {
			ConnectorUtils.closeQuietly(connection);
		}
	}
	

	@Test
	public void transferFromToCardResult() throws PersistentException, SQLException{
		// given
		Connection connection=connectionManager.open(DEFAULT_USERNAME, DEFAULT_PASSWORD);
		try {
			CardOperations operations = new CardOperations(connection);

			// when
			TransferFromToCardResult result = operations.transferFromToCard("20001", "9900990010014", "9900990010052", "1.00", true);

			// then
			Assert.assertNotNull(result);
			Assert.assertTrue(result.getIdTelgr() > 0);
			Assert.assertNotNull(result.getPhoneMobile());
			Assert.assertFalse(result.isCanSendPinInSms());
			// Assert.assertFalse(result.getCardTypeError());
		}finally {
			ConnectorUtils.closeQuietly(connection);
		}
	}

	@Test
	public void targetPrograms() throws PersistentException, SQLException{
		// given
		Connection connection=connectionManager.open(DEFAULT_USERNAME, DEFAULT_PASSWORD);
		try {
			CardOperations operations = new CardOperations(connection);

			// when
			List<GetTargetProgramsResult> result = operations.getTargetPrograms("20001");

			// then
			Assert.assertNotNull(result);
			Assert.assertTrue(result.size() > 0);

		}finally {
			ConnectorUtils.closeQuietly(connection);
		}
	}


	@Test
	public void getComissionDescription() throws PersistentException, SQLException{
		// given
		Connection connection=connectionManager.open(DEFAULT_USERNAME, DEFAULT_PASSWORD);
		try {
			CardOperations operations = new CardOperations(connection);

			// when
			GetComissionDescriptionResult result = operations.getComissionDescription("20001", OperationType.ACTIVATION);

			// then
			Assert.assertNotNull(result);
		}finally {
			ConnectorUtils.closeQuietly(connection);
		}
	}


	@Test
	public void getCardPackages() throws SQLException, PersistentException {
		// given
		Connection connection=connectionManager.open(DEFAULT_USERNAME, DEFAULT_PASSWORD);
		try {
			CardOperations operations = new CardOperations(connection);

			// when
			List<GetCardPackagesResult> result = operations.getCardPackages("20001");

			// then
			Assert.assertNotNull(result);
			Assert.assertTrue(result.size()>0);
		}finally {
			ConnectorUtils.closeQuietly(connection);
		}
	}


	@Test
	public void getOperations() throws SQLException, PersistentException {
		// given
		Connection connection=connectionManager.open(DEFAULT_USERNAME, DEFAULT_PASSWORD);
		try {
			CardOperations operations = new CardOperations(connection);

			// when
			List<GetOperationsResult> result = operations.getOperations("20001", new Date(), new Date());

			// then
			Assert.assertNotNull(result);
			// TODO uncomment it
			// Assert.assertTrue(result.size()>0);
		}finally {
			ConnectorUtils.closeQuietly(connection);
		}
	}

	@Test
	public void getTargetProgramPlaces() throws SQLException, PersistentException {
		// given
		Connection connection=connectionManager.open(DEFAULT_USERNAME, DEFAULT_PASSWORD);
		try {
			CardOperations operations = new CardOperations(connection);

			// when
			List<GetTargetProgramPlacesResult> result = operations.getTargetProgramPlaces("20001", 6);

			// then
			Assert.assertNotNull(result);
			Assert.assertTrue(result.size()>0);
		}finally {
			ConnectorUtils.closeQuietly(connection);
		}
	}


}
