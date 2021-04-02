<div id="page-wrapper">
            <div class="container-fluid">
               
       <div class="row">
                    <div class="col-sm-12">
                        <div class="white-box">
                            <h3 class="box-title m-b-0">Currency setting</h3>
                         <div class="row">
              <div class="col-md-12 ">
                <div class="x_panel">
                  <div class="x_content">
                    <br />
                    <?php $attributes = array('id' => 'demo-form2', 'name'=>'randform','class'=>'form-horizontal form-label-left');
                            echo form_open_multipart('Currency/currency_setting', $attributes); ?>
              
                    <div class="col-sm-5">
                        <div class="form-group">
                          <label class="col-sm-6">Currency Type</label>
                        <select class="form-control" name="currency">
                      <?php foreach ($currency_setting as $currency_setting) { ?> 
                      <option value="<?php echo $currency_setting->id; ?>"<?php if ($currency_setting->status == 1) echo ' selected="selected"'; ?>><?php echo $currency_setting->currency_name; ?> (<?php echo $currency_setting->currency_symbol; ?>)</option>
                      <?php } ?>
                    </select>
                        </div>
                        </div>
                         <div class="col-sm-2">
                        <div class="form-group">
                        </div>
                        </div>

                    <div class="col-md-5" style="margin-top: 23px;">
                    <div class="ln_solid"></div>
                    <div class="form-group">
                    
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

         

