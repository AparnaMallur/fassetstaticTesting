<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_EIGHTEEN%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THIRTY%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>

<div class="breadcrumb">
	<h3>Supplier</h3>
	<a href="homePage">Home</a> »
	<c:if test="${flag==1}">
		 <a href="suppliersList">Supplier</a> 	
	</c:if>
	<c:if test="${flag==2}">
	 <a href="supplierPrimaryValidation">Supplier</a> 	
	</c:if>
	<c:if test="${flag==3}">
	 <a href="supplierSecondaryValidation">Supplier</a> 	
	</c:if>	
	 » <a href="#">View</a>
</div>

<c:if test="${successMsg != null}">
	<div class="successMsg" id = "successMsg"> 
		<strong>${successMsg}</strong>
	</div>
</c:if>
<div class="fassetForm">
		<form:form id="supplierForm"  commandName = "supplier">
			<div class="col-md-12">
				<table class = "table">
					<tr>
						<td><strong>Name:</strong></td>
						<td style="text-align: left;">${supplier.contact_name}</td>
						<td><strong>Email Address:</strong></td>
						<td style="text-align: left;">${supplier.email_id}</td>
						<td><strong>Mobile:</strong></td>
						<td style="text-align: left;"><fmt:formatNumber type="number" pattern="############0"
                                             value="${supplier.mobile}" /></td>					
					</tr>
					<tr>
					<td><strong>Company Name:</strong></td>
						<td style="text-align: left;">${supplier.company_name}</td>
						<td><strong>Company Statutory Type:</strong></td>
						<td style="text-align: left;">${supplier.compStatType.company_statutory_name}</td>
						<td><strong>Industry Type:</strong></td>
						<td style="text-align: left;">${supplier.industryType.industry_name}</td>
					</tr>
					<tr>
						<td><strong>Land line no.:</strong></td>
						<td style="text-align: left;"><fmt:formatNumber type="number" pattern="############0"
                                             value="${supplier.landline_no}" /></td>	
						<td><strong>Owner PAN Number:</strong></td>
						<td style="text-align: left;">${supplier.owner_pan_no}</td>
						<td><strong>Aadhar Number:</strong></td>
						<td style="text-align: left;"><fmt:formatNumber type="number" pattern="############0"
                                             value="${supplier.adhaar_no}" /></td>
					</tr>
					<tr>
						<td><strong>Company PAN Number:</strong></td>
						<td style="text-align: left;">${supplier.company_pan_no}</td>
						<td><strong>GST Applicable:</strong></td>
						<td style="text-align: left;">${supplier.gst_applicable==true ? "Yes" : "No"}</td>	
						<td><strong>GST No:</strong></td>
						<td style="text-align: left;">${supplier.gst_no}</td>	
					</tr>
					<tr>
						<td><strong>Other Tax No:</strong></td>
						<td style="text-align: left;">${supplier.other_tax_no}</td>
						<td><strong>TAN No:</strong></td>							
						<td style="text-align: left;">${supplier.tan_no}</td>	
						<td><strong>Current Address:</strong></td>
						<td style="text-align: left;" >${supplier.current_address}</td>
					</tr>
					<tr>				
						<td><strong>Permanent Address:</strong></td>
						<td style="text-align: left;" >${supplier.permenant_address}</td>
						<td><strong>Country:</strong></td>
						<td style="text-align: left;" >${supplier.country.country_name}</td>
						<td><strong>State:</strong></td>
						<td style="text-align: left;" >${supplier.state.state_name} - ${supplier.state.state_code}</td>
					</tr>
					<tr>
						<td><strong>City:</strong></td>
						<td style="text-align: left;" >${supplier.city.city_name}</td>
						<td><strong>Pin Code:</strong></td>
						<td style="text-align: left;" >${supplier.pincode}</td>
						<td><strong>Reverse Charge Mechanism :</strong></td>
						<td style="text-align: left;" >${supplier.reverse_mecha}</td>

					</tr>
					<tr>
						<td><strong>TDS Applicable :</strong></td>
						<td style="text-align: left;" >${supplier.tds_applicable==1 ? "Yes" : "No"}</td>
						<td><strong>TDS Type:</strong></td>
						<td style="text-align: left;" >${supplier.deductee.deductee_title}</td>
						<td><strong>TDS Rate :</strong></td>
						<td style="text-align: left;" >${supplier.tds_rate}</td>
					</tr>
					<tr>
						<td><strong>Status :</strong></td>
						<td style="text-align: left;" >${supplier.status==true ? "Enable" : "Disable"}</td>
						<td><strong>Closing Balance:</strong></td>
						<td style="text-align: left;">${(supplier.openingbalances.debit_balance)-(supplier.openingbalances.credit_balance)}</td>
						<td><strong>Approval Status:</strong></td>
						<td style="text-align: left;">${supplier.supplier_approval == 0 ? "Pending" : supplier.supplier_approval == 1 ? "Rejected" : supplier.supplier_approval == 2 ? "Primary Approval" : "Approved"}</td>
						
					</tr>
				</table>
			</div>
			<div class="col-md-12">
			
				<div class = "view-panels row">
					<div class="pn-row col-md-12">Sub Ledgers</div>
					<c:forEach var = "subledger" items = "${supplier.subLedgers}">
					<div class="col-md-6 col-xs-6 colrow">
					${subledger.ledger.ledger_name}: ${subledger.subledger_name}
				    </div>
					</c:forEach>
				</div>
				<div class = "view-panels row">
					<div class="pn-row col-md-12">
							Products					
					</div>
					<c:forEach var = "product" items = "${supplier.product}">
					<div class="col-md-6 col-xs-6 colrow">
					${product.product_name}
				    </div>
					</c:forEach>
				</div>
		</div>
			<div class="row"  style = "text-align: center; margin:15px;">
		<c:choose>
		    <c:when test="${(role=='5') || (role=='6') || (role=='7') || (role=='1')}">			
					<c:if test="${((flag==1) && ((supplier.supplier_approval==0) || (supplier.supplier_approval==1)))}">
						<button class="fassetBtn" type="button" onclick = "edit(${supplier.supplier_id})">
							<spring:message code="btn_edit" />
						</button>
					</c:if>	
			</c:when>
			<c:otherwise>
					    <button class="fassetBtn" type="button" onclick = "edit(${supplier.supplier_id})">
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
	
	$(function() {		
	    setTimeout(function() {
	        $("#successMsg").hide()
	    }, 3000);
	});
	
	function edit(id){
		window.location.assign('<c:url value = "editSuppliers"/>?id='+id);	
	}
	
	function back(flag){
		
		if(flag==1)
		{
			<c:if test="${importFlag==true}">  
			window.location.assign('<c:url value = "suppliersList"/>');
			</c:if>
			<c:if test="${importFlag==false}">  
			window.location.assign('<c:url value = "supplierimportfailure"/>');
			</c:if>	
		}
		else if(flag==2)
		{						
			window.location.assign('<c:url value = "supplierPrimaryValidation"/>');			
		}
		else if(flag==3)
		{			
			window.location.assign('<c:url value = "supplierSecondaryValidation"/>');		
		}	
		
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>