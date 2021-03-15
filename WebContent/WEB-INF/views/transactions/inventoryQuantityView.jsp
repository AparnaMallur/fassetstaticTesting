<%@include file="/WEB-INF/includes/header.jsp"%>
<div class="breadcrumb">
	<h3>Inventory Quantity Adjustment</h3>
	<a href="homePage">Home</a> » <a href="inventoryQuantityList">Inventory Quantity Adjustment</a> » <a href="#">View</a>
</div>
<div class="fassetForm">
	<form:form id="invetoryQuantityForm"  commandName = "invetoryQuantity">
		<div class="col-md-12">
			<table class = "table">
				<tr>
					<td><strong>Date:</strong></td>
					<td style="text-align: left;">
					<fmt:parseDate value="${inventoryQuantity.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                    <fmt:formatDate value="${parsedDate}" var="date" type="date" pattern="dd-MM-yyyy" />
					${date}</td>			
				</tr>				
				<tr>
					<td><strong>Product Name:</strong></td>
					<td style="text-align: left;">${inventoryQuantity.product.product_name}</td>			
				</tr>				
				<tr>
					<td><strong>Quantity:</strong></td>
					<td style="text-align: left;">${inventoryQuantity.quantity}</td>			
				</tr>
				
				<tr>
					<td><strong>Value Per Product:</strong></td>
					<td style="text-align: left;">${inventoryQuantity.value}</td>			
				</tr>	
							
				<tr>
					<td><strong>Total Quantity in Stock:</strong></td>
					<td style="text-align: left;">${inventoryQuantity.stock.quantity}</td>			
				</tr>
				<tr>
					<td><strong>Total Amount of Stock:</strong></td>
					<td style="text-align: left;">${inventoryQuantity.stock.amount}</td>			
				</tr>				
				<tr>
					<td><strong>Is Addition:</strong></td>
					<td style="text-align: left;">${inventoryQuantity.is_addition==true ? "Addition" : "Substraction"}</td>
				</tr>
				<tr>
					<td><strong>Financial Year:</strong></td>
					<td style="text-align: left;">${inventoryQuantity.accounting_year_id.year_range}</td>
				</tr>
			
				<tr>
					<td><strong>Remark:</strong></td>
					<td style="text-align: left;">${inventoryQuantity.remark}</td>			
				</tr>	
				
			</table>
		</div>
		<div class="col-md-12"  style = "text-align: center; margin:15px;">
			<%-- <button class="fassetBtn" type="button" onclick = "edit(${inventoryQuantity.inventory_adj_id})">
				<spring:message code="btn_edit" />
			</button> --%>
			<button class="fassetBtn" type="button" onclick = "back();">
				<spring:message code="btn_back" />
			</button>
		</div>
	</form:form>
</div>
<script type="text/javascript">

	function edit(id){
		window.location.assign('<c:url value = "editInvQuantity"/>?id='+id);	
	}
	
	function back(){
		window.location.assign('<c:url value = "inventoryQuantityList"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>