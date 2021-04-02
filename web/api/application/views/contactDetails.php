<div id="page-wrapper">
<div class="container-fluid">
<!-- ============================================================== -->
<!-- .row -->



<!-- ============================================================== -->
<!-- Demo table -->
<!-- ============================================================== -->
<div class="row">
<div class="col-md-12">
  <div class="panel">
      <div class="panel-heading">Requests</div>
      <div class="table-responsive">
          <table class="table table-hover manage-u-table" id="myTable">
              <thead>
               <tr>
             <th>#</th>
            <th>Customer Name</th>
            <th>Customer No.</th>
            <th>Owner Name</th>
            <th>Owner No.</th>
            <th>Pet Name</th>
            <th>Message</th>
            <th>Status</th>
            <th>Message time</th>
                    </tr>
              </thead>
              <tbody>
            <?php  $count=1; foreach ($contact as $contact){
            $userData = $this->Api_model->getSingleRow('users',array('id'=>$contact->user_id)); ?>
              <tr>
                <td><?=  $count++; ?></td>
                <td><?php echo ucfirst($this->Api_model->get_user($contact->user_id)->first_name); ?></td>
                <td><?php echo $this->Api_model->get_user($contact->user_id)->mobile_no; ?></td>
                <td><?php if($userData->first_name!=""){echo ucfirst($userData->first_name);} ?>
                </td>
                <td><?php echo ucfirst($this->Api_model->get_user($contact->user_id_owner)->mobile_no); ?></td>
                <td><?php echo ucfirst($this->Api_model->get_pet($contact->user_id_owner)->petName); ?></td>
                <td><?php echo wordwrap($contact->description,50,"<br>\n"); ?></td>
                <td><?php echo $contact->status; ?></td>
                <td><?php echo date('M d, Y H:i:s', $contact->created); ?></td>
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
function showProductInfo(id)
{
$("#"+id).modal("show");
}
</script>