package bc.ws.service;

import org.apache.commons.codec.digest.DigestUtils;

public class SignatureUtils {

    public static String generate(String operationDate, String userName, String password){
    	String userAndPassword=userName.toUpperCase()+"/"+password.toUpperCase();
        return DigestUtils.md5Hex(operationDate + DigestUtils.md5Hex(userAndPassword).toUpperCase()).toUpperCase();
    }

}
