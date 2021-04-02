

<div id="page-wrapper">
            <div class="container-fluid">
               
       <div class="row">
                    <div class="col-sm-12">
                        <div class="white-box">
                            <h3 class="box-title m-b-0">Add Brand</h3>

                         <div class="row">
              <div class="col-md-12 col-sm-12 col-xs-12">
                <div class="x_panel">
                  <div class="x_content">
                    <br />
                    <?php $attributes = array('id' => 'demo-form2', 'name'=>'randform','class'=>'form-horizontal form-label-left');
                            echo form_open_multipart('admin/addFoodCompany', $attributes); ?>
                    
                     <div class="col-md-12">
                      <div class="col-sm-5" style="margin-right: 50px;">
                        <div class="form-group" >
                        <label for="middle-name" class="col-sm-4">Select Category</label>
                        <select id="" class="form-control" onchange="getCompany(this.value)" name="cat_id" required>
                         <option value="#">--select--</option>
                            <?php foreach ($pet_type as $pet_type) { ?>
                             <option value="<?php echo $pet_type->id; ?>"><?php echo $pet_type->pet_name; ?></option>
                        <?php } ?>
                        </select>
                        </div>
                      </div>


                         <div class="col-sm-5"style="margin-right: 50px;">

                        <div class="form-group">
                      <label for="middle-name" class="col-sm-6">Select Sub Category</label>
                        <select id="pro_id" class="form-control" name="sub_cat_id" required>
                         <option value="#">--select--</option>
                        
                        <?php foreach ($pro_Cat as $proCat){ ?> 
                        <option value="<?php echo $proCat->id; ?>"><?php echo $proCat->cat_title; ?></option> 
                    <?php } ?></select>
                        </div>
                       
                      </div>
                        <div class="col-sm-5" style="margin-right: 50px;">
                        <div class="form-group">
                          <label class="col-sm-4">Brand Name</label>
                        <input  type="text" id="first-name" name="company_name" value="<?php echo set_value('company_name');?>" class="form-control" required style ="height: 57px;">
                          </div>
                        </div>

                         <div class="col-sm-5" style="margin-right: 50px;">             
                      <div class="form-group">
                      <label class="col-sm-6">Brand Description</label>
                         <input  type="text" id="first-name" name="desc" value="<?php echo set_value('desc');?>" class="form-control" required style ="height: 57px;">
                      
                          </div>
                        </div>


                     <div class="col-sm-10" style="width: 960px;" >             
                      <div class="form-group">
                      <label class="col-sm-4">Brand Image</label>
                      <input type="file" id="first-name" name="img_path" class="form-control " required style
                      ="padding-bottom: 50px;">
                      <span>Image size should be 255x255 pixles</span> 

                        </div>
                        </div>



             
                <div class="col-sm-6">      
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




         


                 <div class="row">
                    <div class="col-md-12">
                        <div class="panel">
                            <div class="panel-heading">All Brands</div>
                            <div class="table-responsive">
  <table id="myTable" class=" table-bordered table-hover table-responsive" role="grid" width="100%">            
<!--                    <table class="table table-hover manage-u-table" id="">
 -->                        <thead>
                        <tr>
                        <th class="text-center">#</th>
                        <th class="text-center" style="width: 150px" >Brand</th>
                        <th class="text-center" style="width: 150px" >Category</th>
                        <th class="text-center" style="width: 150px" >Sub Category</th>
                        <th class="text-center" style="width: 500px" >Brand Description</th>
                        <th class="text-center">Brand Image</th>
                        <th class="text-center">Action</th>

                        </tr>
                        </thead>
                        <tbody>
                        <?php  $count=1; foreach ($company as $company){ ?>
                        <tr>
                        <td><?=  $count++; ?></td>
                        <td><?php echo $company->c_name;?></td>
                        <td><?php echo $company->pet_name;?></td>
                        <td><?php echo $company->cat_title;?></td>
                        <td><?php echo $company->c_desc;?></td>
                       <?php if($company->c_img_path){ ?>
                        <td>
                        <img style="border-radius: 50%;width: 40px;height: 40px;" src="<?php echo  base_url().$company->c_img_path; ?>" width="80" height="80" alt="image"/></td>
                        <?php }else{ ?>
                          <td>
                        <img style="border-radius: 50%;width: 40px;height: 40px;" src="<?php echo  base_url('/assets/images/faces-clipart/pet.jpg'); ?>" alt="image" width="80" height="80" />
                      </td>
                        <?php } ?>
                        <td><a href="<?php echo base_url(); ?>Admin/editFood/<?php echo $company->c_id; ?>" title="Edit" class="btn btn-info btn-outline btn-circle btn-lg m-r-5"><i class= "ti-pencil-alt"></i></a>


                        <a title="Delete" onclick="return confirm('Are you sure you want to delete this Food Brand?')" href="<?php echo base_url(); ?>Admin/deleteFood/<?php echo  $company->c_id; ?>" class="btn btn-info btn-outline btn-circle btn-lg m-r-5"><i class="icon-trash"></i></a> 

                        </td>
                        </tr>
                 
                        <?php } ?>  
             
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>

              </div>
            </div>


 <script type="text/javascript">
function getCompany(val)
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