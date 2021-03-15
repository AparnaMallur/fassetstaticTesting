<%@include file="/WEB-INF/includes/header.jsp"%>

<div class="breadcrumb">
	<h3>Unit of Measurement</h3>
	<a href="homePage">Home</a> » <a href="uomList">Unit of Measurement</a> » <a href="#">View</a>
</div>	
<div class="fassetForm">
	<form:form id="uomForm"  commandName = "uom">
		<div class="col-md-12">
			<table class = "table">
				<tr>
					<td><strong>Unit of Measurement:</strong></td>
					<td style="text-align: left;">${uom.unit}</td>			
				</tr>
				<tr>
					<td><strong>Status:</strong></td>
					<td style="text-align: left;">${uom.status==true ? "Enable" : "Disable"}</td>
				</tr>
			</table>
		</div>
		<div class="col-md-12"  style = "text-align: center; margin:15px;">
			<button class="fassetBtn" type="button" onclick = "edit(${uom.uom_id})">
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
		window.location.assign('<c:url value = "editUOM"/>?id='+id);	
	}
	
	function back(){
		window.location.assign('<c:url value = "uomList"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>