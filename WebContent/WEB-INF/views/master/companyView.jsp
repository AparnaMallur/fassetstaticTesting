<%@include file="/WEB-INF/includes/header.jsp"%>

<div class="breadcrumb">
	<h3>Company</h3>
	<a href="homePage">Home</a> » <a href="companyList">Company</a> » <a href="#">View</a>
</div>	
<div class="fassetForm">
	<form:form id="companyForm"  commandName = "company">
		<div class="row">
			<table class = "table">	
				
				<tr>
					<td colspan="4" style="text-align: center;" >Corporate Details</td>
				</tr>				
				<tr>
					<td><strong>Company Name:</strong></td>
					<td style="text-align: left;">${company.company_name}</td>	
									
					<td rowspan="3"><strong>Logo:</strong></td>
					<td style="text-align: center;" rowspan="3"><img width="80px" height="80px" src="${userlogo}${company.logo}" /></td>
				</tr>
				
				<tr>	
					<td><strong>Type Of Company:</strong></td>
					<td style="text-align: left;">${company.company_statutory_type.company_statutory_name}</td>								
				</tr>
				
				<tr>
					<td><strong>Industry Type:</strong></td>
					<td style="text-align: left;">${company.industry_type.industry_name}</td>
				</tr>			
				
				<tr>
					<td><strong>Country:</strong></td>
					<td style="text-align: left;">${company.country.country_name}</td>
				</tr>
				<tr>							
					<td><strong>State:</strong></td>
					<td style="text-align: left;">${company.state.state_name}</td>	
					
					<td><strong>Registration Number:</strong></td>
					<td style="text-align: left;">${company.registration_no}</td>				
				</tr>
				<tr>				
					<td><strong>City:</strong></td>
					<td style="text-align: left;">${company.city.city_name}</td>
										
					<td><strong>PAN Number:</strong></td>
					<td style="text-align: left;">${company.pan_no}</td>					
				</tr>
				<tr>					
					<td><strong>Permanent Address:</strong></td>
					<td style="text-align: left;">${company.permenant_address}</td>
										
					<td><strong>Current Address:</strong></td>
					<td style="text-align: left;">${company.current_address}</td>							
				</tr>
				<tr>
					<td><strong>Land-line Number:</strong></td>
					<td style="text-align: left;">${company.landline_no}</td>	
					
					<td><strong>PIN Code:</strong></td>
					<td style="text-align: left;">${company.pincode}</td>		
				</tr>
				<!-- //yearRangeList -->
				<tr>
					<td><strong>Financial Year:</strong></td>
					<c:forEach var = "year" items = "${yearRangeList}">		
					<td style="text-align: left;">
					
					 ${year.year_range} 
					</td>
					</c:forEach>
					<td><strong>Reverse Change Mechanism:</strong></td>
					<td style="text-align: left;">${company.rcm==true ? "Yes" : "No"}</td>		
				</tr>
				
				<tr>
					<td colspan="4" style="text-align: center;" >Legal Details</td>
				</tr>
				<tr>					
					<td><strong>GST Number:</strong></td>
					<td style="text-align: left;">${company.gst_no}</td>
					
					<td><strong>Nature Of Business:</strong></td>
					<td style="text-align: left;">${company.business_nature}</td>		
				</tr>
				<tr>					
					<td><strong>PTE Number:</strong></td>
					<td style="text-align: left;">${company.pte_no}</td>
					
					<td><strong>PTR Number:</strong></td>
					<td style="text-align: left;">${company.ptr_no}</td>			
				</tr>
				<tr>					
					<td><strong>Eway Bill Registration Number:</strong></td>
					<td style="text-align: left;">${company.eway_bill_no}</td>
					
					<td><strong>IEC Number:</strong></td>
					<td style="text-align: left;">${company.iec_no}</td>			
				</tr>
				<tr>					
					<td><strong>Other Tax 1:</strong></td>
					<td style="text-align: left;">${company.other_tax_1}</td>
					
					<td><strong>Other Tax 2:</strong></td>
					<td style="text-align: left;">${company.other_tax_2}</td>						
					
				</tr>
				<tr>					
					<td><strong>Status:</strong></td>
					<td style="text-align: left;">
					${company.status == 0 ? "Disable" : company.status == 1 ? "Pending For Approval" : company.status == 2 ? "Trial Login" : company.status == 3 ? "Subscribed User" :"Disable"}
					</td>	
									
					<td><strong>Amount:</strong></td>
					<td style="text-align: left;">
					<c:forEach var = "user" items = "${company.user}">
				<c:if test="${user.role.role_id==5}">
					${user.amount}
			    </c:if>	 
				</c:forEach>
					</td>	
				</tr>
				
				
			</table>
				
			<c:if test="${quotationDetails.size() >0}">
				<tr>
				<td>	<b style="text-align: center font-size='60';" >Service Details</b> </td>
				</tr>
				
				<table class="table table-bordered table-striped">
				
					<thead>
							<tr>
								<th>Service </th>
								<th>Frequency</th>
							<!-- 	<th>Amount</th>	 -->					
							</tr>
					</thead>
					<tbody>
							<c:forEach var = "quote_list" items = "${quotationDetails}">	
								<tr>
									<td>${quote_list.service_id.service_name}</td>
									<td>${quote_list.frequency_id.frequency_name}</td>
								<%-- 	<td>${quote_list.amount}</td> --%>
								</tr>
							</c:forEach>
					</tbody>
				</table>
			</c:if>
		</div>
		<div class="row">				
			<table id="table" class = "table">
				<thead>
					<tr>	
						<th>User Name</th>
						<th>Email Address</th>	
						<th>Mobile Number</th>
						<th>Status</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach var = "user" items = "${company.user}">
					<tr>					
						<td style="text-align: left;">${user.first_name} ${user.middle_name} ${user.last_name}</td>
						<td style="text-align: left;">${user.email}</td>
						<td style="text-align: left;">${user.mobile_no}</td>
						 <td style="text-align: left;">${user.status==true ? "Enable" : "Disable"}</td>	
					</tr>
				</c:forEach>
				</tbody>
			</table>
		</div>
		<div class="row"  style = "text-align: center; margin:15px;">
			<button class="fassetBtn" type="button" onclick = "edit(${company.company_id})">
				<spring:message code="btn_edit" />
			</button>
			<button class="fassetBtn" type="button" onclick = "back();">
				<spring:message code="btn_back" />
			</button>
		</div>
	</form:form>
</div>
<script type="text/javascript">

	function edit(id){
		window.location.assign('<c:url value = "editCompany"/>?id='+id);	
	}
	
	function back(){
		window.location.assign('<c:url value = "companyList"/>');	
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>