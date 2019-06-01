<?php 

class iregistration{
    private $proxy;

    public function __construct($pUrl){
        $this->proxy = new SoapClient($pUrl);
    }

    public function getNewClient($bonCardNumber, $password, $birthDay, $email, $phone){
        $userData->bonCardNumber=new SoapVar(urlencode($bonCardNumber), XSD_STRING);
        $userData->password=new SoapVar(urlencode($password), XSD_STRING);
        $userData->birthDay=new SoapVar($birthDay, XSD_DATETIME);
        $userData->email=new SoapVar(urlencode($email), XSD_STRING);
        $userData->phone=new SoapVar(urlencode($phone), XSD_STRING);
        return $this->proxy->__soapCall("getNewClient", array($userData) );
    }
}

?>