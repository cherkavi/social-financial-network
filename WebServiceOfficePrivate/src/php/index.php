<?php
	include_once 'IBookkeeping.php';
	include_once 'IEventNotifier.php';
	include_once 'IMessageFromParent.php';
	include_once 'IRatings.php';
	include_once 'IRegistration.php';
	include_once 'IUsersParent.php';
	include_once 'IAccountSuperBon.php';
	include_once 'ICommodityInformation.php';
	include_once 'IAccountIbon.php';

	$server = 'http://localhost:8080/WebServiceOfficePrivate';
	$ibookkeeping=new ibookkeeping($server."/ibookkeeping?wsdl");
	$ieventnotifier=new ieventnotifier($server."/ieventnotifier?wsdl");
	$imessagefromparent=new imessagefromparent($server."/imessagefromparent?wsdl");
	$iratings=new iratings($server."/iratings?wsdl");
	$iregistration=new iregistration($server."/iregistration?wsdl");
	$iusersparent=new iusersparent($server."/iusersparent?wsdl");
	$iaccountsuperbon=new iaccountsuperbon($server."/iaccountsuperbon?wsdl");
	$icommodityinformation=new icommodityinformation($server."/icommodityinformation?wsdl");
	$iaccountibon=new iaccountibon($server."/iaccountibon?wsdl");

	// ibookkeeping
/*
	print_r($ibookkeeping->getTransactionSize("9DFF579878161755", 
											  346, 
											  1, 
											  "2008-08-12", 
											  "2008-08-14"));
	echo "<br /> <br />";
	$response=$ibookkeeping->getTransactions("9DFF579878161755", 
											  346, 
											  1, 
											  "2008-08-12T12:00:00", 
											  "2008-08-14T12:00:00", 
											  0, 
											  5, 
											  "");
	print_r($response);
	echo "<br /> <br />";
	#echo urldecode($response->return[0]->NAME_SERVICE_PLACE);
*/

	// $ieventnotifier
/*
	$response=$ieventnotifier->notifyServerAboutSendPasswordBySms("+380979204671");
	print_r($response);
	echo "<br />";
	
	$response=$ieventnotifier->sendLoginAndPasswordByMail("9DFF579878161755");
	print_r($response);
	echo "<br />";
	
	$response=$ieventnotifier->sendLoginAndPasswordByMailByEmail("sergikot@nmtg.com.ua");
	print_r($response);
	echo "<br />";
*/
	
	// $imessagefromparent
/*
	$response=$imessagefromparent->setMessageAsReaded(101);
	print_r($response);
	echo "<br />";

	$response=$imessagefromparent->getNewMessages(array(102, 103, 104));
	print_r($response);
	echo "<br />";
	
	$response=$imessagefromparent->sendMessageToAdmin(150, "title of the message", "some text");
	print_r($response);
	echo "<br />";
*/
	
	// $iratings
/*
	$response=$iratings->getUserRatings();
	print_r($response);
	echo "<br />";
*/
	
	// $iregistration
/*
	$response=$iregistration->getNewClient("26004134314","12345", "1990-08-12T12:00:00","test@mail.ru","+380501501234");
	echo urldecode($response->return->errorMessage);
	print_r($response);
	echo "<br />";
*/
	
/*	
	echo('$iusersparent <br />');
	$response=$iusersparent->getUserParentByBoncardNumber("26004134314","12345");
	echo urldecode($response->return->errorMessage);
	print_r($response);
	echo '<br />';
*/

/*
	$response=$iusersparent->isUserParentByEmail("sergikot@nmtg.com.ua");
	print_r($response);
	echo "<br />";
*/

	// 4. Получение перечня кошельков бон-карты
/*
	$response=$iusersparent->getPurseOfBonCard("9DFF579878161755", 346, 1);
	print_r($response);
	echo "<br />";
*/

		
	// Работа со счетом типа Боны:
	// 1. Получение баланса - need to have the example 
	
	// 5. Получение информации по товарам
	// 1. Получение информации по коду бон-карты
/*
	$response=$icommodityinformation->getInformationByBonCard("9DFF579878161755", 346, 1);
	print_r($response);
	echo "<br />";
*/

	// 2. Получение информации по ИД транзакции
/*
	$response=$icommodityinformation->getInformationByIdTransaction(14024602);
	print_r($response);
	echo "<br />";
*/
	
	// Работа со счетом типа Боны: 
	// 1. Получение баланса  ??? нет тестовых переменных
	
	// Работа со счетом типа Супербоны:
	// 1. Получение баланса
/*
	$response=$iaccountsuperbon->getBalance(14);
	print_r($response);
	echo "<br />";
*/
	
	// 2. Получение информации по истории пополнения и расходах
/*
	$response=$iaccountsuperbon->getPurseHistory(4);
	print_r($response);
	echo "<br />";
*/
	
	// 3. Получение информации по подаркам
/*
	$response=$iaccountsuperbon->getGiftInformation(14);
	print_r($response);
	echo "<br />";
*/
	
	// 4. Передача заказа подарка
/*
	$response=$iaccountsuperbon->sendOrderOfGift(718336, 8, 76);
	print_r($response);
	echo "<br />";
*/
	
	// 5. Get account information
/*
	$response=$iaccountibon->getBalance(14);
	print_r($response);
	echo "<br />";
*/	
/*
	$response=$iaccountibon->getPurseHistory(4);
	print_r($response);
	echo "<br />";
*/
?>