<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<c:set var="APPROVAL_STATUS_SECONDARY"><%=MyAbstractController.APPROVAL_STATUS_SECONDARY%></c:set>
<c:set var="ROLE_SUPERUSER"><%=MyAbstractController.ROLE_SUPERUSER%></c:set>
<div class="breadcrumb">
	<h3>Bank</h3>
	<a href="homePage">Home</a> »
	<c:if test="${flag==1}">
		 <a href="bankList">Bank</a> 	
	</c:if>
	<c:if test="${flag==2}">
	 <a href="bankPrimaryValidationList">Bank</a> 	
	</c:if>
	<c:if test="${flag==3}">
	 <a href="bankSecondaryValidationList">Bank</a> 	
	</c:if>	
	 » <a href="#">View</a>
</div>	
<div class="fassetForm">
	<form:form id="bankForm"  commandName = "bank">
		<div class="col-md-12">
			<table class = "table">
				<tr>
					<td><strong>Bank Name:</strong></td>
					<td style="text-align: left;">${bank.bank_name}</td>			
				</tr>
				
				<tr>
					<td><strong>Branch Name:</strong></td>
					<td style="text-align: left;">${bank.branch}</td>			
				</tr>
				
				<tr>
					<td><strong>Account Number:</strong></td>
					<td style="text-align: left;">${bank.account_no}</td>			
				</tr>
				
				<tr>
					<td><strong>IFSC Code:</strong></td>
					<td style="text-align: left;">${bank.ifsc_no}</td>			
				</tr>
				
				<tr>
					<td><strong>Account Sub Group Name:</strong></td>
					<td style="text-align: left;">${bank.account_sub_group_id.subgroup_name}</td>			
				</tr>			
								<tr>
						<td><strong>Credit Balance:</strong></td>
						<td style="text-align: left;">${bank.openingbalances.credit_balance}</td>			
					</tr>
					<tr>
						<td><strong>Debit Balance:</strong></td>
						<td style="text-align: left;">${bank.openingbalances.debit_balance}</td>			
					</tr>
				<tr>
					<td><strong>Status:</strong></td>
					<td style="text-align: left;">${bank.status==true ? "Enable" : "Disable"}</td>
				</tr>
				<tr>
					<td><strong>Approval Status:</strong></td>
					<td style="text-align: left;">${bank.bank_approval == 0 ? "Pending" : bank.bank_approval == 1 ? "Rejected" : bank.bank_approval == 2 ? "Primary Approval" : "Approved"}</td>
				</tr>
				
			</table>
		</div>
		<div class="col-md-12"  style = "text-align: center; margin:15px;">
			<c:choose>
				<c:when test="${(role=='5') || (role=='6') || (role=='7') || (role=='1')}">	
						<c:if test="${((flag==1) && ((bank.bank_approval==0) || (bank.bank_approval==1)))}">
									<button class="fassetBtn" type="button"
										onclick="edit(${bank.bank_id})" id='edit-bank'>
										<spring:message code="btn_edit" />
									</button>
						</c:if>
				</c:when>
				<c:otherwise>
						     <button class="fassetBtn" type="button"
										onclick="edit(${bank.bank_id})" id='edit-bank'>
										<spring:message code="btn_edit" />
							</button>
				</c:otherwise>
			</c:choose>
			<button class="fassetBtn" type="button" onclick = "back(${flag});">
				<spring:message code="btn_back" />
			</button>
			
			
		</div>
	</form:form>
</div>
<script type="text/javascript">

	function edit(id){
		window.location.assign('<c:url value = "editBank"/>?id='+id);	
	}
	
	function back(flag){
		if(flag==1)
			{
			<c:if test="${importFlag==true}">  
			window.location.assign('<c:url value = "bankList"/>');
			</c:if>
			<c:if test="${importFlag==false}">  
			window.location.assign('<c:url value = "bankimportfailure"/>');
			</c:if>		
			}
		else if(flag==2)
			{						
				window.location.assign('<c:url value = "bankPrimaryValidationList"/>');			
			}
		else if(flag==3)
			{			
				window.location.assign('<c:url value = "bankSecondaryValidationList"/>');		
			}		
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>
