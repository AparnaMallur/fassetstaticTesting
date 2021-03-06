<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/images/closeRed.png" var="deleteImg" />
<spring:url value="/resources/images/edit.png" var="editImg" />
<link rel="stylesheet" href="//code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="//code.jquery.com/jquery-1.9.1.js"></script>
<script src="//code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<script type="text/javascript" src="${valid}"></script>
<c:if test="${successMsg != null}">
	<div class="successMsg" id="successMsg">
		<strong>${successMsg}</strong>
	</div>
</c:if>
<div class="breadcrumb">
	<h3>Debit Note</h3>
	<a href="homePage">Home</a> ? <a href="debitNoteList">Debit Note</a> ? <a href="#">Create</a>
</div>
<div class="col-md-12 wideform">
	<div class="fassetForm">
		<form:form id="debitNoteForm" action="debitNote" method="post" commandName="debitNote" onsubmit = "return validate();">
				<form:input path="debitNoteDetails" id = "debitNoteDetails" hidden = "hidden"/>	
		      <!-- Small modal -->
                  <div id="year-model" data-backdrop="static" data-keyboard="false" class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
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
														<form:radiobutton path="yearId" value="${year.year_id}" checked="checked" onclick="setdatelimit('${year.start_date}','${year.end_date}')"/>${year.year_range} 
												</c:when>
												<c:otherwise>
														<form:radiobutton path="yearId" value="${year.year_id}" onclick="setdatelimit('${year.start_date}','${year.end_date}')"/>${year.year_range} 
												</c:otherwise>
												</c:choose>
												<c:set var="first_year" value="${first_year+1}" />
							
							
										</c:forEach>
				                </div>
				                <div class="modal-footer">
				                               <button type="button" class="btn btn-primary waves-effect waves-light" onclick='saveyearid()'>Save Year</button>
				                </div>
                           </div><!-- /.modal-content -->
                      </div>
                </div>
            <!-- /.modal -->  
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
				<form:input path="debit_no_id" hidden="hidden" />
			</div>
			<div class="row">
				<div class="col-md-3 control-label">
					<label>Date<span>*</span></label>
				</div>
				<div class="col-md-9">
					<form:input path="dateString" type="text" style="color: black;" id="debitNoteDate" placeholder="Date" autocomplete="off" onchange="dateRestriction()" onclick="setDatePicker()"/>
					<span class="logError"><form:errors path="dateString" /></span>
				</div>
			</div>
			<div class="row">
				<div class="col-md-3 control-label">
					<label>Supplier<span>*</span></label>
				</div>
				<div class="col-md-9">
					<form:select path="supplierId" class="logInput" id='supplierId' onChange="setPurchaseBillNumber(this.value)">
						<form:option value="0" label="--- Select Supplier ---" />
						<c:forEach var="supplier" items="${supplierList}">
							<form:option value="${supplier.supplier_id}" >${supplier.company_name}</form:option>
						</c:forEach>
					</form:select>
					<span class="logError"><form:errors path="supplierId" /></span>
				</div>
			</div>
			<div class="row">
			
			</div>
			<div class="row">
				<div class="col-md-3 control-label">
					<label>Purchase Bill Number<span>*</span></label>
				</div>
				<div class="col-md-9">
					<form:select path="purchaseEntryId" class="logInput" id="purchaseEntryId"  onClick= "getProducts(this.value)">
						<form:option value="0">--- Select Purchase Bill Number---</form:option>
					</form:select>
					<span class="logError"><form:errors path="purchaseEntryId" /></span>
				</div>
			</div>
			<div class="row">
				<div class="col-md-3 control-label">
					<label>Description<span>*</span></label>
				</div>
				<div class="col-md-9">
				<form:select path="description" class="logInput" name="type" id="type">
						<form:option value="0" label="Select Description"/>
						<form:option value="1" label="Purchase return"/>
						<form:option value="2" label="Post sale discount"/>
						<form:option value="3" label="Deficiency in services"/>
						<form:option value="4" label="Correction in invoices"/>	
						<form:option value="5" label="Change in POS"/>	
						<form:option value="6" label="Finalization of provisional assessment"/>	
						<form:option value="7" label="Other"/>										
					</form:select>
				<span class="logError"><form:errors path="description" /></span>
				</div>
			</div>			
		<div class="row"  id="other-remark" style="display:none">
				<div class="col-md-3 control-label">
					<label>Remark</label>
				</div>
				<div class="col-md-9">
				<form:textarea path="remark" class="logInput" id = "remark" rows="3"  placeholder="Remark" maxlength="250"></form:textarea>
					<span class="logError"><form:errors path="remark" /></span>
				</div>
			</div> 
			<div class="row">
				<div class="col-md-3 control-label">
					<label>SubLedger<span>*</span></label>
				</div>
				<div class="col-md-9">
					<form:select path="subledgerId" class="logInput" id="subledgerId" >
						<form:option value="0">--- Select SubLedger ---</form:option>
					</form:select>
					<span class="logError"><form:errors path="subledgerId" /></span>
				</div>
			</div>
		
			<div class="row" id="amount1">
				<div class="col-md-3 control-label">
					<label>Amount<span>*</span></label>
				</div>
				<div class="col-md-9">
				   <fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" value="${debitNote.round_off}" var="values"/>
					<form:input path="round_off" class="logInput"
						placeholder="Amount Paid" id="round_off_amount"  value="${values}" 	maxlength="18"/>
					<span class="logError"><form:errors path="round_off" /></span>

				</div>
			</div>
			<%-- <div class="row">
				<div class="col-md-3 control-label">
					<label>Status<span>*</span></label>
				</div>
				<div class="col-md-9">
					<form:radiobutton path="status" value="true" checked="checked" />Enable
					<form:radiobutton path="status" value="false" />Disable
				</div>
			</div> --%>
			<div id="productDetails">
				<div class='row'>
					<table id='table-value' style="display:none">
						<tr>
							<td colspan="3">
								<label>Transaction Amount</label>
								<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" value="${debitNote.transaction_value_head}" var="no_formattedtransaction_value_head"/> 
								<form:input path="transaction_value_head" class="logInput hlable" id="transaction_value" readonly="true" style="background-color: transparent;" value="${no_formattedtransaction_value_head}"/>
							</td>
							<td colspan="2">
								<label>CGST</label>
								<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" value="${debitNote.CGST_head}" var="no_formattedCGST_head"/> 
								<form:input path="CGST_head" class="logInput hlable" id="cgst" readonly="true" style="background-color: transparent;" value="${no_formattedCGST_head}"/>
							</td>
							<td colspan="2">
								<label>SGST</label>
								<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" value="${debitNote.SGST_head}" var="no_formattedSGST_head"/> 
								<form:input path="SGST_head" class="logInput hlable" id="sgst" readonly="true" style="background-color: transparent;" value="${no_formattedSGST_head}"/>
							</td>
							<td colspan="2">
								<label>IGST</label>
								<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" value="${debitNote.IGST_head}" var="no_formattedIGST_head"/> 
								<form:input path="IGST_head" class="logInput hlable" id="igst" readonly="true" style="background-color: transparent;" value="${no_formattedIGST_head}"/>
							</td>
							<td colspan="2">
								<label>CESS</label>
								<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" value="${debitNote.SCT_head}" var="no_formattedSCT_head"/> 
								<form:input path="SCT_head" class="logInput hlable" id="state_compansation_tax" readonly="true" style="background-color: transparent;" value="${no_formattedSCT_head}"/>
							</td>
							<td colspan="2">
								<label>VAT</label>
								<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" value="${debitNote.total_vat}" var="no_formattedtotal_vat"/> 
								<form:input path="total_vat" class="logInput hlable" id="vat" readonly="true" style="background-color: transparent;" value="${no_formattedtotal_vat}"/>
							</td>
							<td colspan="2">
								<label>CST</label>
								<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" value="${debitNote.total_vatcst}" var="no_formattedtotal_vatcst"/> 
								<form:input path="total_vatcst" class="logInput hlable" id="cst" readonly="true" style="background-color: transparent;" value="${no_formattedtotal_vatcst}" />
							</td>
							<td colspan="2">
								<label>EXCISE</label> 
								<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" value="${debitNote.total_excise}" var="no_formattedtotal_excise"/>
								<form:input path="total_excise" class="logInput hlable" id="excise" readonly="true" style="background-color: transparent;" value="${no_formattedtotal_excise}"/>
							</td>
							<td colspan="2">
								<label>Total Amount</label>
								<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" value="${debitNote.round_off}" var="no_formattedround_off"/> 
								<form:input path="round_off" class="logInput hlable" id="round_off" readonly="true" style="background-color: transparent;" value="${no_formattedround_off}"/>
							</td>
							<td colspan="2">
								<label>TDS</label> 
								<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2"
								 value="${debitNote.tds_amount}" var="no_formattedtds"/>
								<form:input path="tds_amount" class="logInput hlable" id="tds_amount" readonly="true"
								 style="background-color: transparent;" value="${no_formattedtds}" />
							</td>
						</tr>
					</table>
					<table class="table table-bordered table-striped" id="detailTable"
						style='display: none'>
						<thead>
							<tr>
							 <c:if test="${debitNote.debit_no_id!=null}">
								<th></th>
							</c:if>
								<th>Product</th>
								<th>Quantity</th>
								<th>UOM</th>
								<th>HSN/SAC Code</th>
								<th>Rate</th>
								<th>Discount</th>
								<th>Labour Charge</th>
								<th>Freight Charges</th>
								<th>Other</th>
								<th>CGST/VAT</th>
								<th>SGST/CST</th>
								<th>IGST/EXCISE</th>
								<th>CESS</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>

				</div>
				<div id="dtl-btn">
					<button type="button" class="fassetBtn waves-effect waves-light"
						onclick="showdiv()">Add Details</button>
				</div>
				<div id="details-data" class='row' style="display: none">
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
								<th class="SCT_div" >CESS</th>	
								<th class="VAT_div">VAT</th>	
								<th class="VATCST_div">CST</th>						
								<th class="Excise_div">Excise</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td><select class="logInput" id="product_id" onChange ='setDetails(this.value)'>
										<option value="0">Select Product Name</option>
								</select></td>
								<td>
									<input id="transaction_amount" type="hidden" /> 
									<input class="logInput" id="cgst_hidden" type="hidden" /> 
									<input class="logInput" id="sgst_hidden" type="hidden" /> 
									<input class="logInput" id="igst_hidden" type="hidden" /> 
									<input class="logInput" id="sct_hidden" type="hidden" />  
									<input class="logInput" id="product-name" type='hidden' />
									<input class="logInput" id = "vat_hidden" placeholder="vat_hidden" type="hidden" />
							<input class="logInput" id = "vatcst_hidden" placeholder="vatcst_hidden" type="hidden" />
							<input class="logInput" id = "excise_hidden" placeholder="excise_hidden" type="hidden" />
									<input class="logInput" id="quantity" placeholder="Quantity" onchange="calculaterate()" maxlength="10"/>
								</td>
								<td><input class="logInput" id="UOM" placeholder="UOM" readonly/></td>
								<td class="HSN_div"><input class="logInput" id="HSNCode" onchange="getHsnDetails(this.value)" placeholder="HSNCode" readonly maxlength="10"/></td>
								<td><input class="logInput" id="rate" placeholder="rate" onchange="calculaterate()" maxlength="12"  onpaste="return false;"/></td>
								<td><input class="logInput" id="discount" placeholder="Discount" onchange="calculaterate()" maxlength="6"/></td>
								<td><input class="logInput" id="labour_charges" placeholder="Labour charges" onchange="calculaterate()" maxlength="10"/></td>
								<td><input class="logInput" id="freight" placeholder="Freight charges" onchange="calculaterate()" maxlength="10"/></td>
								<td><input class="logInput" id="others" placeholder="Other" onchange="calculaterate()" maxlength="10"/></td>
								
								<td class="CGST_div" >
							<input id="CGST" class="logInput" placeholder="CGST" maxlength="20" onpaste="return false;"/>									
						</td>
						<td class="SGST_div" >
							<input id="SGST" class="logInput" placeholder="SGST" maxlength="20" onpaste="return false;"/>
			            </td>
						<td class="IGST_div" >
							<input id="IGST" class="logInput" placeholder="IGST" maxlength="20" onpaste="return false;"/>
							</td>
				     	<td class="SCT_div">
						   	<input id= "SCT" class="logInput" placeholder="CESS" maxlength="20" onpaste="return false;"/>
				     	</td>
				     	<td class="VAT_div">
							<input id="VAT" class="logInput" placeholder="VAT" maxlength="20" onpaste="return false;"/>									
						</td>
				     	<td class="VATCST_div">
							<input id="VATCST" class="logInput" placeholder="VATCST" maxlength="20"/>									
						</td>
				     	<td class="Excise_div">
							<input id="Excise" class="logInput" placeholder="Excise" maxlength="20"/>									
						</td>
						<input id="is_gst" class="logInput" placeholder="is_gst" type="hidden"  />
							</tr>
						</tbody>
					</table>
					<div class="text-center">
						<button type="button" class="fassetBtn waves-effect waves-light" onclick="saveDetails()">Save Product Details</button>
					</div>
				</div>
			</div>

			<div class="row text-center-btn">
				<button class="fassetBtn" type="submit" onclick="myFunction()">
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


