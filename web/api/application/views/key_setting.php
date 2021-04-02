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
   <?php if ($this->session->flashdata('msg')) { ?>
      <div id="mydiv" class="alert alert-success"><?= $this->session->flashdata('defsyh') ?>
         <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
         <strong>Key</strong> updated successfully.
      </div>
      <?php } ?>
<div class="row">
   <div class="col-sm-12">
      <div class="white-box">
         <h3 class="box-title m-b-0">Authentication Key </h3>
         <p class="text-muted m-b-30 font-13">please fill this field Properly </p>
         <div class="row">
            <div class="col-md-12 col-sm-12 col-xs-12">
               <div class="x_panel">
                  <div class="x_content">
                     <?php $attributes = array('id' => 'demo-form2', 'name'=>'randform','class'=>'form-horizontal form-label-left','method'=>'POST');
                        echo form_open_multipart('admin/KeySetting', $attributes); ?>
                     <div class="row">
                        <div class="col-md-12 col-sm-12 col-xs-12">
                           <div class="col-sm-12 col-xs-12">
                         
                              <div class="form-group">
                                 <label for="">Authentication Key</label>
                                 <input type="auth_key" id="auth_key" name="auth_key" class="form-control"  placeholder="Auth Key" value="<?php echo $auth_key->auth_key; ?>">
                              </div>
                            </div>
                             
                  
                     <div class="row">
                        <div class="col-md-6 col-sm-6 col-xs-12">
                           <button type="submit" class="btn btn-success waves-effect waves-light m-r-10">Submit</button>
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
</div>
  </div>
   </div>
 </div>
 <script type="text/javascript">
   function showProductInfo(id) {
       $("#" + id).modal("show");
   }
</script>


