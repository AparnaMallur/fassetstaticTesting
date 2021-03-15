
<%@include file="/WEB-INF/includes/mobileHeader.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/images/closeRed.png" var="deleteImg" />
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/notepad-n.png" var="notepadN" />
<spring:url value="/resources/images/notes-n.png" var="notesN" />
<spring:url value="/resources/images/pencil-n.png" var="pencilN" />
<spring:url value="/resources/images/users-n.png" var="usersN" />
<spring:url value="/resources/images/rupee-indian-n.png" var="rupeeIndN" />
<spring:url value="/resources/images/code-n.png" var="codeN" />
<spring:url value="/resources/images/discount-n.png" var="discountN" />
<spring:url value="/resources/images/freightcharge-n.png" var="freightchargeN" />
<spring:url value="/resources/images/labourcharge-n.png" var="labourchargeN" />
<spring:url value="/resources/images/other-n.png" var="otherN" />
<spring:url value="/resources/images/product-n.png" var="productN" />
<spring:url value="/resources/images/quantity-n.png" var="quantityN" />
<spring:url value="/resources/images/tax-n.png" var="taxN" />
<spring:url value="/resources/images/uom-n.png" var="uomN" />
<spring:url value="/resources/images/shippingbill-n.png" var="shippingbillN" />
<spring:url value="/resources/images/exporttype-n.png" var="exporttypeN" />
<spring:url value="/resources/images/billdate-n.png" var="billdateN" />
<spring:url value="/resources/images/portcode-n.png" var="portcodeN" />
<spring:url value="/resources/images/bank-n.png" var="bankN" />
<link rel="stylesheet" href="//code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="//code.jquery.com/jquery-1.9.1.js"></script>
<script src="//code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<script type="text/javascript" src="${valid}"></script>
<c:if test="${successMsg != null}">
	<div class="successMsg" id="successMsg">
		<strong>${successMsg}</strong>
	</div>
</c:if>
<style>
.breadcrumb h3 {
    margin: 0px;
    display: inline-block;
    text-align: center;
    line-height: 15px;
}
.breadcrumb a{float:left;}
.mobilePaymentForm .form-control, .mobileReceiptForm .form-control:focus{
     border: 0;
     border-color: transparent;
     border-bottom: 1px solid #ccc;
     min-height: 38px;
     box-shadow:none;
     border-radius: 0;
     background-color: #fff;
}
.mobilePaymentForm span.input-group-addon{
     border: 0;
     border-bottom: 1px solid #ccc;
     background-color: #fff;
     box-shadow: none;
     border-radius: 0;
}
.table-responsive{border:0;}
</style>
<div class="breadcrumb text-center">
	<a href=""><i class="fa fa-arrow-left" aria-hidden="true"></i> Back</a><h3>Payment</h3>
