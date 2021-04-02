<?php
	$curl = curl_init();
	curl_setopt_array($curl, array(
	    CURLOPT_RETURNTRANSFER => 1,
	    CURLOPT_URL => 'http://139.59.88.66/petEats/WebService/sendReminderNotification'
	));
 		$result = curl_exec($curl );
        curl_close( $curl );
?>	