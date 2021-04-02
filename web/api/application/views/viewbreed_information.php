 <div id="page-wrapper">
            <div class="container-fluid">
             <div class="row">
                    <div class="col-md-12">
                        <div class="panel">
                            <div class="panel-heading">All Breed Information</div>
                            <div class="table-responsive">
                                <div>

<a class="dt-button buttons-print btn btn-primary m-r-10 " tabindex="0" aria-controls="example23" href="addbreed_info"
  style="float: right;"><span>Add Breed Information</span></a><br/><br/><br/>
  
  </div> 
   <table id="myTable" class=" table-bordered table-hover table-responsive" role="grid" width="100%">
<!--  <table class="table table-hover manage-u-table" id="myTable">                 
 -->                    <thead>
                        <tr>
                        <th class="text-center">#</th>
                        
                       <th class="text-center" >Add Title</th>
                        <th class="text-center" >Breed Description</th>
                         <th class="text-center">Breed Image</th>
                    
                        </tr>
                        </thead>
                        <tbody>

                        <?php  $count=1; foreach ($breed as $breed){ ?>
                        <tr>
                        <td><?php echo $count++; ?></td>
                                            
                        <td><?php echo $breed['breed_name'];?></td>
                        <td><?php echo $breed['b_description'];?></td>
                       
                     
                       <td>

                       <?php if($breed['image']){ ?>
                       
                    <img style= width: 40px;height: 40px; src="<?php echo  base_url().$breed['image']; ?>" width="80" height="80" alt="image"/></td>
                        <?php }else{ ?>
                          
                        <img style="border-radius: 50%;width: 40px;height: 40px;" src="<?php echo  base_url('/assets/images/faces-clipart/pet.jpg'); ?>" alt="image" width="80" height="80" />
                     
                      <?php } ?>
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

