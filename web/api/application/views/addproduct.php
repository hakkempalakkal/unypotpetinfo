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
                            <h3 class="box-title m-b-0">Add Product</h3>
                            <div class="row">
                    <div class="col-md-12 col-sm-12 col-xs-12">
                      <div class="x_panel">
                        <div class="x_content"><br/>
                        <?php $attributes = array('id' => 'demo-form2', 'name'=>'randform','class'=>'form-horizontal form-label-left','method'=>'POST');
                            echo form_open_multipart('admin/add_product', $attributes); ?> 
                        <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name">Product Code <span class="required">*</span>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                        <input type="button" class="btn btn-success" value="Generate Product Code" onClick="randomString();">
                        <input type="text" name="randomfield" required="" class="form-control col-md-7 col-xs-12" value="<?php echo set_value('randomfield');?>" >
                        </div>
                        </div>
                        <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name">Product Name <span class="required">*</span>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                        <input  style="max-width: 100%" type="text" id="first-name" name="p_name" value="<?php echo set_value('p_name');?>" required="required" class="form-control col-md-7 col-xs-12">
                        </div>
                        </div>
                        <span style="color:red;"><?php echo form_error('p_name'); ?></span>
                      
                        <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="last-name">Product Description <span class="required">*</span>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                        <textarea id="message" required="required" class="form-control" name="p_description" data-parsley-trigger="keyup" data-parsley-minlength="20" data-parsley-maxlength="100" data-parsley-minlength-message="Come on! You need to enter at least a 20 caracters long comment.."
                        data-parsley-validation-threshold="10"><?php echo set_value('p_description');?></textarea>
                        </div>
                        </div>
                        <span style="color:red;"><?php echo form_error('p_description'); ?></span>

                        
               
                        <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12">Select Category</label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                        <select id="heard" class="form-control" onchange="getSubCat(this.value)" name="p_type" required>
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
                        <select id="sub_id" class="form-control"  onchange="getBrand(this.value)" name="p_pet_type" required>
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
                        <select id="brand_id" class="form-control" name="brand" required>
                        <option value="#">--select--</option>
                        <?php foreach ($company_food as $company_food){ ?>
                        <option value="<?php echo $company_food->c_id; ?>"><?php echo $company_food->c_name; ?></option>
                        <?php } ?>
                        </select>
                        </div>
                        </div>
          

                        <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12">Product Type <span class="required">*</span>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                        <select id="heard" class="form-control" name="p_cat_type" required>
                          <option value="#">--select--</option>
                          <option value="New/Fresh">New/Fresh</option>
                          <option value="Popular">Popular</option>
                          <option value="General">General</option>
                        </select>
                        </div>
                        </div>
                      
                        <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name" min="0">MRP <span class="required">*</span>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                        <input type="number" step="any" id="rate" name="p_rate" value="<?php echo set_value('p_rate');?>" required="required" style="max-width: 100%" class=" calc form-control col-md-7 col-xs-12" min="0">
                        </div>
                        </div><span style="color:red;"><?php echo form_error('p_rate'); ?></span>
                     
                        <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name" min="0">Selling Price <span class="required">*</span>
                          </label>
                            <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="number" step="any" id="sell_rate" name="selling_price" value="<?php echo set_value('selling_price');?>" required="required" style="max-width: 100%" onkeyup="return Calculate(event)" class=" calc form-control col-md-7 col-xs-12" min="0">
                        </div>
                        </div><span style="color:red;"><?php echo form_error('selling_price'); ?></span>
                      
                        <div class="form-group">
                        <label class="calc control-label col-md-3 col-sm-3 col-xs-12" for="first-name" min="0">Quantity <span class="required">*</span>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                        <input onkeyup="return Calculate(event)" style="max-width: 100%" type="number" step="any" id="quantity" name="quantity" value="<?php echo set_value('quantity');?>" required="required" class="form-control col-md-7 col-xs-12" min="0">
                        </div>
                        </div>
                        <span style="color:red;"><?php echo form_error('quantity'); ?></span>
                      
                        <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name" min="0">Gross Amount <span class="required">*</span>
                          </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                        <input  readonly="readonly" style="max-width: 100%" type="text" id="gross_amt" name="gross_amt" value="<?php echo set_value('gross_amt');?>" required="required" class="form-control col-md-7 col-xs-12">
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
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name">Net Amount <span class="required">*</span>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                        <input readonly="readonly" style="max-width: 100%" type="text" id="net_amt" name="net_amt" value="<?php echo set_value('net_amt');?>" required="required" class="form-control col-md-7 col-xs-12" min="0">
                        </div>
                        </div>
                        <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name">Shipping Cost <span class="required">*</span>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                        <input  style="max-width: 60%" type="text" id="" name="shipping_cost" value="<?php echo set_value('shipping_cost');?>" required="required" class="form-control col-md-7 col-xs-12" onkeyup="this.value=this.value.replace(/[^0-9.]/g,'');" min="0">
                        <input style="max-width: 40%" readonly="" type="text" id="" name=""  class="form-control col-md-7 col-xs-12" value="INR" min="0" >
                        <input type="checkbox"  name="mandatory" value="1" /> Mandatory        
                        </div>
                        </div>

                        <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12">Country<span class="required">*</span>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">

                        <select id="heard" class="form-control" name="country" required>
