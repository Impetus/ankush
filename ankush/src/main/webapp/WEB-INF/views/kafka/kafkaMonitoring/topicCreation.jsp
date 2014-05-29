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
<script type="text/javascript">
	$(document).ready(function() {
		$('#topicNameKafka').tooltip();
		$('#partitionCountKafka').tooltip();
		$('#replicasKafka').tooltip();
	});
</script>

<!-- header section -->
<div class="section-header">
	<div class="row-fluid headermargin">
		<div class="span7">
			<h2 class="heading text-left left">Topic Create</h2>
			<button class="btn-error header_errmsg"
				data-loading-text="Creating..." id="validateErrorTopicCreate"
				onclick="com.impetus.ankush.common.focusError();"
				style="display: none; padding: 0 15px; left: 15px; position: relative"></button>
		</div>

		<div class="span5 text-right">
			<button id="" class="btn headerright-setting"
				onclick="com.impetus.ankush.kafkaMonitoring.removeChildPage()">Cancel</button>
			<button class="btn" id="kafkaTopicCreateBtn"  data-loading-text="Creating..." 
				onclick="com.impetus.ankush.kafkaMonitoring.createTopicValidate();">Create</button>
		</div>
	</div>
</div>
<div class="section-body content-body">
	<div class="container-fluid mrgnlft8">
		<div class="row-fluid">
			<div id="errorDivTopicCreate" class="span12 errorDiv"
				style="display: none;"></div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Topic Name:</label>
			</div>
			<div class="span10">
				<input type="text" id="topicNameKafka" class="input-large"
					placeholder="Kafka Topic Name" title="Enter Kafka Topic Name"
					data-placement="right">
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Partition Count:</label>
			</div>
			<div class="span10">
				<input type="text" id="partitionCountKafka" class="input-large"
					placeholder="Kafka Partition Count"
					title="Enter Kafka Partition Count" data-placement="right">
			</div>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Replicas:</label>
			</div>
			<div class="span10">
				<input type="text" id="replicasKafka" class="input-large"
					placeholder="Kafka Replicas" title="Enter Kafka Replicas"
					data-placement="right">
			</div>
		</div>
	</div>
</div>
