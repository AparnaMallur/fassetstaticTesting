<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>

<div class="breadcrumb">
	<h3>Edit Debit Note Product</h3>
	<a href="homePage">Home</a> » <a
		href="editDebitNote?id=${debitDetails.debitId}">Debit Note</a> » <a
		href="#">Edit Debit Note Product</a>

</div>
<div class="col-md-12 wideform">
	<div class="fassetForm">
		<form:form id="editProductForm" action="saveDebitDetails" method="post" commandName="debitDetails" onsubmit = "return validate();">
			<form:input path="debit_detail_id" id="debit_detail_id" type="hidden" />
			<form:input path="productId" id="productId" type="hidden" />
			<form:input path="transaction_amount" id="transaction_amount" type="hidden" />
			<form:input path="debitId" id="debitId" type="hidden" />
			<form:input path="is_gst" id="is_gst" hidden = "hidden"/>
			<input id="transaction_amount" value="${debitDetails.transaction_amount}" type="hidden" />
			<c:choose>
			
			<c:when test="${debitDetails.is_gst==2}">
							<!-- vat start -->
							<input class="logInput" id = "vat_hidden" placeholder="vat_hidden" type="hidden" value="${productvat.vat}"/>
							<input class="logInput" id = "vatcst_hidden" placeholder="vatcst_hidden" type="hidden" value="${productvat.cst}"/>
							<input class="logInput" id = "excise_hidden" placeholder="excise_hidden" type="hidden" value="${productvat.excise}" />
							<!-- vat end -->
			</c:when>
						
			<c:otherwise>
			<c:choose>
				<c:when test="${supplier.gst_applicable==true}">
					<c:choose>
						<c:when test="${compState != supState}">
							<input class="logInput" id="cgst_hidden" value="0" type="hidden" />
							<input class="logInput" id="sgst_hidden" value="0" type="hidden" />
							<input class="logInput" id="igst_hidden" value="${productgst.igst}" type="hidden" />
							<input class="logInput" id="sct_hidden" value="${productgst.state_comp_cess}" type="hidden" />
						</c:when>
						<c:otherwise>
							<input class="logInput" id="cgst_hidden" value="${productgst.cgst}" type="hidden" />
							<input class="logInput" id="sgst_hidden" value="${productgst.sgst}" type="hidden" />
							<input class="logInput" id="igst_hidden" value="0" type="hidden" />
							<input class="logInput" id="sct_hidden" value="${productgst.state_comp_cess}" type="hidden" />
						</c:otherwise>
					</c:choose>
				</c:when>
				<c:otherwise>
					<input class="logInput" id="cgst_hidden" value="0" type="hidden" />
					<input class="logInput" id="sgst_hidden" value="0" type="hidden" />
					<input class="logInput" id="igst_hidden" value="0" type="hidden" />
					<input class="logInput" id="sct_hidden" value="0" type="hidden" />
				</c:otherwise>
				</c:choose>
			</c:otherwise>
			
			</c:choose>

			<div class="row">
				<div class="col-md-2 control-label">
					<label>Product Name</label>
				</div>
				<div class="col-md-4">
					<input value="${debitDetails.product_id.product_name}"
						disabled="disabled" style="width: 100%;" />
				</div>
				<div class="col-md-2 control-label">
					<label>UOM<span>*</span></label>
				</div>
				<div class="col-md-4">
					<input value="${debitDetails.uom_id.unit}" disabled="disabled"
						style="width: 100%;" />
				</div>
			</div>
			
			<div class="row">
				<div class="col-md-2 control-label">
					<label>HSN/SAC Code<span>*</span></label>
				</div>
				<div  class="col-md-4">
					<input value="${debitDetails.product_id.hsn_san_no}"
						disabled="disabled" style="width: 100%;" />
				</div>
				<div class="col-md-2 control-label">
					<label>Quantity<span>*</span></label>
				</div>
				<div  class="col-md-4">
					<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" 
				value="${debitDetails.quantity}" var="no_formattedquantity"/>	
				
					<form:input path="quantity" class="logInput" id="quantity"
						placeholder="quantity" onchange="calculaterate(this.value,1)"
						onkeypress="return isNumber(event)" value="${no_formattedquantity}" maxlength="10" />
					<span class="logError"><form:errors path="quantity" /></span>
				</div>
			</div>
			
			<div class="row">
				<div class="col-md-2 control-label">
					<label>Rate<span>*</span></label>
				</div>
				<div class="col-md-4">
				<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" 
				value="${debitDetails.rate}" var="no_formattedRate"/>	
					<form:input path="rate" class="logInput" id="rate"
						placeholder="rate" onchange="calculaterate(this.value,2)"
						onkeypress="return isNumber(event)" value="${no_formattedRate}" maxlength="12"  />
					<span class="logError"><form:errors path="rate" /></span>
				</div>
				<div class="col-md-2 control-label">
					<label>Discount<span>*</span></label>
				</div>
				<div class="col-md-4">
				<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" 
				value="${debitDetails.discount}" var="no_formatteddiscount"/>	
					<form:input path="discount" class="logInput" id="discount"
						placeholder="discount" onchange="calculaterate(this.value,3)"
						onkeypress="return isNumber(event)" value="${no_formatteddiscount}" maxlength="10"  />
					<span class="logError"><form:errors path="discount" /></span>
				</div>
			</div>
		
			<div class="row">
				<div class="col-md-2 control-label">
					<label>Labour Charge<span>*</span></label>
				</div>
				<div class="col-md-4">
				<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" 
				value="${debitDetails.labour_charges}" var="no_formattedlabour_charges"/>	
					<form:input path="labour_charges" class="logInput"
						id="labour_charges" placeholder="labour_charges"
						onchange="calculaterate(this.value,4)"
						onkeypress="return isNumber(event)"  value="${no_formattedlabour_charges}" maxlength="10" />
					<span class="logError"><form:errors path="labour_charges" /></span>
				</div>
				<div class="col-md-2 control-label">
					<label>Freight Charge<span>*</span></label>
				</div>
				<div class="col-md-4">
				<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" 
				value="${debitDetails.frieght}" var="no_formattedfrieght"/>	
					<form:input path="frieght" class="logInput" id="frieght"
						placeholder="frieght" onchange="calculaterate(this.value,5)"
						onkeypress="return isNumber(event)" value="${no_formattedfrieght}" maxlength="10"  />
					<span class="logError"><form:errors path="frieght" /></span>
				</div>
			</div>
			
			<div class="row">
				<div class="col-md-2 control-label">
					<label>Other<span>*</span></label>
				</div>
				<div class="col-md-4">
				<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" 
				value="${debitDetails.others}" var="no_formattedothers"/>	
					<form:input path="others" class="logInput" id="others"
						placeholder="others" onchange="calculaterate(this.value,6)"
						onkeypress="return isNumber(event)"  value="${no_formattedothers}" maxlength="10" />
					<span class="logError"><form:errors path="others" /></span>
				</div>
				
				<c:if test="${debitDetails.is_gst==2}">
				
				<div class="col-md-2 control-label"><label>VAT<span>*</span></label></div>		
				<div class="col-md-4">	
				<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" 
				value="${debitDetails.VAT}" var="no_formattedVAT"/>	
					<form:input path="VAT" class="logInput" id = "VAT" placeholder="VAT"  value="${no_formattedVAT}" maxlength="20"/>
							<span class="logError"><form:errors path="VAT" /></span>
				</div>
				
				</c:if>
				<c:if test="${debitDetails.is_gst !=2}">
				<div class="col-md-2 control-label">
					<label>CGST<span>*</span></label>
				</div>
				<div class="col-md-4">
				<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" 
				value="${debitDetails.cgst}" var="no_formattedcgst"/>	
				
					<form:input path="cgst" class="logInput" id="cgst"
						placeholder="cgst"  value="${no_formattedcgst}" maxlength="20"/>
					<span class="logError"><form:errors path="cgst" /></span>
				</div>
				</c:if>
				
			</div>
			
			
			
			<c:choose>
						<c:when test="${debitDetails.is_gst==2}">
								
								<div class="row">	
				<div class="col-md-2 control-label"><label>VATCST<span>*</span></label></div>		
				<div class="col-md-4">	
				<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" 
				value="${debitDetails.VATCST}" var="no_formattedVATCST"/>	
					<form:input path="VATCST" class="logInput" id = "VATCST" placeholder="VATCST"  value="${no_formattedVATCST}" maxlength="20" />
							<span class="logError"><form:errors path="VATCST" /></span>
				</div>
				<div class="col-md-2 control-label"><label>Excise<span>*</span></label></div>		
				<div class="col-md-4">	
				<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" 
				value="${debitDetails.excise}" var="no_formattedExcise"/>	
					<form:input path="Excise" class="logInput" id = "Excise" placeholder="Excise" value="${no_formattedExcise}" maxlength="20" />
							<span class="logError"><form:errors path="Excise" /></span>
				</div>
			</div>
	  			</c:when>
						<c:otherwise>
								
								<div class="row">
				<div class="col-md-2 control-label">
					<label>SGST<span>*</span></label>
				</div>
				<div class="col-md-4">
				<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" 
				value="${debitDetails.sgst}" var="no_formattedsgst"/>	
					<form:input path="sgst" class="logInput" id="sgst"
						placeholder="sgst" value="${no_formattedsgst}" maxlength="20"/>
					<span class="logError"><form:errors path="sgst" /></span>
				</div>
				<div class="col-md-2 control-label">
					<label>IGST<span>*</span></label>
				</div>
				<div class="col-md-4"> 
				<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" 
				value="${debitDetails.igst}" var="no_formattedigst"/>	
					<form:input path="igst" class="logInput" id="igst"
						placeholder="igst" value="${no_formattedigst}" maxlength="20"/>
					<span class="logError"><form:errors path="igst" /></span>
				</div>
			</div>
	
			<div class="row">
				<div class="col-md-2 control-label">
					<label>CESS<span>*</span></label>
				</div>
				<div class="col-md-4">
				<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" 
				value="${debitDetails.state_com_tax}" var="no_formattedstate_com_tax"/>	
					<form:input path="state_com_tax" class="logInput"
						id="state_com_tax" placeholder="state_com_tax" value="${no_formattedstate_com_tax}"  maxlength="20"/>
					<span class="logError"><form:errors path="state_com_tax" /></span>
				</div>
			</div>		
						</c:otherwise>
			</c:choose>
		
			<div class="row text-center-btn">
				<button class="fassetBtn" type="submit">
					<spring:message code="btn_save" />
				</button>
				<button class="fassetBtn" type="button" onclick="cancel()">
					<spring:message code="btn_cancel" />
				</button>
			</div>
		</form:form>
	</div>
