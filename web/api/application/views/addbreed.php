<div id="page-wrapper">
            <div class="container-fluid">
               
       <div class="row">
                    <div class="col-md-12">
                        <div class="white-box">
                            <h3 class="box-title m-b-0">Add Breed</h3>
                         <div class="row">
              <div class="col-md-12 col-sm-12 col-xs-12">
                <div class="x_panel">
                  <div class="x_content">
                    <br />
                    <?php $attributes = array('id' => 'demo-form2', 'name'=>'randform','class'=>'form-horizontal form-label-left');
                            echo form_open_multipart('admin/add_breed', $attributes); ?>
              <div class="row">
                    
                     <div class="col-md-4">
                        <div class="form-group">
                          <label class="col-sm-4">Pet Type</label>
                        <select class="form-control " name="pet_type">
                        <?php if(count($pet_type)>0){
                          foreach($pet_type as $petData){
                          ?>
                        <option value="<?= $petData->id;?>"><?= ucfirst($petData->pet_name);?></option>
                        <?php } }?>
                        </select>
                        </div>
                        </div>

                        <div class="col-md-4">
                        <div class="form-group">
                          <label class="col-sm-4">Breed Name </label>
                        <input  type="text" id="first-name" name="breed_name" value="<?php echo set_value('breed_name');?>" required="required" class="form-control">
                          </div>
                        </div>
            <span style="color:red;"><?php echo form_error('breed_name'); ?></span>

                    <div class="col-md-4">
                        <div class="form-group">
                          <label class="col-sm-4">Origin</label>
                        <input type="text" id="first-name" name="origin" value="<?php echo set_value('origin');?>" required="required" class="form-control">
                          </div>
                        </div>
                      </div>
              <span style="color:red;"><?php echo form_error('origin'); ?></span>
              <div class="col-md-4">             
                      <div class="form-group">
                      <label class="col-sm-4">Temperament</label>
                     <input type="text" id="first-name" name="temperament" value="<?php echo set_value('temperament'); ?>" required="required" class="form-control">
                          </div>
                        </div>
                <span style="color:red;"><?php echo form_error('temperament'); ?></span>
                
                <div class="col-md-4">             
                      <div class="form-group">
                      <label class="col-sm-5">Life Span Min.</label>
                     <input  type="number" id="" name="life_span_1" step="any" value="<?php echo set_value('life_span_1');?>" required="required" class="form-control">
                          </div>
                        </div><span style="color:red;"><?php echo form_error('life_span_1'); ?></span>

                       <div class="col-md-4">             
                      <div class="form-group">
                      <label class="col-sm-4" style="width:150px">Life Span Max.</label>
                    <input  type="number" id="" name="life_span_2" step="any" value="<?php echo set_value('life_span_2');?>" required="required" class="form-control">
                          </div>
                        </div>

                        <div class="col-md-4">             
                      <div class="form-group">
                      <label class="col-sm-4" style="width:150px">Weight Male Min.</label>
                    <input   type="number" id="" name="min_weight_male" step="any" value="<?php echo set_value('min_weight_male');?>" required="required" class="form-control">
                          </div>
                        </div>

                        <div class="col-md-4">             
                      <div class="form-group">
                      <label class="col-sm-4" style="width:150px">Weight Male Max.</label>
                    <input type="number" id="" name="max_weight_male" step="any" value="<?php echo set_value('max_weight_male');?>" required="required" class="form-control">
                          </div>
                        </div>
                    
                     <div class="col-md-4">             
                      <div class="form-group">
                      <label class="col-sm-4" style="width:170px">Weight Female Min.</label>
                     <input  type="number" id="" name="min_weight_female" step="any" value="<?php echo set_value('min_weight_female');?>" required="required" class="form-control">
                          </div>
                        </div>
                        
                        <div class="col-md-4">             
                      <div class="form-group">
                      <label class="col-sm-4" style="width:170px">Weight Female Max.</label>
                     <input type="number" id="" name="max_weight_female" step="any" value="<?php echo set_value('max_weight_female');?>" required="required" class="form-control">
                          </div>
                        </div>
                       
                       <div class="col-md-4">             
                      <div class="form-group">
                      <label class="col-sm-4" style="width:150px">Height Male Min.</label>
                     <input type="number" id="" name="min_height_male" step="any"  value="<?php echo set_value('min_height_male');?>" required="required" class="form-control">
                          </div>
                        </div>        
                      <div class="col-md-4">             
                      <div class="form-group">
                      <label class="col-sm-4" style="width:150px">Height Male Max.</label>
                     <input   type="number" id="" name="max_height_male" step="any" value="<?php echo set_value('max_height_male');?>" required="required" class="form-control">
                          </div>
                        </div>       
              
                      <div class="col-md-4">             
                      <div class="form-group">
                      <label class="col-sm-4" style="width:170px">Height Female Min.</label>
                     <input type="number" id="" name="min_height_female" step="any" value="<?php echo set_value('min_height_female');?>" required="required" class="form-control">
                          </div>
                        </div> 
                 <div class="col-md-4">             
                      <div class="form-group">
                      <label class="col-sm-4" style="width:170px">Height Female Max.</label>
                    <input  type="number" id="" name="max_height_female" step="any" value="<?php echo set_value('max_height_female');?>" required="required" class="form-control">
                          </div>
                        </div>
               
                    <div class="col-md-4">             
                      <div class="form-group">
                      <label class="col-sm-4" style="width:150px">Manual Activity</label>
                    <input  type="number" step="any" id="first-name" name="manual_act" value="<?php echo set_value('manual_act');?>" required="required" class="form-control">
                          </div>
                        </div>

                    <div class="col-md-4">             
                      <div class="form-group">
                      <label class="col-sm-4" style="width:150px">Target</label>
                    <input  type="number" step="any"id="first-name" name="target" value="<?php echo set_value('target');?>" required="required" class="form-control">
                          </div>
                        </div>

                         <div class="col-md-4">             
                      <div class="form-group">
                      <label class="col-sm-4" id="b" style="width: 150px;">Breed Category</label>
                     <input type="text" id="first-name" name="breed_cat" value="<?php echo set_value('breed_cat');?>" required="required" class="form-control">
                          </div>
                        </div>
                  <span style="color:red;"><?php echo form_error('breed_cat'); ?></span>
                    
                    <div class="col-md-4">             
                      <div class="form-group">
                      <label class="col-sm-4">Remark</label>
                     <input  type="text" id="first-name" name="remark" value="<?php echo set_value('remark');?>" required="required" class="form-control">
                          </div>
                        </div>
                     <span style="color:red;"><?php echo form_error('remark'); ?></span>
                   
                   <div class="col-md-4">             
                      <div class="form-group">
                      <label class="col-sm-4">Breed Image</label>
                      <input type="file" id="first-name" name="image_path" required="required" class="form-control " style ="padding-bottom: 50px;">
                      <span>Image size should be 255x255 pixles</span> 

                    </div>
                    </div>
                <div class="ln_solid"></div>
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
