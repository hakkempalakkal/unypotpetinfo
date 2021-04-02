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

         /*Get All data with where clause*/
        public function getAllDataWhereAndOr($where, $whereOr, $table)
        {
            $this->db->where($where);
            $this->db->or_where($whereOr);
            $this->db->select("*");
            $this->db->from($table);
            $this->db->order_by("created_at", "desc");
            $query = $this->db->get();          
            return $query->result();
        }

        public function delete_cat($data)
        {       
            $id = $data['id'];
            $this->db->where('id',$id);
            $this->db->delete('pro_cat', $data);    
        }

        /*Get All data with where clause*/
        public function getRowWhereOrWhere($where,$OrWhere, $table)
        {
            $this->db->where($where);
            $this->db->or_where($OrWhere);
            $this->db->select("*");
            $this->db->from($table);
            $this->db->order_by('id', 'desc'); 
            $query = $this->db->get();          
            return $query->row();
        }

          public function getRowWhereOrNotWhere($where,$OrWhere,$table)
        {
           
            $this->db->select("*");
            $this->db->from($table);
             $this->db->where($where);        
            $this->db->where_not_in($OrWhere);
            $query = $this->db->get();          
            return $query->row();
        }
        // public function getRowWhereOrNotWhere($where,$OrWhere,$table)
        // {
        //     $this->db->select('*');
        //     $this->db->from($table);
        //     $this->db->where($where);
        //     $this->db->where_not_in($OrWhere);
        //     $query = $this->db->get();
        //     return $query->row();       
        // }


        public function getRowWhereAndWhere($where,$AndWhere, $table)
        {
            $this->db->where($where);
            $this->db->or_where($AndWhere);
            $this->db->select("*");
            $this->db->from($table);
            $this->db->order_by('id', 'desc'); 
            $query = $this->db->get();          
            return $query->row();
        }

        /*Get All data with where clause*/
        public function getAllDataWhereDistinct($where, $where1, $table)
        {   
            $this->db->distinct('user_id_receiver');
            $this->db->where($where);
            $this->db->or_where($where1);
            $this->db->select("*");
            $this->db->from($table);
            $query = $this->db->get();
            return $query->result();
        }

         /*Get All data with where clause*/
        public function getAllDataWhereOrWhere($where,$OrWhere, $table,$page)
        {
            $this->db->where($where);
            $this->db->or_where($OrWhere);
            $this->db->select("*");
            $this->db->from($table);
             if($page==1)
            {
               $this->db->limit(100, 0); 
            }
            else
            {
                $limit= 100*$page;
                $start= ($limit-100);
                $this->db->limit(100, $start);
            }
            $query = $this->db->get();          
            return $query->result();
        }

        /*Get All data with where clause*/
        public function getAllDataWhereDistinct1($where, $table)
        {   
            $this->db->distinct('user_id_receiver');
            $this->db->where($where);
            $this->db->select("user_id_receiver");
            $this->db->from($table);
            $query = $this->db->get();         
            return $query->result();
        }

        /*Get single row data*/
        public function getSingleRow($table, $condition)
        {
            $this->db->select('*');
            $this->db->from($table);
            $this->db->where($condition);
            $query = $this->db->get();
            return $query->row();       
        }

        public function getTrueFalse($table, $condition){
          $this->db->select('*');
           $this->db->from($table);
           $this->db->where($condition);
           $query = $this->db->get();
           $my =  $query->row();

           if ($my) {
               return 1;
             }
             else
             {
               return 0;
             }

       }

       /* err responce */
        public function errResponse($status, $message){
            $arr = array('status' => $status,'message' => $message); 
            header('Content-Type: application/json');      
             echo json_encode($arr);
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
           // echo $sql = $this->db->last_query();

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
       
        public function getAllData50($banner)
        {
            $this->db->select("image");
            $this->db->from($banner);
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
                if ($value) 
                {
                 $this->db->where_in($key, $value);
                }
              }
            }
            $this->db->where('deleted', false);
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

           public function getoffer()
        {
          $sql="SELECT start_date as st, end_date as ed * from offer where st >= ed;";  
          // SELECT * from offer where 2019-09-01 >= 2019-09-07;
            $query = $this->db->query($sql);
            return $query->result_array();   
        }

         public function getAllDataaaWhere($data)
        {
          $sql = "Select * from food_product where status='1' and p_id=$data";

            $query1 = $this->db->query($sql);
            return $query1->result_array();
            // $this->db->where($where);
            // $this->db->select("*");
            // $this->db->from($table);
            // $query = $this->db->get();
            // return $query->result();
        }

           public function getAllDataaWhere($where, $table)
        {
            $this->db->where($where);
            $this->db->select("*");
            $this->db->from($table);
            $this->db->order_by("created_at", "DESC");
            $query = $this->db->get();
            return $query->result();
        }


           public function getAllDataaWhere111($where, $table)
        {
            $this->db->where($where);
            $this->db->select("*");
            $this->db->from($table);
            $this->db->order_by("updated_at", "DESC");
            $query = $this->db->get();
            return $query->result();
        }
       
              public function getAllDataaWhere1112($where, $table)
        {
            $this->db->where($where);
            $this->db->select("*");
            $this->db->from($table);
            $this->db->order_by("created_at", "DESC");
            $query = $this->db->get();
            return $query->result();
        }
       

        public function getAllDataWhereProducts($where, $table)
        {
            $this->db->where($where);
            $this->db->select("*");
            $this->db->group_by('p_name');
            $this->db->from($table);
            $query = $this->db->get();
            return $query->result();
        }

       public function getAllDataWhereProducts1($keyword,$table)
       {
           $this->db->select('*');
           $this->db->from($table);
           $this->db->like('p_name', $keyword,"both");
          //$this->db->where('p_name','');
          return $this->db->get()->result_array();
       }
        public function getAllDataWhereGroupBy($where,$columnName, $table)
        {
            $this->db->where($where);
            $this->db->select("*");
            $this->db->group_by($columnName);
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
       
   public function getAllVss($where, $table)
        {
            $this->db->where($where);
            $this->db->select("*");
            $this->db->from($table);
            $this->db->order_by('Nearby_id');
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
       public function deleteR($table, $where)
        {
            $this->db-> where($where);
            $query = $this->db->delete($table);  
        } 
        public function responseSuccess($status, $message, $data){
            $arr = array('status' => $status,'message' => $message, 'data'=> $data); 
            header('Content-Type: application/json');      
             echo json_encode($arr); 
        }

       public function getSingleRowOrderBy($table, $condition)
       {
           $this->db->select('*');
           $this->db->from($table);
           $this->db->where($condition);
           $this->db->order_by('id', 'desc');
           $query = $this->db->get();
           return $query->row();
       }
       public function getAllDataWhereOrderBy($where, $table)
        {
            $this->db->where($where);
            $this->db->select("*");
            $this->db->from($table);
            $this->db->order_by("id","desc");
            $query = $this->db->get();     
            return $query->result();
        }

        public function responseSuccessThree($status, $message,$userData, $data){
            $arr = array('status' => $status,'message' => $message,'user_info' => $userData, 'data'=> $data); 
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

        public function response($status, $message){
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

     public function get_post($limit, $offset,$where) 
    {
      $this->db->where($where);  
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
      //  echo $this->db->last_query();
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

       
       public function get_pet($user_id)   
    {
        $this->db-> where('user_id', $user_id);
        $this->db->select("*");
        $this->db->from('pet');
        $query = $this->db->get();
        return $query->row();   
    }
  

    public function no_of_records($table, $where)
     {
        $this->db->where($where);
        $this->db->from($table);
        return $this->db->count_all_results();
     }   
     public function count($table)
     {
        $this->db->from($table);
        return $this->db->count_all_results();
     } 
     public function countWhere($table,$where)
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
  
    public function banner($data)
    {
        $this->db->insert('banner', $data);
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
    

   public function add_vss($data)
    {
        $this->db->insert('nearby_vss', $data);
        return $this->db->insert_id();
    }
 
   // public function address_fill($data)
   // {
   //   $this->db->insert('addressfill', $data)
   //   return $this->db->insert_id();
   // }

     public function deleteFood($data)
    {       
        $c_id = $data['c_id '];
        $this->db->where('c_id ',$c_id);
        $this->db->update('food_product', $data);    
    }


     public function get_all_user() 
    {  
      $this->db->select('*');
      $this->db->from('users');
      $query = $this->db->get();
      return $query->result_array();
     }



      public function get_All_Vss() 
    {  
      $this->db->select('*');
      $this->db->from('nearby_vss');
      $query = $this->db->get();
      return $query->result_array();
     }

     public function update_post($data)
    {       
        $postID = $data['postID'];
        $this->db->where('postID',$postID);
        $this->db->update('post', $data);    
    }
    public function update($table,$data)
    {       
        $this->db->where($data);
        $this->db->update($table);    
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
        /*By pankaj choudhary for new admin panel*/

       
         /*Get single row data*/
        public function getSingleRowCloumn($columnName,$table, $condition)
        {
            $this->db->select($columnName);
            $this->db->from($table);
            $this->db->where($condition);
            $query = $this->db->get();
            return $query->row();       
        }


        /*Check existing record*/
        function getCount($table, $condition)
        {
            $this->db->select("*");
            $this->db->from($table);
            $this->db->where($condition);
            return $this->db->count_all_results();
        }   

         /*Get no of records*/
        function getCountAll($table)
        {
            $this->db->select("*");
            $this->db->from($table);
            return $this->db->count_all_results();
        }

         /*Get All data with Limit*/
        public function getAllDataLimit($table, $limit)
        {
            $this->db->select("*");
            $this->db->from($table);
            $this->db->order_by('id', 'desc');
            $this->db->limit($limit);
            $query = $this->db->get();          
            return $query->result();
        }


         /*Get All data with where clause*/
        public function getAllDataWhereDistinctArtist($where, $table)
        {   
            $this->db->distinct('artist_id');
            $this->db->where($where);
            $this->db->select("artist_id");
            $this->db->from($table);
            $query = $this->db->get();         
            return $query->result();
        }

  
         // Count avarage 
        public function getTotalWhere($table,$where)
        {
            $this->db->from($table);
            $this->db->where($where);
            $query =$this->db->get(); 
            return $query->num_rows(); 
        }

         // Count avarage 
        public function getSum($columnName, $table)
        {
            $this->db->select_sum($columnName);
            $this->db->from($table);
            $query =$this->db->get(); 
            return $query->result(); 
        }
        
        public function getNearestData($lat,$lng,$table)
        {
            $this->db->select("*, ( 3959 * acos( cos( radians($lat) ) * cos( radians( latitude ) ) * cos( radians( longitude ) - radians($lng) ) + sin( radians($lat) ) * sin( radians( latitude ) ) ) ) AS distance");
            $this->db->from($table); 
            $this->db->having('distance <= ', '1');                    
            $this->db->order_by('distance');                    
            $this->db->limit(1, 0);
            $query =$this->db->get(); 
            return $query->row(); 
        }

         public function getNearestDataWhere($lat,$lng,$table,$where,$distance)
        {
            $this->db->select("*, ( 3959 * acos( cos( radians($lat) ) * cos( radians( latitude ) ) * cos( radians( longitude ) - radians($lng) ) + sin( radians($lat) ) * sin( radians( latitude ) ) ) ) AS distance");
            $this->db->from($table); 
            $this->db->where($where);
            $this->db->having('distance <= ', $distance);                    
            $this->db->order_by('distance');                    
            $this->db->limit(1, 0);
            $query =$this->db->get(); 
            return $query->result(); 
        }

        /*Get All data with where clause Not User*/
        public function getAllDataWhereNotUser($where,$userID, $table)
        {
            $this->db->where($where);
            $this->db->where('user_id != ', $userID);
            $this->db->select("*");
            $this->db->from($table);
            $query = $this->db->get();     
            return $query->result();
        }

        public function getWhereInStatus($table,$where,$columnName, $where_in)
        {
            $this->db->select('*');
            $this->db->from($table);
            $this->db->where($where);
            $this->db->where_in($columnName, $where_in);
            $this->db->order_by('id', 'desc');
            $query =$this->db->get(); 
            return $query->row();
        }
         public function getWhereInStatusFood($table,$where,$columnName, $where_in)
        {
            $this->db->select('*');
            $this->db->from($table);
            $this->db->where($where);
            $this->db->where_in($columnName, $where_in);
            $query =$this->db->get(); 
            return $query->result();
        }
         public function get_all_food()
    {
        $this->db->select("*");
        $this->db->from('food_company');
        $query = $this->db->get();
        return $query->result();   
    }

      public function get_all_breedinfo()
    {
        $this->db->select("*");
        $this->db->from('breed');
        $query = $this->db->get();
        return $query->result();   
    }

    


    public function getDataBybreedinfo()
    {
          //$sql = "Select * from breed_info ";
           $sql= "Select * from breed_info order by id desc limit 1";
            $query1 = $this->db->query($sql);
            return $query1->result_array();
    }

     public function getDataBycontactinfo()
    {
         // $sql = "Select * from contact_info";
         $sql= "Select * from contact_info order by id desc limit 1";

            $query1 = $this->db->query($sql);
            return $query1->result_array();
    }
    

  public function get_all_pet_type()
    {
        $this->db->select("*");
        $this->db->from('pet_type');
        $query = $this->db->get();
        return $query->result();   
    }

   
      /*Get All data with where clause Not User in use table*/
        public function getAllDataNotUser($where,$userID, $table)
        {
            $this->db->where($where);
            $this->db->where('id != ', $userID);
            $this->db->select("*");
            $this->db->from($table);
            $query = $this->db->get();     
            return $query->result();
        }
  
    public function updateComunity($user_id, $comunity_id)
        {
            $this->db->where('id', $user_id);
            $this->db->set('comunity_id', $comunity_id, FALSE);
            $this->db->update('users');
             if ($this->db->affected_rows() > 0)
            {
             
              return TRUE;
            }
            else
            {
             
              return FALSE;
            }
        }

    public function upload_image($user_id,$pet_id,$description,$created,$img)
    {
       $done = false;
       for($i=0;$i<count($img);$i++)
       {  
         $image = array(
          'pet_img_path' => $img[$i],
          'user_id'=>$user_id,
          'pet_id'=>$pet_id,
          'description'=>$description,
          'created'=>$created
          );
           if($this->db->insert('pet_memories',$image))
           {
              $done = true;
           }
           else
           {
             $done = false;
           }
       }
          return $done;
        
    }

    public function send_opt_mobile1($country_code,$mobile,$msg)
    {
      //$authKey = "145610AaPIgqOsYXl58cfb1fe";
      $authKey = "256803Acz5xroF5c3f9e38";
    
        //Multiple mobiles numbers separated by comma
        $mobileNumber = $country_code.$mobile;
        
        //Sender ID,While using route4 sender id should be 6 characters long.
        $senderId = "PETSTA";
        
        //Your message to send, Add URL encoding here.
        $message = urlencode($msg);
        
        //Define route 
        $route = "4";
        //Prepare you post parameters
        $postData = array(
            'authkey' => $authKey,
            'mobiles' => $mobileNumber,
            'message' => $message,
            'sender' => $senderId,
            'route' => $route
        );
        
        //API URL
        $url="https://api.msg91.com/api/sendhttp.php?authkey='$authKey'&mobiles='$mobileNumber'&message='$message'&sender='$senderId'&route=4&country=0";

        
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

      public function send_opt_mobile($mobile_number, $msg)
   {
     $authKey = "205521AaBNspcwGS5ab512aa";

     //Multiple mobiles numbers separated by comma
     $mobileNumber = $mobile_number;

     //Sender ID,While using route4 sender id should be 6 characters long.
     $senderId = "PETSTA";

     //Your message to send, Add URL encoding here.
     $message = urlencode($msg);

     //Define route
     $route = "4";
     //Prepare you post parameters
     $postData = array(
         'authkey' => $authKey,
         'mobiles' => $mobileNumber,
         'message' => $message,
         'sender' => $senderId,
         'route' => $route
     );

     //API URL
     $url="https://api.msg91.com/api/sendhttp.php?authkey='$authKey'&mobiles='$mobileNumber'&message='$message'&sender='$senderId'&route=4&country=0";

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

     public function send_email($email_id, $subject, $msg)
    {   
        $userData = $this->Api_model->getSingleRow('users',array('email'=>$email_id));
        if($userData)
        {
        $this->load->library('email'); 
         $config = array(
            'protocol'  => 'smtp',
            'smtp_host' => 'ssl://smtp.googlemail.com',
            'smtp_port' => 465,
            'smtp_user' => 'samyotechindore@gmail.com',
            'smtp_pass' => 'samyo123',
            'mailtype'  => 'html',
            'charset'   => 'iso-8859-1'

        );

        $this->email->initialize($config);
        $this->email->set_mailtype("html");
        $this->email->set_newline("\r\n");

        $from_email = 'samyotechindore@gmail.com'; 
        
        $this->email->from($from_email, 'PETEATS'); 
        $this->email->to($email_id);
        $this->email->subject($subject); 
        //$this->CI->email->set_mailtype('html');

         $datas['msg']=$msg;
         $datas['userName']=$userData->first_name.' '.$userData->last_name;
         $body = $this->load->view('petsemail.php',$datas,TRUE);
         $this->email->message($body);
         if($this->email->send()){
          //echo "All Ok";
        }
        else{
          $this->email->print_debugger();
         }
    }

  }

    public function send_email_invitation($email_id, $subject, $msg)
    {
    
        $this->load->library('email'); 
         $config = array(
            'protocol'  => 'smtp',
            'smtp_host' => 'ssl://smtp.googlemail.com',
            'smtp_port' => 465,
            'smtp_user' => 'samyotechindore@gmail.com',
            'smtp_pass' => 'samyo123',
            'mailtype'  => 'html',
            'charset'   => 'iso-8859-1'

        );

        $this->email->initialize($config);
        $this->email->set_mailtype("html");
        $this->email->set_newline("\r\n");

        $from_email = 'samyotechindore@gmail.com'; 
        
        $this->email->from($from_email, 'FabArtist'); 
        $this->email->to($email_id);
        $this->email->subject($subject); 
        //$this->CI->email->set_mailtype('html');

        $datas['msg']=$msg;
         $body = $this->load->view('mailer.php',$datas,TRUE);
         $this->email->message($body);

       return $this->email->send();
    }
    public function get_pets_record_by_user_id($user_id){
        $this ->db -> where(array('user_id'=>$user_id,'deleted'=>0));
        $this ->db -> from('pet');
        $query = $this ->db -> get();
        return $query -> result(); 
    }

     public function get_pet_type($user_id)
    {
        $this->db->select("*");
        $this->db->where("id", $user_id);
        $this->db->from('pet_type');
        $query = $this->db->get();
        return $query->row();   
    }

     /*Get All data with where clause*/
      public function getChatData($sender_id,$receiver_id)
      {
         $sql= "SELECT *
          FROM `chat`
          WHERE (`user_id` = '".$sender_id."'
          AND `user_id_receiver` = '".$receiver_id."' and  chat_state='0')
          or( `user_id` = '".$receiver_id."'
          and `user_id_receiver` = '".$sender_id."' and  chat_state='0')";
           $query = $this->db->query($sql);          
          return $query->result();
      }

      public function getDataByThreeTable($pet_type) 
      {
        $this->db->select ( 'pet_market.*,pet_type.pet_name,users.first_name,users.email,users.mobile_no,users.country_code,users.profile_pic' );
        $this->db->from ( 'pet_market' );
        $this->db->join ( 'pet_type', 'pet_type.id = pet_market.type_id');
        $this->db->join ( 'users', 'users.id = pet_market.id');
        $this->db->where('pet_market.status',1);
         $this->db->where('pet_market.type_id',$pet_type);
        $query = $this->db->get ();
        return $query->result ();
     }

     
      public function getDataByThreeTable1001($order) 
      {        
        $date=date('d-m-Y h:i A'); 
        $sql= "SELECT order_products.*,food_product.*,pro_images.* from order_products join food_product ON food_product.p_id = order_products.product_id  join pro_images ON food_product.p_id = pro_images.p_id join orders on  order_products.order_id=orders.order_id where orders.order_date='28-07-19'";
          
          $query1 = $this->db->query($sql);
          return $query1->result_array();
        
       //  $this->db->select ( 'order_products.*,food_product.*,pro_images.*' );
       //  $this->db->from ( 'order_products' );
       //  $this->db->join ( 'food_product', 'food_product.p_id = order_products.product_id');
       //  $this->db->join ( 'pro_images', 'food_product.p_id = pro_images.p_id');
       //  $this->db->join ( 'orders', 'order_products.order_id = orders.order_id');
       //  $this->db->where('orders.order_id',$order,'orders.order_date>=',$date);
        
       //  $query = $this->db->get();
       // return $query->result ();
     }

     public function getDataByThreeTable1($order) 
      {
         
        //$date=date('d-m-Y h:i A');
        $this->db->select ( 'order_products.*,food_product.*,pro_images.*' );
        $this->db->from ( 'order_products' );
        $this->db->join ( 'food_product', 'food_product.p_id = order_products.product_id');
        $this->db->join ( 'pro_images', 'food_product.p_id = pro_images.p_id');
        $this->db->join ( 'orders', 'order_products.order_id = orders.order_id');
        //$this->db->where('order_products.order_id',$order,'order_products.created_at>=',$date);
        
        $query = $this->db->get();
       return $query->result ();
     }

       /*Get All data with where clause*/
      public function getAllDataWhereConcat($where, $table)
      {
          $this->db->where($where);
          $this->db->select("id AS media_id, CONCAT('".$this->config->base_url()."', image) AS image");
          $this->db->from($table);
          $query = $this->db->get();
          return $query->result();
          //return $this->db->get()->result_array();
      }




      public function getAllPetMarcketData() 
      {
        $this->db->select ( 'pet_market.*,pet_type.pet_name,users.first_name,users.email,users.mobile_no,users.country_code,users.profile_pic' );
        $this->db->from ( 'pet_market' );
        $this->db->join ( 'pet_type', 'pet_type.id = pet_market.type_id');
        $this->db->join ( 'users', 'users.id = pet_market.id');
        $query = $this->db->get ();
        return $query->result ();
     }

      public function getDataByTwoTable($pet_id) 
      {
        $this->db->select ( 'pet_market_comments.*,users.first_name,users.email,users.mobile_no,users.country_code,users.profile_pic' );
        $this->db->from ( 'pet_market_comments' );
        $this->db->join ( 'users', 'users.id = pet_market_comments.user_id');
        $this->db->where('pet_market_comments.pet_id',$pet_id);
        //$this->db->where('pet_market.status',1);
        $query = $this->db->get ();
        return $query->result ();
     }

public function getDataByTwoTable1($user_id) 
      {
        $this->db->select ( 'orders.city,orders.address,orders.country' );
        $this->db->from ( 'orders' );
        $this->db->join ( 'addressfill', 'addressfill.user_id = orders.user_id');
        $this->db->where('orders.user_id',$user_id);
        //$this->db->where('pet_market.status',1);
        $query = $this->db->get ();
        return $query->result ();
     }

     


public function getAllDataByTwoTable12() 
      {
        $this->db->select ( ' p.*,p1.*' );
        $this->db->from ( 'pro_cat  p1' );
        $this->db->join ( 'pet_type p', 'p1.pet_type=p.id');
        // $this->db->where('orders.user_id',$user_id);
        //$this->db->where('pet_market.status',1);
        $query = $this->db->get ();
        return $query->result ();
     }
     // elect FROM  JOIN  ON  ;

  public function getDataByThreeTable12() 
      {
        $this->db->select ( 'p.pet_name,p1.cat_title,f.*');
        $this->db->from ( 'food_company f' );
        $this->db->join ( 'pro_cat p1', 'f.sub_cat_id = p1.id');
        $this->db->join ( 'pet_type p', 'f.c_id = p.id');
       
        $query = $this->db->get ();
        return $query->result ();
     }

// select p.pet_name,p1.cat_title,f.c_name,f.c_img_path,f.c_desc FROM ((food_company f JOIN pro_cat p1 ON f.sub_cat_id=p1.cat_id ) JOIN pet_type p ON f.cat_id=p.id) WHERE f.c_id=43
    
       public function getDataByFourTable12() 
        {
          // $this->db->select ( 'p.pet_name,p1.cat_title,f.c_name,f1.*');
          // $this->db->from ( 'food_product f1' );
          // $this->db->join ( 'pro_cat p1', 'f1.p_pet_type = p1.id');
          // $this->db->join ( 'pet_type p', 'f1.p_type = p.id');
          // $this->db->join ( 'food_company f', 'f1.c_id = f.c_id');

         
          // $query = $this->db->get ();
          // return $query->result ();
          $sql="Select f.c_name,p.pet_name,p1.cat_title,f1.* from (((food_product f1 Join pro_cat p1 on f1.p_pet_type=p1.id ) Join pet_type p ON p.id=f1.p_type ) Join food_company f ON f1.c_id=f.c_id)";  

            $query1 = $this->db->query($sql);
            // $query1 = $query->row();
            return $query1->result_array();
}

        public function getDataByFourTable15() 
        {
 $sql="Select f.c_name,p.pet_name,p1.cat_title,(f1.p_name) as pro_name,o.* from ((((offer o Join pro_cat p1 on o.p_pet_type=p1.id ) Join pet_type p ON p.id=o.p_type ) Join food_company f ON f.c_id=o.c_id) join food_product f1 ON f1.p_id=o.p_name )";  

            $query1 = $this->db->query($sql);
            return $query1->result_array();
}
      

      public function getMyPetsMarket($user_id) 
      {
        $this->db->select ( 'pet_market.*,pet_type.pet_name,users.first_name,users.email,users.mobile_no,users.country_code,users.profile_pic' );
        $this->db->from ( 'pet_market' );
        $this->db->join ( 'pet_type', 'pet_type.id = pet_market.type_id');
        $this->db->join ( 'users', 'users.id = pet_market.id');
        $this->db->where('pet_market.user_id',$user_id);
        $this->db->where('pet_market.status',1);
        $query = $this->db->get ();
        return $query->result ();
     }

     public function getMyPetSingleMarket($pet_id) 
      {
        $this->db->select ( 'pet_market.*,pet_type.pet_name,users.first_name,users.email,users.mobile_no,users.country_code,users.profile_pic' );
        $this->db->from ( 'pet_market' );
        $this->db->join ( 'pet_type', 'pet_type.id = pet_market.type_id');
        $this->db->join ( 'users', 'users.id = pet_market.id');
        $this->db->where('pet_market.id',$pet_id);
        $this->db->where('pet_market.status',1);
        $query = $this->db->get ();
        return $query->row ();
     }

     public function getNotifyUser($user_id) 
      {
        $this->db->select ( 'pet.id,followers.follower_user_id' );
        $this->db->from ( 'pet');
        $this->db->join ( 'followers', 'pet.id = followers.pet_id');
        $this->db->where('pet.user_id',$user_id);
        $query = $this->db->get ();
      return $query->result();
     }


     public function getFilterMarcket($table,$Data,$user_id)
        {
             
              // $user_id = $Data['user_id'];
              // array_pop($Data);
              unset($Data['user_id']);
              if (!empty($Data))
             {
              foreach ($Data as $key => $value)
                  {
                    if($value)
                    {
                     $this->db->where_in($key, $value);
                    }
                    else
                    {

                    }
                   
                  }
            }
            $this->db->where('status', 1);
            $this->db->where_not_in('user_id', $user_id);
            $this->db->select("*");
            //$this->db->distinct();
            $query = $this->db->get($table);
            return $query->result();
        }

        public function send_email_order($email_id, $subject,$OrderId)
       {   
        $orderData = $this->getSingleRow('orders',array('order_id'=>$OrderId));
        if($orderData)
        {
        $this->load->library('email'); 
         $config = array(
            'protocol'  => 'smtp',
            'smtp_host' => 'ssl://smtp.googlemail.com',
            'smtp_port' => 465,
            'smtp_user' => 'samyotechindore@gmail.com',
            'smtp_pass' => 'samyo123',
            'mailtype'  => 'html',
            'charset'   => 'iso-8859-1'

        );

        $this->email->initialize($config);
        $this->email->set_mailtype("html");
        $this->email->set_newline("\r\n");

        $from_email = 'samyotechindore@gmail.com'; 
        
        $this->email->from($from_email, 'PETEATS'); 
        $this->email->to($email_id);
        $this->email->subject($subject); 
        //$this->CI->email->set_mailtype('html');

         // $datas['msg']=$msg;
         $datas['name']=$orderData->name;
         $datas['email']=$orderData->email;
         $datas['city']=$orderData->city;
         $datas['address']=$orderData->address;
         $datas['zip']=$orderData->zip;
         $datas['country']=$orderData->country;
         $datas['final_price']=$orderData->final_price;
         $datas['discount']=$orderData->discount;
         $datas['order_id']=$OrderId;
         $datas['cod_charges']=$orderData->cod_charges;
         $body = $this->load->view('orderEmail.php',$datas,TRUE);
         $this->email->message($body);
         if($this->email->send()){
          //echo "All Ok";
        }
        else{
          $this->email->print_debugger();
         }
    }

  }

    public function send_email_order1($email_id, $subject,$OrderId)
  {    
    $orderData = $this->getSingleRow('orders',array('order_id'=>$OrderId));
    print_r($orderData);

  }

  }       
    