</div>
<div class="col-md-12 mobilePaymentForm">
	<div class="fassetForm">
		<form:form action="payment" method="post" commandName="payment" onsubmit="return checkamount();">
			<!-- Small modal -->
			<div id="year-model" data-backdrop="static" data-keyboard="false"
				class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog"
				aria-labelledby="mySmallModalLabel" aria-hidden="true">
				<div class="modal-dialog ">
					<div class="modal-content">
						<div class="modal-header">
							<h4 class="modal-title" id="mySmallModalLabel">Select Accounting Year</h4>
						</div>
						<div class="modal-body">
						<c:set var="first_year" value="0" />	   	
							<c:forEach var="year" items="${yearList}">
								<c:choose>
									<c:when test="${first_year==0}">
								        <form:radiobutton path="accountYearId" value="${year.year_id}" checked="checked" onclick="setdatelimit('${year.start_date}','${year.end_date}')"/>${year.year_range} 
									</c:when>
									<c:otherwise>
								        <form:radiobutton path="accountYearId" value="${year.year_id}" onclick="setdatelimit('${year.start_date}','${year.end_date}')"/>${year.year_range} 
									</c:otherwise>
								</c:choose>
								<c:set var="first_year" value="${first_year+1}" />
							</c:forEach>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-primary waves-effect waves-light" onclick='saveyearid()'>Save Year</button>
						</div>
					</div>
					<!-- /.modal-content -->
				</div>
			</div>
			<!-- /.modal-dialog -->
			
			<div class="row" id="bill-tds" style="border: 1px; border-style: double; background: rgb(54, 94, 191); align-content:; color: white;">
				<div class="col-md-2">
					<label>Bill Amount : </label>
				</div>
				<div class="col-md-2">
					<label id="billAmount"></label>
				</div>
				<div class="col-md-2">
					<label>Outstanding Amount : </label>
				</div>
				<div class="col-md-2">
					<label id="outstandingAmount"></label>
				</div>
				<div class="col-md-2">
					<label>TDS : </label>
				</div>
				<div class="col-md-2">
					<label id="tds"></label>
				</div>
			</div>
			<div class="row" id="openingAmount"	style="border: 1px; border-style: double; background: rgb(54, 94, 191); align-content:; color: white;">
				<div class="col-md-2">
					<label>Outstanding Amount : </label>
				</div>
				<div class="col-md-2">
					<label id="outstandingAmountagainstopening"></label>
				</div>
			</div>	
			<div class="row">
				<form:input path="payment_id" hidden="hidden" />
				<form:input path="payDetails" id="payDetails" hidden="hidden" />
				<%-- <form:input path="tds_amount" id = "tds_amount" type="hidden" /> --%>	
			<form:input path="group" id="group" hidden="hidden" />
				<div class="col-md-12 col-xs-12">
					<c:if test='${payment.payment_id!=Null}'>
						<div class="form-group">
							<div class="input-group">
								<span class="input-group-addon"><img src='${notepadN}'	alt="Voucher" title="Voucher"></span> 
								<form:input path="voucher_no" class="logInput form-control" id="voucher_no" placeholder="Voucher no" readonly="true" />
							</div>
						</div>
					</c:if>
					<div class="form-group">
						<div class="input-group">
							<span class="input-group-addon"><img src='${billdateN}'	alt="Date" title="Date"></span> 
							<form:input path="dateString" type="text" class="form-control" style="color: black;"	id="paymentDate" placeholder="Date" autocomplete="off" onchange="dateRestriction()" onclick="setDatePicker()" />
							<span class="logError "><form:errors path="dateString" /></span>
						</div>
					</div>
					<div class="form-group" id="supplierList">
						<div class="input-group">
							<span class="input-group-addon"><img src='${usersN}' alt="Supplier" title="Supplier"></span> 
							<form:select path="supplierId" class="logInput form-control" id='supplierId' onChange="setProduct(this.value)">
								<form:option value="0" label="Select Supplier" />
								<c:forEach var="supplier" items="${supplierList}">
									<form:option value="${supplier.supplier_id}">${supplier.company_name} </form:option>
								</c:forEach>
								<form:option value="-1" label="Expense" />
							</form:select>
							<span class="logError"><form:errors path="supplierId" /></span>
						</div>
					</div>
					<div class="form-group" id="subledgerList">
						<div class="input-group">
							<span class="input-group-addon"><img src='${notepadN}' alt="Expense Type" title="Expense Type"></span> 
							<form:select path="subLedgerId" class="logInput form-control" id='subLedgerId'>
								<form:option value="0" label="Select Expense Type" />
								<c:forEach var="subLedger" items="${subLedgerList}">									
									<c:if test="${subLedger.ledger.accsubgroup.accountGroup.postingSide.posting_id != 2}">
										<form:option value="${subLedger.subledger_Id}">${subLedger.subledger_name}</form:option>
									</c:if>
								</c:forEach>
							</form:select>
							<span class="logError"><form:errors path="subLedgerId" /></span>
						</div>
					</div>
					<div class="form-group" id="billNoList">
						<div class="input-group">
							<span class="input-group-addon"><img src='${shippingbillN}' alt=" Bill No" title=" Bill No"></span> 
							<form:select path="purchaseEntryId" class="logInput form-control" id="purchaseEntryId">
								<option value="0">Select Supplier Bill No</option>
							</form:select>
							<span class="logError"><form:errors path="purchaseEntryId" /></span>
						</div>
					</div>
					<div class="form-group">
						<div class="input-group">
							<span class="input-group-addon"><img src='${notepadN}' alt="Expense Type" title="Expense Type"></span> 
							<form:select path="payment_type" class="logInput form-control" id='payment_type'>
								<option value="0">Select Payment Type</option>
								<form:option value="<%=MyAbstractController.PAYMENT_TYPE_CASH %>">Cash</form:option>
								<form:option value="<%=MyAbstractController.PAYMENT_TYPE_CHEQUE %>">Cheque</form:option>
								<form:option value="<%=MyAbstractController.PAYMENT_TYPE_DD %>">DD</form:option>
								<form:option value="<%=MyAbstractController.PAYMENT_TYPE_NEFT %>">NEFT/RTGS/Net banking/Account Transfer</form:option>
							</form:select>
							<span class="logError"><form:errors path="payment_type" /></span>
						</div>
					</div>
					<div class="form-group" id="chequeDDNo">
						<div class="input-group">
							<span class="input-group-addon"><img src='${shippingbillN}' alt=" Bill No" title=" Bill No"></span> 
							<form:input path="cheque_dd_no" class="logInput form-control" id="cheque_dd_no" placeholder="Cheque No./IB No." />
							<span class="logError"><form:errors path="cheque_dd_no" /></span>
						</div>
					</div>
					<div class="form-group" id="chequeDate">
						<div class="input-group">
							<span class="input-group-addon"><img src='${billdateN}'	alt="Cheque / DD Date / Transaction Date" title="Cheque / DD Date / Transaction Date"></span> 
							<form:input path="chequeDateString"  class="form-control" type="text"	style="color: black;" id="cheque_date" placeholder="Cheque Date" autocomplete="off"/>
							<span class="logError"><form:errors	path="chequeDateString" /></span>
						</div>
					</div>
					<div class="form-group">
						<div class="input-group">
							<span class="input-group-addon"><img src='${rupeeIndN}'	alt="Amount" title="Amount"></span> 
							<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" value="${payment.amount}" var="amtvalue"/>
							<form:input path="amount" class="logInput form-control" id="amount"	placeholder="Amount Paid" value="${amtvalue}" 	maxlength="18"/>
							<span class="logError"><form:errors path="amount" /></span>
						</div>
					</div>
					<div class="form-group">
						<div class="input-group">
							<span class="input-group-addon"><img src='${taxN}'	alt="TDS" title="TDS"></span> 
							<form:select path="tds_paid" class="logInput form-control" onChange="showTDSAmount(this.value)">
								<%-- <form:option value="0" label="--Select--" /> --%>							
								<form:option value="false">No</form:option> 	
								<form:option value="true">Yes</form:option>
							</form:select>		
							<span class="logError"><form:errors path="tds_paid" /></span>
						</div>
					</div>
					<div class="form-group" id="tds-amount">
						<div class="input-group">
							<span class="input-group-addon"><img src='${rupeeIndN}'	alt="tds_amount" title="tds_amount"></span> 
							<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" value="${payment.tds_amount}" var="tdsvalue"/>
							<form:input path="tds_amount" class="logInput form-control" id="cheque_dd_no" placeholder="TDS Amount" value="${tdsvalue}"/>
							<span class="logError"><form:errors path="tds_amount" /></span>
						</div>
					</div>
					<div class="form-group" id="bank-comp">
						<div class="input-group">
							<span class="input-group-addon"><img src='${bankN}'	alt="Bank" title="Bank"></span> 
							<form:select path="bankId" id="bank_Id" class="logInput form-control">
								<form:option value="0" label="Select Bank" />
								<c:forEach var="bank" items="${bankList}">
									<form:option value="${bank.bank_id}">${bank.bank_name} - ${bank.account_no}</form:option>
								</c:forEach>
							</form:select>
							<span class="logError"><form:errors path="bankId" /></span>
						</div>
					</div>
					<div class="form-group" id="tds-amount">
						<div class="input-group">
							<span class="input-group-addon"><img src='${rupeeIndN}'	alt="Advance Payment" title="Advance Payment"></span> 
							<form:select path="advance_payment" class="logInput form-control" onchange="setadvanceno(this.value)">
								<form:option value="0" label="Select"/>									
								<form:option value="false">No</form:option>
								<form:option value="true">Yes</form:option>
							</form:select>
							<span class="logError"><form:errors path="advance_payment" /></span>
						</div>
					</div>
					<div class="form-group">
						<div class="input-group">
							<span class="input-group-addon"><img src='${taxN}'	alt="GST" title="GST"></span> 
							<form:select path="gst_applied" class="logInput form-control" onChange="showDetails(this.value)">
								<form:option value="0" label="Select" />
								<form:option value="true">Yes</form:option>
								<form:option value="false">No</form:option>
							</form:select>
							<span class="logError"><form:errors path="gst_applied" /></span>
						</div>
					</div>
					<div class="form-group">
						<div class="input-group">
							<span class="input-group-addon"><img src='${pencilN}' alt="Remark" title="Note"></span>
							<form:textarea path="other_remark" class="logInput form-control"	id="other_remark" rows="5" placeholder="Remark" maxlength="1000"></form:textarea>
							<span class="logError"><form:errors path="other_remark" /></span>
						</div>
					</div>
				</div>
			</div>
			<div class='row'>
				<div class="table-responsive">
					<table id='table-value' style="display:none">
						<tr>
							<td colspan="3">
								<label>Transaction Amount</label> 
								<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" value="${payment.transaction_value_head}"
						 var="trvalue"/>
								<form:input path="transaction_value_head" class="logInput hlable" id="transaction_value" readonly="true" style="background-color: transparent;" value="${trvalue}"/>
							</td>
							<td colspan="2">
								<label>CGST</label> 
								<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" value="${payment.CGST_head}"
						 var="cgstvalue"/>
								<form:input path="CGST_head" class="logInput hlable" id="cgst" readonly="true" style="background-color: transparent;" value="${cgstvalue}"/>
							</td>
							<td colspan="2">
								<label>SGST</label> 
								<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" value="${payment.SGST_head}"
						 var="sgstvalue"/>
								<form:input path="SGST_head" class="logInput hlable" id="sgst" readonly="true" style="background-color: transparent;" value="${sgstvalue}"/>
							</td>
							<td colspan="2">
								<label>IGST</label>
								<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" value="${payment.IGST_head}"
						 var="igstvalue"/> 
								<form:input path="IGST_head" class="logInput hlable" id="igst" readonly="true" style="background-color: transparent;" value="${igstvalue}"/>
							</td>
							<td colspan="2">
								<label>CESS</label> 
								<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" value="${payment.SCT_head}"
						 var="sctvalue"/>
								<form:input path="SCT_head" class="logInput hlable" id="state_compansation_tax" readonly="true" style="background-color: transparent;" value="${sctvalue}"/>
							</td>
						</tr>
					</table>
				</div>
				<div class="table-responsive">
					<table class="table table-bordered table-striped" id="detailTable"
						style='display: none'>
						<thead>
							<tr>
								<c:if test="${payment.payment_id!=null}">
									<th></th>
							    </c:if>
								<th>Product</th>
								<th>Quantity</th>
								<th>UOM</th>
								<th>HSN/SAC Code</th>
								<th>Rate</th>
								<th>Discount(%)</th>
								<th>Labour Charge</th>
								<th>Freight Charges</th>
								<th>Other</th>
								<th>CGST</th>
								<th>SGST</th>
								<th>IGST</th>
								<th>CESS</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
		</div>
			
			<div id="dtl-btn">
				<button type="button" class="fassetBtn waves-effect waves-light" onclick="showdiv()">Add Details</button>
			</div>
			<div id="details-data" class='row' style="display: none">
				<div class="table-responsive">
					<table class="table table-bordered table-striped">
						<thead>
							<tr>
								<th>Product</th>
								<th>Quantity</th>
								<th>UOM</th>
								<th>HSN/SAC Code</th>
								<th>Rate</th>
								<th>Discount(%)</th>
								<th>Labour Charge</th>
								<th>Freight Charges</th>
								<th>Other</th>
								<th>CGST</th>
								<th>SGST</th>
								<th>IGST</th>
								<th>CESS</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>
									<select class="logInput" id="product_id" onChange="setDetails(this.value)">
										<option value="0">Select Product Name</option>
									</select>
								</td>
								<td>
									<input id="transaction_amount" type="hidden" /> 
									<input class="logInput" id="cgst_hidden" placeholder="cgst_hidden" type="hidden" />
									<input class="logInput" id="sgst_hidden" placeholder="sgst_hidden" type="hidden" /> 
									<input class="logInput" id="igst_hidden" placeholder="igst_hidden" type="hidden" /> 
									<input class="logInput" id="sct_hidden" placeholder="sct_hidden" type="hidden" /> 
									<input class="logInput" id="quantity" placeholder="Quantity" onchange="calculaterate(this.value,1)" onkeypress="return isNumber(event)" maxlength="10"/> 
									<input class="logInput" type='hidden' id="product-name" placeholder="product_name" />
								</td>
								<td><input class="logInput" id="UOM" placeholder="UOM" readonly/></td>
								<td><input class="logInput" id="HSNCode" placeholder="HSNCode" onchange="getHsnDetails(this.value)" readonly maxlength="10"/></td>
								<td><input class="logInput" id="rate" placeholder="rate" onchange="calculaterate(this.value,2)" onkeypress="return isNumber(event)" maxlength="12"/></td>
								<td><input class="logInput" id="discount" placeholder="Discount" onchange="calculaterate(this.value,3)" onkeypress="return isNumber(event)" maxlength="6"/></td>
								<td><input class="logInput" id="labour_charges" placeholder="Labour charges" onchange="calculaterate(this.value,4)" onkeypress="return isNumber(event)" maxlength="10"/></td>
								<td><input class="logInput" id="freight" placeholder="Freight charges" onchange="calculaterate(this.value,5)" onkeypress="return isNumber(event)" maxlength="10"/></td>
								<td><input class="logInput" id="others" placeholder="Other" onchange="calculaterate(this.value,6)" onkeypress="return isNumber(event)" maxlength="10"/></td>
								<td><input id="CGST" class="logInput" placeholder="CGST" onkeypress="return isNumber(event)" maxlength="20"/> </td>
								<td><input id="SGST" class="logInput" placeholder="SGST" onkeypress="return isNumber(event)" maxlength="20"/> </td>
								<td><input id="IGST" class="logInput" placeholder="IGST" onkeypress="return isNumber(event)" maxlength="20"/> </td>
								<td><input id="SCT" class="logInput" placeholder="CESS" onkeypress="return isNumber(event)"  maxlength="20"/> </td>
							</tr>
	
						</tbody>
					</table>
				</div>
				<div class="text-center">
					<button type="button" class="fassetBtn waves-effect waves-light"
						onclick="saveDetails()">Save Product Details</button>
				</div>
			</div>
			<div class="row" style="text-align: center; margin: 15px;">
				<button class="fassetBtn" type="submit" onclick="return save()">
					<spring:message code="btn_save" />
				</button>
				<button class="fassetBtn" type="button" onclick="cancel()">
					<spring:message code="btn_cancel" />
				</button>
			</div>
		</form:form>
	</div>
