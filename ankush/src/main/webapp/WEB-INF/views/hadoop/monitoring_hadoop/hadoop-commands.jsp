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
<!-- Hadoop Command Page containing links to Commands configuration page -->

<script>
$(document).ready(function(){
	com.impetus.ankush.commonMonitoring.removeMonitoringPageAutoRefresh();
});
</script>
<%@ include file="../../layout/blankheader.jsp"%>
<div class="section-header">
	<div class="row-fluid" style="margin-top: 20px;">
		<div class="span4">
			<h2 class="heading text-left">Commands</h2>
		</div>
	</div>
</div>

<div class="section-body content-body">
	<div class="container-fluid">
		<div class="row-fluid" span4>
			<a href="##"
				onclick="com.impetus.ankush.hadoopMonitoring.commandArchive();">
				<div class="span1 text-left">
					<label class="form-label section-heading">Archive</label>
				</div>
				<div class="span1 text-left">
					<img style="margin-top: 15px;"
						src="<%=baseUrl%>/public/images/icon-chevron-right.png" />
				</div>
			</a>
		</div>
		<div class="row-fluid" span4>
			<a href="##"
				onclick="com.impetus.ankush.hadoopMonitoring.commandDistcp();">
				<div class="span1 text-left">
					<label class="form-label section-heading">Distcp</label>
				</div>
				<div class="span1 text-left">
					<img style="margin-top: 15px;"
						src="<%=baseUrl%>/public/images/icon-chevron-right.png" />
				</div>
			</a>
		</div>
		<div class="row-fluid" span4>
			<a href="##"
				onclick="com.impetus.ankush.hadoopMonitoring.commandBalancer();">
				<div class="span1 text-left">
					<label class="form-label section-heading">Balancer</label>
				</div>
				<div class="span1 text-left">
					<img style="margin-top: 15px;"
						src="<%=baseUrl%>/public/images/icon-chevron-right.png" />
				</div>
			</a>
		</div>
		<div class="row-fluid" span4>
			<a href="##"
				onclick="com.impetus.ankush.hadoopMonitoring.commandFsck();">
				<div class="span1 text-left">
					<label class="form-label section-heading">Fsck</label>
				</div>
				<div class="span1 text-left">
					<img style="margin-top: 15px;"
						src="<%=baseUrl%>/public/images/icon-chevron-right.png" />
				</div>
			</a>
		</div>
	</div>
</div>
