<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Admin extends CI_Controller
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
        $this -> load -> model('Api_model');
    }

    public function index()
    {
        if(isset($_SESSION['name'])) 
        { 
           /* $data['artist']=$this->Api_model->getCountAll('artist');
            $data['user']=$this->Api_model->getCount('user', array('role'=>2));
            $data['total_revenue']=$this->Api_model->getSum('total_amount', 'booking_invoice');

            $getInvoice= $this->Api_model->getAllDataLimit('booking_invoice',5);

            $getInvoices = array();
            foreach ($getInvoice as $getInvoice)
            {
              $getBooking= $this->Api_model->getSingleRow('artist_booking', array('id'=>$getInvoice->booking_id));

              $getInvoice->booking_time= $getBooking->booking_time;
              $getInvoice->booking_date= $getBooking->booking_date;

              $getUser= $this->Api_model->getSingleRow('user', array('user_id'=>$getInvoice->user_id));

              $getInvoice->userName= $getUser->name;
              $getInvoice->address= $getUser->address;

              if($getUser->image)
              {
                $getInvoice->userImage= base_url().$getUser->image;
              }
              else
              {
                $getInvoice->userImage= base_url().'assets/images/a.png';
              }

              $get_artists= $this->Api_model->getSingleRow('artist', array('user_id'=>$getInvoice->artist_id));

              $get_cat=$this->Api_model->getSingleRow('category', array('id'=>$get_artists->category_id));

              $getInvoice->ArtistName=$get_artists->name;
              $getInvoice->categoryName=$get_cat->cat_name;

              if($get_artists->image)
              {
                $getInvoice->artistImage= base_url().$get_artists->image;
              }
              else
              {
                $getInvoice->artistImage= base_url().'assets/images/a.png';
              }

              array_push($getInvoices, $getInvoice);
            }*/  
             $total_revenue=$this->Api_model->getSum('final_price','orders');   
                  $total_pet=$this->Api_model->count('pet');   

                  $total_product=$this->Api_model->count('food_product');   
                $total_users=$this->Api_model->count('users');     

              $getorders= $this->Api_model->getAllDataLimit('orders',8);
                $data['getorders']=$getorders;
                $data['total_pet']=$total_pet;
                $data['total_revenue']=$total_revenue;
                $data['total_product']=$total_product;
                 $data['total_user']=$total_users;
                $data['page']='home';
            //$data['getInvoices']=$getInvoices;
            $this -> load -> view('common/head.php');
            $this -> load -> view('common/sidebar.php', $data);
            $this -> load ->view('dashboard.php', $data);
            $this -> load -> view('common/footer.php');
        }
        else
        {
            redirect('');
        }
    }

    /*All Artists*/
     public function post()
    {
        if(isset($_SESSION['name'])) 
        { 
            $get_post=$this->Api_model->getAllData('post');

              $data=array();
              foreach ($get_post as $get_post) {

                $postID=$get_post->postID;
                $userID=$get_post->user_id;
                $contentType='post';

                $where= array('postID'=>$postID);
                $likes=$this->Api_model->no_of_records('likes', $where);

                $get_post->likes =$likes;

                $where= array('postID'=>$postID);
               
                $comments=$this->Api_model->no_of_records('comments', $where);
                $get_post->comments =$comments;
                array_push($data, $get_post);
              } 
             
              $data['post']=$data;
            $data['page']='post';
            $this -> load -> view('common/head.php');
            $this -> load -> view('common/sidebar.php', $data);
            $this -> load ->view('post.php', $data);
            $this -> load -> view('common/footer.php');
        }
        else
        {
            redirect('');
        }
      }
   

     /*All User*/
     public function user()
    {
        if(isset($_SESSION['name'])) 
        {
            $user= $this->Api_model->getAllData('users');
            $data['user']= $user;
            $data['page']='user';
            $this -> load -> view('common/head.php');
            $this -> load -> view('common/sidebar.php', $data);
            $this -> load ->view('user.php', $data);
            $this -> load -> view('common/footer.php');
        }
        else
        {
            redirect('');
        }
      }
    

     /*All User*/
     public function coupon()
    {
        if(isset($_SESSION['name'])) 
        {
            $coupon= $this->Api_model->getAllData('discount_coupon');

            $data['coupon']= $coupon;
            $data['page']='coupon';
            $this -> load -> view('common/head.php');
            $this -> load -> view('common/sidebar.php', $data);
            $this -> load ->view('coupon.php', $data);
            $this -> load -> view('common/footer.php');
        }
        else
        {
            redirect('');
        }
      }

        /*All User*/
     public function postDetails()
      {
        if(isset($_SESSION['name'])) 
        {
            $postID= $_GET['id'];
            $post_title= $_GET['post_title'];

            $get_post=$this->Api_model->getSingleRow('post',array('postID'=>$postID));

            $get_post=$this->Api_model->get_single_post($postID);
            $post['postType']=$get_post->postType; 
            $post['content']=$get_post->content; 
            $post['title']=$get_post->title; 
            $post['createAt']=$get_post->createAt; 
            $post['media']=$get_post->media; 

            $userID=$get_post->user_id; 

            $check_user=$this->Api_model->get_user($userID);

            if($check_user->profile_pic)
            {
                $post['user_image']= base_url().$check_user->profile_pic;
            }
            else
            {
                $post['user_image']= base_url().'assets/images/dummyuser.png';
            }  

            $post['user_name']= $check_user->first_name;

            $where= array('postID'=>$postID);
            $likes=$this->Api_model->no_of_records('likes', $where);
            $post['like_count'] =$likes;

            $where= array('postID'=>$postID);
            $comments=$this->Api_model->no_of_records('comments', $where);
            $post['comments'] =$comments;
           

            $get_likes=$this->Api_model->getAllDataWhere(array('postID'=>$postID),'likes');
            $like= array();
            foreach ($get_likes  as $get_likes)
            {
               print_r($get_likes['userID']);
                $userID=$get_likes['userID'];

                $get_user=$this->Api_model->get_user($userID);

                $get_likes['userName'] =$get_user->name;

                if($get_user->image)
                {
                $get_likes['image']= base_url().$get_user->image;
                }
                else
                {
                $get_likes['image']= base_url().'assets/images/dummyuser.png';
                }          
                array_push($like, $get_likes);
            }     



            $datas= array();
            foreach ($get_likes  as $get_likes) 
            {
     
              $userID=$get_likes['userID'];
              $get_user=$this->Api_model->getAllDataWhere(array('user_id'=>$userID),'comments');

              $get_likes['userName'] =$get_user->name;
              if($get_user->image)
              {
                $get_likes['image']= base_url().$get_user->image;
              }
              else
              {
                $get_likes['image']= base_url().'assets/images/dummyuser.png';
              }
              array_push($datas, $get_likes);
            }
            $get_all_comments=$this->Api_model->getAllDataWhere($where,'comments');  
              $data['all_comments']=$get_all_comments;
          
              $data['comments']=$datas;


            $data['post']=$post;
            $data['likes']=$like;
                 $data['user_id']=$userID;  
            $data['page']='post';

            $this -> load -> view('common/head.php');
            $this -> load -> view('common/sidebar.php', $data);
            $this -> load ->view('postDetails.php', $data);
            $this -> load -> view('common/footer.php');
        }
        else
        {
            redirect('');
        }
      }

