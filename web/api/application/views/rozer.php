<form action="/purchase" method="POST">

<script
    src="https://checkout.razorpay.com/v1/checkout.js"
    data-key="rzp_live_rPgZ7YS0Ruka2r"
    data-amount="5000"
    data-buttontext="Pay with Razorpay"
    data-name="Merchant Name"
    data-description="Purchase Description"
    data-image="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRg8QEeGSbjvkfRassM8VmwMYQrrSFpsaYOoGr2rTXhCUdw0SdG"
    data-prefill.name="Gaurav Kumar"
    data-prefill.email="samyotechindore@gmail.com"
    data-theme.color="#F37254">
   </script>

</form>

<!-- <button id="rzp-button1">Pay</button>
<script src="https://checkout.razorpay.com/v1/checkout.js"></script>
<script>
var options = {
    "key": "rzp_test_bhyVZKEXHJ6wV4",
    "amount": "2000", // 2000 paise = INR 20
    "name": "Merchant Name",
    "description": "Purchase Description",
    "image": "/your_logo.png",
    "handler": function (response){
        //alert(response.razorpay_payment_id);
        if(response.razorpay_payment_id){
             location.assign('<?= base_url('WebService/payusuccess')?>');
        }
    },
    "prefill": {
        "name": "Gaurav Kumar",
        "email": "test@test.com"
    },
    "notes": {
        "address": "Hello World"
    },
    "theme": {
        "color": "#F37254"
    }
};
var rzp1 = new Razorpay(options);

document.getElementById('rzp-button1').onclick = function(e){
    rzp1.open();
    e.preventDefault();
}
</script> -->