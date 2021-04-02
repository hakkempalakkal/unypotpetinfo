_marj<div id="page-wrapper">
            <div class="container-fluid">
                
                
                <!-- ============================================================== -->
                <div class="row">
                    <div class="col-md-12">
                        <div class="panel">
                            <div class="panel-heading"><b>ALL PET MARKET</b></div>
                            <div class="table-responsive">
                        <table class="table table-hover manage-u-table" id="myTable">
                        <thead>
                        <tr>
                        <th>#</th>
                        <th>Title</th>
                        <th>Pet Name</th>
                        <th>Price</th>
                        <th>Sale Type</th>
                        <th>User Name</th>
                        <th>User Email</th>
                        <th>Country</th>
                        <th>City</th>
                        <th>Status</th>
                        <th class="text-center">Action</th>
                        </tr>
                        </thead>
                        <tbody>
                        <?php  $count=1; 
                        foreach ($pets as $pet){
                     $pet_type = $this->Api_model->getSingleRow('pet_type',array('id'=>$pet->type_id));
                     $user = $this->Api_model->getSingleRow('users',array('id'=>$pet->user_id)); 
                        ?>
                        <tr>
                        <td><?=  $count++; ?></td>
                        <td><?php echo $pet->title; ?></td>
                        <td><?php echo $pet_type->pet_name; ?></td>
                        <td><?php echo $pet->price; ?></td>
                        <td><?php if($pet->sale_type==1){ ?>
                        <label class="badge badge-teal">Price</label>
                        <?php }elseif($pet->sale_type==0){ ?>
                        <label class="badge badge-danger">Free</label>
                        <?php } ?>
                    </td>
                        <td><?php echo $user->first_name; ?></td>
                        <td><?php echo $user->email; ?></td>
                        <td><?php echo $user->country; ?></td>
                        <td><?php echo $user->city; ?></td>
                       </td>
                       <td><?php if($pet->status==1){ ?>
                        <label class="badge badge-teal">Activate</label>
                        <?php }elseif($pet->status==0){ ?>
                        <label class="badge badge-danger">Deactivate</label>
                        <?php } ?>
                    </td>
                    <td class="text-center">
                    	<div class="dropdown">
						  <button class="btn dropdown-toggle" type="button" data-toggle="dropdown">Manage
						  <span class="caret"></span></button>
						  <ul class="dropdown-menu">
						    <li><a title="Active" class="" href="<?php echo base_url('/Admin/change_status_pet_market');?>?id=<?php echo $pet->id; ?>&status=1&request=1">Activated</a></li>
						    <li><a title="Deactive" class="" href="<?php echo base_url('/Admin/change_status_pet_market');?>?id=<?php echo $pet->id; ?>&status=0&request=2">Deactivated</a></li>
						  </ul>
						</div>
                       <!--  <?php if($pet->status==0){ ?>
                         <a title="Active" class="btn btn-info btn-outline btn-circle btn-lg m-r-5" href="<?php echo base_url('/Admin/change_status_pet_market');?>?id=<?php echo $pet->id; ?>&status=1&request=1"><i class="fa fa-reply fa-fw"></i></a>
                        <?php } else{?> 
                         <a title="Deactive" class="btn btn-info btn-outline btn-circle btn-lg m-r-5" href="<?php echo base_url('/Admin/change_status_pet_market');?>?id=<?php echo $pet->id; ?>&status=0&request=2"><i class="fa fa-history fa-fw"></i></a>
                     <?php } ?> -->
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


