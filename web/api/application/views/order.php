
       
          <div id="page-wrapper">
            <div class="container-fluid">
                
                <!-- ============================================================== -->
                <!-- .row -->
                <div class="container">
      <div class="row">
      <div class="white-box">
            <ul class="nav nav-tabs tabs customtab">
                    <li class="tab active">
                        <a href="#home" data-toggle="tab"> <span class="visible-xs"><i class="fa fa-home"></i></span> <span class="hidden-xs">Paid</span> </a>
                    </li>
                     <li class="tab">
                        <a href="#Unpaid" data-toggle="tab"> <span class="visible-xs"><i class="fa fa-home"></i></span> <span class="hidden-xs">Unpaid</span> </a>
                    </li> 
                    <li class="tab">
                      <a href="#profile" data-toggle="tab"> <span class="visible-xs"><i class="fa fa-user"></i></span> <span class="hidden-xs">Delivered</span> </a>
                  </li>
                   <li class="tab">
                      <a href="#Invoice" data-toggle="tab"> <span class="visible-xs"><i class="fa fa-user"></i></span> <span class="hidden-xs">Invoice</span> </a>
                  </li>
              </ul>
              <div class="tab-content">
                <div class="tab-pane active" id="home">
                <div class="row">
                  <div class="table-responsive">
                  <table id="example23" class=" table-bordered table-hover table-responsive" role="grid" >
                 <thead>
                <tr>
                  <th class="text-center" style="width: 100px">S. No.</th>
                  <th class="text-center" style="width: 100px">User Name</th>
                  <th class="text-center" style="width: 100px">Product Name</th>
                  <!--  <th>City</th> -->
                  <th class="text-center" style="width: 100px">Order Date</th>
                  <th class="text-center" style="width: 100px">Order Id</th>

<!--                   <th class="text-center" style="width: 100px">Image</th>
 -->                  <th class="text-center" style="width: 100px">Amount</th>
                <!--   <th class="text-center" style="width: 100px">Payment Status</th> -->
                  <th class="text-center" style="width: 100px">Name</th>
                  <th class="text-center" style="width: 100px">Email</th>
                  <th class="text-center" style="width: 100px">Mobile No.</th>
                  <th class="text-center" style="width: 100px">Address</th>
