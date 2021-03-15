<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<spring:url value="/resources/images/closeRed.png" var="deleteImg" />
<spring:url value="/resources/images/edit.png" var="editImg" />
<link rel="stylesheet" href="//code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="//code.jquery.com/jquery-1.9.1.js"></script>
<script src="//code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<script type="text/javascript" src="${valid}"></script>
<c:if test="${successMsg != null}">
	<div class="successMsg" id="successMsg">
		<strong>${successMsg}</strong>
	</div>
</c:if>
<div class="breadcrumb">
	<h3>Payroll Auto JV</h3>
	<a href="homePage">Home</a> » <a href="payrollAutoJVList">Payroll Auto JV</a> » <a href="#">Create</a>
</div>

<div class="col-md-12 wideform">
	<div class="fassetForm">
		<form:form action="payrollAutoJV" method="post" commandName="payrollAutoJV" onsubmit="return save();">
			<!-- Small modal -->
			<div id="year-model" data-backdrop="static" data-keyboard="false"
				class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog"
				aria-labelledby="mySmallModalLabel" aria-hidden="true">
				<div class="modal-dialog ">
					<div class="modal-content">
						<div class="modal-header">
							<h4 class="modal-title" id="mySmallModalLabel">Select Accounting Year</h4>
						</div>
						<div class="modal-body">
						<c:set var="first_year" value="0" />	   	
							<c:forEach var="year" items="${yearList}">
							<c:choose>
									<c:when test="${first_year==0}">
								              <form:radiobutton path="accountYearId" value="${year.year_id}" checked="checked" onclick="setdatelimit('${year.start_date}','${year.end_date}')"/>${year.year_range} 
									</c:when>
									<c:otherwise>
								              <form:radiobutton path="accountYearId" value="${year.year_id}" onclick="setdatelimit('${year.start_date}','${year.end_date}')"/>${year.year_range} 
									</c:otherwise>
							</c:choose>
							<c:set var="first_year" value="${first_year+1}" />
							</c:forEach>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-primary waves-effect waves-light" onclick='saveyearid()'>
								Save Year
							</button>
						</div>
					</div>
					<!-- /.modal-content -->
				</div>
			</div>
			<!-- /.modal-dialog -->
			
	<div class="row">
				<form:input path="payroll_id" hidden="hidden" />
				<form:input path="employeeDetails" id="employeeDetails" hidden="hidden" />
				<form:input path="subledgerDetails" id="subledgerDetails" hidden="hidden" />
				<div class="col-md-9 col-xs-6">
					<c:if test='${payrollAutoJV.payroll_id!=Null}'>
						<div class="row">
							<div class="col-md-4 control-label">
								<label>Voucher No<span>*</span></label>
							</div>
							<div class="col-md-8">
								<form:input path="voucher_no" class="logInput" id="voucher_no" placeholder="Voucher no" readonly="true" />
							</div>
						</div>
					</c:if>
               </div>
               
			<div class="row">	
				<div class="col-md-4 control-label"><label>Date<span>*</span></label></div>		
				<div class="col-md-8">	
			   		<form:input type = "text" style = "color: black;" path="date" id = "date" name = "date"
			   		  placeholder = "Date" autocomplete="off" onchange="dateRestriction();countWorkingDay()" onclick="setDatePicker()"/>	
					<span class="logError"><form:errors path="date" /></span>					
				</div>
			
			</div>
			<div class="row">	
				<div class="col-md-4 control-label"><label>Working Day<span>*</span></label></div>		
					<div class="col-md-8">
						<form:input path="days" class="logInput" id = "wdays" onkeypress="return isNumber(event)"  placeholder="Working Days" maxlength="2"/>
					</div>
		   </div>
		
		
			<div class="row">
				<div class="col-md-4 control-label">
					<label>Remark</label>
				</div>
				<div class="col-md-8">
					<form:textarea path="other_remark" class="logInput"
						id="other_remark" rows="5" placeholder="Remark" maxlength="250"></form:textarea>
					<span class="logError"><form:errors path="other_remark" /></span>
				</div>
			</div>
	</div>
			<div class='row'>

				<div class="table-responsive">
			<table class="table table-bordered table-striped" id="detailTable"
				style='display: block'>
				<thead>
					<tr>
					
						<th>Action</th>
						<th>Employee ID</th>
						<th>Employee Name</th>
						<th>PAN</th>
						<th>Total days</th>
						<th>Basic Salary</th>
						<th>DA</th>
						<th>Conveyance Allowance</th>
						<th>Other allowances</th>
						<th>Gross Salary</th>
						<th>PF Employee Contribution</th>
						<th>ESIC Employee Contribution</th>
						<th>Profession Tax</th>
						<th>LWF</th>
						<th>TDS</th>
						<th>Other Deductions</th>
						<th>Advance Adjustment</th>
						<th>Total Deductions</th>
						<th>Net Salary</th>
						<th>PF Employer Contribution</th>
						<th>ESIC Employer Contribution</th>
						<th>PF Admin Charges</th>
					</tr>
				</thead>
				<tbody>
						
						<%-- <c:forEach var = "payrollemployeedetail" items = "${payrollemployeedetail}">
								<tr>
					<td><a href = '#' onclick = 'deleteRow(this,"+id+")'><img src='${deleteImg}' style = 'width: 20px;'/></a> <a href="#"
											onclick="editpayroll(${payrollemployeedetail.employeeDetails_id})"><img
												src='${editImg}' style="width: 20px;" /></a></td>
						<td id="employeecodenew">${payrollemployeedetail.code}</td>
										<td>${payrollemployeedetail.name}</td>
										<td>${payrollemployeedetail.pan}</td>
										<td>${payrollemployeedetail.totaldays}</td>
										<td>${payrollemployeedetail.basicSalary}</td>
										<td>${payrollemployeedetail.DA}</td>
										<td>${payrollemployeedetail.conveyanceAllowance}</td>
										<td>${payrollemployeedetail.otherAllowances}</td>
										<td>${payrollemployeedetail.grossSalary}</td>
										<td>${payrollemployeedetail.pfEmployeeContribution}</td>
										<td>${payrollemployeedetail.eSICEmployeeContribution}</td>
										<td>${payrollemployeedetail.professionTax}</td>
										<td>${payrollemployeedetail.lWF}</td>
										<td>${payrollemployeedetail.tDS}</td>
										<td>${payrollemployeedetail.otherDeductions}</td>
										<td>${payrollemployeedetail.advanceAdjustment}</td>
										<td>${payrollemployeedetail.totalDeductions}</td>
										<td>${payrollemployeedetail.netSalary}</td>
										<td>${payrollemployeedetail.pfEmployerContribution}</td>
										<td>${payrollemployeedetail.eSICEmployerContribution}</td>
										<td>${payrollemployeedetail.pFAdminCharges}</td>
						
						</tr>
						</c:forEach> --%>
						
				</tbody>
			
			</table>
			</div>
		
			</div>
		
			
			<div id="dtl-btn">
				<button type="button" class="fassetBtn waves-effect waves-light" onclick="showdiv()">Add Details</button>
			</div>
			<div id="details-data" class='row' style="display: none">
