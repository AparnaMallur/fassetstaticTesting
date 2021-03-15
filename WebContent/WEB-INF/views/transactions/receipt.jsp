
<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<link rel="stylesheet" href="//code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="//code.jquery.com/jquery-1.9.1.js"></script>
<script src="//code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_TWO_HUNDRED%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THREE_HUNDRED%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_HUNDRED%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_THOUSAND%></c:set>
<c:set var="ENTRY_PENDING"><%=MyAbstractController.ENTRY_PENDING%></c:set>
<spring:url value="/resources/images/delete.png" var="deleteImg" />
<spring:url value="/resources/images/edit.png" var="editImg" />
<div class="breadcrumb">
	<h3>Receipt</h3>
	<a href="homePage">Home</a> » <a href="receiptList">Receipt</a> » <a
		href="#">Create</a>
</div>
<c:if test="${successMsg != null}">
	<div class="successMsg" id="successMsg">
		<strong>${successMsg}</strong>
	</div>
</c:if>
<div class="col-md-12 wideform">
	<form:form id="receiptForm" action="saveReceipt" method="post"
		commandName="receipt" onsubmit="return checkamount();">
		<!-- Small modal -->
		<div id="year-model" data-backdrop="static" data-keyboard="false"
			class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog"
			aria-labelledby="mySmallModalLabel" aria-hidden="true">
			<div class="modal-dialog ">
				<div class="modal-content">
					<div class="modal-header">
						<h4 class="modal-title" id="mySmallModalLabel">Select
							Accounting Year</h4>
					</div>
					<div class="modal-body">
						<c:set var="first_year" value="0" />
						<c:forEach var="year" items="${yearList}">
							<c:choose>
								<c:when test="${first_year==0}">
									<form:radiobutton path="year_id" value="${year.year_id}"
										checked="checked"
										onclick="setdatelimit('${year.start_date}','${year.end_date}')" />${year.year_range} 
											</c:when>
								<c:otherwise>
									<form:radiobutton path="year_id" value="${year.year_id}"
										onclick="setdatelimit('${year.start_date}','${year.end_date}')" />${year.year_range} 
											</c:otherwise>
							</c:choose>
							<c:set var="first_year" value="${first_year+1}" />
						</c:forEach>
					</div>
					<div class="modal-footer">
						<button type="button"
							class="btn btn-primary waves-effect waves-light"
							onclick='saveyearid()'>Save Year</button>
					</div>
				</div>
				<!-- /.modal-content -->
			</div>
		</div>
		<!-- /.modal-dialog -->
		<div class="fassetForm">
			<form:input path="receipt_id" hidden="hidden" />
			<form:input path="created_date" hidden="hidden" />
			<form:input path="receiptagainstOpeningBalnce" id="receiptagainstOpeningBalnce" hidden="hidden" />
			<%-- 			
		 --%>
			<form:input path="productInformationList" id="productInformationList"
				hidden="hidden" />
			<form:input path="save_id" id="save_id" type="hidden" />
			<input class="logInput" id="cgst_hidden" type="hidden" /> <input
				class="logInput" id="sgst_hidden" type="hidden" /> <input
				class="logInput" id="igst_hidden" type="hidden" /> <input
				class="logInput" id="sct_hidden" type="hidden" />

			<div id="bill-tds" class="row"
				style="border: 1px; border-style: double; background: rgb(54, 94, 191); align-content:; color: white;">
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
			
		<div class="row" id="openingAmount"

				style="border: 1px; border-style: double; background: rgb(54, 94, 191); align-content:; color: white;">


				<div class="col-md-2">

					<label>Outstanding Amount : </label>

				</div>

				<div class="col-md-2">

					<label id="outstandingAmountagainstopening"></label>

				</div>


			</div>	
			<div class="row">
				<div class="col-md-6 col-xs-12">
					<c:if test="${receipt.receipt_id!=Null}">
						<div class="row">
							<div class="col-md-3 control-label">
								<label>Voucher No<span>*</span></label>
							</div>
							<div class="col-md-9">
								<form:input path="voucher_no" class="logInput" id="voucher_no"
									placeholder="Voucher no" readonly="true" />
							</div>
						</div>
					</c:if>
					<%-- <c:choose>
								<c:when test='${receipt.receipt_id==Null}'>
									<div class="row">				
										<div class="col-md-3 control-label"><label>Voucher Serial No<span>*</span></label></div>
										<div class = "col-md-9">
											<form:select path="voucher_range" class="logInput" id="voucher_range">
											    <form:option value="0" label="Select Range"/>	
											    <c:forEach var = "vrange" items = "${voucherrange}">							
													<form:option value = "${vrange}" >${vrange}</form:option>	
												</c:forEach>
											</form:select>
											<span class="logError"><form:errors path="voucher_range" /></span>
										</div>
									  </div>	
								</c:when>
								<c:otherwise>					 	
		
								</c:otherwise>
						</c:choose>			 --%>
					<div class="row">
						<div class="col-md-3 control-label">
							<label>Date<span>*</span></label>
						</div>
						<div class="col-md-9">
							<input type="text" style="color: black;" id="date" name="date"
								placeholder="Date" autocomplete="off" onchange="dateRestriction()" onclick="setDatePicker()" > <span class="logError"><form:errors
									path="date"  /></span>
						</div><!-- onchange="setDate()" -->
					</div>

					<div id="customerInfo">
						<div class="row">
							<div class="col-md-3 control-label">
								<label>Customer<span>*</span></label>
							</div>
							<div class="col-md-9">
								<form:select path="customer_id" class="logInput"
									id="customer_id" onChange="getList(this.value)">
									<form:option value="0" label="Select Customer"/>
									<c:forEach var="list" items="${customerList}">
										<form:option value="${list.customer_id}">${list.firm_name}</form:option>
									</c:forEach>
									<form:option value="-1" label="Income/Assests/Liabilities" />
								</form:select>
								<span class="logError"><form:errors path="customer_id" /></span>
							</div>
						</div>

						<div class="row" id="subledgerInfo">
							<div class="col-md-3 control-label">
								<label>Income Type<span>*</span></label>
							</div>
							<div class="col-md-9">
								<form:select path="subledgerId" class="logInput"
									placeholder="Subledger Name" id="subledger_id">
									<form:option value="0" label="Select Income Type" />
									<c:forEach var="subledger" items="${subledgerList}">
										<c:if
											test="${subledger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 2 ||subledger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 3 ||subledger.ledger.accsubgroup.accountGroup.postingSide.posting_id == 4}">
											<form:option value="${subledger.subledger_Id}">${subledger.subledger_name}  </form:option>
										</c:if>
									</c:forEach>
								</form:select>
								<span class="logError"><form:errors path="subledgerId" /></span>
							</div>
						</div>
						<div class="row" id="customerbillInfo">
							<div class="col-md-3 control-label">
								<label>Customer Bill No.<span>*</span></label>
							</div>
							<div class="col-md-9">
								<form:select path="salesEntryId" class="logInput"
									id="customer_bill_no">
									<form:option value="0">--- Select Customer Bill No ---</form:option>
								</form:select>
								<span class="logError"><form:errors path="salesEntryId" /></span>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-3 control-label">
							<label>Receipt Type<span>*</span></label>
						</div>
						<div class="col-md-9">
							<form:select path="payment_type" class="logInput" id="payment_type">
								<form:option value="0">Select Receipt Type</form:option>
								<form:option value="1">Cash</form:option>
								<form:option value="2">Cheque</form:option>
								<form:option value="3">DD</form:option>
								<form:option value="4">NEFT/RTGS/Net banking/Account Transfer</form:option>
							</form:select>
							<span class="logError"><form:errors path="payment_type" /></span>
						</div>
					</div>
					<div class="row" id="chequeDDNo">
						<div class="col-md-3 control-label">
							<label>Cheque / DD No / IB No / Transaction No<span>*</span></label>
						</div>
						<div class="col-md-9">
							<form:input path="cheque_no" class="logInput" id="cheque_no"
								maxlength="${SIZE_HUNDRED}" placeholder="Cheque / DD No / IB No / Transaction No" />
							<span class="logError"><form:errors path="cheque_no" /></span>

						</div>
					</div>
					<div class="row" id="chequeDate">
						<div class="col-md-3 control-label">
							<label>Cheque / DD Date / Transaction Date<span>*</span></label>
						</div>
						<div class="col-md-9">
							<form:input path="cheque_date" class="logInput" id="cheque_date"
								placeholder="Cheque / DD Date / Transaction Date" autocomplete="off" />
							<span class="logError"><form:errors path="cheque_date" /></span>

						</div>
					</div>
					<div class="row">
						<div class="col-md-3 control-label">
							<label>TDS Paid?<span>*</span></label>
						</div>
						<div class="col-md-9">
							<form:select path="tds_paid" id="tds_paid" class="logInput"
								onChange="showTDSAmount(this.value)">
								<%-- 								<form:option value="0" label="--Select--" />
		 --%>
								<form:option value="false">No</form:option>
								<form:option value="true">Yes</form:option>

							</form:select>
							<span class="logError"><form:errors path="tds_paid" /></span>
						</div>
					</div>
					<div class="row" id="tds-amount">
						<div class="col-md-3 control-label">
							<label>TDS Amount<span>*</span></label>
						</div>
						<div class="col-md-9">
							<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" value="${receipt.tds_amount}"
				 var="tdsvalue"/>
							<form:input path="tds_amount" class="logInput" id="tds_amount"
								placeholder="TDS Amount" value="${tdsvalue}"/>
							<span class="logError"><form:errors path="tds_amount" /></span>
						</div>
					</div>
				</div>
				<div class="col-md-6 col-xs-12">
					<div class="row" id='bank-comp'>
						<div class="col-md-3 control-label">
							<label>Bank<span>*</span></label>
						</div>
						<div class="col-md-9">
							<form:select path="bankId" id="bank_Id" class="logInput">
								<form:option value="0" label="Select Bank" />
								<c:forEach var="bank" items="${bankList}">
									<form:option value="${bank.bank_id}">${bank.bank_name} - ${bank.account_no}</form:option>
								</c:forEach>
							</form:select>
							<span class="logError"><form:errors path="bankId" /></span>
						</div>
					</div>
					<div class="row">
						<div class="col-md-3 control-label">
							<label>Advance Receipt<span>*</span></label>
						</div>
						<div class="col-md-9">
							<form:select path="advance_payment" id="advance_payment" class="logInput"
								onchange="setadvanceno(this.value)">
								<form:option value="0" label="Select" />
								<form:option value="true">Yes</form:option>
								<form:option value="false">No</form:option>
							</form:select>
							<span class="logError"><form:errors path="advance_payment" /></span>

						</div>
					</div>
					<div class="row" style="display:none;">
						<div class="col-md-3 control-label">
							<label>GST Applicable<span>*</span></label>
						</div>
						<div class="col-md-9">
							<form:select path="gst_applied" id="gst_applied"
								onchange="showdetails(this.value)" class="logInput">
								
								<form:option value="2">No</form:option>
							</form:select>
							<span class="logError"><form:errors path="gst_applied" /></span>
						</div>
					</div>
					<div class="row">
						<div class="col-md-3 control-label">
							<label>Amount<span>*</span></label>
						</div>
						<div class="col-md-9">
						<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" value="${receipt.amount}"
				 var="amt"/>
							<form:input path="amount" class="logInput" id="amount"
								maxlength="18" placeholder="Amount" value="${amt}" />
							<span class="logError"><form:errors path="amount" /></span>
						</div>
					</div>
					<div class="row">
						<div class="col-md-3 control-label">
							<label>Other Remark</label>
						</div>
						<div class="col-md-9">
							<form:textarea path="other_remark" class="logInput"
								id="other_remark" rows="5" placeholder="Remark" maxlength="1000"></form:textarea>
						</div>
					</div>

				</div>
			</div>

			<table id='table-value' style="display: none">
				<tr>
					<td colspan="3"><label>Transaction Amount</label> 
					<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" value="${receipt.transaction_value}"
				 var="trvalue"/>
					<form:input
							path="transaction_value" class="logInput hlable"
							id="transaction_value" readonly="true"
							style="background-color: transparent;" value="${trvalue}"/></td>
					<td colspan="2"><label>CGST</label> 
					<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" value="${receipt.cgst}"
				 var="cgstvalue"/>
					<form:input path="cgst"
							class="logInput hlable" id="cgst" readonly="true"
							style="background-color: transparent;" value="${cgstvalue}"/></td>
					<td colspan="2"><label>SGST</label> 
					<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" value="${receipt.sgst}"
				 var="sgstvalue"/>
					<form:input path="sgst"
							class="logInput hlable" id="sgst" readonly="true"
							style="background-color: transparent;" value="${sgstvalue}"/></td>
					<td colspan="2"><label>IGST</label> 
					<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" value="${receipt.igst}"
				 var="igstvalue"/>
					<form:input path="igst"
							class="logInput hlable" id="igst" readonly="true"
							style="background-color: transparent;" value="${igstvalue}"/></td>
					<td colspan="2"><label>CESS</label>
					<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" value="${receipt.state_compansation_tax}"
				 var="cstvalue"/>
					 <form:input
							path="state_compansation_tax" class="logInput hlable"
							id="state_compansation_tax" readonly="true"
							style="background-color: transparent;" value="${cstvalue}"/></td>
					<td colspan="3"><label>Total Amount</label> 
					<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" value="${receipt.round_off}"
				 var="rndvalue"/>
					<form:input
							path="round_off" class="logInput hlable" id="round_off"
							readonly="true" style="background-color: transparent;" value="${rndvalue}"/></td>
				</tr>
			</table>
		</div>
		<div class="clearfix"></div>
		<c:choose>
			<c:when test="${!empty customerproductList}">
				<div class="table-responsive">
					<table class="table table-bordered table-striped">
						<thead>
							<tr>
								<th>Action</th>
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
							<c:forEach var="prod_list" items="${customerproductList}">
								<tr>
									<td><a href="#"
										onclick="deleteproductdetail(${prod_list.receipt_detail_id})"><img
											src='${deleteImg}' style="width: 20px;" /></a> <a href="#"
										onclick="editproductdetail(${prod_list.receipt_detail_id})"><img
											src='${editImg}' style="width: 20px;" /></a></td>
	
									<td>${prod_list.product_name}</td>
									<td><fmt:formatNumber
											type="number" minFractionDigits="2" maxFractionDigits="2"
											value="${prod_list.quantity}"/></td>
									<td>${prod_list.UOM}</td>
									<td>${prod_list.HSNCode}</td>
									<td><fmt:formatNumber
											type="number" minFractionDigits="2" maxFractionDigits="2"
											value="${prod_list.rate}"/></td>
									<td><fmt:formatNumber
											type="number" minFractionDigits="2" maxFractionDigits="2"
											value="${prod_list.discount}"/></td>
									<td><fmt:formatNumber
											type="number" minFractionDigits="2" maxFractionDigits="2"
											value="${prod_list.labour_charges}"/></td>
									<td><fmt:formatNumber
											type="number" minFractionDigits="2" maxFractionDigits="2"
											value="${prod_list.freight}"/></td>
									<td><fmt:formatNumber
											type="number" minFractionDigits="2" maxFractionDigits="2"
											value="${prod_list.others}"/></td>
									<td><fmt:formatNumber
											type="number" minFractionDigits="2" maxFractionDigits="2"
											value="${prod_list.CGST}"/></td>
									<td><fmt:formatNumber
											type="number" minFractionDigits="2" maxFractionDigits="2"
											value="${prod_list.SGST}"/></td>
									<td><fmt:formatNumber
											type="number" minFractionDigits="2" maxFractionDigits="2"
											value="${prod_list.IGST}"/></td>
									<td><fmt:formatNumber
											type="number" minFractionDigits="2" maxFractionDigits="2"
											value="${prod_list.state_com_tax}"/></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</c:when>
		</c:choose>
		<div id="dtl-btn">
			<button type="button" class="fassetBtn waves-effect waves-light"
				onclick="showdiv()">Add Details</button>
		</div>
		<div id="details-data" style="display: none">
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
							<td><form:select path="product_id" class="logInput"
									id="product_id" placeholder="Product Name"
									onChange="product(this.value)">
									<form:option value="0" label="Select Product Name" />
								</form:select></td>
							<td><input id="transaction_amount" type="hidden" /> <input
								class="logInput" id="cgst_hidden" placeholder="cgst_hidden"
								type="hidden" /> <input class="logInput" id="sgst_hidden"
								placeholder="sgst_hidden" type="hidden" /> <input
								class="logInput" id="igst_hidden" placeholder="igst_hidden"
								type="hidden" /> <input class="logInput" id="sct_hidden"
								placeholder="sct_hidden" type="hidden" /> 
								<input
								class="logInput" id="quantity" placeholder="Quantity"
								onchange="calculaterate(this.value,1)"
								onkeypress="return isNumber(event)" maxlength="10"/></td>
							<td><input class="logInput" id="UOM" placeholder="UOM"
								readonly /></td>
							<td><input class="logInput" id="HSNCode"
								placeholder="HSNCode" onchange="getHsnDetails(this.value)" maxlength="10" readonly /></td>
							<td><input class="logInput" id="rate" placeholder="rate"
								onchange="calculaterate(this.value,2)"
								onkeypress="return isNumber(event)" maxlength="12"/></td>
							<td><input class="logInput" id="discount"
								placeholder="Discount" onchange="calculaterate(this.value,3)"
								onkeypress="return isNumber(event)" maxlength="6"/></td>
							<td><input class="logInput" id="labour_charges"
								placeholder="Labour charges"
								onchange="calculaterate(this.value,4)"
								onkeypress="return isNumber(event)" maxlength="10"/></td>
							<td><input class="logInput" id="freight"
								placeholder="Freight charges"
								onchange="calculaterate(this.value,5)"
								onkeypress="return isNumber(event)" maxlength="10"/></td>
							<td><input class="logInput" id="others" placeholder="Other"
								onchange="calculaterate(this.value,6)"
								onkeypress="return isNumber(event)" maxlength="10"/></td>
							<td><input id="CGST" class="logInput" placeholder="CGST"
								onkeypress="return isNumber(event)" maxlength="20"/></td>
							<td><input id="SGST" class="logInput" placeholder="SGST"
								onkeypress="return isNumber(event)" maxlength="20"/></td>
							<td><input id="IGST" class="logInput" placeholder="IGST"
								onkeypress="return isNumber(event)" maxlength="20"/></td>
							<td><input id="SCT" class="logInput" placeholder="CESS"
								onkeypress="return isNumber(event)" maxlength="20"/></td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="text-center">
				<button type="submit" class="fassetBtn waves-effect waves-light"
					onclick="return addproductdetails()">Save Product Details</button>
			</div>
		</div>
		<div class="row" style="text-align: center; margin: 15px;">
			<button class="fassetBtn" type="submit" onclick="return save()">
				Save & Back</button>
			<button class="fassetBtn" type="button" onclick="cancel()">
				<spring:message code="btn_cancel" />
			</button>
		</div>
	</form:form>
