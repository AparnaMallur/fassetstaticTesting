<%@include file="/WEB-INF/includes/header.jsp"%>
<script type="text/javascript" src="${valid}"></script>
    <!-- Tags Input -->
    <link href="resources/css/bootstrap-tagsinput.css" rel="stylesheet">
    <script src="resources/js/bootstrap-tagsinput.js"></script>
<spring:url value="/resources/images/closeRed.png" var="deleteImg" />    
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<c:set var="SIZE_12"><%=MyAbstractController.SIZE_12%></c:set>
<c:set var="SIZE_13"><%=MyAbstractController.SIZE_13%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>
<c:set var="SIZE_15"><%=MyAbstractController.SIZE_15%></c:set>
<c:if test="${successMsg != null}">
	<div class="successMsg" id = "successMsg"> 
		<strong>${successMsg}</strong>
	</div>
</c:if>

<script>
 
var serviceDetails = [];
function deleteRow(e, serviceId, frequencyId){
	
	if(frequencyId>0 || serviceId>0){
		
       $(e).closest('tr').remove();
		
		for(i = 0; i<serviceDetails.length;i++){
			if(serviceDetails[i].serviceId == serviceId){
				serviceDetails.splice(i,1);
			}
			console.log(JSON.stringify(serviceDetails));
			}
		$("#serviceFreq").val(JSON.stringify(serviceDetails)); 
	}
	
}
function deleteService(id)
{
	window.location.assign('<c:url value="deleteServicedetail"/>?id='+id);
	
}

  function addService(){
			
		var frequencyId = $("#select_frequency_id").val();
		var serviceId = $("#select_service_id").val();
	
		var service_name = "";
		var frequency_name = "";
		var service_id;
		var frequency_id;
		
		if(serviceId == 0){
			alert("Please select service.");
			return false;
		}else if(frequencyId == 0){
			alert("Please select frequency.");
			return false; 
		}
		else{
			var flag = false;
			for(var i=parseInt(0);i<serviceDetails.length;i++) {
				var tempserviceId = serviceDetails[i].serviceId;
				
				if(tempserviceId==serviceId)
					{
					flag=true;
					break;
					}
	
			      }	
			
			<c:forEach var="quotdetails"  items="${quotationDetails}" >
		       if(('${quotdetails.service_id.id}' == serviceId))
				{
				flag=true;
				} 
				
			</c:forEach>
			
			if(flag==false)
				{
				<c:forEach var="service"  items="${serviceList}" >
				 service_id = ${service.id};
				if(service_id == serviceId ) 
				{
					 service_name = "${service.service_name}" ;
				}
	  		 </c:forEach>
	  		 
	  		 
	  		<c:forEach var="frequency" items="${frequencyServiceList}" >
	  		 frequency_id = ${frequency.frequency_id} ;
				if(frequency_id == frequencyId){
					frequency_name = "${frequency.frequency_name}";
				}
		  	 </c:forEach>
		  	 
			
	  		serviceDetails.push({
				
				"serviceId":serviceId,
				"frequencyId":frequencyId,
				"service_name":service_name,
				"frequency_name":frequency_name
				});		
			
			 $("#serviceFreq").val(JSON.stringify(serviceDetails)); 
			console.log($("#serviceFreq").val());
			
		
		    var markup = "<tr><td><a href = '#' onclick = 'deleteRow(this,"+serviceId+","+frequencyId+")'><img src='${deleteImg}' style = 'width: 20px;'/></a></td>"
		  		+"<td>"+service_name+"</td>"+
		    	"<td>"+frequency_name+"</td>"+
				"</tr>";
				
	
			$('#detailTable tr:last').after(markup);
			document.getElementById("detailTable").style.display="table";
			

			var frequencyId = $("#select_frequency_id").val("0");
			var serviceId = $("#select_service_id").val("0");
				}
			else
				{
				alert("Service is already added.");
				var frequencyId = $("#select_frequency_id").val("0");
				var serviceId = $("#select_service_id").val("0");
				$("#serviceFreq").val(JSON.stringify(serviceDetails)); 
				return false; 
				}
		}
		
		
}
  
</script>
<div class="breadcrumb">
	<h3>Company</h3>
	<a href="homePage">Home</a> » <a href="companyList">Company</a> » <a href="#">Create</a>