</div>
<script>

var datefield1=document.getElementById("paymentDate").value;


var paymentDetails = [];
var transactionAmount = 0;
var startdate="";
var enddate="";
var totalAmount = 0;
var totalCgst = 0;
var totalIgst = 0;
var totalSgst = 0;
var totalSct = 0;
var supStateId;
var supgst;
var compStateId;
var rcm;
var tdsRate = 0;
var tdsapply;
var paidtds=0;
var closingbalanace=0;
var transaction_id = '<c:out value= "${payment.payment_id}"/>';	
var supplierId = 0;
$("#cheque_dd_no,#amount,#tds_amount,#HSNCode,#quantity,#rate,#discount,#labour_charges,#freight,#others,#CGST,#SGST,#IGST,#SCT").keypress(function(e) {
	if (!digitsAndDotOnly(e)) {
		return false;
	}
});
$(document).ready(function() {	
	$("#bill-tds").hide();
	$("#openingAmount").hide();

	
	$("#gst_applied").val("false");
	$("#gst_applied").css('pointer-events', 'none');

	setTimeout(function() {
        $("#successMsg").hide()
    }, 3000);
	//$("#tds-amount").hide();
	compStateId = '<c:out value = "${stateId}"/>';	
	var payType = '<c:out value = "${payment.payment_type}"/>';
	paidtds='<c:out value = "${paidtds}"/>';
	closingbalanace='<c:out value="${total}"/>';
	var tdspaid=$("#tds_paid").val();
	if(tdspaid=="true")
		{
		$("#tds-amount").show();
		}
	else
		{
		$("#tds-amount").hide();
		}
	if(payType == ""){
		$("#chequeDDNo").hide();
		$("#chequeDate").hide();
		$("#bank-comp").hide();
	}
	else if(payType == 1){
		$("#chequeDDNo").hide();
		$("#chequeDate").hide();
		$("#bank-comp").hide();
	}
	else if(payType != 1){
		$("#chequeDate").show();
		$("#chequeDDNo").show();
		$("#bank-comp").show();	
	}
	
	$("#subledgerList").hide();
	$("#billNoList").hide();	
	
	var datefield=document.getElementById("paymentDate");
	if(datefield.type!="date"){ //if browser doesn't support input type="date", initialize date picker widget:
	    jQuery(function($){ //on document.ready
	        $('#cheque_date').datepicker({dateFormat: 'yy-mm-dd'});
	    })
	}
	
	var paymentType = '<c:out value = "${payment.payment_type}"/>';
	if(paymentType > 0){
		$("#payment_type").val(paymentType);
	}	
	if(${payment.payment_id!=null}){
		if(${payment.supplier==null}){ 
			supplierId =-1;
		}
		else {
			supplierId = '<c:out value = "${payment.supplier.supplier_id}"/>';
		}
	}
	$("#supplierId").val(supplierId);
	setProduct(supplierId);
	if(supplierId!=""){
		var bill_no='<c:out value= "${payment.supplier_bill_no.purchase_id}"/>';
		
		if(bill_no != ""){
			$("#purchaseEntryId").val(<c:out value = "${payment.supplier_bill_no.purchase_id}"/>);
			$("#gst_applied").css('pointer-events', 'none');
			$("#gst_applied").val("false");
		}
		else{
		$("#purchaseEntryId").val(-1);
		$("#tds-amount").show();
		 $("#tds_paid").removeAttr('disabled');

		$("#subLedgerId").val("${payment.subLedger.subledger_Id}");
		}
	}
	else
		{
		$("#tds_paid").val("false");
		// $("#tds_paid").prop('disabled', 'disabled');
		}
	var gst_applied =  $("#gst_applied").val(); 	
	if(gst_applied=="true"){
		document.getElementById("dtl-btn").style.display="block";
		document.getElementById("detailTable").style.display="table";
		if(transaction_id!=""){
			document.getElementById("table-value").style.display="block";
		}
		
	}
	else{
		document.getElementById("dtl-btn").style.display="none";
		document.getElementById("table-value").style.display="none";
	}
	
	var years = '<c:out value= "${yearList}"/>';	
	var transaction_id = '<c:out value= "${payment.payment_id}"/>';	
	var ysize = '<c:out value= "${yearList.size()}"/>';	

	if((years!="")&&(transaction_id=="")){
		if(ysize!=1){
	 		$('#year-model').modal({
		    	show: true,
		   	});	 
	 		var j=0;
	 		<c:forEach var = "year" items = "${yearList}">
		    if(j==0)
		    	{				    
		    	 startdate1='${year.start_date}';
		    	    enddate1='${year.end_date}';
		    	setdatelimit('${year.start_date}','${year.end_date}');
		    	}
		    j++;
		     </c:forEach>
		}
		else
		{
			<c:forEach var = "year" items = "${yearList}">				
			 startdate1='${year.start_date}';
	    	    enddate1='${year.end_date}';
			   setdatelimit('${year.start_date}','${year.end_date}');
			</c:forEach>
		}
	}
	else
		{
		var savedyear='<c:out value= "${payment.accountingYear.year_id}"/>';		
		
		<c:forEach var = "year" items = "${yearList}">			
			 if("${year.year_id}"==savedyear)
				{
				 startdate1='${year.start_date}';
		    	    enddate1='${year.end_date}';
			   setdatelimit('${year.start_date}','${year.end_date}');
			   var supdate="${payment.date}";				
			   supdate=formatDate(supdate);
			   $("#paymentDate").val(supdate);
			   } 
		</c:forEach> 
		}
	<c:if test="${paymentDetailsList != null}">
		<c:forEach var = "paymentDetails" items = "${paymentDetailsList}">
			addRow(${paymentDetails.id},
				${paymentDetails.product_id.product_id},
				"${paymentDetails.product_id.product_name}",
				${paymentDetails.quantity},
				"${paymentDetails.product_id.hsn_san_no}",
				${paymentDetails.rate},
				${paymentDetails.discount},
				${paymentDetails.uom_id.uom_id},
				"${paymentDetails.uom_id.unit}",
				${paymentDetails.cgst},
				${paymentDetails.igst},
				${paymentDetails.sgst},
				${paymentDetails.state_com_tax},
				${paymentDetails.labour_charges},
				${paymentDetails.frieght},
				${paymentDetails.others},
				${paymentDetails.transaction_amount});
		</c:forEach>
	</c:if>
	
	
	/* $("#first_name,#middle_name,#last_name,#company_name").keypress(function(e) {
		if (!letters(e)) {
			return false;
		}
	});
	
	$("#mobile_no,#landline_no,#adhaar_no,#pincode").keypress(function(e) {
		if (!digitsOnly(e)) {
			return false;
		}
	}); */
	
	/* $("#current_address,#permenant_address").keypress(function(e) {
		if (!lettersAndDigitsAndSlashOnly(e)) {
			return false;
		}
	});	
	 */
	
	$("#payment_type").on("change", function(){
		var payType = $("#payment_type").val();
		if(payType == <%=MyAbstractController.PAYMENT_TYPE_CASH%>){
			$("#chequeDDNo").hide();
			$("#chequeDate").hide();
			$("#bank-comp").hide();
		}
		else{
			$("#bank-comp").show();
			$("#chequeDDNo").show();
			$("#chequeDate").show();
		}		
	});
			
	$("#purchaseEntryId").on("change", function(){
		var bill=$("#purchaseEntryId").val();
	
		
		var supplierId = $("#supplierId").val();
		
		
		$("#gst_applied").css('pointer-events', 'none');
		$("#gst_applied").val("false");
		
		if(bill=="-1"){
			$("#advance_payment").css('pointer-events', '');
			$("#gst_applied").css('pointer-events', '');
			$("#tds_paid").css('pointer-events', '');
			$("#openingAmount").hide();
		
		
			$("#bill-tds").hide();
			/* $("#advance_payment").removeAttr('disabled'); */
			/* $("#gst_applied").removeAttr('disabled'); */	
			/*  $("#tds_paid").removeAttr('disabled'); */
			$("#advance_payment").css('pointer-events', 'none');
			$("#advance_payment").val("true");
			$("#gst_applied").css('pointer-events', 'none');
			$("#gst_applied").val("false");
			$("#tds").show();
			$("#tds_paid").val("false");
			 $("#bill-tds").hide();
		}
		else if(bill > 0){
			$("#advance_payment").css('pointer-events', '');
			$("#gst_applied").css('pointer-events', '');
			$("#tds_paid").css('pointer-events', '');
			$("#openingAmount").hide();
			$("#bill-tds").show();
			$("#gst_applied").val("false");
			$("#gst_applied").css('pointer-events', 'none');
			$("#advance_payment").val("false");
			$("#advance_payment").css('pointer-events', 'none');
			$("#tds_paid").val("false");
			$("#tds_paid").css('pointer-events', 'none');
			 $("#tds-amount").hide();
				<c:forEach var = "purchaseEntry" items = "${purchaseEntryList}">
				var purId = <c:out value = "${purchaseEntry.purchase_id}"/>;
				
				if(purId == bill){
					var amount = '<c:out value = "${purchaseEntry.round_off}"/>';
					alert('aomunt is'+amount);
					var tdsAmount = '<c:out value = "${purchaseEntry.tds_amount}"/>';
					alert('tdsAmount is'+tdsAmount);

					
					<c:set var="total" value="${0}"/>
					<c:forEach var = "payment" items = "${purchaseEntry.payments}">
						<c:set var="total" value="${total + payment.amount}" />
					</c:forEach>
					
					<c:forEach var = "note" items = "${purchaseEntry.debitNote}">
					<c:set var="total" value="${total + note.round_off}" />
				</c:forEach>	
				
				
				
					var total = <c:out value = "${total}"/>;
				
					var outstanding = 0;
					/* if(tdsapply==1)
					var tdsAmount = parseFloat((amount*tdsRate)/100).toFixed(2);
					else
						var tdsAmount=0; */
					if(total > 0){
						outstanding = parseFloat(amount - total).toFixed(2);
					}
					else{
						outstanding = parseFloat(amount).toFixed(2);
					}
					$("#billAmount").html(parseFloat(parseFloat(amount)+parseFloat(tdsAmount)).toFixed(2));
					$("#outstandingAmount").html(outstanding);
				
					
					
					$.ajax({
				        type: 'POST',
				        url: 'getPaidTDSPayment?billid='+purId,
				        async:false,
			            contentType: 'application/json',
					      	success: function (data){ 
					      		
					      		paidtds=data;
					      		alert("data is :"+paidtds);
					      	},
					        error: function (e) {
					            console.log("ERROR: ", e);
					        },				              
				    });
					
					var totaltds=parseFloat(tdsAmount-paidtds).toFixed(2);					
					$("#tds").html(parseFloat(tdsAmount).toFixed(2));
					$("#tds_amount").val(totaltds);
				}
			</c:forEach>
		}
		
		/*  For the opening balance   */
		else if(bill == "-2")
		{
			var enterDate= $('#paymentDate').val();
			var closing_value=$('#total').val();
			
			$("#advance_payment").css('pointer-events', '');
			$("#gst_applied").css('pointer-events', '');
			$("#tds_paid").css('pointer-events', '');
			$("#bill-tds").hide();
		
			$("#gst_applied").val("false");
			$("#gst_applied").css('pointer-events', 'none');
			$("#advance_payment").val("false");
			$("#advance_payment").css('pointer-events', 'none');
			$("#tds_paid").val("false");
			$("#tds_paid").css('pointer-events', 'none');
			 $("#tds-amount").hide();
				$("#openingAmount").show();
					
					/* first ajax call for total value */
				 var prevamount='<c:out value = "${payment.amount}"/>';
					amount=parseFloat(amount)-parseFloat(prevamount);
				
				$.ajax({
			        type: 'GET',
			        url: 'getOpeningBalanceforpayment?supplierid='+supplierId,
			        async:false,
		            contentType: 'application/json',
				      	success: function (data){ 
				      		
				      		closingbalanace=data;
				      	
				      	},
				        error: function (e) {
				            console.log("ERROR: ", e);
				        },				              
			    });
			
				
				var bal=parseFloat(closingbalanace).toFixed(2);			
				
				
				
				$("#outstandingAmountagainstopening").html(parseFloat(bal).toFixed(2)).val();
				
				$("#outstandingAmountagainstopening").html(bal);
		
		
		}
		
	});

				
});



					function setDatePicker()
					{
						
						$("#paymentDate").datepicker({dateFormat: 'dd-mm-yy'});
						 $('#paymentDate').datepicker("option", { maxDate: new Date() });
						$('#paymentDate').value = '';
						$('#paymentDate').focus(); // ui-datepicker-div
						 event.preventDefault();
						
					} 


					function dateRestriction() {
						 
						
						 var datefield=document.getElementById("paymentDate").value;
						
						 var res = datefield.split("-");
						 var dd=parseInt(res[0]);
						 var mm=parseInt(res[1]);
						 var yy=parseInt(res[2]);
						 
						    var nd = res[1]+"/"+res[0]+"/"+res[2];
						    var ud = new Date(nd);
							var td = new Date();
							
							ud.setHours(0,0,0,0);
							
							td.setHours(0,0,0,0);
						var flag = 	 checkDate(dd,mm,yy);
						
						if(flag == false){
							alert("Invalid date");
							var id = document.getElementById("paymentDate");

							id.value = '';
							id.focus(); // ui-datepicker-div
							event.preventDefault();	
						}
						else if(ud > td ){
								
								alert("Cannot create voucher for future date");
								var id = document.getElementById("paymentDate");

								id.value = '';
								id.focus(); // ui-datepicker-div
								event.preventDefault();		
								
							}
						
						
					}

					function checkDate(dd,mm,yy)
					{
						if(mm>0 && mm<13)
						{
							if(mm == 1 || mm == 3 || mm == 5 || mm == 7 || mm == 8 || mm == 10 || mm == 12 && (dd>0 && dd <32))
							{	
								return true;
							}
							else if(mm == 4 || mm == 6 || mm == 9 || mm == 11 && (dd>0 && dd <31))
							{	
								return true;
							}
							else if(mm == 2)
							{
								var flag = yy%4 == 0 & yy%100 != 0 || yy %400 == 0; 
								if(flag == true && (dd>0 && dd <30))
								{
								  	return true;
								}
								else if(flag == false && (dd>0 && dd < 29))
								{
								  	return true;
								}
								else
								{
									return false;
								}
							}
						}
						else
						{
							return false;
						}
						
					}

				function showDetails(e){
					
					if(e == "true"){
						document.getElementById("details-data").style.display="block";
						if(transaction_id!="")
						{
						document.getElementById("table-value").style.display="block";	
						}
					}
					else if(e == "false"){
						document.getElementById("details-data").style.display="none";
						document.getElementById("table-value").style.display="none";		
				
					}
				}

