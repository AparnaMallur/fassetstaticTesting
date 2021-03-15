<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<script type="text/javascript" src="${valid}"></script>
<div class="breadcrumb">
	<h3>Activity Log</h3>
	<a href="homePage">Home</a> » <a href="activityLogReport">Activity Log</a>
</div>
<div class="col-md-12">
	<div class="borderForm">
		<table id="table" data-toggle="table" data-search="false"
			data-escape="false" data-filter-control="true"
			data-show-export="false" data-click-to-select="true"
			data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
			class="table">
			<thead>
				<tr>
				
				   <th data-field="name" data-filter-control="input"
						data-sortable="true">User Name</th>	
					<th data-field="date" data-filter-control="input"
						data-sortable="true">Date</th>
					<th data-field="particulars" data-filter-control="input"
						data-sortable="true">Particulars</th>
				    <th data-field="company" data-filter-control="input"
						data-sortable="true">Company Name</th>
					<th data-field="status" data-filter-control="input"
						data-sortable="true">Approval Status</th>
				   
				</tr>
			</thead>
			<tbody>
			 
				<c:if test="${loglist.size()>0}">
				
				<c:forEach var="log" items="${loglist}">	
				<c:if test="${log.customer!=null}">
						
									<tr class='n-style'>
									<td style="text-align: left;">${Approveduser.first_name} ${Approveduser.last_name}</td>
									 <td style="text-align: left;">
						<fmt:parseDate value="${log.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
	                    <fmt:formatDate value="${parsedDate}" var="logDate" type="date" pattern="dd-MM-yyyy"/>
						${logDate}</td>
									<td style="text-align: left;">Customer Name: ${log.customer.firm_name}</td>
									<td style="text-align: left;">${log.customer.company.company_name}</td>
									<td style="text-align: left;">${log.primary_approval == 2 ? "Primary Approval" : log.secondary_approval  == 3 ? "Secondary Approval": "Rejected"}</td>
									
									</tr>
						
				 </c:if>	 
				 <c:if test="${log.supplier!=null}">
						
									<tr class='n-style'>
									<td style="text-align: left;">${Approveduser.first_name} ${Approveduser.last_name}</td>
									 <td style="text-align: left;">
						<fmt:parseDate value="${log.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
	                    <fmt:formatDate value="${parsedDate}" var="logDate" type="date" pattern="dd-MM-yyyy"/>
						${logDate}</td>
									<td style="text-align: left;">Supplier Name: ${log.supplier.company_name}</td>
									<td style="text-align: left;">${log.supplier.company.company_name}</td>
									<td style="text-align: left;">${log.primary_approval == 2 ? "Primary Approval" : log.secondary_approval  == 3 ? "Secondary Approval": "Rejected"}</td>
									</tr>
						
				 </c:if>     
				  <c:if test="${log.product!=null}">
						
									<tr class='n-style'>
									<td style="text-align: left;">${Approveduser.first_name} ${Approveduser.last_name}</td>
									 <td style="text-align: left;">
						<fmt:parseDate value="${log.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
	                    <fmt:formatDate value="${parsedDate}" var="logDate" type="date" pattern="dd-MM-yyyy"/>
						${logDate}</td>
									<td style="text-align: left;">Product Name: ${log.product.product_name}</td>
									<td style="text-align: left;">${log.product.company.company_name}</td>
									<td style="text-align: left;">${log.primary_approval == 2 ? "Primary Approval" : log.secondary_approval  == 3 ? "Secondary Approval": "Rejected"}</td>
									
									</tr>
						
				 </c:if> 
				  <c:if test="${log.ledger!=null}">
						
									<tr class='n-style'>
									<td style="text-align: left;">${Approveduser.first_name} ${Approveduser.last_name}</td>
									 <td style="text-align: left;">
						<fmt:parseDate value="${log.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
	                    <fmt:formatDate value="${parsedDate}" var="logDate" type="date" pattern="dd-MM-yyyy"/>
						${logDate}</td>
									<td style="text-align: left;">Ledger Name: ${log.ledger.ledger_name}</td>
									<td style="text-align: left;">${log.ledger.company.company_name}</td>
									<td style="text-align: left;">${log.primary_approval == 2 ? "Primary Approval" : log.secondary_approval  == 3 ? "Secondary Approval": "Rejected"}</td>
									
									</tr>
						
				 </c:if>   
				  <c:if test="${log.subLedger!=null}">
						
									<tr class='n-style'>
									<td style="text-align: left;">${Approveduser.first_name} ${Approveduser.last_name}</td>
									 <td style="text-align: left;">
						<fmt:parseDate value="${log.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
	                    <fmt:formatDate value="${parsedDate}" var="logDate" type="date" pattern="dd-MM-yyyy"/>
						${logDate}</td>
									<td style="text-align: left;">SubLedger Name: ${log.subLedger.subledger_name}</td>
									<td style="text-align: left;">${log.subLedger.company.company_name}</td>
									<td style="text-align: left;">${log.primary_approval == 2 ? "Primary Approval" : log.secondary_approval  == 3 ? "Secondary Approval": "Rejected"}</td>
									
									</tr>
						
				 </c:if> 
				 <c:if test="${log.bank!=null}">
						
									<tr class='n-style'>
									<td style="text-align: left;">${Approveduser.first_name} ${Approveduser.last_name}</td>
									 <td style="text-align: left;">
						<fmt:parseDate value="${log.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
	                    <fmt:formatDate value="${parsedDate}" var="logDate" type="date" pattern="dd-MM-yyyy"/>
						${logDate}</td>
									<td style="text-align: left;">Bank Name: ${log.bank.bank_name}</td>
									<td style="text-align: left;">${log.bank.company.company_name}</td>
									<td style="text-align: left;">${log.primary_approval == 2 ? "Primary Approval" : log.secondary_approval  == 3 ? "Secondary Approval": "Rejected"}</td>
									
									</tr>
						
				 </c:if> 
				  <c:if test="${log.subscription!=null}">
						
									<tr class='n-style'>
									<td style="text-align: left;">${Approveduser.first_name} ${Approveduser.last_name}</td>
									 <td style="text-align: left;">
						<fmt:parseDate value="${log.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
	                    <fmt:formatDate value="${parsedDate}" var="logDate" type="date" pattern="dd-MM-yyyy"/>
						${logDate}</td>
									<td style="text-align: left;">Quotation Number And Name: ${log.subscription.quotation_id.quotation_no}----${log.subscription.quotation_id.first_name} ${log.subscription.quotation_id.last_name}</td>
									<td style="text-align: left;">${log.subscription.company.company_name}</td>
									<td style="text-align: left;">Quotation Submitted</td>
									</tr>
						
				 </c:if> 
				</c:forEach>
				
				 </c:if>
				
			</tbody>
		</table>
	</div>
