<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_EIGHTEEN%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THIRTY%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>
<div class="breadcrumb">
	<h3>Sales Entry</h3>					
	<a href="homePage">Home</a> » <a href="salesEntryReportList">Sales Entry Report</a> » <a href="#">View</a>
</div>
<div class="fassetForm">
		<form:form id="SalesEntryForm"  commandName = "entry">
			<div class="col-md-12">
				<table class = "table">
					<tr>
						<td><strong>Customer Name:</strong></td>
						<td style="text-align: left;">${entry.customer.contact_name}</td>
						<td><strong>Customer Bill Date:</strong></td>
						<td style="text-align: left;">${entry.customer_bill_date}</td>
						<td><strong>Customer Bill Number:</strong></td>
						<td style="text-align: left;">${entry.customer_bill_no}</td>					
					</tr>
					<tr>
						<td><strong>SubLedger Name:</strong></td>
						<td style="text-align: left;">${entry.subledger.subledger_name}</td>
						<td><strong>Remark:</strong></td>
						<td style="text-align: left;" >${entry.remark}</td>
						<td><strong>Transaction Amount:</strong></td>
						<td style="text-align: left;" >${entry.transaction_value}</td>
					</tr>
					<tr>
					    <td><strong>CGST:</strong></td>
						<td style="text-align: left;">${entry.cgst}</td>
						<td><strong>SGST:</strong></td>
						<td style="text-align: left;">${entry.sgst}</td>
						<td><strong>IGST:</strong></td>
						<td style="text-align: left;">${entry.igst}</td>
					</tr>
					<tr>
					    <td><strong>Total Amount:</strong></td>
						<td style="text-align: left;">${entry.round_off}</td>
						<td><strong>SCT:</strong></td>
						<td style="text-align: left;">${entry.state_compansation_tax}</td>
					</tr>
				</table>
			</div>
			<table class="table table-bordered table-striped">
			<thead>
					<tr>
						<th>Product </th>
						<th>Quantity</th>
						<th>UOM</th>
						<th>HSN/SAC Code</th>
						<th>Rate</th>
						<th>Discount(%)</th>
						<th>Labour Charge</th>
						<th>Freight Charges</th>
						<th>Others</th>
						<th>CGST</th>
						<th>SGST</th>
						<th>IGST</th>
						<th>SCT</th>
						
					</tr>
			</thead>
			<tbody>
						<c:forEach var = "prod_list" items = "${customerProductList}">	
								<tr>
									<td>${prod_list.product_name}</td>
									<td>${prod_list.quantity}</td>
									<td>${prod_list.UOM}</td>
									<td>${prod_list.HSNCode}</td>
									<td>${prod_list.rate}</td>
									<td>${prod_list.discount}</td>
									<td>${prod_list.labour_charges}</td>
									<td>${prod_list.freight}</td>
									<td>${prod_list.others}</td>
									<td>${prod_list.CGST}</td>
									<td>${prod_list.SGST}</td>
									<td>${prod_list.IGST}</td>
									<td>${prod_list.state_com_tax}</td>
								</tr>
							</c:forEach>	
			</tbody>
		</table>
			<div class="col-md-12"  style = "text-align: center; margin:15px;">
				<button class="fassetBtn" type="button" onclick = "back()">
					<spring:message code="btn_back" />
				</button>
			</div>
		</form:form>
	</div>
<script type="text/javascript">
	function back(){
		window.location.assign('<c:url value = "salesEntryReportList"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>