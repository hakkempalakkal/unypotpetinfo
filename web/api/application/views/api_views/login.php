<html>
<head>
    <title>Login</title>
</head>
<body>

<?php echo form_open('Webservice/login_api'); ?>

<h1>Url is :  <?php echo base_url('Webservice/login_api'); ?> </h1>
This is Post api<br><br>
email*<input type="text" name="email"><br>
password*<input type="password" name="password"><br>
<input type="submit" value="Login"><br>
</form>
</body>
</html>