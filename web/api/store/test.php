<?php
 $products = ["[variant_id='2168746246244', quantity='4', title='petu', price='1000.00', variant_id='2182707314752', quantity='1', title='1 Pcs Bird Parrot Toys Natural Wooden Grass Chewing Bite Hanging Cage Accessories Bell Swing Climb Chew Toys Bird Supplies', price='2.60', variant_id='2182707773504', quantity='3', title='1 PC 20ml Parrot Feeding Syringe Parrots Bird Feeding Syringe With 6PCS Curved Gavage Tubes', price='22.00']","[variant_id='2168746246244', quantity='4', title='petu', price='1000.00', variant_id='2182707314752', quantity='1', title='1 Pcs Bird Parrot Toys Natural Wooden Grass Chewing Bite Hanging Cage Accessories Bell Swing Climb Chew Toys Bird Supplies', price='2.60', variant_id='2182707773504', quantity='3', title='1 PC 20ml Parrot Feeding Syringe Parrots Bird Feeding Syringe With 6PCS Curved Gavage Tubes', price='22.00']","[variant_id='2168746246244', quantity='4', title='petu', price='1000.00', variant_id='2182707314752', quantity='1', title='1 Pcs Bird Parrot Toys Natural Wooden Grass Chewing Bite Hanging Cage Accessories Bell Swing Climb Chew Toys Bird Supplies', price='2.60', variant_id='2182707773504', quantity='3', title='1 PC 20ml Parrot Feeding Syringe Parrots Bird Feeding Syringe With 6PCS Curved Gavage Tubes', price='22.00']"];
 $a = json_encode($products);
 

 $b = json_decode($a);

 print_r($b);
?>