<div class="table-responsive">
				<table class="table table-bordered table-striped">
					<thead>
						<tr>
						<th>Employee ID</th>
						<th>Employee Name</th>
						<th>PAN</th>
						<th>Total days</th>
						<th>Basic Salary</th>
						<th>DA</th>
						<th>Conveyance Allowance</th>
						<th>Other allowances</th>
						<th>Gross Salary</th>
						<th>PF Employee Contribution</th>
						<th>ESIC Employee Contribution</th>
						<th>Profession Tax</th>
						<th>LWF</th>
						<th>TDS</th>
						<th>Other Deductions</th>
						<th>Advance Adjustment</th>
						<th>Total Deductions</th>
						<th>Net Salary</th>
						<th>PF Employer Contribution</th>
						<th>ESIC Employer Contribution</th>
						<th>PF Admin Charges</th>
						</tr>
					</thead>
					<tbody>
					<c:if test="${payrollAutoJV!=null}">
						<tr>
							<td>
								
								<select class="logInput" id="employee_id" onChange="setDetails(this.value)">
									<option value="0">Select Employee Code</option>
								</select>
							</td>
							<td>
							    <input class="logInput" id="employeecode" placeholder="Employee Code" hidden="hidden"/> 
								<input class="logInput" id="employeeName" placeholder="Employee Name" readonly/> 
							</td>
							<td><input class="logInput" id="PAN" placeholder="PAN" readonly/></td>
							
							<td class="tright"><input id="totaldays" class="logInput" placeholder="Total days"  onchange="calculaterate(this.value,1)" onkeypress="return isNumber(event)" maxlength="2"/> </td>
							
							<td class="tright"><input id="basicSalary" class="logInput" placeholder="Basic Salary" onchange="calculaterate(this.value,2)" onkeypress="return isNumber(event)" maxlength="10"/> </td>
							
							<td class="tright"><input id="DA" class="logInput" placeholder="DA" onchange="calculaterate(this.value,3)" onkeypress="return isNumber(event)" maxlength="10"/> </td>
							
							<td class="tright"><input id="conveyanceAllowance" class="logInput" placeholder="Conveyance Allowance" onchange="calculaterate(this.value,4)" onkeypress="return isNumber(event)" maxlength="10"/> </td>
							
							<td class="tright"><input id="otherAllowances" class="logInput" placeholder="Other Allowances" onchange="calculaterate(this.value,5)" onkeypress="return isNumber(event)" maxlength="10"/> </td>
							
							<td class="tright"><input id="grossSalary" class="logInput" placeholder="Gross Salary"  onchange="calculaterate1(this.value,1) onkeypress="return isNumber(event)" maxlength="10"/> </td>
							
							<td class="tright"><input id="pfEmployeeContribution" class="logInput" placeholder="PF EmployeeContribution"   onchange="calculaterate1(this.value,2)" onkeypress="return isNumber(event)" maxlength="10"/> </td>
							
							<td class="tright"><input id="eSICEmployeeContribution" class="logInput" placeholder="ESIC Employee Contribution"  onchange="calculaterate1(this.value,3)" onkeypress="return isNumber(event)" maxlength="10"/> </td>
							
							<td class="tright"><input id="professionTax" class="logInput" placeholder="Profession Tax" onchange="calculaterate1(this.value,4)" onkeypress="return isNumber(event)" maxlength="10"/> </td>
							
							<td class="tright"><input id="lWF" class="logInput" placeholder="LWF"  onkeypress="return isNumber(event)"  onchange="calculaterate1(this.value,5)" maxlength="10"/> </td>
							
							<td class="tright"><input id="tDS" class="logInput" placeholder="TDS"  onkeypress="return isNumber(event)" onchange="calculaterate1(this.value,6)" maxlength="10"/> </td>
							
							<td class="tright"><input id="otherDeductions" class="logInput" placeholder="Other Deductions"  onchange="calculaterate1(this.value,7)"  onkeypress="return isNumber(event)" maxlength="10"/> </td>
							
							<td class="tright"><input id="advanceAdjustment" class="logInput" placeholder="Advance Adjustment"  onchange="calculaterate1(this.value,8)"  onkeypress="return isNumber(event)" maxlength="10"/> </td>
							
							<td class="tright"><input id="totalDeductions" class="logInput" placeholder="Total Deductions"  onkeypress="return isNumber(event)" maxlength="10" readonly="true" /> </td>
							
							<td class="tright"><input id="netSalary" class="logInput" placeholder="netSalary"  onkeypress="return isNumber(event)" onchange="calculaterate1(this.value,9)"  readonly="true" maxlength="10"/> </td>
							
							<td class="tright"><input id="pfEmployerContribution" class="logInput" placeholder="PF EmployerContribution" onkeypress="return isNumber(event)" onchange="calculaterate1(this.value,10)" maxlength="10"/> </td>
							
							<td class="tright"><input id="eSICEmployerContribution" class="logInput" placeholder="ESIC Employer Contribution"  onkeypress="return isNumber(event)" onchange="calculaterate1(this.value,11)"  maxlength="10"/> </td>
							
							<td class="tright"><input type="text" id="pFAdminCharges" class="logInput" placeholder="PF Admin Charges"  onkeypress="return isNumber(event)" onchange="calculaterate1(this.value,12)" maxlength="10" /> </td>
							
							
						</tr>
						</c:if>
					</tbody>
				</table>
				</div>
				<div class="text-center">
					<button type="button" class="fassetBtn waves-effect waves-light"
						onclick="saveDetails()">Save Employee Details</button>
				</div>
			</div>
			<div class="row" style="text-align: center; margin: 15px;">
				<button class="fassetBtn" type="submit"  >
					<spring:message code="btn_save" />
				</button>
				<button class="fassetBtn" type="button" onclick="cancel()">
					<spring:message code="btn_cancel" />
				</button>
			</div>
		</form:form>
	</div>
