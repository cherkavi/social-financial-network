package bonclub.office_private.web_service;


import javax.jws.WebMethod; 
import javax.jws.WebParam; 
import javax.jws.WebService; 

import bonclub.office_private.web_service.result.WsReturnValue;

@WebService
public interface IEventNotifier{
    @WebMethod
    public WsReturnValue<Boolean> notifyServerAboutSendPasswordBySms(
        @WebParam(name="phoneNumber") String phoneNumber);
    @WebMethod
    public WsReturnValue<Boolean> sendLoginAndPasswordByMail(
        @WebParam(name="boncardNumber") String boncardNumber);
    @WebMethod
    public WsReturnValue<Boolean> sendLoginAndPasswordByMailByEmail(
        @WebParam(name="email") String email);

}