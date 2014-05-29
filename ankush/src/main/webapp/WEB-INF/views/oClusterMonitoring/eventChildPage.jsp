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
<!-- Page for event table child drill down -->
<%@ include file="../layout/blankheader.jsp"%>
<script>
</script>
<div class="section-header">
	<div class="row-fluid mrgt20">
		<div class="span1">
			<h2 class="heading">Event</h2>
		</div>
	</div>
</div>
<div class="section-body">
	<div class="container-fluid  mrgnlft8">
		<div class="row-fluid">
			<div class="span2  ">
				<label class="text-right form-label">Type:</label>
			</div>
			<div class="span10 ">
				<label class=" form-label label-black" id="eventType"></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">Severity:</label>
			</div>
			<div class="span10 ">
				<label class=" form-label label-black" id="eventSeverity"></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">Status:</label>
			</div>
			<div class="span10 ">
				<label class=" form-label label-black" id="eventStatus"></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">Time:</label>
			</div>
			<div class="span10 ">
				<label class=" form-label label-black" id="eventTime"></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2 ">
				<label class="text-right form-label">IP:</label>
			</div>
			<div class="span10 ">
				<label class=" form-label label-black" id="eventIp"></label>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span9 ">
				<h4 class="section-heading">Description</h4>
			</div>
		</div>
		<div class="row-fluid">

			<div class="span8  ">
				<textarea
					style="width: 100%; min-height: 100px; resize: none; overflow: hidden; cursor: default;"
					id="eventDesc" readonly="readonly">
			</textarea>
			</div>
		</div>
	</div>
</div>