</div>
<script>
var empDetails = [];
$("#wdays").keypress(function(e) {
	if (!digitsOnly(e)) {
		return false;
	}
});
var startdate="";
var enddate="";
function setDatePicker()
{
	$("#date").datepicker({dateFormat: 'dd-mm-yy'});
	 $('#date').datepicker("option", { maxDate: new Date() });
	$('#date').value = '';
	$('#date').focus(); // ui-datepicker-div
	 event.preventDefault();
		
} 
function dateRestriction() {
	 
	 
	 var datefield=document.getElementById("date").value;
	
	 var res = datefield.split("-");
	 var dd=parseInt(res[0]);
	 
	
	 var mm=parseInt(res[1]);
	 
	 var yy=parseInt(res[2]);
	 
	 
	 
	    var nd = res[1]+"/"+res[0]+"/"+res[2];
	    var ud = new Date(nd);
		var td = new Date();
		
		ud.setHours(0,0,0,0);
		
		td.setHours(0,0,0,0);
	var flag = 	 checkDate(dd,mm,yy);
	
	if(flag == false){
		alert("Invalid date");
		var id = document.getElementById("date");
		id.value = '';
		id.focus(); // ui-datepicker-div
		event.preventDefault();	
	}
	else if(ud > td ){
			
			alert("Cannot create voucher for future date");
			var id = document.getElementById("date");
			id.value = '';
			id.focus(); // ui-datepicker-div
			event.preventDefault();		
			
		}
	
	
}
function checkDate(dd,mm,yy)
{
	if(mm>0 && mm<13)
	{
		if(mm == 1 || mm == 3 || mm == 5 || mm == 7 || mm == 8 || mm == 10 || mm == 12 && (dd>0 && dd <32))
		{	
			return true;
		}
		else if(mm == 4 || mm == 6 || mm == 9 || mm == 11 && (dd>0 && dd <31))
		{	
			return true;
		}
		else if(mm == 2)
		{
			var flag = yy%4 == 0 & yy%100 != 0 || yy %400 == 0; 
			if(flag == true && (dd>0 && dd <30))
			{
			  	return true;
			}
			else if(flag == false && (dd>0 && dd < 29))
			{
			  	return true;
			}
			else
			{
				return false;
			}
		}
	}
	else
	{
		return false;
	}
	
}
$(document).ready(function() {
	
/* 	 <c:forEach var="payrollempdetail"  items="${payrollemployeedetail}">
		if('${payrollempdetail}'!=0)
	     {
			empDetails.push({
				
				"id":payrollempdetail.id,
				"empId":payrollempdetail.empId,
				"employeeName":payrollempdetail.employeeName, 
				"PAN":payrollempdetail.PAN, 
				"totaldays":payrollempdetail.totaldays, 
				"basicSalary":payrollempdetail.basicSalary, 
				"DA":payrollempdetail.DA,
				"conveyanceAllowance":payrollempdetail.conveyanceAllowance, 
				"otherAllowances":payrollempdetail.otherAllowances, 
				"grossSalary":payrollempdetail.grossSalary, 
				"pfEmployeeContribution":payrollempdetail.pfEmployeeContribution, 
				"eSICEmployeeContribution":payrollempdetail.eSICEmployeeContribution, 
				"professionTax":payrollempdetail.professionTax, 
				"lWF":payrollempdetail.lWF, 
				"tDS":payrollempdetail.tDS,
				"otherDeductions":payrollempdetail.otherDeductions,
				"advanceAdjustment":payrollempdetail.advanceAdjustment,
				"totalDeductions":payrollempdetail.totalDeductions,
				"netSalary":payrollempdetail.netSalary,
				"pfEmployerContribution":payrollempdetail.pfEmployerContribution,
				"eSICEmployerContribution":payrollempdetail.eSICEmployerContribution,
				"pFAdminCharges":payrollempdetail.pFAdminCharges
			
				});	
$("#employeeDetails").val(JSON.stringify(empDetails)); 
				console.log($("#employeeDetails").val());
			
				var  markup = ""; 
				markup = "<tr><td><a href = '#' onclick = 'deleteRow(this,"+id+")'><img src='${deleteImg}' style = 'width: 20px;'/></a></td>"; 
				markup = markup + "<td>"+empId+"</td>"+
					"<td>"+employeeName+"</td>"+
					"<td>"+PAN+"</td>"+
					"<td>"+totaldays+"</td>"+ 
					"<td>"+basicSalary+"</td>"+
					"<td>"+DA+"</td>"+
					"<td>"+conveyanceAllowance+"</td>"+
					"<td>"+otherAllowances+"</td>"+
					"<td>"+grossSalary+"</td>"+
					"<td>"+pfEmployeeContribution+"</td>"+
					"<td>"+eSICEmployeeContribution+"</td>"+
					"<td>"+professionTax+"</td>"+
					"<td>"+lWF+"</td>"+
					"<td>"+tDS+"</td>"+
					"<td>"+otherDeductions+"</td>"+
					"<td>"+advanceAdjustment+"</td>"+
					"<td>"+totalDeductions+"</td>"+
					"<td>"+netSalary+"</td>"+
					"<td>"+pfEmployerContribution+"</td>"+
					"<td>"+eSICEmployerContribution+"</td>"+
					"<td>"+pFAdminCharges+"</td></tr>";

				$('#detailTable tr:last').after(markup);
				document.getElementById("detailTable").style.display="table";			
				
			
		 }
		  </c:forEach>  */
	
	 
var dateTest = '<c:out value= "${payrollAutoJV.date}"/>';			
	if(dateTest!=""){			
	 	newdate=formatDate(dateTest);	
		$("#date").datepicker("setDate", newdate);
		$("#date").datepicker("refresh");
	}
	

	
	var years = '<c:out value= "${yearList}"/>';	
	var transaction_id = '<c:out value= "${payrollAutoJV.payroll_id}"/>';	
	var ysize = '<c:out value= "${yearList.size()}"/>';	
	if((years!="")&&(transaction_id=="")){
		if(ysize!=1){
	 		$('#year-model').modal({
		    	show: true,
		   	});	 
	 		var j=0;
	 		<c:forEach var = "year" items = "${yearList}">
		    if(j==0)
		    	{			
		    	    startdate1='${year.start_date}';
		    	    enddate1='${year.end_date}';
		    	setdatelimit('${year.start_date}','${year.end_date}');
		    	}
		    j++;
		     </c:forEach>
		    
		}
		else
		{
			<c:forEach var = "year" items = "${yearList}">
	    	        startdate1='${year.start_date}';
	    	        enddate1='${year.end_date}';
				   setdatelimit('${year.start_date}','${year.end_date}');
			</c:forEach>	
			
		}			
	}	
	else
	{
	var savedyear='<c:out value= "${payrollAutoJV.accounting_year_id.year_id}"/>';
	<c:forEach var = "year" items = "${yearList}">
		if("${year.year_id}"==savedyear)
		{
	    	  startdate1='${year.start_date}';
	    	  enddate1='${year.end_date}';
	       setdatelimit('${year.start_date}','${year.end_date}');
	       var supdate="${journalVoucher.date}";
		   supdate=formatDate(supdate);
		   $("#date").val(supdate);
		}  
	</c:forEach>   
	
	}
		
});

function formatDate(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();
    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;	
    return [day,month,year].join('-');
}
function formatDate1(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();
    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;	
    return [day,month,year].join('/');
}

function setdatelimit(startdate,enddate)
{
	startdate1=startdate;
	enddate1=enddate;
	startdate=formatDate(startdate);
	enddate=formatDate(enddate);	
	curdate=formatDate(new Date());		
	var maxdate=curdate;		
	if(curdate < enddate)
		maxdate=enddate;		
	
	$("#date").datepicker({dateFormat: 'dd-mm-yy'});
	 $('#date').datepicker("option", { minDate: startdate, 
        maxDate: enddate });
}

function isValidDate(dateStr) {
	 
	 // Checks for the following valid date formats:
	 // MM/DD/YYYY
	 // Also separates date into month, day, and year variables
	 var datePat = /^(\d{2,2})(-)(\d{2,2})\2(\d{4}|\d{4})$/;
	 
	 var matchArray = dateStr.match(datePat); // is the format ok?
	 if (matchArray == null) {
	  //alert("Date must be in MM-DD-YYYY format")
	  return false;
	 }
	 
	 month = matchArray[1]; // parse date into variables
	 day = matchArray[3];
	 year = matchArray[4];
	 if (month < 1 || month > 12) { // check month range
	  //alert("Month must be between 1 and 12");
	  return false;
	 }
	 if (day < 1 || day > 31) {
	 // alert("Day must be between 1 and 31");
	  return false;
	 }
	 if ((month==4 || month==6 || month==9 || month==11) && day==31) {
	  //alert("Month "+month+" doesn't have 31 days!")
	  return false;
	 }
	 if (month == 2) { // check for february 29th
	  var isleap = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
	  if (day>29 || (day==29 && !isleap)) {
	  // alert("February " + year + " doesn't have " + day + " days!");
	   return false;
	    }
	 }
	 return true;  // date is valid
	}

