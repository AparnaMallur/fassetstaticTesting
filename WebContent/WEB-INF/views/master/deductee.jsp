<%@include file="/WEB-INF/includes/header.jsp"%>
<%@page import="com.fasset.controller.abstracts.MyAbstractController"%>
<script type="text/javascript" src="${valid}"></script>
<c:set var="SIZE_EIGHTEEN"><%=MyAbstractController.SIZE_TWO_HUNDRED%></c:set>
<c:set var="SIZE_THIRTY"><%=MyAbstractController.SIZE_THREE_HUNDRED%></c:set>
<c:set var="SIZE_TEN"><%=MyAbstractController.SIZE_TEN%></c:set>
<c:set var="SIZE_SIX"><%=MyAbstractController.SIZE_SIX%></c:set>
<div class="breadcrumb">
	<h3>TDS Master</h3>					
	<a href="homePage">Home</a> » <a href="deducteeList">TDS Master</a> » <a href="#">Create</a>
</div>	
<c:if test="${successMsg != null}">
	<div class="successMsg" id = "successMsg"> 
		<strong>${successMsg}</strong>
	</div>
</c:if>
<div class="col-md-12 wideform">
	<div class="fassetForm">
		<form:form id="DeducteeForm" action="savedeductee" method="post" commandName = "deductee">
			<div class="row">	
				<form:input path="deductee_id" hidden = "hidden"/>
				<form:input path="deductee_title"  id="deductee_title" hidden = "hidden"/>
			
			</div>
			<div class="row">	
				<div class="col-md-2 control-label"><label>TDS Type<span>*</span></label></div>		
				<div class="col-md-10">
					<%-- <form:input path="deductee_title" class="logInput"
						id = "deductee_title" maxlength="${SIZE_THREE_HUNDRED}" placeholder="TDS Type" /> --%>
						
						<form:select path="tds_type_id" class="logInput"  id="tds_type_id" onChange="setTitle()" placeholder="status">
						<form:option value="0" label="Select TDS Type"/>	
						<c:forEach var = "tdsType" items = "${tdstypeList}">							
							<form:option value = "${tdsType.tdsType_id}">${tdsType.tdsType_desc}</form:option>	
						</c:forEach>									
					</form:select>
					<span class="logError"><form:errors path="deductee_title" /></span>
				</div>	
			</div>	
			<%-- <div class="row">	
				<div class="col-md-2 control-label"><label>Industry Type<span>*</span></label></div>		
				<div class="col-md-10">
					<form:select path="industry_id" class="logInput">
						<form:option value="0" label="Select Industry Type"/>						
						<c:forEach var = "list" items = "${industrytypeList}">		
							<form:option value = "${list.industry_id}">${list.industry_name}</form:option>	
						</c:forEach>									
					</form:select>
					<span class="logError"><form:errors path="industry_id" /></span>
				</div>	
			</div>		 --%>		
			<div class="row">	
				<div class="col-md-2 control-label"><label>Rate<span>*</span></label></div>		
				<div class="col-md-10">
					<form:input path="value1" class="logInput"
						id = "value"  placeholder="Rate"  maxlength="05" />
					<span class="logError"><form:errors path="value1" /></span>
				</div>	
			</div>		
				<div class="row">
				<div class="col-md-2 control-label">
					<label>Effective Date<span id="doj"> *</span></label>
				</div>
				<div class="col-md-10">
						
					<input type = "text" style = "color: black;" id = "Effective_date" name = "Effective_date"  onchange="setDate(this)"  placeholder = "Effective Dat">	
					<span class="logError"><form:errors path="Effective_date" /></span>
					
					
				</div>
			</div>	
			<div class="row">	
				<div class="col-md-2 control-label"><label>Status<span>*</span></label></div>		
				<div class="col-md-10">
					<form:radiobutton path="status" value="true" checked="checked" />Enable 
					<form:radiobutton path="status" value="false" />Disable
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
	$(function() {		
	    setTimeout(function() {
	        $("#successMsg").hide()
	    }, 3000);

		$("#deductee_title").keypress(function(e) {
			if (!lettersAndHyphenOnly(e)) {
				return false;
			}
		});
		$("#value").keypress(function(e) {
			if (!digitsAndDotOnly(e)) {
				return false;
			}
			
		});
		$( "#Effective_date" ).datepicker();
	});
	function cancel(){
		window.location.assign('<c:url value = "deducteeList"/>');	
	}
	function setTitle(){
		var abc = document.getElementById("tds_type_id");
		var value= abc.options[abc.selectedIndex].text;
		
		document.getElementById("deductee_title").value = value;
		
	}
	function setDate(e){
		document.getElementById("Effective_date").value = e.value;
		// date format validation
		var datevali = document.getElementById("Effective_date").value;
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

		    return [month,day,year].join('/');
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
		
		$( document ).ready(function() {
			
			var start_date = '<c:out value= "${deductee.effective_date}"/>';	
			

			if(start_date!="")
			{
			 	newdate=formatDate(start_date);	
				$("#Effective_date").datepicker("setDate", newdate);
				$("#Effective_date").datepicker("refresh");
			}
			var typeId = '<c:out value= "${deductee.tds_type.tdsType_id}"/>';
			 
			 if(typeId!="")
				{
				 document.getElementById("tds_type_id").value=typeId;
				}
			
		});
</script>
<%@include file="/WEB-INF/includes/footer.jsp" %>