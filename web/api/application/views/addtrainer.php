
<div id="page-wrapper">
            <div class="container-fluid">
               
<?php if ($this->session->flashdata('msg1')) { ?>
      <div id="mydiv" class="alert alert-success"><?= $this->session->flashdata('category_success') ?>
         Added successfully.
      </div>
     
  <?php } ?>
       <div class="row">
            <div class="col-sm-12">
              <div class="white-box">
                <h3 class="box-title m-b-0">Add Trainer</h3>
                <br/>
               
                <div class="row">
                  <div class="col-md-12 col-sm-12 col-xs-12">
                    <div class="x_panel">
                      <div class="x_content">
                         <?php $attributes = array('class'=>'form-horizontal form-label-left','method'=>'POST');
                            echo form_open_multipart('admin/addtrainer', $attributes); ?>


                        <div class="col-sm-4" style="margin-right: 70px;">
                        <div class="form-group">
                          <label class="col-sm-4">Name</label>
                        <input  type="text" id="nearby_id" name="name" value="<?php echo set_value('name');?>" required="required" class="form-control">
                        <input  type="hidden" name="nearby_id" value="5" required="required" class="form-control">
                          </div>
                        </div>
          
                       <div class="col-sm-4" style="margin-right: 70px;">
                      <div class="form-group">
                      <label class="col-sm-4">Email</label>
                      <input type="text" id="nearby_id" name="email" value="<?php echo set_value('email');?>" required="required" class="form-control">
                      </div>
                      </div>

                     <div class="col-sm-4" style="margin-right: 70px;">             
                     <div class="form-group">
                     <label class="col-sm-4">Mobile</label>
                    <input  type="number" step="any" id="mobile" onkeypress="phoneno()" pattern="\d{3}[\-]\d{3}[\-]\d{4}" name="mobile" value="<?php echo set_value('mobile');?>" required="required" class="form-control">
                    </div>
                    </div>
                  

                  <!--   <div class="form-group">
                    <div class="col-sm-4">
                    <button type="button" class="selected">Morning(AM)</button>
                    </div>
                    </div>

                     <div class="col-sm-5" id="myDiv" style="margin-right: 50px;">
                      <div class="form-group">
                      <label class="m-t-20 ">Morning Opening Time</label>
                       <div class="input-group clockpicker">
                      <input type="text"  id="datetimepicker3" name="start_timing" class="form-control" value="" required="required" readonly=""> 
                      <span class="input-group-addon"> <span class="glyphicon glyphicon-time"></span> 
                      </div>
                      </div>
                      </div>


                       <div class="col-sm-5"  id="myDivv" style="margin-right: 50px;">
                       <div class="form-group">
                      <label class="m-t-20">Morning Closing Time</label>
                       <div class="input-group clockpicker">
                     <input type="text"  id="datetimepicker3" name="end_timing" class="form-control" value=""  required="required" readonly=""> 
                      <span class="input-group-addon"> <span class="glyphicon glyphicon-time"></span> 
                      </div>
                      </div>
                      </div>


                    <div class="form-group">
                    <div class="col-sm-4">
                    <button type="button" class="selected1">Evening(PM)</button>
                    </div>
                    </div>

          
                    
                       <div class="col-sm-5" id="myDiv1" style="margin-right: 50px;">
                      <div class="form-group">
                      <label class="m-t-20 ">Evening Opening Time</label>
                       <div class="input-group clockpicker">
                      <input type="text"  id="datetimepicker3" name="e_open_time" class="form-control" value="" required="required" readonly=""> 
                      <span class="input-group-addon"><span class="glyphicon glyphicon-time"></span> 
                      </div>
                      </div>
                      </div>


                       <div class="col-sm-5"  id="myDiv2" style="margin-right: 50px;">
                       <div class="form-group">
                      <label class="m-t-20">Evening Closing Time</label>
                      <div class="input-group clockpicker">
                      <input type="text"  id="datetimepicker3" name="e_close_time" class="form-control" value=""  required="required" readonly=""> 
                      <span class="input-group-addon"><span class="glyphicon glyphicon-time"></span> 
                      </div>
                      </div>
                      </div> -->

                        <div class="col-sm-4" style="margin-right: 70px;">
                        <div class="form-group">
                        <label class="col-sm-4">Address</label>
                     <textarea id="nearby_id" name="address" value="<?php echo set_value('address');?>" required="required" class="form-control"></textarea>
                        </div>
                        </div>
           
                
                  
                      <div class="col-sm-4">             
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


<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
<!-- 
<script>
  $(document).ready(function(){
$(".selected").click(function() {

  $("#myDiv").show(function(){
    $("#myDivv").show()

  });

});
});
 $(document).ready(function(){
$(".selected1").click(function() {

  $("#myDiv1").show(function(){
    $("#myDiv2").show()


  });

});
}); 
</script>
 -->



<script type="text/javascript">
  setTimeout(function() {
     $('#mydiv').fadeOut('fast');
  }, 5000); // <-- time in milliseconds
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