</div>
<div class="col-md-12 wideform">
	<div class="fassetForm">
		<form:form id="userForm" action="saveCompany" method="post" enctype="multipart/form-data" commandName = "userform" onsubmit="return addProduct();">
			<div class="row">
			
		<!--  	<c:if test="${company.company_id != null}">
	
		
	
 </c:if>-->
				
			 <form:input path="user.user_id"  hidden = "hidden" />
				
				<form:input path="company.company_id" hidden = "hidden"/>
				<form:input path="company.serviceFreq" hidden="hidden"  id = "serviceFreq"/>
				<form:input path="company.serviceList" id="servicename_id"	hidden="hidden" />
				<form:input path="company.frequencyServiceList" id="frequencyname_id" hidden="hidden" />
					<form:input path="company.subscription_from" id="from_date" hidden="hidden" />
				<form:input path="company.subscription_to" id="to_date" hidden="hidden" />
			
			</div>
						<div class="row">	
				<div class="col-md-3 control-label"><label>Company Name<span>*</span></label></div>		
				<div class="col-md-9">
					<form:input path="company.company_name" class="logInput" id = "company_name" maxlength="40" placeholder="Company Name" />
					<span class="logError"><form:errors path="company.company_name" /></span>
				</div>	
			</div>
			<div class="row">	
				<div class="col-md-3 control-label"><label>Contact Person's First Name<span>*</span></label></div>		
				<div class="col-md-9">
					<form:input path="user.first_name" class="logInput" id = "first_name" placeholder="First Name" maxlength="20" pattern='[A-Za-z\\s]*'/>
					<span class="logError"><form:errors path="user.first_name" /></span>
				</div>	
			</div>
						
			<div class="row">	
				<div class="col-md-3 control-label"><label>Contact Person's Middle Name</label></div>		
				<div class="col-md-9">
					<form:input path="user.middle_name" class="logInput" id = "middle_name" placeholder="Middle Name" maxlength="20" pattern='[A-Za-z\\s]*'/>
					<span class="logError"><form:errors path="user.middle_name" /></span>
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>Contact Person's Last Name<span>*</span></label></div>		
				<div class="col-md-9">
					<form:input path="user.last_name" class="logInput" id = "last_name" placeholder="Last Name" maxlength="20" pattern='[A-Za-z\\s]*'/>
					<span class="logError"><form:errors path="user.last_name" /></span>
				</div>	
			</div>
			
			<div class="row">	
				<div class="col-md-3 control-label"><label>Password<span>*</span></label></div>		
				<div class="col-md-9">
				<div class="input-group m-b-15 passwd">
	               <div class="bootstrap-timepicker">
					<form:password path="user.password" class="logInput" maxlength="${SIZE_TEN}" id = "password" placeholder="Password" autocomplete="off"/>
					<span class="logError"><form:errors path="user.password" /></span>
					</div>
					   <span class="input-group-addon">		
                                	 <i class="fa fa-eye" onmouseover="mouseoverPass(1);" onmouseout="mouseoutPass(1);" ></i>
						</span>
					</div>
				</div>	
			</div>

			<div class="row">	
				<div class="col-md-3 control-label"><label>Mobile Number<span>*</span></label></div>		
				<div class="col-md-9">
					<form:input path="user.mobile_no" class="logInput" id = "mobile_no" maxlength="${SIZE_13}" minlength="${SIZE_TEN}" placeholder="Mobile Number" />
					<span class="logError"><form:errors path="user.mobile_no" /></span>
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>Landline Number</label></div>		
				<div class="col-md-9">
					<form:input type="number" path="company.landline_no" class="logInput" id = "landline_no"  maxlength="${SIZE_13}"  placeholder="Landline Number" />
					<span class="logError"><form:errors path="company.landline_no" /></span>
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>Email ID<span>*</span></label></div>		
				<div class="col-md-9">
					<form:input path="user.email" class="logInput" id = "email" placeholder="Email Address" autocomplete="off"/>
					<span class="logError"><form:errors path="user.email" /></span>
				</div>	
			</div>	
			
			<div class="row">	
				<div class="col-md-3 control-label"><label>Aadhar Number<span>*</span></label></div>		
				<div class="col-md-9">
					<form:input path="user.adhaar_no" class="logInput" id = "adhaar_no" placeholder="Aadhar Number" maxlength="${SIZE_12}" />
					<span class="logError"><form:errors path="user.adhaar_no" /></span>
				</div>	
			</div>
			
			<div class="row">	
				<div class="col-md-3 control-label"><label>Employee Limit<span>*</span></label></div>		
				<div class="col-md-9">
					<form:input path="company.empLimit" class="logInput" id = "empLimit" placeholder="Employee Limit" maxlength="${SIZE_12}" />
					<span class="logError"><form:errors path="company.empLimit"/></span>
				</div>	
			</div>		
			<div class="row">	
				<div class="col-md-3 control-label"><label>Amount<span>*</span></label></div>		
				<div class="col-md-9">
					<form:input path="user.amount" class="logInput" id = "amount" placeholder="Amount"  maxlength="${SIZE_13}"/>
					<span class="logError"><form:errors path="user.amount" /></span>
				</div>	
			</div>			
			
			<div class="row">	
				<div class="col-md-3 control-label"><label>Super User PAN Number<span>*</span></label></div>		
				<div class="col-md-9">
					<form:input path="user.pan_no" class="logInput" id = "pan_no" placeholder="PAN Number" maxlength="${SIZE_TEN}"/>
					<span class="logError"><form:errors path="user.pan_no" /></span>
				</div>	
			</div>

			
			<div class = "row">
				<div class="col-md-3 control-label"><label>Financial Year<span>*</span></label></div>
				<div class = "col-md-9">
					<form:select path="company.yearRange" id="yearrangeId" class="logInput"  multiple="multiple">
						<form:option value="0" label="Select Financial Year"/>		
						<c:forEach var = "year" items = "${yearRangeList}">							
							<form:option value = "${year.year_id}">${year.year_range}</form:option>	
						</c:forEach>									
					</form:select>						
						<span class="logError"><form:errors path="company.yearRange" /></span>	
				</div>
			</div>
			
			<div class="row">
				<div class="col-md-3 control-label">
					<label>Subscription From<span>*</span></label>
				</div>
				<div class="col-md-9">
				<form:input path="company.subscription_from" class="logInput" id = "fromDate" onchange="setDate(this)"  placeholder="Subscription From Date"  />
					<span class="logError"><form:errors path="company.subscription_from" /></span>
				</div>
			</div>
			<div class="row">
				<div class="col-md-3 control-label">
					<label>Subscription To<span>*</span></label>
				</div>
				<div class="col-md-9">
				<form:input path="company.subscription_to" class="logInput" id = "toDate"  onchange="setDate1(this)" placeholder=" Subscription  To Date" />
				<span class="logError"><form:errors path="company.subscription_to" /></span>
				
				</div>
			</div>				
			<div class = "row">	
				<div class="col-md-3 control-label"><label>Type of Company<span>*</span></label></div>
				<div class = "col-md-9">										
					<form:select path = "company.companyTypeId" class="logInput">
						<form:option value="0" label="Type of Company" id="companyTypeId"/>		
						<c:forEach var = "companyType" items = "${companyTypeList}">							
							<form:option value = "${companyType.company_statutory_id}">${companyType.company_statutory_name}</form:option>	
						</c:forEach>									
					</form:select>
					<span class="logError"><form:errors path="company.companyTypeId" /></span>
				</div>
			</div>		
			
			
			<div class = "row">	
				<div class="col-md-3 control-label"><label>Industry Type<span>*</span></label></div>
				<div class = "col-md-9">										
					<form:select path = "company.industryTypeId" class="logInput">
						<form:option value="0" label="Industry Type" id="industryTypeId"/>		
						<c:forEach var = "industryType" items = "${industryTypeList}">							
							<form:option value = "${industryType.industry_id}">${industryType.industry_name}</form:option>	
						</c:forEach>									
					</form:select>
					<span class="logError"><form:errors path="company.industryTypeId" /></span>
				</div>
			</div>
			<div class = "row">	
				<div class="col-md-3 control-label"><label>Country<span>*</span></label></div>
				<div class = "col-md-9">										
					<form:select path = "company.countryId" class="logInput"
					placeholder="Country Name" onChange="getStateList(this.value)" id="country">
								<form:option value="0" label="Select Country Name" />
								<c:forEach var="Country" items="${countryList}">
									<form:option value="${Country.country_id}">${Country.country_name}  </form:option>
								</c:forEach>
							</form:select>
					<span class="logError"><form:errors path="company.countryId" /></span>
				</div>
			</div>
			<div class = "row">	
				<div class="col-md-3 control-label"><label>State<span>*</span></label></div>
				<div class = "col-md-9">										
					<form:select path = "company.stateId" class="logInput"
					placeholder="State Name" id="stateId" onChange='getCityList(this.value)'>
								<form:option value="0" label="Select State Name" />
							</form:select>
					<span class="logError"><form:errors path="company.stateId" /></span>
				</div>
			</div>
			<div class = "row">	
				<div class="col-md-3 control-label"><label>City<span>*</span></label></div>
				<div class = "col-md-9">										
					<form:select path = "company.cityId" class="logInput" id="cityId">
						<form:option value="0" label="Select City"/>											
					</form:select>
					<span class="logError"><form:errors path="company.cityId" /></span>
				</div>
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>Current Address<span>*</span></label></div>		
				<div class="col-md-9">
				<form:textarea path="company.current_address" class="logInput"
								id="current_address" rows="3" placeholder="Current Address" maxlength="200"></form:textarea>
					<span class="logError"><form:errors path="company.current_address" /></span>
				</div>	
			</div>	
			<div class="row">
				<div class="col-md-3"></div>
				<div class="col-md-9">
					<div class="checkbox checkbox-primary">
						<input id="checkbox2" type="checkbox" onclick="check(this)">
						<label> Same as current address </label>
					</div>
				</div>
				<div class="col-md-3 control-label"><label>Permanent Address<span>*</span></label></div>		
				<div class="col-md-9">
				<form:textarea path="company.permenant_address" class="logInput"
								id="permenant_address" rows="3" placeholder="Permanent Address"></form:textarea>
				<span class="logError"><form:errors path="company.permenant_address" /></span>
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>PIN Code<span>*</span></label></div>		
				<div class="col-md-9">
					<form:input path="company.pincode" class="logInput" id = "pincode" maxlength="${SIZE_SIX}" placeholder="PIN Code" />
					<span class="logError"><form:errors path="company.pincode" /></span>
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>Company PAN Number<span>*</span></label></div>		
				<div class="col-md-9">
					<form:input path="company.pan_no" class="logInput" id = "pan_no" placeholder="PAN Number" maxlength="${SIZE_TEN}"/>
					<span class="logError"><form:errors path="company.pan_no" /></span>
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>Registration Number</label></div>		
				<div class="col-md-9">
					<form:input path="company.registration_no" class="logInput" id = "registration_no" placeholder="Registration Number" maxlength="20"/>
					<span class="logError"><form:errors path="company.registration_no" /></span>
				</div>	
			</div>
			<div class = "row">	
				<div class="col-md-3 control-label"><label>Reverse Charge Mechanism</label></div>
				<div class = "col-md-9">										
					<form:select path = "company.rcm" class="logInput">							
						<form:option value = "false">No</form:option>
						<form:option value = "true">Yes</form:option>
					</form:select>
					<span class="logError"><form:errors path="company.rcm" /></span>
				</div>
			</div>
			<div class = "row">	
				<div class="col-md-3 control-label"><label>Bank</label></div>
				<div class = "col-md-9">										
					<form:select path = "company.bankId" id = "bankId" class="logInput">
						<form:option value="0" label="Select Bank"/>		
						<c:forEach var = "bank" items = "${bankList}">							
							<form:option value = "${bank.bank_id}">${bank.bank_name} - ${bank.account_no}</form:option>	
						</c:forEach>									
					</form:select>
					<span class="logError"><form:errors path="company.bankId" /></span>
				</div>
			</div>
			<%-- <div class = "row">
				<div class="col-md-3 control-label"><label>Financial Year</label></div>
				<div class = "col-md-9">
					<form:select path = "company.yearRangeId" class="logInput">
						<form:option value="0" label="Select Financial Year"/>		
						<c:forEach var = "yearRange" items = "${yearRangeList}">							
							<form:option value = "${yearRange.year_id}">${yearRange.year_range}</form:option>	
						</c:forEach>									
					</form:select>
					<span class="logError"><form:errors path="company.yearRangeId" /></span>	
				</div>
			</div>	 --%>
			<div class = "row">	
				<div class="col-md-3 control-label"><label>Voucher Serial No<span>*</span></label></div>
				<div class = "col-md-9">					
				<form:input path="company.voucher_range" class="tagsinput form-control" id = "voucher_range" placeholder="Voucher Number Range" />
	<!-- <input class="tagsinput form-control" type="text" value="Amsterdam,Washington,Sydney,Beijing,Cairo"/>	 -->		                           

					<span class="logError"><form:errors path="company.voucher_range" /></span>
				</div>
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>Nature of Business</label></div>		
				<div class="col-md-9">
					<form:input path="company.business_nature" class="logInput" id = "business_nature" placeholder="Nature of Business" onkeypress="return selectNature()" maxlength="255" />
					<span class="logError"><form:errors path="company.business_nature" /></span>
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>GST Number</label></div>		
				<div class="col-md-9">
					<form:input path="company.gst_no" class="logInput" id = "gst_no" placeholder="GST Number" maxlength="${SIZE_15}"  />
					<span class="logError"><form:errors path="company.gst_no" /></span>
				</div>	
			</div>
			<div class="row">	
				<div class="col-md-3 control-label"><label>PTE Number</label></div>		
				<div class="col-md-9">
					<form:input path="company.pte_no" class="logInput" id = "pte_no" placeholder="PTE Number"  maxlength="${SIZE_15}"   />
					<span class="logError"><form:errors path="company.pte_no" /></span>
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>PTR Number</label></div>		
				<div class="col-md-9">
					<form:input path="company.ptr_no" class="logInput" id = "ptr_no" placeholder="PTR Number"   maxlength="${SIZE_15}" />
					<span class="logError"><form:errors path="company.ptr_no" /></span>
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>Eway Bill Registration Number</label></div>		
				<div class="col-md-9">
					<form:input path="company.eway_bill_no" class="logInput" id = "eway_bill_no" placeholder="Eway Bill Registration Number"   maxlength="${SIZE_15}" />
					<span class="logError"><form:errors path="company.eway_bill_no" /></span>
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>IEC Number</label></div>		
				<div class="col-md-9">
					<form:input path="company.iec_no" class="logInput" id = "iec_no" placeholder="IEC Number"  maxlength="${SIZE_15}"  />
					<span class="logError"><form:errors path="company.iec_no" /></span>
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>Other Tax 1</label></div>		
				<div class="col-md-9">
					<form:input path="company.other_tax_1" class="logInput" id = "other_tax_1" placeholder="Other Tax 1"   maxlength="${SIZE_15}" />
					<span class="logError"><form:errors path="company.other_tax_1" /></span>
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>Other Tax 2</label></div>		
				<div class="col-md-9">
					<form:input path="company.other_tax_2" class="logInput" id = "other_tax_2" placeholder="Other Tax 2"  maxlength="${SIZE_15}"  />
					<span class="logError"><form:errors path="company.other_tax_2" /></span>
				</div>	
			</div>
			<div class = "row">	
				<div class="col-md-3 control-label"><label>Company Status<span>*</span></label></div>
				<div class = "col-md-9">										
					<form:select path = "company.status" class="logInput">
						<form:option value="0">Select Status</form:option>
						<form:option value="<%=MyAbstractController.STATUS_INACTIVE%>">Disable</form:option>
						<form:option value="<%=MyAbstractController.STATUS_PENDING_FOR_APPROVAL%>">Pending for Approval</form:option>
						<form:option value="<%=MyAbstractController.STATUS_SUBSCRIBED_USER%>">Subscribed User</form:option>
						<form:option value="<%=MyAbstractController.STATUS_TRIAL_LOGIN%>">Trial Login</form:option>																				
					</form:select>
					<span class="logError"><form:errors path="company.status" /></span>
				</div>
			</div>
			<div class="row">	
				<div class="col-md-3 control-label"><label>Logo</label></div>		
				<div class="col-md-9">
					<input id="logo" class="logInput" name="logo" type="file" accept=".jpg, .png, .jpeg|images/*">
					
				</div>	
			</div>
			
			<c:if test="${quotationDetails.size() >0}">
				<table class="table table-bordered table-striped">
					<thead>
							<tr>
								<th>Action </th>
								<th>Service </th>
								<th>Frequency</th>
							<!-- 	<th>Amount</th>	 -->					
							</tr>
					</thead>
					<tbody>
							<c:forEach var = "quote_list" items = "${quotationDetails}">	
								<tr>
										<td><a href = "#" onclick = "deleteService(${quote_list.service_id.id})"><img src='${deleteImg}' style = 'width: 20px;'/></a></td>
									<td>${quote_list.service_id.service_name}</td>
									<td>${quote_list.frequency_id.frequency_name}</td>
								<%-- 	<td>${quote_list.amount}</td> --%>
								</tr>
							</c:forEach>
					</tbody>
				</table>
			</c:if>
			
			<table class="table table-bordered table-striped"  id="detailTable">
						<thead>
							<tr>
								<th>Action</th>
								<th>Service</th>
								<th>Frequency</th>
							</tr>
						</thead>
						<tbody>
						
						</tbody>
			 </table>
			 
			 <c:if test="${!empty serviceList}">
							<div class="row" style="background-color: #eee; padding: 5px" >
								<div class="col-md-6 ">
								<h5 align="center">	Service Name:</h5>
									<form:select path="company.serviceList" class="logInput"
										id="select_service_id" placeholder="Service Name" >
										<form:option value="0" label="Select Service" />
										<c:forEach var="service" items="${serviceList}">
											<option value="${service.id}">${service.service_name}</option>
										</c:forEach>
									</form:select>
								</div>
						
								<div class="col-md-6 ">
								<h5 align="center">	Frequency Range:</h5>
									<form:select path="company.serviceList" class="logInput"
										id="select_frequency_id"  >
										<option value="0" label="Select Frequency Range" />
										<c:forEach var="frequency" items="${frequencyServiceList}">
											<option value="${frequency.frequency_id}">${frequency.frequency_name}</option>
										</c:forEach>
									</form:select>
								</div>
								
							</div>
						</c:if>
						
						<div class="text-center">
						<button type="button" class="fassetBtn waves-effect waves-light" onclick="addService()">Add Service(s)</button>
					</div>
					
			<div class="text-center">
				<button class="fassetBtn" type="submit">
		      		<spring:message code="btn_save" />
		        </button>		        
				<button class="fassetBtn" type="button" onclick = "back();">
					<spring:message code="btn_back" />
				</button>
			</div>
		</form:form>
	</div>
