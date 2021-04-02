<html>
<head>
	<title>add_documents</title>
</head>
<body>

  <?php echo form_open_multipart('Webservice/add_documents'); ?> 

    <h1>Url is :  <?php echo base_url('Webservice/add_documents'); ?> </h1>
    This is Post api<br><br>
    email*<input type="text" name="email"><br>
    image*<input type="file" name="image"><br>
    <input type="submit" value="submit"><br>
</form>
</body>
</html>