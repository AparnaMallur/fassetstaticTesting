<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>

<div class="breadcrumb">
	<h3>Edit Payment Product</h3>					
	 <a href="homePage">Home</a> » <a href="editPayment?id=${paymentDetails.paymentId}">Payment</a> » <a href="#">Edit Payment Product</a>
	
</div>	
<div class="col-md-12 wideform">
	<div class="fassetForm">
		<form:form id="editProductForm" action="saveproductdetailforPayment" method="post" commandName = "paymentDetails" onsubmit = "return validate();">
			<form:input path="id" id="id" type="hidden" />
			<form:input path="productId" id="productId" type="hidden" />
			<form:input path="transaction_amount" id="transaction_amount" type="hidden" />
			<form:input path="paymentId" id="transaction_amount" type="hidden" />
			<input id="transaction_amount" value="${paymentDetails.transaction_amount}" type="hidden" />
			<c:choose>
			<c:when test="${payment.supplier.gst_applicable==true}">
					<c:choose>
						<c:when test='${payment.supplier.reverse_mecha=="No"}'>
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
					</c:when>					
					<c:otherwise>
									<input class="logInput" id="cgst_hidden" value="0" type="hidden" />
									<input class="logInput" id="sgst_hidden" value="0" type="hidden" /> 
									<input class="logInput" id="igst_hidden" value="0" type="hidden" /> 
									<input class="logInput" id="sct_hidden" value="0" type="hidden" /> 	
					</c:otherwise>
			</c:choose>
			
			<div class="row">	
				<div class="col-md-2 control-label"><label>Product Name</label></div>		
				<div class="col-md-4">
					<input value="${paymentDetails.product_id.product_name}" disabled="disabled"  style="width:100%;"/>
				</div>	
				<div class="col-md-2 control-label"><label>UOM<span>*</span></label></div>		
				<div class="col-md-4">	
					<input value="${paymentDetails.uom_id.unit}" disabled="disabled"  style="width:100%;"/>
				</div>
				
			</div>	
		
			<div class="row">	
				<div class="col-md-2 control-label"><label>HSN/SAC Code<span>*</span></label></div>		
				<div class="col-md-4">	
					<input value="${paymentDetails.product_id.hsn_san_no}" disabled="disabled"  style="width:100%;"/>
				</div>
				
				<div class="col-md-2 control-label"><label>Quantity<span>*</span></label></div>		
				<div class="col-md-4">	
				<form:input path="quantity" class="logInput" id = "quantity" placeholder="quantity"
				 onchange="calculaterate(this.value,1)" onkeypress="return isNumber(event)"/>
					<span class="logError"><form:errors path="quantity" /></span>
				</div>
			</div>
			
			<div class="row">	
				<div class="col-md-2 control-label"><label>Rate<span>*</span></label></div>		
				<div class="col-md-4">
				<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" value="${paymentDetails.rate}"
				 var="no_formattedRate"/>	
					<form:input path="rate" class="logInput" id = "rate" placeholder="rate" onchange="calculaterate(this.value,2)"
					 onkeypress="return isNumber(event)" value="${no_formattedRate}" maxlength="12"/>
				<%-- 	 <form:input path="rate" class="logInput" id = "rate" placeholder="rate" 
					 onchange="calculaterate(this.value,2)" onkeypress="return isNumber(event)"/> --%>
							<span class="logError"><form:errors path="rate" /></span>
				</div>
				
				<div class="col-md-2 control-label"><label>Discount<span>*</span></label></div>		
				<div class="col-md-4">	
				<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2"
				 value="${paymentDetails.discount}" var="no_formattedDiscount"/>	
				<form:input path="discount" class="logInput" id = "discount" placeholder="discount" 
				onchange="calculaterate(this.value,3)" onkeypress="return isNumber(event)" value="${no_formattedDiscount}" maxlength="6"/>
					<%-- <form:input path="discount" class="logInput" id = "discount" placeholder="discount"  maxlength="6" 
					onchange="calculaterate(this.value,3)" onkeypress="return isNumber(event)" value="${no_formattedDiscount}" maxlength="6"/> --%>
					<span class="logError"><form:errors path="discount" /></span>
				</div>
			</div>
			
			<div class="row">	
				<div class="col-md-2 control-label"><label>Labour Charge<span>*</span></label></div>		
				<div class="col-md-4">	
				<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" 
				value="${paymentDetails.labour_charges}" var="no_formattedlabour_charges"/>	
				
					<form:input path="labour_charges" class="logInput" id = "labour_charges" placeholder="labour_charges" 
					onchange="calculaterate(this.value,4)" onkeypress="return isNumber(event)" value="${no_formattedlabour_charges}" maxlength="10"/>
					<span class="logError"><form:errors path="labour_charges" /></span>
				</div>
				
				<div class="col-md-2 control-label"><label>Freight Charge<span>*</span></label></div>		
				<div class="col-md-4">
				<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" 
				value="${paymentDetails.frieght}" var="no_formattedfrieght"/>	
					
					<form:input path="frieght" class="logInput" id = "frieght" placeholder="frieght" 
					onchange="calculaterate(this.value,5)" onkeypress="return isNumber(event)"  value="${no_formattedfrieght}" maxlength="10"/>
					<span class="logError"><form:errors path="frieght" /></span>
				</div>
			</div>
		
			<div class="row">	
				<div class="col-md-2 control-label"><label>Other<span>*</span></label></div>		
				<div class="col-md-4">
				<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" 
				value="${paymentDetails.others}" var="no_formattedothers"/>	
					
					<form:input path="others" class="logInput" id = "others" placeholder="others" onchange="calculaterate(this.value,6)"
					 onkeypress="return isNumber(event)"  value="${no_formattedothers}" maxlength="10"/>
					<span class="logError"><form:errors path="others" /></span>
				</div>
				
				<div class="col-md-2 control-label"><label>CGST<span>*</span></label></div>		
				<div class="col-md-4">
				<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" 
				value="${paymentDetails.cgst}" var="no_formattedcgst"/>	
					<form:input path="cgst" class="logInput" id = "cgst" placeholder="cgst" value="${no_formattedcgst}"/>
					<span class="logError"><form:errors path="cgst" /></span>
				</div>
			</div>
			
			<div class="row">	
				<div class="col-md-2 control-label"><label>SGST<span>*</span></label></div>		
				<div class="col-md-4">
				<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" 
				 value="${paymentDetails.sgst}" var="no_formattedsgst"/>	
					<form:input path="sgst" class="logInput" id = "sgst" placeholder="sgst"  value="${no_formattedsgst}"/>
					<span class="logError"><form:errors path="sgst" /></span>
				</div>
				
				<div class="col-md-2 control-label"><label>IGST<span>*</span></label></div>		
				<div class="col-md-4">	
				<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" value="${paymentDetails.igst}" var="no_formattedigst"/>
					<form:input path="igst" class="logInput" id = "igst" placeholder="igst" value="${no_formattedigst}" />
					<span class="logError"><form:errors path="igst" /></span>
				</div>
			</div>
		
			<div class="row">	
				<div class="col-md-2 control-label"><label>CESS<span>*</span></label></div>		
				<div class="col-md-4">
				<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2"
				 value="${paymentDetails.state_com_tax}" var="no_formattedstate_com_tax"/>	
					<form:input path="state_com_tax" class="logInput" id = "state_com_tax" placeholder="state_com_tax" 
					value="${no_formattedstate_com_tax}"/>
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
		$("#quantity,#rate,#discount,#CGST,#IGST,#SGST,#state_com_tax,#labour_charges,#frieght,#Others,#VAT,#VATCST,#Excise").keypress(function(e) {
			if (!digitsAndDotOnly(e)) {
				return false;
			}
		});
		
	});
	
	function cancel(){
		var payment_id="<c:out value= "${paymentDetails.paymentId}"/>";
		window.location.assign('<c:url value="editPayment"/>?id='+payment_id);
		
	}
	function validate()
	{
		var quantity=document.getElementById("quantity").value;
		var rate=document.getElementById("rate").value;
		var discount=document.getElementById("discount").value;
		var SGST=document.getElementById("sgst").value;
		var IGST=document.getElementById("igst").value;
		var CGST=document.getElementById("cgst").value;
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
		var cgst_hidden=document.getElementById("cgst_hidden").value;
		var igst_hidden=document.getElementById("igst_hidden").value;
		var sgst_hidden=document.getElementById("sgst_hidden").value;
		var sct_hidden=document.getElementById("sct_hidden").value;
		var quantity=document.getElementById("quantity").value;
		var rate=document.getElementById("rate").value;
		var labour_charges=document.getElementById("labour_charges").value;
		var discount=document.getElementById("discount").value;
		var frieght=document.getElementById("frieght").value;
		var others=document.getElementById("others").value;
		
		if(rate!="")
	 		rate=parseFloat(rate);
		if(quantity!="")
	 		quantity=parseFloat(quantity);
		if(labour_charges!="")
			labour_charges=parseFloat(labour_charges);
		if(discount!="")
			discount=parseFloat(discount);
		if(frieght!="")
			frieght=parseFloat(frieght);
		if(others!="")
			others=parseFloat(others);
		
		var amount = (rate*quantity)+labour_charges+frieght+others;
	 	var damount=rate*quantity;
		if(discount!=""){				
			var disamount=parseFloat(damount)-parseFloat((damount*discount)/100);		
		 	amount=parseFloat(disamount)+labour_charges+frieght+others;
		}
		
		if(discount>100)
	 		alert("Discount should be less than 100");
		document.getElementById("transaction_amount").value=amount;	 
		
		$("#cgst").val(parseFloat((amount*cgst_hidden)/100).toFixed(2));
		
		$("#sgst").val(parseFloat((amount*sgst_hidden)/100).toFixed(2));
		
		$("#igst").val(parseFloat((amount*igst_hidden)/100).toFixed(2));
		
		$("#state_com_tax").val(parseFloat((amount*sct_hidden)/100).toFixed(2));	

	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>