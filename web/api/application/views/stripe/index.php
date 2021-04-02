	<!DOCTYPE html>
	<html>
		<head>
			<title>Add Money</title>
			<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">

		</head>
	<body>
	<div class="col-sm-12"><br/>
	<h3>Add Money</h3><br/>
	<form action="/your-charge-code" method="POST" id="payment-form" class="form-inline"> 
		<!--  <?php $attributes = array('id' => 'payment-form', 'class'=>'form-inline'); echo form_open_multipart('Stripe/Payment/process', $attributes); ?>  -->
		  <span class="payment-errors"></span>
		<div class="col-sm-12">
		  <div class="form-row form-group">
			<label>
			  <span>Card Number</span>
			   <input type="hidden" class="form-control fullname" name="fullname" value="tetet" required>
			  <input type="text" size="20" class="form-control" data-stripe="number" required>
			</label>
		  </div>
		</div>
		<div class="col-sm-12">
		  <div class="form-row form-group">
			<label>
			  <span>Expiration (MM/YY)</span>
			  <input type="text" size="2" class="form-control" data-stripe="exp_month" required>
			
			<span> / </span>
			<input type="text" size="2" class="form-control" data-stripe="exp_year" required>
			</label>
		  </div>
		</div>
		<div class="col-sm-12">
		  <div class="form-row form-group">
			<label>
			  <span>CVC</span>
			  <input type="text" size="4" class="form-control" data-stripe="cvc" required>
			</label>
		  </div>
			</div>
			<div class="col-sm-12">
		 <!--  <div class="form-row form-group">
			<label>
			  <span>Billing Zip</span>
			  <input type="text" size="6" class="form-control" data-stripe="address_zip" required>
			</label>
		  </div> -->
			</div>
			<div class="col-sm-12"><br/><br/>
			<input type="submit" class="submit btn btn-primary" value="Submit Payment">
		  </div>
	</form>
	</div>
	<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
	<script type="text/javascript" src="https://js.stripe.com/v2/"></script>

	<script type="text/javascript">
	  Stripe.setPublishableKey('pk_test_zmMa3W0VdpXcRpdmaPtQhCRL');
	  $(function() {
	  	/*var fullname = $(".fullname").val();
	  	console.log(fullname);*/
	  var $form = $('#payment-form');
	  $form.submit(function(event) {

		// Disable the submit button to prevent repeated clicks:
		$form.find('.submit').prop('disabled', true);
		$form.find('.submit').val('Please wait...');

		// Request a token from Stripe:
		Stripe.card.createToken($form, stripeResponseHandler);
		// Prevent the form from being submitted:
		return false;
	  });
	});
	 function stripeResponseHandler(status, response) {
		 
		 if (response.error) {
			alert(response.error.message);
		 } else {
			$.ajax({
				url: '<?php echo base_url('Stripe/Payment/process');?>',
				data: {access_token: response.id},
				type: 'POST',
				dataType: 'JSON',
				success: function(response){
					console.log(response);
					if(response.success)
					window.location.href="<?php echo base_url('Stripe/Payment/success'); ?>";
				},
				error: function(error){
					console.log(error);
				}
			});
			//console.log(response.id);
		}
	 }
	</script>

    </body>
</html>
