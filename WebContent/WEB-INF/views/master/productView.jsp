<%@include file="/WEB-INF/includes/header.jsp"%>

<div class="breadcrumb">
	<h3>Product</h3>
	<a href="homePage">Home</a> »
	<c:if test="${flag==1}">
		 <a href="productList">Product</a> 	
	</c:if>
	<c:if test="${flag==2}">
	 <a href="productPrimaryValidationList">Product</a> 	
	</c:if>
	<c:if test="${flag==3}">
	 <a href="productSecondaryValidationList">Product</a> 	
	</c:if>	
	 » <a href="#">View</a>
</div>	

<div class="fassetForm">
	<form:form commandName = "product">
		<div class="col-md-12">
			<table class = "table">

				<tr>
					<td><strong>Company Name:</strong></td>
					<td style="text-align: left;">${product.company.company_name}</td>
				</tr>
				<tr>
					<td><strong>Product Name:</strong></td>
					<td style="text-align: left;">${product.product_name}</td>			
				</tr>
				<tr>
					<td><strong>Product Type:</strong></td>
					<td style="text-align: left;">
					<c:choose>
                	<c:when test="${product.type == 1}">
             			Goods
             		</c:when>
					<c:when test="${product.type == 2}">
             			Services
             		</c:when>             		
                	<c:otherwise>
						NA          	
					</c:otherwise>
                </c:choose>	</td>			
				</tr>
				<tr>
					<td><strong>Unit of Measurement:</strong></td>
					<td style="text-align: left;">${product.uom.unit}</td>			
				</tr>
				<tr>
					<td><strong>Tax Type:</strong></td>
					<td style="text-align: left;">${product.tax_type==1 ? "GST" : (product.tax_type==2 ? "VAT" : "NA")}</td>			
				</tr>
			<c:if test="${product.gst_id.hsc_sac_code!=null}">
				<tr>
					<td><strong>HSN/SAC Number:</strong></td>
					<td style="text-align: left;">${product.gst_id.hsc_sac_code}</td>			
				</tr>
				<tr>
					<td><strong>CGST:</strong></td>
					<td style="text-align: left;">${product.gst_id.cgst}</td>			
				</tr>
				<tr>
					<td><strong>IGST:</strong></td>
					<td style="text-align: left;">${product.gst_id.igst}</td>			
				</tr>
				<tr>
					<td><strong>SGST:</strong></td>
					<td style="text-align: left;">${product.gst_id.sgst}</td>			
				</tr>
				<tr>
					<td><strong>CESS:</strong></td>
					<td style="text-align: left;">${product.gst_id.state_comp_cess}</td>			
				</tr>
			 </c:if>
			 <c:if test="${product.taxMaster.tax_id!=null}">
			 	<tr>
					<td><strong>Tax Name:</strong></td>
					<td style="text-align: left;">${product.taxMaster.tax_name}</td>			
				</tr>
			     <tr>
					<td><strong>VAT:</strong></td>
					<td style="text-align: left;">${product.taxMaster.vat}</td>			
				</tr>
				<tr>
					<td><strong>CST:</strong></td>
					<td style="text-align: left;">${product.taxMaster.cst}</td>			
				</tr>
				<tr>
					<td><strong>EXCISE:</strong></td>
					<td style="text-align: left;">${product.taxMaster.excise}</td>			
				</tr>
			  </c:if>
				<tr>
					<td><strong>Status:</strong></td>
					<td style="text-align: left;">${product.status==true ? "Enable" : "Disable"}</td>			
				</tr>
					<tr>
					<td><strong>Approval Status:</strong></td>
					<td style="text-align: left;">${product.product_approval == 0 ? "Pending" : product.product_approval  == 1 ? "Rejected" : product.product_approval  == 2 ? "Primary Approval" : "Approved"}</td>			
				</tr>
			</table>
		</div>
		<div class="row"  style = "text-align: center; margin:15px;">
		<c:choose>
		    <c:when test="${(role=='5') || (role=='6') || (role=='7') || (role=='1')}">		
					<c:if test="${(product.allocated != true) && ((product.product_approval==0) || (product.product_approval==1))}">		
					<c:if test="${flag==1}">
						<button class="fassetBtn" type="button" onclick = "edit(${product.product_id})">
						<spring:message code="btn_edit" />
						</button>
					</c:if>	
				</c:if>
			</c:when>
			<c:otherwise>
					    <button class="fassetBtn" type="button" onclick = "edit(${product.product_id})">
						<spring:message code="btn_edit" />
						</button>
			</c:otherwise>
		</c:choose>
			<button class="fassetBtn" type="button" onclick = "back(${flag})">
				<spring:message code="btn_back" />
			</button>
		</div>
	</form:form>
</div>
<script type="text/javascript">

	function edit(id){
		window.location.assign('<c:url value = "editProduct"/>?id='+id);	
	}
	
		
	function back(flag){
		if(flag==1)
		{
			<c:if test="${importFlag==true}">  
			window.location.assign('<c:url value = "productList"/>');
			</c:if>
			<c:if test="${importFlag==false}">  
			window.location.assign('<c:url value = "productimportfailure"/>');
			</c:if>
					
		}
		else if(flag==2)
		{						
			window.location.assign('<c:url value = "productPrimaryValidationList"/>');			
		}
		else if(flag==3)
		{			
			window.location.assign('<c:url value = "productSecondaryValidationList"/>');		
		}	
		
	}
		
	
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>