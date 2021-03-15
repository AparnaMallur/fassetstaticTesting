<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>

<div class="breadcrumb">
	<h3>Edit Receipt Product</h3>		
	<a href="homePage">Home</a> » <a href="editReceipt?id=${entry.receipt_header_id}">Receipt</a> » <a href="#">Edit Receipt Product</a>
	
</div>	
<div class="col-md-12 wideform">
	<div class="fassetForm">
		<form:form id="editProductForm" action="saveproductdetailforReceipt" method="post" commandName = "entry" onsubmit = "return validate();">
			<div class="row">				
				<form:input path="product_id" hidden = "hidden"/>
				<form:input path="receipt_header_id" hidden = "hidden"/>
				<form:input path="receipt_detail_id" hidden = "hidden"/>
				<form:input path="transaction_amount" id="transaction_amount" hidden = "hidden"/>
				
				 <c:choose>					
									<c:when test="${receipt.customer.state.state_id!=stateId}">
												<input class="logInput" id="cgst_hidden" value="0" type="hidden" />
												<input class="logInput" id="sgst_hidden" value="0" type="hidden" /> 
												<input class="logInput" id="igst_hidden" value="${productgst.igst}" type="hidden" /> 
												<input class="logInput" id="sct_hidden" value="${productgst.state_comp_cess}" type="hidden" />									</c:when>
										<c:otherwise>
													<input class="logInput" id="cgst_hidden" value="${productgst.cgst}" type="hidden" />
													<input class="logInput" id="sgst_hidden" value="${productgst.sgst}" type="hidden" /> 
													<input class="logInput" id="igst_hidden" value="0" type="hidden" /> 
													<input class="logInput" id="sct_hidden" value="${productgst.state_comp_cess}" type="hidden" /> 
										</c:otherwise>		
								</c:choose>	
			</div>
			<div class="row">	
				<div class="col-md-2 control-label"><label>Product Name</label></div>		
				<div class="col-md-4">
					<input value="${entry.product_name}" disabled="disabled" style="width:100%;"/>
				</div>	
				<div class="col-md-2 control-label"><label>UOM<span>*</span></label></div>		
				<div class="col-md-4">	
					<form:input path="UOM" class="logInput" id = "UOM" placeholder="UOM" readonly="true"/>
							<span class="logError"><form:errors path="UOM" /></span>
				</div>
			</div>	
		
			<div class="row">	
				<div class="col-md-2 control-label"><label>HSN/SAC Code<span>*</span></label></div>		
				<div class="col-md-4">	
					<input value="${entry.HSNCode}" disabled="disabled" style="width:100%;"/>
				</div>
				
				<div class="col-md-2 control-label"><label>Quantity<span>*</span></label></div>		
				<div class="col-md-4">	
				<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2"
				 value="${entry.quantity}" var="no_formattedQuantity"/>	
					
					<form:input path="quantity" class="logInput" id = "quantity" placeholder="quantity" 
					onchange="calculaterate(this.value,1)"  maxlength="10"  value="${no_formattedQuantity}"/>
							<span class="logError"><form:errors path="quantity" /></span>
				</div>
			</div>
			
			<div class="row">	
				<div class="col-md-2 control-label"><label>Rate<span>*</span></label></div>		
				<div class="col-md-4">
				<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" value="${entry.rate}" var="no_formattedRate"/>	
					<form:input path="rate" class="logInput" id = "rate" placeholder="rate" onchange="calculaterate(this.value,1)" 
					value="${no_formattedRate}"  maxlength="12"/>
							<span class="logError"><form:errors path="rate" /></span>
				</div>
				
				<div class="col-md-2 control-label"><label>Discount<span>*</span></label></div>		
				<div class="col-md-4">
				<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" 
				value="${entry.discount}" var="no_formattedDiscount"/>	
					
					<form:input path="discount" class="logInput" id = "discount" placeholder="discount" onchange="calculaterate(this.value,1)"
					value="${no_formattedDiscount}"   maxlength="6" />
							<span class="logError"><form:errors path="discount" /></span>
				</div>
			</div>
		
			<div class="row">	
				<div class="col-md-2 control-label"><label>Labour Charge<span>*</span></label></div>		
				<div class="col-md-4">
				<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2"
				 value="${entry.labour_charges}" var="no_formattedlabour_charges" />	
					<form:input path="labour_charges" class="logInput" id = "labour_charges" placeholder="labour_charges" 
					onchange="calculaterate(this.value,1)" value="${no_formattedlabour_charges}"   maxlength="10"  />
							<span class="logError"><form:errors path="labour_charges" /></span> 
				</div>
				
				<div class="col-md-2 control-label"><label>Freight Charge<span>*</span></label></div>		
				<div class="col-md-4">	
				<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" 
				value="${entry.freight}" var="no_formattedfreight"/>	
					<form:input path="freight" class="logInput" id = "freight" placeholder="freight" onchange="calculaterate(this.value,1)"
					value="${no_formattedfreight}"   maxlength="10" />
							<span class="logError"><form:errors path="freight" /></span>
				</div>
			</div>
		
			 <div class="row">	
				<div class="col-md-2 control-label"><label>Other<span>*</span></label></div>		
				<div class="col-md-4">	
				<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" 
				value="${entry.others}" var="no_formattedOthers"/>	
					<form:input path="Others" class="logInput" id = "Others" placeholder="Others" onchange="calculaterate(this.value,1)"
					value="${no_formattedOthers	}"   maxlength="10"  />
							<span class="logError"><form:errors path="Others" /></span>
				</div>
				
				<div class="col-md-2 control-label"><label>CGST<span>*</span></label></div>		
				<div class="col-md-4">
				<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" value="${entry.CGST}" var="no_formattedCGST"/>	
					<form:input path="CGST" class="logInput" id = "CGST" placeholder="CGST"  value="${no_formattedCGST}" maxlength="20"/>
							<span class="logError"><form:errors path="CGST" /></span>
				</div>
			</div> 
			<div class="row">	
				
				
				<div class="col-md-2 control-label"><label>SGST<span>*</span></label></div>		
				<div class="col-md-4">
				<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" value="${entry.SGST}" var="no_formattedSGST"/>	
					<form:input path="SGST" class="logInput" id = "SGST" placeholder="SGST" value="${no_formattedSGST}" maxlength="20"/>
							<span class="logError"><form:errors path="SGST" /></span>
				</div>
				<div class="col-md-2 control-label"><label>IGST<span>*</span></label></div>		
				<div class="col-md-4">
				<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" value="${entry.IGST}" var="no_formattedIGST"/>	
					<form:input path="IGST" class="logInput" id = "IGST" placeholder="IGST" value="${no_formattedIGST}" maxlength="20"/>
							<span class="logError"><form:errors path="IGST" /></span>
				</div>
				
			</div>
			
			<div class="row">	
			
				
				<div class="col-md-2 control-label"><label>CESS<span>*</span></label></div>		
				<div class="col-md-4">
				<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" value="${entry.state_com_tax}" var="no_formattedstate_com_tax"/>	
					<form:input path="state_com_tax" class="logInput" id = "state_com_tax" placeholder="state_com_tax" value="${no_formattedstate_com_tax}" maxlength="20"/>
							<span class="logError"><form:errors path="state_com_tax" /></span>
				</div>
			</div>
		
			<div class="row text-center-btn">
				<button class="fassetBtn" type="submit" >
					<spring:message code="btn_save" />
				</button>
				<button class="fassetBtn" type="button" onclick = "cancel()">
					<spring:message code="btn_cancel" />
				</button>
			</div>
		</form:form>
		</div>
	</div>
