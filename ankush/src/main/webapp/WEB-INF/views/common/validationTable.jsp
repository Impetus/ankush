<!------------------------------------------------------------------------------
-===========================================================
-Ankush : Big Data Cluster Management Solution
-===========================================================
-
-(C) Copyright 2014, by Impetus Technologies
-
-This is free software; you can redistribute it and/or modify it under
-the terms of the GNU Lesser General Public License (LGPL v3) as
-published by the Free Software Foundation;
-
-This software is distributed in the hope that it will be useful, but
-WITHOUT ANY WARRANTY; without even the implied warranty of
-MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
-See the GNU Lesser General Public License for more details.
-
-You should have received a copy of the GNU Lesser General Public License 
-along with this software; if not, write to the Free Software Foundation, 
-Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
------------------------------------------------------------------------------->
<script>
var validationTable = null;
$(document).ready(function(){
	 $('.validation-inspect-node').hide();
	validationTable  =	$('#validationTable').dataTable({
		"bJQueryUI" : true,
		"bPaginate" : false,
		"bLengthChange" : true,
		"bFilter" : true,
		"bSort" : false,
		"bInfo" : false,
		"bAutoWidth" : false,
		"sPaginationType" : "full_numbers",
		"bAutoWidth" : false,
		"bRetrieve" : true,
		/* "aaSorting": [[0,'asc']],
		"aoColumnDefs" : [ {
			'sType' : "ip-address",
			'aTargets' : [ 1 ]
		}], */
		"oLanguage": {
	        "sEmptyTable": 'Loading...',
	    }
	});
	$("#validationTable_filter").css({
		'display' : 'none'
	});
	$('#searchvalidationTable').keyup(function() {
		$("#validationTable").dataTable().fnFilter($(this).val());
	});
	if(Object.keys(inspectNodeData).length != 0){
		 $('.validation-inspect-node').show();
		 setTimeout(function()
		    		{
			 			var ip = ipNodeInspect;
			 			if(inspectNodeData[ip] == undefined){
			 				$('.validation-inspect-node').hide();
			 				return;
			 			}
			 			var classInspect = {
								"Ok" : 'text-success',
								"Critical" : 'text-error',
								"Warning" : 'text-error'
						};
			 			if(inspectNodeData.status == true){
							for(var key in inspectNodeData[ip]){
								validationTable.fnAddData([
								                           inspectNodeData[ip][key].label,
								                           '<span class="'+classInspect[inspectNodeData[ip][key].status]+'">'+inspectNodeData[ip][key].status+'</span>',
								                           inspectNodeData[ip][key].message
								                           ]);
							}
			 			}else{
			 				validationTable.fnSettings().oLanguage.sEmptyTable = inspectNodeData.error[0];
			 				validationTable.fnClearTable();
			 			}
		    		},0);
	}  
});
</script>
<div class="validation-inspect-node" style="display:none;">
<div class="row-fluid">
		<div class="span6 text-left">
			<h4 class="section-heading" style="text-align: left;">Validation</h4>
		</div>
		<div class="text-right span6">
					<input id="searchvalidationTable" type="text"
						placeholder="Search">
		</div>
	</div>
<div class="row-fluid">
		<div class="span12 text-left">
			<table class="table table-striped table-border" id="validationTable"
				width="100%">
				<thead class="text-left">
					<tr>
						<th>Validation</th>
						<th>Status</th>
						<th>Message</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
</div>