<!--                   <th class="text-center" style="width: 100px">Landmark</th>
 -->                   <th class="text-center" style="width: 100px">Status</th>
                  <th class="text-center">Action</th>
                </tr>
            </thead>
            <tbody class="text-center" style="width: 100px">
                <?php $i=0; foreach ($getordersPaid as $order) {
                $order_product=$this->Api_model->getSingleRow('order_products', array('order_id'=>$order->order_id));
                $food_product=$this->Api_model->getSingleRow('food_product', array('p_id '=>$order_product->product_id));
                // $pro_images=$this->Api_model->getSingleRow('pro_images', array('p_id '=>$order_product->product_id));
             $order_status=$this->Api_model->getSingleRow('order_status', array('order_id '=>$order_product->order_id));   
                $i++; ?>
                  <tr>
                    <td><?php echo $i; ?></td>
                    <td> <?php echo ucfirst($this->Api_model->get_user($order->user_id)->first_name); ?></td>

                    <td><?php echo ucfirst($food_product->p_name); ?></td>
                   <!--  <td><?php echo wordwrap($order->city,30,"<br>\n"); ?></td> -->
                    <td><?php echo $order->order_date; ?> </td>
                    <td><?php echo $order->order_id; ?> </td>

                    <!-- <td class="py-1">
                        <?php if(count($pro_images)>0 && $pro_images->image){ ?>
                        <img style="border-radius: 50%;width: 40px;height: 40px;" src="<?php echo  base_url().$pro_images->image; ?>" width="80" height="80" alt="image"/>
                        <?php }else{ ?>
                        <img style="border-radius: 50%;width: 40px;height: 40px;" src="<?php echo  base_url('/assets/images/faces-clipart/userblank.png'); ?>" width="80" height="80" alt="image"/>
                        <?php } ?>
                    </td> -->
                    <td><?php echo number_format($order->final_price,2); ?></td>
                   <!--  <td><?php if($order->payment_status==1){ ?>
                    <label class="badge badge-teal">Paid</label>
                    <?php }elseif($order->payment_status==0){ ?>
                    <label class="badge badge-danger">Unpaid</label>
                    <?php } ?></td> -->
                    <td><?php echo $order->name; ?></td>
                    <td><?php echo $order->email; ?></td>
                    <td><?php echo $order->mobile_no; ?></td>
                    <td><?php echo wordwrap($order->address,30,"<br>\n"); ?></td>
<!--                     <td><?php echo wordwrap($order->landMark,30,"<br>\n"); ?></td>
 -->                    <td><?php if($order->status==2){ ?>
                        <label class="badge badge-teal">Delivered</label>
                        <?php echo $order_status->od_delivered_date; ?>
                        <?php } elseif($order->status==6){ ?>
                        <label class="badge badge-danger">Pending</label>
                        <?php echo $order_status->od_confirm_date; ?>
                        <?php } if($order->status==1){ ?>
                        <label class="badge teal">Confrimed</label><?php echo $order_status->od_pending_date; } if($order->status==3){ ?>
                        <label class="badge teal">Packed</label>
                            <?php echo $order_status->od_packed_date; ?>
                        <?php }elseif($order->status==4){ ?>
                        <label class="badge danger">Dispatched</label>
                            <?php echo $order_status->od_dispatched_date; ?>
                        <?php }elseif($order->status==5){ ?>
                        <label class="badge danger">Cancelled</label>
                            <?php echo $order_status->od_canceled_date; ?>
                        <?php } ?>

                         </td>

                         
                        <td class="text-center">
                        <?php if($order->status==5){ ?>
                          <a title="Change order Status" class="btn btn-info btn-outline btn-circle btn-lg m-r-5" onclick="showorderDetails1('myModal11<?php echo $order->id; ?>')"><i class="fa fa-reply fa-fw"></i></a>
                        <?php }else if($order->status==2){ ?>
                          <a title="Change order Status" class="btn btn-info btn-outline btn-circle btn-lg m-r-5" onclick="showorderDetails1('myModal12<?php echo $order->id; ?>')"><i class="fa fa-reply fa-fw"></i></a>
                        <?php }else{?> 
                        <a title="Change order Status" class="btn btn-info btn-outline btn-circle btn-lg m-r-5" onclick="showorderDetails1('myModal1<?php echo $order->id; ?>')"><i class="fa fa-reply fa-fw"></i></a>
                        <?php } ?>
                         <a title="Details" class="btn btn-info btn-outline btn-circle btn-lg m-r-5" onclick="showorderDetails1('myModal<?php echo $order->id; ?>')"><i class="fa fa-eye fa-fw"></i></a>
                    </td>
                  </tr>
                  <!--Modal for canceled-->
                   <div class="modal fade" id="myModal11<?php echo $order->id; ?>" role="dialog">
                  <div class="modal-dialog">
                  
                    <!-- Modal content-->
                    <div class="modal-content">
                      <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Order Status</h4>
                      </div>
                      <div class="modal-body">
                        <h1>Order is Cancelled<h1>
                      </div>
                   </div>
                  </div>
                </div>

                     <!--Modal for Delivery-->
                   <div class="modal fade" id="myModal12<?php echo $order->id; ?>" role="dialog">
                  <div class="modal-dialog">
                  
                    <!-- Modal content-->
                    <div class="modal-content">
                      <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Order Status</h4>
                      </div>
                      <div class="modal-body">
                        <h1>Order is Delivered<h1>
                      </div>
                   </div>
                  </div>
                </div>



                    <!-- Modal content-->
                  <div class="modal fade" id="myModal1<?php echo $order->id; ?>" role="dialog">
                  <div class="modal-dialog">
                  
                    <div class="modal-content">
                      <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Change Order Status</h4>
                      </div>
                      <div class="modal-body">
                        <form method="post" action="<?php echo base_url('Admin/updateorder')?>">
                        <div class="form-group">
                          <input type="hidden"  name="order_id" value="<?php echo $order->order_id;  ?>">
                          <label class="col-sm-4">Order Status</label>
                          <select class="form-control" name="status" >
                          <?php  if($order->status=='6'){ ?>
                       <option <?php if($order->status=='6'){echo 'selected';}?> value="5">Cancel</option>
                      <option <?php if($order->status=='6'){echo 'selected';}?> value="1">Confirm</option>
                          <?php } if($order->status=='1'){ ?>
                      <option <?php if($order->status=='1'){ echo 'selected'; }?> value="3">Packed</option>
                          <?php } if($order->status=='3'){ ?>
                      <option <?php if($order->status=='3'){echo 'selected';}?> value="4">Dispatched</option>
                          <?php } if($order->status=='4'){ ?>
                      <option <?php if($order->status=='4'){echo 'selected';}?> value="2">Delivered</option>
                           <?php } if($order->status=='2'){ ?>
                      <option <?php if($order->status=='2'){echo 'selected';}?> value="2">Delivered</option>
                          <?php } ?> 
                          </select>
                        </div>

                         <div class="form-group">
                          <label class="col-sm-4">Date</label>
                          <input type="Date" id="first-name" name="order_date" value="" required="required" class="form-control">
                        </div>
                        <div class="form-group">
                      </div>
                       
                       <!-- <input type="text" name="date" id="date" /> 
                      <input type="button" id="calendarButton" value="Date" /> -->

                        <?php  if($order->status=='6'){ 
                          ?>
                           <div class="form-group" id="es_date">
                          <label class="col-sm-5">Estimated Delivery Date</label>
                          <input type="Date" id="first-name" name="place_date" value="" class="form-control">
                        </div>
                          <?php } ?>
                       <div class="form-group">
                        <input type="submit" class="btn btn-success" Value="Change"/>
                      </div>


                      </form>
                      </div>
                   </div>
                  </div>
                </div>

                  <?php $where= array('order_id'=>$order->order_id); 
                  $order_info= $this->Api_model->getAllDataWhere($where,'order_products'); ?>
                  <div id="myModal<?php echo $order->id; ?>" class="modal fade" role="dialog">
                  <div class="modal-dialog" >
                  <!-- Modal content-->
                  <div class="modal-content" style="width: 200%;margin-left: -315px;">
                  <div class="modal-header">
                  <span class="pull-left"><?php  echo ucfirst($this->Api_model->get_user($order->user_id)->first_name);  ?></span>
                  <button type="button" class="close" data-dismiss="modal">&times;</button>
                  </div>

                  <div class="modal-body" >
                    <div class="row">
                      <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
                      <label class="font-weight-bold text-right mb-0" >#</label>
                      </div>
                      <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                      <label class="font-weight-bold text-right mb-0" >Product</label>
                      </div>
                      <div class="col-xs-3 col-sm-3 col-md-3 col-lg-3">
                      <label class="font-weight-bold text-right mb-0" >Description</label>
                      </div>
                      <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
                      <label class="font-weight-bold text-right mb-0" >Rate</label>
                      </div>
                      <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
                      <label class="font-weight-bold text-right mb-0" >Color</label>
                      </div>
                      <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
                      <label class="font-weight-bold text-right mb-0" >Size</label>
                      </div>
                        <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
                      <label class="font-weight-bold text-right mb-0" >Weight</label>
                      </div> 
                      <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
                      <label class="font-weight-bold text-right mb-0" >Quantity</label>
                      </div>
                      <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
                      <label class="font-weight-bold text-right mb-0" >Gross Amount</label>
                      </div>
                     <!--  <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
                      <label class="font-weight-bold text-right mb-0" >Total Discount </label>
                      </div>  -->
                     <!--  <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
                      <label class="font-weight-bold text-right mb-0" >Net Amount</label>
                      </div> -->
                      </div>
                      <?php $count=1; foreach ($order_info as $data) { ?>
                      <div class="row">
                      <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
                      <label class="" ><?= $count++;?> </label>
                      </div>
                      <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                      <?php $where= array('p_id'=>$data->product_id);
                       $pro = $this->Api_model->getSingleRow('food_product',$where);
                       ?>
                      <label class="" ><?php echo  $pro->p_name ; ?> </label>   
                      </div>
                      <div class="col-xs-3 col-sm-3 col-md-3 col-lg-3">
                      <label class="" ><?= $data->p_description ?> </label>
                      </div>
                      <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
                      <label class="" ><strike><?= number_format($data->p_rate,2) ?></strike></label>
                      </div>
                      <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
                      <label class="" ><?= $data->color; ?></label>
                      </div>
                      <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
                      <label class="" ><?= $data->size; ?></label>
                      </div>
                       <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
                      <label class="" ><?= $data->weight; ?></label>
                      </div> 
                      <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
                      <label class="" ><?= number_format($data->quantity,2) ?></label>
                      </div>
                      <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
       <label class="" ><?= number_format($data->quantity*$data->p_rate,2) ?></label>
                      </div>
                      <!-- <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
                      <label class="" ><?= number_format($data->discount_total,2) ?> </label>
                      </div> -->
                       <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
                      <label class="" ><?= number_format($data->total_price,2) ?> </label>
                      </div> 
                      </div>
                      <hr>
                      <?php } ?>
                      </div>
                    </div>
                  </div>
                <?php } ?>                 
              </tbody>
            </table>
          </div>
           </div>
        </div>
              <div id="Unpaid" class="tab-pane">
               <div class="row">
                <div class="table-responsive">
                <table id="myTable" class="table-bordered dataTable" role="grid" >
                <thead>
                <tr>
                  <th class="text-center" style="width: 100px">S.No.</th>
                  <th class="text-center" style="width: 100px">User Name</th>
                  <th class="text-center" style="width: 100px">Product Name</th>
                  <th class="text-center" style="width: 100px">Order Date</th>
                  <th class="text-center" style="width: 100px">Order Id</th>

                 <!--  <th class="text-center" style="width: 100px">Image</th> -->
                  <th class="text-center" style="width: 100px">Amount</th>
                  <th class="text-center" style="width: 100px">Name</th>
                  <th class="text-center" style="width: 100px">Email</th>
                  <th class="text-center" style="width: 100px">Mobile No.</th>
                  <th class="text-center" style="width: 100px">Address</th>
