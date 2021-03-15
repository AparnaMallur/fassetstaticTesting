<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>
<div class="breadcrumb">
	<h3>Menu Master</h3>					
	<a href="homePage">Home</a> » <a href="MenuList">Menu Master</a> » <a href="#">View</a> 
</div>	
<div class="fassetForm">
	<form:form id="MenuMasterForm"  commandName = "menuMaster">
		<div class="col-md-12">
			<table class = "table">
				<tr>
					<td><strong>Menu Name:</strong></td>
					<td><strong>Menu Url:</strong></td>
					<td><strong>Sequence:</strong></td>					
					<td><strong>Parent:</strong></td>					
					<td><strong>Status:</strong></td>	
				</tr>
				<tr>
					 <td style="text-align: left;">${menuMaster.menu_name}</td> 
					 <td style="text-align: left;">${menuMaster.menu_url}</td> 
					 <td style="text-align: left;">${menuMaster.sequence_no}</td> 					 
					 <td style="text-align: left;">${MenuMaster.parent_id == Null ? "No Parent" : MenuMaster.parent_id.menu_name}</td>
					 <td style="text-align: left;">${menuMaster.status==true ? "Enable" : "Disable"}</td> 
				</tr>
			</table>
		</div>
		<div class="col-md-12"  style = "text-align: center; margin:15px;">
			<button class="fassetBtn" type="button" onclick = "edit(${menuMaster.menu_id})">
				<spring:message code="btn_edit"/>
			</button>
			<button class="fassetBtn" type="button" onclick = "back()">
				<spring:message code="btn_back"/>
			</button>
		</div>
	</form:form>
</div>
<script   type="text/javascript">

	function edit(id){
		window.location.assign('<c:url value = "editMenu"/>?id='+id);	
	}
	
	function back(){
		window.location.assign('<c:url value = "MenuList"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>