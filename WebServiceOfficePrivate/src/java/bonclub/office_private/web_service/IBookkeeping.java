package bonclub.office_private.web_service;

import bonclub.office_private.web_service.common_objects.BookkeepingTransactionItem;
import java.util.Date;

import javax.jws.WebMethod; 
import javax.jws.WebParam; 
import javax.jws.WebService; 

@WebService
public interface IBookkeeping{
    @WebMethod
    public int getTransactionSize(
        @WebParam(name="cardSerialNumber") String cardSerialNumber, 
        @WebParam(name="idEmitentCard") Integer idEmitentCard, 
        @WebParam(name="idPaySystemCard") Integer idPaySystemCard, 
        @WebParam(name="dateBegin") Date dateBegin, 
        @WebParam(name="dateEnd") Date dateEnd 
        );
    @WebMethod
    public BookkeepingTransactionItem[] getTransactions(
        @WebParam(name="cardSerialNumber") String cardSerialNumber, 
        @WebParam(name="idEmitentCard") Integer idEmitentCard, 
        @WebParam(name="idPaySystemCard") Integer idPaySystemCard, 
        @WebParam(name="dateBegin") Date dateBegin, 
        @WebParam(name="dateEnd") Date dateEnd, 
        @WebParam(name="begin") int begin, 
        @WebParam(name="count") int count, 
        @WebParam(name="orderValue") String orderValue 
        );

}