<%@include file="/WEB-INF/includes/header.jsp"%>

<div class="breadcrumb">
	<h3>Debit Note</h3>
	<a href="homePage">Home</a> » <a href="debitNoteReportList">Debit Note Report</a> » <a href="#">View</a>
</div>	
<div class="fassetForm">
	<form:form id="debitNoteForm"  commandName = "debitNote">
		<div class="col-md-12">
			<table class = "table">
				<tr>
					<td><strong>Voucher No.:</strong></td>
					<td style="text-align: left;">${debitNote.voucher_no.voucher_no}</td>			
				</tr>				
				<tr>
					<td><strong>Date:</strong></td>
					<td style="text-align: left;">${debitNote.date}</td>			
				</tr>				
				<tr>
					<td><strong>Customer Name:</strong></td>
					<td style="text-align: left;">${debitNote.customer.contact_name}</td>			
				</tr>								
				<tr>
					<td><strong>Amount:</strong></td>
					<td style="text-align: left;">${debitNote.amount}</td>			
				</tr>				
				<tr>
					<td><strong>Remark:</strong></td>
					<td style="text-align: left;">${debitNote.remark}</td>			
				</tr>					
				<tr>
					<td><strong>Status:</strong></td>
					<td style="text-align: left;">${debitNote.status==true ? "Active" : "Inactive"}</td>
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
		window.location.assign('<c:url value = "debitNoteReportList"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>