function setDetails(e){
	<c:forEach var = "product" items = "${productList}">
		var id = <c:out value = "${product.product_id}"/>
		if(id == e){
	  		$("#UOM").val("${product.uom.unit}"); 
		  	$("#product-name").val("${product.product_name}");
		  	$('#UOM').attr('data-uomId', '${product.uom.uom_id}');
			$("#quantity").val("0");
	  		$("#rate").val("0");
		  	$("#labour_charges").val("0");
		  	$("#discount").val("0");
		  	$("#freight").val("0");
		  	$("#others").val("0"); 	
		  	$("#HSNCode").val("${product.gst_id.hsc_sac_code}"); 
	  		if("${product.gst_id.hsc_sac_code}"=="") {
  		 		$('#HSNCode').attr('readonly', false); 
			}
		  	else{
  		 		$('#HSNCode').attr('readonly', true); 
	  		}	  		
	  		var currentdate=$("#paymentDate").val();
			if(currentdate=="")
			{
			alert("Select Bill Date");
			$("#product_id").val(0);
			return false;
			}	
			else
			{
				currentdate = currentdate.split("-");			
				currentdate=currentdate[2]+"-"+currentdate[1]+"-"+currentdate[0];
				var hsn='${product.gst_id.hsc_sac_code}';
				var gstOrVat='${product.tax_type}';
				
				
				if(hsn==""||hsn=="0")
		  		{
					if(gstOrVat!="2")
			  		{
					alert("Please Enter HSN First For Non-Inventory Product");
					$('#quantity').attr('readonly', true); 
					$('#rate').attr('readonly', true); 
					$('#discount').attr('readonly', true); 
					$('#labour_charges').attr('readonly', true); 
					$('#freight').attr('readonly', true); 
					$('#others').attr('readonly', true); 
			  		}
					else
						{
						$('#quantity').attr('readonly', false); 
						$('#rate').attr('readonly', false); 
						$('#discount').attr('readonly', false); 
						$('#labour_charges').attr('readonly', false); 
						$('#freight').attr('readonly', false); 
						$('#others').attr('readonly', false); 
						}
		  		}
				else
				{
				$('#quantity').attr('readonly', false); 
				$('#rate').attr('readonly', false); 
				$('#discount').attr('readonly', false); 
				$('#labour_charges').attr('readonly', false); 
				$('#freight').attr('readonly', false); 
				$('#others').attr('readonly', false); 
				}
				
				$.ajax({
	           		type: "GET",
	            		url: '<c:url value="getHSNSACByDate?date="/>'+currentdate+'&hsn='+hsn,                     		
	            		cache: false, 
	            		aysnc:false,
	            		success: function (data) {
			            			cgst=data["cgst"];
			            			sgst=data["sgst"];
			            			igst=data["igst"];
			            			cess=data["state_comp_cess"];   
			            			$("#sct_hidden").val(cess); 
			            		  	$("#SCT").val(cess);	
			            		  	if(supgst=="true"){  
			            		  		if(rcm=="No"){
			            					if(supStateId != compStateId){
			            				  		$("#CGST").val(0);  
			            					  	$("#cgst_hidden").val(0);  
			            			
			            					  	$("#SGST").val(0);  
			            					  	$("#sgst_hidden").val(0);  
			            			
			            					  	$("#IGST").val(igst);  
			            					  	$("#igst_hidden").val(igst);  
			            					}
			            					else{
			            					  	$("#CGST").val(cgst);  
			            					  	$("#cgst_hidden").val(cgst);  
			            			
			            					  	$("#SGST").val(sgst);  
			            					  	$("#sgst_hidden").val(sgst);  
			            			
			            					  	$("#IGST").val(0);  
			            					  	$("#igst_hidden").val(0);  			
			            					}
			            		 		}
			            		  		else{
			            			  		$("#CGST").val(0);  
			            				  	$("#cgst_hidden").val(0);  	
			            				  	$("#SGST").val(0);  
			            				  	$("#sgst_hidden").val(0); 
			            				  	$("#IGST").val(0);  
			            				  	$("#igst_hidden").val(0);  		 
			            		  		}
			            	  		}
			            	  		else{
			            		  		$("#CGST").val(0);  
			            			  	$("#cgst_hidden").val(0);  	
			            			  	$("#SGST").val(0);  
			            			  	$("#sgst_hidden").val(0); 
			            			  	$("#IGST").val(0);  
			            			  	$("#igst_hidden").val(0);  
			            		  }
			            		  	
	            		},
		            	error: function (e) {
		            		//alert(e);
		                	alert("Error while retrieving HSN/SAC");
		            	}
		        	});	
				}
		  	
	}
	</c:forEach>
}

