<!-- $pages=$page; -->
<body class="fix-header">
    <!-- ============================================================== -->
    <!-- Preloader -->
    <!-- ============================================================== -->
    <div class="preloader">
        <svg class="circular" viewBox="25 25 50 50">
            <circle class="path" cx="50" cy="50" r="20" fill="none" stroke-width="2" stroke-miterlimit="10" />
        </svg>
    </div>
    <!-- ============================================================== -->
    <!-- Wrapper -->
    <!-- ============================================================== -->
    <div id="wrapper">
        <!-- ============================================================== -->
        <!-- Topbar header - style you can find in pages.scss -->
        <!-- ============================================================== -->
        <nav class="navbar navbar-default navbar-static-top m-b-0">
            <div class="navbar-header">
                <div class="top-left-part">
                    <!-- Logo -->
                    <a class="logo" href="<?php echo base_url();?>Admin">
                        <!-- Logo icon image, you can use font-icon also --><b>
                        <!--This is dark logo icon--><img src="<?php echo base_url();?>assets/plugins/images/favicon.png" alt="PETEATS" class="dark-logo" /><!--This is light logo icon--><img src="<?php echo base_url();?>assets/plugins/images/admin-logo-dark.png" alt="PETEATS" class="light-logo" />
                     </b>
                        <!-- Logo text image you can use text also --><span class="hidden-xs">
                        <!--This is dark logo text--><img style="width: 55%;" src="<?php echo base_url();?>assets/plugins/images/peteats.png" alt="PETEATS" class="dark-logo" /><!--This is light logo text--><img src="<?php echo base_url();?>assets/plugins/images/admin-text-dark.png" alt="PETEATS" class="light-logo" />
                     </span> </a>
                </div>
                <!-- /Logo -->
                
                <ul class="nav navbar-top-links navbar-right pull-right">
                   <li class="dropdown">
                        <a class="dropdown-toggle profile-pic" data-toggle="dropdown" href="#">
                         <img src="<?php echo base_url();?>assets/plugins/images/users/dummy.jpg" alt="user-img" width="36" class="img-circle"><b class="hidden-xs">
                            <?php echo $this->session->userdata('name');?></b>
                            <span class="caret"></span> </a>
                        <ul class="dropdown-menu dropdown-user animated flipInY">
                            <li>
                                <div class="dw-user-box">
                                    <div class="u-img"><img src="<?php echo base_url();?>assets/plugins/images/users/dummy.jpg" alt="user" /></div>
                                    <div class="u-text">
                                        <h4><?php echo $this->session->userdata('name');?></h4>
                                        <p class="text-muted"><?php echo $this->session->userdata('email');?></p><!-- <a href="profile.html" class="btn btn-rounded btn-danger btn-sm">View Profile</a> --></div>
                                </div>
                            </li>
                           <li role="separator" class="divider"></li>
                            <li><a href="<?php echo base_url();?>Admin/logout"><i class="fa fa-power-off"></i> Logout</a></li>
                        </ul>
                        <!-- /.dropdown-user -->
                    </li>
                    <!-- /.dropdown -->
                </ul>
            </div>
            <!-- /.navbar-header -->
            <!-- /.navbar-top-links -->
            <!-- /.navbar-static-side -->
        </nav>
        <!-- End Top Navigation -->
        <!-- ============================================================== -->
        <!-- Left Sidebar - style you can find in sidebar.scss  -->
        <!-- ============================================================== -->
        <div class="navbar-default sidebar" role="navigation">
            <div class="sidebar-nav slimscrollsidebar">
                <div class="sidebar-head">
                    <h3><span class="fa-fw open-close"><i class="ti-menu hidden-xs"></i><i class="ti-close visible-xs"></i></span> <span class="hide-menu">PetInfo</span></h3> </div>
                <ul class="nav" id="side-menu">
                 <li> <a href="<?php echo base_url('Admin/index'); ?>" class="waves-effect"><i class="mdi mdi-av-timer fa-fw" data-icon="v"></i> <span class="hide-menu"> Dashboard </span></a>
                 </li> 
                    
                         <li class="devider"></li>

                  <li> <a href="javascript:void(0);" class="waves-effect"><i class="mdi mdi-clipboard-text fa-fw"></i> <span class="hide-menu">Home Page<span class="fa arrow"></span></span></a>
                       <ul class="nav nav-second-level">

                            <li><a href="<?php echo base_url('Admin/all_banners'); ?>"><i class="fa-fw">B</i><span class="hide-menu">Banner</span></a></li>
                            <li><a href="<?php echo base_url('Admin/viewoffer'); ?>"><i class="fa-fw">T</i><span class="hide-menu">Top Offers</span></a></li>
                          <li><a href="<?php echo base_url('Admin/contact_information'); ?>"><i class="fa-fw">C</i><span class="hide-menu">Contact</span></a></li>
                            <li><a href="<?php echo base_url('Admin/viewbreed_information'); ?>"><i class="fa-fw">B</i><span class="hide-menu">Breed Information</span></a></li>

                        </ul>
                        </li>






                                    <!--     <li class="devider"></li>
                  <li> <a href="<?php echo base_url('Admin/all_banners'); ?>" class="waves-effect"><i class="fa fa-user" data-icon="v"></i> <span class="hide-menu" style="margin-left: 10px;"> Home Page</span></a>
                    </li> -->
                                 <li class="devider"></li>
                    <li> <a href="<?php echo base_url('Admin/user'); ?>" class="waves-effect"><i class="fa fa-user" data-icon="v"></i> <span class="hide-menu" style="margin-left: 10px;"> All Users </span></a>
                    </li>
                    
                    <li class="devider"></li>
                    <li> <a href="javascript:void(0);" class="waves-effect"><i class="mdi mdi-clipboard-text fa-fw"></i> <span class="hide-menu">Pet<span class="fa arrow"></span></span></a>
                        <ul class="nav nav-second-level">
                        <li>
                         <li><a href="<?php echo base_url('Admin/all_pets'); ?>"><i class="fa-fw">A</i><span class="hide-menu">All Pets</span></a></li>
                            
                         <!-- <li><a href="<?php echo base_url('Admin/sell_pets_details'); ?>"><i class="fa-fw">S</i><span class="hide-menu">Sell Pet</span></a></li> -->
                         <!--  <li><a href="<?php echo base_url('Admin/pet_marcket_details'); ?>"><i class="fa-fw">P</i><span class="hide-menu">Pet Market</span></a></li>  -->
                         

                     </li>
                       </ul>



                    </li>
                    <li class="devider"></li>
                    <li> <a href="javascript:void(0);" class="waves-effect"><i class="mdi mdi-clipboard-text fa-fw"></i> <span class="hide-menu">Product<span class="fa arrow"></span></span></a>
                        <ul class="nav nav-second-level">
                            <li><a href="<?php echo base_url('Admin/pet_type_details'); ?>"><i class="fa-fw">C</i><span class="hide-menu">Category</span></a></li>
                            <li><a href="<?php echo base_url('Admin/addCategory'); ?>"><i class="fa-fw">S</i><span class="hide-menu">Sub Category</span></a></li>
                            <li><a href="<?php echo base_url('Admin/addFoodCompany'); ?>"><i class="fa-fw">B</i><span class="hide-menu">Brand</span></a></li>

                            <li><a href="<?php echo base_url('Admin/addproduct'); ?>"><i class="fa-fw">A</i><span class="hide-menu">Add Product</span></a></li>
                            <li><a href="<?php echo base_url('Admin/productlist'); ?>"><i class="fa-fw">V</i><span class="hide-menu">View Product</span></a></li>
                            <li><a href="<?php echo base_url('Admin/order'); ?>"><i class="fa-fw">O</i><span class="hide-menu">Order</span></a></li>
                            
                        </ul>
                    </li>

                    <li class="devider"></li>

                    <li> <a href="javascript:void(0);" class="waves-effect"><i class="mdi mdi-table fa-fw"></i> <span class="hide-menu">Breed<span class="fa arrow"></span></span></a>
                        <ul class="nav nav-second-level">
                          <li><a href="<?php echo base_url('Admin/addbreed'); ?>"><i class="fa-fw">A</i><span class="hide-menu">Add breed</span></a></li>
                            <li><a href="<?php echo base_url('Admin/breedList'); ?>"><i class="fa-fw">V</i><span class="hide-menu">View breed</span></a></li>
                           
                            
                        </ul>
                    </li>
                   
                     <li class="devider"></li>

                    <li> <a href="javascript:void(0);" class="waves-effect"><i class="mdi fa-fw mdi-near-me"></i> <span class="hide-menu">Near By<span class="fa arrow"></span></span></a>
                        <ul class="nav nav-second-level">
                        <li><a href="<?php echo base_url('Admin/Veterinary'); ?>"><i class="fa-fw">V</i><span class="hide-menu">Veterinary</span></a></li>
                        <li><a href="<?php echo base_url('Admin/Salon'); ?>"><i class="fa-fw">P</i><span class="hide-menu">Pet Spa</span></a></li>
                         <li><a href="<?php echo base_url('Admin/Shop'); ?>"><i class="fa-fw">P</i><span class="hide-menu">Pet Shop</span></a></li>
                         <li><a href="<?php echo base_url('Admin/Hostels'); ?>"><i class="fa-fw">H</i><span class="hide-menu">Hostels</span></a></li>   
                        <li><a href="<?php echo base_url('Admin/Trainer'); ?>"><i class="fa-fw">T</i><span class="hide-menu">Trainer</span></a></li>   
                        </ul>
                    </li>

                    <li class="devider"></li>
                    <li> <a href="<?php echo base_url('Admin/post'); ?>" class="waves-effect"><i class="mdi mdi-chart-bar fa-fw"></i> <span class="hide-menu"> All Posts </span></a>       
                    </li>
                    <li class="devider"></li>
                    <li> <a href="javascript:void(0);" class="waves-effect"><i  class="mdi mdi-settings fa-fw"></i> <span class="hide-menu">Setting<span class="fa arrow"></span></span></a> 
                    <ul class="nav nav-second-level">
                            <li><a href="<?php echo base_url('Currency/setting'); ?>"><i class="fa-fw">C</i><span class="hide-menu">Currency setting</span></a></li>
                            
                
                            <li><a href="<?php echo base_url('Admin/app_setting'); ?>"><i class="fa-fw">A</i><span class="hide-menu">App setting</span></a></li>
                            <li><a href="<?php echo base_url('Admin/key_setting'); ?>"><i class="fa-fw">K</i><span class="hide-menu">Key setting</span></a></li>
                            
                        </ul>
                    </li>
                 <li class="devider"></li>

                    <li> <a href="<?php echo base_url('Admin/notification'); ?>" class="waves-effect"><i class="far fa-bell"></i><span class="hide-menu" style="margin-left: 10px;"> Notifications </span></a> </li>
                    <li class="devider"></li>
                </ul>
            </div>
        </div>
