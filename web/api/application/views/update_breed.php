 <div id="page-wrapper">
            <div class="container-fluid">            
       <div class="row">
                    <div class="col-md-12">
                        <div class="white-box">
                            <h3 class="box-title m-b-0">Edit Breed</h3>
              <div class="row">
              <div class="col-md-12 col-sm-12 col-xs-12">
                <div class="x_panel">
                  <div class="x_content">
                    <br />
                    <?php $attributes = array('id' => 'demo-form2', 'name'=>'randform','class'=>'form-horizontal form-label-left');
                            echo form_open_multipart('admin/update_breed_action', $attributes); ?>
                            <div class="row">
                                <div class="col-md-4">
                                <div class="form-group">
                                  <label class="col-sm-4">Pet Type</label>
                                  <select class="form-control " name="pet_type">
                                <?php if(count($pet_type)>0){
                                  foreach($pet_type as $petData){
                                  ?>

                                <option <?php if($petData->id== $breed->pet_type)?>  value="<?= $petData->id;?>"><?= ucfirst($petData->pet_name);?></option>
                                <?php } }?>
                                </select>
                                      </div>
                                    </div>
                            
                                  
                                    <input  type="hidden" id="id" name="id" value="<?php echo $breed->id; ?>" required="required" class="form-control">
                                     
                                  <div class="col-md-4">             
                                  <div class="form-group">
                                  <label class="col-sm-4">Breed Name</label>
                                  <input  type="text" id="first-name" name="breed_name" value="<?php echo $breed->breed_name; ?>" required="required" class="form-control">
                                      </div>
                                    </div>
                                    <span style="color:red;"><?php echo form_error('breed_name'); ?></span>      
                      
                      <div class="col-md-4">             
                            <div class="form-group">
                            <label class="col-sm-4">Origin</label>
                           <input  type="text" id="first-name" name="origin" value="<?php echo $breed->origin; ?>" required="required" class="form-control">
                                </div>
                              </div>
                    <span style="color:red;"><?php echo form_error('origin'); ?></span>
                    
                    <div class="col-md-4">             
                      <div class="form-group">
                      <label class="col-sm-4">Temperament</label>
                     <input type="text" id="first-name" name="temperament" value="<?php echo $breed->temperament; ?>" required="required" class="form-control">
                          </div>
                        </div>
                       <span style="color:red;"><?php echo form_error('temperament'); ?></span>

                      <div class="col-md-4">             
                      <div class="form-group">
                      <label class="col-sm-5">Life Span Min.</label>
                     <input  type="number" id="" name="life_span_1" step="any" value="<?php echo $life_span_min; ?>" required="required" class="form-control">
                          </div>
                        </div><span style="color:red;"><?php echo form_error('life_span_1'); ?></span>

                     <div class="col-md-4">             
                      <div class="form-group">
                      <label class="col-sm-5">Life Span Max.</label>
                     <input  type="number" id="" name="life_span_2" step="any" value="<?php echo $life_span_max; ?>" required="required" class="form-control">
                          </div>
                        </div><span style="color:red;"><?php echo form_error('life_span_2'); ?></span>


                      

                       <div class="col-md-4">             
                      <div class="form-group">
                      <label class="col-sm-5">Weight Male Min.</label>
                    <input   type="number" id="" name="min_weight_male" step="any" value="<?php echo $weight_male_min; ?>" required="required" class="form-control">
                          </div>
                        </div>
                      
                       <div class="col-md-4">             
                      <div class="form-group">
                      <label class="col-sm-5">Weight Male Max.</label>
                    <input type="number" id="" name="max_weight_male" step="any" value="<?php echo $weight_male_max; ?>" required="required" class="form-control">
                          </div>
                        </div><span style="color:red;"><?php echo form_error('weight_male'); ?></span>
                       

                       <div class="col-md-4">             
                      <div class="form-group">
                      <label class="col-sm-6">Weight Female Min.</label>
                     <input  type="number" id="" name="min_weight_female" step="any" value="<?php echo $weight_female_min; ?>" required="required" class="form-control">
                          </div>
                        </div>
                        
                        <div class="col-md-4">             
                      <div class="form-group">
                      <label class="col-sm-6">Weight Female Max.</label>
                     <input type="number" id="" name="max_weight_female" step="any" value="<?php echo $weight_female_max; ?>" required="required" class="form-control">
                          </div>
                        </div>
                          
                        
                     <div class="col-md-4">             
                      <div class="form-group">
                      <label class="col-sm-5">Height Male Min.</label>
                     <input  type="number" id="" name="min_height_male" step="any" value="<?php echo $height_male_min; ?>" required="required" class="form-control">
                          </div>
                        </div>        
                      <div class="col-md-4">             
                      <div class="form-group">
                      <label class="col-sm-5">Height Male Max.</label>
                     <input   type="number" id="" name="max_height_male" step="any" value="<?php echo $height_male_max; ?>" required="required" class="form-control">
                          </div>
                        </div> 
             
              
              <div class="col-md-4">             
                      <div class="form-group">
                      <label class="col-sm-6">Height Female Min.</label>
                     <input type="number" id="" name="min_height_female" step="any" value="<?php echo $height_female_min; ?>" required="required" class="form-control">
                          </div>
                        </div> 
                 <div class="col-md-4">             
                      <div class="form-group">
                      <label class="col-sm-6">Height Female Max.</label>
                    <input  type="number" id="" name="max_height_female" step="any" value="<?php echo $height_female_max; ?>" required="required" class="form-control">
                          </div>
                        </div>
      
                    
                    <div class="col-md-4">             
                      <div class="form-group">
                      <label class="col-sm-5">Manual Activity</label>
                    <input  type="number" step="any" id="first-name" name="manual_act" value="<?php echo $breed->manual_activity; ?>" required="required" class="form-control">
                          </div>
                        </div>
                     

                      <div class="col-md-4">             
                      <div class="form-group">
                      <label class="col-sm-5">Target</label>
                    <input  type="number" step="any" id="first-name" name="target" value="<?php echo $breed->target; ?>" required="required" class="form-control">
                          </div>
                        </div>
             
                    
                    <div class="col-md-4">             
                      <div class="form-group">
                      <label class="col-sm-5" id="b" style="width: 150px;">Breed Category</label>
                     <input type="text" id="first-name" name="breed_cat" value="<?php echo $breed->breed_cat; ?>" required="required" class="form-control">
                          </div>
                        </div>
                  <span style="color:red;"><?php echo form_error('breed_cat'); ?></span>
                    
                    <div class="col-md-4">             
                      <div class="form-group">
                      <label class="col-sm-4">Remark</label>
                     <input  type="text" id="first-name" name="remark" value="<?php echo $breed->remark; ?>" required="required" class="form-control">
                          </div>
                        </div>
                     <span style="color:red;"><?php echo form_error('remark'); ?></span>
                       
                        <div class="col-md-4">             
                      <div class="form-group">
                      <label class="col-sm-4">Breed Image</label>
                      <input type="file" id="first-name" name="image_path" value="<?php echo $breed->image_path; ?>" class="form-control " style="padding-bottom: 50px">
                          </div>
                        </div>
                        <div class="col-sm-12">             
                      <div class="form-group">
                    <img style="width: 20%; height: 15%" src="<?= base_url(); ?><?php  echo $breed->image_path; ?>"/>
                          </div>
                        </div>
                      <div class="form-group">
                        <div class="col-md-4 col-md-4 col-xs-12 col-md-offset-3">
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
<script language="javascript" type="text/javascript">
 
function randomString() {
  var chars = "0123456789";
  var string_length = 12;
  var randomstring = '';
  for (var i=0; i<string_length; i++) {
    var rnum = Math.floor(Math.random() * chars.length);
    randomstring += chars.substring(rnum,rnum+1);
  }
  document.randform.randomfield.value = randomstring;
}

function Calculate(evt)
{
   
var rate= $('#rate').val();
var quantity = $('#quantity').val();
var gross_amt= rate* quantity;
 $('#gross_amt').val(gross_amt);
 var discount = $('#discount').val();
var diss_amt= gross_amt*discount/100;
var net_amt= gross_amt-diss_amt;
 $('#net_amt').val(net_amt);
 var charCode = (evt.which) ? evt.which : evt.keyCode;
          if (charCode != 46 && charCode > 31 
            && (charCode < 48 || charCode > 57))
             return false;
 
}
</script>

