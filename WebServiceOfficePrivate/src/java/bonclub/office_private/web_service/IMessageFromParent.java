package bonclub.office_private.web_service;

import bonclub.office_private.web_service.common_objects.ParentMessage;
import bonclub.office_private.web_service.result.WsReturnValue;

import javax.jws.WebMethod; 
import javax.jws.WebParam; 
import javax.jws.WebService; 

@WebService
public interface IMessageFromParent{
    @WebMethod
    public WsReturnValue<Boolean> setMessageAsReaded(
        @WebParam(name="messageId") Integer messageId);
    @WebMethod
    public WsReturnValue<ParentMessage[]> getNewMessages(
        @WebParam(name="userId") Integer[] userId);
    @WebMethod
    public WsReturnValue<Integer> sendMessageToAdmin(
        @WebParam(name="idNatPrs") int idNatPrs, 
        @WebParam(name="titleMessage") String titleMessage, 
        @WebParam(name="textMessage") String textMessage);

}