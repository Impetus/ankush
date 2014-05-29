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
<!-- Hadoop Cluster Creation  Page -->

<%@ include file="../layout/blankheader.jsp"%>
<link rel="stylesheet/less" type="text/css"
	href="<c:out value='${baseUrl}' />/public/css/styles.less" />
<script>
	function dispatch(fn, args) {
		fn = (typeof fn == "function") ? fn : window[fn]; // Allow fn to be a function object or the name of a global function
		return fn.apply(this, args); // args is optional, use an empty array by default
	}
	function f1(result) {
		//alert(result.test);
	}
	$(document).ready(function() {
		com.impetus.ankush.hadoopCluster.initHadoop();
		$("#deleteClusterDialogHadoop").appendTo('body');
		$('.ui-dialog-titlebar').hide();
		clusterId = "<c:out value='${clusterId}' />";
		if (clusterId == null || clusterId == "") {
			com.impetus.ankush.hadoopCluster.tooltip();
			com.impetus.ankush.hadoopCluster.getDefaultHadoopJson();
		}
// 		var result = new Object();
// 		result.test = 'abc';
// 		dispatch("f1", [ result ]);
	});
</script>

<div class="section-header">
	<div class="row-fluid" style="margin-top: 20px;">
		<div class="span7">
			<h2 class="heading text-left left">Hadoop</h2>
			
			<button class="btn-error" id="errorBtnHadoop"
				onclick="com.impetus.ankush.hadoopCluster.scrollToTop();"
				style="display: none; height: 29px; color: white; border: none; background-color: #EF3024 !important;padding:0 15px; left:15px; position:relative"></button>
		</div>
		
		<!--  
		<div class="span3 minhgt0">
			<button class="span3 btn-error" id="errorBtnHadoop"
				onclick="com.impetus.ankush.hadoopCluster.scrollToTop();"
				style="display: none; height: 29px; color: white; border: none; background-color: #EF3024 !important;"></button>
		</div>
		-->
		
		
		<div class="span5 text-right">
			<button id="commonDeleteButtonHadoop" class="btn mrgr8" disabled="disabled"
				onclick="com.impetus.ankush.hadoopCluster.deleteDialogHadoop();">Delete</button>
			<button class="btn" id="hadoopClusterDeploy" data-loading-text="Deploying..."
				onclick="com.impetus.ankush.hadoopCluster.clusterCreation();">Deploy</button>
		</div>
	</div>
</div>

<div class="content-body section-body mrgnlft8">
	<div id="deleteClusterDialogHadoop" class="modal hide fade"
		style="display: none;">
		<div class="modal-header text-center">
			<h4>Delete Cluster</h4>
		</div>
		<div class="modal-body">
			<div class="row-fluid">
				<div class="span12" style="text-align: left; font-size: 14px;">
					The Cluster will be permanently deleted. Once deleted data cannot
					be recovered.</div>
			</div>
		</div>
		<br>
		<div class="modal-footer">
			<a href="#" data-dismiss="modal" class="btn"
				id="cancelDeleteButtonHadoop">Cancel</a> <a href="#"
				id="confirmDeleteButtonHadoop"
				onclick="com.impetus.ankush.hadoopCluster.deleteClusterHadoop();"
				class="btn">Delete</a>
		</div>
	</div>
	<div id="discardAdvSettingsBox" class="box-dialog"
		style="display: none;">
		<div class="row-fluid">
			<div class="span12">
				<h4 style="text-align: center; color: black">Discard Advanced
					Settings</h4>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span12" style="text-align: left;">
				<label id="lblDiscardComponentMessage" class="text-left"></label>
			</div>
		</div>
		<br>
		<div class="row-fluid text-right">
			<button class="btn  span2 offset8" id="confirmDiscard"
				onclick="com.impetus.ankush.hadoopCluster.discardComponentAppliedValues();">Delete</button>
			<button class="btn span2" id="cancelDiscard">Cancel</button>
		</div>
	</div>
	<div class="container-fluid">
		<div class="row-fluid">

			<div id="error-div-hadoop" class="span12 error-div-hadoop"
				style="display: none;">
				<span id="popover-content" style="color: red;"></span>
			</div>
		</div>
		<%@ include file="_general_details.jsp"%>
		<%@ include file="_java_installation.jsp"%>
		<%@ include file="_cloud_authentication.jsp"%>
		<%@ include file="_cloud_details.jsp"%>
		<%@ include file="_node_authentication.jsp"%>
		<%@ include file="_select_nodes.jsp"%>
		<%@ include file="_hadoop_ecosystem.jsp"%>
	</div>
</div>
