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
<!-- Page for plan history table child drill down -->
<%@ include file="../layout/blankheader.jsp"%>

<script>
	/* planHistoryChildTable=	$("#planChildTable").dataTable({
	 "bJQueryUI" : false,
	 "bPaginate" : false,
	 "bLengthChange" : false,
	 "bFilter" : true,
	 "bSort" : false,
	 "bInfo" : false,
	 "bAutoWidth" : false,
	 });
	 $("#planChildTable_filter").css({
	 'text-align' : 'right'
	 });
	 $("#planChildTable_filter")
	 .prepend(
	 '<div style="float:left;margin-top:15px;" id="planHistoryChildDatatable"></div>');
	 $("#planHistoryChildDatatable").append(
	 "<h4 class='section-heading'>Plan Parameters</h4>"); */
</script>
<body style="background: none;">
	<div class="section-header">
		<div class="row-fluid">
			<div class="span1">
				<h3>Plan</h3>
			</div>

		</div>
	
	</div>
	<div class="section-body">

<div class="container-fluid mrgnlft8">
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">ID:</label>
			</div>
			<div class="span10 ">
				<label class=" form-label label-black" id="planId" ></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">Type:</label>
			</div>
			<div class="span10 ">
				<label class=" form-label label-black" id="planType" ></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">Status:</label>
			</div>
			<div class="span10 ">
				<label class=" form-label label-black" id="planStatus" ></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">Attempts:</label>
			</div>
			<div class="span10 ">
				<label class=" form-label label-black" id="planAttempts" ></label>
			</div>
		</div>
		<!-- <div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">Start:</label>
			</div>
			<div class="span10 ">
				<label class=" form-label label-black" id="planStart" ></label>
			</div>
		</div> -->
		<!-- <div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">Stop:</label>
			</div>
			<div class="span10 ">
				<label class=" form-label label-black" id="planStop" ></label>
			</div>
		</div> -->
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">Create:</label>
			</div>
			<div class="span10 ">
				<label class=" form-label label-black" id="planCreate" ></label>
			</div>
		</div>
<div id="planHistoryChildDetail"></div>
		<div class="row-fluid">
			<div class="span8 ">
				<h4 class="section-heading">Plan Parameters</h4>
			</div>
			<div class="span5" id="planChildTable" style="margin-left:0">
				<table class="table table-striped datatable" style="border: 1px solid #E1E3E4">
					<tbody id="planChildTableBody"></tbody>
					
				</table>
			</div>
			<!-- <div class="span11 ">
				<table class="table" id="planChildTable"
					style="border: 1px solid #E1E3E4; border-top: 2px solid #E1E3E4; margin-left: 15px;">
					<thead style="text-align: left; border-bottom: 1px solid #E1E3E4">

					</thead>
					<tbody>

					</tbody>
				</table>
			</div> -->
		</div>
		
		</div>
	</div>
</body>
