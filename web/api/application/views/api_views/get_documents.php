<html>
<head>
	<title>SignUp</title>
</head>
<body>

  <?php echo form_open_multipart('Webservice/get_documents'); ?> 

    <h1>Url is :  <?php echo base_url('Webservice/get_documents'); ?> </h1>
    This is Post api<br><br>
    email*<input type="text" name="email"><br>
    <input type="submit" value="Signup"><br>

</form>
 

</body>
</html>