$("#HSNCode").autocomplete({
	
    source: function (request, response) {
    	
    	if($("#HSNCode").val().length>=4)
    	{
    		var inventorydate=$("#paymentDate").val();
    		inventorydate = inventorydate.split("-");			
    		inventorydate=inventorydate[2]+"-"+inventorydate[1]+"-"+inventorydate[0];
    		
			$.ajax({
           		type: "GET",
            		url: '<c:url value="getHSNSACNoForNonInventory?term="/>'+request.term+'&date='+inventorydate,                     		
            		cache: false,                     		
            		success: function (data) {
            			
            	
            			response($.map(data, function(item){	  
            				            				
                  			return{
                  				label:"HSN/SAC-No:"+item.hsc_sac_code+" Chapter-No:"+item.chapterNo+" Schedule-Name:"+item.scheduleName+" GST-Rate:"+item.igst+"%",
                  				value:item.hsc_sac_code,
                  				cgst:item.cgst,
                 				igst:item.igst,
                 				sgst:item.sgst,
                 				scc:item.state_comp_cess,
                 				taxId:item.tax_id
                  			}
                  		
        	            		
            			}));                       		
        		},
            	error: function (e) {
            		//alert(e);
                	alert("Error while retrieving HSN/SAC number list(Connection failed).");
            	}
        	});	             	
    	}
	},
	select: function (event, ui) {
	
  	},
	minLength: 2 
}); 

function getHsnDetails(hsnCode){
	var cgst=0;
	var sgst=0;
	var igst=0;
	var cess=0;
	var currentdate=$("#paymentDate").val();
	var hsn=hsnCode;
	if(hsn!="" && hsn!="0")
		{
		$('#quantity').attr('readonly', false); 
		$('#rate').attr('readonly', false); 
		$('#discount').attr('readonly', false); 
		$('#labour_charges').attr('readonly', false); 
		$('#freight').attr('readonly', false); 
		$('#others').attr('readonly', false); 
		}
	
	if(currentdate=="")
	{
	alert("Select Bill Date");
	$("#product_id").val(0);
	return false;
	}	
	else
	{
	currentdate = currentdate.split("-");			
	currentdate=currentdate[2]+"-"+currentdate[1]+"-"+currentdate[0];
	$.ajax({
   		type: "GET",
    		url: '<c:url value="getHSNSACByDate?date="/>'+currentdate+'&hsn='+hsn,                     		
    		cache: false, 
    		aysnc:false,
    		success: function (data) {
            			cgst=data["cgst"];
            			sgst=data["sgst"];
            			igst=data["igst"];
            			cess=data["state_comp_cess"];   
            			$("#sct_hidden").val(cess); 
            		  	$("#SCT").val(cess);	
            		  	if(supgst=="true"){  
            		  		if(rcm=="No"){
            					if(supStateId != compStateId){
            				  		$("#CGST").val(0);  
            					  	$("#cgst_hidden").val(0);  
            			
            					  	$("#SGST").val(0);  
            					  	$("#sgst_hidden").val(0);  
            			
            					  	$("#IGST").val(igst);  
            					  	$("#igst_hidden").val(igst);  
            					}
            					else{
            					  	$("#CGST").val(cgst);  
            					  	$("#cgst_hidden").val(cgst);  
            			
            					  	$("#SGST").val(sgst);  
            					  	$("#sgst_hidden").val(sgst);  
            			
            					  	$("#IGST").val(0);  
            					  	$("#igst_hidden").val(0);  			
            					}
            		 		}
            		  		else{
            			  		$("#CGST").val(0);  
            				  	$("#cgst_hidden").val(0);  	
            				  	$("#SGST").val(0);  
            				  	$("#sgst_hidden").val(0); 
            				  	$("#IGST").val(0);  
            				  	$("#igst_hidden").val(0);  		 
            		  		}
            	  		}
            	  		else{
            		  		$("#CGST").val(0);  
            			  	$("#cgst_hidden").val(0);  	
            			  	$("#SGST").val(0);  
            			  	$("#sgst_hidden").val(0); 
            			  	$("#IGST").val(0);  
            			  	$("#igst_hidden").val(0);  
            		  }
            		  	
    		},
        	error: function (e) {
        		//alert(e);
            	alert("Error while retrieving HSN/SAC");
        	}
    	});
      }
}

