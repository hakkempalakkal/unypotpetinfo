<div class="main-panel">
  <div class="content-wrapper">
    <div class="row">
      <div class="col-lg-12 grid-margin stretch-card">
        <div class="card">
        <span style="color:red;"><h4><?php echo $this->session->flashdata('success'); ?></h4></span>
          <div class="card-body">
			      <form method="POST" action="<?php echo base_url('Admin/sell_pets_action'); ?>">
              <h2><u>Sell Pets</u></h2><br>
			          <div class="row">
                   <div class="col-md-6">
                      <div class="col-md-12">
                        <div class="form-group row">
                          <label class="col-sm-3 col-form-label">Pet Name</label>
                          <div class="col-sm-9">
                          <input type="text" class="form-control" value="" name="pet_name" id="code">
                         </div>
                       </div>
                       <span style="color:red;"><?php echo form_error('pet_name'); ?></span>
                     </div>
                   </div>
                 <div class="col-md-6">
                      <div class="col-md-12">
                        <div class="form-group row">
                          <label class="col-sm-3 col-form-label">Pet Owner Name</label>
                          <div class="col-sm-9">
                          <input type="text" class="form-control" value="" name="pet_owner" id="code">
                         </div>
                       </div>
                       <span style="color:red;"><?php echo form_error('pet_owner'); ?></span>
                     </div>
                  </div>
                <div class="col-md-6">
                      <div class="col-md-12">
                        <div class="form-group row">
                          <label class="col-sm-3 col-form-label">Mobile No.</label>
                          <div class="col-sm-9">
                          <input type="text" class="form-control" value="" name="mobile" id="code">
                         </div>
                       </div>
                       <span style="color:red;"><?php echo form_error('mobile'); ?></span>
                     </div>
                  </div>
                <div class="col-md-6">
                      <div class="col-md-12">
                        <div class="form-group row">
                          <label class="col-sm-3 col-form-label">Breed Name</label>
                          <div class="col-sm-9">
                          <input type="text" class="form-control" value="" name="breed_name" id="code">
                         </div>
                       </div>
                       <span style="color:red;"><?php echo form_error('breed_name'); ?></span>
                     </div>
                   </div>
                  <div class="col-md-6">
                      <div class="col-md-12">
                        <div class="form-group row">
                          <label class="col-sm-3 col-form-label">Pet Price</label>
                          <div class="col-sm-9">
                          <input type="text" class="form-control" value="" name="pet_price" id="code">
                         </div>
                       </div>
                       <span style="color:red;"><?php echo form_error('pet_price'); ?></span>
                     </div>
                  </div>
               <div class="col-md-6">
                      <div class="col-md-12">
                        <div class="form-group row">
                          <label class="col-sm-3 col-form-label">Pet Age</label>
                          <div class="col-sm-9">
                          <input type="text" class="form-control" value="" name="pet_age" id="code">
                         </div>
                        </div>
                       <span style="color:red;"><?php echo form_error('pet_age'); ?></span>
                     </div>
                   </div>
                <div class="col-md-6">
                      <div class="col-md-12">
                        <div class="form-group row">
                          <label class="col-sm-3 col-form-label">Pet's Image</label>
                          <div class="col-sm-9">
                          <input type="file" class="form-control" name="image" id="code">
                        </div>
                      </div>
                    <span style="color:red;"><?php echo form_error('image'); ?></span>
                  </div>
               </div>
              </div>
              <button class="btn btn-primary"><a style="color:white;">Submit</a></button>
            </form>
			 </div>
		  </div>
		</div>
	  </div>
	 </div>