function process(date){
	   var parts = date.split("-");
	   var date = new Date(parts[1] + "/" + parts[0] + "/" + parts[2]);
	   return date.getTime();
	}

var empDetails = [];
var subDetails = [];


var totalBasicSalary = 0;
var totalDA = 0;
var totalconveyanceAllowance = 0;
var totalotherAllowances = 0;
var totalgrossSalary = 0;
var totalpfEmployeeContribution = 0;
var totalESICEmployeeContribution = 0;
var totalprofessionTax= 0;
var totalLWF = 0;
var totalTDS = 0;
var totalotherDeductions = 0;
var totaladvanceAdjustment = 0;
var allTotalDeductions = 0;
var totalnetSalary = 0;
var totalpfEmployerContribution = 0;
var totalESICEmployerContribution = 0;
var totalPFAdminCharges = 0;

var salaryandWages="Salary and Wages";
var ePFPayable="PF Payable";
var eSICPayable="ESIC Payable";
var pTPayable="PT Payable";
var lWFPayable="LWF Payable";
var tDS192BPayable="TDS 192B Payable";
var salaryPayable="Salary Payable";
var providentFund ="Provident Fund";
var administrativeExpense="PF administrative charges";
var eSICEmployerContribution="ESIC";
var otherSalaryDeductions="Other Salary Deductions";
var salaryAdvance="Salary Advance";


var salaryandWagesDrAmount=0;
var ePFPayableCrAmount=0;
var eSICPayableCrAmount=0;
var pTPayableCrAmount=0;
var lWFPayableCrAmount=0;
var tDS192BPayableCrAmount=0;
var salaryPayableCrAmount=0;
var providentFundDrAmount =0;
var administrativeExpenseCrAmount=0;
var eSICDrAmount=0;
var eSICEmployerContributionDrAmount=0;
var totalCramount = 0;
var totalDramount = 0;
var otherSalaryDeductionsCramount=0;
var salaryAdvanceCramount=0;






$("#cheque_dd_no,#amount,#tds_amount,#HSNCode,#quantity,#rate,#discount,#labour_charges,#freight,#others,#CGST,#SGST,#IGST,#SCT").keypress(function(e) {
	if (!digitsAndDotOnly(e)) {
		return false;
	}
});

$(document).ready(function() {	
	
	setTimeout(function() {
        $("#successMsg").hide()
    }, 3000);
	
	    var employeeArray = []; 
		$('#employee_id').find('option').remove();
		$('#employee_id').append($('<option>', {
		    value: 0,
		    text: 'Select Employee Code'
		}));
		<c:forEach var = "employee" items = "${employeeList}">
		if(${employee.status=="true" }){
			var tempArray = []; 
    		tempArray["id"]=${employee.employee_id};
		    tempArray["name"]='${employee.code}';
		    employeeArray.push(tempArray);
		}
		</c:forEach>
		
		for(i=0;i<employeeArray.length;i++) {
	 		$('#employee_id').append($('<option>', {
		    	value: employeeArray[i].id,
			    text: employeeArray[i].name
			})); 
		}				
});
	 


				

function setDetails(e){
	var id = $("#employee_id").val();
	var empcodenew = $("#employeecodenew").html();
	var emplyeecodeold = $("#employee_id option[value=" + id + "]").html();
	 if(empcodenew == emplyeecodeold){
	    	$("#employee_id option[value=" + id + "]").hide();
	    	$("#employee_id").val(0);
	    	
	    	
	    	alert("Employee already exist");
	    	return false;
	    }
	<c:forEach var = "employee" items = "${employeeList}">
		var id = <c:out value = "${employee.employee_id}"/>
		if(id == e){
			
			$("#employee_id").val("${employee.employee_id}");
			$("#employeecode").val("${employee.code}");
	  		$("#employeeName").val("${employee.name}"); 
		  	$("#PAN").val("${employee.pan}");
		  	$("#totaldays").val("0");
		  	$("#basicSalary").val("${employee.basicSalary}");
		  	$("#DA").val("${employee.DA}");
		  	$("#conveyanceAllowance").val("${employee.conveyanceAllowance}");
		  	$("#otherAllowances").val("${employee.otherAllowances}");
		  	$("#grossSalary").val("${employee.grossSalary}");
		  	$("#pfEmployeeContribution").val("${employee.pfEmployeeContribution}");
		  	$("#eSICEmployeeContribution").val("${employee.eSICEmployeeContribution}");
		  	$("#professionTax").val("${employee.professionTax}");
		  	$("#lWF").val("${employee.lWF}");
		  	$("#tDS").val("${employee.tDS}");
		  	
			$("#administrativeExpense").val("${employee.pFAdminCharges}");
		  	
		  	$("#otherDeductions").val("${employee.otherDeductions}");
		  	$("#advanceAdjustment").val("${employee.advanceAdjustment}");
		  	$("#totalDeductions").val("${employee.totalDeductions}");
		  	$("#netSalary").val("${employee.netSalary}");
		  	$("#pfEmployerContribution").val("${employee.pfEmployerContribution}");
		  	$("#eSICEmployerContribution").val("${employee.eSICEmployerContribution}");
		  	$("#pFAdminCharges").val("${employee.pFAdminCharges}");
			
		  	
		  	
		  	
	       }
	</c:forEach>
}

