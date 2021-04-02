<?php
    /**
    * 
    */
    class Comman_model extends CI_Model
    {
        
        function __construct()
        {
            parent:: __construct();
        }

        /*Get single row data*/
        public function getSingleRow()
        {
            echo 'hello';
            die();
           /* $this->db->select('*');
            $this->db->from($table);
            $this->db->where($condition);
            $query = $this->db->get();
            return $query->row();       */
        }

        /*Update any data*/
         public function updateSingleRow($table, $where, $data)
        {
            $this->db->where($where);
            $this->db->update($table,$data);
        }

        public function getUserByCounrty($country)
        { 
          $country = explode(',',$country);  
          $result = "" . implode ( "', '", $country ) . "";  
          $query =$this->db->query("SELECT * FROM `users` WHERE `country` IN ('".$result."') AND `status`='1'");
          return $query->result();
       }

       public function get_post_by_country($limit, $offset,$where,$userId) 
    {
        
      $this->db->where($where);
      $this->db->where_in('user_id',$userId);    
      $this->db->select('*');
      $this->db->order_by('postID', 'desc');  
      $this->db->from('post');
      $this->db->limit($limit, $offset);
      $query = $this->db->get();
      return $query->result_array();
     }

    }       