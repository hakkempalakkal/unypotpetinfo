<div id="page-wrapper">
            <div class="container-fluid">
             <div class="row">
            <div class="col-md-12">
                        <div class="panel">


                            <div class="panel-heading">All Hostels</br></div>
                            <div class="table-responsive">

                                             <div>

<a class="dt-button buttons-print btn btn-primary m-r-10 " tabindex="0" aria-controls="example23" href="addVss"
  style="float: right;"><span>Add Hostels</span></a><br/>
  
  </div>

<table id="example23" class=" table-bordered table-hover table-responsive"  role="grid" >
                    <thead>
                    <tr>
                        <th class="text-center" >#</th>
                        <th class="text-center" style="width: 100px">Name</th>
                        <th class="text-center" style="width: 100px">Email</th>
                        <th class="text-center" style="width: 100px">Mobile</th>
                        <th class="text-center" style="width: 300px">Address</th>
                        <th>Opening Time</th>
                        <th>Closing Time</th>
                        <th>Image</th>
                        <th>Status</th>
                        <th class="text-center">Action</th>
                    </tr>
                    </thead>

                    <tbody>
                        <?php  $count=1; 
                        foreach ($vsss as $vss){
                         $id = $this->Api_model->getSingleRow('nearby_vss',array('id'=>$vss->id));
                         ?>
                    <tr>
                    <td class="py-1">
                    <?php echo $count++; ?>
                    </td>
                    <td class="py-1">
                    <?php echo $vss->name; ?>
                    </td>
                    <td>
                    <?php echo $vss->email; ?>
                    </td>
                    <td>
                    <?php echo $vss->mobile; ?>
                    </td>
                    <td>
                    <?php echo $vss->address;?>
                    </td>
                    <td>
                    <?php echo $vss->start_timing;?>
                    </td>
                    <td>
                    <?php echo $vss->end_timing;?>
                    </td>
                    
                    <td>    
                    <?php if($vss->image_path!=""){ ?>
                    <img style="border-radius: 50%;width: 40px;height: 40px;" src="<?= base_url(); ?><?php  echo $vss->image_path; ?>" alt="Image not Available"/>
                    <?php }else{?>
                    <img style="border-radius: 50%;width: 40px;height: 40px;" src="<?= base_url(); ?>assets/images/faces-clipart/pet.jpg" alt="Image not Available"/>
                    <?php } ?>
                    </td>
                   <td><?php if($vss->status==1){ ?>
                        <label class="badge badge-teal">Active</label>
                        <?php }elseif($vss->status==0){ ?>
                        <label class="badge badge-danger">Deactive</label>
                        <?php } ?> 
                        </td>





                    <td class="text-center">
                           <div class="dropdown">
                        <button class="btn dropdown-toggle" type="button" data-toggle="dropdown">Manage
                        <span class="caret"></span></button>
                        <ul class="dropdown-menu">
                          <li><a title="Verified" class="" href="<?php echo base_url('/Admin/change_hostels_status');?>?id=<?php echo $vss->id; ?>&status=1&request=1">Activate</a></li>
                          <li><a title="Not Verified" class="" href="<?php echo base_url('/Admin/change_hostels_status');?>?id=<?php echo $vss->id; ?>&status=0&request=2" >Deactivate</a></li>
                        </ul>
                    </div>

                        
                    <a title="Edit" class="btn btn-info btn-outline btn-circle btn-lg m-r-5" href="edit_hostels/<?php echo $vss->id; ?>"><i class="ti-pencil-alt"></i></a>

        <a title="Delete" onclick="return confirm('Are you sure you want to delete this Hostels?')" href="deleteHostels/<?php echo $vss->id; ?>" class="btn btn-info btn-outline btn-circle btn-lg m-r-5"><i class="icon-trash"></i></a> 
                              
                    </td>
                    </tr>
                    <?php } ?>  
                </tbody>
                 </table>
             </div>
         </div>
             </div>
