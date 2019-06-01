package bonclub.office_private.web_service;

import java.util.List;

import bonclub.office_private.web_service.common_objects.GiftInformation;
import bonclub.office_private.web_service.common_objects.GiftOrderResult;
import bonclub.office_private.web_service.common_objects.Purse;
import bonclub.office_private.web_service.common_objects.PurseHistory;

import javax.jws.WebMethod; 
import javax.jws.WebParam; 
import javax.jws.WebService; 

@WebService
public interface IAccountSuperBon{
    @WebMethod
    public List<Purse> getBalance(
        @WebParam(name="id_club_card_purse") Integer id_club_card_purse);
    @WebMethod
    public List<PurseHistory> getPurseHistory(
        @WebParam(name="id_club_card_purse") Integer id_club_card_purse);

    @WebMethod
    public List<GiftInformation> getGiftInformation(
        @WebParam(name="id_club_card_purse") Integer id_club_card_purse);
    
    @WebMethod
    public GiftOrderResult sendOrderOfGift(
    	@WebParam(name="p_id_nat_prs") Integer p_id_nat_prs, 
    	@WebParam(name="p_id_club_card_purse") String p_id_club_card_purse, 
    	@WebParam(name="p_id_club_event_gift") String p_id_club_event_gift);
    
}