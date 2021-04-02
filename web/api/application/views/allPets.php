 <div id="page-wrapper">
            <div class="container-fluid">
                
                
                <!-- ============================================================== -->
                <div class="row">
                    <div class="col-md-12">
                        <div class="panel">
                            <div class="panel-heading"><b>ALL PETS</b></div>
                            <div class="table-responsive">
                        <table class="table table-hover manage-u-table" id="myTable">
                        <thead>
                        <center>
                        <tr>
                        <th>#</th>
                        <th>Pet Name</th>
                        <th>Breed</th>
                        <th>Pet Type</th>
                        <th>Owner Name</th>
                        <th>Sex</th>
                        <th>Image</th>
                        <th>Status</th>
                       <th class="text-center">Action</th>
                        </tr>
                       </center>  

                        </thead>
                        <tbody>
                        <?php  $count=1; 
                        foreach ($pets as $pet){
                         $userData = $this->Api_model->getSingleRow('users',array('id'=>$pet->user_id));
                         $breed = $this->Api_model->getSingleRow('breed',array('id'=>$pet->breed_id));
                         $pet_type = $this->Api_model->getSingleRow('pet_type',array('id'=>$pet->pet_type));
                        ?>
                        <tr>
                        <td><?=  $count++; ?></td>
                        <td><?php echo $pet->petName; ?></td>
                        <td><?php echo $breed->breed_name; ?></td>
                        <td><?php echo $pet_type->pet_name; ?></td>
                        <td><?php echo $userData->first_name.' '.$userData->last_name; ?></td>
                        <td><?php echo $pet->sex; ?></td>
                         <td>
                        <?php if($pet->pet_img_path){ ?>
                        <img style="border-radius: 50%;width: 40px;height: 40px;" src="<?php echo  base_url().$pet->pet_img_path; ?>" width="80" height="80" alt="image"/>
                        <?php }else{ ?>
                        <img style="border-radius: 50%;width: 40px;height: 40px;" src="<?php echo  base_url('/assets/images/faces-clipart/dog.jpeg'); ?>" alt="image" width="80" height="80"/>
                        <?php } ?>
                        </td>
                        <td><?php if($pet->verified==1){ ?>
                        <label class="badge badge-teal">verified</label>
                        <?php }elseif($pet->verified==0){ ?>
                        <label class="badge badge-danger">Not verified</label>
                        <?php } ?>
                    </td>
                    <td class="text-center">
                       <!--  <?php if($pet->verified==0){ ?>
                         <a title="Verified" class="btn btn-info btn-outline btn-circle btn-lg m-r-5" href="<?php echo base_url('/Admin/change_status_pet');?>?id=<?php echo $pet->id; ?>&status=1&request=1"><i class="fa fa-reply fa-fw"></i></a>
                        <?php } else{?> 
                         <a title="Not Verified" class="btn btn-info btn-outline btn-circle btn-lg m-r-5" href="<?php echo base_url('/Admin/change_status_pet');?>?id=<?php echo $pet->id; ?>&status=0&request=2"><i class="fa fa-history fa-fw"></i></a>
                     <?php } ?> -->
                     <div class="dropdown">
                        <button class="btn dropdown-toggle" type="button" data-toggle="dropdown">Manage
                        <span class="caret"></span></button>
                        <ul class="dropdown-menu">
                          <li><a title="Verified" class="" href="<?php echo base_url('/Admin/change_status_pet');?>?id=<?php echo $pet->id; ?>&status=1&request=1">Verified</a></li>
                          <li><a title="Not Verified" class="" href="<?php echo base_url('/Admin/change_status_pet');?>?id=<?php echo $pet->id; ?>&status=0&request=2" >Not Verified</a></li>
                        </ul>
                      </div>
                  </td>
                        </tr>
                        <?php } ?>  
             
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
             </div>
           </div>


