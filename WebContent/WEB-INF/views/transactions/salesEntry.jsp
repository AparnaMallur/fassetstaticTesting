<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="//code.jquery.com/jquery-1.9.1.js"></script>
<script src="//code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_TWO_HUNDRED%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THREE_HUNDRED%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_THOUSAND%></c:set>
<spring:url value="/resources/images/delete.png" var="deleteImg" />
<spring:url value="/resources/images/edit.png" var="editImg" />
<div class="breadcrumb">
	<h3>Sales Entry</h3>
	<a href="homePage">Home</a> » <a href="salesEntryList">Sales Entry</a>
	» <a href="#">Create</a>
</div>

<c:if test="${successMsg != null}">
	<div class="successMsg" id="successMsg">
		<strong>${successMsg}</strong>
	</div>
</c:if>
<div class="col-md-12 wideform">
	<div class="fassetForm">
		<form:form id="SalesEntryForm" action="saveSalesEntry" method="post"
			commandName="entry" onsubmit="return validate();">
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
										<form:radiobutton path="accounting_year_id"
											id="accounting_year_id" value="${year.year_id}"
											checked="checked"
											onclick="setdatelimit('${year.start_date}','${year.end_date}','${year.year_id}')" />${year.year_range} 
								</c:when>
									<c:otherwise>
										<form:radiobutton path="accounting_year_id"
											id="accounting_year_id" value="${year.year_id}"
											onclick="setdatelimit('${year.start_date}','${year.end_date}','${year.year_id}')" />${year.year_range} 
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
			<!-- /.modal -->
			<div class="row">
				<div class="col-md-8 col-xs-8">
					<form:input path="sales_id" type="hidden" />
					<form:input path="created_date" type="hidden" />
					<form:input path="productInformationList"
						id="productInformationList" type="hidden" />
					<form:input path="subledger_Id" id="subledger_Id" type="hidden" />
					<form:input path="save_id" id="save_id" type="hidden" />
					<form:input path="receiptId" id="receiptId" type="hidden" />
					<form:input path="againstOpeningbalanceId"
						id="againstOpeningbalanceId" type="hidden" />


					<c:if test="${entry.total_vat==null}">
						<form:input path="total_vat" id="total_vat" type="hidden"
							value="0" />
					</c:if>
					<c:if test="${entry.total_excise==null}">
						<form:input path="total_excise" id="total_excise" type="hidden"
							value="0" />
					</c:if>
					<c:if test="${entry.total_vatcst==null}">
						<form:input path="total_vatcst" id="total_vatcst" type="hidden"
							value="0" />
					</c:if>

					<c:if test="${entry.cgst==null}">
						<form:input path="cgst" id="cgst" type="hidden" value="0" />
					</c:if>
					<c:if test="${entry.sgst==null}">
						<form:input path="sgst" id="sgst" type="hidden" value="0" />
					</c:if>
					<c:if test="${entry.igst==null}">
						<form:input path="igst" id="igst" type="hidden" value="0" />
					</c:if>
					<c:if test="${entry.state_compansation_tax==null}">
						<form:input path="state_compansation_tax"
							id="state_compansation_tax" type="hidden" value="0" />
					</c:if>
					<form:input path="sale_type" id="sale_type" type="hidden" />



					<c:choose>
						<c:when test='${entry.sales_id==Null}'>
							<div class="row">
								<div class="col-md-3 control-label">
									<label>Voucher Serial No<span>*</span></label>
								</div>
								<div class="col-md-9">
									<form:select path="voucher_range" class="logInput"
										id="voucher_range">
										<form:option value="0" label="Select Range" />
										<c:forEach var="vrange" items="${voucherrange}">
											<form:option value="${vrange}">${vrange}</form:option>
										</c:forEach>
									</form:select>
									<span class="logError"><form:errors path="voucher_range" /></span>
								</div>
							</div>
						</c:when>
						<c:otherwise>
							<div class="row">
								<div class="col-md-3 control-label">
									<label>Voucher No<span>*</span></label>
								</div>
								<div class="col-md-9">
									<form:input path="voucher_no" class="logInput" id="voucher_no"
										placeholder="Voucher no" readonly="true" />
								</div>
							</div>

						</c:otherwise>
					</c:choose>
					<div class="row">
						<div class="col-md-3 control-label">
							<label>Customer Name</label><span>*</span>
						</div>
						<div class="col-md-9">
							<form:select path="customer_id" class="logInput" id="customer_id"
								placeholder="Customer Name" onChange="getList(this.value)">
								<form:option value="0" label="Select Customer Name" />
								<c:forEach var="list" items="${customerList}">
									<form:option value="${list.customer_id}">${list.firm_name}</form:option>
								</c:forEach>
								<form:option value="-1" label="Cash Sales" />
								<form:option value="-2" label="Card Sales" />

							</form:select>
							<span class="logError"><form:errors path="customer_id" /></span>
						</div>
					</div>
					<div class="row" id="bank-comp" style="display: none">
						<div class="col-md-3 control-label">
							<label>Bank<span>*</span></label>
						</div>
						<div class="col-md-9">
							<form:select path="bankId" class="logInput">
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
							<label>Income type<span>*</span></label>
						</div>
						<div class="col-md-9">
							<div id="radio_home"></div>
							<form:errors path="subledger_Id" />

						</div>
					</div>
					<div class="row">
						<div class="col-md-3 control-label">
							<label>Remark</label>
						</div>
						<div class="col-md-9">
							<form:textarea path="remark" class="logInput" id="remark"
								rows="3" placeholder="Remark (Max 250 characters)" maxlength="250"></form:textarea>
						</div>
					</div>

					<div class="row">
						<div class="col-md-3 control-label">
							<label>Entry Type<span>*</span></label>
						</div>
						<div class="col-md-9">
							<form:select path="entrytype" class="logInput" id="entryType">
								<form:option value="0" label="Select Entry Type" />
								<form:option value="1" label="Local" />
								<form:option value="2" label="Export" />
							</form:select>
							<span class="logError"><form:errors path="entrytype" /></span>
						</div>
					</div>

					<div id="exportDiv">
						<div class="row">
							<div class="col-md-3 control-label">
								<label>Shipping Bill No<span>*</span></label>
							</div>
							<div class="col-md-9">
								<form:input path="shipping_bill_no" class="logInput"
									id="shipping_bill_no" placeholder="Shipping Bill No" />
								<span class="logError"><form:errors
										path="shipping_bill_no" /></span>
							</div>
						</div>

						<div class="row">
							<div class="col-md-3 control-label">
								<label>Export Type<span>*</span></label>
							</div>
							<div class="col-md-9">
								<form:select path="export_type" class="logInput"
									id="export_type">
									<form:option value="0" label="Select Export Type" />
									<form:option value="1" label="WPAY" />
									<form:option value="2" label="WOPAY" />
								</form:select>
								<span class="logError"><form:errors path="export_type" /></span>
							</div>
						</div>
						<div class="row">
							<div class="col-md-3 control-label">
								<label>Shipping bill date<span>*</span></label>
							</div>
							<div class="col-md-9">
								<input type="text" style="color: black;" id="shipping_bill_date"
									name="shipping_bill_date" placeholder="Shipping bill date">
								<span class="logError"><form:errors
										path="shipping_bill_date" /></span>
							</div>

						</div>
						<div class="row">
							<div class="col-md-3 control-label">
								<label>Port Code<span>*</span></label>
							</div>
							<div class="col-md-9">
								<form:input path="port_code" class="logInput" id="port_code"
									placeholder="Port Code" />
								<span class="logError"><form:errors path="port_code" /></span>
							</div>
						</div>
					</div>
					<div class="row" style='display: none' id='adv_voucher'>
						<div class="col-md-3 control-label">
							<label>Receipt No</label>
						</div>
						<div class="col-md-9" id='adv_voucher_no'></div>
					</div>

					<div class="row" style='display: none' id='open_voucher'>

						<div class="col-md-3 control-label">
							<label>Sales against</label>
						</div>

						<div class="col-md-9" id='open_voucher_no'></div>

					</div>

					<div id="total-amount-calc" style="display: none">
						<div class="">
							<div class="col-md-4 control-label">
								<label>Transaction Amount:</label>
							</div>
							<div class="col-md-2">
								<fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.transaction_value}" />
								<form:input path="transaction_value" class="logInput"
									id="transaction_value" readonly="true" type="hidden" />
							</div>
							<div class="col-md-3 control-label">
								<label>Total Amount:</label>
							</div>
							<div class="col-md-3">
								<fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.round_off}" />
								<form:input path="round_off" class="logInput" id="round_off"
									readonly="true" type="hidden" />
							</div>
							<div class="col-md-4 control-label">
								<label>CGST:</label>
							</div>
							<div class="col-md-2">
								<fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.cgst}" />
							</div>
							<form:input path="cgst" class="logInput " id="cgst"
								readonly="true" type="hidden" />

							<div class="col-md-3 control-label">
								<label>SGST:</label>
							</div>
							<div class="col-md-3">
								<fmt:formatNumber type="number" minFractionDigits="2"
									maxFractionDigits="2" value="${entry.sgst}" />
							</div>
							<form:input path="sgst" class="logInput " id="sgst"
								readonly="true" type="hidden" />
						</div>

						<div class="col-md-4 control-label">
							<label>IGST:</label>
						</div>
						<div class="col-md-2">
							<fmt:formatNumber type="number" minFractionDigits="2"
								maxFractionDigits="2" value="${entry.igst}" />
						</div>
						<form:input path="igst" class="logInput " id="igst"
							readonly="true" type="hidden" />

						<div class="col-md-3 control-label">
							<label>CESS:</label>
						</div>
						<div class="col-md-3">
							<fmt:formatNumber type="number" minFractionDigits="2"
								maxFractionDigits="2" value="${entry.state_compansation_tax}" />
						</div>
						<form:input path="state_compansation_tax" class="logInput "
							id="state_compansation_tax" readonly="true" type="hidden" />

						<div class="col-md-4 control-label" style="font-weight: 20px;">
							<label>VAT:</label>
						</div>
						<div class="col-md-2">
							<fmt:formatNumber type="number" minFractionDigits="2"
								maxFractionDigits="2" value="${entry.total_vat}" />
						</div>
						<form:input path="total_vat" class="logInput " id="total_vat"
							readonly="true" type="hidden" />

						<div class="col-md-3 control-label">
							<label>CST:</label>
						</div>
						<div class="col-md-3">
							<fmt:formatNumber type="number" minFractionDigits="2"
								maxFractionDigits="2" value="${entry.total_vatcst}" />
						</div>
						<form:input path="total_vatcst" class="logInput "
							id="total_vatcst" readonly="true" type="hidden" />

						<div class="col-md-4 control-label">
							<label>EXCISE:</label>
						</div>
						<div class="col-md-2">
							<fmt:formatNumber type="number" minFractionDigits="2"
								maxFractionDigits="2" value="${entry.total_excise}" />
						</div>
						<form:input path="total_excise" class="logInput "
							id="total_excise" readonly="true" type="hidden" />

						<div class="col-md-3 control-label">
							<label>TDS Amount:</label>
						</div>
						<div class="col-md-3">
							<fmt:formatNumber type="number" minFractionDigits="2"
								maxFractionDigits="2" value="${entry.tds_amount}" />
							<form:input path="tds_amount" class="logInput " id="tds_amount"
								readonly="true" type="hidden" />
						</div>

					</div>
				</div>
				<div class="info-sidebar col-md-3 col-sm-4 " style='display: block'
					id='info-sidebr'>
					<h4 class="m-b-30 m-t-0">Advance Receipt</h4>
					<div class="info-data" id="advanceReceipt"></div>
				</div>
				<div class="info-sidebar col-md-3 col-sm-4 " style='display: block'
					id='info-sidebr1'>

					<h4 class="m-b-30 m-t-0">Opening Balance</h4>

					<div class="info-data" id="openingbalance"></div>

				</div>
			</div>






			<c:choose>
				<c:when test="${!empty customerProductList}">
					<div class="table-responsive">
						<table class="table table-bordered table-striped">
							<%-- <c:choose>
							<c:when test="${}">
							</c:when>
							<c:otherwise>
							</c:otherwise>
						</c:choose> --%>
							<thead>
								<tr>
									<th>Action</th>
									<th>Product</th>
									<th>Quantity</th>
									<th>UOM</th>
									<th>HSN/SAC Code</th>
									<th>Rate</th>
									<th>Discount</th>
									<th>Labour Charge</th>
									<th>Freight Charges</th>
									<th>Other</th>
									<th>Tax Type</th>
									<th>CGST/VAT</th>
									<th>SGST/CST</th>
									<th>IGST/EXCISE</th>
									<th>CESS</th>
	
	
								</tr>
							</thead>
							<tbody>
								<c:forEach var="prod_list" items="${customerProductList}">
									<tr>
										<td><a href="#"
											onclick="deleteproductdetail(${prod_list.sales_detail_id})"><img
												src='${deleteImg}' style="width: 20px;" /></a> <a href="#"
											onclick="editproductdetail(${prod_list.sales_detail_id})"><img
												src='${editImg}' style="width: 20px;" /></a></td>
										<td>${prod_list.product_name}</td>
										<td><fmt:formatNumber type="number" minFractionDigits="2"
												maxFractionDigits="2"  value="${prod_list.quantity}" /></td>
										<td>${prod_list.UOM}</td>
										<td>${prod_list.HSNCode}</td>
										<td><fmt:formatNumber type="number" minFractionDigits="2"
												maxFractionDigits="2" value="${prod_list.rate}" /></td>
										<td><fmt:formatNumber type="number" minFractionDigits="2"
												maxFractionDigits="2" value="${prod_list.discount}" /></td>
										<td><fmt:formatNumber type="number" minFractionDigits="2"
												maxFractionDigits="2" value="${prod_list.labour_charges}" /></td>
										<td><fmt:formatNumber type="number" minFractionDigits="2"
												maxFractionDigits="2" value="${prod_list.freight}" /></td>
										<td><fmt:formatNumber type="number" minFractionDigits="2"
												maxFractionDigits="2" value="${prod_list.others}" /></td>
	
										<c:choose>
											<c:when test="${prod_list.is_gst==2}">
												<td>VAT</td>
												<td><fmt:formatNumber type="number"
														minFractionDigits="2" maxFractionDigits="2"
														value="${prod_list.VAT}" /></td>
												<td><fmt:formatNumber type="number"
														minFractionDigits="2" maxFractionDigits="2"
														value="${prod_list.VATCST}" /></td>
												<td><fmt:formatNumber type="number"
														minFractionDigits="2" maxFractionDigits="2"
														value="${prod_list.excise}" /></td>
												<td>0</td>
											</c:when>
											<c:otherwise>
												<td>GST</td>
												<td><fmt:formatNumber type="number"
														minFractionDigits="2" maxFractionDigits="2"
														value="${prod_list.CGST}" /></td>
												<td><fmt:formatNumber type="number"
														minFractionDigits="2" maxFractionDigits="2"
														value="${prod_list.SGST}" /></td>
												<td><fmt:formatNumber type="number"
														minFractionDigits="2" maxFractionDigits="2"
														value="${prod_list.IGST}" /></td>
												<td><fmt:formatNumber type="number"
														minFractionDigits="2" maxFractionDigits="2"
														value="${prod_list.state_com_tax}" /></td>
											</c:otherwise>
										</c:choose>
	
	
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</c:when>
			</c:choose>
			
			<!-- <input type="hidden" id="conditioncheck" />
		    <input type="hidden" id="conditioncheckforcustomergstno" /> -->
			
			
			<button type="button" class="fassetBtn waves-effect waves-light"
				onclick="showdiv()">Add Product Details</button>
			<div id="details-data" style="display: none">
			<div class="table-responsive">
				<table class="table table-bordered table-striped">
					<thead>
						<tr>
							<th>Product</th>
							<th>Quantity</th>
							<th>UOM</th>
							<th class="HSN_div">HSN/SAC Code</th>
							<th>Rate</th>
							<th>Discount</th>
							<th>Labour Charge</th>
							<th>Freight Charges</th>
							<th>Other</th>
							<th class="CGST_div">CGST</th>
							<th class="SGST_div">SGST</th>
							<th class="IGST_div">IGST</th>
							<th class="SCT_div">CESS</th>
							<th class="VAT_div">VAT</th>
							<th class="VATCST_div">CST</th>
							<th class="Excise_div">EXCISE</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td><form:select path="product_id" class="logInput"
									id="product_id" placeholder="Product Name"
									onChange='product(this.value)'>
									<form:option value="0" label="Select Product Name" />
								</form:select></td>
							<td><input id="transaction_amount" type="hidden" /> <input
								class="logInput" id="cgst_hidden" placeholder="cgst_hidden"
								type="hidden" /> <input class="logInput" id="sgst_hidden"
								placeholder="sgst_hidden" type="hidden" /> <input
								class="logInput" id="igst_hidden" placeholder="igst_hidden"
								type="hidden" /> <input class="logInput" id="sct_hidden"
								placeholder="sct_hidden" type="hidden" /> <input
								class="logInput" id="vat_hidden" placeholder="vat_hidden"
								type="hidden" /> <input class="logInput" id="vatcst_hidden"
								placeholder="vatcst_hidden" type="hidden" /> <input
								class="logInput" id="excise_hidden" placeholder="excise_hidden"
								type="hidden" /> <input class="logInput numbersOnly" id="quantity"
								placeholder="Quantity" onchange="calculaterate(this.value,1)"
								maxlength="10" /></td>
							<td><input class="logInput" id="UOM" placeholder="UOM"
								readonly /></td>
							<td class="HSN_div"><input class="logInput" id="HSNCode"
								onchange="getHsnDetails(this.value)" placeholder="HSNCode" /></td>
							<td><input class="logInput numbersOnly"  id="rate" placeholder="rate"
								onchange="calculaterate(this.value,2)" maxlength="12" /></td>
							<td><input class="logInput" id="discount"
								placeholder="Discount" onchange="calculaterate(this.value,3)"
								maxlength="6" /></td>
							<td><input class="logInput" id="labour_charges"
								placeholder="Labour charges"
								onchange="calculaterate(this.value,4)" maxlength="10" /></td>
							<td><input class="logInput" id="freight"
								placeholder="Freight charges"
								onchange="calculaterate(this.value,5)" maxlength="10" /></td>
							<td><input class="logInput" id="others" placeholder="Other"
								onchange="calculaterate(this.value,6)" maxlength="10" /></td>
							<td class="CGST_div"><input id="CGST" class="logInput"
								placeholder="CGST" maxlength="20" /></td>
							<td class="SGST_div"><input id="SGST" class="logInput"
								placeholder="SGST" maxlength="20" /></td>
							<td class="IGST_div"><input id="IGST" class="logInput"
								placeholder="IGST" maxlength="20" /></td>
							<td class="SCT_div"><input id="SCT" class="logInput"
								placeholder="CESS" maxlength="20" /></td>
							<td class="VAT_div"><input id="VAT" class="logInput"
								placeholder="VAT" maxlength="20" /></td>
							<td class="VATCST_div"><input id="VATCST" class="logInput"
								placeholder="VATCST" maxlength="20" /></td>
							<td class="Excise_div"><input id="Excise" class="logInput"
								placeholder="Excise" maxlength="20" /></td>
							<input id="is_gst" class="logInput" placeholder="is_gst"
								type="hidden" />
						</tr>

					</tbody>
				</table>
				</div>
				<div class="row text-center">
					<button type="submit" class="fassetBtn waves-effect waves-light"
						onclick="return addproductdetails()">Save Product
						Details</button>
					<button class="fassetBtn" type="submit" onclick="return save()">
						Save & Back</button>

				</div>
			</div>
			<div class='row  text-center' id='details-data1'>
				<button class="fassetBtn" type="submit" onclick="return save()">
					Save & Back</button>
			</div>
		</form:form>
	</div>
