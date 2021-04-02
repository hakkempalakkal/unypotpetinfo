<!DOCTYPE html>
<html lang="en">
<head>
  <title>Payment</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
	<body>
	<div class="row"> 
		<div class="container">
		<div class="col-md-4 col-sm-12 col-lg-4"></div>
		<div class="col-md-4 col-sm-12 col-lg-4">
			<div style="text-align: center; padding-top: 50px;">
				<form action='https://www.paypal.com/cgi-bin/webscr' method='post' name='frmPayPal1'>
					<input type='hidden' name='business' value='paypal@pet-fiesta.com'>
					<input type='hidden' name='cmd' value='_xclick'>
					<input type='hidden' name='item_name' value='<?php echo $user_name; ?>'>
					<input type='hidden' name='order_id' value='<?php echo $order_id
					; ?>'>
					<input type='hidden' name='amount' value='<?php echo $amount
					; ?>'>
					<input type='hidden' name='user_id' value='<?php echo $user_id; ?>'>
					<div class="card">
					  <h5><b>Welcome to PETEATS Payment</b></h5>
					</div>
					<input type='hidden' name='no_shipping' value='0'>
					<input type='hidden' name='currency_code' value='<?= $currency;?>'>
					<input type='hidden' name='handling' value='0'>
					<input type='hidden' name='cancel_return' value="<?php echo base_url('/Paypal/paymentfailure'); ?>">
					<input type='hidden' id="url" name='return' value="<?php echo base_url('/Paypal/paymentsuccess?order_id=')?><?php echo $order_id; ?>&user_id=<?php echo $user_id ?>&amount=<?php echo $amount; ?>">
					<!-- <input type="image" src="https://www.sandbox.paypal.com/en_US/i/btn/btn_buynowCC_LG.gif" border="0" name="submit" alt="PayPal - The safer, easier way to pay online!">
					<img alt="" border="0" src="https://www.sandbox.paypal.com/en_US/i/scr/pixel.gif" width="1" height="1"> -->
					<input type="submit" id="myBtn" onclick=" return myFunction()" class="btn btn-sm btn-primary" name="submit" value="Pay Now">
				</form> 
			</div>
			</div>
		</div>
		</div>
	</body>
	<script>
		function myFunction() {
		    var amt = document.getElementById("amount").value;
		      var url = "<?php echo base_url('/Webservices/paymentsuccess?order_id=')?><?php echo $order_id; ?>&user_id=<?php echo $user_id ?>&amount=";
		      document.getElementById("url").value ='';
		      document.getElementById("url").value= url+amt;
		   alert(url);
			return true;

		}
	</script>
</html>