function saveDetails(){
	
	
	var paytype=$("#payment_type").val();
	var check_no=$("#cheque_dd_no").val();
	var check_date=$("#cheque_date").val();
	var bank_id=$("#bank_Id").val();
	
	var productId = $("#product_id").val();
	var productName = $("#product-name").val();
	var quantity = $("#quantity").val();
	var rate = $("#rate").val();
	var discount =$("#discount").val();
	var uomUnit =  $("#UOM").val(); 
	var uomId = $('#UOM').attr('data-uomId');
	var hsn = $("#HSNCode").val();  
	var cgst = $("#CGST").val();
	var igst = $("#IGST").val();
	var sgst = $("#SGST").val();
	var tamount = $("#transaction_amount").val();
	var state_com_tax = $("#SCT").val();
	var labour_charges = $("#labour_charges").val();
	var freight=document.getElementById("freight").value;
	var others = $("#others").val();
	
	transactionAmount = ($("#transaction_value").val() == "") ? 0 : $("#transaction_value").val();
	totalCgst = ( $("#cgst").val() == "" ) ? 0 : $("#cgst").val();
	totalSgst = ( $("#sgst").val() == "" ) ? 0 : $("#sgst").val();
	totalIgst = ( $("#igst").val() == "" ) ? 0 : $("#igst").val();
	totalSct = ( $("#state_compansation_tax").val() == "" ) ? 0 : $("#state_compansation_tax").val();

	

	 if(paytype!=1)
		{
		if(check_no=="" || check_no==0){
			alert("Select Cheque No/IB No");
			bValid="false";
		
		}else if(check_date==""){
			alert("Select Cheque Date");
			bValid="false";
		
		}
		else if(bank_id==0){
			alert("Select Bank");
			bValid="false";
		
		}
	  }
	 
	if(productId==0){
		alert("Please Select Product");
		return false;
	}
	else if(quantity.trim()=="" || quantity.trim()=="0"){
		alert("Please Enter Quantity");
		return false;
	}
	else if(hsn.trim()=="" || hsn.trim()=="0"){
		alert("Please Enter HSN");
		return false;
	}
	else if(rate.trim()=="" || rate.trim()=="0"){
		alert("Please Enter Rate");
		return false;
	}
	else if(discount.trim()==""){
		alert("Please Enter Discount");
		return false;
		
	}else if(discount >= 101){
		alert("Discount should less than 100");
		bValid="false";
		return false;
	}
	else if(((sgst!=0 && sgst!="") || (cgst!=0 && cgst!="")) && (igst!=0 && igst!=""))
	{
	alert("Either input CGST+SGST or IGST");
	return false;
	}
	if(cgst.trim()=="")
		document.getElementById("CGST").value="0";
	if(sgst.trim()=="")
		document.getElementById("SGST").value="0";
	if(igst.trim()=="")
		document.getElementById("IGST").value="0";
	else{
		if(!isDuplicate(productId)){
			transactionAmount = parseFloat(transactionAmount) + parseFloat(tamount);
			totalCgst = parseFloat(totalCgst) + parseFloat(cgst);
			totalSgst = parseFloat(totalSgst) + parseFloat(sgst);
			totalIgst = parseFloat(totalIgst) + parseFloat(igst);
			totalSct = parseFloat(totalSct) + parseFloat(state_com_tax);			
			totalAmount = parseFloat(transactionAmount) + parseFloat(totalCgst) + parseFloat(totalSgst) + parseFloat(totalIgst) + parseFloat(totalSct);
			
			$("#transaction_value").val(parseFloat(transactionAmount).toFixed(2));
			$("#cgst").val(parseFloat(totalCgst).toFixed(2));
			$("#sgst").val(parseFloat(totalSgst).toFixed(2));
			$("#igst").val(parseFloat(totalIgst).toFixed(2));
			$("#state_compansation_tax").val(parseFloat(totalSct).toFixed(2));
			$("#round_off").val(parseFloat(totalAmount).toFixed(2));		
			$("#amount").val(parseFloat(totalAmount).toFixed(2));
			addRow(0,productId,productName,quantity,hsn,rate,discount,uomId,uomUnit,cgst,igst,sgst,state_com_tax,labour_charges,freight,others,tamount);
		}
		else{
			alert("Product already added");
			return false;
		}
	}
	$("#product_id").val(0);
	$("#quantity").val("");
	$("#rate").val("");
	$("#discount").val("");
	$("#UOM").val(""); 
	//$('#UOM').attr('data-uomId');
	$("#HSNCode").val("");  
	$("#CGST").val("");
	$("#IGST").val("");
	$("#SGST").val("");
	$("#SCT").val("");
	$("#labour_charges").val("");
	$("#freight").val("");
	$("#others").val("");
}

function showdiv(){
	document.getElementById("details-data").style.display="block";
	if(transaction_id!="")
	{
	document.getElementById("table-value").style.display="block";		
	}

}

function deleteRow(e, prodId, id){
	if(id > 0){
		window.location.assign('<c:url value="deleteproductdetailforPayment"/>?id='+id);
	}
	else{
		$(e).closest('tr').remove();
		for(i = 0; i<paymentDetails.length;i++){
			if(paymentDetails[i].productId == prodId){
				transactionAmount = transactionAmount - paymentDetails[i].transactionAmount;
				totalCgst = totalCgst - paymentDetails[i].cgst;
				totalSgst = totalSgst - paymentDetails[i].sgst;
				totalIgst = totalIgst - paymentDetails[i].igst;
				totalSct = totalSct - paymentDetails[i].state_com_tax;			
				totalAmount = transactionAmount + totalCgst + totalSgst + totalIgst + totalSct;				
				$("#transaction_value").val(parseFloat(transactionAmount).toFixed(2));
				$("#cgst").val(totalCgst.toFixed(2));
				$("#sgst").val(totalSgst.toFixed(2));
				$("#igst").val(totalIgst.toFixed(2));
				$("#state_compansation_tax").val(totalSct.toFixed(2));
				$("#round_off").val(parseFloat(totalAmount).toFixed(2));		
				$("#amount").val(parseFloat(totalAmount).toFixed(2));			
				
				paymentDetails.splice(i,1);
			}
		}	
		console.log(JSON.stringify(paymentDetails));		
	}
}
function isDuplicate(prodId){
	for(i = 0; i<paymentDetails.length;i++){
		if(paymentDetails[i].productId == prodId){
			return true;
		}
	}
	return false;
}

function addRow(id,productId,productName,quantity,hsn_san_no,rate,discount,uomId,uomUnit,cgst,igst,sgst,state_com_tax,labour_charges,freight,others,tamount){
	paymentDetails.push({"id":id,
		"productId":productId,
		"quantity":quantity, 
		"rate":rate, 
		"discount":discount, 
		"uomId":uomId, 
		"hsn_san_no":hsn_san_no,
		"cgst":cgst, 
		"igst":igst, 
		"sgst":sgst, 
		"state_com_tax":state_com_tax, 
		"labour_charges":labour_charges, 
		"freight":freight, 
		"others":others,
		"transactionAmount":tamount});	
	$("#payDetails").val(JSON.stringify(paymentDetails));
	console.log($("#payDetails").val());
	var markup = "";
	if(id > 0){
		markup = "<tr><td><a href = '#' onclick = 'deleteRow(this,"+productId+","+id+")'><img src='${deleteImg}' style = 'width: 20px;'/></a>"+
	    		"<a href = '#' onclick = 'editproductdetail("+id+")'><img src='${editImg}' style = 'width: 20px;'/></a></td>";
	   }
	else{
		/* markup = "<tr><td><a href = '#' onclick = 'deleteRow(this,"+productId+","+id+")'><img src='${deleteImg}' style = 'width: 20px;'/></a></td>"; */
		   
		   
		   markup = "<tr>"; 
			 <c:if test="${payment.payment_id!=null}">
			markup = markup + "<td>" + "</td>"; 
		    </c:if>  
    }
	
	markup = markup + "<td>"+productName+"</td>"+
		"<td>"+quantity+"</td>"+
		"<td>"+uomUnit+"</td>"+
		"<td>"+hsn_san_no+"</td>"+ 
		"<td>"+rate+"</td>"+
		"<td>"+discount+"</td>"+
		"<td>"+labour_charges+"</td>"+
		"<td>"+freight+"</td>"+
		"<td>"+others+"</td>"+
		"<td>"+cgst+"</td>"+
		"<td>"+sgst+"</td>"+
		"<td>"+igst+"</td>"+
		"<td>"+state_com_tax+"</td></tr>";

	$('#detailTable tr:last').after(markup);
	document.getElementById("detailTable").style.display="table";
}

