<?php

	session_start();

	require __DIR__.'/vendor/autoload.php';
	use phpish\shopify;

	require __DIR__.'/conf.php';

	$shopify = shopify\client(SHOPIFY_SHOP, SHOPIFY_APP_API_KEY, SHOPIFY_APP_PASSWORD, true);

	try
	{
		# Making an API request can throw an exception
		$products = $shopify('GET /admin/products.json', array('published_status'=>'published'));
		if($products)
		{
			$arr = array('status' => 1, 'message' =>"Get all Products", "data"=>$products); 
	        header('Content-Type: application/json');
            echo json_encode($arr);
		}
		else
		{
			$arr = array('status' => 0, 'message' => "No products found."); 
            header('Content-Type: application/json');      
            echo json_encode($arr);
		}
			
	}
	catch (shopify\ApiException $e)
	{
		# HTTP status code was >= 400 or response contained the key 'errors'
		echo $e;
		print_R($e->getRequest());
		print_R($e->getResponse());
	}
	catch (shopify\CurlException $e)
	{
		# cURL error
		echo $e;
		print_R($e->getRequest());
		print_R($e->getResponse());
	}

?>
