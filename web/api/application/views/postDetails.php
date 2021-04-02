<div id="page-wrapper">
   <div class="container-fluid">
               
                <!-- ============================================================== -->
                <!-- Other sales widgets -->
                <!-- ============================================================== -->
                <!-- .row -->
      <div class="container">
      <div class="row">
      <div class="white-box">
            <ul class="nav nav-tabs tabs customtab">
                    <li class="tab active">
                        <a href="#home" data-toggle="tab"> <span class="visible-xs"><i class="fa fa-home"></i></span> <span class="hidden-xs">Profile</span> </a>
                    </li>
                    <li class="tab">
                      <a href="#profile" data-toggle="tab"> <span class="visible-xs"><i class="fa fa-user"></i></span> <span class="hidden-xs">Comments</span> </a>
                  </li>
                  <li class="tab">
                      <a href="#abuse" data-toggle="tab"> <span class="visible-xs"><i class="fa fa-user"></i></span> <span class="hidden-xs">Abuse Post</span> </a>
                  </li>

              </ul>
              <div class="tab-content">
                <div class="tab-pane active" id="home">
                <div class="row">
                  <div class="col-md-3">
                  <div class="user-bg"> <img width="100%" alt="user" src="<?php echo base_url(); ?>assets/plugins/images/large/img1.jpg">
                                <div class="overlay-box">
                                    <div class="user-content">
                                        <a href="javascript:void(0)"><img src="<?php echo $post['user_image']; ?>" class="thumb-lg img-circle" alt="img"></a>
                                        <h4 class="text-white">Title</h4>
                                        <h5 class="text-white"><?php echo ucfirst($post['title']); ?></h5> </div>
                                </div>
                            </div>
                          </div>
                          <div class="col-md-9">
                         <div class="col-md-3 col-xs-6 b-r"> <strong>Description</strong>
                                <br>
                                <p class="m-t-30"><?php echo ucfirst($post['content']); ?></p>
                            </div>
                        </div>
                        </div>
                  </div>

                    <div class="tab-pane" id="profile">
                      <div style="text-align: right;cursor: pointer;"><a href="<?php echo base_url('Admin/post'); ?>"><button class="btn btn-primary">Go Back</button></a></div>
                      
                  <table id="demo-foo-accordion myTable" class="table m-b-0 toggle-arrow-tiny" data-paging="true" data-paging-size="7">
                                        <thead>
                                            <tr>
                                                <th data-toggle="true"> Customer Name</th>
                                                <th> Comments </th>
                                                <th data-hide="all"> Commented On </th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                          <?php  $count=1;
                                           foreach ($all_comments as $get_comments) {

                                            ?>
<!--                                             <?php print_r($get_comments)?>
 -->                                            <tr>
                                                <td><?php echo $this->Api_model->get_user($get_comments->user_id)->first_name; ?></td>
                                                <td><?php echo $get_comments->content; ?></td>
                                                <td><?php echo date('d M Y H:i:s',$get_comments->createAt); ?></td>
                                                
                                             <td class="text-center">
                  <a title="Delete" onclick="return confirm('Are you sure you want to delete this comment?')" href="deleteComment/<?php echo $get_comments->commentID; ?>" class="btn btn-info btn-outline btn-circle btn-lg m-r-5"><i class="icon-trash"></i></a> </td>   
                                           </tr>
                                             <?php
                                                 }
                                              ?> 
                                        </tbody>
                                    </table>

                                    
                    </div>
                    <div class="tab-pane" id="abuse">
                      <div style="text-align: right;cursor: pointer;"><a href="<?php echo base_url('Admin/post'); ?>"><button class="btn btn-primary">Go Back</button></a></div>
                  <table id="demo-foo-accordion myTable" class="table m-b-0 toggle-arrow-tiny" data-paging="true" data-paging-size="7">

                    
                    
                                        <thead>
                                            <tr>
                                              <th data-toggle="true"> Customer Name</th>
                                                <th> Reason  </th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                          <?php  $count=1;
                                           foreach ($all_abuse_post as $abuse_post) {
                                            ?>
                                            <tr>
                                                <td><?php echo $abuse_post->userName; ?></td>
                                                <td><?php echo $abuse_post->reason ; ?></td>
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