</div>
<script>
var range="${userform.company.yearRange}";
var range1=range.split(",");
var option_str = document.getElementById('yearrangeId');
//alert(option_str.options.length);

 for(  i=0;i<range1.length;i++){
	
	 
	    for(j=0;j<option_str.options.length;j++){
	    	
	    	
	    	if(range1[i] == option_str.options[j].value){
	    		
	    		
	    		option_str.options[j].selected=true;
	    		//exit for;
	    	}
	   //	 alert(option_str.options[j].text);
	   }
	//alert(option_str.options[i].value);
	 //$("#yearrangeId option[value="+range1[i]+"]").attr("selected", "selected");
	
} 
//alert(range);
 function selectNature()
 {

	 if ((event.keyCode > 64 && event.keyCode < 91) || (event.keyCode > 96 && event.keyCode < 123) || event.keyCode == 8  || event.keyCode == 32)
	    return true;
	 else
	    {
		  alert("Please enter  alphabets  in Business Nature");
	        return false;
	    }

	 
	 /* 
	 var regex ='^([a-zA-z\s]{4,32})$';
	 var val=document.getElementById("business_nature").value;
	  if (regex.test(val)) {
  return true;
	  } else {
	      alert("Please enter  alphabets  in Business Nature");
	      document.getElementById("business_nature").value="";
	      return false;
	  } */
	/* var val= document.getElementById("business_nature").value ;
	var regx="^[a-zA-Z_ ]*$";
	
	if(regx==val)
		{
		return true;
		}
	else
		{
		alert(" Enter only character in Business Nature");
		return false;
		
		} */
 }
