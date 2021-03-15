<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>

<script type="text/javascript" src="${valid}"></script>
<div class="breadcrumb">
	<h3>BANK BALANCE REPORT</h3>
	<a href="homePage">Home</a> » <a href="exceptionReport2">BANK BALANCE REPORT</a>
</div>
<div class="col-md-12 wideform">
	<div class="fassetForm">
		<form:form id=""
			action="exceptionReport2" method="post" commandName="form" onsubmit="return checkdata();">
			<div class="row">
				<form:input path="fromDate" id="from_date" hidden="hidden" />
				<form:input path="toDate" id="to_date" hidden="hidden" />
			</div>

			<div class="row">
				<div class="col-md-2 control-label">
					<label>From Date<span>*</span></label>
				</div>
				<div class="col-md-10">
					<input type="text" style="color: black;" id="frmDate"
						name="frmDate" onchange="setDate(this)" placeholder="From Date" autocomplete="off">
					<span class="logError"><form:errors path="fromDate" /></span>
				</div>
			</div>
			<div class="row">
				<div class="col-md-2 control-label">
					<label>To Date<span>*</span></label>
				</div>
				<div class="col-md-10">
					<input type="text" style="color: black;" id="toDate" name="toDate"
						onchange="setDate1(this)" placeholder="To Date" autocomplete="off"> <span
						class="logError"><form:errors path="toDate" /></span>
				</div>
			</div>		
			<div class="row text-center-btn">
				<button class="fassetBtn" type="submit" onsubmit="submit()">
					<spring:message code="btn_show_report" />
				</button>
				<button class="fassetBtn" type="button" onclick="cancel()">
					<spring:message code="btn_cancel" />
				</button>
			</div>
		</form:form>
	</div>
</div>
<script type="text/javascript">

	function setDate(e){
		document.getElementById("from_date").value = e.value;
		// date format validation
		var datevali = document.getElementById("frmDate").value;
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
	function cancel(){
		window.location.assign('<c:url value = "homePage"/>');	
	}
	
	
	function checkdata(){
		
	
		var frmDate=document.getElementById("frmDate").value;
		var toDate=document.getElementById("toDate").value;
		var date1 = new Date(frmDate);
		var date2 = new Date(toDate);	
		
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
			else if(date1 > date2)
			{				
				alert("From Date Cann't be greater than To Date");
				return false;
			}
		else
			{
			return true;
			}
		//frmDate
		//toDate
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
</script>
<script>
	$(function() {
			$( "#frmDate" ).datepicker();
			$( "#toDate" ).datepicker();
	   });
 </script>

<%@include file="/WEB-INF/includes/footer.jsp"%>