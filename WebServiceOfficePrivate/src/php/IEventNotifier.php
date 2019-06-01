<?php

class ieventnotifier{
    private $proxy;

    public function __construct($pUrl){
        $this->proxy = new SoapClient($pUrl);
    }

    public function notifyServerAboutSendPasswordBySms($phoneNumber){
        $userData->phoneNumber=new SoapVar(urlencode($phoneNumber), XSD_STRING);
        return $this->proxy->__soapCall("notifyServerAboutSendPasswordBySms", array($userData) );
    }
    public function sendLoginAndPasswordByMail($boncardNumber){
        $userData->boncardNumber=new SoapVar(urlencode($boncardNumber), XSD_STRING);
        return $this->proxy->__soapCall("sendLoginAndPasswordByMail", array($userData) );
    }
    public function sendLoginAndPasswordByMailByEmail($email){
        $userData->email=new SoapVar(urlencode($email), XSD_STRING);
        return $this->proxy->__soapCall("sendLoginAndPasswordByMailByEmail", array($userData) );
    }
}

?>