/*
add product  start
** created by kapil 
*/
PUBLIC function addbreed()
{
  if(isset($_SESSION['name']))
  {
            $data['page']='addbreed';
            $this -> load -> view('common/head.php');
            $this -> load -> view('common/sidebar.php',$data);
            $this -> load -> view('addbreed.php', $data);
            $this -> load -> view('common/footer.php');
  }
  else
  {

  }
}
public function order()
    {
if(isset($_SESSION['name'])) 
        {
             $table='orders';
             $where=array('status'=>'1');
             $getordersPending= $this->Api_model->getAllDataWhere($where, $table);
             $wherePlaced=array('status'=>'2');
			 $getordersplaced= $this->Api_model->getAllDataWhere($wherePlaced, $table);
			$data['getordersPending']=$getordersPending;
                 $data['getordersplaced']=$getordersplaced;
                
              $data['page']='order';
            $this -> load -> view('common/head.php');
            $this -> load -> view('common/sidebar.php',$data);
            $this -> load -> view('order.php', $data);
            $this -> load -> view('common/footer.php');
        }
        else
        {
 redirect('');
        }
      }
  public function addproduct()
    {
if(isset($_SESSION['name'])) 
        { 
            $data['company_food']=$this->Api_model->get_all_food();
          $data['pet_type']=$this->Api_model->get_all_pet_type();
           $data['page']='addproduct';
            $this -> load -> view('common/head.php');
            $this -> load -> view('common/sidebar.php', $data);
            $this -> load -> view('addproduct.php', $data);
            $this -> load -> view('common/footer.php');
        }
        else
        {
            redirect('');
        }
  }
  function add_product_action() {
       if(isset($_SESSION['name'])) 
         {
            $data['p_code']=time();
            $data['status']=1;
            $data['c_name']=$this->input->post('c_name');
            $data['p_name']=$this->input->post('p_name');
            $data['p_description']=$this->input->post('p_description');
            $data['p_type']=$this->input->post('p_type');
            $data['p_pet_type']=$this->input->post('p_pet_type');
            $data['p_cat_type']=$this->input->post('p_cat_type');
            $data['p_rate']=$this->input->post('p_rate');
            $data['quantity']=$this->input->post('quantity');
            $data['discount']=$this->input->post('discount');
            $img_path=$this->input->post('img_path');


            $data['p_rate_sale']=$this->getFinalAmount($data['p_rate'],$data['discount']);
             $data['gross_amt']=$this->getTotalAmount($data['p_rate'],$data['quantity']);
             $data['net_amt']=$this->getTotalAmount($data['p_rate_sale'],$data['quantity']);

             
             $config['upload_path']   = './assets/images/products/'; 
             $config['allowed_types'] = 'gif|jpg|jpeg|png';
             $config['max_size']      = 10000; 
             $config['file_name']     = time();
            $this->load->library('upload', $config);
            $this->upload->initialize($config);
             $productsimage="";
             if ( $this->upload->do_upload('img_path'))
             {
             
              $productsimage='assets/images/products/'.$this->upload->data('file_name'); 
             }
             else   
             {

             }

             $data['img_path'] =$productsimage;
            $this ->Api_model->insertGetId('food_product',$data);

            redirect('Admin/addproduct');
        }
         else
        {
            redirect('');
        }
    }
    function update_product_action() {
       if(isset($_SESSION['name'])) 
         {
            $data['p_code']=time();
            $data['status']=1;
            $data['c_name']=$this->input->post('c_name');
            $data['p_name']=$this->input->post('p_name');
            $data['p_description']=$this->input->post('p_description');
            $data['p_type']=$this->input->post('p_type');
            $data['p_pet_type']=$this->input->post('p_pet_type');
            $data['p_cat_type']=$this->input->post('p_cat_type');
            $data['p_rate']=$this->input->post('p_rate');
            $data['quantity']=$this->input->post('quantity');
            $data['discount']=$this->input->post('discount');
            $img_path=$this->input->post('img_path');


            $data['p_rate_sale']=$this->getFinalAmount($data['p_rate'],$data['discount']);
             $data['gross_amt']=$this->getTotalAmount($data['p_rate'],$data['quantity']);
             $data['net_amt']=$this->getTotalAmount($data['p_rate_sale'],$data['quantity']);

             
             $config['upload_path']   = './assets/images/products/'; 
             $config['allowed_types'] = 'gif|jpg|jpeg|png';
             $config['max_size']      = 10000; 
             $config['file_name']     = time();
            $this->load->library('upload', $config);
            $this->upload->initialize($config);
             $productsimage="";
             if ( $this->upload->do_upload('img_path'))
             {
             
              $productsimage='assets/images/products/'.$this->upload->data('file_name'); 
            $data['img_path'] =$productsimage;
             }
             else   
             {

             }

            
            $product_id=$this->uri->segment(3);
              $where = array('p_id'=>$product_id);
            $this ->Api_model->updateSingleRow('food_product', $where, $data);

            redirect('Admin//updateproduct/'.$product_id);
        }
         else
        {
            redirect('');
        }
    }
    function getFinalAmount($number, $percent){
      $s = 100 - $percent;
    return ($s * $number) /100; 
  }

    function getTotalAmount($rate, $quntity){
    return ($rate * $quntity);
}
/*
product list
*/
public function productlist()
{
  if(isset($_SESSION['name'])) 
        {
            $get_product=$this->Api_model->getAllData('food_product');
            $data['product_list']= $get_product;
            $data['page']='productlist';
            $this -> load -> view('common/head.php');
            $this -> load -> view('common/sidebar.php', $data);
            $this -> load ->view('productlist.php', $data);
            $this -> load -> view('common/footer.php');
        }
        else
        {
          redirect('');
        }
}