<!--                   <th class="text-center" style="width: 100px">Landmark</th>
 -->                  <th class="text-center" style="width: 100px">Status</th>
                  <th class="text-center">Action</th>
                </tr>
            </thead>
            <tbody class="text-center" style="width: 100px">
                

                <?php $i=0; foreach ($getordersUnpaid as $order){
                                $order1=$order->order_id;
                           $order_product=$this->Api_model->getDataByThreeTable1($order1);
                // $food_product=$this->Api_model->getSingleRow('food_product', array('p_id '=>$order_product->product_id));
                // $pro_images=$this->Api_model->getSingleRow('pro_images', array('p_id '=>$order_product->product_id));
                 // $order_status=$this->Api_model->getSingleRow('order_status', array('order_id '=>$order_product->order_id));   
                $i++; ?>
                  <tr>
                    <td><?php echo $i; ?></td>
                    <td> <?php echo ucfirst($this->Api_model->get_user($order->user_id)->first_name); ?></td>
                   <!--   <td><?php echo ucfirst($order->p_name); ?></td> -->
                  <td><?php echo ucfirst($order_product[0]->p_name); ?></td>
                 <!--  <td><?php echo wordwrap($order->city,30,"<br>\n"); ?></td> -->
                    <td><?php echo $order->order_date; ?> </td>
                    <td><?php echo $order->order_id; ?> </td>
                    <!-- <td class="py-1">
                        <?php if(count($pro_images)>0 && $pro_images->image){ ?>
                        <img style="border-radius: 50%;width: 40px;height: 40px;" src="<?php echo  base_url().$pro_images->image; ?>" width="80" height="80" alt="image"/>
                        <?php }else{ ?>
                        <img style="border-radius: 50%;width: 40px;height: 40px;" src="<?php echo  base_url('/assets/images/faces-clipart/userblank.png'); ?>" width="80" height="80" alt="image"/>
                        <?php } ?>
                    </td> -->
                    <td><?php echo number_format($order->final_price,2); ?></td>
                  <!--   <td><?php if($order->payment_status==1){ ?>
                    <label class="badge badge-teal">Paid</label>
                    <?php }elseif($order->payment_status==0){ ?>
                    <label class="badge badge-danger">Unpaid</label>
                    <?php } ?></td> -->
                    <td><?php echo $order->name; ?></td>
                    <td><?php echo $order->email; ?></td>
                    <td><?php echo $order->mobile_no; ?></td>
                    <td><?php echo wordwrap($order->address,30,"<br>\n"); ?></td>
<!--                     <td><?php echo wordwrap($order->landMark,30,"<br>\n"); ?></td>
 -->                    <td><?php if($order->status==2){ ?>
                        <label class="badge badge-teal">Delivered</label>
                        <?php }elseif($order->status==1){ ?>
                        <label class="badge badge-danger">Pending</label>
                        <?php } ?>
                        <?php if($order->status==3){ ?>
                        <label class="badge badge-teal">Packed</label>
                        <?php }elseif($order->status==4){ ?>
                        <label class="badge badge-danger">Dispatched</label>
                        <?php } 
                        ?>


                    </td>
                    <td class="text-center">
                              <div class="dropdown">
                        <button class="btn dropdown-toggle" type="button" data-toggle="dropdown">Manage
                        <span class="caret"></span></button>
                        <ul class="dropdown-menu">
                          <li><a title="Delivered" class="" href="<?php echo base_url('/Admin/change_order_status');?>?id=<?php echo $order->id; ?>&status=2&request=2">Delivered</a></li>
                          <li><a title="Packed" class="" href="<?php echo base_url('/Admin/change_order_status');?>?id=<?php echo $order->id; ?>&status=3&request=3" >Packed</a></li>
                          <li><a title="Pending" class="" href="<?php echo base_url('/Admin/change_order_status');?>?id=<?php echo $order->id; ?>&status=1&request=1">Pending</a></li>
                          <li><a title="Dispatched" class="" href="<?php echo base_url('/Admin/change_order_status');?>?id=<?php echo $order->id; ?>&status=4&request=4" >Dispatched</a></li>
                          <li><a title="Canceled" class="" href="<?php echo base_url('/Admin/change_order_status');?>?id=<?php echo $order->id; ?>&status=5&request=5" >Canceled</a></li>
                        </ul>
                    </div>
                        <a title="Details" class="btn btn-info btn-outline btn-circle btn-lg m-r-5" onclick="showorderDetails1('myModal<?php echo $order->id; ?>')"><i class="fa fa-eye fa-fw"></i></a>
                      </td>
                  </tr>
                  <?php $where= array('order_id'=>$order->order_id); 
                  $order_info= $this->Api_model->getAllDataWhere($where,'order_products'); ?>
                  <div id="myModal<?php echo $order->id; ?>" class="modal fade" role="dialog">
                  <div class="modal-dialog" >
                  <!-- Modal content-->
                  <div class="modal-content" style="width: 180%;margin-left: -215px;">
                  <div class="modal-header" >
                  <span class="pull-left"><?php  echo ucfirst($this->Api_model->get_user($order->user_id)->first_name);  ?></span>
                  <button type="button" class="close" data-dismiss="modal">&times;</button>
                  </div>
                  <div class="modal-body">
                    <div class="row">
                      <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
                      <label class="font-weight-bold text-right mb-0" ># </label>
                      </div>
                      <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                      <label class="font-weight-bold text-right mb-0" >Product </label>
                      </div>
                      <div class="col-xs-3 col-sm-3 col-md-3 col-lg-3">
                      <label class="font-weight-bold text-right mb-0" >Description </label>
                      </div>
                      <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
                      <label class="font-weight-bold text-right mb-0" >Rate </label>
                      </div>
                      <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
                      <label class="font-weight-bold text-right mb-0" >Color </label>
                      </div>
                      <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
                      <label class="font-weight-bold text-right mb-0" >Size </label>
                      </div>
                        <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
                      <label class="font-weight-bold text-right mb-0" >Weight </label>
                      </div> 
                      <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
                      <label class="font-weight-bold text-right mb-0" >Quantity </label>
                      </div>
                      <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
                      <label class="font-weight-bold text-right mb-0" >Gross Amount </label>
                      </div>
                      <!-- <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
                      <label class="font-weight-bold text-right mb-0" >Total Discount </label>
                      </div> -->
                     <!--  <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
                      <label class="font-weight-bold text-right mb-0" >Net Amount </label>
                      </div> -->
                      </div>
                      <?php $count=1; foreach ($order_info as $data) { ?>
                      <div class="row">
                      <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
                      <label class="" ><?= $count++;?> </label>
                      </div>
                      <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
                      <?php $where= array('p_id'=>$data->product_id);
                       $pro = $this->Api_model->getSingleRow('food_product',$where);
                       ?>
                      <label class="" ><?= $data->p_name ; ?> </label>   
                      </div>
                      <div class="col-xs-3 col-sm-3 col-md-3 col-lg-3">
                      <label class="" ><?= $data->p_description ?> </label>
                      </div>
                      <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
                      <label class="" ><strike><?= number_format($data->p_rate,2) ?></strike></label>
                      </div>
                      <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
                      <label class="" ><?= $data->color; ?></label>
                      </div>
                      <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
                      <label class="" ><?= $data->size; ?></label>
                      </div>
                       <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
                      <label class="" ><?= $data->weight; ?></label>
                      </div> 
                      <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
                      <label class="" ><?= number_format($data->quantity,2) ?></label>
                      </div>
                      <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
                      <label class="" ><?= number_format($data->quantity*$data->p_rate,2) ?></label>
                      </div>
                      <!-- <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
                      <label class="" ><?= number_format($data->discount_total,2) ?> </label>
                      </div> -->
                       <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
                      <label class="" ><?= number_format($data->total_price,2) ?> </label>
                      </div> 

                      </div>
                      <hr>
                      <?php } ?>
                      </div>
                    </div>
                  </div>
                <?php } ?>                 
              </tbody>
            </table>
          </div>
         </div>
               </div>
 

            <div class="tab-pane" id="profile">
