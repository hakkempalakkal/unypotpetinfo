 <!-- partial -->
      <div class="main-panel">
        <div class="content-wrapper">
          <div class="row">
            <div class="col-12 grid-margin">
              <div class="card">
                <div class="card-body">
                  <h4 class="card-title">Add Coupons</h4>
                   <?php $attributes = array('id' => 'form_validation','name'=>'add_coupon','class'=>'form-sample'); echo form_open_multipart('Admin/add_coupon', $attributes); ?>
                    <div class="row">
                    <div class="col-md-12">
                      <div class="col-md-8">
                        <div class="form-group row">
                          <label class="col-sm-3 col-form-label">Coupon Code</label>
                          <div class="col-sm-9">
                            <input type="text" name="coupon_code" class="form-control" required="" placeholder="Coupon Code" />
                          </div>
                        </div>
                      </div>
                      <div class="col-md-8">
                        <div class="form-group row">
                          <label class="col-sm-3 col-form-label">Discount Type</label>
                          <div class="col-sm-9">
                            <select class="form-control" name="discount_type">
                              <option value="1">Percentage (%)</option>
                              <option value="2">Flat Cost ($)</option>
                            </select>
                          </div>
                        </div>
                      </div>
                      <div class="col-md-8">
                        <div class="form-group row">
                            <label class="col-sm-3 col-form-label">Discount</label>
                            <div class="col-sm-9">
                            <input type="number" name="discount" class="form-control" required="" placeholder="Discount" />
                          </div>
                        </div>
                      </div>
                      <div class="col-md-8">
                        <div class="form-group row">
                            <label class="col-sm-3 col-form-label">Descripton</label>
                            <div class="col-sm-9">
                            <textarea class="form-control" name="description" required="" placeholder="Descripton"></textarea>
                          </div>
                        </div>
                      </div>
                      </div>
                    </div>
                    <button type="submit" class="btn btn-success mr-2">Submit</button>
                  </form>
                </div>
              </div>
            </div>
             <div class="col-lg-12 grid-margin stretch-card">
      <div class="card">
        <div class="card-body">
          <h4 class="card-title">All Coupons</h4>
 
          <table id="example" class="table table-striped">
            <thead>
              <tr>
                <th>
                  S. No
                </th>
                <th>
                 Code
                </th>
                <th>
                  Type
                </th>
                <th>
                  Discount
                </th>
                <th>
                  Descripton
                </th>
                <th>
                  Status
                </th>
                <th>
                  Action
                </th>
              </tr>
            </thead>
            <tbody>
            <?php
            $i=0;
             foreach ($coupon as $coupon) {
              $i++;
              ?>
              <tr>
                <td class="py-1">
                  <?php echo $i; ?>
                </td>
                <td>
                  <?php echo $coupon->coupon_code; ?>
                </td>
                <td>
                 <?php if($coupon->discount_type==1)
                 {
                   echo "%";
                   ?>
                 <?php
                  }
                  elseif($coupon->discount_type==2) {
                    echo "$";
                     ?>
                 <?php
                   } ?>
                </td>
                 <td>
                <?php echo $coupon->discount; ?>
                </td>
                <td>
                <?php echo $coupon->description; ?>
                </td>
                 <td>
                 <?php if($coupon->status==1)
                 {
                   ?>
                 <label class="badge badge-teal">Active</label>
                 <?php
                  }
                  elseif($coupon->status==0) {
                     ?>
                 <label class="badge badge-danger">Deactive</label>
                 <?php
                   } ?>
                </td>
                 <td>
                 <div class="btn-group dropdown">
                  <button type="button" class="btn btn-teal dropdown-toggle btn-sm" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    Manage
                  </button>
                  <div class="dropdown-menu">
                    <a class="dropdown-item" href="<?php echo base_url('/Admin/change_status_coupon');?>?id=<?php echo $coupon->id; ?>&status=1&request=1"><i class="fa fa-reply fa-fw"></i>Active</a>
                    <div class="dropdown-divider"></div>
                    <a class="dropdown-item" href="<?php echo base_url('/Admin/change_status_coupon');?>?id=<?php echo $coupon->id; ?>&status=0&request=1"><i class="fa fa-history fa-fw"></i>Deative</a>
                  </div>
                </div>
                </td>
              </tr>
            <?php
              }
            ?>      
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</div>