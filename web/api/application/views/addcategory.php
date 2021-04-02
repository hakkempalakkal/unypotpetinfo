<div id="page-wrapper">
            <div class="container-fluid">
               
       <div class="row">
                    <div class="col-sm-12">
                        <div class="white-box">
                            <h3 class="box-title m-b-0">Add Sub Category</h3>

                         <div class="row">
              <div class="col-md-12 col-sm-12 col-xs-12">
                <div class="x_panel">
                  <div class="x_content">
                    <br />
                    <?php $attributes = array('id' => 'demo-form2', 'name'=>'randform','class'=>'form-horizontal form-label-left'); ?>
                       <?php echo form_open_multipart('Admin/addCategory', $attributes); ?>
                        <div class="col-md-12">
                         <div class="form-group">
                        <label class="col-sm-4">Select Category</label>
                     
                        <select id="heard" class="form-control" name="pet_type" required>
                        <option value="#">--select--</option>
                        <?php foreach ($pet_type as $pet_type) { ?>
                        <option value="<?php echo $pet_type->id; ?>"><?php echo $pet_type->pet_name; ?></option>
                        <?php } ?>
                        </select>
                        </div>
                        </div>
                        <div class="col-md-12">
                        <div class="form-group">
                        <label class="col-sm-4">Sub Category Name</label>

                        <input  type="text" id="first-name" name="cat_name" value="<?php echo set_value('cat_name');?>" class="form-control" required>
                        </div>
                        </div>
             
            <!-- 
                        <div class="col-sm-12">             
                      <div class="form-group">
                      <label class="col-sm-4">Category Description</label>

                      <textarea name="desc" class="form-control" required></textarea>
                          </div>
                        </div> -->

                      <div class="col-sm-12">             
                      <div class="form-group">
                      <label class="col-sm-4">Add Image</label>
                      <input  name="img_path" type="file" class="form-control" required style
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
                            <div class="panel-heading">All Sub Category</div>
                            <div class="table-responsive">
                        <table class="table table-hover manage-u-table" id="myTable">
                        <thead>
                        <tr>
                        <th>#</th>
                       
                        <th>Category Name</th>
                        <th>Sub Category Name</th>
                        <th>Sub Category Image</th>
                        <th>Sub Category Status</th>
                        <th>Action</th>

                        
                        </tr>
                        </thead>
                        <tbody>
                        <?php  $count=1; foreach ($get_company as $company){ ?>
                        <tr>
                        <td><?php  $count++; ?></td>
                         <td><?php echo $company->pet_name; ?></td>
                        <td><?php echo $company->cat_title; ?></td>
                        
                       <?php if($company->c_img_path){ ?>
                        <td>
                        <img style="border-radius: 50%;width: 50px;height: 50px;    background: #4dabe3;" src="<?php echo  base_url().$company->c_img_path; ?>" width="80" height="80" alt="image"/></td>
                        <?php }else{ ?>
                          <td>
                        <img style="border-radius: 50%;width: 50px;height: 50px;    background: #4dabe3;" src="<?php echo  base_url('/assets/images/faces-clipart/pet.jpg'); ?>" alt="image" width="50" height="50" />
                      </td>


                        <?php } ?>
                       <td><?php if($company->status==1){ ?>  
                        <label class="badge badge-teal">Active</label>
                        <?php }elseif($company->status==0){ ?>
                        <label class="badge badge-danger">Deactive</label>
                        <?php } ?> 


                        </td>
                        <td>
                          <!-- <?php if($company->status==0){ ?>
                          <a title="Active" class="btn btn-info btn-outline btn-circle btn-lg m-r-5" href="<?php echo base_url('/Admin/change_cat_status');?>?id=<?php echo $company->id; ?>&status=1"><i class="fa fa-reply fa-fw"></i></a>
                           <?php }else{?>
                         <a title="Deactive" class="btn btn-info btn-outline btn-circle btn-lg m-r-5" href="<?php echo base_url('/Admin/change_cat_status');?>?id=<?php echo $company->id; ?>&status=0"><i class="fa fa-history fa-fw"></i></a>
                         <?php } ?> -->
                        <div class="dropdown">
                        <button class="btn dropdown-toggle" type="button" data-toggle="dropdown">Manage
                        <span class="caret"></span></button>
                        <ul class="dropdown-menu">
                          <li><a title="Verified" class="" href="<?php echo base_url('/Admin/change_cat_status');?>?id=<?php echo $company->id; ?>&status=1">Activate</a></li>
                          <li><a title="Not Verified" class="" href="<?php echo base_url('/Admin/change_cat_status');?>?id=<?php echo $company->id; ?>&status=0" >Deactivate</a></li>
                        </ul>
                       <!--   <a class="btn btn-info btn-outline btn-circle btn-lg m-r-5" data-toggle="modal" data-target="#myModal<?php echo $company->id; ?>" title="Edit"><i class="ti-pencil-alt"></i></a> -->
                            <a title="Edit" class="btn btn-info btn-outline btn-circle btn-lg m-r-5" href="update_category/<?php echo  $company->id; ?>"><i class="fa fa-edit fa-fw"></i></a>

	<!-- 
                         <a title="Delete" onclick="return confirm('Are you sure you want to delete this Category ?')" href="delete_Cat/<?php echo  $company->id; ?>" class="btn btn-info btn-outline btn-circle btn-lg m-r-5"><i class="icon-trash"></i></a> -->

                         
        <!--  <a title="Delete" onclick="return confirm('Are you sure you want to delete this Category?')" href="deleteCategory/<?php echo  $company->id; ?>" class="btn btn-info btn-outline btn-circle btn-lg m-r-5"><i class="icon-trash"></i></a> -->
                      </div>
                        </td>
                        </tr>
                        <div id="myModal<?php echo $company->id; ?>" class="modal fade" role="dialog">
                          <div class="modal-dialog">
                            <!-- Modal content-->
                            <div class="modal-content">
                              <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                                <h4 class="modal-title"><b>Update Category</b></h4>
                              </div>
                              <div class="modal-body">
                                <p><?php $attributes = array('id' => 'demo-form2', 'name'=>'randform','class'=>'form-horizontal form-label-left');
                            echo form_open_multipart('admin/update_category', $attributes); ?>
                           <div class="col-md-12">
                         <div class="form-group">
                        <label class="col-sm-4">Select Category</label>
                         <select id="heard" class="form-control" name="pet_type1" required>
                <?php foreach ($pet_type1 as $pet_type1) { ?>
                        <option value="<?php echo $pet_type1->id; ?>"><?php echo $pet_type1->pet_name; ?></option>
                        <?php } ?>
                        </select>
                        </div>
                        </div>
                        <div class="col-sm-12">
                        <div class="form-group">
                          <br/><br/>  
                          <label class="col-sm-4">Sub Category Name</label>
                          <input  type="hidden" id="first-name" name="id" value="<?php echo $company->id; ?>" class="form-control">
                        <input  type="text" id="first-name" name="company_name" value="<?php echo $company->cat_title;?>" class="form-control">
                          </div>
                        </div>
                        <div class="col-sm-12">             
                      <div class="form-group">
                      <label class="col-sm-4">Add Image</label>
                      <input name="img_path" type="file" class="form-control" style="padding-bottom: 50px;">
                      <br/>
                   <img style="width: 100px;height: 100px; background: #4dabe3;" src="<?php echo  base_url().$company->c_img_path; ?>" width="80" height="80" alt="image"/>
                      </div>
                      </div>
                <div class="ln_solid"></div>
                    <div class="form-group">
                    <div class="col-md-6">
                    <button type="submit" class="btn btn-success">Submit</button>
                    </div>
                    </div>
                  </form>
                  </p>
                              </div>
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

        