 <div id="page-wrapper">
            <div class="container-fluid">
               
             <div class="row">
                    <div class="col-sm-12">
                        <div class="white-box">
                            <h3 class="box-title m-b-0">Add Offer</h3>
                            <div class="row">
                    <div class="col-md-12 col-sm-12 col-xs-12">
                      <div class="x_panel">
                        <!-- <div>

<a class="dt-button buttons-print btn btn-primary m-r-10 " tabindex="0" aria-controls="example23" href="viewoffer"
  style="float: right;"><span>view offer</span></a><br/>
  
  </div>  -->
                        <div class="x_content"><br/>
                        <?php $attributes = array('id' => 'demo-form2', 'name'=>'randform','class'=>'form-horizontal form-label-left','method'=>'POST');
                            echo form_open_multipart('admin/add_offer_action', $attributes); ?>                     




                        <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12">Select Category</label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                        <select id="heard" class="form-control" onchange="getSubCat1(this.value)" name="p_type" required>
                        <option value="#">--select--</option>
                        <?php foreach ($pet_type as $pet_type) { ?>
                        <option value="<?php echo $pet_type->id; ?>"><?php echo $pet_type->pet_name; ?></option>
                        <?php } ?> 
                        </select>
                        </div>
                        </div>

                        <div class="form-group">
                        <label for="middle-name" class="control-label col-md-3 col-sm-3 col-xs-12">Select Sub Category</label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                        <select id="sub_id" class="form-control"  onchange="getBrand1(this.value)" name="p_pet_type" required>
                         <option value="#">--select--</option>
                           <?php foreach ($pro_cat as $proCat){ ?>
                        <option value="<?php echo $proCat->id; ?>"><?php echo $proCat->cat_title; ?></option>
                        <?php } ?> 
                        </select>
                        </div> 
                        </div>

                        <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12">Select Brand</label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                        <select id="brand_id" class="form-control" onchange="getProduct1(this.value)" name="c_id" required>
                        <option value="#">--select--</option>
                        <?php foreach ($company_food as $company_food){ ?>
                        <option value="<?php echo $company_food->c_id; ?>"><?php echo $company_food->c_name; ?></option>
                        <?php } ?> 
                        </select>
                        </div>
                        </div>

                        <div class="form-group">
                      <label class="control-label col-md-3 col-sm-3 col-xs-12">Select Product 
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                      <select id="product_id" class="form-control" name="p_name" required>
                        <option value="#">--select--</option>
                          <!--   < <?php foreach ($product_food as $product_food){ ?>
                            <option value="<?php echo $product_food->p_id; ?>"><?php echo $product_food->p_name; ?></option>
                            <?php } ?>  -->
                        </select>
                        </div>
                        </div>
                        


                         <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name" min="0">Discount(%) <span class="required">*</span>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                        <div class="input-group">
                        <div class="input-group-addon"><i class="ti-cut"></i></div>
                        <input onkeyup="return Calculate(event)" style="max-width: 100%" type="number" step="any" id="discount" name="discount" value="<?php echo set_value('discount');?>" required="required" class=" calc form-control col-md-7 col-xs-12" min="0">
                        </div>
                        </div>
                       </div>
                       <span style="color:red;"><?php echo form_error('discount'); ?></span>
                       

                        <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name">Start Date<span class="required">*</span>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                        <input  style="max-width: 100%" type="date" id="first-name" name="s_date" value="<?php echo set_value('start_date');?>" required="required" class="form-control col-md-7 col-xs-12">
                        </div>
                        </div>
                        <span style="color:red;"><?php echo form_error('start_date'); ?></span>

                         <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name">End Date<span class="required">*</span>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                    <input  style="max-width: 100%" type="date" id="first-name" name="end_date" value="<?php echo set_value('end_date');?>" required="required" class="form-control col-md-7 col-xs-12">
                        </div>
                        </div>
                        <span style="color:red;"><?php echo form_error('end_date'); ?></span>

                        <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name">Product Image <span class="required">*</span>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                        <input style="padding-bottom: 50px !important;" type="file" id="first-name" 
                        name="img_path" accept="image/*" multiple required="required" class="form-control col-md-7 col-xs-12" >
                        <span>Image size should be 350x180 pixles</span> 
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
                  </form>
                    </div>
                  </div>
                </div>
              </div> 
              
             </div>
           </div>

<script type="text/javascript">
function getSubCat1(val)
{
var xhttp = new XMLHttpRequest();
xhttp.onreadystatechange = function() {

if (this.readyState == 4 && this.status == 200) {
 // alert(this.responseText);
document.getElementById("sub_id").innerHTML = this.responseText;
 
}
};

xhttp.open("GET", "http://peteats.in/Admin/Admin/getSubCat1/"+val, true);
 //alert('ssss')
xhttp.send();
}

</script>
<script type="text/javascript">
function getBrand1(val)
{
var xhttp = new XMLHttpRequest();
xhttp.onreadystatechange = function() {

if (this.readyState == 4 && this.status == 200) {
 // alert(this.responseText);
document.getElementById("brand_id").innerHTML = this.responseText;
 
}
};

xhttp.open("GET", "http://peteats.in/Admin/Admin/getBrand1/"+val, true);
 //alert('ssss')
xhttp.send();
}

</script>

<script type="text/javascript">
function getProduct1(val)
{
    // alert(val);
var xhttp = new XMLHttpRequest();
xhttp.onreadystatechange = function() {

if (this.readyState == 4 && this.status == 200) {
 // alert(this.responseText);
document.getElementById("product_id").innerHTML = this.responseText;
 
}
};

xhttp.open("GET", "http://peteats.in/Admin/Admin/getProduct1/"+val, true);
 //alert('ssss')
xhttp.send();
}

</script>