<!--                          <option value="#">--Select--</option>   
 -->                        <?php 
                         if(count($country)>0)
                         {
                         foreach ($country as $coun)
                         {
                          
                        ?>
                          <option value="<?php echo $coun->iso; ?>"><?php echo $coun->name; ?></option>
                        <?php } } ?> 
                        </select>
                        </div>
                        </div>
                         

                        <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name">Color 
                        </label>
                        <div class="row color_group">
                            <ul>
                        
                        <?php 
                         if(count($attributeColor)>0)
                         {
                         foreach ($attributeColor as $attrColor)
                         {
                          
                        ?>
                        <li><?php echo $attrColor->value;?></li>
                        <li>
                        <input   type="checkbox" id="" name="color[]" value="<?php echo $attrColor->value;?>" class="">
                        </li>
                         <?php } } ?>
                         </ul>

                         </div>
                       
                        </div>

                        <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name">Size 
                        </label>
                        <div class="row size_group">
                            <ul>
                        <?php 
                         if(count($attributeSize)>0)
                         {
                         foreach ($attributeSize as $attrSize)
                         {
                          
                        ?>
                         <li><?php echo $attrSize->value;?></li>
                        <li>
                        <input type="radio" id="" name="size[]" value="<?php echo $attrSize->value;?>" class="">
                        </li>
                    <?php } } ?>
                        </ul>

                         </div>
                        </div>
                    
                       
                        <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name">Weight
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                        <input  style="max-width: 100%" type="text" id="first-name" name="weight" value="<?php echo set_value('weight');?>" class="form-control col-md-7 col-xs-12">
                        </div>
                        </div>
                        <span style="color:red;"><?php echo form_error('weight'); ?></span>
                       

              <!--       <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name">Shipping Cost <span class="required">*</span>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                        <input  style="max-width: 60%" type="text" id="ship_cost" name="shipping_cost" value="<?php echo set_value('shipping_cost');?>" required="required" class="form-control col-md-7 col-xs-12" onkeyup="this.value=this.value.replace(/[^0-9.]/g,'');" min="0">
                        <input style="max-width: 40%" readonly="" type="text" id="" name=""  class="form-control col-md-7 col-xs-12" value="INR" min="0" >
                        </div>
                        </div>
 -->
 <!-- <div class="form-group">
                  <label for="store">Warehouse</label>
                  <select class="form-control select_group" id="store" name="store">
                    <?php foreach ($stores as $k => $v): ?>
                      <option value="<?php echo $v['id'] ?>"><?php echo $v['name'] ?></option>
                    <?php endforeach ?>
                  </select>
                </div>

                <div class="form-group">
                  <label for="store">Availability</label>
                  <select class="form-control" id="availability" name="availability">
                    <option value="1">Yes</option>
                    <option value="2">No</option>
                  </select>
                </div> -->

                       
                        <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name">Product Image <span class="required">*</span>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                        <input style="padding-bottom: 50px !important;" type="file" id="first-name" 
                        name="files[]" accept="image/*" multiple required="required" class="form-control col-md-7 col-xs-12" >
                        <span>Image size should be 225x225 pixles</span> 
                        </div>
                        </div>



                        
                        <div class="ln_solid"></div>
                        <div class="form-group">
                        <div class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
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
        <!-- <div class="container hide">
        <br />
        <h3 align="center">How to Import Excel Data into Mysql in Codeigniter</h3>
        <form method="post" id="import_form" enctype="multipart/form-data" action="<?php echo base_url(); ?>Admin/importProduct">
        <p><label>Select Excel File</label>
        <input type="file" name="file" id="file" required accept=".xls, .xlsx" /></p>
        <br />
        <input type="submit" name="import" value="Import" class="btn btn-info" onclick="impotrFile();" />
        </form>
        <br />
        <div class="table-responsive" id="customer_data">

        </div>
        </div> -->
          <script src="https://code.jquery.com/jquery-1.10.2.js"></script>
