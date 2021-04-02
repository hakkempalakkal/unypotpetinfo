<?php
if (!defined('BASEPATH'))
    exit('No direct script access allowed');

class WebService extends CI_Controller
{
    
    public function __construct()
    {
        require_once APPPATH . "/third_party/FCMPushNotification.php";
        parent::__construct();
        $this->load->library('session');
        $this->load->database();
        $this->load->library('form_validation');
        /*Added By Varun_Andro*/
        $this->load->library('image_lib');
        $this->load->model('Api_model');
        $this->load->model('Comman_model');
        //Reminders Functions
        
        header('Access-Control-Allow-Origin: *');
        header("Access-Control-Allow-Methods: GET, POST, OPTIONS, PUT, DELETE");
    }
    /*Added By Varun_Andro*/
    public function index()
    {
        echo "Hello PetCare";
    }
    
    /*get conversation*/
    public function getNotifications()
    {
        $user_id = $this->input->post('user_id', TRUE);
        
        $get_notifications = $this->Api_model->getAllDataWhereAndOr(array(
            'user_id' => $user_id
        ), array(
            'type' => "All"
        ), "notifications");
        if ($get_notifications) {
            $this->Api_model->responseSuccess(1, "Get my notifications.", $get_notifications);
        } else {
            $this->Api_model->responseFailed(0, "Not yet any notifications.");
        }
    }
    
    public function signUp()
    {
        //updated by hakkem email failed
        $email      = $this->input->post('email');
        $password   = $this->input->post('password');
        $first_name = $this->input->post('first_name');
        
        $user = $this->Api_model->getSingleRow('users', array(
            'email' => $email
        ));
        if ($user) {
             
            $this->Api_model->responseFailed(0,"Email Id already registered. Please login or use another Email Id.");
            exit();
        } else {
             $data['email']      = $email;
             $data['password']   = $password;
             $data['first_name'] = $first_name;
             $getId              = $this->Api_model->insertGetId('users', $data);

            $user               = $this->Api_model->getSingleRow('users', array(
                'id' => $getId
            ));

              $url= 'http://192.168.1.7/Admin/WebService/userActive?id='.base64_encode($getId);

            $subject="PetInfo Registration";
            $msg="Thanks for signing up with PetInfo! Your account has been created, Please verify your account with below link.  ".$url;

            // $this->send_email_new_by_msg($email,$subject,$msg);
            $this->send_email_smtpV2($email);
            if ($user->profile_pic) {
                $user->profile_pic = $this->config->base_url() . $user->profile_pic;
            } else {
                $user->profile_pic = $this->config->base_url() . 'assets/images/dummyuser.png';
            }
            $this->Api_model->responseSuccess(1, "Please check your email and verified your account.", $user);
            exit();
        }
    }
    
   public function userActive()
    {   

       $user_id= base64_decode($_GET['id']);
       $get_user= $this->Api_model->getSingleRow('users', array('id'=>$user_id));
       if($get_user)
       {
          $where = array('id'=>$get_user->id);
          $data = array('verified'=>1);
          $update= $this->Api_model->updateSingleRow('users', $where, $data);
            
             // redirect('Admin/post');
          $this ->load->view('activeUser.php');

       }
    }
   public function userData()
    {   


       $user_id= base64_decode($_GET['id']);
       $get_user= $this->Api_model->getSingleRow('users', array('id'=>$user_id));


       if($get_user)
       {
         $data['password'] = $get_user->password;
         $data['name'] = $get_user->first_name;
         $this->load->view('forgetPasswordd.php', $data);
       }
    }

    public function login()
    {
        $email        = $this->input->post('email', TRUE);
        $password     = $this->input->post('password', TRUE);
        $device_id    = $this->input->post('device_id', TRUE);
        $os_type      = $this->input->post('os_type', TRUE);
        $device_tokan = $this->input->post('device_tokan', TRUE);
        
        $getdata = $this->Api_model->getSingleRow('users', array(
            'email' => $email
        ));
        if ($getdata) {
            $user = $this->Api_model->getSingleRow('users', array(
                'email' => $email,
                'password' => $password
            ));
            if (!$user) {
                $this->Api_model->responseFailed(0, "Please check your password.");
                exit();
            }
            
            if ($getdata->verified == 0) {
                $this->send_email_smtp($email);
                $this->Api_model->responseFailed(0, "Please check your email and verified your account.");
                exit();
            }
            if ($getdata->status == 1) {
                if ($getdata->profile_pic) {
                    $getdata->profile_pic = $this->config->base_url() . $getdata->profile_pic;
                } else {
                    $getdata->profile_pic = $this->config->base_url() . 'assets/images/dummyuser.png';
                }
                $data = array(
                    'device_id' => $device_id,
                    'os_type' => strtolower($os_type),
                    'device_tokan' => $device_tokan
                );
                $this->Api_model->updateSingleRow("users", array(
                    'email' => $email
                ), $data);
                $this->Api_model->responseSuccess(1, "Login successfully.", $getdata);
            } else {
                $this->Api_model->responseFailed(0, "You account has been blocked. Please contact to admin.");
                exit();
            }
        } else {
            $this->Api_model->responseFailed(0, "Please enter valid Username or Password");
            exit();
        }
    }
    
    public function send_email_smtp($email_id)
    {
        $this->load->library('email');
        $config = array(
            'protocol' => 'smtp',
            'smtp_host' => 'ssl://smtp.googlemail.com',
            'smtp_port' => 465,
            'smtp_user' => 'peteatsind@gmail.com',
            'smtp_pass' => 'sam@123yo',
            'mailtype' => 'html',
            'charset' => 'iso-8859-1'
        );
        
        $this->email->initialize($config);
        $this->email->set_mailtype("html");
        $this->email->set_newline("\r\n");
        $this->email->to($email_id);
        $this->email->from('peteatsind@gmail.com', REG_SUB);
        $this->email->subject(REG_SUB);
        $datas['email_id'] = $email_id;
        $body              = $this->load->view('email/registrationEmail.php', $datas, TRUE);
        $this->email->message($body);
        $this->email->send();
    }
    


    public function send_email_new_by_msg($email_id,$subject,$msg)
    {
         $KEY=$this->Api_model->getSingleRow('auth_key',array('id'=>1));
         $authKey=$KEY->auth_key;


          // $authKey = MSG_AUTH_KEY;
          $from= SENDER_EMAIL;

          //Prepare you post parameters
          $postData = array(
              'authkey' => $authKey,
              'to' => $email_id,
              'from' => $from,
              'subject' => $subject,
              'body' => $msg
          );
         //API URL
          $url="https://control.msg91.com/api/sendmail.php?authkey='$authKey'&to='$email_id'&from='$from'&body='$msg'&subject='$subject'";
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
          if(curl_errno($ch))
          {
            echo 'error:' . curl_error($ch);
          }
          curl_close($ch);
    }
    

    public function send_email_smtpV2()
    {
        $email_id="pnkjrko@gmail.com";
        $this->load->library('email');
        $config = array(
            'protocol' => 'smtp',
            'smtp_host' => 'ssl://smtp.googlemail.com',
            'smtp_port' => 465,
            'smtp_user' => 'peteatsind@gmail.com',
            'smtp_pass' => 'sam@123yo',
            'mailtype' => 'html',
            'charset' => 'iso-8859-1'
        );
        
        $this->email->initialize($config);
        $this->email->set_mailtype("html");
        $this->email->set_newline("\r\n");
        $this->email->to($email_id);
        $this->email->from('peteatsind@gmail.com', REG_SUB);
        $this->email->subject(REG_SUB);
        $datas['email_id'] = $email_id;
        $body              = $this->load->view('email/registrationEmail.php', $datas, TRUE);
        $this->email->message($body);
        $this->email->send();
         echo $this->email->print_debugger();

    }

    public function verificationStore()
    {
        $email_id = $_GET['userAuth'];
        $email_id = base64_decode($email_id);
        $store    = $this->Api_model->getSingleRow('users', array(
            'email' => $email_id
        ));
        if ($store) {
            $update = $this->Api_model->updateSingleRow('users', array(
                'email' => $email_id
            ), array(
                'verified' => 1
            ));
            $this->load->view('email/verificationSuccess.php');
        } else {
            $this->load->view('email/no-user.php');
        }
    }
    
    public function forgotPassword()
    {
        $email   = $this->input->post('email', TRUE);
        $getdata = $this->Api_model->getSingleRow('users', array(
            'email' => $email
        ));

        if ($getdata) {
            $getId=$getdata->id;
            $password = $this->strongToken(6);
            $update   = $this->Api_model->updateSingleRow('users', array(
                'email' => $email
            ), array(
                'password' => $password
            ));
            // $this->send_email_forget_password($email, $password);

            $url= 'http://192.168.1.7/Admin/WebService/userData?id='.base64_encode($getId);

            $subject=PWD_SUB;
            $msg="Your Password successfully updated. Please click here to see.".$url;

            $this->send_email_new_by_msg($email,$subject,$msg);
            $this->Api_model->responseFailed(1, "Please check your email for the updated password.");
            exit();
        } else {
            $this->Api_model->responseFailed(0, "Please enter valid Username.");
            exit();
        }
    }
    
    public function strongToken($length = 12, $add_dashes = false, $available_sets = 'luds')
    {
        $sets = array();
        if (strpos($available_sets, 'l') !== false)
            $sets[] = 'abcdefghjkmnpqrstuvwxyz';
        if (strpos($available_sets, 'u') !== false)
            $sets[] = 'ABCDEFGHJKMNPQRSTUVWXYZ';
        if (strpos($available_sets, 'd') !== false)
            $sets[] = '23456789';
        $all      = '';
        $password = '';
        foreach ($sets as $set) {
            $password .= $set[array_rand(str_split($set))];
            $all .= $set;
        }
        $all = str_split($all);
        for ($i = 0; $i < $length - count($sets); $i++)
            $password .= $all[array_rand($all)];
        $password = str_shuffle($password);
        if (!$add_dashes)
            return $password;
        $dash_len = floor(sqrt($length));
        $dash_str = '';
        while (strlen($password) > $dash_len) {
            $dash_str .= substr($password, 0, $dash_len) . '-';
            $password = substr($password, $dash_len);
        }
        $dash_str .= $password;
        return $dash_str;
    }
    public function send_email_forget_password($email_id, $password)
    {
        $this->load->library('email');
        $config = array(
            'protocol' => 'smtp',
            'smtp_host' => 'ssl://smtp.googlemail.com',
            'smtp_port' => 465,
            'smtp_user' => 'peteatsind@gmail.com',
            'smtp_pass' => 'sam@123yo',
            'mailtype' => 'html',
            'charset' => 'iso-8859-1'
        );
        
        $this->email->initialize($config);
        $this->email->set_mailtype("html");
        $this->email->set_newline("\r\n");
        $this->email->to($email_id);
        $this->email->from('peteatsind@gmail.com', PWD_SUB);
        $this->email->subject(PWD_SUB);
        $datas['password'] = $password;
        $body              = $this->load->view('email/forgetPassword.php', $datas, TRUE);
        $this->email->message($body);
        $this->email->send();
    }
    
