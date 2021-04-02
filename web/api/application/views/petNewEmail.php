<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Email/title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<body>
    <div class="email_data" style="text-align: center;align-content: center;color: black;
            font-size: 18px;">
        <p>Dear (<?= $userName;?>),</p>
        <p>Thank you for signing up on PETEATS.</p>
        <p>Warm Greetings! If you have any queries or suggestions, just get in touch with us - peteatsind@gmail.com</p>
        <p><?php  echo $msg; ?></p>
        <p>Team PETEATS</p>
        <img class="img-responsive image" style="width: 100%;height: auto;" src="<?php echo base_url();?>assets/images/banner.png">
    </div>
    
</body>
</html>