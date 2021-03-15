<%@include file="/WEB-INF/includes/header.jsp"%>

<div class="breadcrumb">
	<h3>City</h3>
	<a href="homePage">Home</a> » <a href="cityList">City</a> » <a href="#">View</a>
</div>	
<div class="fassetForm">
	<form:form id="cityForm"  commandName = "city">
		<div class="col-md-12">
			<table class = "table">
				<tr>
					<td><strong>Name:</strong></td>
					<td style="text-align: left;">${city.city_name}</td>			
				</tr>
				<tr>
					<td><strong> State Name:</strong></td>
					<td style="text-align: left;">${state.state_name}</td>			
				</tr>
				<tr>
					<td><strong>Country Name:</strong></td>
					<td style="text-align: left;">${cntry.country_name}</td>			
				</tr>
				<tr>
					<td><strong>Status:</strong></td>
					<td style="text-align: left;">${city.status==true ? "Enable" : "Disable"}</td>
				</tr>
			</table>
		</div>
		<div class="col-md-12"  style = "text-align: center; margin:15px;">
			<button class="fassetBtn" type="button" onclick = "edit(${city.city_id})">
				<spring:message code="btn_edit" />
			</button>
			<button class="fassetBtn" type="button" onclick = "back();">
				<spring:message code="btn_back" />
			</button>
		</div>
	</form:form>
</div>
<script type="text/javascript">

	function edit(id){
		window.location.assign('<c:url value = "editCity"/>?id='+id);	
	}
	
	function back(){
		window.location.assign('<c:url value = "cityList"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>