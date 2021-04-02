<?php
$order_id=$_POST['order_id'];
session_start();
	
	require __DIR__.'/vendor/autoload.php';
	use phpish\shopify;
	require __DIR__.'/conf.php';

	$shopify_key = shopify\client(SHOPIFY_SHOP, SHOPIFY_APP_API_KEY, SHOPIFY_APP_PASSWORD, true);
		# Making an API request can throw an exception
		$products = $shopify_key('GET /admin/orders/'.$order_id.'.json', array('published_status'=>'published'));
	
		if($products)
		{
			$arr = array('status' => 1, 'message' =>"Get order deatil.", "data"=>$products); 
	        header('Content-Type: application/json');
            echo json_encode($arr);
		}
		else
		{
			$arr = array('status' => 0, 'message' => "No products found."); 
            header('Content-Type: application/json');      
            echo json_encode($arr);
		}
?>