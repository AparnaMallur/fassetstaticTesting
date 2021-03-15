<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<spring:url value="/resources/images/delete.png" var="deleteImg" />
<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_EIGHTEEN%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THIRTY%></c:set>
<c:set var="STATUS_SUBSCRIBED_USER"><%=MyAbstractController.STATUS_SUBSCRIBED_USER%></c:set>
<c:set var="STATUS_INACTIVE"><%=MyAbstractController.STATUS_INACTIVE%></c:set>
<div class="breadcrumb">
	<h3>Company Approval</h3>					
	<a href="homePage">Home</a> » <a href="companyCheckList">Company Approval</a>  » <a href="#">Create</a>
</div>
<c:if test="${successMsg != null}">
	<div class="successMsg" id = "successMsg"> 
		<strong>${successMsg}</strong>
	</div>
</c:if>
<div class="col-md-12 wideform">
	<div class="fassetForm">
		<form:form id="checklistStatusForm" action="checklistStatus" method="post" commandName = "form" onsubmit = "addCompany();">
			<div class="row">				
				<form:input path="status.checklist_status_id" hidden = "hidden"/>
				<form:input path="status.created_date" hidden = "hidden"/>
				<form:input path="status.checkList" id = "checkList"  hidden = "hidden"/>
				<!--<form:input path="status.fromDate" id="from_date" hidden="hidden" />
				<form:input path="status.toDate" id="to_date" hidden="hidden" />-->
			</div>
			<div class="row">	
				<div class="col-md-3 control-label"><label>First Name</label></div>		
				<div class="col-md-9">
					<input value="${form.user.first_name}" disabled="disabled"/>
				</div>	
			</div>
			<div class="row">	
				<div class="col-md-3 control-label"><label>Middle Name</label></div>		
				<div class="col-md-9">
					<input value="${form.user.middle_name}"  disabled="disabled" />
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>Last Name</label></div>		
				<div class="col-md-9">
					<input value="${form.user.last_name}"  disabled="disabled" />
				</div>	
			</div>
			<div class="row">	
				<div class="col-md-3 control-label"><label>Primary Mobile Number</label></div>		
				<div class="col-md-9">
					<input value="${form.user.mobile_no}"  id = "mobile_no" disabled="disabled" />
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>Landline Number</label></div>		
				<div class="col-md-9">
					<input value="${form.company.landline_no}" disabled="disabled" />
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>Email Id</label></div>		
				<div class="col-md-9">
					<input value="${form.user.email}" id = "email" disabled="disabled" />
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>Adhaar Card Number</label></div>		
				<div class="col-md-9">
					<input value="${form.user.adhaar_no}"  id = "adhaar_no" disabled="disabled" />
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>Super User PAN Number</label></div>		
				<div class="col-md-9">
					<input value="${form.user.pan_no}"  id = "pan_no" disabled="disabled"/>
				</div>	
			</div>
			<div class="row">	
				<div class="col-md-3 control-label"><label>Company Name</label></div>		
				<div class="col-md-9">
					<input value="${form.company.company_name}"  id = "company_name" disabled="disabled"/>
				</div>	
			</div>
			<div class="row">	
				<div class="col-md-3 control-label"><label>Type of Company</label></div>		
				<div class="col-md-9">
					<input value="${form.company.company_statutory_type.company_statutory_name}" id = "company_name" disabled="disabled"/>
				</div>	
			</div>
			<div class="row">	
				<div class="col-md-3 control-label"><label>Industry Type</label></div>		
				<div class="col-md-9">
					<input value="${form.company.industry_type.industry_name}" id = "company_name" disabled="disabled"/>
				</div>	
			</div>
			<div class="row">	
				<div class="col-md-3 control-label"><label>Country</label></div>		
				<div class="col-md-9">
					<input value="${form.company.country.country_name}"  class="logInput" id = "company_name" disabled="disabled"/>
				</div>	
			</div>
			
			<div class="row">	
				<div class="col-md-3 control-label"><label>State</label></div>		
				<div class="col-md-9">
					<input value="${form.company.state.state_name}"  class="logInput" id = "company_name" disabled="disabled"/>
				</div>	
			</div>
			<div class="row">	
				<div class="col-md-3 control-label"><label>City</label></div>		
				<div class="col-md-9">
					<input value="${form.company.city.city_name}" class="logInput" id = "company_name" disabled="disabled"/>
				</div>	
			</div>
			<div class="row">	
				<div class="col-md-3 control-label"><label>Current Address</label></div>		
				<div class="col-md-9">
				<input value="${form.company.current_address}" class="logInput"
								id="current_address" disabled="disabled"/>
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>Permanent Address</label></div>		
				<div class="col-md-9">
				<input value= "${form.company.permenant_address}" class="logInput"
								id="current_address" disabled="disabled"/>
				</div>	
			</div>	
			
			<div class="row">	
				<div class="col-md-3 control-label"><label>Pin Code</label></div>		
				<div class="col-md-9">
					<input value="${form.company.pincode}" class="logInput" id = "pincode" disabled="disabled" />
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>Company PAN Number</label></div>		
				<div class="col-md-9">
					<input value="${form.company.pan_no}"  class="logInput" id = "pan_no" disabled="disabled" />
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>Registration Number</label></div>		
				<div class="col-md-9">
					<input value="${form.company.registration_no}" class="logInput" id = "registration_no" disabled="disabled" />
				</div>	
			</div>
			<div class = "row">
				<div class="col-md-3 control-label"><label>Financial Year<span>*</span></label></div>
				<div class = "col-md-9">
					<form:select path="status.yearRange" class="logInput"  multiple="multiple">
						<form:option value="0" label="Select Financial Year"/>		
						<c:forEach var = "yearRange" items = "${form.yearList}">							
							<form:option value = "${yearRange.year_id}">${yearRange.year_range}</form:option>	
						</c:forEach>									
					</form:select>
					<span class="logError"><form:errors path="status.yearRange" /></span>	
				</div>
			</div>	
			<div class="row">
				<div class="col-md-3 control-label">
					<label>Subscription From<span>*</span></label>
				</div>
				<div class="col-md-9">
				<form:input path="status.fromDate" class="logInput" id = "fromDate"  placeholder="From Date" />
					<span class="logError"><form:errors path="status.fromDate" /></span>
				</div>
			</div>
			<div class="row">
				<div class="col-md-3 control-label">
					<label>Subscription To<span>*</span></label>
				</div>
				<div class="col-md-9">
				<form:input path="status.toDate" class="logInput" id = "toDate"  placeholder="To Date" />
				<span class="logError"><form:errors path="status.toDate" /></span>
				</div>
			</div>
			<div class="row">	
				<div class="col-md-3 control-label"><label>Nature of Business</label></div>		
				<div class="col-md-9">
					<input value="${form.company.business_nature}"  class="logInput" id = "business_nature" disabled="disabled" />
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>GST Number</label></div>		
				<div class="col-md-9">
					<input value="${form.company.gst_no}" class="logInput" id = "gst_no" disabled="disabled" />
				</div>	
			</div>
			<div class="row">	
				<div class="col-md-3 control-label"><label>PTE Number</label></div>		
				<div class="col-md-9">
					<input value="${form.company.pte_no}" class="logInput" id = "pte_no"  disabled="disabled" />
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>PTR Number</label></div>		
				<div class="col-md-9">
					<input value="${form.company.ptr_no}"  class="logInput" id = "ptr_no"  disabled="disabled" />
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>Eway Bill Registration Number</label></div>		
				<div class="col-md-9">
					<input value="${form.company.eway_bill_no}" class="logInput" id = "eway_bill_no" disabled="disabled" />
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>IEC Number</label></div>		
				<div class="col-md-9">
					<input value="${form.company.iec_no}" class="logInput" id = "iec_no" disabled="disabled" />
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>Other Tax 1</label></div>		
				<div class="col-md-9">
					<input value="${form.company.other_tax_1}" class="logInput" id = "other_tax_1" disabled="disabled" />
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-3 control-label"><label>Other Tax 2</label></div>		
				<div class="col-md-9">
					<input value="${form.company.other_tax_2}" class="logInput" id = "other_tax_2" disabled="disabled" />
				</div>	
			</div>
			<div class="row">	
				<div class="col-md-3 control-label"><label>Employee Limit<span>*</span></label></div>		
				<div class="col-md-9">
					<form:input path="status.emplimit" class="logInput" id = "emplimit"  placeholder="Employee Limit" />
					<span class="logError"><form:errors path="status.emplimit" /></span>
				</div>	
			</div>	
		    <div class="row">	
				<div class="col-md-3 control-label"><label>Status<span>*</span></label></div>		
				<div class="col-md-9">	
					<form:radiobutton path="status.status" value="${STATUS_SUBSCRIBED_USER}" checked="checked" />Approve 
					<form:radiobutton path="status.status" value="${STATUS_INACTIVE}"/>Reject
				</div>
			</div>
			<c:if test="${form.quotationDetails != null}">
				<table class="table table-bordered table-striped">
					<thead>
							<tr>
								<th>Service </th>
								<th>Frequency</th>
								<th>Amount</th>						
							</tr>
					</thead>
					<tbody>
							<c:forEach var = "quote_list" items = "${form.quotationDetails}">	
								<tr>
									<td>${quote_list.service_id.service_name}</td>
									<td>${quote_list.frequency_id.frequency_name}</td>
									<td>${quote_list.amount}</td>
								</tr>
							</c:forEach>
					</tbody>
				</table>
			</c:if>
			<div class="row">
		<ul class="nav nav-tabs navtab-bg">
		
			<li class="active">
    			<a href="#profile" data-toggle="tab" aria-expanded="true"> <span class="visible-xs"><i class="fa fa-user"></i></span>Select Checklist</a>
			</li>
		</ul>
		<div class="tab-content" >
     		<div class="tab-pane active" id="profile">
     			<c:if test="${checkListError != null}">
					<span class="logError">${checkListError}</span>
				</c:if>
				
				<div class="row sub-list">                                                    
					<c:forEach var = "comp" items = "${form.checklist}">							
						<div class="col-md-4 col-sm-6 col-xs-12 sub-list1">
							<div class="row">
								<div class="col-md-9" >
									<i class="fa fa-arrow-right"></i>${comp.checklist_name} <span style='color:red'>${comp.is_mandatory==true ? "*" : ""}	</span>												
								</div>
								<div class="col-md-3">
									<input name="32-ck" id="32-ck" value="${comp.checklist_id}" type="checkbox" onchange = "addCom(this)">
								</div>
							</div>
						</div>							
					</c:forEach>	
				</div>                                                    
			</div>	
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
var comList = []; 
	$(function() {		
	    setTimeout(function() {
	        $("#successMsg").hide()
	    }, 3000);
		
		$("#emplimit,#landline_no,#adhaar_no,#pincode").keypress(function(e) {
			if (!digitsOnly(e)) {
				return false;
			}
		});
		function setDate(e){
			document.getElementById("from_date").value = e.value;		
		}
		
		function setDate1(e){
			document.getElementById("to_date").value = e.value;		
		}
	});	
	function cancel(){
		window.location.assign('<c:url value = "companyCheckList"/>');	
	}
	
	function addCom(comp){
		var comValue = comp.value;
		if(comp.checked){
			comList.push(comValue);
			console.log(comList);
		}
		else{
			for(i = 0; i<comList.length; i++){
				if(comList[i] == comValue){
					comList.splice(i,1);
					break;
				}
			}
			console.log(comList);
		}
	}
	
	function addCompany(){
		$("#checkList").val(comList);
		return true;
	}
</script>
<script>
	$(function() {
			$( "#fromDate" ).datepicker();
			$( "#toDate" ).datepicker();
	   });
 </script>
	
	<%@include file="/WEB-INF/includes/footer.jsp" %>