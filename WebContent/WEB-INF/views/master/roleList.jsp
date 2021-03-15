<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>

<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<script type="text/javascript" src="${valid}"></script>

<div class="breadcrumb">
	<h3>Role</h3>
	<a href="homePage">Home</a> » <a href="#">Role</a>
</div>	
<div class="col-md-12">
	<div class = "borderForm">
		<table  class = "table">
			<thead>
				<tr>
					<th class='test' >Sr. No.</th>	
					<th class='test' >Role Name</th>	
					<th class='test' >Status</th>				
				</tr>
			</thead>
			<tbody>
				<c:forEach var = "role" items = "${roleList}">
					<tr>
						<td style="text-align: left;">${role.role_id}</td>					
						<td style="text-align: left;">${role.role_name}</td>
						<td style="text-align: left;">${role.status == true ? "Enable" : "Disable"}</td>					
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>
<%@include file="/WEB-INF/includes/footer.jsp" %>