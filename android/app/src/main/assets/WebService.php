<?php
if (!defined('BASEPATH'))
	exit('No direct script access allowed');

class WebService extends CI_Controller {

	public function __construct() {
		parent::__construct();
		$this -> load -> library('session');
		$this -> load -> database();
		$this -> load -> library('form_validation');
		 /*Added By Varun_Andro*/
		 $this -> load-> library('image_lib');
		$this -> load -> model('Api_model');

		//Reminders Functions

		$this->load-> model('Db');
        header('Access-Control-Allow-Origin: *');
		header("Access-Control-Allow-Methods: GET, POST, OPTIONS, PUT, DELETE");
	}
 /*Added By Varun_Andro*/
 	public function index()
	{
		echo"Hello PetCare";
	}

/*Login Api user can send mobile number, device id, device type, device tokan, os type and otp.. 
This api provided user data*/
	public function sendOTP()
	{

		$mobile_no = $this->input->post('mobile_no',TRUE);
		$device_id = $this->input->post('device_id',TRUE);
		$otp = $this->input->post('otp',TRUE);
		$os_type = $this->input->post('os_type',TRUE);
		$device_tokan = $this->input->post('device_tokan',TRUE);
		$country_code = $this->input->post('country_code',TRUE);
		// $longi = $this->input->post('longi',TRUE);
	    $table= 'users';       
        $condition = array('mobile_no'=>$mobile_no);	
      	
        $check_user = $this->Api_model->getSingleRow($table, $condition);      
	
		if ($check_user) 
		{
			$data = array('mobile_no'=>$mobile_no,'device_id'=>$device_id,'os_type'=>$os_type,'device_tokan'=>$device_tokan,'country_code'=>$country_code);//,'longi'=>$longi
			$this->Api_model->updateSingleRow($table, array('mobile_no'=>$mobile_no), $data);      
			$check_user = $this->Api_model->getSingleRow($table, array('mobile_no'=>$mobile_no));      
			$check_user->profile_pic=$this->config->base_url().$check_user->profile_pic;
			$this->Api_model->responseSuccess(1, OTP_MSG, $check_user);

		}
		else
		{
			$data = array('mobile_no'=>$mobile_no,'device_id'=>$device_id,'os_type'=>$os_type,'device_tokan'=>$device_tokan);//,'lati'=>$lati,'longi'=>$longi
			$user_id = $this->Api_model->insertGetId($table, $data);      
			if ($user_id) 
			{
				$check_user = $this->Api_model->getSingleRow($table, array('id'=>$user_id));      
			$check_user->profile_pic=$this->config->base_url().$check_user->profile_pic;
				$this->Api_model->responseSuccess(1, OTP_MSG, $check_user);
			}
			else
			{
				$this->Api_model->responseFailed(0, NOT_RESPONDING);
			}
		}
	
	}

/*Update Profile Api user can send all perameter for update. Update user record where user id. 
This api provided user data*/
	public function updateProfileUser()
	{
		$user_id = $this->input->post('user_id',TRUE);
		$mobile_no = $this->input->post('mobile_no',TRUE);
		$first_name = $this->input->post('first_name',TRUE);
		$last_name = $this->input->post('last_name',TRUE);
		$email = $this->input->post('email',TRUE);
		$address = $this->input->post('address',TRUE);
		$city = $this->input->post('city',TRUE);
		$state = $this->input->post('state',TRUE);
		$postcode = $this->input->post('postcode',TRUE);
		$country = $this->input->post('country',TRUE);
		$lati = $this->input->post('lati',TRUE);
		$longi = $this->input->post('longi',TRUE);
		$profile_pic = $this->input->post('profile_pic',TRUE);

		if ($user_id!=''||$user_id!=NULL) 
		{
			$table= 'users';       
        	$condition = array('id'=>$user_id);	
      	
        $check_user = $this->Api_model->getSingleRow($table, $condition);
        if ($check_user)
         {
         	 $this->load->library('upload');
  
             $config['upload_path']   = './assets/images/'; 
             $config['allowed_types'] = 'gif|jpg|jpeg|png';
             $config['max_size']      = 10000; 
             $config['file_name']     = time();
             $this->upload->initialize($config);
             $profileimage="";
             if ( $this->upload->do_upload('profile_pic'))
             {
              $profileimage='assets/images/'.$this->upload->data('file_name'); 
             }
             else   
             {

             }

	        if($profileimage)
	        {
	        	$data['profile_pic']= $profileimage;
	        }
         	$data['mobile_no'] = $mobile_no;
         	$data['first_name'] = $first_name;
			$data['last_name'] = $last_name;
			$data['email'] = $email;
			$data['address'] = $address;
			$data['city'] = $city;
			$data['state'] = $state;
			$data['postcode'] = $postcode;
			$data['country'] = $country;
			$data['lati'] = $lati;
			$data['longi'] = $longi;
			
			$this->Api_model->updateSingleRow($table, array('id'=>$user_id), $data);      
			$check_user = $this->Api_model->getSingleRow($table, array('id'=>$user_id)); 
			$check_user->profile_pic=$this->config->base_url().$check_user->profile_pic;     


			// $profile['id'] = $check_user->id;
			// $profile['first_name'] = $check_user->first_name;
	
			// $profile['profile_pic']=$check_user->profile_pic;     
			$this->Api_model->responseSuccess(1, UPDATE_PROFILE, $check_user);
         }
         else
         {
			$this->Api_model->responseFailed(0, NOT_RESPONDING);
         }     
		}
		else
         {
			$this->Api_model->responseFailed(0, NOT_RESPONDING);
         } 		
	}
	/*Add Pet Api user can add pet releted infromation. add pet record where user id. 
This api provided success or failed*/
	public function add_pet()
	{
    	$user_id=$this->input->post('user_id');
    	$petName=$this->input->post('petName');
        $breed_id=$this->input->post('breed_id');
        $sex=$this->input->post('sex');
		$current_weight=$this->input->post('current_weight');
        $current_height=$this->input->post('current_height');
        $microchip_id=$this->input->post('microchip_id');
        $pet_type=$this->input->post('pet_type');
        $active_area=$this->input->post('active_area');
        $lifestyle=$this->input->post('lifestyle');
        $neutered=$this->input->post('neutered');
        $trained=$this->input->post('trained');
        $birth_date=$this->input->post('birth_date');
        $adoption_date=$this->input->post('adoption_date');
        $allergies=$this->input->post('allergies');
        $medical_conditions=$this->input->post('medical_conditions');
        $medicines=$this->input->post('medicines');
		$likes=$this->input->post('likes');
        $dislikes=$this->input->post('dislikes');
        $incidents=$this->input->post('incidents');

       

        $pet_img_path = $this->input->post('pet_img_path',TRUE);

        if (!empty($user_id) ) 
        {
        

         $this->load->library('upload');
  
             $config['upload_path']   = './assets/images/'; 
             $config['allowed_types'] = 'gif|jpg|jpeg|png';
             $config['max_size']      = 10000; 
             $config['file_name']     = time();
             $this->upload->initialize($config);
             $petimage="";
             if ( $this->upload->do_upload('pet_img_path'))
             {
              $petimage='assets/images/'.$this->upload->data('file_name'); 
             }
             else   
             {

             }

	        if($petimage)
	        {
	        	$petdata['pet_img_path']= $petimage;
	        }
	        $petdata['user_id']=$user_id;
	    	$petdata['petName']=isset($petName) ? $petName: "";
	        $petdata['breed_id']=$breed_id;
	        $petdata['sex']=isset($sex) ? $sex: "";
			$petdata['current_weight']=isset($current_weight) ? $current_weight: "";
	        $petdata['current_height']=isset($current_height) ? $current_height: "";
	        $petdata['microchip_id']=isset($microchip_id) ? $microchip_id: "";
	        $petdata['pet_type']=isset($pet_type) ? $pet_type: "";
	        $petdata['active_area']=isset($active_area) ? $active_area: "";
	        $petdata['lifestyle']=isset($lifestyle) ? $lifestyle: "";
	        $petdata['neutered']=isset($neutered) ? $neutered: "";
	        $petdata['trained']=isset($trained) ? $trained: "";
	        $petdata['birth_date']=isset($birth_date) ? $birth_date: "";
	        $petdata['adoption_date']=isset($adoption_date) ? $adoption_date: "";
	        $petdata['allergies']=isset($allergies) ? $allergies: "";
	        $petdata['medical_conditions']=isset($medical_conditions) ? $medical_conditions: "";
	        $petdata['medicines']=isset($medicines) ? $medicines: "";
			$petdata['likes']=isset($likes) ? $likes: "";
	        $petdata['dislikes']=isset($dislikes) ? $dislikes: "";
	        $petdata['incidents']=isset($incidents) ? $incidents: "";
		    $petdata['updated_stamp']= time();
		    $petdata['deleted']=false;


		    $my_user = $this->Api_model->getSingleRow('users', array('id'=>$user_id)); 

		    if (!empty($my_user)) 
		    {
		    	$petdata['city']= $my_user->city;
		    	$petdata['state']= $my_user->state;
		    	$petdata['country']= $my_user->country;
		    	$petdata['postcode']= $my_user->postcode;
		    	$petdata['lati']= $my_user->lati;
		    	$petdata['longi']= $my_user->longi;

		    }
		    

        $table = 'pet';     
       	$check = $this->Api_model->insert($table, $petdata); 
	       	if ($check) 
	       	{
	       		

	       		$this->Api_model->responseSuccessWithOutData(1, ADD_PET);
	       	    	
	        }
	        else
	        {
				$this->Api_model->responseFailed(0, NOT_RESPONDING);
	        }

        }
        else
        {
        	$this->Api_model->responseFailed(0, 'please send user id');
        }


	}
	/*Get My Pet Api user can add pet releted infromation. add pet record where user id. 
This api provided success or failed*/