function setDate(e){
	document.getElementById("from_date").value = e.value;
	// date format validation
	var datevali = document.getElementById("fromDate").value;
if(isValidDate(datevali)==true){
	 
		 return true;
 }else{
	alert("Invalid Date");
	window.location.reload();
	
}  
}

function setDate1(e){
	document.getElementById("to_date").value = e.value;	
	// date format validation
	var datevali1 = document.getElementById("toDate").value;
if(isValidDate(datevali1)==true){
	 
		 return true;
 }else{
	alert("Invalid Date");
	window.location.reload();
	
}  
}

function addProduct(){

	if(${userform.user.user_id!=null})
		{
				if(document.getElementById('password').value=="")
				{
					alert("Insert Password");
					return false;
				}
		}	
		var logo = document.getElementById("logo").value;
		var validFileExtensions = [".png",".jpg",".jpeg"];	
		
		if(logo != null && logo != ""){
			var blnValid = false;
		    for (var j = 0; j < validFileExtensions.length; j++) {
		        var sCurExtension = validFileExtensions[j];
		        if (logo.substr(logo.length - sCurExtension.length, sCurExtension.length).toLowerCase() == sCurExtension.toLowerCase()) {
		            blnValid = true;
		            break;
		        }
		    }
		    
		    if (!blnValid) {
		        alert("File format should be " + validFileExtensions.join(","));
		        $("#logo").val('');
		        return false;
		    }
	
		    
			//alert("file size :"+fileSize);
		    /* if(fileSize > 1024){
	   			alert("Please Select a file below 1GB");
	   			return false;
	   		} */
		
	}

var company_name=$("#company_name").val();		
var first_name=$("#first_name").val();
var last_name=$("#last_name").val();
var mobile_no=$("#mobile_no").val();
var email=$("#email").val();
var adhaar_no=$("#adhaar_no").val();
var empLimit=$("#empLimit").val();
var amount=$("#amount	").val();
var pan_no=$("#pan_no").val();
		
		
		
		var frmDate=$("#fromDate").val();
		var toDate=$("#toDate").val();

		var date1=new Date(frmDate);
		var date2=new Date(toDate);
		var flag=false;
 if(company_name!="" && company_name!=null && first_name!="" && first_name!=null && last_name!="" && last_name!=null && mobile_no!="" && mobile_no!=null && email!="" && email!=null && adhaar_no!="" && adhaar_no!=null && empLimit!="" && empLimit!=null && amount!="" && amount!=null && pan_no!="" && pan_no!=null && companyTypeId!="" && companyTypeId!=null && industryTypeId!="" && industryTypeId!=null && country!="" && country!=null && stateId!="" && stateId!=null && cityId!="" && cityId!=null && current_address!="" && current_address!=null && permenant_address!="" && permenant_address!=null && pincode!="" && pincode!=null && pan_no!="" && pan_no!="" && pan_no!=null && voucher_range!="" && voucher_range!= null)
	{ 
		if(frmDate=="")
			{
				alert("Please Select From Date");
				return false;
			}
		else if(toDate=="")
			{
				alert("Please Select To Date");
				return false;
			}
		else if(date1>date2)
			{
				alert("From Date Cann't be greater than To Date");
				return false;
			}
	
/* if(company_name!="" && company_name!=null)
  {
	if(serviceDetails.length>0 )
		{
			flag=true;
		}
	if(flag==true)
		{
			return true;
		}		 
   }
else
	{
 */	

	/* } */
	}
 if(company_name!="" && company_name!=null)
 {
 if(serviceDetails.length>0 )
	{
		flag=true;
	}
if(flag==true)
	{
		return true;
	}
 }
 else
	 {
	 if(serviceDetails.length>0 )
		{
			flag=true;
		}
	if(flag==true)
		{
			return true;
		}
		else
		{
			alert("Please select at least one service.");
			return false;
		}
	 	
	 }
}


