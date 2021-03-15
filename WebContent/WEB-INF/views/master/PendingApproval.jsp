<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>PendingApproval</title>
</head>
<body>
<!-- Pending Ledger Approval -->
	<div class = "borderForm" id="sub-div" >
		<table id="table" 
			 data-toggle="table"
			 data-search="false"
			 data-escape="false"			 
			 data-filter-control="true" 
			 data-show-export="false"
			 data-click-to-select="true"
			 data-pagination="true"
			 data-page-size="10"
			 data-toolbar="#toolbar" class = "table">
			<thead>
				<tr>
					
					<th data-field="Company Name" data-filter-control="input" data-sortable="true" >Ledger Name</th>	
				<!-- 	<th  data-field="SubGroup" data-filter-control="input" data-sortable="true" >Sub Group</th> -->
					<th  data-field="Ledger" data-filter-control="input" data-sortable="true" >Company Name</th>
					<th  data-field="subledger" data-filter-control="select" data-sortable="true" >Excecutive/Manager</th>	
					<!-- <th  data-field="Status" data-filter-control="select" data-sortable="true" >Status</th>	 -->
					<th  data-field="ApprovalStatus" data-filter-control="select" data-sortable="true" >Approval Status</th>			
				</tr>
			</thead>
		</table>
	</div>
	<!--  Pending Subledger APproval-->
	<div class = "borderForm" id="sub-div" >
		<table id="table" 
			 data-toggle="table"
			 data-search="false"
			 data-escape="false"			 
			 data-filter-control="true" 
			 data-show-export="false"
			 data-click-to-select="true"
			 data-pagination="true"
			 data-page-size="10"
			 data-toolbar="#toolbar" class = "table">
			<thead>
				<tr>
					
					<th data-field="Company Name" data-filter-control="input" data-sortable="true" >Sub_Ledger_Name</th>	
				<th  data-field="Ledger" data-filter-control="input" data-sortable="true" >Company Name</th>
					<th  data-field="subledger" data-filter-control="select" data-sortable="true" >Excecutive/Manager</th>	
					<!-- <th  data-field="Status" data-filter-control="select" data-sortable="true" >Status</th>	 -->
					<th  data-field="ApprovalStatus" data-filter-control="select" data-sortable="true" >Approval Status</th>			
				</tr>
			</thead>
		</table>
	</div>
	<!--  Pending Product Approval-->
	<div class = "borderForm" id="sub-div" >
		<table id="table" 
			 data-toggle="table"
			 data-search="false"
			 data-escape="false"			 
			 data-filter-control="true" 
			 data-show-export="false"
			 data-click-to-select="true"
			 data-pagination="true"
			 data-page-size="10"
			 data-toolbar="#toolbar" class = "table">
			<thead>
				<tr>
					
					<th data-field="Company Name" data-filter-control="input" data-sortable="true" >Product</th>	
				<!-- 	<th  data-field="SubGroup" data-filter-control="input" data-sortable="true" >Sub Group</th> -->
					<th  data-field="Ledger" data-filter-control="input" data-sortable="true" >Company Name</th>
					<th  data-field="subledger" data-filter-control="select" data-sortable="true" >Excecutive/Manager</th>	
					<!-- <th  data-field="Status" data-filter-control="select" data-sortable="true" >Status</th>	 -->
					<th  data-field="ApprovalStatus" data-filter-control="select" data-sortable="true" >Approval Status</th>			
				</tr>
			</thead>
		</table>
	</div>
	<!--  Pending Bank approval-->
	
	<div class = "borderForm" id="sub-div" >
		<table id="table" 
			 data-toggle="table"
			 data-search="false"
			 data-escape="false"			 
			 data-filter-control="true" 
			 data-show-export="false"
			 data-click-to-select="true"
			 data-pagination="true"
			 data-page-size="10"
			 data-toolbar="#toolbar" class = "table">
			<thead>
				<tr>
					
					<th data-field="Company Name" data-filter-control="input" data-sortable="true" >Bank_Name</th>	
				<!-- 	<th  data-field="SubGroup" data-filter-control="input" data-sortable="true" >Sub Group</th> -->
					<th  data-field="Ledger" data-filter-control="input" data-sortable="true" >Company Name</th>
					<th  data-field="subledger" data-filter-control="select" data-sortable="true" >Excecutive/Manager</th>	
					<!-- <th  data-field="Status" data-filter-control="select" data-sortable="true" >Status</th>	 -->
					<th  data-field="ApprovalStatus" data-filter-control="select" data-sortable="true" >Approval Status</th>			
				</tr>
			</thead>
		</table>
	</div>
	<!--  Pending supplier Approval -->
	<div class = "borderForm" id="sub-div" >
		<table id="table" 
			 data-toggle="table"
			 data-search="false"
			 data-escape="false"			 
			 data-filter-control="true" 
			 data-show-export="false"
			 data-click-to-select="true"
			 data-pagination="true"
			 data-page-size="10"
			 data-toolbar="#toolbar" class = "table">
			<thead>
				<tr>
					
					<th data-field="Company Name" data-filter-control="input" data-sortable="true" >Supplioer_Name</th>	
					<th  data-field="SubGroup" data-filter-control="input" data-sortable="true" >Supplier_Company_Name</th>
					<th  data-field="Ledger" data-filter-control="input" data-sortable="true" >Company Name</th>
					<th  data-field="subledger" data-filter-control="select" data-sortable="true" >Excecutive/Manager</th>	
					<!-- <th  data-field="Status" data-filter-control="select" data-sortable="true" >Status</th>	 -->
					<th  data-field="ApprovalStatus" data-filter-control="select" data-sortable="true" >Approval Status</th>			
				</tr>
			</thead>
		</table>
	</div>
	<!--Pending Customer Approval  -->
	<div class = "borderForm" id="sub-div" >
		<table id="table" 
			 data-toggle="table"
			 data-search="false"
			 data-escape="false"			 
			 data-filter-control="true" 
			 data-show-export="false"
			 data-click-to-select="true"
			 data-pagination="true"
			 data-page-size="10"
			 data-toolbar="#toolbar" class = "table">
			<thead>
				<tr>
					
					<th data-field="Company Name" data-filter-control="input" data-sortable="true" >Customer_Name</th>	
					<th  data-field="SubGroup" data-filter-control="input" data-sortable="true" >Customer_Company_Name</th>
					<th  data-field="Ledger" data-filter-control="input" data-sortable="true" >Company Name</th>
					<th  data-field="subledger" data-filter-control="select" data-sortable="true" >Excecutive/Manager</th>	
					<!-- <th  data-field="Status" data-filter-control="select" data-sortable="true" >Status</th>	 -->
					<th  data-field="ApprovalStatus" data-filter-control="select" data-sortable="true" >Approval Status</th>			
				</tr>
			</thead>
		</table>
	</div>
	
			
</body>
</html>