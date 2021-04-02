<div id="page-wrapper">
            <div class="container-fluid">
                
                 
                <!-- .row -->
               
                
               
                <!-- ============================================================== -->
                <!-- Demo table -->
                <!-- ============================================================== -->
                <div class="row">
                    <div class="col-md-12">
                        <div class="panel">
                            <div class="panel-heading">All Product</div>
                            <div class="table-responsive">
                                <table class="table table-hover manage-u-table" id="myTable">
                                    <thead>
                                     <tr>
                                  <th> # </th>
                                <th>Product Name</th>
                                <th>Category Name</th>
                                <th>Sub-Category Name</th>
                                <th>Brand Name</th>
                                 <th>Product Image</th>
                                <th class="text-center">Action</th>
                                    </tr>
                                    </thead>
                                    <tbody>

   <?php $count=1;foreach ($product_list as $products) {  
           $pro_images= $this->Api_model->getSingleRow('pro_images',array('p_id'=>$products['p_id']));?>
          
              <tr>
                 <td class="py-1">
                  <?php echo $count++; ?>
                </td>
                <td class="py-1">
                  <?php echo $products['p_name']; ?>
                </td>
               <td>
                  <?php echo $products['pet_name']; ?>
                </td>
                 <td>
                  <?php echo $products['cat_title']; ?>
                </td>
                <td>
                  <?php echo $products['c_name']; ?>
                </td> 

                  <td ><?php if(count($pro_images)>0 && $pro_images->image!=""){?>
                    <img style="border-radius: 50%;width: 40px;height: 40px;" src="<?= base_url(); ?><?php  echo $pro_images->image; ?>" alt="Image not Available"/>


                  <?php }else{?>
                    <img  style="border-radius: 50%;width: 40px;height: 40px;" src="<?= base_url(); ?>assets/images/faces-clipart/product.png" alt="Image not Available"/>

                </td>

             <?php } ?>
                
                <td class="text-center">
                        
                        <a title="Edit" class="btn btn-info btn-outline btn-circle btn-lg m-r-5" href="updateproduct/<?php echo  $products['p_id']; ?>"><i class="fa fa-edit fa-fw"></i></a>

                         <a title="Details" class="btn btn-info btn-outline btn-circle btn-lg m-r-5" onclick="showProductInfo('myModal<?php echo $products['p_id']; ?>')"><i class="fa fa-eye fa-fw"></i></a>
       
                         <a title="Delete" onclick="return confirm('Are you sure you want to delete this product?<?php print_r($products['p_id'])?>')" href="deleteProduct/<?php echo  $products['p_id']; ?>" class="btn btn-info btn-outline btn-circle btn-lg m-r-5"><i class="icon-trash"></i></a> 
                       

                    </td>
           


               <!-- Modal -->
<div id="myModal<?php echo $products['p_id']; ?>" class="modal fade" role="dialog">
  <div class="modal-dialog">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">

      <h4><b>Product Details</b></h4>
       <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
              <div class="row">

                <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                 <div class="row">

                  <div class="col-md-4">
                     <label class="text-bold" >Product Code</label>
                  </div>
                 <div class="col-md-7">
                  <span><?php echo $products['p_code']; ?></span>
                   </div>
                </div>
               
                 <div class="row">
                  <div class="col-md-4">
                    <label>Product Name</label>
                      </div>
                         <div class="col-md-7">
                     <span><?php echo $products['p_name']; ?></span>
                     </div>
                </div>
                     </div>
                   </div>

    
                     <div class="row">
                    <div class="col-md-4">
                      <label>Product Decription </label>
                        </div>
                    <div class="col-md-7">
                    <span><?php echo $products['p_description']; ?></span>
          </div>
                  </div>

                <div class="row">
                  <div class="col-md-4">
                    <label>Category</label>
                      </div>
                         <div class="col-md-7">
                     <span><?php echo $products['pet_name']; ?></span>
                     </div>
                 </div>
                
                 <div class="row">
                  <div class="col-md-4">
                    <label>Product Sub Category</label>
                      </div>
                         <div class="col-md-7">
                     <span><?php echo $products['cat_title']; ?></span>
                     </div>
                 </div>
     
                <div class="row">
                  <div class="col-md-4">
                    <label>Brand</label>
                      </div>
                         <div class="col-md-7">
                     <span><?php echo $products['c_name']; ?></span>
                     </div>
                 </div>
                 
                 
                 <div class="row">
                  <div class="col-md-4">
                    <label>Product Type</label>
                      </div>
                         <div class="col-md-7">
                     <span><?php echo $products['p_cat_type']; ?></span>
                     </div>
                 </div>

                 <div class="row">
                  <div class="col-md-4">
                    <label>Quantity </label>
                      </div>
                         <div class="col-md-7">
                     <span><?php echo $products['quantity']; ?></span>
                     </div>
                 </div>
                 
                 <!--  <div class="row">
                  <div class="col-md-4">
                    <label>Availability</label>
                      </div>
                         <div class="col-md-7">
                     <span><?php echo $products['quantity']; ?></span>
                     </div>
                 </div> -->


                 <div class="row">
                  <div class="col-md-4">
                    <label>Size</label>
                      </div>
                         <div class="col-md-7">
                     <span><?php echo $products['size']; ?></span>
                     </div>
                 </div>

                  <div class="row">
                  <div class="col-md-4">
                    <label>Weight</label>
                      </div>
                         <div class="col-md-7">
                     <span><?php echo $products['weight']; ?></span>
                     </div>
                 </div>

                <div class="row">
                  <div class="col-md-4">
                    <label>MRP</label>
                      </div>
                         <div class="col-md-7">
                     <span>Rs. <?php echo $products['p_rate']; ?>/-</span>
                     </div>
                 </div>

                   <div class="row">
                  <div class="col-md-4">
                    <label>Selling Price</label>
                      </div>
                         <div class="col-md-7">
                     <span>Rs. <?php echo $products['p_rate_sale']; ?>/-</span>
                     </div>
                 </div>
   
                

                <div class="row">
                  <div class="col-md-4">
                    <label>Discount</label>
                      </div>
                         <div class="col-md-7">
                     <span><?php echo $products['discount']; ?>%</span>
                     </div>

                 </div>
                  <div class="row">
                  <div class="col-md-4">
                    <label>Shipping cost</label>
                      </div>
                         <div class="col-md-7">
                     <span>Rs. <?php echo $products['shipping_cost']; ?>/-</span>
                     </div>
                     
                 </div>
                 <div class="row">
                  <div class="col-md-4">
                    <label>Product Image</label>
                      </div>
                         <div class="col-md-7">
     <img style="width: 100px;height: 100px;" src="<?= base_url(); ?><?php  echo $pro_images->image; ?>" alt="Image not Available"/>
                     </div>
                 </div>
               
               </div>
              </div>
             </div>
            </div>

          </div>
        </div>
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
           </div>


<script type="text/javascript">
  function showProductInfo(id)
  {
 $("#"+id).modal("show");
  }
</script>