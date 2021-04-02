<html>
<head>
	<title>SignUp</title>
</head>
<body>

  <?php echo form_open_multipart('Webservice/signup_api'); ?> 

    <h1>Url is :  <?php echo base_url('Webservice/signup_api'); ?> </h1>
    This is Post api<br><br>
    fullname* <input type="text " name="fullname" ><br>
    mobile_no* <input type="text " name="mobile_no" ><br>
    email*<input type="text" name="email"><br>
    password*<input type="password" name="password"><br>
    image*<input type="file" name="image"><br>
    <input type="submit" value="Signup"><br>

</form>
 

</body>
</html>