function countWorkingDay()	
{
	
	 var datefield=document.getElementById("date").value;
		
	 var res = datefield.split("-");
	 var dd=parseInt(res[0]);
	 
	
	 var mm=parseInt(res[1]);
	 
	 var yy=parseInt(res[2]);
	 var days = new Date(yy, mm, 0).getDate();
	 $("#wdays").val(days);

}
function saveDetails(){
	var empId = $("#employeecode").val();
	var id = $("#employee_id").val();
	var empcodenew = $("#employeecodenew").html();
//	alert(empcodenew);
	var emplyeecodeold = $("#employee_id option[value=" + id + "]").html();
//	alert(emplyeecodeold);
	 if(empcodenew == emplyeecodeold){
	    	$("#employee_id option[value=" + id + "]").hide();
	    	$("#employee_id").val(0);
	    	
	    	
	    	alert("Employee already exist");
	    	return false;
	    }
	var employeeName = $("#employeeName").val();
	var PAN = $("#PAN").val();
	var totaldays = $("#totaldays").val();
	var basicSalary =$("#basicSalary").val();
	var DA =  $("#DA").val(); 
	var conveyanceAllowance = $("#conveyanceAllowance").val();  
	var otherAllowances = $("#otherAllowances").val();
	var grossSalary = $("#grossSalary").val();
	var pfEmployeeContribution = $("#pfEmployeeContribution").val();
	var eSICEmployeeContribution = $("#eSICEmployeeContribution").val();
	var professionTax = $("#professionTax").val();
	var lWF = $("#lWF").val();
	var tDS = $("#tDS").val();
	var otherDeductions = $("#otherDeductions").val();
	var advanceAdjustment = $("#advanceAdjustment").val();
	var totalDeductions = $("#totalDeductions").val();
	var netSalary = $("#netSalary").val();
	var pfEmployerContribution = $("#pfEmployerContribution").val();
	var eSICEmployerContribution = $("#eSICEmployerContribution").val();
	var pFAdminCharges = $("#pFAdminCharges").val();
	var workingdays = $("#wdays").val();
   
	/* 
	 var datefield=document.getElementById("date").value;
		
	 var res = datefield.split("-");
	 var dd=parseInt(res[0]);
	 
	
	 var mm=parseInt(res[1]);
	 
	 var yy=parseInt(res[2]);
	 */


/* 	if(workingdays.trim()  > "31")
		{
		alert("Working days can not Enter More than 31 days ");
		return false;
		} */ 
/* 		var workingdays = $("#wdays").val();
	var totaldays = $("#totaldays").val();
	
	
	 */

	
		 

	
	 if(empId==0){
		alert("Please Select Employee Code");
		return false;
	}
	/* else if(totaldays.trim() > "30"){
		alert(" Days Should be  less than or equal to  30");
		return false;
	} */
	else if(totaldays.trim()=="" || totaldays.trim()=="0"){
		alert("Please Enter Total Days");
		return false;
	}
	
	
	else if(basicSalary.trim()=="" || basicSalary.trim()=="0"){
		alert("Please Enter Basic Salary");
		return false;
	}
	else if(DA.trim()=="" || DA.trim()=="0"){
		alert("Please Enter DA");
		return false;
	}
	else if(conveyanceAllowance.trim()=="" || conveyanceAllowance.trim()=="0"){
		alert("Please Enter Conveyance Allowance");
		return false;
	}
	else if(otherAllowances.trim()=="" || otherAllowances.trim()=="0"){
		alert("Please Enter otherAllowances");
		return false;
	}
	else if(grossSalary.trim()=="" || grossSalary.trim()=="0"){
		alert("Please Enter grossSalary");
		return false;
	}
	else if(pfEmployeeContribution.trim()=="" || pfEmployeeContribution.trim()=="0"){
		//alert("Please Enter pfEmployeeContribution");
		//return false;
		//pfEmployeeContribution=0.0;
	}
	else if(eSICEmployeeContribution.trim()==""){
		//alert("Please Enter eSICEmployeeContribution");
		//return false;
	//	eSICEmployeeContribution=0.0;
		
	}
	else if(professionTax.trim()=="" ){
		alert("Please Enter professionTax");
		return false;
	}
	else if(lWF.trim()==""){
		//alert("Please Enter lWF");
		//return false;
	}
	else if(tDS.trim()=="" ){
		alert("Please Enter tDS");
		return false;
	}
	else if(otherDeductions.trim()=="" ){
		alert("Please Enter otherDeductions");
		return false;
	}
	else if(advanceAdjustment.trim()=="" ){
		alert("Please Enter advanceAdjustment");
		return false;
	}
	else if(totalDeductions.trim()=="" || totalDeductions.trim()=="0"){
		alert("Please Enter totalDeductions");
		return false;
	}
	else if(netSalary.trim()=="" || netSalary.trim()=="0"){
		alert("Please Enter netSalary");
		return false;
	}
	else if(pfEmployerContribution.trim()=="" ){
		//alert("Please Enter pfEmployerContribution");
		//return false;
	}
	else if(eSICEmployerContribution.trim()=="" ){
		//alert("Please Enter eSICEmployerContribution");
		//return false;
	}
	else if(pFAdminCharges.trim()=="" ){
		//alert("Please Enter pFAdminCharges");
		//return false;
	}

		if(!isDuplicate(empId)){
			
			if(pfEmployeeContribution.trim()==""){
				pfEmployeeContribution="0";
			}
			 if(eSICEmployeeContribution.trim()==""){
			       eSICEmployeeContribution="0";
		          }	
			if(pfEmployerContribution.trim()==""){
				pfEmployerContribution="0";
			}
			if(eSICEmployerContribution.trim()==""){
				eSICEmployerContribution="0";
			}
			if(pFAdminCharges.trim()==""){
				pFAdminCharges="0";
			}
			
			totalBasicSalary = parseFloat(totalBasicSalary) + parseFloat(basicSalary);
			totalDA = parseFloat(totalDA) + parseFloat(DA);
			totalconveyanceAllowance = parseFloat(totalconveyanceAllowance) + parseFloat(conveyanceAllowance);
			totalotherAllowances = parseFloat(totalotherAllowances) + parseFloat(otherAllowances);
			totalgrossSalary = parseFloat(totalgrossSalary) + parseFloat(grossSalary);
			totalpfEmployeeContribution = parseFloat(totalpfEmployeeContribution) + parseFloat(pfEmployeeContribution);
			totalESICEmployeeContribution = parseFloat(totalESICEmployeeContribution) + parseFloat(eSICEmployeeContribution);
			
			totalprofessionTax = parseFloat(totalprofessionTax) + parseFloat(professionTax);
			totalLWF = parseFloat(totalLWF) + parseFloat(lWF);
			totalTDS = parseFloat(totalTDS) + parseFloat(tDS);
			totalotherDeductions = parseFloat(totalotherDeductions) + parseFloat(otherDeductions);
			totaladvanceAdjustment = parseFloat(totaladvanceAdjustment) + parseFloat(advanceAdjustment);
			allTotalDeductions = parseFloat(allTotalDeductions) + parseFloat(totalDeductions);
			totalnetSalary = parseFloat(totalnetSalary) + parseFloat(netSalary);
			totalpfEmployerContribution = parseFloat(totalpfEmployerContribution) + parseFloat(pfEmployerContribution);
			totalESICEmployerContribution = parseFloat(totalESICEmployerContribution) + parseFloat(eSICEmployerContribution);
			totalPFAdminCharges = parseFloat(totalPFAdminCharges) + parseFloat(pFAdminCharges);
			
			addRow(id,empId,employeeName,PAN,totaldays,basicSalary,DA,conveyanceAllowance,otherAllowances,grossSalary,pfEmployeeContribution,eSICEmployeeContribution,professionTax,lWF,tDS,otherDeductions,advanceAdjustment,totalDeductions,netSalary,pfEmployerContribution,eSICEmployerContribution,pFAdminCharges);
			//hide dropdown selected value 
			$("#employee_id option[value=" + id + "]").hide();

		}
 
	$("#employee_id").val(0);	
	$("#employeecode").val("");
	$("#employee_id").val("");
	$("#employeeName").val("");
	$("#PAN").val("");
	$("#totaldays").val("");
	$("#basicSalary").val("");
	$("#DA").val(""); 
    $("#conveyanceAllowance").val("");  
	$("#otherAllowances").val("");
	$("#grossSalary").val("");
	$("#pfEmployeeContribution").val("");
	$("#eSICEmployeeContribution").val("");
    $("#professionTax").val("");
	$("#lWF").val("");
	$("#tDS").val("");
    $("#otherDeductions").val("");
	$("#advanceAdjustment").val("");
	$("#totalDeductions").val("");
    $("#netSalary").val("");
	$("#pfEmployerContribution").val("");
    $("#eSICEmployerContribution").val("");
    $("#pFAdminCharges").val("");
	
}

function showdiv(){
	var date = $("#date").val();
	var wday=$("#wdays").val();
	
	 if(date==0){
			alert("Please Select Date ");
			return false;
		}	
	 if(wday.trim()=="" || wday<0){
		 
		 alert("Please Enter Working Days ");
			return false;
	 }
	document.getElementById("details-data").style.display="block";
}

