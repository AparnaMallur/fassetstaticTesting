<%@include file="/WEB-INF/includes/header.jsp"%>

<div class="breadcrumb">
	<h3>Credit Note</h3>
	<a href="homePage">Home</a> » <a href="creditNoteList">Credit Note Report</a> » <a href="#">View</a>
</div>	
<div class="fassetForm">
	<form:form id="creditNoteForm"  commandName = "creditNote">
		<div class="col-md-12">
			<table class = "table">
				<tr>
					<td><strong>Voucher No.:</strong></td>
					<td style="text-align: left;">${creditNote.voucher_no.voucher_no}</td>			
				</tr>				
				<tr>
					<td><strong>Date:</strong></td>
					<td style="text-align: left;">${creditNote.date}</td>			
				</tr>				
				<tr>
					<td><strong>Supplier Name:</strong></td>
					<td style="text-align: left;">${creditNote.supplier.contact_name}</td>			
				</tr>				
				<tr>
					<td><strong>Amount:</strong></td>
					<td style="text-align: left;">${creditNote.amount}</td>			
				</tr>				
				<tr>
					<td><strong>Remark:</strong></td>
					<td style="text-align: left;">${creditNote.remark}</td>			
				</tr>					
				<tr>
					<td><strong>Status:</strong></td>
					<td style="text-align: left;">${creditNote.status==true ? "Active" : "Inactive"}</td>
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
		window.location.assign('<c:url value = "creditNoteReportList"/>');	
	}
	
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>