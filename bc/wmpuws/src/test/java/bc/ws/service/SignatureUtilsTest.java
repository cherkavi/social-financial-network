package bc.ws.service;

import org.junit.Assert;
import org.junit.Test;

public class SignatureUtilsTest {

    @Test
    public void checkSignature(){
        // given
        String user="U20005";
        String password="1234";
        String date="2016-10-22T22:25:28+03:00";
        // when
        // then
        Assert.assertEquals("4958044741735035BF516DC083731684", SignatureUtils.generate(date, user, password));
    }
    
/*    
    public static void main(String[] args){
        String userName="U20005";
        String password="1234";
        String operationDate="2016-10-22T22:25:28+03:00";
    	
    	String userAndPassword=userName.toUpperCase()+"/"+password.toUpperCase();
        System.out.println(DigestUtils.md5Hex(operationDate + DigestUtils.md5Hex(userAndPassword).toUpperCase()).toUpperCase());
    }
*/
    
}
