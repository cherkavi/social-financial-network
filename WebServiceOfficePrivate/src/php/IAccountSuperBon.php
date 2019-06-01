<?php
class iaccountsuperbon{
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

    public function getGiftInformation($id_club_card_purse){
        $userData->id_club_card_purse=new SoapVar($id_club_card_purse, XSD_INT);
        return $this->proxy->__soapCall("getGiftInformation", array($userData) );
    }

    public function sendOrderOfGift($p_id_nat_prs, $p_id_club_card_purse, $p_id_club_event_gift){
        $userData->p_id_nat_prs=new SoapVar($p_id_nat_prs, XSD_INT);
        $userData->p_id_club_card_purse=new SoapVar(urlencode($p_id_club_card_purse), XSD_STRING);
        $userData->p_id_club_event_gift=new SoapVar(urlencode($p_id_club_event_gift), XSD_STRING);
        return $this->proxy->__soapCall("sendOrderOfGift", array($userData) );
    }

}
?>