var purchaseEntryProductDetails = [];
function setDatePicker()
	   {
	   	$("#debitNoteDate").datepicker({dateFormat: 'dd-mm-yy'});
	   	 $('#debitNoteDate').datepicker("option", { maxDate: new Date() });
	   	$('#debitNoteDate').value = '';
	   	$('#debitNoteDate').focus(); // ui-datepicker-div
	   	 event.preventDefault();
	   	
	   } 
function setPurchaseBillNumber(supId){
$("#openingAmount").hide();
	$('#purchaseEntryId').html("");
	$('#purchaseEntryId').find('option').remove();
	$('#purchaseEntryId').append($('<option>', {
	    value: 0,
	    text: '--- Select Supplier Bill No ---'
	}));
	 $("#type").val(0);
	 $('#subledgerId').find('option').remove();
		$('#subledgerId').append($('<option>', {
		    value: 0,
		    text: 'Select SubLedger'
		}));
	 $("#subledgerId").val(0);
	// setSubledger(supId);
$('#purchaseEntryId').append($('<option>', {
		    value: -2,
		    text: 'Opening Balance'
		    
		}));
	<c:forEach var = "entry" items = "${purchaseEntryList}">
		var sId = <c:out value = "${entry.supplier.supplier_id}"/>
		if(sId == supId){
			supStateId = '<c:out value = "${entry.supplier.state.state_id}"/>';
			tdsapply = '<c:out value = "${entry.supplier.tds_applicable}"/>';
			tdsRate = '<c:out value = "${entry.supplier.tds_rate}"/>';
			custgst='<c:out value = "${entry.supplier.gst_applicable}"/>';
			billdate='<c:out value = "${entry.supplier_bill_date}"/>';
			$('#purchaseEntryId').append($('<option>', {
			    value: '${entry.purchase_id}',
			    text: '${entry.voucher_no}'
			}));				
		}
	</c:forEach>
	
}
function dateRestriction() {
  	 
  	 
  	 var datefield=document.getElementById("debitNoteDate").value;
  	
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
  		var id = document.getElementById("debitNoteDate");

  		id.value = '';
  		id.focus(); // ui-datepicker-div
  		event.preventDefault();	
  	}
  	else if(ud > td ){
  			
  			alert("Cannot create voucher for future date");
  			var id = document.getElementById("debitNoteDate");

  			id.value = '';
  			id.focus(); // ui-datepicker-div
  			event.preventDefault();		
  			
  		}
  	
  	
  }
