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
<!-- Fields related to Java Installation during Cluster Creation   -->
<div id="javaInstallation">
	<div class="row-fluid">
		<div class="span12 text-left">
			<label class="form-label section-heading">Java</label>
		</div>
	</div>
	<div class="row-fluid">
			<div class="span2">
				<label class="text-right form-label">Oracle Java JDK :</label>
			</div>
			<div class="span10">
				<div class="input-prepend" style="white-space: inherit;" id="hadoopJavaInstallationFields">
					<span class="add-on" style="height: 28px; margin-top: 4px;"><input
						type="checkbox" class="inputSelect" name="" id="install_java_checkbox"
						onclick="com.impetus.ankush.hadoopCluster.enableJavaHome();"
						style="margin-left: 0px;"></span> <input type="text"
						class="inputText" disabled="disabled" name="" title="Path for the Java setup bundle"
						id="java_bundle_path" placeholder="Bundle Path" />
				</div>
			</div>
		</div>
	<div class="row-fluid">
		<div class="span2">
			<label class="text-right form-label">Java Home:</label>
		</div>
		<div class="span10 text-left">
			<input type="text" class="input-large" id="java_home_hadoop"
				style="width: 300px;"
				placeholder="Default Java Home	" data-toggle="tooltip"
				data-placement="right" title="Installation path for the Java Runtime Environment"></input>
		</div>
	</div>
</div>