function isValidDate(dateStr) {
	
	
	 // Checks for the following valid date formats:
	 // MM/DD/YYYY
	 // Also separates date into month, day, and year variables
	 //var datePat = /^(\d{2,2})(-)(\d{2,2})\2(\d{4}|\d{4})$/;
	 
	 //var datePat= /^\d{2}\/\d{2}\/\d{4}$/;
	 
	 var datePat= /^((0?[1-9]|1[012])[- /.](0?[1-9]|[12][0-9]|3[01])[- /.](19|20)?[0-9]{2})*$/;
	 
	 var matchArray = dateStr.match(datePat); // is the format ok?
			 
			//alert(matchArray);
			 
	 if (matchArray == null) {
	  //alert("Date must be in MM-DD-YYYY format")
	  return false;
	 }
	 
	 month = matchArray[2];
	 day   = matchArray[3];
	 
	 
	 if (month < 1 || month > 12) { // check month range
		  alert("Month must be between 1 and 12");
		  return false;
		 }
	 
	 if (day < 1 || day > 31) {
		  alert("Day must be between 1 and 31");
		  return false;
		 }
	
	 return true;  // date is valid
	}
	
$(function() {
	  $( "#fromDate" ).datepicker();
		$( "#toDate" ).datepicker();
		
		setTimeout(function() {
	        $("#successMsg").hide()
	    }, 3000);
	    
	$("#first_name,#middle_name,#last_name,#company_name").keypress(function(e) {
		if (!letters(e)) {
			return false;
		}
	});
	
	
	$("#mobile_no,#landline_no,#adhaar_no,#pincode,#empLimit").keypress(function(e) {
		if (!digitsOnly(e)) {
			return false;
		}
	});
	/* $("#gst_no,#pan_no,#pan_no").keyup(function(e) {
		   $(this).val($(this).val().toUpperCase());
		});  */
	
	$("#gst_no,#pan_no,#pan_no").on('input', function(){	    
	    var start = this.selectionStart,
	        end = this.selectionEnd;
	    
	    this.value = this.value.toUpperCase();
	    this.setSelectionRange(start, end);
	});
	
	getStateList('${userform.company.countryId}');
	
	//alert(${userform.company.countryId});
	<c:if test="${userform.company.stateId != null}">
   $("#stateId").val('${userform.company.stateId}');
    </c:if>
	
    getCityList(document.getElementById("stateId").value);
    <c:if test="${userform.company.cityId != null}">
	$("#cityId").val('${userform.company.cityId}');
    </c:if> 
    
    <c:if test="${userform.company.bank!= null}">
    	$("#bankId").val('${userform.company.bank.bank_id}');
    </c:if> 

});
function check(cb) {
	if ($(cb).is(":checked")) {
		$("#permenant_address").val($("#current_address").val());
		$('#permenant_address').attr('readonly', true);
	} else {
		$("#permenant_address").val("");
		$('#permenant_address').attr('readonly', false);

	}
}

