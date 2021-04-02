

       <!-- ============================================================== -->
        <!-- End Left Sidebar -->
        <!-- ============================================================== -->
        <!-- ============================================================== -->
        <!-- Page Content -->

        <!-- ============================================================== -->
       
          <div id="page-wrapper">
            <div class="container-fluid">
                
                <!-- .row -->
                <div class="row">
                    <div class="col-lg-3 col-sm-3 col-xs-12">
                        <div class="white-box" style="height: 173px;">
                            <h3 class="box-title">Total Revenue</h3>
                            <ul class="list-inline two-part">
                                <li><i class=""></i></li>
                                <li class="text-center" style="margin-left: 10px;font-size: 20px;"><span class="counter"><?= number_format($total_revenue[0]->final_price ,2)  ?></span></li>
                            </ul>
                        </div>
                    </div>
                    
                    <div class="col-lg-3 col-sm-6 col-xs-12">
                        <div class="white-box">
                            <h3 class="box-title">Products</h3>
                            <ul class="list-inline two-part">
                                <li><i class="icon-folder-alt text-danger"></i></li>
                                <li class="text-right"><span class=""><?= $total_product ?></span></li>
                            </ul>
                        </div>
                    </div>
                    <div class="col-lg-3 col-sm-6 col-xs-12">
                        <div class="white-box">
                            <h3 class="box-title">Pets</h3>
                            <ul class="list-inline two-part">
                                <li><i class="icon-folder-alt text-danger"></i></li>
                                <li class="text-right"><span class=""><?= $total_pet; ?></span></li>
                            </ul>
                        </div>
                    </div>
                    <div class="col-lg-3 col-sm-6 col-xs-12">
                        <div class="white-box">
                            <h3 class="box-title">Users</h3>
                            <ul class="list-inline two-part">
                                <li><i class="icon-folder-alt text-danger"></i></li>
                                <li class="text-right"><span class=""><?= $total_user;?></span></li>
                            </ul>
                        </div>
                    </div>
                </div>
                
               
                <!-- ============================================================== -->
                <!-- Demo table -->
                <!-- ============================================================== -->
                <div class="row">
                    <div class="col-md-12">
                        <div class="panel">
                            <div class="panel-heading">Manage Order</div>
                            <div class="table-responsive">
                            <table id="myTable" class=" table-bordered dataTable" role="grid" >
                                    <thead>
                                     <tr>
                   <th class="text-center" style="width: 100px">S. No.</th>
                   <th class="text-center" style="width: 100px" >User Name</th>
                   <th class="text-center" style="width: 100px">Product</th>
                                    <!-- <th>Quantity</th> -->
                   <th class="text-center" style="width: 100px">Payment Status</th>
                   <th class="text-center" style="width: 100px">Amount</th>
                   <th class="text-center" style="width: 100px">Name</th>
                   <th class="text-center" style="width: 100px">Email</th>
                   <th class="text-center" style="width: 100px">Mobile No.</th>
                   <th class="text-center" style="width: 200px">Address</th>
                   <th class="text-center" style="width: 100px">Order Date </th>
                   <th class="text-center" style="width: 100px">Image</th>
                   <th class="text-center" style="width: 100px">Status</th>
                                    <!-- <th class="text-center">action</th> -->
                                  </tr>
                                    </thead>
                                    <tbody class="text-center" style="width: 100px" >
                            <?php $i=0; foreach ($getorders as $order){
                                $order1=$order->order_id;
                            
                           $order_product=$this->Api_model->getDataByThreeTable1($order1);
                         
                           // $food_product=$this->Api_model->getSingleRow('food_product', array('p_id '=>$order_product->product_id));
                           //  $pro_images=$this->Api_model->getSingleRow('pro_images', array('p_id '=>$order_product->product_id));

                            $i++; ?>
                             

                        <tr>
                        <td><?php echo $i; ?></td>
                        <td><?php echo ucfirst($this->Api_model->get_user($order->user_id)->first_name); ?></td>

                        <td><?php echo ucfirst($order_product[0]->p_name); ?></td>
                        <!-- <td><?php echo $food_product->quantity; ?></td>  -->
                        <td><?php if($order->payment_status==1){ ?>
                        <label class="badge badge-teal">Paid</label>
                        <?php }elseif($order->payment_status==0){ ?>
                        <label class="badge badge-danger">Unpaid</label>
                        <?php } ?></td>
                         <td><?php echo number_format($order->final_price,2); ?></td>
                         <td><?php echo $order->name; ?></td>
                         <td><?php echo $order->email; ?></td>
                        <td><?php echo $order->mobile_no; ?></td>

                        <td><?php echo wordwrap($order->address,30,"<br>\n"); ?></td>
                        <td><?php echo $order->order_date; ?></td>
                       
                        <td class="py-1">
                        <?php if( $order_product[0]->image){ ?>
                        <img style="border-radius: 50%;width: 40px;height: 40px;" src="<?php echo  base_url().$order_product[0]->image; ?>" width="80" height="80" alt="image"/>
                        <?php }else{ ?>
                        <img style="border-radius: 50%;width: 40px;height: 40px;" src="<?php echo  base_url('/assets/images/faces-clipart/userblank.png'); ?>" width="80" height="80" alt="image"/>

                        <?php } ?>
                        </td>
                        <td><?php if($order->status==2){ ?>
                        <label class="badge badge-teal">Delivered</label>
                        <?php }elseif($order->status==1){ ?>
                        <label class="badge badge-danger">Confirmed</label>
                    	<?php }elseif($order->status==6){ ?>
                        <label class="badge badge-danger">Pending</label>
                        <?php } ?>
                        <?php if($order->status==3){ ?>
                        <label class="badge badge-teal">Packed</label>
                        <?php }elseif($order->status==4){ ?>
                        <label class="badge badge-danger">Dispatched</label>
                        <?php }elseif($order->status==5){ ?>
                        <label class="badge badge-teal">Cancelled</label>
                        <?php } ?>
                         </td>
                        <!-- <td class="text-center">
                        <?php if($order->status==2){ ?>
                         <a onclick="return confirm('Are you sure you want to pending this order?')" title="Pending" class="btn btn-info btn-outline btn-circle btn-lg m-r-5" href="<?php echo base_url('/Admin/change_status_order');?>?id=<?php echo $order->id; ?>&status=1&request=1"><i class="fa fa-reply fa-fw"></i></a>
                         <?php }else{ ?>
                         <a title="Delivered" onclick="return confirm('Are you sure you want to delivered this order?')"  class="btn btn-info btn-outline btn-circle btn-lg m-r-5" href="<?php echo base_url('/Admin/change_status_order');?>?id=<?php echo $order->id; ?>&status=2&request=2"><i class="fa fa-history fa-fw"></i></a>
                   <?php } ?>

                    </td> -->
                    </tr>

                    <?php } ?>
                   </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
               
                <!-- ============================================================== -->
                <!-- End Right sidebar -->
                <!-- ============================================================== -->
            </div>


