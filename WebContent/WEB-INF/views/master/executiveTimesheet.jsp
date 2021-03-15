<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_EIGHTEEN%></c:set>
<div class="breadcrumb">
	<h3>Executive Time Sheet</h3>
	<a href="homePage">Home</a> » <a href="executiveTimesheetList">Executive Time Sheet</a> » <a href="#">Create</a>
</div>
<c:if test="${successMsg != null}">
	<div class="successMsg" id="successMsg">
		<strong>${successMsg}</strong>
	</div>
</c:if>

<div class="col-md-12 wideform">
	<div class="fassetForm">
		<form:form id="saveExecutiveTimesheetForm" action="saveexecutivetimesheet" method="post" commandName="executiveTimesheet">
			
			<!-- Small modal -->
			<div id="date-model" data-backdrop="static" data-keyboard="false" class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
				<div class="modal-dialog ">
	       			<div class="modal-content">
	           			<div class="modal-header">
	                		<h4 class="modal-title" id="mySmallModalLabel">Select Date</h4>
	            		</div>
						<div class="modal-body">
							<input type="text" style="color: black;" id="dateId" name="dateId" onchange="setDate(this)" placeholder="Date"> 
							<span class="logError"><form:errors path="date" /></span>
						</div>
						<div class="modal-footer">
	               			<button type="button" class="btn btn-primary waves-effect waves-light" onclick = "closeModel()">Submit</button>
						</div>
	       			</div><!-- /.modal-content -->
	      		</div>
			</div>
			<!-- /.modal -->
			
			
			<div class="row">
				<form:input path="timesheet_id" id="timesheet_id" hidden="hidden" />
				<form:input path="date" id="date" hidden="hidden" />
				<form:input path="timesheetDetails" id="timesheetDetails" hidden="hidden" />
			</div>
			
			<div class="row">
				<div class="col-md-2 control-label">
					<label>Company Name</label>
				</div>
				<div class="col-md-10">
					<form:select path="company_id" class="logInput" id="companyId">
						<form:option value="0" label="Select Company Name" />
						<c:forEach var="company" items="${companyList}">
							<form:option value="${company.company_id}">${company.company_name}</form:option>
						</c:forEach>
					</form:select>
					<span class="logError"><form:errors path="company_id" /></span>
				</div>
			</div>

			<div class="row">
				<div class="col-md-2 control-label">
					<label>Category</label>
				</div>
				<div class="col-md-10">
					<form:select path="service_id" class="logInput" id="serviceId">
						<form:option value="0" label="Select Service Name" />
						<c:forEach var="service" items="${serviceList}">
							<form:option value="${service.id}">${service.service_name}</form:option>
						</c:forEach>
					</form:select>
					<span class="logError"><form:errors path="service_id" /></span>
				</div>
			</div>
			<div class="row">
				<div class="col-md-2 control-label">
					<label>Total Time in Hrs<span>*</span></label>
				</div>
				<div class="col-md-10">
					<form:input path="total_time" class="logInput" id="total_time" maxlength="${SIZE_EIGHTEEN}" placeholder="Total time" />
					<span class="logError"><form:errors path="total_time" /></span>
				</div>
			</div>
			<div class="row">
						<div class="col-md-2 control-label">
							<label>Remark</label>
						</div>
						<div class="col-md-10">
							<form:textarea path="remark" class="logInput"
								id="remark" rows="5" placeholder="Remark"></form:textarea>
							<span class="logError"><form:errors path="remark" /></span>
						</div>
					</div>

				<div class='row'>
					<div class="text-center">
						<button type="button" class="fassetBtn waves-effect waves-light" onclick="saveTimesheet()">Save Timesheet Details</button>
					</div>	
					<table class="table table-bordered table-striped" id="detailTable">
						<thead>
							<tr>
								<th></th>
								<th>Date</th>
								<th>Comapny</th>
								<th>Category</th>
								<th>Total time</th>
								<th>Remark</th>								
							</tr>
						</thead>
						<tbody>
							
						</tbody>
					</table>
				</div>
			
			

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
	var timeDetails = [];
	
	$(function() {		
	    setTimeout(function() {
	        $("#successMsg").hide()
	    }, 3000);
		
	    $('#date-model').modal({
	    	show: true,
	   	});
	    
	    $("#total_time").keypress(function(e) {
			if (!digitsAndDotOnly(e)) {
				return false;
			}
		});
	    
		$( "#dateId" ).datepicker({dateFormat: 'dd-mm-yy', maxDate: new Date()});
	 	$( "#date" ).datepicker();	 	

		var date = '<c:out value= "${executiveTimesheet.date}"/>';
		
		if(date != "") {
		 	newdate=formatDate(date);	
			$("#date").datepicker("setDate", newdate);
			$("#date").datepicker("refresh");
		}
		
		
	});
	
	function setDate(e){
		var oldDate = ($("#dateId").val()).split("-");
		var date2=oldDate[1]+"/"+oldDate[0]+"/"+oldDate[2];
		document.getElementById("date").value = date2;
		
	}
	
	function closeModel(){
		var dateToSave = $("#dateId").val();

		if(dateToSave == ""){
			alert("Please select date.");
		}
		else{
			$('#date-model').modal('hide');		
		}
		
	}
	
	function cancel(){
		window.location.assign('<c:url value = "executiveTimesheetList"/>');	
	}
	
	function saveTimesheet() {
		var companyId = $("#companyId").val();
		var serviceId = $("#serviceId").val();
		var totalTime = $("#total_time").val();
		var dateToSave = $("#dateId").val();
		var remark = $("#remark").val();

		var service_name = "";
		var company_name = "";
		
		if(companyId == 0){
			alert("Please select company.");
		}else if(serviceId == 0){
			alert("Please select service.");
		}else if(totalTime == ""){
			alert("Please enter total time.");
		}else{
			<c:forEach var="company" items="${companyList}">
				var company_id = ${company.company_id};
				if(company_id == companyId){
					 company_name = "${company.company_name}";
				}
		  	 </c:forEach>
		  	 
		  	<c:forEach var="service" items="${serviceList}">
				var service_id = ${service.id};
				if(service_id == serviceId){
					 service_name = "${service.service_name}" ;
				}
	  		 </c:forEach>
			
			timeDetails.push({
				"companyId":companyId,
				"serviceId":serviceId,
				"dateToSave":dateToSave,
				"totalTime":totalTime,
				"remark":remark});		
			
			$("#timesheetDetails").val(JSON.stringify(timeDetails));
			console.log($("#timesheetDetails").val());
			
		    var markup = "<tr><td>"+''+"</td>"+"<td>"+dateToSave+"</td>"+
		    	"<td>"+company_name+"</td>"+
				"<td>"+service_name+"</td>"+
				"<td>"+totalTime+"</td>"+"<td>"+remark+"</td>"+"</tr>";
	
			$('#detailTable tr:last').after(markup);
			document.getElementById("detailTable").style.display="table";
	
			var companyId = $("#companyId").val("0");
			var serviceId = $("#serviceId").val("0");
			var totalTime = $("#total_time").val("");
			var remark = $("#remark").val();
		}
		
		
		
	}
	
    function formatDate(date) {
	    var d = new Date(date),
	        month = '' + (d.getMonth() + 1),
	        day = '' + d.getDate(),
	        year = d.getFullYear();
	    if (month.length < 2) month = '0' + month;
	    if (day.length < 2) day = '0' + day;
	    	return [day,month,year].join('-');
	}
	
</script>

<%@include file="/WEB-INF/includes/footer.jsp"%>