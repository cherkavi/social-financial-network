<?php
class icommodityinformation{
    private $proxy;

    public function __construct($pUrl){
        $this->proxy = new SoapClient($pUrl);
    }

    public function getInformationByBonCard($card_serial_number, $id_issuer, $id_payment_system){
        $userData->card_serial_number=new SoapVar(urlencode($card_serial_number), XSD_STRING);
        $userData->id_issuer=new SoapVar($id_issuer, XSD_INT);
        $userData->id_payment_system=new SoapVar($id_payment_system, XSD_INT);
        return $this->proxy->__soapCall("getInformationByBonCard", array($userData) );
    }
    public function getInformationByIdTransaction($id_trans){
        $userData->id_trans=new SoapVar($id_trans, XSD_INT);
        return $this->proxy->__soapCall("getInformationByIdTransaction", array($userData) );
    }
}

?>