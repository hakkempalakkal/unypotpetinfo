<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Webservicenew extends CI_Controller
{
    public function __construct()
    {
        parent::__construct();
        $this->load->library('session');
        $this->load->helper('form');
        $this->load->helper('url');
        $this->load->library('image_lib');
        $this->load->library('csvimport');
        $this->load->library('api');
        $this->load->database();
        $this->load->library('form_validation');
        $this->load->model('Comman_model');
        $this->load->model('Api_model');
        header('Access-Control-Allow-Origin: *');
        header("Access-Control-Allow-Methods: GET, POST, OPTIONS, PUT, DELETE");
    }
    
    /*******************************Users Related API's**********************************/
    /*Check User Session*/
    public function checkUserKey($user_key, $user_id)
    {
        $getUser = $this->Api_model->getSingleRow(USR_SIN_TBL, array(
            'user_id' => $user_id,
            'user_key' => $user_key
        ));
        if (!$getUser) {
            $this->api->api_message(0, INVALID_KEY);
            exit();
        } //!$getUser
    }
    
    /*Check User Session*/
    public function checkUserStatus($user_id)
    {
        $getUser = $this->Api_model->getSingleRow(USR_TBL, array(
            'id' => $user_id
        ));
        if (!$getUser) {
            $this->api->api_message(3, "User Not Found");
            exit();
        } //!$getUser
    }
    
    public function send_otp()
    {
        $this->form_validation->set_rules('country_code', 'country_code', 'trim|required');
        $this->form_validation->set_rules('mobile', 'mobile', 'trim|required');
        $this->form_validation->set_rules('otp', 'otp', 'trim|required');
        
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $mobile       = $this->input->post('mobile', TRUE);
        $country_code = $this->input->post('country_code', TRUE);
        $otp          = $this->input->post('otp', TRUE);
        
        $msg        = "Use $otp as one time password (OTP) to verify your Wedding App account. Valid for one hour. Please do not share this OTP to anyone for security reasons.";
        $contact_no = $country_code . $mobile;
        //$this->send_opt_mobile($contact_no, $msg);
        $this->api->api_message(1, "OTP send successfully.");
        exit();
    }
    
    public function send_opt_mobile($mobile_number, $msg)
    {
        $authKey = "205521AaBNspcwGS5ab512aa";
        
        //Multiple mobiles numbers separated by comma
        $mobileNumber = $mobile_number;
        
        //Sender ID,While using route4 sender id should be 6 characters long.
        $senderId = "WEDAPP";
        
        //Your message to send, Add URL encoding here.
        $message = urlencode($msg);
        
        //Define route 
        $route    = "4";
        //Prepare you post parameters
        $postData = array(
            'authkey' => $authKey,
            'mobiles' => $mobileNumber,
            'message' => $message,
            'sender' => $senderId,
            'route' => $route
        );
        
        //API URL
        $url = "https://api.msg91.com/api/sendhttp.php?authkey='$authKey'&mobiles='$mobileNumber'&message='$message'&sender='$senderId'&route=4&country=0";
        
        // init the resource
        $ch = curl_init();
        curl_setopt_array($ch, array(
            CURLOPT_URL => $url,
            CURLOPT_RETURNTRANSFER => true,
            CURLOPT_POST => true,
            CURLOPT_POSTFIELDS => $postData
            //,CURLOPT_FOLLOWLOCATION => true
        ));
        
        //Ignore SSL certificate verification
        curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, 0);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, 0);
        
        //get response
        $output = curl_exec($ch);
        
        //Print error if any
        if (curl_errno($ch)) {
            echo 'error:' . curl_error($ch);
        } //curl_errno($ch)
        
        curl_close($ch);
    }
    
    /*Use Sign In*/
    
    
    public function userAuthontication()
    {
        $mobile       = $this->input->post('mobile', TRUE);
        $country_code = $this->input->post('country_code', TRUE);
        $device_type  = $this->input->post('device_type', TRUE);
        $device_token = $this->input->post('device_token', TRUE);
        
        
        $getdata = $this->Api_model->getSingleRow('user', array(
            'mobile' => $mobile,
            'country_code' => $country_code
            
            
        ));
        
        if ($getdata) {
            if ($getdata->status == 1) {
                if ($getdata->image) {
                    $getdata->image = $this->config->base_url() . $getdata->image;
                } //$getdata->image
                else {
                    $getdata->image = $this->config->base_url() . 'assets/images/dummyuser.png';
                }
                $data = array(
                    'device_type' => strtolower($device_type),
                    'device_token' => $device_token
                );
                $this->Api_model->updateSingleRow("user", array(
                    'mobile' => $mobile
                ), $data);
                // $this->Api_model->responseSuccess(1, "Login successfully.", $getdata);
                $this->api->api_message_data(1, LOGINSUCCESSFULL, 'user_deatils', $getdata);
            } //$getdata->status == 1
        } //$getdata
        else {
            // $this->Api_model->responseFailed(0, "Please enter your valid details");
            $this->api->api_message(0, "Please enter your valid details");
            
        }
    }
    
    /*Login User*/
    public function userVerify()
    {
        $this->form_validation->set_rules('mobile', 'mobile', 'required');
        $this->form_validation->set_rules('country_code', 'country_code', 'required');
        $this->form_validation->set_rules('device_token', 'device_token', 'required');
        $this->form_validation->set_rules('function_code', 'function_code', 'required');
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $mobile        = $this->input->post('mobile', TRUE);
        $country_code  = $this->input->post('country_code', TRUE);
        $device_token  = $this->input->post('device_token', TRUE);
        $device_type   = $this->input->post('device_type', TRUE);
        $function_code = $this->input->post('function_code', TRUE);
        $getFuction    = $this->Api_model->getSingleRow(FCN_TBL, array(
            'function_code' => $function_code,
            'status' => 1
        ));
        
        if ($getFuction) {
            $getUser = $this->Api_model->getSingleRow(USR_TBL, array(
                'mobile' => $mobile,
                'country_code' => $country_code
            ));
            
            if ($getUser) {
                if ($getUser->status == 0) {
                    $this->api->api_message(0, USR_NT_ACTIVE);
                    exit();
                } //$getUser->status == 0
                
                if ($getUser->image) {
                    $getUser->image = base_url() . $getUser->image;
                } //$getUser->image
                else {
                    $getUser->image = base_url() . 'assets/images/dummyuser.png';
                }
                
                $guest = $this->Api_model->getSingleRow('guest_list', array(
                    'function_id' => $getFuction->id,
                    'user_id' => $getUser->id
                ));
                if ($getUser->id == $getFuction->user_id || $guest) {
                    $datas['id']           = $getUser->id;
                    $datas['device_token'] = $device_token;
                    if (isset($device_type)) {
                        $datas['device_type'] = $device_type;
                    } //isset($device_type)
                    
                    $this->Api_model->updateSingleRow('user', array(
                        'id' => $getUser->id
                    ), $datas);
                    $this->api->api_message_data(1, USERRAGISTER, 'user_deatils', $getUser);
                } //$getUser->id == $getFuction->user_id || $guest
                // else
                // {
                //  $this->api->api_message(0,'Function not available. Please try after some time'); 
                // }
            } //$getUser
            else {
                $this->api->api_message(0, "User not found.");
                exit();
            }
        } //$getFuction
        else {
            $this->api->api_message(0, "Function not available. Please try after some time");
            exit();
        }
    }
    
    public function changePassword()
    {
        $mobile       = $this->input->post('mobile', TRUE);
        $password     = $this->input->post('password', TRUE);
        $new_password = $this->input->post('new_password', TRUE);
        
        $checkPass = $this->Api_model->getSingleRow("user", array(
            'mobile' => $mobile,
            'password' => $password
        ));
        if ($checkPass) {
            $where            = array(
                'mobile' => $mobile
            );
            $data['password'] = $new_password;
            $updateUser       = $this->Api_model->updateSingleRow("user", $where, $data);
            
            $this->Api_model->responseFailed(1, "Password updated successfully.");
        } //$checkPass
        else {
            $this->Api_model->responseFailed(0, "Old Password does not matched.");
        }
        exit();
    }
    
    
    /*User signup*/
    public function signUp()
    {
        $this->form_validation->set_rules('mobile', 'mobile', 'required');
        $this->form_validation->set_rules('country_code', 'country_code', 'required');
        $this->form_validation->set_rules('name', 'name', 'required');
        $this->form_validation->set_rules('email_id', 'email_id', 'required');
        $this->form_validation->set_rules('gender', 'gender', 'required');
        $this->form_validation->set_rules('device_token', 'device_token', 'required');
        
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $mobile       = $this->input->post('mobile', TRUE);
        $device_type  = $this->input->post('device_type', TRUE);
        $name         = $this->input->post('name', TRUE);
        $email_id     = $this->input->post('email_id', TRUE);
        $gender       = $this->input->post('gender', TRUE);
        $country_code = $this->input->post('country_code', TRUE);
        $device_token = $this->input->post('device_token', TRUE);
        $getUser      = $this->Api_model->getSingleRow(USR_TBL, array(
            'mobile' => $mobile,
            'country_code' => $country_code
        ));
        
        if ($getUser) {
            
            // if($getUser->password)
            // {
            //   $this->api->api_message(0, "User alraedy registered. Please login.");
            //   exit();
            // }
            
            if ($getUser->status == 0) {
                $this->api->api_message(0, USR_NT_ACTIVE);
                exit();
            } //$getUser->status == 0
            
            if ($getUser->image) {
                $getUser->image = base_url() . $getUser->image;
            } //$getUser->image
            else {
                $getUser->image = base_url() . 'assets/images/dummyuser.png';
            }
            
            $datas['id']           = $getUser->id;
            $datas['device_token'] = $device_token;
            // $datas['password']= $password;
            $datas['name']         = $name;
            $datas['email_id']     = $email_id;
            $datas['gender']       = $gender;
            $this->Api_model->updateSingleRow('user', array(
                'id' => $getUser->id
            ), $datas);
            $this->api->api_message_data(1, ' User already registered', 'user_deatils', $getUser);
        } //$getUser
        else {
            if (isset($device_type)) {
                $datauser['device_type'] = $device_type;
            } //isset($device_type)
            
            $datauser['mobile']       = $mobile;
            $datauser['country_code'] = $country_code;
            $datauser['name']         = $name;
            $datauser['email_id']     = $email_id;
            $datauser['gender']       = $gender;
            $datauser['device_token'] = $device_token;
            $datauser['signup_at']    = time();
            
            $getUserId = $this->Api_model->insertGetId(USR_TBL, $datauser);
            $getUser   = $this->Api_model->getSingleRow(USR_TBL, array(
                'id' => $getUserId
            ));
            
            if ($getUser->image) {
                $getUser->image = base_url() . $getUser->image;
            } //$getUser->image
            else {
                $getUser->image = base_url() . 'assets/images/dummyuser.png';
            }
            $this->api->api_message_data(1, 'User registered successfully', 'user_deatils', $getUser);
        }
    }
    
    
    public function editPersonalInfo()
    {
        $name     = $this->input->post('name', TRUE);
        $email_id = $this->input->post('email_id', TRUE);
        $user_id  = $this->input->post('user_id', TRUE);
        $gender   = $this->input->post('gender', TRUE);
        $image    = $this->input->post('image', TRUE);
        
        $this->checkUserStatus($user_id);
        
        $checkUser = $this->Api_model->getSingleRow('user', array(
            'id' => $user_id
        ));
        if ($checkUser) {
            
            $this->load->library('upload');
            $config['image_library']  = 'gd2';
            $config['upload_path']    = './assets/images/';
            $config['allowed_types']  = 'gif|jpg|jpeg|png';
            $config['max_size']       = 10000;
            $config['file_name']      = time();
            $config['create_thumb']   = TRUE;
            $config['maintain_ratio'] = TRUE;
            $config['width']          = 250;
            $config['height']         = 250;
            $this->upload->initialize($config);
            $ProfileImage = "";
            if ($this->upload->do_upload('image') && $this->load->library('image_lib', $config)) {
                $ProfileImage = 'assets/images/' . $this->upload->data('file_name');
            } //$this->upload->do_upload('image') && $this->load->library('image_lib', $config)
            else {
                //  echo $this->upload->display_errors();
            }
            
            if ($ProfileImage) {
                $data['image'] = $ProfileImage;
            } //$ProfileImage
            
            $where = array(
                'id' => $user_id
            );
            
            $data['name']     = isset($name) ? $name : $checkUser->name;
            $data['gender']   = isset($gender) ? $gender : $checkUser->gender;
            $data['email_id'] = isset($email_id) ? $email_id : $checkUser->email_id;
            
            $updateUser       = $this->Api_model->updateSingleRow('user', $where, $data);
            $checkUser        = $this->Api_model->getSingleRow('user', array(
                'id' => $user_id
            ));
            $checkUser->image = base_url() . $checkUser->image;
            $this->api->api_message_data(1, EDITSUCCESSFULL, 'user_deatils', $checkUser);
        } //$checkUser
        else {
            $this->api->api_message(0, NOTAVAILABLE);
        }
    }
    /*******************************End User Related API's**********************************/
    
    
    /*******************************Fuctions Related API's**********************************/
    /*Add new functions*/
    
    public function createFunction()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('title', 'title', 'required');
        $this->form_validation->set_rules('event_type', 'event_type', 'required');
        $this->form_validation->set_rules('date', 'date', 'required');
        $this->form_validation->set_rules('start_time', 'start_time', 'required');
        $this->form_validation->set_rules('end_time', 'end_time', 'required');
        $this->form_validation->set_rules('to_date', 'to_date', 'required');
        $this->form_validation->set_rules('to_start_time', 'to_start_time', 'required');
        $this->form_validation->set_rules('to_end_time', 'to_end_time', 'required');
        $this->form_validation->set_rules('long_description', 'long_description', 'required');
        $this->form_validation->set_rules('address', 'address', 'required');
        
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        }
        
        $data['user_id']          = $this->input->post('user_id', TRUE);
        $data['title']            = $this->input->post('title', TRUE);
        $data['event_type']       = $this->input->post('event_type', TRUE);
        $data['long_description'] = $this->input->post('long_description', TRUE);
        $data['address']          = $this->input->post('address', TRUE);
        $data['latitude']         = $this->input->post('latitude', TRUE);
        $data['longitude']        = $this->input->post('longitude', TRUE);
        $data['date']             = $this->input->post('date', TRUE);
        $data['start_time']       = $this->input->post('start_time', TRUE);
        $data['end_time']         = $this->input->post('end_time', TRUE);
        $data['to_date']          = $this->input->post('to_date', TRUE);
        $data['to_start_time']    = $this->input->post('to_start_time', TRUE);
        $data['to_end_time']      = $this->input->post('to_end_time', TRUE);
        $data['function_code']    = mt_rand(100000, 999999);
        if ($data['latitude']) {
            $data['latitude'] = $data['latitude'];
        } else {
            $data['latitude'] = "";
        }
        if ($data['longitude']) {
            $data['longitude'] = $data['longitude'];
        } else {
            $data['longitude'] = "";
        }
        
        $this->checkUserStatus($data['user_id']);
        
        $functionId = $this->Api_model->insertGetId(FCN_TBL, $data);
        
        if ($functionId) {
            if (isset($_FILES['banner_image'])) {
                $filesCount = count($_FILES['banner_image']['name']);
                for ($i = 0; $i < $filesCount; $i++) {
                    $_FILES['file']['name']     = $_FILES['banner_image']['name'][$i];
                    $_FILES['file']['type']     = $_FILES['banner_image']['type'][$i];
                    $_FILES['file']['tmp_name'] = $_FILES['banner_image']['tmp_name'][$i];
                    $_FILES['file']['error']    = $_FILES['banner_image']['error'][$i];
                    $_FILES['file']['size']     = $_FILES['banner_image']['size'][$i];
                    
                    $config['upload_path']   = 'assets/images/';
                    $config['allowed_types'] = '*';
                    $config['encrypt_name']  = TRUE;
                    
                    $this->load->library('upload', $config);
                    $this->upload->initialize($config);
                    if ($this->upload->do_upload('file')) {
                        // Uploaded file data
                        $fileData = $this->upload->data();
                        $fileData['file_name'];
                        $uploadData[$i] = 'assets/images/' . $fileData['file_name'];
                    }
                }
                foreach ($uploadData as $uploadData) {
                    $datas['function_id'] = $functionId;
                    $datas['media']       = $uploadData;
                    $datas['title']       = $this->input->post('title', TRUE);
                    $datas['description'] = $this->input->post('long_description', TRUE);
                    $listID               = $this->Api_model->insertGetId("function_gallery", $datas);
                    
                }
            }
            
            if (isset($_FILES['image'])) {
                $filesCount = count($_FILES['image']['name']);
                for ($i = 0; $i < $filesCount; $i++) {
                    $_FILES['file']['name']     = $_FILES['image']['name'][$i];
                    $_FILES['file']['type']     = $_FILES['image']['type'][$i];
                    $_FILES['file']['tmp_name'] = $_FILES['image']['tmp_name'][$i];
                    $_FILES['file']['error']    = $_FILES['image']['error'][$i];
                    $_FILES['file']['size']     = $_FILES['image']['size'][$i];
                    
                    $config['upload_path']   = 'assets/images/';
                    $config['allowed_types'] = '*';
                    $config['encrypt_name']  = TRUE;
                    $this->load->library('upload', $config);
                    $this->upload->initialize($config);
                    if ($this->upload->do_upload('file')) {
                        $fileData = $this->upload->data();
                        $fileData['file_name'];
                        $uploadData1[$i] = 'assets/images/' . $fileData['file_name'];
                    }
                }
                foreach ($uploadData1 as $uploadData1) {
                    $datass['function_id'] = $functionId;
                    $datass['image']       = $uploadData1;
                    $listID                = $this->Api_model->insertGetId("invitaionCard", $datass);
                }
                
            }
            $where   = array(
                'id' => $functionId
            );
            $getdata = $this->Api_model->getSingleRow(FCN_TBL, array(
                'id' => $functionId
            ));
            
            $getdata->banner_image = $this->config->base_url() . $datas['media'];
            $Image         = $this->Api_model->getAllDataWhereConcat(array(
                'function_id' => $functionId
            ), 'function_gallery');
            $getdata->image        = $Image;
            // $getdata->banner_image      = base_url() . $getFunction->image;

            //    $productsImagee  = $this->Api_model->getAllDataWhereConcat(array(
            //     'function_id' => $functionId
            // ), 'invitaionCard');
            // $getdata->banner_image = $productsImagee;
            
            if ($getdata) {
                $this->api->api_message_data(1, ADD_FUNCTION, 'data', $getdata);
            } else {
                $this->api->api_message(0, NOTAVAILABLE);
                
            }
            
        }
        
    }
    
    // /*Add new functions*/
    // public function updateFunctionn()
    // {
    //     $this->form_validation->set_rules('user_id', 'user_id', 'required');
    //     $this->form_validation->set_rules('function_id', 'function_id', 'required');
    
    //     if ($this->form_validation->run() == false) {
    //         $this->api->api_message(0, ALL_FIELD_MANDATORY);
    //         exit();
    //     } //$this->form_validation->run() == false
    
    //     $function_id = $this->input->post('function_id', TRUE);
    //     $getFuction  = $this->Api_model->getSingleRow(FCN_TBL, array(
    //         'id' => $function_id
    //     ));
    //     if (!$getFuction) {
    //         $this->api->api_message(0, FNC_NT_FND);
    //         exit();
    //     } //!$getFuction
    //     $user_id          = $this->input->post('user_id', TRUE);
    //     $title            = $this->input->post('title', TRUE);
    //     $image            = $this->input->post('image', TRUE);
    //     $long_description = $this->input->post('long_description', TRUE);
    //     $date             = $this->input->post('date', TRUE);
    //     $start_time       = $this->input->post('start_time', TRUE);
    //     $end_time         = $this->input->post('end_time', TRUE);
    
    //     $this->checkUserStatus($user_id);
    
    //     $this->load->library('upload');
    
    //     $config['image_library']  = 'gd2';
    //     $config['upload_path']    = './assets/images/';
    //     $config['allowed_types']  = 'gif|jpg|jpeg|png';
    //     $config['max_size']       = 10000;
    //     $config['file_name']      = time();
    //     $config['create_thumb']   = TRUE;
    //     $config['maintain_ratio'] = TRUE;
    //     $config['width']          = 250;
    //     $config['height']         = 250;
    //     $this->upload->initialize($config);
    //     $functionImage = "";
    //     if ($this->upload->do_upload('image') && $this->load->library('image_lib', $config)) {
    //         $functionImage = 'assets/images/' . $this->upload->data('file_name');
    //     } //$this->upload->do_upload('image') && $this->load->library('image_lib', $config)
    //     else {
    //         //  echo $this->upload->display_errors();
    //     }
    
    //     if ($functionImage) {
    //         $data['image'] = $functionImage;
    //     } //$functionImage
    //     $data['title']            = isset($title) ? $title : $getEvent->title;
    //     $data['date']             = isset($date) ? $date : $getEvent->date;
    //     $data['long_description'] = isset($long_description) ? $long_description : $getEvent->long_description;
    
    //     $this->Api_model->updateSingleRow(FCN_TBL, array(
    //         'id' => $function_id
    //     ), $data);
    //     $this->api->api_message(1, FNC_UPDATED);
    // }
    
    
    ##########################################################################################################
    
    public function updateFunction()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $function_id = $this->input->post('function_id', TRUE);
        $getFuction  = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id
        ));
        if (!$getFuction) {
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFuction
        
        $data['user_id']          = $this->input->post('user_id', TRUE);
        $data['title']            = $this->input->post('title', TRUE);
        $image                    = $this->input->post('image', TRUE);
        $data['long_description'] = $this->input->post('long_description', TRUE);
        $data['date']             = $this->input->post('date', TRUE);
        $data['start_time']       = $this->input->post('start_time', TRUE);
        $data['end_time']         = $this->input->post('end_time', TRUE);
        
        $this->checkUserStatus($data['user_id']);
        
        $where = array(
            'id' => $function_id
        );
        $this->Api_model->updateSingleRow(FCN_TBL, $where, $data);
        if (isset($_FILES['image'])) {
            $filesCount = count($_FILES['image']['name']);
            for ($i = 0; $i < $filesCount; $i++) {
                $_FILES['file']['name']     = $_FILES['image']['name'][$i];
                $_FILES['file']['type']     = $_FILES['image']['type'][$i];
                $_FILES['file']['tmp_name'] = $_FILES['image']['tmp_name'][$i];
                $_FILES['file']['error']    = $_FILES['image']['error'][$i];
                $_FILES['file']['size']     = $_FILES['image']['size'][$i];
                // File upload configuration                       
                // $uploadPath                 = base_url() .'./assets/images/';
                $config['upload_path']      = 'assets/images/';
                // $config['upload_path']      = $uploadPath;
                $config['allowed_types']    = 'gif|jpg|jpeg|png|webm|mkv|flv|mp4|3gp|mpeg';
                $config['encrypt_name']     = TRUE;
                // Load and initialize upload library
                $this->load->library('upload', $config);
                $this->upload->initialize($config);
                if ($this->upload->do_upload('file')) {
                    // Uploaded file data
                    $fileData = $this->upload->data();
                    $fileData['file_name'];
                    $uploadData[$i] = 'assets/images/' . $fileData['file_name'];
                }
            }
            
            foreach ($uploadData as $uploadData) {
                $wheree               = array(
                    'function_id' => $function_id
                );
                $datas['function_id'] = $function_id;
                $datas['media']       = $uploadData;
                $datas['title']       = $this->input->post('title', TRUE);
                $datas['description'] = $this->input->post('long_description', TRUE);
                $this->Api_model->deleteRecord($wheree, 'function_gallery');
                // $listID = $this->Api_model->insertGetId("pro_images", $datas);
                $listID = $this->Api_model->insertGetId("function_gallery", $datas);
            } //$uploadData as $uploadData
        } //isset($_FILES['files'])
        $getFunction = $this->Api_model->getSingleRow('function', array(
            'id' => $function_id
        ));
        
        // $this->api->api_message(1, FNC_UPDATED);      
        $this->api->api_message(1, FNC_UPDATED);
        
    }
    
    
    /*delete Function*/
    public function deleteFunction()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $function_id = $this->input->post('function_id', TRUE);
        $user_id     = $this->input->post('user_id', TRUE);
        $getFuction  = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id
        ));
        if (!$getFuction) {
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFuction
        
        $this->checkUserStatus($user_id);
        $this->Api_model->updateSingleRow(FCN_TBL, array(
            'id' => $function_id
        ), array(
            'status' => 0
        ));
        $this->api->api_message(1, FUNCTION_DLT);
    }
    /*Update Profile*/
    /*******************************End Fuctions Related API's**********************************/
    
    /*******************************Events Related API's**********************************/
    //     public function getMyEvent()
    //    {
    //       $user_id= $this->input->post('user_id', TRUE);
    //       // $function_id= $this->input->post('function_id', TRUE);
    //       $this->checkUserStatus($user_id);
    //       $getFunction = $this->Api_model->getAllDataWhere(array('user_id'=>$user_id),'events_list');
    //       // print_r($getFunction);
    //       // exit();
    //       if($getFunction)
    //       { 
    //         $getFunctions= array();
    //         foreach ($getFunction as $getFunctiondata) 
    //         {
    //           $getGuest=$this->Api_model->getAllDataWhere(array('function_id'=>$getFunctiondata->function_id),'guest_list');
    //            if ($getGuest) {
    //                 foreach ($getGuest as $getGuestt) {
    //                   $function_id=$getGuestt->function_id;      
    //                      $getGuestOne=$this->Api_model->getAllDataWhere(array('function_id'=>$function_id,'invitation_status'=>1),'guest_list'); 
    //                        $getFunctiondata->invitation=count($getFunctions);
    //                        $getFunctiondata->accept=count($getGuestOne)
    // ;                }
    //            }
    //            array_push($getFunctions, $getFunctiondata);
    //         }
    //          $invitation=count($getGuestOne);
    //          $accept=count($getGuestOne);
    
    //           $arr = array('status' => 1,'message' => "Get Events", 'data'=> $getFunctions); 
    //             header('Content-Type: application/json');      
    //              echo json_encode($arr); 
    
    //           exit();
    //         }
    //       else
    //       {
    //         $this->api->api_message(0, "Not yet any event.");
    //       }
    //    }
    public function getMyEvent()
    {
        $user_id = $this->input->post('user_id', TRUE);
        $this->checkUserStatus($user_id);
        $getFunction = $this->Api_model->getWhereInStatusResult(FCN_TBL, array(
            'user_id' => $user_id
        ), 'status', array(
            2,
            1
        ));
        
        if ($getFunction) {
            $getFunctions = array();
            foreach ($getFunction as $getFunction) {
                $getFunction->attendance_count = $this->Api_model->getCount('guest_attendance', array(
                    'function_id' => $getFunction->id
                ));
                $getUser                       = $this->Api_model->getAllDataWhere(array(
                    'id' => $getFunction->user_id
                ), 'user');
                
                $getFunction->user_name  = $getUser[0]->name;
                $getFunction->image      = base_url() . $getFunction->image;
                $getFunction->type       = "1";
                $getFunction->invitation = "1";
                $getFunction->accept     = "1";
                array_push($getFunctions, $getFunction);
            }
        }
        if ($getFunction) {
            $this->api->api_message_data(1, ALL_FUNCTION, 'data', $getFunctions);
            
        } //$getFunction
        else {
            $this->api->api_message(0, FNC_NT_FND);
        }
    } //$getFunction as $getFunction
    
    
    /*Add new Events*/
    // public function createEvent()
    // {
    //     $this->form_validation->set_rules('user_id', 'user_id', 'required');
    //     $this->form_validation->set_rules('event_type', 'event_type', 'required');
    //     $this->form_validation->set_rules('title', 'title', 'required');
    //     $this->form_validation->set_rules('from_date', 'from_date', 'required');
    //     $this->form_validation->set_rules('from_start_time', 'from_start_time', 'required');
    //     $this->form_validation->set_rules('from_end_time', 'from_end_time', 'required');
    //     $this->form_validation->set_rules('to_date', 'to_date', 'required');
    //     $this->form_validation->set_rules('to_start_time', 'to_start_time', 'required');
    //     $this->form_validation->set_rules('to_end_time', 'to_end_time', 'required');
    //     $this->form_validation->set_rules('function_id', 'function_id', 'required');
    //     $this->form_validation->set_rules('address', 'address', 'required');
    //     $this->form_validation->set_rules('description', 'description', 'required');
    
    //     if ($this->form_validation->run() == false) {
    //         $this->api->api_message(0, ALL_FIELD_MANDATORY);
    //         exit();
    //     } //$this->form_validation->run() == false
    //     $data['user_id']         = $this->input->post('user_id', TRUE);
    //     $data['from_date']       = $this->input->post('from_date', TRUE);
    //     $data['from_start_time'] = $this->input->post('from_start_time', TRUE);
    //     $data['from_end_time']   = $this->input->post('from_end_time', TRUE);
    //     $data['to_date']         = $this->input->post('to_date', TRUE);
    //     $data['to_start_time']   = $this->input->post('to_start_time', TRUE);
    //     $data['to_end_time']     = $this->input->post('to_end_time', TRUE);
    //     $function_id             = $this->input->post('function_id', TRUE);
    //     $data['title']           = $this->input->post('title', TRUE);
    //     $data['event_type']      = $this->input->post('event_type', TRUE);
    //     $data['address']         = $this->input->post('address', TRUE);
    //     $data['latitude']        = $this->input->post('latitude', TRUE);
    //     $data['longitude']       = $this->input->post('longitude', TRUE);
    //     $data['description']     = $this->input->post('description', TRUE);
    //     $this->checkUserStatus($data['user_id']);
    //     $getFuction = $this->Api_model->getSingleRow(FCN_TBL, array(
    //         'id' => $function_id
    //     ));
    //     if (!$getFuction) {
    //         $this->api->api_message(0, FNC_NT_FND);
    //         exit();
    //     } //!$getFuction
    
    //     $data['function_id']     = $function_id;
    //     $image       = $this->input->post('image', TRUE);
    //     $config['upload_path']   = './assets/images/banner/';
    //     $config['allowed_types'] = 'gif|jpg|jpeg|png';
    //     $config['max_size']      = 10000;
    //     $config['file_name']     = time();
    //     $this->load->library('upload', $config);
    //     $this->upload->initialize($config);
    //     $banner_image = "";
    
    //     if ($this->upload->do_upload('banner_image')) {
    //         $banner_image         = './assets/images/banner/' . $this->upload->data('file_name');
    //         $data['banner_image'] = $banner_image;
    //     } //$this->upload->do_upload('banner_image')
    
    //     $eventId   = $this->Api_model->insertGetId(EVT_TBL, $data);
    //      if ($eventId) {
    //            if (isset($_FILES['image'])) {
    //                 $filesCount = count($_FILES['image']['name']);
    //                 for ($i = 0; $i < $filesCount; $i++) {
    //                     $_FILES['file']['name']     = $_FILES['image']['name'][$i];
    //                     $_FILES['file']['type']     = $_FILES['image']['type'][$i];
    //                     $_FILES['file']['tmp_name'] = $_FILES['image']['tmp_name'][$i];
    //                     $_FILES['file']['error']    = $_FILES['image']['error'][$i];
    //                     $_FILES['file']['size']     = $_FILES['image']['size'][$i];
    //                     // File upload configuration                       
    //                     $uploadPath                 = 'assets/images/banner/';
    //                     $config['upload_path']      = $uploadPath;
    //                     $config['allowed_types'] = 'gif|jpg|jpeg|png|webm|mkv|flv|mp4|3gp|mpeg';
    //                     $config['encrypt_name']     = TRUE;
    //                     // Load and initialize upload library
    //                     $this->load->library('upload', $config);
    //                     $this->upload->initialize($config);
    //                     if ($this->upload->do_upload('file')) {
    //                         // Uploaded file data
    //                         $fileData = $this->upload->data();
    //                         $fileData['file_name'];
    //                         $uploadData[$i] = 'assets/images/banner/' . $fileData['file_name'];
    //                     } //$this->upload->do_upload('file')
    //                 } //$i = 0; $i < $filesCount; $i++
    
    //                 foreach ($uploadData as $uploadData) {
    //                     $datas['event_id'] = $eventId;
    //                     $datas['image']      = $uploadData;                      
    //                     $datas['created_at'] =time();
    //                     $datas['status']= 1;
    //                     $listID              = $this->Api_model->insertGetId("event_gallery", $datas);
    
    
    //                 } //$uploadData as $uploadData
    
    //             } //isset($_FILES['files'])
    //            }
    //     $getGuests = $this->Api_model->getAllDataWhere(array(
    //         'function_id' => $function_id
    //     ), GST_LIST_TBL);
    
    //     foreach ($getGuests as $getGuests) {
    //         $title = "New Event";
    //         $type  = "10011";
    //         $msg   = $data['title'] . " new event add on " . $getFuction->title;
    //         $this->firebase_notification($getGuests->user_id, $msg, $title, $type, $function_id);
    //     } //$getGuests as $getGuests
    
    //     $this->api->api_message(1, ADD_FUNCTION);
    // }
    
    public function createEvent()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('event_type', 'event_type', 'required');
        $this->form_validation->set_rules('title', 'title', 'required');
        $this->form_validation->set_rules('from_date', 'from_date', 'required');
        $this->form_validation->set_rules('from_start_time', 'from_start_time', 'required');
        $this->form_validation->set_rules('from_end_time', 'from_end_time', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        $this->form_validation->set_rules('address', 'address', 'required');
        $this->form_validation->set_rules('description', 'description', 'required');
        
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        $data['user_id']         = $this->input->post('user_id', TRUE);
        $data['from_date']       = $this->input->post('from_date', TRUE);
        $data['from_start_time'] = $this->input->post('from_start_time', TRUE);
        $data['from_end_time']   = $this->input->post('from_end_time', TRUE);
        $function_id             = $this->input->post('function_id', TRUE);
        $data['title']           = $this->input->post('title', TRUE);
        $data['event_type']      = $this->input->post('event_type', TRUE);
        $data['address']         = $this->input->post('address', TRUE);
        $data['latitude']        = $this->input->post('latitude', TRUE);
        $data['longitude']       = $this->input->post('longitude', TRUE);
        $data['description']     = $this->input->post('description', TRUE);
        if ($data['latitude']) {
            $data['latitude'] = $data['latitude'];
        } else {
            $data['latitude'] = "";
        }
        if ($data['longitude']) {
            $data['longitude'] = $data['longitude'];
        } else {
            $data['longitude'] = "";
        }
        $this->checkUserStatus($data['user_id']);
        $getFuction = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id
        ));
        if (!$getFuction) {
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFuction
        
        $data['function_id'] = $function_id;
        
        
        $eventId = $this->Api_model->insertGetId(EVT_TBL, $data);
        if ($eventId) {
            if (isset($_FILES['image'])) {
                $filesCount = count($_FILES['image']['name']);
                for ($i = 0; $i < $filesCount; $i++) {
                    $_FILES['file']['name']     = $_FILES['image']['name'][$i];
                    $_FILES['file']['type']     = $_FILES['image']['type'][$i];
                    $_FILES['file']['tmp_name'] = $_FILES['image']['tmp_name'][$i];
                    $_FILES['file']['error']    = $_FILES['image']['error'][$i];
                    $_FILES['file']['size']     = $_FILES['image']['size'][$i];
                    // File upload configuration                       
                    $uploadPath                 = 'assets/images/banner/';
                    $config['upload_path']      = $uploadPath;
                    $config['allowed_types']    = 'gif|jpg|jpeg|png|webm|mkv|flv|mp4|3gp|mpeg';
                    $config['encrypt_name']     = TRUE;
                    // Load and initialize upload library
                    $this->load->library('upload', $config);
                    $this->upload->initialize($config);
                    if ($this->upload->do_upload('file')) {
                        // Uploaded file data
                        $fileData = $this->upload->data();
                        $fileData['file_name'];
                        $uploadData[$i] = 'assets/images/banner/' . $fileData['file_name'];
                    } //$this->upload->do_upload('file')
                } //$i = 0; $i < $filesCount; $i++
                
                foreach ($uploadData as $uploadData) {
                    $datas['event_id'] = $eventId;
                    $datas['image']    = $uploadData;
                    // $datas['created_at'] =time();
                    $datas['status']   = 1;
                    $listID            = $this->Api_model->insertGetId("event_gallery", $datas);
                    
                } //$uploadData as $uploadData
                
            } //isset($_FILES['files'])
        }
        $getGuests = $this->Api_model->getAllDataWhere(array(
            'function_id' => $function_id
        ), GST_LIST_TBL);
        
        foreach ($getGuests as $getGuests) {
            $title = "New Event";
            $type  = "10011";
            $msg   = $data['title'] . " new event add on " . $getFuction->title;
            $this->firebase_notification($getGuests->user_id, $msg, $title, $type, $function_id);
        } //$getGuests as $getGuests
        
        $this->api->api_message(1, ADD_EVENT);
    }
    
    
    /*Edit Events*/
    public function editEvent()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        $this->form_validation->set_rules('event_id', 'event_id', 'required');
        
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $user_id         = $this->input->post('user_id', TRUE);
        $function_id     = $this->input->post('function_id', TRUE);
        $event_id        = $this->input->post('event_id', TRUE);
        $from_date       = $this->input->post('from_date', TRUE);
        $from_start_time = $this->input->post('from_start_time', TRUE);
        $from_end_time   = $this->input->post('from_end_time', TRUE);
        $event_type      = $this->input->post('event_type', TRUE);
        $title           = $this->input->post('title', TRUE);
        $address         = $this->input->post('address', TRUE);
        $latitude        = $this->input->post('latitude', TRUE);
        $longitude       = $this->input->post('longitude', TRUE);
        $description     = $this->input->post('description', TRUE);
        
        $this->checkUserStatus($user_id);
        
        $getEvent = $this->Api_model->getSingleRow(EVT_TBL, array(
            'id' => $event_id,
            'function_id' => $function_id,
            'user_id' => $user_id
        ));
        if (!$getEvent) {
            $this->api->api_message(0, EVT_NT_FND);
            exit();
        } //!$getEvent
        $data['title']           = isset($title) ? $title : $getEvent->title;
        $data['event_type']      = isset($event_type) ? $event_type : $getEvent->event_type;
        $data['from_date']       = isset($from_date) ? $from_date : $getEvent->from_date;
        $data['from_start_time'] = isset($from_start_time) ? $from_start_time : $getEvent->from_start_time;
        $data['from_end_time']   = isset($from_end_time) ? $from_end_time : $getEvent->from_end_time;
        $data['address']         = isset($address) ? $address : $getEvent->address;
        $data['latitude']        = isset($latitude) ? $latitude : $getEvent->latitude;
        $data['longitude']       = isset($longitude) ? $longitude : $getEvent->longitude;
        $data['description']     = isset($description) ? $description : $getEvent->description;
        
        
        $this->Api_model->updateSingleRow(EVT_TBL, array(
            'id' => $event_id,
            'function_id' => $function_id,
            'user_id' => $user_id
        ), $data);
        
        if (isset($_FILES['image'])) {
            $filesCount = count($_FILES['image']['name']);
            for ($i = 0; $i < $filesCount; $i++) {
                $_FILES['file']['name']     = $_FILES['image']['name'][$i];
                $_FILES['file']['type']     = $_FILES['image']['type'][$i];
                $_FILES['file']['tmp_name'] = $_FILES['image']['tmp_name'][$i];
                $_FILES['file']['error']    = $_FILES['image']['error'][$i];
                $_FILES['file']['size']     = $_FILES['image']['size'][$i];
                // File upload configuration                       
                // $uploadPath                 = base_url() .'./assets/images/';
                $config['upload_path']      = 'assets/images/banner/';
                // $config['upload_path']      = $uploadPath;
                $config['allowed_types']    = 'gif|jpg|jpeg|png|webm|mkv|flv|mp4|3gp|mpeg';
                $config['encrypt_name']     = TRUE;
                // Load and initialize upload library
                $this->load->library('upload', $config);
                $this->upload->initialize($config);
                if ($this->upload->do_upload('file')) {
                    // Uploaded file data
                    $fileData = $this->upload->data();
                    $fileData['file_name'];
                    $uploadData[$i] = 'assets/images/banner/' . $fileData['file_name'];
                }
            }
            
            foreach ($uploadData as $uploadData) {
                
                $datas['event_id'] = $event_id;
                $datas['image']    = $uploadData;
                // $wheree                 = array(
                //    'event_id' => $event_id
                //    );
                // $this->Api_model->deleteRecord($wheree, 'event_gallery');  
                $listID            = $this->Api_model->insertGetId("event_gallery", $datas);
                
            }
            
        }
        // $data['image'] =$this->config->base_url().$datas['image'];
        
        $this->api->api_message(1, EVT_UPDATED);
    }
    
    ####################################################################################################
    /*Get Events By Fucntion*/
    public function getEventsByFunction()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $user_id     = $this->input->post('user_id', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        
        $this->checkUserStatus($user_id);
        $getFuction = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id
        ));
        
        if (!$getFuction) {
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFuction
        
        $getFunction = $this->Api_model->getAllDataWhereEventss(array(
            'function_id' => $function_id
        ), EVT_TBL);
        
        
        
        if ($getFunction) {
            $getFunctions = array();
            foreach ($getFunction as $getFunction) {
                $getGuest           = $this->Api_model->getSingleRow(EVT_GST_TBL, array(
                    'user_id' => $user_id,
                    'event_id' => $getFunction->id
                ));
                $Image              = $this->Api_model->getAllDataWhereConcat1(array(
                    'event_id' => $getFunction->id
                ), 'event_gallery');
                $getFunction->media = $Image;
                // $getFunction->banner_image   = base_url() . $getFunction->banner_image; 
                
                if ($getGuest) {
                    $getFunction->userJoin = "1";
                } //$getGuest
                else {
                    $getFunction->userJoin = "0";
                }
                // $getFunction->image  =  base_url() . $getFunction->image;
                array_push($getFunctions, $getFunction);
            } //$getFunction as $getFunction
            
            
            $this->api->api_message_data(1, ALL_EVENTS, 'getEvents', $getFunctions);
            
        } //$getFunction
        else {
            $this->api->api_message(0, EVT_NT_FND);
            exit();
        }
    }
    
    
    
    ################################################################################################
    /*Get Events By Fucntion*/
    public function Event()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $user_id     = $this->input->post('user_id', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        $data        = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id,
            'user_id' => $user_id
        ));
        if (!empty($data)) {
            $data->image     = base_url() . $data->image;
            $productsImage   = $this->Api_model->getAllDataWhereConcat(array(
                'function_id' => $data->id
            ), 'function_gallery');
            $getUser         = $this->Api_model->getSingleRow('user', array(
                'id' => $data->user_id
            ));
            $data->user_name = $getUser->name;
            $data->media     = $productsImage;
            $going           = $this->Api_model->getAllDataWhere(array(
                'function_id' => $function_id,
                'user_id' => $user_id,
                'invitation_status' => 1
            ), 'guest_list');
            $pending         = $this->Api_model->getAllDataWhere(array(
                'function_id' => $function_id,
                'user_id' => $user_id,
                'invitation_status' => 0
            ), 'guest_list');
            $invited         = $this->Api_model->getAllDataWhere(array(
                'function_id' => $function_id,
                'user_id' => $user_id
            ), 'guest_list');
            $data->invited   = count($invited);
            $data->going     = count($going);
            $data->pending   = count($pending);
            
            
            $getAgenda = $this->Api_model->getAllDataWhere(array(
                'function_id' => $function_id,
                'user_id' => $user_id
            ), 'events_list');
            
            if ($getAgenda) {
                $getAgendaa = array();
                foreach ($getAgenda as $getAgenda) {
                    $getGuest           = $this->Api_model->getAllDataWhere(array(
                        'function_id' => $getAgenda->function_id
                    ), 'guest_list');
                    $going              = $this->Api_model->getAllDataWhere(array(
                        'function_id' => $getAgenda->function_id,
                        'invitation_status' => 1
                    ), 'guest_list');
                    $pending            = $this->Api_model->getAllDataWhere(array(
                        'function_id' => $getAgenda->function_id,
                        'invitation_status' => 0
                    ), 'guest_list');
                    $Image              = $this->Api_model->getAllDataWhereConcat1(array(
                        'event_id' => $getAgenda->id
                    ), 'event_gallery');
                    $getAgenda->media   = $Image;
                    // $getAgenda->image   = base_url() . $getAgenda->image;
                    $getAgenda->invited = count($getGuest);
                    $getAgenda->going   = count($going);
                    $getAgenda->pending = count($pending);
                    array_push($getAgendaa, $getAgenda);
                } //$getAgenda as $getAgenda
            } //$getAgenda
            
            if ($getAgenda) {
                array_multisort($getAgendaa, SORT_ASC, $getAgendaa);
                $data->All_Agenda = $getAgendaa;
            }
            //$getAgenda
            else {
                $data->All_Agenda = array();
            }
            
            $getPost = $this->Api_model->getAllDataWhere(array(
                'function_id' => $function_id,
                'user_id' => $user_id
            ), 'post');
            
            if ($getPost) {
                $getPosts = array();
                foreach ($getPost as $getPost) {
                    $getServicename         = $this->Api_model->getSingleRow('function', array(
                        'id' => $getPost->function_id
                    ));
                    $getUsername            = $this->Api_model->getSingleRow('user', array(
                        'id' => $user_id
                    ));
                    $getPost->Function_name = $getServicename->title;
                    $getPost->user_name     = $getUsername->name;
                    $getPost->user_image    = base_url() . $getUsername->image;
                    $Image                  = $this->Api_model->getAllDataWhereConcat(array(
                        'post_id' => $getPost->id
                    ), 'post_gallery');
                    $getPost->media         = $Image;
                    
                    $checkLike = $this->Api_model->getSingleRow(LKS_TBL, array(
                        'post_id' => $getPost->id,
                        'user_id' => $user_id
                    ));
                    if ($checkLike) {
                        $getPost->is_like = 1;
                    } //$checkLike
                    else {
                        $getPost->is_like = 0;
                    }
                    $getPost->comments    = $this->Api_model->getCountWhere(CMT_TBL, array(
                        'post_id' => $getPost->id
                    ));
                    $getPost->likes       = $this->Api_model->getCountWhere(LKS_TBL, array(
                        'post_id' => $getPost->id,
                        'status' => 1
                    ));
                    $getPost->image_count = $this->Api_model->getCountWhere('post_gallery', array(
                        'post_id' => $getPost->id
                    ));
                    $getComments          = $this->Api_model->getAllDataWhere(array(
                        'post_id' => $getPost->id
                    ), CMT_TBL);
                    
                    $getComment = array();
                    foreach ($getComments as $getComments) {
                        $getUser                 = $this->Api_model->getSingleRow(USR_TBL, array(
                            'id' => $getComments->user_id
                        ));
                        $getComments->user_name  = $getUser->name;
                        $getComments->user_image = base_url() . $getUser->image;
                        array_push($getComment, $getComments);
                    } //$getComments as $getComments
                    
                    $getPost->getComments = $getComment;
                    array_push($getPosts, $getPost);
                } //$getAgenda as $getAgenda
            } //$getAgenda
            if ($getPost) {
                array_multisort($getPosts, SORT_ASC, $getPosts);
                $data->All_Post = $getPosts;
            } //$getAgenda
            else {
                $data->All_Post = array();
            }
            $getServices = $this->Api_model->getAllDataWhere(array(
                'status' => '1'
            ), 'service_type');
            
            if ($getServices) {
                $getServicess = array();
                foreach ($getServices as $getServices) {
                    $getServices->image = base_url() . $getServices->image;
                    array_push($getServicess, $getServices);
                } //$getServices as $getServices
            } //$getServices
            if ($getServices) {
                array_multisort($getServicess, SORT_ASC, $getServicess);
                $data->All_Servicess = $getServicess;
            } //$getServices
            else {
                $data->All_Servicess = $getServicess;
            }
        }
        
        if ($data) {
            $this->api->api_message_data(1, ALL_EVENTS, 'dashboard_data', $data);
        } //$data
        else {
            $this->api->api_message(0, NO_DATA);
        }
    }
    /*Get Events By Fucntion*/
    public function Event1()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $user_id     = $this->input->post('user_id', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        $getFuction  = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id,
            'user_id' => $user_id
        ));
        if (!empty($getFuction)) {
            $this->api->api_message_data(1, ALL_EVENTS, 'dashboard_data', $getFuction);
            
        }
        
        else {
            $this->api->api_message(0, NO_DATA);
            
            
        }
        
    }
    
    /*Get Events By Function*/
    public function inviteEvent()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        }
        
        $user_id     = $this->input->post('user_id', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        $this->checkUserStatus($user_id);
        $data = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id
            /* 'user_id' => $user_id*/
        ));
        if (!$data) {
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFuction
        
        
        if ($data) {
            $data->image   = base_url() . $data->image;
            $productsImage = $this->Api_model->getAllDataWhereConcat(array(
                'function_id' => $data->id
            ), 'function_gallery');
            $data->media   = $productsImage;
            $user          = $this->Api_model->getSingleRow(USR_TBL, array(
                'id' => $user_id
            ));
            
            $data->user_name  = $user->name;
            $data->user_image = base_url() . $user->image;
        }
        
        $getAgenda = $this->Api_model->getAllDataWhere(array(
            'function_id' => $function_id,
            'user_id' => $user_id
        ), 'events_list');
        
        if ($getAgenda) {
            $getAgendaa = array();
            foreach ($getAgenda as $getAgenda) {
                $getAgenda->image = base_url() . $getAgenda->image;
                
                array_push($getAgendaa, $getAgenda);
            } //$getAgenda as $getAgenda
        } //$getAgenda
        if ($getAgenda) {
            array_multisort($getAgendaa, SORT_ASC, $getAgendaa);
            $data->All_Agenda = $getAgendaa;
            
        } //$getAgenda
        else {
            $data->All_Agenda = array();
            
        }
        
        $getGallery = $this->Api_model->getAllDataWhere(array(
            'function_id' => $function_id
        ), 'function_gallery');
        
        if ($getGallery) {
            $getServicess = array();
            foreach ($getGallery as $getGallery) {
                $getGallery->media = base_url() . $getGallery->media;
                array_push($getServicess, $getGallery);
            } //$getGallery as $getGallery
        } //$getGallery
        if ($getGallery) {
            array_multisort($getServicess, SORT_ASC, $getServicess);
            $data->Gallery = $getServicess;
            
        } //$getGallery
        else {
            $data->Gallery = array();
            
        }
        
        
        $getInvitation = $this->Api_model->getAllDataWhere(array(
            'function_id' => $function_id
        ), 'invitaionCard');
        
        if ($getInvitation) {
            $getInvitationn = array();
            foreach ($getInvitation as $getInvitation) {
                $getInvitation->image = base_url() . $getInvitation->image;
                //  $getInvitationn = $this->Api_model->getSingleRow('function',array(
                //  'id' => $getInvitation->function_id
                // ));
                //  $getInvitation->title= $getInvitationn->title;
                array_push($getInvitationn, $getInvitation);
            } //$getInvitation as $getInvitation
        } //$getInvitation
        if ($getInvitation) {
            array_multisort($getInvitationn, SORT_ASC, $getInvitationn);
            $data->Invitation = $getInvitationn;
        } //$getInvitation
        else {
            $data->Invitation = array();
            
        }
        
        if ($data) {
            $this->api->api_message_data(1, ALL_EVENTS, 'dashboard_data', $data);
        } //$data
        else {
            $this->api->api_message(0, NO_DATA);
        }
    }
    
    
    /*Get Events By Function*/
    public function getEventsById()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('event_id', 'event_id', 'required');
        
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $user_id  = $this->input->post('user_id', TRUE);
        $event_id = $this->input->post('event_id', TRUE);
        
        $this->checkUserStatus($user_id);
        $getFunction = $this->Api_model->getSingleRow(EVT_TBL, array(
            'id' => $event_id
        ));
        if (!$getFunction) {
            $this->api->api_message(0, EVT_NT_FND);
            exit();
        } //!$getFunction
        
        else {
            $getGuest = $this->Api_model->getSingleRow(EVT_GST_TBL, array(
                'user_id' => $user_id,
                'event_id' => $event_id
            ));
            if ($getGuest) {
                $getFunction->userJoin = "1";
            } //$getGuest
            else {
                $getFunction->userJoin = "0";
            }
            $this->api->api_message_data(1, ALL_EVENTS, 'getEvent', $getFunction);
        }
    }
    
    /*Remove Welcome Gallery Images*/
    public function removeEvent()
    {
        $this->form_validation->set_rules('event_id', 'event_id', 'required');
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $event_id    = $this->input->post('event_id', TRUE);
        $user_id     = $this->input->post('user_id', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        $this->checkUserStatus($user_id);
        
        $getFuction = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id
        ));
        if (!$getFuction) {
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFuction
        
        $getmedia = $this->Api_model->getSingleRow(EVT_TBL, array(
            'id' => $event_id
        ));
        if ($getmedia) {
            $this->Api_model->deleteRecord(array(
                'id' => $event_id
            ), EVT_TBL);
            $this->api->api_message(1, EVT_DLT);
            exit();
        } //$getmedia
        else {
            $this->api->api_message(0, EVT_NT_FND);
            exit();
        }
    }
    
    /*Join Event*/
    public function joinEvent()
    {
        $this->form_validation->set_rules('event_id', 'event_id', 'required');
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $event_id = $this->input->post('event_id', TRUE);
        $user_id  = $this->input->post('user_id', TRUE);
        $this->checkUserStatus($user_id);
        
        $getEvent = $this->Api_model->getSingleRow(EVT_TBL, array(
            'id' => $event_id
        ));
        if (!$getEvent) {
            $this->api->api_message(0, EVT_NT_FND);
            exit();
        } //!$getEvent
        
        $data['user_id']  = $user_id;
        $data['event_id'] = $event_id;
        
        $getGuest = $this->Api_model->getSingleRow(EVT_GST_TBL, array(
            'user_id' => $user_id,
            'event_id' => $event_id
        ));
        if ($getGuest) {
            $this->api->api_message(0, JN_ALREADY);
            exit();
        } //$getGuest
        
        $user = $this->Api_model->getSingleRow(USR_TBL, array(
            'id' => $user_id
        ));
        if ($user->name) {
            $userName = $user->name;
        } //$user->name
        else {
            $userName = $user->country_code . $user->mobile;
        }
        
        $getId = $this->Api_model->insertGetId(EVT_GST_TBL, $data);
        if ($getId) {
            $getGuests = $this->Api_model->getAllDataWhere(array(
                'event_id' => $event_id
            ), EVT_GST_TBL);
            
            if ($getGuest) {
                foreach ($getGuest as $getGuest) {
                    $getUser     = $this->Api_model->getSingleRow(USR_TBL, array(
                        'id' => $getGuest->user_id
                    ));
                    $deviceToken = $getUser->device_token;
                    if ($deviceToken) {
                        $title_sent = "Join Event";
                        $msg_sent   = $userName . " recenlty join event.";
                        
                        $API_ACCESS_KEY = 'AAAA9Eytjuk:APA91bEwOIgFw9Fwn-Ke0ZxZmFn5Tvh4nWtoKeydqxWqAMe7zTq05kAOE6XP17iEHrMBBPomQ8ruMgas34wULL6uqm_uYyasrgN0ZG5fysMJ91RPazo50JMAtifoYTHN6QxfjZSj-yzr';
                        
                        $msg = array(
                            'body' => $msg_sent,
                            'title' => $title,
                            'type' => '1003',
                            'icon' => 'myicon',
                            /*Default Icon*/
                            'sound' => 'mySound'
                            /*Default sound*/
                        );
                        if ($getUser->device_type == "IOS") {
                            $fields = array(
                                'to' => $deviceToken,
                                'notification' => $msg
                            );
                        } //$getUser->device_type == "IOS"
                        else {
                            $fields = array(
                                'to' => $deviceToken,
                                'data' => $msg
                            );
                        }
                        $headers = array(
                            'Authorization: key=' . $API_ACCESS_KEY,
                            'Content-Type: application/json'
                        );
                        #Send Reponse To FireBase Server    
                        $ch      = curl_init();
                        curl_setopt($ch, CURLOPT_URL, 'https://fcm.googleapis.com/fcm/send');
                        curl_setopt($ch, CURLOPT_POST, true);
                        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
                        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
                        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
                        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
                        $result = curl_exec($ch);
                        curl_close($ch);
                    } //$deviceToken
                } //$getGuest as $getGuest
            } //$getGuest
            
            $this->api->api_message(1, JOIN_EVT);
        } //$getId
        else {
            $this->api->api_message(0, TRY_AGAIN);
        }
    }
    
    /*Get Recent join User on Event*/
    public function eventJoinUser()
    {
        $this->form_validation->set_rules('event_id', 'event_id', 'required');
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $event_id = $this->input->post('event_id', TRUE);
        $user_id  = $this->input->post('user_id', TRUE);
        $this->checkUserStatus($user_id);
        
        $getEvent = $this->Api_model->getSingleRow(EVT_TBL, array(
            'id' => $event_id
        ));
        if (!$getEvent) {
            $this->api->api_message(0, EVT_NT_FND);
            exit();
        } //!$getEvent
        
        $getUser = $this->Api_model->getAllDataLimitWhere(EVT_GST_TBL, array(
            'event_id' => $event_id
        ), 10);
        if ($getUser) {
            $getUsers = array();
            foreach ($getUser as $getUser) {
                $user                  = $this->Api_model->getSingleRow(USR_TBL, array(
                    'id' => $getUser->user_id
                ));
                $getUser->userName     = $user->name;
                $getUser->mobile       = $user->mobile;
                $getUser->country_code = $user->country_code;
                $getUser->image        = base_url() . $user->image;
                
                array_push($getUsers, $getUser);
            } //$getUser as $getUser
            
            $this->api->api_message_data(1, ALL_GUEST, 'getUsers', $getUsers);
        } //$getUser
        else {
            $this->api->api_message(0, EVT_NT_FND);
            exit();
        }
    }
    
    /*get All posts*/
    public function getEventMedia()
    {
        $this->form_validation->set_rules('event_id', 'event_id', 'required');
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $event_id = $this->input->post('event_id', TRUE);
        $user_id  = $this->input->post('user_id', TRUE);
        $this->checkUserStatus($user_id);
        
        
        $getEvent = $this->Api_model->getSingleRow(EVT_TBL, array(
            'id' => $event_id
        ));
        if (!$getEvent) {
            $this->api->api_message(0, EVT_NT_FND);
            exit();
        } //!$getEvent
        
        $getPost = $this->Api_model->getAllDataWhereNot(array(
            'event_id' => $event_id
        ), PST_TBL);
        if ($getPost) {
            $getPosts = array();
            foreach ($getPost as $getPost) {
                if ($getPost->post_type == 1 || $getPost->post_type == 2) {
                    $getPost->media       = base_url() . $getPost->media;
                    $getPost->thumb_image = base_url() . $getPost->thumb_image;
                } //$getPost->post_type == 1 || $getPost->post_type == 2
                
                array_push($getPosts, $getPost);
            } //$getPost as $getPost
            
            $this->api->api_message_data(1, ALL_POSTS, 'getPosts', $getPosts);
            exit();
        } //$getPost
        else {
            $this->api->api_message(0, PST_NT_FND);
            exit();
        }
    }
    
    /*******************************End Events Related API's**********************************/
    
    
    /*******************************Venue Related API's**********************************/
    /*Add new Events*/
    public function addVenue()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('venue_name', 'venue_name', 'required');
        $this->form_validation->set_rules('date', 'date', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        $this->form_validation->set_rules('address', 'address', 'required');
        $this->form_validation->set_rules('latitude', 'latitude', 'required');
        $this->form_validation->set_rules('longitude', 'longitude', 'required');
        
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $data['user_id']    = $this->input->post('user_id', TRUE);
        $function_id        = $this->input->post('function_id', TRUE);
        $data['venue_name'] = $this->input->post('venue_name', TRUE);
        $data['address']    = $this->input->post('address', TRUE);
        $data['latitude']   = $this->input->post('latitude', TRUE);
        $data['longitude']  = $this->input->post('longitude', TRUE);
        $image              = $this->input->post('image', TRUE);
        $data['date']       = $this->input->post('date', TRUE);
        
        $this->checkUserStatus($data['user_id']);
        $getFuction = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id
        ));
        if (!$getFuction) {
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFuction
        
        $this->load->library('upload');
        
        $config['image_library']  = 'gd2';
        $config['upload_path']    = './assets/functions/function' . $function_id . '/';
        $config['allowed_types']  = 'gif|jpg|jpeg|png';
        $config['max_size']       = 10000;
        $config['file_name']      = time();
        $config['create_thumb']   = TRUE;
        $config['maintain_ratio'] = TRUE;
        $config['width']          = 250;
        $config['height']         = 250;
        $this->upload->initialize($config);
        $functionImage = "";
        if ($this->upload->do_upload('image') && $this->load->library('image_lib', $config)) {
            $functionImage = 'assets/functions/function' . $function_id . '/' . $this->upload->data('file_name');
        } //$this->upload->do_upload('image') && $this->load->library('image_lib', $config)
        else {
            //  echo $this->upload->display_errors();
        }
        
        $data['function_id'] = $function_id;
        
        $venueId = $this->Api_model->insertGetId(VNU_TBL, $data);
        if ($venueId) {
            if ($functionImage) {
                $datas['image']       = $functionImage;
                $datas['function_id'] = $function_id;
                $datas['venue_id']    = $venueId;
                $this->Api_model->insertGetId(VN_MDA_TBL, $datas);
            } //$functionImage
            
            $this->api->api_message(1, ADD_VENUE);
        } //$venueId
        else {
            $this->api->api_message(0, TRY_AGAIN);
        }
    }
    
    /*Edit Events*/
    public function editVenue()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('venue_id', 'venue_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $venue_id    = $this->input->post('venue_id', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        $user_id     = $this->input->post('user_id', TRUE);
        $venue_name  = $this->input->post('venue_name', TRUE);
        $address     = $this->input->post('address', TRUE);
        $latitude    = $this->input->post('latitude', TRUE);
        $longitude   = $this->input->post('longitude', TRUE);
        $image       = $this->input->post('image', TRUE);
        $date        = $this->input->post('date', TRUE);
        
        $this->checkUserStatus($user_id);
        
        $getFuction = $this->Api_model->getSingleRow(VNU_TBL, array(
            'id' => $venue_id
        ));
        if (!$getFuction) {
            $this->api->api_message(0, VNU_NT_FND);
            exit();
        } //!$getFuction
        
        $getVenue = $this->Api_model->getSingleRow(VNU_TBL, array(
            'id' => $venue_id
        ));
        if (!$getVenue) {
            $this->api->api_message(0, EVT_NT_FND);
            exit();
        } //!$getVenue
        
        $this->load->library('upload');
        
        $config['image_library']  = 'gd2';
        $config['upload_path']    = './assets/functions/function' . $function_id . '/';
        $config['allowed_types']  = 'gif|jpg|jpeg|png';
        $config['max_size']       = 10000;
        $config['file_name']      = time();
        $config['create_thumb']   = TRUE;
        $config['maintain_ratio'] = TRUE;
        $config['width']          = 250;
        $config['height']         = 250;
        $this->upload->initialize($config);
        $functionImage = "";
        if ($this->upload->do_upload('image') && $this->load->library('image_lib', $config)) {
            $functionImage = 'assets/functions/function' . $function_id . '/' . $this->upload->data('file_name');
        } //$this->upload->do_upload('image') && $this->load->library('image_lib', $config)
        else {
            //  echo $this->upload->display_errors();
        }
        
        if ($functionImage) {
            $datas['image']       = $functionImage;
            $datas['function_id'] = $function_id;
            $datas['venue_id']    = $getVenue->id;
            $this->Api_model->insertGetId(VN_MDA_TBL, $datas);
        } //$functionImage
        
        $data['venue_name'] = isset($venue_name) ? $venue_name : $getVenue->venue_name;
        $data['address']    = isset($address) ? $address : $getVenue->address;
        $data['latitude']   = isset($latitude) ? $latitude : $getVenue->latitude;
        $data['longitude']  = isset($longitude) ? $longitude : $getVenue->longitude;
        $data['date']       = isset($date) ? $date : $getVenue->date;
        
        $this->Api_model->updateSingleRow(VNU_TBL, array(
            'id' => $venue_id
        ), $data);
        $this->api->api_message(1, EVT_UPDATED);
    }
    
    
    /*Get Events By Fucntion*/
    public function getVenueByFuctionId()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $user_id     = $this->input->post('user_id', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        
        $this->checkUserStatus($user_id);
        $getFunction = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id
        ));
        
        if (!$getFunction) {
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFunction
        else {
            $getVenue = $this->Api_model->getSingleRow(VNU_TBL, array(
                'function_id' => $function_id
            ));
            if ($getVenue) {
                $getVenueMedia = $this->Api_model->getAllDataWhere(array(
                    'venue_id' => $getVenue->id
                ), VN_MDA_TBL);
                
                $medias = array();
                if ($getVenueMedia) {
                    foreach ($getVenueMedia as $getVenueMedia) {
                        $getVenueMedia->image = base_url() . $getVenueMedia->image;
                        array_push($medias, $getVenueMedia);
                    } //$getVenueMedia as $getVenueMedia
                } //$getVenueMedia
                $getVenue->medias = $medias;
                
                $this->api->api_message_data(1, GET_VENUE, 'getVenue', $getVenue);
            } //$getVenue
            else {
                $this->api->api_message(0, FNC_NT_FND);
                exit();
            }
        }
    }
    
    /*******************************End Venue Related API's**********************************/
    
    /*******************************Guest Related API's**********************************/
    
    public function getGuestType()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $user_id     = $this->input->post('user_id', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        $this->checkUserStatus($user_id);
        $guestType = $this->Api_model->getAllDataWhere(array(
            'status' => 1
        ), GST_TYPE_TBL);
        if ($guestType) {
            $getPosts = array();
            foreach ($guestType as $guestType) {
                
                $getGuest = $this->Api_model->getAllDataWhere(array(
                    'type' => $guestType->id,
                    // 'user_id'=>$user_id,
                    'function_id' => $function_id
                    // 'event_id'=>$event_id
                ), 'guest_list');
                
                //    $getEvent = $this->Api_model->getSingleRow('events_list', array(
                //     'function_id' => $function_id,'user_id'=>$user_id,
                //     // 'id'=>$event_id
                //     ));
                //     if ($getEvent) {
                //        $guestType->event_id=$getEvent->id ;
                //     }
                //      else{
                //     $guestType->event_id=""; 
                // }
                $guestType->total_type_guest = count($getGuest);
                $guestType->function_id      = $function_id;
                // $guestType->event_id=$getEvent->id ;
                
                array_push($getPosts, $guestType);
            }
            
        }
        if ($guestType) {
            
            $this->api->api_message_data(1, ALL_EVENTS, 'guest_type_with_count', $getPosts);
        } //$guestType
        else {
            $this->api->api_message(0, NO_DATA);
        }
        
    }
    
    
    /*Get Events By Fucntion*/
    public function getAllUserByType()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        $this->form_validation->set_rules('type', 'type', 'required');
        
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $user_id     = $this->input->post('user_id', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        $type        = $this->input->post('type', TRUE);
        $this->checkUserStatus($user_id);
        $getUser = $this->Api_model->getAllDataWhere(array(
            'function_id' => $function_id,
            // 'user_id' => $user_id,
            'type' => $type
        ), 'guest_list');
        if (!empty($getUser)) {
            
            $getPosts = array();
            foreach ($getUser as $guestType) {
                $getGuest = $this->Api_model->getAllDataWhere(array(
                    'id' => $user_id
                ), 'user');
                
                $guestType->user_name = $getGuest[0]->name;
                $guestType->image     = base_url() . $getGuest[0]->image;
                
                array_push($getPosts, $guestType);
            }
        }
        
        if ($getUser) {
            $this->api->api_message_data(1, "ALL_USER", 'all_user', $getPosts);
        }
        
        else {
            $this->api->api_message(0, NO_DATA);
        }
    }
    
    public function addGuest()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('name', 'name', 'required');
        $this->form_validation->set_rules('mobile', 'mobile', 'required');
        $this->form_validation->set_rules('country_code', 'country_code', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        $this->form_validation->set_rules('type', 'type', 'required');
        // $this->form_validation->set_rules('event_id', 'event_id', 'required');
        
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $user_id      = $this->input->post('user_id', TRUE);
        $name         = $this->input->post('name', TRUE);
        $mobile       = $this->input->post('mobile', TRUE);
        $country_code = $this->input->post('country_code', TRUE);
        $function_id  = $this->input->post('function_id', TRUE);
        $type         = $this->input->post('type', TRUE);
        $event_id     = $this->input->post('event_id', TRUE);
        if ($event_id) {
            $event_id = $event_id;
        } else {
            $event_id = "";
        }
        
        
        $this->checkUserStatus($user_id);
        
        $getFuction = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id
        ));
        if (!$getFuction) {
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFuction
        else {
            if ($getFuction->status == 0) {
                $this->api->api_message(0, "This function is not approve by Admin. You need to ask to Admin for approving this function.");
                exit();
            } //$getFuction->status == 0
        }
        
        $msg = "You have been invited for “" . $getFuction->title . "”, please download celebrino app for detail! Enjoy the celebrations.\n Android: https://bit.ly/2HK4kRo \n iOS: ";
        //$this->send_opt_mobile($country_code.$mobile, $msg);
        
        $getUser = $this->Api_model->getSingleRow(USR_TBL, array(
            'mobile' => $mobile,
            'country_code' => $country_code
        ));
        if ($getUser) {
            $getGuest = $this->Api_model->getSingleRow(GST_LIST_TBL, array(
                'user_id' => $getUser->id,
                'function_id' => $function_id
            ));
            // print_r($getGuest);
            // exit(); 
            if ($getGuest) {
                $this->api->api_message(0, GST_ALREADY_ADD);
                exit();
            } //$getGuest
            
            $guestData['user_id']     = $user_id;
            $guestData['name']        = $name;
            $guestData['function_id'] = $function_id;
            $guestData['event_id']    = $event_id;
            $guestData['type']        = $type;
            
            $title = "New Function";
            $type  = "10012";
            $msg   = "You have been invited for “" . $getFuction->title . "”, please download";
            $this->firebase_notification($getUser->id, $msg, $title, $type, $function_id);
            
            $listID = $this->Api_model->insertGetId(GST_LIST_TBL, $guestData);
            if ($listID) {
                $this->api->api_message(1, ADD_GUEST);
            } //$listID
            else {
                $this->api->api_message(0, TRY_AGAIN);
            }
        } //$getUser
        else {
            $datauser['name']         = $name;
            $datauser['mobile']       = $mobile;
            $datauser['signup_at']    = time();
            $datauser['country_code'] = $country_code;
            $datauser['role_id']      = 1;
            
            $getUserId = $this->Api_model->insertGetId(USR_TBL, $datauser);
            
            $guestData['user_id']     = $getUserId;
            $guestData['name']        = $name;
            $guestData['function_id'] = $function_id;
            $guestData['type']        = $type;
            
            
            $listID = $this->Api_model->insertGetId(GST_LIST_TBL, $guestData);
            if ($listID) {
                $this->api->api_message(1, ADD_GUEST);
            } //$listID
            else {
                $this->api->api_message(0, TRY_AGAIN);
            }
        }
    }
    
    
    public function addGuestAttendence()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('name', 'name', 'required');
        $this->form_validation->set_rules('gender', 'gender', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        $this->form_validation->set_rules('age', 'age', 'required');
        
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $data['function_id'] = $this->input->post('function_id', TRUE);
        $data['user_id']     = $this->input->post('user_id', TRUE);
        $data['name']        = $this->input->post('name', TRUE);
        $data['age']         = $this->input->post('age', TRUE);
        $data['gender']      = $this->input->post('gender', TRUE);
        $data['created_at']  = time();
        $this->checkUserStatus($data['user_id']);
        
        $getFuction = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $data['function_id']
        ));
        if (!$getFuction) {
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFuction
        else {
            if ($getFuction->status == 0) {
                $this->api->api_message(0, "This function is not approve by Admin. You need to ask to Admin for approving this function.");
                exit();
            } //$getFuction->status == 0
        }
        
        $listID = $this->Api_model->insertGetId("guest_attendance", $data);
        if ($listID) {
            $this->api->api_message(1, ADD_GUEST);
        } //$listID
        else {
            $this->api->api_message(0, TRY_AGAIN);
        }
    }
    
    
    public function createGroup()
    {
        $this->form_validation->set_rules('type', 'type', 'required');
        
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $data['type'] = $this->input->post('type', TRUE);
        $listID       = $this->Api_model->insertGetId("guest_type", $data);
        if ($listID) {
            $this->api->api_message(1, 'Guest type added successfully');
        } //$listID
        else {
            $this->api->api_message(0, TRY_AGAIN);
        }
    }
    
    public function invitationConfirm()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        $this->form_validation->set_rules('attendance', 'attendance', 'required');
        
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $function_id = $this->input->post('function_id', TRUE);
        $user_id     = $this->input->post('user_id', TRUE);
        $attendance  = $this->input->post('attendance', TRUE);
        
        
        $this->checkUserStatus($user_id);
        
        $getFuction = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id
        ));
        
        
        $listID = $this->Api_model->updateSingleRow("guest_list", array(
            'function_id' => $function_id,
            "user_id" => $user_id
        ), array(
            'invitation_status' => 1,
            'attendance' => $attendance
        ));
        if ($listID) {
            $this->api->api_message_data(1, "Invitaion accepted successfully.", 'is_confirmed', $attendance);
        } //$listID
        else {
            $this->api->api_message(0, GST_ALREADY_ADD);
        }
    }
    
    public function notConfirm()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $function_id = $this->input->post('function_id', TRUE);
        $user_id     = $this->input->post('user_id', TRUE);
        $this->checkUserStatus($user_id);
        
        $getFuction = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id
        ));
        if (!$getFuction) {
            
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFuction
        else {
            if ($getFuction->status == 0) {
                $this->api->api_message(0, "This function is not approve by Admin. You need to ask to Admin for approving this function.");
                exit();
            } //$getFuction->status == 0
        }
        
        $listID = $this->Api_model->updateSingleRow("guest_list", array(
            'function_id' => $function_id,
            "user_id" => $user_id
        ), array(
            'invitation_status' => 0
        ));
        if ($listID) {
            $this->api->api_message(1, "Not accepted.");
        } //$listID
        else {
            $this->api->api_message(0, TRY_AGAIN);
        }
    }
    
    public function approvePost()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        // $this->form_validation->set_rules('event_id', 'event_id', 'required');
        $this->form_validation->set_rules('id', 'id', 'required');
        
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $function_id = $this->input->post('function_id', TRUE);
        $user_id     = $this->input->post('user_id', TRUE);
        // $event_id     = $this->input->post('event_id', TRUE);
        $id          = $this->input->post('id', TRUE);
        
        $this->checkUserStatus($user_id);
        
        $listID = $this->Api_model->updateSingleRow("post", array(
            'function_id' => $function_id,
            "user_id" => $user_id,
            // "event_id" =>$event_id,
            "id" => $id
        ), array(
            'status' => 1
        ));
        if ($listID) {
            $this->api->api_message(1, "Approved successfully.");
        } //$listID
        else {
            $this->api->api_message(0, TRY_AGAIN);
        }
    }
    public function HidePost()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        // $this->form_validation->set_rules('event_id', 'event_id', 'required');
        $this->form_validation->set_rules('id', 'id', 'required');
        
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $function_id = $this->input->post('function_id', TRUE);
        $user_id     = $this->input->post('user_id', TRUE);
        // $event_id     = $this->input->post('event_id', TRUE);
        $id          = $this->input->post('id', TRUE);
        $this->checkUserStatus($user_id);
        
        $listID = $this->Api_model->updateSingleRow("post", array(
            'function_id' => $function_id,
            "user_id" => $user_id,
            // "event_id" =>$event_id,
            "id" => $id
        ), array(
            'status' => 0
        ));
        if ($listID) {
            $this->api->api_message(1, "Hide successfully.");
        } //$listID
        else {
            $this->api->api_message(0, TRY_AGAIN);
        }
    }
    
    
    /*get Ticket*/
    public function getMyAttendence()
    {
        $user_id     = $this->input->post('user_id', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        $this->checkUserStatus($user_id);
        
        $get_attendence = $this->Api_model->getAllDataWhere(array(
            'user_id' => $user_id,
            'function_id' => $function_id
        ), 'guest_attendance');
        if ($get_attendence) {
            $this->api->api_message_data(1, "Get my attendence.", 'get_attendence', $get_attendence);
        } //$get_attendence
        else {
            $this->api->api_message(0, "Not yet any attendence.");
        }
    }
    
    public function removeGuestAttendence()
    {
        $this->form_validation->set_rules('id', 'id', 'required');
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $id          = $this->input->post('id', TRUE);
        $user_id     = $this->input->post('user_id', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        $this->checkUserStatus($user_id);
        
        $getFuction = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id
        ));
        if (!$getFuction) {
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFuction
        
        $getmedia = $this->Api_model->getSingleRow("guest_attendance", array(
            'id' => $id
        ));
        if ($getmedia) {
            $this->Api_model->deleteRecord(array(
                'id' => $id
            ), "guest_attendance");
            $this->api->api_message(1, "Guest deleted successfully.");
            exit();
        } //$getmedia
        else {
            $this->api->api_message(0, "Guest not found.");
            exit();
        }
    }
    ######################################################################################################
    // Announcement API
    public function addAnnouncement()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('title', 'title', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        $this->form_validation->set_rules('description', 'description', 'required');
        
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $data['user_id']     = $this->input->post('user_id', TRUE);
        $data['title']       = $this->input->post('title', TRUE);
        $image               = $this->input->post('image', TRUE);
        $data['description'] = $this->input->post('description', TRUE);
        $data['function_id'] = $this->input->post('function_id', TRUE);
        
        $this->checkUserStatus($data['user_id']);
        $data['created_at'] = time();
        $this->load->library('upload');
        
        $config['image_library']  = 'gd2';
        $config['upload_path']    = './assets/images/';
        $config['allowed_types']  = 'gif|jpg|jpeg|png';
        $config['max_size']       = 10000;
        $config['file_name']      = time();
        $config['create_thumb']   = TRUE;
        $config['maintain_ratio'] = TRUE;
        $config['width']          = 250;
        $config['height']         = 250;
        $this->upload->initialize($config);
        $functionImage = "";
        if ($this->upload->do_upload('image') && $this->load->library('image_lib', $config)) {
            $functionImage = 'assets/images/' . $this->upload->data('file_name');
        } //$this->upload->do_upload('image') && $this->load->library('image_lib', $config)
        
        if ($functionImage) {
            $data['image'] = $functionImage;
        } //$functionImage
        
        $functionId = $this->Api_model->insertGetId("announcement", $data);
        if ($functionId) {
            $getGuests = $this->Api_model->getAllDataWhere(array(
                'function_id' => $data['function_id']
            ), GST_LIST_TBL);
            
            foreach ($getGuests as $getGuests) {
                $title = $data['title'];
                $type  = "10019";
                $msg   = $data['description'];
                $this->firebase_notification($getGuests->user_id, $msg, $title, $type, $data['function_id']);
            } //$getGuests as $getGuests
            
            $this->api->api_message(1, "Announcement added successfully.");
        } //$functionId
        else {
            $this->api->api_message(0, TRY_AGAIN);
        }
    }
    
    public function getAnnouncement()
    {
        $user_id     = $this->input->post('user_id', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        $this->checkUserStatus($user_id);
        
        $getAnnouncement = $this->Api_model->getAllDataWhere(array(
            'function_id' => $function_id,
            'user_id' => $user_id
        ), 'announcement');
        if ($getAnnouncement) {
            $getAnnouncements = array();
            foreach ($getAnnouncement as $getAnnouncement) {
                if ($getAnnouncement->image) {
                    $getAnnouncement->image = base_url() . $getAnnouncement->image;
                } //$getAnnouncement->image
                
                array_push($getAnnouncements, $getAnnouncement);
            } //$getAnnouncement as $getAnnouncement
            $this->api->api_message_data(1, "Get announcement.", 'get_announcement', $getAnnouncements);
        } //$getAnnouncement
        else {
            $this->api->api_message(0, "Not yet any announcement.");
        }
    }
    
    
    // public function updateAnnouncement()
    // {
    //     $data['user_id']     = $this->input->post('user_id', TRUE);
    //     $data['title']       = $this->input->post('title', TRUE);
    //     $data['description'] = $this->input->post('description', TRUE);
    //     $data['function_id'] = $this->input->post('function_id', TRUE);
    //     $data['id'] = $this->input->post('id', TRUE);
    
    
    //     $user_id             = $data['user_id'];
    
    //     $userStatus = $this->Api_model->getSingleRow('user', array(
    //         'id' => $user_id
    //     ));
    //     if ($userStatus) {
    //         if ($userStatus->status == 1) {
    
    //             $table     = 'announcement';
    //             $condition = array(
    //                 'function_id' => $data['function_id'],
    //                 'user_id' => $user_id
    //             );
    
    //             $check_basket = $this->Api_model->updateSingleRow('announcement', array(
    //                 'function_id' => $data['function_id'],
    //                 'user_id'=> $data['user_id'],
    //                 'id'=>  $data['id'] 
    //             ), $data);
    
    //             if ($check_basket) {
    //                 $this->api->api_message(1, FNC_UPDATED);
    //             } //$check_basket
    //             else {
    //                 $this->api->api_message(0, FNC_NT_FND);
    //             }
    //         } //$userStatus->status == 1
    //     } //$userStatus
    // }
    
    public function updateAnnouncement()
    {
        $data['user_id']     = $this->input->post('user_id', TRUE);
        $data['title']       = $this->input->post('title', TRUE);
        $data['description'] = $this->input->post('description', TRUE);
        $data['function_id'] = $this->input->post('function_id', TRUE);
        $data['id']          = $this->input->post('id', TRUE);
        $user_id             = $data['user_id'];
        
        $this->load->library('upload');
        
        $config['image_library']  = 'gd2';
        $config['upload_path']    = './assets/images/';
        $config['allowed_types']  = 'gif|jpg|jpeg|png';
        $config['max_size']       = 10000;
        $config['file_name']      = time();
        $config['create_thumb']   = TRUE;
        $config['maintain_ratio'] = TRUE;
        $config['width']          = 250;
        $config['height']         = 250;
        $this->upload->initialize($config);
        $functionImage = "";
        if ($this->upload->do_upload('image') && $this->load->library('image_lib', $config)) {
            $functionImage = 'assets/images/' . $this->upload->data('file_name');
        }
        
        if ($functionImage) {
            $data['image'] = $functionImage;
        }
        
        $userStatus = $this->Api_model->getSingleRow('user', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                
                $table     = 'announcement';
                $condition = array(
                    'function_id' => $data['function_id'],
                    'user_id' => $user_id
                );
                
                $check_basket = $this->Api_model->updateSingleRow('announcement', array(
                    'function_id' => $data['function_id'],
                    'user_id' => $data['user_id'],
                    'id' => $data['id']
                ), $data);
                
                if ($check_basket) {
                    
                    $this->api->api_message(1, FNC_UPDATED);
                } //$check_basket
                else {
                    $this->api->api_message(0, FNC_NT_FND);
                }
            } //$userStatus->status == 1
        } //$userStatus
    }
    
    public function removeAnnouncement()
    {
        $function_id     = $this->input->post('function_id', TRUE);
        $user_id         = $this->input->post('user_id', TRUE);
        $announcement_id = $this->input->post('id', TRUE);
        
        $userStatus = $this->Api_model->getSingleRow('announcement', array(
            'user_id' => $user_id,
            'function_id' => $function_id,
            'id' => $announcement_id
        ));
        
        if ($userStatus) {
            
            $this->Api_model->deleteRecord(array(
                'function_id' => $function_id,
                'user_id' => $user_id,
                'id' => $announcement_id
            ), 'announcement');
            
            $this->api->api_message(1, ANNOUNC_DLT);
            
        } //$userStatus
        else {
            $this->api->api_message(0, FNC_NT_FND);
            
        }
    }
    ###############################################################################################################
    // OFFER API
    public function getOffer()
    {
        $getOffer = $this->Api_model->getAllDataWhere(array(
            'status' => 1
        ), 'offer');
        if ($getOffer) {
            $getOfferArray = array();
            foreach ($getOffer as $getOffer) {
                
                if ($getOffer->image) {
                    $getOffer->image = base_url() . $getOffer->image;
                } //$getOffer->image
                $gettitle = $this->Api_model->getAllDataWhere(array(
                    'id' => $getOffer->service_type_id
                ), 'service_type');
                foreach ($gettitle as $gettitle) {
                    $getOffer->title = $gettitle->title;
                } //$gettitle as $gettitle
                
                
                array_push($getOfferArray, $getOffer);
            } //$getOffer as $getOffer
            $this->api->api_message_data(1, "All Offer", 'getOffer', $getOfferArray);
            
        } //$getOffer
        else {
            $this->api->api_message(0, "No Offer");
            
        }
        
    }
    
    // OFFER API
    public function getFunction()
    {
        
        $function_id = $this->input->post('function_id', TRUE);
        $user_id     = $this->input->post('user_id', TRUE);
        $getOffer    = $this->Api_model->getAllDataWhere(array(
            'function_id' => $function_id,
            'user_id' => $user_id
        ), 'events_list');
        if ($getOffer) {
            $getOfferArray = array();
            foreach ($getOffer as $getOffer) {
                
                if ($getOffer->image) {
                    $getOffer->image = base_url() . $getOffer->image;
                }
                
                array_push($getOfferArray, $getOffer);
            } //$getOffer as $getOffer
            $this->api->api_message_data(1, "All Function", 'getFunction', $getOfferArray);
            
        } //$getOffer
        else {
            $this->api->api_message(0, "No Function");
            
        }
        
    }
    
    // guest API
    public function getGuest()
    {
        $function_id = $this->input->post('function_id', TRUE);
        $getOffer    = $this->Api_model->getAllDataWhere(array(
            'status' => 1,
            'function_id' => $function_id
        ), 'guest_list');
        
        if ($getOffer) {
            $getOfferArray = array();
            foreach ($getOffer as $getOffer) {
                
                $getUserDetail        = $this->Api_model->getSingleRow('user', array(
                    'id' => $getOffer->user_id
                ));
                $getOffer->user_name  = $getUserDetail->name;
                $getOffer->user_image = base_url() . $getUserDetail->image;
                array_push($getOfferArray, $getOffer);
            } //$getOffer as $getOffer
            $this->api->api_message_data(1, "All guest", 'get_guest', $getOfferArray);
            
        } //$getOffer
        else {
            $this->api->api_message(0, "No guest");
        }
    }
    
    // guest API
    public function getGuestEvent()
    {
        $function_id = $this->input->post('function_id', TRUE);
        $user_id     = $this->input->post('user_id', TRUE);
        $getOffer    = $this->Api_model->getAllDataWhere(array(
            'status' => 1,
            'function_id' => $function_id,
            'user_id' => $user_id
        ), 'guest_list');
        if ($getOffer) {
            $getOfferArray = array();
            foreach ($getOffer as $getOffer) {
                
                $getUserDetail        = $this->Api_model->getSingleRow('user', array(
                    'id' => $getOffer->user_id
                ));
                $getOffer->user_name  = $getUserDetail->name;
                $getOffer->user_image = base_url() . $getUserDetail->image;
                array_push($getOfferArray, $getOffer);
            } //$getOffer as $getOffer
            $this->api->api_message_data(1, "All Guest", 'getGuest', $getOfferArray);
            
        } //$getOffer
        else {
            $this->api->api_message(0, "No Guest");
            
        }
        
    }
    
    public function getGuestSubEvent()
    {
        $event_id = $this->input->post('event_id', TRUE);
        $user_id  = $this->input->post('user_id', TRUE);
        $getOffer = $this->Api_model->getAllDataWhere(array(
            'event_id' => $event_id
            // 'user_id'=>$user_id,
        ), 'evnet_guest');
        if ($getOffer) {
            $getOfferArray = array();
            foreach ($getOffer as $getOffer) {
                
                $getUserDetail        = $this->Api_model->getSingleRow('user', array(
                    'id' => $getOffer->user_id
                ));
                $getOffer->user_name  = $getUserDetail->name;
                $getOffer->user_image = base_url() . $getUserDetail->image;
                array_push($getOfferArray, $getOffer);
            } //$getOffer as $getOffer
            $this->api->api_message_data(1, "All Guest", 'getGuest', $getOfferArray);
            
        } //$getOffer
        else {
            $this->api->api_message(0, "No Guest");
            
        }
        
    }
    ##########################################################################################################
    public function uploadGuestCSV()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $function_id = $this->input->post('function_id');
        $getFuction  = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id
        ));
        if (!$getFuction) {
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFuction
        
        $file_data = $this->csvimport->get_array($_FILES["csv_file"]["tmp_name"]);
        $data      = array();
        $total     = count($file_data);
        foreach ($file_data as $file_data) {
            $name         = $file_data['name'];
            $address      = $file_data['venue_address'];
            $mobile       = $file_data['mobile'];
            $country_code = $file_data['country_code'];
            $type         = $file_data['type'];
            $getUser      = $this->Api_model->getSingleRow(USR_TBL, array(
                'mobile' => $mobile,
                'country_code' => $country_code
            ));
            if ($getUser) {
                $getGuest = $this->Api_model->getSingleRow(GST_LIST_TBL, array(
                    'user_id' => $getUser->id,
                    'function_id' => $function_id
                ));
                if (!$getGuest) {
                    $guestData['user_id']       = $getUser->id;
                    $guestData['name']          = $name;
                    $guestData['venue_address'] = $address;
                    $guestData['function_id']   = $function_id;
                    $guestData['type']          = $type;
                    $listID                     = $this->Api_model->insertGetId(GST_LIST_TBL, $guestData);
                } //!$getGuest
            } //$getUser
            else {
                $datauser['name']           = $name;
                $datauser['mobile']         = $mobile;
                $datauser['country_code']   = $country_code;
                $datauser['role_id']        = 1;
                $getUserId                  = $this->Api_model->insertGetId(USR_TBL, $datauser);
                $guestData['user_id']       = $getUserId;
                $guestData['venue_address'] = $address;
                $guestData['name']          = $name;
                $guestData['function_id']   = $function_id;
                $guestData['type']          = $type;
                
                $listID = $this->Api_model->insertGetId(GST_LIST_TBL, $guestData);
            }
        } //$file_data as $file_data
        $this->api->api_message(1, "Guest List Uploaded successfully.");
    }
    
    
    /*Get Guest according to function*/
    public function getGuestByFunction()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $user_id     = $this->input->post('user_id', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        
        $this->checkUserStatus($user_id);
        
        $getGuest  = $this->Api_model->getAllDataWhere(array(
            'function_id' => $function_id
        ), GST_LIST_TBL);
        $getGuests = array();
        if ($getGuest) {
            foreach ($getGuest as $getGuest) {
                $getUser = $this->Api_model->getSingleRow(USR_TBL, array(
                    'id' => $getGuest->user_id
                ));
                if ($getUser) {
                    if ($getUser->image) {
                        $getUser->image = base_url() . $getUser->image;
                    } //$getUser->image
                    else {
                        $getUser->image = base_url() . "assets/images/image.png";
                    }
                    $getUser->mobile           = $getUser->mobile;
                    $getUser->country_code     = $getUser->country_code;
                    $getUser->attendance_count = $this->Api_model->getCount('guest_attendance', array(
                        'function_id' => $function_id,
                        "user_id" => $getUser->id
                    ));
                } //$getUser
                array_push($getGuests, $getUser);
            } //$getGuest as $getGuest
            $this->api->api_message_data(1, ALL_GUEST, 'guest', $getGuests);
        } //$getGuest
        else {
            $this->api->api_message(0, NO_DATA);
        }
    }
    
    /*Get Guest according to function*/
    public function getGuestByType()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        $this->form_validation->set_rules('type', 'type', 'required');
        
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $user_id     = $this->input->post('user_id', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        $type        = $this->input->post('type', TRUE);
        
        $this->checkUserStatus($user_id);
        
        $getGuest  = $this->Api_model->getAllDataWhere(array(
            'function_id' => $function_id,
            'type' => $type
        ), GST_LIST_TBL);
        $getGuests = array();
        if ($getGuest) {
            foreach ($getGuest as $getGuest) {
                $getUser = $this->Api_model->getSingleRow(USR_TBL, array(
                    'id' => $getGuest->user_id
                ));
                if ($getUser) {
                    if ($getUser->image) {
                        $getGuest->image = base_url() . $getUser->image;
                    } //$getUser->image
                    else {
                        $getGuest->image = base_url() . "assets/images/image.png";
                    }
                    $getGuest->mobile           = $getUser->mobile;
                    $getGuest->country_code     = $getUser->country_code;
                    $getGuest->attendance_count = $this->Api_model->getCount('guest_attendance', array(
                        'function_id' => $function_id,
                        "user_id" => $getUser->id
                    ));
                } //$getUser
                
                array_push($getGuests, $getGuest);
            } //$getGuest as $getGuest
            $this->api->api_message_data(1, ALL_GUEST, 'guest', $getGuests);
        } //$getGuest
        else {
            $this->api->api_message(0, NO_DATA);
        }
    }
    
    
    /*Remove Guests*/
    public function removeGuest()
    {
        $this->form_validation->set_rules('guest_id', 'guest_id', 'required');
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $guest_id    = $this->input->post('guest_id', TRUE);
        $user_id     = $this->input->post('user_id', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        $this->checkUserStatus($user_id);
        
        $getFuction = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id
        ));
        if (!$getFuction) {
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFuction
        
        $getmedia = $this->Api_model->getSingleRow(GST_LIST_TBL, array(
            'id' => $guest_id,
            'function_id' => $function_id
        ));
        if ($getmedia) {
            $this->Api_model->deleteRecord(array(
                'id' => $guest_id
            ), GST_LIST_TBL);
            $this->api->api_message(1, GST_DLT);
            exit();
        } //$getmedia
        else {
            $this->api->api_message(0, NO_DATA);
            exit();
        }
    }
    
    /*******************************End Guest Related API's**********************************/
    
    /*******************************DashBoard Related API's**********************************/
    public function dashboard()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('type', 'type', 'required');
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $user_id = $this->input->post('user_id', TRUE);
        $type    = $this->input->post('type', TRUE);
        
        $this->checkUserStatus($user_id);
        if ($type == 1) {
            $getFunction  = $this->Api_model->getWhereInStatusResult(FCN_TBL, array(
                'user_id' => $user_id
            ), 'status', array(
                2,
                1
            ));
            $getFunctions = array();
            if ($getFunction) {
                foreach ($getFunction as $getFunction) {
                    $getFunction->attendance_count = $this->Api_model->getCount('guest_attendance', array(
                        'function_id' => $getFunction->id
                    ));
                    $getFunctionnn                 = $this->Api_model->getCount('guest_attendance', array(
                        'function_id' => $getFunction->id
                    ));
                    $getFunctionnn                 = $this->Api_model->getAllDataWhere(array(
                        'function_id' => $getFunction->id
                    ), 'guest_list');
                    $getFunctionn                  = $this->Api_model->getAllDataWhere(array(
                        'function_id' => $getFunction->id,
                        'invitation_status' => 1
                    ), 'guest_list');
                    $getFunn                       = $this->Api_model->getAllDataWhere(array(
                        'function_id' => $getFunction->id,
                        'invitation_status' => 0
                    ), 'guest_list');
                    $getUser                       = $this->Api_model->getSingleRow('user', array(
                        'id' => $getFunction->user_id
                    ));
                    $productsImage                 = $this->Api_model->getAllDataWhereConcat(array(
                        'function_id' => $getFunction->id
                    ), 'function_gallery');
                    $getFunction->media            = $productsImage;
                    
                    $getFunction->user_name  = $getUser->name;
                    // $getFunction->image            = base_url().$getFunction->image;
                    $getFunction->type       = "1";
                    $getFunction->accept     = count($getFunctionn);
                    $getFunction->not_accept = count($getFunn);
                    
                    $getFunction->invitation = count($getFunctionnn);
                    
                    array_push($getFunctions, $getFunction);
                } //$getFunction as $getFunction
            } //$getFunction
            
            $getGuest = $this->Api_model->getAllDataWhere(array(
                'user_id' => $user_id,
                'function_id' =>$function_id
            ), GST_LIST_TBL);
            
            $getGuests = array();
            if ($getGuest) {
                foreach ($getGuest as $getGuest) {
                    
                    
                    $getFun = $this->Api_model->getSingleRow(FCN_TBL, array(
                        'id' => $getGuest->function_id,
                        'status' => 2
                    ));
                    if ($getFun) {
                        $getFun->attendance_count = $this->Api_model->getCount('guest_attendance', array(
                            'function_id' => $getFun->id
                        ));
                        $getinvitgetFuned         = $this->Api_model->getAllDataWhere(array(
                            'function_id' => $getFun->id,
                            'invitation_status' => 1
                        ), 'guest_list');
                        
                        
                        $invited = $this->Api_model->getSingleRow('guest_list', array(
                            'function_id' => $getFun->id
                        ));
                        
                        
                        $getaccept     = $this->Api_model->getAllDataWhere(array(
                            'function_id' => $getFun->id
                        ), 'guest_list');
                        $getnotaccept  = $this->Api_model->getAllDataWhere(array(
                            'function_id' => $getFun->id,
                            'invitation_status' => 0
                        ), 'guest_list');
                        $productsImage = $this->Api_model->getAllDataWhereConcat(array(
                            'function_id' => $getFun->id
                        ), 'function_gallery');
                        $getFun->media = $productsImage;
                        
                        $getFun->invitation   = count($getaccept);
                        $getFun->accept       = count($invited);
                        $getFun->not_accept   = count($getnotaccept);
                        $getFun->is_confirmed = $invited->invitation_status;
                        
                        // $getFun->image            = base_url() . $getFun->image;
                        $getFun->type = "0";
                        // $getFun->accept           = "1";
                        // $getFun->not_accept       = "0";
                        
                        array_push($getGuests, $getFun);
                    } //$getFun
                } //$getGuest as $getGuest
            } //$getGuest
            
            array_multisort(array_map('strtotime', array_column($getFunctions, 'date')), SORT_ASC, $getFunctions);
            array_multisort(array_map('strtotime', array_column($getGuests, 'date')), SORT_ASC, $getGuests);
            
            $data['my_function'] = $getFunctions;
            $data['as_a_guest']  = $getGuests;
            
        } //$type == 1
        if ($type == 2) {
            $getPastEvent  = $this->Api_model->getPastEvent($user_id);
            $getPastEvents = array();
            if ($getPastEvent) {
                foreach ($getPastEvent as $getPastEventt) {
                    // $getPastEventt->image     = base_url() . $getPastEventt['image'];
                    $getUser = $this->Api_model->getSingleRow('user', array(
                        'id' => $getPastEventt['user_id']
                    ));
                    
                    $getPastEventt['user_name'] = $getUser->name;
                    
                    $productsImage          = $this->Api_model->getAllDataWhereConcat(array(
                        'function_id' => $getPastEventt['id']
                    ), 'function_gallery');
                    $getPastEventt['media'] = $productsImage;
                    
                    
                    $getinvited   = $this->Api_model->getAllDataWhere(array(
                        'function_id' => $getPastEventt['id'],
                        'invitation_status' => 1
                    ), 'guest_list');
                    $getaccept    = $this->Api_model->getAllDataWhere(array(
                        'function_id' => $getPastEventt['id']
                    ), 'guest_list');
                    $getnotaccept = $this->Api_model->getAllDataWhere(array(
                        'function_id' => $getPastEventt['id'],
                        'invitation_status' => 0
                    ), 'guest_list');
                    
                    $getPastEventt['invitation'] = count($getaccept);
                    $getPastEventt['accept']     = count($getinvited);
                    $getPastEventt['not_accept'] = count($getnotaccept);
                    // $getPastEventt['image'] = base_url() .$getPastEventt['image'];
                    array_push($getPastEvents, $getPastEventt);
                } //$getPastEvent as $getPastEventt
            } //$getPastEvent
            array_multisort($getPastEvents, SORT_ASC, $getPastEvents);
            // array_multisort(array_map('strtotime', array_column($getPastEvents, 'date')), SORT_ASC, $getPastEvents);
            $data['my_function'] = $getPastEvents;
            $data['as_a_guest']  = array();
        } //$type == 2
        
        if ($type == 3) {
            $getallevent  = $this->Api_model->getAllDataWhere(array(
                'user_id' => $user_id
            ), 'function');
            $getallevents = array();
            if ($getallevent) {
                foreach ($getallevent as $getallevent) {
                    $getUser = $this->Api_model->getAllDataWhere(array(
                        'id' => $getallevent->user_id
                    ), 'user');
                    
                    $getallevent->user_name = $getUser[0]->name;
                    
                    $productsImage      = $this->Api_model->getAllDataWhereConcat(array(
                        'function_id' => $getallevent->id
                    ), 'function_gallery');
                    $getallevent->media = $productsImage;
                    
                    // $getallevent->image = base_url() .$getallevent->image;
                    $getinvited   = $this->Api_model->getAllDataWhere(array(
                        'function_id' => $getallevent->id,
                        'invitation_status' => 1
                    ), 'guest_list');
                    $getaccept    = $this->Api_model->getAllDataWhere(array(
                        'function_id' => $getallevent->id
                    ), 'guest_list');
                    $getnotaccept = $this->Api_model->getAllDataWhere(array(
                        'function_id' => $getallevent->id,
                        'invitation_status' => 0
                    ), 'guest_list');
                    
                    $getallevent->invitation = count($getaccept);
                    $getallevent->accept     = count($getinvited);
                    $getallevent->not_accept = count($getnotaccept);
                    
                    array_push($getallevents, $getallevent);
                } //$getallevent as $getallevent
            } //$getallevent
            array_multisort(array_map('strtotime', array_column($getallevents, 'date')), SORT_ASC, $getallevents);
            $data['my_function'] = $getallevents;
            $data['as_a_guest']  = array();
        } //$type == 3
        
        if ($data) {
            $this->api->api_message_data(1, ALL_FUNCTION, 'dashboard_data', $data);
        } //$data
        else {
            $this->api->api_message(0, NO_DATA);
        }
    }
    
    
    public function eventHome()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $user_id = $this->input->post('user_id', TRUE);
        
        $this->checkUserStatus($user_id);
        $getFunction  = $this->Api_model->getWhereInStatusResult(FCN_TBL, array(
            'user_id' => $user_id
        ), 'status', array(
            2,
            1
        ));
        $getFunctions = array();
        if ($getFunction) {
            foreach ($getFunction as $getFunction) {
                $getFunction->attendance_count = $this->Api_model->getCount('guest_attendance', array(
                    'function_id' => $getFunction->id
                ));
                $getUser                       = $this->Api_model->getSingleRow('user', array(
                    'id' => $getFunction->user_id
                ));
                $getFunction->user_name        = $getUser->name;
                $getFunction->image            = base_url() . $getFunction->image;
                $getFunction->type             = "1";
                
                array_push($getFunctions, $getFunction);
            } //$getFunction as $getFunction
        } //$getFunction
        
        $getGuest  = $this->Api_model->getAllDataWhere(array(
            'user_id' => $user_id
        ), GST_LIST_TBL);
        $getGuests = array();
        if ($getGuest) {
            foreach ($getGuest as $getGuest) {
                $getFun = $this->Api_model->getSingleRow(FCN_TBL, array(
                    'id' => $getGuest->function_id,
                    'status' => 1
                ));
                if ($getFun) {
                    $getFun->attendance_count = $this->Api_model->getCount('guest_attendance', array(
                        'function_id' => $getFun->id
                    ));
                    $getFun->image            = base_url() . $getFun->image;
                    $getFun->type             = "0";
                    array_push($getGuests, $getFun);
                } //$getFun
            } //$getGuest as $getGuest
        } //$getGuest
        $data = array_merge($getGuests, $getFunctions);
        array_multisort(array_map('strtotime', array_column($data, 'date')), SORT_ASC, $data);
        if ($data) {
            $this->api->api_message_data(1, ALL_EVENTS, 'dashboard_data', $data);
        } //$data
        else {
            $this->api->api_message(0, NO_DATA);
        }
    }
    /*******************************End Guest Related API's**********************************/
    
    /*******************************Invitation Card Related API's**********************************/
    
    /*Add invitation Card*/
    public function AddCard()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $user_id     = $this->input->post('user_id', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        $this->checkUserStatus($user_id);
        
        $getFuction = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id
        ));
        if (!$getFuction) {
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFuction
        // If file upload form submitted
        if (isset($_FILES['files'])) {
            $filesCount = count($_FILES['files']['name']);
            for ($i = 0; $i < $filesCount; $i++) {
                $_FILES['file']['name']     = $_FILES['files']['name'][$i];
                $_FILES['file']['type']     = $_FILES['files']['type'][$i];
                $_FILES['file']['tmp_name'] = $_FILES['files']['tmp_name'][$i];
                $_FILES['file']['error']    = $_FILES['files']['error'][$i];
                $_FILES['file']['size']     = $_FILES['files']['size'][$i];
                // File upload configuration
                if (!is_dir('./assets/functions/function' . $function_id . '/cards')) {
                    mkdir('./assets/functions/function' . $function_id . '/cards', 0777, TRUE);
                } //!is_dir('./assets/functions/function' . $function_id . '/cards')
                $uploadPath              = './assets/functions/function' . $function_id . '/cards';
                $config['upload_path']   = $uploadPath;
                $config['allowed_types'] = 'gif|jpg|jpeg|png';
                $config['encrypt_name']  = TRUE;
                // Load and initialize upload library
                $this->load->library('upload', $config);
                
                $this->upload->initialize($config);
                // Upload file to server
                if ($this->upload->do_upload('file')) {
                    // Uploaded file data
                    $fileData = $this->upload->data();
                    $fileData['file_name'];
                    $uploadData[$i] = 'assets/functions/function' . $function_id . '/cards/' . $fileData['file_name'];
                } //$this->upload->do_upload('file')
                else {
                    $a = $this->upload->display_errors();
                    $this->api->api_message(0, $a);
                    exit();
                }
            } //$i = 0; $i < $filesCount; $i++
            
            foreach ($uploadData as $uploadData) {
                $getGuests = $this->Api_model->getAllDataWhere(array(
                    'function_id' => $function_id
                ), GST_LIST_TBL);
                foreach ($getGuests as $getGuests) {
                    $title = "Invitaion Card";
                    $type  = "10015";
                    $msg   = "New invitation card added on " . $getFuction->title;
                    $this->firebase_notification($getGuests->user_id, $msg, $title, $type, $function_id);
                } //$getGuests as $getGuests
                
                $data['function_id'] = $function_id;
                $data['image']       = $uploadData;
                $listID              = $this->Api_model->insertGetId(IVN_CRD_TBL, $data);
            } //$uploadData as $uploadData
            $this->api->api_message(1, ADD_CRD);
            exit();
        } //isset($_FILES['files'])
        else {
            $this->api->api_message(0, "Please Select any image.");
            exit();
        }
    }
    
    /*Get Invitaion Card by Function*/
    public function getInvitationCards()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $user_id     = $this->input->post('user_id', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        $this->checkUserStatus($user_id);
        
        $getFuction = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id
        ));
        if (!$getFuction) {
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFuction
        
        $cards = $this->Api_model->getAllDataWhere(array(
            'function_id' => $function_id
        ), IVN_CRD_TBL);
        
        if ($cards) {
            $card = array();
            foreach ($cards as $cards) {
                $cards->image = base_url() . $cards->image;
                array_push($card, $cards);
            } //$cards as $cards
            $this->api->api_message_data(1, GET_CARD, 'getInvitationCards', $card);
        } //$cards
        else {
            $this->api->api_message(0, NO_DATA);
            exit();
        }
    }
    
    /*Remove Guests*/
    public function removeInvitationCardImage()
    {
        $this->form_validation->set_rules('card_id', 'card_id', 'required');
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $card_id     = $this->input->post('card_id', TRUE);
        $user_id     = $this->input->post('user_id', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        $this->checkUserStatus($user_id);
        
        $getFuction = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id
        ));
        if (!$getFuction) {
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFuction
        
        $getmedia = $this->Api_model->getSingleRow(IVN_CRD_TBL, array(
            'id' => $card_id,
            'function_id' => $function_id
        ));
        if ($getmedia) {
            $this->Api_model->deleteRecord(array(
                'id' => $card_id
            ), IVN_CRD_TBL);
            $this->api->api_message(1, CRD_DLT);
            exit();
        } //$getmedia
        else {
            $this->api->api_message(0, NO_DATA);
            exit();
        }
    }
    
    /*******************************End Invitation Card Related API's******************************/
    
    /*******************************Function Gallery Related API's******************************/
    
    public function addFunctionGallery()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $user_id     = $this->input->post('user_id', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        $title       = $this->input->post('title', TRUE);
        $description = $this->input->post('description', TRUE);
        $this->checkUserStatus($user_id);
        
        $getFuction = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id
        ));
        if (!$getFuction) {
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFuction
        // If file upload form submitted
        if (isset($_FILES['files'])) {
            $filesCount = count($_FILES['files']['name']);
            for ($i = 0; $i < $filesCount; $i++) {
                $_FILES['file']['name']     = $_FILES['files']['name'][$i];
                $_FILES['file']['type']     = $_FILES['files']['type'][$i];
                $_FILES['file']['tmp_name'] = $_FILES['files']['tmp_name'][$i];
                $_FILES['file']['error']    = $_FILES['files']['error'][$i];
                $_FILES['file']['size']     = $_FILES['files']['size'][$i];
                // File upload configuration
                if (!is_dir('./assets/functions/function' . $function_id . '/gallery')) {
                    mkdir('./assets/functions/function' . $function_id . '/gallery', 0777, TRUE);
                } //!is_dir('./assets/functions/function' . $function_id . '/gallery')
                $uploadPath              = './assets/functions/function' . $function_id . '/gallery';
                $config['upload_path']   = $uploadPath;
                $config['allowed_types'] = 'gif|jpg|jpeg|png';
                $config['encrypt_name']  = TRUE;
                // Load and initialize upload library
                $this->load->library('upload', $config);
                
                $this->upload->initialize($config);
                // Upload file to server
                if ($this->upload->do_upload('file')) {
                    // Uploaded file data
                    $fileData = $this->upload->data();
                    $fileData['file_name'];
                    $uploadData[$i] = 'assets/functions/function' . $function_id . '/gallery/' . $fileData['file_name'];
                } //$this->upload->do_upload('file')
                else {
                    $a = $this->upload->display_errors();
                    $this->api->api_message(0, $a);
                    exit();
                }
            } //$i = 0; $i < $filesCount; $i++
            foreach ($uploadData as $uploadData) {
                if (isset($description)) {
                    $data['description'] = $description;
                } //isset($description)
                if (isset($title)) {
                    $data['title'] = $title;
                } //isset($title)
                $data['function_id'] = $function_id;
                $data['media']       = $uploadData;
                $getGuests           = $this->Api_model->getAllDataWhere(array(
                    'function_id' => $function_id
                ), GST_LIST_TBL);
                foreach ($getGuests as $getGuests) {
                    $title = "Function Galley";
                    $type  = "10017";
                    $msg   = "New function gallery uploaded on " . $getFuction->title;
                    $this->firebase_notification($getGuests->user_id, $msg, $title, $type, $function_id);
                } //$getGuests as $getGuests
                $listID = $this->Api_model->insertGetId(FCN_GLY_TBL, $data);
            } //$uploadData as $uploadData
            $this->api->api_message(1, "Welcome uploaded successfully.");
            exit();
        } //isset($_FILES['files'])
        else {
            $this->api->api_message(0, "Please select any image.");
            exit();
        }
    }
    
    /*Get Welcome Galley by Function*/
    public function getWelcomGallery()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $user_id     = $this->input->post('user_id', TRUE);
        $image       = $this->input->post('image', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        $this->checkUserStatus($user_id);
        
        $getFuction = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id
        ));
        if (!$getFuction) {
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFuction
        
        $getGallery = $this->Api_model->getAllDataWhere(array(
            'function_id' => $function_id
        ), FCN_GLY_TBL);
        if ($getGallery) {
            $getGalleries = array();
            foreach ($getGallery as $getGallery) {
                $getGallery->media = base_url() . $getGallery->media;
                
                array_push($getGalleries, $getGallery);
            } //$getGallery as $getGallery
            
            $this->api->api_message_data(1, GET_WELCOME_GALLERY, 'getWelcomeGallery', $getGalleries);
            exit();
        } //$getGallery
        else {
            $this->api->api_message(0, NO_GALLERY);
            exit();
        }
    }
    
    
    
    /*Get Welcome Galley by Function*/
    public function functionGallery()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $user_id     = $this->input->post('user_id', TRUE);
        $image       = $this->input->post('image', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        $this->checkUserStatus($user_id);
        
        $getFuction = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id
        ));
        if (!$getFuction) {
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFuction
        
        $getGallery = $this->Api_model->getAllDataWhere(array(
            'function_id' => $function_id
        ), FCN_GLY_TBL);
        if ($getGallery) {
            $getGalleries = array();
            foreach ($getGallery as $getGallery) {
                $getGallery->media = base_url() . $getGallery->media;
                
                array_push($getGalleries, $getGallery);
            } //$getGallery as $getGallery
            
            $this->api->api_message_data(1, GET_WELCOME_GALLERY, 'getWelcomeGallery', $getGalleries);
            exit();
        } //$getGallery
        else {
            $this->api->api_message(0, NO_GALLERY);
            exit();
        }
    }
    
    
    /*Get Welcome Galley by Function*/
    public function getWelcomeData()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $user_id     = $this->input->post('user_id', TRUE);
        $image       = $this->input->post('image', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        $this->checkUserStatus($user_id);
        
        $getFuction = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id
        ));
        if (!$getFuction) {
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFuction
        
        $getGallery = $this->Api_model->getAllDataWhere(array(
            'function_id' => $function_id
        ), FCN_GLY_TBL);
        if ($getGallery) {
            $getGalleries = array();
            foreach ($getGallery as $getGallery) {
                $getGallery->media = base_url() . $getGallery->media;
                
                array_push($getGalleries, $getGallery);
            } //$getGallery as $getGallery
        } //$getGallery
        else {
            $getGalleries = array();
        }
        
        $getVideo = $this->Api_model->getSingleRow(WLM_VDO_TBL, array(
            'function_id' => $function_id
        ));
        if ($getVideo) {
            
            $getVideo->media = base_url() . $getVideo->media;
        } //$getVideo
        else {
            $getVideo = '{}';
        }
        $data['videos'] = $getVideo;
        $data['images'] = $getGalleries;
        
        if ($getVideo == '{}' && $getGalleries == NULL) {
            $this->api->api_message(0, NO_GALLERY);
            exit();
        } //$getVideo == '{}' && $getGalleries == NULL
        
        else {
            $this->api->api_message_data(1, GET_WELCOME_GALLERY, 'getWelcomeGallery', $data);
            exit();
        }
    }
    
    /*Remove Welcome Gallery Images*/
    public function removeWelcomeGalleryImage()
    {
        $this->form_validation->set_rules('media_id', 'media_id', 'required');
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $media_id    = $this->input->post('media_id', TRUE);
        $user_id     = $this->input->post('user_id', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        $this->checkUserStatus($user_id);
        
        $getFuction = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id
        ));
        if (!$getFuction) {
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFuction
        
        $getmedia = $this->Api_model->getSingleRow(FCN_GLY_TBL, array(
            'id' => $media_id
        ));
        if ($getmedia) {
            $this->Api_model->deleteRecord(array(
                'id' => $media_id
            ), FCN_GLY_TBL);
            $this->api->api_message(1, IMG_DLT);
            exit();
        } //$getmedia
        else {
            $this->api->api_message(0, NO_GALLERY);
            exit();
        }
    }
    
    /*Add Welcome Video*/
    public function addWelcomeVideo()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $user_id     = $this->input->post('user_id', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        $this->checkUserStatus($user_id);
        
        $getFuction = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id
        ));
        if (!$getFuction) {
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFuction
        
        $getVideo = $this->Api_model->getSingleRow(WLM_VDO_TBL, array(
            'function_id' => $function_id,
            'user_id' => $user_id
        ));
        if ($getVideo) {
            $this->api->api_message(0, "You alread add video for this function.");
            exit();
        } //$getVideo
        
        if (!is_dir('assets/functions/function' . $function_id . '/welcomeVideo')) {
            mkdir('./assets/functions/function' . $function_id . '/welcomeVideo', 0777, TRUE);
        } //!is_dir('assets/functions/function' . $function_id . '/welcomeVideo')
        
        $this->load->library('upload');
        $config['image_library']  = 'gd2';
        $config['upload_path']    = './assets/functions/function' . $function_id . '/welcomeVideo/';
        $config['allowed_types']  = 'gif|jpg|jpeg|png|mp4|3gp';
        $config['max_size']       = 10000000;
        $config['file_name']      = time();
        $config['create_thumb']   = TRUE;
        $config['maintain_ratio'] = TRUE;
        $config['width']          = 250;
        $config['height']         = 250;
        $this->upload->initialize($config);
        $functionImage = "";
        if ($this->upload->do_upload('media') && $this->load->library('image_lib', $config)) {
            $functionImage = 'assets/functions/function' . $function_id . '/welcomeVideo/' . $this->upload->data('file_name');
        } //$this->upload->do_upload('media') && $this->load->library('image_lib', $config)
        else {
            // echo $this->upload->display_errors();
        }
        
        if ($functionImage) {
            $data['media']       = $functionImage;
            $data['function_id'] = $function_id;
            $data['user_id']     = $user_id;
            
            $getId = $this->Api_model->insertGetId(WLM_VDO_TBL, $data);
            if ($getId) {
                $getGuests = $this->Api_model->getAllDataWhere(array(
                    'function_id' => $function_id
                ), GST_LIST_TBL);
                foreach ($getGuests as $getGuests) {
                    $title = "Invitaion Card";
                    $type  = "10016";
                    $msg   = "New welcome video uploaded on " . $getFuction->title;
                    $this->firebase_notification($getGuests->user_id, $msg, $title, $type, $function_id);
                } //$getGuests as $getGuests
                
                $getVideo = $this->Api_model->getSingleRow(WLM_VDO_TBL, array(
                    'id' => $getId
                ));
                
                $getVideo->media = base_url() . $getVideo->media;
                $this->api->api_message_data(1, "Video uploaded successfully.", 'getWelcomeVideo', $getVideo);
                exit();
            } //$getId
            else {
                $this->api->api_message(0, TRY_AGAIN);
                exit();
            }
        } //$functionImage
        else {
            $this->api->api_message(0, TRY_AGAIN);
            exit();
        }
    }
    
    /*Get My Welcome Video*/
    public function getMyWelcomeVideo()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $user_id     = $this->input->post('user_id', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        $this->checkUserStatus($user_id);
        
        $getFuction = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id
        ));
        if (!$getFuction) {
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFuction
        
        $getVideo = $this->Api_model->getSingleRow(WLM_VDO_TBL, array(
            'function_id' => $function_id,
            'user_id' => $user_id
        ));
        if (!$getVideo) {
            $this->api->api_message(0, NO_DATA);
            exit();
        } //!$getVideo
        
        $getVideo->media = base_url() . $getVideo->media;
        $this->api->api_message_data(1, GET_WELCOME_VIDEO, 'getWelcomeVideo', $getVideo);
        exit();
    }
    
    /*Remove Welcome Gallery Images*/
    public function removeWelcomeVideo()
    {
        $this->form_validation->set_rules('media_id', 'media_id', 'required');
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $media_id    = $this->input->post('media_id', TRUE);
        $user_id     = $this->input->post('user_id', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        $this->checkUserStatus($user_id);
        
        $getFuction = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id
        ));
        if (!$getFuction) {
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFuction
        
        $getmedia = $this->Api_model->getSingleRow(WLM_VDO_TBL, array(
            'id' => $media_id
        ));
        if ($getmedia) {
            $this->Api_model->deleteRecord(array(
                'id' => $media_id
            ), WLM_VDO_TBL);
            $this->api->api_message(1, VDO_DLT);
            exit();
        } //$getmedia
        else {
            $this->api->api_message(0, NO_VIDEO);
            exit();
        }
    }
    /*******************************End Function Gallery Related API's***************************/
    
    
    /*******************************End Post Related API's***************************/
    
    /*Add Post*/
    public function addOthersPost()
    {
        $this->form_validation->set_rules('content', 'content', 'required');
        $this->form_validation->set_rules('title', 'title', 'required');
        $this->form_validation->set_rules('post_type', 'post_type', 'required');
        // $this->form_validation->set_rules('event_id', 'event_id', 'required');
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $content     = $this->input->post('content', TRUE);
        $media       = $this->input->post('media', TRUE);
        $title       = $this->input->post('title', TRUE);
        $post_type   = $this->input->post('post_type', TRUE);
        $event_id    = $this->input->post('event_id', TRUE);
        $user_id     = $this->input->post('user_id', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        // $status ='0';
        
        if ($event_id) {
            $event_id = $event_id;
        } else {
            $event_id = "";
        }
        $this->checkUserStatus($user_id);
        
        $getEvent = $this->Api_model->getSingleRow(EVT_TBL, array(
            'id' => $event_id
        ));
        if (!$getEvent) {
            //$this->api->api_message(0, EVT_NT_FND);
            // exit();
        } //!$getEvent
        
        $getFuction = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id
        ));
        if (!$getFuction) {
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFuction
        
        
        $data['event_id']    = $event_id;
        $data['user_id']     = $user_id;
        $data['title']       = $title;
        $data['post_type']   = $post_type;
        $data['function_id'] = $function_id;
        $data['content']     = $content;
        $data['status']      = "0";
        $getId               = $this->Api_model->insertGetId(PST_TBL, $data);
        if ($getId) {
            if (isset($_FILES['media'])) {
                $filesCount = count($_FILES['media']['name']);
                for ($i = 0; $i < $filesCount; $i++) {
                    $_FILES['file']['name']     = $_FILES['media']['name'][$i];
                    $_FILES['file']['type']     = $_FILES['media']['type'][$i];
                    $_FILES['file']['tmp_name'] = $_FILES['media']['tmp_name'][$i];
                    $_FILES['file']['error']    = $_FILES['media']['error'][$i];
                    $_FILES['file']['size']     = $_FILES['media']['size'][$i];
                    // File upload configuration                       
                    $uploadPath                 = 'assets/images/post/';
                    $config['upload_path']      = $uploadPath;
                    $config['allowed_types']    = 'gif|jpg|jpeg|png|webm|mkv|flv|mp4|3gp|mpeg';
                    $config['encrypt_name']     = TRUE;
                    // Load and initialize upload library
                    $this->load->library('upload', $config);
                    $this->upload->initialize($config);
                    if ($this->upload->do_upload('file')) {
                        // Uploaded file data
                        $fileData = $this->upload->data();
                        $fileData['file_name'];
                        $uploadData[$i] = 'assets/images/post/' . $fileData['file_name'];
                    } //$this->upload->do_upload('file')
                } //$i = 0; $i < $filesCount; $i++
                
                foreach ($uploadData as $uploadData) {
                    $datas['post_id']    = $getId;
                    $datas['media']      = $uploadData;
                    $datas['created_at'] = time();
                    $listID              = $this->Api_model->insertGetId("post_gallery", $datas);
                    
                    
                } //$uploadData as $uploadData
                
            } //isset($_FILES['files'])
            
            $getGuest = $this->Api_model->getAllDataWhere(array(
                'function_id' => $function_id,
                'user_id' => $user_id
            ), 'guest_list');
            
            if ($getGuest) {
                foreach ($getGuest as $getGuest) {
                    $getUser     = $this->Api_model->getSingleRow(USR_TBL, array(
                        'id' => $getGuest->user_id
                    ));
                    $user        = $this->Api_model->getSingleRow(USR_TBL, array(
                        'id' => $user_id
                    ));
                    $deviceToken = $getUser->device_token;
                    if ($deviceToken) {
                        $title_sent = "Event Post";
                        $msg_sent   = $user->name . " posted on wall.";
                        
                        $API_ACCESS_KEY = 'AAAA9Eytjuk:APA91bEwOIgFw9Fwn-Ke0ZxZmFn5Tvh4nWtoKeydqxWqAMe7zTq05kAOE6XP17iEHrMBBPomQ8ruMgas34wULL6uqm_uYyasrgN0ZG5fysMJ91RPazo50JMAtifoYTHN6QxfjZSj-yzr';
                        
                        $msg = array(
                            'body' => $msg_sent,
                            'title' => $title_sent,
                            'type' => '1001',
                            // 'event_id' => $event_id,
                            'function_name' => $getFuction->title,
                            'function_id' => $function_id,
                            'icon' => 'myicon',
                            /*Default Icon*/
                            'sound' => 'mySound'
                            /*Default sound*/
                        );
                        if ($getUser->device_type == "IOS") {
                            $fields = array(
                                'to' => $deviceToken,
                                'notification' => $msg
                            );
                        } //$getUser->device_type == "IOS"
                        else {
                            $fields = array(
                                'to' => $deviceToken,
                                'data' => $msg
                            );
                        }
                        $headers = array(
                            'Authorization: key=' . $API_ACCESS_KEY,
                            'Content-Type: application/json'
                        );
                        #Send Reponse To FireBase Server    
                        $ch      = curl_init();
                        curl_setopt($ch, CURLOPT_URL, 'https://fcm.googleapis.com/fcm/send');
                        curl_setopt($ch, CURLOPT_POST, true);
                        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
                        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
                        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
                        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
                        $result = curl_exec($ch);
                        curl_close($ch);
                    }
                }
            }
            
            $this->api->api_message(1, "Your Post uploaded successfully.");
            exit();
        } //$getId
        else {
            $this->api->api_message(0, TRY_AGAIN);
            exit();
        }
    }
    /*Add Post*/
    public function addMyPost()
    {
        $this->form_validation->set_rules('content', 'content', 'required');
        $this->form_validation->set_rules('title', 'title', 'required');
        $this->form_validation->set_rules('post_type', 'post_type', 'required');
        // $this->form_validation->set_rules('event_id', 'event_id', 'required');
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $content     = $this->input->post('content', TRUE);
        $media       = $this->input->post('media', TRUE);
        $title       = $this->input->post('title', TRUE);
        $post_type   = $this->input->post('post_type', TRUE);
        $event_id    = $this->input->post('event_id', TRUE);
        $user_id     = $this->input->post('user_id', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        
        // $status ='1';
        if ($event_id) {
            $event_id = $event_id;
        } else {
            $event_id = "";
        }
        $this->checkUserStatus($user_id);
        
        $getEvent = $this->Api_model->getSingleRow(EVT_TBL, array(
            'id' => $event_id
        ));
        if (!$getEvent) {
            //$this->api->api_message(0, EVT_NT_FND);
            // exit();
        } //!$getEvent
        
        $getFuction = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id
        ));
        if (!$getFuction) {
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFuction
        
        
        
        // if ($post_type == 1 || $post_type == 2) {
        
        // $config['upload_path']   = 'assets/images/';
        // $config['allowed_types'] = 'gif|jpg|jpeg|png|webm|mkv|flv|mp4|3gp|mpeg';
        // $config['max_size']      = 10000;
        // $config['file_name']     = time();
        // $this->load->library('upload', $config);
        // $this->upload->initialize($config);
        // $thumb_image = "";
        // if ($this->upload->do_upload('thumb_image')) {
        //     $thumb_image         = 'assets/images/' . $this->upload->data('file_name');
        //     $data['thumb_image'] = $thumb_image;
        // } //$this->upload->do_upload('banner_image')
        
        // } //$post_type == 1 || $post_type == 2
        
        $data['event_id']    = $event_id;
        $data['user_id']     = $user_id;
        $data['title']       = $title;
        $data['post_type']   = $post_type;
        $data['function_id'] = $function_id;
        $data['content']     = $content;
        $data['status']      = "1";
        $data['created_at']  = time();
        $getId               = $this->Api_model->insertGetId(PST_TBL, $data);
        if ($getId) {
            if (isset($_FILES['media'])) {
                $filesCount = count($_FILES['media']['name']);
                for ($i = 0; $i < $filesCount; $i++) {
                    $_FILES['file']['name']     = $_FILES['media']['name'][$i];
                    $_FILES['file']['type']     = $_FILES['media']['type'][$i];
                    $_FILES['file']['tmp_name'] = $_FILES['media']['tmp_name'][$i];
                    $_FILES['file']['error']    = $_FILES['media']['error'][$i];
                    $_FILES['file']['size']     = $_FILES['media']['size'][$i];
                    // File upload configuration                       
                    $uploadPath                 = 'assets/images/post/';
                    $config['upload_path']      = $uploadPath;
                    $config['allowed_types']    = 'gif|jpg|jpeg|png|webm|mkv|flv|mp4|3gp|mpeg';
                    $config['encrypt_name']     = TRUE;
                    // Load and initialize upload library
                    $this->load->library('upload', $config);
                    $this->upload->initialize($config);
                    if ($this->upload->do_upload('file')) {
                        // Uploaded file data
                        $fileData = $this->upload->data();
                        $fileData['file_name'];
                        $uploadData[$i] = 'assets/images/post/' . $fileData['file_name'];
                    } //$this->upload->do_upload('file')
                } //$i = 0; $i < $filesCount; $i++
                
                foreach ($uploadData as $uploadData) {
                    $datas['post_id']    = $getId;
                    $datas['media']      = $uploadData;
                    $datas['created_at'] = time();
                    $listID              = $this->Api_model->insertGetId("post_gallery", $datas);
                    
                    
                } //$uploadData as $uploadData
                
            } //isset($_FILES['files'])
            
            $getGuest = $this->Api_model->getAllDataWhere(array(
                'function_id' => $function_id,
                'user_id' => $user_id
            ), 'guest_list');
            
            if ($getGuest) {
                foreach ($getGuest as $getGuest) {
                    $getUser     = $this->Api_model->getSingleRow(USR_TBL, array(
                        'id' => $getGuest->user_id
                    ));
                    $user        = $this->Api_model->getSingleRow(USR_TBL, array(
                        'id' => $user_id
                    ));
                    $deviceToken = $getUser->device_token;
                    if ($deviceToken) {
                        $title_sent = "Event Post";
                        $msg_sent   = $user->name . " posted on wall.";
                        
                        $API_ACCESS_KEY = 'AAAA9Eytjuk:APA91bEwOIgFw9Fwn-Ke0ZxZmFn5Tvh4nWtoKeydqxWqAMe7zTq05kAOE6XP17iEHrMBBPomQ8ruMgas34wULL6uqm_uYyasrgN0ZG5fysMJ91RPazo50JMAtifoYTHN6QxfjZSj-yzr';
                        
                        $msg = array(
                            'body' => $msg_sent,
                            'title' => $title_sent,
                            'type' => '1001',
                            // 'event_id' => $event_id,
                            'function_name' => $getFuction->title,
                            'function_id' => $function_id,
                            'icon' => 'myicon',
                            /*Default Icon*/
                            'sound' => 'mySound'
                            /*Default sound*/
                        );
                        if ($getUser->device_type == "IOS") {
                            $fields = array(
                                'to' => $deviceToken,
                                'notification' => $msg
                            );
                        } //$getUser->device_type == "IOS"
                        else {
                            $fields = array(
                                'to' => $deviceToken,
                                'data' => $msg
                            );
                        }
                        $headers = array(
                            'Authorization: key=' . $API_ACCESS_KEY,
                            'Content-Type: application/json'
                        );
                        #Send Reponse To FireBase Server    
                        $ch      = curl_init();
                        curl_setopt($ch, CURLOPT_URL, 'https://fcm.googleapis.com/fcm/send');
                        curl_setopt($ch, CURLOPT_POST, true);
                        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
                        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
                        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
                        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
                        $result = curl_exec($ch);
                        curl_close($ch);
                    }
                }
            }
            
            $this->api->api_message(1, "Your Post uploaded successfully.");
            exit();
        } //$getId
        else {
            $this->api->api_message(0, TRY_AGAIN);
            exit();
        }
    }
    
    /*Edit Post*/
    public function editPost()
    {
        $this->form_validation->set_rules('post_id', 'post_id', 'required');
        $this->form_validation->set_rules('content', 'content', 'required');
        $this->form_validation->set_rules('title', 'title', 'required');
        $this->form_validation->set_rules('post_type', 'post_type', 'required');
        // $this->form_validation->set_rules('event_id', 'event_id', 'required');
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $post_id     = $this->input->post('post_id', TRUE);
        $content     = $this->input->post('content', TRUE);
        $media       = $this->input->post('media', TRUE);
        $title       = $this->input->post('title', TRUE);
        $post_type   = $this->input->post('post_type', TRUE);
        // $event_id    = $this->input->post('event_id', TRUE);
        $user_id     = $this->input->post('user_id', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        $this->checkUserStatus($user_id);
        
        $getFuction = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id
        ));
        if (!$getFuction) {
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFuction
        
        // $getEvent = $this->Api_model->getSingleRow(EVT_TBL, array(
        //     'id' => $event_id
        // ));
        // if (!$getEvent) {
        //     $this->api->api_message(0, EVT_NT_FND);
        //     exit();
        // } //!$getEvent
        
        $getPost = $this->Api_model->getSingleRow(PST_TBL, array(
            'id' => $post_id
        ));
        if (!$getPost) {
            $this->api->api_message(0, PST_NT_FND);
            exit();
        } //!$getPost
        
        if (!is_dir('assets/functions/function' . $function_id . '/posts')) {
            mkdir('./assets/functions/function' . $function_id . '/posts', 0777, TRUE);
            mkdir('./assets/functions/function' . $function_id . '/posts/thumbnails', 0777, TRUE);
        } //!is_dir('assets/functions/function' . $function_id . '/posts')
        
        $upload_path             = './assets/functions/function' . $function_id . '/posts/';
        $config['upload_path']   = $upload_path;
        $config['allowed_types'] = 'gif|jpg|jpeg|png|webm|mkv|flv|mp4|3gp|mpeg';
        $config['max_size']      = 12288000;
        $config['filename']      = time();
        $config['create_thumb']  = TRUE;
        $config['encrypt_name']  = true;
        
        $this->load->library('upload', $config);
        
        $this->upload->initialize($config);
        
        $ProductFile = '';
        if ($this->upload->do_upload('media')) {
            $f_name      = $this->upload->data('file_name');
            $ProductFile = $upload_path . $f_name;
            $filename    = explode('.', $f_name);
            $thumb_name  = $upload_path . 'thumbnails/' . $filename[0] . '.jpg';
            shell_exec('ffmpeg -itsoffset -1 -i ' . $ProductFile . ' -vcodec mjpeg -vframes 1 -an -f rawvideo -s 200x200 ' . $thumb_name);
            
            if ($post_type == 1 || $post_type == 2) {
                if ($ProductFile) {
                    $data['media'] = $ProductFile;
                    if ($post_type == 2) {
                        $data['thumb_image'] = $thumb_name;
                    } //$post_type == 2
                } //$ProductFile
            } //$post_type == 1 || $post_type == 2
        } //$this->upload->do_upload('media')function_id
        $data['function_id'] = isset($function_id) ? $function_id : $getPost->function_id;
        $data['title']       = isset($title) ? $title : $getPost->title;
        $data['post_type']   = isset($post_type) ? $post_type : $getPost->post_type;
        $data['content']     = isset($content) ? $content : $getPost->content;
        
        $this->Api_model->updateSingleRow(PST_TBL, array(
            'id' => $post_id
        ), $data);
        $this->api->api_message(1, PST_UPDATED);
        
    }
    
    /*Remove Post*/
    public function removePost()
    {
        $this->form_validation->set_rules('post_id', 'post_id', 'required');
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $post_id     = $this->input->post('post_id', TRUE);
        $user_id     = $this->input->post('user_id', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        $this->checkUserStatus($user_id);
        
        $getFuction = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id
        ));
        if (!$getFuction) {
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFuction
        
        $getmedia = $this->Api_model->getSingleRow(PST_TBL, array(
            'id' => $post_id
        ));
        if ($getmedia) {
            $this->Api_model->deleteRecord(array(
                'id' => $post_id
            ), PST_TBL);
            $this->api->api_message(1, PST_DLT);
            exit();
        } //$getmedia
        else {
            $this->api->api_message(0, PST_NT_FND);
            exit();
        }
    }
    
    
    /*get All posts*/
    public function getPostByEvent()
    {
        $this->form_validation->set_rules('page', 'page', 'required');
        $this->form_validation->set_rules('event_id', 'event_id', 'required');
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $page        = $this->input->post('page', TRUE);
        $event_id    = $this->input->post('event_id', TRUE);
        $post_id     = $this->input->post('post_id', TRUE);
        $user_id     = $this->input->post('user_id', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        $this->checkUserStatus($user_id);
        
        $getFuction = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id
        ));
        if (!$getFuction) {
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFuction
        
        $getEvent = $this->Api_model->getSingleRow(EVT_TBL, array(
            'id' => $event_id
        ));
        if (!$getEvent) {
            $this->api->api_message(0, EVT_NT_FND);
            exit();
        } //!$getEvent
        
        $page    = isset($page) ? $page : 1;
        $getPost = $this->Api_model->getAllDataWhereLimit(PST_TBL, array(
            'event_id' => $event_id
        ), $page);
        if ($getPost) {
            $getPosts = array();
            foreach ($getPost as $getPost) {
                $getUser              = $this->Api_model->getSingleRow(USR_TBL, array(
                    'id' => $getPost->user_id
                ));
                $getPost->user_name   = $getUser->name;
                $getPost->user_mobile = $getUser->mobile;
                $getPost->user_image  = base_url() . $getUser->image;
                if ($getPost->post_type == 1 || $getPost->post_type == 2) {
                    $getPost->media       = base_url() . $getPost->media;
                    $getPost->thumb_image = base_url() . $getPost->thumb_image;
                } //$getPost->post_type == 1 || $getPost->post_type == 2
                
                $checkLike = $this->Api_model->getSingleRow(LKS_TBL, array(
                    'post_id' => $getPost->id,
                    'user_id' => $user_id
                ));
                if ($checkLike) {
                    $getPost->is_like = 1;
                } //$checkLike
                else {
                    $getPost->is_like = 0;
                }
                
                $getPost->comments = $this->Api_model->getCountWhere(CMT_TBL, array(
                    'post_id' => $getPost->id
                ));
                $getPost->likes    = $this->Api_model->getCountWhere(LKS_TBL, array(
                    'post_id' => $getPost->id
                ));
                array_push($getPosts, $getPost);
            } //$getPost as $getPost
            
            $this->api->api_message_data(1, ALL_POSTS, 'getPosts', $getPosts);
            exit();
        } //$getPost
        else {
            $this->api->api_message(0, PST_NT_FND);
            exit();
        }
    }
    
    /*Like a Post*/
    // public function like()
    // {
    //     $this->form_validation->set_rules('user_id', 'user_id', 'required');
    //     $this->form_validation->set_rules('post_id', 'post_id', 'required');
    //     $this->form_validation->set_rules('function_id', 'function_id', 'required');
    //     if ($this->form_validation->run() == false) {
    //         $this->api->api_message(0, ALL_FIELD_MANDATORY);
    //         exit();
    //     } //$this->form_validation->run() == false
    
    //     $post_id     = $this->input->post('post_id', TRUE);
    //     $user_id     = $this->input->post('user_id', TRUE);
    //     $function_id = $this->input->post('function_id', TRUE);
    //     $this->checkUserStatus($user_id);
    
    //     $check = $this->Api_model->getSingleRow('likes', array(
    //         'function_id' => $function_id,
    //         'user_id' =>$user_id,
    //         'post_id'=>$post_id
    //     ));
    //     if ($check) {
    
    //        $listID =  $this->Api_model->updateSingleRow('likes', array(
    //         'function_id' => $function_id,
    //         'user_id' => $user_id,
    //          'post_id'=>$post_id
    //          ), array(
    //         'status' => 0,
    
    //     ));
    //        if ($listID) {
    
    //          $this->api->api_message(1,'Unlike a Post');
    //          exit();
    //     }
    // }
    //     $getFuction = $this->Api_model->getSingleRow(FCN_TBL, array(
    //         'id' => $function_id
    //     ));
    //     if (!$getFuction) {
    //         $this->api->api_message(0, FNC_NT_FND);
    //         exit();
    //     } //!$getFuction
    
    //     $getPost = $this->Api_model->getSingleRow(PST_TBL, array(
    //         'id' => $post_id
    //     ));
    //     if (!$getPost) {
    //         $this->api->api_message(0, PST_NT_FND);
    //         exit();
    //     } //!$getPost
    
    //     $data['user_id']     = $user_id;
    //     $data['post_id']     = $post_id;
    //     $data['function_id'] = $function_id;
    //     $data['status']      =  "1";
    //     $getId               = $this->Api_model->insertGetId(LKS_TBL, $data);
    //     if ($getId) {
    
    //         $getUser = $this->Api_model->getSingleRow(USR_TBL, array(
    //             'id' => $user_id
    //         ));
    
    //         $title = "Like Post";
    //         $type  = "10013";
    //         $msg   = $getUser->name . " like your post";
    //         $this->firebase_notification($getPost->user_id, $msg, $title, $type, $function_id);
    
    //         $this->api->api_message(1, "Like a Post");
    //         exit();
    //     } //$getId
    //     else {
    //         $this->api->api_message(0, TRY_AGAIN);
    //         exit();
    //     }
    // }
    
    public function likePost()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('post_id', 'post_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $post_id     = $this->input->post('post_id', TRUE);
        $user_id     = $this->input->post('user_id', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        $this->checkUserStatus($user_id);
        
        $check = $this->Api_model->getSingleRow('likes', array(
            'function_id' => $function_id,
            'user_id' => $user_id,
            'post_id' => $post_id
        ));
        if ($check) {
            if ($check->status == 1) {
                $listID = $this->Api_model->updateSingleRow('likes', array(
                    'function_id' => $function_id,
                    'user_id' => $user_id,
                    'post_id' => $post_id
                ), array(
                    'status' => 0
                    
                ));
                if ($listID) {
                    
                    $this->api->api_message(1, 'Unlike a Post');
                    exit();
                }
            } else {
                $listID = $this->Api_model->updateSingleRow('likes', array(
                    'function_id' => $function_id,
                    'user_id' => $user_id,
                    'post_id' => $post_id
                ), array(
                    'status' => 1
                    
                ));
                if ($listID) {
                    
                    $this->api->api_message(1, 'like a Post');
                    exit();
                }
            }
            
        }
        $getFuction = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id
        ));
        if (!$getFuction) {
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFuction
        
        $getPost = $this->Api_model->getSingleRow(PST_TBL, array(
            'id' => $post_id
        ));
        if (!$getPost) {
            $this->api->api_message(0, PST_NT_FND);
            exit();
        } //!$getPost
        
        $data['user_id']     = $user_id;
        $data['post_id']     = $post_id;
        $data['function_id'] = $function_id;
        $data['status']      = "1";
        $getId               = $this->Api_model->insertGetId(LKS_TBL, $data);
        if ($getId) {
            
            $getUser = $this->Api_model->getSingleRow(USR_TBL, array(
                'id' => $user_id
            ));
            
            $title = "Like Post";
            $type  = "10013";
            $msg   = $getUser->name . " like your post";
            $this->firebase_notification($getPost->user_id, $msg, $title, $type, $function_id);
            
            $this->api->api_message(1, "Like a Post");
            exit();
        } //$getId
        else {
            $this->api->api_message(0, TRY_AGAIN);
            exit();
        }
    }
    /*Get Guest according to function*/
    public function getGuestByPostLiked()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('post_id', 'post_id', 'required');
        
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $user_id = $this->input->post('user_id', TRUE);
        $post_id = $this->input->post('post_id', TRUE);
        $this->checkUserStatus($user_id);
        $getGuest  = $this->Api_model->getAllDataWhere(array(
            'post_id' => $post_id
        ), 'likes');
        $getGuests = array();
        if ($getGuest) {
            foreach ($getGuest as $getGuest) {
                $getUser = $this->Api_model->getSingleRow(USR_TBL, array(
                    'id' => $getGuest->user_id
                ));
                if ($getUser) {
                    if ($getUser->image) {
                        $getUser->image = base_url() . $getUser->image;
                    } //$getUser->image
                    else {
                        $getUser->image = base_url() . "assets/images/image.png";
                    }
                } //$getUser
                array_push($getGuests, $getUser);
            } //$getGuest as $getGuest
            $this->api->api_message_data(1, ALL_GUEST, 'guest', $getGuests);
        } //$getGuest
        else {
            $this->api->api_message(0, NO_DATA);
        }
    }
    
    //Add comments
    public function addComment()
    {
        $this->form_validation->set_rules('content', 'content', 'required');
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('post_id', 'post_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $content     = $this->input->post('content', TRUE);
        $post_id     = $this->input->post('post_id', TRUE);
        $user_id     = $this->input->post('user_id', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        $this->checkUserStatus($user_id);
        
        $getFuction = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id
        ));
        if (!$getFuction) {
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFuction
        
        $getPost = $this->Api_model->getSingleRow(PST_TBL, array(
            'id' => $post_id
        ));
        if (!$getPost) {
            $this->api->api_message(0, PST_NT_FND);
            exit();
        } //!$getPost
        
        $data['user_id']     = $user_id;
        $data['post_id']     = $post_id;
        $data['content']     = $content;
        $data['function_id'] = $function_id;
        $getId               = $this->Api_model->insertGetId(CMT_TBL, $data);
        if ($getId) {
            $getUser = $this->Api_model->getSingleRow(USR_TBL, array(
                'id' => $user_id
            ));
            $title   = "Comment Post";
            $type    = "10014";
            $msg     = $getUser->name . ": " . $content;
            $this->firebase_notification($getPost->user_id, $msg, $title, $type, $function_id);
            
            $this->api->api_message(1, "Comment added successfully.");
            exit();
        } //$getId
        else {
            $this->api->api_message(0, TRY_AGAIN);
            exit();
        }
    }
    
    public function getComments()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('post_id', 'post_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $page        = $this->input->post('page', TRUE);
        $post_id     = $this->input->post('post_id', TRUE);
        $user_id     = $this->input->post('user_id', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        $this->checkUserStatus($user_id);
        
        $getFuction = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id
        ));
        if (!$getFuction) {
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFuction
        
        $getPost = $this->Api_model->getSingleRow(PST_TBL, array(
            'id' => $post_id
        ));
        if (!$getPost) {
            $this->api->api_message(0, PST_NT_FND);
            exit();
        } //!$getPost
        
        $page        = isset($page) ? $page : 1;
        $getComments = $this->Api_model->getAllDataWhereLimit(CMT_TBL, array(
            'post_id' => $post_id
        ), $page);
        if ($getComments) {
            $getComment = array();
            foreach ($getComments as $getComments) {
                $getUser = $this->Api_model->getSingleRow(USR_TBL, array(
                    'id' => $getComments->user_id
                ));
                
                $getComments->user_name  = $getUser->name;
                $getComments->user_image = base_url() . $getUser->image;
                array_push($getComment, $getComments);
            } //$getComments as $getComments
            
            $this->api->api_message_data(1, ALL_COOMENTS, 'getComments', $getComment);
            exit();
        } //$getComments
        else {
            $this->api->api_message(0, "No comments found.");
            exit();
        }
    }
    
    //get my Post
    public function getMyPost()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        // $this->form_validation->set_rules('event_id', 'event_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $page        = $this->input->post('page', TRUE);
        $user_id     = $this->input->post('user_id', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        // $event_id = $this->input->post('event_id', TRUE);
        $this->checkUserStatus($user_id);
        $getFuction = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id
        ));
        if (!$getFuction) {
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFuction
        
        // $getEvent = $this->Api_model->getSingleRow(EVT_TBL, array(
        //     'id' => $event_id
        // ));
        // if (!$getEvent) {
        //     $this->api->api_message(0, EVT_NT_FND);
        //     exit();
        // } //!$getEvent
        $page    = isset($page) ? $page : 1;
        $getPost = $this->Api_model->getAllDataWhereLimit(PST_TBL, array(
            // 'event_id' =>$event_id,
            'function_id' => $function_id,
            'user_id' => $user_id,
            'status' => 1
        ), $page);
        if ($getPost) {
            $getPosts = array();
            foreach ($getPost as $getPost) {
                $getUser             = $this->Api_model->getSingleRow(USR_TBL, array(
                    'id' => $getPost->user_id
                ));
                $getPost->user_name  = $getUser->name;
                $getPost->user_image = base_url() . $getUser->image;
                
                
                $getPost->media       = base_url() . $getPost->media;
                $getPost->thumb_image = base_url() . $getPost->thumb_image;
                // }
                // If file upload form submitted
                if ($getPost->post_type == 1 || $getPost->post_type == 2) {
                    $productsImage  = $this->Api_model->getAllDataWhereConcat(array(
                        'post_id' => $getPost->id
                    ), 'post_gallery');
                    $getPost->media = $productsImage;
                    
                    
                } //$getPost->post_type == 1 || $getPost->post_type == 2
                
                $checkLike = $this->Api_model->getSingleRow(LKS_TBL, array(
                    'post_id' => $getPost->id,
                    'user_id' => $user_id,
                    'status' => 1
                ));
                if ($checkLike) {
                    $getPost->is_like = 1;
                } //$checkLike
                else {
                    $getPost->is_like = 0;
                }
                
                $getPost->comments = $this->Api_model->getCountWhere(CMT_TBL, array(
                    'post_id' => $getPost->id
                ));
                $getPost->likes    = $this->Api_model->getCountWhere(LKS_TBL, array(
                    'post_id' => $getPost->id,
                    'status' => 1
                ));
                $getPost->comments = $this->Api_model->getCountWhere(CMT_TBL, array(
                    'post_id' => $getPost->id
                ));
                $getComments       = $this->Api_model->getAllDataWhere(array(
                    'post_id' => $getPost->id
                ), CMT_TBL);
                
                $getComment = array();
                foreach ($getComments as $getComments) {
                    $getUser                 = $this->Api_model->getSingleRow(USR_TBL, array(
                        'id' => $getComments->user_id
                    ));
                    $getComments->user_name  = $getUser->name;
                    $getComments->user_image = base_url() . $getUser->image;
                    array_push($getComment, $getComments);
                } //$getComments as $getComments
                $getPost->getComments = $getComment;
                array_push($getPosts, $getPost);
            } //$getPost as $getPost
            $this->api->api_message_data(1, ALL_POSTS, 'getPosts', array_reverse($getPosts));
            exit();
        } //$getPost
        else {
            $this->api->api_message(0, PST_NT_FND);
            exit();
        }
    }
    
    
    //get my Post
    public function eventWall()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('event_id', 'event_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $page        = $this->input->post('page', TRUE);
        $event_id    = $this->input->post('event_id', TRUE);
        $user_id     = $this->input->post('user_id', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        $this->checkUserStatus($user_id);
        
        $getUser = $this->Api_model->getAllDataWhere(array(
            'function_id' => $function_id,
            'user_id' => $user_id
        ), 'guest_list');
        
        if (!$getUser) {
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getUser
        else {
            $getPastEvents = array();
            foreach ($getUser as $getUser) {
                $getUserDetail       = $this->Api_model->getSingleRow('user', array(
                    'id' => $getUser->user_id
                ));
                $getUser->user_name  = $getUserDetail->name;
                $getUser->user_image = base_url() . $getUserDetail->image;
                
                
                array_push($getPastEvents, $getUser);
            }
        } //$getUser as $getUser
        if ($getUser) {
            array_multisort($getPastEvents, SORT_ASC, $getPastEvents);
            $data['guest_list'] = $getPastEvents;
            
        } else {
            $data['guest_list'] = array();
            
        }
        
        $getFuction = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id
        ));
        if (!$getFuction) {
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFuction
        
        $getEvent        = $this->Api_model->getSingleRow(EVT_TBL, array(
            'id' => $event_id
        ));
        $getEvent->title = $getEvent->title;
        if (!$getEvent) {
            $this->api->api_message(0, EVT_NT_FND);
            exit();
        } //!$getEvent
        
        $page    = isset($page) ? $page : 1;
        $getPost = $this->Api_model->getAllDataWhereLimit(PST_TBL, array(
            'event_id' => $event_id,
            'user_id' => $user_id,
            'function_id' => $function_id,
            'status' => 1
        ), $page);
        if ($getPost) {
            $getPosts = array();
            foreach ($getPost as $getPost) {
                $getUser             = $this->Api_model->getSingleRow(USR_TBL, array(
                    'id' => $getPost->user_id
                ));
                $getPost->user_name  = $getUser->name;
                $getPost->user_image = base_url() . $getUser->image;
                
                // If file upload form submitted
                if ($getPost->post_type == 1 || $getPost->post_type == 2) {
                    $productsImage  = $this->Api_model->getAllDataWhereConcat(array(
                        'post_id' => $getPost->id
                    ), 'post_gallery');
                    $getPost->media = $productsImage;
                } //$getPost->post_type == 1 || $getPost->post_type == 2
                
                $checkLike = $this->Api_model->getSingleRow(LKS_TBL, array(
                    'post_id' => $getPost->id,
                    'user_id' => $user_id
                ));
                if ($checkLike) {
                    $getPost->is_like = 1;
                } //$checkLike
                else {
                    $getPost->is_like = 0;
                }
                $getPost->likes    = $this->Api_model->getCountWhere(LKS_TBL, array(
                    'post_id' => $getPost->id
                ));
                $getPost->comments = $this->Api_model->getCountWhere(CMT_TBL, array(
                    'post_id' => $getPost->id
                ));
                $getComments       = $this->Api_model->getAllDataWhere(array(
                    'post_id' => $getPost->id
                ), CMT_TBL);
                
                $getComment = array();
                foreach ($getComments as $getComments) {
                    $getUser                 = $this->Api_model->getSingleRow(USR_TBL, array(
                        'id' => $getComments->user_id
                    ));
                    $getComments->user_name  = $getUser->name;
                    $getComments->user_image = base_url() . $getUser->image;
                    array_push($getComment, $getComments);
                } //$getComments as $getComments
                $getPost->getComments = $getComment;
                array_push($getPosts, $getPost);
            } //$getPost as $getPost
            
        }
        if ($getPost) {
            array_multisort($getPosts, SORT_ASC, $getPosts);
            $data['All_Post'] = $getPosts;
        } else {
            $data['All_Post'] = array();
        }
        
        if ($data) {
            $this->api->api_message_data(1, ALL_EVENTS, 'dashboard_data', $data);
        } //$data
        else {
            $this->api->api_message(0, NO_DATA);
        }
        
        
    }
    /*******************************End Post Related API's***************************/
    //get my Post
    // public function eventDetails()
    // {
    //     $this->form_validation->set_rules('user_id', 'user_id', 'required');
    //     $this->form_validation->set_rules('event_id', 'event_id', 'required');
    //     $this->form_validation->set_rules('function_id', 'function_id', 'required');
    //     if ($this->form_validation->run() == false) {
    //         $this->api->api_message(0, ALL_FIELD_MANDATORY);
    //         exit();
    //     } //$this->form_validation->run() == false
    
    //     $page        = $this->input->post('page', TRUE);
    //     $event_id    = $this->input->post('event_id', TRUE);
    //     $user_id     = $this->input->post('user_id', TRUE);
    //     $function_id = $this->input->post('function_id', TRUE);
    //     $this->checkUserStatus($user_id);
    
    //     $getUser = $this->Api_model->getSingleRow('events_list',array(
    //         'id' => $event_id,
    //         'function_id' => $function_id,
    //         'user_id' => $user_id
    //     ));
    //     if (!$getUser) {
    //         $this->api->api_message(0, "Event not found");
    //         exit();
    //     } //!$getUser
    //     else {
    //           $getUser->image   = base_url() . $getUser->image;
    //           $getUser->banner_image   = base_url() . $getUser->banner_image; 
    
    //           $getAllUser = $this->Api_model->getAllDataWhere(array(
    //           'id' => $user_id
    //          ), 'user');
    //           $getUser->user_image   = base_url() . $getAllUser[0]->image;
    
    //           $getUser->user_name   = base_url() . $getAllUser[0]->name;
    
    //           $getGallery = $this->Api_model->getAllDataWhere(array(
    //           'event_id' => $event_id
    //        ), 'event_gallery');
    
    //     if ($getGallery) {
    //         $getServicess = array();
    //         foreach ($getGallery as $getGallery) {
    //             $getGallery->image = base_url() . $getGallery->image;
    //             array_push($getServicess, $getGallery);
    //         } //$getGallery as $getGallery
    //     } //$getGallery
    //     if ($getGallery) {
    //         array_multisort($getServicess, SORT_ASC, $getServicess);
    //         $getUser->Gallery= $getServicess;
    
    //     } //$getGallerygetUser
    //     else {
    //         $getUser->Gallery = array();
    
    //     }
    
    //           $this->api->api_message_data(1, ALL_EVENTS, 'Event_Details', $getUser);
    //     }
    
    // }
    
    public function eventDetails()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('event_id', 'event_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $page        = $this->input->post('page', TRUE);
        $event_id    = $this->input->post('event_id', TRUE);
        $user_id     = $this->input->post('user_id', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        $this->checkUserStatus($user_id);
        
        $getUser = $this->Api_model->getSingleRow('events_list', array(
            'id' => $event_id,
            'function_id' => $function_id,
            'user_id' => $user_id
        ));
        if (!$getUser) {
            $this->api->api_message(0, "Event not found");
            exit();
        } //!$getUser
        else {
            // $getUser->image   = base_url() . $getUser->image;
            $Image          = $this->Api_model->getAllDataWhereConcat1(array(
                'event_id' => $getUser->id
            ), 'event_gallery');
            $getUser->media = $Image;
            // $getUser->banner_image   = base_url() . $getUser->banner_image; 
            
            $getAllUser          = $this->Api_model->getAllDataWhere(array(
                'id' => $user_id
            ), 'user');
            $getUser->user_image = base_url() . $getAllUser[0]->image;
            
            $getUser->user_name = $getAllUser[0]->name;
            
            $getGallery = $this->Api_model->getAllDataWhere(array(
                'event_id' => $event_id
            ), 'event_gallery');
            
            
            
            if ($getGallery) {
                $getServicess = array();
                foreach ($getGallery as $getGallery) {
                    $getGallery->media = base_url() . $getGallery->image;
                    array_push($getServicess, $getGallery);
                } //$getGallery as $getGallery
            } //$getGallery
            if ($getGallery) {
                array_multisort($getServicess, SORT_ASC, $getServicess);
                $getUser->Gallery = $getServicess;
                
            } //$getGallerygetUser
            else {
                $getUser->Gallery = array();
                
            }
            
            $this->api->api_message_data(1, ALL_EVENTS, 'Event_Details', $getUser);
        }
        
    }
    /*get Services List*/
    public function getServices()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $user_id     = $this->input->post('user_id', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        $getFuction  = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id
        ));
        if (!$getFuction) {
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFuction
        
        $getServices = $this->Api_model->getAllDataWhere(array(
            'status' => 1
        ), SRV_TBL);
        if ($getServices) {
            $getService = array();
            foreach ($getServices as $getServices) {
                $getServices->image = base_url() . $getServices->image;
                array_push($getService, $getServices);
            } //$getServices as $getServices
            
            $this->api->api_message_data(1, ALL_SERVICES, 'getServices', $getService);
            exit();
        } //$getServices
        else {
            $this->api->api_message(0, NO_DATA);
            exit();
        }
    }
    
    /*get Services List*/
    public function getAllServices()
    {
        $getServices = $this->Api_model->getAllDataWhere(array(
            'status' => 1
        ), SRV_TBL);
        if ($getServices) {
            $getService = array();
            foreach ($getServices as $getServices) {
                $getServices->image = base_url() . $getServices->image;
                array_push($getService, $getServices);
            } //$getServices as $getServices
            
            $this->api->api_message_data(1, ALL_SERVICES, 'getServices', $getService);
            exit();
        } //$getServices
        else {
            $this->api->api_message(0, NO_DATA);
            exit();
        }
    }
    
    /*get Services List*/
    public function getAllPostByUser()
    {
        $user_id     = $this->input->post('user_id', TRUE);
        $getServices = $this->Api_model->getAllDataWhere(array(
            'user_id' => $user_id,
            'status' => 1
            
        ), 'post');
        if ($getServices) {
            $getService = array();
            foreach ($getServices as $getServices) {
                $getServicename             = $this->Api_model->getSingleRow('function', array(
                    'id' => $getServices->function_id
                ));
                $getUsername                = $this->Api_model->getSingleRow('user', array(
                    'id' => $user_id
                ));
                $getServices->Function_name = $getServicename->title;
                $getServices->user_name     = $getUsername->name;
                $getServices->user_image    = base_url() . $getUsername->image;
                $Image                      = $this->Api_model->getAllDataWhereConcat(array(
                    'post_id' => $getServices->id
                ), 'post_gallery');
                $getServices->media         = $Image;
                
                // $check_like = $this->Api_model->getSingleRow(LKS_TBL, array(
                //     'post_id' => $getServices->id,
                //     'function_id'=>$getServices->function_id,
                //     'user_id' => $user_id,
                //     'status' =>1
                
                // ));
                $check_like = $this->Api_model->check_like($getServices->id, $user_id);
                if ($check_like) {
                    $getServices->is_like = '1';
                } else {
                    $getServices->is_like = '0';
                }
                
                $getServices->comments = $this->Api_model->getCountWhere(CMT_TBL, array(
                    'post_id' => $getServices->id
                ));
                $getServices->likes    = $this->Api_model->getCountWhere(LKS_TBL, array(
                    'post_id' => $getServices->id,
                    'status' => 1
                ));
                $getComments           = $this->Api_model->getAllDataWhere(array(
                    'post_id' => $getServices->id
                ), CMT_TBL);
                
                $getComment = array();
                foreach ($getComments as $getComments) {
                    $getUser                 = $this->Api_model->getSingleRow(USR_TBL, array(
                        'id' => $getComments->user_id
                    ));
                    $getComments->user_name  = $getUser->name;
                    $getComments->user_image = base_url() . $getUser->image;
                    array_push($getComment, $getComments);
                } //$getComments as $getComments
                
                $getServices->getComments = $getComment;
                array_push($getService, $getServices);
            } //$getServices as $getServices
            
            $this->api->api_message_data(1, ALL_SERVICES, 'GET_ALL_POST', $getService);
            exit();
        } //$getServices
        else {
            $this->api->api_message(0, NO_DATA);
            exit();
        }
    }
    /*get Services List*/
    public function getAllPostByUser1()
    {
        $user_id     = $this->input->post('user_id', TRUE);
        $getServices = $this->Api_model->getAllDataWhere(array(
            'user_id' => $user_id
        ), 'post');
        if ($getServices) {
            $getService = array();
            foreach ($getServices as $getServices) {
                $getServicename             = $this->Api_model->getSingleRow('function', array(
                    'id' => $getServices->function_id
                ));
                $getUsername                = $this->Api_model->getSingleRow('user', array(
                    'id' => $user_id
                ));
                $getServices->Function_name = $getServicename->title;
                $getServices->user_name     = $getUsername->name;
                $Image                      = $this->Api_model->getAllDataWhereConcat(array(
                    'post_id' => $getServices->id
                ), 'post_gallery');
                $getServices->media         = $Image;
                
                $checkLike = $this->Api_model->getSingleRow(LKS_TBL, array(
                    'post_id' => $getServices->id,
                    'user_id' => $user_id
                ));
                if ($checkLike) {
                    $getServices->is_like = 1;
                } //$checkLike
                else {
                    $getServices->is_like = 0;
                }
                $getServices->comments = $this->Api_model->getCountWhere(CMT_TBL, array(
                    'post_id' => $getServices->id
                ));
                $getServices->likes    = $this->Api_model->getCountWhere(LKS_TBL, array(
                    'post_id' => $getServices->id
                ));
                $getComments           = $this->Api_model->getAllDataWhere(array(
                    'post_id' => $getServices->id
                ), CMT_TBL);
                
                $getComment = array();
                foreach ($getComments as $getComments) {
                    $getUser                 = $this->Api_model->getSingleRow(USR_TBL, array(
                        'id' => $getComments->user_id
                    ));
                    $getComments->user_name  = $getUser->name;
                    $getComments->user_image = base_url() . $getUser->image;
                    array_push($getComment, $getComments);
                } //$getComments as $getComments
                
                $getServices->getComments = $getComment;
                array_push($getService, $getServices);
            } //$getServices as $getServices
            
            $this->api->api_message(1, ALL_SERVICES, 'GET_ALL_POST');
            exit();
        } //$getServices
        else {
            $this->api->api_message(0, NO_DATA);
            exit();
        }
    }
    
    
    /*get service store*/
    public function getServiceStore()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        $this->form_validation->set_rules('service_id', 'service_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $user_id     = $this->input->post('user_id', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        $service_id  = $this->input->post('service_id', TRUE);
        $getFuction  = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id
        ));
        if (!$getFuction) {
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFuction
        
        $getServices = $this->Api_model->getAllDataWhere(array(
            'status' => 1,
            'service_type' => $service_id
        ), SRV_LST_TBL);
        if ($getServices) {
            $this->api->api_message_data(1, ALL_SERVICES, 'getServiceStore', $getServices);
            exit();
        } //$getServices
        else {
            $this->api->api_message(0, NO_DATA);
            exit();
        }
    }
    
    /*Get All Notifications*/
    /*get conversation*/
    public function getNotifications()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $user_id     = $this->input->post('user_id', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        
        $getFuction = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id
        ));
        if (!$getFuction) {
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFuction
        
        $get_notifications = $this->Api_model->getAllDataWhereAndOr(array(
            'user_id' => $user_id,
            'function_id' => $function_id
        ), array(
            'type' => "All",
            'function_id' => $function_id
        ), NTS_TBL);
        if ($get_notifications) {
            $this->api->api_message_data(1, "Get my notifications.", 'my_notifications', $get_notifications);
        } //$get_notifications
        else {
            $this->api->api_message(0, "Not yet any notifications.");
        }
    }
    
    /*Send Regard Message*/
    public function sendRegardsMessage()
    {
        $this->form_validation->set_rules('msg', 'msg', 'required');
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $user_id     = $this->input->post('user_id', TRUE);
        $msg         = $this->input->post('msg', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        
        $getFuction = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id,
            'user_id' => $user_id
        ));
        if (!$getFuction) {
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFuction
        $getUser = $this->Api_model->getSingleRow(USR_TBL, array(
            'id' => $user_id
        ));
        
        if ($getUser) {
            $data['title']       = "Regards message from " . $getUser->name;
            $data['msg']         = $msg;
            $data['function_id'] = $function_id;
            $data['type']        = "All";
            $data['created_at']  = time();
            
            $getId = $this->Api_model->insertGetId(NTS_TBL, $data);
            if ($getId) {
                $this->api->api_message(1, "Message sent successfully.");
                exit();
            } //$getId
            else {
                $this->api->api_message(0, TRY_AGAIN);
                exit();
            }
        } //$getUser
        else {
            $this->api->api_message(0, NO_DATA);
            exit();
        }
    }
    
    /***************************************Tickets***************************************/
    /*Update artist personal info*/
    public function generateTicket()
    {
        $data['user_id']     = $this->input->post('user_id', TRUE);
        $data['function_id'] = $this->input->post('function_id', TRUE);
        $data['reason']      = $this->input->post('reason', TRUE);
        $data['craeted_at']  = time();
        
        $this->checkUserStatus($data['user_id']);
        
        $ticketId = $this->Api_model->insertGetId('ticket', $data);
        if ($ticketId) {
            $this->api->api_message(1, "Ticket generated successfully.");
        } //$ticketId
        else {
            $this->api->api_message(0, NO_DATA);
        }
    }
    
    /*get Ticket*/
    public function getMyTicket()
    {
        $user_id     = $this->input->post('user_id', TRUE);
        $function_id = $this->input->post('function_id', TRUE);
        $this->checkUserStatus($user_id);
        
        $get_ticket = $this->Api_model->getAllDataWhere(array(
            'user_id' => $user_id,
            'function_id' => $function_id
        ), 'ticket');
        if ($get_ticket) {
            $this->api->api_message_data(1, "Get my tickets.", 'my_ticket', $get_ticket);
        } //$get_ticket
        else {
            $this->api->api_message(0, "Not yet any tickets.");
        }
    }
    
    /*Add ticket Comments*/
    public function addTicketComments()
    {
        $user_id = $this->input->post('user_id', TRUE);
        $this->checkUserStatus($user_id);
        
        $data['ticket_id']  = $this->input->post('ticket_id', TRUE);
        $data['comment']    = $this->input->post('comment', TRUE);
        $data['user_id']    = $user_id;
        $data['role']       = 1;
        $data['created_at'] = time();
        
        $ticketId = $this->Api_model->insertGetId('ticket_comments', $data);
        if ($ticketId) {
            $this->api->api_message(1, "Thanks for the review.");
        } //$ticketId
        else {
            $this->api->api_message(0, NO_DATA);
        }
    }
    
    /*get Ticket Comments*/
    public function getTicketComments()
    {
        $user_id   = $this->input->post('user_id', TRUE);
        $ticket_id = $this->input->post('ticket_id', TRUE);
        $this->checkUserStatus($user_id);
        
        $ticket_comments = $this->Api_model->getAllDataWhere(array(
            'ticket_id' => $ticket_id
        ), 'ticket_comments');
        
        $ticket_comment = array();
        foreach ($ticket_comments as $ticket_comments) {
            if ($ticket_comments->user_id != 0) {
                $getUser                   = $this->Api_model->getSingleRow(USR_TBL, array(
                    'id' => $ticket_comments->user_id
                ));
                $ticket_comments->userName = $getUser->name;
            } //$ticket_comments->user_id != 0
            else {
                $ticket_comments->userName = "Admin";
            }
            array_push($ticket_comment, $ticket_comments);
        } //$ticket_comments as $ticket_comments
        
        if ($ticket_comments) {
            $this->api->api_message_data(1, "Get ticket comments.", 'ticket_comment', $ticket_comment);
        } //$ticket_comments
        else {
            $this->api->api_message(0, "Not yet any tickets.");
        }
    }
    
    public function send_event_notification()
    {
        $this->form_validation->set_rules('msg', 'msg', 'required');
        $this->form_validation->set_rules('title', 'title', 'required');
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('function_id', 'function_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        
        $user_id     = $this->input->post('user_id', TRUE);
        $function_id = $this->input->post('function_id');
        $title       = $this->input->post('title');
        $msg         = $this->input->post('msg');
        $getFuction  = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id,
            'user_id' => $user_id
        ));
        if (!$getFuction) {
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFuction
        
        $getGuest = $this->Api_model->getAllDataWhere(array(
            'function_id' => $function_id
        ), GST_LIST_TBL);
        if ($getGuest) {
            foreach ($getGuest as $getGuest) {
                $getUser     = $this->Api_model->getSingleRow(USR_TBL, array(
                    'id' => $getGuest->user_id
                ));
                $deviceToken = $getUser->device_token;
                if ($deviceToken) {
                    $title_sent = $title;
                    $msg_sent   = $msg;
                    
                    $API_ACCESS_KEY = 'AAAA9Eytjuk:APA91bEwOIgFw9Fwn-Ke0ZxZmFn5Tvh4nWtoKeydqxWqAMe7zTq05kAOE6XP17iEHrMBBPomQ8ruMgas34wULL6uqm_uYyasrgN0ZG5fysMJ91RPazo50JMAtifoYTHN6QxfjZSj-yzr';
                    
                    $msg = array(
                        'body' => $msg_sent,
                        'title' => $title,
                        'type' => '1002',
                        'icon' => 'myicon',
                        /*Default Icon*/
                        'sound' => 'mySound'
                        /*Default sound*/
                    );
                    if ($getUser->device_type == "IOS") {
                        $fields = array(
                            'to' => $deviceToken,
                            'notification' => $msg
                        );
                    } //$getUser->device_type == "IOS"
                    else {
                        $fields = array(
                            'to' => $deviceToken,
                            'data' => $msg
                        );
                    }
                    $headers = array(
                        'Authorization: key=' . $API_ACCESS_KEY,
                        'Content-Type: application/json'
                    );
                    #Send Reponse To FireBase Server    
                    $ch      = curl_init();
                    curl_setopt($ch, CURLOPT_URL, 'https://fcm.googleapis.com/fcm/send');
                    curl_setopt($ch, CURLOPT_POST, true);
                    curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
                    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
                    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
                    curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
                    $result = curl_exec($ch);
                    curl_close($ch);
                    
                    $this->api->api_message(1, "Message sent successfully.");
                    exit();
                } //$deviceToken
                else {
                    $this->api->api_message(1, "Message sent successfully.");
                    exit();
                }
            } //$getGuest as $getGuest
        } //$getGuest
        else {
            $this->api->api_message(1, "Message sent successfully.");
            exit();
        }
    }
    
    /***************************************Subscription****************************/
    
    /*Subscription*/
    public function getAllPackages()
    {
        $packages = $this->Api_model->getAllDataWhere(array(
            'status' => 1
        ), 'packages');
        if (empty($packages)) {
            $this->api->api_message(0, NO_DATA);
        } //empty($packages)
        else {
            $package = array();
            foreach ($packages as $packages) {
                if ($packages->subscription_type == 0) {
                    $packages->subscription_name = FREE;
                } //$packages->subscription_type == 0
                elseif ($packages->subscription_type == 1) {
                    $packages->subscription_name = MONTHLY;
                } //$packages->subscription_type == 1
                    elseif ($packages->subscription_type == 2) {
                    $packages->subscription_name = QUARTERLY;
                } //$packages->subscription_type == 2
                    elseif ($packages->subscription_type == 3) {
                    $packages->subscription_name = HALFYEARLY;
                } //$packages->subscription_type == 3
                    elseif ($packages->subscription_type == 4) {
                    $packages->subscription_name = YEARLY;
                } //$packages->subscription_type == 4
                array_push($package, $packages);
            } //$packages as $packages
            $this->api->api_message_data(1, ALL_PACKAGES, 'packages', $package);
        }
    }
    
    public function subscription()
    {
        $this->form_validation->set_rules('order_id', 'order_id', 'required');
        $this->form_validation->set_rules('txn_id', 'txn_id', 'required');
        $this->form_validation->set_rules('package_id', 'package_id', 'required');
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        $user_id = $this->input->post('user_id', TRUE);
        $chkUser = $this->Api_model->getSingleRow('user', array(
            'id' => $user_id
        ));
        if (!$chkUser) {
            $this->api->api_message(0, USER_NOT_FOUND);
            exit();
        } //!$chkUser
        
        $package_id = $this->input->post('package_id', TRUE);
        $txn_id     = $this->input->post('txn_id', TRUE);
        $order_id   = $this->input->post('order_id', TRUE);
        $chkPackage = $this->Api_model->getSingleRow('packages', array(
            'id' => $package_id
        ));
        if (!$chkPackage) {
            $this->api->api_message(0, PKG_NOT);
            exit();
        } //!$chkPackage
        
        $date         = date('Y-m-d');
        $current_date = strtotime($date);
        
        $get_package = $this->Api_model->getSingleRow('packages', array(
            'id' => $package_id
        ));
        if (!$get_package) {
            $this->api->api_message(0, NO_DATA);
            exit();
        } //!$get_package
        $price             = $get_package->price;
        $subscription_type = $get_package->subscription_type;
        
        $data['user_id']                 = $user_id;
        $data['txn_id']                  = $txn_id;
        $data['order_id']                = $order_id;
        $data['subscription_start_date'] = $current_date;
        $data['price']                   = $price;
        $data['subscription_type']       = $subscription_type;
        $dataupdate                      = $this->Api_model->getSingleRow('user_subscription', array(
            'user_id' => $user_id
        ));
        
        if ($dataupdate) {
            $check_end_date = $dataupdate->subscription_end_date;
            
            if ($current_date >= $check_end_date) {
                if ($subscription_type == 1) {
                    $end_date                      = date('Y-m-d', mktime(date('h'), date('i'), date('s'), date('m'), date('d') + 30, date('Y'))) . "\n";
                    $data['subscription_end_date'] = strtotime($end_date);
                } //$subscription_type == 1
                elseif ($subscription_type == 3) {
                    $end_date                      = date('Y-m-d', mktime(date('h'), date('i'), date('s'), date('m'), date('d') + 180, date('Y'))) . "\n";
                    $data['subscription_end_date'] = strtotime($end_date);
                } //$subscription_type == 3
                    elseif ($subscription_type == 2) {
                    $end_date                      = date('Y-m-d', mktime(date('h'), date('i'), date('s'), date('m'), date('d') + 90, date('Y'))) . "\n";
                    $data['subscription_end_date'] = strtotime($end_date);
                } //$subscription_type == 2
                    elseif ($subscription_type == 0) {
                    $end_date                      = date('Y-m-d', mktime(date('h'), date('i'), date('s'), date('m'), date('d') + 30, date('Y'))) . "\n";
                    $data['subscription_end_date'] = strtotime($end_date);
                } //$subscription_type == 0
                    elseif ($subscription_type == 4) {
                    $end_date                      = date('Y-m-d', mktime(date('h'), date('i'), date('s'), date('m'), date('d') + 365, date('Y'))) . "\n";
                    $data['subscription_end_date'] = strtotime($end_date);
                } //$subscription_type == 4
                $this->Api_model->updateSingleRow('user_subscription', array(
                    'user_id' => $user_id
                ), $data);
                $this->Api_model->insertGetId('subscription_history', $data);
                $this->api->api_message(1, SUB_SUCCESS);
            } //$current_date >= $check_end_date
            else {
                if ($subscription_type == 1) {
                    $no_of_days                    = 30;
                    $end_date                      = strtotime('+' . $no_of_days . ' days', $check_end_date);
                    $data['subscription_end_date'] = $end_date;
                } //$subscription_type == 1
                elseif ($subscription_type == 3) {
                    $no_of_days                    = 180;
                    $end_date                      = strtotime('+' . $no_of_days . ' days', $check_end_date);
                    $data['subscription_end_date'] = $end_date;
                } //$subscription_type == 3
                    elseif ($subscription_type == 2) {
                    $no_of_days                    = 90;
                    $end_date                      = strtotime('+' . $no_of_days . ' days', $check_end_date);
                    $data['subscription_end_date'] = $end_date;
                } //$subscription_type == 2
                    elseif ($subscription_type == 0) {
                    $no_of_days                    = 30;
                    $end_date                      = strtotime('+' . $no_of_days . ' days', $check_end_date);
                    $data['subscription_end_date'] = $end_date;
                } //$subscription_type == 0
                    elseif ($subscription_type == 4) {
                    $no_of_days                    = 365;
                    $end_date                      = strtotime('+' . $no_of_days . ' days', $check_end_date);
                    $data['subscription_end_date'] = $end_date;
                } //$subscription_type == 4
                $this->Api_model->updateSingleRow('user_subscription', array(
                    'user_id' => $user_id
                ), $data);
                $this->Api_model->insertGetId('subscription_history', $data);
                $this->api->api_message(1, SUB_SUCCESS);
            }
        } //$dataupdate
        else {
            if ($subscription_type == 1) {
                $end_date                      = date('Y-m-d', mktime(date('h'), date('i'), date('s'), date('m'), date('d') + 30, date('Y'))) . "\n";
                $data['subscription_end_date'] = strtotime($end_date);
            } //$subscription_type == 1
            elseif ($subscription_type == 3) {
                $end_date                      = date('Y-m-d', mktime(date('h'), date('i'), date('s'), date('m'), date('d') + 180, date('Y'))) . "\n";
                $data['subscription_end_date'] = strtotime($end_date);
            } //$subscription_type == 3
                elseif ($subscription_type == 2) {
                $end_date                      = date('Y-m-d', mktime(date('h'), date('i'), date('s'), date('m'), date('d') + 90, date('Y'))) . "\n";
                $data['subscription_end_date'] = strtotime($end_date);
            } //$subscription_type == 2
                elseif ($subscription_type == 0) {
                $end_date                      = date('Y-m-d', mktime(date('h'), date('i'), date('s'), date('m'), date('d') + 30, date('Y'))) . "\n";
                $data['subscription_end_date'] = strtotime($end_date);
            } //$subscription_type == 0
                elseif ($subscription_type == 4) {
                $end_date                      = date('Y-m-d', mktime(date('h'), date('i'), date('s'), date('m'), date('d') + 365, date('Y'))) . "\n";
                $data['subscription_end_date'] = strtotime($end_date);
            } //$subscription_type == 4
            $this->Api_model->insertGetId('user_subscription', $data);
            $this->Api_model->insertGetId('subscription_history', $data);
            
            $this->api->api_message(1, SUB_SUCCESS);
        }
    }
    
    
    public function get_my_subscription_history()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        $user_id = $this->input->post('user_id', TRUE);
        $chkUser = $this->Api_model->getSingleRow('user', array(
            'id' => $user_id
        ));
        if (!$chkUser) {
            $this->api->api_message(0, USER_NOT_FOUND);
            exit();
        } //!$chkUser
        
        $get_my_subscription = $this->Api_model->getAllDataWhere(array(
            'user_id' => $user_id
        ), 'subscription_history');
        
        if ($get_my_subscription) {
            $get_my_subscriptions = array();
            foreach ($get_my_subscription as $get_my_subscription) {
                if ($get_my_subscription->subscription_type == 0) {
                    $get_my_subscription->subscription_name = FREE;
                } //$get_my_subscription->subscription_type == 0
                elseif ($get_my_subscription->subscription_type == 1) {
                    $get_my_subscription->subscription_name = MONTHLY;
                } //$get_my_subscription->subscription_type == 1
                    elseif ($get_my_subscription->subscription_type == 2) {
                    $get_my_subscription->subscription_name = QUARTERLY;
                } //$get_my_subscription->subscription_type == 2
                    elseif ($get_my_subscription->subscription_type == 3) {
                    $get_my_subscription->subscription_name = HALFYEARLY;
                } //$get_my_subscription->subscription_type == 3
                    elseif ($get_my_subscription->subscription_type == 4) {
                    $get_my_subscription->subscription_name = YEARLY;
                } //$get_my_subscription->subscription_type == 4
                
                array_push($get_my_subscriptions, $get_my_subscription);
            } //$get_my_subscription as $get_my_subscription
            $this->api->api_message_data(1, SUB_HISTORY, 'my_subscription_history', $get_my_subscriptions);
        } //$get_my_subscription
        else {
            $this->api->api_message(0, NO_DATA);
        }
    }
    
    public function get_my_subscription()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        } //$this->form_validation->run() == false
        $user_id = $this->input->post('user_id', TRUE);
        $chkUser = $this->Api_model->getSingleRow('user', array(
            'id' => $user_id
        ));
        if (!$chkUser) {
            $this->api->api_message(0, USER_NOT_FOUND);
            exit();
        } //!$chkUser
        $get_my_subscription = $this->Api_model->getSingleRow('user_subscription', array(
            'user_id' => $user_id
        ));
        if ($get_my_subscription) {
            $date                                    = date('Y-m-d');
            $current_date                            = strtotime($date);
            $end_date                                = $get_my_subscription->subscription_end_date;
            $get_title                               = $this->Api_model->getSingleRow('packages', array(
                'subscription_type' => $get_my_subscription->subscription_type
            ));
            $get_my_subscription->subscription_title = $get_title->title;
            $datediff                                = $end_date - time();
            $get_my_subscription->days               = round($datediff / (60 * 60 * 24));
            if ($get_my_subscription->subscription_type == 0) {
                $get_my_subscription->subscription_name = FREE;
            } //$get_my_subscription->subscription_type == 0
            elseif ($get_my_subscription->subscription_type == 1) {
                $get_my_subscription->subscription_name = MONTHLY;
            } //$get_my_subscription->subscription_type == 1
                elseif ($get_my_subscription->subscription_type == 2) {
                $get_my_subscription->subscription_name = QUARTERLY;
            } //$get_my_subscription->subscription_type == 2
                elseif ($get_my_subscription->subscription_type == 3) {
                $get_my_subscription->subscription_name = HALFYEARLY;
            } //$get_my_subscription->subscription_type == 3
                elseif ($get_my_subscription->subscription_type == 4) {
                $get_my_subscription->subscription_name = YEARLY;
            } //$get_my_subscription->subscription_type == 4
            
            if ($current_date > $end_date) {
                $this->api->api_message(0, NO_DATA);
            } //$current_date > $end_date
            else {
                $this->api->api_message_data(1, MY_SUB, 'my_subscription', $get_my_subscription);
            }
        } //$get_my_subscription
        else {
            $this->api->api_message(0, NOT_SUB);
        }
    }
    
    
    public function send_noti()
    {
        $title       = "hello";
        $msg_sent    = "hello wedding";
        $deviceToken = "fUNJ0fMGfMM:APA91bHrlWD4eIFVOWtKG-m3ssySguzUrKoymHYdePbrYH4Oxf-mKjnjrHsACJmXv8w5GeU2Sdz1kEbmUK3_6TDO7vJrTYoS6D5T1NMmk1jYryggMTbHXlSOo8beimAIeLxzyO_lMPKD";
        
        $API_ACCESS_KEY = 'AAAA9Eytjuk:APA91bEwOIgFw9Fwn-Ke0ZxZmFn5Tvh4nWtoKeydqxWqAMe7zTq05kAOE6XP17iEHrMBBPomQ8ruMgas34wULL6uqm_uYyasrgN0ZG5fysMJ91RPazo50JMAtifoYTHN6QxfjZSj-yzr';
        
        $msg     = array(
            'body' => $msg_sent,
            'title' => $title,
            'icon' => 'myicon',
            /*Default Icon*/
            'sound' => 'mySound'
            /*Default sound*/
        );
        $fields  = array(
            'to' => $deviceToken,
            'notification' => $msg
        );
        $headers = array(
            'Authorization: key=' . $API_ACCESS_KEY,
            'Content-Type: application/json'
        );
        #Send Reponse To FireBase Server    
        $ch      = curl_init();
        curl_setopt($ch, CURLOPT_URL, 'https://fcm.googleapis.com/fcm/send');
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
        $result = curl_exec($ch);
        curl_close($ch);
    }
    
    public function firebase_notification($user_id, $msg, $title, $type, $function_id)
    {
        $getUser     = $this->Api_model->getSingleRow(USR_TBL, array(
            'id' => $user_id
        ));
        $deviceToken = $getUser->device_token;
        if ($deviceToken) {
            $title_sent = $title;
            $msg_sent   = $msg;
            
            $getFunction        = $this->Api_model->getSingleRow('function', array(
                'id' => $function_id
            ));
            $getFunction->image = base_url() . $getFunction->image;
            $API_ACCESS_KEY     = 'AAAA9Eytjuk:APA91bEwOIgFw9Fwn-Ke0ZxZmFn5Tvh4nWtoKeydqxWqAMe7zTq05kAOE6XP17iEHrMBBPomQ8ruMgas34wULL6uqm_uYyasrgN0ZG5fysMJ91RPazo50JMAtifoYTHN6QxfjZSj-yzr';
            
            $msg = array(
                'body' => $msg_sent,
                'title' => $title,
                'type' => $type,
                'function_id' => $function_id,
                'function' => json_encode($getFunction),
                'icon' => 'myicon',
                /*Default Icon*/
                'sound' => 'mySound'
                /*Default sound*/
            );
            if ($getUser->device_type == "IOS") {
                $fields = array(
                    'to' => $deviceToken,
                    'notification' => $msg
                );
            } //$getUser->device_type == "IOS"
            else {
                $fields = array(
                    'to' => $deviceToken,
                    'data' => $msg
                );
            }
            $headers = array(
                'Authorization: key=' . $API_ACCESS_KEY,
                'Content-Type: application/json'
            );
            #Send Reponse To FireBase Server    
            $ch      = curl_init();
            curl_setopt($ch, CURLOPT_URL, 'https://fcm.googleapis.com/fcm/send');
            curl_setopt($ch, CURLOPT_POST, true);
            curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
            curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
            curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
            curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
            $result = curl_exec($ch);
            curl_close($ch);
        } //$deviceToken
    }
    
    
    
    /*Contact syncing */
    public function sendGroupMessage()
    {
        $input       = json_decode(file_get_contents('php://input'), true);
        $function_id = $input['function_id'];
        
        $getFuction = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id
        ));
        if (!$getFuction) {
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFuction
        $msg      = "From this happy code " . $getFuction->function_code . " you can login into Celebrino for " . $getFuction->title;
        $contact  = $input['contact'];
        $contacts = array();
        foreach ($contact as $contact) {
            //$this->send_opt_mobile($contact['country_code'].$contact['mobile'], $msg);
        } //$contact as $contact
        $this->api->api_message(1, "Message send successfully.");
    }
    
    
    /*Contact syncing */
    public function contactSyncing()
    {
        $input       = json_decode(file_get_contents('php://input'), true);
        $function_id = $input['function_id'];
        $type        = $input['type'];
        
        $getFuction = $this->Api_model->getSingleRow(FCN_TBL, array(
            'id' => $function_id
        ));
        if (!$getFuction) {
            
            $this->api->api_message(0, FNC_NT_FND);
            exit();
        } //!$getFuction
        else {
            if ($getFuction->status == 0) {
                $this->api->api_message(0, "This function is not approve by Admin. You need to ask to Admin for approving this function.");
                exit();
            } //$getFuction->status == 0
        }
        
        $msg      = "From this happy code " . $getFuction->function_code . " you can login into Celebrino for " . $getFuction->title;
        $contact  = $input['contact'];
        $contacts = array();
        foreach ($contact as $contact) {
            //$this->send_opt_mobile($contact['country_code'].$contact['mobile'], $msg);
            $getUser = $this->Api_model->getSingleRow(USR_TBL, array(
                'mobile' => $contact['mobile'],
                'country_code' => $contact['country_code']
            ));
            if ($getUser) {
                $getGuest = $this->Api_model->getSingleRow(GST_LIST_TBL, array(
                    'user_id' => $getUser->id,
                    'function_id' => $function_id
                ));
                if (!$getGuest) {
                    $guestData['user_id']     = $getUser->id;
                    $guestData['function_id'] = $function_id;
                    if (isset($type)) {
                        $guestData['type'] = $type;
                    } //isset($type)
                    else {
                        $guestData['type'] = 3;
                    }
                    
                    $guestData['name'] = $contact['name'];
                    $listID            = $this->Api_model->insertGetId(GST_LIST_TBL, $guestData);
                } //!$getGuest
            } //$getUser
            else {
                $datauser['mobile']       = $contact['mobile'];
                $datauser['name']         = $contact['name'];
                $datauser['signup_at']    = time();
                $datauser['country_code'] = $contact['country_code'];
                $datauser['role_id']      = 1;
                
                $getUserId = $this->Api_model->insertGetId(USR_TBL, $datauser);
                
                $guestData['user_id']     = $getUserId;
                $guestData['name']        = $contact['name'];
                $guestData['function_id'] = $function_id;
                if (isset($type)) {
                    $guestData['type'] = $type;
                } //isset($type)
                else {
                    $guestData['type'] = 3;
                }
                $listID = $this->Api_model->insertGetId(GST_LIST_TBL, $guestData);
            }
        } //$contact as $contact
        $this->api->api_message(1, ADD_GUEST);
    }
}