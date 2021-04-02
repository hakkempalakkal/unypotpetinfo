<div class="main-panel">
      <div class="content-wrapper">    
        <div class="row">
            <div class="col-12 grid-margin">
              <div class="card">
                <div class="card-body">
                  <h5 class="card-title mb-4">Profile Details:</h5>
                  <div class="fluid-container">
                    <div class="row ticket-card mt-3 pb-2 border-bottom">
                      <div class="col-1">
                        <img class="img-sm rounded-circle" src="<?php echo base_url('/assets/images/faces/face1.jpg'); ?>" alt="profile image">
                      </div>
                      <div class="ticket-details col-12">
                         <div class="d-flex">
                          <p class="text-primary font-weight-bold mr-2 mb-0 no-wrap"><?php echo $user->name; ?> :</p>
                          <p class="font-weight-medium mr-1 mb-0">Description :</p>
                          <p class="font-weight-bold mb-0 ellipsis"><?php echo $user->description; ?></p>
                        </div>
                        <p class="text-small text-gray"><span class="font-weight-bold">Address :</span> <?php echo $user->address; ?></p>
                     
                        
                        <div class="row text-muted d-flex">
                        <div class="col-4 d-flex">
                            <p class="mb-0 mr-2 font-weight-bold">Mobile No. :</p>
                            <p class="Last-responded mr-2 mb-0"><?php echo $user->address; ?></p>
                          </div>
                          <div class="col-4 d-flex">
                            <p class="mb-0 mr-2 font-weight-bold">City :</p>
                            <p class="Last-responded mr-2 mb-0"><?php echo $user->address; ?></p>
                          </div>
                          <div class="col-4 d-flex">
                            <p class="mb-0 mr-2 font-weight-bold">State :</p>
                            <p class="Last-responded mr-2 mb-0"><?php echo $user->address; ?></p>
                          </div>
                        </div>
                      </div>
                      
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>