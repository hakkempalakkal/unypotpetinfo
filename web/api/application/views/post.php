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
                            <div class="panel-heading">All Post</div>
                            <div class="table-responsive">
                        <table class="table table-hover manage-u-table" id="myTable">
                              <thead>
                              <tr>
                              <th>#</th>
                              <th>Title</th>
                              <th>Content</th>
                              <th>Created At </th>
                              <th class="text-center">Action</th>
                               </tr>
                              </thead>
                              <tbody>
                              <?php  $count=1; foreach ($post as $post) {
                               ?>
                              <tr>
                              <td><?=  $count++; ?></td>
                              <td class="py-1">
                              <?php echo $post->title; ?>
                              </td>
                              <td>
                              <?php echo $post->content; ?>
                              </td>
                              <td><?php echo date('M d, Y H:i:s', $post->createAt); ?>
                                
                              </td>
                              <td class="text-center">
                              <a title="View" class="btn btn-info btn-outline btn-circle btn-lg m-r-5" href="<?php echo base_url('/Admin/postDetails');?>?id=<?php echo $post->postID; ?>&post_title=<?php echo $post->title; ?>" ><i class="fa fa-eye"></i></a>
                              
                               <a title="Delete" onclick="return confirm('Are you sure you want to delete this post?')" href="deletePost/<?php echo    $post->postID; ?>" class="btn btn-info btn-outline btn-circle btn-lg m-r-5"><i class="icon-trash"></i></a> 
                              


                              </td>
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