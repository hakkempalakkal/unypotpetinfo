  <div id="page-wrapper">
            <div class="container-fluid">
               
       <div class="row">
                    <div class="col-sm-12">
                        <div class="white-box">
                            <h3 class="box-title m-b-0">Edit Brand</h3>
                         <div class="row">
              <div class="col-md-12 col-sm-12 col-xs-12">
                <div class="x_panel">
                  <div class="x_content">
                    <br />
                    <?php $attributes = array('id' => 'demo-form2', 'name'=>'randform','class'=>'form-horizontal form-label-left');
                            echo form_open_multipart('admin/update_company', $attributes); ?>
                               <div class="col-md-12">
                         <div class="form-group">
                        <label class="col-sm-4">Select Category</label>
                         <select id="heard" class="form-control" onchange="getSubCat(this.value)" name="cat_id" required>

                <?php foreach ($pet_type1 as $pet_type1) { ?>

                  <option value="<?php echo $pet_type1->id; ?>" <?php if($food_company->cat_id==$pet_type1->id) { echo "selected"; } ?>><?php echo $pet_type1->pet_name; ?></option> 
                      <!--   <option value="<?php echo $pet_type1->id; ?>"><?php echo $pet_type1->pet_name; ?></option> -->
                        <?php } ?>
                        </select>
                        </div>
                        </div>
                         <div class="col-md-12">
                          <div class="form-group">
                        <label class="col-sm-4">Select Sub Category</label>
                         <select id="pro_id" class="form-control" name="sub_cat_id" required>

                <?php foreach ($pro_Cat as $pet_type) { ?>

<option value="<?php echo $pet_type->id; ?>" <?php if($food_company->sub_cat_id==$pet_type->id) { echo "selected"; } ?>><?php echo $pet_type->cat_title; ?></option> 
                       <!--  <option value="<?php echo $pet_type->id; ?>"><?php echo $pet_type->pet_name; ?></option> -->
                        <?php } ?>
                        </select>
                        </div>
                        </div>
                        <div class="col-sm-12">
                        <div class="form-group">
                          <label class="col-sm-4">Brand Name</label>

                          <input  type="hidden" id="first-name" name="company_id" value="<?php echo $food_company->c_id; ?>" class="form-control">

                        <input  type="text" id="first-name" name="company_name" value="<?php echo $food_company->c_name; ?>" class="form-control" style ="height: 50px;">
                          </div>
                        </div>
                        <br/>

                        <div class="col-sm-12">
                        <div class="form-group">
                          <label class="col-sm-4">Brand Description</label>

                          <input  type="hidden" id="first-name" name="company_id" value="<?php echo $food_company->c_id; ?>" class="form-control">

                        <input  type="text" id="first-name" name="desc" value="<?php echo $food_company->c_desc; ?>" class="form-control" style ="height: 50px;">
                          </div>
                        </div>

         
                 <div class="col-sm-12">             
                      <div class="form-group">
                      <label class="col-sm-4">Brand Image</label>
                    <input type="file" id="first-name" name="img_path" class="form-control " style ="padding-bottom: 50px;">

                          </div>
                        </div>

  <img style="width: 80px; height: 80px;" src="<?php echo  base_url().$food_company->c_img_path; ?>" width="100" height="100" alt="image"/>
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
       <script type="text/javascript">
function getSubCat(val)
{
var xhttp = new XMLHttpRequest();
xhttp.onreadystatechange = function() {

if (this.readyState == 4 && this.status == 200) {
 // alert(this.responseText);
document.getElementById("pro_id").innerHTML = this.responseText;
 
}
};

xhttp.open("GET", "http://peteats.in/Admin/Admin/addFood_company/"+val, true);
// alert('ssss')
xhttp.send();
}

</script>