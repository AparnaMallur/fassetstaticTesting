<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>
<div class="breadcrumb">
	<h3>Country</h3>					
	<a href="homePage">Home</a> » <a href="countryList">Country</a> » <a href="#">View</a>
</div>	
<div class="fassetForm">
	<form:form id="countryForm"  commandName = "country">
		<div class="col-md-12">
			<table class = "table">
				<tr>
					<td><strong>Name:</strong></td>
					<td style="text-align: left;">${country.country_name}</td>			
				</tr>
				<tr>
					<td><strong>Status:</strong></td>
					<td style="text-align: left;">${country.status==true ? "Enable" : "Disable"}</td>			
				</tr>
			</table>
		</div>
		<div class="col-md-12"  style = "text-align: center; margin:15px;">
			<button class="fassetBtn" type="button" onclick = "edit(${country.country_id})">
				<spring:message code="btn_edit"/>
			</button>
			<button class="fassetBtn" type="button" onclick = "back()">
				<spring:message code="btn_back"/>
			</button>
		</div>
	</form:form>
</div>
<script type="text/javascript">

	function edit(id){
		window.location.assign('<c:url value = "editCountry"/>?id='+id);	
	}
	
	function back(){
		window.location.assign('<c:url value = "countryList"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>