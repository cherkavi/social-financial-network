<?php

class imessagefromparent{
    private $proxy;

    public function __construct($pUrl){
        $this->proxy = new SoapClient($pUrl);
    }

    public function setMessageAsReaded($messageId){
        $userData->messageId=new SoapVar($messageId, XSD_INT);
        return $this->proxy->__soapCall("setMessageAsReaded", array($userData) );
    }
    public function getNewMessages($userId){
        $userData->userId=new SoapVar($userId, XSD_INT);
        return $this->proxy->__soapCall("getNewMessages", array($userData) );
    }
    public function sendMessageToAdmin($idNatPrs, $titleMessage, $textMessage){
        $userData->idNatPrs=new SoapVar($idNatPrs, XSD_INT);
        $userData->titleMessage=new SoapVar(urlencode($titleMessage), XSD_STRING);
        $userData->textMessage=new SoapVar(urlencode($textMessage), XSD_STRING);
        return $this->proxy->__soapCall("sendMessageToAdmin", array($userData) );
    }
}

?>