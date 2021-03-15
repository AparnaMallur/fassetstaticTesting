<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/images/approve.png" var="approveImg" />
<spring:url value="/resources/images/reject.png" var="rejectImg" />
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<script type="text/javascript" src="${valid}"></script>

<div class="breadcrumb">
	<h3>Approvals For Editing Last Year Account</h3>
	<a href="homePage">Home</a> » <a href="#">Approvals For Editing Last Year Account</a>
</div>	
<div class="col-md-12" >	
	<c:if test="${successMsg != null}">
		<div class="successMsg" id = "successMsg"> 
			<strong>${successMsg}</strong>
		</div>
	</c:if>

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
			    <th data-field="Company Name" data-filter-control="input" data-sortable="true" >Company Name</th>		
				<th  data-field="FromDate" data-filter-control="input" data-sortable="true" >From Date</th>
				<th  data-field="ToDate" data-filter-control="input" data-sortable="true" >To Date</th>
				<th class='test' >Approve</th>			
			</tr>
			</thead>
			<tbody>			
			<c:forEach var = "year" items = "${yearList}">
				<tr>
				    <td style="text-align: left;">${year.company.company_name}</td>
					<td style="text-align: left;">${year.fromDate}</td>
					<td style="text-align: left;">${year.toDate}</td>
					<td style="text-align: left;">
						<a href = "#" onclick = "approve('${year.year_ending_id}')" title="Approve"><img src='${approveImg}' style = "width: 20px;"/></a>
					</td>	
				</tr>
			</c:forEach>
			</tbody>
		</table>
</div>
	</div>

<script type="text/javascript">
var proList = [];
$(function() {		
    setTimeout(function() {
        $("#successMsg").hide()
    }, 5000);
});

	function approve(id){
		window.location.assign('<c:url value="approvalOfCompanyForEditingLastYear"/>?id='+id);
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>