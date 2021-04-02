<div id="page-wrapper">
            <div class="container-fluid">
                
                
                <!-- ============================================================== -->
                <div class="row">
                    <div class="col-md-12">
                        <div class="panel">
                            <div class="panel-heading">All Sell Pets</div>
                            <div class="table-responsive">
                        <table class="table table-hover manage-u-table" id="myTable">
                        <thead>
                        <tr>
                        <th>#</th>
                        <th>Pet Name</th>
                        <th>Pets Owner Name</th>
                        <th>Mobile No.</th>
                        <th>Breed Name</th>
                        <th>Sell Price</th>
                        <th>Pet Age</th>
                        <th>Image</th>
                        <th>Time</th>
                        </tr>
                        </thead>
                        <tbody>
                        <?php  $count=1; foreach ($pets as $pets){ ?>
                        <tr>
                        <td><?=  $count++; ?></td>
                        <td><?php echo $pets->pet_name; ?></td>
                        <td><?php echo $pets->owner_name; ?></td>
                        <td><?php echo "Not in Demo";//$pets->mobile; ?></td>
                        <td><?php echo $pets->breed_name; ?></td>
                        <td><?php echo $pets->pet_price; ?></td>
                        <td><?php echo $pets->pet_age; ?></td>
                        <td>
                        <?php if($pets->image){ ?>
                        <img style="border-radius: 50%;width: 40px;height: 40px;" src="<?php echo  base_url().$pets->image; ?>" width="80" height="80" alt="image"/>
                        <?php }else{ ?>
                        <img style="border-radius: 50%;width: 40px;height: 40px;" src="<?php echo  base_url('/assets/images/faces-clipart/pet.jpg'); ?>" alt="image" width="80" height="80" />
                        <?php } ?>
                        </td>
                        <td><?php echo $pets->created_at; ?></td>
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


