<!-- partial -->
<div class="main-panel">
  <div class="content-wrapper">
    <div class="row">
  
    <div class="col-lg-12 grid-margin stretch-card">
      <div class="card">
        <div class="card-body">
          <h4 class="card-title">Invoice of <?php echo $artist_name; ?></h4>
 
          <table id="example" class="table table-striped">
            <thead>
              <tr>
                <th>User Name</th>
                <th>Booking Date</th>
                <th>Working Min</th>
                <th>Total Amount</th>
                <th>Status</th>
              </tr>
              </thead>
              <tbody>
              <?php foreach ($get_invoice as $get_invoice){ ?>
              <tr>
                <td class="py-1">
                <?php echo $get_invoice->userName; ?>
                </td>
                <td>
                <?php echo date('M d, Y H:i', $get_invoice->created_at); ?>
                </td>
                <td>
                <?php echo $get_invoice->working_min; ?>
                </td>
                <td>
                <?php echo $get_invoice->total_amount; ?>&nbsp;<?php echo $get_invoice->currency_type; ?>
                </td>
                <td>
                <?php if($get_invoice->flag==0){ ?>
                <label class="badge badge-warning">Pending</label>
                <?php }elseif($get_invoice->flag==1){ ?>
                <label class="badge badge-primary">Paid</label>
                <?php } ?>
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
<!-- content-wrapper ends -->
