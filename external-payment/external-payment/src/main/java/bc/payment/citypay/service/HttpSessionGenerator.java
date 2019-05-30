package bc.payment.citypay.service;

import java.util.Random;

public class HttpSessionGenerator {
	private static Random random=new Random();
	
	public static String generateHttpSessionId(){
		return Integer.toString(random.nextInt());
	}
}
