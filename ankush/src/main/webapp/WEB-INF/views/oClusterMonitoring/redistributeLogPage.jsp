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
<!-- Page for showing redistribute logs -->
<%@ include file="../layout/blankheader.jsp"%>

<script>

$(document).ready(function() {

	
});
</script>

<div class="section-header">
	<div class="row-fluid" style="margin-top: 20px;">
		<div class="span7">
			<h3 id="actionLogPage">Redistribute</h3>
		</div>
		<div class="span3 text-right">
				<button class="btn" style="margin-right: 100px;"
					onclick="">Stop</button>
			</div>
	</div>
</div>
<div class="section-body">

	<div class="row-fluid">
		<div class="span11 offset1" id='redistributeProgressLog'></div>
	</div>
</div>
