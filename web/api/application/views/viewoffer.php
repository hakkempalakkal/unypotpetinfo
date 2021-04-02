 <div id="page-wrapper">
            <div class="container-fluid">
             <div class="row">
                    <div class="col-md-12">
                        <div class="panel">
                            <div class="panel-heading">All Offers</div>
                            <div class="table-responsive">
                                <div>

<a class="dt-button buttons-print btn btn-primary m-r-10 " tabindex="0" aria-controls="example23" href="addoffer"
  style="float: right;"><span>Add Offer</span></a><br/><br/><br/>
  
  </div> 
<table class="table table-hover manage-u-table" id="myTable">                 
                    <thead>
                        <tr>
                        <th>#</th>
                        
                        <th>Category</th>
                        <th>Sub Category </th>
                        <th>Brand</th>
                        <th>Product Name</th>
                        <th>Discount</th>
                        <th>Start Date</th>
                        <th>End Date</th>
                        <th>Image</th>
                        <th>Status</th>
                        <th class="text-center">Action</th>

                        </tr>
                        </thead>
                        <tbody>

                        <?php  $count=1; foreach ($offer as $offer){ ?>
                        <tr>
                        <td><?php echo $count++; ?></td>
                                            
                        <td><?php echo $offer['pet_name'];?></td>
                        <td><?php echo $offer['cat_title'];?></td>
                        <td><?php echo $offer['c_name'];?></td>
                        <td><?php echo $offer['pro_name'];?></td>
                        <td><?php echo $offer['discount'];?>%</td>
                        <td><?php echo $offer['start_date'];?></td>
                        <td><?php echo $offer['end_date'];?></td>
                     
                       <td>

                       <?php if($offer['image']){ ?>
                       
                        <img style= width: 40px;height: 40px; src="<?php echo  base_url().$offer['image']; ?>" width="150" height="100" alt="image"/></td>
                        <?php }else{ ?>
                          
                        <img style="border-radius: 50%;width: 40px;height: 40px;" src="<?php echo  base_url('/assets/images/faces-clipart/pet.jpg'); ?>" alt="image" width="150" height="100" />
                     
                      <?php } ?>
                       </td>
                      

                      <td><?php if($offer['status']==1){ ?>
                        <label class="badge badge-teal">Active</label>
                        <?php }elseif($offer['status']==0){ ?>
                        <label class="badge badge-danger">Deactive</label>
                        <?php } ?> 
                        </td>



                    <td class="text-center">
                           <div class="dropdown">
                        <button class="btn dropdown-toggle" type="button" data-toggle="dropdown">Manage
                        <span class="caret"></span></button>
                        <ul class="dropdown-menu">
                          <li><a title="Verified" class="" href="<?php echo base_url('/Admin/change_offer_status');?>?id=<?php echo $offer['id']; ?>&status=1&request=1">Activate</a></li>
                          <li><a title="Not Verified" class="" href="<?php echo base_url('/Admin/change_offer_status');?>?id=<?php echo $offer['id']; ?>&status=0&request=2" >Deactivate</a></li>
                        </ul>
                    </div>

                        
                   

        <a title="Delete" onclick="return confirm('Are you sure you want to delete this Offer?')" href="deleteOffer/<?php echo $offer['id']; ?>" class="btn btn-info btn-outline btn-circle btn-lg m-r-5"><i class="icon-trash"></i></a> 
                              
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

