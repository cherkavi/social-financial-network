binary
	- ������� � ������� WAR ������

php_part
	- ������ ���������� �� PHP ( ����� ���������������� ������ - ���������������� ��� )
	
generated_data
	- �����, ������� �������� �����������-������ �� ������ �� ���������� 


=======================================================
PHP
	����������� ������ ���� ����������� ������ PHP_SOAP
		extension=php_soap.dll

	php.ini
	[soap]
; Enables or disables WSDL caching feature.
; http://php.net/soap.wsdl-cache-enabled
soap.wsdl_cache_enabled=0

; Sets the directory name where SOAP extension will put cache files.
; http://php.net/soap.wsdl-cache-dir
soap.wsdl_cache_dir="/tmp"

; (time to live) Sets the number of second while cached file will be used 
; instead of original one.
; http://php.net/soap.wsdl-cache-ttl
soap.wsdl_cache_ttl=86400

; Sets the size of the cache limit. (Max. number of WSDL files to cache)
soap.wsdl_cache_limit = 5

=======================================================
�������� �� �������� ���������� ��������� ������ ����� ��������������:
java -> PHP
	java: URLEncoder.encode( String s )
	php:  urldecode($returnValue->p_card_name_issuer)

PHP -> java
	php: urlencode( $value )
	java: URLDecoder.decode( String s )
=======================================================
Example of the communication between WebService and PHP 

	$wsdl = "http://localhost:8080/TestWeb/ibookkeeping?wsdl";
	$client=new ibookkeeping($wsdl);
									
	print_r($client->getTransactionSize("9DFF579878161755", 
										346, 
										1, 
										"2008-08-12", 
										"2008-08-14"));
	echo "<br /> <br />";
	$response=$client->getTransactions("9DFF579878161755", 
									  346, 
									  1, 
									  "2008-08-12T12:00:00", 
									  "2008-08-14T12:00:00", 
									  0, 
									  5, 
									  "");
	print_r($response);
	echo "<br /> <br />";
	echo urldecode($response->return[0]->NAME_SERVICE_PLACE);
