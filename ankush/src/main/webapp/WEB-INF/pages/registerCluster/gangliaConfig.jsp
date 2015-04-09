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

<%@ include file="../layout/blankheader.jsp"%>
<script
	src="<c:out value='${baseUrl}'/>/public/js3.0/tooltip/gangliaClusterCreationTooltip.js"
	type="text/javascript"></script>
<!-- header section -->
<body>
	<div class="">
		<div class="">
			<div class="">
				<div class="col-md-4"></div>
				<div class="col-md-1"></div>
				<div class="col-md-7 text-right mrgt20 padr45"></div>
			</div>
		</div>

		<!-- body section  -->

		<div class="" id="main-content">
			<div class="container-fluid mrgnlft8">
				<div class="row"></div>
				<div class="panel ">
					<div class="panel-heading">
						<div class="">
							<h3 class="panel-title col-md-2 mrgt5">Ganglia/Configuration</h3>
							<button id="revertGanglia" class="btn btn-default"
								onclick="com.impetus.ankush.hybridClusterCreation.dynamicRowRemove();">Cancel</button>
							<button class="btn btn-default" id="applyGanglia"
								onclick="com.impetus.ankush.register_Ganglia.gangliaConfigValidate();">Apply</button>
						</div>
						<div class="row">
							<div id="errorDivMainGanglia" class="col-md-12 errorDiv mrgt10"
								style="display: none;"></div>
						</div>
					</div>
					<div class="row panel-body">
						<div id="registerDiv">
							<div class="row">
								<div class=" col-md-2 text-right">
									<label class=" form-label">Gmetad Host:</label>
								</div>
								<div class="col-md-3 col-lg-2 form-group">
									<input id="gmetadHost" type="text" data-toggle="tooltip"
										class="input-xlarge form-control" placeholder="Gmetad Host"
										title="Enter gmetadHost for ganglia" data-placement="right"></input>
								</div>
							</div>
							<div class="row">
								<div class=" col-md-2 text-right">
									<label class=" form-label">Gmond Conf Path:</label>
								</div>
								<div class="col-md-5 col-lg-4 form-group">
									<input id="gmondConfPath" type="text" data-toggle="tooltip"
										class="input-xlarge form-control"
										placeholder="Gmond Conf Path" title="Enter gmond conf path"
										data-placement="right"></input>
								</div>
							</div>
							<div class="row">
								<div class=" col-md-2 text-right">
									<label class=" form-label">Gmetad Conf Path:</label>
								</div>
								<div class="col-md-5 col-lg-4 form-group">
									<input id="gmetadConfPath" type="text" data-toggle="tooltip"
										class="input-xlarge form-control"
										placeholder="Gmetad Conf Path" title="Enter gmetad conf path"
										data-placement="right"></input>
								</div>
							</div>

							<div class="row">
								<div class=" col-md-2 text-right">
									<label class=" form-label">Ganglia Port:</label>
								</div>
								<div class="col-md-2 col-lg-1 form-group">
									<input id="gangliaPort" type="text" data-toggle="tooltip"
										class="input-mini form-control" placeholder="Ganglia Port"
										title="Enter ganglia port" data-placement="right"></input>
								</div>
							</div>
							<div class="row" id="">
								<div class="col-md-2 text-right">
									<label class=" form-label">Registration Type:</label>
								</div>
								<div class="col-md-2 col-lg-2 form-group">
									<select id="registerLevel" title="Select Registration type"
										class="form-control" data-placement="right">
									</select>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script>
		$(document).ready(function() {
			if ($("#toggleRegisterBtnGanglia .active").data("value") == 0) {
				$('#pollingIntervalDiv').hide();
			}
			$('#gridName').focus();
			com.impetus.ankush.register_Ganglia.gangliaConfigPopulate();
		});
	</script>
</body>
