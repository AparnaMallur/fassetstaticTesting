<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_EIGHTEEN%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THIRTY%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>
<c:set var="SIZE_TWELVE"><%=MyAbstractController.SIZE_TWELVE%></c:set>
<c:set var="SIZE_THIRTEEN"><%=MyAbstractController.SIZE_13%></c:set>

<c:if test="${successMsg != null}">
	<div class="successMsg" id = "successMsg"> 
		<strong>${successMsg}</strong>
	</div>
</c:if>
<div class="breadcrumb">
	<h3>Employee Master</h3>				
	<a href="homePage">Home</a> » <a href="allemployee">Employee Master</a> » <a href="#">Create</a>
</div>	
<div class="col-md-12 wideform">
	<div class="fassetForm">
	
		<form:form id="employeeForm" action="saveemployee" method="post" commandName = "employee" onsubmit="return validateForm();">
			<div class="row">				
				<form:input path="employee_id" hidden = "hidden"/>
			</div>


			<div class="row">	
				<div class="col-md-2 control-label"><label> Employee ID:<span>*</span></label></div>		
				<div class="col-md-10">
					<c:if test="${employee.employee_id != null}">
						<form:input path="code" class="logInput" id = "name" placeholder="Employee ID" />
					</c:if>
					<c:if test="${employee.employee_id == null}">
						<form:input path="code" class="logInput" id = "name" placeholder="Employee ID" />
					</c:if>
			
					<%-- <form:input path="code" class="logInput" id ="name" maxlength="15" readonly="readonly" placeholder="Employee ID" /> --%>
					<span class="logError"><form:errors path="code" /></span>				
				</div>	
			</div>
			
			<div class="row">	
				<div class="col-md-2 control-label"><label> Name:<span>*</span></label></div>		
				<div class="col-md-10">
					<form:input path="name" class="logInput" id ="name" maxlength="60" 
					onkeypress="return (event.charCode > 64 && 
					event.charCode < 91) || (event.charCode > 96 && event.charCode < 123)||(event.charCode==32)" 
					
					placeholder="Name" />
					<span class="logError"><form:errors path="name" /></span>				
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-2 control-label"><label>Date of Joining:<span>*</span></label></div>		
				<div class="col-md-10">	
			   		<form:input  type="text" style = "color: black;" path="doj" id ="date" name = "date"
			   		  placeholder = "Date of Joining" autocomplete="off" onchange="dateRestriction()" onclick="setDatePicker()" />
			   		  <span class="logError"><form:errors path="doj" /></span>				
				</div>
			
			</div>
			<%-- 
			<div class="row">	
				<div class="col-md-2 control-label"><label>Date<span>*</span></label></div>		
				<div class="col-md-10">	
			   		<form:input type = "text" style = "color: black;" path="date" id = "date" name = "date"
			   		  placeholder = "Date" autocomplete="off" onchange="dateRestriction()" onclick="setDatePicker()"/>	
					<span class="logError"><form:errors path="date" /></span>					
				</div>
			
			</div>
			 --%>
			
			<div class="row">	
				<div class="col-md-2 control-label"><label>Mobile Number:<span>*</span></label></div>		
				<div class="col-md-10">
					<form:input path="mobile" class="logInput" id = "mobile_no" minlength="${SIZE_TEN}" maxlength="${SIZE_THIRTEEN}" placeholder="Mobile Number" />
					<span class="logError"><form:errors path="mobile" /></span>
				</div>	
			</div>	
			
			<div class="row">	
				<div class="col-md-2 control-label"><label>Address:<span>*</span></label></div>		
				<div class="col-md-10">
				<form:textarea path="current_address" class="logInput" maxlength="255"
								id="current_address" rows="3" placeholder="Current Address"></form:textarea>
					<span class="logError"><form:errors path="current_address" /></span>
				</div>	
			</div>
			
			<div class="row">	
				<div class="col-md-2 control-label"><label>PAN:<span>*</span></label></div>		
				<div class="col-md-10">
					<form:input path="pan" class="logInput" id ="pan_no" maxlength="${SIZE_TEN}"  minlength="${SIZE_TEN}" placeholder="PAN Number" />
					<span class="logError"><form:errors path="pan" /></span>
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-2 control-label"><label>Aadhaar No:<span>*</span></label></div>		
				<div class="col-md-10">
					<form:input path="adharNo" class="logInput" id ="adhaar_no" maxlength="${SIZE_TWELVE}"  minlength="${SIZE_TWELVE}"  placeholder="Aadhaar No" />
					<span class="logError"><form:errors path="adharNo" /></span>
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-2 control-label"><label>Basic Salary:</label></div>		
				<div class="col-md-10">
				<c:if test="${employee.basicSalary==null}">
					<form:input path="basicSalary" class="logInput" value="0"  id ="basicSalary" maxlength="${SIZE_TEN}" placeholder="Basic Salary" 
					 onchange="validateSalary()"/>
					 </c:if>
					 <c:if test="${employee.basicSalary!=null}">
					<form:input path="basicSalary" class="logInput"   id ="basicSalary" maxlength="${SIZE_TEN}" placeholder="Basic Salary" 
					 onchange="validateSalary()"/>
					 </c:if>
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-2 control-label"><label>DA:</label></div>		
				<div class="col-md-10">	 
					<c:if test="${employee.DA==null}">
					<form:input path="DA" class="logInput" value="0" id = "DA" maxlength="${SIZE_TEN}" placeholder="DA" />
					</c:if>
					<c:if test="${employee.DA!=null}">
					<form:input path="DA" class="logInput"  id = "DA" maxlength="${SIZE_TEN}" placeholder="DA" />
					</c:if>
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-2 control-label"><label>Conveyance Allowance:</label></div>		
				<div class="col-md-10">
				<c:if test="${employee.conveyanceAllowance==null}">
					<form:input path="conveyanceAllowance" value="0"  class="logInput" id = "conveyanceAllowance" maxlength="${SIZE_TEN}" placeholder="Conveyance Allowance" />
				</c:if>
					<c:if test="${employee.conveyanceAllowance!=null}">
					<form:input path="conveyanceAllowance"   class="logInput" id = "conveyanceAllowance" maxlength="${SIZE_TEN}" placeholder="Conveyance Allowance" />
				</c:if>
				</div>	
			</div>	
			<div class="row">	
				<div class="col-md-2 control-label"><label>Other Allowances:</label></div>		
				<div class="col-md-10">
				<c:if test="${employee.otherAllowances==null}">
					<form:input path="otherAllowances" class="logInput"  value="0" id = "otherAllowances" maxlength="${SIZE_TEN}" placeholder="Other Allowances" />
				</c:if>
				<c:if test="${employee.otherAllowances!=null}">
					<form:input path="otherAllowances" class="logInput"   id = "otherAllowances" maxlength="${SIZE_TEN}" placeholder="Other Allowances" />
				</c:if>
				</div>	
			</div>	
				
 			<div class="row">
				<div class="col-md-2 control-label"><label>Status:</label></div>		
				<div class="col-md-10">	
					<form:radiobutton path="status" value="true" checked="checked" />Join
					<form:radiobutton path="status" value="false" />Left
				</div>
			</div>
			
			<!-- <div class="col-md-9">
							<form:radiobutton path="status" value="true" checked="checked" />
							Join
							<form:radiobutton path="status" value="false" />
							Left
						</div> -->
			
			<div class="row text-center-btn">
				<button class="fassetBtn" type="submit"  >
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

