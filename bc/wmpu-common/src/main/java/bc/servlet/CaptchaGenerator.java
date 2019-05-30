package bc.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.octo.captcha.service.CaptchaServiceException;

import BonCard.Reports.Reporter;
import bc.captcha.CaptchaService;

public class CaptchaGenerator extends JndiParamHttpServlet {

	private static final long serialVersionUID = 1L;

	private final static Logger LOGGER = Logger.getLogger(Reporter.class);

	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			byte[] captchaBytes=CaptchaService.INSTANCE.generateImage(request);
			writeImageAsResponse(response, captchaBytes);
		} catch (CaptchaServiceException cse) {
			LOGGER.error("Can't generate captcha ", cse);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Problem generating captcha image.");
			return;
		} catch (IOException ioe) {
			LOGGER.error("Can't " + ioe.getMessage());
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Problem writing captcha image.");
			return;
		}
	}
	
	private void writeImageAsResponse(HttpServletResponse response, byte[] captchaBytes) throws IOException {
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/" + CaptchaService.INSTANCE.getImageType());

		ServletOutputStream outputStream = response.getOutputStream();
		outputStream.write(captchaBytes);
		outputStream.flush();
		outputStream.close();
	}

}
