<?php
session_start();

	require __DIR__.'/vendor/autoload.php';
	use phpish\shopify;
	require __DIR__.'/conf.php';

$shopify_key = shopify\client(SHOPIFY_SHOP, SHOPIFY_APP_API_KEY, SHOPIFY_APP_PASSWORD, true);

	
		# Making an API request can throw an exception
		$products = $shopify_key('GET /admin/collects.json');
		print_r($products);
	
	
	

?>