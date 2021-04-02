  <div id="page-wrapper">
            <div class="container-fluid">
               
       <div class="row">
                    <div class="col-sm-12">
                        <div class="white-box">
                            <h3 class="box-title m-b-0">Edit Pet Shop</h3>
                         <div class="row">
              <div class="col-md-12 col-sm-12 col-xs-12">
                <div class="x_panel">
                  <div class="x_content">
                    <br />
                    <?php $attributes = array('id' => 'demo-form2', 'name'=>'randform','class'=>'form-horizontal form-label-left');
                            echo form_open_multipart('admin/update_shop', $attributes); ?>
                        <div class="col-sm-12">
                        <div class="form-group">
                          <label class="col-sm-4">Name</label>

                          <input  type="hidden" id="name" name="id" value="<?php echo $nearby_vss->id; ?>" class="form-control">

                        <input  type="text" id="name" name="name" value="<?php echo $nearby_vss->name; ?>" class="form-control" style ="height: 50px;">
                          </div>
                        </div>
                        <br/>
                           <div class="col-sm-12">
                        <div class="form-group">
                          <label class="col-sm-4">Address</label>

                          <input  type="hidden" id="name" name="id" value="<?php echo $nearby_vss->id; ?>" class="form-control">

                        <input  type="text" id="address" name="address" value="<?php echo $nearby_vss->address; ?>" class="form-control" style ="height: 50px;">
                          </div>
                        </div>
                        <br/>
                           <div class="col-sm-12">
                        <div class="form-group">
                          <label class="col-sm-4">Email</label>

                          <input  type="hidden" id="name" name="id" value="<?php echo $nearby_vss->id; ?>" class="form-control">

                        <input  type="text" id="email" name="email" value="<?php echo $nearby_vss->email; ?>" class="form-control" style ="height: 50px;">
                          </div>
                        </div>
                        <br/>
                           <div class="col-sm-12">
                        <div class="form-group">
                          <label class="col-sm-4">Mobile</label>

                          <input  type="hidden" id="name" name="id" value="<?php echo $nearby_vss->id; ?>" class="form-control">

                        <input  type="number" id="mobile" name="mobile" value="<?php echo $nearby_vss->mobile; ?>" class="form-control" style ="height: 50px;">
                          </div>
                        </div>
                        <br/>
                        
                        <div class="col-sm-12">
                       <div class="form-group">
                       <label class="col-sm-4">Opening Time</label>
                       <div class="input-group clockpicker">

                      <input type="text"  id="nearby_id" name="start_timing" class="form-control" value="<?php echo $nearby_vss->start_timing; ?>" required="required" readonly=""> 
                      <span class="input-group-addon"> <span class="glyphicon glyphicon-time"></span> 
                      </div>
                      </div>
                      </div>
                      

                       <div class="col-sm-12">
                       <div class="form-group">
                       <label class="col-sm-4">Closing Time</label>
                       <div class="input-group clockpicker">

                      <input type="text"  id="nearby_id" name="end_timing" class="form-control" value="<?php echo $nearby_vss->end_timing; ?>" required="required" readonly=""> 
                      <span class="input-group-addon"> <span class="glyphicon glyphicon-time"></span> 
                      </div>
                      </div>
                      </div>
         
                 <div class="col-sm-12">             
                      <div class="form-group">
                      <label class="col-sm-4">Image</label>
                    <input type="file" id="image_path" name="image_path" class="form-control " style ="padding-bottom: 50px;">

                          </div>
                        </div>

  <img style="width: 80px; height: 80px;" src="<?php echo  base_url().$nearby_vss->image_path; ?>" width="100" height="100" alt="image"/>
<br/><br/><br/>

                <div class="ln_solid"></div>
                    <div class="form-group">
                    <div class="col-md-6">
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
      </div>
    