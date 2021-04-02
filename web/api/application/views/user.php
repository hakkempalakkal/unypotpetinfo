

       <!-- ============================================================== -->
        <!-- End Left Sidebar -->
        <!-- ============================================================== -->
        <!-- ============================================================== -->
        <!-- Page Content -->

        <!-- ============================================================== -->
       
          <div id="page-wrapper">
            <div class="container-fluid">
              
                <!-- .row -->
                <div class="row">
                    
                 <div class="row">
                    <div class="col-md-12">
                        <div class="panel">
                            <div class="panel-heading">MANAGE USERS</div>
                            <div class="table-responsive">
                               <table class="table table-hover manage-u-table" id="myTable">
             
                                    <thead>
                                    <tr>
                                    <th>#</th>
                                    <th>Name</th>
                                    <th>Email</th>
                                    <th>Contact No</th>
                                    <th>Image</th>
                                    <th>Status</th>
                                    <th class="text-center">Action</th>
                                    </tr>
                                    </thead>
                <tbody>
                <?php $count=1; foreach ($user as $user) { ?>
                <tr>
                
                <td><?= $count++;?></td>
                <td><?php echo $user->first_name; ?></td>
                <td><?php echo $user->email; ?></td>
                <td><?php echo $user->mobile_no; ?></td>
                <td class="py-1">
                <?php if($user->profile_pic){ ?>
                <img style="border-radius: 50%;width: 40px;height: 40px;" src="<?php echo  base_url().$user->profile_pic; ?>" width="80" height="80" alt="image"/>
                <?php }else{ ?>
                <img style="border-radius: 50%;width: 40px;height: 40px;" src="<?php echo  base_url('/assets/images/faces-clipart/d1.jpeg'); ?>" width="80" height="80" alt="image"/>
                <?php } ?>
                 
                
                </td>
                <td><?php if($user->status==1){ ?>
                <label class="badge badge-teal">Active</label>
                <?php }elseif($user->status==0){ ?>
                <label class="badge badge-danger">Deactive</label>
                <?php } ?>
                </td>
                 <td class="text-center">
                        <!-- <?php if($user->status==0){ ?>
                         <a title="Active" class="btn btn-info btn-outline btn-circle btn-lg m-r-5" href="<?php echo base_url('/Admin/change_status_user');?>?id=<?php echo $user->id; ?>&status=1&request=1"><i class="fa fa-reply fa-fw"></i></a>
                         <?php }
                         else{ ?>
                         <a title="Deactive" class="btn btn-info btn-outline btn-circle btn-lg m-r-5" href="<?php echo base_url('/Admin/change_status_user');?>?id=<?php echo $user->id; ?>&status=0&request=2"><i class="fa fa-history fa-fw"></i></a>
                        <?php } ?> -->
                        <div class="dropdown">
                        <button class="btn dropdown-toggle" type="button" data-toggle="dropdown">Manage
                        <span class="caret"></span></button>
                        <ul class="dropdown-menu">
                          <li><a title="Active" class="" href="<?php echo base_url('/Admin/change_status_user');?>?id=<?php echo $user->id; ?>&status=1&request=1">Activate</a></li>
                          <li><a title="Deactive" class="" href="<?php echo base_url('/Admin/change_status_user');?>?id=<?php echo $user->id; ?>&status=0&request=2" >Deactivate</a></li>
                        </ul>
                        <a title="View Profile" class="btn btn-info btn-outline btn-circle btn-lg m-r-5" onclick="showDetails('myModal<?php echo $user->id; ?>')" ><i class="fa fa-user fa-fw"></i></a>
                         <a title="View Pets" class="btn btn-info btn-outline btn-circle btn-lg m-r-5" onclick="showDetails('myModal1<?php echo $user->id ?>')"><i class="fa fa-eye fa-fw"></i></a>
                      </div>
                     </td>



        <!-- Modal -->


        <div id="myModal<?php echo $user->id; ?>" class="modal fade" role="dialog" >
          <div class="modal-dialog">

        <!-- Modal content-->
    
          <div class="modal-content">
            <div class="modal-header">
              <h3><b>User Details</b></h3>

             <button type="button" class="close" data-dismiss="modal">&times;</button>
                </div>
                
                
          <div class="modal-body">
                    <center>
                  <?php if($user->profile_pic) { ?>
                <img src="<?php echo  base_url().$user->profile_pic; ?>" name="aboutme" width="140" height="140" border="0" class="img-circle"/>
                <?php } else { ?>
                <img  src="<?php echo  base_url('/assets/images/faces-clipart/d1.jpeg' ); ?>" wname="aboutme" width="140" height="140" border="0" class="img-circle"/>
                <?php } ?>
                    <h3 class="media-heading"><?php echo $user->first_name; ?></h3>
                    <h4 class="media-heading"><?php echo $user->email; ?></h4>
                    <span><strong>Mobile: </strong></span>
                      <span><?php echo $user->mobile_no; ?></span>
                    </center>
                    <hr>
                    <center>
                    <p class="text-left"><strong>Address: </strong><br>
                        <?php echo $user->address; ?></p>
                    <br>
                    </center>
                </div>
        </div>
      </div>
    </div>

<div id="myModal1<?php echo $user->id ?>" class="modal fade" role="dialog" >
  <div class="modal-dialog">

    <div class="modal-content" style="width: 140%;margin-left: -76px;">
      <div class="modal-header">
      <span class="pull-left"><h3><b>All Pets</b></h3></span>
        <button type="button" class="close" data-dismiss="modal">&times;</button>
      </div>

      <?php  $pets = $this->Api_model->get_pets_record_by_user_id($user->id);
       $i=1; 
       if(count($pets)>0){ 
      foreach ($pets as $pets) {
      $pet_type = $this->Api_model->getsingleRow('pet_type',array('id'=>$pets->pet_type));
      ?>
 
      

      <div class="modal-body">
      <div class="white-box" style="height: 160px;">
      <div class="row"> 
      <div class="col-md-1">
      <div class="user-bg">
      <span>
      <p><b><?php echo $i++; ?><b>.</p>
      </span>
      </div>
      </div>
        <div class="col-md-2">
        <div class="user-bg">
       <?php if($pets->pet_img_path) { ?>
      <img src="<?= base_url(); ?><?php echo $pets->pet_img_path; ?>" style="height:146px;width:151px;" class="img-circle" alt="image"/>
      <?php }else{ ?>
      <img src="<?php echo  base_url('/assets/images/faces-clipart/d1.jpeg' ); ?>" width="100%" alt="image"/>
      <?php } ?> </div>
    </div>
<div class="col-md-9" style="margin-top: -35px;">
    
        <!-- .row -->
        <div class="row text-center m-t-10">
            <div class="col-md-6 b-r"><strong>Pet Name</strong>
                <p><?php echo $pets->petName; ?></p>
            </div>
            <div class="col-md-6"><strong>Sex</strong>
                <p><?php echo $pets->sex; ?></p>
            </div>
        </div>
         <div class="row text-center m-t-10">
            
            <div class="col-md-6"><strong>Height</strong>
                <p><?php echo $pets->current_height; ?> CM</p>

            </div>
            <div class="col-md-6 b-r"><strong>Weight</strong>
                <p><?php echo $pets->current_weight; ?> KG</p>
            </div>
        </div>
        <div class="row text-center m-t-10">
            
            <div class="col-md-6"><strong>Trained</strong>
                <p><?php echo $pets->trained; ?></p>
            </div>
            <div class="col-md-6"><strong>Pet Type</strong>
                <p><?php echo ucfirst($pet_type->pet_name); ?></p>
            </div>
            </div>
       
        </div>
       </div>
      </div>
     </div>
  
   <hr style="width:100%;">
   <?php } }else{?>
 <div class="text-center" style="font-size:19px;margin:19px;">No Pets Available</div>
  <?php } ?>
    
    </div>
 </div>
</div>
</tr>
    <?php } ?>
            </tbody>
            </table>
            </div>
          </div>
         </div>
        </div>
                <!-- ============================================================== -->
                <!-- Right sidebar -->
                <!-- ============================================================== -->
                <!-- .right-sidebar -->
                <div class="right-sidebar">
                    <div class="slimscrollright">
                        <div class="rpanel-title"> Service Panel <span><i class="ti-close right-side-toggle"></i></span> </div>
                        <div class="r-panel-body">
                            <ul id="themecolors" class="m-t-20">
                                <li><b>With Light sidebar</b></li>
                                <li><a href="javascript:void(0)" data-theme="default" class="default-theme">1</a></li>
                                <li><a href="javascript:void(0)" data-theme="green" class="green-theme">2</a></li>
                                <li><a href="javascript:void(0)" data-theme="gray" class="yellow-theme">3</a></li>
                                <li><a href="javascript:void(0)" data-theme="blue" class="blue-theme">4</a></li>
                                <li><a href="javascript:void(0)" data-theme="purple" class="purple-theme">5</a></li>
                                <li><a href="javascript:void(0)" data-theme="megna" class="megna-theme">6</a></li>
                                <li class="full-width"><b>With Dark sidebar</b></li>
                                <li><a href="javascript:void(0)" data-theme="default-dark" class="default-dark-theme">7</a></li>
                                <li><a href="javascript:void(0)" data-theme="green-dark" class="green-dark-theme">8</a></li>
                                <li><a href="javascript:void(0)" data-theme="gray-dark" class="yellow-dark-theme">9</a></li>
                                <li><a href="javascript:void(0)" data-theme="blue-dark" class="blue-dark-theme working">10</a></li>
                                <li><a href="javascript:void(0)" data-theme="purple-dark" class="purple-dark-theme">11</a></li>
                                <li><a href="javascript:void(0)" data-theme="megna-dark" class="megna-dark-theme">12</a></li>
                            </ul>
                            <ul class="m-t-20 all-demos">
                                <li><b>Choose other demos</b></li>
                            </ul>
                            <ul class="m-t-20 chatonline">
                                <li><b>Chat option</b></li>
                                <li>
                                    <a href="javascript:void(0)"><img src="<?php echo base_url();?>assets/plugins/images/users/varun.jpg" alt="user-img" class="img-circle"> <span>Varun Dhavan <small class="text-success">online</small></span></a>
                                </li>
                                <li>
                                    <a href="javascript:void(0)"><img src="<?php echo base_url();?>assets/plugins/images/users/genu.jpg" alt="user-img" class="img-circle"> <span>Genelia Deshmukh <small class="text-warning">Away</small></span></a>
                                </li>
                                <li>
                                    <a href="javascript:void(0)"><img src="<?php echo base_url();?>assets/plugins/images/users/ritesh.jpg" alt="user-img" class="img-circle"> <span>Ritesh Deshmukh <small class="text-danger">Busy</small></span></a>
                                </li>
                                <li>
                                    <a href="javascript:void(0)"><img src="<?php echo base_url();?>assets/plugins/images/users/arijit.jpg" alt="user-img" class="img-circle"> <span>Arijit Sinh <small class="text-muted">Offline</small></span></a>
                                </li>
                                <li>
                                    <a href="javascript:void(0)"><img src="<?php echo base_url();?>assets/plugins/images/users/govinda.jpg" alt="user-img" class="img-circle"> <span>Govinda Star <small class="text-success">online</small></span></a>
                                </li>
                                <li>
                                    <a href="javascript:void(0)"><img src="<?php echo base_url();?>assets/plugins/images/users/hritik.jpg" alt="user-img" class="img-circle"> <span>John Abraham<small class="text-success">online</small></span></a>
                                </li>
                                <li>
                                    <a href="javascript:void(0)"><img src="<?php echo base_url();?>assets/plugins/images/users/john.jpg" alt="user-img" class="img-circle"> <span>Hritik Roshan<small class="text-success">online</small></span></a>
                                </li>
                                <li>
                                    <a href="javascript:void(0)"><img src="<?php echo base_url();?>assets/plugins/images/users/pawandeep.jpg" alt="user-img" class="img-circle"> <span>Pwandeep rajan <small class="text-success">online</small></span></a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                <!-- ============================================================== -->
                <!-- End Right sidebar -->
                <!-- ============================================================== -->
        </div>
      </div>
<script type="text/javascript">
  function showDetails(id)
  {
 $("#"+id).modal("show");
  }
</script>
