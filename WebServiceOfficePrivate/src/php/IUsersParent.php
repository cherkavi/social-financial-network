<?php

class iusersparent{
    private $proxy;

    public function __construct($pUrl){
        $this->proxy = new SoapClient($pUrl);
    }

    public function getUserParentByBoncardNumber($boncardNumber, $password){
        $userData->boncardNumber=new SoapVar(urlencode($boncardNumber), XSD_STRING);
        $userData->password=new SoapVar(urlencode($password), XSD_STRING);
        return $this->proxy->__soapCall("getUserParentByBoncardNumber", array($userData) );
    }
    public function isUserParentByEmail($email){
        $userData->email=new SoapVar(urlencode($email), XSD_STRING);
        return $this->proxy->__soapCall("isUserParentByEmail", array($userData) );
    }
/*
    public function updateObjectInDatabase($usersParent){
        $userData->usersParent=new SoapVar($usersParent, XSD_);
        return $this->proxy->__soapCall("updateObjectInDatabase", array($userData) );
    }
*/

    public function getPurseOfBonCard($card_serial_number, $id_issuer, $id_payment_system){
        $userData->card_serial_number=new SoapVar(urlencode($card_serial_number), XSD_STRING);
        $userData->id_issuer=new SoapVar($id_issuer, XSD_INT);
        $userData->id_payment_system=new SoapVar($id_payment_system, XSD_INT);
        return $this->proxy->__soapCall("getPurseOfBonCard", array($userData) );
    }

}

?>