function calculaterate(e,flag){
	var cgst_hidden=document.getElementById("cgst_hidden").value;
	var igst_hidden=document.getElementById("igst_hidden").value;
	var sgst_hidden=document.getElementById("sgst_hidden").value;
	var sct_hidden=document.getElementById("sct_hidden").value;
	var quantity=document.getElementById("quantity").value;
	var rate=document.getElementById("rate").value;
	var labour_charges=document.getElementById("labour_charges").value;
	var discount=document.getElementById("discount").value;
	var freight=document.getElementById("freight").value;
	var others=document.getElementById("others").value;
	
	if(rate!="")
 		rate=parseFloat(rate);
	if(quantity!="")
 		quantity=parseFloat(quantity);
	if(labour_charges!="")
		labour_charges=parseFloat(labour_charges);
	if(discount!="")
		discount=parseFloat(discount);
	if(freight!="")
		freight=parseFloat(freight);
	if(others!="")
		others=parseFloat(others);
	
	var amount = (rate*quantity)+labour_charges+freight+others;
 	var damount=rate*quantity;
	if(discount!=""){
		var disamount=parseFloat(damount)-parseFloat((damount*discount)/100);
	 	amount=parseFloat(disamount)+labour_charges+freight+others;
	}
	
	document.getElementById("transaction_amount").value=amount;	 
	
	$("#CGST").val(parseFloat((amount*cgst_hidden)/100).toFixed(2));
	
	$("#SGST").val(parseFloat((amount*sgst_hidden)/100).toFixed(2));
	
	$("#IGST").val(parseFloat((amount*igst_hidden)/100).toFixed(2));
	
	$("#SCT").val(parseFloat((amount*sct_hidden)/100).toFixed(2));	

}

function setProduct(supId){
	var productArray = [];
	
	if(supId == -1){
		$("#advance_payment").css('pointer-events', '');
		$("#gst_applied").css('pointer-events', '');
		$("#tds_paid").css('pointer-events', '');
		
		$("#gst_applied").val("false");
		$("#gst_applied").css('pointer-events', 'none');
		$("#advance_payment").val("false");
		$("#advance_payment").css('pointer-events', 'none');
		$("#tds_paid").val("false");
		$("#tds_paid").css('pointer-events', 'none');
		$("#subledgerList").show();
		$("#billNoList").hide();
		$("#purchaseEntryId").val("0");
	 	/* $("#gst_applied ").removeAttr('disabled');
	 	$("#tds_paid").prop('disabled', 'disabled');
		 $("#tds_paid").val("false"); */
		 $("#tds-amount").hide();
		$("#bill-tds").hide();
		
	}
	else if(supId > 0){
		$("#advance_payment").css('pointer-events', '');
		$("#gst_applied").css('pointer-events', '');
		$("#tds_paid").css('pointer-events', '');
		
		$("#bill-tds").show();
		$("#billNoList").show();
		$("#subledgerList").hide();
		$("#subLedgerId").val("0");		
		<c:forEach var = "supplier" items = "${supplierList}">
			var id = <c:out value = "${supplier.supplier_id}"/>
			
			if(id == supId){
				supStateId = '<c:out value = "${supplier.state.state_id}"/>';
				tdsapply = '<c:out value = "${supplier.tds_applicable}"/>';
				tdsRate = '<c:out value = "${supplier.tds_rate}"/>';
				supgst='<c:out value = "${supplier.gst_applicable}"/>';				
				rcm='<c:out value = "${supplier.reverse_mecha}"/>';
				$('#product_id').find('option').remove();
				$('#product_id').append($('<option>', {
				    value: 0,
				    text: 'Select Product Name'
				}));
				<c:forEach var = "product" items = "${supplier.product}">
				if((${product.status=="true"}) && (${product.flag=="true"}) && (${product.tax_type==1}) && (${product.product_approval==3})){
					var tempArray = [];
		    		tempArray["id"]=${product.product_id};
				    tempArray["name"]='${product.product_name}';
				    productArray.push(tempArray);
				}
				</c:forEach>
				
				productArray.sort(function(a, b) {
	         		var textA = a.name.toUpperCase();
		         	var textB = b.name.toUpperCase();
		         	return (textA < textB) ? -1 : (textA > textB) ? 1 : 0;
		     	});
				
				for(i=0;i<productArray.length;i++) {
			 		$('#product_id').append($('<option>', {
				    	value: productArray[i].id,
					    text: productArray[i].name
					})); 
				}				
			}
		</c:forEach>
		
		$('#purchaseEntryId').find('option').remove();
		$('#purchaseEntryId').append($('<option>', {
		    value: 0,
		    text: 'Select Supplier Bill No'
		}));
		$('#purchaseEntryId').append($('<option>', {
		    value: -1,
		    text: 'Advance'
		    
		}));
		$('#purchaseEntryId').append($('<option>', {
		    value: -2,
		    text: 'Opening Balance'
		    
		}));
		
		<c:forEach var = "purchaseEntry" items = "${purchaseEntryList}">
			var sId = <c:out value = "${purchaseEntry.supplier.supplier_id}"/>
			if(sId == supId){
				var status = <c:out value = "${purchaseEntry.entry_status}"/>;
				if(status == <%=MyAbstractController.ENTRY_PENDING%>){
					$('#purchaseEntryId').append($('<option>', {
					    value: '${purchaseEntry.purchase_id}',
					    text: '${purchaseEntry.supplier_bill_no}'
					}));					
				}				
			}var amount = (rate*quantity);
			
			if(discount!=""){				
				amount = parseFloat(amount) - parseFloat((amount*discount)/100);				 
			}
			amount = parseFloat(amount)+parseFloat(labour_charges)+parseFloat(freight)+parseFloat(others);
		</c:forEach>
		var purchase_id = '<c:out value= "${payment.supplier_bill_no.purchase_id}"/>';	
		$("#purchaseEntryId option[value='"+purchase_id+"']").attr("selected", "selected");
	}
	
	
	
}

/*  */
 
 
 
 
 /*  */

function cancel(){
	window.location.assign('<c:url value="paymentList"/>');
}

function saveyearid(){
	 $('#year-model').modal('hide');
}

function checkamount(){
	
	var supplier_id=document.getElementById("supplierId").value;
	var supplier_bill_no=document.getElementById("purchaseEntryId").value;
		var dateString = $("#paymentDate").val();	
		
		if(dateString=="")
		{
		alert("Select payment Date");
		return false;
		}	
		if(validatedate(startdate1,enddate1,dateString)==false)
		{		
			return false;
		}
		else
		{
				if((supplier_bill_no!=0) && (supplier_id!=0)&& (supplier_id!=-1)){
					<c:forEach var = "purchaseEntry" items = "${purchaseEntryList}">
					var purId = <c:out value = "${purchaseEntry.purchase_id}"/>;
					if(purId == supplier_bill_no){
						
						var date1 = '<c:out value = "${purchaseEntry.supplier_bill_date}"/>';
						var date2 = $("#paymentDate").val();						
						if(isValidDate(date1)==false){
							date1=formatDate(date1);
						 }	
						if(isValidDate(date2)==false){
							enddate1=formatDate(date2);
							 }		
							var date11=process(date1);
							var date22=process(date2);						
						if(date11 > date22)
						{				
							alert("Payment Date cann't be less than bill date");
							return false;
						}
					}
					</c:forEach>
					var outstanding=$("#outstandingAmount").html();
					var amount=document.getElementById("amount").value;
					if((amount!="") && (outstanding!="")){
						outstanding=parseFloat(Math.ceil(outstanding));
						
						if(transaction_id==""){
							amount=parseFloat(amount);
						 	var prevamount=0;
						}
						else{
					  		var prevamount='<c:out value = "${payment.amount}"/>';
							amount=parseFloat(amount)-parseFloat(prevamount);
						}
					
						if(amount>outstanding){
							alert("Payment Amount Can not be Greater than Bill Outstanding Amount");
							return false;
						}
						else{
							$("#gst_applied ").removeAttr('disabled');
							return true;
						}
					}
				}
				if (supplier_bill_no == -2)
				{
				
				var outstandingAmountagainstopening=$("#outstandingAmountagainstopening").html();
				var amount=document.getElementById("amount").value;
				
				if((amount!="") && (outstandingAmountagainstopening!="")){
					outstandingAmountagainstopening=parseFloat(Math.ceil(outstandingAmountagainstopening));

					if(transaction_id==""){
						amount=parseFloat(amount);
					 	var prevamount=0;
					}
					else{
				  		var prevamount='<c:out value = "${payment.amount}"/>';
						amount=parseFloat(amount)-parseFloat(prevamount);
					}
					
					if(amount>outstandingAmountagainstopening){
						//alert("old outstanding amount:"+outstandingAmountagainstopening);
						alert("Payment Amount Can not be Greater than Bill opening Outstanding Amount");
						return false;
					}
					else{
						$("#gst_applied ").removeAttr('disabled');
						return true;
					}
				
				}
				}
		}
	//return true;
}

function setadvanceno(e){
	if(e=="false"){
	   $("#gst_applied").val("false");
	}
} 