/*
**update product 

*/
public function updateorder()
{
  if(isset($_SESSION['name'])) 
        {
           $order_id=$this->uri->segment(3);
           $where= array('order_id'=>$order_id);
         	$data= array('status'=>'2');
         $this->Api_model->updateSingleRow('orders', $where, $data);
      
           redirect('Admin/order');
        }
        else
        {
        	redirect('');
        }
}
public function Cancelorder()
{
  if(isset($_SESSION['name'])) 
        {
           $order_id=$this->uri->segment(3);
           $where= array('order_id'=>$order_id);
         	$data= array('status'=>'1');
         $this->Api_model->updateSingleRow('orders', $where, $data);
      
           redirect('Admin/order');
        }
        else
        {
        	redirect('');
        }
}

public function updateproduct()
{
 if(isset($_SESSION['name'])) 
        {
            $product_id=$this->uri->segment(3);
            $where = array('p_id'=>$product_id);
            $data['product']=$this->Api_model->getSingleRow('food_product',$where);
            $data['company_food']=$this->Api_model->get_all_food();
            $data['pet_type']=$this->Api_model->get_all_pet_type();
            $data['page']='addproduct';
            $this -> load -> view('common/head.php');
            $this -> load -> view('common/sidebar.php', $data);
            $this -> load -> view('update_product.php', $data);
            $this -> load -> view('common/footer.php');
        }
        else
        {
          redirect('');
        }

}

