<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Login extends CI_Controller
{

    public function __construct()
    {
        parent::__construct();
        $this -> load -> library('session');
        $this -> load -> helper('form');
        $this -> load -> helper('url');
        $this -> load -> database();
        $this -> load -> library('form_validation');
        $this -> load -> model('Comman_model');

    }

    public function index()
    {
        $this -> load ->view('login.php');
    }
    public function signup()
    {
        $this -> load -> view('common/head.php');
        $this -> load ->view('signup.php');
        $this -> load -> view('common/footer.php');
    }

    public function forgotpassword()
    {
        $this -> load -> view('common/head.php');
        $this->load ->view('forgotpassword.php');
        $this -> load -> view('common/footer.php');
    }

     /*public function process()  
    {  
        $user = $this->input->post('user');  
        $pass = $this->input->post('pass');  
        if ($user=='juhi' && $pass=='123')   
        {  
            //declaring session  
            $this->session->set_userdata(array('user'=>$user));  
            $this->load->view('welcome_view');  
        }  
        else{  
            $data['error'] = 'Your Account is Invalid';  
            $this->load->view('login_view', $data);  
        }  
    }  */
}