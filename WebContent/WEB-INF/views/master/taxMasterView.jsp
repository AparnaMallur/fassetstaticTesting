<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_EIGHTEEN%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THIRTY%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>
<div class="breadcrumb">
	<h3>TaxMaster</h3>					
	<a href="homePage">Home</a> » <a href="taxMasterList">TaxMaster</a> » <a href="#">View</a>
</div>
<div class="fassetForm">
		<form:form id="TaxMasterForm"  commandName ="taxMaster">
			<div class="col-md-12">
				<table class = "table">
					<tr>
						<td><strong>Tax Name:</strong></td>
						<td style="text-align: left;">${taxMaster.tax_name}</td>
						<td><strong>VAT:</strong></td>
						<td style="text-align: left;">${taxMaster.vat}</td>
					</tr>
					<tr>
						<td><strong>CST:</strong></td>
						<td style="text-align: left;">${taxMaster.cst}</td>
						<td><strong>EXCISE:</strong></td>
						<td style="text-align: left;">${taxMaster.excise}</td>	
					</tr>
					<tr>
						<td><strong>Status:</strong></td>
						<td style="text-align: left;">${taxMaster.status== true ? "Enable" : "Disable"}</td>	
						<td></td>
						<td></td>				
					</tr>
				</table>
			</div>
			<div class="col-md-12"  style = "text-align: center; margin:15px;">
				<button class="fassetBtn" type="button" onclick = "edit(${taxMaster.tax_id})">
					<spring:message code="btn_edit" />
				</button>
				<button class="fassetBtn" type="button" onclick = "back()">
					<spring:message code="btn_back" />
				</button>
			</div>
		</form:form>
	</div>
<script type="text/javascript">
	
	$(function() {		
	    setTimeout(function() {
	        $("#successMsg").hide()
	    }, 3000);
	});
	
	function edit(id){
		window.location.assign('<c:url value = "editTaxMaster"/>?id='+id);	
	}
	
	function back(){
		window.location.assign('<c:url value = "taxMasterList"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>