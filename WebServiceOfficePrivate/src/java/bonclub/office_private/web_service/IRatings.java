package bonclub.office_private.web_service;

import bonclub.office_private.web_service.common_objects.RatingsUser;
import bonclub.office_private.web_service.result.WsReturnValue;

import javax.jws.WebMethod; 
import javax.jws.WebService; 

@WebService
public interface IRatings{
    @WebMethod
    public WsReturnValue<RatingsUser[]> getUserRatings(
);

}