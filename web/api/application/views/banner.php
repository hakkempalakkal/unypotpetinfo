<div id="page-wrapper">
            <div class="container-fluid">
               
       <div class="row">
                    <div class="col-sm-12">
                        <div class="white-box">
                            <h3 class="box-title m-b-0">Add Banner</h3>
                         <div class="row">
              <div class="col-md-12 col-sm-12 col-xs-12">
                <div class="x_panel">
                  <div class="x_content">
                    <br/>
                    <?php $attributes = array('id' => 'demo-form2', 'name'=>'randform','class'=>'form-horizontal form-label-left'); ?>
                       <?php echo form_open_multipart('Admin/add_banners', $attributes); ?>
                       
                      <div class="col-sm-5">             
                      <div class="form-group">
                      <label class="col-sm-4">Add Image</label>
                      <input  name="img_path" type="file" class="form-control" required="" style
                      ="padding-bottom: 50px;"> 
                       <span>Image size should be 1080x548 pixles</span> 

                      </div>
                      </div>
                
              
                  <br/><br/>

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
                            <div class="panel-heading">All Banners</div>
                            <div class="table-responsive">
                        <table class="table table-hover manage-u-table" id="">
                        <thead>
                        <tr>
                        <th>#</th>
                      
                        <th>Image</th>
                         <th>Action</th> 
                        
                        </tr>
                        </thead>
                        <tbody>
                          
                        <?php  $count=1; foreach ($banner as $banners){ ?>
                        <tr>
                        <td><?=  $count++; ?></td>

                    
                       <?php if($banners->b_image){ ?>
                      
                        <td>
                        <img style="width: 150px;height: 70px;    background: #4dabe3;" src="<?php echo  base_url().$banners->b_image; ?>" width="80" height="80" alt="image"/></td>
                        <?php }else{ ?>
                          <td>
                        <img style="border-radius: 50%;width: 150px;height: 70px;    background: #4dabe3;" src="<?php echo  base_url('/assets/images/banner/brand.png'); ?>" alt="image" width="50" height="50" />
                      </td>


                        <?php } ?>
                       <!-- <td><?php if($pet->status==1){ ?>
                        <label class="badge badge-teal">Active</label>
                        <?php }elseif($pet->status==0){ ?>
                        <label class="badge badge-danger">Deactive</label>
                        <?php } ?>
                        </td> -->
                        <!-- <td>  -->
                          <?php //if($pet->status==0){ ?>
                          <!-- <a title="Active" class="btn btn-info btn-outline btn-circle btn-lg m-r-5" href="<?php echo base_url('/Admin/change_pet_type_status');?>?id=<?php echo $pet->id; ?>&status=1"><i class="fa fa-reply fa-fw"></i></a>
                           <?php//}else{?>
                         <a title="Deactive" class="btn btn-info btn-outline btn-circle btn-lg m-r-5" href="<?php echo base_url('/Admin/change_pet_type_status');?>?id=<?php echo $pet->id; ?>&status=0"><i class="fa fa-history fa-fw"></i></a> -->
                         <?php //} ?>
                         <!--  <a class="btn btn-info btn-outline btn-circle btn-lg m-r-5" data-toggle="modal" data-target="#myModal<?php echo $pet->id; ?>" title="Edit"><i class="ti-pencil-alt"></i></a> -->
                           <td>
                            <a title="Delete" onclick="return confirm('Are you sure you want to delete this Banner ?')" href="deleteBanner/<?php echo  $banners->id; ?>" class="btn btn-info btn-outline btn-circle btn-lg m-r-5"><i class="icon-trash"></i></a>
                         
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

        </div>