function getProducts(e){
var supplier_id = $("#supplierId").val();
	if(e!=0 && e!=-2){
	$("#openingAmount").hide();
	<c:forEach var = "entry" items = "${purchaseEntryList}">
	var sId = '<c:out value = "${entry.purchase_id}"/>';
	 <c:out value="${sId}" escapeXml="false" />
		 seId=sId;
		 if(sId==e)
			{
			
			    <c:set var="totalPaid" value="${0}"/>
				<c:forEach var = "note" items = "${entry.debitNote}">
				<c:set var="totalPaid" value="${totalPaid + note.round_off+note.tds_amount}" />
			    </c:forEach>
			   
			    var totaldebit = <c:out value = "${totalPaid}"/>;
			    var totalsales = '<c:out value = "${entry.round_off+entry.tds_amount}"/>';
				sales_tamount= parseFloat((parseFloat(totalsales)-parseFloat(totaldebit))).toFixed(2);		
			}
	</c:forEach>
	var productArray = [];
	var purchase_id = e;
	$.ajax({
        type: "POST",
        url: "getProductDetails?id=" + purchase_id, 	        
        success: function (data) {
        	$('#product_id').find('option').remove();
			$('#product_id').append($('<option>', {
			    value: 0,
			    text: 'Select Product Name'
			}));
			
			$.each(data, function (index, product) {
				purchaseEntryProductDetails.push({"product_id":product.product_id,"product_name":product.product_name,"quantity":product.quantity,
					"rate":product.rate,"discount":product.discount,"labour_charges":product.labour_charges,"freight":product.freight,"others":product.others,
					"transaction_amount":product.transaction_amount,"CGST":product.CGST,"SGST":product.SGST,"IGST":product.IGST,"state_com_tax":product.state_com_tax,"is_gst":is_gst,"VAT":VAT,"VATCST":VATCST,"Excise":Excise});
				
				var tempArray = [];
	    		tempArray["id"]=product.product_id;
			    tempArray["name"]=product.product_name;
			    productArray.push(tempArray);  
         	});
			
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
			
        },
        error: function (e) {
            console.log("ERROR: ", e);	            
        },
        done: function (e) {	            
            console.log("DONE");
        }
    });
    }else if(e==-2){
			$("#openingAmount").show();
			
			/* first ajax call for total value */
		
		$.ajax({
			
	        type: 'GET',
	        url: 'getOpeningBalancefordebitnote?supplierid='+supplier_id,
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


compStateId = '<c:out value = "${stateId}"/>';



setTimeout(function() {
    $("#successMsg").hide()
}, 3000);

	$("#round_off_amount,#HSNCode,#quantity,#rate,#discount,#labour_charges,#freight,#others,#CGST,#SGST,#IGST,#SCT").keypress(function(e) {
	if (!digitsAndDotOnly(e)) {
		return false;
	}
});
	
$('#dtl-btn').hide(); 
document.getElementById("table-value").style.display="none";

$('#type').change(function(){
	
	console.log("hello12");
	//
    if($('#type').val() != 2 && $('#type').val() != 3 && $('#type').val() != 0) {
        $('#dtl-btn').show(); 
		document.getElementById("table-value").style.display="block";
		 $("#round_off_amount").attr('readonly', true); 
		 if($('#type').val()==7)
			{
			document.getElementById("other-remark").style.display="block";
			}
		else
			{
			document.getElementById("other-remark").style.display="none";
			}
		 } else {
        $('#dtl-btn').hide(); 
		document.getElementById("table-value").style.display="none";
    	$('#details-data').hide();
		$('#round_off_amount').attr('readonly', false); 
			document.getElementById("other-remark").style.display="none";
			
    } 
   console.log("end change")
    setSubledger($('#type').val());
});





var supplierId=$('#supplierId').val();
setPurchaseBillNumber(supplierId);
//New code for DebitNote issue
  var someJsVar = "<c:out value='${debitNote.purchaseEntryId}'/>";
  console.log("someJsVar");
  console.log(someJsVar);
<c:if test="${debitNote.purchaseEntryId != null}">

	$('#purchaseEntryId').val("${debitNote.purchaseEntryId}"); 
	getProducts(document.getElementById("purchaseEntryId").value);
</c:if>
<c:if test="${debitNote.description != null}">

	$('#type').val("${debitNote.description}");
	$('#type').trigger("change");
</c:if>

<c:if test="${debitNote.subledgerId != null}">
	$('#subledgerId').val("${debitNote.subledgerId}");
</c:if>

<c:if test="${debitNote.subledgerId != null}">
	$('#subledgerId').val("${debitNote.subledgerId}");
</c:if>
 /* function setSubledger(desc){
	console.log("new set subledger");
	
	var subArray = [];
	console.log("setSubledger");
	
	if(desc > 0){
		<c:forEach var = "subledger" items = "${subLedgerList}">
		var str = '<c:out value = "${subledger.ledger.accsubgroup.subgroup_name}"/>';
		var str1 = "Revenue from Operations";
		var str2 = "Direct Expenses";
		var str3 = "Cost of Materials Consumed";
		if(desc == 2 || desc == 3){
			if(str.trim().toUpperCase() == str1.trim().toUpperCase()){
				var tempArray = [];
				var subName = '<c:out value= "${subledger.subledger_name}"/>';
				
				subName=subName.replace('\'', '\'');
				
	    		tempArray["id"]=${subledger.subledger_Id};
			    tempArray["name"]=subName;
			    subArray.push(tempArray);
			}
		}else{
			if(str.trim().toUpperCase() == str2.trim().toUpperCase()  || str.trim().toUpperCase() == str3.trim().toUpperCase()){
				console.log("else");
				var tempArray = [];
				var subName = '<c:out value= "${subledger.subledger_name}"/>';
				
				subName=subName.replace('\'', '\'');
				
				tempArray["id"]=${subledger.subledger_Id};
			    tempArray["name"]=subName;
			    subArray.push(tempArray);
			}
		}
	</c:forEach>	
				
	}
	$('#subledgerId').find('option').remove();
	$('#subledgerId').append($('<option>', {
	    value: 0,
	    text: 'Select SubLedger'
	}));
	
	subArray.sort(function(a, b) {
 		var textA = a.name.toUpperCase();
     	var textB = b.name.toUpperCase();
     	return (textA < textB) ? -1 : (textA > textB) ? 1 : 0;
 	});
	
	for(i=0;i<subArray.length;i++) {
 		$('#subledgerId').append($('<option>', {
	    	value: subArray[i].id,
		    text: subArray[i].name
		})); 
	}		
}  */
</script>
<script type="text/javascript">

	var datefield=document.getElementById("debitNoteDate");
	var purchaseEntryProductDetails = [];
	var productDetails = [];
	var transactionAmount = 0;
	var totalAmount = 0;
	var totalCgst = 0;
	var totalIgst = 0;
	var totalSgst = 0;
	var totalSct = 0;
	var totalVat = 0;
	var totalCst = 0;
	var totalExcise = 0;
	
	var compStateId = 0;
	var supStateId = 0;
	var tdsRate;
	var tdsapply;	
	var startdate="";
	var enddate="";
	var startdate1="";
	var enddate1="";
	var billdate="";
	var sales_tamount=0;
	var total_debit_qty=0;
	var seId=0;
	
	
    function postDateRestriction(){
    	
    	var datefield=document.getElementById("debitNoteDate").value;
   	
   	 var res = datefield.split("-");
   	    var nd = res[1]+"/"+res[0]+"/"+res[2];
   	    var ud = new Date(nd);
   		var td = new Date();
   		
   		ud.setHours(0,0,0,0);
   		
   		td.setHours(0,0,0,0);
   		
   		if(ud > td ){
   			
   			
   			document.getElementById("debitNoteDate").value='';
   			
   		}
    	
    	
    }
	

		compStateId = '<c:out value = "${stateId}"/>';
		
		
		
		setTimeout(function() {
	        $("#successMsg").hide()
	    }, 3000);
		
	 	$("#round_off_amount,#HSNCode,#quantity,#rate,#discount,#labour_charges,#freight,#others,#CGST,#SGST,#IGST,#SCT").keypress(function(e) {
			if (!digitsAndDotOnly(e)) {
				return false;
			}
		});
	 	
	    $('#dtl-btn').hide(); 
		document.getElementById("table-value").style.display="none";

	   /*  $('#type').change(function(){
	    	debugger;
	    	console.log("hello12");
			//setSubledger($('#type').val());
	        if($('#type').val() != 2 && $('#type').val() != 3 && $('#type').val() != 0) {
	            $('#dtl-btn').show(); 
				document.getElementById("table-value").style.display="block";
				 $("#round_off_amount").attr('readonly', true); 
				 if($('#type').val()==7)
					{
					document.getElementById("other-remark").style.display="block";
					}
				else
					{
					document.getElementById("other-remark").style.display="none";
					}
				 } else {
	            $('#dtl-btn').hide(); 
				document.getElementById("table-value").style.display="none";
	        	$('#details-data').hide();
				$('#round_off_amount').attr('readonly', false); 
					document.getElementById("other-remark").style.display="none";
					
	        } 
	    }); */
	    
	    
	   
	    
	    
		var supplierId=$('#supplierId').val();
		setPurchaseBillNumber(supplierId);
		debugger;
		  var someJsVar = "<c:out value='${debitNote.purchaseEntryId}'/>";
		  console.log("someJsVar");
		  console.log(someJsVar);
		<c:if test="${debitNote.purchaseEntryId != null}">
		
			$('#purchaseEntryId').val("${debitNote.purchaseEntryId}"); 
			getProducts(document.getElementById("purchaseEntryId").value);
		</c:if>
		<c:if test="${debitNote.description != null}">
		
			$('#type').val("${debitNote.description}");
			$('#type').trigger("change");
		</c:if>
	
		<c:if test="${debitNote.subledgerId != null}">
			$('#subledgerId').val("${debitNote.subledgerId}");
		</c:if>
		
		var gst_applied =  $("#type").val(); 
		if(gst_applied == 1){
			document.getElementById("dtl-btn").style.display="block";
			document.getElementById("table-value").style.display="block";
			document.getElementById("detailTable").style.display="table";
		}
		else{
			document.getElementById("dtl-btn").style.display="none";
			document.getElementById("table-value").style.display="none";
		}
		
		if($("#debitNoteDetails").val() != ""){
			var myArray = JSON.parse($("#debitNoteDetails").val());
			for(var i=0; i<=myArray.length; i++){	
				if(myArray[i].id == 0){	
					if(myArray[i].is_gst == 1)
					{
					var markup = "<tr><td><a href = '#' onclick = 'deleteRow(this,"+ myArray[i].productId+","+myArray[i].id
					+ ")'><img src='${deleteImg}' style = 'width: 20px;'/></a></td>"
					+ "<td>" + myArray[i].productName + "</td>" + "<td>" + myArray[i].quantity + "</td>"
					+ "<td>" + myArray[i].uomUnit + "</td>" + "<td>" + myArray[i].hsn_san_no + "</td>"
					+ "<td>" + myArray[i].rate + "</td>" + "<td>" + myArray[i].discount + "</td>"
					+ "<td>" + myArray[i].labour_charges + "</td>" + "<td>" + myArray[i].freight
					+ "</td>" + "<td>" + myArray[i].others + "</td>" + "<td>" + myArray[i].cgst + "</td>"
					+ "<td>" + myArray[i].sgst + "</td>" + "<td>" + myArray[i].igst + "</td>" + "<td>"
					+ myArray[i].state_com_tax + "</td></tr>";
					}
					else
					{
						var markup = "<tr><td><a href = '#' onclick = 'deleteRow(this,"+ myArray[i].productId+","+myArray[i].id
						+ ")'><img src='${deleteImg}' style = 'width: 20px;'/></a></td>"
						+ "<td>" + myArray[i].productName + "</td>" + "<td>" + myArray[i].quantity + "</td>"
						+ "<td>" + myArray[i].uomUnit + "</td>" + "<td>" + myArray[i].hsn_san_no + "</td>"
						+ "<td>" + myArray[i].rate + "</td>" + "<td>" + myArray[i].discount + "</td>"
						+ "<td>" + myArray[i].labour_charges + "</td>" + "<td>" + myArray[i].freight
						+ "</td>" + "<td>" + myArray[i].others + "</td>" + "<td>" + myArray[i].VAT + "</td>"
						+ "<td>" + myArray[i].VATCST + "</td>" + "<td>" + myArray[i].Excise + "</td>" + "<td>"
						+ myArray[i].state_com_tax + "</td></tr>";
					}
					$('#detailTable tr:last').after(markup);
					document.getElementById("detailTable").style.display = "table";
				}
			}
		}
		
		<c:if test="${debitDetailsList != null}">
			<c:forEach var = "debitDetails" items = "${debitDetailsList}">
				addRow(${debitDetails.debit_detail_id},
					${debitDetails.product_id.product_id},
					"${debitDetails.product_id.product_name}",
					${debitDetails.quantity},
					"${debitDetails.product_id.hsn_san_no}",
					${debitDetails.rate},
					${debitDetails.discount},
					${debitDetails.uom_id.uom_id},
					"${debitDetails.uom_id.unit}",
					${debitDetails.cgst},
					${debitDetails.igst},
					${debitDetails.sgst},
					${debitDetails.state_com_tax},
					${debitDetails.VAT},
					${debitDetails.VATCST},
					${debitDetails.excise},
					${debitDetails.is_gst},
					${debitDetails.labour_charges},
					${debitDetails.frieght},
					${debitDetails.others},
					${debitDetails.transaction_amount});
			</c:forEach>
		</c:if>
	    
		var years = '<c:out value= "${yearList}"/>';	
		var transaction_id = '<c:out value= "${debitNote.debit_no_id}"/>';
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
		var savedyear='<c:out value= "${debitNote.accounting_year_id.year_id}"/>';	
		<c:forEach var = "year" items = "${yearList}">			
			 if("${year.year_id}"==savedyear)
				{
				 startdate1='${year.start_date}';
		    	 enddate1='${year.end_date}';
			   setdatelimit('${year.start_date}','${year.end_date}');
			   var supdate="${debitNote.date}";				
			   supdate=formatDate(supdate);
			   $("#debitNoteDate").val(supdate);
			   } 
		</c:forEach> 
		}
	
	function showdiv(){
		document.getElementById("details-data").style.display="block";
	}
	
	function cancel(){
		window.location.assign('<c:url value = "debitNoteList"/>');	
	}
	
	

	function saveyearid(){
		 $('#year-model').modal('hide');
	}
	
	
	function setDetails(e){
		
		for(i=0;i<purchaseEntryProductDetails.length;i++){
			if(purchaseEntryProductDetails[i].product_id == e){

				$("#quantity").val(purchaseEntryProductDetails[i].quantity);
				$("#rate").val(purchaseEntryProductDetails[i].rate);
				$("#discount").val(purchaseEntryProductDetails[i].discount);
				$("#labour_charges").val(purchaseEntryProductDetails[i].labour_charges);
				$("#freight").val(purchaseEntryProductDetails[i].freight);
				$("#others").val(purchaseEntryProductDetails[i].others);
			  	$("#CGST").val(purchaseEntryProductDetails[i].CGST); 
				$("#SGST").val(purchaseEntryProductDetails[i].SGST); 
				$("#IGST").val(purchaseEntryProductDetails[i].IGST); 
				$("#SCT").val(purchaseEntryProductDetails[i].state_com_tax);
				$("#is_gst").val(purchaseEntryProductDetails[i].is_gst);
				$("#VAT").val(purchaseEntryProductDetails[i].VAT);
				$("#VATCST").val(purchaseEntryProductDetails[i].VATCST);
				$("#Excise").val(purchaseEntryProductDetails[i].Excise);
			}
		}
		
		<c:forEach var = "product" items = "${products}">
			var id = <c:out value = "${product.product_id}"/>
			if(id == e){
	
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
	            		url: '<c:url value="getHSNSACByDate?date="/>'+billdate+'&hsn='+hsn,                     		
	            		cache: false, 
	            		aysnc:false,
	            		success: function (data) {
			            			cgst=data["cgst"];
			            			sgst=data["sgst"];
			            			igst=data["igst"];
			            			cess=data["state_comp_cess"];  			  
								  	$("#sct_hidden").val(cess); 
								  	$("#SCT").val(cess);		
								   	if(custgst=="true"){ 
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
			  	$("#product-name").val("${product.product_name}");
			  	$('#UOM').attr('data-uomId', '${product.uom.uom_id}');
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
		  			
		  			$("#UOM").val("${product.uom.unit}"); 
				  	$("#product-name").val("${product.product_name}");
				  	$('#UOM').attr('data-uomId', '${product.uom.uom_id}');
				  	$("#HSNCode").val(0);  
					$("#IGST").val(0);
					$("#CGST").val(0);
					$("#SGST").val(0);
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
	
	$("#HSNCode").autocomplete({
		
	    source: function (request, response) {
	    	
	    	if($("#HSNCode").val().length>=4)
	    	{
				$.ajax({
	           		type: "GET",
	            		url: '<c:url value="getHSNSACNoForNonInventory?term="/>'+request.term+'&date='+billdate,                     		
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
        		url: '<c:url value="getHSNSACByDate?date="/>'+billdate+'&hsn='+hsn,                     		
        		cache: false, 
        		aysnc:false,
        		success: function (data) {
	            			cgst=data["cgst"];
	            			sgst=data["sgst"];
	            			igst=data["igst"];
	            			cess=data["state_comp_cess"];  			  
						  	$("#sct_hidden").val(cess); 
						  	$("#SCT").val(cess);		
						   	if(custgst=="true"){ 
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
        		},
            	error: function (e) {
            		//alert(e);
                	alert("Error while retrieving HSN/SAC");
            	}
        	});	
	}
	
	
	function saveDetails(){
		
		var vdate=$("#debitNoteDate").val();
		var vsupplier=$("#supplierId").val();
		var vpurchase_bill_no=$("#purchaseEntryId").val();
		var vdescription=$("#type").val();
		var vsubLedger=$("#subledgerId").val();
		
		var cgst = $("#CGST").val();
		var igst = $("#IGST").val();
		var sgst = $("#SGST").val();
		var state_com_tax = $("#SCT").val();
		var VAT=document.getElementById("VAT").value;
		var VATCST=document.getElementById("VATCST").value;
		var Excise=document.getElementById("Excise").value;
		var is_gst=document.getElementById("is_gst").value;
		
		
		if(cgst.trim()=="")
			document.getElementById("CGST").value="0";
		if(sgst.trim()=="")
			document.getElementById("SGST").value="0";
		if(igst.trim()=="")
			document.getElementById("IGST").value="0";
		if(state_com_tax.trim()=="")
			document.getElementById("SCT").value="0";
		if(VAT.trim()=="")
			document.getElementById("VAT").value="0";
		if(VATCST.trim()=="")
			document.getElementById("VATCST").value="0";
		if(Excise.trim()=="")
			document.getElementById("Excise").value="0";
		
		calculaterate();
		
		var productId = $("#product_id").val();
		var productName = $("#product-name").val();
		var quantity = $("#quantity").val();
		var rate = $("#rate").val();
		var discount =$("#discount").val();
		var labour_charges = $("#labour_charges").val();
		var freight=document.getElementById("freight").value;
		var others = $("#others").val();
		var uomUnit =  $("#UOM").val(); 
		var uomId = $('#UOM').attr('data-uomId');
		var hsn = $("#HSNCode").val();  
		var tamount = $("#transaction_amount").val();
	
				
		transactionAmount = ($("#transaction_value").val() == "") ? 0 : $("#transaction_value").val();
		totalCgst = ( $("#cgst").val() == "" ) ? 0 : $("#cgst").val();
		totalSgst = ( $("#sgst").val() == "" ) ? 0 : $("#sgst").val();
		totalIgst = ( $("#igst").val() == "" ) ? 0 : $("#igst").val();
		totalSct = ( $("#state_compansation_tax").val() == "" ) ? 0 : $("#state_compansation_tax").val();
		totalVat = ( $("#vat").val() == "" ) ? 0 : $("#vat").val();
		totalCst = ( $("#cst").val() == "" ) ? 0 : $("#cst").val();
		totalExcise = ( $("#excise").val() == "" ) ? 0 : $("#excise").val();
		
		
		if(vdate==""){
			alert("Select date");
			return false;
		
		}else if(vsupplier==0){
			alert("Select supplier");
			return false;
		
		}
		else if(vpurchase_bill_no==0){
			alert("Select purchase bill number");
			return false;
		
		}else if(vdescription==0){
			alert("Select description");
			return false;
		
		}else if(vsubLedger==0){
			alert("Select sub ledger");
			return false;
		
		}
		else if(productId==0){
			alert("Please Select Product");
			return false;
		}
		else if((is_gst==1)&&(hsn.trim()=="" || hsn.trim()=="0"))
		{
			alert("Please Enter HSN");
			return false;	
		}
		else if((is_gst==1)&&(((sgst!=0 && sgst!="") || (cgst!=0 && cgst!="")) && (igst!=0 && igst!="")))
		{
			alert("Either input CGST+SGST or IGST");
			return false;
		}
		else if(quantity.trim()=="" || quantity.trim()=="0"){
			alert("Please Enter Quantity");
			return false;
		}
		else if(rate.trim()=="" || rate.trim()=="0"){
			alert("Please Enter Rate");
			return false;
		}
		else if(discount.trim()==""){
			alert("Please Enter Discount");
			return false;
			
		}		
		else{
			if(!isDuplicate(productId)){
				if(!moreQuantity(quantity,productId)){					
					transactionAmount = parseFloat(transactionAmount) + parseFloat(tamount);
					
					if(is_gst==1)
					{
				totalCgst = parseFloat(totalCgst) + parseFloat(cgst);
				totalSgst = parseFloat(totalSgst) + parseFloat(sgst);
				totalIgst = parseFloat(totalIgst) + parseFloat(igst);
				totalSct = parseFloat(totalSct) + parseFloat(state_com_tax);
					}
				else
					{
					totalVat = parseFloat(totalVat) + parseFloat(VAT);
					totalCst = parseFloat(totalCst) + parseFloat(VATCST);
					totalExcise = parseFloat(totalExcise) + parseFloat(Excise);
					}			
					totalAmount = transactionAmount + parseFloat(totalCgst) + parseFloat(totalSgst) + parseFloat(totalIgst) + parseFloat(totalSct) + parseFloat(totalVat) + parseFloat(totalCst) + parseFloat(totalExcise);	
					if(totalAmount>sales_tamount)
					{
					alert("Total debit amount should be less than purchase voucher's amount.");	
					return false;
					}
				else
					{
						$("#transaction_value").val(parseFloat(transactionAmount).toFixed(2));
						$("#cgst").val(parseFloat(totalCgst).toFixed(2));
						$("#sgst").val(parseFloat(totalSgst).toFixed(2));
						$("#igst").val(parseFloat(totalIgst).toFixed(2));
						$("#state_compansation_tax").val(parseFloat(totalSct).toFixed(2));
						$("#vat").val(parseFloat(totalVat).toFixed(2));
						$("#cst").val(parseFloat(totalCst).toFixed(2));
						$("#excise").val(parseFloat(totalExcise).toFixed(2));
						
						if(tdsapply==1)
						{
							var td=parseFloat((transactionAmount*tdsRate)/100).toFixed(2);
							tdsAmount = parseFloat(td);
							
						}
						else
						{
						tdsAmount=0;
						}
						
						$("#round_off_total").val(parseFloat(totalAmount-tdsAmount).toFixed(2));
						$("#round_off_amount").val(parseFloat(totalAmount-tdsAmount).toFixed(2));
						$("#round_off").val(parseFloat(totalAmount-tdsAmount).toFixed(2));
						$("#tds_amount").val(parseFloat(tdsAmount).toFixed(2));
						
						addRow(0, productId, productName, quantity, hsn, rate,
								discount, uomId, uomUnit, cgst, igst, sgst,
								state_com_tax, VAT, VATCST, Excise, is_gst, labour_charges, freight, others,
								tamount);
					}
				}
				else {
					if(total_debit_qty==0)
					alert("Quantity should be less than purchase voucher's quantity.");
					else
					 alert("Quantity should be less than purchase voucher's quantity.You Have already returns "+total_debit_qty+" for Purchase entry");
					return false;
				}
			}
			else{
				alert("Product already added");
				return false;
			}
			$("#product_id").val(0);
			$("#quantity").val("");
			$("#rate").val("");
			$("#discount").val("");
			$("#UOM").val(""); 
			$("#HSNCode").val("");  
			$("#CGST").val("");
			$("#IGST").val("");
			$("#SGST").val("");
			$("#SCT").val("");
			$("#VAT").val("");
			$("#VATCST").val("");
			$("#Excise").val("");
			$("#labour_charges").val("");
			$("#freight").val("");
			$("#others").val("");
		}
	}

	function addRow(id, productId, productName, quantity, hsn_san_no, rate,discount, uomId, uomUnit, cgst, igst, sgst, state_com_tax, VAT, VATCST, Excise, is_gst, labour_charges, freight, others, tamount) {
		
		productDetails.push({"id":id,
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
			"VAT" : VAT,
			"VATCST" : VATCST,
			"Excise" : Excise,
			"is_gst" : is_gst,
			"labour_charges":labour_charges, 
			"freight":freight, 
			"others":others,
			"transactionAmount":tamount,
			"productName" : productName,
			"uomUnit" : uomUnit
		});		
		
		$("#debitNoteDetails").val(JSON.stringify(productDetails));
		console.log($("#debitNoteDetails").val());
		

		var markup = "";
		if(id > 0){
			markup = "<tr><td><a href = '#' onclick = 'deleteRow(this,"+productId+","+id+")'><img src='${deleteImg}' style = 'width: 20px;'/></a>"+
		    		"<a href = '#' onclick = 'editproductdetail("+id+")'><img src='${editImg}' style = 'width: 20px;'/></a></td>";
		   }
		else{
			markup = "<tr>"; 
			 <c:if test="${debitNote.debit_no_id!=null}">
			markup = markup + "<td>" + "</td>"; 
		    </c:if>  
	    }
		
		if(is_gst==1){
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
		}else
			{
			markup = markup + "<td>"+productName+"</td>"+
			"<td>"+quantity+"</td>"+
			"<td>"+uomUnit+"</td>"+
			"<td>"+hsn_san_no+"</td>"+ 
			"<td>"+rate+"</td>"+
			"<td>"+discount+"</td>"+
			"<td>"+labour_charges+"</td>"+
			"<td>"+freight+"</td>"+
			"<td>"+others+"</td>"+
			"<td>"+VAT+"</td>"+
			"<td>"+VATCST+"</td>"+
			"<td>"+Excise+"</td>"+
			"<td>"+state_com_tax+"</td></tr>";
			}

		$('#detailTable tr:last').after(markup);
		document.getElementById("detailTable").style.display="table";
	}
	
	function isDuplicate(prodId){
		for(i = 0; i<productDetails.length;i++){
			if(productDetails[i].productId == prodId){
				return true;
			}
		}
		return false;
	}
	
	function moreQuantity(quatity,productId){

		for(j = 0; j<purchaseEntryProductDetails.length;j++){
			if(purchaseEntryProductDetails[j].product_id == productId){
				
				
				if(purchaseEntryProductDetails[j].quantity < quatity){
					return true;
				}
			}
		}
		return false;
	}
	
	function calculaterate(){
		var cgst_hidden=document.getElementById("cgst_hidden").value;
		var igst_hidden=document.getElementById("igst_hidden").value;
		var sgst_hidden=document.getElementById("sgst_hidden").value;
		var sct_hidden=document.getElementById("sct_hidden").value;
		
		var vat_hidden=document.getElementById("vat_hidden").value;
		var vatcst_hidden=document.getElementById("vatcst_hidden").value;
		var excise_hidden=document.getElementById("excise_hidden").value;
		
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
		
		var amount = (rate*quantity);
		if(discount!=""){				
			amount = parseFloat(amount) - parseFloat(discount);				 
		}
		amount = parseFloat(amount)+parseFloat(labour_charges)+parseFloat(freight)+parseFloat(others);
		
		$("#transaction_amount").val(parseFloat(amount).toFixed(2));	 
		
		$("#CGST").val(parseFloat((amount*cgst_hidden)/100).toFixed(2));
		$("#SGST").val(parseFloat((amount*sgst_hidden)/100).toFixed(2));
		$("#IGST").val(parseFloat((amount*igst_hidden)/100).toFixed(2));
		$("#SCT").val(parseFloat((amount*sct_hidden)/100).toFixed(2));	
		
		$("#VAT").val(parseFloat((amount*vat_hidden)/100).toFixed(2));
		$("#VATCST").val(parseFloat((amount*vatcst_hidden)/100).toFixed(2));
		$("#Excise").val(parseFloat((amount*excise_hidden)/100).toFixed(2));

	}
	
	 function deleteRow(e, prodId, id){
		if(id > 0){
			window.location.assign('<c:url value="deleteDebitDetails"/>?id='+id);
		}
		else{
			$(e).closest('tr').remove();
			for(i = 0; i<productDetails.length;i++){
				if(productDetails[i].productId == prodId){					
					transactionAmount = transactionAmount - productDetails[i].transactionAmount;
					totalCgst = totalCgst - productDetails[i].cgst;
					totalSgst = totalSgst - productDetails[i].sgst;
					totalIgst = totalIgst - productDetails[i].igst;
					totalSct = totalSct - productDetails[i].state_com_tax;
					totalAmount = transactionAmount + totalCgst + totalSgst + totalIgst + totalSct;
					$("#transaction_value").val(parseFloat(transactionAmount).toFixed(2));
					$("#cgst").val(totalCgst.toFixed(2));
					$("#sgst").val(totalSgst.toFixed(2));
					$("#igst").val(totalIgst.toFixed(2));
					$("#state_compansation_tax").val(totalSct.toFixed(2));
					$("#round_off_total").val(parseFloat(totalAmount).toFixed(2));
					$("#round_off_amount").val(parseFloat(totalAmount).toFixed(2));		
					
					productDetails.splice(i,1);
				}
			}	
			console.log(JSON.stringify(paymentDetails));		
		}
	} 
	
	function validate(){
		var amt=$("#round_off_amount").val();
		var supplier_id = $("#purchaseEntryId").val();

		if(sales_tamount!="")
			sales_tamount=parseFloat(sales_tamount);
		if(amt!="")
			amt=parseFloat(amt);
		alert("Amt it s" +amt);
	/* 	if(amt>sales_tamount)
		{
		alert("Total debit amount should be less than purchase voucher's amount.");	
		return false;
		}
	else
		{ */
				var dateString = $("#debitNoteDate").val();	
				var flag = true;
			 	if($('#type').val() != 2 && $('#type').val() != 3 && $('#type').val() != 0){
			 		if($("#debitNoteDetails").val() == ""){
			 			alert("Please select atleast one product");
			 			flag = false;
			 		}
					
				}
				if(dateString!="" && flag == true){
					if(validatedate(startdate1,enddate1,dateString)==false)
					{		
						return false;
					}
					else
					{
						if(isValidDate(dateString)==true){
							var oldDate = dateString.split("-");					
							$("#debitNoteDate").val(oldDate[2]+"-"+oldDate[1]+"-"+oldDate[0]);		
						}
					}
				}	
				if(supplier_id ==-2)
				{
					
				var outstandingAmountagainstopening=$("#outstandingAmountagainstopening").html();
				alert(outstandingAmountagainstopening);
				if((outstandingAmountagainstopening!="")){
					outstandingAmountagainstopening=parseFloat(Math.ceil(outstandingAmountagainstopening));
			 	}else{
			 		outstandingAmountagainstopening=parseFloat(Math.ceil(0));
			 	}
				if(amt>outstandingAmountagainstopening){
					flag=false;
					alert("Debit Note Amount Can not be Greater than Bill opening Outstanding Amount");
				}
				}else {
					if(amt>sales_tamount)
					{
					alert("Total debit amount should be less than sale voucher's amount.");	
					flag=false;
					}
				}
				return flag;
		//}
	}

	function editproductdetail(id){
		window.location.assign('<c:url value="editDebitDetails"/>?id='+id);
	}
//This is correct	 
	/* function setSubledger(desc){
		console.log("executed");
		console.log(desc);
		alert("new set sub executed " + desc);
		var subArray = [];
		var e = document.getElementById("supplierId");
		var num = e.options[e.selectedIndex].value;
		var text = e.options[e.selectedIndex].text;
		if(desc > 0){
			<c:forEach var = "subledger" items = "${subLedgerList}">
				var str = '<c:out value = "${subledger.ledger.accsubgroup.subgroup_name}"/>';
				var str1 = "Revenue from Operations";
				var str2 = "Direct Expenses";
				var str3 = "Cost of Materials Consumed";
				//console.log(str);
				 		
				
				if(desc == 2 || desc == 3){
					if(str.trim().toUpperCase() == str1.trim().toUpperCase()){
                         var subLedId=${subledger.subledger_Id};
						
						<c:forEach var="entry" items="${supplierSubledger}">
						var Key='<c:out value="${entry.key}"/>';
						
						if(subLedId==Key){
							  
							<c:forEach var="currentValue" items="${entry.value}">
							         var value= <c:out value="${currentValue}"/>
							         if(value==num){
							      
							         var tempArray = [];
										var subName = '${subledger.subledger_name}'.replace('\'', '\'');
							    		tempArray["id"]=${subledger.subledger_Id};
									    tempArray["name"]=subName;
									   						
									    subArray.push(tempArray);
							        
							         }
							    </c:forEach>
							    }
						
						</c:forEach>
						
					}
				}else{
					if(str.trim().toUpperCase() == str2.trim().toUpperCase()  || str.trim().toUpperCase() == str3.trim().toUpperCase()){
						
						var subLedId=${subledger.subledger_Id};
						
						<c:forEach var="entry" items="${supplierSubledger}">
						var Key='<c:out value="${entry.key}"/>';
						
						if(subLedId==Key){
							 
							<c:forEach var="currentValue" items="${entry.value}">
							         var value= <c:out value="${currentValue}"/>
							         if(value==num){
							      
							         var tempArray = [];
										var subName = '${subledger.subledger_name}'.replace('\'', '\'');
							    		tempArray["id"]=${subledger.subledger_Id};
									    tempArray["name"]=subName;
									   						
									    subArray.push(tempArray);
							        
							         }
							    </c:forEach>
							    }
						
						</c:forEach>
						
					}
				}
			</c:forEach>			
		}
	
		$('#subledgerId').find('option').remove();
		$('#subledgerId').append($('<option>', {
		    value: 0,
		    text: 'Select SubLedger'
		}));
		
		subArray.sort(function(a, b) {
     		var textA = a.name.toUpperCase();
         	var textB = b.name.toUpperCase();
         	return (textA < textB) ? -1 : (textA > textB) ? 1 : 0;
     	});
		
		for(i=0;i<subArray.length;i++) {
	 		$('#subledgerId').append($('<option>', {
		    	value: subArray[i].id,
			    text: subArray[i].name
			})); 
		}		
	}  */
	
	 function setSubledger(desc){
		console.log("executed");
		console.log(desc);
		alert("chek new " + desc);
		var ysize = '<c:out value= "${subLedgerList.size()}"/>';	
		alert("sub list is " +ysize);
		var subArray = [];
		var e = document.getElementById("supplierId");
		var num = e.options[e.selectedIndex].value;
		var text = e.options[e.selectedIndex].text;
		if(desc > 0){
			<c:forEach var = "subledger" items = "${subLedgerList}">
					
								
                         var subLedId=${subledger.subledger_Id};
						console.log("subled id is ",subLedId);
						<c:forEach var="entry" items="${supplierSubledger}">
						var Key='<c:out value="${entry.key}"/>';
						console.log("key is ",Key);
						if(subLedId==Key){
							  
							<c:forEach var="currentValue" items="${entry.value}">
							         var value= <c:out value="${currentValue}"/>
							         if(value==num){
							      
							         var tempArray = [];
										var subName = '${subledger.subledger_name}'.replace('\'', '\'');
							    		tempArray["id"]=${subledger.subledger_Id};
									    tempArray["name"]=subName;
									   						
									    subArray.push(tempArray);
							        
							         }
							    </c:forEach>
							    }
						
						</c:forEach>
						
				
				
			</c:forEach>			
		}
	
		$('#subledgerId').find('option').remove();
		$('#subledgerId').append($('<option>', {
		    value: 0,
		    text: 'Select SubLedger'
		}));
		
		subArray.sort(function(a, b) {
     		var textA = a.name.toUpperCase();
         	var textB = b.name.toUpperCase();
         	return (textA < textB) ? -1 : (textA > textB) ? 1 : 0;
     	});
		
		for(i=0;i<subArray.length;i++) {
	 		$('#subledgerId').append($('<option>', {
		    	value: subArray[i].id,
			    text: subArray[i].name
			})); 
		}		
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
		$("#debitNoteDate").datepicker({dateFormat: 'dd-mm-yy'});
		$('#debitNoteDate').datepicker("option", { minDate: startdate, 
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
	
	function myFunction() {
		
		var str=$('#round_off_amount').val();
	       while (str.search(",") >= 0) {
	           str = (str + "").replace(',', '');
	       }
	       return str;
	   };
	   
	   function setDatePicker()
	   {
		  
	   	$("#debitNoteDate").datepicker({dateFormat: 'dd-mm-yy'});
	   	 $('#debitNoteDate').datepicker("option", { maxDate: new Date() });
	   	$('#debitNoteDate').value = '';
	   	$('#debitNoteDate').focus(); // ui-datepicker-div
	   	 event.preventDefault();
	   	
	   } 


	   

	 
</script>
<style>
	input, select, textarea {   ysiz
	    width: 50px;
	}
</style>
<%@include file="/WEB-INF/includes/footer.jsp"%>