</div>

<script type="text/javascript">
		
		
function setDatePicker()
{
	
	$("#date").datepicker({dateFormat: 'dd-mm-yy'});
	 $('#date').datepicker("option", { maxDate: new Date() });
	$('#date').value = '';
	$('#date').focus(); // ui-datepicker-div
	 event.preventDefault();
	
} 


function dateRestriction() {
	 
	 
	 var datefield=document.getElementById("date").value;
	
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
		var id = document.getElementById("date");

		id.value = '';
		id.focus(); // ui-datepicker-div
		event.preventDefault();	
	}
	else if(ud > td ){
			
			alert("Cannot create voucher for future date");
			var id = document.getElementById("date");

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



		
		
function setDate(){
	//document.getElementById("from_date").value = e.value;	
	// date format validation
	var datevali = document.getElementById("date").value;
	
	var res = datevali.split("-");
    var nd = res[1]+"/"+res[0]+"/"+res[2];
    var ud = new Date(nd);
	var td = new Date();
	
	ud.setHours(0,0,0,0);
	
	
	td.setHours(0,0,0,0);
	
	if(ud > td ){
		
		alert("cant select post date");
		document.getElementById("date").value='';
		
	}
	
if(isValidDate(datevali)==true){
	 
		 return true;
 }else{
	alert("Invalid Date");
	window.location.reload();
	
}  
}
		
		var startdate="";
		var enddate="";
		var custStateId;
		var compStateId;
		var tdsRate = 0;
		var proList = [];     
		var tdsapply;
		var saved_cgst='<c:out value= "${receipt.cgst}"/>';
		var saved_sgst='<c:out value= "${receipt.sgst}"/>';
		var saved_igst='<c:out value= "${receipt.igst}"/>';
		var saved_transaction='<c:out value= "${receipt.transaction_value}"/>';
		var saved_sct='<c:out value= "${receipt.state_compansation_tax}"/>';
		var saved_total='<c:out value= "${receipt.round_off}"/>';
		var transaction_id = '<c:out value= "${receipt.receipt_id}"/>';	
		
		var paidtds=0;
		if(saved_cgst!="")
			saved_cgst=parseFloat(saved_cgst);
		if(saved_sgst!="")
			saved_sgst=parseFloat(saved_sgst);
		if(saved_igst!="")
			saved_igst=parseFloat(saved_igst);
		if(saved_sct!="")
			saved_sct=parseFloat(saved_sct);
		if(saved_transaction!="")
			saved_transaction=parseFloat(saved_transaction);
		if(saved_total!="")
			saved_total=parseFloat(saved_total);	
		
		$(document).ready(function() {
			$("#openingAmount").hide();
			var tdspaid=$("#tds_paid").val();
			if(tdspaid=="true")
				{
				$("#tds-amount").show();
				$('#tds_amount').attr('readonly', false);
				}
			else
				{
				$("#tds-amount").hide();
				}
			compStateId = '<c:out value = "${stateId}"/>';
			paidtds='<c:out value = "${paidtds}"/>';
		    setTimeout(function() {
		        $("#successMsg").hide()
		    }, 3000);
		    
		    $("#").keypress(function(e) {
				if (!lettersAndHyphenOnly(e)) {
					return false;
				}
			});
			
			$("#cheque_no,#amount,#tds_amount,#hsn_code,#quantity,#rate,#discount,#labour_charges,#freight,#others,#CGST,#SGST,#IGST,#SCT").keypress(function(e) {
				if (!digitsAndDotOnly(e)) {
					return false;
				}
			});
		    
		    $("#cheque_date").keypress(function(e) {
		    	
				if (!lettersAndDigitsAndSlashOnly(e)) {
					return false;
				}
			});
		    
		    $( "#cheque_date" ).datepicker({dateFormat: 'dd-mm-yy'});
		    $("#tds_rate1").keypress(function(e) {
				if (!digitsAndDotOnly(e)) {
					return false;
				}
			});	    
		    $("#amount").keypress(function(e) {
		    	
				if (!digitsAndDotOnly(e)) {
					return false;
				}
			});
		
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
			
			$("#customer_bill_no").on("change", function(){
				var customer_id = $("#customer_id").val();
				var bill=$("#customer_bill_no").val();
				 var closingbalanace=$("#total").val();
				if(bill=="-1"){
					/* $("#advance_payment").removeAttr('disabled'); */
					/* 	$("#gst_applied").removeAttr('disabled');
					$("#tds_paid").removeAttr('disabled'); */
					$("#advance_payment").css('pointer-events', '');
					$("#gst_applied").css('pointer-events', '');
					$("#tds_paid").css('pointer-events', '');	
					$("#advance_payment").css('pointer-events', 'none');
				    $("#advance_payment").val("true");
				    $("#billAmount").html('');
					$("#outstandingAmount").html('');
					$("#tds").html('');
					$("#gst_applied").css('pointer-events', 'none');
					$("#gst_applied").val(2);
				 	$("#bill-tds").hide();
					$("#openingAmount").hide();
				 	
				 	$('#tds_amount').attr('readonly', false);
				}
				else if(bill > 0){
					
					$("#openingAmount").hide();
					$("#bill-tds").show();		
					<c:forEach var = "salesEntry" items = "${salesEntryList}">
						var saleId = <c:out value = "${salesEntry.sales_id}"/>;
						if(saleId == bill){
							var amount = <c:out value = "${salesEntry.transaction_value}"/>;
							var totalAmount = <c:out value = "${salesEntry.round_off}"/>;
							<c:set var="totalPaid" value="${0}"/>
							<c:forEach var = "receipt" items = "${salesEntry.receipts}">
								<c:set var="totalPaid" value="${totalPaid + receipt.amount}" />
							</c:forEach>
							<c:forEach var = "note" items = "${salesEntry.creditNote}">
							<c:set var="totalPaid" value="${totalPaid + note.round_off}" />
						    </c:forEach>
							var total = <c:out value = "${totalPaid}"/>;
							var outstanding = 0;
							if(tdsapply==1)
								var tdsAmount = parseFloat((amount*tdsRate)/100).toFixed(2);
							else
								var tdsAmount=0;					
							if(total > 0){
								outstanding = parseFloat(totalAmount - total).toFixed(2);
							}
							else{
								outstanding = parseFloat(totalAmount).toFixed(2);
							}
							
							var billAmount = parseFloat((parseFloat(totalAmount)+parseFloat(tdsAmount))).toFixed(2);
							$("#billAmount").html(billAmount);
							$("#outstandingAmount").html(outstanding);
							
							/* $.ajax({
						        type: 'POST',
						        url: 'getPaidTDS?billid='+saleId,
						        async:false,
					            contentType: 'application/json',
							      	success: function (data){  		
							      		paidtds=data;
							      	},
							        error: function (e) {
							            console.log("ERROR: ", e);
							        },				              
						    }); */
							/* var totaltds=tdsAmount-paidtds;	
							totaltds=parseFloat(totaltds).toFixed(2);
							if(totaltds>0)
								{
								$("#tds_paid").val("true");
								$("#tds_paid").removeAttr('disabled');
		
								} */
							$("#advance_payment").css('pointer-events', '');
							$("#gst_applied").css('pointer-events', '');
							$("#tds_paid").css('pointer-events', '');
							$("#tds").html(parseFloat(tdsAmount).toFixed(2));
							/* $("#tds-amount").show();
							$("#tds_amount").val(totaltds);
						    $('#tds_amount').attr('readonly', true); */
						    $("#tds_paid").val("false");
						    $("#tds_paid").css('pointer-events', 'none');
							$("#advance_payment").val("false");
						    $("#advance_payment").css('pointer-events', 'none');
							$("#gst_applied").val(2);
							$("#gst_applied").css('pointer-events', 'none');
							
						}
					</c:forEach>
				}
				
							else if(bill == "-2")
							{
								
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
								$("#gst_applied").val(2);
								$("#gst_applied").css('pointer-events', 'none');
								 $("#tds-amount").hide();
									$("#openingAmount").show();
										
										/* first ajax call for total value */
									
									$.ajax({
										
								        type: 'GET',
								        url: 'getOpeningBalanceforreceipt?customerid='+customer_id,
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
						
			var customer_id = '<c:out value= "${receipt.customer_id}"/>';
			getList(customer_id);
				if(customer_id=="")
				{		
					/*  $("#customer_id").val(-1); */
					 $("#tds_paid").val("false");
					 $("#tds_paid").prop('disabled', 'disabled');
				}
			else if(customer_id=="-1")
				{
					 $("#tds_paid").val("false");
					 //$("#tds_paid").prop('disabled', 'disabled');
				}
			else
				{
				$("#bill-tds").hide();
				  $("#tds_paid").removeAttr('disabled');
				}
				
				
			var bill_no='<c:out value= "${receipt.salesEntryId}"/>';
			if(bill_no > 0){		
				$("#customer_bill_no").val(bill_no);
				//$("#tds_paid").val("true");
				 //$("#tds_paid").prop('disabled', 'disabled');
				
			}	
			else
				{
				$("#customer_bill_no").val(-1);
				 $("#tds_paid").removeAttr('disabled');
				}
			var transaction_id = '<c:out value= "${receipt.receipt_id}"/>';	
		
				if((customer_id == -1) || (customer_id.trim()=="")){
					document.getElementById("subledgerInfo").style.display="block";
					document.getElementById("customerbillInfo").style.display="none";
					
				}
				
				if(customer_id==0)
				{
					 $("#tds_paid").removeAttr('disabled');
				document.getElementById("customerbillInfo").style.display="none";
				}
			else if((customer_id == -1) || (customer_id.trim()=="")){
					document.getElementById("subledgerInfo").style.display="block";
					document.getElementById("customerbillInfo").style.display="none";
				}
				else{	
					document.getElementById("subledgerInfo").style.display="none";
					document.getElementById("customerbillInfo").style.display="block";
					var bill=document.getElementById("customer_bill_no").value;
					<c:forEach var = "salesEntry" items = "${salesEntryList}">
					var saleId = <c:out value = "${salesEntry.sales_id}"/>;
					if(saleId == bill){
						var amount = <c:out value = "${salesEntry.round_off}"/>;
						var totalAmount = <c:out value = "${salesEntry.transaction_value}"/>;
		
						<c:set var="total" value="${0}"/>
						<c:forEach var = "receipt" items = "${salesEntry.receipts}">
							<c:set var="total" value="${total + receipt.amount}" />
						</c:forEach>
						<c:forEach var = "note" items = "${salesEntry.creditNote}">
						<c:set var="total" value="${total + note.round_off}" />
					    </c:forEach>
						
						var total = <c:out value = "${total}"/>;
						var outstanding = 0;
						if(tdsapply==1)
						var tdsAmount = parseFloat((totalAmount*tdsRate)/100).toFixed(2);
						else
							var tdsAmount=0;
						if(total > 0){
							outstanding = parseFloat(amount - total).toFixed(2);
						}
						else{
							outstanding = parseFloat(amount).toFixed(2);
						}
						var billAmount = parseFloat((parseFloat(amount)+parseFloat(tdsAmount))).toFixed(2);
						$("#billAmount").html(billAmount);
						$("#outstandingAmount").html(outstanding);
						
					
					    $("#tds").html(parseFloat(tdsAmount).toFixed(2));
						/* $("#tds-amount").show();
						$("#tds_amount").val(totaltds);
					    $('#tds_amount').attr('readonly', true); */
					    $("#tds_paid").val("false");
					    $("#tds_paid").css('pointer-events', 'none');
						$("#advance_payment").val("false");
					    $("#advance_payment").css('pointer-events', 'none');
						$("#gst_applied").val(2);
						$("#gst_applied").css('pointer-events', 'none');
					}
				</c:forEach>
				}
			
			var payType = '<c:out value = "${receipt.payment_type}"/>';
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
		   		$("#chequeDDNo").show();
		   		$("#chequeDate").show();
		   		$("#bank-comp").show();	
		   	}
		       	
		   
			var gst_applied =  $("#gst_applied").val(); 	
			if(gst_applied==1){
				document.getElementById("dtl-btn").style.display="block";
				if(transaction_id!="")
				{
		   		document.getElementById("table-value").style.display="block";
				}
		   		document.getElementById("details-data").style.display="block";
			}
			else{
				document.getElementById("dtl-btn").style.display="none";
		   		document.getElementById("table-value").style.display="none";
		   		document.getElementById("details-data").style.display="none";
			}
			var date = '<c:out value= "${receipt.date}"/>';	
			
			if(date!=""){		
				newdate=formatDate(date);	
				$("#date").val(newdate);
				$("#date").datepicker("setDate", newdate);
				$("#date").datepicker("refresh");
			}
			
			var cheque_date = '<c:out value= "${receipt.cheque_date}"/>';	
		
			if(cheque_date!=""){
				cheque_date=formatDate(cheque_date);	
				$("#cheque_date").datepicker("setDate", cheque_date);
				$("#cheque_date").datepicker("refresh");
			}
			
			var years = '<c:out value= "${yearList}"/>';
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
				
				var savedyear='<c:out value= "${receipt.accountingYear.year_id}"/>';			
				<c:forEach var = "year" items = "${yearList}">			
					 if("${year.year_id}"==savedyear)
						{
						 startdate1='${year.start_date}';
				    	    enddate1='${year.end_date}';
					   setdatelimit('${year.start_date}','${year.end_date}');
					   var supdate="${receipt.date}";				
					   supdate=formatDate(supdate);
					   $("#date").val(supdate);
					   } 
				</c:forEach> 
				}
			if(customer_id!=""){
				$("#customer_bill_no").val(<c:out value = "${receipt.salesEntryId}"/>);
			}
		});
			
		function cancel(){
			window.location.assign('<c:url value = "receiptList"/>');	
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
		function formatDate1(date) {
			
		    var d = new Date(date),
		        month = '' + (d.getMonth() + 1),
		        day = '' + d.getDate(),
		        year = d.getFullYear();
		   
		    if (month.length < 2) month = '0' + month;
		    if (day.length < 2) day = '0' + day;
		
		    return [day,month,year].join('-');
		}
		
		function showdetails(e){ 
			
			if(e==1){		
				document.getElementById("details-data").style.display="block";
				if(transaction_id!="")
				{
				document.getElementById("table-value").style.display="block";
				}
			}
			else{
				document.getElementById("table-value").style.display="none";
				document.getElementById("details-data").style.display="none";
			}
		}
		
		function calculaterate(e,flag){
			
			var total=e;
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
		 	else
		 		rate=0;
		 	if(quantity!="")
			 	quantity=parseFloat(quantity);
		 	else
		 		quantity=0;
		 	if(labour_charges!="")
			 	labour_charges=parseFloat(labour_charges);
		 	else
		 		labour_charges=0;
		 	if(discount!="")
			 	discount=parseFloat(discount);
		 	else
		 		discount=0;
		 	if(freight!="")
			 	freight=parseFloat(freight);
		 	else
		 		freight=0;
		 	if(others!="")
			 	others=parseFloat(others);
		 	else
		 		others=0;
			var amount=(rate*quantity)+labour_charges+freight+others;
		 	var damount=rate*quantity;
			
		 	if(discount!=""){				
		 		var disamount=parseFloat(damount)-parseFloat((damount*discount)/100);		
			 	var tamount=parseFloat(disamount)+labour_charges+freight+others;
		 	}
		 	else{
			 	var tamount=amount;				 
		 	}
			document.getElementById("transaction_amount").value=Number(tamount).toFixed(2);	 
			document.getElementById("CGST").value=parseFloat((tamount*cgst_hidden)/100).toFixed(2);
			document.getElementById("SGST").value=parseFloat((tamount*sgst_hidden)/100).toFixed(2);
			document.getElementById("IGST").value=parseFloat((tamount*igst_hidden)/100).toFixed(2);
			document.getElementById("SCT").value=parseFloat((tamount*sct_hidden)/100).toFixed(2);		
			
			/* document.getElementById("cgst").value=parseFloat(saved_cgst+parseFloat(document.getElementById("CGST").value)).toFixed(2);
			document.getElementById("igst").value=parseFloat(saved_igst+parseFloat(document.getElementById("IGST").value)).toFixed(2);
			document.getElementById("sgst").value=parseFloat(saved_sgst+parseFloat(document.getElementById("SGST").value)).toFixed(2);
			document.getElementById("state_compansation_tax").value=parseFloat(saved_sct+parseFloat(document.getElementById("SCT").value)).toFixed(2);
			var total_cgst=document.getElementById("cgst").value;
			var total_igst=document.getElementById("igst").value;
			var total_sgst=document.getElementById("sgst").value;
			var total_sct=document.getElementById("state_compansation_tax").value;
			
			if(total_cgst!="")
				total_cgst=parseFloat(total_cgst);
			if(total_igst!="")
				total_igst=parseFloat(total_igst);
			if(total_sgst!="")
				total_sgst=parseFloat(total_sgst);
			if(total_sct!="")
				total_sct=parseFloat(total_sct);
			var gst_amount=total_cgst+total_sgst+total_igst+total_sct;
			if(saved_transaction!="")
			{
				document.getElementById("transaction_value").value=parseFloat(saved_transaction)+parseFloat(tamount);
			}
			else
				{
				document.getElementById("transaction_value").value=parseFloat(tamount).toFixed(2);
				}
			var roff=parseFloat(document.getElementById("transaction_value").value)+parseFloat(gst_amount);
			document.getElementById("round_off").value=parseFloat(roff).toFixed(2);	 */
		}
		
		function addproductdetails(){
			
			var divinfo=document.getElementById("details-data");
		

			var paytype=$("#payment_type").val();
			var check_no=$("#cheque_no").val();
			var check_date=$("#cheque_date").val();
			var bank_id=$("#bank_Id").val();
			
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
			var others=document.getElementById("others").value;
			var transaction_amount=document.getElementById("transaction_amount").value;
			var prdtNmm="";
			
			
			
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
		
			
			if(divinfo.style.display != "none"){
				
				if(productId==0){
					alert("Please Select Product");
					bValid="false";
				}
				else
					{
					<c:forEach items="${customerproductList}" var="match"> 
					
				       prdtNmm= '<c:out value="${match.product_id}" />';
				    
			 			if( prdtNmm == productId ){   	   
				   			alert("Product already present");
				   			
				   			return false;
			  		      }  
				    </c:forEach>
					}
				
			
				if(quantity.trim()=="" || quantity.trim()=="0"){
					alert("Please Enter Quantity");
					bValid="false";
				}
				else if(hsncode.trim()=="" || hsncode.trim()=="0"){
					alert("Please Enter HSN");
					bValid="false";
				}
				else if(rate.trim()=="" || rate.trim()=="0"){
					alert("Please Enter Rate");
					bValid="false";
				}
				else if(discount.trim()==""){
					alert("Please Enter Discount");
					bValid="false";
				}else if(discount >=101){
					alert("Discount should less than 100");
					bValid="false";
					return false;
				}
				else if(((SGST!=0 && SGST!="") || (CGST!=0 && CGST!="")) && (IGST!=0 && IGST!=""))
				{
				alert("Either input CGST+SGST or IGST");
				bValid="false";
				}
				if(CGST.trim()=="")
					document.getElementById("CGST").value="0";
				if(SGST.trim()=="")
					document.getElementById("SGST").value="0";
				if(IGST.trim()=="")
					document.getElementById("IGST").value="0";
				if(bValid=="true"){
					if(labourCharge.trim()==""){
						document.getElementById("labour_charges").value="0";
						labourCharge="0";
					}
					if(freightCharges.trim()==""){
						document.getElementById("freight").value="0";
						freightCharges="0";
					}
					if(others.trim()==""){
						document.getElementById("others").value="0";
						others="0";
					}
					proList.push({"productId":productId, "quantity":quantity,"unit":unit,
					"hsncode":hsncode,"rate":rate,"discount":discount,"labourCharge":labourCharge,
					"freightCharges":freightCharges,"others":others,"CGST":CGST,"SGST":SGST,
					"IGST":IGST,"SCT":SCT,"discount":discount,"transaction_amount":transaction_amount
					});
					$("#productInformationList").val(JSON.stringify(proList));
						
					$("#save_id").val(0);  
					document.getElementById("cgst").value=parseFloat(saved_cgst+parseFloat(document.getElementById("CGST").value)).toFixed(2);
					document.getElementById("igst").value=parseFloat(saved_igst+parseFloat(document.getElementById("IGST").value)).toFixed(2);
					document.getElementById("sgst").value=parseFloat(saved_sgst+parseFloat(document.getElementById("SGST").value)).toFixed(2);
					document.getElementById("state_compansation_tax").value=parseFloat(saved_sct+parseFloat(document.getElementById("SCT").value)).toFixed(2);
					var total_cgst=document.getElementById("cgst").value;
					var total_igst=document.getElementById("igst").value;
					var total_sgst=document.getElementById("sgst").value;
					var total_sct=document.getElementById("state_compansation_tax").value;
						
					if(total_cgst!="")
						total_cgst=parseFloat(total_cgst);
					if(total_igst!="")
						total_igst=parseFloat(total_igst);
					if(total_sgst!="")
						total_sgst=parseFloat(total_sgst);
					if(total_sct!="")
						total_sct=parseFloat(total_sct);
						
					var gst_amount=total_cgst+total_sgst+total_igst+total_sct;
					if(saved_transaction!=""){
						document.getElementById("transaction_value").value=parseFloat(saved_transaction)+parseFloat(transaction_amount);
					}
					else{
						document.getElementById("transaction_value").value=parseFloat(transaction_amount).toFixed(2);
					}
					var roff=parseFloat(document.getElementById("transaction_value").value)+parseFloat(gst_amount);
						document.getElementById("round_off").value=parseFloat(roff).toFixed(2); 
						document.getElementById("amount").value=parseFloat(roff).toFixed(2); 
		
					return true;
				}
				else{
					return false;
				}
			}
		}
		
		function getList(custId){
			var productArray = [];
			
			if(custId > 0){	
				
				$("#advance_payment").css('pointer-events', '');
				$("#gst_applied").css('pointer-events', '');
				$("#tds_paid").css('pointer-events', '');
				
				 $("#tds_paid").removeAttr('disabled');
				$("#subledgerInfo").hide();
				$("#customerbillInfo").show();
				<c:forEach var = "list" items = "${customerList}">
					var id = <c:out value = "${list.customer_id}"/>	
					if(id == custId){
						tdsapply = '<c:out value = "${list.tds_applicable}"/>';				
						tdsRate = '<c:out value = "${list.tds_rate}"/>';			
						custStateId = '<c:out value = "${list.state.state_id}"/>';
						$('#product_id').find('option').remove();
						$('#product_id').append($('<option>', {
						    value: 0,
						    text: 'Select Product Name'
						}));
						<c:forEach var = "product" items = "${list.product}">
							console.log("id :  ${product.product_id}   name : ${product.product_name} uom : ${product.uom.unit}");
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
							    text: productArray[i].name,
							    onclick : 'product(this)',
							})); 
						}	
					}
				</c:forEach>
				$('#customer_bill_no').find('option').remove();
				$('#customer_bill_no').append($('<option>', {
				    value: 0,
				    text: '--- Select Customer Bill No ---'
				}));
				
				$('#customer_bill_no').append($('<option>', {
				    value: -1,
				    text: 'Advance'
				}));
				
				$('#customer_bill_no').append($('<option>', {
				    value: -2,
				    text: 'Opening Balance'
				}));
				
				<c:forEach var = "salesEntry" items = "${salesEntryList}">
				var sId = '<c:out value = "${salesEntry.customer.customer_id}"/>';		
				var bill_no=0;
			 	if(transaction_id!="") {
		       		var bill_no='<c:out value= "${receipt.salesEntryId}"/>';
				}	
				 
				if(sId == custId){
					var status = '<c:out value = "${salesEntry.entry_status}"/>';				
					if((status == ${ENTRY_PENDING}) || (${salesEntry.sales_id}==bill_no)){					
						$('#customer_bill_no').append($('<option>', {
						    value: '${salesEntry.sales_id}',
						    text: '${salesEntry.voucher_no}'
						}));
					}				
				}
				</c:forEach>
			}
			else if(custId==-1){
				
				$("#advance_payment").css('pointer-events', '');
				$("#gst_applied").css('pointer-events', '');
				$("#tds_paid").css('pointer-events', '');
				/* $("#tds-amount").show();
				$("#tds_amount").val(totaltds);
			    $('#tds_amount').attr('readonly', true); */
			    $("#tds_paid").val("false");
			    $("#tds_paid").css('pointer-events', 'none');
				$("#advance_payment").val("false");
			    $("#advance_payment").css('pointer-events', 'none');
				$("#gst_applied").val(2);
				$("#gst_applied").css('pointer-events', 'none');
				$("#subledgerInfo").show();
				$("#customerbillInfo").hide();
				$("#bill-tds").hide();
				
			}
			else{
				
				$("#bill-tds").hide();
				$("#subledgerInfo").hide();
				$("#customerbillInfo").hide();
			 	$("#gst_applied").removeAttr('disabled');
			 	 $("#tds_paid").val("false");
				 $("#tds_paid").prop('disabled', 'disabled');
			}	
		}
		
		function save(){
			
			//----------------------------------
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
			
				
			if(divinfo.style.display != "none"){
			
				var prodlist="false";
				var items="${customerproductList.size()}";
				var size=items.length;
				if(size>0)
					{
					prodlist="true";
					}
			if(prodlist=="false")
			{
		    if(productId==0){
				alert("Please Select Product");
				bValid="false";
			}
			else if(quantity.trim()=="" || quantity.trim()=="0"){
				alert("Please Enter Quantity");
				bValid="false";
			}
			else if(hsncode.trim()=="" || hsncode.trim()=="0"){
				alert("Please Enter HSN");
				bValid="false";
			}
			else if(rate.trim()=="" || rate.trim()=="0"){
				alert("Please Enter Rate");
				bValid="false";
			}
			else if(discount.trim()==""){
				alert("Please Enter Discount");
				bValid="false";
			}else if(discount > 100){
				alert("Discount should less than 100");
				bValid="false";
			}	else{
				bValid="true";
			}
			}
			}
			//-------------------------
			
			if(bValid=="true")
		    {
			$("#save_id").val(1);  
			var dateString = $("#date").val();
			var gst=$("#gst_applied").val();
			
			var customer_bill_no=document.getElementById("customer_bill_no").value;
			var customer_id=document.getElementById("customer_id").value;
			
			if((transaction_id=="") && (dateString!="") && (customer_bill_no!=-1))	 {
				//alert(dateString);
				var oldDate = dateString.split("-");
				if(oldDate[1]!=undefined)
				{
					
				$("#date").val(oldDate[1]+"/"+oldDate[0]+"/"+oldDate[2]);
				}
				else
				{ 
					var oldDate = dateString.split("/");
					$("#date").val(oldDate[1]+"-"+oldDate[0]+"-"+oldDate[2]);
				}
			}
			else if((dateString!="") && (gst==2) && (transaction_id!="") ) {
				
				var oldDate = dateString.split("/");
				
				if(oldDate[1]!=undefined)
				{ 
					$("#date").val(oldDate[1]+"-"+oldDate[0]+"-"+oldDate[2]);
				    //$("#date").val(oldDate[1]+"/"+oldDate[0]+"/"+oldDate[2]);
				}
				else
				{ 
					var oldDate1 = dateString.split("-");
					$("#date").val(oldDate1[1]+"/"+oldDate1[0]+"/"+oldDate1[2]);
				}  
			}
		      else if((dateString!="") && (gst==1) && (transaction_id!="") ) {		
				var oldDate = dateString.split("/");		
				if(oldDate[1]!=undefined)
				{ 
					$("#date").val(oldDate[1]+"-"+oldDate[0]+"-"+oldDate[2]);
				    //$("#date").val(oldDate[1]+"/"+oldDate[0]+"/"+oldDate[2]);
				}
				else
				{ 
					var oldDate1 = dateString.split("-");
					$("#date").val(oldDate1[1]+"/"+oldDate1[0]+"/"+oldDate1[2]);
				}  
			}
			return true;
		   }
			else
				{
				return false;
				}
			
		}
		
		function deleteproductdetail(id){
			window.location.assign('<c:url value="deleteReceiptProduct"/>?id='+id);
		}
		
		function editproductdetail(id){
			window.location.assign('<c:url value="editproductdetailforReceipt"/>?id='+id);
		}
		
		function product(e){	
			
			<c:forEach var = "product" items = "${productList}">	
				var id = <c:out value = "${product.product_id}"/>	
				
				if(id == e){			
					var currentdate=$("#date").val();
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
										$("#SCT").val(cess);
									  	$("#sct_hidden").val(cess);
									  	
										if(custStateId != compStateId){
											$("#IGST").val(igst);  
										  	$("#igst_hidden").val(igst); 
											
											$("#CGST").val(0); 
										  	$("#cgst_hidden").val(0); 
											  
										  	$("#SGST").val(0); 
										  	$("#sgst_hidden").val(0);   
									  	}
										else{
											$("#IGST").val(0);
										  	$("#igst_hidden").val(0);
										  	
											$("#CGST").val(cgst); 
										  	$("#cgst_hidden").val(cgst); 
											  
										  	$("#SGST").val(sgst); 
										  	$("#sgst_hidden").val(sgst);  
									  	}  	   
			            		},
				            	error: function (e) {
				            		//alert(e);
				                	alert("Error while retrieving HSN/SAC");
				            	}
				        	});	
					}
					if("${product.gst_id.hsc_sac_code}"=="")
			  		{
			  		 $('#HSNCode').attr('readonly', false); 
			  		}
				  	else
				  	{
				  		 $('#HSNCode').attr('readonly', true); 
				  	}
				  	$("#HSNCode").val("${product.gst_id.hsc_sac_code}");    
				  	$("#UOM").val("${product.uom.unit}");
					$("#quantity").val("0");
				  	$("#rate").val("0");
				  	$("#labour_charges").val("0");
				  	$("#discount").val("0");
				  	$("#freight").val("0");
				  	$("#others").val("0"); 
				}
			</c:forEach>
		}
		
		$("#HSNCode").autocomplete({
			
		    source: function (request, response) {
		    	
		    	if($("#HSNCode").val().length>=4)
		    	{
		    		var inventorydate=$("#date").val();
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
			var currentdate=$("#date").val();
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
							$("#SCT").val(cess);
						  	$("#sct_hidden").val(cess);
						  	
							if(custStateId != compStateId){
								$("#IGST").val(igst);  
							  	$("#igst_hidden").val(igst); 
								
								$("#CGST").val(0); 
							  	$("#cgst_hidden").val(0); 
								  
							  	$("#SGST").val(0); 
							  	$("#sgst_hidden").val(0);   
						  	}
							else{
								$("#IGST").val(0);
							  	$("#igst_hidden").val(0);
							  	
								$("#CGST").val(cgst); 
							  	$("#cgst_hidden").val(cgst); 
								  
							  	$("#SGST").val(sgst); 
							  	$("#sgst_hidden").val(sgst);  
						  	}  	   
            		},
	            	error: function (e) {
	            		//alert(e);
	                	alert("Error while retrieving HSN/SAC");
	            	}
	        	});
		      }
		}
		function showdiv(){
			document.getElementById("details-data").style.display="block";
			if(transaction_id!="") {
				document.getElementById("table-value").style.display="block";
			}
		}
		
		function saveyearid(){
			 $('#year-model').modal('hide');
		}
		
		function checkamount() {
		
			var customer_id=document.getElementById("customer_id").value;
			var customer_bill_no=document.getElementById("customer_bill_no").value;
			var gstapplied=$("#gst_applied").val();
			if((customer_bill_no!=0) && (customer_id!=0)&& (customer_id!=-1)){
			
					if($("#date").val()=="") 
					{
						alert("Please Select Receipt Date");
						return false;
					}
						if(customer_bill_no==-1) {
					var dateString=$("#date").val();
					if(validatedate(startdate1,enddate1,dateString)==false)
					{
						
						return false;
					}
					else
					{
						var dateString = $("#date").val();
						
						if(dateString!="" && transaction_id=="") {
							var oldDate = dateString.split("-");
							$("#date").val(oldDate[1]+"/"+oldDate[0]+"/"+oldDate[2]);
						}
						else if(transaction_id!="" && gstapplied==1)
							{
							var oldDate = dateString.split("-");
							 if(oldDate[1]!=undefined)
								{
								$("#date").val(oldDate[1]+"/"+oldDate[0]+"/"+oldDate[2]);
								}
							
							}
					}
					
					
				}
				else {
				
					var dateString=$("#date").val();
					<c:forEach var = "salesEntry" items = "${salesEntryList}">
						var saleId = <c:out value = "${salesEntry.sales_id}"/>;
						if(saleId == customer_bill_no){
							var date1 = '<c:out value = "${salesEntry.created_date}"/>';
							if(validatedate(startdate1,enddate1,dateString)==false)
							{						
								return false;
							}
							else {		
									if(transaction_id==""){		
													
													var oldDate = ($("#date").val()).split("-");											
													if(oldDate[1]!=undefined)
														{							
														var date2=oldDate[2]+"/"+oldDate[1]+"/"+oldDate[0];
														}
													else
														{
														var oldDate = ($("#date").val()).split("/");							
														var date2=oldDate[1]+"-"+oldDate[0]+"-"+oldDate[2];
														}
												}
												else {						
													var oldDate = ($("#date").val()).split("/");
													var date2=oldDate[1]+"-"+oldDate[0]+"-"+oldDate[2];
												}
									
									var rdate = date2.split("/");											
									if(rdate[1]!=undefined)
										{							
										var date2=rdate[2]+"-"+rdate[1]+"-"+rdate[0];
										}
									var rdate2 = date1.split("-");											
									if(rdate2[1]!=undefined)
										{							
										date1=rdate2[2]+"-"+rdate2[1]+"-"+rdate2[0];
										}
									date1=process(date1);	
									date2=process(date2);							
									if(date1 > date2) {				
										alert("Receipt Date cann't be less than bill date");
										return false;
									}	
											
							}		
							
						}
					</c:forEach>
				}
				var outstanding=$("#outstandingAmount").html();
				var amount=document.getElementById("amount").value;
				if((amount!="") && (outstanding!="")){
					outstanding=parseFloat(Math.ceil(outstanding));
					if(transaction_id==""){
							amount=parseFloat(amount);
							 var prevamount=0;
					}
					else{
						  var prevamount='<c:out value = "${receipt.amount}"/>';
							amount=parseFloat(amount)-parseFloat(prevamount);
					}
					if(amount>outstanding){
						alert("Receipt Amount Can not be Greater than Bill Outstanding Amount");
						var dateString = $("#date").val();		
						var oldDate = dateString.split("/");
						if(oldDate[1]!=undefined)
						{

						$("#date").val(oldDate[1]+"-"+oldDate[0]+"-"+oldDate[2]);
						}
						else
							{			
							var oldDate = dateString.split("-");
							var date2=oldDate[2]+"/"+oldDate[1]+"/"+oldDate[0];
							}
						return false;
					}	
					else{
						$("#gst_applied ").removeAttr('disabled');	
						return true;
					}
				}
			}
			
			if(customer_bill_no ==-2)
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
				  		var prevamount='<c:out value = "${receipt.amount}"/>';
						amount=parseFloat(amount)-parseFloat(prevamount);
					}
					
					if(amount>outstandingAmountagainstopening){
						//alert("old outstanding amount:"+outstandingAmountagainstopening);
						alert("Payment Amount Can not be Greater than Bill opening Outstanding Amount");
						var dateString = $("#date").val();		
					
						var oldDate = dateString.split("/");
						
						if(oldDate[1]!=undefined)
						{
						$("#date").val(oldDate[1]+"-"+oldDate[0]+"-"+oldDate[2]);
						}
						else
							{			
							var oldDate = dateString.split("-");
							var date2=oldDate[2]+"/"+oldDate[1]+"/"+oldDate[0];
							}
						return false;
					}
					else{
						$("#gst_applied ").removeAttr('disabled');
						return true;
					}
				
				}
				}
			
			else
				{
				if(customer_id!="" && customer_id!=null && payment_type!="" && payment_type!=null && amount!="" &&amount!=null)
				{

					if($("#date").val()=="") {
						alert("Please Select Receipt Date");
						return false;
					}
				}	
					var dateString=$("#date").val();
		
					if(validatedate(startdate1,enddate1,dateString)==false)
					{
						
						return false;
					}
					else
						{
						return true;
						}
				}
			$("#gst_applied ").removeAttr('disabled');	
			
			return true;
			
		}
		
		function setadvanceno(e) {
			if(e=="false") {
				$("#gst_applied").val(2);							
			}
		} 
		
		function isNumber(evt) {
			evt = (evt) ? evt : window.event;
		    var charCode = (evt.which) ? evt.which : evt.keyCode;
		
		    if (charCode > 31 && ((charCode < 48) || (charCode > 57)) && charCode!=46) {
		        return false;
		    }
		    return true;
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
			$("#date").datepicker({dateFormat: 'dd-mm-yy'});
			$('#date').datepicker("option", { minDate: startdate, 
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
			 
			 month = matchArray[3]; // parse date into variables
			 day = matchArray[1];
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
		 function formatDate2(date) {
		    var d = new Date(date),
		        month = '' + (d.getMonth() + 1),
		        day = '' + d.getDate(),
		        year = d.getFullYear();
		    if (month.length < 2) month = '0' + month;
		    if (day.length < 2) day = '0' + day;	
		    return [day,month,year].join('/');
		} 
		function validatedate(startdate1,enddate1,dateString)
		{
			
			if(isValidDate(startdate1)==false){
				startdate1=formatDate(startdate1);
			 }
			if(isValidDate(enddate1)==false){
				enddate1=formatDate(enddate1);
				 }	
			
			//var dateString = $("#date").val();
			var fisrtDate = dateString.split("/");
			var secondDate = dateString.split("-");		
			
			if(fisrtDate[1]!=undefined)
				{		
				   var dateString1=formatDate2(fisrtDate);
				  dateString1 = dateString1.split("/");		
				   dateString=dateString1[0]+"-"+dateString1[1]+"-"+dateString1[2];
				 }
				if(secondDate[1]!=undefined){			
				   dateString=secondDate[0]+"-"+secondDate[1]+"-"+secondDate[2];			
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
		function showTDSAmount(tdspaid)
		{
			if(tdspaid=="true")
			{
			$("#tds-amount").show();
			$('#tds_amount').attr('readonly', false);
			}
		else
			{
			$("#tds-amount").hide();
			}
		}
		
		</script>

<style>
input, select, textarea {
	width: 50px;
}
</style>
<%@include file="/WEB-INF/includes/footer.jsp"%>