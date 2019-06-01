<?php
class iaccountibon{
    private $proxy;

    public function __construct($pUrl){
        $this->proxy = new SoapClient($pUrl);
    }

    public function getBalance($id_club_card_purse){
        $userData->id_club_card_purse=new SoapVar($id_club_card_purse, XSD_INT);
        return $this->proxy->__soapCall("getBalance", array($userData) );
    }
    public function getPurseHistory($id_club_card_purse){
        $userData->id_club_card_purse=new SoapVar($id_club_card_purse, XSD_INT);
        return $this->proxy->__soapCall("getPurseHistory", array($userData) );
    }
}

?>