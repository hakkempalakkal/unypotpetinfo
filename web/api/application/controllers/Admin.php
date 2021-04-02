<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Admin extends CI_Controller
{
    
    public function __construct()
    {
        require_once APPPATH . "/third_party/FCMPushNotification.php";
        parent::__construct();
        $this->load->library('session');
        $this->load->helper('form');
        $this->load->helper('url');
        $this->load->database();
        $this->load->library('form_validation');
        $this->load->model('Comman_model');
        $this->load->model('Api_model');
        // $this->load->model('excel_import_model');
        // $this->load->library('excel');
    }
    
    public function index()
    {
        if (isset($_SESSION['name'])) {
           
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
            $total_revenue = $this->Api_model->getSum('final_price', 'orders');
            $total_pet     = $this->Api_model->count('pet');
            
            $total_product = $this->Api_model->countWhere('food_product', array(
                'deleted' => 1
            ));

            $total_users   = $this->Api_model->count('users');
            
            $getorders             = $this->Api_model->getAllDataLimit('orders',10);
            $data['getorders']     = $getorders;
            $data['total_pet']     = $total_pet;
            $data['total_revenue'] = $total_revenue;
            $data['total_product'] = $total_product;
            $data['total_user']    = $total_users;
            $data['page']          = 'home';
            //$data['getInvoices']=$getInvoices;
            $this->load->view('common/head.php');
            $this->load->view('common/sidebar.php', $data);
            $this->load->view('dashboard.php', $data);
            $this->load->view('common/footer.php');
        } else {
            redirect('');
        }
    }
    
   


    /*All Artists*/
    public function post()
    {
        if (isset($_SESSION['name'])) {
            $get_post = $this->Api_model->getAllData('post');
            
            $data = array();
            foreach ($get_post as $get_post) {
                
                $postID      = $get_post->postID;
                $userID      = $get_post->user_id;
                $contentType = 'post';
                
                $where = array(
                    'postID' => $postID
                );
                $likes = $this->Api_model->no_of_records('likes', $where);
                
                $get_post->likes = $likes;
                
                $where = array(
                    'postID' => $postID
                );
                
                $comments           = $this->Api_model->no_of_records('comments', $where);
                $get_post->comments = $comments;
                array_push($data, $get_post);
            }
            
            $data['post'] = $data;
            $data['page'] = 'post';
            $this->load->view('common/head.php');
            $this->load->view('common/sidebar.php', $data);
            $this->load->view('post.php', $data);
            $this->load->view('common/footer.php');
        } else {
            redirect('');
        }
    }
    
    
    /*All User*/
    public function user()
    {
        if (isset($_SESSION['name'])) {
            
            $user         = $this->Api_model->getAllData('users');
            $data['user'] = $user;
            $data['page'] = 'user';
            $this->load->view('common/head.php');
            $this->load->view('common/sidebar.php', $data);
            $this->load->view('user.php', $data);
            $this->load->view('common/footer.php');
        } else {
            redirect('');
        }
    }
    
    
    /*All User*/
    public function coupon()
    {
        if (isset($_SESSION['name'])) {
            $coupon = $this->Api_model->getAllData('discount_coupon');
            
            $data['coupon'] = $coupon;
            $data['page']   = 'coupon';
            $this->load->view('common/head.php');
            $this->load->view('common/sidebar.php', $data);
            $this->load->view('coupon.php', $data);
            $this->load->view('common/footer.php');
        } else {
            redirect('');
        }
    }
    
    /*All User*/
    public function postDetails()
    {
        if (isset($_SESSION['name'])) {
            
            $postID = $_GET['id'];
            
            $post_title = $_GET['post_title'];
            
            $get_post = $this->Api_model->getSingleRow('post', array(
                'postID' => $postID
            ));
            
            
            $get_post         = $this->Api_model->get_single_post($postID);
            $post['postType'] = $get_post->postType;
            $post['content']  = $get_post->content;
            $post['title']    = $get_post->title;
            $post['createAt'] = $get_post->createAt;
            $post['media']    = $get_post->media;
            
            
            
            $userID = $get_post->user_id;
            
            $check_user = $this->Api_model->get_user($userID);
            
            
            if ($check_user->profile_pic) {
                $post['user_image'] = base_url() . $check_user->profile_pic;
            } else {
                $post['user_image'] = base_url() . 'assets/images/dummyuser.png';
            }
            
            $post['user_name'] = $check_user->first_name;
            
            $where              = array(
                'postID' => $postID
            );
            $likes              = $this->Api_model->no_of_records('likes', $where);
            $post['like_count'] = $likes;
            
            
            $where            = array(
                'postID' => $postID
            );
            $comments         = $this->Api_model->no_of_records('comments', $where);
            $post['comments'] = $comments;
            
            
            
            $get_likes = $this->Api_model->getAllDataWhere(array(
                'postID' => $postID
            ), 'likes');
            
            $like = array();
            
            foreach ($get_likes as $get_likes) {
                
                $userID = $get_likes->user_id;
                
                $get_user = $this->Api_model->get_user($userID);
                
                $get_likes->userName = $get_user->first_name;
                
                if ($get_user->profile_pic) {
                    $get_likes->image = base_url() . $get_user->profile_pic;
                } else {
                    $get_likes->image = base_url() . 'assets/images/dummyuser.png';
                }
                array_push($like, $get_likes);
            }
            
            $get_user = $this->Api_model->getAllDataWhere(array(
                'user_id' => $userID
            ), 'comments');
            
            $datas = array();
            
            
            
            foreach ($get_user as $get_user1) {
                
                $userID = $get_user1->user_id;
                
                // $get_likes->userName = $get_user1->first_name;
                
                // if($get_user1->profile_pic)
                // {
                // $get_likes->image = base_url().$get_user1->profile_pic;
                // }
                // else
                // {
                //  $get_likes->image = base_url().'assets/images/dummyuser.png';
                // }
                array_push($datas, $get_likes);
            }
            $get_abuse_post         = $this->Api_model->getAllDataWhere($where, 'abuse_post');
            $data['all_abuse_post'] = $get_abuse_post;
            $get_all_comments       = $this->Api_model->getAllDataWhere($where, 'comments');
            $data['all_comments']   = $get_all_comments;
            $data['comments']       = $datas;
            $data['post']           = $post;
            $data['likes']          = $like;
            $data['user_id']        = $userID;
            $data['page']           = 'post';
            
            $this->load->view('common/head.php');
            $this->load->view('common/sidebar.php', $data);
            $this->load->view('postDetails.php', $data);
            $this->load->view('common/footer.php');
        } else {
            redirect('');
        }
    }
    
    public function deletePost()
    {
        $post = $this->uri->segment('3');
        $this->Api_model->deleteRecord(array(
            'postID' => $post
        ), 'post');
        $this->Api_model->deleteRecord(array(
            'postID' => $post
        ), 'abuse_post');
        
        
        redirect('Admin/post');
    }
    
    public function deleteComment()
      {
    $post = $this->uri->segment('3');
    $this->Api_model->deleteRecord(array('commentID'=>$post),'comments');
    
    
        redirect('Admin/post');
      }
    
    
    public function deleteVeterinary()
    {
        $id = $this->uri->segment('3');
        $this->Api_model->deleteRecord(array(
            'id' => $id
        ), 'nearby_vss');
        redirect('Admin/veterinary');
        
    }
    public function deleteSalon()
    {
        $id = $this->uri->segment('3');
        $this->Api_model->deleteRecord(array(
            'id' => $id
        ), 'nearby_vss');
        redirect('Admin/Salon');
        
    }
    
    public function deleteShop()
    {
        $id = $this->uri->segment('3');
        $this->Api_model->deleteRecord(array(
            'id' => $id
        ), 'nearby_vss');
        redirect('Admin/Shop');
        
    }
    
    public function deleteHostels()
    {
        $id = $this->uri->segment('3');
        $this->Api_model->deleteRecord(array(
            'id' => $id
        ), 'nearby_vss');
        redirect('Admin/Hostels');
        
    }

    public function deleteTrainer()
    {
        $id = $this->uri->segment('3');
        $this->Api_model->deleteRecord(array(
            'id' => $id
        ), 'nearby_vss');
        redirect('Admin/Trainer');
        
    }
    


    public function pet_type_details()
    {
        if (isset($_SESSION['name'])) {
            
            $data['pet_type'] = $this->Api_model->getAllData('pet_type');
            $this->load->view('common/head.php');
            $this->load->view('common/sidebar.php');
            $this->load->view('addPetType', $data);
            $this->load->view('common/footer.php');
        } else {
            redirect('');
        }
    }

     public function all_banners()
    {
        if (isset($_SESSION['name'])) {
            $data['banner'] = $this->Api_model->getAllData('banner');
            $this->load->view('common/head.php');
            $this->load->view('common/sidebar.php');
            $this->load->view('banner', $data);
            $this->load->view('common/footer.php');
        } else {
            redirect();
        }
    }
    
      public function viewoffer()
      {
     if(isset($_SESSION['name'])){  
        // $viewoffer= $this->Api_model->getDataByFourTable15(); 
         $data['offer'] = $this->Api_model->getDataByFourTable15();

       // $data['viewoffer'] = $viewoffer;
       //    $data['page'] = 'viewoffer';

       $this->load -> view('common/head.php');
        $this ->load-> view('common/sidebar.php');
        $this->load ->view('viewoffer',$data);
        $this ->load -> view('common/footer.php');
    }
      else
        {
              redirect('');
        }
    }

    public function contact_information()
      {
     if(isset($_SESSION['name'])){  
        // $viewoffer= $this->Api_model->getDataByFourTable15(); 
         $data['contact'] = $this->Api_model->getDataBycontactinfo();

       // $data['viewoffer'] = $viewoffer;
       //    $data['page'] = 'viewoffer';

       $this->load -> view('common/head.php');
        $this ->load-> view('common/sidebar.php');
        $this->load ->view('contact',$data);
        $this ->load -> view('common/footer.php');
    }
      else
        {
              redirect('');
        }
    }
    
    public function viewbreed_information()
      {
     if(isset($_SESSION['name'])){  
        // $viewoffer= $this->Api_model->getDataByFourTable15(); 
         $data['breed'] = $this->Api_model->getDataBybreedinfo();

       // $data['viewoffer'] = $viewoffer;
       //    $data['page'] = 'viewoffer';

       $this->load -> view('common/head.php');
        $this ->load-> view('common/sidebar.php');
        $this->load ->view('viewbreed_information',$data);
        $this ->load -> view('common/footer.php');
    }
      else
        {
              redirect('');
        }
    }


    
    
    public function deleteCategory()
    {
        $company = $this->uri->segment('3');
        $this->Api_model->deleteRecord(array(
            'id' => $company
        ), 'pro_cat');
        //$this->Api_model->deleteRecord(array('p_id'=>$product_id), 'pro_images');
        redirect('Admin/addcategory');
    }

    public function deleteBanner()
    {
        $banner = $this->uri->segment('3');
        $this->Api_model->deleteRecord(array(
            'id' => $banner
        ), 'banner');
        //$this->Api_model->deleteRecord(array('p_id'=>$product_id), 'pro_images');
        redirect('Admin/all_banners');
    }
    
    public function change_cat_status()
    {
        $id     = $_GET['id'];
        $status = $_GET['status'];
        $where  = array(
            'id' => $id
        );
        $data   = array(
            'status' => $status
        );
        
        $update = $this->Api_model->updateSingleRow('pro_cat', $where, $data);
        
        if ($update) {
            redirect('Admin/addCategory');
        }
    }
    
    
    public function change_veterinary_status()
    {
        $id      = $_GET['id'];
        $status  = $_GET['status'];
        $request = $_GET['request'];
        $where   = array(
            'id' => $id
        );
        $data    = array(
            'status' => $status
        );
        $update  = $this->Api_model->updateSingleRow('nearby_vss', $where, $data);
        
        if ($request == 1) {
            $getdata  = $this->Api_model->getSingleRow('nearby_vss', $where);
            $user_id  = $getdata->id;
            $userdata = $this->Api_model->getSingleRow('nearby_vss', array(
                'id' => $id
            ));
            $msg      = ACTIVATE;
            
            $this->Api_model->send_email($email, ACTIVATE, $msg);
            redirect('Admin/Veterinary');
        } elseif ($request == 2) {
            $getdata  = $this->Api_model->getSingleRow('nearby_vss', $where);
            $id       = $getdata->id;
            $getToken = $this->Api_model->getSingleRow('nearby_vss', array(
                'id' => $id
            ));
            $userdata = $this->Api_model->getSingleRow('nearby_vss', array(
                'id' => $id
            ));
            $msg      = INACTIVATE;
            $this->Api_model->send_email($email, INACTIVATE, $msg);
            redirect('Admin/Veterinary');
        }
    }
    
    
    public function change_shop_status()
    {
        $id      = $_GET['id'];
        $status  = $_GET['status'];
        $request = $_GET['request'];
        $where   = array(
            'id' => $id
        );
        $data    = array(
            'status' => $status
        );
        $update  = $this->Api_model->updateSingleRow('nearby_vss', $where, $data);
        
        if ($request == 1) {
            $getdata  = $this->Api_model->getSingleRow('nearby_vss', $where);
            $user_id  = $getdata->id;
            $userdata = $this->Api_model->getSingleRow('nearby_vss', array(
                'id' => $id
            ));
            $msg      = ACTIVATE;
            
            $this->Api_model->send_email($email, ACTIVATE, $msg);
            redirect('Admin/Shop');
        } elseif ($request == 2) {
            $getdata  = $this->Api_model->getSingleRow('nearby_vss', $where);
            $id       = $getdata->id;
            $getToken = $this->Api_model->getSingleRow('nearby_vss', array(
                'id' => $id
            ));
            $userdata = $this->Api_model->getSingleRow('nearby_vss', array(
                'id' => $id
            ));
            $msg      = INACTIVATE;
            $this->Api_model->send_email($email, INACTIVATE, $msg);
            redirect('Admin/Shop');
        }
    }
    
    public function change_salon_status()
    {
        $id      = $_GET['id'];
        $status  = $_GET['status'];
        $request = $_GET['request'];
        $where   = array(
            'id' => $id
        );
        $data    = array(
            'status' => $status
        );
        $update  = $this->Api_model->updateSingleRow('nearby_vss', $where, $data);
        
        if ($request == 1) {
            $getdata  = $this->Api_model->getSingleRow('nearby_vss', $where);
            $user_id  = $getdata->id;
            $userdata = $this->Api_model->getSingleRow('nearby_vss', array(
                'id' => $id
            ));
            $msg      = ACTIVATE;
            
            $this->Api_model->send_email($email, ACTIVATE, $msg);
            redirect('Admin/Salon');
        } elseif ($request == 2) {
            $getdata  = $this->Api_model->getSingleRow('nearby_vss', $where);
            $id       = $getdata->id;
            $getToken = $this->Api_model->getSingleRow('nearby_vss', array(
                'id' => $id
            ));
            $userdata = $this->Api_model->getSingleRow('nearby_vss', array(
                'id' => $id
            ));
            $msg      = INACTIVATE;
            $this->Api_model->send_email($email, INACTIVATE, $msg);
            redirect('Admin/Salon');
        }
    }
    public function change_order_status()
    {
        $id      = $_GET['id'];
        $status  = $_GET['status'];
        $request = $_GET['request'];
        $where   = array(
            'id' => $id
        );
        $data    = array(
            'status' => $status
        );
        $update  = $this->Api_model->updateSingleRow('orders', $where, $data);
        
        if ($request == 6) {
            $getdata  = $this->Api_model->getSingleRow('orders', $where);
            $user_id  = $getdata->id;
            $userdata = $this->Api_model->getSingleRow('orders', array(
                'id' => $id
            ));
            $msg      = Pending;
            $this->Api_model->send_email($email, Pending, $msg);
            redirect('Admin/order');
        } 
        elseif ($request == 5) {
            $getdata  = $this->Api_model->getSingleRow('orders', $where);
            $id       = $getdata->id;
            $getToken = $this->Api_model->getSingleRow('orders', array(
                'id' => $id
            ));
            $userdata = $this->Api_model->getSingleRow('orders', array(
                'id' => $id
            ));
            $msg      = Canceled;
            $this->Api_model->send_email($email, Canceled, $msg);
            redirect('Admin/order');
         }
         elseif ($request == 2) {
            $getdata  = $this->Api_model->getSingleRow('orders', $where);
            $id       = $getdata->id;
            $getToken = $this->Api_model->getSingleRow('orders', array(
                'id' => $id
            ));
            $userdata = $this->Api_model->getSingleRow('orders', array(
                'id' => $id
            ));
            $msg      = Delivered;
            $this->Api_model->send_email($email, Delivered, $msg);
            redirect('Admin/order');
        } 
        elseif ($request == 3) {
            $getdata  = $this->Api_model->getSingleRow('orders', $where);
            $id       = $getdata->id;
            $getToken = $this->Api_model->getSingleRow('orders', array(
                'id' => $id
            ));
            $userdata = $this->Api_model->getSingleRow('orders', array(
                'id' => $id
            ));
            $msg      = Packed;
            $this->Api_model->send_email($email, Packed, $msg);
            redirect('Admin/order');
        } 
        elseif ($request == 4) {
            $getdata  = $this->Api_model->getSingleRow('orders', $where);
            $id       = $getdata->id;
            $getToken = $this->Api_model->getSingleRow('orders', array(
                'id' => $id
            ));
            $userdata = $this->Api_model->getSingleRow('orders', array(
                'id' => $id
            ));
            $msg      = Dispatched;
            $this->Api_model->send_email($email, Dispatched, $msg);
            redirect('Admin/order');
        } 
        else 
        {
            redirect('Admin/order');
        }
    }
    
    public function change_hostels_status()
    {
        $id      = $_GET['id'];
        $status  = $_GET['status'];
        $request = $_GET['request'];
        $where   = array(
            'id' => $id
        );
        $data    = array(
            'status' => $status
        );
        $update  = $this->Api_model->updateSingleRow('nearby_vss', $where, $data);
        
        if ($request == 1) {
            $getdata  = $this->Api_model->getSingleRow('nearby_vss', $where);
            $user_id  = $getdata->id;
            $userdata = $this->Api_model->getSingleRow('nearby_vss', array(
                'id' => $id
            ));
            $msg      = ACTIVATE;
            
            $this->Api_model->send_email($email, ACTIVATE, $msg);
            redirect('Admin/Hostels');
        } elseif ($request == 2) {
            $getdata  = $this->Api_model->getSingleRow('nearby_vss', $where);
            $id       = $getdata->id;
            $getToken = $this->Api_model->getSingleRow('nearby_vss', array(
                'id' => $id
            ));
            $userdata = $this->Api_model->getSingleRow('nearby_vss', array(
                'id' => $id
            ));
            $msg      = INACTIVATE;
            $this->Api_model->send_email($email, INACTIVATE, $msg);
            redirect('Admin/Hostels');
        }
    }
    
    public function change_trainer_status()
    {
        $id      = $_GET['id'];
        $status  = $_GET['status'];
        $request = $_GET['request'];
        $where   = array(
            'id' => $id
        );
        $data    = array(
            'status' => $status
        );
        $update  = $this->Api_model->updateSingleRow('nearby_vss', $where, $data);
        
        if ($request == 1) {
            $getdata  = $this->Api_model->getSingleRow('nearby_vss', $where);
            $user_id  = $getdata->id;
            $userdata = $this->Api_model->getSingleRow('nearby_vss', array(
                'id' => $id
            ));
            $msg      = ACTIVATE;
            
            $this->Api_model->send_email($email, ACTIVATE, $msg);
            redirect('Admin/Trainer');
        } elseif ($request == 2) {
            $getdata  = $this->Api_model->getSingleRow('nearby_vss', $where);
            $id       = $getdata->id;
            $getToken = $this->Api_model->getSingleRow('nearby_vss', array(
                'id' => $id
            ));
            $userdata = $this->Api_model->getSingleRow('nearby_vss', array(
                'id' => $id
            ));
            $msg      = INACTIVATE;
            $this->Api_model->send_email($email, INACTIVATE, $msg);
            redirect('Admin/Trainer');
        }
    }


     public function change_offer_status()
    {
        $id      = $_GET['id'];
        $status  = $_GET['status'];
        $request = $_GET['request'];
        $where   = array(
            'id' => $id
        );
        $data    = array(
            'status' => $status
        );
        $update  = $this->Api_model->updateSingleRow('offer', $where, $data);
        
        if ($request == 1) {
            $getdata  = $this->Api_model->getSingleRow('offer', $where);
            $user_id  = $getdata->id;
            $userdata = $this->Api_model->getSingleRow('offer', array(
                'id' => $id
            ));
            $msg      = ACTIVATE;
            
            $this->Api_model->send_email($email, ACTIVATE, $msg);
            redirect('Admin/viewoffer');
        } elseif ($request == 2) {
            $getdata  = $this->Api_model->getSingleRow('offer', $where);
            $id       = $getdata->id;
            $getToken = $this->Api_model->getSingleRow('offer', array(
                'id' => $id
            ));
            $userdata = $this->Api_model->getSingleRow('offer', array(
                'id' => $id
            ));
            $msg      = INACTIVATE;
            $this->Api_model->send_email($email, INACTIVATE, $msg);
            redirect('Admin/viewoffer');
        }
    }
    
    
  
    
    // public function change_shop_status()
    // {
    //     $id= $_GET['id'];
    //     $status= $_GET['status'];
    //     $where = array('id'=>$id);
    //     $data = array('status'=>$status);
    
    //     $update= $this->Api_model->updateSingleRow('nearby_vss', $where, $data);
    
    //       if($update){
    //       redirect('Admin/Shop');
    //     }        
    // }
    
    public function change_pet_type_status()
    {
        $id     = $_GET['id'];
        $status = $_GET['status'];
        $where  = array(
            'id' => $id
        );
        $data   = array(
            'status' => $status
        );
        
        $update = $this->Api_model->updateSingleRow('pet_type', $where, $data);
        
        if ($update) {
            redirect('Admin/pet_type_details');
        }
    }
    
    function addCategory()
    {
        if (isset($_SESSION['name'])) {
            $this->form_validation->set_rules('cat_name', 'Category Name', 'required');
            $this->form_validation->set_rules('pet_type', 'Pet Name ', 'required');
            if ($this->form_validation->run() == FALSE) {
                $data['get_company'] = $this->Api_model->getAllDataByTwoTable12();
                //            $data['get_company'] = $this->Api_model->getAllData('pro_cat');
                
                $data['pet_type']  = $this->Api_model->getAllData('pet_type');
                $data['pet_type1'] = $this->Api_model->getAllData('pet_type');
                
                $data['page'] = 'Add Category';
                $this->load->view('common/head.php');
                $this->load->view('common/sidebar.php', $data);
                $this->load->view('addcategory.php', $data);
                $this->load->view('common/footer.php');
            } else {
                $data['pet_type']        = $this->input->post('pet_type');
                $data['cat_title']       = $this->input->post('cat_name');
                // $data['cat_desc']       =   $this->input->post('desc');
                $data['created_at']      = date('Y-m-d H:i:s');
                $data['updated_on']      = date('Y-m-d H:i:s');
                $config['upload_path']   = './assets/images/category/';
                $config['allowed_types'] = 'gif|jpg|jpeg|png';
                $config['max_size']      = 10000;
                $config['file_name']     = time();
                $this->load->library('upload', $config);
                $this->upload->initialize($config);
                $productsimage = "";
                if ($this->upload->do_upload('img_path')) {
                    
                    $productsimage      = 'assets/images/category/' . $this->upload->data('file_name');
                    $data['c_img_path'] = $productsimage;
                } else {
                    echo "image is not inserted";
                }
                $this->Api_model->insert('pro_cat', $data);
                redirect('Admin/addCategory');
                
            }
            
        } else {
            redirect();
        }
    }
    
    
    public function update_category_action()
    {
        if (isset($_SESSION['name'])) {
            $data['pet_type'] = $this->Api_model->getAllData('pet_type');
            $company_id       = $this->input->post('id');
            
            $data['cat_title'] = $this->input->post('company_name');
            // $data['pet_type1']=$this->Api_model->getAllData('pet_type');
            $data['pet_type']  = $this->input->post('pet_type1');
            if ($_FILES['img_path']['name'] != "") {
                
                $config['upload_path']   = './assets/images/category/';
                $config['allowed_types'] = 'gif|jpg|jpeg|png';
                $config['max_size']      = 10000;
                $config['file_name']     = time();
                $this->load->library('upload', $config);
                $this->upload->initialize($config);
                $productsimage = "";
                if ($this->upload->do_upload('img_path')) {
                    
                    $productsimage      = 'assets/images/category/' . $this->upload->data('file_name');
                    $data['c_img_path'] = $productsimage;
                } else {
                    echo "image is not inserted";
                }
            }
            $where = array(
                'id' => $company_id
            );
            $this->Api_model->updateSingleRow('pro_cat', $where, $data);
            redirect('Admin/addcategory');
        } else {
            
            redirect('');
        }
    }
    
    
    public function update_category()
    {
        if (isset($_SESSION['name'])) {
            $company_id          = $this->uri->segment(3);
            $where               = array(
                'id' => $company_id
            );
            $data['get_company'] = $this->Api_model->getAllDataByTwoTable12();
            
            $data['pet_type1'] = $this->Api_model->getAllData('pet_type');
            $data['company']   = $this->Api_model->getSingleRow('pro_cat', $where);
            $data['page']      = 'updatecategory';
            $this->load->view('common/head.php');
            $this->load->view('common/sidebar.php', $data);
            $this->load->view('update_category.php', $data);
            $this->load->view('common/footer.php');
        } else {
            redirect('');
        }
        
    }
    
    public function deletePetType()
    {
        $pet = $this->uri->segment('3');
        $this->Api_model->deleteRecord(array(
            'id' => $pet
        ), 'pet_type');
        //$this->Api_model->deleteRecord(array('p_id'=>$product_id), 'pro_images');
        redirect('Admin/pet_type_details');
    }
    
    
    function add_pet_type_action()
    {
        if (isset($_SESSION['name'])) {
            $data['pet_type'] = $this->Api_model->getAllData('pet_type');
            $this->form_validation->set_rules('cat_name', 'Name', 'required');
            if ($this->form_validation->run() == FALSE) {
                $data['page'] = 'Add Pet Type';
                $this->load->view('common/head.php');
                $this->load->view('common/sidebar.php', $data);
                $this->load->view('addPetType.php', $data);
                $this->load->view('common/footer.php');
            } else {
                $datas['pet_name']       = $this->input->post('cat_name');
                $datas['created_at']     = time();
                $config['upload_path']   = './assets/images/category/';
                $config['allowed_types'] = 'gif|jpg|jpeg|png';
                $config['max_size']      = 10000;
                $config['file_name']     = time();
                $this->load->library('upload', $config);
                $this->upload->initialize($config);
                $productsimage = "";
                if ($this->upload->do_upload('img_path')) {
                    
                    $productsimage      = 'assets/images/category/' . $this->upload->data('file_name');
                    $datas['pet_image'] = $productsimage;
                } else {
                    echo "image is not inserted";
                }
                $this->Api_model->insert('pet_type', $datas);
                redirect('Admin/pet_type_details');
                
            }
            
        } else {
            redirect();
        }
    }
    
     function add_banners()
    {
        if (isset($_SESSION['name'])) {
            $data['banner'] = $this->Api_model->getAllData('banner');
            // $this->form_validation->set_rules('name', 'Name', 'required');
            if ($this->form_validation->run() == TRUE) {
                $data['page'] = 'Add Banner';
                $this->load->view('common/head.php');
                $this->load->view('common/sidebar.php', $data);
                $this->load->view('banner.php', $data);
                $this->load->view('common/footer.php');
            } else {
                // $datas['name']       = $this->input->post('name');
                  // $datas['image']       = $this->input->post('image');
                // $datas['created_at']     = time();
                $config['upload_path']   = './assets/images/category/';
                $config['allowed_types'] = 'gif|jpg|jpeg|png';
                $config['max_size']      = 10000;
                $config['file_name']     = time();
                $this->load->library('upload', $config);
                $this->upload->initialize($config);
                $productsimage = "";
                if ($this->upload->do_upload('img_path')) {
                    $productsimage      = 'assets/images/category/' . $this->upload->data('file_name');
                    $datas['b_image'] = $productsimage;
                } else {
                   
                    echo "image is not inserted";
                }
              

                $this->Api_model->insert('banner', $datas);
                redirect('Admin/all_banners');
                
            }
            
        } else {
            redirect();
        }
    }
    
   
    
    public function update_pet_type_action()
    {
        if (isset($_SESSION['name'])) {
            $company_id       = $this->input->post('company_id');
            $data['pet_name'] = $this->input->post('company_name');
            if ($_FILES['img_path']['name'] != "") {
                $config['upload_path']   = './assets/images/category/';
                $config['allowed_types'] = 'gif|jpg|jpeg|png';
                $config['max_size']      = 10000;
                $config['file_name']     = time();
                $this->load->library('upload', $config);
                $this->upload->initialize($config);
                $productsimage = "";
                if ($this->upload->do_upload('img_path')) {
                    
                    $productsimage     = 'assets/images/category/' . $this->upload->data('file_name');
                    $data['pet_image'] = $productsimage;
                } else {
                    echo "image is not inserted";
                }
            }
            
            $where = array(
                'id' => $company_id
            );
            $this->Api_model->updateSingleRow('pet_type', $where, $data);
            // $this->Api_model->updateSingleRow('breed',$where, $data);
            redirect('Admin/pet_type_details');
            
            
        } else {
            redirect('Admin/login');
        }
    }
    
    
    /*
    add product  start
    ** created by kapil 
    */
    public function addbreed()
    {
        if (isset($_SESSION['name'])) {
            $data['page']     = 'addbreed';
            $data['pet_type'] = $this->Api_model->get_all_pet_type();
            $this->load->view('common/head.php');
            $this->load->view('common/sidebar.php', $data);
            $this->load->view('addbreed.php', $data);
            $this->load->view('common/footer.php');
        } else {
            redirect();
        }
    }
    

     public function aboutUs()
    {
       
            $this->load->view('aboutUs.php');
    }

    
     public function policy()
    {
       
            $this->load->view('Policy.php');
    }

  
     public function TermsAndCondition()
    {
       
            $this->load->view('TermsAndCondition.php');
    }

     public function contactUs()
    {

            $this->load->view('contactUs.php');              
    }


      public function  contactDetails()
    {
        if (isset($_SESSION['name'])) {
            $data['page']     = 'contactDetails';
            $data['contact'] = $this->Api_model->getAllData('contact');

            $this->load->view('common/head.php');
            $this->load->view('common/sidebar.php', $data);
            $this->load->view('contactDetails.php', $data);
            $this->load->view('common/footer.php');
        } else {
            redirect();
        }
    }
    
    
    
    
    public function order()
    {
        if (isset($_SESSION['name'])) {
            $table                   = 'orders';
            $where                   = array(
                'payment_status' => '1'
            );
            $where1                  = array(
                'payment_status' => '0'
            );
            $getordersPpaid          = $this->Api_model->getAllDataaWhere($where, $table);
            $getordersUnppaid        = $this->Api_model->getAllDataaWhere($where1, $table);
            $data['invoice']         = $this->Api_model->getAllDataWhereGroupBy(array('status'=>1),'txn_id', 'product_invoice');
            $wherePlaced             = array(
                'status' => '2'
            );
            $getordersplaced         = $this->Api_model->getAllDataWhere($wherePlaced, $table);
            $data['getordersPaid']   = $getordersPpaid;
            $data['getordersUnpaid'] = $getordersUnppaid;
            $data['getordersplaced'] = $getordersplaced;
            
            $data['page'] = 'order';
            $this->load->view('common/head.php');
            $this->load->view('common/sidebar.php', $data);
            $this->load->view('order.php', $data);
            $this->load->view('common/footer.php');
        } else {
            redirect('');
        }
    }





  public function addoffer()
    {
        if (isset($_SESSION['name'])) {
            $data['company_food'] = $this->Api_model->get_all_food();
            $data['pet_type']     = $this->Api_model->get_all_pet_type();
            
            
            $data['pro_cat']        = $this->Api_model->getAllDataWhere(array(
                'status' => 1
            ), 'pro_cat');
            $data['attribute']      = $this->Api_model->getAllDataWhere(array(
                'status' => 1
            ), 'attribute');
         
           $data['page'] = 'addoffer';
            $this->load->view('common/head.php');
            $this->load->view('common/sidebar.php', $data);
            $this->load->view('addoffer.php', $data);
            $this->load->view('common/footer.php');
        } else {
            redirect('');
        }
    }

    public function addbreed_info()
    {
        if (isset($_SESSION['name'])) {

            $data['breed'] = $this->Api_model->get_all_breedinfo();

           $data['page'] = 'addbreed_info';
            $this->load->view('common/head.php');
            $this->load->view('common/sidebar.php', $data);
            $this->load->view('addbreed_info.php', $data);
            $this->load->view('common/footer.php');
        } else {
            redirect('');
        }
    }

    public function contactadd()
    {
        if (isset($_SESSION['name'])) {

           //$data['breed'] = $this->Api_model->get_all_breedinfo();

           $data['page'] = 'addbreed_info';
            $this->load->view('common/head.php');
            $this->load->view('common/sidebar.php', $data);
            $this->load->view('contact_info.php', $data);
            $this->load->view('common/footer.php');
        } else {
            redirect('');
        }
    }
   


    
    public function addproduct()
    {
        if (isset($_SESSION['name'])) {
            $data['company_food'] = $this->Api_model->get_all_food();
            $data['pet_type']     = $this->Api_model->get_all_pet_type();
            
            $data['pro_cat']        = $this->Api_model->getAllDataWhere(array(
                'status' => 1
            ), 'pro_cat');
            $data['attribute']      = $this->Api_model->getAllDataWhere(array(
                'status' => 1
            ), 'attribute');
            $data['attributeSize']  = $this->Api_model->getAllDataWhere(array(
                'status' => 1
            ), 'pro_size');
            $data['attributeColor'] = $this->Api_model->getAllDataWhere(array(
                'status' => 1
            ), 'pro_color');
            // $data['attributeWeight']=$this->Api_model->getAllDataWhere(array('status'=>1),'pro_weight');
            
            $data['country'] = $this->Api_model->getAllDataWhere(array(
                'status' => 1
            ), 'country');
            
            // $data['country']=$this->Api_model->getAllData('country');
            $data['page'] = 'addproduct';
            $this->load->view('common/head.php');
            $this->load->view('common/sidebar.php', $data);
            $this->load->view('addproduct.php', $data);
            $this->load->view('common/footer.php');
        } else {
            redirect('');
        }
    }
    
    //function for custom error message
    
    public function customAlpha($str)
    {
        if (preg_match('/^[a-z .,\-)\(]+$/i', $str)) {
            return true;
        } else {
            
            $this->form_validation->set_message('customAlpha', '%s is not valid.');
            return false;
        }
    }
    
    
    /*add breed
    created by jitendra */
    
    function add_breed()
    {
        if (isset($_SESSION['name'])) {
            
            /*concate values of inputs*/
            $life_span_1 = $this->input->post('life_span_1');
            $life_span_2 = $this->input->post('life_span_2');
            $lifespan    = $life_span_1 . "–" . $life_span_2 . " Years";
            
            $min_weight_male = $this->input->post('min_weight_male');
            $max_weight_male = $this->input->post('max_weight_male');
            $weightmale      = $min_weight_male . "–" . $max_weight_male . " KG";
            
            $min_weight_female = $this->input->post('min_weight_female');
            $max_weight_female = $this->input->post('max_weight_female');
            $weightfemale      = $min_weight_female . "–" . $max_weight_female . " KG";
            
            $min_height_male = $this->input->post('min_height_male');
            $max_height_male = $this->input->post('max_height_male');
            $heightmale      = $min_height_male . "–" . $max_height_male . " CM";
            
            $min_height_female = $this->input->post('min_height_female');
            $max_height_female = $this->input->post('max_height_female');
            $heightfemale      = $min_height_female . "–" . $max_height_female . " CM";
            
            
            $this->form_validation->set_rules('breed_name', 'Breed Name', 'required|callback_customAlpha');
            $this->form_validation->set_rules('origin', 'Origin', 'required|callback_customAlpha');
            $this->form_validation->set_rules('temperament', 'Temperament', 'required|callback_customAlpha');
            $this->form_validation->set_rules('life_span', 'Life Span', '');
            $this->form_validation->set_rules('weight_male', 'Weight Male', '');
            $this->form_validation->set_rules('weight_female', 'Weight Female', '');
            $this->form_validation->set_rules('height_male', 'Height Male', '');
            $this->form_validation->set_rules('height_female', 'Height Female', '');
            $this->form_validation->set_rules('target', 'Target', 'required');
            $this->form_validation->set_rules('manual_act', 'Manual Activity', 'required');
            $this->form_validation->set_rules('breed_cat', 'Breed Category', 'required|callback_customAlpha');
            $this->form_validation->set_rules('remark', 'Remark', 'required|callback_customAlpha');
            
            
            if ($this->form_validation->run() == FALSE) {
                
                $data['page'] = 'add_breed';
                $this->load->view('common/head.php');
                $this->load->view('common/sidebar.php', $data);
                $this->load->view('addbreed.php', $data);
                $this->load->view('common/footer.php');
            } else {
                
                /*get values of input box from addbreed form*/
                
                $data['breed_name']      = $this->input->post('breed_name');
                $data['origin']          = $this->input->post('origin');
                $data['temperament']     = $this->input->post('temperament');
                $data['life_span']       = $lifespan;
                $data['weight_male']     = $weightmale;
                $data['weight_female']   = $weightfemale;
                $data['height_male']     = $heightmale;
                $data['height_female']   = $heightfemale;
                $data['target']          = $this->input->post('target');
                $data['manual_activity'] = $this->input->post('manual_act');
                $data['breed_cat']       = $this->input->post('breed_cat');
                $data['remark']          = $this->input->post('remark');
                $data['image_path']      = $this->input->post('image_path');
                $data['pet_type']        = $this->input->post('pet_type');
                $data['active_status']   = "True";
                
                
                $config['upload_path']   = './assets/images/breeds/';
                $config['allowed_types'] = 'gif|jpg|jpeg|png';
                $config['max_size']      = 10000;
                $config['file_name']     = time();
                $this->load->library('upload', $config);
                $this->upload->initialize($config);
                $productsimage = "";
                
                if ($this->upload->do_upload('image_path')) {
                    
                    $productsimage = 'assets/images/breeds/' . $this->upload->data('file_name');
                    
                }
                
                else {
                    
                }
                
                $data['image_path'] = $productsimage;
                $this->Api_model->insert('breed', $data);
                redirect('Admin/breedList');
            }
            
        }
        
        else {
            redirect('Admin/addbreed');
        }
        
    }
    
    function update_breed_action()
    {
        if (isset($_SESSION['name'])) {
            $id          = $this->input->post('id');
            /*concate values of inputs*/
            $life_span_1 = $this->input->post('life_span_1');
            $life_span_2 = $this->input->post('life_span_2');
            $lifespan    = $life_span_1 . "–" . $life_span_2 . " Years";
            
            $min_weight_male = $this->input->post('min_weight_male');
            $max_weight_male = $this->input->post('max_weight_male');
            $weightmale      = $min_weight_male . "–" . $max_weight_male . " KG";
            
            $min_weight_female = $this->input->post('min_weight_female');
            $max_weight_female = $this->input->post('max_weight_female');
            $weightfemale      = $min_weight_female . "–" . $max_weight_female . " KG";
            
            $min_height_male = $this->input->post('min_height_male');
            $max_height_male = $this->input->post('max_height_male');
            $heightmale      = $min_height_male . "–" . $max_height_male . " CM";
            
            $min_height_female = $this->input->post('min_height_female');
            $max_height_female = $this->input->post('max_height_female');
            $heightfemale      = $min_height_female . "–" . $max_height_female . " CM";
            
            
            $this->form_validation->set_rules('breed_name', 'Breed Name', 'required|callback_customAlpha');
            $this->form_validation->set_rules('origin', 'Origin', 'required|callback_customAlpha');
            $this->form_validation->set_rules('temperament', 'Temperament', 'required|callback_customAlpha');
            $this->form_validation->set_rules('life_span', 'Life Span', '');
            $this->form_validation->set_rules('weight_male', 'Weight Male', '');
            $this->form_validation->set_rules('weight_female', 'Weight Female', '');
            $this->form_validation->set_rules('height_male', 'Height Male', '');
            $this->form_validation->set_rules('height_female', 'Height Female', '');
            $this->form_validation->set_rules('target', 'Target', 'required');
            $this->form_validation->set_rules('manual_act', 'Manual Activity', 'required');
            $this->form_validation->set_rules('breed_cat', 'Breed Category', 'required|callback_customAlpha');
            $this->form_validation->set_rules('remark', 'Remark', 'required|callback_customAlpha');
            
            
            if ($this->form_validation->run() == FALSE) {
                
                $data['page'] = 'addbreed';
                $this->load->view('common/head.php');
                $this->load->view('common/sidebar.php', $data);
                $this->load->view('addbreed.php', $data);
                $this->load->view('common/footer.php');
            } else {
                
                
                /*get values of input box from addbreed form*/
                
                $data['breed_name']      = $this->input->post('breed_name');
                $data['origin']          = $this->input->post('origin');
                $data['temperament']     = $this->input->post('temperament');
                $data['life_span']       = $lifespan;
                $data['weight_male']     = $weightmale;
                $data['weight_female']   = $weightfemale;
                $data['height_male']     = $heightmale;
                $data['height_female']   = $heightfemale;
                $data['target']          = $this->input->post('target');
                $data['manual_activity'] = $this->input->post('manual_act');
                $data['breed_cat']       = $this->input->post('breed_cat');
                $data['remark']          = $this->input->post('remark');
                // $data['image_path']      =   $this->input->post('image_path');
                $data['pet_type']        = $this->input->post('pet_type');
                $data['active_status']   = "True";
                
                if ($_FILES['image_path']['name'] != "") {
                    
                    $config['upload_path']   = './assets/images/breeds/';
                    $config['allowed_types'] = 'gif|jpg|jpeg|png';
                    $config['max_size']      = 10000;
                    $config['file_name']     = time();
                    $this->load->library('upload', $config);
                    $this->upload->initialize($config);
                    $productsimage = "";
                    
                    if ($this->upload->do_upload('image_path')) {
                        
                        $productsimage = 'assets/images/breeds/' . $this->upload->data('file_name');
                        
                    }
                    
                    else {
                        
                    }
                    
                    $data['image_path'] = $productsimage;
                }
                $where = array(
                    'id' => $id
                );
                $this->Api_model->updateSingleRow('breed', $where, $data);
                
                redirect('Admin/breedList');
            }
            
        }
        
        else {
            redirect('Admin/update_breed');
        }
        
    }

function add_offer_action()
    {
     
            if (isset($_SESSION['name'])) {
            
            if ($this->form_validation->run() == TRUE) {
                $data['page'] = 'Add Banner';
                $this->load->view('common/head.php');
                $this->load->view('common/sidebar.php', $data);
                $this->load->view('banner.php', $data);
                $this->load->view('common/footer.php');
            } else {
            $datas['p_name']        = $this->input->post('p_name');
            $datas['c_id']         =   $this->input->post('c_id');
            // $data['c_id']          = $this->input->post('brand');
            $datas['p_type']        = $this->input->post('p_type');
            $datas['p_pet_type']    = $this->input->post('p_pet_type');
            $datas['discount']      = $this->input->post('discount');
            // $data['img_path']       =   $this->input->post('img_path');
            $datas['start_date']      = $this->input->post('s_date');
            $datas['end_date']      = $this->input->post('end_date');
              $config['upload_path']   = './assets/images/category/';
                $config['allowed_types'] = 'gif|jpg|jpeg|png';
                $config['max_size']      = 10000;
                $config['file_name']     = time();
                $this->load->library('upload', $config);
                $this->upload->initialize($config);
                $productsimage = "";
                if ($this->upload->do_upload('img_path')) {
                    $productsimage      = 'assets/images/category/' . $this->upload->data('file_name');
                    $datas['image'] = $productsimage;
                } else {
                   
                    echo "image is not inserted";
                }
              

                $this->Api_model->insert('offer', $datas);
                redirect('Admin/viewoffer');
             } 
            
        }
        redirect();
    }
    
function contact_add()
    {
     
            if (isset($_SESSION['name'])) {
            
            if ($this->form_validation->run() == TRUE) {
                $data['page'] = 'Add Contact';
                $this->load->view('common/head.php');
                $this->load->view('common/sidebar.php', $data);
                $this->load->view('banner.php', $data);
                $this->load->view('common/footer.php');
            } else {
            $datas['name']        = $this->input->post('name');
            $datas['mobile_no']        = $this->input->post('mobile_no');
            $datas['email']       =$this->input->post('email');
           

                $this->Api_model->insert('contact_info', $datas);
                redirect('Admin/contact_information');
             } 
            
        }
        redirect();
    }


    function breed_add()
    {
     
            if (isset($_SESSION['name'])) {
            
            if ($this->form_validation->run() == TRUE) {
                $data['page'] = 'Add Breed Information';
                $this->load->view('common/head.php');
                $this->load->view('common/sidebar.php', $data);
                $this->load->view('banner.php', $data);
                $this->load->view('common/footer.php');
            } else {
            $datas['breed_name']        = $this->input->post('breed_name');
            $datas['b_description']        = $this->input->post('b_description');
            $config['upload_path']   = './assets/images/category/';
                $config['allowed_types'] = 'gif|jpg|jpeg|png';
                $config['max_size']      = 10000;
                $config['file_name']     = time();
                $this->load->library('upload', $config);
                $this->upload->initialize($config);
                $productsimage = "";
                if ($this->upload->do_upload('img_path')) {
                    $productsimage      = 'assets/images/category/' . $this->upload->data('file_name');
                    $datas['image'] = $productsimage;
                } else {
                   
                    echo "image is not inserted";
                }
              

                $this->Api_model->insert('breed_info', $datas);
                redirect('Admin/viewbreed_information');
             } 
            
        }
        redirect();
    }

    
    
    function add_product()
    {
        if (isset($_SESSION['name'])) {
            
            
            // $this->form_validation->set_rules('randomfield', 'Random Field', 'required');
            // $this->form_validation->set_rules('p_name', 'Product Name', 'required');//callback_customAlpha
            // $this->form_validation->set_rules('p_description', 'Product Description', 'required');
            // $this->form_validation->set_rules('c_name', 'Company Name', 'required');
            // $this->form_validation->set_rules('p_type', 'Product Type', 'required');
            // $this->form_validation->set_rules('p_pet_type', 'Pet Type', 'required');
            // $this->form_validation->set_rules('p_cat_type', 'Product Category', 'required');
            // $this->form_validation->set_rules('p_rate', 'Product Rate', 'required');
            // $this->form_validation->set_rules('selling_price', 'Selling Price', 'required');
            // $this->form_validation->set_rules('quantity', 'Quantity', 'required');
            // $this->form_validation->set_rules('discount', 'Discount', 'required');
            // $this->form_validation->set_rules('shipping_cost', 'Shipping Cost', 'required');
            // $this->form_validation->set_rules('country', 'Country', 'required');
            //  $this->form_validation->set_rules('color', 'Color', 'required');
            // $this->form_validation->set_rules('size', 'Size', 'required');
            // $this->form_validation->set_rules('weight', 'Weight', 'required');
            
            
            //  if ($this->form_validation->run() == FALSE)
            //  {
            //      redirect('Admin/addproduct');
            //  }
            //  else
            //  {
            if (isset($_POST['color'])) {
                $color = implode(',', $this->input->post('color'));
            } else {
                $color = '';
            }
            if (isset($_POST['size'])) {
                $size = implode(',', $this->input->post('size'));
            } else {
                $size = '';
            }
            
            
            $data['p_code']        = $this->input->post('randomfield');
            $data['p_name']        = $this->input->post('p_name');
            $data['p_description'] = $this->input->post('p_description');
            // $data['c_name']         =   $this->input->post('c_name');
            $data['country']       = $this->input->post('country');
            $data['c_id']          = $this->input->post('brand');
            $data['p_type']        = $this->input->post('p_type');
            $data['p_pet_type']    = $this->input->post('p_pet_type');
            $data['p_cat_type']    = $this->input->post('p_cat_type');
            $data['p_rate']        = $this->input->post('p_rate');
            $data['p_rate_sale']   = $this->input->post('selling_price');
            $data['quantity']      = $this->input->post('quantity');
            $data['gross_amt']     = $this->input->post('gross_amt');
            $data['discount']      = $this->input->post('discount');
            $data['net_amt']       = $this->input->post('net_amt');
            $data['color']         = $color;
            $data['size']          = $size;
            $data['weight']        = $this->input->post('weight');
            $mandatory             = $this->input->post('mandatory');
            if ($mandatory) {
                $data['mandatory'] = $mandatory;
            }
            
            $data['shipping_cost']  = $this->input->post('shipping_cost');
            //$data['img_path']       =   $this->input->post('img_path');
            $data['status']         = 1;
            $data['product_rating'] = 1;
            $data['created_at']     = time();
            
            $proId = $this->Api_model->insertGetId('food_product', $data);
            if ($proId) {
                
                if (isset($_FILES['files'])) {
                    $filesCount = count($_FILES['files']['name']);
                    for ($i = 0; $i < $filesCount; $i++) {
                        $_FILES['file']['name']     = $_FILES['files']['name'][$i];
                        $_FILES['file']['type']     = $_FILES['files']['type'][$i];
                        $_FILES['file']['tmp_name'] = $_FILES['files']['tmp_name'][$i];
                        $_FILES['file']['error']    = $_FILES['files']['error'][$i];
                        $_FILES['file']['size']     = $_FILES['files']['size'][$i];
                        // File upload configuration
                        
                        $uploadPath              = './assets/images/products';
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
                            $uploadData[$i] = 'assets/images/products/' . $fileData['file_name'];
                        }
                        // else
                        // {
                        //    $a = $this->upload->display_errors();
                        //    $this->Api_model->responseFailed(0, $a);
                        //    exit();
                        // }
                    }
                    foreach ($uploadData as $uploadData) {
                        $datas['p_id']       = $proId;
                        $datas['image']      = $uploadData;
                        $datas['created_at'] = time();
                        $listID              = $this->Api_model->insertGetId("pro_images", $datas);
                    }
                }
                
            }
            redirect('Admin/productlist');
            // }
            
        } else {
            redirect('Admin/addproduct');
        }
    }
    
    
    function update_product_action()
    {
        if (isset($_SESSION['name'])) {
            // $this->form_validation->set_rules('p_name', 'Product Name', 'required|callback_customAlpha');
            // $this->form_validation->set_rules('p_description', 'Product Description', 'required|callback_customAlpha');
            // $this->form_validation->set_rules('c_name', 'Company Name', 'required');
            // $this->form_validation->set_rules('p_type', 'Product Type', 'required');
            // $this->form_validation->set_rules('p_pet_type', 'Pet Type', 'required');
            // $this->form_validation->set_rules('p_cat_type', 'Product Category', 'required');
            // $this->form_validation->set_rules('p_rate', 'Product Rate', 'required');
            // $this->form_validation->set_rules('selling_price', 'Selling Price', 'required');
            // $this->form_validation->set_rules('quantity', 'Quantity', 'required');
            // $this->form_validation->set_rules('discount', 'Discount', 'required');
            // $this->form_validation->set_rules('shipping_cost', 'Shipping cost', 'required');
            
            // if ($this->form_validation->run() == FALSE)
            // {
            // $data['product']=$this->Api_model->getSingleRow('food_product',array('p_id'=>$this->input->post('p_id')));
            // $data['company_food']=$this->Api_model->get_all_food();
            // $data['pet_type']=$this->Api_model->get_all_pet_type();
            // $data['pro_cat']=$this->Api_model->getAllDataWhere(array('status'=>1),'pro_cat');
            // $data['page']='update_product';
            // $this -> load -> view('common/head.php');
            // $this -> load -> view('common/sidebar.php',$data);
            // $this -> load -> view('update_product.php', $data);
            // $this -> load -> view('common/footer.php');
            // } 
            // else
            // {
            $product_id = $this->input->post('p_id');
            if (isset($_POST['color'])) {
                $color = implode(',', $this->input->post('color'));
            } else {
                $color = '';
            }
            if (isset($_POST['size'])) {
                $size = implode(',', $this->input->post('size'));
            } else {
                $size = '';
            }
            // if(isset($_POST['weight']))
            // {
            //   $weight = implode(',',$this->input->post('weight'));
            // }
            // else
            // {
            //   $weight = '';
            // }
            
            
            $product_id             = $this->input->post('p_id');
            $data['p_name']         = $this->input->post('p_name');
            $data['mandatory']      = $this->input->post('mandatory');
            $data['shipping_cost']  = $this->input->post('shipping_cost');
            $data['country']        = $this->input->post('country');
            $data['p_description']  = $this->input->post('p_description');
            $data['c_id']           = $this->input->post('c_id');
            $data['p_type']         = $this->input->post('p_type');
            $data['p_pet_type']     = $this->input->post('p_pet_type');
            $data['p_cat_type']     = $this->input->post('p_cat_type');
            $data['p_rate']         = $this->input->post('p_rate');
            $data['p_rate_sale']    = $this->input->post('selling_price');
            $data['quantity']       = $this->input->post('quantity');
            $data['gross_amt']      = $this->input->post('gross_amt');
            $data['discount']       = $this->input->post('discount');
            $data['net_amt']        = $this->input->post('net_amt');
            // $data['img_path']       =   $this->input->post('img_path');
            $data['status']         = 1;
            $data['product_rating'] = 1;
            $data['color']          = $color;
            $data['size']           = $size;
            $data['weight']         = $this->input->post('weight');
            $data['updated_at'] = time();
            $where = array(
                'p_id' => $product_id
            );
            $this->Api_model->updateSingleRow('food_product', $where, $data);
            if (isset($_FILES['files'])) {
                $filesCount = count($_FILES['files']['name']);
                for ($i = 0; $i < $filesCount; $i++) {
                    $_FILES['file']['name']     = $_FILES['files']['name'][$i];
                    $_FILES['file']['type']     = $_FILES['files']['type'][$i];
                    $_FILES['file']['tmp_name'] = $_FILES['files']['tmp_name'][$i];
                    $_FILES['file']['error']    = $_FILES['files']['error'][$i];
                    $_FILES['file']['size']     = $_FILES['files']['size'][$i];
                    // File upload configuration
                    
                    $uploadPath              = './assets/images/products';
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
                        $uploadData[$i] = 'assets/images/products/' . $fileData['file_name'];
                    }
                    
                }
                foreach ($uploadData as $uploadData) {
                    $datas['p_id']       = $product_id;
                    $datas['image']      = $uploadData;
                    $datas['created_at'] = time();
                    $this->Api_model->deleteRecord($where, 'pro_images');
                    $listID = $this->Api_model->insertGetId("pro_images", $datas);
                }
            }
            redirect('Admin/productlist');
            ///}
            
        } else {
            redirect('');
        }
    }
    
    /*
    product list
    */
    public function productlist()
    {
        if (isset($_SESSION['name'])) {
            //$get_product=$this->Api_model->getAllDataWhere(array('deleted'=>1),'food_product');
            $get_product= $this->Api_model->getDataByFourTable12();
            // print_r($get_product);
            // exit();
            $data['product_list'] = $get_product;
            $data['page']         = 'productlist';
            $this->load->view('common/head.php');
            $this->load->view('common/sidebar.php', $data);
            $this->load->view('productlist.php', $data);
            $this->load->view('common/footer.php');
        } else {
            redirect('');
        }
    }
    
    public function breedList()
    {
        if (isset($_SESSION['name'])) {
            $get_product        = $this->Api_model->getAllData('breed');
            $data['breed_list'] = $get_product;
            $data['page']       = 'breedList';
            $this->load->view('common/head.php');
            $this->load->view('common/sidebar.php', $data);
            $this->load->view('breedList.php', $data);
            $this->load->view('common/footer.php');
        } else {
            redirect('');
        }
    }
    // public function addressfill()
    //    {
    //        if(isset($_SESSION['name'])) 
    //        { $user_id= $_GET['user_id'];
    //            $address= $this->Api_model->getAllDataWhere(array('user_id'=>$user_id),'addressfill');
    
    //            $data['coupon']= $address;
    //            $data['page']='coupon';
    //            $this -> load -> view('common/head.php');
    //            $this -> load -> view('common/sidebar.php', $data);
    //            $this -> load ->view('coupon.php', $data);
    //            $this -> load -> view('common/footer.php');
    //        }
    //        else
    //        {
    //            redirect('');
    //        }
    //      }
    
    
    
    public function Veterinary()
    {
        if (isset($_SESSION['name'])) {
            $id    = 1;
            $where = array(
                'nearby_id' => $id
            );
            $vsss  = $this->Api_model->getAllDataWhere($where, 'nearby_vss');
            
            $data['vsss'] = $vsss;
            // $data['page']='addVss';
            $this->load->view('common/head.php');
            $this->load->view('common/sidebar.php');
            $this->load->view('veterinary.php', $data);
            $this->load->view('common/footer.php');
        } else {
            redirect('');
        }
    }
    public function Salon()
    {
        if (isset($_SESSION['name'])) {
            $id    = 2;
            $where = array(
                'nearby_id' => $id
            );
            $vsss  = $this->Api_model->getAllDataWhere($where, 'nearby_vss');
            
            $data['vsss'] = $vsss; // //$data['breed_list']= $get_product;
            // $data['page']='addVss';
            $this->load->view('common/head.php');
            $this->load->view('common/sidebar.php');
            $this->load->view('Salon.php', $data);
            $this->load->view('common/footer.php');
        } else {
            redirect('');
        }
    }
    public function Shop()
    {
        if (isset($_SESSION['name'])) {
            $id    = 3;
            $where = array(
                'nearby_id' => $id
            );
            $vsss  = $this->Api_model->getAllDataWhere($where, 'nearby_vss');
            
            $data['vsss'] = $vsss;
            $this->load->view('common/head.php');
            $this->load->view('common/sidebar.php');
            $this->load->view('Shop.php', $data);
            $this->load->view('common/footer.php');
        } else {
            redirect('');
        }
    }
    
    public function Hostels()
    {
        if (isset($_SESSION['name'])) {
            $id    = 4;
            $where = array(
                'nearby_id' => $id
            );
            $vsss  = $this->Api_model->getAllDataWhere($where, 'nearby_vss');
            
            $data['vsss'] = $vsss; // //$data['breed_list']= $get_product;
            // $data['page']='addVss';
            $this->load->view('common/head.php');
            $this->load->view('common/sidebar.php');
            $this->load->view('Hostels.php', $data);
            $this->load->view('common/footer.php');
        } else {
            redirect('');
        }
    }
    
    public function Trainer()
    {
        if (isset($_SESSION['name'])) {
            $id    = 5;
            $where = array(
                'nearby_id' => $id
            );
            $vsss  = $this->Api_model->getAllDataWhere($where, 'nearby_vss');
            
            $data['vsss'] = $vsss; // //$data['breed_list']= $get_product;
            // $data['page']='addVss';
            $this->load->view('common/head.php');
            $this->load->view('common/sidebar.php');
            $this->load->view('Trainer.php', $data);
            $this->load->view('common/footer.php');
        } else {
            redirect('');
        }
    }
    
    /*
     **update product 
     
     */
    
    public function updateorder()
    {
        if (isset($_SESSION['name'])) {
            $order_id  = $this->input->post('order_id');
            $status    = $this->input->post('status');
            $date      = $this->input->post('order_date');
            $place_date      = $this->input->post('place_date');

            $where     = array(
                'order_id' => $order_id
            );
            $orderData = array(
                'status' => $status
            );
            $this->Api_model->updateSingleRow('orders', $where, $orderData);
            
            if ($status == 1) {
                $order_status_data = array(
                    'od_confirm_status' => $status,
                    'od_confirm_date' => $date,
                    'od_status' => $status
                );
                $this->Api_model->updateSingleRow('order_status', $where, $order_status_data);
                $this->Api_model->updateSingleRow('orders', $where, array(
                    'place_date' => $place_date));

                $this->Api_model->updateSingleRow('product_invoice', $where, array(
                    'status' => 1
                ));
                $getdata      = $this->Api_model->getSingleRow('orders', $where);
                $user_id      = $getdata->user_id;
                $order_id     = $getdata->order_id;
                $title        = 'Order';
                $getToken     = $this->Api_model->getSingleRow('users', array(
                    'id' => $user_id
                ));
                $device_token = $getToken->device_tokan;
                $this->firebase_with_class($device_token, '', '', $title, CORDER . $getdata->order_id . '.');
                $username     = $getToken->first_name;
                $email        = $getdata->email;
                $msg          = CORDER . $getdata->order_id;
                $notification = array(
                    'user_id' => $user_id,
                    'title' => $title,
                    'type' => $title,
                    'ids' => $getdata->order_id,
                    'msg' => $msg,
                    'created_at' => time()
                );
                $this->Api_model->insertGetId(NOTOFICATION_TBL, $notification);
                $this->Api_model->send_email($email, ORDER, $msg);
            }
            if ($status == 2) {
                $order_status_data = array(
                    'od_delivered_status' => $status,
                    'od_delivered_date' => $date,
                    'od_status' => $status
                );
                $this->Api_model->updateSingleRow('order_status', $where, $order_status_data);
                $getdata  = $this->Api_model->getSingleRow('orders', $where);
                $user_id  = $getdata->user_id;
                $order_id = $getdata->order_id;
                $title    = 'Order';
                
                $getToken     = $this->Api_model->getSingleRow('users', array(
                    'id' => $user_id
                ));
                $device_token = $getToken->device_tokan;
                $this->firebase_with_class($device_token, '', '', $title, DELIVERED . $getdata->order_id . '.');
                $username     = $getToken->first_name;
                $email        = $getdata->email;
                $msg          = DELIVERED . $getdata->order_id;
                $notification = array(
                    'user_id' => $user_id,
                    'title' => $title,
                    'type' => $title,
                    'ids' => $getdata->order_id,
                    'msg' => $msg,
                    'created_at' => time()
                );
                $this->Api_model->insertGetId(NOTOFICATION_TBL, $notification);
                $this->Api_model->send_email($email, ORDER, $msg);
            }
            if ($status == 3) {
                $order_status_data = array(
                    'od_packed_status' => $status,
                    'od_packed_date' => $date,
                    'od_status' => $status
                );
                $this->Api_model->updateSingleRow('order_status', $where, $order_status_data);
                
                $getdata  = $this->Api_model->getSingleRow('orders', $where);
                $user_id  = $getdata->user_id;
                $order_id = $getdata->order_id;
                $title    = 'Order';
                
                $getToken     = $this->Api_model->getSingleRow('users', array(
                    'id' => $user_id
                ));
                $device_token = $getToken->device_tokan;
                $this->firebase_with_class($device_token, '', '', $title, PCORDER . $getdata->order_id . '.');
                $username     = $getToken->first_name;
                $email        = $getdata->email;
                $msg          = PCORDER . $getdata->order_id;
                $notification = array(
                    'user_id' => $user_id,
                    'title' => $title,
                    'type' => $title,
                    'ids' => $getdata->order_id,
                    'msg' => $msg,
                    'created_at' => time()
                );
                $this->Api_model->insertGetId(NOTOFICATION_TBL, $notification);
                $this->Api_model->send_email($email, ORDER, $msg);
            }
            if ($status == 4) {
                $order_status_data = array(
                    'od_dispatched_status' => $status,
                    'od_dispatched_date' => $date,
                    'od_status' => $status
                );
                $this->Api_model->updateSingleRow('order_status', $where, $order_status_data);
                
                $getdata  = $this->Api_model->getSingleRow('orders', $where);
                $user_id  = $getdata->user_id;
                $order_id = $getdata->order_id;
                $title    = 'Order';
                
                $getToken     = $this->Api_model->getSingleRow('users', array(
                    'id' => $user_id
                ));
                $device_token = $getToken->device_tokan;
                $this->firebase_with_class($device_token, '', '', $title, DISORDER . $getdata->order_id . '.');
                $username     = $getToken->first_name;
                $email        = $getdata->email;
                $msg          = DISORDER . $getdata->order_id;
                $notification = array(
                    'user_id' => $user_id,
                    'title' => $title,
                    'type' => $title,
                    'ids' => $getdata->order_id,
                    'msg' => $msg,
                    'created_at' => time()
                );
                $this->Api_model->insertGetId(NOTOFICATION_TBL, $notification);
                $this->Api_model->send_email($email, ORDER, $msg);
            }
            if ($status == 5) {
                $order_status_data = array(
                    'od_canceled_status' => $status,
                    'od_canceled_date' => $date,
                    'od_status' => $status
                );
                $this->Api_model->updateSingleRow('order_status', $where, $order_status_data);
                
                $getdata  = $this->Api_model->getSingleRow('orders', $where);
                $user_id  = $getdata->user_id;
                $order_id = $getdata->order_id;
                $title    = 'Order';
                
                $getToken     = $this->Api_model->getSingleRow('users', array(
                    'id' => $user_id
                ));
                $device_token = $getToken->device_tokan;
                $this->firebase_with_class($device_token, '', '', $title, CANCEL . $getdata->order_id . '.');
                $username     = $getToken->first_name;
                $email        = $getdata->email;
                $msg          = CANCEL . $getdata->order_id;
                $notification = array(
                    'user_id' => $user_id,
                    'title' => $title,
                    'type' => $title,
                    'ids' => $getdata->order_id,
                    'msg' => $msg,
                    'created_at' => time()
                );
                $this->Api_model->insertGetId(NOTOFICATION_TBL, $notification);
                $this->Api_model->send_email($email, ORDER, $msg);
            }
                
            redirect('Admin/order');
        } 
        else {
            redirect('');
        }
    }
    
    public function Cancelorder()
    {
        if (isset($_SESSION['name'])) {
            $order_id = $this->uri->segment(3);
            $where    = array(
                'order_id' => $order_id
            );
            $data     = array(
                'status' => '1'
            );
            $this->Api_model->updateSingleRow('orders', $where, $data);
            
            $getdata      = $this->Api_model->getSingleRow('orders', $where);
            $user_id      = $getdata->user_id;
            $order_id     = $getdata->order_id;
            $title        = 'Order';
            //$this->firebase_notification($user_id,$title,CANCEL.$getdata->order_id.'.');
            $getToken     = $this->Api_model->getSingleRow('users', array(
                'id' => $user_id
            ));
            $device_token = $getToken->device_tokan;
            $this->firebase_with_class($device_token, '', '', $title, CANCEL . $getdata->order_id . '.');
            $getuser      = $this->Api_model->getSingleRow('users', array(
                'id' => $user_id
            ));
            $username     = $getuser->first_name;
            $email        = $getdata->email;
            $msg          = CANCEL . $getdata->order_id;
            $notification = array(
                'user_id' => $user_id,
                'title' => $title,
                'type' => $title,
                'ids' => $getdata->order_id,
                'msg' => $msg,
                'created_at' => time()
            );
            $this->Api_model->insertGetId(NOTOFICATION_TBL, $notification);
            $this->Api_model->send_email($email, ORDER, $msg);
            redirect('Admin/order');
        } else {
            redirect('');
        }
    }
    
    public function updateproduct()
    {
        if (isset($_SESSION['name'])) {
            $product_id             = $this->uri->segment(3);
            $where                  = array(
                'p_id' => $product_id
            );
            $data['product']        = $this->Api_model->getSingleRow('food_product', $where);
            $data['company_food']   = $this->Api_model->get_all_food();
            $data['pet_type']       = $this->Api_model->get_all_pet_type();
            $data['pro_Cat']        = $this->Api_model->getAllDataWhere(array(
                'status' => 1
            ), 'pro_cat');
            $data['attribute']      = $this->Api_model->getAllDataWhere(array(
                'status' => 1
            ), 'attribute');
            $data['pro_attribute']  = $this->Api_model->getAllDataWhere(array(
                'p_id' => $product_id
            ), 'pro_attribute');
            $data['attributeSize']  = $this->Api_model->getAllDataWhere(array(
                'status' => 1
            ), 'pro_size');
            $data['attributeColor'] = $this->Api_model->getAllDataWhere(array(
                'status' => 1
            ), 'pro_color');
            
            $data['country'] = $this->Api_model->getAllData('country');
            $data['page']    = 'addproduct';
            
            $this->load->view('common/head.php');
            $this->load->view('common/sidebar.php', $data);
            $this->load->view('update_product.php', $data);
            $this->load->view('common/footer.php');
        } else {
            redirect('');
        }
    }
    
    public function update_breed()
    {
        if (isset($_SESSION['name'])) {
            $breed_id         = $this->uri->segment(3);
            $where            = array(
                'id' => $breed_id
            );
            $data['pet_type'] = $this->Api_model->get_all_pet_type();
            $data['breed']    = $this->Api_model->getSingleRow('breed', $where);
            
            $life_span             = $data['breed']->life_span;
            $life_arr              = explode('–', $life_span);
            $data['life_span_min'] = $life_arr[0];
            $life_arr              = explode(' ', $life_arr[1]);
            $data['life_span_max'] = $life_arr[0];
            
            $height_male             = $data['breed']->height_male;
            $height_arr              = explode('–', $height_male);
            $data['height_male_min'] = $height_arr[0];
            $height_arr              = explode(' ', $height_arr[1]);
            $data['height_male_max'] = $height_arr[0];
            
            $height_female             = $data['breed']->height_female;
            $height_arr                = explode('–', $height_female);
            $data['height_female_min'] = $height_arr[0];
            $height_arr                = explode(' ', $height_arr[1]);
            $data['height_female_max'] = $height_arr[0];
            
            $weight_male             = $data['breed']->weight_male;
            $weight_arr              = explode('–', $weight_male);
            $data['weight_male_min'] = $weight_arr[0];
            $weight_arr              = explode(' ', $weight_arr[1]);
            $data['weight_male_max'] = $weight_arr[0];
            
            $weight_female             = $data['breed']->weight_female;
            $weight_arr                = explode('–', $weight_female);
            $data['weight_female_min'] = $weight_arr[0];
            $weight_arr                = explode(' ', $weight_arr[1]);
            $data['weight_female_max'] = $weight_arr[0];
            
            $data['page'] = 'addbreed';
            $this->load->view('common/head.php');
            $this->load->view('common/sidebar.php', $data);
            $this->load->view('update_breed.php', $data);
            $this->load->view('common/footer.php');
        } else {
            redirect('');
        }
    }
    
    /*
    add product  end 
   */
    
    /*All User*/
    public function artistDetails()
    {
        if (isset($_SESSION['name'])) {
            $user_id     = $_GET['id'];
            $role        = $_GET['role'];
            $artist_name = $_GET['artist_name'];
            
            $artist = $this->Api_model->getSingleRow('artist', array(
                'user_id' => $user_id
            ));
            
            $jobDone = $this->Api_model->getTotalWhere('artist_booking', array(
                'artist_id' => $user_id,
                'booking_flag' => 4
            ));
            
            $data['total'] = $this->Api_model->getTotalWhere('artist_booking', array(
                'artist_id' => $user_id
            ));
            if ($data['total'] == 0) {
                $data['percentages'] = 0;
            } else {
                $data['percentages'] = ($jobDone * 100) / $data['total'];
            }
            
            
            $data['jobDone'] = $jobDone;
            
            if ($role == 1) {
                $where = array(
                    'artist_id' => $user_id
                );
                
                $get_appointment = $this->Api_model->getAllDataWhere($where, 'artist_booking');
                
                $get_appointments = array();
                foreach ($get_appointment as $get_appointment) {
                    $get_user = $this->Api_model->getSingleRow('user', array(
                        'user_id' => $get_appointment->user_id
                    ));
                    
                    $get_appointment->name    = $get_user->name;
                    $get_appointment->image   = base_url() . $get_user->image;
                    $get_appointment->address = $get_user->address;
                    
                    array_push($get_appointments, $get_appointment);
                }
                
            } elseif ($role == 2) {
                $where = array(
                    'user_id' => $user_id
                );
                
                $get_appointment = $this->Api_model->getAllDataWhere($where, 'artist_booking');
                
                $get_appointments = array();
                foreach ($get_appointment as $get_appointment) {
                    $get_user        = $this->Api_model->getSingleRow('artist', array(
                        'user_id' => $get_appointment->artist_id
                    ));
                    $get_user->image = base_url() . $get_user->image;
                    
                    $get_appointment->name    = $get_user->name;
                    $get_appointment->image   = base_url() . $get_user->image;
                    $get_appointment->address = $get_user->address;
                    
                    array_push($get_appointments, $get_appointment);
                }
            }
            
            $where = array(
                'user_id' => $user_id
            );
            
            $get_products = $this->Api_model->getAllDataWhere($where, 'products');
            
            $data['get_products'] = $get_products;
            
            $where = array(
                'artist_id' => $user_id
            );
            
            $getInvoice = $this->Api_model->getAllDataWhere($where, 'booking_invoice');
            
            $getInvoices = array();
            foreach ($getInvoice as $getInvoice) {
                $getBooking = $this->Api_model->getSingleRow('artist_booking', array(
                    'id' => $getInvoice->booking_id
                ));
                
                $getInvoice->booking_time = $getBooking->booking_time;
                $getInvoice->booking_date = $getBooking->booking_date;
                
                $getUser = $this->Api_model->getSingleRow('user', array(
                    'user_id' => $getInvoice->user_id
                ));
                
                $getInvoice->userName = $getUser->name;
                $getInvoice->address  = $getUser->address;
                
                if ($getUser->image) {
                    $getInvoice->userImage = base_url() . $getUser->image;
                } else {
                    $getInvoice->userImage = base_url() . 'assets/images/a.png';
                }
                
                $get_artists = $this->Api_model->getSingleRow('artist', array(
                    'user_id' => $getInvoice->artist_id
                ));
                
                $get_cat = $this->Api_model->getSingleRow('category', array(
                    'id' => $get_artists->category_id
                ));
                
                $getInvoice->ArtistName   = $get_artists->name;
                $getInvoice->categoryName = $get_cat->cat_name;
                
                if ($get_artists->image) {
                    $getInvoice->artistImage = base_url() . $get_artists->image;
                } else {
                    $getInvoice->artistImage = base_url() . 'assets/images/a.png';
                }
                
                array_push($getInvoices, $getInvoice);
            }
            
            
            $data['get_invoice'] = $getInvoices;
            
            
            $data['get_appointments'] = $get_appointments;
            $data['artist_name']      = $artist_name;
            
            $data['user'] = $artist;
            $data['page'] = 'artist';
            
            $this->load->view('common/head.php');
            $this->load->view('common/sidebar.php', $data);
            $this->load->view('artistDetails.php', $data);
            $this->load->view('common/footer.php');
        } else {
            redirect('');
        }
    }
    
    
    /*Add coupon*/
    public function add_coupon()
    {
        $data['coupon_code']   = $this->input->post('coupon_code', TRUE);
        $data['description']   = $this->input->post('description', TRUE);
        $data['discount_type'] = $this->input->post('discount_type', TRUE);
        $data['discount']      = $this->input->post('discount', TRUE);
        
        $data['created_at'] = time();
        $data['updated_at'] = time();
        
        $this->Api_model->insertGetId('discount_coupon', $data);
        
        redirect('Admin/coupon');
    }
    
    
    /*Change Status Artist*/
    public function change_status_coupon()
    {
        $id      = $_GET['id'];
        $status  = $_GET['status'];
        $request = $_GET['request'];
        $where   = array(
            'id' => $id
        );
        $data    = array(
            'status' => $status
        );
        
        $update = $this->Api_model->updateSingleRow('discount_coupon', $where, $data);
        
        redirect('Admin/coupon');
    }
    
    public function notification()
    {
        if (isset($_SESSION['name'])) {
            
            $data['user'] = $this->Api_model->getAllData('users');
            $data['page'] = 'notification';
            
            $this->load->view('common/head.php');
            $this->load->view('common/sidebar.php', $data);
            $this->load->view('notification.php', $data);
            $this->load->view('common/footer.php');
        } else {
            redirect();
        }
    }
    
    public function putnotification()
    {
        $id = explode(',', $_POST['uid']);

        $count = count($id);
        for ($i = 0; $i < $count; $i++) {
            $user_id = $id[$i];
            $title   = $_POST['title'];
            $msg     = $_POST['message'];
            if ($user_id != '') 
            {
                $getToken     = $this->Api_model->getSingleRow('users', array(
                    'id' => $user_id
                ));
                $device_token = $getToken->device_tokan;
                $this->firebase_with_class($device_token, '', '', $title, $msg);
                $getToken     = $this->Api_model->getSingleRow('users', array(
                    'id' => $user_id
                ));
                $device_token = $getToken->device_tokan;
                $this->firebase_with_class($device_token, '', '', $title, $msg);
                $data['user_id']    = $user_id;
                $data['title']      = $title;
                $data['msg']        = $msg;
                $data['created_at'] = time();
                $this->Api_model->insert('notifications', $data);
            }
        }
        
        redirect('Admin/notification');
    }
    
    /*Firebase for notification*/
    public function firebase_notification($user_id, $title, $msg)
    {
        
        $get_data = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));
          $get_key = $this->Api_model->getSingleRow('users', array(
            'id' => $user_id
        ));

        
        if ($get_data->device_tokan) {
            if ($get_data->role_id == 1) {
                $API_ACCESS_KEY = $api_key;
            } else {
                $API_ACCESS_KEY = $api_key;
                
            }
            
            $registrationIds = $get_data->device_tokan;
            
            $msg = array(
                'body' => $msg,
                'title' => $title,
                'icon' => 'myIcon',
                /*Default Icon*/
                'sound' => 'mySound'
                /*Default sound*/
            );
            
            $fields  = array(
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
    }
    
    /*All Appointment*/
    public function appointments()
    {
        $user_id     = $_GET['id'];
        $role        = $_GET['role'];
        $artist_name = $_GET['artist_name'];
        
        if ($role == 1) {
            $where = array(
                'artist_id' => $user_id
            );
            
            $get_appointment = $this->Api_model->getAllDataWhere($where, 'artist_booking');
            
            $get_appointments = array();
            foreach ($get_appointment as $get_appointment) {
                $get_user = $this->Api_model->getSingleRow('user', array(
                    'user_id' => $get_appointment->user_id
                ));
                
                $get_appointment->name    = $get_user->name;
                $get_appointment->image   = base_url() . $get_user->image;
                $get_appointment->address = $get_user->address;
                
                array_push($get_appointments, $get_appointment);
            }
            
        } elseif ($role == 2) {
            $where = array(
                'user_id' => $user_id
            );
            
            $get_appointment = $this->Api_model->getAllDataWhere($where, 'artist_booking');
            
            $get_appointments = array();
            foreach ($get_appointment as $get_appointment) {
                $get_user        = $this->Api_model->getSingleRow('artist', array(
                    'user_id' => $get_appointment->artist_id
                ));
                $get_user->image = base_url() . $get_user->image;
                
                $get_appointment->name    = $get_user->name;
                $get_appointment->image   = base_url() . $get_user->image;
                $get_appointment->address = $get_user->address;
                
                array_push($get_appointments, $get_appointment);
            }
        }
        
        $data['get_appointments'] = $get_appointments;
        $data['artist_name']      = $artist_name;
        $data['page']             = 'artist';
        $this->load->view('common/head.php');
        $this->load->view('common/sidebar.php', $data);
        $this->load->view('appointments.php', $data);
        $this->load->view('common/footer.php');
    }
    
    /*Get All Products*/
    public function products()
    {
        $user_id     = $_GET['id'];
        $role        = $_GET['role'];
        $artist_name = $_GET['artist_name'];
        
        $where = array(
            'user_id' => $user_id
        );
        
        $get_products = $this->Api_model->getAllDataWhere($where, 'products');
        
        $data['get_products'] = $get_products;
        $data['artist_name']  = $artist_name;
        $data['page']         = 'artist';
        $this->load->view('common/head.php');
        $this->load->view('common/sidebar.php', $data);
        $this->load->view('products.php', $data);
        $this->load->view('common/footer.php');
    }
    
    /*Get All Invoice*/
    public function invoice()
    {
        $user_id     = $_GET['id'];
        $artist_id   = $_GET['artist_id'];
        $artist_name = $_GET['artist_name'];
        
        $where = array(
            'artist_id' => $user_id
        );
        
        $getInvoice = $this->Api_model->getAllDataWhere($where, 'booking_invoice');
        
        $getInvoices = array();
        foreach ($getInvoice as $getInvoice) {
            $getBooking = $this->Api_model->getSingleRow('artist_booking', array(
                'id' => $getInvoice->booking_id
            ));
            
            $getInvoice->booking_time = $getBooking->booking_time;
            $getInvoice->booking_date = $getBooking->booking_date;
            
            $getUser = $this->Api_model->getSingleRow('user', array(
                'user_id' => $getInvoice->user_id
            ));
            
            $getInvoice->userName = $getUser->name;
            $getInvoice->address  = $getUser->address;
            
            if ($getUser->image) {
                $getInvoice->userImage = base_url() . $getUser->image;
            } else {
                $getInvoice->userImage = base_url() . 'assets/images/a.png';
            }
            
            $get_artists = $this->Api_model->getSingleRow('artist', array(
                'user_id' => $getInvoice->artist_id
            ));
            
            $get_cat = $this->Api_model->getSingleRow('category', array(
                'id' => $get_artists->category_id
            ));
            
            $getInvoice->ArtistName   = $get_artists->name;
            $getInvoice->categoryName = $get_cat->cat_name;
            
            if ($get_artists->image) {
                $getInvoice->artistImage = base_url() . $get_artists->image;
            } else {
                $getInvoice->artistImage = base_url() . 'assets/images/a.png';
            }
            
            array_push($getInvoices, $getInvoice);
        }
        
        
        $data['get_invoice'] = $getInvoices;
        $data['artist_name'] = $artist_name;
        
        $data['page'] = 'artist';
        $this->load->view('common/head.php');
        $this->load->view('common/sidebar.php', $data);
        $this->load->view('invoice.php', $data);
        $this->load->view('common/footer.php');
    }
    /*Change Status Artist*/
    public function change_status_user()
    {
        $id      = $_GET['id'];
        $status  = $_GET['status'];
        $request = $_GET['request'];
        $where   = array(
            'id' => $id
        );
        $data    = array(
            'status' => $status
        );
        
        $update = $this->Api_model->updateSingleRow('users', $where, $data);
        
        if ($request == 1) {
            $getdata      = $this->Api_model->getSingleRow('users', $where);
            $user_id      = $getdata->id;
            $title        = 'Account';
            //$this->firebase_notification($user_id,$title,ACTIVATE);
            $getToken     = $this->Api_model->getSingleRow('users', array(
                'id' => $user_id
            ));
            $device_token = $getToken->device_tokan;
            $this->firebase_with_class($device_token, '', '', $title, ACTIVATE);
            $userdata = $this->Api_model->getSingleRow('users', array(
                'id' => $user_id
            ));
            $username = $userdata->first_name;
            $email    = $userdata->email;
            $msg      = ACTIVATE;
            
            $this->Api_model->send_email($email, ACTIVATE, $msg);
            redirect('Admin/user');
        } elseif ($request == 2) {
            $getdata      = $this->Api_model->getSingleRow('users', $where);
            $user_id      = $getdata->id;
            $title        = 'Account';
            //$this->firebase_notification($user_id,$title,INACTIVATE);
            $getToken     = $this->Api_model->getSingleRow('users', array(
                'id' => $user_id
            ));
            $device_token = $getToken->device_tokan;
            $this->firebase_with_class($device_token, '', '', $title, INACTIVATE);
            $userdata = $this->Api_model->getSingleRow('users', array(
                'id' => $user_id
            ));
            $username = $userdata->first_name;
            $email    = $userdata->email;
            $msg      = INACTIVATE;
            
            $this->Api_model->send_email($email, INACTIVATE, $msg);
            redirect('Admin/user');
        }
    }
    
    public function change_status_order()
    {
        $id      = $_GET['id'];
        $status  = $_GET['status'];
        $request = $_GET['request'];
        $where   = array(
            'id' => $id
        );
        
        if ($request == 1) {
            $data         = array(
                'status' => $status
            );
            $update       = $this->Api_model->updateSingleRow('orders', $where, $data);
            $getdata      = $this->Api_model->getSingleRow('orders', $where);
            $user_id      = $getdata->user_id;
            $order_id     = $getdata->order_id;
            $title        = 'Order';
            //$this->firebase_notification($user_id,$title,PORDER.$getdata->order_id.'.');
            $getToken     = $this->Api_model->getSingleRow('users', array(
                'id' => $user_id
            ));
            $device_token = $getToken->device_tokan;
            $this->firebase_with_class($device_token, '', '', $title, PORDER . $getdata->order_id . '.');
            $userdata     = $this->Api_model->getSingleRow('users', array(
                'id' => $user_id
            ));
            $username     = $userdata->first_name;
            $email        = $userdata->email;
            $msg          = PORDER . $getdata->order_id;
            $notification = array(
                'user_id' => $user_id,
                'title' => $title,
                'type' => $title,
                'ids' => $getdata->order_id,
                'msg' => $msg,
                'created_at' => time()
            );
            $this->Api_model->insertGetId(NOTOFICATION_TBL, $notification);
            $this->Api_model->send_email($email, ORDER, $msg);
            redirect('Admin');
        } elseif ($request == 2) {
            $data         = array(
                'status' => $status
            );
            $update       = $this->Api_model->updateSingleRow('orders', $where, $data);
            $getdata1     = $this->Api_model->getSingleRow('orders', $where);
            $user_id      = $getdata1->user_id;
            $title        = 'Order';
            //$this->firebase_notification($user_id,$title,DORDER.$getdata1->order_id.'.');
            $getToken     = $this->Api_model->getSingleRow('users', array(
                'id' => $user_id
            ));
            $device_token = $getToken->device_tokan;
            $this->firebase_with_class($device_token, '', '', $title, DORDER . $getdata1->order_id . '.');
            $userdata     = $this->Api_model->getSingleRow('users', array(
                'id' => $user_id
            ));
            $username     = $userdata->first_name;
            $email        = $getdata1->email;
            $msg          = DORDER . $getdata1->order_id;
            $notification = array(
                'user_id' => $user_id,
                'title' => $title,
                'type' => $title,
                'ids' => $getdata->order_id,
                'msg' => $msg,
                'created_at' => time()
            );
            $this->Api_model->insertGetId(NOTOFICATION_TBL, $notification);
            
            $this->Api_model->send_email($email, ORDER, $msg);
            redirect('Admin');
        }
    }
    
    public function login()
    {
         

        
        $email    = $this->input->post('email', TRUE);
        $password = $this->input->post('password', TRUE);

        // print_r($email);
        // exit();
        $data['email']    = $email;
        $data['password'] = $password;
       
        $sess_array       = array();
        $getdata          = $this->Api_model->getSingleRow('admin', $data);
        // print_r($data['email']);
        // exit();
        if ($getdata) {
            
            $this->session->unset_userdata($sess_array);
            $sess_array = array(
                'name' => $getdata->name,
                'email' => $getdata->email,
                'id' => $getdata->id
            );
            
            $this->session->set_userdata($sess_array);
            $dataget['get_data'] = $getdata;
            $dataget['see_data'] = $sess_array;
            redirect('Admin/index');
        } else {
            
            $this->session->set_flashdata('msg', LOGIN_FLASH);
            redirect('Admin/index');
            
        }
    }
    
    public function contact_msg()
    {
        if (isset($_SESSION['name'])) {
            $data['contact'] = $this->Api_model->getAllData('contact');
            
            $this->load->view('common/head.php');
            $this->load->view('common/sidebar.php', $data);
            $this->load->view('contactDetails.php', $data);
            $this->load->view('common/footer.php');
        } else {
            redirect();
        }
    }
    
    public function sell_pets()
    {
        $this->load->view('common/head.php');
        $this->load->view('common/sidebar.php');
        $this->load->view('sellPets.php');
        $this->load->view('common/footer.php');
    }
    
    public function sell_pets_details()
    {
        if (isset($_SESSION['name'])) {
            $data['pets'] = $this->Api_model->getAllData('sell_pets');
            $this->load->view('common/head');
            $this->load->view('common/sidebar');
            $this->load->view('sellDetails', $data);
            $this->load->view('common/footer');
        } else {
            redirect();
        }
    }
    
    public function all_pets()
    {
        if (isset($_SESSION['name'])) {
            $data['pets'] = $this->Api_model->getAllData('pet');
            $this->load->view('common/head');
            $this->load->view('common/sidebar');
            $this->load->view('allPets', $data);
            $this->load->view('common/footer');
        } else {
            redirect();
        }
    }
    
   
    

    public function sell_pets_action()
    {
        $this->form_validation->set_rules('pet_name', 'Pet Name', 'required');
        $this->form_validation->set_rules('pet_owner', 'Pet Owner', 'required');
        $this->form_validation->set_rules('mobile', 'Mobile', 'required');
        $this->form_validation->set_rules('breed_name', 'Breed Name', 'required');
        $this->form_validation->set_rules('pet_price', 'Pet Price', 'required');
        $this->form_validation->set_rules('pet_age', 'Pet Age', 'required');
        $this->form_validation->set_rules('image', 'Image', 'required');
        
        if ($this->form_validation->run() == False) {
            
            $this->load->view('common/head.php');
            $this->load->view('common/sidebar.php');
            $this->load->view('sellPets.php');
            $this->load->view('common/footer.php');
            
        } else {
            
            $data = array(
                'pet_name' => $this->input->post('pet_name'),
                'owner_name' => $this->input->post('pet_owner'),
                'mobile' => $this->input->post('mobile'),
                'breed_name' => $this->input->post('breed_name'),
                'pet_price' => $this->input->post('pet_price'),
                'pet_age' => $this->input->post('pet_age'),
                'image' => $this->input->post('image')
            );
            
            $this->Api_model->insert('sell_pets', $data);
            $this->session->set_flashdata('success', 'Your data has been submitted successfully.');
            redirect('Admin/sell_pets');
        }
    }
    
    /* View Artist Profile*/
    public function profile_artist()
    {
        $user_id = $_GET['id'];
        
        $artist = $this->Api_model->getSingleRow('artist', array(
            'user_id' => $user_id
        ));
        
        $data['user'] = $artist;
        $data['page'] = 'artist';
        $this->load->view('common/head.php');
        $this->load->view('common/sidebar.php', $data);
        $this->load->view('profile.php', $data);
        $this->load->view('common/footer.php');
    }
    public function signup()
    {
        $this->load->view('common/head.php');
        $this->load->view('signup.php');
        $this->load->view('common/footer.php');
    }
    
    public function forgotpassword()
    {
        $this->load->view('common/head.php');
        $this->load->view('forgotpassword.php');
        $this->load->view('common/footer.php');
    }
    
    public function paystack()
    {
        $this->load->view('common/head.php');
        $this->load->view('paystack');
        
    }
    
     /*Added By hakkem*/
    public function addFood_company($id)
    {
        
        $pro_Cat = $this->Api_model->getAllDataWhere(array(
            'pet_type' => $id
        ), 'pro_cat');
        
        if (count($pro_Cat) > 0) {
            echo '<option value="">Please Select Sub Category</option>';
            foreach ($pro_Cat as $data) {
                echo '<option value=' . $data->id . '>' . $data->cat_title . '</option>';
            }
        } else {
            echo '<option value="">No Sub Cat Found</option>';
        }
        
    }

    public function getSubCat($id)
    {
        $country        = $this->Api_model->getAllDataWhere(array(
            'status' => 1
        ), 'country');
        $attribute      = $this->Api_model->getAllDataWhere(array(
            'status' => 1
        ), 'attribute');
        $attributeSize  = $this->Api_model->getAllDataWhere(array(
            'status' => 1
        ), 'pro_size');
        $attributeColor = $this->Api_model->getAllDataWhere(array(
            'status' => 1
        ), 'pro_color');
        $pro_Cat        = $this->Api_model->getAllDataWhere(array(
            'pet_type' => $id
        ), 'pro_cat');
        if (count($pro_Cat) > 0) {
            echo '<option value="">Please Select Sub Category</option>';
            foreach ($pro_Cat as $data) {
                echo '<option value=' . $data->id . '>' . $data->cat_title . '</option>';
            }
        } else {
            echo '<option value="">No Sub Cat Found</option>';
        }
        
    }

    public function getBrand($id)
    {
        $country        = $this->Api_model->getAllDataWhere(array(
            'status' => 1
        ), 'country');
        $attribute      = $this->Api_model->getAllDataWhere(array(
            'status' => 1
        ), 'attribute');
        $attributeSize  = $this->Api_model->getAllDataWhere(array(
            'status' => 1
        ), 'pro_size');
        $attributeColor = $this->Api_model->getAllDataWhere(array(
            'status' => 1
        ), 'pro_color');
        $pro_Cat        = $this->Api_model->getAllDataWhere(array(
            'sub_cat_id' => $id
        ), 'food_company');
        if (count($pro_Cat) > 0) {
            echo '<option value="">Please Select Brand</option>';
            foreach ($pro_Cat as $data) {
                echo '<option value=' . $data->c_id . '>' . $data->c_name . '</option>';
            }
        } else {
            echo '<option value="">No Brand Found</option>';
        }
    }
     public function getSubCat1($id)
    {
       
        $pro_Cat        = $this->Api_model->getAllDataWhere(array(
            'pet_type' => $id
        ), 'pro_cat');
        if (count($pro_Cat) > 0) {
            echo '<option value="">Please Select Sub Category</option>';
            foreach ($pro_Cat as $data) {
                echo '<option value=' . $data->id . '>' . $data->cat_title . '</option>';
            }
        } else {
            echo '<option value="">No Sub Cat Found</option>';
        }
        
    }
    public function getBrand1($id)
    {
        
        $pro_Cat        = $this->Api_model->getAllDataWhere(array(
            'sub_cat_id' => $id
        ), 'food_company');
        if (count($pro_Cat) > 0) {
            echo '<option value="">Please Select Brand</option>';
            foreach ($pro_Cat as $data) {
                echo '<option value=' . $data->c_id . '>' . $data->c_name . '</option>';
            }
        } else {
            echo '<option value="">No Brand Found</option>';
        }
    }
    
    public function getProduct1($id)
    {
        
        $food_product  = $this->Api_model->getAllDataWhere(array(
           'c_id' => $id
        ), 'food_product');

        if (count($food_product) > 0) {
            echo '<option value="">Please Select Product</option>';
            foreach ($food_product as $data) {
                echo '<option value=' . $data->p_id . '>' . $data->p_name . '</option>';
            }
        } else {
            echo '<option value="">No Product Found</option>';
        }
    }
    
    // public function getBreedName($id)
    // {
        
    //     $breed = $this->Api_model->getAllDataWhere(array(
    //        'id' => $id
    //     ), 'breed');

    //     if (count($breed) > 0) {
    //         echo '<option value="">Please Select Breed</option>';
    //         foreach ($breed as $data) {
    //             echo '<option value=' . $data->id . '>' . $data->breed_name . '</option>';
    //         }
    //     } else {
    //         echo '<option value="">No Product Found</option>';
    //     }
    // }

    public function deleteProduct() 
    {
        $product_id = $this->uri->segment('3');
        $this->Api_model->deleteRecord(array(
            'p_id' => $product_id
        ), 'food_product');
        
        redirect('Admin/productlist');
    }
      
    
    public function addFoodCompany()
    {
        if (isset($_SESSION['name'])) {
            $this->form_validation->set_rules('company_name', 'Company Name', 'required');
            if ($this->form_validation->run() == FALSE) {
                $data['pet_type'] = $this->Api_model->get_all_pet_type('pet_type');
                $company          = $this->Api_model->getAllData('food_company');
                $companys         = array();
                foreach ($company as $company) {
                    $category           = $this->Api_model->getSingleRow('pet_type', array(
                        'id' => $company->cat_id
                    ));
                    $sub_category       = $this->Api_model->getSingleRow('pro_cat', array(
                        'id' => $company->sub_cat_id
                    ));
                    $company->pet_name  = $category->pet_name;
                    $company->cat_title = $sub_category->cat_title;
                    array_push($companys, $company);
                }
                $data['company'] = $companys;
                $data['page']    = 'Add Company';
                
                $this->load->view('common/head.php');
                $this->load->view('common/sidebar.php', $data);
                $this->load->view('addFood_company.php', $data);
                $this->load->view('common/footer.php');
            } else {
                $data['cat_id']          = $this->input->post('cat_id');
                $data['sub_cat_id']      = $this->input->post('sub_cat_id');
                $data['c_name']          = $this->input->post('company_name');
                $data['c_desc']          = $this->input->post('desc');
                $config['upload_path']   = './assets/images/products/';
                $config['allowed_types'] = 'gif|jpg|jpeg|png';
                $config['max_size']      = 10000;
                $config['file_name']     = time();
                $this->load->library('upload', $config);
                $this->upload->initialize($config);
                $productsimage = "";
                if ($this->upload->do_upload('img_path')) {
                    
                    $productsimage      = 'assets/images/products/' . $this->upload->data('file_name');
                    $data['c_img_path'] = $productsimage;
                } else {
                    echo "image is not inserted";
                }
                
                $this->Api_model->insert('food_company', $data);
                redirect('Admin/addFoodCompany');
                
            }
            
        } else {
            redirect('Admin/addFoodCompany');
        }
    }
    
    public function update_company()
    {
        if (isset($_SESSION['name'])) {
            
            $company_id         = $this->input->post('company_id');
            $data['cat_id']     = $this->input->post('cat_id');
            $data['sub_cat_id'] = $this->input->post('sub_cat_id');
            $data['c_name']     = $this->input->post('company_name');
            $data['c_desc']     = $this->input->post('desc');
            if ($_FILES['img_path']['name'] != "") {
                $config['upload_path']   = './assets/images/products/';
                $config['allowed_types'] = 'gif|jpg|jpeg|png';
                $config['max_size']      = 10000;
                $config['file_name']     = time();
                $this->load->library('upload', $config);
                $this->upload->initialize($config);
                $productsimage = "";
                if ($this->upload->do_upload('img_path')) {
                    
                    $productsimage      = 'assets/images/products/' . $this->upload->data('file_name');
                    $data['c_img_path'] = $productsimage;
                } else {
                    echo "image is not inserted";
                }
            } else {
                
            }
            
            $where = array(
                'c_id' => $company_id
            );
            $this->Api_model->updateSingleRow('food_company', $where, $data);
            // $this->Api_model->updateSingleRow('breed',$where, $data);
            redirect('Admin/addFoodCompany');
            
        } else {
            
            redirect('');
        }
    }
    public function editFood()
    {
        if (isset($_SESSION['name'])) {
            
            $company_id           = $this->uri->segment(3);
            $data['food_company'] = $this->Api_model->getSingleRow('food_company', array(
                'c_id' => $company_id
            ));
            $data['pro_Cat']      = $this->Api_model->getAllData('pro_cat');
            $data['pet_type1']    = $this->Api_model->getAllData('pet_type');
            
            $this->load->view('common/head.php');
            $this->load->view('common/sidebar.php');
            $this->load->view('update_company.php', $data);
            $this->load->view('common/footer.php');
        } else {
            redirect();
        }
    }
    
    // public function deleteFood(){
    //     if(isset($_SESSION['name'])) 
    //       {
    //            $company_id=$this->uri->segment(3);
    //            $data['food_company']= $this->Api_model->deleteR('food_company', array('c_id'=>$company_id));
    
    //           $this -> load -> view('common/head.php');
    //           $this -> load -> view('common/sidebar.php');
    //           $this -> load -> view('addFood_company.php',$data);
    //           $this -> load -> view('common/footer.php');
    //         }
    //         else
    //         {
    //           redirect();
    //         }
    //   }
    
    //        public function update_category()
    //          {
    //           if(isset($_SESSION['name'])) 
    //          {
    
    //            $pet_type=$this->input->post('pet_type');
    //            $data['company'] = $this->input->post('company_name');
    //           if ($_FILES['img_path']['name']!="")
    //            { 
    //             $config['upload_path']   = './assets/images/products/'; 
    //             $config['allowed_types'] = 'gif|jpg|jpeg|png';
    //             $config['max_size']      = 10000; 
    //             $config['file_name']     = time();
    //            $this->load->library('upload', $config);
    //            $this->upload->initialize($config);
    //            $productsimage = "";
    //             if ( $this->upload->do_upload('img_path'))
    //             {
    
    //            $productsimage='assets/images/products/'.$this->upload->data('file_name'); 
    //            $data['c_img_path'] =$productsimage;
    //             }
    //             else   
    //             {
    //                echo "image is not inserted";
    //             }
    //           }
    //           else
    //           {
    
    //           }
    
    //            $where = array('cat_title'=>$pet_type);
    //            $this->Api_model->updateSingleRow('food_company', $where, $data);
    //            // $this->Api_model->updateSingleRow('breed',$where, $data);
    //            redirect('Admin/addCategory');
    
    //     }
    //        else{
    
    //          redirect('');
    //        }
    // }
    
    // public function updateCategory(){
    //      if(isset($_SESSION['name'])) 
    //        {
    //             $company_id=$this->uri->segment(3);
    //             $data['pet_type']= $this->Api_model->getSingleRow('food_company', array('id'=>$pet_type));
    
    //            $this -> load -> view('common/head.php');
    //            $this -> load -> view('common/sidebar.php');
    //            $this -> load -> view('update_category.php',$data);
    //            $this -> load -> view('common/footer.php');
    //          }
    //          else
    //          {
    //            redirect();
    //          }
    //    }
    public function deleteOffer()
    {
        $offer = $this->uri->segment('3');
        $this->Api_model->deleteRecord(array(
            'id' => $offer
        ), 'offer');
        
        redirect('Admin/viewoffer');
    }

    
    public function deleteFood()
    {
        $company = $this->uri->segment('3');
        $this->Api_model->deleteRecord(array(
            'c_id' => $company
        ), 'food_company');
        
        redirect('Admin/addFoodCompany');
    }
    
    
    
    public function pay()
    {
        $amount   = $this->input->post('amount');
        $user_id  = $this->input->post('user_id');
        $order_id = $this->input->post('order_id');
        
        $data['amount']     = $amount;
        $data['user_id']    = $user_id;
        $data['order_id']   = $order_id;
        $data['txn_id']     = strtoupper($this->api->strongToken(8));
        $data['created_at'] = time();
        
        $getId = $this->Api_model->insertGetId('product_invoice', $data);
        
        if ($getId) {
            $update = $this->Api_model->updateSingleRow('orders', array(
                'order_id' => $order_id
            ), array(
                'payment_status' => 1
            ));
            
            $getuser      = $this->Api_model->getSingleRow('users', array(
                'id' => $user_id
            ));
            $username     = $getuser->first_name;
            $email        = $getuser->email;
            $title        = "Payment Info";
            //$this->firebase_notification($user_id,$title,PAYMENT_SUCCESS);
            $getToken     = $this->Api_model->getSingleRow('users', array(
                'id' => $user_id
            ));
            $device_token = $getToken->device_tokan;
            $this->firebase_with_class($device_token, '', '', $title, PAYMENT_SUCCESS);
            $msg = PAYMENT_SUCCESS;
            $this->Api_model->send_email($email, $title, $msg);
            redirect('Admin/orders');
        }
    }
    
    
    
    public function change_status_pet()
    {
        $id      = $_GET['id'];
        $status  = $_GET['status'];
        $request = $_GET['request'];
        $where   = array(
            'id' => $id
        );
        $data    = array(
            'verified' => $status
        );
        
        $update  = $this->Api_model->updateSingleRow('pet', $where, $data);
        $petdata = $this->Api_model->getSingleRow('pet', $where);
        if ($request == 1) {
            $getToken = $this->Api_model->getSingleRow('users', array(
                'id' => $petdata->user_id
            ));
            $user_id  = $getToken->id;
            $title    = PETPRO;
            // $this->firebase_notification($user_id,$title,VERIFIED);
            
            $device_token = $getToken->device_tokan;
            $this->firebase_with_class($device_token, '', '', $title, $petdata->petName . VERIFIED);
            redirect('Admin/all_pets');
        } elseif ($request == 2) {
            $getToken     = $this->Api_model->getSingleRow('users', array(
                'id' => $petdata->user_id
            ));
            $user_id      = $getToken->id;
            $title        = PETPRO;
            //$this->firebase_notification($user_id,$title,NOT_VERIFIED);
            $device_token = $getToken->device_tokan;
            $this->firebase_with_class($device_token, '', '', $title, $petdata->petName . NOT_VERIFIED);
            redirect('Admin/all_pets');
        }
    }
    
    
    public function change_status_pet_market()
    {
        $id      = $_GET['id'];
        $status  = $_GET['status'];
        $request = $_GET['request'];
        $where   = array(
            'id' => $id
        );
        $data    = array(
            'status' => $status
        );
        
        $update  = $this->Api_model->updateSingleRow('pet_market', $where, $data);
        $petdata = $this->Api_model->getSingleRow('pet_market', $where);
        if ($request == 1) {
            $getToken     = $this->Api_model->getSingleRow('users', array(
                'id' => $petdata->user_id
            ));
            $user_id      = $getToken->id;
            $title        = PETPRO;
            //$this->firebase_notification($user_id,$title,VERIFIED);
            $device_token = $getToken->device_tokan;
            $this->firebase_with_class($device_token, '', '', $title, $petdata->title . ' ' . ADVVERIFIED);
            redirect('Admin/pet_marcket_details');
        } elseif ($request == 2) {
            $getToken = $this->Api_model->getSingleRow('users', array(
                'id' => $petdata->user_id
            ));
            $user_id  = $getToken->id;
            $title    = 'Account';
            //$this->firebase_notification($user_id,$title,NOT_VERIFIED);
            
            $device_token = $getToken->device_tokan;
            $this->firebase_with_class($device_token, '', '', $title, $petdata->title . ' ' . ADVUNVERIFIED);
            redirect('Admin/pet_marcket_details');
        }
    }
    
    /*Added By Kavita_Web*/
    public function addVss()
    {
        if (isset($_SESSION['name'])) {
            $this->form_validation->set_rules('name', 'Name', 'required|callback_customAlpha');
            //  $this->form_validation->set_rules('mobile', 'mobile' ,'trim|required|xss_clean|callback_validate_num|htmlspecialchars');
            
            if ($this->form_validation->run() == FALSE) {
                //  $data['nearby_vss']=$this->Api_model->getAllData('nearby_vss');
                $data['get_vss'] = $this->Api_model->getAllData('nearby_type');
                $this->load->view('common/head.php');
                $this->load->view('common/sidebar.php');
                $this->load->view('addVss.php', $data);
                $this->load->view('common/footer.php');
            } else {
                
                $data['nearby_id']    = $this->input->post('nearby_id');
                $data['name']         = $this->input->post('name');
                $data['email']        = $this->input->post('email');
                $data['mobile']       = $this->input->post('mobile');
                $data['address']      = $this->input->post('address');
                $data['start_timing'] = $this->input->post('start_timing');
                $data['end_timing']   = $this->input->post('end_timing');
                if ($data['nearby_id'] == 1) {
                     $data['lati']      = $this->input->post('lati');
                     $data['longi']      = $this->input->post('longi');
                    $data['e_open_time']  = $this->input->post('e_open_time');
                    $data['e_close_time'] = $this->input->post('e_close_time');
                }
                 if ($data['nearby_id'] == 5) {
                $data['nearby_id']    = $this->input->post('nearby_id');
                $data['name']         = $this->input->post('name');
                $data['email']        = $this->input->post('email');
                $data['mobile']       = $this->input->post('mobile');
                $data['address']      = $this->input->post('address');
                }
                

                $data['image_path']      = $this->input->post('image_path');
                $config['upload_path']   = './assets/images/vss/';
                $config['allowed_types'] = 'gif|jpg|jpeg|png';
                $config['max_size']      = 10000;
                $config['file_name']     = time();
                $this->load->library('upload', $config);
                $this->upload->initialize($config);
                $productsimage = "";
                
                if ($this->upload->do_upload('image_path')) {
                    
                    $productsimage = 'assets/images/vss/' . $this->upload->data('file_name');
                    
                    
                }
                
                else {
                    
                }
                $data['image_path'] = $productsimage;
                $this->Api_model->insert('nearby_vss', $data);
                $this->session->set_flashdata('msg1', 'Added successfully.');
                redirect('Admin/addVss');
                
            }
            
        }
        
        else {
            redirect('Admin/addVss');
        }
        
    }
    
    public function addvet()
    {
        if (isset($_SESSION['name'])) {
            $this->form_validation->set_rules('name', 'Name', 'required|callback_customAlpha');
            //  $this->form_validation->set_rules('mobile', 'mobile' ,'trim|required|xss_clean|callback_validate_num|htmlspecialchars');
            
            if ($this->form_validation->run() == FALSE) {
                //  $data['nearby_vss']=$this->Api_model->getAllData('nearby_vss');
                
                $data['get_vss'] = $this->Api_model->getAllData('nearby_type');
                $this->load->view('common/head.php');
                $this->load->view('common/sidebar.php');
                $this->load->view('addvet.php', $data);
                $this->load->view('common/footer.php');
            } else {
                $data['nearby_id']       = $this->input->post('nearby_id');
                $data['name']            = $this->input->post('name');
                $data['email']           = $this->input->post('email');
                $data['mobile']          = $this->input->post('mobile');
                $data['lati']            = $this->input->post('lati');
                $data['longi']           = $this->input->post('longi');
                $data['address']         = $this->input->post('address');
                $data['start_timing']    = $this->input->post('start_timing');
                $data['end_timing']      = $this->input->post('end_timing');
                $data['image_path']      = $this->input->post('image_path');
                $config['upload_path']   = './assets/images/vss/';
                $config['allowed_types'] = 'gif|jpg|jpeg|png';
                $config['max_size']      = 10000;
                $config['file_name']     = time();
                $this->load->library('upload', $config);
                $this->upload->initialize($config);
                $productsimage = "";
                
                if ($this->upload->do_upload('image_path')) {
                    
                    $productsimage = 'assets/images/vss/' . $this->upload->data('file_name');
                    
                    
                }
                
                else {
                    
                }
                $data['image_path'] = $productsimage;
                $this->Api_model->insert('nearby_vss', $data);
                $this->session->set_flashdata('msg1', 'Added successfully.');
                redirect('Admin/addvet');
                
            }
            
        }
        
        else {
            redirect('Admin/addvet');
        }
        
    }
    
    public function edit_vet()
    {
        if (isset($_SESSION['name'])) {
            $id                 = $this->uri->segment(3);
            $data['nearby_vss'] = $this->Api_model->getSingleRow('nearby_vss', array(
                'id' => $id
            ));
            
            $this->load->view('common/head.php');
            $this->load->view('common/sidebar.php');
            $this->load->view('update_vet.php', $data);
            $this->load->view('common/footer.php');
        } else {
            redirect();
        }
    }

    public function update_vet()
    {
        
        if (isset($_SESSION['name'])) {
            $id                   = $this->input->post('id');
            $data['name']         = $this->input->post('name');
            $data['address']      = $this->input->post('address');
            $data['mobile']       = $this->input->post('mobile');
            $data['email']        = $this->input->post('email');
            $data['lati']        = $this->input->post('lati');
            $data['longi']        = $this->input->post('longi');
            $data['start_timing'] = $this->input->post('start_timing');
            $data['end_timing']   = $this->input->post('end_timing');
            $data['e_open_time']  = $this->input->post('e_open_time');
            $data['e_close_time'] = $this->input->post('e_close_time');
            
            
            if ($_FILES['image_path']['name'] != "") {
                $config['upload_path']   = './assets/images/vss/';
                $config['allowed_types'] = 'gif|jpg|jpeg|png';
                $config['max_size']      = 10000;
                $config['file_name']     = time();
                $this->load->library('upload', $config);
                $this->upload->initialize($config);
                $productsimage = "";
                if ($this->upload->do_upload('image_path')) {
                    
                    $productsimage      = 'assets/images/vss/' . $this->upload->data('file_name');
                    $data['image_path'] = $productsimage;
                } else {
                    echo "image is not inserted";
                }
            }
            
            $where = array(
                'id' => $id
            );
            $this->Api_model->updateSingleRow('nearby_vss', $where, $data);
           
            
            redirect('Admin/veterinary');
            
            
        } else {
            
            redirect('Admin/veterinary');
        }
    }
    
    public function edit_salon()
    {
        if (isset($_SESSION['name'])) {
            $id = $this->uri->segment(3);
            
            $data['nearby_vss'] = $this->Api_model->getSingleRow('nearby_vss', array(
                'id' => $id
            ));
            
            $this->load->view('common/head.php');
            $this->load->view('common/sidebar.php');
            $this->load->view('update_salon.php', $data);
            $this->load->view('common/footer.php');
        } else {
            redirect();
        }
    }

    public function update_salon()
    {
        
        if (isset($_SESSION['name'])) {
            $id                   = $this->input->post('id');
            $data['name']         = $this->input->post('name');
            $data['address']      = $this->input->post('address');
            $data['mobile']       = $this->input->post('mobile');
            $data['email']        = $this->input->post('email');
            $data['start_timing'] = $this->input->post('start_timing');
            $data['end_timing']   = $this->input->post('end_timing');
            
            
            if ($_FILES['image_path']['name'] != "") {
                $config['upload_path']   = './assets/images/vss/';
                $config['allowed_types'] = 'gif|jpg|jpeg|png';
                $config['max_size']      = 10000;
                $config['file_name']     = time();
                $this->load->library('upload', $config);
                $this->upload->initialize($config);
                $productsimage = "";
                if ($this->upload->do_upload('image_path')) {
                    
                    $productsimage      = 'assets/images/vss/' . $this->upload->data('file_name');
                    $data['image_path'] = $productsimage;
                } else {
                    echo "image is not inserted";
                }
            }
            
            $where = array(
                'id' => $id
            );
            $this->Api_model->updateSingleRow('nearby_vss', $where, $data);
          
            
            redirect('Admin/Salon');
            
            
        } else {
            
            redirect('Admin/Salon');
        }
    }
    
    public function edit_shop()
    {
        if (isset($_SESSION['name'])) {
            $id = $this->uri->segment(3);
            
            $data['nearby_vss'] = $this->Api_model->getSingleRow('nearby_vss', array(
                'id' => $id
            ));
            
            $this->load->view('common/head.php');
            $this->load->view('common/sidebar.php');
            $this->load->view('update_shop.php', $data);
            $this->load->view('common/footer.php');
        } else {
            redirect();
        }
    }

    public function update_shop()
    {
        
        if (isset($_SESSION['name'])) {
            $id                   = $this->input->post('id');
            $data['name']         = $this->input->post('name');
            $data['address']      = $this->input->post('address');
            $data['mobile']       = $this->input->post('mobile');
            $data['email']        = $this->input->post('email');
            $data['start_timing'] = $this->input->post('start_timing');
            $data['end_timing']   = $this->input->post('end_timing');
            
            
            if ($_FILES['image_path']['name'] != "") {
                $config['upload_path']   = './assets/images/vss/';
                $config['allowed_types'] = 'gif|jpg|jpeg|png';
                $config['max_size']      = 10000;
                $config['file_name']     = time();
                $this->load->library('upload', $config);
                $this->load->helper(array(
                    'form'
                ));
                $this->load->library('form_validation');
                
                
                $this->upload->initialize($config);
                $productsimage = "";
                if ($this->upload->do_upload('image_path')) {
                    
                    $productsimage      = 'assets/images/vss/' . $this->upload->data('file_name');
                    $data['image_path'] = $productsimage;
                } else {
                    echo "image is not inserted";
                }
            }
            
            
            $where = array(
                'id' => $id
            );
            $this->Api_model->updateSingleRow('nearby_vss', $where, $data);
           
            
            redirect('Admin/Shop');
            
            
        } else {
            
            redirect('Admin/Shop');
        }
    }
    
    public function edit_hostels()
    {
        if (isset($_SESSION['name'])) {
            $id = $this->uri->segment(3);
            
            $data['nearby_vss'] = $this->Api_model->getSingleRow('nearby_vss', array(
                'id' => $id
            ));
            
            $this->load->view('common/head.php');
            $this->load->view('common/sidebar.php');
            $this->load->view('update_hostels.php', $data);
            $this->load->view('common/footer.php');
        } else {
            redirect();
        }
    }

    public function edit_trainer()
    {
        if (isset($_SESSION['name'])) {
            $id = $this->uri->segment(3);
            
            $data['nearby_vss'] = $this->Api_model->getSingleRow('nearby_vss', array(
                'id' => $id
            ));
            
            $this->load->view('common/head.php');
            $this->load->view('common/sidebar.php');
            $this->load->view('update_trainer.php', $data);
            $this->load->view('common/footer.php');
        } else {
            redirect();
        }
    }

    
    public function update_hostels()
    {
        
        if (isset($_SESSION['name'])) {
            $id                   = $this->input->post('id');
            $data['name']         = $this->input->post('name');
            $data['address']      = $this->input->post('address');
            $data['mobile']       = $this->input->post('mobile');
            $data['email']        = $this->input->post('email');
            $data['start_timing'] = $this->input->post('start_timing');
            $data['end_timing']   = $this->input->post('end_timing');
            
            if ($_FILES['image_path']['name'] != "") {
                $config['upload_path']   = './assets/images/vss/';
                $config['allowed_types'] = 'gif|jpg|jpeg|png';
                $config['max_size']      = 10000;
                $config['file_name']     = time();
                $this->load->library('upload', $config);
                $this->load->helper(array(
                    'form'
                ));
                $this->load->library('form_validation');
                
                
                $this->upload->initialize($config);
                $productsimage = "";
                if ($this->upload->do_upload('image_path')) {
                    
                    $productsimage      = 'assets/images/vss/' . $this->upload->data('file_name');
                    $data['image_path'] = $productsimage;
                } else {
                    echo "image is not inserted";
                }
            }
            
            
            $where = array(
                'id' => $id
            );
            $this->Api_model->updateSingleRow('nearby_vss', $where, $data);
            
            
            redirect('Admin/Hostels');
            
            
        } else {
            
            redirect('Admin/Hostels');
        }
    }
    
    public function update_trainer()
    {
        
        if (isset($_SESSION['name'])) {
            $id                   = $this->input->post('id');
            $data['name']         = $this->input->post('name');
            $data['address']      = $this->input->post('address');
            $data['mobile']       = $this->input->post('mobile');
            $data['email']        = $this->input->post('email');
            // $data['start_timing'] = $this->input->post('start_timing');
            // $data['end_timing']   = $this->input->post('end_timing');
            
            if ($_FILES['image_path']['name'] != "") {
                $config['upload_path']   = './assets/images/vss/';
                $config['allowed_types'] = 'gif|jpg|jpeg|png';
                $config['max_size']      = 10000;
                $config['file_name']     = time();
                $this->load->library('upload', $config);
                $this->load->helper(array(
                    'form'
                ));
                $this->load->library('form_validation');
                
                
                $this->upload->initialize($config);
                $productsimage = "";
                if ($this->upload->do_upload('image_path')) {
                    
                    $productsimage      = 'assets/images/vss/' . $this->upload->data('file_name');
                    $data['image_path'] = $productsimage;
                } else {
                    echo "image is not inserted";
                }
            }
            
            
            $where = array(
                'id' => $id
            );
            $this->Api_model->updateSingleRow('nearby_vss', $where, $data);
           
            
            redirect('Admin/Trainer');
            
            
        } else {
            
            redirect('Admin/Trainer');
        }
    }
    
    public function addtrainer()
    {
        if (isset($_SESSION['name'])) {
            $this->form_validation->set_rules('name', 'Name', 'required|callback_customAlpha');
           
            
            if ($this->form_validation->run() == FALSE) {
                //  $data['nearby_vss']=$this->Api_model->getAllData('nearby_vss');
                
                $data['get_vss'] = $this->Api_model->getAllData('nearby_type');
                $this->load->view('common/head.php');
                $this->load->view('common/sidebar.php');
                $this->load->view('addtrainer.php', $data);
                $this->load->view('common/footer.php');
            } else {
                $data['nearby_id']       = $this->input->post('nearby_id');
                $data['name']            = $this->input->post('name');
                $data['email']           = $this->input->post('email');
                $data['mobile']          = $this->input->post('mobile');
                $data['address']         = $this->input->post('address');
                // $data['start_timing']    = "";
                // $data['end_timing']      = "";
                // $data['e_open_time']     = "";
                // $data['e_close_time']    = "";


                $data['image_path']      = $this->input->post('image_path');
                $config['upload_path']   = './assets/images/vss/';
                $config['allowed_types'] = 'gif|jpg|jpeg|png';
                $config['max_size']      = 10000;
                $config['file_name']     = time();
                $this->load->library('upload', $config);
                $this->upload->initialize($config);
                $productsimage = "";
                
                if ($this->upload->do_upload('image_path')) {
                    
                    $productsimage = 'assets/images/vss/' . $this->upload->data('file_name');
                    
                    
                }
                
                else {
                    
                }
                $data['image_path'] = $productsimage;
                $this->Api_model->insert('nearby_vss', $data);
                $this->session->set_flashdata('msg1', 'Added successfully.');
                redirect('Admin/addtrainer');
                
            }
            
        }
        
        else {
            redirect('Admin/addtrainer');
        }
        
    }


  public function app_setting()
    {
      if(isset($_SESSION['name'])) 
      {
        $data['firebase']= $this->Api_model->getSingleRow('firebase_keys',array('id'=>1));
              
            $this->load->view('common/head.php');
            $this->load->view('common/sidebar.php');
            $this->load->view('app_setting.php', $data);
            $this->load->view('common/footer.php');
      }
      else
      {
        redirect('');
      }
    }

     public function firebaseSetting()
    {
      $data['firebase_keys']= $this->input->post('firebase_keys', TRUE);
      $this->Api_model->updateSingleRow('firebase_keys', array('id'=>1), $data);
      $this->session->set_flashdata('msg', 'Firebase key changed successfully.');
      redirect('Admin/app_setting');
    }
    

    public function key_setting()
    {
      if(isset($_SESSION['name'])) 
      {
        $data['auth_key']= $this->Api_model->getSingleRow('auth_key',array('id'=>1));
              
            $this->load->view('common/head.php');
            $this->load->view('common/sidebar.php');
            $this->load->view('key_setting.php', $data);
            $this->load->view('common/footer.php');
      }
      else
      {
        redirect('');
      }
    }

     public function KeySetting()
    {
      $data['auth_key']= $this->input->post('auth_key', TRUE);
      $this->Api_model->updateSingleRow('auth_key', array('id'=>1), $data);
      $this->session->set_flashdata('msg', 'Firebase key changed successfully.');
      redirect('Admin/key_setting');
    }


    public function pet_marcket_details()
    {
        if (isset($_SESSION['name'])) {
            //$data['pets'] = $this->Api_model->getAllPetMarcketData();
            $data['pets'] = $this->Api_model->getAllData('pet_market');
            $this->load->view('common/head');
            $this->load->view('common/sidebar');
            $this->load->view('allPetmarket', $data);
            $this->load->view('common/footer');
        } else {
            redirect();
        }
    }
    
    
    function importProduct()
    {
        if (isset($_FILES["file"]["name"])) {
            $path   = $_FILES["file"]["tmp_name"];
            $object = PHPExcel_IOFactory::load($path);
            foreach ($object->getWorksheetIterator() as $worksheet) {
                print_r($worksheet);
                exit;
                $highestRow    = $worksheet->getHighestRow();
                $highestColumn = $worksheet->getHighestColumn();
                for ($row = 2; $row <= $highestRow; $row++) {
                    $customer_name = $worksheet->getCellByColumnAndRow(0, $row)->getValue();
                    $address       = $worksheet->getCellByColumnAndRow(1, $row)->getValue();
                    $city          = $worksheet->getCellByColumnAndRow(2, $row)->getValue();
                    $postal_code   = $worksheet->getCellByColumnAndRow(3, $row)->getValue();
                    $country       = $worksheet->getCellByColumnAndRow(4, $row)->getValue();
                    $data[]        = array(
                        'CustomerName' => $customer_name,
                        'Address' => $address,
                        'City' => $city,
                        'PostalCode' => $postal_code,
                        'Country' => $country
                    );
                }
            }
            $this->excel_import_model->insert($data);
            echo 'Data Imported successfully';
        }
    }
    
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
    
    public function logout()
    {
        $this->session->sess_destroy();
        redirect('./', 'refresh');
    }
}


