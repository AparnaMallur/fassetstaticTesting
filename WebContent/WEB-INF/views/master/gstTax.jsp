<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>

<spring:url value="/resources/images/edit.png" var="editImg" />
<spring:url value="/resources/images/view.png" var="viewImg" />
<script type="text/javascript" src="${valid}"></script>
<div class="breadcrumb">
	<h3>GST Master</h3>
	<a href="homePage">Home</a> » <a href="gstList">GST</a> » <a href="#">Create</a>
</div>
<div class="col-md-12 wideform">	
	<c:if test="${successMsg != null}">
		<div class="successMsg" id = "successMsg"> 
			<strong>${successMsg}</strong>
		</div>
	</c:if>
	<div class="fassetForm" id = "gstDiv">
		<form:form id="gstForm" action="saveGST" method="post" commandName = "gst" onsubmit="return checkDate();">
			<div class="row">							
				<form:hidden path="tax_id"/>
			</div>
		<div class="row">	
				<div class="col-md-3 control-label"><label>Start Date<span>*</span></label></div>		
				<div class="col-md-9">	
				   <input type = "text" style = "color: black;" id = "start_date" name = "start_date"  onchange="setDate(this)"  placeholder = "Start Date">	
					<span class="logError"><form:errors path="start_date" /></span>
					
			</div>
		</div>	
		<%-- <div class="row">	
				<div class="col-md-3 control-label"><label>End Date<span>*</span></label></div>		
				<div class="col-md-9">	
 	                <input type = "text" style = "color: black;" id = "end_date" name = "end_date" placeholder = "End Date">
					<span class="logError"><form:errors path="end_date" /></span>
				
				</div>
		</div>	 --%>
				<div class = "row">					
				<div class="col-md-3 control-label"><label>HSN/SAC Number<span>*</span></label></div>
				<div class = "col-md-9">
					<form:input path="hsc_sac_code" class="logInput" id = "hsc_sac_code" placeholder="HSN/SAC Number"  maxlength="10" />
					<span class="logError"><form:errors path="hsc_sac_code" /></span>
				</div>				
			</div>	
			
			<div class = "row">					
				<div class="col-md-3 control-label"><label>Description<span>*</span></label></div>
				<div class = "col-md-9">
					<form:input path="description" class="logInput" id = "description" placeholder="Description"  maxlength="8000" />
					<span class="logError"><form:errors path="description" /></span>
				</div>				
			</div>	
		    <div class="row">
				<div class="col-md-3 control-label"><label>Chapter<span>*</span></label></div>
				<div class="col-md-9">	
					<form:select path="chapter_id" class="logInput">
						<form:option value="0" label="Select Chapter" id="chapter_id" />						
						<c:forEach var = "chapter" items = "${chapterList}">							
							<form:option value = "${chapter.chapter_id}">${chapter.chapterNo}</form:option>	
						</c:forEach>									
					</form:select>
					<span class="logError"><form:errors path="chapter_id" /></span>
				</div>
			</div>
			
			 <div class="row">
				<div class="col-md-3 control-label"><label>Schedule<span>*</span></label></div>
				<div class="col-md-9">	
					<form:select path="schedule_id" class="logInput">
						<form:option value="0" label="Select Schedule"/>						
						<c:forEach var = "schedule" items = "${scheduleList}">							
							<form:option value = "${schedule.schedule_id}">${schedule.scheduleName}</form:option>	
						</c:forEach>									
					</form:select>
					<span class="logError"><form:errors path="schedule_id" /></span>
				</div>
			</div>
			
	
			<div class = "row">	
				<div class="col-md-3 control-label"><label>CGST<span>*</span></label></div>
				<div class = "col-md-9">
					<form:input path = "cgst1" class="logInput" id = "cgst1" placeholder="CGST"   maxlength="3"/>			
					<span class="logError"><form:errors path="cgst1" /></span>
				</div>	
			</div>
			<div class = "row">	
				
				<div class="col-md-3 control-label"><label>IGST<span>*</span></label></div>
				<div class = "col-md-9">
					<form:input path = "igst1" class="logInput" id = "igst1" placeholder="IGST"  maxlength="3" />	
					<span class="logError"><form:errors path="igst1" /></span>
				</div>
			</div>
			<div class = "row">
				<div class="col-md-3 control-label"><label>SGST<span>*</span></label></div>
				<div class = "col-md-9">
					<form:input path = "sgst1" class="logInput" id = "sgst1" placeholder="SGST"  maxlength="3" />			
					<span class="logError"><form:errors path="sgst1" /></span>
				</div>
			</div>
			<div class = "row">	
				
				<div class="col-md-3 control-label"><label>CESS<span>*</span></label></div>
				<div class = "col-md-9">
					<form:input path = "scc" class="logInput" id = "scc" placeholder="CESS" maxlength="3" />
					<span class="logError"><form:errors path="scc" /></span>
				</div>
			</div>
			<div class = "row">					
				<div class="col-md-3 control-label"><label>Status<span>*</span></label></div>
				<div class = "col-md-9">					
					<form:radiobutton path="status" value="true" checked="checked" />Enable 
					<form:radiobutton path="status" value="false" />Disable
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
<script>
	$(function() {
	    setTimeout(function() {
	        $("#successMsg").hide()
	    }, 3000);	    
	         $( "#start_date" ).datepicker();
			 $( "#end_date" ).datepicker();	      
	    $("#igst1").keypress(function(e) {
			if (!digitsAndDotOnly(e)) {
				return false;
			}
		});
	    
	    $("#sgst1").keypress(function(e) {
			if (!digitsAndDotOnly(e)) {
				return false;
			}
		});
	    
	    $("#cgst1").keypress(function(e) {
			if (!digitsAndDotOnly(e)) {
				return false;
			}
		});
		
	});
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
	function checkDate()
	{
		var startdate=document.getElementById("start_date").value;
		var enddate=document.getElementById("end_date").value;
		var date1 = new Date(startdate);
		var date2 = new Date(enddate);
		if(startdate=="")
		{
		alert("Please Select Start Date");
		return false;
		}
	else if(enddate=="")
		{
		alert("Please Select End Date");
		return false;
		}
		else if(date1 > date2)
		{				
			alert("End Date Cann't be greater than Start Date");
			return false;
		}
		else
			{
		return true;
			}
	}
	function cancel(){
		window.location.assign('<c:url value = "gstList"/>');	
	}
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
		var start_date = '<c:out value= "${gst.start_date}"/>';	
		
		if(start_date!="")
		{
		 	newdate=formatDate(start_date);	
			$("#start_date").datepicker("setDate", newdate);
			$("#start_date").datepicker("refresh");
		}
		var end_date = '<c:out value= "${gst.end_date}"/>';	
		if(end_date!="")
		{
		 	newdate=formatDate(end_date);	
			$("#end_date").datepicker("setDate", newdate);
			$("#end_date").datepicker("refresh");
		}
	});
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>