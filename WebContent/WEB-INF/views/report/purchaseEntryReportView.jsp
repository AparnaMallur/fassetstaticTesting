<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_EIGHTEEN%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THIRTY%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>
<div class="breadcrumb">
	<h3>Purchase Register</h3>					
	<a href="homePage">Home</a> » <a href="purchaseEntryReportList">Purchase Register</a> » <a href="#">View</a>
</div>
<div class="fassetForm">
		<form:form id="PurchaseEntryForm"  commandName = "entry">
			<div class="col-md-12">
				<table class = "table">
					<tr>
						<td><strong>Supplier Name:</strong></td>
						<td style="text-align: left;">${entry.supplier.contact_name}</td>
						<td><strong>Supplier Bill Date:</strong></td>
						<td style="text-align: left;">${entry.supplier_bill_date}</td>
						<td><strong>Supplier Bill Number:</strong></td>
						<td style="text-align: left;">${entry.supplier_bill_no}</td>					
					</tr>
					<tr>
						<td><strong>GRN Date:</strong></td>
						<td style="text-align: left;">${entry.grn_date}</td>	
						<td><strong>SubLedger Name:</strong></td>
						<td style="text-align: left;">${entry.subledger.subledger_name}</td>
						<td><strong>Voucher Bill date:</strong></td>
						<td style="text-align: left;">${entry.voucher_date}</td>
					</tr>
					<tr>
						<td><strong>Remark:</strong></td>
						<td style="text-align: left;" >${entry.remark}</td>
						<td><strong>Transaction Amount:</strong></td>
						<td style="text-align: left;" >${entry.transaction_value}</td>
						<td><strong>CGST:</strong></td>
						<td style="text-align: left;">${entry.cgst}</td>
					</tr>
					<tr>
						<td><strong>SGST:</strong></td>
						<td style="text-align: left;">${entry.sgst}</td>
						<td><strong>IGST:</strong></td>
						<td style="text-align: left;">${entry.igst}</td>
						<td><strong>SCT:</strong></td>
						<td style="text-align: left;">${entry.state_compansation_tax}</td>
					</tr>
					<tr>
						<td><strong>Total Amount:</strong></td>
						<td style="text-align: left;">${entry.round_off}</td>
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
						<c:forEach var = "prod_list" items = "${suppilerproductList}">	
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
		window.location.assign('<c:url value = "purchaseEntryReportList"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>