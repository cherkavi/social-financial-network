<?php

class iratings{
    private $proxy;

    public function __construct($pUrl){
        $this->proxy = new SoapClient($pUrl);
    }

    public function getUserRatings(){
        return $this->proxy->__soapCall("getUserRatings", array() );
    }
}

?>