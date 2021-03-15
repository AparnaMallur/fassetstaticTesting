<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_EIGHTEEN%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THIRTY%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>
<div class="breadcrumb">
	<h3>Accounting Year</h3>					
	<a href="homePage">Home</a> » <a href="accountingYearList">Accounting Year</a> » <a href="#">Create</a>
</div>	
<c:if test="${successMsg != null}">
	<div class="successMsg" id = "successMsg"> 
		<strong>${successMsg}</strong>
	</div>
</c:if>
<div class="col-md-12 wideform">
	<div class="fassetForm">
		<form:form id="accountingYearForm" action="saveAccountingYear" method="post" commandName = "year">
		<div class="row">
				<form:input path="year_id" hidden = "hidden"/>
				<form:input path="created_date" hidden = "hidden"/>
		</div>
		<div class="row">	
				<div class="col-md-2 control-label"><label>Year Range<span>*</span></label></div>		
				<div class="col-md-10">	
					<form:input path="year_range" class="logInput"
						id = "year_range" maxlength="${SIZE_EIGHTEEN}" placeholder="Year Range" />
					<span class="logError"><form:errors path="year_range" /></span>
				</div>
		</div>	
		<div class="row">	
				<div class="col-md-2 control-label"><label>Start Date<span>*</span></label></div>		
				<div class="col-md-10">	
				   <input type = "text" style = "color: black;" id = "start_date" name = "start_date"  onchange="setDate(this)" placeholder = "Start Date">	
					<span class="logError"><form:errors path="start_date" /></span>
					
			</div>
		</div>	
		<div class="row">	
				<div class="col-md-2 control-label"><label>End Date<span>*</span></label></div>		
				<div class="col-md-10">	
 	                <input type = "text" style = "color: black;" id = "end_date" name = "end_date" onchange="setDate1(this)" placeholder = "End Date">
					<span class="logError"><form:errors path="end_date" /></span>
				
				</div>
		</div>	
		<div class="row">	
				<div class="col-md-2 control-label"><label>Status<span>*</span></label></div>		
				<div class="col-md-10">	
					<form:radiobutton path="status" value="true" checked="checked" />Enable 
					<form:radiobutton path="status" value="false" />Disable
					<%-- <span class="logError"><form:errors path="status" /></span> --%>
					
				</div>
		</div>	
			<div class="row text-center-btn">
				<button class="fassetBtn" type="submit">
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

function setDate(e){
	document.getElementById("start_date").value = e.value;
	// date format validation
	var datevali = document.getElementById("start_date").value;
if(isValidDate(datevali)==true){
	 
		 return true;
 }else{
	alert("Invalid Date");
	window.location.reload();
	
}  
}
function setDate1(e){
	document.getElementById("end_date").value = e.value;
	// date format validation
	var datevali = document.getElementById("end_date").value;
if(isValidDate(datevali)==true){
	 
		 return true;
 }else{
	alert("Invalid Date");
	window.location.reload();
	
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
	    setTimeout(function() {
	        $("#successMsg").hide()
	    }, 3000);
	    
	});
	function cancel(){
		window.location.assign('<c:url value = "accountingYearList"/>');	
	}

      $(function() {
         $( "#start_date" ).datepicker();
		 $( "#end_date" ).datepicker();
      });
      function formatDate(date) {
    	    var d = new Date(date),
    	        month = '' + (d.getMonth() + 1),
    	        day = '' + d.getDate(),
    	        year = d.getFullYear();
    	    if (month.length < 2) month = '0' + month;
    	    if (day.length < 2) day = '0' + day;

    	    return [month,day,year].join('/');
    	}
    	$( document ).ready(function() {
    		var start_date = '<c:out value= "${year.start_date}"/>';	
    		
    		if(start_date!="")
    		{
    		 	newdate=formatDate(start_date);	
    			$("#start_date").datepicker("setDate", newdate);
    			$("#start_date").datepicker("refresh");
    		}
    		var end_date = '<c:out value= "${year.end_date}"/>';	
    		if(end_date!="")
    		{
    		 	newdate=formatDate(end_date);	
    			$("#end_date").datepicker("setDate", newdate);
    			$("#end_date").datepicker("refresh");
    		}
    	});
 </script>
<%@include file="/WEB-INF/includes/footer.jsp" %>