function editpayroll(id)
{
	
	window.location.assign('<c:url value="editEmployeeForPayroll"/>?id='+id);
	}

function deleteRow(e, id){
		$(e).closest('tr').remove();
		$("#employee_id option[value=" + id + "]").show();
		for(i = 0; i<empDetails.length;i++){
			if(empDetails[i].id == id){
				
				totalBasicSalary = totalBasicSalary - empDetails[i].basicSalary;
				
				totalDA = totalDA - empDetails[i].DA;
				
				totalconveyanceAllowance = totalconveyanceAllowance - empDetails[i].conveyanceAllowance;
				
				totalotherAllowances = totalotherAllowances - empDetails[i].otherAllowances;
				
				totalgrossSalary = totalgrossSalary - empDetails[i].grossSalary;
				
				totalpfEmployeeContribution = totalpfEmployeeContribution - empDetails[i].pfEmployeeContribution;
				
				totalESICEmployeeContribution = totalESICEmployeeContribution - empDetails[i].eSICEmployeeContribution;
				
				totalprofessionTax = totalprofessionTax - empDetails[i].professionTax;
				
				totalLWF = totalLWF - empDetails[i].lWF;
				
				totalTDS = totalTDS - empDetails[i].tDS;
				
				totalotherDeductions = totalotherDeductions - empDetails[i].otherDeductions;
				
				totaladvanceAdjustment = totaladvanceAdjustment - empDetails[i].advanceAdjustment;
				
				allTotalDeductions = allTotalDeductions - empDetails[i].totalDeductions;
				
				totalnetSalary = totalnetSalary - empDetails[i].netSalary;
				
				totalpfEmployerContribution = totalpfEmployerContribution - empDetails[i].pfEmployerContribution;
				
				totalESICEmployerContribution = totalESICEmployerContribution - empDetails[i].eSICEmployerContribution;
				
				totalPFAdminCharges = totalPFAdminCharges - empDetails[i].pFAdminCharges;
					
				empDetails.splice(i,1);
			}
		}	
		console.log(JSON.stringify(empDetails));		
	
}

function isDuplicate(empId){
	for(i = 0; i<empDetails.length;i++){
		if(empDetails[i].empId == empId){
			return true;
		}
	}
	return false;
}

function addRow(id,empId,employeeName,PAN,totaldays,basicSalary,DA,conveyanceAllowance,otherAllowances,grossSalary,pfEmployeeContribution,eSICEmployeeContribution,professionTax,lWF,tDS,otherDeductions,
		advanceAdjustment,totalDeductions,netSalary,pfEmployerContribution,eSICEmployerContribution,pFAdminCharges){
	empDetails.push({"id":id,
		"empId":empId,
		"employeeName":employeeName, 
		"PAN":PAN, 
		"totaldays":totaldays, 
		"basicSalary":basicSalary, 
		"DA":DA,
		"conveyanceAllowance":conveyanceAllowance, 
		"otherAllowances":otherAllowances, 
		"grossSalary":grossSalary, 
		"pfEmployeeContribution":pfEmployeeContribution, 
		"eSICEmployeeContribution":eSICEmployeeContribution, 
		"professionTax":professionTax, 
		"lWF":lWF, 
		"tDS":tDS,
		"otherDeductions":otherDeductions,
		"advanceAdjustment":advanceAdjustment,
		"totalDeductions":totalDeductions,
		"netSalary":netSalary,
		"pfEmployerContribution":pfEmployerContribution,
		"eSICEmployerContribution":eSICEmployerContribution,
		"pFAdminCharges":pFAdminCharges
		});	
	
	console.log(JSON.stringify(empDetails));
	var  markup = ""; 
	markup = "<tr><td><a href = '#' onclick = 'deleteRow(this,"+id+")'><img src='${deleteImg}' style = 'width: 20px;'/></a></td>"; 
	markup = markup + "<td>"+empId+"</td>"+
		"<td>"+employeeName+"</td>"+
		"<td>"+PAN+"</td>"+
		"<td>"+totaldays+"</td>"+ 
		"<td>"+basicSalary+"</td>"+
		"<td>"+DA+"</td>"+
		"<td>"+conveyanceAllowance+"</td>"+
		"<td>"+otherAllowances+"</td>"+
		"<td>"+grossSalary+"</td>"+
		"<td>"+pfEmployeeContribution+"</td>"+
		"<td>"+eSICEmployeeContribution+"</td>"+
		"<td>"+professionTax+"</td>"+
		"<td>"+lWF+"</td>"+
		"<td>"+tDS+"</td>"+
		"<td>"+otherDeductions+"</td>"+
		"<td>"+advanceAdjustment+"</td>"+
		"<td>"+totalDeductions+"</td>"+
		"<td>"+netSalary+"</td>"+
		"<td>"+pfEmployerContribution+"</td>"+
		"<td>"+eSICEmployerContribution+"</td>"+
		"<td>"+pFAdminCharges+"</td></tr>";

	$('#detailTable tr:last').after(markup);
	document.getElementById("detailTable").style.display="table";
}
function calculaterate1(e,flag){
    var empId = $("#employee_id").val();
	var employeeName = $("#employeeName").val();
	var PAN = $("#PAN").val();
	var totaldays = $("#totaldays").val();
	var basicSalary =$("#basicSalary").val();
	var DA =  $("#DA").val(); 
	var conveyanceAllowance = $("#conveyanceAllowance").val();  
	var otherAllowances = $("#otherAllowances").val();
	var grossSalary = $("#grossSalary").val();
	var pfEmployeeContribution = $("#pfEmployeeContribution").val(); // aDI
	var eSICEmployeeContribution = $("#eSICEmployeeContribution").val();
	var professionTax = $("#professionTax").val();
	var lWF = $("#lWF").val();
	var tDS = $("#tDS").val();
	var otherDeductions = $("#otherDeductions").val();
	var advanceAdjustment = $("#advanceAdjustment").val();
	var totalDeductions = $("#totalDeductions").val();
	var netSalary = $("#netSalary").val();
	var pfEmployerContribution = $("#pfEmployerContribution").val();
	var eSICEmployerContribution = $("#eSICEmployerContribution").val();
	
	
	
	var pFAdminCharges = $("#pFAdminCharges").val();
	
	if(pfEmployeeContribution.trim() ==""){
		pfEmployeeContribution=0.0;
	}
	if(eSICEmployeeContribution.trim()==""){
		
			eSICEmployeeContribution=0.0;
	}
	if(tDS==""){
		tDS=0.0;
	}
	if(lWF==""){
		lWF=0.0;
	}
	totalDeductions=parseFloat(pfEmployeeContribution)+parseFloat(professionTax)+parseFloat(eSICEmployeeContribution)+parseFloat(tDS)+parseFloat(lWF)+parseFloat(advanceAdjustment)+parseFloat(otherDeductions);
	totalDeductions=Math.round(totalDeductions);
	
	
	
	//calculate Net Salary
	netSalary=parseFloat(grossSalary)-parseFloat(totalDeductions);
	
	netSalary=Math.round(netSalary);
	
	if(parseFloat(grossSalary) < 21000)
	{
	eSICEmployerContribution=grossSalary*0.0475;
	}
else
	{
	eSICEmployerContribution=0;
	}
	//if(parseFloyerContribution=Math.round(eSICEmployerContribution);
	eSICEmployerContribution=eSICEmployerContribution.toFixed(2); 


//calculate PF Admin Charges
//pFAdminCharges= 0;


	
	
	$("#totalDeductions").val((totalDeductions).toFixed(2)); // cHANGE
	$("#netSalary").val((netSalary).toFixed(2)); // cHANGE
	$("#pfEmployerContribution").val(pfEmployerContribution);// cHANGE
	$("#eSICEmployerContribution").val((eSICEmployerContribution).toFixed(2)); // cHANGE
	$("#pFAdminCharges").val(pFAdminCharges);	   // cHANGE
	
}

