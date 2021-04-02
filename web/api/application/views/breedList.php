<div id="page-wrapper">
            <div class="container-fluid">
                
                   <div class="row">
                    <div class="col-md-12">
                        <div class="panel">
                            <div class="panel-heading">All Breeds</div>
                            <div class="table-responsive">
                  <table id="myTable" class="display nowrap table table-hover table-striped table-bordered dataTable" cellspacing="0" width="100%" role="grid" aria-describedby="example23_info" style="width: 100%;">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Breed Name</th>
                        <th>Origin</th>
                        <th>LifeSpan</th>
                        <th>Temperament</th>
                        <th>Image</th>
                        <th class="text-center">Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <?php $count=1; foreach ($breed_list as $breed){ ?>
                    <tr>
                    <td class="py-1">
                    <?php echo $count++; ?>
                    </td>
                    <td class="py-1">
                    <?php echo $breed->breed_name; ?>
                    </td>
                    <td>
                    <?php echo $breed->origin; ?>
                    </td>
                    <td>
                    <?php echo $breed->life_span; ?>
                    </td>
                    <td>
                    <?php echo $breed->temperament?>
                    </td>
                    <td>
                    <?php if($breed->image_path!=""){ ?>
                    <img style="border-radius: 50%;width: 40px;height: 40px;" src="<?= base_url(); ?><?php  echo $breed->image_path; ?>" alt="Image not Available"/>
                    <?php }else{?>
                    <img style="border-radius: 50%;width: 40px;height: 40px;" src="<?= base_url(); ?>assets/images/faces-clipart/pet.jpg" alt="Image not Available"/>
                    <?php } ?>
                    </td>
                    <td class="text-center">
                        
                    <a title="Edit" class="btn btn-info btn-outline btn-circle btn-lg m-r-5" href="update_breed/<?php echo $breed->id; ?>"><i class="ti-pencil-alt"></i></a>

                    <a title="Details" class="btn btn-info btn-outline btn-circle btn-lg m-r-5" onclick="showProductInfo('myModal<?php echo $breed->id; ?>')"><i class="fa fa-eye fa-fw"></i></a>

                    </td>
                    </tr>

                <!-- Modal -->

                <div id="myModal<?php echo $breed->id; ?>" class="modal fade" role="dialog">
                <div class="modal-dialog">

                <!-- Modal content-->
                <div class="modal-content">
                  <div class="modal-header">
                  <h4><b>View Breed List</b></h4>
                  
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    
                    <div class="modal-body">
                    <div class="row">
                    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
         
                    <div class="row">
                    <div class="col-md-4">
                    <label class="text-bold" >Breed Name</label>
                    </div>
                    <div class="col-md-7">
                    <span><?php echo $breed->breed_name; ?></span>
                     </div>
                    </div>
        
                    <div class="row">
                    <div class="col-md-4">
                    <label>Origin</label>
                    </div>
                    <div class="col-md-7">
                    <span><?php echo $breed->origin; ?></span>
                    </div>
                   </div>


                    <div class="row">
                    <div class="col-md-4 ">
                    <label>Life Span</label>
                    </div>
                     <div class="col-md-7">
                   <span><?php echo $breed->life_span; ?></span>
                    </div>
                   </div>


                    <div class="row">
                    <div class="col-md-4">
                    <label>Weight Male</label>
                    </div>
                     <div class="col-md-7">
                    <span><?php echo $breed->weight_male; ?></span>
                    </div>
                     </div>
 
                    <div class="row">
                    <div class="col-md-4">
                    <label>Weight Female</label>
                    </div>
                     <div class="col-md-7">
                    <span><?php echo $breed->weight_female; ?></span>
                    </div>
                    </div>

                    <div class="row">
                    <div class="col-md-4 ">
                    <label>Height Male</label>
                    </div>
                     <div class="col-md-7">
                   <span><?php echo $breed->height_male; ?></span>
                    </div>
                      </div>
   
                    <div class="row">
                    <div class="col-md-4 ">
                    <label>Height Female</label>
                    </div>
                      <div class="col-md-7">
                    <span><?php echo $breed->height_female; ?></span>
                    </div>
                    </div>
                 
                    <div class="row">
                    <div class="col-md-4">
                    <label>Temperament</label>
                    </div>
                     <div class="col-md-7">
                   <span><?php echo $breed->temperament; ?></span>
                    </div>
                    </div>

                    <div class="row">
                    <div class="col-md-4">
                    <label>Active Status</label>
                    </div>
                    <div class="col-md-7">         
                    <span><?php echo $breed->active_status; ?></span>
                    </div>
                   </div>
 
                    <div class="row">
                    <div class="col-md-4">
                    <label>Target</label>
                    </div>
                     <div class="col-md-7">
                   <span><?php echo $breed->target; ?></span>
                    </div>
                   </div>
   
                    <div class="row">
                    <div class="col-md-4">
                    <label>Manual Activity</label>
                    </div>
                     <div class=" col-md-7">
                  <span><?php echo $breed->manual_activity; ?></span>
                    </div>
                  </div>
   
                    <div class="row">
                    <div class="col-md-4">
                    <label>Breed Category</label>
                    </div>
                     <div class="col-md-7">
                    <span><?php echo $breed->breed_cat; ?></span>
                     </div>
                    </div>
  
                    <div class="row">
                    <div class="col-md-4">
                    <label>Pet Type</label>
                    </div>
                    <div class="col-md-7">
                  <span><?php echo $this->Api_model->get_pet_type($breed->pet_type)->pet_name; ?></span>
                     </div>
                    </div>

                    <div class="row">
                    <div class="col-md-4 ">
                    <label>Remark</label>
                    </div>
                    <div class="col-md-7">
                     <span><?php echo $breed->remark; ?></span>
                   </div>
                    </div>

                     <div class="row">
                    <div class="col-md-4">
                    <label>Image</label>
                    </div>
                     <div class="col-md-7">
      <img style="max-width: 50%;clear:auto;" src="<?= base_url(); ?><?php  echo $breed->image_path; ?>"/>
                     </div>
                    </div>




                  </div>
                 </div>

                    </div>
                    </div>
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
             </div>
           </div>


<script type="text/javascript">
  function showProductInfo(id)
  {
 $("#"+id).modal("show");
  }
 
</script>