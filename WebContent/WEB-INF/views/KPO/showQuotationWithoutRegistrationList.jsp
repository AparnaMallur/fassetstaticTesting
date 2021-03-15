<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<spring:url value="/resources/images/delete.png" var="deleteImg" />

<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_EIGHTEEN%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THIRTY%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>
<div class="breadcrumb">
	<h3>Pending Quotation List</h3>					
	<a href="homePage">Home</a>
</div>	
	<div class = "borderForm" >
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
				<th  data-field="fisrt_name" data-filter-control="input" data-sortable="true" >First Name</th>
				<th  data-field="quoteno" data-filter-control="input" data-sortable="true" >Quotation No</th>
				<th  data-field="date" data-filter-control="input" data-sortable="true" >Date</th>
				<th  data-field="company_name" data-filter-control="input" data-sortable="true" >Company Name</th>
				<th  data-field="email" data-filter-control="input" data-sortable="true" >Email</th>
				<th  data-field="mobile_no" data-filter-control="select" data-sortable="true" >Mobile</th>
				<th  data-field="status" data-filter-control="select" data-sortable="true" >Status</th>	
			</tr>
			</thead>
			<tbody>
			<c:forEach var = "quote" items = "${quoteList}">
				<tr>
					<td style="text-align: left;">${quote.first_name} 
					${quote.last_name} </td>	
					 <td style="text-align: left;">${quote.quotation_no}</td>	
					 
					 <td style="text-align: left;">
						<fmt:parseDate value="${quote.created_date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
	                    <fmt:formatDate value="${parsedDate}" var="quotaDate" type="date" pattern="dd-MM-yyyy"/>
						${quotaDate}</td>						 
					<td style="text-align: left;">${quote.company_name}</td>	
					<td style="text-align: left;">${quote.email}</td>	
					<td style="text-align: left;">${quote.mobile_no}</td>					
					<td style="text-align: left;">${quote.status==true ? "Enable" : "Disable"}</td>
					
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
<script type="text/javascript">
$(function() {		
    setTimeout(function() {
        $("#successMsg").hide()
    }, 3000);
});
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>
