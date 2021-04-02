

<div id="page-wrapper">
            <div class="container-fluid">
               
       <div class="row">
                    <div class="col-sm-12">
                        <div class="white-box">
                            <h3 class="box-title m-b-0">Add Contact</h3>

                         <div class="row">
              <div class="col-md-12 col-sm-12 col-xs-12">
                <div class="x_panel">
                  <div class="x_content">
                    <br />
                    <?php $attributes = array('id' => 'demo-form2', 'name'=>'randform','class'=>'form-horizontal form-label-left');
                            echo form_open_multipart('admin/contact_add', $attributes); ?>
                    

                     <div class="form-group">
                      <label class="control-label col-md-3 col-sm-3 col-xs-12">Name</label>
                      <div class="col-md-6 col-sm-6 col-xs-12" >
                 <input  type="text"  name="name" value="<?php echo set_value('name');?>" class="form-control" required style ="height: 57px;">
                        </div>
                        </div>
                      </br>

          
                     <!--  </div> -->
                       
                        <div class="form-group">
                         <label class="control-label col-md-3 col-sm-3 col-xs-12">Mobile Number</label>
                         <div class="col-md-6 col-sm-6 col-xs-12" >
                        <input  type="text"  name="mobile_no" value="<?php echo set_value('mobile_no');?>" class="form-control" required style ="height: 57px;">
                          </div>
                        </div>


                      

                                      
                      <div class="form-group">
                   <label class="control-label col-md-3 col-sm-3 col-xs-12">Email</label>
                    <div class="col-md-6 col-sm-6 col-xs-12" >
                         <input  type="text"  name="email" value="<?php echo set_value('email');?>" class="form-control" required style ="height: 57px;">
                      
                          </div>
                        </div>
                      </br>


               <div class="ln_solid">      
               <div class="form-group">
                        <div class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
                    <button type="submit" class="btn btn-success">Submit</button>
                    </div>
                    </div>
                  </div>
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
                            <div class="panel-heading">All Contact</div>
                            <div class="table-responsive">
<!--   <table id="myTable" class=" table-bordered table-hover table-responsive" role="grid" width="100%">            
 -->                  <table class="table table-hover manage-u-table" id="">
                        <thead>
                        <tr>
                        <th>#</th>
                        <th>Name</th>
                        <th>Mobile Number</th>
                        <th>Email</th>
                        

                        </tr>
                        </thead>
                        <tbody>
                        <?php  $count=1; foreach ($contact as $contact){ ?>
                        <tr>
                        <td><?=  $count++; ?></td>
                        <td><?php echo $contact['name'];?></td>
                        <td><?php echo $contact['mobile_no'];?></td>
                        <td><?php echo $contact['email'];?></td>

                       
                      
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