<div class="table-responsive">
         <table  class="table table-hover manage-u-table dataTable" > 
         <thead>
          <tr>
            <th>S. No.</th>
            <th>User Name</th>
            <th>Product Name</th>
            <!-- <th>Quantity</th> -->
            <th>Address</th>
            <th>Order Date</th>
            <th>Order Id</th>

<!--             <th>Image</th>
 -->            <th>Amount</th>
            <th>Name</th>
            <th>Email</th>
            <th>Mobile No.</th>
            <th class="text-center">Action</th>
          </tr>
        </thead>
        <tbody>
            <?php $i=0; foreach ($getordersplaced as $order) { 
              $order_product=$this->Api_model->getSingleRow('order_products', array('order_id'=>$order->order_id));
              $product_invoice=$this->Api_model->getSingleRow('product_invoice', array('order_id'=>$order->order_id));
              $food_product=$this->Api_model->getSingleRow('food_product', array('p_id '=>$order_product->product_id));
              // $pro_images=$this->Api_model->getSingleRow('pro_images', array('p_id '=>$order_product->product_id)); 
              $i++; ?>
            <tr>
              <td><?php echo $i; ?></td>
              <td> <?php echo ucfirst($this->Api_model->get_user($order->user_id)->first_name); ?></td>
              <td><?php echo ucfirst($food_product->p_name); ?></td>
             <!--  <td><?php echo $food_product->quantity; ?></td>  -->
              <td><?php echo wordwrap($order->address,30,"<br>\n"); ?></td>
              <td><?php echo $order->order_date; ?></td>
              <td><?php echo $order->order_id; ?></td>

             <!--  <td class="py-1">
                  <?php if(count($pro_images)>0 && $pro_images->image){ ?>
                      <img style="border-radius: 50%;width: 40px;height: 40px;" src="<?php echo  base_url().$pro_images->image; ?>" width="80" height="80" alt="image"/>
                        <?php }else{ ?>
                  <img style="border-radius: 50%;width: 40px;height: 40px;" src="<?php echo  base_url('/assets/images/faces-clipart/userblank.png'); ?>" width="80" height="80" alt="image"/>
                  <?php } ?>
              </td> -->
              <td><?php echo number_format($order->final_price,2); ?></td>
              <td><?php echo $order->name; ?></td>
              <td><?php echo $order->email; ?></td>
              <td><?php echo $order->mobile_no; ?></td>
               <td class="text-center">
                        
                         <!-- <a onclick="return confirm('Are you sure you want to cancel this order?')" title="Cancel" class="btn btn-info btn-outline btn-circle btn-lg m-r-5" href="/Admin/Cancelorder/<?php echo  $order->order_id; ?>"><i class="fa fa-reply fa-fw"></i></a> -->

                       <a title="Details" class="btn btn-info btn-outline btn-circle btn-lg m-r-5" onclick="showorderDetails('myModal<?php echo $order->order_id; ?>')"><i class="fa fa-eye fa-fw"></i></a>
                       <!-- <?php if($order->payment_status==0){?>
                         <a title="Pay" class="btn btn-info btn-outline btn-circle btn-lg m-r-5" onclick="showorderDetails('payModal<?php echo $order->id; ?>')"><i class="fa fa-credit-card fa-fw"></i></a>
                       <?php } ?> -->
                </td>
              </tr>


      <?php 
              $where = array('order_id'=>$order->order_id); 
              $order_info= $this->Api_model->getAllDataWhere($where,'order_products'); ?>
              <div id="myModal<?php echo $order->order_id; ?>" class="modal fade" role="dialog">
              <div class="modal-dialog" >
              <!-- Modal content-->

             <div class="modal-content" style="width: 180%;margin-left: -215px;">
                        <div class="modal-header">
              <span class="pull-left"><?php  echo ucfirst($this->Api_model->get_user($order->user_id)->first_name);  ?></span>
              <button type="button" class="close" data-dismiss="modal">&times;</button>
              </div>
              <div class="modal-body" >
              <div class="row">
              <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
              <label class="font-weight-bold text-right mb-0" ># </label>
              </div>
              <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
              <label class="font-weight-bold text-right mb-0" >Product </label>
              </div>
              <div class="col-xs-3 col-sm-3 col-md-3 col-lg-3">
              <label class="font-weight-bold text-right mb-0" >Description </label>
              </div>
              <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
              <label class="font-weight-bold text-right mb-0" >Rate </label>
              </div>
              <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
              <label class="font-weight-bold text-right mb-0" >Quantity </label>
              </div>
              <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
              <label class="font-weight-bold text-right mb-0" >Color </label>
              </div>
              <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
              <label class="font-weight-bold text-right mb-0" >Size </label>
              </div>
                <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
              <label class="font-weight-bold text-right mb-0" >Weight </label>
              </div> 
              <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
              <label class="font-weight-bold text-right mb-0" >Gross Amount </label>
              </div>
             
         <!--      <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
              <label class="font-weight-bold text-right mb-0" >Net Amount </label>
              </div> -->

              </div>
              <?php $count=1; foreach ($order_info as  $data) { ?>
              <div class="row">
              <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
              <label class="" ><?= $count++;?> </label>
              </div>
              <div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
              <?php $where= array('p_id'=>$data->product_id);
             $pro = $this->Api_model->getSingleRow('food_product',$where);
             ?>

              <label class="" ><?php echo  $pro->p_name; ?></label>
              </div>
              <div class="col-xs-3 col-sm-3 col-md-3 col-lg-3">
              <label class="" ><?= $data->p_description ?> </label>
              </div>
              <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
              <label class="" ><strike><?= number_format($data->p_rate,2) ?></strike></label>
              </div>
              <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
              <label class="" ><?= number_format($data->quantity,2) ?></label>
              </div>
              <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
              <label class="" ><?= $data->color; ?></label>
              </div>
              <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
              <label class="" ><?= $data->size; ?></label>
              </div>
               <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
              <label class="" ><?= $data->weight; ?></label>
              </div> 
              <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
              <label class="" ><?= number_format($data->quantity*$data->p_rate,2) ?> 
              </label>
              </div>
             <!--  <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
              <label class="" ><?= number_format($data->discount_total,2) ?> </label>
              </div> -->
               <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1">
              <label class="" ><?= number_format($data->total_price,2) ?> </label>
              </div> 
            </div>

          <hr>
          <?php } ?>
          </div>
        </div>
      </div>
    </div>

            
    <div id="payModal<?php echo $order->id; ?>" class="modal fade" role="dialog">
              <div class="modal-dialog" >  
              <!-- Modal content-->
  
              <div class="modal-content" style="width: 200%;margin-left:-215px;">
              <div class="modal-header">
              <span class="pull-left"><?php  echo ucfirst($this->Api_model->get_user($order->user_id)->first_name);  ?></span>
              <button type="button" class="close" data-dismiss="modal">&times;</button>
              </div>
              <div class="modal-body" >
               <form method="" action="<?= base_url();?>"> 
              <div class="row">
               <div class="col-xs-6">
               <label for="email"><b>Amount</b></label>
               <input type="hidden"  name="user_id" value="<?= $order->user_id;?>">
               <input type="hidden"  name="order_id" value="<?= $order->order_id;?>">
               <input type="text" placeholder="Amount" name="amount" required class="form-control" value="<?= $order->final_price;?>">
             </div>
              <div class="col-xs-4">
              <button style="margin-top: 22px;" type="submit" name="submit" class="btn-primary form-control">Pay</button>  
             </div>
            </div>
          </form>
         </div>
        </div>
      </div>
    </div>
         <?php } ?> 


      </tbody>
      </table>
    </div>
    </div>



      <div class="tab-pane" id="Invoice">
        <div class="table-responsive">
        <table class="table table-hover manage-u-table dataTable">
        <thead>
          <tr>
            <th>S. No.</th>
           <th>Order Id</th>
           <th>Transaction Id</th>
            <th>User Name</th>
            <th>Amount</th>
            <th>Date</th>
          </tr>
        </thead>
        <tbody>
            <?php $i=0; foreach ($invoice as $invoicedata) { 
              $order_product=$this->Api_model->getSingleRow('order_products', array('order_id'=>$invoicedata->order_id));
              $food_product=$this->Api_model->getSingleRow('food_product', array('p_id '=>$order_product->product_id));
                $pro_images=$this->Api_model->getSingleRow('pro_images', array('p_id '=>$order_product->product_id));
               // $product_invoice=$this->Api_model->getSingleRow('product_invoice', array('order_id'=>$order_product->order_id));
                $orders=$this->Api_model->getSingleRow('orders', array('order_id'=>$order_product->order_id));
               

              $i++; ?>
            <tr>
              <td><?php echo $i; ?></td> 
<!--               <td><?php echo $order_id; ?></td> 
 -->              <td><?php echo $order_product->order_id; ?> </td>
              <td><?php echo $invoicedata->txn_id ; ?></td>
              <td><?php echo ucfirst($this->Api_model->get_user($invoicedata->user_id)->first_name); ?></td>
              <td><?php echo number_format($orders->final_price,2); ?></td>
              <td><?php echo date('d-m-y', $invoicedata->created_at); ?></td>
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
</div>

<script type="text/javascript">
function showorderDetails(id){

 $("#"+id).modal("show");
}
function showorderDetails1(id){
 $("#"+id).modal("show");
}

</script>

 <!-- <script type="text/javascript" src="./prototype.js"></script>
    <script type="text/javascript" src="./calendarview.js"></script>
    <script type="text/javascript" >
      window.onload = function() {
        Calendar.setup({
          dateField      : 'date',
          triggerElement : 'calendarButton'
        })
      }
    </script> -->
