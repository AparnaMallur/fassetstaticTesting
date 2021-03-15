<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_EIGHTEEN%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THIRTY%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>


<div class="breadcrumb">
	<h3>Customer</h3>
	<a href="homePage">Home</a> »
	<c:if test="${flag==1}">
		 <a href="customersList">Customer</a> 	
	</c:if>
	<c:if test="${flag==2}">
	 <a href="customerPrimaryValidationList">Customer</a> 	
	</c:if>
	<c:if test="${flag==3}">
	 <a href="customerSecondaryValidationList">Customer</a> 	
	</c:if>	
	 » <a href="#">View</a>
</div>	


<c:if test="${successMsg != null}">
	<div class="successMsg" id = "successMsg"> 
		<strong>${successMsg}</strong>
	</div>
</c:if>
	<div class="fassetForm">
		<form:form id="customerForm"  commandName = "customer">
			<div class="col-md-12">
				<table class = "table">
					<tr>
						<td><strong>Name:</strong></td>
						<td style="text-align: left;">${customer.contact_name}</td>
						<td><strong>Mobile:</strong></td>
						<td style="text-align: left;">${customer.mobile}</td>	
						<td><strong>Email Address:</strong></td>
						<td style="text-align: left;">${customer.email_id}</td>									
					</tr>
					<tr>
						<td><strong>Firm Name:</strong></td>
						<td style="text-align: left;">${customer.firm_name}</td>	
						<td><strong>Company Statutory Type:</strong></td>
						<td style="text-align: left;">${customer.compStatType.company_statutory_name}</td>
						<td><strong>Industry Type:</strong></td>
						<td style="text-align: left;">${customer.industryType.industry_name}</td>
					</tr>
					<tr>
						<td><strong>Land line no.:</strong></td>
						<td style="text-align: left;">${customer.landline_no}</td>		
						<td><strong>Aadhar Number:</strong></td>
						<td style="text-align: left;">${customer.adhaar_no}</td>
						<td><strong>Owner PAN Number:</strong></td>
						<td style="text-align: left;">${customer.owner_pan_no}</td>	
					</tr>
					<tr>
						<td><strong>GST Applicable:</strong></td>
						<td style="text-align: left;">${customer.gst_applicable==true ? "Yes" : "No"}</td>
						<td><strong>GST Number:</strong></td>
						<td style="text-align: left;">${customer.gst_no}</td>
						<td><strong>Company PAN Number:</strong></td>
						<td style="text-align: left;">${customer.company_pan_no}</td>
					</tr>
					<tr>					
						<td><strong>Current Address:</strong></td>
						<td style="text-align: left;" >${customer.current_address}</td>
						<td><strong>Permanent Address:</strong></td>
						<td style="text-align: left;" >${customer.permenant_address}</td>
						<td><strong>Country:</strong></td>
						<td style="text-align: left;" >${customer.country.country_name}</td>
					</tr>
					<tr>
						<td><strong>State:</strong></td>
						<td style="text-align: left;" >${customer.state.state_name} - ${customer.state.state_code}</td>	
						<td><strong>City:</strong></td>
						<td style="text-align: left;" >${customer.city.city_name}</td>
						<td><strong>Pin Code:</strong></td>
						<td style="text-align: left;" >${customer.pincode}</td>					
					</tr>
					<tr>
						<td><strong>Other Tax No:</strong></td>
						<td style="text-align: left;">${customer.other_tax_no}</td>
						<td><strong>TAN No:</strong></td>							
						<td style="text-align: left;">${customer.tan_no}</td>		
						<td><strong>TDS Applicable :</strong></td>
						<td style="text-align: left;" >${customer.tds_applicable==1 ? "Yes" : "No"}</td>				
					</tr>
					<tr>						
						<td><strong>TDS Type:</strong></td>
						<td style="text-align: left;" >${customer.deductee.deductee_title}</td>
						<td><strong>TDS Rate :</strong></td>
						<td style="text-align: left;" >${customer.tds_rate}</td>
						<td><strong>Status:</strong></td>
						<td style="text-align: left;">${customer.status==true ? "Enable" : "Disable"}</td>		
					</tr>
					<tr>
						<td><strong>Closing Balance:</strong></td>
						<td style="text-align: left;">${(customer.openingbalances.debit_balance)-(customer.openingbalances.credit_balance)}</td>
					    <td><strong>Approval Status:</strong></td>
						<td style="text-align: left;">${customer.customer_approval == 0 ? "Pending" : customer.customer_approval == 1 ? "Rejected" : customer.customer_approval == 2 ? "Primary Approval" : "Approved"}</td>
						<td></td>
						<td></td>
					</tr>
					
				</table>
			</div>
			
			<div class="col-md-12">
			<div class = "view-panels row">
					<div class="pn-row col-md-12">Sub Ledgers</div>
					<c:forEach var = "subledger" items = "${customer.subLedgers}">
					<div class="col-md-6 col-xs-6 colrow">
					${subledger.ledger.ledger_name}: ${subledger.subledger_name}
				    </div>
					</c:forEach>
				</div>
				<div class = "view-panels row">
					<div class="pn-row col-md-12">
							Products					
					</div>
					<c:forEach var = "product" items = "${customer.product}">
					<div class="col-md-6 col-xs-6 colrow">
					${product.product_name}
				    </div>
					</c:forEach>
				</div>
		</div>
			<div class="row"  style = "text-align: center; margin:15px;">
			<c:choose>
			     <c:when test="${(role=='5') || (role=='6') || (role=='7') || (role=='1')}">		
						<c:if test="${((flag==1) && ((customer.customer_approval==0) || (customer.customer_approval==1)))}">
							<button class="fassetBtn" type="button" onclick = "edit(${customer.customer_id})">
								<spring:message code="btn_edit" />
							</button>
						</c:if>	
				</c:when>
				<c:otherwise>
							<button class="fassetBtn" type="button" onclick = "edit(${customer.customer_id})">
								<spring:message code="btn_edit" />
							</button>
				</c:otherwise>
			</c:choose>
				<button class="fassetBtn" type="button" onclick = "back(${flag})">
					<spring:message code="btn_back" />
				</button>
			</div>
		</form:form>
	</div>
<script type="text/javascript">
	
	$(function() {		
	    setTimeout(function() {
	        $("#successMsg").hide()
	    }, 3000);
	});
	
	function edit(id){
		window.location.assign('<c:url value = "editCustomers"/>?id='+id);	
	}
	
	function back(flag){
		
		if(flag==1)
		{
			<c:if test="${importFlag==true}">  
			window.location.assign('<c:url value = "customersList"/>');
			</c:if>
			<c:if test="${importFlag==false}">  
			window.location.assign('<c:url value = "customerimportfailure"/>');
			</c:if>
					
		}
		else if(flag==2)
		{						
			window.location.assign('<c:url value = "customerPrimaryValidationList"/>');			
		}
		else if(flag==3)
		{			
			window.location.assign('<c:url value = "customerSecondaryValidationList"/>');		
		}	
		
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>