function add_breed()
{
    if (isset($_SESSION['name'])) {
        $this->form_validation->set_rules('breed_name', 'Breed Name', 'required|callback_customAlpha');
        $this->form_validation->set_rules('origin', 'Origin', 'required|callback_customAlpha');
        $this->form_validation->set_rules('temperament', 'Temperament', 'required|callback_customAlpha');
        $this->form_validation->set_rules('life_span', 'Life Span', '');
        $this->form_validation->set_rules('weight_male', 'Weight Male', '');
        $this->form_validation->set_rules('weight_female', 'Weight Female', '');
        $this->form_validation->set_rules('height_male', 'Height Male', '');
        $this->form_validation->set_rules('height_female', 'Height Female', '');
        $this->form_validation->set_rules('target', 'Target', 'required');
        $this->form_validation->set_rules('manual_act', 'Manual Activity', 'required');
        $this->form_validation->set_rules('breed_cat', 'Breed Category', 'required|callback_customAlpha');
        $this->form_validation->set_rules('remark', 'Remark', 'required|callback_customAlpha');
        
        
        if ($this->form_validation->run() == FALSE) {
            
            $data['page'] = 'add_breed';
            $this->load->view('common/head.php');
            $this->load->view('common/sidebar.php', $data);
            $this->load->view('addbreed.php', $data);
            $this->load->view('common/footer.php');
        } else {
            
            /*get values of input box from addbreed form*/
            
            $data['name']            = $this->input->post('name');
            $data['origin']          = $this->input->post('origin');
            $data['temperament']     = $this->input->post('temperament');
            $data['life_span']       = $lifespan;
            $data['weight_male']     = $weightmale;
            $data['weight_female']   = $weightfemale;
            $data['height_male']     = $heightmale;
            $data['height_female']   = $heightfemale;
            $data['target']          = $this->input->post('target');
            $data['manual_activity'] = $this->input->post('manual_act');
            $data['breed_cat']       = $this->input->post('breed_cat');
            $data['remark']          = $this->input->post('remark');
            $data['image_path']      = $this->input->post('image_path');
            $data['pet_type']        = $this->input->post('pet_type');
            $data['active_status']   = "True";
            
            
            $config['upload_path']   = './assets/images/breeds/';
            $config['allowed_types'] = 'gif|jpg|jpeg|png';
            $config['max_size']      = 10000;
            $config['file_name']     = time();
            $this->load->library('upload', $config);
            $this->upload->initialize($config);
            $productsimage = "";
            
            if ($this->upload->do_upload('image_path')) {
                
                $productsimage = 'assets/images/breeds/' . $this->upload->data('file_name');
                
            }
            
            else {
                
            }
            
            $data['image_path'] = $productsimage;
            $this->Api_model->insert('breed', $data);
            redirect('Admin');
        }
        
    }
    
    else {
        redirect('Admin/addbreed');
    }
    
}
