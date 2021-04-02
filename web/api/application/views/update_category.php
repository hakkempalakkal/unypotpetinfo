
        <div id="page-wrapper">
            <div class="container-fluid">
               
       <div class="row">
                    <div class="col-sm-12">
                        <div class="white-box">
                            <h3 class="box-title m-b-0">Edit Sub Category</h3>
                         <div class="row">
              <div class="col-md-12 col-sm-12 col-xs-12">
                <div class="x_panel">
                  <div class="x_content">
                    <br />
                      <p><?php $attributes = array('id' => 'demo-form2', 'name'=>'randform','class'=>'form-horizontal form-label-left');
                            echo form_open_multipart('admin/update_category_action', $attributes); ?>
                           <div class="col-md-12">
                         <div class="form-group">
<!--                            <option ><?php echo $get_company->pet_name;?></option> 
 -->                        <label class="col-sm-4">Select Category</label>
                         <select id="heard" class="form-control" name="pet_type1" required>

                <?php foreach ($pet_type1 as $pet_type1) { ?>

                        <option value="<?php echo $pet_type1->id; ?>" <?php if($company->pet_type==$pet_type1->id) { echo "selected"; } ?>><?php echo $pet_type1->pet_name; ?></option>
                        <?php } ?>
                        </select>
                        </div>
                        </div>
                        <div class="col-sm-12">
                        <div class="form-group">
                         
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
                  </div>
                </div>
              </div>
            </div> 
              
             </div>
           </div>
         </div>
        </div>
      </div>
                