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
<!-- Common Page for action logs -->
<%@ include file="../layout/blankheader.jsp"%>
<script>
	$(document).ready(function() {
	});
</script>
<div class="section-header">
	<div class="row-fluid">
		<div class="span3">
			<h3 id="actionLogPageHeader"></h3>
		</div>
		<div class="span1" style="padding-top: 24px;">
			<span class="label" id="actionStatusLabel"></span>
		</div>
	</div>
</div>
<div class="section-body">
	<div class="row-fluid">
		<div class="span11 offset1" id='commonProgressLog'
			style="margin-top: 30px;"></div>
	</div>
</div>
