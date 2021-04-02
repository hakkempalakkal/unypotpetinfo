<?php
    /**
    * 
    */
    class Api_model extends CI_Model
    {
        
        function __construct()
        {
            parent:: __construct();
        }

        /*Get single row data*/
        public function getSingleRow($table, $condition)
        {
            $this->db->select('*');
            $this->db->from($table);
            $this->db->where($condition);
            $query = $this->db->get();
            //echo $this->db->last_query();
            return $query->row();       
        }

        public function getSumWithWhere($columnName,$table,$where)
        {
            $this->db->select_sum($columnName);
            $this->db->from($table);
            $this->db->where($where);
            $query=$this->db->get();
            return $query->row();
        }
        /*Insert and get last Id*/
        public function insertGetId($table,$data)
        {
            $this->db->insert($table, $data);
            return $this->db->insert_id();
        }
        /*Check existing record*/
        function checkData($table, $condition, $columnName)
        {
            $this->db->select($columnName);
            $this->db->from($table);
            $this->db->like($condition);
            return $this->db->count_all_results();
        }   

        /*Update any data*/
         public function updateSingleRow($table, $where, $data)
        {                 
            $this->db->where($where);
            $this->db->update($table, $data);

            if ($this->db->affected_rows() > 0)
            {
              return TRUE;
            }
            else
            {
              return FALSE;
            }
        }

        public function updateViewPet($pet_id)
        {
            $this->db->where('id', $pet_id);
            $this->db->set('view_profile', 'view_profile+1', FALSE);
            $this->db->update('pet');
             if ($this->db->affected_rows() > 0)
            {
             
              return TRUE;
            }
            else
            {
             
              return FALSE;
            }
        }

         public function updateRatingPet($pet_id, $rating)
        {
            $this->db->where('id', $pet_id);
            $this->db->set('total_rating_user', 'total_rating_user+1', FALSE);
            $this->db->set('rating', $rating, FALSE);
            $this->db->update('pet');
             if ($this->db->affected_rows() > 0)
            {
             
              return TRUE;
            }
            else
            {
             
              return FALSE;
            }
        }


        

        /*Add new data*/
        function insert($table,$data)
         {
            if($this->db->insert($table, $data))
            {
                return TRUE;
            }
            else
            {
                return FALSE;
            }

         }

        /*Get All data*/
        public function getAllData($table)
        {
            $this->db->select("*");
            $this->db->from($table);
            $query = $this->db->get();          
            return $query->result();
        }

        public function getAllDataColumn($table, $columnName,$where)
        {
            $this->db->where($where);
            $this->db->select($columnName);
            $this->db->distinct();
            $query = $this->db->get($table);
            return $query->result();
        }
       
        public function getFilterData($table,$Data)
        {

            if (!empty($Data))
             {
              foreach ($Data as $key => $value)
                  {
                    if ($value) {
                   $this->db->where_in($key, $value);
                    }else{
                    }
                   
                  }
            }
      
     

            
            // $this->db->where_in($columnName2, $where2);
            // $this->db->where_in($columnName3, $where3);
              $this -> db -> where('deleted', false);
            $this->db->select("*");
            $this->db->distinct();

            $query = $this->db->get($table);
            return $query->result();
        }
     
        /*Get All data with where clause*/
        public function getAllDataWhere($where, $table)
        {
            $this->db->where($where);
            $this->db->select("*");
            $this->db->from($table);
            $query = $this->db->get();     
            return $query->result();
        }

           public function getAllBreed($where, $table)
        {
            $this->db->where($where);
            $this->db->select("*");
            $this->db->from($table);
            $this->db->order_by('breed_name');
            $query = $this->db->get();     
            return $query->result();
        }


        

        public function getSingleDataWhere($where, $table)
        {
            $this->db->where($where);
            $this->db->select("pet_img_path");
            $this->db->from($table);
            $query = $this->db->get();     
            return $query->result();
        }

         // Count avarage 
        public function getAvgWhere($columnName, $table,$where)
        {
            $this->db->select_avg($columnName);
            $this->db->from($table);
            $this->db->where($where);
            $query =$this->db->get(); 
            return $query->result(); 
        }

        public function deleteRecord($where, $table)
        {
            $this->db-> where($where);
            $query = $this->db->delete($table);  
        } 
      /*Added By Varun_Andro*/
        //Response Function TRUE WITH DATA

        public function responseSuccess($status, $message, $data){
            $arr = array('status' => $status,'message' => $message, 'data'=> $data); 
            header('Content-Type: application/json');      
             echo json_encode($arr); 
        }
        //Response Function TRUE 

        public function responseSuccessWithOutData($status, $message){
            $arr = array('status' => $status,'message' => $message); 
            header('Content-Type: application/json');      
             echo json_encode($arr); 
        }

         //Response Function FALSE

        public function responseFailed($status, $message){
            $arr = array('status' => $status,'message' => $message); 
            header('Content-Type: application/json');      
             echo json_encode($arr);
        }

        public function distance($lat1, $lon1, $lat2, $lon2) 
        {

          $theta = $lon1 - $lon2;
          $dist = sin(deg2rad($lat1)) * sin(deg2rad($lat2)) +  cos(deg2rad($lat1)) * cos(deg2rad($lat2)) * cos(deg2rad($theta));
          $dist = acos($dist);
          $dist = rad2deg($dist);
          $miles = $dist * 60 * 1.1515;
        
            return ($miles * 1.859344);
        }

        function get_user_record_by_user_id($user_id) {
        $this -> db -> where('id', $user_id);
        $this -> db -> from('users');
        $query = $this -> db -> get();
        return $query -> result();
    }   

    function get_reminder_history($app_id='',$user_id='',$pet_id='')
    {
        if ($user_id !='' && $pet_id!='') 
        {
            return $this->db->where('user_id',$user_id)->where('pet_id',$pet_id)->get('appointment');
        }
        elseif($user_id !='') 
        {
            return $this->db->where('user_id',$user_id)->get('appointment');
        }
      
    }

    // get all appointment

    function get_app($app_id='',$user_id='',$pet_id='')
    {
      if($app_id !='')
      {
          return $this->db->where('id',$app_id)->get('appointment');

      }
      elseif($user_id !='' && $pet_id!='')
      {
          return $this->db->where('user_id',$user_id)->where('pet_id',$pet_id)->where('status','0')->get('appointment');

      }
       elseif($user_id !='')
      {
          return $this->db->where('user_id',$user_id)->where('status','0')->get('appointment');

      }
      else
      {
         return $this->db->where('status','0')->get('appointment');
      }



    }

    function change_status($app_id,$status)
    {
      if(!empty($app_id))
      {
          $changed = $this->db->where('id',$app_id)->update('appointment',array('status'=>$status));
          if($this->db->affected_rows()>0)
          {
            return true;
          }
          else
          {
            return false;
          }
      }
      else
      {
        return false;
      }

    }

    function create_notification($data)
    {        
      if($this->db->insert('notification',$data))
      {
          return true;
      }
      else
      {
          return false;
      }
    }

    public function add_appointment($data)
    {
        $this->db->insert('appointment', $data);
        return $this->db->insert_id();
    }

    public function get_manual_activity_bydate($pet_id,$user_id,$date)
    {
      $this ->db -> where('pet_id', $pet_id);
      $this ->db -> where('user_id', $user_id);
      $this ->db -> where ('date', $date );
      $this ->db -> from('manualactivity');
      //$this -> db -> order_by("id", "asc");

      $query = $this -> db -> get();
      return $query -> result();

    }

    public function update_manual_activity($pet_id,$user_id,$date,$data)
    {
          $this->db->where('pet_id', $pet_id);
          $this ->db -> where('user_id', $user_id);
          $this ->db -> where( 'date', $date);
          $this->db->update('manualactivity' , $data);
    }

    public function set_manual_activity($data)
    {
      //print_r($data);
      if($this ->db -> insert('manualactivity', $data))
      {
        //echo "data inserted";

      }
    }

    public function get_manual_activity($pet_id,$user_id)
    {

        $this ->db -> where('pet_id', $pet_id);
        $this ->db -> where('user_id', $user_id);
        $this->db->where('date BETWEEN DATE_SUB(NOW(), INTERVAL 7 DAY) AND NOW()');
        $this ->db -> from('manualactivity');
        $query = $this->db -> get();
        return $query -> result();

    }

     public function get_post($limit, $offset) 
    {
      $this->db->where('flag',1);  
      $this->db->select('*');
      $this->db->order_by('postID', 'desc');  
      $this->db->from('post');
      $this->db->limit($limit, $offset);
      $query = $this->db->get();
      return $query->result_array();
     }

      public function check_like($postID,$user_id)   
    {
        $this->db-> where('user_id', $user_id);
        $this->db-> where('postID', $postID);
        $this->db->select("*");
        $this->db->from('likes');
        $query = $this->db->get();
        return $query->row();   
    }

    

  
       public function get_user($user_id)   
    {
        $this->db-> where('id', $user_id);
        $this->db->select("*");
        $this->db->from('users');
        $query = $this->db->get();
        return $query->row();   
    }

    public function no_of_records($table, $where)
     {
        $this->db->where($where);
        $this->db->from($table);
        return $this->db->count_all_results();
     }   

      public function get_my_post($limit, $offset, $user_id)
    {
      $this->db->where('user_id',$user_id);
      $this->db->where('flag',1);  
      $this->db->select('*');
      $this->db->order_by('postID', 'desc');  
      $this->db->from('post');
      $this->db->limit($limit, $offset);
      $query = $this->db->get();
      return $query->result_array();
     }

   
      public function like($data)
    {
        $this->db->insert('likes', $data);
        return $this->db->insert_id();
    }

     public function get_comments_true($postID) 
    {
      $this->db->where('postID', $postID);  
      $this->db->where('flag', 1);  
      $this->db->select('*');
      $this->db->order_by('commentID', 'desc');  
      $this->db->from('comments');
      $query = $this->db->get();
      return $query->result_array();
     }

      public function comment($data)
    {
        $this->db->insert('comments', $data);
        return $this->db->insert_id();
    }

     public function get_single_post($postID)   
    {
        $this->db-> where('postID', $postID);
        $this->db->select("*");
        $this->db->from('post');
        $query = $this->db->get();
        return $query->row();   
    }

    public function get_abuse($user_id, $postID) 
    {
      $this->db->where('user_id', $user_id);  
      $this->db->where('postID', $postID);  
      $this->db->select('*');
      $this->db->from('abuse_post');
      $query = $this->db->get();
      return $query->row();
    }

     public function abuse_post($data)
    {
        $this->db->insert('abuse_post', $data);
        return $this->db->insert_id();
    }

       public function check_post($postID,$user_id)   
    {
        $this->db-> where('postID', $postID);
        $this->db-> where('user_id', $user_id);        
        $this->db->select("*");
        $this->db->from('post');
        $query = $this->db->get();
        return $query->row();   
    }
    
    public function delete_post($data)
    {       
        $postID = $data['postID'];
        $this->db->where('postID',$postID);
        $this->db->update('post', $data);    
    }

    public function add_post($data)
    {
        $this->db->insert('post', $data);
        return $this->db->insert_id();
    }

     public function get_all_user() 
    {  
      $this->db->select('*');
      $this->db->from('users');
      $query = $this->db->get();
      return $query->result_array();
     }

     public function update_post($data)
    {       
        $postID = $data['postID'];
        $this->db->where('postID',$postID);
        $this->db->update('post', $data);    
    }

     public function check_review($user_id, $pet_id)
    {
        $this->db-> where('pet_id', $pet_id);
        $this->db-> where('user_id', $user_id);
        $this->db->select("*");
        $this->db->from('rating');
        $query = $this->db->get();
        return $query->result();   
    }
      public function add_review($data)
    {
        $this->db->insert('rating', $data);
        return true;
    }

     public function count_total_rating($pet_id) 
    {
      $this->db->select('AVG(rating) as average');
      $this->db->where('pet_id', $pet_id);
      $this->db->from('rating');
      $query = $this->db->get();
      return $query->row();   
    }
      public function count_total_rating_product($product_id) 
    {
      $this->db->select('AVG(rating) as average');
      $this->db->where('product_id', $product_id);
      $this->db->from('review_product');
      $query = $this->db->get();
      return $query->row();   
    }

     public function check_review_product($user_id, $product_id)
    {
        $this->db-> where('product_id', $product_id);
        $this->db-> where('user_id', $user_id);
        $this->db->select("*");
        $this->db->from('review_product');
        $query = $this->db->get();
        return $query->result();   
    }

     public function updateRatingProduct($product_id, $rating)
        {
            $this->db->where('p_id', $product_id);
            $this->db->set('product_rating', $rating, FALSE);
            $this->db->update('food_product');
             if ($this->db->affected_rows() > 0)
            {
             
              return TRUE;
            }
            else
            {
             
              return FALSE;
            }
        }

    }       