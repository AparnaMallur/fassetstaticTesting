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
	<h3>UnadjustedList</h3>
	<a href="homePage">Home</a> » <a href="UnadjustedList">Cash Payment and receipt having Unadjusted List</a>
</div>
<div class="col-md-12">	

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
					<th data-field="ledger" data-filter-control="input"
						data-sortable="true">Ledger</th>
					<th data-field="subLedger" data-filter-control="input"
						data-sortable="true">Sub Ledger</th>
					<th data-field="debitBalance" data-filter-control="input"
						data-sortable="true">Debit Balance</th>
					<th data-field="creditBalance" data-filter-control="input"
						data-sortable="true">Credit Balance</th>
					<th data-field="running" data-filter-control="input"
						data-sortable="true">Running Balance</th>
				</tr>
			</thead>
			<tbody>	
			
			
			</tbody>
		</table>
	</div>

</div>

<%@include file="/WEB-INF/includes/footer.jsp" %>