</div>
<script type="text/javascript">
jQuery('.numbersOnly').keypress(function () { 
    this.value = this.value.replace(/^((?!(1-9))[0])$/g,'');
});
 jQuery('.numbersOnly').keyup(function () { 
	    this.value = this.value.replace(/^((?!(1-9))[0])$/g,'');
	});
	var supStateId;
	var compStateId;
	var proList = [];   
	var productArray = [];
	var tdsapply;
	var  tdstypeId=0;
	var tdsrate;
	var saved_cgst='<c:out value= "${entry.cgst}"/>';
	var saved_sgst='<c:out value= "${entry.sgst}"/>';
	var saved_igst='<c:out value= "${entry.igst}"/>';
	var saved_transaction='<c:out value= "${entry.transaction_value}"/>';
	var saved_sct='<c:out value= "${entry.state_compansation_tax}"/>';
	var saved_total='<c:out value= "${entry.round_off}"/>';
	var saved_vat='<c:out value= "${entry.total_vat}"/>';
	var saved_vatcst='<c:out value= "${entry.total_vatcst}"/>';
	var saved_excise='<c:out value= "${entry.total_excise}"/>';
	var tdsAmount='<c:out value= "${entry.tds_amount}"/>';
	$("#againstOpeningbalanceId").val(0);

	var openingbalanace=0;

    var yid=0;
    if(tdsAmount!="")
		tdsAmount=parseFloat(tdsAmount);
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
	var transaction_id = '<c:out value= "${entry.sales_id}"/>';	
	//vat
	var saved_vat='<c:out value= "${entry.total_vat}"/>';
	var saved_vatcst='<c:out value= "${entry.total_vatcst}"/>';
	var saved_excise='<c:out value= "${entry.total_excise}"/>';
	if(saved_vat!="")
		saved_vat=parseFloat(saved_vat);
	if(saved_vatcst!="")
		saved_vatcst=parseFloat(saved_vatcst);
	if(saved_excise!="")
		saved_excise=parseFloat(saved_excise);
	var startdate1="";
	 var enddate1="";
	$(document).ready(function() {	
		compStateId = '<c:out value = "${stateId}"/>';
		$( "#customer_bill_date" ).datepicker();
	    $( "#shipping_bill_date" ).datepicker();
	    $("#against_advance_received").val(false);
	    
		setTimeout(function() {
	        $("#successMsg").hide()
	    }, 3000);
	    
	    $("#port_code,#hsn_code,#quantity,#rate,#discount,#labour_charges,#freight,#others,#CGST,#SGST,#IGST,#SCT,#VAT,#VATCST,#Excise").keypress(function(e) {
			if (!digitsAndDotOnly(e)) {
				return false;
			}
		});
	    
	   /*  $("#bill_no").keypress(function(e) {
			if (!lettersAndDigitsAndSlashOnly(e)) {
				return false;
			}
		});
	     */
	    $("#subledger").change(function(){
			if(this.checked){
				alert = "checked"
				$("#against_advance_received").val(true);
				var test = document.getElementById("against_advance_received").value;
				
			}
		});
			
		$("#entryType").on("change", function(){
			if(this.value == 2){
				$("#exportDiv").show();
			}
			else{
				$("#exportDiv").hide();
			}
		});	    
	    
		var billDate = '<c:out value= "${entry.customer_bill_date}"/>';	
		if(billDate!=""){
		 	newbillDate=formatDate(billDate);	
			$("#customer_bill_date").datepicker("setDate", newbillDate);
			$("#customer_bill_date").datepicker("refresh");
		 	/* $('#customer_bill_date').datepicker({
		  		dateFormat: "yy-mm-dd",
			  	maxDate: new Date()
	 		}); */
		}
		
		var entryType = $("#entryType").val();
		if(entryType != 2){
			$("#exportDiv").hide();			
		}
		var transaction_id = '<c:out value= "${entry.sales_id}"/>';	

	    var shiDate = '<c:out value= "${entry.shipping_bill_date}"/>';		
		if(shiDate!=""){
		 	newbillDate=formatDate(shiDate);	
			$("#shipping_bill_date").datepicker("setDate", newbillDate);
			$("#shipping_bill_date").datepicker("refresh");
		}
		
		var supId=document.getElementById("customer_id").value;
		var sale_type=document.getElementById("sale_type").value;
		
		var csid='<c:out value= "${entry.customer_id}"/>';
		 if(sale_type!=0)
			{
			 if(sale_type==2)
				 {
					$("#bank-comp").show();
					document.getElementById("bankId").value='<c:out value= "${entry.bank.bank_id}"/>'
					   getList(-2);
					   document.getElementById("customer_id").value=-2;
				 }
			 else
				 {
					$("#bank-comp").hide();
					getList(-1);
					document.getElementById("customer_id").value=-1;

				 } 			
				 var subledger_Id='<c:out value= "${entry.subledger.subledger_Id}"/>';
				 if(subledger_Id=="")
					 {
					 subledger_Id='<c:out value= "${entry.subledger_Id}"/>';
					 }
				 document.getElementById("subledger_Id").value=subledger_Id;
				radiobtn = document.getElementById("subledger_"+subledger_Id);
				radiobtn.checked = true; 
					 
			}
		else
			{
			$("#bank-comp").hide();
				if(supId!=0){
					getList(supId);
					var subledger_Id=document.getElementById("subledger_Id").value;
					radiobtn = document.getElementById("subledger_"+subledger_Id);
					radiobtn.checked = true;
				}
			}
		
		
		var years = '<c:out value= "${yearList}"/>';	
		var ysize = '<c:out value= "${yearList.size()}"/>';	
		if((years!="")&&(transaction_id=="")){
			if(ysize!=1)
			{
		 		$('#year-model').modal({
			    	show: true,
			   	});	 		 		
		 		var j=0;
		 		<c:forEach var = "year" items = "${yearList}">
			    if(j==0)
			    	{				    
			    	 startdate1='${year.start_date}';
			    	    enddate1='${year.end_date}';
			    	    yid='${year.year_id}';
			    	}
			    j++;
			     </c:forEach>
			}		
			else
			{
				
				<c:forEach var = "year" items = "${yearList}">
				 startdate1='${year.start_date}';
		    	    enddate1='${year.end_date}';
		    	    yid='${year.year_id}';
				</c:forEach>
			}	
			$(".VATCST_div").hide();
			 $(".VAT_div").hide();
			 $(".Excise_div").hide();
		}
		if(transaction_id!="")
			{		
			document.getElementById("total-amount-calc").style.display="block";
			
			//
			   
			//
			}
	});

	function formatDate(date) {
	    var d = new Date(date),
	        month = '' + (d.getMonth() + 1),
	        day = '' + d.getDate(),
	        year = d.getFullYear();
	    if (month.length < 2) month = '0' + month;
	    if (day.length < 2) day = '0' + day;
	    return [month,day,year].join('/');
	}
	
	function showdiv(){
		/* var condition = $("#conditioncheck").val();
		var conditiongstno = $("#conditioncheckforcustomergstno").val();
		
		if(condition =="false" && conditiongstno== "false"){
			$("#CGST").val("0.00").prop("disabled",true);
			 $("#SGST").val("0.00").prop("disabled",true);
			$("#IGST").val("0.00").prop("disabled",true);
			$("#SCT").val("0.00").prop("disabled",true); 
			
		} */
			document.getElementById("details-data").style.display="block";
			document.getElementById("details-data1").style.display="none";
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
		//vat//
		var vat_hidden=document.getElementById("vat_hidden").value;
		var vatcst_hidden=document.getElementById("vatcst_hidden").value;
		var excise_hidden=document.getElementById("excise_hidden").value;
		var is_gst=document.getElementById("is_gst").value;

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
	 	
	 	/* var damount=rate*quantity;
	 	if(discount!=""){				
		 	var disamount=parseFloat(damount)-parseFloat((damount*discount)/100);		
		 	var tamount=parseFloat(disamount)+labour_charges+freight+others;
		}
	 	else{
	 		var tamount=amount;				 
	 	} */
	 	
	 	var damount=rate*quantity;

	 	if(discount!=""){				
		 	var disamount=parseFloat(damount)-parseFloat((discount));		
		 	var tamount=parseFloat(disamount)+labour_charges+freight+others;
		}
	 	else{
	 		var tamount=amount;				 
	 	}
	 	document.getElementById("transaction_amount").value=Number(tamount).toFixed(2);	
	 	

	 	
	
	 	//gst
	 	if(is_gst==1)
	 	{

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
					if(saved_transaction!=""){
						document.getElementById("transaction_value").value=parseFloat(saved_transaction)+parseFloat(tamount);
					}
					else{
						document.getElementById("transaction_value").value=parseFloat(tamount).toFixed(2);
					}
					var roff=parseFloat(document.getElementById("transaction_value").value)+parseFloat(gst_amount);
	 				document.getElementById("round_off").value=parseFloat(roff).toFixed(2); */
	 	}
	 	else
	 		{
		 		document.getElementById("VAT").value=parseFloat((tamount*vat_hidden)/100).toFixed(2);
				document.getElementById("VATCST").value=parseFloat((tamount*vatcst_hidden)/100).toFixed(2);
				document.getElementById("Excise").value=parseFloat((tamount*excise_hidden)/100).toFixed(2);
	 		}
	}
	
	function getList(custId){	
		productArray = []; 
		if((custId==-1) || (custId==-2))
		{
    		document.getElementById("info-sidebr").style.display="none";
    		document.getElementById("info-sidebr1").style.display="none";

			if(custId==-1)
				{
			document.getElementById("sale_type").value=1;
			$("#bank-comp").hide();
				}
			else if(custId==-2)
				{
			document.getElementById("sale_type").value=2;
			$("#bank-comp").show();
				}			
			
            $("#radio_home").html("");	
			<c:forEach var = "subledger" items = "${subledgerList}">
			var groupname = "${subledger.ledger.accsubgroup.subgroup_name}".replace('\'', '\'');
			
			if(groupname == 'Revenue from Operations')
				{			
					if((${subledger.status=="true"}) && (${subledger.flag=="true"}) && (${subledger.subledger_approval==3}))
					{
					var yes_button = makeRadioButton("subledger_${subledger.subledger_Id}","${subledger.subledger_Id}", "${subledger.subledger_Id}", "${subledger.subledger_name}");
					radio_home.appendChild(yes_button);
					}
		       }
			</c:forEach>
			$('#product_id').find('option').remove();
			
			$('#product_id').append($('<option>', {
			    value: 0,
			    text: 'Select Product Name'
			}));
			<c:forEach var = "product" items = "${productList}">
				if((${product.status=="true"}) && (${product.flag=="true"}) && (${product.product_approval==3})) {
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
	
		else {	
			$("#bank-comp").hide();
		
			document.getElementById("sale_type").value=0;
				<c:forEach var = "list" items = "${customerList}">
					var id = <c:out value = "${list.customer_id}"/>
					if(id == custId){
						
						supStateId = <c:out value = "${list.state.state_id}"/>;
						supgst='<c:out value = "${list.gst_applicable}"/>';
						tdsapply = '<c:out value = "${list.tds_applicable}"/>';
						//tdsrate = '<c:out value = "${list.tds_rate}"/>';
						 tdstypeId='<c:out value = "${list.tdstype.tdsType_id}"/>';
						$('#product_id').find('option').remove();
						$('#product_id').append($('<option>', {
						    value: 0,
						    text: 'Select Product Name'
						}));
						<c:forEach var = "product" items = "${list.product}">
							console.log("id :  ${product.product_id}   name : ${product.product_name} uom : ${product.uom.unit}");
							
							if((${product.status=="true"}) && (${product.flag=="true"}) && (${product.product_approval==3}))
							{
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
		                $("#radio_home").html("");				
						
						<c:forEach var = "subledger" items = "${list.subLedgers}">
						if((${subledger.status=="true"}) && (${subledger.flag=="true"}) && (${subledger.subledger_approval==3}))
						{
						var yes_button = makeRadioButton("subledger_${subledger.subledger_Id}","${subledger.subledger_Id}", "${subledger.subledger_Id}", "${subledger.subledger_name}");
						radio_home.appendChild(yes_button);
						}
						</c:forEach>
					}
				</c:forEach>
				
			
				
						$.ajax({
					        type: "POST",
					        url: "getAdvanceReceipt?id=" + custId+'&yid='+yid,  	        
					        success: function (data) {
					        	
					        	if(data!=""){
					        		document.getElementById("info-sidebr").style.display="block";
					        		document.getElementById("info-sidebr1").style.display="block";
				    	 		}   
					        	else
					        		{
					        		document.getElementById("info-sidebr").style.display="none";
					        		}
					        	$('#advanceReceipt').html("");
				        	 	$.each(data, function (index, receipt) {
				               		$('#advanceReceipt').append('<p class="font-600 m-b-5">'+receipt.voucher_no+' - '+parseFloat(receipt.amount).toFixed(2)+'Rs.<span class="text-primary pull-left"><i id="add-ic'+receipt.receipt_id+'" class="fa fa-plus btn-plus add-ic" onclick=setAdvanceReceipt('+receipt.receipt_id+','+'"'+receipt.voucher_no+'",1)></i><i id="min-ic'+receipt.receipt_id+'" class="fa fa-minus btn-minus min-ic" onclick=setAdvanceReceipt('+receipt.receipt_id+','+'"'+receipt.voucher_no+'",2)></i></span></p>');	                         
				             	});
					        },
					        error: function (e) {
					            console.log("ERROR: ", e);	            
					        },
					        done: function (e) {	            
					            console.log("DONE");
					        }
					    });
						
						 $(function () {
							
						        $("#customer_id").change(function () {
						            if ($(this).val() != "0") {
						                $("#info-sidebr1").show();
						            } else {
						                $("#info-sidebr1").hide();
						            }
						        });
						    });
						 //getopeningBalance (id=openingbalance)
					 	$.ajax({

					        type: 'GET',

					        url: 'getOpeningBalanceforsales?customerid='+custId,

					        async:false,

				            contentType: 'application/json',

						      	success: function (data){ 

						      		

						      		openingbalanace=data;

						      		

						      		$('#openingbalance').html("");

					        	 	/* $.each(data, function (index) { */

					               	///	$('#openingbalance').append('<p class=" btn-plus fa fa-plus  add-ic font-600 m-b-5">'+"Opening balance"+' - '+openingbalanace+' </p>');

		$('#openingbalance').append('<p class="font-600 m-b-5"><span class="text-primary pull-left"><i id="add-ic-open" class="fa fa-plus btn-plus add-ic" onclick=setOpeningSales(\"openingbalance\",'+'"'+openingbalanace+'",1)></i><i id="minus-ic-open" class="fa fa-minus btn-minus min-ic" onclick=setOpeningSales(\"openingbalance\",'+'"'+openingbalanace+'",0)></i></span> Opening balance - '+openingbalanace+'</p>');

            /*  	});	 */

									    

						      		if(data!=""){

						      			//alert("false");

			        		document.getElementById("info-sidebr1").style.display="block";

		    	 		} 

			        	else

			        		{

			        		//alert("true");

			        		 document.getElementById("info-sidebr1").style.display="none";

			        		}

			        	/* $('#openingbalance').html("");

		        	 	$.each(data, function (index) {

		               		$('#openingbalance').append('<p>'+"Opening balance"+' - '+openingbalanace+' class="fa fa-plus btn-plus add-ic"</p>');	                         

		             	});

						     */  	},

						        error: function (e) {

						            console.log("ERROR: ", e);

						        },

						        done: function (e) {	            

						            console.log("DONE");

						        }

					    });

				
		}
							
	}
	
					function setOpeningSales(openingbalance,openingbalanace,flag)
					{
						 var cols = document.getElementsByClassName('minus-ic-open');

				         for(i=0; i<cols.length; i++) {

				        	 cols[i].style.display="none";

				         }

				         var cols = document.getElementsByClassName('add-ic-open');

				         for(i=0; i<cols.length; i++) {

				        	 cols[i].style.display="block";

				         }

				         

				         if(flag==1)

				        	 {

				        		document.getElementById('adv_voucher').style.display="none";

				        		document.getElementById('open_voucher').style.display="block";

								$("#open_voucher_no").html(openingbalance);

				        		document.getElementById('minus-ic-open').style.display="block";


				        	 document.getElementById('add-ic-open').style.display="none";

								alert("Opening Balance Reference Added");

								$("#againstOpeningbalanceId").val(1);	

								

				        	 }

				         else

				        	 {

				        		document.getElementById('adv_voucher').style.display="none";

				        	 document.getElementById('open_voucher').style.display="none";

								$("#open_voucher_no").html(openingbalance);

				        		document.getElementById('minus-ic-open').style.display="none";

								

				           	 document.getElementById('add-ic-open').style.display="block";

				         	alert("Opening Balance Reference Removed");

				         	$("#againstOpeningbalanceId").val(0);

				        	 }

					}

	function setAdvanceReceipt(receiptId,voucher_no,flag){
		 var cols = document.getElementsByClassName('min-ic');
         for(i=0; i<cols.length; i++) {
        	 cols[i].style.display="none";
         }
         var cols = document.getElementsByClassName('add-ic');
         for(i=0; i<cols.length; i++) {
        	 cols[i].style.display="block";
         }
		if(flag==1)
		{
			document.getElementById('adv_voucher').style.display="block";
			$("#adv_voucher_no").html(voucher_no);
			$("#receiptId").val(receiptId);
			document.getElementById('open_voucher').style.display="none";
			document.getElementById('min-ic'+receiptId).style.display="block";
			document.getElementById('add-ic'+receiptId).style.display="none";
			alert("Receipt Reference Added");
		}
		else
			{
			$("#receiptId").val("");
			alert("Receipt Reference Removed");		
			document.getElementById('open_voucher').style.display="none";
			document.getElementById('add-ic'+receiptId).style.display="block";
			document.getElementById('min-ic'+receiptId).style.display="none";
			document.getElementById('adv_voucher').style.display="none";
			$("#adv_voucher_no").html("");
			
			}
	}

	function product(e){
		var cgst=0;
		var sgst=0;
		var igst=0;
		var cess=0;
		
		<c:forEach var = "product" items = "${productList}">
			var id = <c:out value = "${product.product_id}"/>				
			var custid=$("#customer_id").val();
			if(id == e){	
				var date = new Date();
				var day = date.getDate()
				var month = date.getMonth() + 1;
				var year = date.getFullYear();
				var currentdate=year+"-"+month+"-"+day;
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
				
				$("#is_gst").val("${product.tax_type}"); 
				if(${product.tax_type==1})
				{
				$.ajax({
	           		type: "GET",
	            		url: '<c:url value="getHSNSACByDate?date="/>'+currentdate+'&hsn='+hsn,                     		
	            		cache: false, 
	            		aysnc:false,
	            		success: function (data) {
	            			console.log("success");
	            			console.log(data["cgst"]);
	            			console.log("cust id is");
	            			console.log(custid);
	            			cgst=data["cgst"];
	            			sgst=data["sgst"];
	            			igst=data["igst"];
	            			cess=data["state_comp_cess"];  
	            		 	$("#SCT").val(cess);
	        			  	$("#sct_hidden").val(cess); 
	        			  	if((custid==-1) || (custid==-2))
	        			  		{
	        			  		console.log("company gst !-1");
	        			  		$("#CGST").val(cgst); 
	        				  	$("#cgst_hidden").val(cgst); 
	        					  
	        				  	$("#SGST").val(sgst); 
	        				  	$("#sgst_hidden").val(sgst); 
	        				  	
	        				  	$("#IGST").val(igst);  
	        				  	$("#igst_hidden").val(igst); 
	        			  		}
	        			  	else
	        			  		{			console.log("company gst no");
	        			  				  	if("${company.gst_no}"!=null && "${company.gst_no}"!=""){
	        			  				  	console.log("company gst not null");
	        			  				  console.log("supStateId");
	        			  				 console.log(supStateId);
	        			  				console.log("compStateId");
	        			  				console.log(compStateId);
	        									if(supStateId != compStateId){
	        									$("#IGST").val(igst);  
	        								  	$("#igst_hidden").val(igst); 
	        									
	        									$("#CGST").val(0); 
	        								  	$("#cgst_hidden").val(0); 
	        									  
	        								  	$("#SGST").val(0); 
	        								  	$("#sgst_hidden").val(0);  
	        							  	}
	        								else{
	        									console.log("company gst not null else");
	        									$("#IGST").val(0);
	        								  	$("#igst_hidden").val(0);
	        								  	
	        									$("#CGST").val(cgst); 
	        								  	$("#cgst_hidden").val(cgst); 
	        									  
	        								  	$("#SGST").val(sgst); 
	        								  	$("#sgst_hidden").val(sgst);  
	        							  	}
	        						  	}
	        						  	else{
	        								$("#CGST").val(0); 
	        							  	$("#cgst_hidden").val(0); 
	        								  
	        							  	$("#SGST").val(0); 
	        							  	$("#sgst_hidden").val(0);  
	        							  	
	        								$("#IGST").val(0);
	        							  	$("#igst_hidden").val(0);
	        							  	
	        							 	$("#SCT").val(0);
	        							  	$("#sct_hidden").val(0);
	        						  	}
	        			  	}
	        		},
	            	error: function (e) {
	            		//alert(e);
	                	alert("Error while retrieving HSN/SAC");
	            	}
	        	});	     
				
		
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
				  	$("#VAT").val(0); 
		  			$("#VATCST").val(0); 
		  			$("#Excise").val(0); 
		  			 $(".VATCST_div").hide();
		  			 $(".VAT_div").hide();
		  			 $(".Excise_div").hide();
		  			 $(".IGST_div").show();
		  			 $(".SGST_div").show();
		  			 $(".CGST_div").show();
		  			 $(".SCT_div").show();
		  			 $(".HSN_div").show();
				}
				else
					{
						$("#VATCST").val("${product.taxMaster.cst}"); 
			  			$("#vatcst_hidden").val("${product.taxMaster.cst}"); 
			  			$("#VAT").val("${product.taxMaster.vat}"); 
			  			$("#vat_hidden").val("${product.taxMaster.vat}"); 
			  			$("#excise_hidden").val("${product.taxMaster.excise}"); 
			  			$("#Excise").val("${product.taxMaster.excise}"); 
						$("#IGST").val(0);
						$("#CGST").val(0);
						$("#SGST").val(0);
						$("#HSNCode").val(0);  
						$("#UOM").val("${product.uom.unit}");
					 	$("#SCT").val(0);
			  			 $(".IGST_div").hide();
			  			 $(".SGST_div").hide();
			  			 $(".CGST_div").hide();
			  			 $(".SCT_div").hide();
			  			 $(".HSN_div").hide();
						 $(".VATCST_div").show();
						 $(".VAT_div").show();
						 $(".Excise_div").show();
							$("#quantity").val("0");
						  	$("#rate").val("0");
						  	$("#labour_charges").val("0");
						  	$("#discount").val("0");
						  	$("#freight").val("0");
						  	$("#others").val("0");
						  						 
					}
			}
		</c:forEach>
	}
	
	var date = new Date();
	var day = date.getDate()
	var month = date.getMonth()+1;
	var year = date.getFullYear();
	var inventorydate=year+"-"+month+"-"+day;
	
	$("#HSNCode").autocomplete({
		
	    source: function (request, response) {
	    	
	    	if($("#HSNCode").val().length>=4)
	    	{
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
		var custid=$("#customer_id").val();
		var date = new Date();
		var day = date.getDate()
		var month = date.getMonth() + 1;
		var year = date.getFullYear();
		var currentdate=year+"-"+month+"-"+day;
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
    			  	if((custid==-1) || (custid==-2))
    			  		{
    			  		$("#CGST").val(cgst); 
    				  	$("#cgst_hidden").val(cgst); 
    					  
    				  	$("#SGST").val(sgst); 
    				  	$("#sgst_hidden").val(sgst); 
    				  	
    				  	$("#IGST").val(igst);  
    				  	$("#igst_hidden").val(igst); 
    			  		}
    			  	else
    			  		{			
    			  		            if("${company.gst_no}"!=null && "${company.gst_no}"!="")
    			  		            {
    							
    									if(supStateId != compStateId){
    										console.log("supStateId");
    									$("#IGST").val(igst);  
    								  	$("#igst_hidden").val(igst); 
    									
    									$("#CGST").val(0); 
    								  	$("#cgst_hidden").val(0); 
    									  
    								  	$("#SGST").val(0); 
    								  	$("#sgst_hidden").val(0);  
    							  	}
    								else{
    									console.log("else supStateId");
    									$("#IGST").val(0);
    								  	$("#igst_hidden").val(0);
    								  	
    									$("#CGST").val(cgst); 
    								  	$("#cgst_hidden").val(cgst); 
    									  
    								  	$("#SGST").val(sgst); 
    								  	$("#sgst_hidden").val(sgst);  
    							  	}
    						  	}
    						  	else{
    								$("#CGST").val(0); 
    							  	$("#cgst_hidden").val(0); 
    								  
    							  	$("#SGST").val(0); 
    							  	$("#sgst_hidden").val(0);  
    							  	
    								$("#IGST").val(0);
    							  	$("#igst_hidden").val(0);
    							  	
    							 	$("#SCT").val(0);
    							  	$("#sct_hidden").val(0);
    						  	}
    			  	}
    		},
        	error: function (e) {
            	alert("Error while retrieving HSN/SAC");
        	}
    	});
	}
	
	function addproductdetails(){	
		  
		var productNm = "";

		var voucherNo=$("#voucher_range").val();
		var customerName=$("#customer_id").val();
		var incomeType=$("#subledger_Id").val(); 
		var entryType=$("#entryType").val();
		
		var againstAdvance1=$("#receiptId").val();
		var againstAdvance2='<c:out value = "${entry.against_advance_receipt}"/>';
		
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
		var tdsrate=0;
		var VAT=document.getElementById("VAT").value;
		var VATCST=document.getElementById("VATCST").value;
		var Excise=document.getElementById("Excise").value;
		var is_gst=document.getElementById("is_gst").value;
		
		
		var damount=rate*quantity;
		for(i=0;i<productArray.length;i++) {
			if(productArray[i].id == productId){
				productNm = productArray[i].name;
			}
		}
		<c:forEach items="${customerProductList}" var="match"> 
	    prdtNmm= '<c:out value="${match.product_id}" />';	
 			if( prdtNmm == productId ){   	   
	   			alert("Product already present");
	   			bValid="false";
	   			return false;
  		}  
	</c:forEach> 
		
		if(voucherNo == "" || voucherNo == 0){
			alert("Select Voucher Range")
			return false;
		}
		else if(customerName == "" || customerName == 0){
			alert("Select Customer Name");
			return false;
		}
		else if(incomeType == "" || incomeType == 0){
			alert("Select Income Type");
			return false;
		}
		else if(entryType == "" || entryType == 0){
			alert("Select Entry Type");
			return false;
		}
		else if(productId==0){
			alert("Please Select Product");
			bValid="false";
			
		}
		
		
		
		
		else if(quantity.trim()=="" || quantity.trim()=="0"){
			alert("Please Enter Quantity");
			bValid="false";
		}
		else if(rate.trim()=="" || rate.trim()=="0"){
			alert("Please Enter Rate");
			bValid="false";
		}
		else if(discount.trim()==""){
			alert("Please Enter Discount");
			bValid="false";
			
		}else if(discount > damount){
			alert("Discount cannot be greater than amount");
			bValid="false";
		}
		
		
		if(is_gst==1)
		{
			 if(hsncode.trim()=="" || hsncode.trim()=="0"){
				alert("Please Enter HSN");
				bValid="false";
			}
			 if(((SGST!=0) || (CGST!=0 )) && (IGST!=0))
				{
				alert("Either input CGST+SGST or IGST");
				bValid="false";
				}
	
		}
		
		
		
		if(VAT.trim()=="")
			document.getElementById("VAT").value="0";
		if(VATCST.trim()=="")
			document.getElementById("VATCST").value="0";
		if(Excise.trim()=="")
			document.getElementById("Excise").value="0";	 
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
				"IGST":IGST,"SCT":SCT,"discount":discount,"transaction_amount":transaction_amount,"is_gst":is_gst,"VAT":VAT,"VATCST":VATCST,"Excise":Excise
			});
			$("#productInformationList").val(JSON.stringify(proList));
			$("#save_id").val(0);  
			
					document.getElementById("cgst").value=parseFloat(saved_cgst+parseFloat(document.getElementById("CGST").value)).toFixed(2);
					document.getElementById("igst").value=parseFloat(saved_igst+parseFloat(document.getElementById("IGST").value)).toFixed(2);
					document.getElementById("sgst").value=parseFloat(saved_sgst+parseFloat(document.getElementById("SGST").value)).toFixed(2);
					document.getElementById("state_compansation_tax").value=parseFloat(saved_sct+parseFloat(document.getElementById("SCT").value)).toFixed(2);
					document.getElementById("total_vat").value=parseFloat(saved_vat+parseFloat(document.getElementById("VAT").value)).toFixed(2);
					document.getElementById("total_vatcst").value=parseFloat(saved_vatcst+parseFloat(document.getElementById("VATCST").value)).toFixed(2);
					document.getElementById("total_excise").value=parseFloat(saved_excise+parseFloat(document.getElementById("Excise").value)).toFixed(2);
					
					var total_cgst=document.getElementById("cgst").value;
					var total_igst=document.getElementById("igst").value;
					var total_sgst=document.getElementById("sgst").value;
					var total_sct=document.getElementById("state_compansation_tax").value;
					var total_vat=document.getElementById("total_vat").value;
					var total_vatcst=document.getElementById("total_vatcst").value;
					var total_excise=document.getElementById("total_excise").value;
					
					if(total_cgst!="")
						total_cgst=parseFloat(total_cgst);
					if(total_igst!="")
						total_igst=parseFloat(total_igst);
					if(total_sgst!="")
						total_sgst=parseFloat(total_sgst);
					if(total_sct!="")
						total_sct=parseFloat(total_sct);
					if(total_vat!="")
						total_vat=parseFloat(total_vat);
					if(total_vatcst!="")
						total_vatcst=parseFloat(total_vatcst);
					if(total_excise!="")
						total_excise=parseFloat(total_excise);
					
					var gst_amount=total_cgst+total_sgst+total_igst+total_sct+total_vat+total_vatcst+total_excise;
					if(saved_transaction!=""){
						document.getElementById("transaction_value").value=parseFloat(saved_transaction)+parseFloat(transaction_amount);
					}
					else{
						document.getElementById("transaction_value").value=parseFloat(transaction_amount).toFixed(2);
					}
					var roff=parseFloat(document.getElementById("transaction_value").value)+parseFloat(gst_amount);
						document.getElementById("round_off").value=parseFloat(roff).toFixed(2); 
			
				if(tdsAmount!="")
				{
				tdsAmount=parseFloat(tdsAmount);				
				}
			else
				{
				tdsAmount=0;
				}
				alert("tdsamount "+tdsAmount);
			if(tdsapply==1)
				{
				
				$.ajax({
		       		type: "GET",
		        		url: '<c:url value="getTDSRateById?tdsTypeId="/>'+tdstypeId,           
							
		        		cache: false, 
		        		async:false,
		        		success: function (data) {
		        			alert(data);
		        			tdsrate=data;
		        			
								  	
		        			
		        		},
		            	error: function (e) {
		            		console.log("ERROR: ", e);
		                	alert("Error while retrieving HSN/SAC");
		            	},
		            	 done: function (e) {
		 		            console.log("DONE");
		 		        }        
		        	});	
				//alert("after func call "+tdsrate);
				
				if((againstAdvance1=="" && againstAdvance2=="")||(againstAdvance2=='false'))
				{
					//alert("tans amount is "+transaction_amount);
					//alert("tdsrate is "+tdsrate);
				var td=parseFloat((transaction_amount*tdsrate)/100).toFixed(2);
				
					tdsAmount = tdsAmount+parseFloat(td);
				}
				else
					{
					tdsAmount=0;
					}
				
				}	
			
			document.getElementById("round_off").value=parseFloat(roff-tdsAmount).toFixed(2);
			document.getElementById("tds_amount").value=parseFloat(tdsAmount).toFixed(2);
			
			//$("#save_id").val(0); 
			

			var againstOpeningBalnce = '<c:out value = "${entry.againstOpeningBalnce}"/>';

			

			if($("#againstOpeningbalanceId").val()==1 || againstOpeningBalnce=='true')

			{
			/* 	if(parseFloat(round_off_amoumt-total_tds_amoumt).toFixed(2)>openingbalanace)
				 */	
				if(parseFloat(roff-tdsAmount).toFixed(2)>openingbalanace)

					{

					alert("Total amount should be less than or equal to opening balance");

					return false;

					}

				else

					{

					return true;

					}

			}

			else

				{

				return true;

				}

			}
			 else{
				return false;
			} 
	
				}
		/*  */
	
	function getTdsrate(id){
			
		$.ajax({
       		type: "GET",
        		url: '<c:url value="getTDSRateById?tdsTypeId="/>'+id,           
					
        		cache: false, 
        		aysnc:false,
        		success: function (data) {
        			//alert(data);
        			//tdsrate=data;
        			
					return data	  	
        			
        		},
            	error: function (e) {
            		console.log("ERROR: ", e);
                	alert("Error while retrieving HSN/SAC");
            	},
            	 done: function (e) {
 		            console.log("DONE");
 		        }        
        	});	
		}
	function makeRadioButton(ID,name, value, text) {
	    var label = document.createElement("label");
	    var radio = document.createElement("input");
	    radio.type = "radio";
	    radio.name = "subledger_Id";
	    radio.path="subledger_Id"
	    radio.id = ID;
	    radio.value = value;
	    radio.setAttribute("onclick","radiocheck("+radio.value+")");    
	    label.appendChild(radio);
	    label.appendChild(document.createTextNode(text));
	    return label;
  	}
	
	function radiocheck(e){
		document.getElementById("subledger_Id").value=e;
	}
	
	
	function cancel(){
		window.location.assign('<c:url value = "salesEntryList"/>');	
	}
	
	function save(){
		$("#save_id").val(1);  
		
		var round_off_amoumt = $("#round_off").val();

		var total_tds_amoumt = $("#tds_amount").val();

		var againstOpeningBalnce = '<c:out value = "${entry.againstOpeningBalnce}"/>';
		
		if($("#againstOpeningbalanceId").val()==1 || againstOpeningBalnce=='true')

		{

			if(parseFloat(round_off_amoumt-total_tds_amoumt).toFixed(2)>openingbalanace)

				{
				alert("Total amount should be less than or equal to opening balance");
				return false;
				}

			else

				{
				return true;
				}
		}
		else

			{

			return true;

			}
	}
	
	function deleteproductdetail(id){
		window.location.assign('<c:url value="deleteSalesEntryProduct"/>?id='+id);
	}
	
	function editproductdetail(id){
		window.location.assign('<c:url value="editproductdetailforSalesEntry"/>?id='+id);
	}
	
	function saveyearid(){
		$('#year-model').modal('hide');		 
	}
	
	function validate()
	
	{   
		var productId=document.getElementById("product_id").value;
		var currentTime = new Date().toISOString().slice(0,10);	
		var flag =  true;
		var dateStringnew=process(currentTime);		
		var startdatenew=process(startdate1);
		var enddatenew=process(enddate1);
		
		if(dateStringnew<startdatenew)
		  {			
			alert("You Can not add sales entry in selected year as start Date is: "+startdate1);
			flag=false;
			return flag;
		  }
		  else if(dateStringnew>enddatenew)
		  {			  
			  alert("You Can not add sales entry in selected year as end Date is: "+enddate1);
			  flag=false;
			  return flag;
		  }		  
		if(flag==true)
		{
				var prod=$("#productInformationList").val();	 
				var voucherNo=$("#voucher_range").val();
				var customerName=$("#customer_id").val();
				var incomeType=$("#subledger_Id").val(); 
				var entryType=$("#entryType").val();
				
				if(voucherNo == "" || voucherNo == 0){
					alert("Select Voucher Range")
					return false;
				}
				else if(customerName == "" || customerName == 0){
					alert("Select Customer Name");
					return false;
				}
				else if(incomeType == "" || incomeType == 0){
					alert("Select Income Type");
					return false;
				}
				else if(entryType == "" || entryType == 0){
					alert("Select Entry Type");
					return false;
				}
				/* else if(productId == 0){
					alert(select product);
				} */
				else if(transaction_id=="")
					{
						if((prod==null) || (prod==""))
							
							{
							alert("Please Add Product Details");
							return false;
							}
						}
				
				
				else			
					{
					return true;
					}
				
		}
		else
		{
			return false;
		}
		
	}
	function setdatelimit(startdate,enddate,year_id)
	{    	
		yid=year_id;
    	startdate1=startdate;
    	enddate1=enddate;		
	}
	function process(date){
		
	   var parts = date.split("-");
	   var date = new Date(parts[0] + "/" + parts[1] + "/" + parts[2]);	  
	   return date.getTime();
	}
	
	
	
/* function getcustomergst(id){
		
		
		$.ajax({
	   		type: "GET", 
	    		url: '<c:url value="getGstNumberForCustomer?id="/>'+id,                  		
	    		success: function (data) {
	    			//alert(data);
	    			
	    		 if(data == false){
	    				 $("#conditioncheckforcustomergstno").val("false");
	    			}else{
	    				//alert("success");
	    		 	}  
	    		  },
	    	  error: function (e) {
	    					//alert(e);
	        			alert("GST number not found.");
	    	  }
		});
		
		
	} */
	
	
</script>
<style>
#radio_home label {
	display: ruby
}
</style>

<%@include file="/WEB-INF/includes/footer.jsp"%>