<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_EIGHTEEN%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THIRTY%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>
<div class="breadcrumb">
	<h3>Payment</h3>
	<a href="homePage">Home</a> » <a href="paymentReportList">Payment</a> » <a href="#">View</a>
</div>	
	<div class="fassetForm">
		<form:form id="paymentForm"  commandName = "payment">
			<div class="col-md-12">
				<table class = "table">
					<tr>
						<td><strong>Voucher Number:</strong></td>
						<td style="text-align: left;">${payment.voucher_no.voucher_no}</td>
						<td><strong>Date:</strong></td>
						<td style="text-align: left;">${payment.dateString}</td>
					</tr>
					<tr>
						<td><strong>Supplier Name:</strong></td>
						<td style="text-align: left;">${payment.supplier.contact_name}</td>	
						<td><strong>Supplier Bill Number:</strong></td>
						<td style="text-align: left;">${payment.supplier_bill_no}</td>
					</tr>
					<tr>
						<td><strong>Cheque Number:</strong></td>
						<td style="text-align: left;">${payment.cheque_dd_no}</td>
						<td><strong>Cheque Date:</strong></td>
						<td style="text-align: left;">${payment.chequeDateString}</td>
					</tr>
					<tr>
						<td><strong>Amount:</strong></td>
						<td style="text-align: left;">${payment.amount}</td>
						<td><strong>Remark:</strong></td>
						<td style="text-align: left;">${payment.other_remark}</td>
					</tr>
				</table>
			</div>
			<div class="col-md-12"  style = "text-align: center; margin:15px;">
				<button class="fassetBtn" type="button" onclick = "back()">
					<spring:message code="btn_back" />
				</button>
			</div>
		</form:form>
	</div>
<script type="text/javascript">
	function back(){
		window.location.assign('<c:url value = "paymentReportList"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>