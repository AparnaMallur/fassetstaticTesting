<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>

<script type="text/javascript" src="${valid}"></script>


<div class="breadcrumb">
	<h3>Ledger</h3>
	<a href="homePage">Home</a> »
	<c:if test="${flag==1}">
		 <a href="ledgerList">Ledger</a> 	
	</c:if>
	<c:if test="${flag==2}">
	 <a href="customerPrimaryValidationList">Ledger</a> 	
	</c:if>
	<c:if test="${flag==3}">
	 <a href="customerSecondaryValidationList">Ledger</a> 	
	</c:if>	
	 » <a href="#">View</a>
</div>	
	
	<div class="fassetForm">
		<form:form id="LedgerForm"  commandName = "ledger">
			<div class="col-md-12">
				<table class = "table">
				     <tr>
						<td><strong>Ledger Name:</strong></td>
						<td style="text-align: left;">${ledger.ledger_name}</td>			
					</tr>
					<tr>
						<td><strong>Account Sub Group Name:</strong></td>
						<td style="text-align: left;">${subgroup.subgroup_name}</td>			
					</tr>
					<tr>
						<td><strong>Account Group Name:</strong></td>
						<td style="text-align: left;">${group.group_name}</td>			
					</tr>
					<tr>
						<td><strong>Account Group type:</strong></td>
						<td style="text-align: left;">${type.account_group_name}</td>			
					</tr>
				<tr>
						<td><strong>Credit Balance:</strong></td>
						<td style="text-align: left;">${ledger.openingbalances.credit_balance}</td>			
					</tr>
					<tr>
						<td><strong>Debit Balance:</strong></td>
						<td style="text-align: left;">${ledger.openingbalances.debit_balance}</td>			
					</tr>
					<tr>
						<td><strong>Is Sub Ledger:</strong></td>
								<td style="text-align: left;">${ledger.as_subledger==true ? "Yes" : "No"}</td>			
					</tr>
				<tr>
					<td><strong>Status:</strong></td>
					<td style="text-align: left;">${ledger.status==true ? "Enable" : "Disable"}</td>			
				</tr>
				<tr>
					<td><strong>Approval Status:</strong></td>
					<td style="text-align: left;">${ledger.ledger_approval == 0 ? "Pending" : ledger.ledger_approval == 1 ? "Rejected" : ledger.ledger_approval == 2 ? "Primary Approval" : "Approved"}</td>			
				</tr>
				</table>
			</div>
			<div class="col-md-12"  style = "text-align: center; margin:15px;">
			<c:choose>
				<c:when test="${(role=='5') || (role=='6') || (role=='7') || (role=='1')}">	
					<c:if test="${(ledger.allocated != true) && ((ledger.ledger_approval==0) || (ledger.ledger_approval==1))}">		
						<c:if test="${flag==1}">
							<button class="fassetBtn" type="button" onclick = "edit(${ledger.ledger_id})">
								<spring:message code="btn_edit" />
							</button>
						</c:if>
					</c:if>
				</c:when>
				<c:otherwise>
							<button class="fassetBtn" type="button" onclick = "edit(${ledger.ledger_id})">
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

	function edit(id){
		window.location.assign('<c:url value = "editLedger"/>?id='+id);	
	}
	
	function back(flag){
		
		if(flag==1)
		{
			<c:if test="${importFlag==true}">  
			window.location.assign('<c:url value = "ledgerList"/>');
			</c:if>
			<c:if test="${importFlag==false}">  
			window.location.assign('<c:url value = "ledgerimportfailure"/>');
			</c:if>		
		}
		else if(flag==2)
		{						
			window.location.assign('<c:url value = "ledgerPrimaryValidationList"/>');			
		}
		else if(flag==3)
		{			
			window.location.assign('<c:url value = "ledgerSecondaryValidationList"/>');		
		}	
		else if(flag==4)
		{			
			window.location.assign('<c:url value = "ledgerList"/>');		
		}	
		
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>