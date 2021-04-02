<style type="text/css">
.color_group ul li {
    display: inline;
    margin-right: 10px;
}
.size_group ul li {
    display: inline;
    margin-right: 10px;
}
</style>
<div id="page-wrapper">
            <div class="container-fluid">
               
       <div class="row">
                    <div class="col-sm-12">
                        <div class="white-box">
                            <h3 class="box-title m-b-0">Edit Product</h3>
                            <div class="row">
                    <div class="col-md-12 col-sm-12 col-xs-12">
                      <div class="x_panel">
                        <div class="x_content"><br/>
                 
                    <?php $attributes = array('id' => 'demo-form2', 'name'=>'randform','class'=>'form-horizontal form-label-left');
                            echo form_open_multipart('Admin/update_product_action', $attributes); ?>
                   
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input  style="max-width: 100%" type="hidden" id="first-name" name="p_id" required="required" value="<?php echo $product->p_id; ?>" class="form-control col-md-7 col-xs-12">
                        </div>

                        <div class="form-group">
                        <label style="margin-left: -580px;" class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name">Product Name <span class="required">*</span>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input  style="max-width: 100%; margin-left: -290px;" type="text" id="first-name" name="p_name" required="required" value="<?php echo $product->p_name; ?>" class="form-control col-md-7 col-xs-12">
                        </div>
                      </div><span style="color:red;"><?php echo form_error('p_name'); ?></span>

                      
                      <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="last-name">Product Description <span class="required">*</span>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <textarea id="message" required="required" class="form-control" name="p_description" data-parsley-trigger="keyup" data-parsley-minlength="20" data-parsley-maxlength="100" data-parsley-minlength-message="Come on! You need to enter at least a 20 characters long comment.."
                            data-parsley-valida tion-threshold="10"><?php echo $product->p_description; ?></textarea>
                        </div>
                      </div><span style="color:red;"><?php echo form_error('p_description'); ?></span>
                       
                        <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12">Select Category</label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <select id="heard" class="form-control" onchange="getSubCat(this.value)" name="p_type" required="required">
                             <?php foreach ($pet_type as $pet_type) {
                             ?>
                              <option value="<?php echo $pet_type->id; ?>" <?php if($product->p_type == $pet_type->id) { echo 'selected'; } ?> ><?php echo $pet_type->pet_name; ?></option>
                             <?php
                            } ?>
                          </select>
                        </div>
                      </div>

                       <div class="form-group">
                        <label for="middle-name" class="control-label col-md-3 col-sm-3 col-xs-12">Select Sub Category</label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <select id="sub_id" class="form-control" onchange="getBrand(this.value)" name="p_pet_type" required="required">

                            <?php foreach ($pro_Cat as $pet_type){ ?>
                       <option value="<?php echo $pet_type->id; ?>" <?php if($product->p_pet_type==$pet_type->id) { echo "selected"; } ?>><?php echo $pet_type->cat_title; ?></option> 
                        <?php } ?>
                          </select>
                        </div>
                      </div>

                       <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12">Select Brand</label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <select id="brand_id" class="form-control" name="c_id" required="required">
                             <?php foreach ($company_food as $company_food) {
                             ?>
                             <option value="<?php echo $company_food->c_id; ?>" <?php if($product->c_id==$company_food->c_id) { echo "selected"; } ?>><?php echo $company_food->c_name; ?></option> 
                           <!--    <option value="<?php echo $company_food->c_name; ?>"<?php if($product->c_id == $company_food->c_id) { echo 'selected'; } ?> ><?php echo $company_food->c_name; ?></option> -->
                             <?php
                            } ?>
                          </select>
                        </div>
                      </div>
                      <!-- <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12">Select Category</label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                        <select id="heard" class="form-control" name="cat_id" required>
                        <?php foreach ($pro_cat as $proCat){ ?>
                        <option value="<?php echo $proCat->id; ?>"><?php echo $proCat->cat_title; ?></option>
                        <?php } ?>
                        </select>
                        </div>
                        </div> -->
                     
                      
                     
                      
                      <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12">Product Type <span class="required">*</span>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                         <select id="heard" class="form-control" name="p_cat_type" required="required">
                            <option value="New/Fresh" <?php if($product->p_cat_type == 'New/Fresh') { echo 'selected'; } ?>>New/Fresh</option>
                            <option value="Popular" <?php if($product->p_cat_type == 'Popular') { echo 'selected'; } ?>>Popular</option>
                    <option value="General" <?php if($product->p_cat_type == 'General') { echo 'selected'; } ?>>General</option>
                          </select>
                        </div>
                      </div>
                      
                      <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name">MRP <span class="required">*</span>
                          </label>
                            <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="number" step="any" id="rate" name="p_rate" required="required" value="<?php echo $product->p_rate; ?>" style="max-width: 100%" class=" calc form-control col-md-7 col-xs-12">
                        </div>
                      </div><span style="color:red;"><?php echo form_error('p_rate'); ?></span>
                      
                      <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name">Selling Price <span class="required">*</span>
                          </label>
                            <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="number" step="any" id="sell_rate" name="selling_price" value="<?php echo $product->p_rate_sale; ?>" required="required" style="max-width: 100%" onkeyup="return Calculate(event)" class=" calc form-control col-md-7 col-xs-12">
                        </div>
                      </div><span style="color:red;"><?php echo form_error('selling_price'); ?></span>
                      
                      <div class="form-group">
                        <label class="calc control-label col-md-3 col-sm-3 col-xs-12" for="first-name">Quantity <span class="required">*</span>
                          </label>
                            <div class="col-md-6 col-sm-6 col-xs-12">
                          <input onkeyup="return Calculate(event)" style="max-width: 100%" type="number" step="any" id="quantity" value="<?php echo $product->quantity; ?>" name="quantity" required="required" class="form-control col-md-7 col-xs-12">
                        </div>
                      </div><?php echo form_error('quantity'); ?>
                      
                      <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name">Gross Amount <span class="required">*</span>
                          </label>
                            <div class="col-md-6 col-sm-6 col-xs-12">
                          <input  readonly="readonly" style="max-width: 100%" value="<?php echo $product->gross_amt; ?>" type="text" id="gross_amt" name="gross_amt"  required="required" class="form-control col-md-7 col-xs-12">
                        </div>
                      </div>
                      
                      <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name">Discount(%) <span class="required">*</span>
                          </label>
                            <div class="col-md-6 col-sm-6 col-xs-12">
                          <input onkeyup="return Calculate(event)" value="<?php echo $product->discount; ?>" style="max-width: 100%" type="number" step="any" id="discount" name="discount" required="required" class=" calc form-control col-md-7 col-xs-12">
                        </div>
                      </div><?php echo form_error('discount'); ?>
                      
                      
                      <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name">Net Amount <span class="required">*</span>
                          </label>
                            <div class="col-md-6 col-sm-6 col-xs-12">
                          <input readonly="readonly" style="max-width: 100%" value="<?php echo $product->net_amt; ?>" type="text" id="net_amt" name="net_amt"  required="required" class="form-control col-md-7 col-xs-12">
                        </div>
                      </div>
                      <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name">Shipping Cost <span class="required">*</span>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                        <input  style="max-width: 60%" type="text" id="" name="shipping_cost" value="<?php echo $product->shipping_cost; ?>" required="required" class="form-control col-md-7 col-xs-12" onkeyup="this.value=this.value.replace(/[^0-9.]/g,'');">
                        <input style="max-width: 40%" readonly="" type="text" id="" name=""  class="form-control col-md-7 col-xs-12" value="INR" />
                        <input type="checkbox"  name="mandatory" value="1" <?php if($product->mandatory==1) { echo "checked"; } ?> /> Mandatory 
                        </div>
                        </div>
                      <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12">Country<span class="required">*</span>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                        <select id="heard" class="form-control" name="country" required="required">
                        <?php 
                         if(count($country)>0)
                         {
                         foreach ($country as $coun)
                         {
                          
                        ?>
                          
                          <option <?php if($product->country == $coun->iso){echo "selected";}?> value="<?php echo $coun->iso; ?>"><?php echo $coun->name; ?></option>
                        <?php } } ?>
                        </select>
                        </div>
                        </div>
                        
                        <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name">Color <span class="required">*</span>
                        </label>
                        <div class="row color_group">
                          <ul>
                        
                        <?php 
                          $colores = explode(',',$product->color);
                         if(count($attributeColor)>0)
                         {
                         foreach ($attributeColor as $attrColor)
                         {
                          
                        ?>
                        <li><?php echo $attrColor->value;?></li>
                        <li>
                        <input   type="checkbox" id="" <?php if (in_array($attrColor->value, $colores)) {echo "checked";}?>  name="color[]" value="<?php echo $attrColor->value;?>" class="">
                        </li>
                         <?php } } ?>
                         </ul>

                         </div>
                       
                        </div>

                        <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name">Size <span class="required">*</span>
                        </label>
                        <div class="row size_group">
                          <ul>
                        <?php
                        $sizes = explode(',',$product->size); 
                         if(count($attributeSize)>0)
                         {
                         foreach ($attributeSize as $attrSize) { ?>
                         <li><?php echo $attrSize->value;?></li>
                        <li>
                        <input type="radio" <?php if (in_array($attrSize->value, $sizes)) {echo "checked";}?> name="size[]" value="<?php echo $attrSize->value;?>" class="">
                        </li>
                    <?php } } ?>
                        </ul>

                         </div>
                        </div>

                     
                      
                        <div class="form-group">
                        <label  class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name">Weight
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="text" id="weight" name="weight" value="<?php echo $product->weight; ?>" class="form-control col-md-7 col-xs-12">
                        </div>
                      </div><span style="color:red;"><?php echo form_error('weight'); ?></span>



                 
                      
                      <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name">Product Image <span class="required">*</span>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input style="padding-bottom: 70px;" type="file" id="first-name" 
                        name="files[]" multiple  class="form-control col-md-7 col-xs-12">
                        </div>
                                      <?php $pro_images= $this->Api_model->getSingleRow('pro_images',array('p_id'=>$product->p_id));?>


                                  <img style="width: 100px;height: 79px;" src="<?= base_url(); ?><?php  echo $pro_images->image; ?>" alt="Image not Available"/>
                      </div>

        
                      <div class="ln_solid"></div>
                      <br>
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
    </div>   

 <!-- /page content -->
      
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
   
var rate= $('#sell_rate').val();
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
<script type="text/javascript">
function getSubCat(val)
{
var xhttp = new XMLHttpRequest();
xhttp.onreadystatechange = function() {

if (this.readyState == 4 && this.status == 200) {
 // alert(this.responseText);
document.getElementById("sub_id").innerHTML = this.responseText;
 
}
};

xhttp.open("GET", "http://peteats.in/Admin/Admin/getSubCat/"+val, true);
// alert('ssss')
xhttp.send();
}

</script>
<script type="text/javascript">
function getBrand(val)
{
var xhttp = new XMLHttpRequest();
xhttp.onreadystatechange = function() {

if (this.readyState == 4 && this.status == 200) {
 // alert(this.responseText);
document.getElementById("brand_id").innerHTML = this.responseText;
 
}
};

xhttp.open("GET", "http://peteats.in/Admin/Admin/getBrand/"+val, true);
// alert('ssss')
xhttp.send();
}

</script>