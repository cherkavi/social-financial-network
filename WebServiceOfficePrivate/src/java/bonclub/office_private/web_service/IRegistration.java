package bonclub.office_private.web_service;

import javax.jws.WebService;
import javax.jws.WebMethod;
import bonclub.office_private.web_service.common_objects.RegistrationResponse;
import bonclub.office_private.web_service.result.WsReturnValue;

import java.util.Date;
import javax.jws.WebParam;

@WebService
public interface IRegistration{
    @WebMethod
    public WsReturnValue<RegistrationResponse> getNewClient(
        @WebParam(name="bonCardNumber") String bonCardNumber, 
        @WebParam(name="password") String password, 
        @WebParam(name="birthDay") Date birthDay, 
        @WebParam(name="email") String email, 
        @WebParam(name="phone") String phone);

}