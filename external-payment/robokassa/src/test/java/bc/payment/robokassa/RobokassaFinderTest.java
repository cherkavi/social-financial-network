package bc.payment.robokassa;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import bc.payment.PaymentExternalVO;
import bc.payment.PaymentType;
import bc.payment.exception.GeneralPaymentException;

@ContextConfiguration(locations = "classpath:spring-context-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class RobokassaFinderTest {

	@Autowired
	DataSource	dataSource;

	@Autowired
	RobokassaFinder	finder;

	@Test
	public void checkFind() throws GeneralPaymentException, SQLException{
		// given
		Long userId=101L;
		Connection connection=this.dataSource.getConnection();
		Long paymentId=190393L;
		String language="RU";
		String generalDbScheme="bc_demo.";

		// when
		PaymentExternalVO returnValue = finder.find(userId, connection, paymentId, language, generalDbScheme);
		
		// then
		Assert.assertNotNull(returnValue);
	}

	
	@Test
	public void checkFindWithExtraParameters() throws GeneralPaymentException, SQLException{
		// given
		Long userId=101L;
		Connection connection=this.dataSource.getConnection();
		String language="RU";
		String generalDbScheme="bc_demo.";

		int maxRows=100;
		
		PaymentType type=PaymentType.PAID;
		@SuppressWarnings("deprecation")
		Date dateBeginInclude=new Date(116,3,18,12,00,00);
		@SuppressWarnings("deprecation")
		Date dateEndExclude=new Date(116,4,25,12,00,00);
		
		// when
		List<PaymentExternalVO> returnValue = finder.find(userId, connection, dateBeginInclude, dateEndExclude, type, maxRows, language, generalDbScheme);
		
		// then
		Assert.assertNotNull(returnValue);
		Assert.assertTrue(returnValue.size()>0);
	}
}
