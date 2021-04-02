
<div id="page-wrapper">
            <div class="container-fluid">
               
<?php if ($this->session->flashdata('msg1')) { ?>
      <div id="mydiv" class="alert alert-success"><?= $this->session->flashdata('category_success') ?>
         Added successfully.
      </div>
     
  <?php } ?>
       <div class="row">
            <div class="col-md-12">
              <div class="white-box">
                <h3 class="box-title m-b-0">Add</h3>
                <br/>
               
                <div class="row">
                  <div class="col-md-12 col-sm-12 col-xs-12">
                    <div class="x_panel">
                      <div class="x_content">
                         <?php $attributes = array('class'=>'form-horizontal form-label-left','method'=>'POST');
                            echo form_open_multipart('admin/addVss', $attributes); ?>

                        <div class="col-sm-12">
                        <div class="form-group">
                        <label class="col-sm-12">Select Category</label>

                        <select required id="heard" class="form-control" name="nearby_id">

                           <option value="">---Select Category---</option>
                        <?php foreach ($get_vss as $get_vss){ ?>
                       
                        <option value="<?php echo $get_vss->nearby_id; ?>" <?php if($get_vss->nearby_id==1){ ?> style="display: none;" <?php } ?>><?php echo $get_vss->nearby_type_name; ?></option>
                        <?php } ?>
                        </select>
                        </div>
                        </div>

                        <div class="col-md-4">
                        <div class="form-group">
                          <label class="col-sm-3">Name</label>
                        <input  type="text" id="nearby_id" name="name" value="<?php echo set_value('name');?>" required="required" class="form-control">
                          </div>
                        </div>
                
                      <div class="col-md-4">
                      <div class="form-group">
                      <label class="col-sm-3">Email</label>
                      <input type="text" id="nearby_id" name="email" value="<?php echo set_value('email');?>" required="required" class="form-control">
                      </div>
                      </div>

                    <div class="col-md-4">             
                    <div class="form-group">
                     
                     <label class="col-sm-3" style="width:150px">Mobile No</label>
                    <input  type="number" step="any" id="mobile"   onkeypress="phoneno()" minlength="10" maxlength="10" pattern="\d{3}[\-]\d{3}[\-]\d{4}" name="mobile" value="<?php echo set_value('mobile');?>" required="required" class="form-control">
                    </div>
                    </div>
                       
           

                       <div class="col-md-4">
                       <div class="form-group">
                      <label class="m-t-20">Opening Time</label>
                       <div class="input-group clockpicker">
                      <input type="text"  id="nearby_id" name="start_timing" class="form-control" value="" required="required" readonly=""> 
                      <span class="input-group-addon"> <span class="glyphicon glyphicon-time"></span> 
                      </div>
                      </div>
                      </div>


                       <div class="col-md-4">
                       <div class="form-group">
                      <label class="m-t-20">Closing Time</label>
                       <div class="input-group clockpicker">
                      <input type="text"  id="nearby_id" name="end_timing" class="form-control" value=""  required="required" readonly=""> 
                     
                      <span class="input-group-addon"><span class="glyphicon glyphicon-time"></span> 
                      </div>
                      </div>
                      </div>
                    </div>


                       <div class="col-md-4">
                        <div class="form-group">
                        <label class="col-sm-3">Address</label>
                     <textarea id="nearby_id" name="address" value="<?php echo set_value('address');?>" required="required" class="form-control"></textarea>
                        </div>
                        </div>
                      </div>
                    </div>
                  



                     <div class="col-md-4">             
                      <div class="form-group">
                      <label class="col-sm-4">Image</label>
                      <input type="file" id="nearby_id" name="image_path" required="required" class="form-control" value="<?php echo set_value('image_path');?>" style="padding-bottom: 50px;">
                       <span>Image size should be 255x255 pixles</span> 
                      </div>
                      </div>

                    <!-- <div class="ln_solid"></div> -->
                    <div class="form-group">
                    <div class="col-md-12">
                    <button type="submit" class="btn btn-success">Submit</button>
                    </div>
                    </div>
                    </div>
                  </div>
              </div>
            </div> 

             </div>
           </div>





<script type="text/javascript">
  setTimeout(function() {
     $('#mydiv').fadeOut('fast');
  }, 5000); // <-- time in milliseconds
</script>

<script type="text/javascript">
            $(function () {
                $('#nearby_id').datetimepicker({
                    format: 'LT'
                });
            });
        </script>

        <script>     
           
           function phoneno(){          
            $('#mobile').keypress(function(e) {
                var a = [];
                var k = e.which;

                for (i = 48; i < 58; i++)
                    a.push(i);

                if (!(a.indexOf(k)>=0))
                    e.preventDefault();
            });
        }
      </script>
      