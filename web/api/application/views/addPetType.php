<div id="page-wrapper">
            <div class="container-fluid">
               
       <div class="row">
                    <div class="col-sm-12">
                        <div class="white-box">
                            <h3 class="box-title m-b-0">Add Pet Category</h3>
                         <div class="row">
              <div class="col-md-12 col-sm-12 col-xs-12">
                <div class="x_panel">
                  <div class="x_content">
                    <br />
                    <?php $attributes = array('id' => 'demo-form2', 'name'=>'randform','class'=>'form-horizontal form-label-left'); ?>
                       <?php echo form_open_multipart('Admin/add_pet_type_action', $attributes); ?>
                        <div class="col-sm-5" style="margin-right: 50px;">
                        <div class="form-group">
                          <label class="col-sm-4">Name</label>
              <input  type="text" id="first-name" name="cat_name" value="<?php echo set_value('cat_name');?>" class="form-control" style="height: 57px;"required  >
                          </div>
                        </div>
                      <div class="col-sm-5">             
                      <div class="form-group">
                      <label class="col-sm-4">Add Image</label>
                      <input  name="img_path" type="file" class="form-control" required="" style
                      ="padding-bottom: 50px;"> 
                       <span>Image size should be 255x255 pixles</span> 

                      </div>
                      </div>

                <div class="ln_solid"></div>
                    <div class="form-group">
                    <div class="col-md-6">
                    <button type="submit" class="btn btn-success">Submit</button>
                    </div>
                    </div>
                  </form>
                  </div>
                </div>
              </div>
            </div> 
              
             </div>
           </div>
         </div>


         <div class="row">
                    <div class="col-md-12">
                        <div class="panel">
                            <div class="panel-heading">All Pet Category</div>
                            <div class="table-responsive">
                        <table class="table table-hover manage-u-table" id="">
                        <thead>
                        <tr>
                        <th>#</th>
                        <th>Name</th>
                        <th>Image</th>
                        <th>Action</th>
                        
                        </tr>
                        </thead>
                        <tbody>
                        <?php  $count=1; foreach ($pet_type as $pet){ ?>
                        <tr>
                        <td><?=  $count++; ?></td>
                        <td><?php echo $pet->pet_name; ?></td>
                       <?php if($pet->pet_image){ ?>
                        <td>
                        <img style="border-radius: 50%;width: 50px;height: 50px;    background: #4dabe3;" src="<?php echo  base_url().$pet->pet_image; ?>" width="80" height="80" alt="image"/></td>
                        <?php }else{ ?>
                          <td>
                        <img style="border-radius: 50%;width: 50px;height: 50px;    background: #4dabe3;" src="<?php echo  base_url('/assets/images/faces-clipart/pet.jpg'); ?>" alt="image" width="50" height="50" />
                      </td>


                        <?php } ?>
                       <!-- <td><?php if($pet->status==1){ ?>
                        <label class="badge badge-teal">Active</label>
                        <?php }elseif($pet->status==0){ ?>
                        <label class="badge badge-danger">Deactive</label>
                        <?php } ?>
                        </td> -->
                        <td>
                          <?php //if($pet->status==0){ ?>
                          <!-- <a title="Active" class="btn btn-info btn-outline btn-circle btn-lg m-r-5" href="<?php echo base_url('/Admin/change_pet_type_status');?>?id=<?php echo $pet->id; ?>&status=1"><i class="fa fa-reply fa-fw"></i></a>
                           <?php//}else{?>
                         <a title="Deactive" class="btn btn-info btn-outline btn-circle btn-lg m-r-5" href="<?php echo base_url('/Admin/change_pet_type_status');?>?id=<?php echo $pet->id; ?>&status=0"><i class="fa fa-history fa-fw"></i></a> -->
                         <?php //} ?>
                          <a class="btn btn-info btn-outline btn-circle btn-lg m-r-5" data-toggle="modal" data-target="#myModal<?php echo $pet->id; ?>" title="Edit"><i class="ti-pencil-alt"></i></a>
<!-- 
                            <a title="Delete" onclick="return confirm('Are you sure you want to delete this Pet Type ?')" href="delete_Pet_Type/<?php echo  $pet->id; ?>" class="btn btn-info btn-outline btn-circle btn-lg m-r-5"><i class="icon-trash"></i></a> -->
                         
                        </td>
                        </tr>

                        
                        <div id="myModal<?php echo $pet->id; ?>" class="modal fade" role="dialog">
                          <div class="modal-dialog">

                            <!-- Modal content-->
                            <div class="modal-content">
                              <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                                <h4 class="modal-title"><b>Update Pet Category</b></h4>
                              </div>
                              <div class="modal-body">
                                <p><?php $attributes = array('id' => 'demo-form2', 'name'=>'randform','class'=>'form-horizontal form-label-left');
                            echo form_open_multipart('admin/update_pet_type_action', $attributes); ?></p>
                        <div class="col-sm-12">
                        <div class="form-group">
                           
                          <label class="col-sm-4">Name</label>

                         <input  type="hidden" id="first-name" name="company_id" value="<?php echo $pet->id; ?>" class="form-control" style ="padding-bottom: 50px;">

                        <input  type="text" id="first-name" name="company_name" value="<?php echo $pet->pet_name ; ?>" class="form-control">
                          </div>
                        </div>
                      <div class="col-sm-12">             
                      <div class="form-group">
                      <label class="col-sm-4">Add Image</label>
                      <input name="img_path" type="file" class="form-control" value="<?php echo $pet->pet_image ; ?>"  style ="padding-bottom: 50px;"> 
                      <br/> 
                      <img style="width: 70px;height: 70px; background: #4dabe3;" src="<?php echo  base_url().$pet->pet_image; ?>" width="80" height="80" alt="image/">

                  </div>
                </div>
                </div>

                     
                   <div class="ln_solid"></div>
                    <div class="form-group">
                    <div class="col-md-6">
                    <button type="submit" class="btn btn-success">Submit</button>
                    </div>
                    </div>

                  </form>
                  
                              <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                              </div>
                            </div>

                          </div>
                        </div>
                 
                        <?php } ?>  
             
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>

              </div>
            </div>

        