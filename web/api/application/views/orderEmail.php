<?php 

$order_products= $this->Api_model->getAllDataWhere(array('order_id'=>$order_id), 'order_products');
?>
<!DOCTYPE html>
<html>
<body>
    <div class="email_data" style="text-align: center;align-content: center;color: black;
            font-size: 18px;">
        
        <p>Warm Greetings! If you have any queries or suggestions, just get in touch with us - peteatsind@gmail.com</p>
       <!--  <p><?php  echo $msg; ?></p> -->
       <p><table class="section_wrapper main" data-id="main" id="section-main" border="0" width="100%" cellpadding="0" cellspacing="0" align="center" style="min-width: 100%;" role="presentation" bgcolor="#ffffff">
                          <tbody><tr>
                            <td class="section_wrapper_th" style="mso-line-height-rule: exactly;" bgcolor="#ffffff">
                              <table border="0" width="100%" cellpadding="0" cellspacing="0" align="center" style="min-width: 100%;" id="mixContainer" role="presentation">
                                <!-- BEGIN SECTION: Heading -->
                                <tbody><tr id="section-1468266" class="section heading">
                                  <th style="mso-line-height-rule: exactly; color: #4b4b4b; padding: 26px 52px 13px;" bgcolor="#ffffff">
                                    <table cellspacing="0" cellpadding="0" border="0" width="100%" role="presentation" style="color: #4b4b4b;" bgcolor="#ffffff">
                                      <tbody><tr style="color: #4b4b4b;" bgcolor="#ffffff">
                                        <th style="mso-line-height-rule: exactly; color: #4b4b4b;" bgcolor="#ffffff" valign="top">
                                          <h1 data-key="1468266_heading" style="font-family: Georgia,serif,'Playfair Display'; font-size: 28px; line-height: 46px; font-weight: 700; color: #4b4b4b; text-transform: none; background-color: #ffffff; margin: 0;">Order Confirmation</h1>
                                        </th>
                                      </tr>
                                    </tbody></table>
                                  </th>
                                </tr>
                                <!-- END SECTION: Heading -->
                                <!-- BEGIN SECTION: Introduction -->
                                <tr id="section-1468267" class="section introduction">
                                  <th style="mso-line-height-rule: exactly; padding: 13px 52px;" bgcolor="#ffffff">
                                    
                                      <p style="mso-line-height-rule: exactly; font-family: -apple-system,BlinkMacSystemFont,'Segoe UI',Arial,'Karla'; font-size: 16px; line-height: 26px; font-weight: 400; color: #666363; margin: 0 0 13px;" align="center">
                                        <span data-key="1468267_greeting_text" style="text-align: center; font-family: -apple-system,BlinkMacSystemFont,'Segoe UI',Arial,'Karla'; font-size: 16px; line-height: 26px; font-weight: 400; color: #666363;">
                                          Hey
                                        </span>
                                        <?php echo $name?>,
                                      </p>
                                    
                                    
                                    <p data-key="1468267_introduction_text" class="text" style="mso-line-height-rule: exactly; font-family: -apple-system,BlinkMacSystemFont,'Segoe UI',Arial,'Karla'; font-size: 16px; line-height: 26px; font-weight: 400; color: #666363; margin: 13px 0;" align="center">
                                    </p>
                                    <p style="mso-line-height-rule: exactly; font-family: -apple-system,BlinkMacSystemFont,'Segoe UI',Arial,'Karla'; font-size: 16px; line-height: 26px; font-weight: 400; color: #666363; margin: 13px 0;" align="center">We've got your order! Your world is about to look a whole lot better.</p>
                                    <p style="mso-line-height-rule: exactly; font-family: -apple-system,BlinkMacSystemFont,'Segoe UI',Arial,'Karla'; font-size: 16px; line-height: 26px; font-weight: 400; color: #666363; margin: 13px 0;" align="center">We'll drop you another email when your order ships.</p>
                                  </th>
                                </tr>
       	<table class="table-inner" cellspacing="0" cellpadding="0" border="0" width="100%" style="min-width: 100%;" role="presentation">
   <tbody>
      <tr>
         <th class="product-table" style="mso-line-height-rule: exactly;" bgcolor="#ffffff" valign="top">
            <table cellspacing="0" cellpadding="0" border="0" width="100%" style="min-width: 100%;" role="presentation">
               <tbody>
                  <tr>
                     <th colspan="2" class="product-table-h3-wrapper" style="mso-line-height-rule: exactly;" bgcolor="#ffffff" valign="top">
                        <h3 data-key="1468271_item" style="font-family: -apple-system,BlinkMacSystemFont,'Segoe UI',Arial,'Karla'; color: #bdbdbd; font-size: 16px; line-height: 52px; font-weight: 700; text-transform: uppercase; border-bottom-width: 2px; border-bottom-color: #dadada; border-bottom-style: solid; letter-spacing: 1px; margin: 0;" align="left">Items ordered</h3>
                     </th>
                  </tr>
                  <!-- Bold 2 -->
                  <!-- end Bold 2 -->
                  <?php if($order_products)
                  {
                  	foreach($order_products as $order_products_data)
                  	{
                  	$product= $this->Api_model->getSingleRow('food_product', array('p_id'=>$order_products_data->product_id));
                  	$pro_images= $this->Api_model->getSingleRow('pro_images',array('p_id'=>$product->p_id));
                  	
                   ?>
                  <tr class="row-border-bottom">
                     <th class="table-stack product-image-wrapper stack-column-center" width="1" style="mso-line-height-rule: exactly; border-bottom-width: 2px; border-bottom-color: #dadada; border-bottom-style: solid; padding: 13px 13px 13px 0;" bgcolor="#ffffff" valign="middle">
                        <img width="140" class="product-image" src="http://phpstack-225750-688566.cloudwaysapps.com/<?php echo $pro_images->image; ?>" alt="Product Image" style="vertical-align: middle; text-align: center; width: 140px; max-width: 140px; height: auto !important; border-radius: 1px; padding: 0px;">
                     </th>
                     <th class="product-details-wrapper table-stack stack-column" style="mso-line-height-rule: exactly; padding-top: 13px; padding-bottom: 13px; border-bottom-width: 2px; border-bottom-color: #dadada; border-bottom-style: solid;" bgcolor="#ffffff" valign="middle">
                        <table cellspacing="0" cellpadding="0" border="0" width="100%" style="min-width: 100%;" role="presentation">
                           <tbody>
                              <tr>
                                 <th class="line-item-description" style="mso-line-height-rule: exactly; font-family: -apple-system,BlinkMacSystemFont,'Segoe UI',Arial,'Karla'; font-size: 16px; line-height: 26px; font-weight: 400; color: #666363; padding: 13px 6px 13px 0;" align="left" bgcolor="#ffffff" valign="top">
                                    <p style="mso-line-height-rule: exactly; font-family: -apple-system,BlinkMacSystemFont,'Segoe UI',Arial,'Karla'; font-size: 16px; line-height: 26px; font-weight: 400; color: #666363; margin: 0;" align="left">
                                       <a href="https://us.tens.co/tools/emails/click/order-confirmation/1/product/link?url=https%3A%2F%2Fus.tens.co%2Fproducts%2Ftravel-case" target="_blank" style="color: #666363; text-decoration: none !important; text-underline: none; word-wrap: break-word; text-align: left !important; font-weight: bold;">
                                       <?php echo $product->p_name;?>
                                       </a>
                                       <br>
                                       <span class="muted" style="text-align: center; font-family: -apple-system,BlinkMacSystemFont,'Segoe UI',Arial,'Karla'; font-size: 14px; line-height: 26px; font-weight: normal; color: #bdbdbd; word-break: break-all;">
                                      <?php echo $product->color;?>
                                       </span>
                                    </p>
                                 </th>
                                 <th style="mso-line-height-rule: exactly;" bgcolor="#ffffff" valign="top"></th>
                                 <th class="right line-item-qty" width="1" style="mso-line-height-rule: exactly; white-space: nowrap; padding: 13px 270px 13px 13px;" align="right" bgcolor="#ffffff" valign="top">
                                    <p style="mso-line-height-rule: exactly; font-family: -apple-system,BlinkMacSystemFont,'Segoe UI',Arial,'Karla'; font-size: 16px; line-height: 26px; font-weight: 400; color: #666363; margin: 0;" align="right">
                                      Quantity :- <?php echo $order_products_data->quantity; ?>
                                    </p>
                                 </th>
                                 <th class="right line-item-line-price" width="1" style="mso-line-height-rule: exactly; white-space: nowrap; padding: 13px 0 13px 26px;" align="right" bgcolor="#ffffff" valign="top">
                                    <p style="mso-line-height-rule: exactly; font-family: -apple-system,BlinkMacSystemFont,'Segoe UI',Arial,'Karla'; font-size: 16px; line-height: 26px; font-weight: 400; color: #666363; margin: 0;" align="right">
                                       $<?php echo $order_products_data->total_price; ?>
                                    </p>
                                 </th>
                              </tr>
                           </tbody>
                        </table>
                     </th>
                  </tr>
                  <tr>
                     <th colspan="2" class="product-empty-row" style="mso-line-height-rule: exactly;" bgcolor="#ffffff" valign="top"></th>
                  </tr>
              <?php } }?>
                </tbody>
            </table>
         </th>
      </tr>
      <tr>
         <th class="pricing-table" style="mso-line-height-rule: exactly; padding: 13px 0;" bgcolor="#ffffff" valign="top">
            <table cellspacing="0" cellpadding="0" border="0" width="100%" style="min-width: 100%;" role="presentation">
               <tbody>
                  <tr>
                     <th class="table-title" style="mso-line-height-rule: exactly; font-family: -apple-system,BlinkMacSystemFont,'Segoe UI',Arial,'Karla'; font-size: 16px; line-height: 26px; font-weight: bold; color: #666363; width: 65%; padding: 6px 0;" align="left" bgcolor="#ffffff" valign="top">
                        <span data-key="1468271_discount" style="font-weight: bold;">Discount</span>
                     </th>
                     <th class="table-text" style="mso-line-height-rule: exactly; font-family: -apple-system,BlinkMacSystemFont,'Segoe UI',Arial,'Karla'; font-size: 16px; line-height: 26px; font-weight: 400; color: #666363; width: 35%; padding: 6px 0;" align="right" bgcolor="#ffffff" valign="middle">-$<?php echo $discount; ?></th>
                  </tr>
                  <tr>
                     <th class="table-title" data-key="1468271_subtotal" style="mso-line-height-rule: exactly; font-family: -apple-system,BlinkMacSystemFont,'Segoe UI',Arial,'Karla'; font-size: 16px; line-height: 26px; font-weight: bold; color: #666363; width: 65%; padding: 6px 0;" align="left" bgcolor="#ffffff" valign="top">Subtotal</th>
                     <th class="table-text" style="mso-line-height-rule: exactly; font-family: -apple-system,BlinkMacSystemFont,'Segoe UI',Arial,'Karla'; font-size: 16px; line-height: 26px; font-weight: 400; color: #666363; width: 35%; padding: 6px 0;" align="right" bgcolor="#ffffff" valign="middle">$<?php echo $final_price; ?></th>
                  </tr>
                  <tr>
                     <th class="table-title" style="mso-line-height-rule: exactly; font-family: -apple-system,BlinkMacSystemFont,'Segoe UI',Arial,'Karla'; font-size: 16px; line-height: 26px; font-weight: bold; color: #666363; width: 65%; padding: 6px 0;" align="left" bgcolor="#ffffff" valign="top">Shipping Charge</th>
                     <th class="table-text" style="mso-line-height-rule: exactly; font-family: -apple-system,BlinkMacSystemFont,'Segoe UI',Arial,'Karla'; font-size: 16px; line-height: 26px; font-weight: 400; color: #666363; width: 35%; padding: 6px 0;" align="right" bgcolor="#ffffff" valign="middle">$<?php echo $cod_charges; ?></th>
                  </tr>
                  <tr class="pricing-table-total-row">
                     <th class="table-title" data-key="1468271_total" style="mso-line-height-rule: exactly; font-family: -apple-system,BlinkMacSystemFont,'Segoe UI',Arial,'Karla'; font-size: 16px; line-height: 26px; font-weight: bold; color: #666363; width: 65%; padding: 6px 0;" align="left" bgcolor="#ffffff" valign="top">Total</th>
                     <th class="table-text" style="mso-line-height-rule: exactly; font-family: -apple-system,BlinkMacSystemFont,'Segoe UI',Arial,'Karla'; font-size: 16px; line-height: 26px; font-weight: 400; color: #666363; width: 35%; padding: 6px 0;" align="right" bgcolor="#ffffff" valign="middle">$<?php echo $final_price; ?></th>
                  </tr>
               </tbody>
            </table>
         </th>
      </tr>
      <tr id="section-1468273" class="section customer_and_shipping_address">
                                                  <!-- BEGIN : 2 COLUMNS : BILL_TO -->
          <th style="mso-line-height-rule: exactly; padding: 13px 52px;" bgcolor="#ffffff">
            <table border="0" width="100%" cellpadding="0" cellspacing="0" align="center" style="min-width: 100%;" role="presentation">
              <tbody><tr>
                <!-- BEGIN : Column 1 of 2 : BILL_TO -->
                <th width="50%" class="column_1_of_2 column_bill_to " style="mso-line-height-rule: exactly;" align="left" bgcolor="#ffffff" valign="top">
                  <table align="center" border="0" width="100%" cellpadding="0" cellspacing="0" style="min-width: 100%;" role="presentation">
                    <tbody><tr>
                      <th style="mso-line-height-rule: exactly; padding-right: 5%;" align="left" bgcolor="#ffffff" valign="top">
                        <h3 data-key="1468273_bill_to" style="font-family: -apple-system,BlinkMacSystemFont,'Segoe UI',Arial,'Karla'; color: #bdbdbd; font-size: 16px; line-height: 52px; font-weight: 700; text-transform: uppercase; letter-spacing: 1px; margin: 0;" align="left">Shipping Address</h3>
                      </th>
                    </tr>
                    <tr>
                      <th class="billing_address " style="mso-line-height-rule: exactly; padding-right: 5%;" align="left" bgcolor="#ffffff" valign="top">
                        <p style="mso-line-height-rule: exactly; font-family: -apple-system,BlinkMacSystemFont,'Segoe UI',Arial,'Karla'; font-size: 16px; line-height: 26px; font-weight: 400; color: #666363; margin: 0;" align="left"><?php echo $name; ?><br>
                            <?php echo $address; ?><br>
                            <?php echo $city; ?><br>
                            <?php echo $zip; ?><br>
                            <?php echo $country; ?><br>
                            <a href="mailto:" style="color: #ecba78; text-decoration: none !important; text-underline: none; word-wrap: break-word;" target="_blank"><?php echo $email; ?></a></p>
                      </th>
                    </tr>
                  </tbody></table>
                </th>
               
              </tr>
            </tbody></table>
          </th>
          <!-- END : 2 COLUMNS : SHIP_TO -->
        </tr>
   </tbody>
</table></p>
        <p>Team PETEATS</p>
        <img class="img-responsive image" style="width: 100%;height: auto;" src=" http://phpstack-225750-688566.cloudwaysapps.com/assets/images/banner.png">
    </div>
    
</body>
</html>
