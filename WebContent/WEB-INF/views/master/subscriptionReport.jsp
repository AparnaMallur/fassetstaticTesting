<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/images/approve.png" var="approveImg" />
<spring:url value="/resources/images/reject.png" var="rejectImg" />
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<script type="text/javascript" src="${valid}"></script>

<div class="breadcrumb">
	<h3>Subscription Report</h3>
	<a href="homePage">Home</a>
</div>	
<div class="col-md-12" >	
	
	<div class = "borderForm">
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
				<th data-field="Company" data-filter-control="input" data-sortable="true" >Company Name</th>
				<th  data-field="User Name" data-filter-control="input" data-sortable="true" >Client Super User Name</th>
				<th  data-field="email" data-filter-control="input" data-sortable="true" >Email</th>
				<th  data-field="mobile_no" data-filter-control="select" data-sortable="true" >Mobile</th>			
				<th  data-field="From" data-filter-control="input" data-sortable="true" >Subscription From</th>
				<th  data-field="To" data-filter-control="input" data-sortable="true" >Subscription To</th>
				<th  data-field="Subscription" data-filter-control="input" data-sortable="true" >Total Quotation Amount</th>
			<!-- 	<th  data-field="Payment" data-filter-control="input" data-sortable="true" >Payment Mode</th> -->
			</tr>
			</thead>
			<tbody>			
			<c:forEach var = "subscri" items = "${subscriptionList}">
				<tr>				
					<td style="text-align: left;">${subscri.company.company_name}</td>
					<c:forEach var = "ClientSuperUser" items = "${subscri.company.user}">
					<c:if test="${(ClientSuperUser.role.role_id == '5')}">
					<td style="text-align: left;"> 
				     ${ClientSuperUser.first_name}  ${ClientSuperUser.last_name}
				     </td>	
				     <td style="text-align: left;">
				      ${ClientSuperUser.email}
				      </td>
				       <td style="text-align: left;">
				        ${ClientSuperUser.mobile_no}
				      </td>
				   </c:if>
				   </c:forEach>
					
					<td style="text-align: left;">${subscri.subscription_from}</td>
					<td style="text-align: left;">${subscri.subscription_to}</td>
					<td style="text-align: left;">${subscri.amount}</td>
					<%-- <td style="text-align: left;">${client.payment_mode == 1 ? "OFFLINE" : client.payment_mode == 2 ? "RTGS" : "ONLINE"}</td> --%>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
</div>
<script type="text/javascript">
$(function() {		
    setTimeout(function() {
        $("#successMsg").hide()
    }, 3000);
});
	function viewProduct(id){
		window.location.assign('<c:url value="viewClientSubscriptionReport"/>?id='+id);
	}
	
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>