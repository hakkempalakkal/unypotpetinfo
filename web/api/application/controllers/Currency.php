<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Currency extends CI_Controller
{

    public function __construct()
    {
        parent::__construct();
        $this ->load-> library('session');
        $this ->load-> helper('form');
        $this ->load-> helper('url');
        $this ->load-> database();
        $this ->load-> library('form_validation');
        $this ->load-> model('Comman_model');
        $this ->load-> model('Api_model');
    }

     public function setting()
    {
      if(isset($_SESSION['name'])) 
      {
        
        $data['currency_setting']=$this->Api_model->getAllData(CRYSET_TBL);
        $currency_setting= $this->Api_model->getSingleRow('currency_setting',array('status'=>1));
        $data['currency_type']= $currency_setting->currency_symbol;
   
        $data['page']='setting';
        $this -> load -> view('common/head.php');
        $this -> load -> view('common/sidebar.php');
        $this -> load ->view('setting.php', $data);
        $this -> load -> view('common/footer.php');
      }
      else
      {
        redirect();
      }
    }

    

    public function currency_setting()
    {
      if(isset($_SESSION['name'])) 
        {
      $currency_id=$this->input->post('currency', TRUE);
      $this->Api_model->updateSingleRow(CRYSET_TBL, array('status'=>1), array('status'=>0));
      $this->Api_model->updateSingleRow(CRYSET_TBL, array('id'=>$currency_id), array('status'=>1));
      $this->session->set_flashdata('msg1', 'Currency Type changed successfully.');
      redirect('Currency/setting');
    }else{
      redirect();
    }

}

}