	public function getMyPet()
	 {
	 	$user_id=$this->input->post('user_id', TRUE);

	 	$pets=$this->Api_model->getAllDataWhere(array('user_id'=>$user_id, 'deleted'=>false), 'pet');

	 	if($pets)
	 	{
		 	$pet = array();
		 	foreach ($pets as $pets) {

		 		$table= 'breed';       
        		$condition = array('id'=>$pets->breed_id);	
        		$check_pet = $this->Api_model->getSingleRow($table, $condition);  
        		$pets->breed_name= $check_pet->breed_name;
        		$pets->breed_target= $check_pet->target;

        		$data1=	$this->Api_model->get_manual_activity_bydate($pets->id,$user_id,date('Y-m-d'));	

        		// print_r($data1);
        		// exit();
        		if(empty($data1))
        		{
        			$pets->percent= 0;
        		}
        		else
        		{
        			$per=($data1[0]->manualactivity / $check_pet->target) * 100;
					$pets->percent= round($per, 0);
        		}
				

        		$table= 'pet_type';       
        		$condition = array('id'=>$check_pet->pet_type);	
        		$my_pet_type = $this->Api_model->getSingleRow($table, $condition);  
        		$pets->pet_type_name= $my_pet_type->pet_name;

		 		$pets->pet_img_path= $this->config->base_url().$pets->pet_img_path;

		 		$data =$this->Api_model->get_manual_activity($pets->id,$user_id);

					if (empty($data))
					{
						$pets->chart=  $data;
					} 
					else
					{
						$pets->chart=  $data;
					}

		 		array_push($pet, $pets);
		 	}
		 	

	 		$this->Api_model->responseSuccess(1, ALL_MY_PET, $pet);
	 	}
	 	else
	 	{
	 		$this->Api_model->responseFailed(0, NOT_RESPONDING);
	 	}

	 }

	
	public function getSinglePet()
	 {
		$user_id=$this->input->post('user_id', TRUE);
		$pet_id=$this->input->post('pet_id', TRUE);

		$table= 'pet';       
        $condition = array('user_id'=>$user_id,'id'=>$pet_id, 'deleted'=>false);	
      	
        $check_pet = $this->Api_model->getSingleRow($table, $condition);    

        if ($check_pet) 
        {
        		$table= 'breed';       
        		$condition = array('id'=>$check_pet->breed_id);	
        		$breed_pet = $this->Api_model->getSingleRow($table, $condition);  
        		$check_pet->breed_name= $breed_pet->breed_name;
				$check_pet->breed_target= $breed_pet->target;

				$data1=	$this->Api_model->get_manual_activity_bydate($check_pet->id,$user_id,date('Y-m-d'));	
				

				if(empty($data1))
        		{
        			$check_pet->percent= 0;
        		}
        		else
        		{
        			$per=($data1[0]->manualactivity / $breed_pet->target) * 100;
					$check_pet->percent= round($per, 0);
        		}

        		$table= 'pet_type';       
        		$condition = array('id'=>$check_pet->pet_type);	
        		$my_pet_type = $this->Api_model->getSingleRow($table, $condition);  
        		$check_pet->pet_type_name= $my_pet_type->pet_name;

        		$check_pet->pet_img_path= $this->config->base_url().$check_pet->pet_img_path;

        		$data =$this->Api_model->get_manual_activity($check_pet->id,$user_id);

					if (empty($data))
					{
						$check_pet->chart=  $data;
					} 
					else
					{
						$check_pet->chart=  $data;
					}

       	 	$this->Api_model->responseSuccess(1, ALL_MY_PET, $check_pet);
        }
        else
         {
			$this->Api_model->responseFailed(0, NOT_RESPONDING);
         }     

	 }

	 public function getPetByUser()
	 {
	 	$user_id=$this->input->post('user_id', TRUE);

	 	$pets=$this->Api_model->getAllDataWhere(array('user_id'=>$user_id, 'deleted'=>false), 'pet');

	 	if($pets)
	 	{
		 	$pet = array();
		 	foreach ($pets as $pets) {

		 		if($pets->public_private == 1)
		 		{
					
		 		}else
		 		{
		 			$table= 'breed';       
        			$condition = array('id'=>$pets->breed_id);	
        			$check_pet = $this->Api_model->getSingleRow($table, $condition);  
        			$pets->breed_name= $check_pet->breed_name;
        			$pets->breed_target= $check_pet->target;

        			$data1=	$this->Api_model->get_manual_activity_bydate($pets->id,$user_id,date('Y-m-d'));	

        			if(empty($data1))
        			{
        				$pets->percent= 0;
        			}
        			else
        			{

						$per=($data1[0]->manualactivity / $check_pet->target) * 100;
						$pets->percent= round($per, 0);

        			}
					

        			$table= 'pet_type';       
        			$condition = array('id'=>$check_pet->pet_type);	
        			$my_pet_type = $this->Api_model->getSingleRow($table, $condition);  
        			$pets->pet_type_name= $my_pet_type->pet_name;


		 			$pets->pet_img_path= $this->config->base_url().$pets->pet_img_path;


					$data =$this->Api_model->get_manual_activity($pets->id,$user_id);


					if (empty($data))
					{
						$pets->chart=  $data;
					} 
					else
					{
						$pets->chart=  $data;
					}

		 			array_push($pet, $pets);


		 		}


		 		
		 	}

		 	if ($pet != NULL) 
		 	{
	 			$this->Api_model->responseSuccess(1, ALL_MY_PET, $pet);
		 		
		 	}
		 	else
		 	{
		 		$this->Api_model->responseFailed(0, PET_NOT_AVAILABLE);
		 	}
		 	

	 	}
	 	else
	 	{
	 		$this->Api_model->responseFailed(0, NOT_RESPONDING);
	 	}

	 }

	 public function updatePublicPrivate()
	 {
	 	$user_id=$this->input->post('user_id', TRUE);
		$pet_id=$this->input->post('pet_id', TRUE);
		$public_private=$this->input->post('public_private', TRUE);
		$table= 'pet';       
        $condition = array('id'=>$pet_id, 'user_id'=> $user_id);	
      	
        $public = $this->Api_model->updateSingleRow($table, array('id'=>$pet_id), array('public_private'=>$public_private));

        if ($public)
        {
        	if($public_private == 0)
        	{
        		$this->Api_model->responseFailed(1, UPDATE_PUBLIC_PRIVATE_1);
        	}
        	else
        	{
        		$this->Api_model->responseFailed(1, UPDATE_PUBLIC_PRIVATE_2);
        	}
        	
        }
        else
        {
        	$this->Api_model->responseFailed(0, NOT_RESPONDING);
        }
	 }

	  public function updateSaleNotSale()
	 {
	 	$user_id=$this->input->post('user_id', TRUE);
		$pet_id=$this->input->post('pet_id', TRUE);
		$sel_notsel=$this->input->post('sel_notsel', TRUE);
		$table= 'pet';       
        $condition = array('id'=>$pet_id, 'user_id'=> $user_id);	
      	
        $public = $this->Api_model->updateSingleRow($table, array('id'=>$pet_id), array('sel_notsel'=>$sel_notsel));

         if ($public)
        {
        	if($sel_notsel == 0)
        	{
        		$this->Api_model->responseFailed(1, UPDATE_SALE_1);
        	}
        	else
        	{
        		$this->Api_model->responseFailed(1, UPDATE_SALE_2);
        	}
        	
        }
        else
        {
        	$this->Api_model->responseFailed(0, NOT_RESPONDING);
        }
	 }

	 public function getNearByUser()
	 {

	 	$user_id=$this->input->post('user_id', TRUE);
	 	$lati=$this->input->post('lati', TRUE);
	 	$longi=$this->input->post('longi', TRUE);

	 	$users=$this->Api_model->getAllDataWhere(array("id !=" => $user_id), 'users');
	 

	 	if($users)
	 	{
			

		 	$user = array();
		 	foreach ($users as $users) 
		 	{
		 		if($users->role_id == 1)
		 		{
					
		 		}
		 		else
		 		{
		 			$distance =$this->Api_model->distance($lati,$longi,$users->lati,$users->longi);
            		$distance=round($distance,2);

            		$distance_str="$distance";
					$users->distance= $distance_str;
		 			$users->profile_pic= $this->config->base_url().$users->profile_pic;
		 		
		 			array_push($user, $users);
		 		}
	 		
		 	}
		 	

	 		$this->Api_model->responseSuccess(1, ALL_NEAR_BY, $user);
	 	}
	 	else
	 	{
	 		$this->Api_model->responseFailed(0, NOT_RESPONDING);
	 	}

	 }

	 

	 public function getAllBreedByType()
	 {

	 	$pet_type=$this->input->post('pet_type', TRUE);
	 
	 	$breed=$this->Api_model->getAllBreed(array("pet_type"=> $pet_type) , 'breed');
	 

	 	if($breed)
	 	{
			

		 	$breeds = array();
		 	foreach ($breed as $breed) 
		 	{
	 		
		 		$breed->image_path= $this->config->base_url().$breed->image_path;
		 		
		 		array_push($breeds, $breed);
		 	}
		 	
	 		$this->Api_model->responseSuccess(1, FIND_BREED, $breeds);
	 	}
	 	else
	 	{
	 		$this->Api_model->responseFailed(0, NOT_RESPONDING);
	 	}

	 }

