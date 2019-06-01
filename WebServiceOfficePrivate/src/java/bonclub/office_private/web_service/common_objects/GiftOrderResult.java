package bonclub.office_private.web_service.common_objects;

import java.io.Serializable;
import java.math.BigDecimal;

public class GiftOrderResult implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private BigDecimal p_id_nat_prs_gift_request;
	private String p_result_msg;
	
	public BigDecimal getP_id_nat_prs_gift_request() {
		return p_id_nat_prs_gift_request;
	}
	public void setP_id_nat_prs_gift_request(BigDecimal pIdNatPrsGiftRequest) {
		p_id_nat_prs_gift_request = pIdNatPrsGiftRequest;
	}
	public String getP_result_msg() {
		return p_result_msg;
	}
	public void setP_result_msg(String pResultMsg) {
		p_result_msg = pResultMsg;
	}
	
	
}
