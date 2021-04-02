 <footer class="footer text-center"> <?php date('Y')?><a href="http://www.samyotech.com/" target="_blank"> &copy; Designed By PetInfo.</a></footer>
        </div>
        <!-- /#page-wrapper -->
    </div>
    <!-- /#wrapper -->
    <!-- jQuery -->
    <script src="<?php echo base_url();?>assets/js/jquery.min.js"></script>
    <!-- Bootstrap Core JavaScript -->
    <script src="<?php echo base_url();?>assets/bootstrap/dist/js/bootstrap.min.js"></script>
    <!-- Menu Plugin JavaScript -->
    <script src="<?php echo base_url();?>assets/plugins/bower_components/sidebar-nav/dist/sidebar-nav.min.js"></script>
    <!--slimscroll JavaScript -->
    <script src="<?php echo base_url();?>assets/js/jquery.slimscroll.js"></script>
    <!--Wave Effects -->
    <script src="<?php echo base_url();?>assets/js/waves.js"></script>
    <script src="<?php echo base_url();?>assets/js/custom.min.js"></script>
    <!-- Vector map JavaScript -->
 <!--    <script src="<?php echo base_url();?>assets/plugins/bower_components/vectormap/jquery-jvectormap-2.0.2.min.js"></script>
    <script src="<?php echo base_url();?>assets/plugins/bower_components/vectormap/jquery-jvectormap-world-mill-en.js"></script>
    <script src="<?php echo base_url();?>assets/plugins/bower_components/vectormap/jquery-jvectormap-in-mill.js"></script>
    <script src="<?php echo base_url();?>assets/plugins/bower_components/vectormap/jquery-jvectormap-us-aea-en.js"></script> -->
    <!-- chartist chart -->
    <!-- <script src="<?php echo base_url();?>assets/plugins/bower_components/chartist-js/dist/chartist.min.js"></script>
    <script src="<?php echo base_url();?>assets/plugins/bower_components/chartist-plugin-tooltip-master/dist/chartist-plugin-tooltip.min.js"></script> -->
    <!-- sparkline chart JavaScript -->
    <script src="<?php echo base_url();?>assets/plugins/bower_components/jquery-sparkline/jquery.sparkline.min.js"></script>
    <script src="<?php echo base_url();?>assets/plugins/bower_components/jquery-sparkline/jquery.charts-sparkline.js"></script>
    <!-- Custom Theme JavaScript -->
    <script src="<?php echo base_url();?>assets/js/dashboard3.js"></script>
    <!--Style Switcher -->
    <script src="<?php echo base_url();?>assets/plugins/bower_components/styleswitcher/jQuery.style.switcher.js"></script>
    <script src="<?php echo base_url();?>assets/plugins/bower_components/datatables/datatables.min.js"></script>
    <script src="https://cdn.datatables.net/buttons/1.2.2/js/dataTables.buttons.min.js"></script>
    <script src="https://cdn.datatables.net/buttons/1.2.2/js/buttons.flash.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jszip/2.5.0/jszip.min.js"></script>
    <script src="https://cdn.rawgit.com/bpampuch/pdfmake/0.1.18/build/pdfmake.min.js"></script>
    <script src="https://cdn.rawgit.com/bpampuch/pdfmake/0.1.18/build/vfs_fonts.js"></script>
    <script src="https://cdn.datatables.net/buttons/1.2.2/js/buttons.html5.min.js"></script>
    <script src="https://cdn.datatables.net/buttons/1.2.2/js/buttons.print.min.js"></script>
    <script src="<?php echo base_url();?>assets/plugins/bower_components/timepicker/bootstrap-timepicker.min.js"></script>
    <script src="<?php echo base_url();?>assets/plugins/bower_components/clockpicker/dist/jquery-clockpicker.min.js"></script>
  <!--  <script src="https://code.jquery.com/jquery-3.4.0.min.js"></script>
   <script src="dist/wickedpicker.min.js"></script> -->

    <script>
    $(function() {
        $('#myTable').DataTable();
        $(document).ready(function() {
            var table = $('#example').DataTable({
                "columnDefs": [{
                    "visible": false,
                    "targets": 2
                }],
                "order": [
                    [2, 'asc']
                ],
                "displayLength": 10,
                "drawCallback": function(settings) {
                    var api = this.api();
                    var rows = api.rows({
                        page: 'current'
                    }).nodes();
                    var last = null;
                    api.column(2, {
                        page: 'current'
                    }).data().each(function(group, i) {
                        if (last !== group) {
                            $(rows).eq(i).before('<tr class="group"><td colspan="5">' + group + '</td></tr>');
                            last = group;
                        }
                    });
                }
            });
            // Order by the grouping
            $('#example tbody').on('click', 'tr.group', function() {
                var currentOrder = table.order()[0];
                if (currentOrder[0] === 2 && currentOrder[1] === 'asc') {
                    table.order([2, 'desc']).draw();
                } else {
                    table.order([2, 'asc']).draw();
                }
            });
        });
    });
    $('#example23').DataTable({
        dom: 'Bfrtip',
        buttons: [
            'copy', 'csv', 'excel', 'pdf', 'print'
        ]
    });
    $('.buttons-copy, .buttons-csv, .buttons-print, .buttons-pdf, .buttons-excel').addClass('btn btn-primary m-r-10');




    $('.clockpicker').clockpicker({
    placement: 'top',
    align: 'left',
    donetext: 'Done',
    autoclose: true,
    twelvehour: true
});
     $('.clockpicker').clockpicker({
        donetext: 'Done',
    }).find('input').change(function() {
        console.log(this.value);
    });
    $('#check-minutes').click(function(e) {
        // Have to stop propagation here
        e.stopPropagation();
        input.clockpicker('show').clockpicker('toggleView', 'minutes');
    });



     </script>

     


</body>

</html>
