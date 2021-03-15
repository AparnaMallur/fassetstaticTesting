<%@include file="/WEB-INF/includes/header.jsp"%>
<div class="breadcrumb">
	<h3>User</h3>
	<!-- <a href="homePage">Home</a> » <a href="#">User</a> » <a href="#">View</a> -->
</div>	
<div class="fassetForm">
	<form:form id="userForm"  commandName = "userform">
		<div class="row">
			<table class = "table">
				<tr>
					<td colspan="4" style="text-align: center;" >Personal Details</td>
				</tr>
				<tr>
					<td><strong>Company Name:</strong></td>
					<td style="text-align: left;">${userform.company.company_name}</td>	
					<td><strong>User Name:</strong></td>
					<td style="text-align: left;">${userform.user.first_name} ${userform.user.middle_name} ${userform.user.last_name}</td>
										
				</tr>
				
				<tr>
				
					<td><strong>Email Address:</strong></td>
					<td style="text-align: left;">${userform.user.email}</td>	
					
					<td><strong>Mobile Number:</strong></td>
					<td style="text-align: left;">${userform.user.mobile_no}</td>
					
				</tr>
				
				<tr>			
					
					<td><strong>Land-line Number:</strong></td>
					<td style="text-align: left;">${userform.company.landline_no}</td>		
							
				
					
					
					<td><strong>PAN Number:</strong></td>
					<td style="text-align: left;">${userform.user.pan_no}</td>
				</tr>
				
				<tr>
					<td colspan="4" style="text-align: center;" >Corporate Details</td>
				</tr>
				
				<tr>
						<td><strong>Aadhar Number:</strong></td>
					<td style="text-align: left;">${userform.user.adhaar_no}</td>
									
					<td rowspan="3"><strong>Logo:</strong></td>
					<td style="text-align: center;" rowspan="3">
					   <c:choose>
			                	<c:when test="${userform.company.logo != null}">
			                				<img width="80px" height="80px" src="${userlogo}${userform.company.logo}" />
			                	</c:when>
			                	<c:otherwise>
			                			No Logo Uploaded
			                	</c:otherwise>
			           </c:choose>
					
					</td>
				</tr>
				
				<tr>	
					<td><strong>Type Of Company:</strong></td>
					<td style="text-align: left;">${userform.company.company_statutory_type.company_statutory_name}</td>								
				</tr>
				
				<tr>
					<td><strong>Industry Type:</strong></td>
					<td style="text-align: left;">${userform.company.industry_type.industry_name}</td>
				</tr>			
				
				<tr>
					<td><strong>Country:</strong></td>
					<td style="text-align: left;">${userform.company.country.country_name}</td>
					<td><strong>Employee Limit:</strong></td>
					<td style="text-align: left;">${userform.company.empLimit}</td>		
				</tr>
				<tr>							
					<td><strong>State:</strong></td>
					<td style="text-align: left;">${userform.company.state.state_name} - ${userform.company.state.state_code}</td>	
					
					<td><strong>Registration Number:</strong></td>
					<td style="text-align: left;">${userform.company.registration_no}</td>				
				</tr>
				<tr>				
					<td><strong>City:</strong></td>
					<td style="text-align: left;">${userform.company.city.city_name}</td>
										
					<td><strong>PAN Number:</strong></td>
					<td style="text-align: left;">${userform.company.pan_no}</td>					
				</tr>
				<tr>					
					<td rowspan="2"><strong>Permanent Address:</strong></td>
					<td style="text-align: left;" rowspan="2">${userform.company.permenant_address}</td>
					
					<td><strong>PIN Code:</strong></td>
					<td style="text-align: left;">${userform.company.pincode}</td>			
				</tr>
				<tr>
					<td><strong>Reverse Change Mechanism:</strong></td>
					<td style="text-align: left;">${userform.company.rcm==true ? "Yes" : "No"}</td>
							
				</tr>
				<tr>					
					<td rowspan="2"><strong>Current Address:</strong></td>
					<td style="text-align: left;" rowspan="2">${userform.company.current_address}</td>
					
					<td><strong>Financial Year:</strong></td>
					<td style="text-align: left;">
			    	<c:forEach var = "year" items = "${yearlist}">		
			    	   <ul><li>${year.year_range}</li></ul> 				
					</c:forEach>	
			      </td>	
				</tr>
				<tr></tr>
				<tr>
					<td colspan="4" style="text-align: center;" >Legal Details</td>
				</tr>
				<tr>					
					<td><strong>GST Number:</strong></td>
					<td style="text-align: left;">${userform.company.gst_no}</td>
					
					<td><strong>Nature Of Business:</strong></td>
					<td style="text-align: left;">${userform.company.business_nature}</td>		
				</tr>
				<tr>					
					<td><strong>PTE Number:</strong></td>
					<td style="text-align: left;">${userform.company.pte_no}</td>
					
					<td><strong>PTR Number:</strong></td>
					<td style="text-align: left;">${userform.company.ptr_no}</td>			
				</tr>
				<tr>					
					<td><strong>Eway Bill Registration Number:</strong></td>
					<td style="text-align: left;">${userform.company.eway_bill_no}</td>
					
					<td><strong>IEC Number:</strong></td>
					<td style="text-align: left;">${userform.company.iec_no}</td>			
				</tr>
				<tr>					
					<td><strong>Other Tax 1:</strong></td>
					<td style="text-align: left;">${userform.company.other_tax_1}</td>
					
					<td><strong>Other Tax 2:</strong></td>
					<td style="text-align: left;">${userform.company.other_tax_2}</td>						
					
				</tr>
				
			</table>
		</div>
		<div class="row"  style = "text-align: center; margin:15px;">
			<button class="fassetBtn" type="button" onclick = "edit()">
				<spring:message code="btn_edit" />
			</button>
			<button class="fassetBtn" type="button" onclick = "back();">
				<spring:message code="btn_back" />
			</button>
		</div>
	</form:form>
</div>
<script type="text/javascript">

	function edit(){
		window.location.assign('<c:url value = "userProfile"/>');	
	}
	
	function back(){
		window.location.assign('<c:url value = "homePage"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>