<script type="text/javascript">
	$(function() {		
		$("#quantity,#rate,#discount,#CGST,#IGST,#SGST,#state_com_tax,#labour_charges,#freight,#Others,#VAT,#VATCST,#Excise").keypress(function(e) {
			if (!digitsAndDotOnly(e)) {
				return false;
			}
		});
		
	});
	function validate()
	{
		
		var quantity=document.getElementById("quantity").value;
		var rate=document.getElementById("rate").value;
		var discount=document.getElementById("discount").value;
		var SGST=document.getElementById("SGST").value;
		var IGST=document.getElementById("IGST").value;
		var CGST=document.getElementById("CGST").value;
		
		if(quantity.trim()=="" || quantity.trim()=="0"){
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
		else if(discount>100){
			alert("Please Enter Discount Less Than 100");
			return false;
		}
		else if(((SGST!=0 && SGST!="") || (CGST!=0 && CGST!="")) && (IGST!=0 && IGST!=""))
		{
		alert("Either input CGST+SGST or IGST");
		return false;
		}
		else
			{
			return true;
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
		var others=document.getElementById("Others").value;
		
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
	 	if(discount>100)
	 		alert("Discount should be less than 100");
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
		document.getElementById("state_com_tax").value=parseFloat((tamount*sct_hidden)/100).toFixed(2);
		$("#transaction_amount").val(Number(tamount).toFixed(2));   
	}
	function cancel(){
		var receipt_header_id="<c:out value= "${entry.receipt_header_id}"/>";
			window.location.assign('<c:url value="editReceipt"/>?id='+receipt_header_id);
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>