function calculaterate(e,flag){

	$("#pfEmployeeContribution").val(0);
	$("#eSICEmployeeContribution").val(0);
	$("#professionTax").val(0);
	$("#lWF").val(0);
	$("#tDS").val(0);
	$("#advanceAdjustment").val(0);
    var empId = $("#employee_id").val();
	var employeeName = $("#employeeName").val();
	var PAN = $("#PAN").val();
	var totaldays = $("#totaldays").val();
	var basicSalary =$("#basicSalary").val();
	var DA =  $("#DA").val(); 
	var conveyanceAllowance = $("#conveyanceAllowance").val();  
	var otherAllowances = $("#otherAllowances").val();
	var grossSalary = $("#grossSalary").val();
	var pfEmployeeContribution = $("#pfEmployeeContribution").val(); // aDI
	var eSICEmployeeContribution = $("#eSICEmployeeContribution").val();
	var professionTax = $("#professionTax").val();
	var lWF = $("#lWF").val();
	var tDS = $("#tDS").val();
	var otherDeductions = $("#otherDeductions").val();
	var advanceAdjustment = $("#advanceAdjustment").val();
	var totalDeductions = $("#totalDeductions").val();
	var netSalary = $("#netSalary").val();
	var pfEmployerContribution = $("#pfEmployerContribution").val();
	var eSICEmployerContribution = $("#eSICEmployerContribution").val();
	var pFAdminCharges = $("#pFAdminCharges").val();
	var workingdays = $("#wdays").val();
	
	
	if(pfEmployeeContribution==""){
		pfEmployeeContribution=0.0;
	}
	if(eSICEmployeeContribution==""){
		eSICEmployeeContribution=0.0;
	}
	  if(workingdays < totaldays)
	{
	$("#totaldays").val("");
	alert("Total Days Should be less than or equal to   Working days");
	return false;
	}

//	var grossamountcal=parseFloat(basicSalary) +parseFloat(DA)+parseFloat(conveyanceAllowance)+parseFloat(otherAllowances);

	//calculate GrossSalary
	grossSalary=((parseFloat(basicSalary)+parseFloat(DA)+parseFloat(conveyanceAllowance)+parseFloat(otherAllowances))/workingdays)*totaldays;
	grossSalary=Math.round(grossSalary);

	//	document.getElementById("grossSalary").innerHTML = Math.round(grossSalary);
	//alert("new grossSalary  after round  off is :"+grossSalary);
	//calculate PF Employee Contribution
	if(parseFloat(grossSalary)>=15000 )
		{
			pfEmployeeContribution=1800
		}
	else
		{
			pfEmployeeContribution=(parseFloat(grossSalary)*0.12);
		}
	pfEmployeeContribution=Math.round(pfEmployeeContribution);
	//alert("new pfEmployeeContribution  after round  off is :"+pfEmployeeContribution);
//	pfEmployeeContribution=pfEmployeeContribution.toFixed(2); 
	//calculate ESIC Employee Contribution
	if(parseFloat(grossSalary) < 21000)
		{
			eSICEmployeeContribution=grossSalary*0.0475;
		}
	else
		{
			eSICEmployeeContribution=0;
		}

	eSICEmployeeContribution=Math.round(eSICEmployeeContribution);
	
	//alert("new eSICEmployeeContribution  after round  off is :"+eSICEmployeeContribution);
	//eSICEmployeeContribution=eSICEmployeeContribution.toFixed(2); 
	//calculate Profession Tax
	 if(parseFloat(grossSalary) > 7500 && parseFloat(grossSalary) <= 10000)
		{
			professionTax=175;
		}
	 else if(parseFloat(grossSalary) > 10000)
		{
			professionTax=200;
		}
	else
		{
			professionTax=0;
		}
	// professionTax=professionTax.toFixed(2); 
	//calculate lWF
	lWF=0;
	
	
	
	//calculate tDS
	tDS=0;
	
	
	//calculate Advance Adjustment
	advanceAdjustment=0;
	
	
	//calculate Other Deductions
	otherDeductions=0;
	
	
	//calculate Total Deductions
	totalDeductions=(parseFloat(pfEmployeeContribution)+parseFloat(professionTax)+parseFloat(eSICEmployeeContribution)+parseFloat(tDS)+parseFloat(lWF)+parseFloat(advanceAdjustment)+parseFloat(otherDeductions));

	
	
	//calculate Net Salary
	netSalary=parseFloat(grossSalary)-parseFloat(totalDeductions);
	
	
	//calculate PF Employer Contribution
	
	//calculate PF Employee Contribution
	if(parseFloat(grossSalary)>=15000 )
		{
		pfEmployerContribution=1800
		}
	else
		{
		pfEmployerContribution=(parseFloat(grossSalary)*0.12);
		}
	pfEmployerContribution=Math.round(pfEmployerContribution);

	//calculate ESIC Employer Contribution
	if(parseFloat(grossSalary) < 21000)
		{
		eSICEmployerContribution=grossSalary*0.0475;
		}
	else
		{
		eSICEmployerContribution=0;
		}
	

	eSICEmployerContribution=Math.round(eSICEmployerContribution);
	//calculate PF Admin Charges
	
	//pFAdminCharges= 0;
	pFAdminCharges= parseFloat(pFAdminCharges);

	
	

$("#grossSalary").val(grossSalary);
$("#pfEmployeeContribution").val(pfEmployeeContribution);
$("#eSICEmployeeContribution").val(eSICEmployeeContribution);// cHANGE
$("#professionTax").val(professionTax);

$("#lWF").val(lWF);
$("#tDS").val(tDS);
$("#advanceAdjustment").val(advanceAdjustment);


$("#otherDeductions").val(otherDeductions);

//totalDeductions=parseFloat((pfEmployeeContribution)+parseFloat(professionTax)+parseFloat(eSICEmployeeContribution)+parseFloat(tDS)+parseFloat(lWF)+parseFloat(advanceAdjustment)+parseFloat(otherDeductions));

$("#totalDeductions").val(totalDeductions);
$("#netSalary").val(netSalary);
$("#pfEmployerContribution").val(pfEmployerContribution);
$("#eSICEmployerContribution").val(eSICEmployerContribution);
if(!isNaN(pFAdminCharges)){
	$("#pFAdminCharges").val(pFAdminCharges);	   // cHANGE	
}else{
	var charges = 0;
	$("#pFAdminCharges").val(charges);;
}

	

}


function cancel(){
	window.location.assign('<c:url value="payrollAutoJVList"/>');
}

function saveyearid(){
	 $('#year-model').modal('hide');
} 

