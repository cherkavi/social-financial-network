package bonclub.office_private.web_service;

import java.util.List;

import bonclub.office_private.web_service.common_objects.BalanceResult;
import bonclub.office_private.web_service.common_objects.PurseHistory;

import javax.jws.WebMethod; 
import javax.jws.WebParam; 
import javax.jws.WebService; 

@WebService
public interface IAccountIbon{
    @WebMethod
    public BalanceResult getBalance(
        @WebParam(name="id_club_card_purse") Integer id_club_card_purse);
    @WebMethod
    public List<PurseHistory> getPurseHistory(
        @WebParam(name="id_club_card_purse") Integer id_club_card_purse);

}