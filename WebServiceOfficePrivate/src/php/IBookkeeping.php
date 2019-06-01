<?php

class ibookkeeping{
    private $proxy;

    public function __construct($pUrl){
        $this->proxy = new SoapClient($pUrl);
    }

    public function getTransactionSize($cardSerialNumber, $idEmitentCard, $idPaySystemCard, $dateBegin, $dateEnd){
        $userData->cardSerialNumber=new SoapVar(urlencode($cardSerialNumber), XSD_STRING);
        $userData->idEmitentCard=new SoapVar($idEmitentCard, XSD_INT);
        $userData->idPaySystemCard=new SoapVar($idPaySystemCard, XSD_INT);
        $userData->dateBegin=new SoapVar($dateBegin, XSD_DATETIME);
        $userData->dateEnd=new SoapVar($dateEnd, XSD_DATETIME);
        return $this->proxy->__soapCall("getTransactionSize", array($userData) );
    }
    public function getTransactions($cardSerialNumber, $idEmitentCard, $idPaySystemCard, $dateBegin, $dateEnd, $begin, $count, $orderValue){
        $userData->cardSerialNumber=new SoapVar(urlencode($cardSerialNumber), XSD_STRING);
        $userData->idEmitentCard=new SoapVar($idEmitentCard, XSD_INT);
        $userData->idPaySystemCard=new SoapVar($idPaySystemCard, XSD_INT);
        $userData->dateBegin=new SoapVar($dateBegin, XSD_DATETIME);
        $userData->dateEnd=new SoapVar($dateEnd, XSD_DATETIME);
        $userData->begin=new SoapVar($begin, XSD_INT);
        $userData->count=new SoapVar($count, XSD_INT);
        $userData->orderValue=new SoapVar(urlencode($orderValue), XSD_STRING);
        return $this->proxy->__soapCall("getTransactions", array($userData) );
    }
}

?>