</div>


<div class="col-md-12">
	<div class="borderForm">
		<table id="table" data-toggle="table" data-search="false"
			data-escape="false" data-filter-control="true"
			data-show-export="false" data-click-to-select="true"
			data-pagination="true" data-page-size="10" data-toolbar="#toolbar"
			class="table">
			<thead>
				<tr>
				
				    <th data-field="name" data-filter-control="input"
						data-sortable="true">User Name</th>
					<th data-field="LogIn" data-filter-control="input"
						data-sortable="true">LogIn/LogOut Time</th>
					<th data-field="Ip" data-filter-control="input"
						data-sortable="true">Ip Address</th>
				  
				</tr>
			</thead>
			<tbody>
			 
				<c:if test="${loginLogList.size()>0}">
				
				<c:forEach var="log" items="${loginLogList}">	
				
						
									<tr class='n-style'>
									<td style="text-align: left;">${Approveduser.first_name} ${Approveduser.last_name}</td>
									
									<c:if test="${log.type==1}">
									 <td style="text-align: left;">
									 LogIn: ${log.created_date}
						             </td>
						             </c:if>
						             <c:if test="${log.type==2}">
						             <td style="text-align: left;">
									  LogOut: ${log.created_date}
						             </td>
						              </c:if>
									<td style="text-align: left;">${log.ip_address}</td>
									</tr>
						
				 
				</c:forEach>
				
				 </c:if>
				
			</tbody>
		</table>
	</div>
</div>
<div class="row text-center-btn">
		 	<button class="fassetBtn" type="button" onclick = "back();">
				<spring:message code="btn_back" />
			</button>
</div>
<script type="text/javascript">
	$(function() {		
	    setTimeout(function() {
	        $("#successMsg").hide();
	    }, 3000);
	});
	function back(){
		window.location.assign('<c:url value = "activityLogReport"/>');	
	}
	
</script>
<%@include file="/WEB-INF/includes/footer.jsp"%>