function getStateList(countryId){
	//alert("country id in state list"+countryId);
	var stateArray = [];
	<c:forEach var = "country" items = "${countryList}">
		var id = <c:out value = "${country.country_id}"/>
		if(id == countryId){
			$('#stateId').find('option').remove();
			$('#stateId').append($('<option>', {
			    value: 0,
			    text: 'Select State Name'
			}));
			<c:forEach var = "state" items = "${country.state}">
			if(${state.status}==true)
			{
				/* console.log("id :  ${state.state_id}   name : ${state.state_name}");
				$('#stateId').append($('<option>', {
				    value: '${state.state_id}',
				    text: '${state.state_name}' */
				    
				var tempArray = [];
			    tempArray["id"]=${state.state_id};
			    tempArray["name"]='${state.state_name}';
			    stateArray.push(tempArray);
			}
	</c:forEach>	
		
	stateArray.sort(function(a, b) {
            var textA = a.name.toUpperCase();
            var textB = b.name.toUpperCase();
            return (textA < textB) ? -1 : (textA > textB) ? 1 : 0;
        });
			for(i=0;i<stateArray.length;i++)
			{
				 $('#stateId').append($('<option>', {
			    value: stateArray[i].id,
			    text: stateArray[i].name,
			})); 
			}
	}
   </c:forEach>
}