//code for date
 function setDate(e){
	 	document.getElementById("from_date").value = e.value;	
	 	// date format validation
	 	var datevali = document.getElementById("date").value;
	 if(isValidDate(datevali)==true){
	 	 
	 		 return true;
	  }else{
	 	alert("Invalid Date");
	 	window.location.reload();
	 	
	 }  
	 }


	 

	 function isValidDate(dateStr) {
	 	
	 	 var datePat= /^((0?[1-9]|1[012])[- /.](0?[1-9]|[12][0-9]|3[01])[- /.](19|20)?[0-9]{2})*$/;
	 	 
	 	 var matchArray = dateStr.match(datePat); // is the format ok?
	 			 
	 	 if (matchArray == null) {
	 	  return false;
	 	 }
	 	 
	 	 month = matchArray[2];
	 	 day   = matchArray[3];
	 	 
	 	 
	 	 if (month < 1 || month> 12) { // check month range
	 		  alert("Month must be between 1 and 12");
	 		  return false;
	 		 }
	 	 
	 	 if (day < 1 || day > 31) {
	 		  alert("Day must be between 1 and 31");
	 		  return false;
	 		 }
	 	
	 	 return true;  // date is valid
	 	}

	 function saveyearid(){
		 $('#year-model').modal('hide');
	}

	 $(function() {
	 		$( "#date" ).datepicker({maxDate:0});
	 		
	    });

		
		var datefield=document.getElementById("date");
		
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
			
			// var res = datefield.split("-"); commented not allowing feb month date of joining
			 var res = datefield.split("/");
			 
			 var dd=parseInt(res[1]);
			 var mm=parseInt(res[0]);
			 var yyyy=parseInt(res[2]);
			 
			   
			var flag = 	 checkDate(dd,mm,yyyy);
			 var nd = res[0]+"/"+res[1]+"/"+res[2];
			    var ud = new Date(nd);
				var td = new Date();
				
				ud.setHours(0,0,0,0);
				
				td.setHours(0,0,0,0);
				
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
		 
		 function checkDate(dd,mm,yyyy)
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
					
					var flag = ((yyyy%4 == 0) & (yyyy%100 != 0)) || yyyy %400 == 0;
					
					
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

 function ValidateForm(){

 	
 	/* var doj=document.getElementById("doj").value; */
 	var first_name=document.getElementById("name").value;
 	var doj=document.getElementById("date").value;
 	var pan_no=document.getElementById("pan_no").value;
 	var adhaar_no=document.getElementById("adhaar_no").value;

 	var mobile_no=document.getElementById("mobile_no").value;
 	var current_address=document.getElementById("current_address").value;


 if(first_name =="" || first_name == null){
 		alert("Please Enter First Name");
 		
 	}
 if(doj==0){
 		alert("Please Enter Date of Joining");
 		return false;
 	}

 if(pan_no==0){
 		alert("Please Select Pan no");
 		return false;
 	}
 if(adhaar_no==0){
 		alert("Please Select Adhaar No");
 		return false;
 	}
 if(mobile_no==0){
 	alert("Please Enter Mobile umber");
 	return false;
 }

 if(current_address==0){
 	alert("Please Enter Current address");
 	return false;
 }

 }


 setTimeout(function() {
     $("#successMsg").hide()
 }, 3000);

 $("#").keypress(function(e) {
 	if (!lettersAndHyphenOnly(e)) {
 		return false;
 	}
 });

 
 $(function() {
 	     
 	$("#last_name").keypress(function(e) {
 		if (!letters(e)) {
 			return false;
 		}
 	});	
 	
 	
 	$("#mobile_no,#adhaar_no").keypress(function(e) {
 		if (!digitsOnly(e)) {
 			return false;
 		}
 	});
 	
 	$("#pan_no").on('input', function(){	    
 	    var start = this.selectionStart,
 	        end = this.selectionEnd;
 	    
 	    this.value = this.value.toUpperCase();
 	    this.setSelectionRange(start, end);
 	});
 	
 	$("#basicSalary,#conveyanceAllowance,#DA,#otherAllowances").keypress(function(e) {
 		if (!digitsOnly(e)) {
 			return false;
 		}
 	});

 	getStateList('${userform.company.countryId}');
 	<c:if test="${userform.company.stateId != null}">
  $("#stateId").val('${userform.company.stateId}');
   </c:if>
 	getCityList(document.getElementById("stateId").value);
   <c:if test="${userform.company.cityId != null}">
 	$("#cityId").val('${userform.company.cityId}');
   </c:if > 
   
   <c:if test="${userform.company.bank!= null}">
   	$("#bankId").val('${userform.company.bank.bank_id}');
   </c:if> 

 });
 	
 	
 	 
 	 function cancel(){
 		 window.location.assign('<c:url value = "allemployee"/>');	
 	 }
 
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>