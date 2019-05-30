package bc.payment.citypay.resource;

import bc.payment.citypay.domain.OperationResponse;
import bc.utils.RestUtils;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DedicatedChecking {
    private final static SimpleDateFormat SDF=new SimpleDateFormat("yyyyMMddHHmmss");

    public static void main(String[] args) throws Exception {
        // given
        String transactionId=Long.toString(System.currentTimeMillis());
        // when
        String data= RestUtils.getString("http://localhost:8080/citypay/payment_app.cgi?QueryType=check&TransactionId=6059&Account=9900990010125&PayElementId=1&ProviderId=999&TerminalId=112&TerminalTransactionId=808172");
        // then
        Serializer serializer = new Persister(new AnnotationStrategy());
        OperationResponse response=serializer.read(OperationResponse.class, new StringReader(data));

        System.out.println(data);
//		Assert.assertEquals(transactionId, response.getTransactionId());
//		Assert.assertEquals(null, response.getComment());
//		Assert.assertEquals(0, response.getResultCode());
    }

}