function getCityList(e){	

    // Testing the implementation
    var cityArray = [];
	var stateId = e;	
	<c:forEach var = "state" items = "${stateList}">
		var id = <c:out value = "${state.state_id}"/>
		if(id == stateId){
			$('#cityId').find('option').remove();
			$('#cityId').append($('<option>', {
			    value: 0,
			    text: 'Select City Name'
			}));
			
			<c:forEach var = "city" items = "${state.city}">	
			if(${city.status}==true)
			{
		    var tempArray = [];
		    tempArray["id"]=${city.city_id};
		    tempArray["name"]='${city.city_name}';
			cityArray.push(tempArray);
			}
	        </c:forEach>
	        cityArray.sort(function(a, b) {
	            var textA = a.name.toUpperCase();
	            var textB = b.name.toUpperCase();
	            return (textA < textB) ? -1 : (textA > textB) ? 1 : 0;
	        });
				for(i=0;i<cityArray.length;i++)
				{
					 $('#cityId').append($('<option>', {
				    value: cityArray[i].id,
				    text: cityArray[i].name,
				})); 
				}
		}
	</c:forEach>
    
}



$(document).ready(function(){
	if(${userform.user.password!=null})
		{				
	document.getElementById('password').value='${userform.user.password}';
		}
    $('.tagsinput').tagsinput({
        tagClass: 'label label-primary'
    });
});
function back(){
	window.location.assign('<c:url value = "companyList"/>');	
}
function mouseoverPass(obj) {
	  var obj = document.getElementById('password');
	  obj.type = "text";
	}
	function mouseoutPass(obj) {
	  var obj = document.getElementById('password');
	  obj.type = "password";
	}
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>