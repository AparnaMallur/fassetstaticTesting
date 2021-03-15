	<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/images/home_bg.png" var="logoHOME" />
<spring:url value="/resources/images/steps.png" var="steps" />
<spring:url value="/resources/images/steps-help.png" var="stepsHelp" />
<spring:url value="/resources/images/view.png" var="viewImg" />

<div class="content">
	<!-- <div class="">
		<div class="page-header-title"></div>
	</div> -->
	<div id="year-model" data-backdrop="static" data-keyboard="false" class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
	<div class="modal-dialog ">
	       			<div class="modal-content">
	           			<div class="modal-header">
	                		<h4 class="modal-title" id="mySmallModalLabel">Select Accounting Year</h4>
	            		</div>
						<div class="modal-body">
						<c:set var="first_year" value="0" />
						<form>
								<c:forEach var="year" items="${yearList}">
									<c:choose>
										<c:when test="${first_year==0}">
											<input type="radio"
												id="accounting_year_id" name="accYear" value="${year.year_id}"
												checked="checked"
												 onclick="setdatelimit('${year.start_date}','${year.end_date}','${year.year_id}')"/>${year.year_range} 
									</c:when>
										<c:otherwise>
											<input type="radio"
												id="accounting_year_id" name="accYear" value="${year.year_id}" 
												 onclick="setdatelimit('${year.start_date}','${year.end_date}','${year.year_id}')"/>${year.year_range} 
									</c:otherwise>
									</c:choose>
									<c:set var="first_year" value="${first_year+1}" />
								</c:forEach>
								</form>
							</div>
						<div class="modal-footer">
	               			<button type="button" class="btn btn-primary waves-effect waves-light" onclick='saveyearid()'>Save Year</button>
						</div>
	       			</div><!-- /.modal-content -->
	      		</div>
	      		</div>
	<div class="page-content-wrapper ">
		<div class="container">
			<div class="row">
			
			
			<div class="col-sm-3 col-lg-3">
					<div class="panel text-center">
						<div class="panel-heading">
							<h4 class="panel-title text-muted font-light">
							
							<c:if test="${(role == '2')}">
							Pending Ledger Approvals
							</c:if>
							<c:if test="${(role == '4')}">
							Pending Primary Ledger Approvals
							</c:if>	
							<c:if test="${(role == '3')}">
							Pending Primary Ledger Approvals
							</c:if>	
							<c:if test="${(role == '5')||(role=='6') ||(role=='7')}">
							Cash Balance
							</c:if>		
							</h4>
						</div>
						<div class="panel-body p-t-10">
							<h4>
							<c:choose>
								<c:when test="${(role == '5')||(role=='6')||(role=='3') ||(role=='7')}"><a onclick="viewLedgerList()"> </c:when>
								<c:otherwise><a onclick="viewApprovals(1)"> </c:otherwise>
							</c:choose>
					    
					    <c:choose>
		<c:when test="${(role == '5')||(role=='6')||(role=='7')}"><i
									class="mdi mdi-arrow-up-bold-circle-outline text-primary m-r-10"></i><b><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${closingBalanceOfCashInHand}" /></b></a></c:when>
		<c:otherwise><i
									class="mdi mdi-arrow-up-bold-circle-outline text-primary m-r-10"></i><b>${ledgerList}</b></a></c:otherwise>
	</c:choose>
								
							</h4>
													</div>
					</div>
				</div>
				
				
				<div class="col-sm-3 col-lg-3">
					<div class="panel text-center">
						<div class="panel-heading">
							<h4 class="panel-title text-muted font-light">
							
							<c:if test="${(role == '2')}">
							Pending Subledger Approvals
							</c:if>
							<c:if test="${(role == '4')}">
							Pending Primary Subledger Approvals
							</c:if>	
							<c:if test="${(role == '3')}">
							Pending Primary Subledger Approvals
							</c:if>	
							<c:if test="${(role == '5')||(role=='6') ||(role=='7')}">
							 Bank Balance
							</c:if>		
							</h4>
						</div>
						<div class="panel-body p-t-10">
							<h4>	
							<c:choose>
								<c:when test="${(role == '5')||(role=='6')||(role=='3') ||(role=='7')}"><a onclick="viewSubledgerList()"> </c:when>
								<c:otherwise><a onclick="viewApprovals(2)"> </c:otherwise>
							</c:choose>
							
							<c:choose>
		<c:when test="${(role == '5')||(role=='6') ||(role=='7')}"><i
									class="mdi mdi-arrow-up-bold-circle-outline text-primary m-r-10"></i><b><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${closingBalanceOfAllBanks+bankOpeningbalance}" /></b></a></c:when>
		<c:otherwise><i
									class="mdi mdi-arrow-up-bold-circle-outline text-primary m-r-10"></i><b>${subledgerCount}</b></a></c:otherwise>
	</c:choose>
							
						</h4>
						</div>
					</div>
				</div>
							
				
				<div class="col-sm-3 col-lg-3">
					<div class="panel text-center">
						<div class="panel-heading">
							<h4 class="panel-title text-muted font-light">
							
							<c:if test="${(role == '2')}">
							Pending Bank Approvals
							</c:if>
							<c:if test="${(role == '4')}">
							Pending Primary Bank Approvals
							</c:if>	
							<c:if test="${(role == '3')}">
							Pending Primary Bank Approvals
							</c:if>	
							<c:if test="${(role == '5')||(role=='6') ||(role=='7')}">
							 Bills Receivable
							</c:if>		
							</h4>
						</div>
						<div class="panel-body p-t-10">
							<h4>
							<c:choose>
								<c:when test="${(role == '5')||(role=='6')||(role=='3') ||(role=='7')}"><a onclick="viewBankList()"> </c:when>
								<c:otherwise><a onclick="viewApprovals(6)"></c:otherwise>
							</c:choose>
								
								<c:choose>
		<c:when test="${(role == '5')||(role=='6') ||(role=='7')}"><i
									class="mdi mdi-arrow-up-bold-circle-outline text-primary m-r-10"></i><b><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${totalBillsReceivable}" /></b></a></c:when>
		<c:otherwise><i
									class="mdi mdi-arrow-up-bold-circle-outline text-primary m-r-10"></i><b>${bankList}</b></a></c:otherwise>
	</c:choose>
							
							</h4>
						</div>
					</div>
				</div>
				
				<c:if test="${(role == '5')||(role=='6') ||(role=='7')}">
				<div class="col-sm-3 col-lg-3">
					<div class="panel text-center" >
						<div class="panel-heading">
							<h4 class="panel-title text-muted font-light">
							 Bills Payable
							</h4>
						</div>
						<div class="panel-body p-t-10">
							<h4>
							<a onclick="viewBillsPayable()">
							
							<i
								class="mdi mdi-arrow-up-bold-circle-outline text-primary m-r-10"></i><b><fmt:formatNumber type="number"
									minFractionDigits="2" maxFractionDigits="2"
									value="${totalBillsPayable}" /></b></a>
							
							</h4>
						</div>
					</div>
				</div>
				</c:if>	
				<div class="col-sm-3 col-lg-3">
					<div class="panel text-center">
						<div class="panel-heading">
							<h4 class="panel-title text-muted font-light">
							
							<c:if test="${(role == '2')}">
							Pending Customer Approvals
							</c:if>
							<c:if test="${(role == '4')}">
							Pending Primary Customer Approvals
							</c:if>	
							<c:if test="${(role == '3')}">
							Pending Primary Customer Approvals
							</c:if>	
							<c:if test="${(role == '5')||(role=='6') ||(role=='7')}">
							 Customers
							</c:if>		
							</h4>
						</div>
						<div class="panel-body p-t-10">
							<h4>
							<c:choose>
								<c:when test="${(role == '5')||(role=='6')||(role=='3') ||(role=='7')}"><a onclick="viewCustomerList()"> </c:when>
								<c:otherwise><a onclick="viewApprovals(4)"> </c:otherwise>
							</c:choose>
							
							<i
									class="mdi mdi-arrow-up-bold-circle-outline text-primary m-r-10"></i><b>${customerCount}</b></a>
							</h4>
						</div>
					</div>
				</div>
				
				<div class="col-sm-3 col-lg-3">
					<div class="panel text-center">
						<div class="panel-heading">
							<h4 class="panel-title text-muted font-light">
							
							<c:if test="${(role == '2')}">
							Pending Supplier Approvals
							</c:if>
							<c:if test="${(role == '4')}">
							Pending Primary Supplier Approvals
							</c:if>	
							<c:if test="${(role == '3')}">
							Pending Primary Supplier Approvals
							</c:if>	
							<c:if test="${(role == '5')||(role=='6') ||(role=='7')}">
							 Suppliers
							</c:if>		
							</h4>
						</div>
						<div class="panel-body p-t-10">
							<h4>
								<c:choose>
								<c:when test="${(role == '5')||(role=='6')||(role=='3') ||(role=='7')}"><a onclick="viewSupplierList()"> </c:when>
								<c:otherwise><a onclick="viewApprovals(5)"> </c:otherwise>
							</c:choose>
							
							<i
									class="mdi mdi-arrow-up-bold-circle-outline text-primary m-r-10"></i><b>${supplierCount}</b></a>
							</h4>
						</div>
					</div>
				</div>
				
			<div class="col-sm-3 col-lg-3">
					<div class="panel text-center">
						<div class="panel-heading">
							<h4 class="panel-title text-muted font-light">
							<c:if test="${(role == '2')}">
							Pending Product Approvals
							</c:if>
							<c:if test="${(role == '4')}">
							Pending Primary Product Approvals
							</c:if>	
							<c:if test="${(role == '3')}">
							Pending Primary Product Approvals
							</c:if>	
							<c:if test="${(role == '5')||(role=='6') ||(role=='7')}">
							 Products
							</c:if>		
						  </h4>
						</div>
						<div class="panel-body p-t-10">
							<h4>
							<c:choose>
								<c:when test="${(role == '5')||(role=='6')||(role=='3') ||(role=='7')}"><a onclick="viewProductList()"> </c:when>
								<c:otherwise><a onclick="viewApprovals(3)"> </c:otherwise>
							</c:choose>
							
								
							<i class="mdi mdi-arrow-down-bold-circle-outline text-danger m-r-10"></i><b>${productlist}</b></a>
							</h4>	
						</div>
					</div>
				</div>
			
			
				
				
				
				
	
				
				<c:if test="${(role == '2')}">
				<div class="col-sm-3 col-lg-3">
					<div class="panel text-center">
						<div class="panel-heading">
							<h4 class="panel-title text-muted font-light">
							Quotations
							</h4>
						</div>
						<div class="panel-body p-t-10">
							<h2 class="m-t-0 m-b-15">
							<a onclick="viewQuotationList()">
							
							<i
									class="mdi mdi-arrow-up-bold-circle-outline text-primary m-r-10"></i><b>${quoteList}</b></a>
							</h2>
						</div>
					</div>
				</div>
				</c:if>
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
$(document).ready(function() {
var years = '<c:out value= "${yearList}"/>';	
var ysize = '<c:out value= "${yearList.size()}"/>';	
var file='<c:out value="${setYear}"/>';
;

if((years!="")){
//	alert("1");
	//alert(ysize);
	if(ysize>1){
		if(file=="NA"){
		$('#year-model').modal({
	    	show: true,
	   	});	}else{ $('#year-model').modal('hide');}
	}
	}
	});
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
	function setdatelimit(startdate,enddate,year_id)
	{    
    	
		 var startdate="";
		 var enddate="";
		 startdate=stardate;
		 enddate=enddate;
	}
	function saveyearid(){
		var rates = document.getElementsByName('accYear');
		var rate_value;
		for(var i = 0; i < rates.length; i++){
		    if(rates[i].checked){
		    	
		        rate_value = rates[i].value;
		    }
		}
	
	$.ajax({
   		type: "GET", 
    		url: '<c:url value="setAccountingYear?id="/>'+rate_value,                  		
    		success: function (data) {
    			//alert(data);
    			
    		 if(data == true){
    			 $('#year-model').modal('hide');
    			}
    		  },
    	  error: function (e,status) {
    		  if(status==="timeout") {
    			  $('#year-model').modal('hide');
    		  }else{
    					alert(e);
        			alert("GST number not found.");}
    	  }
	});
	
	}
	function viewSupplier(id){
		window.location.assign('<c:url value="viewSupplier"/>?id='+id+'&flag='+1);
	}
	function viewCustomerList(){
		
		<c:choose>
		<c:when test="${(role == '5')||(role=='6') ||(role=='7')}">window.location.assign('<c:url value="customersList"/>');</c:when>
		<c:otherwise>window.location.assign('<c:url value="customerPrimaryValidationList"/>');</c:otherwise>
	</c:choose>
		
	}
	function viewSupplierList(){
		
		<c:choose>
		<c:when test="${(role == '5')||(role=='6') ||(role=='7')}">window.location.assign('<c:url value="suppliersList"/>');</c:when>
		<c:otherwise>window.location.assign('<c:url value="supplierPrimaryValidation"/>');</c:otherwise>
	</c:choose>
		
	}
	function viewLedgerList(){
		
		<c:choose>
		<c:when test="${(role == '5')||(role=='6') ||(role=='7')}">window.location.assign('<c:url value="cashBookBankBookReport"/>');</c:when>
		<c:otherwise>window.location.assign('<c:url value="ledgerPrimaryValidationList"/>');</c:otherwise>
	</c:choose>
		
	}
	function viewSubledgerList(){
		<c:choose>
		<c:when test="${(role == '5')||(role=='6') ||(role=='7')}">window.location.assign('<c:url value="cashBookBankBookReport"/>');</c:when>
		<c:otherwise>window.location.assign('<c:url value="subLedgerPrimaryValidationList"/>');</c:otherwise>
	</c:choose>
		
	}
	function viewBankList(){
		<c:choose>
		<c:when test="${(role == '5')||(role=='6') ||(role=='7')}">window.location.assign('<c:url value="billsReceivableReport"/>');</c:when>
		<c:otherwise>window.location.assign('<c:url value="bankPrimaryValidationList"/>');</c:otherwise>
	</c:choose>
		
	}
	function viewBillsPayable(){
		window.location.assign('<c:url value="billsPayableReport"/>');
		
	}
	function viewProductList(){
		<c:choose>
		<c:when test="${(role == '5')||(role=='6') ||(role=='7')}">window.location.assign('<c:url value="productList"/>');</c:when>
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

<%@include file="/WEB-INF/includes/footer.jsp"%>