function editproductdetail(id){
	window.location.assign('<c:url value="editproductdetailforPayment"/>?id='+id);
}
function setdatelimit(startdate,enddate)
{    	
	startdate1=startdate;
	enddate1=enddate;
	startdate=formatDate(startdate);
	enddate=formatDate(enddate);		
	curdate=formatDate(new Date());
	var maxdate=curdate;
	if(curdate < enddate)
		maxdate=enddate;
	$("#paymentDate").datepicker({dateFormat: 'dd-mm-yy'});
	$('#paymentDate').datepicker("option", { minDate: startdate, 
        maxDate: enddate });
}
function isValidDate(dateStr) {
	 
	 // Checks for the following valid date formats:
	 // MM/DD/YYYY
	 // Also separates date into month, day, and year variables
	 var datePat = /^(\d{2,2})(-)(\d{2,2})\2(\d{4}|\d{4})$/;
	 
	 var matchArray = dateStr.match(datePat); // is the format ok?
	 if (matchArray == null) {
	  //alert("Date must be in MM-DD-YYYY format")
	  return false;
	 }
	 
	 month = matchArray[1]; // parse date into variables
	 day = matchArray[3];
	 year = matchArray[4];
	 if (month < 1 || month > 12) { // check month range
	  //alert("Month must be between 1 and 12");
	  return false;
	 }
	 if (day < 1 || day > 31) {
	 // alert("Day must be between 1 and 31");
	  return false;
	 }
	 if ((month==4 || month==6 || month==9 || month==11) && day==31) {
	  //alert("Month "+month+" doesn't have 31 days!")
	  return false;
	 }
	 if (month == 2) { // check for february 29th
	  var isleap = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
	  if (day>29 || (day==29 && !isleap)) {
	  // alert("February " + year + " doesn't have " + day + " days!");
	   return false;
	    }
	 }
	 return true;  // date is valid
	}
 
function validatedate(startdate1,enddate1,dateString)
{
	if(isValidDate(startdate1)==false){
		startdate1=formatDate(startdate1);
	 }	
	if(isValidDate(enddate1)==false){
		enddate1=formatDate(enddate1);
		 }		
		var dateStringnew=process(dateString);
		var startdatenew=process(startdate1);
		var enddatenew=process(enddate1);
		var flag=false;
		if(dateStringnew<startdatenew)
		  {
			
			alert("Select Date between "+startdate1+" and "+enddate1);
			flag=false;
		  }
		  else if(dateStringnew>enddatenew)
		  {
			  
			  alert("Select Date between "+startdate1+" and "+enddate1);
			  flag=false;
		  }
		  else
			{
			flag=true;
			}
		return flag;
}
function process(date){
   var parts = date.split("-");
   var date = new Date(parts[1] + "/" + parts[0] + "/" + parts[2]);
   return date.getTime();
}
function formatDate(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [day,month,year].join('-');
}
function showTDSAmount(tdspaid)
{
	if(tdspaid=="true")
	{
	$("#tds-amount").show();
	}
else
	{
	$("#tds-amount").hide();
	}
}

  function save(){
	  
	  var divinfo=document.getElementById("details-data");
		var bValid="true";		
		var productId=document.getElementById("product_id").value;
		var unit=document.getElementById("UOM").value;
		var hsncode=document.getElementById("HSNCode").value;
		var CGST=document.getElementById("CGST").value;
		var SGST=document.getElementById("SGST").value;
		var IGST=document.getElementById("IGST").value;
		var SCT=document.getElementById("SCT").value;
		var quantity=document.getElementById("quantity").value;
		var rate=document.getElementById("rate").value;
		var labourCharge=document.getElementById("labour_charges").value;
		var discount=document.getElementById("discount").value;
		var freightCharges=document.getElementById("freight").value;
		
		/* transactionAmount = ($("#transaction_value").val() == "") ? 0 : $("#transaction_value").val();
		totalCgst = ( $("#cgst").val() == "" ) ? 0 : $("#cgst").val();
		totalSgst = ( $("#sgst").val() == "" ) ? 0 : $("#sgst").val();
		totalIgst = ( $("#igst").val() == "" ) ? 0 : $("#igst").val();
		totalSct = ( $("#state_compansation_tax").val() == "" ) ? 0 : $("#state_compansation_tax").val();

		if(productId==0){
			alert("Please Select Product");
			return false;
		}
		else if(quantity.trim()=="" || quantity.trim()=="0"){
			alert("Please Enter Quantity");
			return false;
		}
		else if(hsn.trim()=="" || hsn.trim()=="0"){
			alert("Please Enter HSN");
			return false;
		}
		else if(rate.trim()=="" || rate.trim()=="0"){
			alert("reched");
			alert("Please Enter Rate");
			return false;
		}
		else if(discount.trim()==""){
			alert("Please Enter Discount");
			return false;
		}
		else if(((sgst!=0 && sgst!="") || (cgst!=0 && cgst!="")) && (igst!=0 && igst!=""))
		{
		alert("Either input CGST+SGST or IGST");
		return false;
		}
		if(cgst.trim()=="")
			document.getElementById("CGST").value="0";
		if(sgst.trim()=="")
			document.getElementById("SGST").value="0";
		if(igst.trim()=="")
			document.getElementById("IGST").value="0";
		else{
			if(!isDuplicate(productId)){
				transactionAmount = parseFloat(transactionAmount) + parseFloat(tamount);
				totalCgst = parseFloat(totalCgst) + parseFloat(cgst);
				totalSgst = parseFloat(totalSgst) + parseFloat(sgst);
				totalIgst = parseFloat(totalIgst) + parseFloat(igst);
				totalSct = parseFloat(totalSct) + parseFloat(state_com_tax);			
				totalAmount = parseFloat(transactionAmount) + parseFloat(totalCgst) + parseFloat(totalSgst) + parseFloat(totalIgst) + parseFloat(totalSct);
				
				$("#transaction_value").val(parseFloat(transactionAmount).toFixed(2));
				$("#cgst").val(parseFloat(totalCgst).toFixed(2));
				$("#sgst").val(parseFloat(totalSgst).toFixed(2));
				$("#igst").val(parseFloat(totalIgst).toFixed(2));
				$("#state_compansation_tax").val(parseFloat(totalSct).toFixed(2));
				$("#round_off").val(parseFloat(totalAmount).toFixed(2));		
				$("#amount").val(parseFloat(totalAmount).toFixed(2));
				addRow(0,productId,productName,quantity,hsn,rate,discount,uomId,uomUnit,cgst,igst,sgst,state_com_tax,labour_charges,freight,others,tamount);
			}
			else{
				alert("Product already added");
				return false;
			}
		} */
		
		 if(divinfo.style.display != "none"){
			
			 
			 var prodlist="false";
				var size=paymentDetails.length;
				
				if(size>0)
					{
					prodlist="true";
					}
				
				
		if(prodlist=="false")
			{
			if(productId == 0){
				alert("Please Select Product");
				bValid="false";
				return false;
			}
			else if(quantity.trim()=="" || quantity.trim()=="0"){
				alert("Please Enter Quantity");
				bValid="false";
				return false;
			}
			else if(hsncode.trim()=="" || hsncode.trim()=="0"){
				alert("Please Enter HSN");
				bValid="false";
				return false;
			}
			else if(rate.trim()=="" || rate.trim()=="0"){
				alert("Please Enter Rate");
				bValid="false";
				return false;
			}
			else if(discount.trim()==""){
				alert("Please Enter Discount");
				bValid="false";
				return false;
			}
			else if(discount > 100){
				alert("Discount should less than 100");
				bValid="false";
			}	else{
				if(!isDuplicate(productId)){
					transactionAmount = parseFloat(transactionAmount) + parseFloat(tamount);
					totalCgst = parseFloat(totalCgst) + parseFloat(cgst);
					totalSgst = parseFloat(totalSgst) + parseFloat(sgst);
					totalIgst = parseFloat(totalIgst) + parseFloat(igst);
					totalSct = parseFloat(totalSct) + parseFloat(state_com_tax);			
					totalAmount = parseFloat(transactionAmount) + parseFloat(totalCgst) + parseFloat(totalSgst) + parseFloat(totalIgst) + parseFloat(totalSct);
					
					$("#transaction_value").val(parseFloat(transactionAmount).toFixed(2));
					$("#cgst").val(parseFloat(totalCgst).toFixed(2));
					$("#sgst").val(parseFloat(totalSgst).toFixed(2));
					$("#igst").val(parseFloat(totalIgst).toFixed(2));
					$("#state_compansation_tax").val(parseFloat(totalSct).toFixed(2));
					$("#round_off").val(parseFloat(totalAmount).toFixed(2));		
					$("#amount").val(parseFloat(totalAmount).toFixed(2));
					addRow(0,productId,productName,quantity,hsn,rate,discount,uomId,uomUnit,cgst,igst,sgst,state_com_tax,labour_charges,freight,others,tamount);
				    return true;
				}
				else{
					alert("Product already added");
					return false;
				}
		
		      }
	  
          }else{
        	 
        	  return true;
          }
          
      }
  }	
  
</script>

<style>
input, select, textarea {
	width: 50px;
}
</style>
<%@include file="/WEB-INF/includes/mobileFooter.jsp"%>