	public function update_pet()
	{
		$user_id=$this->input->post('user_id', TRUE);
		$pet_id=$this->input->post('pet_id', TRUE);
    	$petName=$this->input->post('petName');
        $sex=$this->input->post('sex');
		$current_weight=$this->input->post('current_weight');
        $current_height=$this->input->post('current_height');
        $microchip_id=$this->input->post('microchip_id');
        $active_area=$this->input->post('active_area');
        $lifestyle=$this->input->post('lifestyle');
        $neutered=$this->input->post('neutered');
        $trained=$this->input->post('trained');
        $birth_date=$this->input->post('birth_date');
        $adoption_date=$this->input->post('adoption_date');
        $allergies=$this->input->post('allergies');
        $medical_conditions=$this->input->post('medical_conditions');
        $medicines=$this->input->post('medicines');
		$likes=$this->input->post('likes');
        $dislikes=$this->input->post('dislikes');
        $incidents=$this->input->post('incidents');
 		$pet_img_path = $this->input->post('pet_img_path',TRUE);

         $this->load->library('upload');
  
             $config['upload_path']   = './assets/images/'; 
             $config['allowed_types'] = 'gif|jpg|jpeg|png';
             $config['max_size']      = 10000; 
             $config['file_name']     = time();
             $this->upload->initialize($config);
             $petimage="";
             if ( $this->upload->do_upload('pet_img_path'))
             {
              $petimage='assets/images/'.$this->upload->data('file_name'); 
             }
             else   
             {

             }


		  
        $table = 'pet';     
      	$check_pet = $this->Api_model->getSingleRow($table, array('id'=>$pet_id));
     	   
       	if ($check_pet) 
       	{

       		 if($petimage)
	        {
	        	$petdata['pet_img_path']= $petimage;
	        }

	        $petdata['updated_stamp']= time();

	    	$petdata['petName']=isset($petName) ? $petName: $check_pet->petName;
	        $petdata['sex']=isset($sex) ? $sex: $check_pet->sex;
			$petdata['current_weight']=isset($current_weight) ? $current_weight: $check_pet->current_weight;
	        $petdata['current_height']=isset($current_height) ? $current_height: $check_pet->current_height;
	        $petdata['microchip_id']=isset($microchip_id) ? $microchip_id: $check_pet->microchip_id;
	        $petdata['active_area']=isset($active_area) ? $active_area: $check_pet->active_area;
	        $petdata['lifestyle']=isset($lifestyle) ? $lifestyle: $check_pet->lifestyle;
	        $petdata['neutered']=isset($neutered) ? $neutered: $check_pet->neutered;
	        $petdata['trained']=isset($trained) ? $trained: $check_pet->trained;
	        $petdata['birth_date']=isset($birth_date) ? $birth_date: $check_pet->birth_date;
	        $petdata['adoption_date']=isset($adoption_date) ? $adoption_date: $check_pet->adoption_date;
	        $petdata['allergies']=isset($allergies) ? $allergies: $check_pet->allergies;
	        $petdata['medical_conditions']=isset($medical_conditions) ? $medical_conditions: $check_pet->medical_conditions;
	        $petdata['medicines']=isset($medicines) ? $medicines: $check_pet->medicines;
			$petdata['likes']=isset($likes) ? $likes: $check_pet->likes;
	        $petdata['dislikes']=isset($dislikes) ? $dislikes: $check_pet->dislikes;
	        $petdata['incidents']=isset($incidents) ? $incidents: $check_pet->incidents;


	         $my_user = $this->Api_model->getSingleRow('users', array('id'=>$user_id)); 

		    if (!empty($my_user)) 
		    {
		    	$petdata['city']= $my_user->city;
		    	$petdata['state']= $my_user->state;
		    	$petdata['country']= $my_user->country;
		    	$petdata['postcode']= $my_user->postcode;
		    	$petdata['lati']= $my_user->lati;
		    	$petdata['longi']= $my_user->longi;

		    }
		    

		    $this->Api_model->updateSingleRow($table, array('id'=>$pet_id), $petdata);
       		$this->Api_model->responseSuccessWithOutData(1, UPDATE_PET);
       	    	
        }
        else
        {
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
      $app_id=$this->input->post('app_id');
	  
      $user_id=$this->input->post('user_id');
      $pet_id=$this->input->post('pet_id');
      // $timing=$this->input->post('timing');
      $category=$this->input->post('category');
      $date=$this->input->post('date');
      $timezone=$this->input->post('timezone');
      $advance=$this->input->post('advance');
      $repeat=$this->input->post('repeat');
      $remark=$this->input->post('remark');
 
 /*setting timezone according offset */
 $offset = preg_replace('/[^0-9:+]/', '', $this->input->post('timezone'));
   
    // Calculate seconds from offset
    list($hours, $minutes) = explode(':', $offset);
    $seconds = $hours * 60 * 60 + $minutes * 60;

    // Get timezone name from seconds
    $tz = timezone_name_from_abbr('', $seconds, 1);

     // using abbr
    if($tz === false) $tz = timezone_name_from_abbr('', $seconds, 0);

    // Set timezone
    date_default_timezone_set($tz);
	
      $current_time = strtotime($date);
	   
      $data['timezone']= $timezone;
      $data['user_id']= $user_id;
     
      $data['date_string']= $date;
      $data['category']= $category;
      $data['repeat']= $repeat;
      $data['advance']= $advance;
  	  $data['status']= 0;
  	  $data['remark']= $remark;
  	  $data['pet_id']= $pet_id;

      $get_date = strtotime($date);
      
      $date_temp=date_create($date);
      $c_date= date_format($date_temp,"Y-m-d");
      $c_date= date_format($date_temp,"Y-m-d");
     
      $timing= date_format($date_temp,"h:i A");
      $data['timing']= $timing;
      
      $data['appointment_timestamp']= $current_time;
      $data['appointment_date'] = $c_date;

      $send_time= date( "Y-m-d h:i A", strtotime('-'.$advance.' minutes', $get_date));

      $check_app['timing']= $timing;

    $msg="TEST";
    if(isset($app_id) && $app_id !='')
  	{
  		$done = $this->db->where('id',$app_id)->update('appointment',$data); 		
  		if($done)
          { 
          	 $this->Api_model->responseSuccess(1, 'done', $data);
          // $arr = array('error' => 'false', 'message' => 'done','data' => $data); 
            // header('Content-Type: application/json');      
            // echo json_encode($arr);  
          }
         else
         {
         		
			 $this->Api_model->responseFailed(0, 'not done');
          //$arr = array('error' => 'true', 'message' => 'not done'); 
            // header('Content-Type: application/json');      
            // echo json_encode($arr);  
         }
    	}
  else{
  	 $done = $this->Api_model->add_appointment($data);
  	 if($done)
         { 
         	$this->Api_model->responseSuccessWithOutData(1, 'done');
         //$arr = array('error' => 'false', 'message' => 'done'); 
          // header('Content-Type: application/json');      
          // echo json_encode($arr);  
       }
       else
       {
       		$this->Api_model->responseFailed(0, 'not done');
        //$arr = array('error' => 'true', 'message' => 'not done'); 
          // header('Content-Type: application/json');      
          // echo json_encode($arr);  
       }
    }  

      $template =  "Task created";

      $user_deatils = $this->Api_model->get_user_record_by_user_id($user_id);
      $os_type= $user_deatils[0]->os_type;
      $device_id= $user_deatils[0]->device_id;

      $sendTimestamp= strtotime($send_time);
      
      $send_temp=date_create($send_time);

      $s_date= date_format($send_temp,"Y-m-d");
     
      $s_timing= date_format($send_temp,"h:i A");

      $Send_times= date( "Y-m-d h:i A", strtotime('-60 minutes', $sendTimestamp));

      $sendTimestamp= strtotime($Send_times);

       $notification =array(
               'content'  =>  $category,            
               'deviceToken' => $device_id,
               'type'=>1,
               'deviceType'=>$os_type,
               'isRead'=>0,
               'user_id'=>$user_id,
               'sentTime'=>$s_timing,
               'sentTimestamp'=>$sendTimestamp,
               'flag'=>'0',
               'title'=>'PoochPlay',
               'sentDate'=>$s_date,
               'trueTime'=>$timing,
               'trueDate'=>$c_date
              );
          $done = $this->Api_model->create_notification($notification);       
    }

     


	function change_status()
	{
	  $app_id = $this->input->post('id');
	  $status = $this->input->post('status');
	  $send = false;
	  $msg = "Invalid Request";
	  if(isset($app_id) && $app_id !='' && isset($status) && $status !='')
	  {
	    
	    if($status == 4)
	    {
	      $changed = $this->Api_model->change_status($app_id,$status);
	      $this->Api_model->responseSuccessWithOutData(1, 'Reminder deleted.');
	     
	      // $arr = array('error' => 'false', 'message' => 'Reminder deleted.');
	      // header('Content-Type: application/json');      
	      // echo json_encode($arr);
	    }
	    else
	    {
	      $my_app = $this->Api_model->get_app($app_id)->row();
	    if($my_app->repeat!=0 and $my_app->repeat!='') 
	      {
	               $user_id=$my_app->user_id;
	               $category=$my_app->category;
	               $offset =  preg_replace('/[^0-9:+]/', '', $my_app->timezone);
	              // Calculate seconds from offset
	               list($hours, $minutes) = explode(':', $offset);
	               $seconds = $hours * 60 * 60 + $minutes * 60;

	              // Get timezone name from seconds
	               $tz = timezone_name_from_abbr('', $seconds, 1);

	              // using abbr 
	               if($tz === false)
	                $tz = timezone_name_from_abbr('', $seconds, 0);

	              // Set timezone
	              date_default_timezone_set($tz);
	              /*end here 8*/

	              $date = date('Y-m-d');
	              /* dont change below anything */    

	              $current_date_c = date('Y-m-d', strtotime($date. ' + '.$my_app->repeat.' days'));
	              $date_c=date_create("$current_date_c $my_app->timing");
	              $date_c = date_format($date_c,"D, M d, Y h:i A");
	              $date = $date_c;

	              /* end here **/  
	              $timezone=$my_app->timezone;
	              $advance=$my_app->advance;
	              $repeat=$my_app->repeat;

	              $data['timezone']= $timezone;
	              $data['user_id']= $user_id;
	              $data['pet_id']= $my_app->pet_id;

	              $data['date_string']= $date_c;
	              $data['category']= $category;
	              $data['repeat']= $repeat;
	              $data['advance']= $advance;
	              $data['status']= 0;
	              $data['remark']= $my_app->remark;


	              $get_date = strtotime($date);

	              $date_temp=date_create($date);
	              $c_date= date_format($date_temp,"Y-m-d");
	              $c_date= date_format($date_temp,"Y-m-d");

	                  // timing
	              $timing= date_format($date_temp,"h:i A");

	              $current_date=strtotime($c_date);
	              
	              $data['timing']= $timing;
	              $data['appointment_timestamp']= strtotime($date_c);
	              $data['appointment_date']= $c_date;

	              $changed = $this->Api_model->change_status($app_id,$status);
	              $app_id =  $this->Api_model->add_appointment($data);

	              $send_time= date( "Y-m-d h:i A", strtotime('-'.$advance.' minutes', $current_date));
	              
	              $user_deatils = $this->Api_model->get_user_record_by_user_id($user_id);
	              $os_type= $user_deatils[0]->os_type;
	              $device_id= $user_deatils[0]->device_id;

	              $sendTimestamp= strtotime($send_time);
	              
	              $send_temp=date_create($send_time);

	              $s_date= date_format($send_temp,"Y-m-d");
	             
	              $s_timing= date_format($send_temp,"h:i A");

	              $Send_times= date( "Y-m-d h:i A", strtotime('-60 minutes', $sendTimestamp));

	      		  $sendTimestamp= strtotime($Send_times);

	               $notification =array(
	                       'content'  =>  $category,            
	                       'deviceToken' => $device_id,
	                       'type'=>1,
	                       'deviceType'=>$os_type,
	                       'isRead'=>0,
	                       'user_id'=>$user_id,
	                       'sentTime'=>$s_timing,
	                       'sentTimestamp'=>$sendTimestamp,
	                       'flag'=>'0',
	                       'title'=>'PoochPlay',
	                       'sentDate'=>$s_date,
	                       'trueTime'=>$timing,
	                       'trueDate'=>$c_date
	                      );
	                  $done = $this->Api_model->create_notification($notification); 

	                $this->Api_model->responseSuccessWithOutData(1, '');
	               // $arr = array('error' => 'false', 'message' => '');

	               // header('Content-Type: application/json');      
	               // echo json_encode($arr);

	      }
	    else
	    {
	     $changed = $this->Api_model->change_status($app_id,$status);
	     	 $this->Api_model->responseSuccessWithOutData(1, 'Success');

	         // $arr = array('error' => 'false', 'message' => 'Success');
	         // header('Content-Type: application/json');      
	         // echo json_encode($arr);  
	    }
	    }
	      
	  
	        
	  }
	  else
	  {
	  	 $this->Api_model->responseFailed(0, $msg);

	    // $arr = array('error' => 'true', 'message' => $msg);
	    // header('Content-Type: application/json');      
	    // echo json_encode($arr);
	  }
	  

	}

	function get_reminders()
		{
		  $app_id = $this->input->post('id');
		  $user_id = $this->input->post('user_id');
		  $pet_id = $this->input->post('pet_id');
		  $send = false;
		  $msg = "No Task Found";

		  if(isset($app_id) && $app_id !='')
		  {
		      	$app_data = $this->Api_model->get_app($app_id)->row();
	
	      		$table= 'pet';       
				$condition = array('user_id'=>$user_id,'id'=>$app_data->pet_id);	

				$check_pet = $this->Api_model->getSingleRow($table, $condition); 


				$app_data->pet_name= $check_pet->petName;
				$app_data->pet_img_path= $this->config->base_url().$check_pet->pet_img_path;
				$reminder = $app_data;

		      if(!empty($app_data))
		      {
		        $send = true;
		      }
		      else
		      {
		          $send = false;
		          $msg = "No Task found with this id";
		      }
		  }

		  elseif(isset($user_id) && $user_id !='' && isset($pet_id) && $pet_id !='')
		  {
		       $app_data = $this->Api_model->get_app('',$user_id,$pet_id)->result();

		        	$reminder = array();

		        foreach ($app_data as $app_data)
		       {
		      		
		      		$table= 'pet';       
        			$condition = array('user_id'=>$user_id,'id'=>$pet_id);	
        			$check_pet = $this->Api_model->getSingleRow($table, $condition);  
        			$app_data->pet_name= $check_pet->petName;
        			$app_data->pet_img_path= $this->config->base_url().$check_pet->pet_img_path;
        			array_push($reminder, $app_data);
        			
		       }

		      
		      if(!empty($app_data))
		      {
		        $send = true;
		      }
		      else
		      {
		          $send = false;
		          $msg = "No Task found with this user id";
		      }
		  }
		   elseif(isset($user_id) && $user_id !='')
		  {
		       $app_data = $this->Api_model->get_app('',$user_id,'')->result();

		       	$reminder = array();

		        foreach ($app_data as $app_data)
		       {
		      		
		      		$table= 'pet';       
        			$condition = array('user_id'=>$user_id,'id'=>$app_data->pet_id);	
        			$check_pet = $this->Api_model->getSingleRow($table, $condition);  
        			$app_data->pet_name= $check_pet->petName;
        			$app_data->pet_img_path= $this->config->base_url().$check_pet->pet_img_path;
        			array_push($reminder, $app_data);
        			
		       }

		    		     
		      if(!empty($app_data))
		      {
		        $send = true;
		      }
		      else
		      {
		          $send = false;
		          $msg = "No Task found with this user id";
		      }
		  }
		else
		{
		  $app_data = $this->Api_model->get_app()->result();

		  	$reminder = array();

		        foreach ($app_data as $app_data)
		       {
		      		
		      		$table= 'pet';       
        			$condition = array('user_id'=>$app_data->user_id,'id'=>$app_data->pet_id);	
        			$check_pet = $this->Api_model->getSingleRow($table, $condition);  
        			$app_data->pet_name= $check_pet->petName;
        			$app_data->pet_img_path= $this->config->base_url().$check_pet->pet_img_path;
        			array_push($reminder, $app_data);
        			
		       }
		      if(!empty($app_data))
		      {
		        $send = true;
		      }
		      else
		      {
		          $send = false;
		          $msg = "No Task Found";
		      }
		}

		if($send)
		{
			$this->Api_model->responseSuccess(1, 'success', $reminder);
		 // $arr = array('error' => 'false', 'message' => 'success','reminders'=> $app_data);
		  // header('Content-Type: application/json');      
		  // echo json_encode($arr);
		}
		else
		{
			$this->Api_model->responseFailed(0, $msg);
		 // $arr = array('error' => 'true', 'message' => $msg);
		   // header('Content-Type: application/json');      
		   // echo json_encode($arr);
		}


		}
	function reminder_history()
	{
	  $app_id = $this->input->post('id');
	  $user_id = $this->input->post('user_id');
	  $pet_id = $this->input->post('pet_id');
	  $send = false;
	  $msg = "No Task Found";
	 

	 if(isset($user_id) && $user_id !='' && isset($pet_id) && $pet_id !='')
	  {
	       $app_data = $this->Api_model->get_reminder_history('',$user_id,$pet_id)->result();
	       $reminder = array();

		        foreach ($app_data as $app_data)
		       {
		      		
		      		$table= 'pet';       
        			$condition = array('user_id'=>$user_id,'id'=>$pet_id);	
        			$check_pet = $this->Api_model->getSingleRow($table, $condition);  
        			$app_data->pet_name= $check_pet->petName;
        			array_push($reminder, $app_data);
        			
		       }
	      if(!empty($app_data))
	      {
	        $send = true;
	      }
	      else
	      {
	          $send = false;
	          $msg = "No Task found with this email";
	      }
	  }
	  elseif (isset($user_id) && $user_id !='')
	  {
	       $app_data = $this->Api_model->get_reminder_history('',$user_id,'')->result();
	       $reminder = array();

		        foreach ($app_data as $app_data)
		       {
		      		
		      		$table= 'pet';       
        			$condition = array('user_id'=>$user_id,'id'=>$app_data->pet_id);	
        			$check_pet = $this->Api_model->getSingleRow($table, $condition);  
        			$app_data->pet_name= $check_pet->petName;
        			array_push($reminder, $app_data);
        			
		       }
	      if(!empty($app_data))
	      {
	        $send = true;
	      }
	      else
	      {
	          $send = false;
	          $msg = "No Task found with this email";
	      }
	  }

	if($send)
	{
		$this->Api_model->responseSuccess(1, 'success',$reminder);
	  // $arr = array('error' => 'false', 'message' => 'success','reminders'=> $app_data);
	  // header('Content-Type: application/json');      
	  // echo json_encode($arr);
	}
	else
	{
		$this->Api_model->responseFailed(0, $msg);
	  // $arr = array('error' => 'true', 'message' => $msg);
	  //  header('Content-Type: application/json');      
	  //  echo json_encode($arr);
	}


	}
	/*check product*/
	public function checkProduct()
	{
		$product_id=$this->input->post('product_id', TRUE);
		$user_id=$this->input->post('user_id', TRUE);

		$checkProduct= $this->Api_model->getSingleRow('product_basket', array('product_id'=>$product_id, 'user_id'=>$user_id));
		if ($checkProduct) {

			//$app_data['quantity']= $checkProduct->quantity;
			$this->Api_model->responseSuccess(1, 'success',$checkProduct->quantity);
		}
		else
		{
			$this->Api_model->responseFailed(0, "failed");
		}
	}

	public function addMemories()
	{
		$pet_id=$this->input->post('pet_id', TRUE);
		$user_id=$this->input->post('user_id', TRUE);
		$pet_img_path=$this->input->post('pet_img_path', TRUE);
		$description=$this->input->post('description', TRUE);
		$created=$this->input->post('created', TRUE);



		if (!empty($user_id) ) 
        {
        

         $this->load->library('upload');
  
             $config['upload_path']   = './assets/images/'; 
             $config['allowed_types'] = 'gif|jpg|jpeg|png';
             $config['max_size']      = 10000; 
             $config['file_name']     = time();
             $this->upload->initialize($config);
             $petimage="";
             if ( $this->upload->do_upload('pet_img_path'))
             {
              $petimage='assets/images/'.$this->upload->data('file_name'); 
             }
             else   
             {

             }

	        if($petimage)
	        {
	        	$petdata['pet_img_path']= $petimage;
	        }
	        $petdata['user_id']=$user_id;
	        $petdata['pet_id']=$pet_id;
	    	$petdata['description']=isset($description) ? $description: "";
		    $petdata['created']= time();
       		
       		$table = 'pet_memories';     
       		$check = $this->Api_model->insert($table, $petdata); 
	       	if ($check) 
	       	{
	       		

	       		$this->Api_model->responseSuccessWithOutData(1, ADD_PET_MEMORIES);
	       	    	
	        }
	        else
	        {
				$this->Api_model->responseFailed(0, NOT_RESPONDING);
	        }

        }
        else
        {
        	$this->Api_model->responseFailed(0, 'please send user id');
        }
	}
	public function addContact()
	{
		$pet_id=$this->input->post('pet_id', TRUE);
		$user_id=$this->input->post('user_id', TRUE);
		$user_id_owner=$this->input->post('user_id_owner', TRUE);
		$description=$this->input->post('description', TRUE);
		$created=$this->input->post('created', TRUE);



		if (!empty($user_id) ) 
        {
	        $petdata['user_id']=$user_id;
	        $petdata['pet_id']=$pet_id;
	        $petdata['user_id_owner']=$user_id_owner;
	    	$petdata['description']=isset($description) ? $description: "";
		    $petdata['created']= time();
       		
       		$table = 'contact';     
       		$check = $this->Api_model->insert($table, $petdata); 
	       	if ($check) 
	       	{
	       		

	       		$this->Api_model->responseSuccessWithOutData(1, "Your request sent successfully");
	       	    	
	        }
	        else
	        {
				$this->Api_model->responseFailed(0, NOT_RESPONDING);
	        }

        }
        else
        {
        	$this->Api_model->responseFailed(0, 'please send user id');
        }
	}

	public function getMemories()
	{
		$user_id=$this->input->post('user_id', TRUE);

	 	$pets=$this->Api_model->getAllDataWhere(array('user_id'=>$user_id), 'pet_memories');

	 	if($pets)
	 	{
		 	$pet = array();
		 	foreach ($pets as $pets) {


		 		$pets->pet_img_path= $this->config->base_url().$pets->pet_img_path;

		 		array_push($pet, $pets);
		 	}
		 	

	 		$this->Api_model->responseSuccess(1, GET_PET_MEMORIES, $pet);
	 	}
	 	else
	 	{
	 		$this->Api_model->responseFailed(0, NOT_RESPONDING);
	 	}

	}
	public function getMemoriesByPet()
	{
		$user_id=$this->input->post('user_id', TRUE);
		$pet_id=$this->input->post('pet_id', TRUE);

	 	$pets=$this->Api_model->getAllDataWhere(array('user_id'=>$user_id, 'pet_id'=>$pet_id), 'pet_memories');

	 	if($pets)
	 	{
		 	$pet = array();
		 	foreach ($pets as $pets) {


		 		$pets->pet_img_path= $this->config->base_url().$pets->pet_img_path;

		 		array_push($pet, $pets);
		 	}
		 	

	 		$this->Api_model->responseSuccess(1, GET_PET_MEMORIES, $pet);
	 	}
	 	else
	 	{
	 		$this->Api_model->responseFailed(0, NOT_RESPONDING);
	 	}

	}
 /*Added By Varun_Andro*/

 /*Added By GURU JI*/

 	/*Get All Products*/
	 public function getAllProduct()
	 {
	 	$p_pet_type=$this->input->post('p_pet_type', TRUE);

	 	$products=$this->Api_model->getAllDataWhere(array('p_pet_type'=>$p_pet_type), 'food_product');

	 	if($products)
	 	{
		 	$product = array();
		 	foreach ($products as $products) {

		 		$products->img_path= $this->config->base_url().$products->img_path;

		 		$data =$this->Api_model->getAllDataWhere(array('product_id'=>$products->p_id), 'review_product');


					if (empty($data))
					{

						$products->reviewlist=  $data;
					} 
					else
					{
						$datas = array();
						foreach ($data as $data) {
							
						$table= 'users';       
        				$condition = array('id'=>$data->user_id);	
        				$users = $this->Api_model->getSingleRow($table, $condition);

						$data->user_name= $users->first_name;
						array_push($datas, $data);
						}
						
						$products->reviewlist=  $datas;
					}

		 		array_push($product, $products);
		 	}
		 	

	 		$this->Api_model->responseSuccess(1, ALL_PRODUCTS, $product);
	 	}
	 	else
	 	{
	 		$this->Api_model->responseFailed(0, NOT_RESPONDING);
	 	}

	 }

	 /*Add to Cart*/
	 public function addTocart()
	 {
	 	$data['user_id'] =$this->input->post('user_id', TRUE);
	 	$data['product_id'] =$this->input->post('product_id', TRUE);
	 	$data['quantity'] =$this->input->post('quantity', TRUE);

	 	$data['created_at']= time();
	 	$data['updated_at']= time();
	 	$getId = $this->Api_model->insertGetId('product_basket', $data);

	 	if($getId)
	 	{
	 		$this->Api_model->responseFailed(1, CART_UPDATE);
	 	}
	 	else
	 	{
	 		$this->Api_model->responseFailed(0, NOT_RESPONDING);
	 	}
	 }

	 /*Get My Cart*/
	 public function getMyCart()
	 {
	 	$user_id= $this->input->post('user_id', TRUE);

	 	$get_cart= $this->Api_model->getAllDataWhere(array('user_id'=>$user_id), 'product_basket');
	 	if($get_cart)
	 	{
	 		$get_carts= array();
		 	foreach ($get_cart as $get_cart) 
		 	{
		 		$product_id=$get_cart->product_id;
		 		$product= $this->Api_model->getSingleRow('food_product', array('p_id'=>$product_id));
		 		$quantity= $get_cart->quantity;
		 		$price= $product->p_rate;

		 		$price_dicount=$price - ($price*$product->discount)/100;

		 		$get_cart->p_name=$product->p_name;
		 		$get_cart->p_description=$product->p_description;
		 		$get_cart->p_rate=$product->p_rate;
		 		$get_cart->discount=$product->discount;
		 		$get_cart->price_dicount=$price_dicount;
		 		$get_cart->img_path=$this->config->base_url().$product->img_path;
		 		$get_cart->product_total_price= $price_dicount*$quantity;

		 		array_push($get_carts, $get_cart);
		 	}
		 	
		 	$this->Api_model->responseSuccess(1, "Get my Cart.", $get_carts);
	 	}
	 		
	 	else
	 	{
	 		$this->Api_model->responseFailed(0, NOT_RESPONDING);
	 	}
	 }

	 /*Get All Pet Type*/
	 public function getAllPetType()
	 {
	 	$get_pet_type= $this->Api_model->getAllData('pet_type');

	 	if($get_pet_type)
	 	{
		 	$get_pet_types = array();
		 	foreach ($get_pet_type as $get_pet_type) {

		 		$get_pet_type->pet_image= $this->config->base_url().$get_pet_type->pet_image;

		 		array_push($get_pet_types, $get_pet_type);
		 	}

	 		$this->Api_model->responseSuccess(1, ALL_PET, $get_pet_types);
	 	}
	 	else
	 	{
	 		$this->Api_model->responseFailed(0, NOT_RESPONDING);
	 	}
	 }

	 /*update Cart Quantity*/
	 public function updateCartQuantity()
	 {
	 	$product_id = $this->input->post('product_id',TRUE);
		$quantity = $this->input->post('quantity',TRUE);	
		$user_id = $this->input->post('user_id',TRUE);	

		$table= 'product_basket';       
        $condition = array('product_id'=>$product_id, 'user_id'=> $user_id);	
      	
        $check_basket = $this->Api_model->updateSingleRow('product_basket', array('product_id'=>$product_id), array('quantity'=>$quantity));

        if ($check_basket)
        {
        	$this->Api_model->responseFailed(1, CART_UPDATE);
        }
        else
        {
        	$this->Api_model->responseFailed(0, NOT_RESPONDING);
        }
	 }

	 /*Remove Product from Cart*/
	 public function remove_product_cart()
	 {
	 	$product_id = $this->input->post('product_id',TRUE);	
		$user_id = $this->input->post('user_id',TRUE);	

		$this->Api_model->deleteRecord(array('product_id'=>$product_id, 'user_id'=>$user_id), 'product_basket');

		$this->Api_model->responseFailed(1, REMOVE_CART);				
	 }

	 public function order()
	 {
	 	$user_id= $this->input->post('user_id', TRUE);
	 	$address= $this->input->post('address', TRUE);

	 	$get_cart= $this->Api_model->getAllDataWhere(array('user_id'=>$user_id), 'product_basket');
	 	if($get_cart)
	 	{
	 		$current_time= time().$user_id;
	 		$get_carts= array();
		 	foreach ($get_cart as $get_cart) 
		 	{
		 		$product_id=$get_cart->product_id;
		 		$product= $this->Api_model->getSingleRow('food_product', array('p_id'=>$product_id));
		 		$quantity= $get_cart->quantity;
		 		$price= $product->p_rate;


		 		$price_discount=$price - ($price*$product->discount)/100;
		 		$data['p_name']=$product->p_name;
		 		$data['quantity']=$quantity;
		 		$data['user_id']=$user_id;
		 		$data['product_id']=$product_id;
		 		$data['p_description']=$product->p_description;
		 		$data['p_rate']=$product->p_rate;
		 		$data['discount']=$product->discount;
		 		$data['discount_total']=($product->p_rate * $product->discount) /100;
		 		$data['price_discount']=round($price_discount, 2);
		 		$data['img_path']=$product->img_path;
		 		$data['order_id']=$current_time;
		 		$data['updated_at']=time();
		 		$data['created_at']=time();
		 		$data['total_price']= round($price_discount*$quantity, 2);

		 		$this->Api_model->insertGetId('order_products', $data);
		 	}

		 	$get_total_price=$this->Api_model->getSumWithWhere('total_price','order_products',array('order_id'=>$current_time));

		 	$get_total_price_Old=$this->Api_model->getSumWithWhere('p_rate','order_products',array('order_id'=>$current_time));

		 	$get_discount=$this->Api_model->getSumWithWhere('discount','order_products',array('order_id'=>$current_time));

		 	$get_discount_total=$this->Api_model->getSumWithWhere('discount_total','order_products',array('order_id'=>$current_time));

		 	if($get_total_price->total_price > 500)
		 	{
		 		$dataorder['cod_charges']=0;
		 	}
		 	else
		 	{
		 		$dataorder['cod_charges']=25;
		 	}
		 	$dataorder['order_id']=$current_time;
		 	$dataorder['total_price']=round($get_total_price_Old->p_rate, 2); 
		 	$dataorder['discount']= $get_discount_total->discount_total;
		 	$dataorder['user_id']= $user_id;
		 	$dataorder['final_price']= round($get_total_price->total_price + $dataorder['cod_charges'], 2); 
		 	$dataorder['place_date']= date('Y-m-d', strtotime("+4 days"));
		 	$dataorder['order_date']= date('Y-m-d');
		 	$dataorder['address']= $address;
		 	$dataorder['updated_at']=time();
		 	$dataorder['created_at']=time();

		 	$getID=$this->Api_model->insertGetId('orders', $dataorder);


		 	if($getID)
		 	{
		 		$getOrder = $this->Api_model->getSingleRow('orders', array('id'=>$getID));      
			

				$this->Api_model->deleteRecord(array('user_id'=>$user_id), 'product_basket');
				$this->Api_model->responseSuccess(1, 'Make Order', $getOrder);
		 		
		 	}
		 	else
		 	{
		 		$this->Api_model->responseFailed(0, 'Fail');	
		 	}
	 	}
	 	else
	 	{
	 		$this->Api_model->responseFailed(0, 'No products');
	 	}
	}

	


	/*Added By GURU JI*/
	/*Added By VARUN*/

	public function  set_manual_activity()
	{
		//echo "set_manual_activity";
			$pet_id= $this ->input ->post('pet_id');
			$user_id= $this->input->post('user_id');
			$time_stamp=  $this->input->post('time_stamp');
			$manualActivity=  $this->input->post('manualActivity');
			$unit=  $this->input->post('unit');
			$target =$this->input ->post('target');

			$remendar= $time_stamp /1000;	
			  

			 $rem= (explode(".",$remendar));


			 //print_r($rem);


			$dt = new DateTime("@$rem[0]");  // convert UNIX timestamp to PHP DateTime

			
			$date= $dt->format('Y-m-d');



			$data = array('pet_id'=>$pet_id, 'user_id'=>$user_id, 'time_stamp' =>$time_stamp, 'manualactivity'=>$manualActivity,'unit'=>$unit,'date'=>$date ,'target' =>$target);


			$return_data = array('pet_id'=>$pet_id, 'user_id'=>$user_id, 'time_stamp' =>$time_stamp, 'manualActivity'=>$manualActivity,'unit'=>$unit,'date'=>$date ,'target' =>$target);

			$data1=	$this->Api_model->get_manual_activity_bydate($pet_id,$user_id,$date );	


				
				if($data1)
				{
					//update
					//echo "update";
					$this->Api_model->update_manual_activity($pet_id,$user_id,$date,$data);


					$this->Api_model->responseSuccess(1, MANUAL_UPDATED, $return_data);

				


				}

				else{
					//insert
					//echo "insert";
					$this->Api_model->set_manual_activity($data);

					$this->Api_model->responseSuccess(1, MANUAL_INSERTED, $return_data);
					
				}




		}

	public function  get_manual_activity()
	{

			$pet_id = $this->input->post('pet_id');
			$user_id = $this->input->post('user_id');

			$data =$this->Api_model->get_manual_activity($pet_id,$user_id);


			if ($data)
			{
				$this->Api_model->responseSuccess(1, ACTIVITY_SEND, $data);
			} 
			else
			{
				$this->Api_model->responseFailed(0, ACTIVITY_SEND);
			}
	
	}


	public function getAllPets()
	 {

	 	$user_id=$this->input->post('user_id', TRUE);
	 	$lati=$this->input->post('lati', TRUE);
	 	$longi=$this->input->post('longi', TRUE);

	 
	 

		//$pets=$this->Api_model->getAllData('pet');
		$pets=$this->Api_model->getAllDataWhere(array("user_id !=" => $user_id, 'deleted'=>false), 'pet');

		 if($pets)
	 	{
		 	$pet = array();
		 	foreach ($pets as $pets) {

		 		if($pets->public_private == 1)
		 		{
					
		 		}else
		 		{

		 		$table= 'breed';       
        		$condition = array('id'=>$pets->breed_id);	
        		$check_pet = $this->Api_model->getSingleRow($table, $condition);  
        		$pets->breed_name= $check_pet->breed_name;
        		$pets->breed_target= $check_pet->target;

        		$data1=	$this->Api_model->get_manual_activity_bydate($pets->id,$pets->user_id,date('Y-m-d'));	

        		 /*print_r($data1);
        		 exit();*/
        		if(empty($data1))
        		{
        			$pets->percent= 0;
        		}
        		else
        		{
        			$per=($data1[0]->manualactivity / $check_pet->target) * 100;
					$pets->percent= round($per, 0);
        		}
				

        		$table= 'pet_type';       
        		$condition = array('id'=>$check_pet->pet_type);	
        		$my_pet_type = $this->Api_model->getSingleRow($table, $condition);  
        		$pets->pet_type_name= $my_pet_type->pet_name;

		 		$pets->pet_img_path= $this->config->base_url().$pets->pet_img_path;

		 		$data =$this->Api_model->get_manual_activity($pets->id,$pets->user_id);

					if (empty($data))
					{
						$pets->chart=  $data;
					} 
					else
					{
						$pets->chart=  $data;
					}

		 		array_push($pet, $pets);
		 		}

		 		
		 	}
		 	

	 		$this->Api_model->responseSuccess(1, ALL_MY_PET, $pet);
	 	}
	 	else
	 	{
	 		$this->Api_model->responseFailed(0, NOT_RESPONDING);
	 	}



	 	

	 }

	public function getMyOrder()
	{
		$user_id =$this->input->post('user_id', TRUE);

		$get_order=$this->Api_model->getAllDataWhere(array("user_id =" => $user_id), 'orders');

		if (empty($get_order))
		{
			$this->Api_model->responseFailed(0, GET_ORDERS_FAIL);
		} 
		else
		{
			$order = array();

				foreach ($get_order as $get_order) 
				{

					if ($get_order->status == 1) 
					{
						
 				 		$get_order->status_txt= 'PENDING';

					}
					else
					{
 						 $get_order->status_txt= 'OTHER';
					}

				 	
				 array_push($order, $get_order);

				}

			$this->Api_model->responseSuccess(1, GET_ORDERS, $order);
		}
	}
	public function getMyOrderDetails()
	{
		$user_id =$this->input->post('user_id', TRUE);
		$order_id =$this->input->post('order_id', TRUE);


		$get_order=$this->Api_model->getAllDataWhere(array("user_id =" => $user_id,"order_id =" => $order_id), 'order_products');

		if (empty($get_order))
		{
			$this->Api_model->responseFailed(0, GET_ORDERS_FAIL);
		} 
		else
		{
			$order = array();

				foreach ($get_order as $get_order) 
				{

					$get_order->img_path= $this->config->base_url().$get_order->img_path;
					
				 	array_push($order, $get_order);

				}

			$this->Api_model->responseSuccess(1, GET_ORDERS, $order);
		}
	}

	public function getFilterItem()
	{
		$filters = array();
		$citys = array();
		$states = array();
		$countrys = array();

		$breeds=$this->Api_model->getAllDataColumn('breed', 'breed_name,id', 'breed_name != ""');
		$city=$this->Api_model->getAllDataColumn('pet', 'city',  array('city != ""', 'deleted'=>false));
		$state=$this->Api_model->getAllDataColumn('pet', 'state',  array('state != ""', 'deleted'=>false));
		$country=$this->Api_model->getAllDataColumn('pet', 'country',  array('country != ""', 'deleted'=>false));



			$data['city']=$city;
			$data['country']=$country;
			$data['state']=$state;
			$data['breeds']=$breeds;
			$this->Api_model->responseSuccess(1, GET_ORDERS, $data);
	}

	public function Filter()
	{

		  $Data = json_decode(file_get_contents('php://input'));
		  $pets=$this->Api_model->getFilterData('pet', $Data);

			if($pets)
		 	{
			 	$pet = array();
			 	foreach ($pets as $pets) {

			 		if($pets->public_private == 1)
			 		{
						
			 		}else
			 		{

			 		$table= 'breed';       
	        		$condition = array('id'=>$pets->breed_id);	
	        		$check_pet = $this->Api_model->getSingleRow($table, $condition);  
	        		$pets->breed_name= $check_pet->breed_name;
	        		$pets->breed_target= $check_pet->target;

	        		$data1=	$this->Api_model->get_manual_activity_bydate($pets->id,$pets->user_id,date('Y-m-d'));	

	        		 /*print_r($data1);
	        		 exit();*/
	        		if(empty($data1))
	        		{
	        			$pets->percent= 0;
	        		}
	        		else
	        		{
	        			$per=($data1[0]->manualactivity / $check_pet->target) * 100;
						$pets->percent= round($per, 0);
	        		}
					

	        		$table= 'pet_type';       
	        		$condition = array('id'=>$check_pet->pet_type);	
	        		$my_pet_type = $this->Api_model->getSingleRow($table, $condition);  
	        		$pets->pet_type_name= $my_pet_type->pet_name;

			 		$pets->pet_img_path= $this->config->base_url().$pets->pet_img_path;

			 		$data =$this->Api_model->get_manual_activity($pets->id,$pets->user_id);

						if (empty($data))
						{
							$pets->chart=  $data;
						} 
						else
						{
							$pets->chart=  $data;
						}

			 		array_push($pet, $pets);
			 		}

			 		
			 	}
			 	

		 		$this->Api_model->responseSuccess(1, ALL_MY_PET, $pet);
		 	}
		 	else
		 	{
		 		$this->Api_model->responseFailed(0, 'No Record Found');
		 	}


		}



	public function get_post()
    {
      $limit = $this->input->post('limit', TRUE);
      $offset = $this->input->post('offset', TRUE);
      $user_id = $this->input->post('user_id', TRUE);

      $get_post=$this->Api_model->get_post($limit, $offset);
      if($get_post)
      {
        $data=array();
        foreach ($get_post as $get_post) {
          $postID=$get_post['postID'];
          $user_id=$get_post['user_id'];
          $contentType='post';


		  if($get_post['media'])
          {
          	$get_post['media']= $this->config->base_url().$get_post['media'];
          }
          $check_like=$this->Api_model->check_like($postID, $user_id);
          if($check_like)
          {
            $get_post['is_like']='true';
          }
          else
          {
            $get_post['is_like']='false';
          }

          $check_abuse=$this->Api_model->get_abuse($user_id, $postID);

          if($check_abuse)
          {
            $get_post['is_abuse']='true';
          }
          else
          {
            $get_post['is_abuse']='false';
          }

          $check_user=$this->Api_model->get_user($user_id);
          if($check_user->profile_pic)
          {
            $get_post['user_image']= $this->config->base_url().$check_user->profile_pic;
          }
          else
          {
            $get_post['user_image']= $this->config->base_url().'assets/images/dummyuser.png';
          }           
          $get_post['user_name']= $check_user->first_name;

          $where= array('postID'=>$postID);
          $likes=$this->Api_model->no_of_records('likes', $where);
          $get_post['likes'] =$likes;

          $where= array('postID'=>$postID);
          $comments=$this->Api_model->no_of_records('comments', $where);
          $get_post['comments'] =$comments;

          $get_post['thumb_image']= $this->config->base_url().'assets/images/thumb.png';

          array_push($data, $get_post);
        }
        $this->Api_model->responseSuccess(1, "get post", $data);
      }
      else
      {
         $this->Api_model->responseFailed(0, 'No Record Found');
      }
    } 

  public function get_my_post()
    {
      $limit = $this->input->post('limit', TRUE);
      $offset = $this->input->post('offset', TRUE);
      $user_id = $this->input->post('user_id', TRUE);

      $get_post=$this->Api_model->get_my_post($limit, $offset, $user_id);
      if($get_post)
      {
        $data=array();
          foreach ($get_post as $get_post) {
          $postID=$get_post['postID'];
          $user_id=$get_post['user_id'];
          $contentType='post';

          if($get_post['media'])
          {
          	$get_post['media']= $this->config->base_url().$get_post['media'];
          }

         
          $check_like=$this->Api_model->check_like($postID, $user_id);
          if($check_like)
          {
            $get_post['is_like']='true';
          }
          else
          {
            $get_post['is_like']='false';
          }
          
          $check_user=$this->Api_model->get_user($user_id);
          if($check_user->profile_pic)
          {
            $get_post['user_image']= $this->config->base_url().$check_user->profile_pic;
          }
          else
          {
            $get_post['user_image']= $this->config->base_url().'assets/images/dummyuser.png';
          }           
          $get_post['user_name']= $check_user->first_name;

          $where= array('postID'=>$postID);
          $likes=$this->Api_model->no_of_records('likes', $where);
          $get_post['likes'] =$likes;

          $where= array('postID'=>$postID);
          $comments=$this->Api_model->no_of_records('comments', $where);
          $get_post['comments'] =$comments;

          $get_post['thumb_image']= $this->config->base_url().'assets/images/thumb.jpg';

          array_push($data, $get_post);
        }
         $this->Api_model->responseSuccess(1, "get post", $data);
      }
      else
      {
       $this->Api_model->responseFailed(0, 'No Record Found');
      }
    }

    public function like()
    {
      $postID=$this->input->post('postID', TRUE);
      $user_id=$this->input->post('user_id', TRUE);

    
      $check_like=$this->Api_model->check_like($postID, $user_id);
      if($check_like)
      {
      	 $this->Api_model->responseFailed(0, 'No ALREADY_LIKE');
      }
      else
      {
        $data['postID']=$postID;
        $data['user_id']=$user_id;
        $data['createAt']=time();

        $data['id']=$this->Api_model->like($data);
		$this->Api_model->responseSuccessWithOutData(1, "LIKE_POST");
      }
    }


    public function get_comments()
    {
      $postID=$this->input->post('postID', TRUE);
      $user_id=$this->input->post('user_id', TRUE);

      $get_likes=$this->Api_model->get_comments_true($postID);

      if($get_likes)
      {
        $data= array();
        foreach ($get_likes  as $get_likes) {
 
          $user_id=$get_likes['user_id'];
          $get_user=$this->Api_model->get_user($user_id);

          $get_likes['userName'] =$get_user->first_name;
          if($get_user->profile_pic)
          {
            $get_likes['image']=$this->config->base_url().$get_user->profile_pic;
          }
          else
          {
            $get_likes['image']= $this->config->base_url().'assets/images/dummyuser.png';
          }
         array_push($data, $get_likes);
        }
         $this->Api_model->responseSuccess(1, "all_comments", $data);
      } 
      else
      {
        $this->Api_model->responseFailed(0, 'No Record Found');
      }  
    }


    public function comment()
    {
      $postID=$this->input->post('postID', TRUE);
      $user_id=$this->input->post('user_id', TRUE);
      $content=$this->input->post('content', TRUE);
    

      $get_user= $this->Api_model->get_user($user_id);
      $name=$get_user->first_name;

      $data['postID']=$postID;
      $data['user_id']=$user_id;
      $data['content']=$content;
      $data['createAt']=time();

      $data['id']=$this->Api_model->comment($data);
      if($data['id'])
      {

        $get_post=$this->Api_model->get_single_post($postID);

        $user_id= $get_post->user_id;

        $user_id= $get_post->user_id;
        $get_user= $this->Api_model->get_user($user_id);

        $userId=$get_user->id;
        $title ="PetStand";

        $msg1= "$content $name comment on your post.";

       $this->firebase_notification($userId, $title, $msg1);

         $this->Api_model->responseSuccessWithOutData(1, 'ADD_COMMENT');
      }
      else
      {
         $this->Api_model->responseFailed(0, 'TRY_AGAIN');
      } 
    }


     public function abuse_post()
    {
      $data['postID']=$this->input->post('postID', TRUE);
      $data['user_id']=$this->input->post('user_id', TRUE);
      $data['reason']=$this->input->post('reason', TRUE);

   
      $get_abuse=$this->Api_model->get_abuse($data['user_id'], $data['postID']);
      if($get_abuse)
      {
         $this->Api_model->responseFailed(0, 'REPORT_POST');
        exit();
      }

      $get_user=$this->Api_model->get_user($data['user_id']);
      $data['userName']=$get_user->first_name;

      $data['id']=$this->Api_model->abuse_post($data);

      if($data['id'])
      {
        $this->Api_model->responseSuccessWithOutData(1, 'MARK_ABUSE');
      }
      else
      {
       $this->Api_model->responseFailed(0, 'TRY_AGAIN');
      } 
    }


    public function delete_post()
    {
      $postID=$this->input->post('postID', TRUE);
      $user_id=$this->input->post('user_id', TRUE);

      $data['postID']=$postID;
      $data['user_id']=$user_id;
      $data['flag']=0;

      $check_post= $this->Api_model->check_post($postID,$user_id);
      if($check_post)
      {
        $this->Api_model->delete_post($data);
        $this->Api_model->responseSuccessWithOutData(1, 'DELTE_POST');
                $msg1= "Delete your post.";
                 $title ="PetStand";

       $this->firebase_notification($user_id, $title, $msg1);
      }
      else
      {
        $this->common->responseFailed(0, 'TRY_AGAIN');
      }  
    }


     public function firebase_notification($user_id, $title, $msg1)
    {     
    
        $user = $this->db->where('id',$user_id)->get('users')->row();
        $registrationIds = $user->device_tokan;
        $mobile_sent = $user_id;

        $API_ACCESS_KEY= 'AAAAYHQBg24:APA91bFTFoeZgNVquVY811rY9VvhgWXu4lZyTdV6QKaqbxNXN0na4zt5MLbuGTIa4KaJezjgYBsmgBohsdIeZ8kbW2iRBXKjQzqDcu0GP_5ri3BnR0ro2UXi4JHxhOwgQFj6EOKhyMEZ';
     
     $msg = array
          (
                'body'    => $msg1,
                'title'   => $title,
                'icon'    => 'myicon',/*Default Icon*/
                'sound'   =>  'mySound'/*Default sound*/
          );
    $fields = array
            (
                'to'        => $registrationIds,
                'notification'    => $msg
            );
    
    
    $headers = array
            (
                'Authorization: key=' . $API_ACCESS_KEY,
                'Content-Type: application/json'
            );
        #Send Reponse To FireBase Server    
        $ch = curl_init();
        curl_setopt( $ch,CURLOPT_URL, 'https://fcm.googleapis.com/fcm/send' );
        curl_setopt( $ch,CURLOPT_POST, true );
        curl_setopt( $ch,CURLOPT_HTTPHEADER, $headers );
        curl_setopt( $ch,CURLOPT_RETURNTRANSFER, true );
        curl_setopt( $ch,CURLOPT_SSL_VERIFYPEER, false );
        curl_setopt( $ch,CURLOPT_POSTFIELDS, json_encode( $fields ) );
        $result = curl_exec($ch );
        curl_close( $ch );

    }

    public function add_post()
    {
      $user_id=$this->input->post('user_id', TRUE);
      $postType=$this->input->post('postType', TRUE);
      $content=$this->input->post('content', TRUE);
      $media=$this->input->post('media', TRUE);
      $title=$this->input->post('title', TRUE);
      $public_id=$this->input->post('public_id', TRUE);

      
		if ($postType != 'text') {
			$this->load->library('upload');
		  
		             $config['upload_path']   = './assets/images/'; 
		             $config['allowed_types'] = 'gif|jpg|jpeg|png|avi|flv|wmv|mp4|mov|gif';
		             $config['max_size']      = 10000; 
		             $config['file_name']     = time();
		             $this->upload->initialize($config);
		             $image="";
		             if ( $this->upload->do_upload('media'))
		             {
		              $image='assets/images/'.$this->upload->data('file_name'); 
		             }
		             else   
		             {

		             }

			        if($image)
			        {
			        	$data['media']= $image;
			        }
		}

       


      $data['user_id']=$user_id;
      $data['public_id']=isset($public_id) ? $public_id: "";
      $data['title']=$title;
      $data['postType']=$postType;
      $data['content']=$content;
      $data['createAt']=time();

      $data['id']=$this->Api_model->add_post($data);
      if($data['id'])
      {        
        $get_all_user=$this->Api_model->get_all_user();

        foreach ($get_all_user as $get_all_user) {
          # code...
          $idUser=$get_all_user['id'];
          $msg1= "$title new post on PetStand.";
          $title= "New Post";
          $this->firebase_notification($idUser, $title, $msg1);
        }
        
        $this->Api_model->responseSuccessWithOutData(1, 'ADD_POST');
      }
      else
      {
        $this->Api_model->responseFailed(0, 'TRY_AGAIN');
      }  
    }


    public function edit_post()
    {
      $user_id=$this->input->post('user_id', TRUE);
      $postID=$this->input->post('postID', TRUE);
      $postType=$this->input->post('postType', TRUE);
      $content=$this->input->post('content', TRUE);
      $media=$this->input->post('media', TRUE);
      $title=$this->input->post('title', TRUE);
      $public_id=$this->input->post('public_id', TRUE);

      $get_post=$this->Api_model->get_single_post($postID);
      if($get_post)
      {        

        $data['user_id']=isset($user_id) ? $user_id: $get_post->user_id;
        $data['postID']=isset($postID) ? $postID: $get_post->postID;
        $data['public_id']=isset($public_id) ? $public_id: $get_post->public_id;
        $data['title']=isset($title) ? $title: $get_post->title;
        $data['postType']=isset($postType) ? $postType: $get_post->postType;
        $data['media']=isset($media) ? $media:'';
        $data['content']=isset($content) ? $content: $get_post->content;

        $this->Api_model->update_post($data);

        $this->Api_model->responseSuccessWithOutData(1, 'POST_UPDATE');
      }
      else
      {
        $this->Api_model->responseFailed(0, 'TRY_AGAIN');
      }  
    }

    public function deletePet()
    {
    	$user_id=$this->input->post('user_id', TRUE);
		$pet_id=$this->input->post('pet_id', TRUE);
		$table= 'pet';       
        $condition = array('id'=>$pet_id, 'user_id'=> $user_id);	

         $check_delete = $this->Api_model->getSingleRow($table, $condition);   

         if ($check_delete->deleted == false) 
         {
       			 $delete = $this->Api_model->updateSingleRow($table, array('id'=>$pet_id), array('deleted'=>true));

       			 if ($delete) 
       			 {
       			 	$this->Api_model->updateSingleRow('appointment', array('pet_id'=>$pet_id), array('status'=>'4'));
       			 	$this->Api_model->responseSuccessWithOutData(1, 'your pet deleted successfully.');
       			 }
       			 else
       			 {
					$this->Api_model->responseFailed(0, NOT_RESPONDING);
       			 }
           	
         }  
        else
        {
        	$this->Api_model->responseFailed(0, 'Allready Deleted');
        }


    }

    public function viewPetProfile()
    {
		$pet_id=$this->input->post('pet_id', TRUE);
		$table= 'pet';       
        $condition = array('id'=>$pet_id);	

           
		 $value = $this->Api_model->updateViewPet($pet_id);

		 	if ($value) 
		 	{
		 		$this->Api_model->responseSuccessWithOutData(1, 'View');
		 	}
			 else
			 {
				$this->Api_model->responseFailed(0, NOT_RESPONDING);
		 	}
           	
       

    }

    

	  public function rating()
    {
     	$user_id = $this->input->post('user_id',TRUE);
		$pet_id = $this->input->post('pet_id',TRUE);
		$rating = $this->input->post('rating',TRUE);

      if(!$check_rating=$this->Api_model->check_review($user_id, $pet_id))
      {
	    
	      $data['user_id']= $user_id;
	      $data['pet_id']= $pet_id;
	      $data['rating']= $rating;

	      $this->Api_model->add_review($data);
		  //$this->Api_model->updateSingleRow('pet', array('id'=>$pet_id), array('rating'=>$this->getRating()));
		  $this->Api_model->updateRatingPet($pet_id, $this->getRating());
	     $this->Api_model->responseSuccessWithOutData(1, 'Rating Update');
      }
      else
      {
       $this->Api_model->responseFailed(0, 'Allready Rating');
      }      
    }

    public function getRating()
    {
    	$pet_id = $this->input->post('pet_id',TRUE);
    	$avrage=$this->Api_model->count_total_rating($pet_id);
 		return round($avrage->average, 0);

    }

    public function reviewProduct()
    {
    	$user_id = $this->input->post('user_id',TRUE);
		$product_id = $this->input->post('product_id',TRUE);
		$rating = $this->input->post('rating',TRUE);
		$title = $this->input->post('title',TRUE);
		$description = $this->input->post('description',TRUE);


		 if(!$check_review_Product=$this->Api_model->check_review_product($user_id, $product_id))
		 {
		 	    $data['user_id'] = $user_id;
				$data['product_id'] = $product_id;
				$data['rating'] = $rating;
				$data['title'] = $title;
				$data['description'] = $description;
				$data['created_date']=time();

				$getId = $this->Api_model->insertGetId('review_product', $data);

			 	if($getId)
			 	{
			 		 $this->Api_model->updateRatingProduct($product_id, $this->getRatingProduct());
			 		$this->Api_model->responseFailed(1, 'Review added successfully');
			 	}
			 	else
			 	{
			 		$this->Api_model->responseFailed(0, NOT_RESPONDING);
			 	}


		 }
		 else
		 {

			 	$this->Api_model->responseFailed(0, 'Allready Reviewed');

    	}


	}

	public function getRatingProduct()
    {
    	$product_id = $this->input->post('product_id',TRUE);
    	$avrage=$this->Api_model->count_total_rating_product($product_id);
 		return round($avrage->average, 0);

    }

	/*Added By VARUN*/

}


?>