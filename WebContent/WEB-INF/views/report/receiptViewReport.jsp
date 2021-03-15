<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_EIGHTEEN%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THIRTY%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>
<spring:url value="/resources/images/delete.png" var="deleteImg" />

<div class="breadcrumb">
	<h3>Receipt</h3>
	<a href="homePage">Home</a> » <a href="receiptReportList">Receipt</a> » <a href="#">View</a>
</div>
	<div class="fassetForm">
		<form:form id="ReceiptForm"  commandName = "receipt">
			<div class="col-md-12">
				<table class = "table">
				<tr>
					<td><strong>Voucher No:</strong></td>
					<td style="text-align: left;">${receipt.voucher_no.voucher_no}</td>			
					<td><strong>Date:</strong></td>
					<td style="text-align: left;">
					<fmt:parseDate value="${receipt.date}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                    <fmt:formatDate value="${parsedDate}" var="date" type="date" pattern="dd-MM-yyyy" />
					${date}</td>			
				</tr>
				<tr>
					<td><strong>Customer Name:</strong></td>
					<td style="text-align: left;">${receipt.customer.contact_name}
                    </td>			
					<td><strong>Bill No:</strong></td>
					<td style="text-align: left;">${receipt.customer_bill_no}
                   </td>			
				</tr>
				<tr>
					<td><strong>Cheque No:</strong></td>
					<td style="text-align: left;">${receipt.cheque_no}</td>			
					<td><strong>Cheque Date:</strong></td>
					<td style="text-align: left;">
					${receipt.cheque_date}</td>			
				</tr>
				<tr>
					<td><strong>Advance Receipt:</strong></td>
					<td style="text-align: left;">${receipt.advance_payment==1 ? "Yes" : "No"}</td>			
					<td><strong>GST Applied:</strong></td>
					<td style="text-align: left;">${receipt.gst_applied==1 ? "Yes" : "No"}</td>			
				</tr>
				<tr>
					<td><strong> Amount:</strong></td>
					<td style="text-align: left;">${receipt.amount}</td>
					<td><strong>Other Remark:</strong></td>
					<td style="text-align: left;">${receipt.other_remark}</td>			
				</tr>
				<tr>
					<td><strong>Status:</strong></td>
					<td style="text-align: left;">${receipt.status==true ? "Active" : "Inactive"}</td>
					<td></td>
					<td></td>
				</tr>
				</table>
			</div>
		<c:if test="${(receipt.gst_applied == 1)}">
			
			<table class="table table-bordered table-striped">
			<thead>
					<tr>
						<th></th>
						<th>Product </th>
						<th>Quantity</th>
						<th>UOM</th>
						<th>HSN/SAC Code</th>
						<th>Rate</th>
						<th>Discount</th>
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
						<c:forEach var = "prod_list" items = "${customerproductList}">	
								<tr>
									<td><a href = "#" onclick = "deleteproductdetail(${prod_list.receipt_header_id})"><img src='${deleteImg}' style = "width: 20px;"/></a>
									</td>
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
		</c:if>
		
			<div class="col-md-12"  style = "text-align: center; margin:15px;">
				<button class="fassetBtn" type="button" onclick = "back()">
					<spring:message code="btn_back" />
				</button>
			</div>
		</form:form>
			
	</div>
<script type="text/javascript">	
	function back(){
		window.location.assign('<c:url value = "receiptReportList"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>