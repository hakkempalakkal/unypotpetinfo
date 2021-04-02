<?php

	if( $_SERVER["REQUEST_METHOD"] == "POST" ) {

		$form_error = array();

		function check_input($data) {
			$data = trim($data);
			$data = stripslashes($data);
			$data = htmlspecialchars($data);
			$bad = array("content-type","bcc:","to:","cc:","href");
			return str_replace($bad,"",$data);
		}

		// validation of data

		if ( isset( $_POST['name'] ) && $_POST['name'] != '' ) {

			$name = filter_var(check_input($_POST['name']), FILTER_SANITIZE_STRING); // required
		} else {

			$form_error['name'] = "Please enter your name.";
		}

		if ( isset( $_POST['email'] ) && $_POST['email'] != '' ) {

			$email_from = filter_var($_POST['email'], FILTER_SANITIZE_EMAIL); // required

		} else {

			$form_error['email'] = "Please enter your email.";
		}

		if ( isset( $_POST['subject'] ) && $_POST['subject'] != '' ) {

			$subject = filter_var(check_input($_POST['subject']), FILTER_SANITIZE_STRING); // required
		} else {

			$form_error['subject'] = "Please enter a subject.";
		}

		if ( isset( $_POST['message'] ) && $_POST['message'] != '' ) {

			$message = filter_var(check_input($_POST['message']), FILTER_SANITIZE_STRING); // required
		} else {

			$form_error['message'] = "Please enter some message.";
		}

		if( $form_error == [] ) {


			$email_to = "samyotech@gmail.com";
			$email_subject = "Contact Request - PetInfo";

			$email_message .= "Name: ".ucwords($name)."\n";
			$email_message .= "Email: ".$email_from."\n";
			$email_message .= "Subject: ".$subject."\n";
			$email_message .= "Message: ".$message."\n";
		
			// create email headers
			$headers = 'From: '.$email_from;

			if( mail($email_to, $email_subject, $email_message, $headers) ) {

				$result = array(
							"status" 	=> "success",
							"response"	=> "Thank you, ".ucwords($name)."<br/>We will be in touch with you very soon."
						);
			} else {

				$result = array(
							"status" 	=> "fail",
							"response"	=> "Sorry! some error occurred please try again later."
						);
			}
		} else {

			$result = array(
						"status" 	=> "error",
						"response"	=> $form_error
					);
		}

		echo json_encode($result);
	} else {

		header("Location: http://PetInfo.in");
		die();
	}

?>