function save(){
	var tabledata=document.getElementById("detailTable").style.display="block";
	var datefield= $("#date").val();
	
	/* $("#pFAdminCharges").val(pFAdminCharges);	   // cHANGE */
	
	if(isValidDate(startdate1)==false){
		startdate1=formatDate(startdate1);
	 }
	if(isValidDate(enddate1)==false){
		enddate1=formatDate(enddate1);
		 }	
	
	var dateString = $("#date").val();
	var fisrtDate = dateString.split("/");
	var secondDate = dateString.split("-");		
	
	if(fisrtDate[1]!=undefined)
		{
		   var dateString1=formatDate1(fisrtDate);
		  dateString1 = dateString1.split("/");		
		   dateString=dateString1[0]+"-"+dateString1[1]+"-"+dateString1[2];
		 }
		if(secondDate[1]!=undefined)
		{
		   dateString=secondDate[0]+"-"+secondDate[1]+"-"+secondDate[2];			
		}
	if(dateString!="")
		{
	
		var dateStringnew=process(dateString);
		var startdatenew=process(startdate1);
		var enddatenew=process(enddate1);
		
		var oldDate = dateString.split("-");						
		//alert(oldDate[1]+"/"+oldDate[0]+"/"+oldDate[2]);
		$("#date").val(oldDate[1]+"/"+oldDate[0]+"/"+oldDate[2]);
		}
	
	var frmDate=document.getElementById("date").value;

	 if(frmDate=="")
		{
		alert("Please Select Date");
		return false;
		}
	
	
	salaryandWagesDrAmount=parseFloat(totalgrossSalary).toFixed(2);
	//alert(salaryandWagesDrAmount);
	ePFPayableCrAmount=parseFloat(totalpfEmployeeContribution+totalpfEmployerContribution+totalPFAdminCharges).toFixed(2);
	eSICPayableCrAmount=parseFloat(totalESICEmployeeContribution+totalESICEmployerContribution).toFixed(2);
	pTPayableCrAmount=parseFloat(totalprofessionTax).toFixed(2);
	lWFPayableCrAmount=parseFloat(totalLWF).toFixed(2);
	tDS192BPayableCrAmount=parseFloat(totalTDS).toFixed(2);
	salaryPayableCrAmount=parseFloat(totalnetSalary).toFixed(2);
	providentFundDrAmount=parseFloat(totalpfEmployerContribution).toFixed(2);
	pFAdminCharges=parseFloat(totalPFAdminCharges).toFixed(2);
	eSICDrAmount=parseFloat(totalESICEmployerContribution).toFixed(2);
	otherSalaryDeductionsCramount=parseFloat(totalotherDeductions).toFixed(2);
	salaryAdvanceCramount=parseFloat(totaladvanceAdjustment).toFixed(2);
	totalCramount=0;
	totalDramount=0;
	
	//alert
	//totalCramount = parseFloat((totalCramount)+parseFloat(otherSalaryDeductionsCramount)+parseFloat(salaryAdvanceCramount)+parseFloat(ePFPayableCrAmount)+parseFloat(eSICPayableCrAmount)+parseFloat(pTPayableCrAmount)+parseFloat(lWFPayableCrAmount)+parseFloat(tDS192BPayableCrAmount)+parseFloat(salaryPayableCrAmount)).toFixed(2);
	totalCramount = parseFloat((totalCramount)+parseFloat(otherSalaryDeductionsCramount)+parseFloat(salaryAdvanceCramount)+parseFloat(ePFPayableCrAmount)+parseFloat(eSICPayableCrAmount)+parseFloat(pTPayableCrAmount)+parseFloat(lWFPayableCrAmount)+parseFloat(tDS192BPayableCrAmount)+parseFloat(salaryPayableCrAmount)).toFixed(2);
	totalDramount = parseFloat((totalDramount)+parseFloat(salaryandWagesDrAmount)+parseFloat(providentFundDrAmount)+parseFloat(eSICDrAmount)+parseFloat(totalPFAdminCharges)).toFixed(2);
	
	
	subDetails.push({
		"salaryandWages":salaryandWages,
		"ePFPayable":ePFPayable, 
		"eSICPayable":eSICPayable,
		"otherSalaryDeductions":otherSalaryDeductions, 
		"salaryAdvance":salaryAdvance,
		"pTPayable":pTPayable, 
		"lWFPayable":lWFPayable, 
		"tDS192BPayable":tDS192BPayable,
		"salaryPayable":salaryPayable, 
		"providentFund":providentFund, 
		"administrativeExpense":administrativeExpense,
		"eSICEmployerContribution":eSICEmployerContribution,
		"salaryandWagesDrAmount":salaryandWagesDrAmount,
		"ePFPayableCrAmount":ePFPayableCrAmount, 
		"eSICPayableCrAmount":eSICPayableCrAmount, 
		"pTPayableCrAmount":pTPayableCrAmount, 
		"lWFPayableCrAmount":lWFPayableCrAmount, 
		"tDS192BPayableCrAmount":tDS192BPayableCrAmount, 
		"salaryPayableCrAmount":salaryPayableCrAmount, 
		"providentFundDrAmount":providentFundDrAmount, 
		"administrativeExpenseCrAmount":pFAdminCharges,
		"otherSalaryDeductionsCramount":otherSalaryDeductionsCramount, 
		"salaryAdvanceCramount":salaryAdvanceCramount,
		"eSICDrAmount":eSICDrAmount ,
		"totalCramount":totalCramount,
		"totalDramount":totalDramount
		
		
		});	
	
	 
/* 	alert("eSICPayableCrAmount is"+eSICPayableCrAmount);
	alert("pTPayableCrAmount is"+pTPayableCrAmount);
	alert("lWFPayableCrAmount is"+lWFPayableCrAmount);
	alert("tDS192BPayableCrAmount is"+tDS192BPayableCrAmount);
	alert("salaryPayableCrAmount is"+salaryPayableCrAmount);
	alert("providentFundDrAmount is"+providentFundDrAmount); 
	alert("administrativeExpenseCrAmount is"+pFAdminCharges);
	alert("eSICDrAmount is"+eSICDrAmount); 
	alert("ePFPayableCrAmount is"+ePFPayableCrAmount); 	 
	alert("lWFPayableCrAmount is"+lWFPayableCrAmount);
	alert("tDS192BPayableCrAmount is"+tDS192BPayableCrAmount);
	alert("salaryandWagesDrAmount is"+salaryandWagesDrAmount); 
	alert("otherSalaryDeductionsCramount is"+otherSalaryDeductionsCramount);
	alert("salaryAdvanceCramount is"+salaryAdvanceCramount);
	alert("totalCramount is"+totalCramount);
	alert("totalDramount is"+totalDramount);	*/
	$("#subledgerDetails").val(JSON.stringify(subDetails));
	console.log($("#subledgerDetails").val());
	$("#employeeDetails").val(JSON.stringify(empDetails));
	console.log($("#employeeDetails").val());
	
	/*  */
	
if(totalCramount>0 && totalDramount>0 && totalCramount==totalDramount)
	{
	 return true;
	}
 else
	 {
	 if(totalCramount==0 && totalDramount==0 )
		 {
		 subDetails = [];
		 
		 alert(" at least add 1 employee");
		 return false;
		 }
	 alert(" Cr amount and Dr amount is not equal");
	 return false;
	 } 

/* 
  if(totalCramount==totalDramount)
		{
		 return true;
		}
	 else
		 {
		 alert(" Cr amount and Dr amount is not equal");
		 return false;
		 } 
		 	  */
	
	 
} 

</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>