    /*Login Api user can send mobile number, device id, device type, device tokan, os type and otp.. 
    This api provided user data*/
    public function sendOTP()
    {
        $mobile_no    = $this->input->post('mobile_no', TRUE);
        $device_id    = $this->input->post('device_id', TRUE);
        $otp          = $this->input->post('otp', TRUE);
        $os_type      = $this->input->post('os_type', TRUE);
        $device_tokan = $this->input->post('device_tokan', TRUE);
        $country_code = $this->input->post('country_code', TRUE);
        $table        = 'users';
        $condition    = array(
            'mobile_no' => $mobile_no,
            'country_code' => $country_code
        );
        
        $check_user = $this->Api_model->getSingleRow($table, $condition);
        $msg        = "Use $otp as One Time Password (OTP) to verify your PetInfo account. Do not share this OTP with anyone for security reasons.";
        
        if ($check_user) {
            if ($check_user->status == 1) {
                $data = array(
                    'mobile_no' => $mobile_no,
                    'device_id' => $device_id,
                    'os_type' => $os_type,
                    'device_tokan' => $device_tokan,
                    'country_code' => $country_code
                ); //,'longi'=>$longi
                $this->Api_model->updateSingleRow($table, array(
                    'mobile_no' => $mobile_no,
                    'country_code' => $country_code
                ), $data);
                $check_user              = $this->Api_model->getSingleRow($table, array(
                    'mobile_no' => $mobile_no,
                    'country_code' => $country_code
                ));
                $check_user->profile_pic = $this->config->base_url() . $check_user->profile_pic;
                $this->Api_model->responseSuccess(1, OTP_MSG, $check_user);
                $this->Api_model->send_opt_mobile($country_code . $mobile_no, $msg);
                if ($os_type != 'ios') {
                    $this->Api_model->send_opt_mobile($country_code . $mobile_no, $msg);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $data    = array(
                'country_code' => $country_code,
                'mobile_no' => $mobile_no,
                'device_id' => $device_id,
                'os_type' => $os_type,
                'device_tokan' => $device_tokan
            ); //,'lati'=>$lati,'longi'=>$longi
            $user_id = $this->Api_model->insertGetId($table, $data);
            if ($user_id) {
                $check_user              = $this->Api_model->getSingleRow($table, array(
                    'id' => $user_id
                ));
                $check_user->profile_pic = $this->config->base_url() . $check_user->profile_pic;
                $this->Api_model->responseSuccess(1, OTP_MSG, $check_user);
                $this->Api_model->send_opt_mobile($country_code, $mobile_no, $msg);
            } else {
                $this->Api_model->responseFailed(0, OTP_FAIL);
            }
        }
    }
    
    public function changePassword()
    {
        $user_id      = $this->input->post('user_id', TRUE);
        $password     = $this->input->post('password', TRUE);
        $new_password = $this->input->post('new_password', TRUE);
        
        $checkPass = $this->Api_model->getSingleRow("users", array(
            'id' => $user_id,
            'password' => $password
        ));
        if ($checkPass) {
            $where            = array(
                'id' => $user_id
            );
            $data['password'] = $new_password;
            $updateUser       = $this->Api_model->updateSingleRow("users", $where, $data);
            
            $this->Api_model->responseFailed(1, "Password updated successfully.");
        } else {
            $this->Api_model->responseFailed(0, "Old Password does not matched.");
        }
        exit();
    }
    
    /*Update Profile Api user can send all perameter for update. Update user record where user id. This api provided user data*/
    public function updateProfileUser()
    {
        $user_id     = $this->input->post('user_id', TRUE);
        $mobile_no   = $this->input->post('mobile_no', TRUE);
        $first_name  = $this->input->post('first_name', TRUE);
        $last_name   = $this->input->post('last_name', TRUE);
        $email       = $this->input->post('email', TRUE);
        $address     = $this->input->post('address', TRUE);
        $city        = $this->input->post('city', TRUE);
        $state       = $this->input->post('state', TRUE);
        $postcode    = $this->input->post('postcode', TRUE);
        $country     = $this->input->post('country', TRUE);
        $lati        = $this->input->post('lati', TRUE);
        $longi       = $this->input->post('longi', TRUE);
        $profile_pic = $this->input->post('profile_pic', TRUE);
        
        if ($user_id != '' || $user_id != NULL) {
            $table     = 'users';
            $condition = array(
                'id' => $user_id
            );
            
            $check_user = $this->Api_model->getSingleRow($table, $condition);
            if ($check_user) {
                if ($check_user->status == 1) {
                    $this->load->library('upload');
                    $config['upload_path']   = './assets/images/';
                    $config['allowed_types'] = 'gif|jpg|jpeg|png';
                    $config['max_size']      = 10000;
                    $config['file_name']     = time();
                    $this->upload->initialize($config);
                    $profileimage = "";
                    if ($this->upload->do_upload('profile_pic')) {
                        $profileimage = 'assets/images/' . $this->upload->data('file_name');
                    } else {
                        
                    }
                    
                    if ($profileimage) {
                        $data['profile_pic'] = $profileimage;
                    }
                    $data['mobile_no']  = $mobile_no;
                    $data['first_name'] = $first_name;
                    $data['last_name']  = $last_name;
                    $data['email']      = $email;
                    $data['address']    = $address;
                    $data['city']       = $city;
                    $data['state']      = $state;
                    $data['postcode']   = $postcode;
                    $data['country']    = $country;
                    $data['lati']       = $lati;
                    $data['longi']      = $longi;
                    
                    $this->Api_model->updateSingleRow($table, array(
                        'id' => $user_id
                    ), $data);
                    $check_user              = $this->Api_model->getSingleRow($table, array(
                        'id' => $user_id
                    ));
                    $check_user->profile_pic = $this->config->base_url() . $check_user->profile_pic;
                    $sms_msg                 = WELCOME;
                    $this->Api_model->responseSuccess(1, UPDATE_PROFILE, $check_user);
                    $this->Api_model->send_email($email, WELCOME_MSG, $sms_msg);
                } else {
                    $this->Api_model->responseFailed(3, INACTIVATE);
                }
            } else {
                $this->Api_model->responseFailed(0, PROFILE_UPDATE_FAILED);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
        }
    }
    /*Add Pet Api user can add pet releted infromation. add pet record where user id. 
    This api provided success or failed*/
    public function add_pet()
    {
        $user_id            = $this->input->post('user_id');
        $petName            = $this->input->post('petName');
        $breed_id           = $this->input->post('breed_id');
        $sex                = $this->input->post('sex');
        $current_weight     = $this->input->post('current_weight');
        $current_height     = $this->input->post('current_height');
        $microchip_id       = $this->input->post('microchip_id');
        $pet_type           = $this->input->post('pet_type');
        $active_area        = $this->input->post('active_area');
        $lifestyle          = $this->input->post('lifestyle');
        $neutered           = $this->input->post('neutered');
        $trained            = $this->input->post('trained');
        $birth_date         = $this->input->post('birth_date');
        $adoption_date      = $this->input->post('adoption_date');
        $allergies          = $this->input->post('allergies');
        $medical_conditions = $this->input->post('medical_conditions');
        $medicines          = $this->input->post('medicines');
        $likes              = $this->input->post('likes');
        $dislikes           = $this->input->post('dislikes');
        $incidents          = $this->input->post('incidents');
        
        $pet_img_path = $this->input->post('pet_img_path', TRUE);
        
        if (!empty($user_id)) {
            $userStatus = $this->Api_model->getSingleRow('users', array(
                'id' => $user_id
            ));
            if ($userStatus) {
                if ($userStatus->status == 1) {
                    $this->load->library('upload');
                    
                    $config['upload_path']   = './assets/images/';
                    $config['allowed_types'] = 'gif|jpg|jpeg|png';
                    $config['max_size']      = 10000;
                    $config['file_name']     = time();
                    $this->upload->initialize($config);
                    $petimage = "";
                    if ($this->upload->do_upload('pet_img_path')) {
                        $petimage = 'assets/images/' . $this->upload->data('file_name');
                    } else {
                        
                    }
                    if ($petimage) {
                        $petdata['pet_img_path'] = $petimage;
                    }
                    $petdata['user_id']            = $user_id;
                    $petdata['petName']            = isset($petName) ? $petName : "";
                    $petdata['breed_id']           = $breed_id;
                    $petdata['sex']                = isset($sex) ? $sex : "";
                    $petdata['current_weight']     = isset($current_weight) ? $current_weight : "";
                    $petdata['current_height']     = isset($current_height) ? $current_height : "";
                    $petdata['microchip_id']       = isset($microchip_id) ? $microchip_id : "";
                    $petdata['pet_type']           = isset($pet_type) ? $pet_type : "";
                    $petdata['active_area']        = isset($active_area) ? $active_area : "";
                    $petdata['lifestyle']          = isset($lifestyle) ? $lifestyle : "";
                    $petdata['neutered']           = isset($neutered) ? $neutered : "";
                    $petdata['trained']            = isset($trained) ? $trained : "";
                    $petdata['birth_date']         = isset($birth_date) ? $birth_date : "";
                    $petdata['adoption_date']      = isset($adoption_date) ? $adoption_date : "";
                    $petdata['allergies']          = isset($allergies) ? $allergies : "";
                    $petdata['medical_conditions'] = isset($medical_conditions) ? $medical_conditions : "";
                    $petdata['medicines']          = isset($medicines) ? $medicines : "";
                    $petdata['likes']              = isset($likes) ? $likes : "";
                    $petdata['dislikes']           = isset($dislikes) ? $dislikes : "";
                    $petdata['incidents']          = isset($incidents) ? $incidents : "";
                    $petdata['updated_stamp']      = time();
                    $petdata['deleted']            = false;
                    
                    $my_user = $this->Api_model->getSingleRow('users', array(
                        'id' => $user_id
                    ));
                    
                    if (!empty($my_user)) {
                        $petdata['city']     = isset($my_user->city) ? $my_user->city : "0";
                        $petdata['state']    = isset($my_user->state) ? $my_user->state : "0";
                        $petdata['country']  = isset($my_user->country) ? $my_user->country : "0";
                        $petdata['postcode'] = isset($my_user->postcode) ? $my_user->postcode : "0";
                        $petdata['lati']     = isset($my_user->lati) ? $my_user->lati : "0";
                        $petdata['longi']    = isset($my_user->longi) ? $my_user->longi : "0";
                    }
                    $table = 'pet';
                    $check = $this->Api_model->insert($table, $petdata);
                    if ($check) {
                        $this->Api_model->responseSuccessWithOutData(1, ADD_PET);
                    } else {
                        $this->Api_model->responseFailed(0, NOT_RESPONDING);
                    }
                } else {
                    $this->Api_model->responseFailed(3, INACTIVATE);
                }
            } else {
                $this->Api_model->responseFailed(0, NOT_RESPONDING);
            }
        } else {
            $this->Api_model->responseFailed(0, SEND_USER_ID);
        }
    }
    /*Get My Pet Api user can add pet releted infromation. add pet record where user id. 
    This api provided success or failed*/
    
    public function getMyPet()
    {
        $user_id    = $this->input->post('user_id', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $pets = $this->Api_model->getAllDataWhere(array(
                    'user_id' => $user_id,
                    'deleted' => false
                ), 'pet');
                
                if ($pets) {
                    $pet = array();
                    foreach ($pets as $pets) {
                        $this->checkFollow($pets, $pets->id, $user_id);
                        $table              = 'breed';
                        $condition          = array(
                            'id' => $pets->breed_id
                        );
                        $check_pet          = $this->Api_model->getSingleRow($table, $condition);
                        $pets->breed_name   = $check_pet->breed_name;
                        $pets->breed_target = $check_pet->target;
                        
                        $data1 = $this->Api_model->get_manual_activity_bydate($pets->id, $user_id, date('Y-m-d'));
                        
                        // print_r($data1);
                        // exit();
                        if (empty($data1)) {
                            $pets->percent = 0;
                        } else {
                            $per           = ($data1[0]->manualactivity / $check_pet->target) * 100;
                            $pets->percent = round($per, 0);
                        }
                        
                        
                        $table               = 'pet_type';
                        $condition           = array(
                            'id' => $check_pet->pet_type
                        );
                        $my_pet_type         = $this->Api_model->getSingleRow($table, $condition);
                        $pets->pet_type_name = $my_pet_type->pet_name;
                        
                        $pets->pet_img_path = $this->config->base_url() . $pets->pet_img_path;
                        
                        $data = $this->Api_model->get_manual_activity($pets->id, $user_id);
                        
                        if (empty($data)) {
                            $pets->chart = $data;
                        } else {
                            $pets->chart = $data;
                        }
                        
                        array_push($pet, $pets);
                    }
                    $this->Api_model->responseSuccess(1, ALL_MY_PET, $pet);
                } else {
                    $this->Api_model->responseFailed(0, NO_PET_FOUND);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
    }
    
    
    public function getSinglePet()
    {
        $user_id    = $this->input->post('user_id', TRUE);
        $pet_id     = $this->input->post('pet_id', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $table     = 'pet';
                $condition = array(
                    'user_id' => $user_id,
                    'id' => $pet_id,
                    'deleted' => false
                );
                
                $check_pet = $this->Api_model->getSingleRow($table, $condition);
                
                if ($check_pet) {
                    
                    $this->checkFollow($check_pet, $check_pet->id, $user_id);
                    $table                   = 'breed';
                    $condition               = array(
                        'id' => $check_pet->breed_id
                    );
                    $breed_pet               = $this->Api_model->getSingleRow($table, $condition);
                    $check_pet->breed_name   = $breed_pet->breed_name;
                    $check_pet->breed_target = $breed_pet->target;
                    
                    $data1 = $this->Api_model->get_manual_activity_bydate($check_pet->id, $user_id, date('Y-m-d'));
                    
                    
                    if (empty($data1)) {
                        $check_pet->percent = 0;
                    } else {
                        $per                = ($data1[0]->manualactivity / $breed_pet->target) * 100;
                        $check_pet->percent = round($per, 0);
                    }
                    
                    $table                    = 'pet_type';
                    $condition                = array(
                        'id' => $check_pet->pet_type
                    );
                    $my_pet_type              = $this->Api_model->getSingleRow($table, $condition);
                    $check_pet->pet_type_name = $my_pet_type->pet_name;
                    
                    $check_pet->pet_img_path = $this->config->base_url() . $check_pet->pet_img_path;
                    
                    $data = $this->Api_model->get_manual_activity($check_pet->id, $user_id);
                    
                    if (empty($data)) {
                        $check_pet->chart = $data;
                    } else {
                        $check_pet->chart = $data;
                    }
                    
                    $this->Api_model->responseSuccess(1, ALL_MY_PET, $check_pet);
                } else {
                    $this->Api_model->responseFailed(0, NOT_RESPONDING);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
        
    }
    
    public function getPetByUser()
    {
        $user_id          = $this->input->post('user_id', TRUE);
        $follower_user_id = $this->input->post('follower_user_id', TRUE);
        $userStatus       = $this->Api_model->getSingleRow('users', array(
            'id' => $follower_user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $pets = $this->Api_model->getAllDataWhere(array(
                    'user_id' => $user_id,
                    'deleted' => false
                ), 'pet');
                if ($pets) {
                    $pet                   = array();
                    $userInfo              = array();
                    $userData              = $this->Api_model->getSingleRow('users', array(
                        'id' => $user_id
                    ));
                    $userData->profile_pic = $this->config->base_url() . $userData->profile_pic;
                    
                    foreach ($pets as $pets) {
                        if ($pets->public_private == 1) {
                            
                        } else {
                            $this->checkFollow($pets, $pets->id, $follower_user_id);
                            $table              = 'breed';
                            $condition          = array(
                                'id' => $pets->breed_id
                            );
                            $check_pet          = $this->Api_model->getSingleRow($table, $condition);
                            $pets->breed_name   = $check_pet->breed_name;
                            $pets->breed_target = $check_pet->target;
                            
                            $data1 = $this->Api_model->get_manual_activity_bydate($pets->id, $user_id, date('Y-m-d'));
                            
                            if (empty($data1)) {
                                $pets->percent = 0;
                            } else {
                                $per           = ($data1[0]->manualactivity / $check_pet->target) * 100;
                                $pets->percent = round($per, 0);
                            }
                            
                            $table               = 'pet_type';
                            $condition           = array(
                                'id' => $check_pet->pet_type
                            );
                            $my_pet_type         = $this->Api_model->getSingleRow($table, $condition);
                            $pets->pet_type_name = $my_pet_type->pet_name;
                            $pets->pet_img_path  = $this->config->base_url() . $pets->pet_img_path;
                            $data                = $this->Api_model->get_manual_activity($pets->id, $user_id);
                            
                            if (empty($data)) {
                                $pets->chart = $data;
                            } else {
                                $pets->chart = $data;
                            }
                            
                            array_push($pet, $pets);
                        }
                    }
                    if ($pet != NULL) {
                        $this->Api_model->responseSuccessThree(1, ALL_MY_PET, $userData, $pet);
                    } else {
                        $this->Api_model->responseFailed(0, PET_NOT_AVAILABLE);
                    }
                } else {
                    $this->Api_model->responseFailed(0, NOT_RESPONDING);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
        }
    }
    
    public function checkFollow($data, $pet_id, $user_id)
    {
        $followerData  = $this->Api_model->getSingleRow(FOLLOW_TBL, array(
            'pet_id' => $pet_id,
            'follower_user_id' => $user_id
        ));
        $followerCount = $this->Api_model->getAllDataWhere(array(
            'pet_id' => $pet_id
        ), FOLLOW_TBL);
        if ($followerData) {
            $data->is_follow = IS_FOLLOW_TRUE;
        } else {
            $data->is_follow = IS_FOLLOW_FALSE;
        }
        $data->followerCount = count($followerCount);
        return $data;
    }
    
    public function updatePublicPrivate()
    {
        $user_id    = $this->input->post('user_id', TRUE);
        $pet_id     = $this->input->post('pet_id', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $public_private = $this->input->post('public_private', TRUE);
                $table          = 'pet';
                $condition      = array(
                    'id' => $pet_id,
                    'user_id' => $user_id
                );
                
                $public = $this->Api_model->updateSingleRow($table, array(
                    'id' => $pet_id
                ), array(
                    'public_private' => $public_private
                ));
                
                if ($public) {
                    if ($public_private == 0) {
                        $this->Api_model->responseFailed(1, UPDATE_PUBLIC_PRIVATE_1);
                    } else {
                        $this->Api_model->responseFailed(1, UPDATE_PUBLIC_PRIVATE_2);
                    }
                } else {
                    $this->Api_model->responseFailed(0, NOT_RESPONDING);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
    }
    
    public function updateAdopt()
    {
        $user_id    = $this->input->post('user_id', TRUE);
        $pet_id     = $this->input->post('pet_id', TRUE);
        $adopt      = $this->input->post('adopt', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $table     = 'pet';
                $condition = array(
                    'id' => $pet_id,
                    'user_id' => $user_id
                );
                $public    = $this->Api_model->updateSingleRow($table, array(
                    'id' => $pet_id
                ), array(
                    'adopt' => $adopt
                ));
                if ($public) {
                    if ($adopt == 0) {
                        $this->Api_model->responseFailed(1, ENABLE_ADOPT);
                    } else {
                        $this->Api_model->responseFailed(1, DISABLE_ADOPT);
                    }
                } else {
                    $this->Api_model->responseFailed(0, NOT_RESPONDING);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
    }
    
    public function updateSaleNotSale()
    {
        $user_id    = $this->input->post('user_id', TRUE);
        $pet_id     = $this->input->post('pet_id', TRUE);
        $sel_notsel = $this->input->post('sel_notsel', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $table     = 'pet';
                $condition = array(
                    'id' => $pet_id,
                    'user_id' => $user_id
                );
                
                $public = $this->Api_model->updateSingleRow($table, array(
                    'id' => $pet_id
                ), array(
                    'sel_notsel' => $sel_notsel
                ));
                
                if ($public) {
                    if ($sel_notsel == 0) {
                        $this->Api_model->responseFailed(1, UPDATE_SALE_1);
                    } else {
                        $this->Api_model->responseFailed(1, UPDATE_SALE_2);
                    }
                } else {
                    $this->Api_model->responseFailed(0, NOT_RESPONDING);
                }
                
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
            
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
        
    }
    
    public function getNearByUser()
    {
        
        $user_id    = $this->input->post('user_id', TRUE);
        $lati       = $this->input->post('lati', TRUE);
        $longi      = $this->input->post('longi', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                
                $users = $this->Api_model->getAllDataWhere(array(
                    "id !=" => $user_id
                ), 'users');
                if ($users) {
                    $user = array();
                    foreach ($users as $users) {
                        if ($users->role_id != 1) {
                            $distance = $this->Api_model->distance($lati, $longi, $users->lati, $users->longi);
                            $distance = round($distance, 2);
                            if ($distance < 100) {
                                $distance_str       = "$distance";
                                $users->distance    = $distance_str;
                                $users->profile_pic = $this->config->base_url() . $users->profile_pic;
                                
                                array_push($user, $users);
                            }
                        }
                    }
                    if (empty($user)) {
                        $this->Api_model->responseFailed(0, NOT_RESPONDING);
                    } else {
                        $this->Api_model->responseSuccess(1, ALL_NEAR_BY, $user);
                    }
                } else {
                    $this->Api_model->responseFailed(0, NOT_RESPONDING);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
        
    }
    
    
    
    public function getAllBreedByType()
    {
        $user_id  = $this->input->post('user_id', TRUE);
        $pet_type = $this->input->post('pet_type', TRUE);
        $breed    = $this->Api_model->getAllBreed(array(
            "pet_type" => $pet_type
        ), 'breed');
        if ($breed) {
            $breeds = array();
            foreach ($breed as $breed) {
                $breed->image_path = $this->config->base_url() . $breed->image_path;
                array_push($breeds, $breed);
            }
            $this->Api_model->responseSuccess(1, FIND_BREED, $breeds);
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
        }
    }
    
    public function update_pet()
    {
        $user_id            = $this->input->post('user_id', TRUE);
        $pet_id             = $this->input->post('pet_id', TRUE);
        $petName            = $this->input->post('petName');
        $sex                = $this->input->post('sex');
        $current_weight     = $this->input->post('current_weight');
        $current_height     = $this->input->post('current_height');
        $microchip_id       = $this->input->post('microchip_id');
        $active_area        = $this->input->post('active_area');
        $lifestyle          = $this->input->post('lifestyle');
        $neutered           = $this->input->post('neutered');
        $trained            = $this->input->post('trained');
        $birth_date         = $this->input->post('birth_date');
        $adoption_date      = $this->input->post('adoption_date');
        $allergies          = $this->input->post('allergies');
        $medical_conditions = $this->input->post('medical_conditions');
        $medicines          = $this->input->post('medicines');
        $likes              = $this->input->post('likes');
        $dislikes           = $this->input->post('dislikes');
        $incidents          = $this->input->post('incidents');
        $pet_img_path       = $this->input->post('pet_img_path', TRUE);
        $userStatus         = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $this->load->library('upload');
                
                $config['upload_path']   = './assets/images/';
                $config['allowed_types'] = 'gif|jpg|jpeg|png';
                $config['max_size']      = 10000;
                $config['file_name']     = time();
                $this->upload->initialize($config);
                $petimage = "";
                if ($this->upload->do_upload('pet_img_path')) {
                    $petimage = 'assets/images/' . $this->upload->data('file_name');
                }
                
                $table     = 'pet';
                $check_pet = $this->Api_model->getSingleRow($table, array(
                    'id' => $pet_id
                ));
                if ($check_pet) {
                    
                    if ($petimage) {
                        $petdata['pet_img_path'] = $petimage;
                    }
                    
                    $petdata['updated_stamp']      = time();
                    $petdata['petName']            = isset($petName) ? $petName : $check_pet->petName;
                    $petdata['sex']                = isset($sex) ? $sex : $check_pet->sex;
                    $petdata['current_weight']     = isset($current_weight) ? $current_weight : $check_pet->current_weight;
                    $petdata['current_height']     = isset($current_height) ? $current_height : $check_pet->current_height;
                    $petdata['microchip_id']       = isset($microchip_id) ? $microchip_id : $check_pet->microchip_id;
                    $petdata['active_area']        = isset($active_area) ? $active_area : $check_pet->active_area;
                    $petdata['lifestyle']          = isset($lifestyle) ? $lifestyle : $check_pet->lifestyle;
                    $petdata['neutered']           = isset($neutered) ? $neutered : $check_pet->neutered;
                    $petdata['trained']            = isset($trained) ? $trained : $check_pet->trained;
                    $petdata['birth_date']         = isset($birth_date) ? $birth_date : $check_pet->birth_date;
                    $petdata['adoption_date']      = isset($adoption_date) ? $adoption_date : $check_pet->adoption_date;
                    $petdata['allergies']          = isset($allergies) ? $allergies : $check_pet->allergies;
                    $petdata['medical_conditions'] = isset($medical_conditions) ? $medical_conditions : $check_pet->medical_conditions;
                    $petdata['medicines']          = isset($medicines) ? $medicines : $check_pet->medicines;
                    $petdata['likes']              = isset($likes) ? $likes : $check_pet->likes;
                    $petdata['dislikes']           = isset($dislikes) ? $dislikes : $check_pet->dislikes;
                    $petdata['incidents']          = isset($incidents) ? $incidents : $check_pet->incidents;
                    $my_user                       = $this->Api_model->getSingleRow('users', array(
                        'id' => $user_id
                    ));
                    if (!empty($my_user)) {
                        $petdata['city']     = $my_user->city;
                        $petdata['state']    = $my_user->state;
                        $petdata['country']  = $my_user->country;
                        $petdata['postcode'] = $my_user->postcode;
                        $petdata['lati']     = $my_user->lati;
                        $petdata['longi']    = $my_user->longi;
                    }
                    $this->Api_model->updateSingleRow($table, array(
                        'id' => $pet_id
                    ), $petdata);
                    $this->Api_model->responseSuccessWithOutData(1, UPDATE_PET);
                } else {
                    $this->Api_model->responseFailed(0, NOT_RESPONDING);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
        }
    }
    
    /*0 = {HashMap$Node@6494} "date" -> "Wed, Mar 21, 2018 01:10 PM"
    1 = {HashMap$Node@6495} "user_id" -> "1"
    2 = {HashMap$Node@6496} "timezone" -> "GMT+05:30"
    3 = {HashMap$Node@6497} "repeat" -> "5"
    4 = {HashMap$Node@6498} "remark" -> "vjjvvjbiib"
    5 = {HashMap$Node@6499} "category" -> "Fun Walk"
    6 = {HashMap$Node@6500} "pet_id" -> "1"
    7 = {HashMap$Node@6501} "advance" -> "5760"*/
    //Reminder Functions
    public function create_reminder()
    {
        $app_id   = $this->input->post('app_id');
        $user_id  = $this->input->post('user_id');
        $pet_id   = $this->input->post('pet_id');
        // $timing=$this->input->post('timing');
        $category = $this->input->post('category');
        $date     = $this->input->post('date');
        $timezone = $this->input->post('timezone');
        $advance  = $this->input->post('advance');
        $repeat   = $this->input->post('repeat');
        $remark   = $this->input->post('remark');
        
        /*setting timezone according offset */
        $offset = preg_replace('/[^0-9:+]/', '', $this->input->post('timezone'));
        
        // Calculate seconds from offset
        list($hours, $minutes) = explode(':', $offset);
        $seconds = $hours * 60 * 60 + $minutes * 60;
        
        // Get timezone name from seconds
        $tz = timezone_name_from_abbr('', $seconds, 1);
        
        // using abbr
        if ($tz === false)
            $tz = timezone_name_from_abbr('', $seconds, 0);
        
        // Set timezone
        date_default_timezone_set($tz);
        
        $current_time = strtotime($date);
        
        $data['timezone'] = $timezone;
        $data['user_id']  = $user_id;
        
        $data['date_string'] = $date;
        $data['category']    = $category;
        $data['repeat']      = $repeat;
        $data['advance']     = $advance;
        $data['status']      = 0;
        $data['remark']      = $remark;
        $data['pet_id']      = $pet_id;
        
        $get_date = strtotime($date);
        
        $date_temp = date_create($date);
        $c_date    = date_format($date_temp, "Y-m-d");
        $c_date    = date_format($date_temp, "Y-m-d");
        
        $timing         = date_format($date_temp, "h:i A");
        $data['timing'] = $timing;
        
        $data['appointment_timestamp'] = $current_time;
        $data['appointment_date']      = $c_date;
        
        $send_time = date("Y-m-d h:i A", strtotime('-' . $advance . ' minutes', $get_date));
        
        $check_app['timing'] = $timing;
        
        $msg = "TEST";
        if (isset($app_id) && $app_id != '') {
            $done = $this->db->where('id', $app_id)->update('appointment', $data);
            if ($done) {
                $this->Api_model->responseSuccess(1, DONE, $data);
            } else {
                $this->Api_model->responseFailed(0, NOT_DONE);
            }
        } else {
            $done = $this->Api_model->add_appointment($data);
            if ($done) {
                $this->Api_model->responseSuccessWithOutData(1, DONE);
            } else {
                $this->Api_model->responseFailed(0, NOT_DONE);
            }
        }
        
        $template      = "Task created";
        $user_deatils  = $this->Api_model->get_user_record_by_user_id($user_id);
        $os_type       = $user_deatils[0]->os_type;
        $device_id     = $user_deatils[0]->device_id;
        $sendTimestamp = strtotime($send_time);
        $send_temp     = date_create($send_time);
        $s_date        = date_format($send_temp, "Y-m-d");
        $s_timing      = date_format($send_temp, "h:i A");
        $Send_times    = date("Y-m-d h:i A", strtotime('-60 minutes', $sendTimestamp));
        $sendTimestamp = strtotime($Send_times);
        $notification  = array(
            'content' => $category,
            'deviceToken' => $device_id,
            'type' => 1,
            'deviceType' => $os_type,
            'isRead' => 0,
            'user_id' => $user_id,
            'sentTime' => $s_timing,
            'sentTimestamp' => $sendTimestamp,
            'flag' => '0',
            'title' => 'PetInfo',
            'sentDate' => $s_date,
            'trueTime' => $timing,
            'trueDate' => $c_date
        );
        $done          = $this->Api_model->create_notification($notification);
    }
    
    function change_status()
    {
        $user_id    = $this->input->post('user_id');
        $app_id     = $this->input->post('id');
        $status     = $this->input->post('status');
        $send       = false;
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                if (isset($app_id) && $app_id != '' && isset($status) && $status != '') {
                    if ($status == 4) {
                        $changed = $this->Api_model->change_status($app_id, $status);
                        $this->Api_model->responseSuccessWithOutData(1, REMINDER_DELETED);
                    } else {
                        $my_app = $this->Api_model->get_app($app_id)->row();
                        if ($my_app->repeat != 0 and $my_app->repeat != '') {
                            $user_id  = $my_app->user_id;
                            $category = $my_app->category;
                            $offset   = preg_replace('/[^0-9:+]/', '', $my_app->timezone);
                            // Calculate seconds from offset
                            list($hours, $minutes) = explode(':', $offset);
                            $seconds = $hours * 60 * 60 + $minutes * 60;
                            // Get timezone name from seconds
                            $tz      = timezone_name_from_abbr('', $seconds, 1);
                            // using abbr 
                            if ($tz === false)
                                $tz = timezone_name_from_abbr('', $seconds, 0);
                            // Set timezone
                            date_default_timezone_set($tz);
                            /*end here 8*/
                            $date                          = date('Y-m-d');
                            /* dont change below anything */
                            $current_date_c                = date('Y-m-d', strtotime($date . ' + ' . $my_app->repeat . ' days'));
                            $date_c                        = date_create("$current_date_c $my_app->timing");
                            $date_c                        = date_format($date_c, "D, M d, Y h:i A");
                            $date                          = $date_c;
                            $timezone                      = $my_app->timezone;
                            $advance                       = $my_app->advance;
                            $repeat                        = $my_app->repeat;
                            $data['timezone']              = $timezone;
                            $data['user_id']               = $user_id;
                            $data['pet_id']                = $my_app->pet_id;
                            $data['date_string']           = $date_c;
                            $data['category']              = $category;
                            $data['repeat']                = $repeat;
                            $data['advance']               = $advance;
                            $data['status']                = 0;
                            $data['remark']                = $my_app->remark;
                            $get_date                      = strtotime($date);
                            $date_temp                     = date_create($date);
                            $c_date                        = date_format($date_temp, "Y-m-d");
                            $c_date                        = date_format($date_temp, "Y-m-d");
                            $timing                        = date_format($date_temp, "h:i A");
                            $current_date                  = strtotime($c_date);
                            $data['timing']                = $timing;
                            $data['appointment_timestamp'] = strtotime($date_c);
                            $data['appointment_date']      = $c_date;
                            $changed                       = $this->Api_model->change_status($app_id, $status);
                            $app_id                        = $this->Api_model->add_appointment($data);
                            $send_time                     = date("Y-m-d h:i A", strtotime('-' . $advance . ' minutes', $current_date));
                            $user_deatils                  = $this->Api_model->get_user_record_by_user_id($user_id);
                            $os_type                       = $user_deatils[0]->os_type;
                            $device_id                     = $user_deatils[0]->device_id;
                            $sendTimestamp                 = strtotime($send_time);
                            $send_temp                     = date_create($send_time);
                            $s_date                        = date_format($send_temp, "Y-m-d");
                            $s_timing                      = date_format($send_temp, "h:i A");
                            $Send_times                    = date("Y-m-d h:i A", strtotime('-60 minutes', $sendTimestamp));
                            $sendTimestamp                 = strtotime($Send_times);
                            $notification                  = array(
                                'content' => $category,
                                'deviceToken' => $device_id,
                                'type' => 1,
                                'deviceType' => $os_type,
                                'isRead' => 0,
                                'user_id' => $user_id,
                                'sentTime' => $s_timing,
                                'sentTimestamp' => $sendTimestamp,
                                'flag' => '0',
                                'title' => 'PetInfo',
                                'sentDate' => $s_date,
                                'trueTime' => $timing,
                                'trueDate' => $c_date
                            );
                            $done                          = $this->Api_model->create_notification($notification);
                            $this->Api_model->responseSuccessWithOutData(1, '');
                        } else {
                            $changed = $this->Api_model->change_status($app_id, $status);
                            $this->Api_model->responseSuccessWithOutData(1, SUCCESS);
                        }
                    }
                } else {
                    $this->Api_model->responseFailed(0, INVALID_REQUEST);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
    }
    
    function get_reminders()
    {
        $app_id     = $this->input->post('id');
        $user_id    = $this->input->post('user_id');
        $pet_id     = $this->input->post('pet_id');
        $send       = false;
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                if (isset($app_id) && $app_id != '') {
                    $app_data               = $this->Api_model->get_app($app_id)->row();
                    $table                  = 'pet';
                    $condition              = array(
                        'user_id' => $user_id,
                        'id' => $app_data->pet_id
                    );
                    $check_pet              = $this->Api_model->getSingleRow($table, $condition);
                    $app_data->pet_name     = $check_pet->petName;
                    $app_data->pet_img_path = $this->config->base_url() . $check_pet->pet_img_path;
                    $reminder               = $app_data;
                    if (!empty($app_data)) {
                        $send = true;
                    } else {
                        $send = false;
                    }
                } elseif (isset($user_id) && $user_id != '' && isset($pet_id) && $pet_id != '') {
                    $app_data = $this->Api_model->get_app('', $user_id, $pet_id)->result();
                    $reminder = array();
                    foreach ($app_data as $app_data) {
                        $table                  = 'pet';
                        $condition              = array(
                            'user_id' => $user_id,
                            'id' => $pet_id
                        );
                        $check_pet              = $this->Api_model->getSingleRow($table, $condition);
                        $app_data->pet_name     = $check_pet->petName;
                        $app_data->pet_img_path = $this->config->base_url() . $check_pet->pet_img_path;
                        array_push($reminder, $app_data);
                    }
                    if (!empty($app_data)) {
                        $send = true;
                    } else {
                        $send = false;
                    }
                } elseif (isset($user_id) && $user_id != '') {
                    $app_data = $this->Api_model->get_app('', $user_id, '')->result();
                    $reminder = array();
                    foreach ($app_data as $app_data) {
                        $table                  = 'pet';
                        $condition              = array(
                            'user_id' => $user_id,
                            'id' => $app_data->pet_id
                        );
                        $check_pet              = $this->Api_model->getSingleRow($table, $condition);
                        $app_data->pet_name     = $check_pet->petName;
                        $app_data->pet_img_path = $this->config->base_url() . $check_pet->pet_img_path;
                        array_push($reminder, $app_data);
                    }
                    if (!empty($app_data)) {
                        $send = true;
                    } else {
                        $send = false;
                    }
                } else {
                    $app_data = $this->Api_model->get_app()->result();
                    $reminder = array();
                    foreach ($app_data as $app_data) {
                        $table                  = 'pet';
                        $condition              = array(
                            'user_id' => $app_data->user_id,
                            'id' => $app_data->pet_id
                        );
                        $check_pet              = $this->Api_model->getSingleRow($table, $condition);
                        $app_data->pet_name     = $check_pet->petName;
                        $app_data->pet_img_path = $this->config->base_url() . $check_pet->pet_img_path;
                        array_push($reminder, $app_data);
                    }
                    if (!empty($app_data)) {
                        $send = true;
                    } else {
                        $send = false;
                    }
                }
                if ($send) {
                    $this->Api_model->responseSuccess(1, SUCCESS, $reminder);
                } else {
                    $this->Api_model->responseFailed(0, NO_TASK);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
        
    }
    function reminder_history()
    {
        $app_id  = $this->input->post('id');
        $user_id = $this->input->post('user_id');
        $pet_id  = $this->input->post('pet_id');
        $send    = false;
        $msg     = "No Task Found";
        if (isset($user_id) && $user_id != '' && isset($pet_id) && $pet_id != '') {
            $app_data = $this->Api_model->get_reminder_history('', $user_id, $pet_id)->result();
            $reminder = array();
            foreach ($app_data as $app_data) {
                $table              = 'pet';
                $condition          = array(
                    'user_id' => $user_id,
                    'id' => $pet_id
                );
                $check_pet          = $this->Api_model->getSingleRow($table, $condition);
                $app_data->pet_name = $check_pet->petName;
                array_push($reminder, $app_data);
                
            }
            if (!empty($app_data)) {
                $send = true;
            } else {
                $send = false;
                $msg  = "No Task found with this email";
            }
        } elseif (isset($user_id) && $user_id != '') {
            $app_data = $this->Api_model->get_reminder_history('', $user_id, '')->result();
            $reminder = array();
            
            foreach ($app_data as $app_data) {
                
                $table              = 'pet';
                $condition          = array(
                    'user_id' => $user_id,
                    'id' => $app_data->pet_id
                );
                $check_pet          = $this->Api_model->getSingleRow($table, $condition);
                $app_data->pet_name = $check_pet->petName;
                array_push($reminder, $app_data);
                
            }
            if (!empty($app_data)) {
                $send = true;
            } else {
                $send = false;
                $msg  = "No Task found with this email";
            }
        }
        
        if ($send) {
            $this->Api_model->responseSuccess(1, 'success', $reminder);
            // $arr = array('error' => 'false', 'message' => 'success','reminders'=> $app_data);
            // header('Content-Type: application/json');      
            // echo json_encode($arr);
        } else {
            $this->Api_model->responseFailed(0, $msg);
            // $arr = array('error' => 'true', 'message' => $msg);
            //  header('Content-Type: application/json');      
            //  echo json_encode($arr);
        }
        
        
    }
    /*check product*/
    public function checkProduct()
    {
        $product_id = $this->input->post('product_id', TRUE);
        $user_id    = $this->input->post('user_id', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $checkProduct = $this->Api_model->getSingleRow('product_basket', array(
                    'product_id' => $product_id,
                    'user_id' => $user_id
                ));
                
                if ($checkProduct) {
                    
                    //$app_data['quantity']= $checkProduct->quantity;
                    $this->Api_model->responseSuccess(1, SUCCESS, $checkProduct->quantity);
                } else {
                    $this->Api_model->responseFailed(0, FAILED);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
    }
    
    public function addMemories()
    {
        $pet_id       = $this->input->post('pet_id', TRUE);
        $user_id      = $this->input->post('user_id', TRUE);
        $pet_img_path = $this->input->post('pet_img_path', TRUE);
        $description  = $this->input->post('description', TRUE);
        $created      = $this->input->post('created', TRUE);
        
        
        
        if (!empty($user_id)) {
            $userStatus = $this->Api_model->getSingleRow('users', array(
                'id' => $user_id
            ));
            if ($userStatus) {
                if ($userStatus->status == 1) {
                    
                    $this->load->library('upload');
                    
                    $config['upload_path']   = './assets/images/';
                    $config['allowed_types'] = 'gif|jpg|jpeg|png';
                    $config['max_size']      = 10000;
                    $config['file_name']     = time();
                    $this->upload->initialize($config);
                    $petimage = "";
                    if ($this->upload->do_upload('pet_img_path')) {
                        $petimage = 'assets/images/' . $this->upload->data('file_name');
                    }
                    
                    if ($petimage) {
                        $petdata['pet_img_path'] = $petimage;
                    }
                    $petdata['user_id']     = $user_id;
                    $petdata['pet_id']      = $pet_id;
                    $petdata['description'] = isset($description) ? $description : "";
                    $petdata['created']     = time();
                    
                    $table = 'pet_memories';
                    $check = $this->Api_model->insert($table, $petdata);
                    if ($check) {
                        $this->Api_model->responseSuccessWithOutData(1, ADD_PET_MEMORIES);
                    } else {
                        $this->Api_model->responseFailed(0, MEMORY_NOT_ADDED);
                    }
                } else {
                    $this->Api_model->responseFailed(3, INACTIVATE);
                }
            } else {
                $this->Api_model->responseFailed(0, NOT_RESPONDING);
                
            }
        } else {
            $this->Api_model->responseFailed(0, SEND_USER_ID);
        }
    }
    public function addContact()
    {
        $pet_id        = $this->input->post('pet_id', TRUE);
        $user_id       = $this->input->post('user_id', TRUE);
        $user_id_owner = $this->input->post('user_id_owner', TRUE);
        $description   = $this->input->post('description', TRUE);
        $created       = $this->input->post('created', TRUE);
        $userStatus    = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                if ($user_id == $user_id_owner) {
                    $this->Api_model->responseFailed(0, MYPETMSG);
                    exit();
                }
                
                if (!empty($user_id)) {
                    $petdata['user_id']       = $user_id;
                    $petdata['pet_id']        = $pet_id;
                    $petdata['user_id_owner'] = $user_id_owner;
                    $petdata['description']   = isset($description) ? $description : "";
                    $petdata['created']       = time();
                    
                    $table       = 'contact';
                    $where       = array(
                        'user_id' => $user_id,
                        'pet_id' => $pet_id,
                        'user_id_owner' => $user_id_owner
                    );
                    $contactData = $this->Api_model->getSingleRow($table, $where);
                    if ($contactData) {
                        $this->Api_model->responseFailed(0, ADOPT);
                        exit();
                    } else {
                        $check = $this->Api_model->insert($table, $petdata);
                        if ($check) {
                            $getuser      = $this->Api_model->getSingleRow('users', array(
                                'id' => $user_id
                            ));
                            $getowner     = $this->Api_model->getSingleRow('users', array(
                                'id' => $user_id_owner
                            ));
                            $device_token = $getowner->device_token;
                            $ownername    = $getowner->first_name;
                            $username     = $getuser->first_name;
                            $email        = $getowner->email;
                            $title        = CONTACTTITLE;
                            $this->firebase_with_class($device_token, $user_id_owner, $username, $title, CONTACT_MSG . '.');
                            
                            $msg = '<p><b>Mr/Mrs. ' . $username . '</b>' . CONTACT_MSG . '</p>';
                            
                            $this->Api_model->send_email($email, $title, $msg);
                            
                            $this->Api_model->responseSuccessWithOutData(1, REQUEST_SUCCESS);
                        } else {
                            $this->Api_model->responseFailed(0, NOT_RESPONDING);
                        }
                    }
                } else {
                    $this->Api_model->responseFailed(0, SEND_USER_ID);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
    }
    
    public function getAllRequest()
    {
        $user_id    = $this->input->post('user_id', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $req = $this->Api_model->getAllDataWhere(array(
                    'user_id' => $user_id
                ), 'contact');
                
                if ($req) {
                    $requests = array();
                    
                    foreach ($req as $request) {
                        
                        $table     = 'pet';
                        $condition = array(
                            'user_id' => $request->user_id_owner,
                            'id' => $request->pet_id,
                            'deleted' => false
                        );
                        
                        $check_pet = $this->Api_model->getSingleRow($table, $condition);
                        
                        if ($check_pet) {
                            $user_owner               = $this->Api_model->getSingleRow('users', array(
                                'id' => $request->user_id_owner
                            ));
                            $user                     = $this->Api_model->getSingleRow('users', array(
                                'id' => $request->user_id
                            ));
                            $request->user_owner_name = $user_owner->first_name;
                            $request->user_name       = $user->first_name;
                            
                            $table                   = 'breed';
                            $condition               = array(
                                'id' => $check_pet->breed_id
                            );
                            $breed_pet               = $this->Api_model->getSingleRow($table, $condition);
                            $check_pet->breed_name   = $breed_pet->breed_name;
                            $check_pet->breed_target = $breed_pet->target;
                            
                            $data1 = $this->Api_model->get_manual_activity_bydate($check_pet->id, $request->user_id_owner, date('Y-m-d'));
                            
                            
                            if (empty($data1)) {
                                $check_pet->percent = 0;
                            } else {
                                $per                = ($data1[0]->manualactivity / $breed_pet->target) * 100;
                                $check_pet->percent = round($per, 0);
                            }
                            
                            $table                    = 'pet_type';
                            $condition                = array(
                                'id' => $check_pet->pet_type
                            );
                            $my_pet_type              = $this->Api_model->getSingleRow($table, $condition);
                            $check_pet->pet_type_name = $my_pet_type->pet_name;
                            
                            $check_pet->pet_img_path = $this->config->base_url() . $check_pet->pet_img_path;
                            
                            $data = $this->Api_model->get_manual_activity($check_pet->id, $request->user_id_owner);
                            
                            if (empty($data)) {
                                $check_pet->chart = $data;
                            } else {
                                $check_pet->chart = $data;
                            }
                            $request->petdata = $check_pet;
                            array_push($requests, $request);
                        }
                    }
                    $this->Api_model->responseSuccess(1, REQUEST, $req);
                } else {
                    $this->Api_model->responseFailed(0, NOREQUEST);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
        
    }
    
    public function getMemories()
    {
        $user_id    = $this->input->post('user_id', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $pets = $this->Api_model->getAllDataWhere(array(
                    'user_id' => $user_id
                ), 'pet_memories');
                
                if ($pets) {
                    $pet = array();
                    foreach ($pets as $pets) {
                        $pets->pet_img_path = $this->config->base_url() . $pets->pet_img_path;
                        array_push($pet, $pets);
                    }
                    $this->Api_model->responseSuccess(1, GET_PET_MEMORIES, $pet);
                } else {
                    $this->Api_model->responseFailed(0, MEMORY_NOT_AVAILABLE);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
        
    }
    public function getMemoriesByPet()
    {
        $user_id    = $this->input->post('user_id', TRUE);
        $pet_id     = $this->input->post('pet_id', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $pets = $this->Api_model->getAllDataWhere(array(
                    'user_id' => $user_id,
                    'pet_id' => $pet_id
                ), 'pet_memories');
                
                if ($pets) {
                    $pet = array();
                    foreach ($pets as $pets) {
                        
                        
                        $pets->pet_img_path = $this->config->base_url() . $pets->pet_img_path;
                        
                        array_push($pet, $pets);
                    }
                    
                    
                    $this->Api_model->responseSuccess(1, GET_PET_MEMORIES, $pet);
                } else {
                    $this->Api_model->responseFailed(0, NO_MEMORIES);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
        
    }
   
    public function getAllProductByCatName()
    {
        $user_id    = $this->input->post('user_id', TRUE);
        $cat_name = $this->input->post('cat_name', TRUE);

        $food_company = $this->Api_model->getAllDataWhere(array(
                    'status' => 1,
                    'c_name' => $cat_name
                ), 'food_company');
        $food_ids=array();
        foreach ($food_company as $food_company) 
        {
            $food_id=$food_company->c_id;
            array_push($food_ids, $food_id);
        }

        $country    = $this->input->post('country', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        
  
            if ($userStatus->status == 1) {

                    $products = $this->Api_model->getWhereInStatusFood('food_product',array(
                        'country' => $country,
                        'deleted' => 1
                    ),"c_id", $food_ids);
      
    
                if ($products) {
                    $product = array();
                    foreach ($products as $products) {
                        if ($products->color != "") {
                            $color = explode(',', $products->color);
                        } else {
                            $color = array();
                        }
                        if ($products->size != "") {
                            $size = explode(',', $products->size);
                        } else {
                            $size = array();
                        }
                        $products->color         = $color;
                        $discount                = $products->discount;
                        $discount_amount         = ($discount * $products->p_rate_sale) / 100;
                        $products->final_amount  = $products->p_rate_sale - $discount_amount;
                        $products->size          = $size;
                        $products->proImage      = $this->Api_model->getAllDataWhereConcat(array(
                            'p_id' => $products->p_id
                        ), 'pro_images');
                        $currency_setting        = $this->Api_model->getSingleRow('currency_setting', array(
                            'status' => 1
                        ));

                        $get_brand               = $this->Api_model->getSingleRow('food_company', array(
                            'c_id' => $products->c_id
                        ));
                        

                       $products->p_type        = $products->p_type;
                       $products->p_pet_type    = $products->p_pet_type;
                        $products->c_name        = $get_brand->c_name;
                        $products->currency_type = $currency_setting->currency_symbol;
                        $products->img_path      = $products->proImage[0]->image;
                        
                        $data = $this->Api_model->getAllDataWhere(array(
                            'product_id' => $products->p_id
                        ), 'review_product');
                        if (empty($data)) {
                            $products->reviewlist = $data;
                        } else {
                            $datas = array();
                            foreach ($data as $data) {
                                $table           = 'users';
                                $condition       = array(
                                    'id' => $data->user_id
                                );
                                $users           = $this->Api_model->getSingleRow($table, $condition);
                                $data->user_name = $users->first_name;
                                array_push($datas, $data);
                            }
                            $products->reviewlist = $datas;
                        }
                        array_push($product, $products);
                    }
                    $this->Api_model->responseSuccess(1, ALL_PRODUCTS, $product);
                } 
            } 
            else {
                    $this->Api_model->responseFailed(0, NOT_RESPONDING);
                }
    }
    
    /*Get All Products*/
    public function getAllProduct()
    {
        $user_id    = $this->input->post('user_id', TRUE);
        $p_pet_type = $this->input->post('p_pet_type', TRUE);
        $p_type     = $this->input->post('p_type', TRUE);
        $brand_id   = $this->input->post('c_id', TRUE);
        
        $country    = $this->input->post('country', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        
        if ($userStatus) {
            if ($userStatus->status == 1) {
                if ($p_pet_type && $p_type) {
                    $products = $this->Api_model->getAllDataWhereProducts(array(
                        'p_pet_type' => $p_pet_type,
                        'p_type' => $p_type,
                        'c_id' => $brand_id,
                        'country' => $country,
                        'deleted' => 1
                    ), 'food_product');
                } else {
                    $products = $this->Api_model->getAllDataWhereProducts(array(
                        'country' => $country,
                        'p_pet_type' => $p_pet_type,
                        'c_id' => $brand_id,
                        'deleted' => 1
                    ), 'food_product');
                }
                if ($products) {
                    $product = array();
                    foreach ($products as $products) {
                        if ($products->color != "") {
                            $color = explode(',', $products->color);
                        } else {
                            $color = array();
                        }
                        if ($products->size != "") {
                            $size = explode(',', $products->size);
                        } else {
                            $size = array();
                        }
                        $products->color         = $color;
                        $discount                = $products->discount;
                        $discount_amount         = ($discount * $products->p_rate_sale) / 100;
                        $products->final_amount  = $products->p_rate_sale - $discount_amount;
                        $products->size          = $size;
                        $products->proImage      = $this->Api_model->getAllDataWhereConcat(array(
                            'p_id' => $products->p_id
                        ), 'pro_images');
                        $currency_setting        = $this->Api_model->getSingleRow('currency_setting', array(
                            'status' => 1
                        ));
                        // $get_cat                 = $this->Api_model->getSingleRow('pet_type', array(
                        //     'id' => $products->p_type
                        // ));
                        // $get_sub_cat             = $this->Api_model->getSingleRow('pro_cat', array(
                        //     'id' => $products->p_pet_type
                        // ));
                        $get_brand               = $this->Api_model->getSingleRow('food_company', array(
                            'c_id' => $products->c_id
                        ));
                        

                       $products->p_type        = $products->p_type;
                       $products->p_pet_type    = $products->p_pet_type;

                        // $products->p_type        = $get_cat->pet_name;
                        // $products->p_pet_type    = $get_sub_cat->cat_title;
                        $products->c_name        = $get_brand->c_name;
                        $products->currency_type = $currency_setting->currency_symbol;
                        //$products->img_path      = $products->proImage[0]->image;
                        
                        if($products->proImage)
                        {
                       $products->img_path      = $products->proImage[0]->image;

                       }else
                        {
                    $products->img_path=""; 
                      }


                        $data = $this->Api_model->getAllDataWhere(array(
                            'product_id' => $products->p_id
                        ), 'review_product');
                        if (empty($data)) {
                            $products->reviewlist = $data;
                        } else {
                            $datas = array();
                            foreach ($data as $data) {
                                $table           = 'users';
                                $condition       = array(
                                    'id' => $data->user_id
                                );
                                $users           = $this->Api_model->getSingleRow($table, $condition);
                                $data->user_name = $users->first_name;
                                array_push($datas, $data);
                            }
                            $products->reviewlist = $datas;
                        }
                        array_push($product, $products);
                    }
                    $this->Api_model->responseSuccess(1, ALL_PRODUCTS, $product);
                } else {
                    $this->Api_model->responseFailed(0, NOT_RESPONDING);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
        }
    }
    

    public function getSingleProducts()
    {
        $user_id    = $this->input->post('user_id', TRUE);
        $p_pet_type = $this->input->post('p_pet_type', TRUE);
        $p_type     = $this->input->post('p_type', TRUE);
        $brand_id   = $this->input->post('c_id', TRUE);
        $p_id       = $this->input->post('p_id', TRUE);
        $p_name     = $this->input->post('p_name', TRUE);
        $country    = $this->input->post('country', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        
        if ($userStatus) {
           // print_r($userStatus);
            if ($userStatus->status == 1) {
                if ($p_pet_type && $p_type) {
                    $products = $this->Api_model->getSingleRow('food_product', array(
                        'p_id' => $p_id,
                        'p_pet_type' => $p_pet_type,
                        'p_type' => $p_type,
                        'c_id' => $brand_id,
                        'country' => $country,
                        'deleted' => 1
                    ));
                } else {
                    $products = $this->Api_model->getSingleRow('food_product', array(
                        'p_id' => $p_id,
                        'country' => $country,
                        'p_pet_type' => $p_pet_type,
                        'c_id' => $brand_id,
                        'deleted' => 1
                    ));
                }
               // print_r($products);
                if ($products) {
                    if ($products->color != "") {
                        $color = explode(',', $products->color);
                    } else {
                        $color = array();
                    }
                    if ($products->size != "") {
                        $size = explode(',', $products->size);
                    } else {
                        $size = array();
                    }
                    $products->color         = $color;
                    $discount                = $products->discount;
                    $discount_amount         = ($discount * $products->p_rate_sale) / 100;
                    $products->final_amount  = $products->p_rate_sale - $discount_amount;
                    $products->size          = $size;
                    $products->proImage      = $this->Api_model->getAllDataWhereConcat(array(
                        'p_id' => $products->p_id
                    ), 'pro_images');
                    $currency_setting        = $this->Api_model->getSingleRow('currency_setting', array(
                        'status' => 1
                    ));
                    $get_cat                 = $this->Api_model->getSingleRow('pet_type', array(
                        'id' => $products->p_type
                    ));
                    $get_sub_cat             = $this->Api_model->getSingleRow('pro_cat', array(
                        'id' => $products->p_pet_type
                    ));
                    $get_brand               = $this->Api_model->getSingleRow('food_company', array(
                        'c_id' => $products->c_id
                    ));
                    $products->p_type        = $get_cat->pet_name;
                    $products->p_pet_type    = $get_sub_cat->cat_title;
                    $products->c_name        = $get_brand->c_name;
                    $products->currency_type = $currency_setting->currency_symbol;
                    $products->img_path      = $products->proImage[0]->image;
                    
                    $data = $this->Api_model->getAllDataWhere(array(
                        'product_id' => $products->p_id
                    ), 'review_product');
                    if (empty($data)) {
                        $products->reviewlist = $data;
                    } else {
                        $datas = array();
                        foreach ($data as $data) {
                            $table           = 'users';
                            $condition       = array(
                                'id' => $data->user_id
                            );
                            $users           = $this->Api_model->getSingleRow($table, $condition);
                            $data->user_name = $users->first_name;
                            array_push($datas, $data);
                        }
                        $products->reviewlist = $datas;
                    }
                    $productDeatilv = array();
                    if ($p_pet_type && $p_type) {
                        $productDeatil = $this->Api_model->getAllDataWhere(array(
                            'p_name' => $p_name,
                            'p_pet_type' => $p_pet_type,
                            'p_type' => $p_type,
                            'c_id' => $brand_id,
                            'country' => $country,
                            'deleted' => 1
                        ), 'food_product');
                    } else {
                        $productDeatil = $this->Api_model->getAllDataWhere(array(
                            'p_name' => $p_name,
                            'country' => $country,
                            'p_pet_type' => $p_pet_type,
                            'c_id' => $brand_id,
                            'deleted' => 1
                        ), 'food_product');
                    }
                    if ($productDeatil) {
                        foreach ($productDeatil as $productDeatil) {
                            if ($productDeatil->color != "") {
                                $color = explode(',', $productDeatil->color);
                            } else {
                                $color = array();
                            }
                            $productsImage                   = $this->Api_model->getAllDataWhereConcat(array(
                                'p_id' => $productDeatil->p_id
                            ), 'pro_images');
                            $productDeatils['img_path']      = $productsImage[0]->image;
                            $discount                        = $productDeatil->discount;
                            $discount_amount                 = ($discount * $productDeatil->p_rate_sale) / 100;
                            $productDeatils['final_amount']  = $productDeatil->p_rate_sale - $discount_amount;
                            $productDeatils['size']          = $productDeatil->size;
                            $productDeatils['weight']        = $productDeatil->weight;
                            $productDeatils['p_id']          = $productDeatil->p_id;
                            $productDeatils['mandatory']     = $productDeatil->mandatory;
                            $productDeatils['color']         = $color;
                            $productDeatils['p_rate']        = $productDeatil->p_rate;
                            $productDeatils['p_rate_sale']   = $productDeatil->p_rate_sale;
                            $productDeatils['quantity']      = $productDeatil->quantity;
                            $productDeatils['net_amt']       = $productDeatil->net_amt;
                            $productDeatils['shipping_cost'] = $productDeatil->shipping_cost;
                            $productDeatils['discount']      = $productDeatil->discount;
                            array_push($productDeatilv, $productDeatils);
                        }
                        $products->product_deatils = $productDeatilv;
                    }
                    $this->Api_model->responseSuccess(1, ALL_PRODUCTS, $products);
                } else {
                    $this->Api_model->responseFailed(0, NOT_RESPONDING);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
        }
    }
    
    /*Add to Cart*/
    public function addTocart()
    {
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $this->input->post('user_id', TRUE)
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $data['user_id']    = $this->input->post('user_id', TRUE);
                $data['product_id'] = $this->input->post('product_id', TRUE);
                $data['quantity']   = $this->input->post('quantity', TRUE);
                $data['color']      = $this->input->post('color', TRUE);
                $data['size']       = $this->input->post('size', TRUE);
                $weight             = $this->input->post('weight', TRUE);
                $mandatory          = $this->input->post('mandatory', TRUE);
                if (isset($weight)) {
                    $data['weight'] = $weight;
                }
                if (isset($mandatory)) {
                    $data['mandatory'] = $mandatory;
                }
                
                $data['shipping_cost'] = $this->input->post('shipping_cost', TRUE);
                $data['created_at']    = time();
                $data['updated_at']    = time();
                $getId                 = $this->Api_model->insertGetId('product_basket', $data);
                
                if ($getId) {
                    $this->Api_model->responseFailed(1, CART_UPDATE);
                } else {
                    $this->Api_model->responseFailed(0, NOT_RESPONDING);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
    }
    
    /*Get My Cart*/
    public function getMyCart()
    {
        $user_id    = $this->input->post('user_id', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $get_cart = $this->Api_model->getAllDataWhere(array(
                    'user_id' => $user_id
                ), 'product_basket');
                
                if ($get_cart) {
                    $get_carts = array();
                    foreach ($get_cart as $get_cart) {
                        $product_id                    = $get_cart->product_id;
                        $product                       = $this->Api_model->getSingleRow('food_product', array(
                            'p_id' => $product_id
                        ));
                        $pro_images                    = $this->Api_model->getSingleRow('pro_images', array(
                            'p_id' => $product_id
                        ));
                        $currency_setting              = $this->Api_model->getSingleRow('currency_setting', array(
                            'status' => 1
                        ));
                      $get_cart->food_product_quantity =$product->quantity;                 
                        $quantity                      = $get_cart->quantity;
                        $price                         = $product->p_rate_sale;
                        $price_dicount                 = $price - ($price * $product->discount) / 100;
                        $get_cart->currency_type       = $currency_setting->currency_symbol;
                        $get_cart->p_name              = $product->p_name;
                        $get_cart->p_description       = $product->p_description;
                        $get_cart->p_rate              = $product->p_rate_sale;
                        $get_cart->discount            = $product->discount;
                        $get_cart->price_dicount       = $price_dicount;
                        $get_cart->img_path            = $this->config->base_url() . $pro_images->image;
                        $get_cart->product_total_price = $price_dicount * $quantity;
                        $get_cart->shipping_cost       = $product->shipping_cost;
                        
                        array_push($get_carts, $get_cart);
                    }
                    
                    $this->Api_model->responseSuccess(1, GET_CART, $get_carts);
                    
                } else {
                    
                    $this->Api_model->responseFailed(0, CART_EMPTY);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
        
        
    }
    
    /*Get All Pet Type*/
    public function getAllPetType()
    {
        $user_id    = $this->input->post('user_id', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $get_pet_type = $this->Api_model->getAllData('pet_type');
                
                if ($get_pet_type) {
                    $get_pet_types = array();
                    foreach ($get_pet_type as $get_pet_type) {
                        
                        $get_pet_type->pet_image = $this->config->base_url() . $get_pet_type->pet_image;
                        
                        array_push($get_pet_types, $get_pet_type);
                    }
                    
                    $this->Api_model->responseSuccess(1, ALL_PET, $get_pet_types);
                } else {
                    $this->Api_model->responseFailed(0, NOT_RESPONDING);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
    }
    
    /*update Cart Quantity*/
    public function updateCartQuantity()
    {
        $product_id = $this->input->post('product_id', TRUE);
        $quantity   = $this->input->post('quantity', TRUE);
        $user_id    = $this->input->post('user_id', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                
                $table     = 'product_basket';
                $condition = array(
                    'product_id' => $product_id,
                    'user_id' => $user_id
                );
                
                $check_basket = $this->Api_model->updateSingleRow('product_basket', array(
                    'product_id' => $product_id,'user_id' => $user_id
                ), array(
                    'quantity' => $quantity
                ));
                
                if ($check_basket) {
                    $this->Api_model->responseFailed(1, CART_UPDATE);
                } else {
                    $this->Api_model->responseFailed(0, NOT_RESPONDING);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
    }
    
    /*Remove Product from Cart*/
    public function remove_product_cart()
    {
        $product_id = $this->input->post('product_id', TRUE);
        $user_id    = $this->input->post('user_id', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $this->Api_model->deleteRecord(array(
                    'product_id' => $product_id,
                    'user_id' => $user_id
                ), 'product_basket');
                
                $this->Api_model->responseFailed(1, REMOVE_CART);
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
    }
    
    
    public function order()
    {
        $user_id      = $this->input->post('user_id', TRUE);
        $order_id     = $this->input->post('order_id', TRUE);
        $payment_id   = $this->input->post('payment_id', TRUE);
        $address      = $this->input->post('address', TRUE);
        $landMark     = $this->input->post('landMark');
        $name         = $this->input->post('name', TRUE);
        $zip          = $this->input->post('zip');
        $country      = $this->input->post('country');
        $country_code = $this->input->post('country_code');
        $email        = $this->input->post('email', TRUE);
        $city         = $this->input->post('city');
        $mobile_no    = $this->input->post('mobile_no');
        $payment_status  = $this->input->post('payment_status');
        
        $userStatus = $this->Api_model->getSingleRow('users', array('id' => $user_id));   
          if ($userStatus) {
            if ($userStatus->status == 1) {
	        $orderStatus=$this->Api_model->getSingleRow('orders', array('order_id' => $order_id ));
	          	if($orderStatus){   
	            	if($orderStatus->order_id==$order_id)
	            	{
		           		$payment_status="1";
		                $table='orders';
		                $delete = $this->Api_model->updateSingleRow($table, array(
		                        'order_id' => $order_id
		                    ), array('payment_status' => $payment_status));
                         $order_product_id = $this->Api_model->getSingleRow('order_products', array(
                                'order_id' => $order_id
                            ));
                            $pid= $order_product_id->product_id;
                          
                            $product = $this->Api_model->getSingleRow('food_product', array(
                                'p_id' => $pid
                            ));
                              
                               $quantity=$order_product_id->quantity;             
                            $quantityLess=$product->quantity-$quantity;
                          
                            $this->Api_model->updateSingleRow('food_product', array(
                                'p_id' => $pid
                            ), array('quantity' => $quantityLess));
                          
                            $msg1 = "Your order " . $order_id . " has been placed.";
                              $this->firebase_with_class($userStatus->device_tokan, $user_id, $userStatus->first_name, "Order Plaeced", $msg1);
		                $this->Api_model->responseSuccess(1, MAKE_ORDER, $orderStatus);
                        
		                exit();
	   				}
	   			}else{
	                    $payment_status="0";
	                } 
	                $get_cart = $this->Api_model->getAllDataWhere(array(
	                    'user_id' => $user_id
	                ), 'product_basket');
	                
	                if ($get_cart) {
	                    $current_time = time() . $user_id;
	                    $get_carts    = array();
	                    foreach ($get_cart as $get_cart) {
	                        $product_id = $get_cart->product_id;
	                        $product    = $this->Api_model->getSingleRow('food_product', array(
	                            'p_id' => $product_id
	                        ));
                            $quantity = $get_cart->quantity;
                           
	                        $price    = $product->p_rate_sale;
	                          
	                        $price_discount         = $price - ($price * $product->discount / 100);
	                        $data['p_name']         = $product->p_name;
	                        $data['quantity']       = $quantity;
	                        $data['payment_status'] = $payment_status;
	                        $data['user_id']        = $user_id;
	                        $data['product_id']     = $product_id;
	                        $data['p_description']  = $product->p_description;
	                        $data['p_rate']         = $price;
	                        $data['discount']       = $product->discount;
	                        $data['discount_total'] = ($product->p_rate_sale * $product->discount) / 100;
	                        $data['price_discount'] = round($price_discount, 2);
	                        $data['img_path']       = $product->img_path;
	                        $data['order_id']       = $order_id;
	                        $data['updated_at']     = time();
	                        $data['created_at']     = time();
	                        $data['total_price']    = round($price_discount * $quantity, 2);
	                        $data['color']          = $get_cart->color;
	                        $data['size']           = $get_cart->size;
	                        $data['weight']         = $get_cart->weight;
	                        
	                        if($payment_id){
	                            $data['payment_id']     = $payment_id;
	                        }else{
	                            $data['payment_id']     = "";
	                        }

	                        $this->Api_model->insertGetId('order_products', $data);
	                        
	                        $dataInvoice['user_id']    = $user_id;
	                        $dataInvoice['amount']     = round($price_discount * $quantity, 2);
	                        $dataInvoice['order_id']   = $order_id;
	                        
	                        $dataInvoice['created_at'] = time();
	                        $this->Api_model->insertGetId('product_invoice', $dataInvoice);
	                   } 
                         if($payment_id){
	                            $dataInvoice['txn_id']     = $payment_id;
	                        }else{
	                            $dataInvoice['txn_id']     = "";    
	                        }
	                    $get_total_price = $this->Api_model->getSumWithWhere('total_price', 'order_products', array(
	                        'order_id' => $order_id
	                    ));
	                    $get_total_price_Old       = $this->Api_model->getSumWithWhere('p_rate', 'order_products', array(
	                        'order_id' => $order_id
	                    ));
	                    $get_discount              = $this->Api_model->getSumWithWhere('discount', 'order_products', array(
	                        'order_id' => $order_id
	                    ));
	                    $get_discount_total        = $this->Api_model->getSumWithWhere('discount_total', 'order_products', array(
	                        'order_id' => $order_id 
	                    ));
	                    // $payment_status = $this->Api_model->getSingleRow('order',array(
	                    //     'order_id' => $payment_status
	                    // ));
	                    //print_r($payment_status);
                        // print_r($get_total_price->total_price);
                         

	                    $dataorder['payment_status'] = $payment_status;
	                    $dataorder['cod_charges']  = $this->input->post('shipping_cost');
	                    $dataorder['order_id']     = $order_id;
	                    $dataorder['total_price']  = round($get_total_price->total_price, 2);
	                    $dataorder['discount']     = $get_discount_total->discount_total;
	                    $dataorder['user_id']      = $user_id;
	                    $dataorder['final_price']  = round($get_total_price->total_price + $dataorder['cod_charges'], 2);
	                    $dataorder['order_date']   = date('d-m-y h:i A');
	                    $dataorder['address']      = $address;
	                    if($landMark){
                                $dataorder['landMark']     = $landMark;
                            }
                            // else{
                            //     $dataorder['landMark']     = "";
                            // }
	                   if($zip){
                                $dataorder['zip']     = $zip;
                            }
                            // else{
                            //     $dataorder['zip']     = "";
                            // }
	                    $dataorder['email']        = $email;
                        if($userStatus->city){
                                $dataorder['city']     = $userStatus->city;
                            }
                            // else{
                            //     $dataorder['city']     = "";
                            // }
	                    if($country){
                                $dataorder['country']     = $country;
                             }
                             //    else{
                             //    $dataorder['country']     = "";
                             // }
	                    if($country_code){
                                $dataorder['country_code']     = $country_code;
                            }
                            // else{
                            //     $dataorder['country_code']     = "";
                            // }
	                    $dataorder['name']         = $name;
	                    // $dataorder['mobile_no']    = $mobile_no;
                         if($mobile_no){
                                $dataorder['mobile_no']     = $mobile_no;
                            }
	                    $dataorder['updated_at']   = time();
	                    $dataorder['created_at']   = time();
	                    $getID                     = $this->Api_model->insertGetId('orders', $dataorder);
	                    $dataorder['address']      = $address;
	                    $dataorder1['landMark']    = $landMark;
                          // if($landMark){
                          //       $dataorder['landMark']     = $landMark;
                          //   }else{
                          //       $dataorder['landMark']     = "";
                          //   }
                          //    if($zip){
                          //       $dataorder['zip']     = $zip;
                          //   }else{
                          //       $dataorder['zip']     = "";
                          //   }
                          //     if($city){
                          //       $dataorder['city']     = $city;
                          //   }else{
                          //       $dataorder['city']     = "";
                          //   }
	                     $dataorder1['zip']         = $zip;
	                    $dataorder1['city']        = $city;
	                    $dataorder1['user_id']     = $user_id;
	                    $dataorder1['country']     = ucfirst($country);
                         // if($country_code){
                         //        $dataorder['country_code']     = $country_code;
                         //    }else{
                         //        $dataorder['country_code']     = "";
                         //    }
	                    $dataorder1['address']     = $address;
	                    
	                    $getUserAddress = $this->Api_model->getSingleRow('addressfill', array(
	                        'address' => $address,
	                        'user_id' => $user_id
	                    ));
	                    if (empty($getUserAddress)) {
	                        $this->Api_model->insertGetId('addressfill', $dataorder1);
	                    }
	                    
	                    $getOrder                = $this->Api_model->getSingleRow('orders', array(
	                        'id' => $getID
	                    ));
	                    $order_status_data       = array(
	                        'order_id' => $getOrder->order_id,
	                        'od_pending_status' => 6,
	                        'od_status' => 6,
	                        'od_pending_date' => date('Y-m-d')
	                    );
	                    $order_status_id         = $this->Api_model->insertGetId('order_status', $order_status_data);
	                    $currency_setting        = $this->Api_model->getSingleRow('currency_setting', array(
	                        'status' => 1
	                    ));
	                    $getOrder->currency_type = $currency_setting->currency_symbol;
	                    // $msg1                    = "Your order " . $current_time . " has been placed.";
	                    // $this->firebase_with_class($userStatus->device_tokan, $user_id, $userStatus->first_name, "Order Plaeced", $msg1);
	                    $this->Api_model->deleteRecord(array(
	                        'user_id' => $user_id
	                    ), 'product_basket');
	                    $this->Api_model->send_email_order($getOrder->email, ORDER, $getOrder->order_id);
	                    
	                    $this->Api_model->responseSuccess(1, MAKE_ORDER, $getOrder);
	                } 

	                else {
	                    $this->Api_model->responseFailed(0, NO_PRODUCT);
	                }

             
            }else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
        }
    }
    
    
    public function get_order_status()
    {
        $user_id    = $this->input->post('user_id', TRUE);
        $order_id   = $this->input->post('order_id', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $order_status = $this->Api_model->getSingleRow('order_status', array(
                    'order_id' => $order_id
                ));
                
                if ($order_status) {
                    $this->Api_model->responseSuccess(1, ALL_PET, $order_status);
                } else {
                    $this->Api_model->responseFailed(0, NOT_RESPONDING);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
    }
    
    
    /*Added By GURU JI*/
    /*Added By VARUN*/
    
    public function set_manual_activity()
    {
        $pet_id         = $this->input->post('pet_id');
        $user_id        = $this->input->post('user_id');
        $time_stamp     = $this->input->post('time_stamp');
        $manualActivity = $this->input->post('manualActivity');
        $unit           = $this->input->post('unit');
        $target         = $this->input->post('target');
        $userStatus     = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $remendar = $time_stamp / 1000;
                
                $rem = (explode(".", $remendar));
                $dt  = new DateTime("@$rem[0]"); // convert UNIX timestamp to PHP DateTime
                
                $date        = $dt->format('Y-m-d');
                $data        = array(
                    'pet_id' => $pet_id,
                    'user_id' => $user_id,
                    'time_stamp' => $time_stamp,
                    'manualactivity' => $manualActivity,
                    'unit' => $unit,
                    'date' => $date,
                    'target' => $target
                );
                $return_data = array(
                    'pet_id' => $pet_id,
                    'user_id' => $user_id,
                    'time_stamp' => $time_stamp,
                    'manualActivity' => $manualActivity,
                    'unit' => $unit,
                    'date' => $date,
                    'target' => $target
                );
                $data1       = $this->Api_model->get_manual_activity_bydate($pet_id, $user_id, $date);
                if ($data1) {
                    $this->Api_model->update_manual_activity($pet_id, $user_id, $date, $data);
                    $this->Api_model->responseSuccess(1, MANUAL_UPDATED, $return_data);
                } else {
                    $this->Api_model->set_manual_activity($data);
                    $this->Api_model->responseSuccess(1, MANUAL_INSERTED, $return_data);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
    }
    
    public function get_manual_activity()
    {
        
        $pet_id     = $this->input->post('pet_id');
        $user_id    = $this->input->post('user_id');
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $data = $this->Api_model->get_manual_activity($pet_id, $user_id);
                
                if ($data) {
                    $this->Api_model->responseSuccess(1, ACTIVITY_SEND, $data);
                } else {
                    $this->Api_model->responseFailed(0, ACTIVITY_SEND);
                }
                
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
        
    }
    
    
    public function getAllPets()
    {
        $user_id    = $this->input->post('user_id', TRUE);
        $lati       = $this->input->post('lati', TRUE);
        $longi      = $this->input->post('longi', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $pets = $this->Api_model->getAllDataWhere(array(
                    "user_id !=" => $user_id,
                    'deleted' => false
                ), 'pet');
                
                if ($pets) {
                    $pet = array();
                    foreach ($pets as $pets) {
                        
                        if ($pets->public_private != 1) {
                            $distance = $this->Api_model->distance($lati, $longi, $pets->lati, $pets->longi);
                            $distance = round($distance, 2);
                            if ($distance < 100) {
                                $this->checkFollow($pets, $pets->id, $user_id);
                                $table              = 'breed';
                                $condition          = array(
                                    'id' => $pets->breed_id
                                );
                                $check_pet          = $this->Api_model->getSingleRow($table, $condition);
                                $pets->breed_name   = $check_pet->breed_name;
                                $pets->breed_name   = $check_pet->breed_name;
                                $pets->distance     = $distance;
                                $pets->breed_target = $check_pet->target;
                                $data1              = $this->Api_model->get_manual_activity_bydate($pets->id, $pets->user_id, date('Y-m-d'));
                                
                                if (empty($data1)) {
                                    $pets->percent = 0;
                                } else {
                                    $per           = ($data1[0]->manualactivity / $check_pet->target) * 100;
                                    $pets->percent = round($per, 0);
                                }
                                
                                $table               = 'pet_type';
                                $condition           = array(
                                    'id' => $check_pet->pet_type
                                );
                                $my_pet_type         = $this->Api_model->getSingleRow($table, $condition);
                                $pets->pet_type_name = $my_pet_type->pet_name;
                                
                                $pets->pet_img_path = $this->config->base_url() . $pets->pet_img_path;
                                
                                $data = $this->Api_model->get_manual_activity($pets->id, $pets->user_id);
                                
                                if (empty($data)) {
                                    $pets->chart = $data;
                                } else {
                                    $pets->chart = $data;
                                }
                                array_push($pet, $pets);
                            }
                        }
                    }
                    $this->Api_model->responseSuccess(1, ALL_MY_PET, $pet);
                } else {
                    $this->Api_model->responseFailed(0, NOT_RESPONDING);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
    }
    
    public function getMyOrder()
    {
        $user_id    = $this->input->post('user_id', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
               
               $get_order = $this->Api_model->getAllDataWhereOrderBy(array(
                    "user_id =" => $user_id
                ), 'orders');

                  if (empty($get_order)) {
                    $this->Api_model->responseFailed(0, GET_ORDERS_FAIL);
                } else {
                    
                    $order = array();
                   foreach ($get_order as $get_order) {
                        $currency_setting         = $this->Api_model->getSingleRow('currency_setting', array(
                            'status' => 1
                        ));

              
               //  $order1 =$this->Api_model->getAllDataWhere(array("order_id" => $get_order->order_id),'order_products');
               // if ($order1) {
               //      $get_order->order1=$order1;
               //  }else{
               //       $get_order->order="";   
               //  }
                         

                        $get_order->currency_type = $currency_setting->currency_symbol;
                        if ($get_order->status == 1) {
                            $get_order->status_txt = "Confrimed";
                            
                        }
                        if ($get_order->status == 6) {
                            $get_order->status_txt = PENDING;
                            
                        }
                        if ($get_order->status == 2) {
                            $get_order->status_txt = DELIVEREDMSG;
                        }
                        if ($get_order->status == 3) {
                            $get_order->status_txt = PACKEDMSG;
                        }
                        if ($get_order->status == 4) {
                            $get_order->status_txt = DISMSG;
                        }
                        if ($get_order->status == 5) {
                            $get_order->status_txt = CANCELLEDMSG;
                        }
                        
                        array_push($order, $get_order);
                    }
                    
                    $this->Api_model->responseSuccess(1, GET_ORDERS, $order);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
    }
    public function getMyOrderDetails()
    {
        $user_id    = $this->input->post('user_id', TRUE);
        $order_id   = $this->input->post('order_id', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                
                $get_order = $this->Api_model->getAllDataWhere(array(
                    "user_id =" => $user_id,
                    "order_id =" => $order_id
                ), 'order_products');
                
                if (empty($get_order)) {
                    $this->Api_model->responseFailed(0, GET_ORDERS_FAIL);
                } else {
                    $order = array();
                    
                    foreach ($get_order as $get_order) {
                        $currency_setting         = $this->Api_model->getSingleRow('currency_setting', array(
                            'status' => 1
                        ));
             
                        $get_order->currency_type = $currency_setting->currency_symbol;


            $dattt = $this->Api_model->getAllDataWhereConcat(array( 'p_id' => $get_order->product_id ), 'pro_images');

            foreach ($dattt as $dattt) {
             
                 if($dattt)
                {
                $get_order->img_path  =  $dattt->image;
            
               }else
               {
               $get_order->img_path=""; 
                   
            }}      

                        array_push($order, $get_order);
                        
                    }
                    
                    $this->Api_model->responseSuccess(1, GET_ORDERS, $order);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
    }
    
    public function getFilterItem()
    {
        $data['breeds']   = $this->Api_model->getAllDataColumn('breed', 'breed_name,id', 'breed_name != ""');
        $data['pet_type'] = $this->Api_model->getAllDataColumn('pet_type', 'pet_name,id', 'pet_name != ""');
        $data['city']     = $this->Api_model->getAllDataColumn('pet', 'city', 'city != ""');
        $data['state']    = $this->Api_model->getAllDataColumn('pet', 'state', 'state != ""');
        $data['country']  = $this->Api_model->getAllDataColumn('pet', 'country', 'country != ""');
        $this->Api_model->responseSuccess(1, RECORD_FILTER, $data);
    }
    
    public function Filter()
    {
        $Data    = json_decode(file_get_contents('php://input'), true);
        $user_id = $Data['user_id'];
        // array_pop($Data);
        unset($Data['user_id']);
        $pets       = $this->Api_model->getFilterData('pet', $Data);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                if ($pets) {
                    $pet = array();
                    foreach ($pets as $pets) {
                        
                        if ($pets->public_private == 1) {
                            
                        } else {
                            $this->checkFollow($pets, $pets->id, $user_id);
                            $table              = 'breed';
                            $condition          = array(
                                'id' => $pets->breed_id
                            );
                            $check_pet          = $this->Api_model->getSingleRow($table, $condition);
                            $pets->breed_name   = $check_pet->breed_name;
                            $pets->breed_target = $check_pet->target;
                            
                            $data1 = $this->Api_model->get_manual_activity_bydate($pets->id, $pets->user_id, date('Y-m-d'));
                            
                            if (empty($data1)) {
                                $pets->percent = 0;
                            } else {
                                $per           = ($data1[0]->manualactivity / $check_pet->target) * 100;
                                $pets->percent = round($per, 0);
                            }
                            
                            $table               = 'pet_type';
                            $condition           = array(
                                'id' => $check_pet->pet_type
                            );
                            $my_pet_type         = $this->Api_model->getSingleRow($table, $condition);
                            $pets->pet_type_name = $my_pet_type->pet_name;
                            
                            $pets->pet_img_path = $this->config->base_url() . $pets->pet_img_path;
                            
                            $data = $this->Api_model->get_manual_activity($pets->id, $pets->user_id);
                            
                            if (empty($data)) {
                                $pets->chart = $data;
                            } else {
                                $pets->chart = $data;
                            }
                            array_push($pet, $pets);
                        }
                    }
                    $this->Api_model->responseSuccess(1, ALL_MY_PET, $pet);
                } else {
                    $this->Api_model->responseFailed(0, NO_RECORD);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
    }
    
    public function get_post()
    {
        $limit       = $this->input->post('limit', TRUE);
        $offset      = $this->input->post('offset', TRUE);
        $user_id     = $this->input->post('user_id', TRUE);
        $un_user_id  = $user_id;
        $comunity_id = $this->input->post('comunity_id', TRUE);
        $country     = $this->input->post('country', TRUE);
        $get_post    = array();
        $userStatus  = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                if ($country) {
                    $userData = $this->Comman_model->getUserByCounrty($country);
                    
                    if ($userData) {
                        $userId = array();
                        foreach ($userData as $user) {
                            $user_id = $user->id;
                            array_push($userId, $user_id);
                        }
                        $condition = array(
                            'flag' => '1',
                            'comunity_id' => $comunity_id
                        );
                        $get_post  = $this->Comman_model->get_post_by_country($limit, $offset, $condition, $userId);
                        
                        if ($get_post) {
                            $data = array();
                            foreach ($get_post as $get_post) {
                                
                                
                                $postID      = $get_post['postID'];
                                $user_id     = $get_post['user_id'];
                                $contentType = 'post';
                                
                                if ($get_post['media']) {
                                    $get_post['media'] = $this->config->base_url() . $get_post['media'];
                                }
                                $check_like = $this->Api_model->getSingleRow("likes", array(
                                    'user_id' => $un_user_id,
                                    'postID' => $postID
                                ));
                                
                                if ($check_like) {
                                    $get_post['is_like'] = 'true';
                                } else {
                                    $get_post['is_like'] = 'false';
                                }
                                
                                $check_abuse = $this->Api_model->get_abuse($user_id, $postID);
                                if ($check_abuse) {
                                    $get_post['is_abuse'] = 'true';
                                } else {
                                    $get_post['is_abuse'] = 'false';
                                }
                                $check_user = $this->Api_model->get_user($user_id);
                                if ($check_user->profile_pic) {
                                    $get_post['user_image'] = $this->config->base_url() . $check_user->profile_pic;
                                } else {
                                    $get_post['user_image'] = $this->config->base_url() . 'assets/images/dummyuser.png';
                                }
                                $get_post['user_name']   = $check_user->first_name;
                                $where                   = array(
                                    'postID' => $postID
                                );
                                $likes                   = $this->Api_model->no_of_records('likes', $where);
                                $get_post['likes']       = $likes;
                                $where                   = array(
                                    'postID' => $postID
                                );
                                $comments                = $this->Api_model->no_of_records('comments', $where);
                                $get_post['comments']    = $comments;
                                $get_post['thumb_image'] = $this->config->base_url() . 'assets/images/thumb.png';
                                array_push($data, $get_post);
                            }
                            $this->Api_model->responseSuccess(1, GET_POST, $data);
                        } else {
                            $this->Api_model->responseFailed(0, NO_RECORD);
                        }
                    }
                } else {
                    $condition = array(
                        'flag' => '1',
                        'comunity_id' => $comunity_id
                    );
                    $get_post  = $this->Api_model->get_post($limit, $offset, $condition);
                    if ($get_post) {
                        $data = array();
                        foreach ($get_post as $get_post) {
                            $postID      = $get_post['postID'];
                            $user_id     = $get_post['user_id'];
                            $contentType = 'post';
                            
                            if ($get_post['media']) {
                                $get_post['media'] = $this->config->base_url() . $get_post['media'];
                            }
                            $check_like = $this->Api_model->getSingleRow("likes", array(
                                'user_id' => $un_user_id,
                                'postID' => $postID
                            ));
                            if ($check_like) {
                                $get_post['is_like'] = 'true';
                            } else {
                                $get_post['is_like'] = 'false';
                            }
                            
                            $check_abuse = $this->Api_model->get_abuse($user_id, $postID);
                            if ($check_abuse) {
                                $get_post['is_abuse'] = 'true';
                            } else {
                                $get_post['is_abuse'] = 'false';
                            }
                            $check_user = $this->Api_model->get_user($user_id);
                            if ($check_user->profile_pic) {
                                $get_post['user_image'] = $this->config->base_url() . $check_user->profile_pic;
                            } else {
                                $get_post['user_image'] = $this->config->base_url() . 'assets/images/dummyuser.png';
                            }
                            $get_post['user_name']   = $check_user->first_name;
                            $where                   = array(
                                'postID' => $postID
                            );
                            $likes                   = $this->Api_model->no_of_records('likes', $where);
                            $get_post['likes']       = $likes;
                            $where                   = array(
                                'postID' => $postID
                            );
                            $comments                = $this->Api_model->no_of_records('comments', $where);
                            $get_post['comments']    = $comments;
                            $get_post['thumb_image'] = $this->config->base_url() . 'assets/images/thumb.png';
                            array_push($data, $get_post);
                        }
                        $this->Api_model->responseSuccess(1, GET_POST, $data);
                    } else {
                        $this->Api_model->responseFailed(0, NO_RECORD);
                    }
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
        
    }
    
    public function get_my_post()
    {
        $limit      = $this->input->post('limit', TRUE);
        $offset     = $this->input->post('offset', TRUE);
        $user_id    = $this->input->post('user_id', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $get_post = $this->Api_model->get_my_post($limit, $offset, $user_id);
                if ($get_post) {
                    $data = array();
                    foreach ($get_post as $get_post) {
                        $postID      = $get_post['postID'];
                        $user_id     = $get_post['user_id'];
                        $contentType = 'post';
                        
                        if ($get_post['media']) {
                            $get_post['media'] = $this->config->base_url() . $get_post['media'];
                        }
                        
                        
                        $check_like = $this->Api_model->check_like($postID, $user_id);
                        if ($check_like) {
                            $get_post['is_like'] = 'true';
                        } else {
                            $get_post['is_like'] = 'false';
                        }
                        $check_abuse = $this->Api_model->get_abuse($user_id, $postID);
                        
                        if ($check_abuse) {
                            $get_post['is_abuse'] = 'true';
                        } else {
                            $get_post['is_abuse'] = 'false';
                        }
                        
                        $check_user = $this->Api_model->get_user($user_id);
                        if ($check_user->profile_pic) {
                            $get_post['user_image'] = $this->config->base_url() . $check_user->profile_pic;
                        } else {
                            $get_post['user_image'] = $this->config->base_url() . 'assets/images/dummyuser.png';
                        }
                        $get_post['user_name'] = $check_user->first_name;
                        
                        $where             = array(
                            'postID' => $postID
                        );
                        $likes             = $this->Api_model->no_of_records('likes', $where);
                        $get_post['likes'] = $likes;
                        
                        $where                = array(
                            'postID' => $postID
                        );
                        $comments             = $this->Api_model->no_of_records('comments', $where);
                        $get_post['comments'] = $comments;
                        
                        $get_post['thumb_image'] = $this->config->base_url() . 'assets/images/thumb.jpg';
                        
                        array_push($data, $get_post);
                    }
                    $this->Api_model->responseSuccess(1, GET_POST, $data);
                } else {
                    $this->Api_model->responseFailed(0, NO_RECORD);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
    }
    
    public function like()
    {
        $postID     = $this->input->post('postID', TRUE);
        $user_id    = $this->input->post('user_id', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                
                $check_like = $this->Api_model->check_like($postID, $user_id);
                if ($check_like) {
                    $this->Api_model->responseFailed(0, ALREADY_LIKE);
                } else {
                    $data['postID']  = $postID;
                    $data['user_id'] = $user_id;
                    $data['created'] = time();
                    
                    $data['id']   = $this->Api_model->like($data);
                    $getPost      = $this->Api_model->getSingleRow('post', array(
                        'postID' => $postID
                    ));
                    $getuser      = $this->Api_model->getSingleRow('users', array(
                        'id' => $user_id
                    ));
                    $getToken     = $this->Api_model->getSingleRow('users', array(
                        'id' => $getPost->user_id
                    ));
                    $userId       = $getPost->user_id;
                    $device_token = $getToken->device_tokan;
                    $username     = $getuser->first_name;
                    if ($userId != $user_id) {
                        $title = LIKE;
                        $this->firebase_with_class($device_token, $user_id, $username, $title, $username . LIKE_MSG);
                        $notification = array(
                            'user_id' => $userId,
                            'title' => $title,
                            'type' => $title,
                            'ids' => $postID,
                            'msg' => $username . LIKE_MSG,
                            'created_at' => time()
                        );
                        $this->Api_model->insertGetId(NOTOFICATION_TBL, $notification);
                        $this->Api_model->responseSuccessWithOutData(1, LIKE_POST);
                    }
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
        }
    }
    
    
    /* pet viewer list 25-12-18 */
    public function getlikedUser()
    {
        $this->form_validation->set_rules('user_id', 'Pet Id', 'required');
        $this->form_validation->set_rules('postID', 'post Id', 'required');
        if ($this->form_validation->run() == FALSE) {
            $errors = array_values($this->form_validation->error_array());
            //$this->response(["success" => false, "message" =>$errors[0]]);
            $this->Api_model->errResponse(0, $errors[0]);
        } else {
            $user_id    = $this->input->post('user_id');
            $postID     = $this->input->post('postID');
            $userStatus = $this->Api_model->getSingleRow('users', array(
                'id' => $user_id
            ));
            if ($userStatus) {
                if ($userStatus->status == 1) {
                    $viewerData = $this->Api_model->getAllDataWhere(array(
                        'postID' => $postID
                    ), 'likes');
                    if ($viewerData) {
                        $userData = array();
                        foreach ($viewerData as $viewer) {
                            $userInfo          = $this->Api_model->getSingleRow('users', array(
                                'id' => $viewer->user_id
                            ));
                            $viewer->UserName  = $userInfo->first_name . ' ' . $userInfo->last_name;
                            $viewer->UserEmail = $userInfo->email;
                            if ($userInfo->profile_pic) {
                                $viewer->profile_pic = $this->config->base_url() . $userInfo->profile_pic;
                            } else {
                                $viewer->profile_pic = $this->config->base_url() . 'assets/images/dummyuser.png';
                            }
                            array_push($userData, $viewer);
                        }
                        $this->Api_model->responseSuccess(1, "Get Voters.", $userData);
                    } else {
                        $this->Api_model->responseFailed(0, "No Voters found.");
                    }
                } else {
                    $this->Api_model->responseFailed(3, INACTIVATE);
                }
            } else {
                $this->Api_model->responseFailed(0, NOT_RESPONDING);
            }
        }
    }
    
    public function dislike()
    {
        $postID     = $this->input->post('postID', TRUE);
        $user_id    = $this->input->post('user_id', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                
                $where = array(
                    'postID' => $postID,
                    'user_id' => $user_id
                );
                $post  = $this->Api_model->getSingleRow('likes', $where);
                if ($post) {
                    $this->Api_model->deleteRecord($where, 'likes');
                    $this->Api_model->responseSuccessWithOutData(1, DISLIKE_POST);
                } else {
                    $this->Api_model->responseSuccessWithOutData(0, NO_RECORD);
                }
                
                
                
            }
            
            else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
        
    }
    
    public function get_comments()
    {
        $postID     = $this->input->post('postID', TRUE);
        $user_id    = $this->input->post('user_id', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $get_likes = $this->Api_model->get_comments_true($postID);
                
                if ($get_likes) {
                    $data = array();
                    foreach ($get_likes as $get_likes) {
                        
                        $user_id  = $get_likes['user_id'];
                        $get_user = $this->Api_model->get_user($user_id);
                        
                        $get_likes['userName'] = $get_user->first_name;
                        if ($get_user->profile_pic) {
                            $get_likes['image'] = $this->config->base_url() . $get_user->profile_pic;
                        } else {
                            $get_likes['image'] = $this->config->base_url() . 'assets/images/dummyuser.png';
                        }
                        array_push($data, $get_likes);
                    }
                    $this->Api_model->responseSuccess(1, ALL_COMMENT, $data);
                } else {
                    $this->Api_model->responseFailed(0, NO_RECORD);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
    }
    
    
    public function comment()
    {
        $postID     = $this->input->post('postID', TRUE);
        $user_id    = $this->input->post('user_id', TRUE);
        $content    = $this->input->post('content', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $get_user = $this->Api_model->get_user($user_id);
                $name     = $get_user->first_name;
                
                $data['postID']   = $postID;
                $data['user_id']  = $user_id;
                $data['content']  = $content;
                $data['createAt'] = time();
                
                $data['id'] = $this->Api_model->comment($data);
                if ($data['id']) {
                    
                    $get_post = $this->Api_model->get_single_post($postID);
                    
                    $postUserId = $get_post->user_id;
                    $get_user   = $this->Api_model->get_user($user_id);
                    $userId     = $get_user->id;
                    if ($userId != $postUserId) {
                        $title = COMMENT;
                        
                        $msg1         = $name . COMMENT_MSG;
                        $notification = array(
                            'user_id' => $postUserId,
                            'title' => COMMENT,
                            'type' => COMMENT,
                            'ids' => $postID,
                            'msg' => $msg1,
                            'created_at' => time()
                        );
                        $this->Api_model->insertGetId(NOTOFICATION_TBL, $notification);
                        // $this->firebase_notification($postUserId, $title, $msg1);
                        $getToken     = $this->Api_model->getSingleRow('users', array(
                            'id' => $postUserId
                        ));
                        $device_token = $getToken->device_tokan;
                        $this->firebase_with_class($device_token, $user_id, $userStatus->first_name, $title, $msg1);
                    }
                    
                    $this->Api_model->responseSuccessWithOutData(1, ADD_COMMENT);
                } else {
                    $this->Api_model->responseFailed(0, TRY_AGAIN);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
    }
    
    
    public function abuse_post()
    {
        $user_id    = $this->input->post('user_id', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $data['postID']  = $this->input->post('postID', TRUE);
                $data['user_id'] = $user_id;
                $data['reason']  = $this->input->post('reason', TRUE);
                $get_abuse       = $this->Api_model->get_abuse($data['user_id'], $data['postID']);
                if ($get_abuse) {
                    $this->Api_model->responseFailed(0, REPORT_POST);
                    exit();
                }
                
                $get_user         = $this->Api_model->get_user($data['user_id']);
                $data['userName'] = $get_user->first_name;
                
                $data['id'] = $this->Api_model->abuse_post($data);
                
                if ($data['id']) {
                    $this->Api_model->responseSuccessWithOutData(1, MARK_ABUSE);
                } else {
                    $this->Api_model->responseFailed(0, TRY_AGAIN);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
    }
    
    
    public function delete_post()
    {
        $postID     = $this->input->post('postID', TRUE);
        $user_id    = $this->input->post('user_id', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $data['postID']  = $postID;
                $data['user_id'] = $user_id;
                $data['flag']    = 0;
                
                $check_post = $this->Api_model->check_post($postID, $user_id);
                if ($check_post) {
                    $this->Api_model->delete_post($data);
                    $this->Api_model->responseSuccessWithOutData(1, DELETE_POST);
                    
                    //$this->firebase_notification($user_id, DELETE_POST_TITLE, DELETE_MSG);
                } else {
                    $this->Api_model->responseFailed(0, TRY_AGAIN);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
    }
    
    
    public function firebase_notification($user_id, $title, $msg1)
    {
        
        $user            = $this->db->where('id', $user_id)->get('users')->row();
        $registrationIds = $user->device_tokan;
        $mobile_sent     = $user_id;
        $FIRE_BASE_KEY=$this->Api_model->getSingleRow('firebase_keys',array('id'=>1));
          $API_ACCESS_KEY =$FIRE_BASE_KEY->firebase_keys;

       
        $msg    = array(
            'body' => $msg1,
            'title' => $title,
            'icon' => 'myicon',
            /*Default Icon*/
            'sound' => 'mySound'
            /*Default sound*/
        );
        $fields = array(
            'to' => $registrationIds,
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
    
    // function firebase_with_class($device_token, $sender_id, $senderName, $title, $msg1)
    // {
    //     $user = $this->Api_model->getSingleRow('users', array(
    //         'device_tokan' => $device_token
    //     ));
    //      $FIRE_BASE_KEY=$this->Api_model->getSingleRow('firebase_keys',array('id'=>1));
    //        $API_ACCESS_KEY =$FIRE_BASE_KEY->firebase_keys;
    //        print_r( $API_ACCESS_KEY );
    //        exit();
    //     if ($user->os_type == "ios") {
    //        $FIRE_BASE_KEY=$this->Api_model->getSingleRow('firebase_keys',array('id'=>1));
    //        $API_ACCESS_KEY =$FIRE_BASE_KEY->firebase_keys;

            
    //         $msg    = array(
    //             'body' => $msg1,
    //             'title' => $title,
    //             'icon' => 'myicon',
    //             /*Default Icon*/
    //             'sound' => 'default'
    //             /*Default sound*/
    //         );
    //         $fields = array(
    //             'to' => $device_token,
    //             'notification' => $msg
    //         );
            
            
    //         $headers = array(
    //             'Authorization: key=' . $API_ACCESS_KEY,
    //             'Content-Type: application/json'
    //         );
    //         #Send Reponse To FireBase Server    
    //         $ch      = curl_init();
    //         curl_setopt($ch, CURLOPT_URL, 'https://fcm.googleapis.com/fcm/send');
    //         curl_setopt($ch, CURLOPT_POST, true);
    //         curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
    //         curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    //         curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
    //         curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
    //         $result = curl_exec($ch);
    //         curl_close($ch);
    //     } else {
    //         $FCMPushNotification = new \BD\FCMPushNotification($API_ACCESS_KEY);
    //         $sDeviceToken        = $device_token;
    //         $aPayload            = array(
    //             'data' => array(
    //                 'title' => $title,
    //                 'type' => $title,
    //                 "sender_id" => $sender_id,
    //                 'body' => $msg1,
    //                 "senderName" => $senderName
    //             )
    //         );
            
    //         $aOptions = array(
    //             'time_to_live' => 15 //means messages that can't be delivered immediately are discarded. 
    //         );
            
    //         $aResult = $FCMPushNotification->sendToDevice($sDeviceToken, $aPayload, $aOptions // optional
    //             );
    //         return $aResult;
    //     }
    // }
    
    function firebase_with_class($device_token, $sender_id, $senderName, $title, $msg1)
    {
        $user = $this->Api_model->getSingleRow('users', array(
            'device_tokan' => $device_token
        ));

         $FIRE_BASE_KEY=$this->Api_model->getSingleRow('firebase_keys',array('id'=>1));
         $api_key=$FIRE_BASE_KEY->firebase_keys;



        if ($user->os_type == "ios") 
        {
            $API_ACCESS_KEY = $api_key;
            
            $msg    = array(
                'body' => $msg1,
                'title' => $title,
                'icon' => 'myicon',
                /*Default Icon*/
                'sound' => 'default'
                /*Default sound*/
            );
            $fields = array(
                'to' => $device_token,
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
        else 
        {
            $FCMPushNotification = new \BD\FCMPushNotification($api_key);

            $sDeviceToken        = $device_token;

            $senderName        = $user->first_name;
            $sender_id        = $user->id;
       
            $aPayload = array(
                'data' => array('title' =>$title,'type' => $title,"sender_id"=>$sender_id,'body'=> $msg1, "senderName"=>$senderName)
            );
            
         
            $aOptions = array(
                'time_to_live' => 15 //means messages that can't be delivered immediately are discarded. 
            );
            
            $aResult = $FCMPushNotification->sendToDevice(
                $sDeviceToken,      
                $aPayload,
                $aOptions // optional
            );
            // print_r($aResult);
            // exit();

            return $aResult;
        }
    }
    function firebase_with_class_v1()
    {

        $FIRE_BASE_KEY=$this->Api_model->getSingleRow('firebase_keys',array('id'=>1));
        $API_ACCESS_KEY =$FIRE_BASE_KEY->firebase_keys;
        $FCMPushNotification = new \BD\FCMPushNotification($API_ACCESS_KEY);
        $sDeviceToken        = "cmKu3Tyr_tA:APA91bFUcZ5lZsDcXYtBXq4X5vBzVjpRgx87Rj_udB9C3sz356Ns3Vtw8C5i-N7thSXxF3H8ax0V0MbGDFXsObK8bt7QSFbDqZkb8hcIG4VK0WKKu1NBgA9Am-M5FrEWd2V7Ejp96_GE";
        $aPayload            = array(
            'data' => array(
                'title' => "Test",
                'type' => "Test",
                "sender_id" => "1",
                'body' => "this is test",
                "senderName" => "senderName"
            )
        );
        
        $aOptions = array(
            'time_to_live' => 15 //means messages that can't be delivered immediately are discarded. 
        );
        
        $aResult = $FCMPushNotification->sendToDevice($sDeviceToken, $aPayload, $aOptions // optional
            );
       
    }
    
    
    public function add_post()
    {
        $user_id     = $this->input->post('user_id', TRUE);
        $postType    = $this->input->post('postType', TRUE);
        $content     = $this->input->post('content', TRUE);
        $media       = $this->input->post('media', TRUE);
        $title       = $this->input->post('title', TRUE);
        $comunity_id = $this->input->post('comunity_id', TRUE);
        $userStatus  = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                
                if ($postType != 'text') {
                    $this->load->library('upload');
                    
                    $config['upload_path']   = './assets/images/';
                    $config['allowed_types'] = 'gif|jpg|jpeg|png|avi|flv|wmv|mp4|mov|gif';
                    $config['max_size']      = 10000;
                    $config['file_name']     = time();
                    $this->upload->initialize($config);
                    $image = "";
                    if ($this->upload->do_upload('media')) {
                        $image = 'assets/images/' . $this->upload->data('file_name');
                    }
                    
                    if ($image) {
                        $data['media'] = $image;
                    }
                }
                
                $data['user_id']     = $user_id;
                $data['comunity_id'] = isset($comunity_id) ? $comunity_id : "";
                $data['title']       = $title;
                $data['postType']    = $postType;
                $data['content']     = $content;
                $data['createAt']    = time();
                
                $data['id'] = $this->Api_model->add_post($data);
                if ($data['id']) {
                    $pets = $this->Api_model->getNotifyUser($user_id);
                    
                    foreach ($pets as $get_user) {
                        $getUser      = $this->Api_model->getSingleRow('users', array(
                            'id' => $get_user->follower_user_id
                        ));
                        $username     = $getUser->first_name;
                        $device_token = $getUser->device_tokan;
                        $title        = POST;
                        $msg1         = "Please check wall. New post update.";
                        $notification = array(
                            'user_id' => $get_user->follower_user_id,
                            'title' => POST,
                            'type' => POST,
                            'ids' => $data['id'],
                            'msg' => $msg1,
                            'created_at' => time()
                        );
                        $this->Api_model->insertGetId(NOTOFICATION_TBL, $notification);
                        // $this->firebase_notification($get_user->follower_user_id, $title,$msg1);
                        $this->firebase_with_class($device_token, $user_id, $username, $title, $msg1);
                    }
                    
                    $this->Api_model->responseSuccessWithOutData(1, ADD_POST);
                } else {
                    $this->Api_model->responseFailed(0, TRY_AGAIN);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
        }
    }
    
    
    public function edit_post()
    {
        $user_id     = $this->input->post('user_id', TRUE);
        $postID      = $this->input->post('postID', TRUE);
        $postType    = $this->input->post('postType', TRUE);
        $content     = $this->input->post('content', TRUE);
        $media       = $this->input->post('media', TRUE);
        $title       = $this->input->post('title', TRUE);
        $comunity_id = $this->input->post('comunity_id', TRUE);
        $userStatus  = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $get_post = $this->Api_model->get_single_post($postID);
                if ($get_post) {

                    if ($_FILES['media']['name'] != "") {
                        $config['upload_path']   = './assets/images/';
                       $config['allowed_types']  = '*';
                        $config['max_size']      = 1000000000;
                        $config['file_name']     = time();
                        $this->load->library('upload', $config);
                        $this->upload->initialize($config);
                        $productsimage = "";
                        if ($this->upload->do_upload('media')) {
                            
                            $productsimage = 'assets/images/' . $this->upload->data('file_name');
                            $data['media'] = $productsimage;
                        } else {
                            echo "image is not inserted";


                        }
                    }


                    // $data['user_id']     = isset($user_id) ? $user_id : $get_post->user_id;
                    // $data['postID']      = isset($postID) ? $postID : $get_post->postID;
                    // $data['comunity_id'] = isset($comunity_id) ? $comunity_id : $get_post->comunity_id;
                    // $data['title']       = isset($title) ? $title : $get_post->title;
                    // $data['postType']    = isset($postType) ? $postType : $get_post->postType;
                    // $data['media']       = isset($media) ? $media : $get_post->media;
                    // $data['content']     = isset($content) ? $content : $get_post->content;

                    // $this->Api_model->update_post($data);
                $where = array(
                  'postID' => $postID
                   );
                  $this->Api_model->updateSingleRow('post', $where, $data);
                    
                    $this->Api_model->responseSuccessWithOutData(1, POST_UPDATE);
                } else {
                    $this->Api_model->responseFailed(0, TRY_AGAIN);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
    }
    
    public function deletePet()
    {
        $user_id    = $this->input->post('user_id', TRUE);
        $pet_id     = $this->input->post('pet_id', TRUE);
        $table      = 'pet';
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $condition = array(
                    'id' => $pet_id,
                    'user_id' => $user_id
                );
                
                $check_delete = $this->Api_model->getSingleRow($table, $condition);
                
                if ($check_delete->deleted == false) {
                    $delete = $this->Api_model->updateSingleRow($table, array(
                        'id' => $pet_id
                    ), array(
                        'deleted' => true
                    ));
                    
                    if ($delete) {
                        $this->Api_model->updateSingleRow('appointment', array(
                            'pet_id' => $pet_id
                        ), array(
                            'status' => '4'
                        ));
                        $this->Api_model->responseSuccessWithOutData(1, PET_DELETED);
                    } else {
                        $this->Api_model->responseFailed(0, NOT_RESPONDING);
                    }
                } else {
                    $this->Api_model->responseFailed(0, PET_ALREADY_DELETED);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
    }
    
    //   public function viewPetProfile()
    //   {
    // $pet_id=$this->input->post('pet_id', TRUE);
    // $table= 'pet';       
    //       $condition = array('id'=>$pet_id); 
    
    
    //  $value = $this->Api_model->updateViewPet($pet_id);
    
    //    if ($value) 
    //    {
    //      $this->Api_model->responseSuccessWithOutData(1, VIEW);
    //    }
    //   else
    //   {
    //    $this->Api_model->responseFailed(0, NOT_RESPONDING);
    //    }
    
    
    
    //   }
    
    
    public function viewPetProfile()
    {
        $pet_id     = $this->input->post('pet_id', TRUE);
        $user_id    = $this->input->post('user_id', TRUE);
        $condition  = array(
            'user_id' => $user_id,
            'id' => $pet_id
        );
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $table     = 'views';
                $check_pet = $this->Api_model->getTrueFalse('pet', $condition);
                
                if ($check_pet == 1) {
                    $this->Api_model->responseFailed(0, MYPETMSG);
                    exit();
                }
                
                $condition = array(
                    'pet_id' => $pet_id,
                    'user_id' => $user_id
                );
                
                $views = $this->Api_model->getAllDataWhere($condition, $table);
                
                if ($views) {
                    $this->Api_model->responseSuccessWithOutData(1, VIEW);
                } else {
                    $data['user_id'] = $user_id;
                    $data['pet_id']  = $pet_id;
                    $data['view']    = 1;
                    $getId           = $this->Api_model->insertGetId($table, $data);
                    $value           = $this->Api_model->updateViewPet($pet_id);
                    $this->Api_model->responseSuccessWithOutData(1, VIEW);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
    }
    
    
    
    public function rating()
    {
        $user_id    = $this->input->post('user_id', TRUE);
        $pet_id     = $this->input->post('pet_id', TRUE);
        $rating     = $this->input->post('rating', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $table     = 'pet';
                $condition = array(
                    'user_id' => $user_id,
                    'id' => $pet_id
                );
                
                $check_pet = $this->Api_model->getTrueFalse($table, $condition);
                
                if ($check_pet == 1) {
                    $this->Api_model->responseFailed(0, MYPETMSG);
                    exit();
                }
                if (!$check_rating = $this->Api_model->check_review($user_id, $pet_id)) {
                    
                    $data['user_id'] = $user_id;
                    $data['pet_id']  = $pet_id;
                    $data['rating']  = $rating;
                    
                    $this->Api_model->add_review($data);
                    //$this->Api_model->updateSingleRow('pet', array('id'=>$pet_id), array('rating'=>$this->getRating()));
                    $this->Api_model->updateRatingPet($pet_id, $this->getRating());
                    $this->Api_model->responseSuccessWithOutData(1, UPDATE_RATING);
                } else {
                    $this->Api_model->responseFailed(0, ALREADY_RATED);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
        }
    }
    
    public function getRating()
    {
        $user_id    = $this->input->post('user_id', TRUE);
        $pet_id     = $this->input->post('pet_id', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $avrage = $this->Api_model->count_total_rating($pet_id);
                return round($avrage->average, 0);
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
    }
    
    public function reviewProduct()
    {
        $user_id     = $this->input->post('user_id', TRUE);
        $product_id  = $this->input->post('product_id', TRUE);
        $rating      = $this->input->post('rating', TRUE);
        $title       = $this->input->post('title', TRUE);
        $description = $this->input->post('description', TRUE);
        $userStatus  = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                
                if (!$check_review_Product = $this->Api_model->check_review_product($user_id, $product_id)) {
                    $data['user_id']      = $user_id;
                    $data['product_id']   = $product_id;
                    $data['rating']       = $rating;
                    $data['title']        = $title;
                    $data['description']  = $description;
                    $data['created_date'] = time();
                    
                    $getId = $this->Api_model->insertGetId('review_product', $data);
                    
                    if ($getId) {
                        $this->Api_model->updateRatingProduct($product_id, $this->getRatingProduct());
                        $this->Api_model->responseFailed(1, REVIEW_ADDED);
                    } else {
                        $this->Api_model->responseFailed(0, NOT_RESPONDING);
                    }
                } else {
                    $this->Api_model->responseFailed(0, ALREADY_REVIEWED);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
    }
    
    public function getRatingProduct()
    {
        $user_id    = $this->input->post('user_id', TRUE);
        $product_id = $this->input->post('product_id', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $avrage = $this->Api_model->count_total_rating_product($product_id);
                return round($avrage->average, 0);
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
        
    }
    
    public function joincomunity()
    {
        $user_id     = $this->input->post('user_id', TRUE);
        $comunity_id = $this->input->post('comunity_id', TRUE);
        $userStatus  = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $table     = 'users';
                $condition = array(
                    'id' => $user_id
                );
                
                $check_user = $this->Api_model->getSingleRow($table, $condition);
                
                if ($check_user) {
                    $data = $this->Api_model->updateComunity($user_id, $comunity_id);
                    
                    $user = $this->Api_model->getSingleRow($table, $condition);
                    
                    $user->profile_pic = $this->config->base_url() . $user->profile_pic;
                    $this->Api_model->responseSuccess(1, JOIN_COMMUNITY, $user);
                    
                } else {
                    $this->Api_model->responseFailed(0, NOT_RESPONDING);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
        
    }
    
    public function getAllRating()
    {
        $pet_id     = $this->input->post('pet_id', TRUE);
        $user_id    = $this->input->post('user_id', TRUE);
        $rating     = $this->Api_model->getAllDataWhere(array(
            'pet_id' => $pet_id
        ), 'rating');
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                
                if ($rating) {
                    $ratings = array();
                    foreach ($rating as $rating) {
                        
                        $table             = 'users';
                        $condition         = array(
                            'id' => $rating->user_id
                        );
                        $user              = $this->Api_model->getSingleRow($table, $condition);
                        $rating->user_name = $user->first_name;
                        $rating->user_img  = $this->config->base_url() . $user->profile_pic;
                        
                        array_push($ratings, $rating);
                    }
                    
                    
                    $this->Api_model->responseSuccess(1, GET_ALL_RATING, $ratings);
                } else {
                    $this->Api_model->responseFailed(0, NOT_RESPONDING);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
    }
    
    public function getAllCat()
    {
        $user_id  = $this->input->post('user_id', TRUE);
        $pet_type = $this->input->post('pet_type', TRUE);
        $cat      = $this->Api_model->getAllDataWhere(array(
            'pet_type' => $pet_type,
            'status' => 1
        ), 'pro_cat');
        
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($cat) {
                
                $cats = array();
                foreach ($cat as $cat) {
                    $cat->c_img_path = $this->config->base_url() . $cat->c_img_path;
                    array_push($cats, $cat);
                    # code...
                }
                
                $this->Api_model->responseSuccess(1, GET_ALL_CAT, $cats);
            } else {
                $this->Api_model->responseFailed(0, NOT_RESPONDING);
            }
        } else {
            $this->Api_model->responseFailed(3, INACTIVATE);
        }
    }
    
    public function getAllbrand()
    {
        $user_id    = $this->input->post('user_id', TRUE);
        $cat_id     = $this->input->post('cat_id', TRUE);
        $sub_cat_id = $this->input->post('sub_cat_id', TRUE);
        $brand      = $this->Api_model->getAllDataWhere(array(
            'cat_id' => $cat_id,
            'sub_cat_id' => $sub_cat_id,
            'status' => 1
        ), 'food_company');
        
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            
            if ($brand) {
                
                $brands = array();
                foreach ($brand as $brand) {
                    $brand->c_img_path = $this->config->base_url() . $brand->c_img_path;
                    array_push($brands, $brand);
                    # code...
                }
                
                $this->Api_model->responseSuccess(1, GET_ALL_BRAND, $brands);
            } else {
                $this->Api_model->responseFailed(0, NOT_RESPONDING);
            }
        } else {
            $this->Api_model->responseFailed(3, INACTIVATE);
        }
    }
    
    
    
    public function getContactRequest()
    {
        $user_id    = $this->input->post('user_id', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                
                $Request = $this->Api_model->getAllDataWhere(array(
                    'user_id_owner' => $user_id
                ), 'contact');
                
                if ($Request) {
                    $Requests = array();
                    foreach ($Request as $Request) {
                        $user               = $this->Api_model->get_user($Request->user_id);
                        $Request->user_name = $user->first_name;
                        $Request->user_img  = $this->config->base_url() . $user->profile_pic;
                        array_push($Requests, $Request);
                    }
                    
                    $this->Api_model->responseSuccess(1, GET_CONTACT_REQUEST, $Requests);
                    
                } else {
                    
                    $this->Api_model->responseFailed(0, NO_RECORD);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
    }
    
    public function getMyRequest()
    {
        $user_id    = $this->input->post('user_id', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $Request = $this->Api_model->getAllDataWhere(array(
                    'user_id' => $user_id
                ), 'contact');
                
                if ($Request) {
                    $Requests = array();
                    foreach ($Request as $Request) {
                        $check_pet      = $this->Api_model->getSingleRow('pet', array(
                            'id' => $Request->pet_id
                        ));
                        $check_pet->msg = $Request->description;
                        // $Request->pet_img = $this->config->base_url().$user->pet_img_path;
                        
                        $table                   = 'breed';
                        $condition               = array(
                            'id' => $check_pet->breed_id
                        );
                        $breed_pet               = $this->Api_model->getSingleRow($table, $condition);
                        $check_pet->breed_name   = $breed_pet->breed_name;
                        $check_pet->breed_target = $breed_pet->target;
                        
                        $data1 = $this->Api_model->get_manual_activity_bydate($check_pet->id, $user_id, date('Y-m-d'));
                        
                        
                        if (empty($data1)) {
                            $check_pet->percent = 0;
                        } else {
                            $per                = ($data1[0]->manualactivity / $breed_pet->target) * 100;
                            $check_pet->percent = round($per, 0);
                        }
                        
                        $table                    = 'pet_type';
                        $condition                = array(
                            'id' => $check_pet->pet_type
                        );
                        $my_pet_type              = $this->Api_model->getSingleRow($table, $condition);
                        $check_pet->pet_type_name = $my_pet_type->pet_name;
                        
                        $check_pet->pet_img_path = $this->config->base_url() . $check_pet->pet_img_path;
                        
                        $data = $this->Api_model->get_manual_activity($check_pet->id, $user_id);
                        
                        if (empty($data)) {
                            $check_pet->chart = $data;
                        } else {
                            $check_pet->chart = $data;
                        }
                        
                        array_push($Requests, $check_pet);
                    }
                    
                    $this->Api_model->responseSuccess(1, GET_CONTACT_REQUEST, $Requests);
                    
                } else {
                    
                    $this->Api_model->responseFailed(0, NO_RECORD);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
        }
    }
    
    public function getNotificattion()
    {
        $user_id    = $this->input->post('user_id', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $Request = $this->Api_model->getAllDataWhere(array(
                    'user_id' => $user_id
                ), 'admin_notification');
                
                if ($Request) {
                    $Requests = array();
                    foreach ($Request as $Request) {
                        $user                = $this->Api_model->get_user($Request->user_id);
                        $Request->user_name  = $user->first_name;
                        $Request->created_at = date('d M Y H:i A', strtotime($Request->created_at));
                        $Request->user_img   = $this->config->base_url() . $user->profile_pic;
                        array_push($Requests, $Request);
                    }
                    
                    $this->Api_model->responseSuccess(1, GET_NOTIFICATION_REQUEST, $Requests);
                    
                } else {
                    
                    $this->Api_model->responseFailed(0, NO_RECORD);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
        
    }
    
    public function razor()
    {
        $this->load->view('rozer');
        
    }
    
    public function image_upload_for_list()
    {
        $uploadData = [];
        $user_id      = $this->input->post('user_id');
        $pet_id       = $this->input->post('pet_id');
        $description  = $this->input->post('description');
        $created      = time();
        $count        = $this->input->post('count');
        $add_category = false;
        $userStatus   = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                
                for ($i = 0; $i < $count; $i++) {
                    $userfile                      = 'userFiles' . $i;
                    $filesCount                    = count($_FILES[$userfile]['name']);
                    $_FILES[$userfile]['name']     = $_FILES[$userfile]['name'];
                    $_FILES[$userfile]['type']     = $_FILES[$userfile]['type'];
                    $_FILES[$userfile]['tmp_name'] = $_FILES[$userfile]['tmp_name'];
                    $_FILES[$userfile]['error']    = $_FILES[$userfile]['error'];
                    $_FILES[$userfile]['size']     = $_FILES[$userfile]['size'];
                    
                    $uploadPath              = './assets/images/';
                    $uploadPathX             = '/assets/images/';
                    $config['upload_path']   = $uploadPath;
                    $config['allowed_types'] = 'gif|jpg|png|jpeg';
                    
                    $this->load->library('upload', $config);
                    $this->upload->initialize($config);
                    if ($this->upload->do_upload($userfile)) {
                        $fileData     = $this->upload->data();
                        $uploadData[] = $uploadPathX . $fileData['file_name'];
                    } else {
                        echo $this->upload->display_errors();
                    }
                    
                }
                
                
                if (count($uploadData) > 0) {
                    $add_category = $this->Api_model->upload_image($user_id, $pet_id, $description, $created, $uploadData);
                    $data         = $uploadData;
                }
                
                if ($add_category) {
                    $this->Api_model->responseFailed(1, MEMORIES_UPLOADED);
                } else {
                    $this->Api_model->responseFailed(0, MEMORIES_NOT_UPLOADED);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
        
    }
    
    public function send_invitation()
    {
        $this->form_validation->set_rules('user_id', 'User_id', 'trim|required');
        $this->form_validation->set_rules('email', 'email', 'trim|required');
        $this->form_validation->set_rules('subject', 'subject', 'trim|required');
        $this->form_validation->set_rules('msg', 'msg', 'trim|required');
        
        if ($this->form_validation->run() == FALSE) {
            $errors = array_values($this->form_validation->error_array());
            //$this->response(["success" => false, "message" =>$errors[0]]);
            $this->Api_model->response(0, $errors[0]);
        } else {
            $user_id    = $this->input->post('user_id', TRUE);
            $email      = $this->input->post('email', TRUE);
            $subject    = $this->input->post('subject', TRUE);
            $msg        = $this->input->post('msg', TRUE);
            $userStatus = $this->Api_model->getSingleRow('users', array(
                'id' => $user_id
            ));
            if ($userStatus) {
                if ($userStatus->status == 1) {
                    $value = $this->Api_model->send_email_invitation($email, $subject, $msg);
                    
                    if ($value) {
                        $this->Api_model->responseFailed(1, INVITATION_SEND);
                    } else {
                        $this->Api_model->responseFailed(0, FAILED);
                    }
                } else {
                    $this->Api_model->responseFailed(3, INACTIVATE);
                }
            } else {
                $this->Api_model->responseFailed(0, NOT_RESPONDING);
                
            }
        }
    }
    /* add event  */
    public function add_event()
    {
        $this->form_validation->set_rules('event_name', 'Event Name', 'required');
        $this->form_validation->set_rules('user_id', 'User', 'required');
        $this->form_validation->set_rules('address', 'Address', 'required');
        $this->form_validation->set_rules('event_date', 'Event Date', 'required');
        $this->form_validation->set_rules('event_desc', 'Event Description', 'required');
        $this->form_validation->set_rules('pet_type', 'Pet Type', 'required');
        $this->form_validation->set_rules('latitude', 'latitude', 'required');
        $this->form_validation->set_rules('longitude', 'longitude', 'required');
        $this->form_validation->set_rules('event_time', 'Event Time', 'required');
        if ($this->form_validation->run() == FALSE) {
            $errors = array_values($this->form_validation->error_array());
            //$this->response(["success" => false, "message" =>$errors[0]]);
            $this->Api_model->response(0, $errors[0]);
        } else {
            $user_id    = $this->input->post('user_id');
            $userStatus = $this->Api_model->getSingleRow('users', array(
                'id' => $user_id
            ));
            if ($userStatus) {
                if ($userStatus->status == 1) {
                    $data['event_name']      = $this->input->post('event_name');
                    $data['user_id']         = $user_id;
                    $data['address']         = $this->input->post('address');
                    $data['latitude']        = $this->input->post('latitude');
                    $data['longitude']       = $this->input->post('longitude');
                    $data['event_date']      = $this->input->post('event_date');
                    $data['event_time']      = $this->input->post('event_time');
                    $data['event_desc']      = $this->input->post('event_desc');
                    $data['pet_type']        = $this->input->post('pet_type');
                    $data['created_at']      = time();
                    $data['updated_on']      = time();
                    $config['upload_path']   = './assets/images/event/';
                    $config['allowed_types'] = 'gif|jpg|jpeg|png';
                    $config['max_size']      = 10000;
                    $config['file_name']     = time();
                    $this->load->library('upload', $config);
                    $this->upload->initialize($config);
                    $productsimage = "";
                    if ($this->upload->do_upload('img_path')) {
                        
                        $productsimage = 'assets/images/event/' . $this->upload->data('file_name');
                        $data['image'] = $productsimage;
                    } else {
                        echo "image is not inserted";
                    }
                    
                    $eventId = $this->Api_model->insertGetId('event', $data);
                    if ($eventId) {
                        $singleUser = $this->Api_model->getSingleRow('users', array(
                            'id' => $user_id
                        ));
                        $users      = $this->Api_model->getAllDataNotUser(array(
                            'status' => 1
                        ), $user_id, 'users');
                        if ($users) {
                            foreach ($users as $user) {
                                $userData     = $this->Api_model->getSingleRow('users', array(
                                    'id' => $user->id
                                ));
                                $device_token = $userData->device_tokan;
                                $title        = "Event";
                                $msg1         = "$singleUser->first_name.' '.$singleUser->last_name " . CREATEDEVENT;
                                $this->firebase_with_class($device_token, $user_id, $singleUser->first_name, $title, $msg1);
                                //$this->firebase_notification($user->id, $title, $msg1);
                            }
                        }
                        $this->Api_model->responseFailed(1, EVENTADD);
                    } else {
                        $this->Api_model->responseFailed(0, FAILED);
                    }
                } else {
                    $this->Api_model->responseFailed(3, INACTIVATE);
                }
            } else {
                $this->Api_model->responseFailed(0, NOT_RESPONDING);
                
            }
        }
        
    }
    
    /* edit event  */
    public function edit_event()
    {
        $this->form_validation->set_rules('event_name', 'Event Name', 'required');
        $this->form_validation->set_rules('event_id', 'Event id', 'required');
        $this->form_validation->set_rules('user_id', 'User', 'required');
        $this->form_validation->set_rules('address', 'Address', 'required');
        $this->form_validation->set_rules('event_date', 'Event Date', 'required');
        $this->form_validation->set_rules('event_desc', 'Event Description', 'required');
        $this->form_validation->set_rules('pet_type', 'Pet Type', 'required');
        $this->form_validation->set_rules('latitude', 'latitude', 'required');
        $this->form_validation->set_rules('longitude', 'longitude', 'required');
        $this->form_validation->set_rules('event_time', 'Event Time', 'required');
        if ($this->form_validation->run() == FALSE) {
            $errors = array_values($this->form_validation->error_array());
            //$this->response(["success" => false, "message" =>$errors[0]]);
            $this->Api_model->response(0, $errors[0]);
        } else {
            $user_id    = $this->input->post('user_id');
            $userStatus = $this->Api_model->getSingleRow('users', array(
                'id' => $user_id
            ));
            if ($userStatus) {
                if ($userStatus->status == 1) {
                    $event_id           = $this->input->post('event_id');
                    $data['event_name'] = $this->input->post('event_name');
                    $data['address']    = $this->input->post('address');
                    $data['latitude']   = $this->input->post('latitude');
                    $data['longitude']  = $this->input->post('longitude');
                    $data['event_date'] = $this->input->post('event_date');
                    $data['event_time'] = $this->input->post('event_time');
                    $data['event_desc'] = $this->input->post('event_desc');
                    $data['pet_type']   = $this->input->post('pet_type');
                    if ($_FILES['img_path']['name'] != "") {
                        $data['created_at']      = time();
                        $data['updated_on']      = time();
                        $config['upload_path']   = './assets/images/event/';
                        $config['allowed_types'] = 'gif|jpg|jpeg|png';
                        $config['max_size']      = 10000;
                        $config['file_name']     = time();
                        $this->load->library('upload', $config);
                        $this->upload->initialize($config);
                        $productsimage = "";
                        if ($this->upload->do_upload('img_path')) {
                            
                            $productsimage = 'assets/images/event/' . $this->upload->data('file_name');
                            $data['image'] = $productsimage;
                        } else {
                            echo "image is not inserted";
                        }
                    }
                    $where   = array(
                        'user_id' => $user_id,
                        'id' => $event_id
                    );
                    $eventId = $this->Api_model->updateSingleRow('event', $where, $data);
                    if ($eventId) {
                        $this->Api_model->responseFailed(1, EVENTEDIT);
                    } else {
                        $this->Api_model->responseFailed(0, FAILED);
                    }
                } else {
                    $this->Api_model->responseFailed(3, INACTIVATE);
                }
            } else {
                $this->Api_model->responseFailed(0, NOT_RESPONDING);
                
            }
            
        }
        
        
    }
    
    public function delete_event()
    {
        $this->form_validation->set_rules('event_id', 'Event id', 'required');
        $this->form_validation->set_rules('user_id', 'User', 'required');
        if ($this->form_validation->run() == FALSE) {
            $errors = array_values($this->form_validation->error_array());
            //$this->response(["success" => false, "message" =>$errors[0]]);
            $this->Api_model->response(0, $errors[0]);
        } else {
            $user_id    = $this->input->post('user_id');
            $userStatus = $this->Api_model->getSingleRow('users', array(
                'id' => $user_id
            ));
            if ($userStatus) {
                if ($userStatus->status == 1) {
                    $event_id        = $this->input->post('event_id');
                    $where           = array(
                        'user_id' => $user_id,
                        'id' => $event_id
                    );
                    $data['deleted'] = 0;
                    $eventId         = $this->Api_model->updateSingleRow('event', $where, $data);
                    if ($eventId) {
                        $this->Api_model->responseFailed(1, EVENTDETETE);
                    } else {
                        $this->Api_model->responseFailed(0, NO_RECORD);
                    }
                } else {
                    $this->Api_model->responseFailed(3, INACTIVATE);
                }
            } else {
                $this->Api_model->responseFailed(0, NOT_RESPONDING);
                
            }
            
        }
    }
    
    public function getMyEvent()
    {
        $this->form_validation->set_rules('user_id', 'User', 'required');
        if ($this->form_validation->run() == FALSE) {
            $errors = array_values($this->form_validation->error_array());
            //$this->response(["success" => false, "message" =>$errors[0]]);
            $this->Api_model->response(0, $errors[0]);
        } else {
            $user_id    = $this->input->post('user_id');
            $userStatus = $this->Api_model->getSingleRow('users', array(
                'id' => $user_id
            ));
            if ($userStatus) {
                if ($userStatus->status == 1) {
                    $event     = $this->Api_model->getAllDataWhere(array(
                        'user_id' => $user_id,
                        'deleted' => 1
                    ), 'event');
                    $eventData = array();
                    if ($event) {
                        foreach ($event as $events) {
                            $events->image       = $this->config->base_url() . $events->image;
                            $petTypeData         = $this->Api_model->getSingleRow('pet_type', array(
                                'id' => $events->pet_type
                            ));
                            $events->pet_type_id = $events->pet_type;
                            $events->pet_type    = $petTypeData->pet_name;
                            array_push($eventData, $events);
                        }
                        $this->Api_model->responseSuccess(1, GETALLEVENT, $eventData);
                    } else {
                        $this->Api_model->responseFailed(0, NO_RECORD);
                    }
                } else {
                    $this->Api_model->responseFailed(3, INACTIVATE);
                }
            } else {
                $this->Api_model->responseFailed(0, NOT_RESPONDING);
                
            }
        }
    }
    
    public function joinEvent()
    {
        $this->form_validation->set_rules('event_id', 'Event', 'required');
        $this->form_validation->set_rules('user_id', 'User', 'required');
        if ($this->form_validation->run() == FALSE) {
            $errors = array_values($this->form_validation->error_array());
            //$this->response(["success" => false, "message" =>$errors[0]]);
            $this->Api_model->response(0, $errors[0]);
        } else {
            $event_id   = $this->input->post('event_id');
            $user_id    = $this->input->post('user_id');
            $userStatus = $this->Api_model->getSingleRow('users', array(
                'id' => $user_id
            ));
            if ($userStatus) {
                if ($userStatus->status == 1) {
                    $eventData = $this->Api_model->getSingleRow('event', array(
                        'id' => $event_id
                    ));
                    if ($eventData) {
                        if ($eventData->user_id == $user_id) {
                            $this->Api_model->responseFailed(0, OWNEVENT);
                            exit();
                        } else {
                            $joinEventData = $this->Api_model->getSingleRow('join_event', array(
                                'event_id' => $event_id,
                                'user_id' => $user_id
                            ));
                            if (!$joinEventData) {
                                $data['event_id']   = $this->input->post('event_id');
                                $data['user_id']    = $this->input->post('user_id');
                                $data['created_at'] = time();
                                $data['updated_on'] = time();
                                $this->Api_model->insertGetId('join_event', $data);
                                $this->Api_model->responseFailed(1, JOINNEVENT);
                            } else {
                                $this->Api_model->responseFailed(0, AlREADYJOINNEVENT);
                            }
                        }
                    } else {
                        $this->Api_model->responseFailed(0, NO_RECORD);
                    }
                } else {
                    $this->Api_model->responseFailed(3, INACTIVATE);
                }
            } else {
                $this->Api_model->responseFailed(0, NOT_RESPONDING);
            }
        }
    }
    
    public function removeUser()
    {
        $this->form_validation->set_rules('event_id', 'Event', 'required');
        $this->form_validation->set_rules('user_id', 'User', 'required');
        if ($this->form_validation->run() == FALSE) {
            $errors = array_values($this->form_validation->error_array());
            //$this->response(["success" => false, "message" =>$errors[0]]);
            $this->Api_model->response(0, $errors[0]);
        } else {
            $table      = 'join_event';
            $event_id   = $this->input->post('event_id');
            $user_id    = $this->input->post('user_id');
            $userStatus = $this->Api_model->getSingleRow('users', array(
                'id' => $user_id
            ));
            if ($userStatus) {
                if ($userStatus->status == 1) {
                    $where     = array(
                        'user_id' => $user_id,
                        'event_id' => $event_id
                    );
                    $joinEvent = $this->Api_model->getSingleRow($table, $where);
                    if ($joinEvent) {
                        $this->Api_model->deleteRecord($where, $table);
                        $this->Api_model->responseFailed(1, REMOVEUSER);
                    } else {
                        $this->Api_model->responseFailed(0, NO_RECORD);
                    }
                } else {
                    $this->Api_model->responseFailed(3, INACTIVATE);
                }
            } else {
                $this->Api_model->responseFailed(0, NOT_RESPONDING);
                
            }
            
        }
    }
    
    public function getAllEvent()
    {
        $this->form_validation->set_rules('user_id', 'User', 'required');
        if ($this->form_validation->run() == FALSE) {
            $errors = array_values($this->form_validation->error_array());
            //$this->response(["success" => false, "message" =>$errors[0]]);
            $this->Api_model->response(0, $errors[0]);
        } else {
            $user_id    = $this->input->post('user_id');
            $userStatus = $this->Api_model->getSingleRow('users', array(
                'id' => $user_id
            ));
            if ($userStatus) {
                if ($userStatus->status == 1) {
                    $eventData = $this->Api_model->getAllDataWhereNotUser(array(
                        'deleted' => 1
                    ), $user_id, 'event');
                    if ($eventData) {
                        foreach ($eventData as $event) {
                            $joinEventData = $this->Api_model->getSingleRow('join_event', array(
                                'user_id' => $user_id,
                                'event_id' => $event->id
                            ));
                            if ($joinEventData) {
                                $event->is_join = 1;
                            } else {
                                $event->is_join = 0;
                            }
                            $event->image       = $this->config->base_url() . $event->image;
                            $userData           = $this->Api_model->getSingleRow('users', array(
                                'id' => $event->user_id
                            ));
                            $event->user_name   = $userData->first_name . ' ' . $userData->last_name;
                            $event->profile_pic = $this->config->base_url() . $userData->profile_pic;
                            $petTypeData        = $this->Api_model->getSingleRow('pet_type', array(
                                'id' => $event->pet_type
                            ));
                            $event->pet_type_id = $event->pet_type;
                            $event->pet_type    = $petTypeData->pet_name;
                        }
                        $this->Api_model->responseSuccess(1, GETALLEVENT, $eventData);
                        
                    } else {
                        $this->Api_model->responseFailed(0, NO_RECORD);
                    }
                } else {
                    $this->Api_model->responseFailed(3, INACTIVATE);
                }
            } else {
                $this->Api_model->responseFailed(0, NOT_RESPONDING);
                
            }
            
        }
    }
    
    public function joinEventList()
    {
        $this->form_validation->set_rules('user_id', 'User', 'required');
        if ($this->form_validation->run() == FALSE) {
            $errors = array_values($this->form_validation->error_array());
            //$this->response(["success" => false, "message" =>$errors[0]]);
            $this->Api_model->response(0, $errors[0]);
        } else {
            $user_id    = $this->input->post('user_id');
            $userStatus = $this->Api_model->getSingleRow('users', array(
                'id' => $user_id
            ));
            if ($userStatus) {
                if ($userStatus->status == 1) {
                    
                    $joinEventData = $this->Api_model->getAllDataWhere(array(
                        'user_id' => $user_id
                    ), 'join_event');
                    $joinedData    = array();
                    if ($joinEventData) {
                        foreach ($joinEventData as $joinEvent) {
                            $eventData = $this->Api_model->getSingleRow('event', array(
                                'id' => $joinEvent->id
                            ));
                            
                            $petTypeData = $this->Api_model->getSingleRow('pet_type', array(
                                'id' => $eventData->pet_type
                            ));
                            
                            $eventData->pet_type_id = $eventData->pet_type;
                            $eventData->pet_type    = $petTypeData->pet_name;
                            
                            array_push($joinedData, $eventData);
                        }
                        $this->Api_model->responseSuccess(1, GETALLJOINEVENT, $joinedData);
                    } else {
                        $this->Api_model->responseFailed(0, NO_RECORD);
                    }
                } else {
                    $this->Api_model->responseFailed(3, INACTIVATE);
                }
            } else {
                $this->Api_model->responseFailed(0, NOT_RESPONDING);
                
            }
        }
    }
    
    public function joinEventUserList()
    {
        $this->form_validation->set_rules('user_id', 'User id', 'required');
        $this->form_validation->set_rules('event_id', 'Event', 'required');
        if ($this->form_validation->run() == FALSE) {
            $errors = array_values($this->form_validation->error_array());
            //$this->response(["success" => false, "message" =>$errors[0]]);
            $this->Api_model->response(0, $errors[0]);
        } else {
            $user_id    = $this->input->post('user_id');
            $event_id   = $this->input->post('event_id');
            $userStatus = $this->Api_model->getSingleRow('users', array(
                'id' => $user_id
            ));
            if ($userStatus) {
                if ($userStatus->status == 1) {
                    $joinEventData = $this->Api_model->getAllDataWhere(array(
                        'event_id' => $event_id
                    ), 'join_event');
                    $joinedData    = array();
                    if ($joinEventData) {
                        foreach ($joinEventData as $joinEvent) {
                            $userData              = $this->Api_model->getSingleRow('users', array(
                                'id' => $joinEvent->user_id
                            ));
                            $userData->profile_pic = $this->config->base_url() . $userData->profile_pic;
                            array_push($joinedData, $userData);
                        }
                        $this->Api_model->responseSuccess(1, GETALLJOINEVENTUSER, $joinedData);
                    } else {
                        $this->Api_model->responseFailed(0, NO_RECORD);
                    }
                } else {
                    $this->Api_model->responseFailed(3, INACTIVATE);
                }
            } else {
                $this->Api_model->responseFailed(0, NOT_RESPONDING);
                
            }
        }
    }
    
    
    
    /* follower 25-12-18 */
    public function followPetProfile()
    {
        
        $this->form_validation->set_rules('pet_id', 'Pet Id', 'required');
        $this->form_validation->set_rules('follower_user_id', 'Fallower User Id', 'required');
        if ($this->form_validation->run() == FALSE) {
            $errors = array_values($this->form_validation->error_array());
            //$this->response(["success" => false, "message" =>$errors[0]]);
            $this->Api_model->errResponse(0, $errors[0]);
        } else {
            
            $pet_id           = $this->input->post('pet_id');
            $follower_user_id = $this->input->post('follower_user_id');
            $userStatus       = $this->Api_model->getSingleRow('users', array(
                'id' => $follower_user_id
            ));
            if ($userStatus) {
                if ($userStatus->status == 1) {
                    $condition = array(
                        'user_id' => $follower_user_id,
                        'id' => $pet_id
                    );
                    $check_pet = $this->Api_model->getTrueFalse('pet', $condition);
                    if ($check_pet == 1) {
                        $this->Api_model->responseFailed(0, NOTFOLLOW);
                        exit();
                    } else {
                        $userData = $this->Api_model->getSingleRow('users', array(
                            'status' => 1,
                            'id' => $follower_user_id
                        ));
                        if ($userData) {
                            $followerData = $this->Api_model->getSingleRow(FOLLOW_TBL, array(
                                'pet_id' => $pet_id,
                                'follower_user_id' => $follower_user_id
                            ));
                            if (!$followerData) {
                                $data['pet_id']           = $pet_id;
                                $data['follower_user_id'] = $follower_user_id;
                                $data['created_at']       = time();
                                $getId                    = $this->Api_model->insertGetId(FOLLOW_TBL, $data);
                                if ($getId) {
                                    $getPet       = $this->Api_model->getSingleRow('pet', array(
                                        'id' => $pet_id
                                    ));
                                    $getToken     = $this->Api_model->getSingleRow('users', array(
                                        'id' => $getPet->user_id
                                    ));
                                    $getuser      = $this->Api_model->getSingleRow('users', array(
                                        'id' => $follower_user_id
                                    ));
                                    $device_token = $getToken->device_tokan;
                                    $userId       = $getPet->user_id;
                                    $username     = $getuser->first_name;
                                    $title        = FOLLOW;
                                    // $this->firebase_notification($userId,$title,$username. FOLLOW_MSG);
                                    $this->firebase_with_class($device_token, $follower_user_id, $username, $title, $username . FOLLOW_MSG);
                                    $notification = array(
                                        'user_id' => $userId,
                                        'title' => $title,
                                        'type' => $title,
                                        'ids' => $pet_id,
                                        'msg' => $username . FOLLOW_MSG,
                                        'created_at' => time()
                                    );
                                    $this->Api_model->insertGetId(NOTOFICATION_TBL, $notification);
                                    $this->Api_model->responseFailed(1, INSFOLLOW);
                                }
                            } else {
                                $where = array(
                                    'pet_id' => $pet_id,
                                    'follower_user_id' => $follower_user_id
                                );
                                $this->Api_model->deleteRecord($where, FOLLOW_TBL);
                                $this->Api_model->responseFailed(1, UNFOLLOW);
                            }
                        } else {
                            $this->Api_model->responseFailed(0, NOTACTIVATE);
                        }
                    }
                } else {
                    $this->Api_model->responseFailed(3, INACTIVATE);
                }
            } else {
                $this->Api_model->responseFailed(0, NOT_RESPONDING);
                
            }
            
        }
    }
    
    /*follower list 25-12-18 */
    public function getMyPetFollowers()
    {
        $this->form_validation->set_rules('user_id', 'Pet Id', 'required');
        $this->form_validation->set_rules('pet_id', 'Pet Id', 'required');
        if ($this->form_validation->run() == FALSE) {
            $errors = array_values($this->form_validation->error_array());
            $this->Api_model->errResponse(0, $errors[0]);
        } else {
            $user_id    = $this->input->post('user_id');
            $pet_id     = $this->input->post('pet_id');
            $userStatus = $this->Api_model->getSingleRow('users', array(
                'id' => $user_id
            ));
            if ($userStatus) {
                if ($userStatus->status == 1) {
                    $followerData = $this->Api_model->getAllDataWhere(array(
                        'pet_id' => $pet_id
                    ), FOLLOW_TBL);
                    if ($followerData) {
                        $userData = array();
                        foreach ($followerData as $follower) {
                            $userInfo = $this->Api_model->getSingleRow('users', array(
                                'id' => $follower->follower_user_id
                            ));
                            
                            $follower->UserName  = $userInfo->first_name . ' ' . $userInfo->last_name;
                            $follower->UserEmail = $userInfo->email;
                            if ($userInfo->profile_pic) {
                                $follower->profile_pic = $this->config->base_url() . $userInfo->profile_pic;
                            } else {
                                $follower->profile_pic = '';
                            }
                            array_push($userData, $follower);
                        }
                        $this->Api_model->responseSuccess(1, MYFOLLWER, $userData);
                    } else {
                        $this->Api_model->responseFailed(0, NO_FOLLOWER);
                    }
                } else {
                    $this->Api_model->responseFailed(3, INACTIVATE);
                }
            } else {
                $this->Api_model->responseFailed(0, NOT_RESPONDING);
                
            }
        }
    }
    
    
    /* pet viewer list 25-12-18 */
    public function getMyPetViewer()
    {
        $this->form_validation->set_rules('user_id', 'Pet Id', 'required');
        $this->form_validation->set_rules('pet_id', 'Pet Id', 'required');
        if ($this->form_validation->run() == FALSE) {
            $errors = array_values($this->form_validation->error_array());
            //$this->response(["success" => false, "message" =>$errors[0]]);
            $this->Api_model->errResponse(0, $errors[0]);
        } else {
            $user_id    = $this->input->post('user_id');
            $pet_id     = $this->input->post('pet_id');
            $userStatus = $this->Api_model->getSingleRow('users', array(
                'id' => $user_id
            ));
            if ($userStatus) {
                if ($userStatus->status == 1) {
                    $viewerData = $this->Api_model->getAllDataWhere(array(
                        'pet_id' => $pet_id
                    ), 'views');
                    if ($viewerData) {
                        $userData = array();
                        foreach ($viewerData as $viewer) {
                            $userInfo          = $this->Api_model->getSingleRow('users', array(
                                'id' => $viewer->user_id
                            ));
                            $viewer->UserName  = $userInfo->first_name . ' ' . $userInfo->last_name;
                            $viewer->UserEmail = $userInfo->email;
                            if ($userInfo->profile_pic) {
                                $viewer->profile_pic = $this->config->base_url() . $userInfo->profile_pic;
                            }
                            
                            array_push($userData, $viewer);
                        }
                        $this->Api_model->responseSuccess(1, MYPETVIEWER, $userData);
                    } else {
                        $this->Api_model->responseFailed(0, NO_VIEWER);
                    }
                } else {
                    $this->Api_model->responseFailed(3, INACTIVATE);
                }
            } else {
                $this->Api_model->responseFailed(0, NOT_RESPONDING);
                
            }
            
        }
    }
    
    /*editWallComment 25-12-18 */
    public function editWallComment()
    {
        $this->form_validation->set_rules('user_id', 'User Id', 'required');
        $this->form_validation->set_rules('comment_id', 'Comment Id', 'required');
        $this->form_validation->set_rules('content', 'content', 'required');
        if ($this->form_validation->run() == FALSE) {
            $errors = array_values($this->form_validation->error_array());
            $this->Api_model->errResponse(0, $errors[0]);
        } else {
            $user_id    = $this->input->post('user_id');
            $comment_id = $this->input->post('comment_id');
            $content    = $this->input->post('content');
            $userStatus = $this->Api_model->getSingleRow('users', array(
                'id' => $user_id
            ));
            if ($userStatus) {
                if ($userStatus->status == 1) {
                    $table       = 'comments';
                    $where       = array(
                        'commentID' => $comment_id
                    );
                    $commentData = $this->Api_model->getAllDataWhere($where, $table);
                    if ($commentData) {
                        $data['content']  = $content;
                        $data['createAt'] = time();
                        $this->Api_model->updateSingleRow($table, $where, $data);
                        $this->Api_model->responseSuccessWithOutData(1, EDITCOMMENT);
                    } else {
                        $this->Api_model->responseFailed(0, NOT_RESPONDING);
                    }
                } else {
                    $this->Api_model->responseFailed(3, INACTIVATE);
                }
            } else {
                $this->Api_model->responseFailed(0, NOT_RESPONDING);
                
            }
            
        }
    }
    
    
    public function deleteWallComment()
    {
        $this->form_validation->set_rules('user_id', 'User Id', 'required');
        $this->form_validation->set_rules('comment_id', 'Comment Id', 'required');
        if ($this->form_validation->run() == FALSE) {
            $errors = array_values($this->form_validation->error_array());
            $this->Api_model->errResponse(0, $errors[0]);
        } else {
            $user_id    = $this->input->post('user_id');
            $comment_id = $this->input->post('comment_id');
            $userStatus = $this->Api_model->getSingleRow('users', array(
                'id' => $user_id
            ));
            if ($userStatus) {
                if ($userStatus->status == 1) {
                    $table = 'comments';
                    $where = array(
                        'commentID' => $comment_id
                    );
                    $this->Api_model->deleteRecord($where, $table);
                    $this->Api_model->responseSuccessWithOutData(1, DELCOMMENT);
                } else {
                    $this->Api_model->responseFailed(3, INACTIVATE);
                }
            } else {
                $this->Api_model->responseFailed(0, NOT_RESPONDING);
                
            }
            
        }
    }
    
    
    /***********************26 Dec 2018 Pankaj*********************************/
    public function addPetMarket()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('type_id', 'type_id', 'required');
        $this->form_validation->set_rules('title', 'title', 'required');
        $this->form_validation->set_rules('description', 'description', 'required');
        $this->form_validation->set_rules('price', 'price', 'required');
        $this->form_validation->set_rules('sale_type', 'sale_type', 'required');
        $this->form_validation->set_rules('mobile_no', 'mobile_no', 'required');
        $this->form_validation->set_rules('country_code', 'country_code', 'required');
        $this->form_validation->set_rules('city', 'city', 'required');
        $this->form_validation->set_rules('country', 'country', 'required');
        if ($this->form_validation->run() == false) {
            $this->Api_model->responseFailed(0, ALL_FIELD_MANDATORY);
            exit();
        } else {
            $userStatus = $this->Api_model->getSingleRow('users', array(
                'id' => $this->input->post('user_id', TRUE)
            ));
            if ($userStatus) {
                if ($userStatus->status == 1) {
                    
                    $data['user_id']      = $this->input->post('user_id', TRUE);
                    $data['address']      = $this->input->post('address', TRUE);
                    $data['longitude']    = $this->input->post('longitude', TRUE);
                    $data['latitude']     = $this->input->post('latitude', TRUE);
                    $data['user_id']      = $this->input->post('user_id', TRUE);
                    $data['type_id']      = $this->input->post('type_id', TRUE);
                    $data['title']        = $this->input->post('title', TRUE);
                    $data['description']  = $this->input->post('description', TRUE);
                    $data['price']        = $this->input->post('price', TRUE);
                    $data['sale_type']    = $this->input->post('sale_type', TRUE);
                    $data['mobile_no']    = $this->input->post('mobile_no', TRUE);
                    $data['country_code'] = $this->input->post('country_code', TRUE);
                    $data['city']         = $this->input->post('city', TRUE);
                    $data['country']      = $this->input->post('country', TRUE);
                    $data['created_at']   = time();
                    
                    $pet_id = $this->Api_model->insertGetId("pet_market", $data);
                    $this->Api_model->responseFailed(1, "Pet added successfully.");
                    
                } else {
                    $this->Api_model->responseFailed(3, INACTIVATE);
                }
            } else {
                $this->Api_model->responseFailed(0, NOT_RESPONDING);
                
            }
        }
    }
    
    public function addPetMedia()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('pet_id', 'pet_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->Api_model->responseFailed(0, ALL_FIELD_MANDATORY);
            exit();
        } else {
            
            $user_id    = $this->input->post('user_id', TRUE);
            $pet_id     = $this->input->post('pet_id', TRUE);
            $userStatus = $this->Api_model->getSingleRow('users', array(
                'id' => $user_id
            ));
            if ($userStatus) {
                if ($userStatus->status == 1) {
                    if (isset($_FILES['files'])) {
                        $filesCount = count($_FILES['files']['name']);
                        for ($i = 0; $i < $filesCount; $i++) {
                            $_FILES['file']['name']     = $_FILES['files']['name'][$i];
                            $_FILES['file']['type']     = $_FILES['files']['type'][$i];
                            $_FILES['file']['tmp_name'] = $_FILES['files']['tmp_name'][$i];
                            $_FILES['file']['error']    = $_FILES['files']['error'][$i];
                            $_FILES['file']['size']     = $_FILES['files']['size'][$i];
                            // File upload configuration
                            
                            $uploadPath              = './assets/petmarket';
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
                                $uploadData[$i] = 'assets/petmarket/' . $fileData['file_name'];
                            } else {
                                $a = $this->upload->display_errors();
                                $this->Api_model->responseFailed(0, $a);
                                exit();
                            }
                        }
                        foreach ($uploadData as $uploadData) {
                            $datas['pet_id']     = $pet_id;
                            $datas['image']      = $uploadData;
                            $datas['created_at'] = time();
                            $listID              = $this->Api_model->insertGetId("pet_market_images", $datas);
                        }
                        $this->Api_model->responseFailed(1, "Pet added successfully.");
                        exit();
                    } else {
                        $this->Api_model->responseFailed(0, TRY_AGAIN);
                        exit();
                    }
                } else {
                    $this->Api_model->responseFailed(3, INACTIVATE);
                }
            } else {
                $this->Api_model->responseFailed(0, NOT_RESPONDING);
                
            }
        }
    }
    
    public function deleteMedia()
    {
        $this->form_validation->set_rules('user_id', 'media_id', 'required');
        $this->form_validation->set_rules('media_id', 'media_id', 'required');
        $this->form_validation->set_rules('pet_id', 'pet_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->Api_model->responseFailed(0, ALL_FIELD_MANDATORY);
            exit();
        } else {
            $user_id    = $this->input->post('user_id', TRUE);
            $media_id   = $this->input->post('media_id', TRUE);
            $pet_id     = $this->input->post('pet_id', TRUE);
            $userStatus = $this->Api_model->getSingleRow('users', array(
                'id' => $user_id
            ));
            if ($userStatus) {
                if ($userStatus->status == 1) {
                    $this->Api_model->deleteRecord(array(
                        'id' => $media_id,
                        'pet_id' => $pet_id
                    ), 'pet_market_images');
                    $this->Api_model->responseFailed(1, "Media deleted successfully.");
                    exit();
                } else {
                    $this->Api_model->responseFailed(3, INACTIVATE);
                }
            } else {
                $this->Api_model->responseFailed(0, NOT_RESPONDING);
                
            }
        }
    }
    
    public function getPetMarket()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('pet_type', 'user_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->Api_model->responseFailed(0, ALL_FIELD_MANDATORY);
            exit();
        }
        $user_id    = $this->input->post('user_id', TRUE);
        $pet_type   = $this->input->post('pet_type', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                //$pet=$this->Api_model->getDataByThreeTable($pet_type);
                $pet_market_data = $this->Api_model->getAllDataWhere(array(
                    'type_id' => $pet_type
                ), 'pet_market');
                if ($pet_market_data) {
                    $pets = array();
                    foreach ($pet_market_data as $pet) {
                        $pet->petImage      = $this->Api_model->getAllDataWhereConcat(array(
                            'pet_id' => $pet->id
                        ), 'pet_market_images');
                        $user_data          = $this->Api_model->getSingleRow('users', array(
                            'id' => $pet->user_id
                        ));
                        $pet->profile_pic   = $this->config->base_url() . $user_data->profile_pic;
                        // $pet->profile_pic= $this->config->base_url().$pet->profile_pic;
                        $pet->comment_count = $this->Api_model->getCount('pet_market_comments', array(
                            'pet_id' => $pet->id
                        ));
                        if ($this->Api_model->getSingleRow('pet_market_fav', array(
                            'pet_id' => $pet->id,
                            'user_id' => $user_id
                        ))) {
                            $pet->is_fav = 1;
                        } else {
                            $pet->is_fav = 0;
                        }
                        array_push($pets, $pet);
                    }
                    $this->Api_model->responseSuccess(1, GETPETMARKET, $pets);
                } else {
                    $this->Api_model->responseFailed(0, NOPETMARKET);
                    exit();
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
    }
    
    public function addFavPet()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('pet_id', 'pet_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->Api_model->responseFailed(0, ALL_FIELD_MANDATORY);
            exit();
        }
        $user_id         = $this->input->post('user_id', TRUE);
        $data['user_id'] = $user_id;
        $data['pet_id']  = $this->input->post('pet_id', TRUE);
        $userStatus      = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $id     = $this->Api_model->insertGetId("pet_market_fav", $data);
                $getFav = $this->Api_model->getSingleRow('pet_market_fav', array(
                    'pet_id' => $data['user_id'],
                    'user_id' => $data['pet_id']
                ));
                if ($getFav) {
                    $this->Api_model->responseFailed(0, "Pet is in alraedy in your favorite list.");
                    exit();
                }
                
                if ($id) {
                    $this->Api_model->responseFailed(1, "Pet added to favorite.");
                    exit();
                } else {
                    $this->Api_model->responseFailed(0, TRY_AGAIN);
                    exit();
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
    }
    
    public function removeFavPet()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('pet_id', 'pet_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->Api_model->responseFailed(0, ALL_FIELD_MANDATORY);
            exit();
        }
        $user_id    = $this->input->post('user_id', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $data['user_id'] = $user_id;
                $data['pet_id']  = $this->input->post('pet_id', TRUE);
                
                $getFav = $this->Api_model->getSingleRow('pet_market_fav', array(
                    'pet_id' => $data['pet_id'],
                    'user_id' => $data['user_id']
                ));
                if ($getFav) {
                    $this->Api_model->deleteRecord(array(
                        'pet_id' => $data['pet_id'],
                        'user_id' => $data['user_id']
                    ), 'pet_market_fav');
                    $this->Api_model->responseFailed(1, "Pet removed from your favorite list.");
                    exit();
                } else {
                    $this->Api_model->responseFailed(0, TRY_AGAIN);
                    exit();
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
        
    }
    
    
    public function addCommentPet()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('pet_id', 'pet_id', 'required');
        $this->form_validation->set_rules('comment', 'comment', 'required');
        if ($this->form_validation->run() == false) {
            $this->Api_model->responseFailed(0, ALL_FIELD_MANDATORY);
            exit();
        }
        $user_id    = $this->input->post('user_id', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $data['user_id']    = $user_id;
                $data['pet_id']     = $this->input->post('pet_id', TRUE);
                $data['comment']    = $this->input->post('comment', TRUE);
                $data['created_at'] = time();
                
                $id = $this->Api_model->insertGetId("pet_market_comments", $data);
                if ($id) {
                    $this->Api_model->responseFailed(1, ADD_COMMENT);
                    exit();
                } else {
                    $this->Api_model->responseFailed(0, TRY_AGAIN);
                    exit();
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
        
    }
    
    public function removeCommentPet()
    {
        $this->form_validation->set_rules('user_id', 'pet_id', 'required');
        $this->form_validation->set_rules('pet_id', 'pet_id', 'required');
        $this->form_validation->set_rules('comment_id', 'comment_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->Api_model->responseFailed(0, ALL_FIELD_MANDATORY);
            exit();
        }
        $user_id        = $this->input->post('user_id', TRUE);
        $data['id']     = $this->input->post('comment_id', TRUE);
        $data['pet_id'] = $this->input->post('pet_id', TRUE);
        $userStatus     = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $getFav = $this->Api_model->getSingleRow('pet_market_comments', array(
                    'pet_id' => $data['pet_id'],
                    'id' => $data['id']
                ));
                if ($getFav) {
                    $this->Api_model->deleteRecord(array(
                        'pet_id' => $data['pet_id'],
                        'id' => $data['id']
                    ), 'pet_market_comments');
                    $this->Api_model->responseFailed(1, "Comment removed successfully.");
                    exit();
                } else {
                    $this->Api_model->responseFailed(0, TRY_AGAIN);
                    exit();
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
        
    }
    
    public function getCommentPet()
    {
        
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('pet_id', 'pet_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->Api_model->responseFailed(0, ALL_FIELD_MANDATORY);
            exit();
        }
        
        $user_id    = $this->input->post('user_id', TRUE);
        $pet_id     = $this->input->post('pet_id', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $getComment = $this->Api_model->getDataByTwoTable($pet_id);
                if ($getComment) {
                    $getComments = array();
                    foreach ($getComment as $getComment) {
                        $getComment->profile_pic = $this->config->base_url() . $getComment->profile_pic;
                        array_push($getComments, $getComment);
                    }
                    $this->Api_model->responseSuccess(1, "Get all comments", $getComments);
                } else {
                    $this->Api_model->responseFailed(0, "No comments available.");
                    exit();
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
    }
    
    public function editComment()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('comment_id', 'comment_id', 'required');
        $this->form_validation->set_rules('comment', 'comment', 'required');
        if ($this->form_validation->run() == false) {
            $this->Api_model->responseFailed(0, ALL_FIELD_MANDATORY);
            exit();
        }
        $user_id         = $this->input->post('user_id', TRUE);
        $data['id']      = $this->input->post('comment_id', TRUE);
        $data['user_id'] = $user_id;
        $comment         = $this->input->post('comment', TRUE);
        $userStatus      = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $getFav = $this->Api_model->getSingleRow('pet_market_comments', array(
                    'user_id' => $data['user_id'],
                    'id' => $data['id']
                ));
                if ($getFav) {
                    $this->Api_model->updateSingleRow('pet_market_comments', array(
                        'user_id' => $data['user_id'],
                        'id' => $data['id']
                    ), array(
                        'comment' => $comment
                    ));
                    $this->Api_model->responseFailed(1, "Comment edited successfully.");
                    exit();
                } else {
                    $this->Api_model->responseFailed(0, "No comments available.");
                    exit();
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
    }
    
    public function getMyPetsMarket()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->Api_model->responseFailed(0, ALL_FIELD_MANDATORY);
            //exit();
        }
        $user_id    = $this->input->post('user_id', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                //$pet_market_data=$this->Api_model->getMyPetsMarket($user_id);
                $pet_market_data = $this->Api_model->getAllDataWhere(array(
                    'user_id' => $user_id
                ), 'pet_market');
                if ($pet_market_data) {
                    $pets = array();
                    foreach ($pet_market_data as $pet) {
                        $pet->petImage      = $this->Api_model->getAllDataWhereConcat(array(
                            'pet_id' => $pet->id
                        ), 'pet_market_images');
                        $pet->comment_count = $this->Api_model->getCount('pet_market_comments', array(
                            'pet_id' => $pet->id
                        ));
                        $pet_type           = $this->Api_model->getSingleRow('pet_type', array(
                            'id' => $pet->type_id
                        ));
                        $user_data          = $this->Api_model->getSingleRow('users', array(
                            'id' => $user_id
                        ));
                        $pet->profile_pic   = $this->config->base_url() . $user_data->profile_pic;
                        $pet->pet_name      = $pet_type->pet_name;
                        if ($this->Api_model->getSingleRow('pet_market_fav', array(
                            'pet_id' => $pet->id,
                            'user_id' => $user_id
                        ))) {
                            $pet->is_fav = 1;
                        } else {
                            $pet->is_fav = 0;
                        }
                        array_push($pets, $pet);
                    }
                    $this->Api_model->responseSuccess(1, GETPETMARKET, $pets);
                } else {
                    $this->Api_model->responseFailed(0, NOPETMARKET);
                    exit();
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
        
    }
    
    
    public function getMyFavPetsMarket()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->Api_model->responseFailed(0, ALL_FIELD_MANDATORY);
            exit();
        }
        $user_id = $this->input->post('user_id', TRUE);
        $myFav   = $this->Api_model->getAllDataWhere(array(
            'user_id' => $user_id
        ), 'pet_market_fav');
        if (!$myFav) {
            $this->Api_model->responseFailed(0, "No Pet found.");
            exit();
        }
        $pets = array();
        foreach ($myFav as $myFav) {
            $pet_id             = $myFav->pet_id;
            $pet                = $this->Api_model->getMyPetSingleMarket($pet_id);
            $pet->petImage      = $this->Api_model->getAllDataWhereConcat(array(
                'pet_id' => $pet_id
            ), 'pet_market_images');
            $pet->profile_pic   = $this->config->base_url() . $pet->profile_pic;
            $pet->comment_count = $this->Api_model->getCount('pet_market_comments', array(
                'pet_id' => $pet_id
            ));
            if ($this->Api_model->getSingleRow('pet_market_fav', array(
                'pet_id' => $pet_id,
                'user_id' => $user_id
            ))) {
                $pet->is_fav = 1;
            } else {
                $pet->is_fav = 0;
            }
            array_push($pets, $pet);
        }
        $this->Api_model->responseSuccess(1, "Get all pets", $pets);
    }
    
    public function editPetMarket()
    {
        $this->form_validation->set_rules('pet_id', 'pet_id', 'required');
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        $this->form_validation->set_rules('type_id', 'type_id', 'required');
        $this->form_validation->set_rules('title', 'title', 'required');
        $this->form_validation->set_rules('description', 'description', 'required');
        $this->form_validation->set_rules('price', 'price', 'required');
        $this->form_validation->set_rules('sale_type', 'sale_type', 'required');
        $this->form_validation->set_rules('mobile_no', 'mobile_no', 'required');
        $this->form_validation->set_rules('country_code', 'country_code', 'required');
        $this->form_validation->set_rules('city', 'city', 'required');
        $this->form_validation->set_rules('country', 'country', 'required');
        if ($this->form_validation->run() == false) {
            $this->Api_model->responseFailed(0, ALL_FIELD_MANDATORY);
            exit();
        }
        
        $user_id      = $this->input->post('user_id', TRUE);
        $pet_id       = $this->input->post('pet_id', TRUE);
        $address      = $this->input->post('address', TRUE);
        $longitude    = $this->input->post('longitude', TRUE);
        $latitude     = $this->input->post('latitude', TRUE);
        $user_id      = $this->input->post('user_id', TRUE);
        $type_id      = $this->input->post('type_id', TRUE);
        $title        = $this->input->post('title', TRUE);
        $description  = $this->input->post('description', TRUE);
        $price        = $this->input->post('price', TRUE);
        $sale_type    = $this->input->post('sale_type', TRUE);
        $mobile_no    = $this->input->post('mobile_no', TRUE);
        $country_code = $this->input->post('country_code', TRUE);
        $city         = $this->input->post('city', TRUE);
        $country      = $this->input->post('country', TRUE);
        $userStatus   = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $getPet = $this->Api_model->getSingleRow('pet_market', array(
                    'id' => $pet_id,
                    'user_id' => $user_id
                ));
                if ($getPet) {
                    $data['latitude']     = isset($latitude) ? $latitude : $getPet->latitude;
                    $data['longitude']    = isset($longitude) ? $longitude : $getPet->longitude;
                    $data['address']      = isset($address) ? $address : $getPet->address;
                    $data['type_id']      = isset($type_id) ? $type_id : $getPet->type_id;
                    $data['title']        = isset($title) ? $title : $getPet->title;
                    $data['description']  = isset($description) ? $description : $getPet->description;
                    $data['price']        = isset($price) ? $price : $getPet->price;
                    $data['sale_type']    = isset($sale_type) ? $sale_type : $getPet->sale_type;
                    $data['mobile_no']    = isset($mobile_no) ? $mobile_no : $getPet->mobile_no;
                    $data['country_code'] = isset($country_code) ? $country_code : $getPet->country_code;
                    $data['city']         = isset($city) ? $city : $getPet->city;
                    $data['country']      = isset($country) ? $country : $getPet->country;
                    
                    $this->Api_model->updateSingleRow('pet_market', array(
                        'id' => $pet_id,
                        'user_id' => $user_id
                    ), $data);
                    $this->Api_model->responseFailed(1, "Pet updated successfully.");
                    exit();
                } else {
                    $this->Api_model->responseFailed(0, TRY_AGAIN);
                    exit();
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
    }
    
    public function deletePetFromMarket()
    {
        $this->form_validation->set_rules('pet_id', 'pet_id', 'required');
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->Api_model->responseFailed(0, ALL_FIELD_MANDATORY);
            exit();
        }
        
        $user_id    = $this->input->post('user_id', TRUE);
        $pet_id     = $this->input->post('pet_id', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $getPet = $this->Api_model->getSingleRow('pet_market', array(
                    'id' => $pet_id,
                    'user_id' => $user_id
                ));
                if (!$getPet) {
                    $this->Api_model->responseFailed(0, TRY_AGAIN);
                    exit();
                } else {
                    $this->Api_model->deleteRecord(array(
                        'pet_id' => $pet_id
                    ), 'pet_market_comments');
                    $this->Api_model->deleteRecord(array(
                        'pet_id' => $pet_id
                    ), 'pet_market_fav');
                    $this->Api_model->deleteRecord(array(
                        'pet_id' => $pet_id
                    ), 'pet_market_images');
                    $this->Api_model->deleteRecord(array(
                        'id' => $pet_id
                    ), 'pet_market');
                    $this->Api_model->responseFailed(1, "Pet deleted successfully.");
                    exit();
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
        
    }
    
    
    /******************Chat******************************************/
    /*Send message (Chat)*/
    public function sendmsg()
    {
        $this->form_validation->set_rules('chat_type', 'chat_type', 'required');
        if ($this->form_validation->run() == false) {
            $this->Api_model->responseFailed(0, "Chat type not aviable");
            exit();
        }
        $user_id    = $this->input->post('sender_id', TRUE);
        $media      = $this->input->post('media', TRUE);
        $chat_type  = $this->input->post('chat_type', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                if (isset($chat_type)) {
                    $data['chat_type'] = $this->input->post('chat_type', TRUE);
                } else {
                    $data['chat_type'] = '1';
                }
                
                $data['user_id']          = $this->input->post('sender_id', TRUE);
                $data['user_id_receiver'] = $this->input->post('receiver_id', TRUE);
                $data['message']          = $this->input->post('message', TRUE);
                $data['sender_name']      = $this->input->post('sender_name', TRUE);
                $data['date']             = time();
                
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
                $updateduserimage = "";
                if ($this->upload->do_upload('media') && $this->load->library('image_lib', $config)) {
                    $updateduserimage = 'assets/images/' . $this->upload->data('file_name');
                } else {
                    //  echo $this->upload->display_errors();
                }
                
                if ($updateduserimage) {
                    $data['media'] = $updateduserimage;
                }
                
                $chatId = $this->Api_model->insertGetId(CHT_TBL, $data);
                if ($chatId) {
                    $checkUser = $this->Api_model->getSingleRow(USR_TBL, array(
                        'id' => $data['user_id']
                    ));
                    $sender    = $this->Api_model->getSingleRow(USR_TBL, array(
                        'id' => $data['user_id_receiver']
                    ));
                    
                    $msg = $checkUser->first_name . ':' . $data['message'];
                    $this->firebase_with_class($sender->device_tokan, $data['user_id'], $checkUser->first_name, "Chat", $msg);
                    
                    $get_chat  = $this->Api_model->getAllDataWhereOrWhere(array(
                        'user_id' => $data['user_id'],
                        'user_id_receiver' => $data['user_id_receiver']
                    ), array(
                        'user_id' => $data['user_id_receiver'],
                        'user_id_receiver' => $data['user_id']
                    ), CHT_TBL, 1);
                    $get_chats = array();
                    foreach ($get_chat as $get_chat) {
                        if ($get_chat->chat_type == 2) {
                            $get_chat->media = $this->config->base_url() . $get_chat->media;
                        }
                        array_push($get_chats, $get_chat);
                    }
                    $this->Api_model->responseSuccess(1, MSG_SEND, $get_chats);
                } else {
                    $this->Api_model->responseFailed(0, TRY_AGAIN);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
        }
    }
    
    public function getChat()
    {
        $this->form_validation->set_rules('sender_id', 'sender_id', 'required');
        $this->form_validation->set_rules('receiver_id', 'receiver_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->Api_model->responseFailed(0, ALL_FIELD_MANDATORY);
            exit();
        }
        
        $user_pub_id          = $this->input->post('sender_id', TRUE);
        $page                 = $this->input->post('page', TRUE);
        $user_pub_id_receiver = $this->input->post('receiver_id', TRUE);
        $userStatus           = $this->Api_model->getSingleRow('users', array(
            'id' => $user_pub_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $page     = isset($page) ? $page : 1;
                $get_chat = $this->Api_model->getChatData($user_pub_id, $user_pub_id_receiver);
                if ($get_chat) {
                    $get_chats = array();
                    foreach ($get_chat as $get_chat) {
                        if ($get_chat->chat_type == 2) {
                            $get_chat->media = $this->config->base_url() . $get_chat->media;
                        }
                        array_push($get_chats, $get_chat);
                    }
                    $this->Api_model->responseSuccess(1, GET_CHAT, $get_chats);
                } else {
                    $this->Api_model->responseFailed(0, NO_CHAT);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
    }
    
    
    public function getChatHistory()
    {
        $this->form_validation->set_rules('user_id', 'user_id', 'required');
        if ($this->form_validation->run() == false) {
            $this->Api_model->responseFailed(0, ALL_FIELD_MANDATORY);
            exit();
        }
        
        $user_id = $this->input->post('user_id', TRUE);
        
        $chkUser = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if (!$chkUser) {
            $this->Api_model->responseFailed(0, USER_NOT_FOUND);
            exit();
        }
        $chats      = array();
        $chatsn     = array();
        $chats2     = array();
        $where      = array(
            'user_id' => $user_id
        );
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $get_users = $this->Api_model->getAllDataWhere($where, CHT_TBL);
                if ($get_users) {
                    foreach ($get_users as $get_users) {
                        $chat = $this->Api_model->getSingleRowOrderBy(CHT_TBL, array(
                            'user_id' => $user_id,
                            'user_id_receiver' => $get_users->user_id_receiver
                        ));
                        if ($chat) {
                            if ($user_id == $chat->user_id_receiver) {
                                $user = $this->Api_model->getSingleRow(USR_TBL, array(
                                    'id' => $chat->user_id
                                ));
                            } else {
                                $user = $this->Api_model->getSingleRow(USR_TBL, array(
                                    'id' => $chat->user_id_receiver
                                ));
                            }
                            if ($chat->chat_type == 2) {
                                $chat->media = $this->config->base_url() . $chat->media;
                            }
                            
                            $chat->userName         = $user->first_name;
                            $chat->user_id_receiver = $user->id;
                            $chat->userImage        = $this->config->base_url() . $user->profile_pic;
                            array_push($chats, $chat);
                        }
                    }
                    $chatsn = array_unique($chats, SORT_REGULAR);
                }
                $chats1s   = array();
                $where     = array(
                    'user_id_receiver' => $user_id
                );
                $get_users = $this->Api_model->getAllDataWhere($where, CHT_TBL);
                if ($get_users) {
                    foreach ($get_users as $get_users) {
                        $chat1 = $this->Api_model->getSingleRowOrderBy(CHT_TBL, array(
                            'user_id_receiver' => $user_id,
                            'user_id' => $get_users->user_id
                        ));
                        if ($chat1) {
                            if ($user_id == $chat1->user_id_receiver) {
                                $user = $this->Api_model->getSingleRow(USR_TBL, array(
                                    'id' => $chat1->user_id
                                ));
                            } else {
                                $user = $this->Api_model->getSingleRow(USR_TBL, array(
                                    'id' => $chat1->user_id_receiver
                                ));
                            }
                            if ($chat1->chat_type == 2) {
                                $chat1->media = $this->config->base_url() . $chat1->media;
                            }
                            $chat1->userName         = $user->first_name;
                            $chat1->user_id_receiver = $user->id;
                            $chat1->userImage        = $this->config->base_url() . $user->profile_pic;
                            array_push($chats1s, $chat1);
                        }
                    }
                    $chats2 = array_unique($chats1s, SORT_REGULAR);
                }
                $chatT = array_merge($chatsn, $chats2);
                if (empty($chatT)) {
                    $this->Api_model->responseFailed(0, NO_CHAT);
                } else {
                    $chatT = array_reverse($chatT);
                    array_multisort(array_column($chatT, 'id'), SORT_DESC, $chatT);
                    function super_unique($array, $key)
                    {
                      $temp_array = [];
                        foreach ($array as &$v) {
                            if (!isset($temp_array[$v->$key]))
                                $temp_array[$v->$key] =& $v;
                        }
                        $array = array_values($temp_array);
                        return $array;
                    }
                    $chatT = super_unique($chatT, 'user_id_receiver');
                    $this->Api_model->responseSuccess(1, CHAT_HST, $chatT);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
    }
    
    public function getCountry()
    {
        $user_id    = $this->input->post('user_id');
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $country = $this->Api_model->getAllDataColumn('users ', 'country', 'country != ""');
                if ($country) {
                    $this->Api_model->responseSuccess(1, CON_LIST, $country);
                } else {
                    $this->Api_model->responseFailed(0, NOT_RESPONDING);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
        }
    }
    /***********************26 Dec 2018 Pankaj*********************************/
    
    public function sendReminderNotification()
    {
        $getNotifications = $this->Api_model->getAllDataWhere(array(
            'flag' => 0
        ), 'notification');
        if ($getNotifications) {
            foreach ($getNotifications as $getNotifications) {
                if ($getNotifications->sentTimestamp < time()) {
                    $this->Api_model->updateSingleRow('notification', array(
                        'ntfID' => $getNotifications->ntfID,
                        'flag' => 0
                    ), array(
                        'flag' => 1
                    ));
                    $getowner = $this->Api_model->getSingleRow('users', array(
                        'id' => $getNotifications->user_id
                    ));
                    
                    $device_token = $getowner->device_tokan;
                    $msg1         = "Get ready to your pet for" . $getNotifications->content . " on " . $getNotifications->trueDate . " " . $getNotifications->trueTime;
                    // $this->firebase_notification($getNotifications->user_id, "Reminder", $msg1);
                    $this->firebase_with_class($device_token, '', '', "Reminder", $msg1);
                }
            }
        }
    }
    
    /***********************03 jan 2019 Hemendra*********************************/
    public function getMarketFilterItem()
    {
        $data['pet_type'] = $this->Api_model->getAllDataColumn('pet_type', 'pet_name,id', 'pet_name != ""');
        $data['city']     = $this->Api_model->getAllDataColumn('pet_market', 'city', 'city != ""');
        $data['country']  = $this->Api_model->getAllDataColumn('pet_market', 'country', 'country != ""');
        $data['state']    = $this->Api_model->getAllDataColumn('pet_market', 'state', 'state != ""');
        $data['breeds']   = array();
        $this->Api_model->responseSuccess(1, RECORD_FILTER, $data);
    }
    
    public function petMarketFilter()
    {
        $Data    = json_decode(file_get_contents('php://input'), true);
        $user_id = $Data['user_id'];
        
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $pet = $this->Api_model->getFilterMarcket('pet_market', $Data, $user_id);
                if ($pet) {
                    $petData = array();
                    foreach ($pet as $pets) {
                        if ($user_id != $pets->user_id) {
                            if ($pets->status == 0) {
                                
                            } else {
                                $table          = 'pet_type';
                                $condition      = array(
                                    'id' => $pets->type_id
                                );
                                $my_pet_type    = $this->Api_model->getSingleRow($table, $condition);
                                $pets->pet_name = $my_pet_type->pet_name;
                                $pets->petImage = $this->Api_model->getAllDataWhereConcat(array(
                                    'pet_id' => $pets->id
                                ), 'pet_market_images');
                                
                                $userData            = $this->Api_model->getSingleRow('users', array(
                                    'id' => $pets->user_id
                                ));
                                $pets->profile_pic   = $this->config->base_url() . $userData->profile_pic;
                                $pets->first_name    = $userData->first_name;
                                $pets->comment_count = $this->Api_model->getCount('pet_market_comments', array(
                                    'pet_id' => $pets->id
                                ));
                                if ($this->Api_model->getSingleRow('pet_market_fav', array(
                                    'pet_id' => $pets->id,
                                    'user_id' => $user_id
                                ))) {
                                    $pets->is_fav = 1;
                                } else {
                                    $pets->is_fav = 0;
                                }
                                array_push($petData, $pets);
                            }
                        }
                    }
                    $this->Api_model->responseSuccess(1, ALL_PET_MARCKET, $petData);
                } else {
                    $this->Api_model->responseFailed(0, NO_RECORD);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
        }
    }
    
    public function getPostById()
    {
        $postID     = $this->input->post('postID', TRUE);
        $user_id    = $this->input->post('user_id', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $get_post = $this->Api_model->getSingleRow('post', array(
                    'postID' => $postID
                ));
                if ($get_post) {
                    
                    $postID      = $get_post->postID;
                    $userId      = $get_post->user_id;
                    $contentType = 'post';
                    
                    if ($get_post->media) {
                        $get_post->media = $this->config->base_url() . $get_post->media;
                    }
                    
                    $check_like = $this->Api_model->check_like($postID, $user_id);
                    if ($check_like) {
                        $get_post->is_like = 'true';
                    } else {
                        $get_post->is_like = 'false';
                    }
                    $check_abuse = $this->Api_model->get_abuse($user_id, $postID);
                    
                    if ($check_abuse) {
                        $get_post->is_abuse = 'true';
                    } else {
                        $get_post->is_abuse = 'false';
                    }
                    
                    $check_user = $this->Api_model->get_user($user_id);
                    if ($check_user->profile_pic) {
                        $get_post->user_image = $this->config->base_url() . $check_user->profile_pic;
                    } else {
                        $get_post->user_image = $this->config->base_url() . 'assets/images/dummyuser.png';
                    }
                    $get_post->user_name = $check_user->first_name;
                    
                    $where           = array(
                        'postID' => $postID
                    );
                    $likes           = $this->Api_model->no_of_records('likes', $where);
                    $get_post->likes = $likes;
                    
                    $where              = array(
                        'postID' => $postID
                    );
                    $comments           = $this->Api_model->no_of_records('comments', $where);
                    $get_post->comments = $comments;
                    
                    $get_post->thumb_image = $this->config->base_url() . 'assets/images/thumb.jpg';
                    
                    
                    $this->Api_model->responseSuccess(1, GET_POST, $get_post);
                } else {
                    $this->Api_model->responseFailed(0, NO_RECORD);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
    }
    /******************************* shopify *******************************/
    
    
    function addTocartShopify()
    {
        $this->form_validation->set_rules('product_name', 'Product Name', 'required');
        $this->form_validation->set_rules('product_id', 'Product Id', 'required');
        $this->form_validation->set_rules('price', 'Price', 'required');
        $this->form_validation->set_rules('product_desc', 'Product description', 'required');
        $this->form_validation->set_rules('quantity', 'Product description', 'required');
        $this->form_validation->set_rules('product_img', 'Product Image', 'required');
        
        if ($this->form_validation->run() == FALSE) {
            $errors = array_values($this->form_validation->error_array());
            //$this->response(["success" => false, "message" =>$errors[0]]);
            $this->Api_model->response(0, $errors[0]);
        } else {
            $userStatus = $this->Api_model->getSingleRow('users', array(
                'id' => $this->input->post('user_id', TRUE)
            ));
            if ($userStatus) {
                if ($userStatus->status == 1) {
                    $data['user_id']      = $this->input->post('user_id', TRUE);
                    $data['product_id']   = $this->input->post('product_id', TRUE);
                    $data['product_name'] = $this->input->post('product_name', TRUE);
                    $data['price']        = $this->input->post('price', TRUE);
                    $data['quantity']     = $this->input->post('quantity', TRUE);
                    $data['product_desc'] = $this->input->post('product_desc', TRUE);
                    $data['product_img']  = $this->input->post('product_img', TRUE);
                    $data['created_at']   = time();
                    $data['updated_at']   = time();
                    $getId                = $this->Api_model->insertGetId('product_basket', $data);
                    
                    if ($getId) {
                        $this->Api_model->responseFailed(1, CART_UPDATE);
                    } else {
                        $this->Api_model->responseFailed(0, NOT_RESPONDING);
                    }
                } else {
                    $this->Api_model->responseFailed(3, INACTIVATE);
                }
            } else {
                $this->Api_model->responseFailed(0, NOT_RESPONDING);
                
            }
        }
    }
    
    public function getMyCartShopify()
    {
        $user_id    = $this->input->post('user_id', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $get_cart = $this->Api_model->getAllDataWhere(array(
                    'user_id' => $user_id
                ), 'product_basket');
                
                if ($get_cart) {
                    
                    $this->Api_model->responseSuccess(1, GET_CART, $get_cart);
                    
                } else {
                    
                    $this->Api_model->responseFailed(0, CART_EMPTY);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
        
    }
    
    
    public function getMyShopifyOrder()
    {
        $user_id    = $this->input->post('user_id', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $get_order = $this->Api_model->getAllDataWhere(array(
                    'user_id' => $user_id
                ), 'shopifyOrders');
                
                if (empty($get_order)) {
                    $this->Api_model->responseFailed(0, GET_ORDERS_FAIL);
                } else {
                    
                    $this->Api_model->responseSuccess(1, GET_ORDERS, $get_order);
                }
                
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
            
        }
    }
    
    public function getAllNearByVss()
    {
        $id      = $this->input->post('nearby_id', TRUE);
        $user_id = $this->input->post('user_id', TRUE);
        
        
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        if ($userStatus) {
            if ($userStatus->status == 1) {
                $data = $this->Api_model->getAllDataWhere(array(
                    'nearby_id' => $id,
                    'status' => 1
                ), 'nearby_vss');
                
                if (empty($data)) {
                    $this->Api_model->responseFailed(0, 'No data found');
                } else {
                    $datas = array();
                    foreach ($data as $data) {
                        $data->image_path = $this->config->base_url() . $data->image_path;
                        array_push($datas, $data);
                    }
                    $this->Api_model->responseSuccess(1, 'Get All Data', $datas);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
        }
    }
  
 public function search()
    {
        $user_id    = $this->input->post('user_id', TRUE);
        $p_name    = $this->input->post('p_name', TRUE);
        $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
        
        if ($userStatus) {
            if ($userStatus->status == 1) {
                if ($p_name) {
                    $products = $this->Api_model->getAllDataWhereProducts1($p_name, 'food_product');
                }
                if ($products) {
                    $product = array();
                    foreach ($products as $products) {
                        if ($products['color'] != "") {
                            $color = explode(',', $products['color']);
                        } else {
                            $color = array();
                        }
                        if ($products['size'] != "") {
                            $size = explode(',', $products['size']);
                        } else {
                            $size = array();
                        }
                        $products['color']        = $color;
                        $discount                = $products['discount'];
                        $discount_amount         = ($discount * $products['p_rate_sale']) / 100;
                        $products['final_amount']  = $products['p_rate_sale'] - $discount_amount;
                        $products['size']         = $size;
                        $products['proImage']      = $this->Api_model->getAllDataWhereConcat(array(
                            'p_id' => $products['p_id']
                        ), 'pro_images');
                        $currency_setting        = $this->Api_model->getSingleRow('currency_setting', array(
                            'status' => 1
                        ));
                        // $get_cat                 = $this->Api_model->getSingleRow('pet_type', array(
                        //     'id' => $products['p_type']
                        // ));
                        // $get_sub_cat             = $this->Api_model->getSingleRow('pro_cat', array(
                        //     'id' => $products['p_pet_type']
                        // ));
                        $get_brand               = $this->Api_model->getSingleRow('food_company', array(
                            'c_id' => $products['c_id']
                        ));
                        
                        $products['p_type']        = $products['p_type'];
                        $products['p_pet_type']    = $products['p_pet_type'];
                       

                        // $products['p_type']        = $get_cat->pet_name;
                        // $products['p_pet_type']    = $get_sub_cat->cat_title;
                        $products['c_name']        = $get_brand->c_name;
                        $products['currency_type'] = $currency_setting->currency_symbol;
                       $productImage= $products['proImage'];
                        // print_r($productImage[0]->image);
                        $products['img_path']      = $productImage[0]->image;
                        
                        $data = $this->Api_model->getAllDataWhere(array(
                            'product_id' => $products['p_id']
                        ), 'review_product');
                        if (empty($data)) {
                            $products['reviewlist'] = $data;
                        } else {
                            $datas = array();
                            foreach ($data as $data) {
                                $table           = 'users';
                                $condition       = array(
                                    'id' => $data->user_id
                                );
                                $users           = $this->Api_model->getSingleRow($table, $condition);
                                $data->user_name = $users->first_name;
                                array_push($datas, $data);
                            }
                            $products['reviewlist'] = $datas;
                        }
                        array_push($product, $products);
                    }
                    $this->Api_model->responseSuccess(1, ALL_PRODUCTS, $product);
                } else {
                    $this->Api_model->responseFailed(0, NOT_RESPONDING);
                }
            } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        } else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
        }
    }

 public function homedata()
   {
      $user_id = $this->input->post('user_id', TRUE);
      $userStatus = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
     
      if ($userStatus) {
      if ($userStatus->status == 1) {

      $popular  = 'Popular';
      $products = $this->Api_model->getAllDataaWhere1112(array('p_cat_type' => $popular),'food_product');
    
    $product = array();
    foreach ($products as $products) {
        //   if ($products->quantity == "" || $products->quantity <= 0) {        
        // } else {   
        if ($products->color != "") {
            $color = explode(',', $products->color);
        } else {
            $color = array();
        }
        if ($products->size != "") {
            $size = explode(',', $products->size);
        } else {
            $size = array();
        }

        $quantity    = $products->quantity;
        $products->color         = $color;
        $discount                = $products->discount;
        $discount_amount         = ($discount * $products->p_rate_sale) / 100;
        $products->final_amount  = $products->p_rate_sale - $discount_amount;
        $products->size          = $size;
        $products->proImage      = $this->Api_model->getAllDataWhereConcat(array(
            'p_id' => $products->p_id
        ), 'pro_images');
        $currency_setting        = $this->Api_model->getSingleRow('currency_setting', array(
            'status' => 1
        ));
        $get_brand               = $this->Api_model->getSingleRow('food_company', array(
            'c_id' => $products->c_id
        ));

        $products->p_type        = $products->p_type;
        $products->p_pet_type    = $products->p_pet_type;
        $products->c_name        = $get_brand->c_name;
        $products->currency_type = $currency_setting->currency_symbol;

        if($products->proImage)
        {
        $products->img_path      = $products->proImage[0]->image;

        }else
        {
            $products->img_path=""; 
        }

    $data = $this->Api_model->getAllDataWhere(array(
            'product_id' => $products->p_id
        ), 'review_product');
        if (empty($data)) {
            $products->reviewlist = $data;
        } else {
            $datas = array();
            foreach ($data as $data) {
                $table           = 'users';
                $condition       = array(
                    'id' => $data->user_id
                );
                $users           = $this->Api_model->getSingleRow($table, $condition);
                $data->user_name = $users->first_name;
                array_push($datas, $data);
            }
            $products->reviewlist = $datas;
        }
        array_push($product, $products);
    // }
    }

    $data['popular_products']=$product;



      $newfresh  = 'New/Fresh';
      $productsss = $this->Api_model->getAllDataaWhere1112(array('p_cat_type' => $newfresh),'food_product');
    
    $productss = array();
    foreach ($productsss as $productsss) {
        //   if ($productsss->quantity == "" || $productsss->quantity <= 0) {        
        // } else {   
        if ($productsss->color != "") {
            $color = explode(',', $productsss->color);
        } else {
            $color = array();
        }
        if ($productsss->size != "") {
            $size = explode(',', $productsss->size);
        } else {
            $size = array();
        }

        $quantity                  = $productsss->quantity;
        $productsss->color         = $color;
        $discount                  = $productsss->discount;
        $discount_amount           = ($discount * $productsss->p_rate_sale) / 100;
        $productsss->final_amount  = $productsss->p_rate_sale - $discount_amount;
        $productsss->size          = $size;
        $productsss->proImage      = $this->Api_model->getAllDataWhereConcat(array(
            'p_id' => $productsss->p_id
        ), 'pro_images');
        $currency_setting        = $this->Api_model->getSingleRow('currency_setting', array(
            'status' => 1
        ));
        $get_brand               = $this->Api_model->getSingleRow('food_company', array(
            'c_id' => $productsss->c_id
        ));

        $productsss->p_type        = $productsss->p_type;
        $productsss->p_pet_type    = $productsss->p_pet_type;
        $productsss->c_name        = $get_brand->c_name;
        $productsss->currency_type = $currency_setting->currency_symbol;

        if($productsss->proImage)
        {
        $productsss->img_path      = $productsss->proImage[0]->image;

        }else
        {
            $productsss->img_path=""; 
        }

    $datas = $this->Api_model->getAllDataWhere(array(
            'product_id' => $productsss->p_id
        ), 'review_product');
        if (empty($datas)) {
            $productsss->reviewlist = $datas;
        } else {
            $datass = array();
            foreach ($datas as $datas) {
                $table           = 'users';
                $condition       = array(
                    'id' => $datas->user_id
                );
                $users           = $this->Api_model->getSingleRow($table, $condition);
                $datas->user_name = $users->first_name;
                array_push($datass, $datas);
            }
            $productsss->reviewlist = $datass;
        }
        array_push($productss, $productsss);
    // }
    }
 $data['New_Fresh']=$productss;


     $offer =$this->Api_model->getAllDataWhere(array('status'=>1),'offer');
     $offers=array();
     foreach ($offer as $offer) {
     $data1 =$this->Api_model->getAllDataWhere(array('p_id'=>$offer->p_name),'food_product');

if ($data1[0]->discount!=$offer->discount) {
      $daata= $this->Api_model->updateSingleRow('food_product', array('p_id' => $offer->p_name), array('discount' => $offer->discount));

    }

   
     $data1[0]->proImage      = $this->Api_model->getAllDataWhereConcat(array(
            'p_id' => $data1[0]->p_id
        ), 'pro_images');

     $get_brand               = $this->Api_model->getSingleRow('food_company', array(
            'c_id' => $productsss->c_id
        ));
     
       
        if($data1[0]->proImage)
        {
        $data1[0]->img_path      = $data1[0]->proImage[0]->image;

        }else
        {
            $data1[0]->img_path=""; 
        }     

     $data12 =$this->Api_model->getAllDataWhere(array('p_id'=>$offer->p_name),'food_product');
     $offer->p_name =$data1[0]->p_name;
     $offer->p_id=$data1[0]->p_id;
       
      $data12[0]->c_name=$get_brand->c_name;
     $offer->products=$data12;
     
     $offer222=$offer->products;

     $image1=$data1[0]->proImage;
     $offer222[0]->img_path=$image1[0]->image;

    $offer->image=$this->config->base_url().$offer->image;
     array_push($offers, $offer);  
      }
     $data['offer']=$offers;
 

      $brand= $this->Api_model->getAllDataWhereGroupBy(array('status'=>1),"c_name",'food_company');
      $brands=array();
      foreach ($brand as $brand) {
        
          $brand->c_img_path=$this->config->base_url().$brand->c_img_path;
          array_push($brands, $brand);
      }
      $data['brand']=$brands;
      $category = $this->Api_model->getAllData('pet_type');
      $categorys=array();
      foreach ($category as $category) {
          $category->pet_image=$this->config->base_url().$category->pet_image;
          array_push($categorys, $category);
      }
      $data['category']=$categorys;

      $banner = $this->Api_model->getAllData('banner');
      $banners=array();
      foreach ($banner as $banner) {
          $banner->b_image=$this->config->base_url().$banner->b_image;
          array_push($banners, $banner);
      }
      $data['banner']=$banners;

      $contact = $this->Api_model->getAllData('contact_info');
      if ($contact) {
          $data['contact']=end($contact);
      }else{
         $contacts = array();
          $data['contact']=(object)$contacts;
      }
    
      $breed = $this->Api_model->getAllData('breed_info');
      $breedss=array();
       foreach ($breed as $breed) {
          $breed->image=$this->config->base_url().$breed->image;
          array_push($breedss, $breed);
      }
      if ($breed) {
         $data['breed']=end($breedss);
      }else{
         $data['breed'] =(object)$breedss;
      }
      

      if (empty($data)) {
            $this->Api_model->responseFailed(0, 'No data found', $data);
        } else {
            $this->Api_model->responseSuccess(1, 'Get All Data', $data);
        }
        
   } else {
                $this->Api_model->responseFailed(3, INACTIVATE);
            }
        }
         else {
            $this->Api_model->responseFailed(0, NOT_RESPONDING);
        }
    }



 
    public function addressFill1()
    {
        $user_id = $this->input->post('user_id', TRUE);
        
    $data = $this->Api_model->getAllDataWhere(array('user_id' => $user_id), 'addressfill');
        if (empty($data)) {
            $this->Api_model->responseFailed(0, 'No data found', $data);
        } else {
            $this->Api_model->responseSuccess(1, 'Get All Data', $data);
        }
    }
}



?>