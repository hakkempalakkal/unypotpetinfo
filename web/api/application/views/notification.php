<div id="page-wrapper">
<div class="container-fluid">
<div class="row clearfix">
<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 grid-margin stretch-card">
<div class="card">
<div class="card-body">
<div class="header">
<h2>Send Notification</h2>
<div class="send_msg" style="float: right; padding: 5px 0;margin-top: -29px;">
<input disabled="disabled" id="send_msg" class="btn btn-success" type="submit" value="Send Message" data-target="#msgmodal" data-toggle="modal">
</div>
</div>
<div role="dialog" class="modal fade" id="msgmodal" style="display: none;">
<div class="modal-dialog">

<!-- Modal content--> 

<form method="post" action="<?= base_url('Admin/putnotification'); ?>"> 
<div class="modal-content">
<div class="modal-header">
<h4><b>Send Notification</b></h4>
<button data-dismiss="modal" class="close" type="button">×</button>
</div>
<div class="modal-body"> 
<div class="form-group clearfix">
<input type="hidden" id="uid" name="uid"><br>
</div>

<div class="form-group clearfix">
<label>Title</label>
<input id="text" class="col-md-12" type="text" name="title" maxlength="15" required /><br>
<span class="result">0 </span><span>/15</span>
</div>

<div class="form-group clearfix">
<label>Notification Message</label>
<textarea id="textfield1" name="message" class="col-md-12" rows="4" maxlength="150" required></textarea><br>
<span class="result1">0 </span><span>/150</span>
</div>
</div>

<div class="modal-footer">
<input type="submit" class="btn btn-success" id="notify-user">
<button data-dismiss="modal" class="btn btn-default" type="button">Close</button>
</div>
</div>
</form>
</div>
</div>


<div class="body">
<p><input id="select_all" class="mychechk" type="checkbox" value="" name="check" style="opacity:1;position:inherit;"> Select All</p> 
<table id="" class="table table-striped res_table">
<thead>
<tr>
<th class="text-center">S.No.</th>
<th class="text-center">Name</th>
<th class="text-center">Address</th>
<th class="text-center">Mobile No.</th>
<th class="text-center">Email Id</th>
<th class="text-center">Send Message</th>
</tr>
</thead>
<tbody >
<?php $i=0; foreach ($user as $val ){ $i++; ?>
<tr>
<td class="text-center"><?php echo $i; ?></td>
<td class="text-center" style="text-transform:capitalize;"><?php echo $val->first_name; ?></td>
<td><?php echo $val->address; ?></td>
<td class="text-center"><?php echo $val->mobile_no; ?></td>
<td class="text-center"><?php echo $val->email; ?></td>
<td class="text-center"><input class="notification_check mychechk" type="checkbox" value="<?= $val->id ?>" name="check" style="opacity:1;position:inherit;"></td> 
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



<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert-dev.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.css">



<script>

$(document).ready(function(){

$('.mychechk').on('change', function() {
$('#uid').val('');
var sThisVal='';
$('.mychechk').each(function () {
if(this.checked){
sThisVal = sThisVal+$(this).val()+',';
$('#uid').val(sThisVal);
}
});
if( sThisVal!=''){
$('#send_msg').prop("disabled", false);
}else{
$('#send_msg').prop("disabled", true);
}
});
});
</script>

<script>
$(document).ready(function(){
$('input#textfield').on('keyup',function(){
var charCount = $(this).val().replace(/^(\s*)/g, '').length;
$(".result").text(charCount + " ");
});
});
</script>

<script>
$(document).ready(function(){
$('textarea#textfield1').on('keyup',function(){
var charCount = $(this).val().replace(/^(\s*)/g, '').length;
$(".result1").text(charCount + " ");
});
});
</script>

<!-- notification -->

<script type="text/javascript">
var base_url = '<?php echo base_url(); ?>';

jQuery(document).ready(function() {
jQuery('#allusers').dataTable({

});

jQuery('#notification_table1').dataTable({ 
"lengthMenu": [ [10, 50, 100, -1], [10, 50, 100, "All"] ],
});

var data = 
{
mobile:[],msg:'',title:''
}
jQuery(document).on('click','#select_all',function(){

if(jQuery(this).prop('checked') == true)
{
jQuery('.notification_check').each(function(index, el) {
jQuery(this).prop('checked',true);
var mobile = jQuery(this).parent().prev().text(); 
data.mobile.push(mobile); 
}); 
}
else{
jQuery('.notification_check').each(function(index, el) {
jQuery(this).prop('checked',false);
data.mobile=[];
}); 
}
});

Array.prototype.remove = function() {
var what, a = arguments, L = a.length, ax;
while (L && this.length) {
what = a[--L];
while ((ax = this.indexOf(what)) !== -1) {
this.splice(ax, 1);
}
}
return this;
};

jQuery(document).on('click','.notification_check',function(){

if(jQuery(this).prop('checked') == true){

var mobile = jQuery(this).parent().prev().text(); 
data.mobile.push(mobile); 
}
else
{mobile
var mobile = jQuery(this).parent().prev().text();
data.mobile.remove(mobile); 
}
});

jQuery(document).on('click','#send_msg',function(){
console.log(data);
if(data.mobile.length ==0){
swal("Warning", "Please select user to send notification", "error");
}
else
{
jQuery('#msgmodal').modal('show');
}
});

jQuery(document).on('click','#notify-user',function(){

var msg = jQuery('#msgmodal textarea').val();
data.msg= msg;

var title = jQuery('#msgmodal input').val();
data.title= title;

$.ajax({

url: base_url+'Webservice/firebase',
type: 'POST',
dataType: 'json',
data: data,
success:function(data)
{

swal("Success", "Notification send successfully.", "success");
swal({
title: "Success",
text: "Notification send successfully.",
type: "success",
confirmButtonColor: "#DD6B55",
confirmButtonText: "OK",
closeOnConfirm: true, 
},
function(isConfirm){
if (isConfirm) {
window.location.href = base_url + "Admin/notifaction";
} 
});
data.mobile = [];data.title ='';data.msg ='';
}
}) 
});
});
</script>