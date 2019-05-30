package bc.captcha;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import com.octo.captcha.CaptchaFactory;
import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.FunkyBackgroundGenerator;
import com.octo.captcha.component.image.color.RandomRangeColorGenerator;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.RandomTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.engine.GenericCaptchaEngine;
import com.octo.captcha.image.gimpy.GimpyFactory;
import com.octo.captcha.service.multitype.GenericManageableCaptchaService;

public class CaptchaService {
	
	public static CaptchaService INSTANCE=new CaptchaService();

	private static final String IMAGE_TYPE = "png";

	private static final String CHARS="0123456789";
	private static final int CHAR_COUNT=5;
	private static final int IMAGE_HEIGHT=150;
	private static final int IMAGE_WIDTH=50;
	private static final int FONT_SIZE_MIN=15;
	private static final int FONT_SIZE_MAX=35;

	public static GenericManageableCaptchaService captchaService=null;

	static{
		// captchaService=new GenericManageableCaptchaService(new GenericCaptchaEngine(new CaptchaFactory[]{new GimpyFactory(new RandomWordGenerator("0123456789"), new SimpleWordToImage())}), 5, 1024*1024*1);
		WordGenerator wgen = new RandomWordGenerator(CHARS);
        RandomRangeColorGenerator cgen = new RandomRangeColorGenerator(
             new int[] {0, 100},
             new int[] {0, 100},
             new int[] {0, 100});
        TextPaster textPaster = new RandomTextPaster(new Integer(CHAR_COUNT), new Integer(CHAR_COUNT), cgen, true);

        BackgroundGenerator backgroundGenerator = new FunkyBackgroundGenerator(new Integer(IMAGE_HEIGHT), new Integer(IMAGE_WIDTH));

        Font[] fontsList = new Font[] {
            new Font("Arial", 0, 10),
            new Font("Tahoma", 0, 10),
            new Font("Verdana", 0, 10),
         };

         FontGenerator fontGenerator = new RandomFontGenerator(new Integer(FONT_SIZE_MIN), new Integer(FONT_SIZE_MAX), fontsList);

         WordToImage wordToImage = new ComposedWordToImage(fontGenerator, backgroundGenerator, textPaster);
         
 		 captchaService=new GenericManageableCaptchaService(new GenericCaptchaEngine(new CaptchaFactory[]{new GimpyFactory(wgen, wordToImage)}), 5, 1024*1024*1);
 		 
 		 clearTemporaryData();
	}

	private static void clearTemporaryData(){
		captchaService.emptyCaptchaStore();
		captchaService.garbageCollectCaptchaStore();
	}


	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		clearTemporaryData();
	}
	

	public byte[] generateImage(HttpServletRequest request) throws IOException{
		ByteArrayOutputStream imgOutputStream = new ByteArrayOutputStream();
		// Generate the captcha image.
		BufferedImage challengeImage = captchaService.getImageChallengeForID(request.getSession().getId());
		ImageIO.write(challengeImage, IMAGE_TYPE, imgOutputStream);
		return imgOutputStream.toByteArray();
	}
	
	public boolean checkData(HttpServletRequest request, String humanEnterText){
		return captchaService.validateResponseForID(request.getSession().getId(), humanEnterText);
	}


	public String getImageType() {
		return IMAGE_TYPE;
	}
}
