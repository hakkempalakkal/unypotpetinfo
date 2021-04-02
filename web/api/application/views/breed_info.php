 <div id="page-wrapper">
            <div class="container-fluid">
               
             <div class="row">
                    <div class="col-sm-12">
                        <div class="white-box">
                            <h3 class="box-title m-b-0">Add Breed Information</h3>
                            <div class="row">
                    <div class="col-md-12 col-sm-12 col-xs-12">
                      <div class="x_panel">
                        <!-- <div>

<a class="dt-button buttons-print btn btn-primary m-r-10 " tabindex="0" aria-controls="example23" href="viewoffer"
  style="float: right;"><span>view offer</span></a><br/>
  
  </div>  -->
                        <div class="x_content"><br/>
                        <?php $attributes = array('id' => 'demo-form2', 'name'=>'randform','class'=>'form-horizontal form-label-left','method'=>'POST');
                            echo form_open_multipart('admin/add_breed_action', $attributes); ?>                     

                        <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12">Select Breed Name</label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                        <select id="heard" class="form-control" onchange="getBreedName(this.value)" name="breed_name" required>
                        <option value="#">--select--</option>
                        <?php foreach ($breed_name as $breed_name) { ?>
                        <option value="<?php echo $breed_name->id; ?>"><?php echo $breed_name->breed_name; ?></option>
                        <?php } ?> 
                        </select>
                        </div>
                        </div>
                           
                      <div class="col-sm-5" style="margin-right: 50px;">             
                      <div class="form-group">
                      <label class="col-sm-6">Breed Description</label>
                 <input  type="text" id="firsname" name="b_description" value="<?php echo set_value('b_description');?>" class="form-control" required style ="height: 57px;">
                      
                          </div>
                        </div>
                       

                       

                      <!--   <div class="form-group">
                      <label class="control-label col-md-3 col-sm-3 col-xs-12">Select Product 
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                      <select id="product_id" class="form-control" name="p_name" required>
                        <option value="#">--select--</option>
                            <?php foreach ($product_food as $product_food){ ?>
                            <option value="<?php echo $product_food->p_id; ?>"><?php echo $product_food->p_name; ?></option>
                            <?php } ?>  
                        </select>
                        </div>
                        </div> -->
                        


                       
                        <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name">Product Image <span class="required">*</span>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                        <input style="padding-bottom: 50px !important;" type="file" id="first-name" 
                        name="img_path" accept="image/*" multiple required="required" class="form-control col-md-7 col-xs-12" >
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
                  </form>
                    </div>
                  </div>
                </div>
              </div> 
              
             </div>
           </div>

<script type="text/javascript">
function getBreedName(val)
{
var xhttp = new XMLHttpRequest();
xhttp.onreadystatechange = function() {

if (this.readyState == 4 && this.status == 200) {
 // alert(this.responseText);
document.getElementById("").innerHTML = this.responseText;
 
}
};

xhttp.open("GET", "http://139.59.88.66/petEats/Admin/getBreedName/"+val, true);
 //alert('ssss')
xhttp.send();
}


// <script type="text/javascript">
// function getProduct1(val)
// {
//     // alert(val);
// var xhttp = new XMLHttpRequest();
// xhttp.onreadystatechange = function() {

// if (this.readyState == 4 && this.status == 200) {
//  // alert(this.responseText);
// document.getElementById("product_id").innerHTML = this.responseText;
 
// }
// };

// xhttp.open("GET", "http://139.59.88.66/petEats/Admin/getProduct1/"+val, true);
//  //alert('ssss')
// xhttp.send();
// }

// </script>