</div>
<script type="text/javascript">
	$(function() {
		$("#quantity,#rate,#discount,#CGST,#IGST,#SGST,#state_com_tax,#labour_charges,#frieght,#Others,#VAT,#VATCST,#Excise").keypress(function(e) {
			if (!digitsAndDotOnly(e)) {
				return false;
			}
		});
	});

	function cancel() {
		var debit_id = "<c:out value= "${debitDetails.debitId}"/>";
		window.location.assign('<c:url value="editDebitNote"/>?id=' + debit_id);

	}
	function validate()
	{
		var quantity=document.getElementById("quantity").value;
		var rate=document.getElementById("rate").value;
		var discount=document.getElementById("discount").value;
		var SGST=document.getElementById("sgst").value;
		var IGST=document.getElementById("igst").value;
		var CGST=document.getElementById("cgst").value;
		var is_gst=document.getElementById("is_gst").value;
		var bvalid=true;
		
		var damount=rate*quantity;
		
		if(quantity.trim()=="" || quantity.trim()=="0"){
			alert("Please Enter Quantity");
			bvalid= false;
		}
		
		else if(discount>damount){
			alert("Please Enter Discount Less Than damount");
			bvalid= false;
		}
		
		else if(rate.trim()=="" || rate.trim()=="0"){
			alert("Please Enter Rate");
			bvalid= false;
		}
		else if(discount.trim()==""){
			alert("Please Enter Discount");
			bvalid= false;
		}
		else if(discount>100){
			alert("Please Enter Discount Less Than 100");
			bvalid= false;
		}
		if(is_gst!=2)
			{
					if(((SGST!=0 && SGST!="") || (CGST!=0 && CGST!="")) && (IGST!=0 && IGST!=""))
					{
					alert("Either input CGST+SGST or IGST");
					bvalid= false;
					}
			}
		return bvalid;
	}
	function calculaterate(e, flag) {
		var is_gst=document.getElementById("is_gst").value;
		
		if(is_gst==2)
		{
			//vat//
			var vat_hidden=document.getElementById("vat_hidden").value;
			var vatcst_hidden=document.getElementById("vatcst_hidden").value;
			var excise_hidden=document.getElementById("excise_hidden").value;
		}
	else
		{
		var cgst_hidden = document.getElementById("cgst_hidden").value;
		var igst_hidden = document.getElementById("igst_hidden").value;
		var sgst_hidden = document.getElementById("sgst_hidden").value;
		var sct_hidden = document.getElementById("sct_hidden").value;
		}
		
		var quantity = document.getElementById("quantity").value;
		var rate = document.getElementById("rate").value;
		var labour_charges = document.getElementById("labour_charges").value;
		var discount = document.getElementById("discount").value;
		var frieght = document.getElementById("frieght").value;
		var others = document.getElementById("others").value;

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

	/* 	if(discount>100)
	 		alert("Discount should be less than 100"); */
	 	if(frieght!="")
	 		frieght=parseFloat(frieght);
	 	else
	 		frieght=0;
	 	if(others!="")
		 	others=parseFloat(others);
	 	else
	 		others=0;

		var amount = (rate * quantity) + labour_charges + frieght + others;
		var damount = rate * quantity;
		if (discount != "") {
			var disamount = parseFloat(damount)- parseFloat(discount);
			amount = parseFloat(disamount) + labour_charges + frieght + others;
		}

		document.getElementById("transaction_amount").value = Number(amount).toFixed(2);

		if(is_gst==2)
 		{
 		document.getElementById("VAT").value=parseFloat((amount*vat_hidden)/100).toFixed(2);
		document.getElementById("VATCST").value=parseFloat((amount*vatcst_hidden)/100).toFixed(2);
		document.getElementById("Excise").value=parseFloat((amount*excise_hidden)/100).toFixed(2);
 		}
 	else
 		{
		$("#cgst").val(parseFloat((amount * cgst_hidden) / 100).toFixed(2));

		$("#sgst").val(parseFloat((amount * sgst_hidden) / 100).toFixed(2));

		$("#igst").val(parseFloat((amount * igst_hidden) / 100).toFixed(2));

		$("#state_com_tax").val(
				parseFloat((amount * sct_hidden) / 100).toFixed(2));
 		}
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp"%>