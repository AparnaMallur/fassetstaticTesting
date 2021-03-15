<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>

<script type="text/javascript" src="${valid}"></script>
<div class="breadcrumb">
	<h3>Group</h3>					
	<a href="homePage">Home</a> » <a href="accgroupList">Group</a> » <a href="#">View</a>
</div>
	<div class="fassetForm">
		<form:form id="AccountGroupForm"  commandName = "group">
			<div class="col-md-12">
				<table class = "table">
					<tr>
						<td><strong>Account Group Name:</strong></td>
						<td style="text-align: left;">${group.group_name}</td>			
					</tr>
					<tr>
						<td><strong>Group Type:</strong></td>
						<td style="text-align: left;">${gptype.account_group_name}</td>			
					</tr>
					<tr>
						<td><strong>Posting Title:</strong></td>
						<td style="text-align: left;">${pside.posting_title}</td>			
					</tr>
					<tr>
						<td><strong>Status:</strong></td>
						<td style="text-align: left;">${group.status==true ? "Enable" : "Disable"}</td>			
					</tr>
					
				</table>
			</div>
			<div class="col-md-12 text-center" >
				<button class="fassetBtn" type="button" onclick = "edit(${group.group_Id})">
					<spring:message code="btn_edit" />
				</button>
				<button class="fassetBtn" type="button" onclick = "back()">
					<spring:message code="btn_back" />
				</button>
			</div>
		</form:form>
	</div>
<script type="text/javascript">

	function edit(id){
		window.location.assign('<c:url value = "editAccountGroup"/>?id='+id);	
	}
	
	function back(){
		window.location.assign('<c:url value = "accgroupList"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>