/*
add product  end 
**  ereted by kapil 
*/

      /*All User*/
     public function artistDetails()
    {
        if(isset($_SESSION['name'])) 
        {
            $user_id= $_GET['id'];
            $role= $_GET['role'];
            $artist_name= $_GET['artist_name'];

            $artist=$this->Api_model->getSingleRow('artist',array('user_id'=>$user_id));

            $jobDone=$this->Api_model->getTotalWhere('artist_booking',array('artist_id'=>$user_id,'booking_flag'=>4));

            $data['total']=$this->Api_model->getTotalWhere('artist_booking',array('artist_id'=>$user_id));
            if($data['total']==0)
            {
                $data['percentages']=0;
            }
            else
            {
                $data['percentages']=($jobDone*100) / $data['total'];
            }
            

            $data['jobDone']=$jobDone;

            if($role==1)
            {      
                $where=array('artist_id'=>$user_id);   

                  $get_appointment=$this->Api_model->getAllDataWhere($where,'artist_booking');

                $get_appointments = array();
                foreach ($get_appointment as $get_appointment) 
                {
                  $get_user= $this->Api_model->getSingleRow('user', array('user_id'=>$get_appointment->user_id));

                  $get_appointment->name= $get_user->name;
                  $get_appointment->image= base_url().$get_user->image;
                  $get_appointment->address= $get_user->address;

                  array_push($get_appointments, $get_appointment);
                }

              }
              elseif($role==2)
              {
                $where=array('user_id'=>$user_id);

                $get_appointment=$this->Api_model->getAllDataWhere($where,'artist_booking');

                    $get_appointments = array();
                    foreach ($get_appointment as $get_appointment) 
                    {
                      $get_user= $this->Api_model->getSingleRow('artist', array('user_id'=>$get_appointment->artist_id));
                      $get_user->image= base_url().$get_user->image;

                      $get_appointment->name= $get_user->name;
                      $get_appointment->image= base_url().$get_user->image;
                      $get_appointment->address= $get_user->address;

                      array_push($get_appointments, $get_appointment);
                    } 
               }

            $where=array('user_id'=>$user_id);

            $get_products=$this->Api_model->getAllDataWhere($where,'products');

            $data['get_products']= $get_products;

            $where=array('artist_id'=>$user_id);

        $getInvoice=$this->Api_model->getAllDataWhere($where,'booking_invoice');

        $getInvoices = array();
        foreach ($getInvoice as $getInvoice)
        {
          $getBooking= $this->Api_model->getSingleRow('artist_booking', array('id'=>$getInvoice->booking_id));

          $getInvoice->booking_time= $getBooking->booking_time;
          $getInvoice->booking_date= $getBooking->booking_date;

          $getUser= $this->Api_model->getSingleRow('user', array('user_id'=>$getInvoice->user_id));

          $getInvoice->userName= $getUser->name;
          $getInvoice->address= $getUser->address;

          if($getUser->image)
          {
            $getInvoice->userImage= base_url().$getUser->image;
          }
          else
          {
            $getInvoice->userImage= base_url().'assets/images/a.png';
          }

          $get_artists= $this->Api_model->getSingleRow('artist', array('user_id'=>$getInvoice->artist_id));

          $get_cat=$this->Api_model->getSingleRow('category', array('id'=>$get_artists->category_id));

          $getInvoice->ArtistName=$get_artists->name;
          $getInvoice->categoryName=$get_cat->cat_name;

          if($get_artists->image)
          {
            $getInvoice->artistImage= base_url().$get_artists->image;
          }
          else
          {
            $getInvoice->artistImage= base_url().'assets/images/a.png';
          }

          array_push($getInvoices, $getInvoice);
        }


        $data['get_invoice']= $getInvoices;


            $data['get_appointments']= $get_appointments;
            $data['artist_name']= $artist_name;

            $data['user']= $artist;
            $data['page']='artist';

            $this -> load -> view('common/head.php');
            $this -> load -> view('common/sidebar.php', $data);
            $this -> load ->view('artistDetails.php', $data);
            $this -> load -> view('common/footer.php');
        }
        else
        {
            redirect('');
        }
      }


    /*Add coupon*/
    public function add_coupon()
    {
        $data['coupon_code']= $this->input->post('coupon_code', TRUE);
        $data['description']= $this->input->post('description', TRUE);
        $data['discount_type']= $this->input->post('discount_type', TRUE);
        $data['discount']= $this->input->post('discount', TRUE);

        $data['created_at']=time();
        $data['updated_at']=time();

        $this->Api_model->insertGetId('discount_coupon',$data);

         redirect('Admin/coupon');
    }


     /*Change Status Artist*/
     public function change_status_coupon()
    {
        $id= $_GET['id'];
        $status= $_GET['status'];
        $request= $_GET['request'];
        $where = array('id'=>$id);
        $data = array('status'=>$status);

        $update= $this->Api_model->updateSingleRow('discount_coupon', $where, $data);
     
        redirect('Admin/coupon');    
    }

    public function notifaction()
    {

        $data['user']= $this->Api_model->getAllData('users');
        $data['page']='notification';

        $this -> load -> view('common/head.php');
        $this -> load -> view('common/sidebar.php', $data);
        $this -> load ->view('notification.php', $data);
        $this -> load -> view('common/footer.php');
    }

     /*All Appointment*/
     public function appointments()
    {
        $user_id= $_GET['id'];
        $role= $_GET['role'];
        $artist_name= $_GET['artist_name'];

      if($role==1)
      {      
        $where=array('artist_id'=>$user_id);   

          $get_appointment=$this->Api_model->getAllDataWhere($where,'artist_booking');

        $get_appointments = array();
        foreach ($get_appointment as $get_appointment) 
        {
          $get_user= $this->Api_model->getSingleRow('user', array('user_id'=>$get_appointment->user_id));

          $get_appointment->name= $get_user->name;
          $get_appointment->image= base_url().$get_user->image;
          $get_appointment->address= $get_user->address;

          array_push($get_appointments, $get_appointment);
        }

      }
      elseif($role==2)
      {
        $where=array('user_id'=>$user_id);

        $get_appointment=$this->Api_model->getAllDataWhere($where,'artist_booking');

            $get_appointments = array();
            foreach ($get_appointment as $get_appointment) 
            {
              $get_user= $this->Api_model->getSingleRow('artist', array('user_id'=>$get_appointment->artist_id));
              $get_user->image= base_url().$get_user->image;

              $get_appointment->name= $get_user->name;
              $get_appointment->image= base_url().$get_user->image;
              $get_appointment->address= $get_user->address;

              array_push($get_appointments, $get_appointment);
            } 
       }

        $data['get_appointments']= $get_appointments;
        $data['artist_name']= $artist_name;
        $data['page']='artist';
        $this -> load -> view('common/head.php');
        $this -> load -> view('common/sidebar.php', $data);
        $this -> load ->view('appointments.php', $data);
        $this -> load -> view('common/footer.php');
    }

    /*Get All Products*/
    public function products()
    {
        $user_id= $_GET['id'];
        $role= $_GET['role'];
        $artist_name= $_GET['artist_name'];

        $where=array('user_id'=>$user_id);

        $get_products=$this->Api_model->getAllDataWhere($where,'products');

        $data['get_products']= $get_products;
        $data['artist_name']= $artist_name;
        $data['page']='artist';
        $this -> load -> view('common/head.php');
        $this -> load -> view('common/sidebar.php', $data);
        $this -> load ->view('products.php', $data);
        $this -> load -> view('common/footer.php');
    }

    /*Get All Invoice*/
    public function invoice()
    {
        $user_id= $_GET['id'];
        $artist_id= $_GET['artist_id'];
        $artist_name= $_GET['artist_name'];

        $where=array('artist_id'=>$user_id);

        $getInvoice=$this->Api_model->getAllDataWhere($where,'booking_invoice');

        $getInvoices = array();
        foreach ($getInvoice as $getInvoice)
        {
          $getBooking= $this->Api_model->getSingleRow('artist_booking', array('id'=>$getInvoice->booking_id));

          $getInvoice->booking_time= $getBooking->booking_time;
          $getInvoice->booking_date= $getBooking->booking_date;

          $getUser= $this->Api_model->getSingleRow('user', array('user_id'=>$getInvoice->user_id));

          $getInvoice->userName= $getUser->name;
          $getInvoice->address= $getUser->address;

          if($getUser->image)
          {
            $getInvoice->userImage= base_url().$getUser->image;
          }
          else
          {
            $getInvoice->userImage= base_url().'assets/images/a.png';
          }

          $get_artists= $this->Api_model->getSingleRow('artist', array('user_id'=>$getInvoice->artist_id));

          $get_cat=$this->Api_model->getSingleRow('category', array('id'=>$get_artists->category_id));

          $getInvoice->ArtistName=$get_artists->name;
          $getInvoice->categoryName=$get_cat->cat_name;

          if($get_artists->image)
          {
            $getInvoice->artistImage= base_url().$get_artists->image;
          }
          else
          {
            $getInvoice->artistImage= base_url().'assets/images/a.png';
          }

          array_push($getInvoices, $getInvoice);
        }


        $data['get_invoice']= $getInvoices;
        $data['artist_name']= $artist_name;
      
        $data['page']='artist';
        $this -> load -> view('common/head.php');
        $this -> load -> view('common/sidebar.php', $data);
        $this -> load ->view('invoice.php', $data);
        $this -> load -> view('common/footer.php');
    }
    /*Change Status Artist*/
     public function change_status_user()
    {
        $id= $_GET['id'];
        $status= $_GET['status'];
        $request= $_GET['request'];
        $where = array('id'=>$id);
        $data = array('status'=>$status);

        $update= $this->Api_model->updateSingleRow('users', $where, $data);

        if($request==1)
        {
             redirect('Admin/user');
        }
        elseif ($request==2)
        {
           redirect('Admin/artist');
        }        
    }

   public function login()
    {

        $email= $this->input->post('email', TRUE);
        $password=$this->input->post('password', TRUE);

        $data['email']= $email;
        $data['password']= md5($password);
        $sess_array=array();
        $getdata=$this->Api_model->getSingleRow('admin',$data);
        if($getdata)
        {           

            $this->session->unset_userdata($sess_array);
            $sess_array = array(
             'name' => $getdata->name,
             'id' => $getdata->id,
           );

           $this->session->set_userdata( $sess_array);
            $dataget['get_data'] =$getdata;
            $dataget['see_data'] =$sess_array;
            redirect('Admin');    
        }
        else
        {

        $this->session->set_flashdata('msg', 'Please enter valid Username or Password');
        redirect('');

        }
    }

    /* View Artist Profile*/
    public function profile_artist()
    {
        $user_id= $_GET['id'];

        $artist=$this->Api_model->getSingleRow('artist',array('user_id'=>$user_id));

        $data['user']= $artist;
        $data['page']='artist';
        $this -> load -> view('common/head.php');
        $this -> load -> view('common/sidebar.php', $data);
        $this -> load ->view('profile.php', $data);
        $this -> load -> view('common/footer.php');
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

    public function logout()
    {
      $this->session->sess_destroy();         
        redirect('../', 'refresh');
    }
}