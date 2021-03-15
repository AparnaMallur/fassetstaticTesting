<%@include file="/WEB-INF/includes/header.jsp"%>
<div class="breadcrumb">
	<h3>Stock Report Deatils</h3>
	<a href="homePage">Home</a> » <a href="stockReportList">Stock Report</a> 
</div>
<div class="fassetForm">
	<form:form id="invetoryQuantityForm"  commandName = "inventoryQuantityAdjustment">
		<div class="col-md-12">
			<table class = "table">
				<tr>
					<td><strong>Date:</strong></td>
					<td style="text-align: left;">${inventoryQuantityAdjustment.date}</td>			
				</tr>				
				<tr>
					<td><strong>Product Name:</strong></td>
					<td style="text-align: left;">${inventoryQuantityAdjustment.product.product_name}</td>			
				</tr>				
				<tr>
					<td><strong>Quantity:</strong></td>
					<td style="text-align: left;">${inventoryQuantityAdjustment.quantity}</td>			
				</tr>				
				<tr>
					<td><strong>Status:</strong></td>
					<td style="text-align: left;">${inventoryQuantityAdjustment.status==true ? "Active" : "Inactive"}</td>
				</tr>
			</table>
		</div>
		<div class="col-md-12"  style = "text-align: center; margin:15px;">
			<button class="fassetBtn" type="button" onclick = "back();">
				<spring:message code="btn_back" />
			</button>
		</div>
	</form:form>
</div>
<script type="text/javascript">
	function back(){
		window.location.assign('<c:url value = "stockReport"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>