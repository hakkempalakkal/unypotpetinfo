<!-- partial -->
<div class="main-panel">
<div class="content-wrapper">
  <div class="row">
  
    <div class="col-lg-12 grid-margin stretch-card">
      <div class="card">
        <div class="card-body">
          <h4 class="card-title">Appointments of <?php echo $artist_name; ?></h4>
 
          <table id="example" class="table table-striped">
            <thead>
              <tr>
                <th>
                  Customer Name
                </th>
                <th>
                  Timing
                </th>
                <th>
                  Appointment Date
                </th>
                <th>
                  Status
                </th>
              </tr>
            </thead>
            <tbody>
            <?php foreach ($get_appointments as $get_appointments) {
              ?>
              <tr>
                <td class="py-1">
                  <?php echo $get_appointments->name; ?>
                </td>
                <td>
                  <?php echo $get_appointments->booking_time; ?>
                </td>
                <td>
                   <?php echo $get_appointments->booking_date; ?>
                </td>
                 <td>
                 <?php if($get_appointments->booking_flag==0)
                 {
                   ?>
                 <label class="badge badge-warning">Pending</label>
                 <?php
                  }
                  elseif($get_appointments->booking_flag==1) {
                     ?>
                 <label class="badge badge-primary">Accept</label>
                 <?php
                   } 
                   elseif($get_appointments->booking_flag==2) {
                     ?>
                 <label class="badge badge-danger">Decline</label>
                 <?php
                   } 
                   elseif($get_appointments->booking_flag==3) {
                     ?>
                 <label class="badge badge-info">In_process</label>
                 <?php
                   } 
                   elseif($get_appointments->booking_flag==4) {
                     ?>
                 <label class="badge badge-success">Completed</label>
                 <?php
                   } ?>
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
<!-- content-wrapper ends -->
