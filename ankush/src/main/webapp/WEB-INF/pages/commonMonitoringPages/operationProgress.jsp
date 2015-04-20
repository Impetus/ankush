<!------------------------------------------------------------------------------
-  ===========================================================
-  Ankush : Big Data Cluster Management Solution
-  ===========================================================
-  
-  (C) Copyright 2014, by Impetus Technologies
-  
-  This is free software; you can redistribute it and/or modify it under
-  the terms of the GNU Lesser General Public License (LGPL v3) as
-  published by the Free Software Foundation;
-  
-  This software is distributed in the hope that it will be useful, but
-  WITHOUT ANY WARRANTY; without even the implied warranty of
-  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
-  See the GNU Lesser General Public License for more details.
-  
-  You should have received a copy of the GNU Lesser General Public License 
-  along with this software; if not, write to the Free Software Foundation, 
- Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
------------------------------------------------------------------------------->

<html>
<head>
<%@ include file="../layout/header.jsp"%>
<%@ include file="../layout/navigation.jsp"%>
<style>
#operationTable tr{
padding:10px;
}
#operationTableSection > .panel > .panel-heading:after {
    border-bottom: 0px;
    content: "";
    display: block;
    height: 0;
    padding-bottom: 0px !important;
}
#operationTableSection a{
	outline:0;
}
</style>
</head>

<body>
	<div class="page-wrapper">
		<div class="page-header heading">
			<h1 class="left">Operation Detail</h1>
				<div class="col-md-8 pull-right mrgt5">
				
			</div>
		</div>
		<div class="page-body" id="main-content">
		<%@ include file="../layout/breadcrumbs.jsp"%>
			<div class="container-fluid">
			<div class="row mrgb20">
                <div class="col-md-3">
                  <div class="progressTile">
                    <div class="progressTile-body">
                      <div class="current-stats" id="operationName">
                        <h4></h4>
                        <p>&nbsp;</p>
                        <div class="type">
                          <span class="fa fa-clock-o" style="font-size:35px;" aria-hidden="true"></span> 
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="col-md-3">
                  <div class="progressTile">
                    <div class="progressTile-body">
                      <div class="current-stats" id="operationStatus">
                        <h4></h4>
                        <p>&nbsp;</p>
                        <div class="type">
                          <span  class="fa fa-flask" data-icon="" style="font-size:35px;" aria-hidden="true"></span> 
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="col-md-3">
                  <div class="progressTile">
                    <div class="progressTile-body">
                      <div class="current-stats" id="startedAt">
									
										<h4></h4>
										<p>&nbsp;</p>
									
									<div class="type">
                          <span  class="fa fa-user" data-icon="" style="font-size:35px;" aria-hidden="true"></span> 
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="col-md-3">
                  <div class="progressTile">
                    <div class="progressTile-body">
                      <div class="current-stats" id="startedBy">
                        <h4></h4>
                        <p>&nbsp;</p>
                        <div class="type">
                          <span  class="fa fa-user" data-icon="" style="font-size:35px;" aria-hidden="true"></span> 
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="panel-group element-hide" id="operationTableSection" role="tablist" aria-multiselectable="true" style="width:50%;">
					<div class="panel panel-default">
						<div class="panel-heading" role="tab" id="headingOne">
							<h4 class="panel-title">
								<a data-toggle="collapse" data-parent="#operationTableSection"
									href="#collapse" aria-expanded="false" aria-controls="collapse">Operation
									Input</a>
							</h4>
						</div>
						<div id="collapse" class="panel-collapse" role="tabpanel"
							aria-labelledby="headingOne">
							<table id="operationTable" class="table">
							</table>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-10" id='deploymentProgressDiv'></div>
				</div>

			</div>
		</div>
	</div>
	
	<script
	src="<c:out value='${baseUrl}' />/public/js3.0/commonMonitoring/operationProgress.js"
	type="text/javascript"></script>
	<script
	src="<c:out value='${baseUrl}' />/public/libjs3.0/jquery.dateFormat-1.0.js"
	type="text/javascript"></script>
	<script>
		var operationId = '<c:out value="${operationId}"/>';
		$(document).ready(
				function() {
					com.impetus.ankush.operationProgress.progressLogs();
					com.impetus.ankush.operationProgress.operationsTiles();
					var operationsTiles = {};
					operationsTiles.method = function(){
						com.impetus.ankush.operationProgress.operationsTiles();
					};
					pageLevelAutorefreshArray.push(operationsTiles);
					com.impetus.ankush.createAutorefresh(pageLevelAutorefreshArray);
				});
	</script>
</body>

</html>