<!-- 
<script>
$(document).ready(function(){
  $(".ss").click(function(){
   
    $( "#selected" ).show("fast");
  });
});


</script>

 <script>
$(document).ready(function(){
  $(".pp").click(function(){
   
    $( "#selected1" ).show("fast");
  });
});
</script>   -->

<script>
$(document).ready(function(){
  $("#ship_cost").click(function(){

    var rat=$("#rate").val();
 if(rat<500){
    $("#ship_cost").val("50")
 }else if(rat>500||rat<1000){
    $("#ship_cost").val("Free")
 }else{
 $("#ship_cost").val("100")
 }
 });
});
</script>

<script language="javascript" type="text/javascript">
 
function randomString() {
  var chars = "0123456789";
  var string_length = 12;
  var randomstring = '';
  for (var i=0; i<string_length; i++) {
    var rnum = Math.floor(Math.random() * chars.length);
    randomstring += chars.substring(rnum,rnum+1);
  }
  document.randform.randomfield.value = +randomstring;
}

// function Calculate(evt)
// {
   
// var rate= $('#sell_rate').val();
// var quantity = $('#quantity').val();
// var gross_amt= rate* quantity;
//  $('#gross_amt').val(gross_amt);
//  var discount = $('#discount').val();
// var diss_amt= gross_amt*discount/100;
// var net_amt= gross_amt-diss_amt;
//  $('#net_amt').val(net_amt);

//  // var p_rate=('#p_rate').val();
//  // var shipping_cost=('#shipping_costS').val();
//  // var price = net_amt+shipping_cost;
//  // $('price').val(price);
//  var charCode = (evt.which) ? evt.which : evt.keyCode;
//           if (charCode != 46 && charCode > 31 
//             && (charCode < 48 || charCode > 57))
//              return false;
 
// }


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

 // var p_rate=('#p_rate').val();
 // var shipping_cost=('#shipping_costS').val();
 // var price = net_amt+shipping_cost;
 // $('price').val(price);
 var charCode = (evt.which) ? evt.which : evt.keyCode;
          if (charCode != 46 && charCode > 31 
            && (charCode < 48 || charCode > 57))
             return false;
 
}

// function impotrFile(){
//  //$('#import_form').on('submit', function(event){
//   //event.preventDefault();
//   $.ajax({
//    url:"<?php echo base_url(); ?>Admin/import",
//    method:"POST",
//    data:new FormData(this),
//    contentType:false,
//    cache:false,
//    processData:false,
//    success:function(data){
//     $('#file').val('');
//     load_data();
//     alert(data);
//    }
//   })
//  // });
// }

 
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