<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Chat extends CI_Controller
{
    public function __construct()
    {

        parent::__construct();
        $this -> load -> library('session');
        $this -> load -> helper('form');
        $this -> load -> helper('url');
        $this -> load -> library('image_lib');
        $this -> load -> library('api');
        $this -> load -> database();
        $this -> load -> library('form_validation');
        $this -> load -> model('Comman_model');
        $this -> load -> model('Api_model');
        header('Access-Control-Allow-Origin: *');
        header("Access-Control-Allow-Methods: GET, POST, OPTIONS, PUT, DELETE");
    }

    public function sendmsg()
    {
        $this->form_validation->set_rules('chat_type','chat_type','required');
        if ($this->form_validation->run()==false) {
           $this->api->api_message(0, "Chat type not aviable");
           exit();
        }
        $media= $this->input->post('media', TRUE);
        $latitude= $this->input->post('latitude', TRUE);
        $longitude= $this->input->post('longitude', TRUE);
        $chat_type=$this->input->post('chat_type', TRUE);
        if(isset($chat_type))
        {
            $data['chat_type']=$this->input->post('chat_type', TRUE);
        }
        else
        {
            $data['chat_type']='1'; 
        }

        $calling_status=$this->input->post('calling_status', TRUE);
        if(isset($calling_status))
        {
            $data['calling_status']=$calling_status;
        }
        else
        {
            $calling_status=2; 
        }

        $data['user_pub_id']= $this->input->post('sender_id', TRUE);
        $data['user_pub_id_receiver']= $this->input->post('receiver_id', TRUE);
        $data['message']= $this->input->post('message', TRUE);
        $data['sender_name']= $this->input->post('sender_name', TRUE);
        $data['date']= time();

        $this->load->library('upload');

        $config['image_library'] = 'gd2';
        $config['upload_path']   = './assets/images/'; 
        $config['allowed_types'] = 'gif|jpg|jpeg|png';
        $config['max_size']      = 10000; 
        $config['file_name']     = time();
        $config['create_thumb'] = TRUE;
        $config['maintain_ratio'] = TRUE;
        $config['width'] = 250;
        $config['height'] = 250;
        $this->upload->initialize($config);
        $updateduserimage="";
        if ( $this->upload->do_upload('media') && $this->load->library('image_lib', $config))
        {         
          $updateduserimage='assets/images/'.$this->upload->data('file_name');
        }
        else  
        {
        //  echo $this->upload->display_errors();
        }

        if($updateduserimage)
        {
            $data['media']=$updateduserimage;
        }

        if($chat_type==5)
        {
            $data['latitude']=$latitude;
            $data['longitude']=$longitude;
        }
        $chatId=$this->Api_model->insertGetId("chat",$data);
        if($chatId)
        {
            $checkUser= $this->Api_model->getSingleRow("users", array('id'=>$data['user_pub_id_receiver']));

            if($chat_type==3)
            {
                if($calling_status==0)
                {
                    $msg=$checkUser->first_name.':'."Strated a video chat";
                    $this->firebase_notification($data['user_pub_id_receiver'], "Video Chat" ,$msg);
                }
                else
                {
                    $msg="Video chat ended";
                    $this->firebase_notification($data['user_pub_id_receiver'], "Video Chat" ,$msg);
                }
            }
            else
            {
                $msg=$checkUser->first_name.':'.$data['message'];
                $this->firebase_notification($data['user_pub_id_receiver'], "Chat" ,$msg);
            }

            $get_chat= $this->Api_model->getChatData($data['user_pub_id'],$data['user_pub_id_receiver']);
            $get_chats = array();
            foreach ($get_chat as $get_chat) 
            {
              if($get_chat->chat_type==2)
              {
                $get_chat->media= base_url().$get_chat->media;
              }

              if($get_chat->chat_type==4)
              {
                $get_chat->media= base_url().$get_chat->media;
                $get_chat->thumb= base_url().$get_chat->thumb;
              }

              array_push($get_chats, $get_chat);
            }

            $this->api->api_message_data(1, MSG_SEND,'my_chat', $get_chats);
        }
        else
        {
            $this->api->api_message(0, TRY_AGAIN);
        }
   }


    /*get conversation*/
   public function getChat()
   {
       $this->form_validation->set_rules('sender_id','sender_id','required');
       $this->form_validation->set_rules('receiver_id','receiver_id','required');
        if ($this->form_validation->run()==false) 
        {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        }

      $user_pub_id= $this->input->post('sender_id', TRUE);
      $page= $this->input->post('page', TRUE);
      $user_pub_id_receiver= $this->input->post('receiver_id', TRUE);

      $page= isset($page) ? $page: 1;
      $get_chat= $this->Api_model->getChatData($user_pub_id,$user_pub_id_receiver);

      if($get_chat)
      {
        $get_chats = array();
        foreach ($get_chat as $get_chat) 
        {
          if($get_chat->chat_type==2)
          {
            $get_chat->media= base_url().$get_chat->media;
          }

          if($get_chat->chat_type==4)
          {
            $get_chat->media= base_url().$get_chat->media;
            $get_chat->thumb= base_url().$get_chat->thumb;
          }

          if($get_chat->chat_type==3 && $get_chat->calling_status==0)
          {
            $get_chat->message="Strated a video chat";
          }
          if($get_chat->chat_type==3 && $get_chat->calling_status==1)
          {
            $get_chat->message="Video chat ended";
          }
          array_push($get_chats, $get_chat);
        }
        $this->api->api_message_data(1, GET_CHAT,'my_chat', $get_chats);
      }
      else
      {
        $this->api->api_message(0, NO_CHAT);
      }
   }

   public function getChatHistory1()
   {
        $this->form_validation->set_rules('user_pub_id','user_pub_id','required');
        if ($this->form_validation->run()==false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        }

        $user_pub_id= $this->input->post('user_pub_id', TRUE);
        $chkUser=$this->Api_model->getSingleRow('users', array('id' =>$user_pub_id));
        if(!$chkUser)
        {
            $this->api->api_message(0, NO_DATA);
            exit();
        }

      $where=array('user_pub_id'=>$user_pub_id);
      $where1=array('user_pub_id_receiver'=>$user_pub_id);
      $get_users= $this->Api_model->getAllDataWhereDistinct($where,$where1, "chat");

      if($get_users)
      {
        $chats= array();
        foreach ($get_users as $get_users)
        {
          $chat= $this->Api_model->getRowWhereOrWhere(array('user_pub_id'=>$user_pub_id,'user_pub_id_receiver'=>$get_users->user_pub_id_receiver),array('user_pub_id_receiver'=>$user_pub_id,'user_pub_id'=>$get_users->user_pub_id_receiver),"chat");
          if($user_pub_id == $chat->user_pub_id_receiver)
          {
            $user= $this->Api_model->getSingleRow("users", array('id'=>$chat->user_pub_id));
          }
          else
          {
            $user= $this->Api_model->getSingleRow("users", array('id'=>$chat->user_pub_id_receiver));
          }
          
          if($chat->chat_type==4)
          {
            $chat->media= base_url().$chat->media;
            $chat->thumb= base_url().$chat->thumb;
          }
          if($chat->chat_type==2)
          {
            $chat->media= base_url().$chat->media;
          }

          $chat->userName= $user->first_name;
          $chat->user_pub_id_receiver= $user->id;
          $chat->userImage= base_url().$user->profile_pic;
         
          array_push($chats, $chat);
        }

        $chats1=  array_unique($chats, SORT_REGULAR);

        array_multisort(array_column($chats1, 'id'), SORT_DESC, $chats1);
        $this->api->api_message_data(1, CHAT_HST,'my_chat', $chats1);
      }
      else
      {
        $this->api->api_message(0, NO_CHAT);
      }
   }


    public function getChatHistory()
    {
        $this->form_validation->set_rules('user_pub_id','user_pub_id','required');
        if ($this->form_validation->run()==false) {
            $this->api->api_message(0, ALL_FIELD_MANDATORY);
            exit();
        }

        $user_pub_id= $this->input->post('user_pub_id', TRUE);
        $chkUser=$this->Api_model->getSingleRow('users', array('id' =>$user_pub_id));
        if(!$chkUser)
        {
            $this->api->api_message(0, NO_DATA);
            exit();
        }
        $chats= array();
        $chatsn= array();
        $chats2= array();
        $where=array('user_pub_id'=>$user_pub_id);
        $get_users= $this->Api_model->getAllDataWhere($where, "chat");
        if($get_users)
        {
            foreach ($get_users as $get_users)
            {
                $chat= $this->Api_model->getSingleRowOrderBy("chat", array('user_pub_id'=>$user_pub_id,'user_pub_id_receiver'=>$get_users->user_pub_id_receiver));
                if($chat)
                {
                  if($user_pub_id == $chat->user_pub_id_receiver)
                  {
                    $user= $this->Api_model->getSingleRow("users", array('id'=>$chat->user_pub_id));
                  }
                  else
                  {
                    $user= $this->Api_model->getSingleRow("users", array('id'=>$chat->user_pub_id_receiver));
                  }
                  
                  if($chat->chat_type==4)
                  {
                    $chat->media= base_url().$chat->media;
                    $chat->thumb= base_url().$chat->thumb;
                  }
                  if($chat->chat_type==2)
                  {
                    $chat->media= base_url().$chat->media;
                  }

                  $chat->userName= $user->name;
                  $chat->user_pub_id_receiver= $user->id;
                  $chat->userImage= base_url().$user->profile_pic;
                 
                  array_push($chats, $chat);
                }   
            }
            
            $chatsn=  array_unique($chats, SORT_REGULAR);
        }
        $chats1s= array();
        $where=array('user_pub_id_receiver'=>$user_pub_id);
        $get_users= $this->Api_model->getAllDataWhere($where, "chat");

        if($get_users)
        {
            foreach ($get_users as $get_users)
            {
                $chat1= $this->Api_model->getSingleRowOrderBy("chat", array('user_pub_id_receiver'=>$user_pub_id,'user_pub_id'=>$get_users->user_pub_id));
        
              if($chat1)
              {
                  if($user_pub_id == $chat1->user_pub_id_receiver)
                  {
                    $user= $this->Api_model->getSingleRow("users", array('id'=>$chat1->user_pub_id));
                  }
                  else
                  {
                    $user= $this->Api_model->getSingleRow("users", array('id'=>$chat1->user_pub_id_receiver));
                  }
                  
                  if($chat1->chat_type==4)
                  {
                    $chat1->media= base_url().$chat1->media;
                    $chat1->thumb= base_url().$chat1->thumb;
                  }
                  if($chat1->chat_type==2)
                  {
                    $chat1->media= base_url().$chat1->media;
                  }

                  $chat1->userName= $user->name;
                  $chat1->user_pub_id_receiver= $user->id;
                  $chat1->userImage= base_url().$user->profile_image;
                 
                  array_push($chats1s, $chat1);
               }   
            }
            $chats2=  array_unique($chats1s, SORT_REGULAR);
        }

        $chatT= array_merge($chatsn,$chats2);
        if(empty($chatT))
        {
            $this->api->api_message(0, NO_CHAT);
        }
        else
        {
            $chatT=array_reverse($chatT);
            array_multisort(array_column($chatT, 'id'), SORT_DESC, $chatT);
            function super_unique($array,$key)
            {
               $temp_array = [];
               foreach ($array as &$v) {

                   if (!isset($temp_array[$v->$key]))
                   $temp_array[$v->$key] =& $v;
               }
               $array = array_values($temp_array);
               return $array;

            }
 
            $chatT=super_unique($chatT,'user_pub_id_receiver');
            //array_multisort(array_column($chatT, 'id'), SORT_DESC, $chatT);
            $this->api->api_message_data(1, CHAT_HST,'my_chat', $chatT);
        } 
    }
    /*Send Firebase Notification*/
    public function firebase_notification($user_id, $title, $msg1)
    {     
    
        $user = $this->db->where('id',$user_id)->get('users')->row();
        $registrationIds = $user->device_tokan;
        $mobile_sent = $user_id;

        $API_ACCESS_KEY= 'AAAASpTNidM:APA91bEZ31QjBfKtSfrBRy-0ZbmNsgnVqzJYtZMs8PbrIhX-KXeTty_cIgu00B6LlxtBhfEX1vGTlxh0Wms9GiT5TkGJTnt7Rg9lSP_dI0Wpi8TSJ56Y9cDRaBmTwFzSmPyPZ2VyVS1igzTZB2Gx0Z8trHLqVyQV_g';
     
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
}    