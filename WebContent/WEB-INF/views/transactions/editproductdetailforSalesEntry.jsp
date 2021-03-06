<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>
<div class="breadcrumb">
	<h3>Edit Sales Product</h3>	
		 <a href="homePage">Home</a> ? <a href="editSalesEntry?id=${entry.sales_id}">Sales Voucher</a> ? <a href="#">Edit Sales Product</a>
	</div>	
<div class="col-md-12 wideform">
	<div class="fassetForm">
		<form:form id="editProductForm" action="saveproductdetailforSalesEntry" method="post" commandName = "entry" onsubmit = "return validate();">

			<div class="row">				
				<form:input path="product_id" hidden = "hidden"/>
				<form:input path="sales_id" hidden = "hidden"/>
				<form:input path="sales_detail_id" hidden = "hidden"/>
				<form:input path="transaction_amount" id="transaction_amount" hidden = "hidden"/>
				<form:input path="is_gst" id="is_gst" hidden = "hidden"/>		
						
				<c:choose>
						<c:when test="${entry.is_gst==2}">
							<!-- vat start -->
							<input class="logInput" id = "vat_hidden" placeholder="vat_hidden" type="hidden" value="${productvat.vat}"/>
							<input class="logInput" id = "vatcst_hidden" placeholder="vatcst_hidden" type="hidden" value="${productvat.cst}"/>
							<input class="logInput" id = "excise_hidden" placeholder="excise_hidden" type="hidden" value="${productvat.excise}" />
							<!-- vat end -->
						</c:when>
						<c:otherwise>
							<!-- ////Gst -->
								<c:choose>
											<c:when test="${salesEntry.customer!=null}">
													<c:choose>
														<c:when test="${salesEntry.customer.gst_applicable==true}">
												           <c:choose>					
																<c:when test="${salesEntry.customer.state.state_id!=stateId}">
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
												<input class="logInput" id="cgst_hidden" value="${productgst.cgst}" type="hidden" />
												<input class="logInput" id="sgst_hidden" value="${productgst.sgst}" type="hidden" /> 
												<input class="logInput" id="igst_hidden" value="${productgst.igst}" type="hidden" /> 
												<input class="logInput" id="sct_hidden" value="${productgst.state_comp_cess}" type="hidden" /> 
											</c:otherwise>
							     </c:choose>
							<!-- gst -->
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
				<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" value="${entry.quantity}"
				 var="no_formattedquantity"/>	
					
					<form:input path="quantity" class="logInput" id = "quantity" placeholder="quantity" maxlength="10" 
					 onchange="calculaterate(this.value,1)" onkeypress="return isNumber(event)" value="${no_formattedquantity}"/>
					<span class="logError"><form:errors path="quantity" /></span>
				</div>
			</div>
			
			
			<div class="row">	
				<div class="col-md-2 control-label"><label>Rate<span>*</span></label></div>		
				<div class="col-md-4">
					<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" value="${entry.rate}" var="no_formattedRate"/>	
					<form:input path="rate" class="logInput" id = "rate" placeholder="rate" onchange="calculaterate(this.value,1)" 
					onkeypress="return isNumber(event)" value="${no_formattedRate}" maxlength="10" />
					<span class="logError"><form:errors path="rate" /></span>
				</div>
				<div class="col-md-2 control-label"><label>Discount<span>*</span></label></div>		
				<div class="col-md-4">	
					<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2"
				 value="${entry.discount}" var="no_formatteddiscount"/>
					<form:input path="discount" class="logInput" id = "discount" placeholder="discount"  maxlength="6"
					 onchange="calculaterate(this.value,1)" onkeypress="return isNumber(event)" value="${no_formatteddiscount}"/>
					<span class="logError"><form:errors path="discount" /></span>
				</div>
			</div>
			
			
			<div class="row">	
				<div class="col-md-2 control-label"><label>Labour Charge<span>*</span></label></div>		
				<div class="col-md-4">	
				<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" value="${entry.labour_charges}" 
				var="no_formattedlabour_charges"/>	
					
					<form:input path="labour_charges" class="logInput" id = "labour_charges" placeholder="labour_charges"
					 onchange="calculaterate(this.value,1)" onkeypress="return isNumber(event)" 
					 maxlength="10"  value="${no_formattedlabour_charges}"/>
					<span class="logError"><form:errors path="labour_charges" /></span>
				</div>
				<div class="col-md-2 control-label"><label>Freight Charge<span>*</span></label></div>		
				<div class="col-md-4">	
					<form:input path="freight" class="logInput" id = "freight" placeholder="freight" onchange="calculaterate(this.value,1)" onkeypress="return isNumber(event)"/>
					<span class="logError"><form:errors path="freight" /></span>
				</div>
			</div>
			
			
			<div class="row">	
				<div class="col-md-2 control-label"><label>Other<span>*</span></label></div>		
				<div class="col-md-4">	
				<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" value="${entry.others}" 
				var="no_formattedOthers"/>	
					<form:input path="Others" class="logInput" id = "others" placeholder="Others" onchange="calculaterate(this.value,1)" 
					onkeypress="return isNumber(event)"  maxlength="10"  value="${no_formattedOthers}"/>
					<span class="logError"><form:errors path="Others" /></span>
				</div>
				
				<c:if test="${entry.is_gst==2}">
				<div>
				<div class="col-md-2 control-label"><label>VAT<span>*</span></label></div>		
										<div class="col-md-4">
										<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" 
										value="${entry.VAT}" var="no_formattedVAT"/>	
											<form:input path="VAT" class="logInput" id = "VAT" placeholder="VAT" value="${no_formattedVAT}" maxlength="20"/>
													<span class="logError"><form:errors path="VAT" /></span>
										</div></div></c:if>
										
				<c:if test="${entry.is_gst!=2}">
				<div>
				<div class="col-md-2 control-label"><label>CGST<span>*</span></label></div>		
										<div class="col-md-4">
										<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2"
										 maxFractionDigits="2" value="${entry.CGST}" var="no_formattedCGST"/>	
											<form:input path="CGST" class="logInput" id = "CGST" placeholder="CGST" value="${no_formattedCGST}" maxlength="20"/>
												<span class="logError"><form:errors path="CGST" /></span>
										</div></div></c:if>						
			</div>
			<c:choose>
						<c:when test="${entry.is_gst==2}">
								
									<div class="row">	
										<div class="col-md-2 control-label"><label>CST<span>*</span></label></div>		
										<div class="col-md-4">	
										<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" 
										value="${entry.VATCST}" var="no_formattedVATCST"/>
											<form:input path="VATCST" class="logInput" id = "VATCST" placeholder="VATCST"
											 value="${no_formattedVATCST}" maxlength="20"/>
													<span class="logError"><form:errors path="VATCST" /></span>
										</div>
										<div class="col-md-2 control-label"><label>EXCISE<span>*</span></label></div>		
										<div class="col-md-4">	
											<fmt:formatNumber type="number" groupingUsed = "false"  minFractionDigits="2" maxFractionDigits="2" 
										value="${entry.excise}" var="no_formattedExcise"/>
											<form:input path="Excise" class="logInput" id = "Excise" placeholder="Excise"  value="${no_formattedExcise}" maxlength="20"/>
													<span class="logError"><form:errors path="Excise" /></span>
										</div>
									</div>
									
									
						</c:when>
						<c:otherwise>
								
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
												<form:input path="state_com_tax" class="logInput" id = "state_com_tax" placeholder="state_com_tax" maxlength="20"/>
														<span class="logError"><form:errors path="state_com_tax" /></span>
											</div>
									</div>
						</c:otherwise>
			</c:choose>
			
			 
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
		var is_gst=document.getElementById("is_gst").value;
		var bvalid=true;
		var damount=rate*quantity;
		if(quantity.trim()=="" || quantity.trim()=="0"){
			alert("Please Enter Quantity");
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
		else if(discount>damount){
			alert("Please Enter Discount Less Than damount");
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
	function calculaterate(e,flag){
		var total=e;
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
		var cgst_hidden=document.getElementById("cgst_hidden").value;
		var igst_hidden=document.getElementById("igst_hidden").value;
		var sgst_hidden=document.getElementById("sgst_hidden").value;
		var sct_hidden=document.getElementById("sct_hidden").value;
			}
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
	 	
	 	
	 	
		var damount=rate*quantity;

	 	if(discount!=""){				
	 		discount=parseFloat(damount)-parseFloat((discount));		
		 	
		}
	 	
	 	else
	 		discount=0;
	 	if(discount>damount)
	 		alert("Discount should be less than damount");
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
	 	if(is_gst==2)
	 		{
	 		document.getElementById("VAT").value=parseFloat((tamount*vat_hidden)/100).toFixed(2);
			document.getElementById("VATCST").value=parseFloat((tamount*vatcst_hidden)/100).toFixed(2);
			document.getElementById("Excise").value=parseFloat((tamount*excise_hidden)/100).toFixed(2);
	 		}
	 	else
	 		{
		document.getElementById("CGST").value=parseFloat((tamount*cgst_hidden)/100).toFixed(2);
		document.getElementById("SGST").value=parseFloat((tamount*sgst_hidden)/100).toFixed(2);
		document.getElementById("IGST").value=parseFloat((tamount*igst_hidden)/100).toFixed(2);
		document.getElementById("state_com_tax").value=parseFloat((tamount*sct_hidden)/100).toFixed(2);
	 		}
		$("#transaction_amount").val(Number(tamount).toFixed(2));   
	}
	
	function cancel(){
		var sales_id="<c:out value= "${entry.sales_id}"/>";
			window.location.assign('<c:url value="editSalesEntry"/>?id='+sales_id);
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>