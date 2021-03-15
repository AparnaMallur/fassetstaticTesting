<%@include file="/WEB-INF/includes/mobileHeader.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/images/home_bg.png" var="logoHOME" />
<spring:url value="/resources/images/steps.png" var="steps" />
<spring:url value="/resources/images/steps-help.png" var="stepsHelp" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<%-- <%@include file="/resources/js/graphite.js"%>
<%@include file="/resources/js/main.js"%> --%>
<spring:url value="/resources/js/graphite.js" var="graphitejs" />
<spring:url value="/resources/js/main.js" var="mainjs" />
 
<!-- <script type="text/javascript" src="/resources/js/graphite.js"></script>
<script type="text/javascript" src="/resources/js/main.js"></script> --> 
<style>
	

#mobileview .page-content-wrapper{ margin-top: 0;}
#mobileview .page-header-title{color:#fff;padding: 65px 15px 15px 20px;font-size:20px;}
#wrapper #mobileview .panel .panel-heading{padding: 25px 20px;}
#wrapper #mobileview .panel .text-muted{color:#fff;font-size: 30px;line-height: 1.1;}
.reportblock{background:#121739;color:#fff;}
.reportblock h4, .reportblock h1, .reportblock a{color:#fff;}
.rightblock a{display:block;padding: 5px 0;border-bottom: 1px solid #6c6c6c;}
.reportchartblock{background:#2a2d4d;}
.reportchartblock .chartbtnblock{border:1px solid #455390;margin-top:20px;border-radius: 20px;}
.reportchartblock .chartbtnblock a{color:#707fae;}
.reportchartblock .chartbtnblock a.active{color:#fff;border:1px solid #fff;border-radius: 20px;}
@media (max-width: 480px){
.button-menu-mobile {
    display: block;
    position: absolute;
    background: transparent;
    top: 65px;
    left: -75px;
}
}

</style>

<div class="content" id="mobileview">
	<div class="">
		<div class="page-header-title">DASHBOARD</div>
	</div>
	<div class="page-content-wrapper ">
		<div class="container">
			<div class="row">
			
				<div class="col-lg-3 col-md-3 col-sm-3 col-xs-6">
					<div class="panel text-center dashboard-panel1">
						<div class="panel-heading">
							<h4 class="panel-title text-muted font-light">
							Sales Entry
							</h4>
						</div>
					</div>
				</div>
				
				<div class="col-lg-3 col-md-3 col-sm-3 col-xs-6">
					<div class="panel text-center dashboard-panel2">
						<div class="panel-heading">
							<h4 class="panel-title text-muted font-light">
							Receipt Voucher
							</h4>
						</div>
					</div>
				</div>
				
				<div class="col-lg-3 col-md-3 col-sm-3 col-xs-6">
					<div class="panel text-center dashboard-panel3">
						<div class="panel-heading">
							<h4 class="panel-title text-muted font-light">
							Purchase Entry
							</h4>
						</div>
					</div>
				</div>
				
				<div class="col-lg-3 col-md-3 col-sm-3 col-xs-6">
					<div class="panel text-center dashboard-panel4" >
						<div class="panel-heading">
							<h4 class="panel-title text-muted font-light">
							Payment Voucher
							</h4>
						</div>
					</div>
				</div>
				
			</div>
			
			
			<div class="col-xs-12 reportblock">
				<div class="row">
					<div class="col-xs-6">
						<div class="text-center">
							<h4>REPORTS</h4>
							<h1><span><i class="fa fa-inr" aria-hidden="true"></i></span> 0</h1>
						</div>
					</div>
					<div class="col-xs-6">
						<div class="rightblock">
						<a>Sales Register</a>
						<a>Purchase Register</a>
						<a style="border-bottom: 0;">Day Book</a>
						</div>
					</div>
				</div>
			</div>
			<div class="col-xs-12 reportchartblock">
			<div class="chartbtnblock text-center">
				<a class="btn pull-left">Today</a>
				<a class="btn">This Month</a>
				<a class="btn pull-right active">This Year</a>
			</div>
			
			
			      <div class="graph" id="example"></div>
			</div>
			
		</div>
	</div>
</div>
<c:if test="${(approval == '1')}">
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
					
					<th data-field="Ledger Name" data-filter-control="input" data-sortable="true" >Ledger Name</th>	
					<th  data-field="Company Name" data-filter-control="input" data-sortable="true" >Company Name</th>
					<th  data-field="Excecutive/Manager" data-filter-control="input" data-sortable="true" >Executive/Manager</th>	
					<th  data-field="ApprovalStatus" data-filter-control="input" data-sortable="true" >Approval Status Pending</th>			
				</tr>
			</thead>
			<tbody>
			<c:forEach var = "approvalForm" items = "${approvalList}">
			<c:forEach var = "ledger" items = "${approvalForm.ledgerList}">
			<tr>
				<td style="text-align: left;">${ledger.ledger_name}</td>
				<td style="text-align: left;">${ledger.company.company_name}</td>
				<td style="text-align: left;">${approvalForm.first_name} ${approvalForm.last_name}</td>
				<td style="text-align: left;">${ledger.ledger_approval == 0 ? "Primary Approval": "Secondary Approval"}</td>
			</tr>
			</c:forEach>
			</c:forEach>
			</tbody>
		</table>
	</div>

</c:if>
          <!-- for Sub ledger Approval -->
<c:if test="${(approval == '2')}">
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
					
					<th data-field="Sub Ledger Name" data-filter-control="input" data-sortable="true" >Sub Ledger Name</th>	
				    <th  data-field="Company Name" data-filter-control="input" data-sortable="true" >Company Name</th>
					<th  data-field="Excecutive/Manager" data-filter-control="input" data-sortable="true" >Executive/Manager</th>	
					<th  data-field="ApprovalStatus" data-filter-control="input" data-sortable="true" >Approval Status Pending</th>			
				</tr>
			</thead>
			<tbody>
				<c:forEach var="approvalForm" items="${approvalList}">
					<c:forEach var="subledger" items="${approvalForm.subledgerList}">
					<tr>
						<td style="text-align: left;">${subledger.subledger_name}</td>
						<td style="text-align: left;">${subledger.company.company_name}</td>
						<td style="text-align: left;">${approvalForm.first_name} ${approvalForm.last_name}</td>
						<td style="text-align: left;">${subledger.subledger_approval == 0 ? "Primary Approval": "Secondary Approval"}</td>
					</tr>
					</c:forEach>
				</c:forEach>
			</tbody>
		</table>
	</div>
</c:if>	

          <!--  product approval -->
          
  <c:if test="${(approval == '3')}">
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
					
					<th data-field="Product" data-filter-control="input" data-sortable="true" >Product Name</th>	
				    <th  data-field="Company Name" data-filter-control="input" data-sortable="true" >Company Name</th>
					<th  data-field="Excecutive/Manager" data-filter-control="input" data-sortable="true" >Executive/Manager</th>	
					<th  data-field="ApprovalStatus" data-filter-control="input" data-sortable="true" >Approval Status Pending</th>			
				</tr>
			</thead>
			<tbody>
				<c:forEach var="approvalForm" items="${approvalList}">
					<c:forEach var="product" items="${approvalForm.productList}">
						<tr>
						<td style="text-align: left;">${product.product_name}</td>
						<td style="text-align: left;">${product.company.company_name}</td>
						<td style="text-align: left;">${approvalForm.first_name} ${approvalForm.last_name}</td>
						<td style="text-align: left;">${product.product_approval == 0 ? "Primary Approval": "Secondary Approval"}</td>
						</tr>
					</c:forEach>
				</c:forEach>
			</tbody>
		</table>
	</div>
</c:if>	
          <!-- customer approval -->
 <c:if test="${(approval == '4')}">     
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
					<th  data-field="Customer Company Name" data-filter-control="input" data-sortable="true" >Customer Company Name</th>
					<th  data-field="Company Name" data-filter-control="input" data-sortable="true" >Client Company Name</th>
					<th  data-field="Excecutive/Manager" data-filter-control="input" data-sortable="true" >Executive/Manager</th>	
					<th  data-field="ApprovalStatus" data-filter-control="input" data-sortable="true" >Approval Status Pending</th>			
				</tr>
			</thead>
			<tbody>
				<c:forEach var="approvalForm" items="${approvalList}">
					<c:forEach var="customer" items="${approvalForm.customerList}">
					<tr>
						<td style="text-align: left;">${customer.firm_name}</td>
						<td style="text-align: left;">${customer.company.company_name}</td>
						<td style="text-align: left;">${approvalForm.first_name} ${approvalForm.last_name}</td>
						<td style="text-align: left;">${customer.customer_approval == 0 ? "Primary Approval": "Secondary Approval"}</td>
						</tr>
					</c:forEach>
				</c:forEach>
			</tbody>
		</table>
	</div>  
</c:if>		    

  <!-- Supplier approval -->
  <c:if test="${(approval == '5')}">    
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
					
					<th data-field="Supplier Company Name" data-filter-control="input" data-sortable="true" >Supplier Company Name</th>	
					<th  data-field="Company Name" data-filter-control="input" data-sortable="true" >Client Company Name</th>
					<th  data-field="Executive/Manager" data-filter-control="input" data-sortable="true" >Executive/Manager</th>	
					<th  data-field="ApprovalStatus" data-filter-control="input" data-sortable="true" >Approval Status Pending</th>			
				</tr>
			</thead>
			
			  <tbody>
				<c:forEach var="approvalForm" items="${approvalList}">
					<c:forEach var="supplier" items="${approvalForm.supplierList}">
					    <tr>
						<td style="text-align: left;">${supplier.company_name}</td>
						<td style="text-align: left;">${supplier.company.company_name}</td>
						<td style="text-align: left;">${approvalForm.first_name} ${approvalForm.last_name}</td>
						<td style="text-align: left;">${supplier.supplier_approval == 0 ? "Primary Approval": "Secondary Approval"}</td>
						</tr>
					</c:forEach>
				</c:forEach>
			</tbody>
		</table>
	</div>  
</c:if>	

      <!-- Bank Approval -->
<c:if test="${(approval == '6')}">      
  
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
					
					<th data-field="Bank Name" data-filter-control="input" data-sortable="true" >Bank Name</th>	
					<th  data-field="Company Name" data-filter-control="input" data-sortable="true" >Company Name</th>
					<th  data-field="Executive/Manager" data-filter-control="input" data-sortable="true" >Executive/Manager</th>	
					<th  data-field="ApprovalStatus" data-filter-control="input" data-sortable="true" >Approval Status Pending</th>			
				</tr>
			</thead>
			  <tbody>
				<c:forEach var="approvalForm" items="${approvalList}">
					<c:forEach var="bank" items="${approvalForm.bankList}">
					 <tr>
						<td style="text-align: left;">${bank.bank_name}</td>
						<td style="text-align: left;">${bank.company.company_name}</td>
						<td style="text-align: left;">${approvalForm.first_name} ${approvalForm.last_name}</td>
						<td style="text-align: left;">${bank.bank_approval == 0 ? "Primary Approval": "Secondary Approval"}</td>
						</tr>
					</c:forEach>
				</c:forEach>
			</tbody>
		</table>
	</div>
</c:if>
      
<script type="text/javascript">
$(function() {	

	 <c:if test="${reminder != null}">
		alert("Please change your password");
	</c:if>		

	<c:if test="${expired != null}">
		alert("Your password is expired !! Please change it for Login.");
		var expiredPassword = 1;
		window.location.assign('<c:url value="logout"/>?expiredPassword='+expiredPassword);
	</c:if>	 
});
	function viewCustomer(id){
		window.location.assign('<c:url value="viewCustomer"/>?id='+id+'&flag='+1);
	}
	function viewSupplier(id){
		window.location.assign('<c:url value="viewSupplier"/>?id='+id+'&flag='+1);
	}
	function viewCustomerList(){
		
		<c:choose>
		<c:when test="${(role == '5')||(role=='6')}">window.location.assign('<c:url value="customersList"/>');</c:when>
		<c:otherwise>window.location.assign('<c:url value="customerPrimaryValidationList"/>');</c:otherwise>
	</c:choose>
		
	}
	function viewSupplierList(){
		
		<c:choose>
		<c:when test="${(role == '5')||(role=='6')}">window.location.assign('<c:url value="suppliersList"/>');</c:when>
		<c:otherwise>window.location.assign('<c:url value="supplierPrimaryValidation"/>');</c:otherwise>
	</c:choose>
		
	}
	function viewLedgerList(){
		
		<c:choose>
		<c:when test="${(role == '5')||(role=='6')}">window.location.assign('<c:url value="cashBookBankBookReport"/>');</c:when>
		<c:otherwise>window.location.assign('<c:url value="ledgerPrimaryValidationList"/>');</c:otherwise>
	</c:choose>
		
	}
	function viewSubledgerList(){
		<c:choose>
		<c:when test="${(role == '5')||(role=='6')}">window.location.assign('<c:url value="cashBookBankBookReport"/>');</c:when>
		<c:otherwise>window.location.assign('<c:url value="subLedgerPrimaryValidationList"/>');</c:otherwise>
	</c:choose>
		
	}
	function viewBankList(){
		<c:choose>
		<c:when test="${(role == '5')||(role=='6')}">window.location.assign('<c:url value="billsReceivableReport"/>');</c:when>
		<c:otherwise>window.location.assign('<c:url value="bankPrimaryValidationList"/>');</c:otherwise>
	</c:choose>
		
	}
	function viewBillsPayable(){
		window.location.assign('<c:url value="billsPayableReport"/>');
		
	}
	function viewProductList(){
		<c:choose>
		<c:when test="${(role == '5')||(role=='6')}">window.location.assign('<c:url value="productList"/>');</c:when>
		<c:otherwise>window.location.assign('<c:url value="productPrimaryValidationList"/>');</c:otherwise>
	</c:choose>
		
	}
	function viewQuotationList(){
		window.location.assign('<c:url value="showQuotationWithoutRegistration"/>');
	}
	function viewApprovals(flag){
	    window.location.assign('<c:url value="viewApprovals"/>?flag='+flag);
		setmenuid(12);
	}
	function viewemployeelist(){
		window.location.assign('<c:url value="employeeList"/>');
		setmenuid(66);
	}
</script>

<%@include file="/WEB-INF/includes/mobileFooter.jsp" %>
<script type="text/javascript" src="${graphitejs}"></script>  
<script type="text/javascript" src="${mainjs}"></script>
<style>
hr.graphite-separator-0{display:none;}
.graphite-separator-container{display:none;}
</style>
