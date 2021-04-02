<?php
	ob_start();
	session_start();

	require __DIR__.'/vendor/autoload.php';
	use phpish\shopify;

	require __DIR__.'/conf.php';

	// Create connection
	$conn =  mysqli_connect("localhost", "sxfthbeqee", "qtMs3sAfrx", "sxfthbeqee");
	// Check connection
	if (!$conn) {
	    die("Connection failed: " . $conn->connect_error);
	} 
	$email = $_POST['email'];
	$user_id = $_POST['user_id'];
	$variant_id = $_POST['variant_id'];
	$quantity = $_POST['quantity'];
	$title = $_POST['title'];
	$price = $_POST['price'];
	$first_name = $_POST['first_name'];
	$last_name= $_POST['last_name'];
	$address1 = $_POST['address1'];
	$phone = $_POST['phone'];
	$city = $_POST['city'];
	$country = $_POST['country'];
	$zip = $_POST['zip'];
	$image = $_POST['image'];
	
	$shopify = shopify\client(SHOPIFY_SHOP, SHOPIFY_APP_API_KEY, SHOPIFY_APP_PASSWORD, true);

	try
	{
		# Making an API request can throw an exception
		$product = $shopify('POST /admin/orders.json', array(), array
		(
			'order' => array
			(
				"email" => $email,
				"line_items" => array
				(
					array
					(
						"variant_id" =>$variant_id,
						"quantity" => $quantity,
						"title"=>$title,
						"price"=>$price
					)
				),
				"shipping_address" => array
				(
					 "first_name"=>$first_name,
				      "last_name"=> $last_name,
				      "address1"=> $address1,
				      "phone"=> $phone,
				      "city"=> $city,
				      "province"=> "Ontario",
				      "country"=> $country,
				      "zip"=> $zip
				)
			)
		));

		if($product)
		{
			$order_id=$product['id'];
        	$sql = "INSERT INTO `shopifyOrders` VALUES ('','".$order_id."', '".$email."','".$user_id."', '".$variant_id."','".$quantity."', '".$title."','".$price."', '".$first_name."','".$last_name."', '".$address1."','".$phone."', '".$city."','".$country."','".$zip."','".$image."','".time()."')";
        	mysqli_query($conn,$sql);
			$arr = array('status' => 1, 'message' => "Order created successfully."); 
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
		// echo $e;
		// print_R($e->getRequest());
		 print_R($e->getResponse());
	}
	catch (shopify\CurlException $e)
	{
		# cURL error
		// echo $e;
		// print_R($e->getRequest());
		// print_R($e->getResponse());
	}

?>